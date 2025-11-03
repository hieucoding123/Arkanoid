package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import entity.Player;

/**
 * Display game over screen for VsMode.
 */
public class OfflineGameOver extends UserInterface {

    private final int roundsWonP1;
    private final int roundsWonP2;
    private final Player player2;

    /**
     * Constructor.
     * @param main this main
     * @param player1 player1
     * @param player2 player2
     * @param roundsWonP1 player1's round won
     * @param roundsWonP2 player2's round won
     */
    public OfflineGameOver(Main main, Player player1, Player player2, int roundsWonP1, int roundsWonP2) {
        super(main, player1);
        this.roundsWonP1 = roundsWonP1;
        this.roundsWonP2 = roundsWonP2;
        this.player2 = player2;
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setStage(new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        this.setFont(new com.badlogic.gdx.graphics.g2d.BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);
        Player player1 = this.getPlayer();

        Label.LabelStyle titleStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle resultStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        Label titleLabel = new Label("GAME OVER", titleStyle);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(50).padTop(100);
        mainTable.row();

        String winnerName;

        if (roundsWonP1 > roundsWonP2) {
            winnerName = player1.getName();
        } else if (roundsWonP2 > roundsWonP1) {
            winnerName = player2.getName();
        } else {
            winnerName = "NO ONE";
        }

        Label resultLabel = new Label("WINNER: " + winnerName, resultStyle);
        resultLabel.setFontScale(1.2f);
        mainTable.add(resultLabel).padBottom(30);
        mainTable.row();

        Label scoreLabel = new Label("Final Score: " + roundsWonP1 + " - " + roundsWonP2, titleStyle);
        scoreLabel.setFontScale(1.0f);
        mainTable.add(scoreLabel).padBottom(80);
        mainTable.row();

        Label playAgainButton = createClickableLabel(
            "Play Again",
            Game.sfx_click,
            1.0f,
            () -> getMain().setGameState(GameState.VS_MODE)
        );
        mainTable.add(playAgainButton).width(200).height(60).padBottom(20).padRight(30);
        mainTable.row();

        Label mainMenuButton  = createClickableLabel(
            "Main Menu  ",
            Game.sfx_back,
            1.0f,
            () -> getMain().setGameState(GameState.MAIN_MENU)
        );
        mainTable.add(mainMenuButton).width(200).height(60).padBottom(20).padRight(60);
        mainTable.row();
    }
}
