package model;

//import at.favre.lib.crypto.bcrypt.BCrypt;
import mediator.LoginPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository
{
  private final DatabaseSingleton database;

  public UserRepository(DatabaseSingleton database){
    this.database = database;
  }



  private void writeByteArrayToFile(byte[] byteArray, String fileName) {
//    String folderPath = "images";
    String folderPath = System.getProperty("user.dir")+"/SEP2/Server/src/images";

    try {
      // Create the folder if it doesn't exist
      Files.createDirectories(Paths.get(folderPath));

      // Write the byte array to the file
      Path filePath = Paths.get(folderPath, fileName+".png");
      Files.write(filePath, byteArray);

      System.out.println("Image saved successfully to: " + filePath.toAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public byte[] readByteArrayFromFile(String fileName) {
    String folderPath = System.getProperty("user.dir") + "/SEP2/Server/src/images";

    try {
      // Construct the file path
      Path filePath = Paths.get(folderPath, fileName + ".png");

      // Read the file into a byte array
      byte[] byteArray = Files.readAllBytes(filePath);

      System.out.println("Image read successfully from: " + filePath.toAbsolutePath());
      return byteArray;
    } catch (IOException e) {
      try{

      Path filePath = Paths.get(folderPath, "unknown.png");

      return Files.readAllBytes(filePath);
      } catch (IOException es){
        return null;
      }
    }
  }

  public void createUser(User user){
    String sql = "INSERT INTO users (userId, email, password, creationDate, firstname, lastname, dateOfBirth,sex, phoneNumber, profilePicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    writeByteArrayToFile(user.getProfilePicture(), "profilePicture-"+user.getId());
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
      statement.setString(9,user.getPhoneNumber());
      statement.setObject(10, "profilePicture-"+user.getId());

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
              (resultSet.getDate("dateOfBirth")).toLocalDate(),
                  readByteArrayFromFile(resultSet.getString("profilePicture"))
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
              (resultSet.getDate("dateOfBirth")).toLocalDate(),
                  readByteArrayFromFile(resultSet.getString("profilePicture"))


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
    System.out.println(loginPackage.getPassword());
    System.out.println(loginPackage.getEmail());
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

  public boolean isEmailFree(String email){
    boolean exists = true;
    System.out.println(email);
    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,email);

      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      int count = resultSet.getInt(1);
      exists = count <= 0;

    }catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println(exists);
    return exists;
  }


  public void updateEmail(UUID userId, String newEmail){
    String sql = "UPDATE users SET email = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,newEmail);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

public void updatePassword(String password, UUID userId){
  String sql = "UPDATE users SET password = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
  {
    statement.setString(1,password);
    statement.setObject(2,userId);

    statement.executeUpdate();
  }catch (SQLException e){
    e.printStackTrace();
  }
}

public void updateUser(User user){
    String sql = "UPDATE users SET email = ?, phonenumber = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
      statement.setString(1, user.getEmail());
      statement.setString(2, user.getPhoneNumber());
      statement.setObject(3, user.getId());

    statement.executeUpdate();
    }
    catch (SQLException e){
      e.printStackTrace();
    }
}


public void updateFirstname(String firstName, UUID userId){
  String sql = "UPDATE users SET firstName = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
  {
    statement.setString(1,firstName);
    statement.setObject(2,userId);

    statement.executeUpdate();
  }catch (SQLException e){
    e.printStackTrace();
  }
}

  public void updateLastname(String lastName, UUID userId){
    String sql = "UPDATE users SET lastName = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,lastName);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  public void updateSex(String sex, UUID userId){
    String sql = "UPDATE users SET sex = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,sex);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  public void updatePhoneNumber(String phoneNumber, UUID userId){
    String sql = "UPDATE users SET phoneNumber = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,phoneNumber);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  public void updateDateOfBirth(LocalDate dateOfBirth, UUID userId){
    String sql = "UPDATE users SET dateOfBirth = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,dateOfBirth);
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

  public List<User> searchUsersByName(String search) {
    String sql = "SELECT userId,firstname,lastname,email, password, sex, phoneNumber, creationDate, dateOfBirth, profilePicture FROM " +
            "users " +
            "WHERE " +
            "firstname ILIKE ? OR lastname ILIKE ?";
    List<User> users = new ArrayList<>();

    try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
      statement.setString(1, search + "%");
      statement.setString(2, search + "%");

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          User user = new User(
                  UUID.fromString(resultSet.getString("userid")),
                  resultSet.getString("email"),
                  resultSet.getString("password"),
                  resultSet.getString("firstname"),
                  resultSet.getString("lastname"),
                  resultSet.getString("sex"),
                  resultSet.getString("phoneNumber"),
                  resultSet.getTimestamp("creationDate").toLocalDateTime(),
                  resultSet.getDate("dateOfBirth").toLocalDate(),
                  readByteArrayFromFile(resultSet.getString("profilePicture"))
          );

          users.add(user);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }
    public List<User> searchUsersByFullName(String search){
      String sql = "SELECT userId,firstname,lastname,email, password, sex, phoneNumber, creationDate, dateOfBirth, profilePicture " +
              "FROM " +
              "users " +
              "WHERE " +
              "firstname ILIKE ? AND lastname ILIKE ?";
      List<User> users = new ArrayList<>();

      try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
      {
        statement.setString(1,search + "%");
        statement.setString(2,search + "%");

        try(ResultSet resultSet = statement.executeQuery())
        {
          while (resultSet.next()){
            User user = new User(
                    UUID.fromString(resultSet.getString("userid")),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("sex"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getTimestamp("creationDate").toLocalDateTime(),
                    resultSet.getDate("dateOfBirth").toLocalDate(),
                    readByteArrayFromFile(resultSet.getString("profilePicture"))
            );

            users.add(user);
          }
        }
      }catch (SQLException e){
        e.printStackTrace();
      }
      return users;
  }

  public void createUserEvent(Event event){
    String sql = "INSERT INTO userevents (userId, eventId) VALUES (?, ?)";
    try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
      database.getConnection().setAutoCommit(false);
      for (UUID attendeeID:event.getAttendeeIDs()){
        statement.setObject(1,attendeeID);
        statement.setObject(2,event.getEventId());

        statement.addBatch();
      }


      int[] result = statement.executeBatch();

      database.getConnection().commit();
      System.out.println("Inserted rows:" + result.length);
      database.getConnection().setAutoCommit(true);
    }
    catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println("UserEvent created");
  }


}
