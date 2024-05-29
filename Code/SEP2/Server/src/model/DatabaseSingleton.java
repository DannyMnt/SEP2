package model;



import java.io.Serializable;
import java.sql.*;


public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2";
    private static final String USER = "postgres";
    private static final String PSWD = "admin";

    private static final Log log = Log.getInstance();

    private static final String CLASS = "(server/model/DatabaseSingleton)";

    private DatabaseSingleton(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL,USER,PSWD);
        }catch (SQLException e ){
            log.addLog("Failed to connect to the database");
            log.addLog(e.getStackTrace().toString());
        }
        this.connection = conn;
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



    public synchronized Connection getConnection(){
        return connection;
    }

    public synchronized void execute(PreparedStatement statement){
        try
        {
            statement.execute();
        }
        catch (SQLException e){
            log.addLog("Failed to execute statement in the database");
            log.addLog(e.getStackTrace().toString());
        }
    }


    public synchronized void disconnect(){
        try
        {
            this.connection.close();
        }
        catch (SQLException e){
            log.addLog("Failed to disconnect from the database");
            log.addLog(e.getStackTrace().toString());
        }
    }
}
