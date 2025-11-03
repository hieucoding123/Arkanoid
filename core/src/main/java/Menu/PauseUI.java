package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table; // Import Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.Game;

/**
 * Pause menu overlay displayed when the game is paused during active gameplay.
 *
 * <p>This UI provides options to resume the game, start a new game, return to
 * the main menu, or quit the application entirely. It renders as an overlay on
 * top of the paused game screen without clearing the background.</p>
 *
 * @see Game Game controller that manages pause state
 * @see UserInterface Base menu class
 */
public class PauseUI extends UserInterface {

    public PauseUI(SpriteBatch batch, final Game game) {
        super(game.getMain(), game.getPlayer());

        Viewport viewport = new FitViewport(800, 1000);
        Stage stage = new Stage(viewport, batch);
        setStage(stage);

        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        setFont(font);

        Table table = new Table();
        table.setFillParent(true);

        Label resumeLabel = createClickableLabel("RESUME", game::ContinueGame);

        Label newGameLabel = createClickableLabel("NEW GAME", game::NewGame);

        Label returnLabel = createClickableLabel("RETURN TO MENU", game::ReturnMenu);


        Label.LabelStyle redStyle = new Label.LabelStyle(font, Color.RED);
        final Label quitLabel = new Label("QUIT GAME", redStyle);
        final Color HOVER_COLOR = Color.YELLOW;
        final Color DEFAULT_COLOR = Color.RED;

        quitLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                quitLabel.setColor(HOVER_COLOR);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                quitLabel.setColor(DEFAULT_COLOR);
            }
        });


        float fontScale = 1.5f;
        resumeLabel.setFontScale(fontScale);
        newGameLabel.setFontScale(fontScale);
        returnLabel.setFontScale(fontScale);
        quitLabel.setFontScale(fontScale);

        float buttonPadding = 80f;

        table.add(resumeLabel).padBottom(buttonPadding).row();
        table.add(newGameLabel).padBottom(buttonPadding).row();
        table.add(returnLabel).padBottom(buttonPadding).row();
        table.add(quitLabel).row();

        stage.addActor(table);
    }

    public void act(float delta) {
        if (getStage() != null) {
            getStage().act(delta);
        }
    }

    /**
     * Overrides UserInterface.render() to only draw the stage.
     * This prevents clearing the screen, so it acts as an overlay.
     */
    @Override
    public void render() {
        if (getStage() != null) {
            getStage().getViewport().apply();
            getStage().draw();
        }
    }

}
