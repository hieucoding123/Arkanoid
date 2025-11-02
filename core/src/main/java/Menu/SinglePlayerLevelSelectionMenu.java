package Menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.main.Main;
import entity.Player;
import datahandler.LevelDataHandler;

public class SinglePlayerLevelSelectionMenu extends LevelSelectionMenu {

    public SinglePlayerLevelSelectionMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        super.create();

        //Get info from database
        String playerName = this.getPlayer().getName();
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            playerName = "Guest";
        }

        int maxLevel = LevelDataHandler.getPlayerMaxLevel(playerName);
        double totalScore = LevelDataHandler.getPlayerTotalScore(playerName);
        double lvl1 = LevelDataHandler.getPlayerScoreForLevel(playerName, 1);
        double lvl2 = LevelDataHandler.getPlayerScoreForLevel(playerName, 2);
        double lvl3 = LevelDataHandler.getPlayerScoreForLevel(playerName, 3);
        double lvl4 = LevelDataHandler.getPlayerScoreForLevel(playerName, 4);
        double lvl5 = LevelDataHandler.getPlayerScoreForLevel(playerName, 5);

        Label.LabelStyle infoStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        infoStyle.font.getData().setScale(0.7f);

        Label.LabelStyle scoreStyle = new Label.LabelStyle(this.getFont(), Color.LIGHT_GRAY);
        scoreStyle.font.getData().setScale(0.65f);

        Table infoTable = new Table();

        infoTable.add(new Label("Player: " + playerName, infoStyle)).left().padRight(50);
        infoTable.add(new Label("Level Unlocked: " + maxLevel, infoStyle)).right();
        infoTable.row();

        infoTable.add(new Label("Score:", infoStyle)).left().padTop(10).align(Align.topLeft);
        infoTable.add(new Label("Level 1: " + (int)lvl1, scoreStyle)).left().padTop(10).padLeft(1);
        infoTable.row();

        infoTable.add();
        infoTable.add(new Label("Level 2: " + (int)lvl2, scoreStyle)).left().padLeft(1);
        infoTable.row();

        infoTable.add();
        infoTable.add(new Label("Level 3: " + (int)lvl3, scoreStyle)).left().padLeft(1);
        infoTable.row();

        infoTable.add();
        infoTable.add(new Label("Level 4: " + (int)lvl4, scoreStyle)).left().padLeft(1);
        infoTable.row();

        infoTable.add();
        infoTable.add(new Label("Level 5: " + (int)lvl5, scoreStyle)).left().padLeft(1);
        infoTable.row();

        infoTable.add(new Label("Total Score: " + (int)totalScore, infoStyle)).left().padTop(10);
        infoTable.add();
        infoTable.row();

        Table bottomContainerTable = new Table();
        bottomContainerTable.setFillParent(true);

        bottomContainerTable.add(infoTable).expandY().bottom().left().padLeft(10).padBottom(70);

        this.getStage().addActor(bottomContainerTable);
    }
}
