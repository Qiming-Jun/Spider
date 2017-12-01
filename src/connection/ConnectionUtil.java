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
        return "（姓名未设置）";
    }
    
    public static String getTel(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<span class=\"col-lg-3 col-sm-3\" style=\"padding-right:10px;\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            return imgString;
        }
        return "（联系方式未找到）";
    }
    
    public static String getEmail(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("title=\"点击发送邮件\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            return imgString;
        }
        return "（姓名未设置）";
    }
    
    public static String getCollege(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<tr><td width=\"70px\" "
        				+ "class=\"show-title1\">目前就职</td><td width=\"300px\" class=\"show-text1\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            return imgString;
        }
        return "（未找到学院信息）";
    }
    
    public static String getResume(String targetStr) {
    	String resultStr = "";
    	String tmpStr = "";
    	
    	//匹配并获取个人信息的全部html
        Pattern pattern1 = Pattern.compile("</span>基本信息</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
        }else {
        	return "未找到个人简介信息";
        }
        
        //匹配并获取全部段落内信息
        Pattern pattern2 = Pattern.compile("<p.*?>(.*?)</p>");
        Matcher matcher2 = pattern2.matcher(tmpStr);
        while(matcher2.find()) {
        	resultStr = resultStr + matcher2.group(1);
        }
        
        //删除额外信息 比如<strong> <font> &nbsp 
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
//    	System.out.println("输出结束");
    	
    	System.out.println("姓名："+getName(result));
    	System.out.println("电话："+getTel(result));
    	System.out.println("邮箱："+getEmail(result));
    	System.out.println("学院："+getCollege(result));
   // 	System.out.println("测试："+getTest(result));
    	System.out.println("个人简介："+getResume(result));
    	
    }
}