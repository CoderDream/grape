package guda.grape.autogen;



import guda.grape.autogen.helper.VelocityHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/28.
 */
public class GenAction {

    private String controllerVmName = "controller.vm";

    private String formVmName = "form.vm";

    private String editFormVmName = "edit-form.vm";

    private GenContext genContext;

    public GenAction(GenContext genContext){
        this.genContext = genContext;
    }

    public void gen() throws Exception {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("doName",genContext.getDoName());

        params.put("doNameLower",genContext.getDoNameLower());
        params.put("doFieldList",genContext.getDoFieldList());
        String render = VelocityHelper.render(controllerVmName, params);
        String controllerFile = genContext.getControllerFile();
        File file = new File(controllerFile);
        file.getParentFile().mkdirs();

        file = new File(controllerFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(render.getBytes("UTF-8"));
        fileOutputStream.close();
        createForm(params,formVmName,genContext.getFormFile());
        createForm(params,editFormVmName,genContext.getEditFormFile());
    }

    public void gen(String parentPackageName) throws Exception {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("parentPackageName",parentPackageName);
        params.put("doName",genContext.getDoName());

        params.put("doNameLower",genContext.getDoNameLower());
        params.put("doFieldList",genContext.getDoFieldList());
        String render = VelocityHelper.render(controllerVmName, params);
        String controllerFile = genContext.getControllerFile();
        File file = new File(controllerFile);
        file.getParentFile().mkdirs();

        file = new File(controllerFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(render.getBytes("UTF-8"));
        fileOutputStream.close();
        createForm(params,formVmName,genContext.getFormFile());
        createForm(params,editFormVmName,genContext.getEditFormFile());
    }

    public void createForm(Map<String,Object> params,String vm,String filePath) throws Exception {
        String render = VelocityHelper.render(vm, params);
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            filePath+=".c";
        }
        file = new File(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(render.getBytes("UTF-8"));
        fileOutputStream.close();
    }



}
