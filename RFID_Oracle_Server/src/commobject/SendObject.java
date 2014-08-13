package commobject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendObject { 
	/* PrintWriter作用是“写”，主要往三个地方写：1.屏幕，即打印。2.socket输出流，就是把东西发给人家。3.文件
	 * 往什么地方写与PrintWriter实例化时的入口参数有关，如return new PrintWriter里面是socketout，是个socket输出流，则println()
	 * 方法里面的东西就写入输出流了。
	 * 因为对方采用readLine()进行读取，读一行数据，遇到回车和换行的标示符会停止，所以发端要使用println方法，发一行数据（带标示符）
	 * 否则收端readLine会一直阻塞
	 * 
	 * 以下几个都是流，从PacDeliver起才是真正需要调用的方法
	 */
	
	
	
	/* PrintWriter负责输出字符串
	 * PrintWriter需要一个OutputStream作为入口参数，getWriter方法就设定socket为入口参数，然后从socket对象获得输出流，再
	 * 传递给PrintWriter方法并返回，这样PrintWriter就可以对socket输出流进行操作，不然每次实例化很麻烦，现在只要用一次getWriter就行
	 */
	private PrintWriter getWriter(Socket socket)throws IOException{
	    OutputStream socketOut = socket.getOutputStream();
	    return new PrintWriter(socketOut,true);
	  }
	
	 
	 
	 
	 /*
	  * 数据输出流，可以输出Byte数组，也可以输出字符串，分别调用write(byte[] b, int off, int len)和writeUTF(String str) 
	  */
	 private DataOutputStream getDataOutput (Socket socket)throws IOException{
		 	 OutputStream socketout = socket.getOutputStream();
		 return new DataOutputStream(socketout);
	 }
	 
	 /*
	  * 
	  * 
	  * 
	  */
	 
	 private FileWriter  setFile (File file) throws IOException{
		return new FileWriter (file);
	 }
	 
	 /*
	  * BufferedReader的功能是对socket输入流进来的字符串进行读取
	  */
	 
	 private BufferedReader getReader(Socket socket)throws IOException{
		    InputStream socketIn = socket.getInputStream();
		    return new BufferedReader(new InputStreamReader(socketIn));
		  } 

	 /*
	  * 读取输入流进来的数据并根据不同方法转化成不同类型的数据
	  * 
	  */
	 
	 private DataInputStream getDataInput (Socket socket)throws IOException{
		  InputStream socketIn = socket.getInputStream();
		 
		  return new DataInputStream(socketIn);
		  
	 }
	 
	 private FileReader getFile(File file) throws FileNotFoundException {
		 return new FileReader(file);
	 }
	 
	 /*以下为发送接收时使用的方法，这些方法调用了上面的流
	  * 
	  * 
	  */
	 
	 /*接收byte[]，可用来接收数据包，使用DataIutputStream.入口参数为socket对象
	  * 该方法返回一个byte[]给协议解析模块
	  */
	 public  byte[] PacRecv (Socket socket)throws IOException{
		 byte[] buf=new byte[512];
	     
	      boolean flag=true;
	      DataInputStream dataIn;
	     try{
	      while(flag){
	      dataIn=getDataInput(socket);
	      dataIn.read(buf);
	      flag=false;
	      }
	     }catch(Exception e){
	    	 e.printStackTrace();   
	         
	     }
	     String receive =new String(buf);     
	     String [] receive_short = receive.split("\\$");  //注意一定要加上转义符“\\”
	     buf = receive_short[0].getBytes();
	      return buf;
	 }
	 
	 /*
	  * 发送byte[],也就是发送数据包，入口参数为socket对象和要发送的byte[]，无返回值
	  */
	 public void PacDeliver(Socket socket, byte buf[])throws IOException{

		 DataOutputStream dataOut=getDataOutput(socket);
		 dataOut.write(buf,0,buf.length);//将指定 byte 数组中从偏移量 off 开始的 len 个字节写入基础输出流
		 dataOut.flush();
		 
	 }
	 
	
	 
	 
	 
	 
	 //以下方法暂未使用
	 
	 /*发送字符串，可用来传递文本，入口参数为socket对象和一个字符串变量s，无返回值
	  * 
	  * 这里除了字符串变量，也可以用
	  */
	 public void TexDeliver(Socket socket)throws IOException{
		 PrintWriter pw = getWriter(socket);
		 BufferedReader localReader=new BufferedReader(new InputStreamReader(System.in));
		String msg=null;
		
		 while((msg=localReader.readLine())!=null){
			pw.println(msg);
			if(msg.equals("bye")){
				break;
			}
		}
		 
	 }
	 
	 
	 /*接收字符串并打印，可用来接收文本，
	  * 
	  * 
	  */
	 public void TexRecv(Socket socket) throws IOException{
		 BufferedReader br =getReader(socket);
	      String msg = null;
	      while ((msg = br.readLine()) != null) {
		        System.out.println(msg);
	      }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
