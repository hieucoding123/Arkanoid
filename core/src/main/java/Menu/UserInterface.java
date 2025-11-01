package Menu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.main.Game;
import com.main.Main;
import entity.Player;

public class UserInterface extends ApplicationAdapter {
    private Stage stage;
    private final Main main;
    private Skin skin;
    private Texture bg;
    private BitmapFont font;
    private final Player player;


    public UserInterface(Main main, Player player) {
        this.main = main;
        this.player = player;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();

        if (this.bg != null) {
            stage.getBatch().begin();
            stage.getBatch().draw(this.bg, 0, 0,
                stage.getViewport().getWorldWidth(),
                stage.getViewport().getWorldHeight());
            stage.getBatch().end();
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (font != null) font.dispose();
    }

    public void update() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBackground(Texture bg) {
        this.bg = bg;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public Main getMain() {
        return this.main;
    }

    public Stage getStage() {
        return stage;
    }

    public Texture getBGTexture() {
        return this.bg;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkin() {
        return skin;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Constructor for enabled conditionally label button that has all params.
     *
     * @param text The text to display on the label.
     * @param clickSound The sound to play on click.
     * @param soundVolume The volume for the click sound.
     * @param unlocked If false, the label will be gray and non-interactive.
     * @param clickAction The action (as a Runnable) to perform when clicked.
     * @return A new Label instance configured as a button.
     */
    public Label createClickableLabel(String text,
                                      final Sound clickSound, final float soundVolume,
                                      boolean unlocked,
                                      final Runnable clickAction) {

        Label.LabelStyle style = new Label.LabelStyle(this.getFont(), Color.WHITE);
        final Label buttonLabel = new Label(text, style);
        final Color DEFAULT_COLOR = Color.WHITE;
        final Color HOVER_COLOR = Color.YELLOW;

        if (unlocked) {
            buttonLabel.setColor(DEFAULT_COLOR);

            buttonLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (clickSound != null) {
                        clickSound.play(soundVolume);
                    }
                    if (clickAction != null) {
                        clickAction.run();
                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    buttonLabel.setColor(HOVER_COLOR);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    buttonLabel.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            buttonLabel.setColor(Color.GRAY);
        }

        return buttonLabel;
    }

    /**
     * Constructor for enabled conditionally label button that has 3 params.
     *
     * @param text The text to display.
     * @param unlocked If false, the label will be gray and non-interactive.
     * @param clickAction The action to run on click.
     * @return A new Label instance.
     */
    public Label createClickableLabel(String text,
                                      boolean unlocked,
                                      final Runnable clickAction) {

        final Sound CLICK_SOUND = Game.sfx_click;
        final float CLICK_VOLUME = 0.8f;

        return createClickableLabel(
            text,
            CLICK_SOUND, CLICK_VOLUME,
            unlocked,
            clickAction
        );
    }

    /**
     * Constructor for enabled unconditionally label button that has 4 params.
     * @param text The text to display.
     * @param clickSound The sound to play on click.
     * @param soundVolume The volume for the click sound.
     * @param clickAction The action to run on click.
     * @return A new Label instance.
     */
    public Label createClickableLabel(String text,
                                      final Sound clickSound, final float soundVolume,
                                      final Runnable clickAction) {
        return createClickableLabel(
            text,
            clickSound, soundVolume,
            true,
            clickAction
        );
    }

    /**
     * Constructor for enabled unconditionally label button that has 2 params.
     * @param text The text to display.
     * @param clickAction The action to run on click.
     * @return A new Label instance.
     */
    public Label createClickableLabel(String text, final Runnable clickAction) {
        final Sound CLICK_SOUND = Game.sfx_click;
        final float CLICK_VOLUME = 0.8f;

        return createClickableLabel(
            text,
            CLICK_SOUND, CLICK_VOLUME,
            true,
            clickAction
        );
    }

    /**
     * Constructor for enabled unconditionally label button that has 3 params.
     * @param text The text to display.
     * @param clickSound The sound to play on click.
     * @param clickAction The action to run on click.
     * @return A new Label instance.
     */
    public Label createClickableLabel(String text, final Sound clickSound, final Runnable clickAction) {
        final float CLICK_VOLUME = 0.8f;

        return createClickableLabel(
            text,
            clickSound, CLICK_VOLUME,
            true,
            clickAction
        );
    }
}
