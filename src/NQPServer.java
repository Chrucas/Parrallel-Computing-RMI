import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class NQPServer extends UnicastRemoteObject implements NQPInterface {
    private static final long serialVersionUID = -5496219176349859137L;

    private NQPServer() throws RemoteException {
        super();

    }

    public boolean isSafe(int row, int col, String direction, int[][] board){
        //checks \ <-this way if the given coordinate is safe
        int i,j;
        if(direction.equals("LD")){
            //Check Left Upper Diagonal
            for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
                if(board[i][j] == 1){
                    return false;
                }
            }
            //Check Right Downward Diagonal
            for (i = row+1, j = col+1; i < board.length && j < board.length; i++, j++) {

                if(board[i][j] == 1){
                    return false;
                }
            }
        }
        //checks / <-this way if the given coordinate is safe
        if(direction.equals("RD")){
            //Check Right Upper Diagonal
            for (i = row, j = col; i >= 0 && j < board.length; i--, j++) {
                if(board[i][j] == 1){
                    return false;
                }
            }
            //Check Left Downward Diagonal
            for (i = row+1, j = col-1; i < board.length && j >= 0; i++, j--) {
                if(board[i][j] == 1){
                    return false;
                }
            }
        }
        //checks | <-this way if the given coordinate is safe
        if(direction.equals("V")){
            //Check in same Column
            for (i = 0; i < board.length; i++) {
                if(board[i][col] == 1){
                    return false;
                }
            }
        }
        //checks _ <-this way if the given coordinate is safe
        if(direction.equals("H")){
            //Check in same Column
            for (i = 0; i < board.length; i++) {
                if(board[row][i] == 1){
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] initializeBoard(int[][] board){
        for(int[] row : board){
            Arrays.fill(row, 0);
        }
        return board;
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/NQPServer", new NQPServer());
            System.err.println("Server ready");
        } catch(Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

}
