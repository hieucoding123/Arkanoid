package entity.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.BricksMap;
import entity.TextureManager;

import java.util.ArrayList;

public class InfiniteMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private BricksMap currentMap;

    public InfiniteMode() {
        super();
        bricksMaps = new ArrayList<>();
        create();
    }

    @Override
    public void create() {
        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        currentMap =  bricksMaps.get(0);
    }

    @Override
    public void update() {
        currentMap.update();
    }

    @Override
    public void render(SpriteBatch sp) {
        this.handleInput();
        this.update();
        this.draw(sp);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        currentMap.draw(sp);
    }
}
