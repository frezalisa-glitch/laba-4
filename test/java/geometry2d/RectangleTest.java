package geometry2d;

import exceptions.InvalidFigureParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleTest {
    /** Значения в тестах порядка 1e-1…1e4, поэтому абсолютной погрешности 1e-9 достаточно. */
    private static final double EPSILON = 1e-9;

    private static final double[] INVALID_VALUES = {
            0.0, -0.0, -1.0, -1e-9, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
    };

    // ---------- № 2. Площадь и периметр ----------

    @Test
    void areaOfThreeByFourRectangleIsTwelve() {
        Rectangle rectangle = new Rectangle(3.0, 4.0);

        double actual = rectangle.area();

        assertEquals(12.0, actual, EPSILON);
    }

    @Test
    void perimeterOfThreeByFourRectangleIsFourteen() {
        Rectangle rectangle = new Rectangle(3.0, 4.0);

        double actual = rectangle.perimeter();

        assertEquals(14.0, actual, EPSILON);
    }

    @Test
    void areaOfSquareWithSideFiveIsTwentyFive() {
        assertEquals(25.0, new Rectangle(5.0, 5.0).area(), EPSILON);
    }

    @Test
    void perimeterOfSquareWithSideFiveIsTwenty() {
        assertEquals(20.0, new Rectangle(5.0, 5.0).perimeter(), EPSILON);
    }

    @Test
    void areaOfRectanglesWithDifferentSides() {
        assertAll(
                () -> assertEquals(1.5, new Rectangle(0.5, 3.0).area(), EPSILON),
                () -> assertEquals(70.0, new Rectangle(10.0, 7.0).area(), EPSILON),
                () -> assertEquals(2500.0, new Rectangle(100.0, 25.0).area(), EPSILON),
                () -> assertEquals(0.06, new Rectangle(0.2, 0.3).area(), EPSILON)
        );
    }

    @Test
    void perimeterOfRectanglesWithDifferentSides() {
        assertAll(
                () -> assertEquals(7.0, new Rectangle(0.5, 3.0).perimeter(), EPSILON),
                () -> assertEquals(34.0, new Rectangle(10.0, 7.0).perimeter(), EPSILON),
                () -> assertEquals(250.0, new Rectangle(100.0, 25.0).perimeter(), EPSILON),
                () -> assertEquals(1.0, new Rectangle(0.2, 0.3).perimeter(), EPSILON)
        );
    }

    @Test
    void areaAndPerimeterDoNotDependOnSideOrder() {
        assertAll(
                () -> assertEquals(12.0, new Rectangle(4.0, 3.0).area(), EPSILON),
                () -> assertEquals(14.0, new Rectangle(4.0, 3.0).perimeter(), EPSILON)
        );
    }

    // ---------- № 3. Конструктор ----------

    @Test
    void constructorAcceptsPositiveFiniteSides() {
        assertAll(
                () -> assertDoesNotThrow(() -> new Rectangle(3.0, 4.0)),
                () -> assertDoesNotThrow(() -> new Rectangle(1e-6, 1e-6)),
                () -> assertDoesNotThrow(() -> new Rectangle(Double.MIN_VALUE, 1.0)),
                () -> assertDoesNotThrow(() -> new Rectangle(1e6, Double.MAX_VALUE))
        );
    }

    @Test
    void constructorRejectsInvalidWidth() {
        for (double invalid : INVALID_VALUES) {
            assertThrows(InvalidFigureParameterException.class,
                    () -> new Rectangle(invalid, 4.0),
                    "ширина " + invalid + " должна быть отклонена");
        }
    }

    @Test
    void constructorRejectsInvalidHeight() {
        for (double invalid : INVALID_VALUES) {
            assertThrows(InvalidFigureParameterException.class,
                    () -> new Rectangle(3.0, invalid),
                    "высота " + invalid + " должна быть отклонена");
        }
    }

    @Test
    void constructorRejectsZeroWidth() {
        assertThrows(InvalidFigureParameterException.class, () -> new Rectangle(0.0, 4.0));
    }

    @Test
    void constructorRejectsNegativeHeight() {
        assertThrows(InvalidFigureParameterException.class, () -> new Rectangle(3.0, -4.0));
    }

    @Test
    void constructorRejectsNanWidth() {
        assertThrows(InvalidFigureParameterException.class, () -> new Rectangle(Double.NaN, 4.0));
    }

    @Test
    void constructorRejectsInfiniteHeight() {
        assertAll(
                () -> assertThrows(InvalidFigureParameterException.class,
                        () -> new Rectangle(3.0, Double.POSITIVE_INFINITY)),
                () -> assertThrows(InvalidFigureParameterException.class,
                        () -> new Rectangle(3.0, Double.NEGATIVE_INFINITY))
        );
    }

    // ---------- № 4. Геттеры, toString, equals, hashCode ----------

    @Test
    void gettersReturnConstructorArguments() {
        Rectangle rectangle = new Rectangle(2.5, 7.0);

        assertAll(
                () -> assertEquals(2.5, rectangle.getWidth()),
                () -> assertEquals(7.0, rectangle.getHeight())
        );
    }

    @Test
    void toStringContainsTypeAndSides() {
        String text = new Rectangle(2.5, 7.0).toString();

        assertAll(
                () -> assertTrue(text.contains("Rectangle"), text),
                () -> assertTrue(text.contains("width"), text),
                () -> assertTrue(text.contains("2.5"), text),
                () -> assertTrue(text.contains("height"), text),
                () -> assertTrue(text.contains("7.0"), text)
        );
    }

    @Test
    void rectanglesWithSameSidesAreEqualAndHaveSameHashCode() {
        Rectangle a = new Rectangle(3.0, 4.0);
        Rectangle b = new Rectangle(3.0, 4.0);

        assertAll(
                () -> assertEquals(a, b),
                () -> assertEquals(b, a),
                () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void equalsIsReflexive() {
        Rectangle a = new Rectangle(3.0, 4.0);

        assertEquals(a, a);
    }

    @Test
    void rectanglesWithDifferentSidesAreNotEqual() {
        Rectangle a = new Rectangle(3.0, 4.0);

        assertAll(
                () -> assertNotEquals(a, new Rectangle(3.0, 5.0)),
                () -> assertNotEquals(a, new Rectangle(5.0, 4.0)),
                () -> assertNotEquals(a, new Rectangle(4.0, 3.0))
        );
    }

    @Test
    void rectangleIsNotEqualToNullOrOtherType() {
        Rectangle a = new Rectangle(1.0, 1.0);

        assertAll(
                () -> assertNotEquals(null, a),
                () -> assertNotEquals(a, new Circle(1.0)),
                () -> assertNotEquals(a, "Rectangle(width=1.0, height=1.0)")
        );
    }
}
