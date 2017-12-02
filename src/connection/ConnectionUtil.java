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
	
    public static String Connect(String address){
        HttpURLConnection conn = null;
        URL url = null;
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
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
            return imgString;
        }
        return "������δ���ã�";
    }
    
    public static String getCollege(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<tr><td width=\"70px\" "
        				+ "class=\"show-title1\">Ŀǰ��ְ</td><td width=\"300px\" class=\"show-text1\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            return imgString;
        }
        return "��δ�ҵ�ѧԺ��Ϣ��";
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
        resumeinf = resultStr;
        
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
        Pattern pattern1 = Pattern.compile("(\\d{4})����");
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
    	
    	for(int i = 0; i < 26; i++) {
    		if (i == 4 || i == 20 || i==21) continue;
    		String htmltxt  = test.Connect(url + (char)(i+'a'));
    		
            Pattern pattern1 = Pattern.compile("<span>�����</span></div>.*?<script type=\"text/javascript\">");
            Matcher matcher1 = pattern1.matcher(htmltxt);
            if(matcher1.find()) {
            	String str1 = matcher1.group();
            }
            
            Pattern pattern2 = Pattern.compile("<a href=\"(.*?)\" target=\"_self\">");
            Matcher matcher2 = pattern2.matcher(htmltxt);
            while(matcher2.find()) {
            	urlList[index++] = matcher2.group(1);
            }
            System.out.println((char)(i+'a') + " finished," + "find "+index+" urls");
            System.out.println("the last one is:" + urlList[index-1]);
    	}
    	
    	return urlList;
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
    	
    	getUrl(3000);
    	
    }
}