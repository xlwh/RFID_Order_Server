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
		 *��Ӵ������
		 *��������sql�������String�ͱ���sql��
		 *������̣��������ݿ�
		 *          ���ݱ���sql�������ݿ����
		 *          �����ݿ�����Ľ������String�ͱ���result��
		 *          ��result�������Ͻ�����־��strEnd(String)
		***********************************************************************/

      
       
       
       String sqlArray[] = sql.split("\\/");//�ԡ�.��Ϊ�ָ����sql���ָ�
       /*
        * sqlArray[0]��ʾsql�������ͱ�־λ
        * ��sqlArray[0]��ֵΪ"0" �����ʾ��insert������update����delete��䣬
        *                         ���ʱ�������ݵĸ�ʽΪ����ͷ��ʾ��"0"+"."+��������
        *                         sqlArray[1]��ʾ���յ�sql��Ϣ����
        *                         �����updateDB��������
        * ��sqlArray[0]��ֵΪ"1" �� ����select��䣬 
        *                         ���ʱ�������ݵĸ�ʽΪ����ͷ��ʾ��"1"+"."+��������+"."+ѡ������ֶθ���countSelectFields
                                  sqlArray[1]��ʾ���յ�sql��Ϣ����
                                  sqlArray[2]��ʾѡ������ֶεĸ��� 
        *                         �������searchDB��������
        * */
       //insert/update/delete�ʹ������
       if("0".equals(sqlArray[0])){
       System.out.println("================�����Ϊdelete/update/insert��=================");
       System.out.println("���������յ�sql���Ϊ��"+sqlArray[1]);
       int i = 0;
       try{ db = new Database();                          //�������ݿ������
    	    i = db.updateDB(sqlArray[1]); 
    	System.out.println("ִ�н��Ϊ:"+ i);
    	 }catch(Exception e){e.printStackTrace();}
       result = String.valueOf(i);//����ֵΪһ����
       }
       //select�ʹ������
       else if ("1".equals(sqlArray[0])){
    	   System.out.println("================��sql���Ϊselect��=================");
    	   System.out.println("���������յ�sql���Ϊ��"+sqlArray[1]);
    	   result ="1";
    	   int count =Integer.parseInt(sqlArray[2]);
    	   System.out.println("select�ĸ���Ϊ��"+count);
    	  
    	   try{
    		   db = new Database();                          //�������ݿ������
    		   db.searchDB(sqlArray[1]);       //ִ�в�ѯ���
    		   while(db.rs.next()){
    		      for (int i = 0 ;i < count ;i ++){
    			  result= result+"/"+db.rs.getString(i+1);
    			  }
    	         }
    		   }catch(Exception e){e.printStackTrace();}
       }
       
        String strEnd = "$";                 //��$����Ϊ������ʶ��
        result = result + strEnd;
        
        db.close();
       
        System.out.println("�������˷�����Ϣresult��ֵ��"+result);  
        
        /************************************************************
        * ����������
        * **********************************************************/
     
      return result;
    
    }
}