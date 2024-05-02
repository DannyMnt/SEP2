package model;



import java.sql.*;


public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
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

    public void addEvent(Event event){
        String sql = "INSERT INTO events (eventId, title, description, startTime, endTime, ownerId) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getEventId().toString());
            statement.setString(2, event.getTitle());
            statement.setString(3, event.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(event.getStartTime()));
            statement.setTimestamp(5, Timestamp.valueOf(event.getEndTime()));
            statement.setString(6, event.getOwnerId().toString());

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertData(String tableName, String[] columns, Object[] values){
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]);
            if(i < columns.length - 1){
                sql.append(", ");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if(i < values.length - 1){
                sql.append(", ");
            }
        }
        sql.append(")");

        try(PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i+1,values[i]);
            }
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
