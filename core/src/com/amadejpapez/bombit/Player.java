package com.amadejpapez.bombit;

import com.amadejpapez.bombit.config.GameConfig;
import com.amadejpapez.bombit.screen.GameScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    public String username;
    public Integer num;
    public List<Integer> position;
    public Integer numActiveBombs;
    public Integer maxNumOfBombs;
    public Boolean hasBonusHand;

    private Integer kills;
    public Integer tiles;

    public CellState image;
    public CellState imageTile;

    public List<List<CellActor>> cells;
    public Table grid;

    // used only for computer players
    public List<Integer> previousMoves;

    public static final List<List<Integer>> STARTING_POSITIONS = List.of(
            List.of(1, 1),
            List.of(1, 15),
            List.of(13, 1),
            List.of(13, 15)
    );

    Player(int num, CellState playerState) {
        // first player num should be zero!
        this.num = num;
        this.image = playerState;
        this.imageTile = CellActor.COLORED_TILES_MAPPED.get(playerState);
        this.kills = 0;
        this.tiles = 0;
        this.position = new ArrayList<>(STARTING_POSITIONS.get(num));
        this.numActiveBombs = 0;
        this.maxNumOfBombs = GameConfig.MAX_NUMBER_BOMBS_DEFAULT;
        this.cells = new ArrayList<>();
        this.grid = new Table();
        this.hasBonusHand = false;
        this.previousMoves = new ArrayList<>();
    }

    public void updateImage(CellState newImage) {
        this.image = newImage;
    }

    public int getKills() {
        return kills;
    }

    public boolean isComputerPlayer() {
        return !(num == 0 || (GameManager.INSTANCE.getNumPhysicalPlayers() == 2 && num == 1));
    }

    public void hit(int hitBy) {
        if (num == hitBy && isComputerPlayer())
            return;

        // move player to the start
        grid.getCell(cells.get(position.get(0)).get(position.get(1))).getActor().setState(CellState.EMPTY);
        this.position = new ArrayList<>(STARTING_POSITIONS.get(this.num));
        grid.getCell(cells.get(position.get(0)).get(position.get(1))).getActor().setState(image);

        if (!Objects.equals(num, hitBy))
            GameManager.players.get(hitBy).kills++;

        if (GameManager.INSTANCE.getGameMode().equals("TILE_TAG")) {
            unColorTiles();
            GameScreen.colorATile(position.get(0), position.get(1), this);
        }
    }

    public List<Integer> inputMoveGetNextCell(int keycode) {
        Integer nextRow = position.get(0);
        Integer nextCol = position.get(1);

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
            nextRow -= 1;
        else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            nextRow += 1;
        else if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
            nextCol -= 1;
        else nextCol += 1;

        return List.of(nextRow, nextCol);
    }

    public Boolean inputMove(int keycode) {
        List<Integer> nextCellPos = inputMoveGetNextCell(keycode);
        Integer nextRow = nextCellPos.get(0);
        Integer nextCol = nextCellPos.get(1);

        // only move if the future cell is "empty"
        CellActor nextCell = GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor();
        if (!nextCell.isEmpty()) {
            if (nextCell.getState() == CellState.BONUS_BOMB) {
                GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor().setState(CellState.EMPTY);
                maxNumOfBombs++;
            } else if (nextCell.getState() == CellState.BONUS_HAND) {
                GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor().setState(CellState.EMPTY);
                hasBonusHand = true;
            } else {
                return false;
            }
        }

        // player cannot move across bombs
        if (!Bomb.isBombCellEmpty(nextRow, nextCol)) {
            if (hasBonusHand)
                Bomb.handleBonusHand(nextRow, nextCol, keycode);
            return false;
        }

        grid.getCell(cells.get(position.get(0)).get(position.get(1))).getActor().setState(CellState.EMPTY);
        grid.getCell(cells.get(nextRow).get(nextCol)).getActor().setState(image);

        if (Objects.equals(GameManager.INSTANCE.getGameMode(), "TILE_TAG")) {
            GameScreen.colorATile(position.get(0), position.get(1), this);
            GameScreen.colorATile(nextRow, nextCol, this);
        }

        if (position.get(0).equals(nextRow))
            position.set(1, nextCol);
        else
            position.set(0, nextRow);

        if (isComputerPlayer()) {
            previousMoves.add(keycode);

            if (previousMoves.size() > 2)
                previousMoves.remove(0);
        }

        return true;
    }

    public void inputAddBomb() {
        if (numActiveBombs >= maxNumOfBombs)
            return;

        Bomb.grid.getCell(Bomb.cells.get(position.get(0)).get(position.get(1))).getActor().setState(CellState.BOMB);

        float time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        GameManager.activeBombs.add(new Bomb(num, new ArrayList<>(position), time));

        numActiveBombs++;
    }

    public void moveAi() {
        final List<Integer> possibleMoves = List.of(
                Input.Keys.UP,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.RIGHT
        );

        Integer randomMove = possibleMoves.get(ThreadLocalRandom.current().nextInt(possibleMoves.size()));

        Boolean hasMoved = inputMove(randomMove);

        // if player cannot move to the position
        if (!hasMoved) {
            List<Integer> nextCellPosition = inputMoveGetNextCell(randomMove);
            CellState nextState = GameScreen.cellGrid.getCell(GameScreen.cells.get(nextCellPosition.get(0)).get(nextCellPosition.get(1))).getActor().getState();

            // if next cell is a tmp obstacle or player, put a bomb
            if (CellActor.TMP_OBSTACLES.contains(nextState) || CellActor.PLAYERS.contains(nextState))
                inputAddBomb();
            // avoid a bomb
            else if (nextState == CellState.BOMB) {
                if (previousMoves.size() >= 2) {
                    inputMove(previousMoves.get(previousMoves.size() - 1));
                    inputMove(previousMoves.get(previousMoves.size() - 2));
                }
            }
            else
                moveAi();
        }
    }

    private void unColorTiles() {
        // used in Tile Tag, when a player dies
        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                if (GameScreen.bgCells.get(i).get(j).getState() == imageTile) {
                    GameScreen.bgCellGrid.getCell(GameScreen.bgCells.get(i).get(j)).getActor().setState(CellState.FLOOR);
                    tiles--;
                }
            }
        }
    }
}
