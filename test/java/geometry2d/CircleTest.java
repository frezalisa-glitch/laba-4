package geometry2d;

import exceptions.InvalidFigureParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CircleTest {
    /** Ожидаемые значения записаны с 12 значащими цифрами, их величина не больше 1e3. */
    private static final double EPSILON = 1e-9;

    // ---------- № 2. Площадь и периметр ----------
    // Ожидаемые значения посчитаны заранее, вручную (π ≈ 3.14159265358979):
    // S = π·r², P = 2·π·r.

    @Test
    void areaOfUnitCircleIsPi() {
        assertEquals(3.14159265358979, new Circle(1.0).area(), EPSILON);
    }

    @Test
    void perimeterOfUnitCircleIsTwoPi() {
        assertEquals(6.28318530717959, new Circle(1.0).perimeter(), EPSILON);
    }

    @Test
    void areaOfCirclesWithDifferentRadii() {
        assertAll(
                () -> assertEquals(12.5663706143592, new Circle(2.0).area(), EPSILON),
                () -> assertEquals(19.6349540849362, new Circle(2.5).area(), EPSILON),
                () -> assertEquals(0.785398163397448, new Circle(0.5).area(), EPSILON),
                () -> assertEquals(314.159265358979, new Circle(10.0).area(), EPSILON)
        );
    }

    @Test
    void perimeterOfCirclesWithDifferentRadii() {
        assertAll(
                () -> assertEquals(12.5663706143592, new Circle(2.0).perimeter(), EPSILON),
                () -> assertEquals(15.7079632679490, new Circle(2.5).perimeter(), EPSILON),
                () -> assertEquals(3.14159265358979, new Circle(0.5).perimeter(), EPSILON),
                () -> assertEquals(62.8318530717959, new Circle(10.0).perimeter(), EPSILON)
        );
    }

    // ---------- № 3. Конструктор ----------

    @Test
    void constructorAcceptsPositiveFiniteRadius() {
        assertAll(
                () -> assertDoesNotThrow(() -> new Circle(1.0)),
                () -> assertDoesNotThrow(() -> new Circle(1e-6)),
                () -> assertDoesNotThrow(() -> new Circle(Double.MIN_VALUE)),
                () -> assertDoesNotThrow(() -> new Circle(1e9))
        );
    }

    @Test
    void constructorRejectsZeroRadius() {
        assertAll(
                () -> assertThrows(InvalidFigureParameterException.class, () -> new Circle(0.0)),
                () -> assertThrows(InvalidFigureParameterException.class, () -> new Circle(-0.0))
        );
    }

    @Test
    void constructorRejectsNegativeRadius() {
        assertAll(
                () -> assertThrows(InvalidFigureParameterException.class, () -> new Circle(-1.0)),
                () -> assertThrows(InvalidFigureParameterException.class, () -> new Circle(-1e-9))
        );
    }

    @Test
    void constructorRejectsNanRadius() {
        assertThrows(InvalidFigureParameterException.class, () -> new Circle(Double.NaN));
    }

    @Test
    void constructorRejectsPositiveInfinityRadius() {
        assertThrows(InvalidFigureParameterException.class, () -> new Circle(Double.POSITIVE_INFINITY));
    }

    @Test
    void constructorRejectsNegativeInfinityRadius() {
        assertThrows(InvalidFigureParameterException.class, () -> new Circle(Double.NEGATIVE_INFINITY));
    }

    // ---------- № 4. Геттеры, toString, equals, hashCode ----------

    @Test
    void getRadiusReturnsConstructorArgument() {
        assertAll(
                () -> assertEquals(2.5, new Circle(2.5).getRadius()),
                () -> assertEquals(0.001, new Circle(0.001).getRadius())
        );
    }

    @Test
    void toStringContainsTypeAndRadius() {
        String text = new Circle(2.5).toString();

        assertAll(
                () -> assertTrue(text.contains("Circle"), text),
                () -> assertTrue(text.contains("radius"), text),
                () -> assertTrue(text.contains("2.5"), text)
        );
    }

    @Test
    void circlesWithSameRadiusAreEqualAndHaveSameHashCode() {
        Circle a = new Circle(2.0);
        Circle b = new Circle(2.0);

        assertAll(
                () -> assertEquals(a, a),
                () -> assertEquals(a, b),
                () -> assertEquals(b, a),
                () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void circlesWithDifferentRadiusAreNotEqual() {
        assertNotEquals(new Circle(2.0), new Circle(2.000001));
    }

    @Test
    void circleIsNotEqualToNullOrOtherType() {
        Circle a = new Circle(1.0);

        assertAll(
                () -> assertNotEquals(null, a),
                () -> assertNotEquals(a, new Rectangle(1.0, 1.0)),
                () -> assertNotEquals(a, 1.0)
        );
    }
}
