package pl.gof;

import java.util.List;



public abstract class GameOfLifeCellGroup implements Observer, Cloneable {
    protected List<GameOfLifeCell> cells;

    int aliveCount;
    int deadCount;

    public GameOfLifeCellGroup(List<GameOfLifeCell> cells) {
        this.cells = cells;
        for (GameOfLifeCell cell : cells) {
            cell.addObserver(this); // Add this group as an observer for cells.
            updateCellStateStart(cell); // Initialize counts based on the initial state.
        }
    }

    public int getAliveCount() {
        return aliveCount;
    }

    public int getDeadCount() {
        return deadCount;
    }

    // Count alive and dead cells in the initial state.
    private void updateCellStateStart(GameOfLifeCell cell) {
        if (cell.getCellValue()) {
            aliveCount++;
        } else {
            deadCount++;
        }
    }

    @Override
    public void updateCellState(GameOfLifeCell cell) {
        // Check the state of the updated cell and adjust counts accordingly.
        if (cell.getCellValue()) {
            aliveCount++;
            deadCount--;
        } else {
            deadCount++;
            aliveCount--;
        }
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract GameOfLifeCellGroup clone();


}

