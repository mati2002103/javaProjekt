package projekt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testowa sprawdzająca poprawność działania klasy Criteria.
 * Testuje konstruktor, metody dostępowe (gettery/settery), toString, equals i hashCode.
 */
public class CriteriaTest {

    /**
     * Testuje poprawność działania konstruktora oraz metod getName i getMaxPoints.
     *
     * @return brak zwracanej wartości — testuje oczekiwane działanie metod.
     */
    @Test
    public void testConstructorAndGetters() {
        // Tworzenie obiektu i sprawdzenie, czy gettery zwracają wartości ustawione przez konstruktor
        Criteria criteria = new Criteria("Projekt", 100);
        assertEquals("Projekt", criteria.getName(), "Nazwa powinna być ustawiona przez konstruktor");
        assertEquals(100, criteria.getMaxPoints(), "Maksymalna liczba punktów powinna być ustawiona przez konstruktor");
    }

    /**
     * Testuje metodę setName — sprawdza czy nazwa została poprawnie zmieniona.
     *
     * @return brak zwracanej wartości — testuje zmianę wartości nazwy kryterium.
     */
    @Test
    public void testSetNameChangesValue() {
        Criteria criteria = new Criteria("Egzamin", 50);
        criteria.setName("Kolokwium");
        assertEquals("Kolokwium", criteria.getName(), "Metoda setName powinna zmieniać nazwę kryterium");
    }

    /**
     * Testuje metodę setMaxPoints — sprawdza czy maksymalna liczba punktów została poprawnie zmieniona.
     *
     * @return brak zwracanej wartości — testuje aktualizację maksymalnej liczby punktów.
     */
    @Test
    public void testSetMaxPointsChangesValue() {
        Criteria criteria = new Criteria("Lab", 40);
        criteria.setMaxPoints(60);
        assertEquals(60, criteria.getMaxPoints(), "Metoda setMaxPoints powinna zmieniać maksymalną liczbę punktów");
    }

    /**
     * Testuje metodę toString — powinna zwracać nazwę kryterium.
     *
     * @return brak — testuje wynik metody toString.
     */
    @Test
    public void testToStringReturnsName() {
        Criteria criteria = new Criteria("Projekt", 100);
        assertEquals("Projekt", criteria.toString(), "Metoda toString powinna zwracać nazwę kryterium");
    }

    /**
     * Testuje metodę equals — porównanie dwóch obiektów o tej samej nazwie (różne wielkości liter).
     *
     * @return brak — testuje, czy equals działa bez rozróżniania wielkości liter.
     */
    @Test
    public void testEqualsSameNameDifferentCase() {
        Criteria c1 = new Criteria("Egzamin", 70);
        Criteria c2 = new Criteria("egzamin", 30);
        assertEquals(c1, c2, "equals powinno zwrócić true dla nazw różniących się tylko wielkością liter");
    }

    /**
     * Testuje metodę equals — porównanie dwóch różnych nazw kryteriów.
     *
     * @return brak — test sprawdzający rozróżnianie nazw.
     */
    @Test
    public void testEqualsDifferentName() {
        Criteria c1 = new Criteria("Projekt", 100);
        Criteria c2 = new Criteria("Kolokwium", 100);
        assertNotEquals(c1, c2, "equals powinno zwrócić false dla różnych nazw");
    }

    /**
     * Testuje metodę equals — ten sam obiekt porównywany do siebie powinien być równy.
     *
     * @return brak — testuje zachowanie equals przy porównaniu samego siebie.
     */
    @Test
    public void testEqualsSameObject() {
        Criteria c = new Criteria("Lab", 30);
        assertEquals(c, c, "equals powinno zwrócić true dla tego samego obiektu");
    }

    /**
     * Testuje metodę equals — porównanie z null powinno zwrócić false.
     *
     * @return brak — sprawdzenie zachowania equals przy porównaniu z null.
     */
    @Test
    public void testEqualsNull() {
        Criteria c = new Criteria("Lab", 30);
        assertNotEquals(null, c, "equals powinno zwrócić false przy porównaniu z null");
    }

    /**
     * Testuje metodę equals — porównanie z obiektem innej klasy powinno zwrócić false.
     *
     * @return brak — sprawdza czy equals działa poprawnie przy innej klasie.
     */
    @Test
    public void testEqualsDifferentClass() {
        Criteria c = new Criteria("Lab", 30);
        assertNotEquals("Lab", c, "equals powinno zwrócić false przy porównaniu z inną klasą");
    }

    /**
     * Testuje zgodność metod equals i hashCode — te same nazwy (niezależnie od wielkości liter) powinny mieć ten sam hashCode.
     *
     * @return brak — testuje spójność equals i hashCode.
     */
    @Test
    public void testHashCodeConsistentWithEquals() {
        Criteria c1 = new Criteria("Test", 10);
        Criteria c2 = new Criteria("test", 50);
        assertEquals(c1.hashCode(), c2.hashCode(), "hashCode powinien być taki sam dla równych obiektów (case-insensitive)");
    }

    /**
     * Testuje hashCode — różne nazwy powinny generować różne wartości hashCode.
     *
     * @return brak — test sprawdzający rozróżnialność hashCode.
     */
    @Test
    public void testHashCodeDifferentForDifferentNames() {
        Criteria c1 = new Criteria("A", 10);
        Criteria c2 = new Criteria("B", 10);
        assertNotEquals(c1.hashCode(), c2.hashCode(), "hashCode powinien być różny dla różnych nazw");
    }

    /**
     * Testuje kombinację setName i setMaxPoints — oba atrybuty powinny być poprawnie zmienione.
     *
     * @return brak — testuje jednoczesną aktualizację nazwy i punktów.
     */
    @Test
    public void testMultipleChanges() {
        Criteria c = new Criteria("Zaliczenie", 25);
        c.setName("Egzamin końcowy");
        c.setMaxPoints(80);
        assertEquals("Egzamin końcowy", c.getName(), "Po zmianie nazwa powinna być nowa");
        assertEquals(80, c.getMaxPoints(), "Po zmianie liczba punktów powinna być nowa");
    }
}
