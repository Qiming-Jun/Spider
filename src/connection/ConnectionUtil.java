package connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionUtil {
	private static String resumeinf;
	private static String[] urlList;	
	private static int teacherNum;
	private static TeacherInf[] teachers;
	
    public static String Connect(String address){
        HttpURLConnection conn = null;
        URL url = null;
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoInput(true);
            conn.connect();
            in = conn.getInputStream();
        //    in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            stringBuffer = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            conn.disconnect();
            try {
                in.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }
    
    public static String getName(String targetStr) {
        Pattern NamePattern=Pattern.compile("<span class=\"name tit36\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            if(imgString=="") {
            	return "������δ�ҵ���";
            }
            return imgString;
        }
        return "������δ���ã�";
    }
    
    public static String getTel(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<span class=\"col-lg-3 col-sm-3\" style=\"padding-right:10px;\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            if(imgString=="") {
            	return "��Telδ�ҵ���";
            }
            return imgString;
        }
        return "����ϵ��ʽδ�ҵ���";
    }
    
    public static String getEmail(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("title=\"��������ʼ�\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            if(imgString=="") {
            	return "��Emailδ�ҵ���";
            }
            return imgString;
        }
        return "��Emailδ���ã�";
    }
    
    public static String getCollege(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<tr><td width=\"70px\" "
        				+ "class=\"show-title1\">Ŀǰ��ְ</td><td width=\"300px\" class=\"show-text1\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            if(imgString=="") {
            	return "��ѧԺ��Ϣδ���ã�";
            }
            return imgString;
        }
        return "��ѧԺ��Ϣδ���ã�";
    }
    
    public static String getResume(String targetStr) {
    	String resultStr = "";
    	String tmpStr = "";
    	
    	//ƥ�䲢��ȡ������Ϣ��ȫ��html
        Pattern pattern1 = Pattern.compile("</span>(������Ϣ|����|Bio|Dr. Joyce Webb������Ϣ)</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
 //       	System.out.println(tmpStr);
        }else {
        	return "δ�ҵ����˼����Ϣ";
        }
        
        //ƥ�䲢��ȡȫ����������Ϣ
        Pattern pattern2 = Pattern.compile("<p.*?>(.*?)</p>");
        Matcher matcher2 = pattern2.matcher(tmpStr);
        while(matcher2.find()) {
        	resultStr = resultStr + matcher2.group(1);
        }
        
        //ɾ��������Ϣ ����<strong> <font> &nbsp 
        Pattern pattern3 = Pattern.compile("(<.*?>)|(&nbsp;)|(\\t)");
        Matcher matcher3 = pattern3.matcher(resultStr);
        StringBuffer sb = new StringBuffer();
        while(matcher3.find()) {
        	matcher3.appendReplacement(sb, "");
        }
        
        resultStr = new String(sb);
        resultStr = resultStr.replace("\"", "'");
        resumeinf = resultStr;
        if(resumeinf=="") {
        	return "���˼��δ�ҵ�";
        }
        
        if(resultStr.length()>2900) {
        	return resultStr.substring(0, 2900);
        }
        return resultStr;
    }
    
    public static String getAchievement(String targetStr) {
    	String resultStr = "";
    	String tmpStr = "";
    	
    	//ƥ�䲢��ȡ�����ƺŵ�ȫ��html
        Pattern pattern1 = Pattern.compile("</span>�����ƺ�</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
        }else {
        	return "δ�ҵ�����������Ϣ";
        }
        
        //ƥ�䲢��ȡȫ����������Ϣ
        Pattern pattern2 = Pattern.compile("<(li|p).*?>(.*?)</(li|p)>");
        Matcher matcher2 = pattern2.matcher(tmpStr);
        while(matcher2.find()) {
        	resultStr = resultStr + matcher2.group(2);
        }
        
        //ɾ��������Ϣ ����<strong> <font> &nbsp 
        Pattern pattern3 = Pattern.compile("(<.*?>)|(&nbsp;)|&ldquo|&rdquo|(\\t)");
        Matcher matcher3 = pattern3.matcher(resultStr);
        StringBuffer sb = new StringBuffer();
        while(matcher3.find()) {
        	matcher3.appendReplacement(sb, "");
        }
        
        resultStr = new String(sb);  
        resultStr = resultStr.replace("\"", "'");
        if(resultStr=="") {
        	return "����������Ϣδ�ҵ�";
        }
        
        if(resultStr.length()>2900) {
        	return resultStr.substring(0, 2900);
        }
        return resultStr;
    }
    
    public static String getSex(String targetStr) {
        Pattern pattern1 = Pattern.compile("���У�");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
            return "��";
        }
        
        Pattern pattern2 = Pattern.compile("��Ů��");
        Matcher matcher2 = pattern2.matcher(targetStr);
        if (matcher2.find()){
            return "Ů";
        }
        
        return "���Ա���Ϣδ�ҵ���";
    }
    
    public static String getBirthday(String targetStr) {
        Pattern pattern1 = Pattern.compile("(\\d{4}).*?����");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
            return matcher1.group(1);
        }
        
        return "��������Ϣδ�ҵ���";
    }
    
    public static String getTest(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("fcg\">(\\w*)(.+)(\\s*)");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        String imgString = "";//=NameMatcher.group(0);
        while (NameMatcher.find()){
            
        //    return imgString;
            imgString = imgString + NameMatcher.group(0);
            
            System.out.println(NameMatcher.group(1)+"aaa");
            System.out.println(NameMatcher.group(2));
            System.out.println(NameMatcher.group(3));
        //    System.out.println(NameMatcher.group(4));
            
        }
        return imgString;
    }
