package guda.grape.util;

public class Convert {
	
	public static int toInt(Object obj){
		if(obj == null){
			return 0;
		}
		try{
		    return Integer.parseInt(obj.toString());
		}catch(Exception e){

		}
		return 0;
	}

    public static long toLong(Object obj){
        if(obj == null){
            return 0;
        }
        try{
            return Long.parseLong(obj.toString());
        }catch(Exception e){

        }
        return 0;
    }

}
