package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.Game;

public class PauseUI {
    private Stage stage;

    public PauseUI(SpriteBatch batch, final Game game) {
        Viewport viewport = new FitViewport(800, 1000);
        stage = new Stage(viewport, batch);
        Label.LabelStyle WhiteStyle = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")), Color.WHITE);
        Label resumeLabel = new Label("RESUME", WhiteStyle);
        Label newGameLabel = new Label("NEW GAME", WhiteStyle);
        resumeLabel.setFontScale(1.5f);
        newGameLabel.setFontScale(1.5f);
        resumeLabel.pack();
        newGameLabel.pack();

        resumeLabel.setPosition(
            (viewport.getWorldWidth() / 2f) - (resumeLabel.getWidth() / 2f),
            (viewport.getWorldHeight() / 2f) - (resumeLabel.getHeight() / 2f) + 50
        );

        resumeLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game != null) {
                    game.ContinueGame();
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                resumeLabel.setColor(Color.YELLOW);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                resumeLabel.setColor(Color.WHITE);
            }
        });

        newGameLabel.setPosition(
            (viewport.getWorldWidth() / 2f) - (newGameLabel.getWidth() / 2f),
            (viewport.getWorldHeight() / 2f) - (newGameLabel.getHeight() / 2f) - 50
        );

        newGameLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game != null) {
                    game.NewGame();
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                newGameLabel.setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                newGameLabel.setColor(Color.WHITE);
            }
        });

        stage.addActor(resumeLabel);
        stage.addActor(newGameLabel);
    }

    public Stage getStage() {
        return stage;
    }

    public void act(float delta) {
        if (stage != null) {
            stage.act(delta);
        }
    }

    public void render() {
        if (stage != null) {
            stage.getViewport().apply();
            stage.draw();
        }
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
