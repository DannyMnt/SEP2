package mediator;

import model.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ClientCallback interface defines a callback method that can be invoked by the server to notify clients of changes.
 */
public interface ClientCallback extends Remote
{
  /**
   * Notifies the client of a change.
   * @param change The type of change that occurred.
   * @param event The event associated with the change.
   * @throws RemoteException if a remote communication error occurs.
   */
  void notify(String change,Event event) throws RemoteException;
}
