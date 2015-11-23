package guda.grape.autogen;


import guda.grape.autogen.helper.VelocityHelper;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/27.
 */
public class GenBiz {

    private String bizVmName = "biz.vm";

    private String bizImplVmName = "biz-impl.vm";

    private String formVmName = "form.vm";

    private GenContext genContext;

    public GenBiz(GenContext genContext) {
        this.genContext = genContext;
    }

    public void gen() throws Exception {
        String parentPackageName = genContext.getParentPackageName();
        genBizXml(parentPackageName);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentPackageName", parentPackageName);
        params.put("doName", genContext.getDoName());
        params.put("doClassName", genContext.getDoClassName());
        String daoClass = genContext.getDoClassName().substring(0, genContext.getDoClassName().length() - 2) + "DAO";
        daoClass = daoClass.replace("dataobject", "dao");
        params.put("daoClassName", daoClass);
        params.put("doNameLower", genContext.getDoNameLower());

        createVM(params, bizVmName, genContext.getBizFile());
        createVM(params, bizImplVmName, genContext.getBizImplFile());

        params.put("doFieldList", genContext.getDoFieldList());
        createVM(params, formVmName, genContext.getFormFile());
    }

    public void createVM(Map<String, Object> params, String vm, String filePath) {
        try {
            String render = VelocityHelper.render(vm, params);
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                filePath += ".c";
            }

            file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(render.getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void genBizXml(String parentPackageName) {
        String bizXML = genContext.getBizXmlFile();
        File file = new File(bizXML);
        if (!file.exists()) {
            Document document = SpringXml.createDocument();
            document.setXMLEncoding("UTF-8");
            Element springXmlHeader = SpringXml.createSpringXmlHeader(document);
            SpringXml.createBean(springXmlHeader, genContext.getDoNameLower() + "Biz", parentPackageName + ".impl." + genContext.getDoName() + "BizImpl");
            SpringXml.write(bizXML, document);

        }
        Document document = SpringXml.loadXML(bizXML);
        if (!SpringXml.hasBean(document, genContext.getDoNameLower() + "Biz")) {
            SpringXml.createBean(SpringXml.getSpringRoot(document), genContext.getDoNameLower() + "Biz", parentPackageName + ".impl." + genContext.getDoName() + "BizImpl");
            SpringXml.write(bizXML, document);
        }

    }

}
