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
        try {
            FileHandle fileHandle = Gdx.files.internal(systemFile);
            BufferedReader br = fileHandle.reader(8192);

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                board.add(new Player(fields[0], Integer.parseInt(fields[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSystemFile() {
        FileHandle file =  Gdx.files.local(this.systemFile);
        StringBuilder builder = new StringBuilder();

        for (Player player : board) {
            builder.append(player.getName()).append(",").append(player.getScore()).append("\n");
        }

        file.writeString(builder.toString(), false);
    }

    public static void addPlayer(Player newPlayer) {
        System.out.println(newPlayer.getScore());
        // Empty board.
        if (board == null) {
            board = new ArrayList<>();
            board.add(newPlayer);
            return;
        }

        if (board.size() < capacity) {
            board.add(newPlayer);
        }

        // insert to sorted board.
        for (int i = board.size() - 2; i >= 0; i--) {
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
