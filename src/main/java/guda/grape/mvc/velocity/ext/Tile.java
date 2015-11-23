package guda.grape.mvc.velocity.ext;

import org.apache.velocity.Template;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.*;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.InputBase;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by foodoon on 2014/6/28.
 */
public class Tile extends InputBase {
    @Override
    public String getName() {
        return "tile";
    }

    @Override
    public int getType() {
        return LINE;
    }

    private Map<String, Object> params = new HashMap<String, Object>();


    public Tile add(String key, Object val) {
        params.put(key, val);
        return this;
    }

    private int maxDepth;


    public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
            throws TemplateInitException {
        super.init(rs, context, node);

        this.maxDepth = rsvc.getInt(RuntimeConstants.PARSE_DIRECTIVE_MAXDEPTH, 10);
    }


    public boolean render(InternalContextAdapter context,
                          Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException,
            MethodInvocationException {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for(Map.Entry<String,Object> entry:entries){
            context.put(entry.getKey(),entry.getValue());
        }
        if (!context.getAllowRendering()) {
            return true;
        }

        if (node.jjtGetChild(0) == null) {
            rsvc.getLog().error("#parse() null argument");
            return false;
        }

        Object value = node.jjtGetChild(0).value(context);

        if (value == null) {
            rsvc.getLog().error("#parse() null argument");
            return false;
        }
        String sourcearg = value.toString();
        String arg = EventHandlerUtil.includeEvent(rsvc, context, sourcearg, context.getCurrentTemplateName(), getName());
        boolean blockinput = false;
        if (arg == null)
            blockinput = true;
        if (maxDepth > 0) {
            Object[] templateStack = context.getTemplateNameStack();
            if (templateStack.length >= maxDepth) {
                StringBuffer path = new StringBuffer();
                for (int i = 0; i < templateStack.length; ++i) {
                    path.append(" > " + templateStack[i]);
                }
                rsvc.getLog().error("Max recursion depth reached (" +
                        templateStack.length + ')' + " File stack:" +
                        path);
                return false;
            }
        }
        Template t = null;
        try {
            if (!blockinput)
                t = rsvc.getTemplate(arg, getInputEncoding(context));
        } catch (ResourceNotFoundException rnfe) {
            rsvc.getLog().error("#parse(): cannot find template '" + arg +
                    "', called at " + Log.formatFileString(this));
            throw rnfe;
        } catch (ParseErrorException pee) {
            rsvc.getLog().error("#parse(): syntax error in #parse()-ed template '"
                    + arg + "', called at " + Log.formatFileString(this));
            throw pee;
        } catch (RuntimeException e) {
            rsvc.getLog().error("Exception rendering #parse(" + arg + ") at " +
                    Log.formatFileString(this));
            throw e;
        } catch (Exception e) {
            String msg = "Exception rendering #parse(" + arg + ") at " +
                    Log.formatFileString(this);
            rsvc.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        }
        List macroLibraries = context.getMacroLibraries();
        if (macroLibraries == null) {
            macroLibraries = new ArrayList();
        }
        context.setMacroLibraries(macroLibraries);
        macroLibraries.add(arg);

        try {
            if (!blockinput) {
                context.pushCurrentTemplateName(arg);
                ((SimpleNode) t.getData()).render(context, writer);
            }
        } catch (RuntimeException e) {
            rsvc.getLog().error("Exception rendering #parse(" + arg + ") at " +
                    Log.formatFileString(this));
            throw e;
        } catch (Exception e) {
            String msg = "Exception rendering #parse(" + arg + ") at " +
                    Log.formatFileString(this);
            rsvc.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        } finally {
            if (!blockinput)
                context.popCurrentTemplateName();
        }
        return true;
    }


}
