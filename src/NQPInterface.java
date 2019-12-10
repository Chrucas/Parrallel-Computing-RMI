import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NQPInterface extends Remote {

    boolean isSafe(int row, int col, String direction, int [][] board) throws RemoteException;
}
