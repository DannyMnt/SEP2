package model;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.slf4j.*;

import java.util.Arrays;

/**
 * Provides methods to validate email addresses by checking their domain's MX records.
 */
public class EmailValidator
{
  public static final String CLASS = "(server/model/PasswordUtility)";
  private static final Log log = Log.getInstance();
  private static final Object lock = new Object();

  /**
   * Checks if the provided email address is valid by verifying its domain's MX records.
   *
   * @param email The email address to validate.
   * @return True if the email address is valid, false otherwise.
   */
  public static synchronized boolean isEmailValid(String email){
    String domain = getDomainFromEmail(email);
    if (domain == null) return false;


    return hasMXRecords(domain);
  }


  /**
   * Extracts the domain from the given email address.
   *
   * @param email The email address from which to extract the domain.
   * @return The domain extracted from the email address, or null if extraction fails.
   */
  private static String getDomainFromEmail(String email){
    int atIndex = email.lastIndexOf('@');
    if (atIndex > 0 && atIndex < email.length() - 1){
      return email.substring(atIndex+1);
    }
    return null;
  }

  /**
   * Checks if the given domain has MX (Mail Exchange) records.
   *
   * @param domain The domain to check for MX records.
   * @return True if the domain has MX records, false otherwise.
   */
  private static boolean hasMXRecords(String domain) {
    try {
      synchronized (lock){
        Lookup lookup = new Lookup(domain, Type.MX);
        SimpleResolver resolver = new SimpleResolver();
        resolver.setTimeout(5);
        lookup.setResolver(resolver);
        lookup.run();
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
          Record[] records = lookup.getAnswers();
          if (records != null && records.length > 0) {
            return true;
          }
        }
      }

    } catch (Exception e) {
      log.addLog("Failed while searching for MX records " + CLASS);
      log.addLog(Arrays.toString(e.getStackTrace()));
    }
    return false;
  }




}




