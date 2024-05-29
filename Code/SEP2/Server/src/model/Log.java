package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Log
{
  private static Log instance;
  private static final  Object lock = new Object();
  private static final String FILE_PATH = "log.txt";

  private Log(){
    createLogFileIfNotExists();
    addLog(LocalDateTime.now() +": Start of log");
  }

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
