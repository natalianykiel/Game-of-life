package pl.gof;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;




public class GameOfLifeRow extends GameOfLifeCellGroup {
    public GameOfLifeRow(List<GameOfLifeCell> cells) {

        super(cells);
    }

    @Override
    public String toString() {
        String result;
        result = "Row -> Alive: " + getAliveCount() + ", Dead: " + getDeadCount();
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

        GameOfLifeRow otherRow = (GameOfLifeRow) obj;

        if (cells.size() != otherRow.cells.size()) {
            return false;
        }


        for (int i = 0; cells.size() > i; i++) {
            if (cells.get(i).getCellValue() != otherRow.cells.get(i).getCellValue()) {
                return false;
            }
        }

        return aliveCount == otherRow.aliveCount || deadCount == otherRow.deadCount;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5,9).append(aliveCount).append(deadCount)
                .append(cells).toHashCode();
    }

    @Override
    public GameOfLifeRow clone() {
            List<GameOfLifeCell> clonedCells = new ArrayList<>();
            for (GameOfLifeCell cell : cells) {
                GameOfLifeCell clonedCell = cell.clone();
                clonedCells.add(clonedCell);
            }
            return new GameOfLifeRow(clonedCells);
    }
}