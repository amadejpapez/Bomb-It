package com.amadejpapez.bombit.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Be careful which 'GameConfig' class you use in a specific examples. This class is 'public' because
 * of demonstration purposes. In this way, you can use it in all packages inside gameplay and also in
 * Desktop Launcher for this example. But because it is set to 'public' you can also access it from
 * other examples in different packages. Be careful which class/package you include inside an example.
 */
public class GameConfig {

    public static final float WIDTH = 800f; // pixels
    public static final float HEIGHT = 600f;    // pixels

    public static final float HUD_WIDTH = 800f; // pixels
    public static final float HUD_HEIGHT = 600f;    // pixels

    public static final float WORLD_WIDTH = 80f;    // world units
    public static final float WORLD_HEIGHT = 60f;   // world units

    public static final Integer NUM_COLUMNS = 17;
    public static final Integer NUM_ROWS = 15;
    public static final Integer CELL_SIZE = 4;

    public static Integer MAX_NUMBER_BOMBS = 1;

    public static final float TIME_BOMB_ACTIVE = 3f;

    public static final List<Integer> PLAYER1_START = List.of(1, 1);

    public static final List<List<Integer>> LOC_SKIP_OBSTACLES = List.of(
            List.of(1, 2),
            List.of(2, 1)
    );

    public static final List<List<Integer>> LOC_FIXED_OBSTACLES = List.<List<Integer>>of(
            List.of(1, 11),
            List.of(10, 15),
            List.of(10, 7),
            List.of(10, 8),
            List.of(10, 9),
            List.of(11, 11),
            List.of(11, 13),
            List.of(11, 3),
            List.of(11, 5),
            List.of(12, 10),
            List.of(12, 11),
            List.of(12, 13),
            List.of(12, 14),
            List.of(12, 2),
            List.of(12, 3),
            List.of(12, 5),
            List.of(12, 6),
            List.of(12, 8),
            List.of(13, 5),
            List.of(2, 10),
            List.of(2, 11),
            List.of(2, 13),
            List.of(2, 14),
            List.of(2, 2),
            List.of(2, 3),
            List.of(2, 5),
            List.of(2, 6),
            List.of(2, 8),
            List.of(3, 11),
            List.of(3, 13),
            List.of(3, 3),
            List.of(3, 5),
            List.of(4, 1),
            List.of(4, 7),
            List.of(4, 8),
            List.of(4, 9),
            List.of(5, 11),
            List.of(5, 13),
            List.of(5, 3),
            List.of(5, 5),
            List.of(6, 11),
            List.of(6, 13),
            List.of(6, 14),
            List.of(6, 2),
            List.of(6, 3),
            List.of(6, 5),
            List.of(8, 11),
            List.of(8, 13),
            List.of(8, 14),
            List.of(8, 2),
            List.of(8, 3),
            List.of(8, 5),
            List.of(9, 11),
            List.of(9, 13),
            List.of(9, 3),
            List.of(9, 5)
    );

    private GameConfig() {
    }
}