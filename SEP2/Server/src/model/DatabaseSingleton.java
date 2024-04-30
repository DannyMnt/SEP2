package model;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PSWD = "admin";

    private DatabaseSingleton(){
        try{
            connection = DriverManager.getConnection(URL,USER,PSWD);
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
}
