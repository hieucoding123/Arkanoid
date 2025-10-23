package table;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class RecordTable {
    static final int capacity = 10;
    private static ArrayList<Player> board;
    private String systemFile;

    public String getSystemFile() {
        return this.systemFile;
    }

    public void setSystemFile(String systemFile) {
        this.systemFile = systemFile;
    }

    /**
     * Load old record table from system.
     */
    public abstract void createTable();

    public void loadTable() {
        if (board == null) {
            board = new ArrayList<>();
        }
        try {
            FileHandle fileHandle = Gdx.files.internal(systemFile);
            BufferedReader br = fileHandle.reader(8192);

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                board.add(new Player(
                    fields[0], Double.parseDouble(fields[1]), Float.parseFloat(fields[2]))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSystemFile() {
        FileHandle file =  Gdx.files.local(this.systemFile);
        StringBuilder builder = new StringBuilder();

        for (Player player : board) {
            builder.append(
                player.getName()
            ).append(",").append(
                player.getScore()
            ).append(",").append(
                player.getTimePlayed()
            ).append("\n");
        }

        file.writeString(builder.toString(), false);
    }

    public static void addPlayer(Player newPlayer) {
        // Empty board.
        if (board == null) {
            board = new ArrayList<>();
            board.add(newPlayer);
            return;
        }

        // Incomplete table
        if (board.size() < capacity) {
            board.add(newPlayer);
        }

        int idx = board.size() - 1;
        if (!board.get(idx).less(newPlayer)) {
            return;
        }

        // insert to sorted board.
        for ( ; --idx >= 0;) {
            if (board.get(idx).less(newPlayer)) {
                exch(idx, idx + 1);
            }else {
                board.set(idx + 1, newPlayer);
                return;
            }
        }
        // insert on top of table
        board.set(0, newPlayer);
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
