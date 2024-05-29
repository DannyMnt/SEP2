package model;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;


public class EmailValidator
{
  public static final String CLASS = "(server/model/PasswordUtility)";
  private static final Log log = Log.getInstance();
  private static final Object lock = new Object();
  public static synchronized boolean isEmailValid(String email){
    String domain = getDomainFromEmail(email);
    if (domain == null) return false;


    return hasMXRecords(domain);
  }


  private static String getDomainFromEmail(String email){
    int atIndex = email.lastIndexOf('@');
    if (atIndex > 0 && atIndex < email.length() - 1){
      return email.substring(atIndex+1);
    }
    return null;
  }

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
      log.addLog(e.getStackTrace().toString());
    }
    return false;
  }




}




