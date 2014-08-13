package server;
import common.Database;


public class DatabaseDisposing {
	public String result;
	Database db ;
	String sql;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
    public DatabaseDisposing(String sql){
    	this.sql = sql;
    	setResult(disposing());
    
    }
    
    public String disposing (){
    	String result = null;
    	
   	 /***********************************************************************
		 *添加处理程序
		 *传过来的sql语句存放在String型变量sql中
		 *处理过程：连接数据库
		 *          根据变量sql进行数据库操作
		 *          将数据库操作的结果放在String型变量result中
		 *          给result变量加上结束标志符strEnd(String)
		***********************************************************************/

      
       
       
       String sqlArray[] = sql.split("\\/");//以“.”为分割符将sql语句分割
       /*
        * sqlArray[0]表示sql语句的类型标志位
        * 若sqlArray[0]的值为"0" ，则表示是insert语句或者update语句或delete语句，
        *                         则此时接收数据的格式为：开头标示符"0"+"."+传输内容
        *                         sqlArray[1]表示接收的sql消息内容
        *                         需调用updateDB（）函数
        * 若sqlArray[0]的值为"1" ， 则是select语句， 
        *                         则此时接收数据的格式为：开头标示符"1"+"."+传输内容+"."+选择语句字段个数countSelectFields
                                  sqlArray[1]表示接收的sql消息内容
                                  sqlArray[2]表示选择语句字段的个数 
        *                         则需调用searchDB（）函数
        * */
       //insert/update/delete型处理程序
       if("0".equals(sqlArray[0])){
       System.out.println("================改语句为delete/update/insert型=================");
       System.out.println("服务器接收的sql语句为："+sqlArray[1]);
       int i = 0;
       try{ db = new Database();                          //创建数据库类对象
    	    i = db.updateDB(sqlArray[1]); 
    	System.out.println("执行结果为:"+ i);
    	 }catch(Exception e){e.printStackTrace();}
       result = String.valueOf(i);//返回值为一个数
       }
       //select型处理程序
       else if ("1".equals(sqlArray[0])){
    	   System.out.println("================该sql语句为select型=================");
    	   System.out.println("服务器接收的sql语句为："+sqlArray[1]);
    	   result ="1";
    	   int count =Integer.parseInt(sqlArray[2]);
    	   System.out.println("select的个数为："+count);
    	  
    	   try{
    		   db = new Database();                          //创建数据库类对象
    		   db.searchDB(sqlArray[1]);       //执行查询语句
    		   while(db.rs.next()){
    		      for (int i = 0 ;i < count ;i ++){
    			  result= result+"/"+db.rs.getString(i+1);
    			  }
    	         }
    		   }catch(Exception e){e.printStackTrace();}
       }
       
        String strEnd = "$";                 //“$”作为结束标识符
        result = result + strEnd;
        
        db.close();
       
        System.out.println("服务器端发送信息result的值："+result);  
        
        /************************************************************
        * 处理程序结束
        * **********************************************************/
     
      return result;
    
    }
}