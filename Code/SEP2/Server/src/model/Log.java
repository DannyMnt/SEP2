package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Singleton class for logging events to a file.
 */
public class Log
{
  private static Log instance;
  private static final  Object lock = new Object();
  private static final String FILE_PATH = "log.txt";

  /**
   * Private constructor to prevent instantiation from outside.
   * Creates a log file if it doesn't exist and adds a start log entry.
   */
  private Log(){
    createLogFileIfNotExists();
    addLog(LocalDateTime.now() +": Start of log");
  }

  /**
   * Retrieves the singleton instance of Log.
   *
   * @return The Log instance.
   */
  public static Log getInstance(){
    if(instance == null){
      synchronized (lock){
        if (instance == null){
          instance = new Log();
        }
      }
    }
    return instance;
  }

  /**
   * Adds a log entry to the log file with timestamp.
   *
   * @param log The log entry to add.
   */
  public void addLog(String log){
    synchronized (lock){
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH,true)))
      {
        writer.write(LocalDateTime.now() + ": " + log);
        writer.newLine();
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }

  /**
   * Creates the log file if it does not exist.
   */
  private void createLogFileIfNotExists(){
    File logFile = new File(FILE_PATH);
    if(!logFile.exists()){
      try
      {
        logFile.createNewFile();
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }
}
