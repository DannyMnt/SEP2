package model;

import static org.junit.jupiter.api.Assertions.*;

import model.PasswordUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.security.spec.InvalidKeySpecException;

public class PasswordUtilityTest {
  private byte[] oneByteSalt;

  @BeforeEach
  void setUp() {
    oneByteSalt = new byte[]{0};
  }

  @AfterEach
  void tearDown() {
    oneByteSalt = null;
  }

  @Test
  void testHash() {
    byte[] emptySalt = new byte[0];
    assertThrows(IllegalArgumentException.class, () -> PasswordUtility.hash("", emptySalt));

    assertDoesNotThrow(() -> PasswordUtility.hash("a", oneByteSalt));

    byte[] salt = new byte[16];
    String password = "thisisalongpassword";
    assertDoesNotThrow(() -> PasswordUtility.hash(password, salt));

    StringBuilder longPassword = new StringBuilder();
    for (int i = 0; i < 256; i++) {
      longPassword.append('a');
    }
    assertDoesNotThrow(() -> PasswordUtility.hash(longPassword.toString(), salt));

    assertThrows(RuntimeException.class, () -> PasswordUtility.hash(null, salt));
    assertThrows(RuntimeException.class, () -> PasswordUtility.hash(password, null));
  }
}
