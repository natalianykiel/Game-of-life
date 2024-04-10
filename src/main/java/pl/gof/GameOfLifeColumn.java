package pl.gof;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class GameOfLifeColumn extends GameOfLifeCellGroup {
    public GameOfLifeColumn(List<GameOfLifeCell> cells) {
        super(cells);
    }

    @Override
    public String toString() {
        String result;
        result = "Column -> Alive: " + getAliveCount() + ", Dead: " + getDeadCount();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        GameOfLifeColumn otherColumn = (GameOfLifeColumn) obj;

        if (cells.size() != otherColumn.cells.size()) {
            return false;
        }

        for (int i = 0; cells.size() > i; i++) {
            if (cells.get(i).getCellValue() != otherColumn.cells.get(i).getCellValue()) {
                return false;
            }
        }

        return aliveCount == otherColumn.aliveCount && deadCount == otherColumn.deadCount;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5,9).append(aliveCount)
                .append(deadCount).append(cells).toHashCode();
    }

    @Override
    public GameOfLifeColumn clone() {
        List<GameOfLifeCell> clonedCells = new ArrayList<>();
        for (GameOfLifeCell cell : cells) {
            GameOfLifeCell clonedCell = new GameOfLifeCell();
            clonedCell.updateState(cell.getCellValue());
            clonedCells.add(clonedCell);
        }
        return new GameOfLifeColumn(clonedCells);
    }
}