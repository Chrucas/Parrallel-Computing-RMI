import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class NQPClient {
    private static NQPInterface look_up;
    private static int QUEENS = 32;//never <4 !
    private static int[][] board = new int[QUEENS][QUEENS];

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        long startTime = System.currentTimeMillis();
        look_up = (NQPInterface) Naming.lookup("//localhost/NQPServer");
        initializeBoard(board);
        String[] dirs = new String[4];
        boolean[] results = new boolean[4];
        dirs[0]="H";dirs[1]="V";dirs[2]="RD";dirs[3]="LD";
        ArrayList<point> successes= new ArrayList<>();

        Random r = new Random();
        int placedQueens = 0;
        int tries=0;
        int row = 0;
        search:
        while(placedQueens<QUEENS) {

            int col = r.nextInt(QUEENS);
//            int row = r.nextInt(QUEENS);
            for (int i=0; i<4; i++) {
                results[i] = look_up.isSafe(row, col, dirs[i], board);
            }
            boolean allOK = true;
            for (int i=0; i<4; i++) {

                allOK = allOK && results[i];
            }
            if (!allOK) {
                if (tries++>32) {
                    int x = successes.size()-1;
                    row = successes.get(x).getRow();
                    col = successes.get(x).getCol();
                    successes.remove(x);
                    placedQueens=successes.size();
                    reset(row, col, board);
                }
                continue search;
            }
            set(row, col, board);
            successes.add(new point(row, col));
            row++;
            tries = 0;
            placedQueens=successes.size();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Time to place queens: " + duration +" milliseconds");
        printBoard(board);


    }

    private static void printBoard(int[][] board){
        for (int[] ints : board) {
            for (int col = 0; col < board.length; col++) {
                if (ints[col] == 1) {
                    System.out.print("Q ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeBoard(int[][] board){
        for(int[] row : board){
            Arrays.fill(row, 0);
        }
    }

    private static void set(int row, int col, int[][] board) {
//        System.out.println("setting: row["+row+"] col["+col+"] to 1");
        board[row][col] = 1;
    }

    private static void reset(int row, int col, int[][] board) {
//        System.out.println("setting: row["+row+"] col["+col+"] to 0");
        board[row][col] = 0;
    }
}

class point{
    private int row;
    private int col;

    point(int row, int col) {
        this.row = row;
        this.col = col;
    }
    int getRow() {
        return row;
    }
    int getCol() {
        return col;
    }
}
