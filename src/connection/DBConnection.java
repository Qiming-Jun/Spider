package connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;

public class DBConnection  
{  
    private String dbDriver="com.mysql.jdbc.Driver";   
    private String dbUrl="jdbc:mysql://localhost:3306/TeacherManage?characterEncoding=utf8";//根据实际情况变化  
    private String dbUser="root";  
    private String dbPass="";
    
//    private String dbUrl="jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_wqmbookdb?characterEncoding=utf8";//根据实际情况变化  
//    private String dbUser="yxjkzl0jjk";  
//    private String dbPass="m14533xk0mm0yliwy13ihw1kw4lj5iiiw05iywwz";
    
    public Connection getConn()  
    {  
        Connection conn=null;  
        try  
        {  
            Class.forName(dbDriver);  
        }  
        catch (ClassNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        try  
        {  
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//注意是三个参数  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn;  
    }

public int insert(String sql)  
{  
    int i=0;  
    /*String sql="insert into author(authorid,name,age,country) values(?,?,?,?)"; 
    String sql0="insert into book(isbn,title,authorid,publisher,publishdate,price) values(?,?,?,?,?,?)";
    */
    Connection cnn=getConn();  

    try{  
        PreparedStatement preStmt =cnn.prepareStatement(sql);  
        i=preStmt.executeUpdate();  
    }  
    catch (SQLException e)  
    {  
        e.printStackTrace();  
    }
    
    try {
		cnn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return i;//返回影响的行数，1为执行成功  
}  
public int update(String sql)
{  
    int i=0;  
    /*String sql="update author set  authorid=?,name=?,age=?,country=? where authorid=?";//注意要有where条件  
    */
    Connection cnn=getConn();  


    try{  
        PreparedStatement preStmt =cnn.prepareStatement(sql);  
        i=preStmt.executeUpdate();  
    }  
    catch (SQLException e)  
    {  
        e.printStackTrace();  
    }  
    return i;//返回影响的行数，1为执行成功  
}  
public List<String> select(String sql)
{  
    int i;
    Connection cnn = getConn();//此处为通过自己写的方法getConn()获得连接
    List<String> list = new LinkedList<String>();
    try  
    {  
        Statement stmt = cnn.createStatement();  
        ResultSet rs = stmt.executeQuery(sql);  
        while(rs.next())  
        {  
            for(i = 1;i<=rs.getMetaData().getColumnCount();i++){
            list.add(rs.getString(i));}
        }  
        //可以将查找到的值写入类，然后返回相应的对象  
    }  
    catch (SQLException e)  
    {  
        e.printStackTrace();  
    }  
    return list;  
}  
public int delete(String sql)  
{  
    /*String sql = "delete from author where authorid=?"; */ 
    int i=0;  
    Connection conn = getConn();//此处为通过自己写的方法getConn()获得连接  
    try  
    {  
        Statement stmt = conn.createStatement();  
        i = stmt.executeUpdate(sql);  
    }  
    catch (SQLException e)  
    {  
        e.printStackTrace();  
    }  
    return i;//如果返回的是1，则执行成功;  
}  
}