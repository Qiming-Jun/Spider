package connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionUtil {
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
        Pattern pattern1 = Pattern.compile("</span>������Ϣ</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
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
        
        return resultStr;
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
    
    public static void main(String arg[]) {
    	ConnectionUtil test = new ConnectionUtil();
    	String result  = test.Connect("http://homepage.hit.edu.cn/renxl");
//    	String result  = test.Connect("http://map.baidu.com/detail?qt=ninf&from=housezt&detail=house&uid=5ef5edbdc64c1bb49e9d6899");
    	
//    	System.out.println(result);
//    	System.out.println("�������");
    	
    	System.out.println("������"+getName(result));
    	System.out.println("�绰��"+getTel(result));
    	System.out.println("���䣺"+getEmail(result));
    	System.out.println("ѧԺ��"+getCollege(result));
   // 	System.out.println("���ԣ�"+getTest(result));
    	System.out.println("���˼�飺"+getResume(result));
    	
    }
}