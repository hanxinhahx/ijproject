package cn.hanxinhahx.util;

import netscape.javascript.JSObject;

import java.sql.*;
import java.util.Random;
//更新
public class JDBCUtil {
    private static String url="jdbc:mysql://localhost:3306/test";
    private static String user="root";
    private static String password="!huc2000121";
    private static PreparedStatement preparedStatement=null;
    private static Connection connection=null;
    public static Connection getConnection(){
        //加载驱动，先是mysql再是jdbc。这里最好用trycatch，因为我们最后要关闭连接

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("驱动加载失败");
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("获取连接失败");
        }finally {
            //如果连接失败要关闭连接，防止影响别的用户连接，因为连接的最大数是固定的
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        return connection;
    }
    public  static void close(PreparedStatement preparedStatement,Connection connection){
        try {
            //判断下preparedStatement是否关闭，如果没有关闭再进行关闭
            if (!preparedStatement.isClosed()){
                preparedStatement.close();
            }
            //判断connection是否已经关闭，如果没有关闭，则进行关闭操作，这里 一定要先关闭preparedStatement，再关闭connection
            if(!connection.isClosed()){
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int insertMothend(){
        Connection connection = JDBCUtil.getConnection();
        String sql="INSERT INTO student (stu_id,stu_name,stu_password,stu_class_id) VALUE(null,?,?,?)";
        PreparedStatement preparedStatement= null;
        int update=0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            char[] chars=new char[10];
            Random random=new Random();
            for (int i=1;i<=10;i++) {

                for (int j=0;j<chars.length;j++){
                    chars[j]=(char)(random.nextInt(66)+58);
                }
                String name = new String(chars, 0, chars.length - 1);
                int classid = (int) ((Math.random() * 2) + 1);
                System.out.println(name);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, "123456");
                preparedStatement.setInt(3, classid);
                preparedStatement.executeUpdate();
                update++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.close(preparedStatement,connection);
        }

        return update;
    }
    public static void limitMethond(int offset,int number){
        Connection connection = JDBCUtil.getConnection();
        String sql="SELECT * FROM student LIMIT "+number*(offset-1)+","+number+";";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getInt("stu_id") + "\t" + resultSet.getString("stu_name"));
            }

        } catch (Exception e) {
            System.out.println("失败");
            e.printStackTrace();
        }
    }
    public static void main(String[] args)  {
         JDBCUtil.limitMethond(3,3);
    }
}
