package model;

import mediator.LoginPackage;

import java.sql.*;
import java.util.UUID;

public class UserRepository
{
  private DatabaseSingleton database;

  public UserRepository(DatabaseSingleton database){
    this.database = database;
  }

  public void createUser(User user){
    String sql = "INSERT INTO users (userId, email, password, creationDate, firstname, lastname, dateOfBirth,sex, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


    System.out.println("Inserting user into database");
    System.out.println(user.toString());
    System.out.println(user.getEmail());
    System.out.println(user.getSex());
    System.out.println(user.getFirstname());
    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,user.getId());
      statement.setString(2,user.getEmail());
      statement.setString(3,user.getPassword());
      statement.setTimestamp(4, Timestamp.valueOf(user.getCreationDate()));
      statement.setString(5,user.getFirstname());
      statement.setString(6,user.getLastname());
      statement.setDate(7, Date.valueOf(user.getDateOfBirth()));
      statement.setString(8,"M");
      statement.setString(9,user.getPhoneNumber());

      statement.executeUpdate();
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
      System.out.println(user.getEmail());
      System.out.println(user.getPhoneNumber());
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


  public LoginPackage loginUser(LoginPackage loginPackage) throws SQLException, Exception {
    Connection connection = database.getConnection();
    if (connection == null) {
      throw new SQLException("Failed to connect to the database.");
    }

    if (loginPackage.getEmail().isEmpty() && loginPackage.getPassword().isEmpty()) {
      throw new IllegalArgumentException("Email and password are empty.");
    }

    String query = "SELECT userId, email, password FROM users WHERE email = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, loginPackage.getEmail());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
//        throw new UserAuthenticationException("User with email " + loginPackage.getEmail() + " not found.");
        throw new IllegalArgumentException("User with email " + loginPackage.getEmail() + " not found.");
      }
      UUID userId = UUID.fromString(resultSet.getString("userId"));
      String storedPassword = resultSet.getString("password");

      if (!storedPassword.equals(loginPackage.getPassword())) {
        throw new IllegalArgumentException("Incorrect password.");
      }

      loginPackage.setUuid(userId);
      return loginPackage;
    }
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

  public void updateEmail(UUID userId, String newEmail){
    String sql = "UPDATE users SET email = ? WHERE id = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,newEmail);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
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
