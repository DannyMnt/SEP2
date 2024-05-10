package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

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
      statement.setString(3,user.getPassword());
      statement.setTimestamp(4, Timestamp.valueOf(user.getCreationDate()));
      statement.setString(5,user.getFirstname());
      statement.setString(6,user.getLastname());
      statement.setDate(7, Date.valueOf(user.getDateOfBirth()));
      statement.setString(8,user.getSex());
      statement.setString(9,user.getPassword());

    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  public User getUserById(UUID userId){
    String sql = "SELECT * FROM users WHERE userId = ?";
    User user = null;

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,userId);
      try(ResultSet resultSet = statement.executeQuery())
      {
        if(resultSet.next()){
          user = new User(
              UUID.fromString(resultSet.getString("userId")),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("firstname"),
              resultSet.getString("lastname"),
              resultSet.getString("sex"),
              resultSet.getString("phoneNumber"),
              resultSet.getTimestamp("creationDate").toLocalDateTime(),
              (resultSet.getDate("dateOfBirth")).toLocalDate()

          );
        }
      }

    }catch (SQLException e){
      e.printStackTrace();
    }
  return user;
  }

  public User getUserByEmail(String email){
    String sql = "SELECT * FROM users WHERE email = ?";
    User user = null;

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,email);
      try(ResultSet resultSet = statement.executeQuery())
      {
        if(resultSet.next()){
          user = new User(
              UUID.fromString(resultSet.getString("userId")),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("firstname"),
              resultSet.getString("lastname"),
              resultSet.getString("sex"),
              resultSet.getString("phoneNumber"),
              resultSet.getTimestamp("creationDate").toLocalDateTime(),
              (resultSet.getDate("dateOfBirth")).toLocalDate()

          );
        }
      }

    }catch (SQLException e){
      e.printStackTrace();
    }
    return user;
  }


  public boolean isEmailValid(String email){
    boolean exists = false;
    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,email);

      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      int count = resultSet.getInt(1);
      exists = count > 0;

    }catch (SQLException e){
      e.printStackTrace();
    }

    return exists;
  }


  public void updateUser(User user){
    //
  }

  public void deleteUser(UUID userId){
    String sql = "DELETE FROM users WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

}
