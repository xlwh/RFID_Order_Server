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
	private ThreadPool threadPool; // �̳߳�
	private final int POOL_SIZE = 10; // ����CPUʱ�̳߳��й����̵߳���Ŀ

	public lowusersever()  {
	try {
		serverSocket = new ServerSocket(port);
		// �����̳߳�
		// Runtime��availableProcessors()�������ص�ǰϵͳ��CPU����Ŀ
		// ϵͳ��CPUԽ�࣬�̳߳��й����̵߳���ĿҲԽ��
		threadPool = new ThreadPool(Runtime.getRuntime().availableProcessors()* POOL_SIZE);
        System.out.println("����������.");
	   }catch(Exception e){
		   e.printStackTrace();
        System.out.println("�˿��ѱ�ռ��");
	   }
	while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
             System.out.println("a client connected!" );
				handler h = new handler(socket);
				threadPool.execute(h);    // ����ͻ�ͨ�ŵ����񽻸��̳߳�
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
			byte[] buf = s.PacRecv(socket); // ���������տͻ��˷��������ݰ�������buf��
			
           String sql = new String(buf);//���������յ������ݣ���ʱ�Ѿ�ת��ΪString��
           System.out.println("�������˽��յ���Ϣsql:"+sql);

           
           
           
           String result =new DatabaseDisposing(sql).getResult() ;
//           /***********************************************************************
//			 *��Ӵ������
//			 *��������sql�������String�ͱ���sql��
//			 *������̣��������ݿ�
//			 *          ���ݱ���sql�������ݿ����
//			 *          �����ݿ�����Ľ������String�ͱ���result��
//			 *          ��result�������Ͻ�����־��strEnd(String)
//			***********************************************************************/
//         try {
//	       db = new Database();                          //�������ݿ������
//           } catch(Exception e){
//	        e.printStackTrace();
//           }
//           
//           String sqlArray[] = sql.split("\\/");//�ԡ�.��Ϊ�ָ����sql���ָ�
//           /*
//            * sqlArray[0]��ʾsql�������ͱ�־λ
//            * ��sqlArray[0]��ֵΪ"0" �����ʾ��insert������update����delete��䣬
//            *                         ���ʱ�������ݵĸ�ʽΪ����ͷ��ʾ��"0"+"."+��������
//            *                         sqlArray[1]��ʾ���յ�sql��Ϣ����
//            *                         �����updateDB��������
//            * ��sqlArray[0]��ֵΪ"1" �� ����select��䣬 
//            *                         ���ʱ�������ݵĸ�ʽΪ����ͷ��ʾ��"1"+"."+��������+"."+ѡ������ֶθ���countSelectFields
//                                      sqlArray[1]��ʾ���յ�sql��Ϣ����
//                                      sqlArray[2]��ʾѡ������ֶεĸ��� 
//            *                         �������searchDB��������
//            * */
//           //insert/update/delete�ʹ������
//           if("0".equals(sqlArray[0])){
//           System.out.println("================�����Ϊdelete/update/insert��=================");
//           System.out.println("���������յ�sql���Ϊ��"+sqlArray[1]);
//        	int i = db.updateDB(sqlArray[1]); 
//           result = String.valueOf(i);//����ֵΪһ����
//           }
//           //select�ʹ������
//           else if ("1".equals(sqlArray[0])){
//        	   System.out.println("================��sql���Ϊselect��=================");
//        	   System.out.println("���������յ�sql���Ϊ��"+sqlArray[1]);
//        	   result ="1";
//        	   int count =Integer.parseInt(sqlArray[2]);
//        	   System.out.println("select�ĸ���Ϊ��"+count);
//        	   db.searchDB(sqlArray[1]);       //ִ�в�ѯ���
//        	   while(db.rs.next()){
//        		  for (int i = 0 ;i < count ;i ++){
//        			  result= result+"/"+db.rs.getString(i+1);
//        		  }
//        	   }
//           }
//           
//            String strEnd = "$";                 //��$����Ϊ������ʶ��
//            result = result + strEnd;
//            System.out.println("�������˷�����Ϣresult��ֵ��"+result);  
//            
//            /************************************************************
//            * ����������
//            * **********************************************************/
            
            
           byte[] buf_send = result.getBytes();//�������String��ת�������ݰ�
			s.PacDeliver(socket, buf_send);  //�������˽������õĽ�����Ϳͻ�
			
			
			
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