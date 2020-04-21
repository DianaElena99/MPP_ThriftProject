package Observer;

import java.rmi.RemoteException;

public interface IObserver {
    void update() throws RemoteException;
}
