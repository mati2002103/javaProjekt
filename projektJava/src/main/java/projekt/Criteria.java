package projekt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca kryterium oceniania dla przedmiotu,
 * zawierająca nazwę kryterium oraz maksymalną możliwą liczbę punktów.
 * 
 * @author Okoniecki Rafał
 */
public class Criteria {

    /** Nazwa kryterium, np. "Projekt", "Egzamin" */
    private String name;

    /** Maksymalna liczba punktów możliwa do zdobycia */
    private int maxPoints;

    /**
     * Konstruktor inicjalizujący kryterium na podstawie nazwy i maks. punktów.
     *
     * @param name      nazwa kryterium
     * @param maxPoints maksymalna liczba punktów
     */
    public Criteria(String name, int maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
    }

    /**
     * Zwraca nazwę kryterium.
     *
     * @return nazwa kryterium
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwę kryterium.
     *
     * @param name nowa nazwa kryterium
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca maksymalną liczbę punktów.
     *
     * @return maksymalna liczba punktów
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Ustawia maksymalną liczbę punktów.
     *
     * @param maxPoints nowa maksymalna liczba punktów
     */
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Zwraca nazwę kryterium jako reprezentację tekstową.
     *
     * @return tekstowa reprezentacja kryterium
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria criteria = (Criteria) o;
        return name.equalsIgnoreCase(criteria.name);
    }

    @Override
    public int hashCode() {
    	return Objects.hash(name.toLowerCase());
    }
}