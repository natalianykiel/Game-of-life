package pl.gof;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;



public class GameOfLifeCell
        implements Observable, Serializable, Cloneable, Comparable<GameOfLifeCell> {
    private transient BooleanProperty value;

    private boolean easyValue;
    private final List<Observer> observers = new ArrayList<>();
    private List<GameOfLifeCell> neighbors = new ArrayList<>();

    public GameOfLifeCell() {
        Random rd = new Random();
        boolean initialValue = rd.nextBoolean();
        this.value = new SimpleBooleanProperty(initialValue);
        this.easyValue = initialValue; // Initializing easyValue
    }

    public void setNeighbors(List<GameOfLifeCell> neighbors) {
        this.neighbors = neighbors;
    }

    public List<GameOfLifeCell> getNeighbors() {
        return new ArrayList<>(neighbors);
    }

    public BooleanProperty cellValueProperty() {
        return value;
    }

    public boolean getCellValue() {
        return value.get();
    }

    public boolean nextState() {
        long aliveCount = neighbors.stream().filter(neighbor ->
                neighbor != null && neighbor.getCellValue()).count();

        if (value.get()) {
            return aliveCount >= 2 && aliveCount <= 3;
        } else {
            return aliveCount == 3;
        }
    }

    public void updateState(boolean newState) {
        value.set(newState);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.updateCellState(this);
        }
    }

    public List<Observer> getObservers() {
        return new ArrayList<>(observers);
    }

    @Override
    public String toString() {
        String result;
        result = "Cell -> Value: " + getCellValue();
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

        GameOfLifeCell otherCell = (GameOfLifeCell) obj;

        for (int i = 0; i < neighbors.size(); i++) {
            GameOfLifeCell neighbor = neighbors.get(i);
            GameOfLifeCell otherNeighbor = otherCell.neighbors.get(i);
            if (neighbor.getCellValue() != otherNeighbor.getCellValue()) {
                return false;
            }
        }

        return value.get() == otherCell.value.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.get());
    }

    @Override
    public GameOfLifeCell clone() {
        try {
            GameOfLifeCell clonedCell = (GameOfLifeCell) super.clone();

            clonedCell.neighbors = new ArrayList<>(this.neighbors.size());
            for (GameOfLifeCell neighbor : this.neighbors) {
                GameOfLifeCell neighborClone = new GameOfLifeCell();
                neighborClone.updateState(neighbor.getCellValue());
                clonedCell.neighbors.add(neighborClone);
            }
            return clonedCell;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int compareTo(GameOfLifeCell otherCell) {
        Objects.requireNonNull(otherCell, "Argument cannot be null");
        if (this.getCellValue() && !otherCell.getCellValue()) {
            return 1;
        } else if (!this.getCellValue() && otherCell.getCellValue()) {
            return -1;
        }
        return 0;
    }

    // Dodajemy zmienioną metodę writeObject() do obsługi poprawnego zapisu stanu
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(value.get()); // Zapis aktualnej wartości value
    }

    // Dodajemy zmienioną metodę readObject() do obsługi poprawnego odczytu stanu
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        boolean readValue = in.readBoolean(); // Odczyt wartości boolean z pliku
        value = new SimpleBooleanProperty(readValue); // Ustawienie wartości value
    }

}

