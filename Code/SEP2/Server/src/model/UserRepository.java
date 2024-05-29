package model;


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
  private final Log log;

  private final String CLASS = "(server/model/UserRepository)";

  public UserRepository(DatabaseSingleton database){
    this.database = database;
    this.log = Log.getInstance();
  }


  /**
   * Writes a byte array to a file in a specified directory.
   *
   * <p>This method creates the necessary directories if they do not exist,
   * and writes the byte array to a file with the specified name and a .png extension
   * in the directory.</p>
   *
   * @param byteArray the byte array to be written to the file
   * @param fileName the name of the file (without extension) to be created
   */
  private synchronized void writeByteArrayToFile(byte[] byteArray, String fileName) {

    String folderPath = System.getProperty("user.dir")+"/SEP2/Server/src/images";

    try {
      Files.createDirectories(Paths.get(folderPath));

      Path filePath = Paths.get(folderPath, fileName+".png");
      Files.write(filePath, byteArray);

    } catch (IOException e) {
      log.addLog("Failed to save image " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  private String hashPassword(String password){
      return PasswordUtility.hashPasswordWithSalt(password);

  }

  /**
   * Reads a byte array from a file in a specified directory.
   *
   * <p>This method attempts to read the contents of a file with the specified name and a .png extension
   * from a designated directory. If the file is not found or an I/O error occurs, it attempts to read
   * a default "unknown.png" file from the same directory. If that also fails, it logs the error and returns null.</p>
   *
   * @param fileName the name of the file (without extension) to be read
   * @return a byte array containing the file's contents, or null if an error occurs
   */
  public byte[] readByteArrayFromFile(String fileName) {
    String folderPath = System.getProperty("user.dir") + "/SEP2/Server/src/images";

    try {
      Path filePath = Paths.get(folderPath, fileName + ".png");

      byte[] byteArray;
      synchronized (this){
        byteArray = Files.readAllBytes(filePath);
      }
      return byteArray;
    } catch (IOException e) {
        try{

          Path filePath = Paths.get(folderPath, "unknown.png");
          byte[] byteArray;
          synchronized (this){
            byteArray = Files.readAllBytes(filePath);
          }
        return byteArray;
      } catch (IOException es){
          log.addLog("Failed to read image " + CLASS);
          log.addLog(e.getStackTrace().toString());
        return null;
      }
    }
  }

  /**
   * Creates a new user in the database.
   *
   * <p>This method inserts a new user record into the users table in the database. It first saves the user's profile picture
   * to the file system and then stores the user's details in the database, including a reference to the profile picture file.</p>
   *
   * @param user the User object containing the details of the user to be created
   */
  public synchronized void createUser(User user){
    String sql = "INSERT INTO users (userId, email, password, creationDate, firstname, lastname, dateOfBirth,sex, phoneNumber, profilePicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    writeByteArrayToFile(user.getProfilePicture(), "profilePicture-"+user.getId());
    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,user.getId());
      statement.setString(2,user.getEmail());
      statement.setString(3,hashPassword(user.getPassword()));
      statement.setTimestamp(4, Timestamp.valueOf(user.getCreationDate()));
      statement.setString(5,user.getFirstname());
      statement.setString(6,user.getLastname());
      statement.setDate(7, Date.valueOf(user.getDateOfBirth()));
      statement.setString(8,user.getSex());
      statement.setString(9,user.getPhoneNumber());
      statement.setObject(10, "profilePicture-"+user.getId());

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to insert new user into database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  /**
   * Retrieves a user from the database by their user ID.
   *
   * <p>This method queries the database for a user with the specified user ID. If the user is found,
   * it creates a User object with the retrieved data, including reading the profile picture from the file system.
   * If the user is not found or an error occurs, it logs the error and returns null.</p>
   *
   * @param userId the UUID of the user to be retrieved
   * @return the User object containing the user's details, or null if the user is not found or an error occurs
   */
  public synchronized User getUserById(UUID userId){
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
              "",
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
      log.addLog("Failed to get user by id from database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  return user;
  }

  public synchronized User getUserByEmail(String email){
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
              "",
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
      log.addLog("Failed to get user by email from database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
    return user;
  }


  public synchronized LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
    Connection connection = database.getConnection();
    System.out.println(loginPackage.getPassword());
    System.out.println(loginPackage.getEmail());
    if (connection == null) {
      log.addLog("Failed to connect to the database " + CLASS);
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

      if (!PasswordUtility.verifyPassword(loginPackage.getPassword(),storedPassword)) {
        throw new IllegalArgumentException("Incorrect password.");
      }

      loginPackage.setUuid(userId);
      loginPackage.setPassword("");
      loginPackage.setEmail("");
      return loginPackage;
    }
  }

  public synchronized boolean isEmailFree(String email){
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
      log.addLog("Failed while checking if email is in database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
    System.out.println(exists);
    return exists;
  }


  public synchronized void updateEmail(UUID userId, String newEmail){
    String sql = "UPDATE users SET email = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,newEmail);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to update email in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

public synchronized void updatePassword(String password, UUID userId){
  String sql = "UPDATE users SET password = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
  {
    statement.setString(1,hashPassword(password));
    statement.setObject(2,userId);

    statement.executeUpdate();
  }catch (SQLException e){
    log.addLog("Failed to update password in the database " + CLASS);
    log.addLog(e.getStackTrace().toString());
  }
}

public synchronized void updateUser(User user){
    String sql = "UPDATE users SET email = ?, phonenumber = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
      statement.setString(1, user.getEmail());
      statement.setString(2, user.getPhoneNumber());
      statement.setObject(3, user.getId());

    statement.executeUpdate();
    }
    catch (SQLException e){
      log.addLog("Failed to update the user in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
}


public synchronized void updateFirstname(String firstName, UUID userId){
  String sql = "UPDATE users SET firstName = ? WHERE userId = ?";

  try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
  {
    statement.setString(1,firstName);
    statement.setObject(2,userId);

    statement.executeUpdate();
  }catch (SQLException e){
    log.addLog("Failed to update firstname in the database " + CLASS);
    log.addLog(e.getStackTrace().toString());
  }
}

  public synchronized void updateLastname(String lastName, UUID userId){
    String sql = "UPDATE users SET lastName = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,lastName);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to update lastname in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  public synchronized void updateSex(String sex, UUID userId){
    String sql = "UPDATE users SET sex = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,sex);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to update sex in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  public synchronized void updatePhoneNumber(String phoneNumber, UUID userId){
    String sql = "UPDATE users SET phoneNumber = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setString(1,phoneNumber);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to update the phone number in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  public synchronized void updateDateOfBirth(LocalDate dateOfBirth, UUID userId){
    String sql = "UPDATE users SET dateOfBirth = ? WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,dateOfBirth);
      statement.setObject(2,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to update the date of birth in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());
    }
  }

  public synchronized void deleteUser(UUID userId){
    String sql = "DELETE FROM users WHERE userId = ?";

    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,userId);

      statement.executeUpdate();
    }catch (SQLException e){
      log.addLog("Failed to delete the user in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());    }
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
      synchronized (this){
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            User user = new User(
                UUID.fromString(resultSet.getString("userid")),
                resultSet.getString("email"),
                "",
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
      }
    } catch (SQLException e) {
      log.addLog("Failed while searching users by name in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());    }
    return users;
  }


  public synchronized void createUserEvent(Event event){
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
      database.getConnection().setAutoCommit(true);
    }
    catch (SQLException e){
      log.addLog("Failed to create the userevent in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());    }

  }

  public synchronized boolean verifyPassword(UUID userId, String password){
    String sql = "SELECT password FROM users WHERE userid = ?";
    boolean verified = false;
    try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
    {
      statement.setObject(1,userId);
      try(ResultSet resultSet = statement.executeQuery())
      {
        if(resultSet.next()){
          verified = PasswordUtility.verifyPassword(password,resultSet.getString("password"));
          System.out.println(verified);
        }
      }
    }catch (SQLException e){
      log.addLog("Failed to verify the password in the database " + CLASS);
      log.addLog(e.getStackTrace().toString());    }
    return verified;
  }

}
