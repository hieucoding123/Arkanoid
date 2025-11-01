package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.GameState;
import com.main.Main;
import entity.Player;

/**
 * A simple end screen for Level, Coop, and Infinite modes, modeled on GameOver.java.
 * Displays a title (e.g., "Level Complete!" or "Game Over"),
 * the final score, and a button to continue.
 */
public class GameSummaryScreen extends UserInterface {

    private String title;
    private String scoreText;
    private GameState nextState;

    /**
     * Constructor for the game summary screen.
     *
     * @param main        The main game instance.
     * @param player      The player.
     * @param title       The text to display as the main title (e.g., "YOU WIN!").
     * @param scoreText   The text to display for the score (e.g., "Final Score: 5000").
     * @param nextState   The GameState to transition to when "Continue" is clicked.
     */
    public GameSummaryScreen(Main main, Player player, String title, String scoreText, GameState nextState) {
        super(main, player);
        this.title = title;
        this.scoreText = scoreText;
        this.nextState = nextState;
    }

    @Override
    public void create() {
        this.setStage(new Stage(new FitViewport(800, 1000)));
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        Gdx.input.setInputProcessor(this.getStage());

        // Main table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        if (this.getBGTexture() != null) {
            mainTable.setBackground(new TextureRegionDrawable(this.getBGTexture()));
        }
        this.getStage().addActor(mainTable);

        // Label Styles
        Label.LabelStyle titleStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        // Title Label
        Label titleLabel = new Label(this.title, titleStyle);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(50).row();

        // Score Label
        Label scoreLabel = new Label(this.scoreText, scoreStyle);
        scoreLabel.setFontScale(1.0f);
        mainTable.add(scoreLabel).padBottom(100).row();

        // Continue Button
        Label continueButton = createClickableLabel("Continue", new Runnable() {
            @Override
            public void run() {
                getMain().setGameState(nextState);
            }
        });
        mainTable.add(continueButton).row();
    }
}
