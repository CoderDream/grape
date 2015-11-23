package guda.grape.autogen;



import guda.grape.autogen.helper.VelocityHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/28.
 */
public class GenVm {

    public String listVM = "list.vm";

    public String createVM = "create.vm";

    public String editVM = "edit.vm";

    public String detailVM = "detail.vm";

    private GenContext genContext;

    public GenVm(GenContext genContext){
        this.genContext = genContext;
    }

    public void gen() throws Exception {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("doName",genContext.getDoName());
        params.put("doNameLower",genContext.getDoNameLower());
        params.put("doFieldList",genContext.getDoFieldList());



        createFile(params,listVM);
        createFile(params,createVM);
        createFile(params,detailVM);
        createFile(params,editVM);



    }

    public void createFile(Map<String,Object> params,String vm) throws Exception {
        String dir =  genContext.getVmPath();
        File dirFile = new File(dir);
        dirFile.mkdirs();
        String listFile = genContext.getVmPath() + File.separator + vm;
        String render =replace( VelocityHelper.render(vm, params));
        File file = new File(listFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(render.getBytes("UTF-8"));
        fileOutputStream.close();
    }

    public String replace(String str){
        if(str == null){
            return null;
        }
        str  = str.replaceAll("_!","\\$!");
        str  = str.replaceAll("_springBind","#springBind");
        str  = str.replaceAll("_foreach","#foreach");
        str  = str.replaceAll("_end","#end");
        return str;
    }

}
