package model;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.print.attribute.standard.NumberOfDocuments;

public class PasswordUtility
{
  public static String hash(String password, byte[] salt){
    int iterations = 65536;
    int keyLength = 256;
    char[] passwordChars = password.toCharArray();

    try
    {
      PBEKeySpec spec = new PBEKeySpec(passwordChars,salt,iterations,keyLength);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
      throw new RuntimeException(e);
    }
  }

  public static byte[] generateSalt(){
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }

  public static String hashPasswordWithSalt(String password){
    byte[] salt = generateSalt();
    String hash = hash(password,salt);
    return Base64.getEncoder().encodeToString(salt) + ":" + hash;
  }

  public static boolean verifyPassword(String password, String storedHash){
    String[] parts = storedHash.split(":");
    byte[] salt = Base64.getDecoder().decode(parts[0]);
    String storedPasswordHash = parts[1];
    String passwordHash = hash(password,salt);
    System.out.println(storedHash + " | hello | " + passwordHash);
    return storedPasswordHash.equals(passwordHash);
  }
}
