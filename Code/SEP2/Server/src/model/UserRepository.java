package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository
{
  private DatabaseSingleton database;

  public UserRepository(DatabaseSingleton database){
    this.database = database;
  }

  public void createUser(User user){
    String sql = "INSERT INTO users (userId, email, password, creationDate, firstname, lastname, dateOfBirth,sex, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,user.getId());
      statement.setString(2,user.getEmail());
      statement.setString();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

}
