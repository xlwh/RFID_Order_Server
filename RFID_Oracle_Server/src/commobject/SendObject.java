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
	/* PrintWriter�����ǡ�д������Ҫ�������ط�д��1.��Ļ������ӡ��2.socket����������ǰѶ��������˼ҡ�3.�ļ�
	 * ��ʲô�ط�д��PrintWriterʵ����ʱ����ڲ����йأ���return new PrintWriter������socketout���Ǹ�socket���������println()
	 * ��������Ķ�����д��������ˡ�
	 * ��Ϊ�Է�����readLine()���ж�ȡ����һ�����ݣ������س��ͻ��еı�ʾ����ֹͣ�����Է���Ҫʹ��println��������һ�����ݣ�����ʾ����
	 * �����ն�readLine��һֱ����
	 * 
	 * ���¼�������������PacDeliver�����������Ҫ���õķ���
	 */
	
	
	
	/* PrintWriter��������ַ���
	 * PrintWriter��Ҫһ��OutputStream��Ϊ��ڲ�����getWriter�������趨socketΪ��ڲ�����Ȼ���socket���������������
	 * ���ݸ�PrintWriter���������أ�����PrintWriter�Ϳ��Զ�socket��������в�������Ȼÿ��ʵ�������鷳������ֻҪ��һ��getWriter����
	 */
	private PrintWriter getWriter(Socket socket)throws IOException{
	    OutputStream socketOut = socket.getOutputStream();
	    return new PrintWriter(socketOut,true);
	  }
	
	 
	 
	 
	 /*
	  * ������������������Byte���飬Ҳ��������ַ������ֱ����write(byte[] b, int off, int len)��writeUTF(String str) 
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
	  * BufferedReader�Ĺ����Ƕ�socket�������������ַ������ж�ȡ
	  */
	 
	 private BufferedReader getReader(Socket socket)throws IOException{
		    InputStream socketIn = socket.getInputStream();
		    return new BufferedReader(new InputStreamReader(socketIn));
		  } 

	 /*
	  * ��ȡ���������������ݲ����ݲ�ͬ����ת���ɲ�ͬ���͵�����
	  * 
	  */
	 
	 private DataInputStream getDataInput (Socket socket)throws IOException{
		  InputStream socketIn = socket.getInputStream();
		 
		  return new DataInputStream(socketIn);
		  
	 }
	 
	 private FileReader getFile(File file) throws FileNotFoundException {
		 return new FileReader(file);
	 }
	 
	 /*����Ϊ���ͽ���ʱʹ�õķ�������Щ�����������������
	  * 
	  * 
	  */
	 
	 /*����byte[]���������������ݰ���ʹ��DataIutputStream.��ڲ���Ϊsocket����
	  * �÷�������һ��byte[]��Э�����ģ��
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
	     String [] receive_short = receive.split("\\$");  //ע��һ��Ҫ����ת�����\\��
	     buf = receive_short[0].getBytes();
	      return buf;
	 }
	 
	 /*
	  * ����byte[],Ҳ���Ƿ������ݰ�����ڲ���Ϊsocket�����Ҫ���͵�byte[]���޷���ֵ
	  */
	 public void PacDeliver(Socket socket, byte buf[])throws IOException{

		 DataOutputStream dataOut=getDataOutput(socket);
		 dataOut.write(buf,0,buf.length);//��ָ�� byte �����д�ƫ���� off ��ʼ�� len ���ֽ�д����������
		 dataOut.flush();
		 
	 }
	 
	
	 
	 
	 
	 
	 //���·�����δʹ��
	 
	 /*�����ַ����������������ı�����ڲ���Ϊsocket�����һ���ַ�������s���޷���ֵ
	  * 
	  * ��������ַ���������Ҳ������
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
	 
	 
	 /*�����ַ�������ӡ�������������ı���
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
