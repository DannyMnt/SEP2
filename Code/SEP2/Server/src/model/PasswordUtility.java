package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utility class for hashing and verifying passwords.
 */
public class PasswordUtility
{
  private static final int ITERATIONS = 65536;
  private static final int KEY_LENGTH = 256;

  public static final String CLASS = "(server/model/PasswordUtility)";

  private static final Log log = Log.getInstance();


  /**
   * Hashes a password using a provided salt.
   *
   * @param password The password to be hashed.
   * @param salt     The salt used for hashing.
   * @return The hashed password as a Base64-encoded string.
   * @throws RuntimeException If hashing fails due to invalid algorithm or specification.
   */
  public synchronized static String hash(String password, byte[] salt){
    char[] passwordChars = password.toCharArray();
    try
    {
      PBEKeySpec spec = new PBEKeySpec(passwordChars,salt,ITERATIONS,KEY_LENGTH);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
      log.addLog("Failed to hash password " + CLASS);
      log.addLog(Arrays.toString(e.getStackTrace()));
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates a random salt for password hashing.
   *
   * @return The generated salt as a byte array.
   */
  public synchronized static byte[] generateSalt(){
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }

  /**
   * Hashes a password with a randomly generated salt.
   *
   * @param password The password to be hashed.
   * @return The salted and hashed password as a Base64-encoded string.
   */
  public synchronized static String hashPasswordWithSalt(String password){
    byte[] salt = generateSalt();
    String hash = hash(password,salt);
    return Base64.getEncoder().encodeToString(salt) + ":" + hash;
  }

  /**
   * Verifies a password against a stored hashed password.
   *
   * @param password     The password to verify.
   * @param storedHash   The stored hashed password with salt.
   * @return True if the password matches the stored hash, false otherwise.
   */
  public synchronized static boolean verifyPassword(String password, String storedHash){
    String[] parts = storedHash.split(":");
    byte[] salt = Base64.getDecoder().decode(parts[0]);
    String storedPasswordHash = parts[1];
    String passwordHash = hash(password,salt);
    return storedPasswordHash.equals(passwordHash);
  }
}
