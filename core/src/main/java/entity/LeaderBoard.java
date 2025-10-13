package entity;

import java.util.ArrayList;

public class LeaderBoard {
    static final int capacity = 10;
    private static ArrayList<Player> board;

    public static void addUser(Player newPlayer) {
        // Empty board.
        if (board == null) {
            board = new ArrayList<>();
            board.add(newPlayer);
            return;
        }

        if (board.size() < capacity) {
            board.add(new Player());
        }
        // insert to sorted board.
        for (int i = board.size() - 1; i >= 0; i--) {
            if (!board.get(i).less(newPlayer)) {
                exch(i, i + 1);
            }else {
                board.get(i).replace(newPlayer);
            }
        }
    }

    /**
     * Exchange position of two players.
     * @param i position i
     * @param j position j
     */
    private static void exch(int i, int j) {
        Player t =  board.get(i);
        board.set(i, board.get(j));
        board.set(j, t);
    }
}
