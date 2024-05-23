package mediator;

import model.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote
{
  void notify(String change,Event event) throws RemoteException;
}
