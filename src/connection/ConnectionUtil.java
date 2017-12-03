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
            	return "（姓名未找到）";
            }
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
            if(imgString=="") {
            	return "（Tel未找到）";
            }
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
            if(imgString=="") {
            	return "（Email未找到）";
            }
            return imgString;
        }
        return "（Email未设置）";
    }
    
    public static String getCollege(String targetStr) {
        Pattern NamePattern=
        		Pattern.compile("<tr><td width=\"70px\" "
        				+ "class=\"show-title1\">目前就职</td><td width=\"300px\" class=\"show-text1\">([^<]*)<");
        Matcher NameMatcher=NamePattern.matcher(targetStr);
        while (NameMatcher.find()){
            String imgString=NameMatcher.group(1);
            if(imgString=="") {
            	return "（学院信息未设置）";
            }
            return imgString;
        }
        return "（学院信息未设置）";
    }
    
    public static String getResume(String targetStr) {
    	String resultStr = "";
    	String tmpStr = "";
    	
    	//匹配并获取个人信息的全部html
        Pattern pattern1 = Pattern.compile("</span>(基本信息|简历|Bio|Dr. Joyce Webb基本信息)</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
 //       	System.out.println(tmpStr);
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
        resultStr = resultStr.replace("\"", "'");
        resumeinf = resultStr;
        if(resumeinf=="") {
        	return "个人简介未找到";
        }
        
        if(resultStr.length()>2900) {
        	return resultStr.substring(0, 2900);
        }
        return resultStr;
    }
    
    public static String getAchievement(String targetStr) {
    	String resultStr = "";
    	String tmpStr = "";
    	
    	//匹配并获取荣誉称号的全部html
        Pattern pattern1 = Pattern.compile("</span>荣誉称号</h3>.*?</div>");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
        	tmpStr = matcher1.group();
        }else {
        	return "未找到个人荣誉信息";
        }
        
        //匹配并获取全部段落内信息
        Pattern pattern2 = Pattern.compile("<(li|p).*?>(.*?)</(li|p)>");
        Matcher matcher2 = pattern2.matcher(tmpStr);
        while(matcher2.find()) {
        	resultStr = resultStr + matcher2.group(2);
        }
        
        //删除额外信息 比如<strong> <font> &nbsp 
        Pattern pattern3 = Pattern.compile("(<.*?>)|(&nbsp;)|&ldquo|&rdquo|(\\t)");
        Matcher matcher3 = pattern3.matcher(resultStr);
        StringBuffer sb = new StringBuffer();
        while(matcher3.find()) {
        	matcher3.appendReplacement(sb, "");
        }
        
        resultStr = new String(sb);  
        resultStr = resultStr.replace("\"", "'");
        if(resultStr=="") {
        	return "个人荣誉信息未找到";
        }
        
        if(resultStr.length()>2900) {
        	return resultStr.substring(0, 2900);
        }
        return resultStr;
    }
    
    public static String getSex(String targetStr) {
        Pattern pattern1 = Pattern.compile("，男，");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
            return "男";
        }
        
        Pattern pattern2 = Pattern.compile("，女，");
        Matcher matcher2 = pattern2.matcher(targetStr);
        if (matcher2.find()){
            return "女";
        }
        
        return "（性别信息未找到）";
    }
    
    public static String getBirthday(String targetStr) {
        Pattern pattern1 = Pattern.compile("(\\d{4}).*?年生");
        Matcher matcher1 = pattern1.matcher(targetStr);
        if (matcher1.find()){
            return matcher1.group(1);
        }
        
        return "（生日信息未找到）";
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
    		
            Pattern pattern1 = Pattern.compile("<span>条结果</span></div>.*?<script type=\"text/javascript\">");
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
            	teacherNum += 1; 		//教师个数计数
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
    	String result  = test.Connect("http://homepage.hit.edu.cn/peterrolfe");  在这里输入网址   
    	
    	System.out.println("姓名："+getName(result));
    	System.out.println("电话："+getTel(result));
    	System.out.println("邮箱："+getEmail(result));
    	System.out.println("学院："+getCollege(result));
    	System.out.println("个人简介："+getResume(result));
    	System.out.println("性别："+getSex(resumeinf));
    	System.out.println("出生年月："+getBirthday(resumeinf));
    	System.out.println("荣誉称号："+getAchievement(result));*/
    	
    	urlList = getUrl(3000);
    	getAllinf(2635);
    	infToSql(2635);
    	
    }
}