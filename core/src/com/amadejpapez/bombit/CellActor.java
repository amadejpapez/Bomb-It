package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class CellActor extends Image {

    private CellState state;

    public static final List<CellState> PLAYERS = List.of(
            CellState.PLAYER_BLACK,
            CellState.PLAYER_BLUE,
            CellState.PLAYER_GREEN,
            CellState.PLAYER_ORANGE,
            CellState.PLAYER_PINK,
            CellState.PLAYER_PURPLE
    );
    public static final List<CellState> TMP_OBSTACLES = List.of(
            CellState.POM_PINK,
            CellState.POM_YELLOW,
            CellState.WHISTLE
    );
    public static final List<CellState> FIXED_OBSTACLES = List.of(
            CellState.CONE_BLUE,
            CellState.CONE_RED
    );

    public CellActor(CellState state) {
        super(getImageFromState(state));
        this.state = state;
    }

    public void setState(CellState state) {
        this.state = state;
        setDrawable(getImageFromState(state));
    }

    public CellState getState() {
        return state;
    }

    private void setDrawable(TextureRegion region) {
        super.setDrawable(new TextureRegionDrawable(region));

        if (region == Assets.bomb)
            addAnimation();
    }

    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }

    private void addAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.scaleTo(0, 0, 0f),
                        Actions.scaleTo(1, 1, 0.12f)
                )
        );
    }

    public static TextureRegion getImageFromState(CellState state) {
        switch (state) {
            case BOMB:
                return Assets.bomb;
            case BONUS_BOMB:
                return Assets.bonusBombs;
            case BONUS_HAND:
                return Assets.bonusHand;
            case CONE_BLUE:
                return Assets.coneBlue;
            case CONE_RED:
                return Assets.coneRed;
            case FLOOR:
                return Assets.floor;
            case PLAYER_BLACK:
                return Assets.playerBlack;
            case PLAYER_BLUE:
                return Assets.playerBlue;
            case PLAYER_GREEN:
                return Assets.playerGreen;
            case PLAYER_ORANGE:
                return Assets.playerOrange;
            case PLAYER_PINK:
                return Assets.playerPink;
            case PLAYER_PURPLE:
                return Assets.playerPurple;
            case POM_PINK:
                return Assets.pomPink;
            case POM_YELLOW:
                return Assets.pomYellow;
            case TILE_BLACK:
                return Assets.tileBlack;
            case TILE_BLUE:
                return Assets.tileBlue;
            case TILE_GREEN:
                return Assets.tileGreen;
            case TILE_ORANGE:
                return Assets.tileOrange;
            case TILE_PINK:
                return Assets.tilePink;
            case TILE_PURPLE:
                return Assets.tilePurple;
            case WHISTLE:
                return Assets.whistle;
            default:
                return Assets.empty;
        }
    }
}