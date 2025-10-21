package Menu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.main.Main;

import static com.badlogic.gdx.Gdx.gl;

public class UserInterface extends ApplicationAdapter {
    private Stage stage;
    private Main main;
    private Skin skin;
    private Texture bg;
    private BitmapFont font;

    public UserInterface(Main main) {
        this.main = main;
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        font.dispose();
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
}