/*-------------------------------------------------------------------------------*/
    
    public static String[] getUrl(int n) {
    	String url = "http://homepage.hit.edu.cn/neiye.jsp?mcpy=";
    	char indexchar;
    	int index = 0;
    	String[] urlList = new String[n];
     	ConnectionUtil test = new ConnectionUtil();
    	teacherNum = 0;
     	
    	for(int i = 0; i < 26; i++) {
    		if (i == 4 || i == 20 || i==21) continue;
    		String htmltxt  = test.Connect(url + (char)(i+'a'));
    		String str1 = "";
    		
            Pattern pattern1 = Pattern.compile("<span>�����</span></div>.*?<script type=\"text/javascript\">");
            Matcher matcher1 = pattern1.matcher(htmltxt);
            if(matcher1.find()) {
            	str1 = matcher1.group();
            }else {
            	System.out.println("Error!");
            	return urlList;
            }
            
            Pattern pattern2 = Pattern.compile("<a href=\"(.*?)\" target=\"_self\">");
            Matcher matcher2 = pattern2.matcher(str1);
            while(matcher2.find()) {
            	urlList[index++] = matcher2.group(1);
            	System.out.println("No." + (index-1) + ":" + urlList[index-1]);
            	teacherNum += 1; 		//��ʦ��������
            }
            
            System.out.println((char)(i+'a') + " finished," + "find "+index+" urls");
//            System.out.println("the last one is:" + urlList[index-1]);
    	}
    	
    	return urlList;
    }
    
    public static void getAllinf(int n) {
    	teachers = new TeacherInf[3000];
    	ConnectionUtil test = new ConnectionUtil();
    	
    	long time0 = System.currentTimeMillis();
    	for(int i = 0; i<n; i++) {
    		teachers[i] = new TeacherInf();
    		String result  = test.Connect(urlList[i]); 
    		
    		teachers[i].ID = String.format("%04d", i+1);
    		teachers[i].name = getName(result);
    		teachers[i].Email = getEmail(result);
    		teachers[i].Tel = getTel(result);
    		teachers[i].resume = getResume(result);
    		teachers[i].sex = getSex(resumeinf);
    		teachers[i].college = getCollege(result);
    		teachers[i].birthday = getBirthday(resumeinf);    		
    		teachers[i].achievement = getAchievement(result);
    		teachers[i].password = "123456";
    		
    		int time = (int)(System.currentTimeMillis() - time0)/1000;
    		if((i+1)%100==0) {
    			System.out.println("informatio from html to memory:finish " + (i+1)
    					+ ", time cost "+ time + "s");
    		}
    		
    		/*try { 
    			Thread.sleep(500); 
    			} catch (InterruptedException e) { 
    			e.printStackTrace(); 
    			}*/
    	///	System.out.println(teachers[i].ID);
    	}
    	
    	int i = 0;
    	i = i+1;
    	System.out.println("step 1 finished **************************************");
    }
    
    public static void infToSql(int n) {
    	DBConnection connect = new DBConnection();
    	long time0 = System.currentTimeMillis();    	
    	String sql0 = "insert into teacher2 (`TecID`, `Name`, `Sex`, `Birthday`, `College`, "
    			+ "`PhoneNum`, `Email`, `Resume`, `Achievement`, `Date1`, `Date2`, `Password`)"
    			+ " VALUES (";
    	String sql;
    	for(int i = 0; i<n; i++) {
    		sql = sql0 + "'" + teachers[i].ID + "',"
    				+ "'" + teachers[i].name + "',"
    				+ "'" + teachers[i].sex + "',"
    				+ "'" + teachers[i].birthday + "',"
    				+ "'" + teachers[i].college + "',"
    				+ "'" + teachers[i].Tel + "',"
    				+ "'" + teachers[i].Email + "',"
    				+ "\"" + teachers[i].resume + "\","
    				+ "\"" + teachers[i].achievement + "\","
    				+ "'" + teachers[i].data1 + "',"
    				+ "'" + teachers[i].data2 + "',"
    				+ "'" + teachers[i].password + "'" + ")";
    		
    		/*sql = sql0 + "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "',"
    				+ "'" + i + "'" + ")";*/
    		
    		int flag = connect.insert(sql);
    		if(flag != 1) {
    			System.out.println("error sql: " +flag +" "+ sql);
    			return;
    		}
    		
    		long time = (System.currentTimeMillis() - time0)/1000;
    		if((i+1)%100==0) {
    			System.out.println("informatio from memory to database:finish " + (i+1)
    					+ ", time cost "+ time + "s");
    		}
    	}
    	System.out.println("step 2 finished **************************************");
    }
    
/*-------------------------------------------------------------------------------*/    
    public static void main(String arg[]) {
    	/*ConnectionUtil test = new ConnectionUtil();
    	String result  = test.Connect("http://homepage.hit.edu.cn/peterrolfe");  ������������ַ   
    	
    	System.out.println("������"+getName(result));
    	System.out.println("�绰��"+getTel(result));
    	System.out.println("���䣺"+getEmail(result));
    	System.out.println("ѧԺ��"+getCollege(result));
    	System.out.println("���˼�飺"+getResume(result));
    	System.out.println("�Ա�"+getSex(resumeinf));
    	System.out.println("�������£�"+getBirthday(resumeinf));
    	System.out.println("�����ƺţ�"+getAchievement(result));*/
    	
    	urlList = getUrl(3000);
    	getAllinf(2635);
    	infToSql(2635);
    	
    }
}