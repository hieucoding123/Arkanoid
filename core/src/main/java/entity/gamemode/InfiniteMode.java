package entity.gamemode;

import entity.BricksMap;

import java.util.ArrayList;

public class InfiniteMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private BricksMap currentBricksMap;

    public InfiniteMode() {
        super();
        bricksMaps = new ArrayList<>();
        create();
    }

    @Override
    public void create() {
        for (int i = 0; i < 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        currentBricksMap =  bricksMaps.get(0);
    }

    @Override
    public void update() {
        currentBricksMap.update();
    }

    @Override
    public void render() {

    }

    @Override
    public void draw() {

    }
}
