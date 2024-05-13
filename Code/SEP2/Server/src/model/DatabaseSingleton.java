package model;



import java.io.Serializable;
import java.sql.*;


public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2";
    private static final String USER = "postgres";
    private static final String PSWD = "admin";

    private DatabaseSingleton(){
        try{
            this.connection = DriverManager.getConnection(URL,USER,PSWD);
        }catch (SQLException e ){
            System.out.println("Connection to DB failed");
            e.printStackTrace();
        }
    }


    public static DatabaseSingleton getInstance(){
        if(instance == null){
            synchronized (DatabaseSingleton.class){
                if(instance == null){
                    instance = new DatabaseSingleton();
                }
            }
        }
        return instance;
    }



    public Connection getConnection(){
        return connection;
    }

    public void execute(PreparedStatement statement){
        try
        {
            statement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void disconnect(){
        try
        {
            this.connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
