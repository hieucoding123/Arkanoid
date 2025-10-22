package table;

public class InfiModeTable extends RecordTable
{
    public InfiModeTable() {
        this.createTable();
    }
    @Override
    public void createTable() {
        this.setSystemFile("leaderBoard/infiniteRecords.txt");
        this.loadTable();
    }
}
