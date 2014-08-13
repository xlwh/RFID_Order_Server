package socketserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.DatabaseDisposing;

import commobject.SendObject;
import common.Database;


public class lowusersever {

	private int port = 1848;
	private ServerSocket serverSocket;
	private ThreadPool threadPool; // 线程池
	private final int POOL_SIZE = 10; // 单个CPU时线程池中工作线程的数目

	public lowusersever()  {
	try {
		serverSocket = new ServerSocket(port);
		// 创建线程池
		// Runtime的availableProcessors()方法返回当前系统的CPU的数目
		// 系统的CPU越多，线程池中工作线程的数目也越多
		threadPool = new ThreadPool(Runtime.getRuntime().availableProcessors()* POOL_SIZE);
        System.out.println("服务器监听.");
	   }catch(Exception e){
		   e.printStackTrace();
        System.out.println("端口已被占用");
	   }
	while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
             System.out.println("a client connected!" );
				handler h = new handler(socket);
				threadPool.execute(h);    // 把与客户通信的任务交给线程池
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args){
		new lowusersever();
	}
  }

class handler implements Runnable {
	
	public Socket socket;
	static String res;

	public handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		Database db = new Database();
		try {
            System.out.println("New connection accepted "+ socket.getInetAddress() + ":" + socket.getPort());

			SendObject s = new SendObject();
			byte[] buf = s.PacRecv(socket); // 服务器接收客户端发来的数据包，放在buf中
			
           String sql = new String(buf);//服务器接收到的数据，此时已经转化为String型
           System.out.println("服务器端接收的信息sql:"+sql);

           
           
           
           String result =new DatabaseDisposing(sql).getResult() ;
//           /***********************************************************************
//			 *添加处理程序
//			 *传过来的sql语句存放在String型变量sql中
//			 *处理过程：连接数据库
//			 *          根据变量sql进行数据库操作
//			 *          将数据库操作的结果放在String型变量result中
//			 *          给result变量加上结束标志符strEnd(String)
//			***********************************************************************/
//         try {
//	       db = new Database();                          //创建数据库类对象
//           } catch(Exception e){
//	        e.printStackTrace();
//           }
//           
//           String sqlArray[] = sql.split("\\/");//以“.”为分割符将sql语句分割
//           /*
//            * sqlArray[0]表示sql语句的类型标志位
//            * 若sqlArray[0]的值为"0" ，则表示是insert语句或者update语句或delete语句，
//            *                         则此时接收数据的格式为：开头标示符"0"+"."+传输内容
//            *                         sqlArray[1]表示接收的sql消息内容
//            *                         需调用updateDB（）函数
//            * 若sqlArray[0]的值为"1" ， 则是select语句， 
//            *                         则此时接收数据的格式为：开头标示符"1"+"."+传输内容+"."+选择语句字段个数countSelectFields
//                                      sqlArray[1]表示接收的sql消息内容
//                                      sqlArray[2]表示选择语句字段的个数 
//            *                         则需调用searchDB（）函数
//            * */
//           //insert/update/delete型处理程序
//           if("0".equals(sqlArray[0])){
//           System.out.println("================改语句为delete/update/insert型=================");
//           System.out.println("服务器接收的sql语句为："+sqlArray[1]);
//        	int i = db.updateDB(sqlArray[1]); 
//           result = String.valueOf(i);//返回值为一个数
//           }
//           //select型处理程序
//           else if ("1".equals(sqlArray[0])){
//        	   System.out.println("================该sql语句为select型=================");
//        	   System.out.println("服务器接收的sql语句为："+sqlArray[1]);
//        	   result ="1";
//        	   int count =Integer.parseInt(sqlArray[2]);
//        	   System.out.println("select的个数为："+count);
//        	   db.searchDB(sqlArray[1]);       //执行查询语句
//        	   while(db.rs.next()){
//        		  for (int i = 0 ;i < count ;i ++){
//        			  result= result+"/"+db.rs.getString(i+1);
//        		  }
//        	   }
//           }
//           
//            String strEnd = "$";                 //“$”作为结束标识符
//            result = result + strEnd;
//            System.out.println("服务器端发送信息result的值："+result);  
//            
//            /************************************************************
//            * 处理程序结束
//            * **********************************************************/
            
            
           byte[] buf_send = result.getBytes();//将结果由String型转化成数据包
			s.PacDeliver(socket, buf_send);  //服务器端将出来好的结果发送客户
			
			
			
			db.close();
		 } catch (Exception e) {
			e.printStackTrace();
		 } finally {
			try {
			if (socket != null)
					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}