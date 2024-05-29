package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest
{



  @Test
  void testIsEmailValid() {
    assertFalse(EmailValidator.isEmailValid("invalidemail"));

    assertTrue(EmailValidator.isEmailValid("test@example.com"));

    assertFalse(EmailValidator.isEmailValid("invalidemail@"));
    assertFalse(EmailValidator.isEmailValid("invalidemail@domain"));
    assertFalse(EmailValidator.isEmailValid("@domain.com"));
    assertTrue(EmailValidator.isEmailValid("test.valid@email.com"));

    StringBuilder longEmail = new StringBuilder();
    for (int i = 0; i < 320; i++) {
      longEmail.append('a');
    }
    assertTrue(EmailValidator.isEmailValid(longEmail.toString() + "@example.com")); // Maximum length email
    assertFalse(EmailValidator.isEmailValid("test@!#$%&'*+/=?^_`{|}~abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789.com")); // Email with unusual characters

    StringBuilder longDomain = new StringBuilder();
    for (int i = 0; i < 255; i++) {
      longDomain.append('a');
    }
    assertFalse(EmailValidator.isEmailValid("test@" + longDomain.toString() + ".com")); // Maximum length domain

    assertFalse(EmailValidator.isEmailValid("test@example.invalid")); // Invalid domain
  }

}