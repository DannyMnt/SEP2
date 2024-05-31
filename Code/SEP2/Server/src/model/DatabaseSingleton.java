package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Singleton class for managing a database connection.
 */
public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2";
    private static final String USER = "postgres";
    private static final String PSWD = "admin";

    private static final Log log = Log.getInstance();

    private static final String CLASS = "(server/model/DatabaseSingleton)";

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private DatabaseSingleton(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL,USER,PSWD);
        }catch (SQLException e ){
            log.addLog("Failed to connect to the database" + CLASS);
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
        this.connection = conn;
    }

    /**
     * Retrieves the singleton instance of DatabaseSingleton.
     *
     * @return The DatabaseSingleton instance.
     */
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


    /**
     * Retrieves the database connection.
     *
     * @return The database connection.
     */
    public synchronized Connection getConnection(){
        return connection;
    }

    /**
     * Executes a prepared statement.
     *
     * @param statement The prepared statement to execute.
     */
    public synchronized void execute(PreparedStatement statement){
        try
        {
            statement.execute();
        }
        catch (SQLException e){
            log.addLog("Failed to execute statement in the database");
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Disconnects from the database.
     */
    public synchronized void disconnect(){
        try
        {
            this.connection.close();
        }
        catch (SQLException e){
            log.addLog("Failed to disconnect from the database");
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
    }
}
