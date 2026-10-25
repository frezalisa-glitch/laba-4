package geometry3d;

import exceptions.InvalidFigureParameterException;
import geometry2d.Circle;
import geometry2d.Figure;
import geometry2d.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CylinderTest {
    /** Объёмы в тестах не больше 1e4, абсолютной погрешности 1e-9 достаточно. */
    private static final double EPSILON = 1e-9;

    private static final double[] INVALID_VALUES = {
            0.0, -0.0, -1.0, -1e-9, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
    };

    // ---------- № 4.1. Объём ----------
    // Ожидаемый объём посчитан вручную: V = S_основания · h,
    // для круга S = π·r² (π ≈ 3.14159265358979), для прямоугольника S = a·b.

    @Test
    void volumeOfCylinderWithCircleBase() {
        assertAll(
                () -> assertEquals(3.14159265358979, new Cylinder(new Circle(1.0), 1.0).volume(), EPSILON),
                () -> assertEquals(31.4159265358979, new Cylinder(new Circle(1.0), 10.0).volume(), EPSILON),
                () -> assertEquals(62.8318530717959, new Cylinder(new Circle(2.0), 5.0).volume(), EPSILON),
                () -> assertEquals(0.392699081698724, new Cylinder(new Circle(0.5), 0.5).volume(), EPSILON)
        );
    }

    @Test
    void volumeOfCylinderWithRectangleBase() {
        assertAll(
                () -> assertEquals(24.0, new Cylinder(new Rectangle(2.0, 3.0), 4.0).volume(), EPSILON),
                () -> assertEquals(1.0, new Cylinder(new Rectangle(1.0, 1.0), 1.0).volume(), EPSILON),
                () -> assertEquals(1250.0, new Cylinder(new Rectangle(5.0, 10.0), 25.0).volume(), EPSILON),
                () -> assertEquals(0.15, new Cylinder(new Rectangle(0.5, 0.6), 0.5).volume(), EPSILON)
        );
    }

    @Test
    void volumeOfCylinderWithSquareBase() {
        assertEquals(1000.0, new Cylinder(new Rectangle(10.0, 10.0), 10.0).volume(), EPSILON);
    }

    @Test
    void volumeWithCustomBaseUsesBaseArea() {
        // Основание с заранее известной площадью 7 — объём не зависит от формулы конкретной фигуры.
        Figure base = new Figure() {
            @Override
            public double area() {
                return 7.0;
            }

            @Override
            public double perimeter() {
                return 11.0;
            }
        };

        assertEquals(21.0, new Cylinder(base, 3.0).volume(), EPSILON);
    }

    // ---------- № 3. Конструктор ----------

    @Test
    void constructorAcceptsValidParameters() {
        assertAll(
                () -> assertDoesNotThrow(() -> new Cylinder(new Circle(1.0), 1.0)),
                () -> assertDoesNotThrow(() -> new Cylinder(new Rectangle(2.0, 3.0), 4.0)),
                () -> assertDoesNotThrow(() -> new Cylinder(new Circle(1.0), Double.MIN_VALUE)),
                () -> assertDoesNotThrow(() -> new Cylinder(new Circle(1.0), 1e9))
        );
    }

    @Test
    void constructorRejectsInvalidHeightWithCircleBase() {
        Circle base = new Circle(1.0);
        for (double invalid : INVALID_VALUES) {
            assertThrows(InvalidFigureParameterException.class,
                    () -> new Cylinder(base, invalid),
                    "высота " + invalid + " должна быть отклонена");
        }
    }

    @Test
    void constructorRejectsInvalidHeightWithRectangleBase() {
        Rectangle base = new Rectangle(2.0, 3.0);
        for (double invalid : INVALID_VALUES) {
            assertThrows(InvalidFigureParameterException.class,
                    () -> new Cylinder(base, invalid),
                    "высота " + invalid + " должна быть отклонена");
        }
    }

    @Test
    void constructorRejectsZeroHeight() {
        assertThrows(InvalidFigureParameterException.class, () -> new Cylinder(new Circle(1.0), 0.0));
    }

    @Test
    void constructorRejectsNegativeHeight() {
        assertThrows(InvalidFigureParameterException.class, () -> new Cylinder(new Circle(1.0), -5.0));
    }

    @Test
    void constructorRejectsNanHeight() {
        assertThrows(InvalidFigureParameterException.class, () -> new Cylinder(new Circle(1.0), Double.NaN));
    }

    @Test
    void constructorRejectsInfiniteHeight() {
        assertAll(
                () -> assertThrows(InvalidFigureParameterException.class,
                        () -> new Cylinder(new Circle(1.0), Double.POSITIVE_INFINITY)),
                () -> assertThrows(InvalidFigureParameterException.class,
                        () -> new Cylinder(new Circle(1.0), Double.NEGATIVE_INFINITY))
        );
    }

    @Test
    void constructorRejectsNullBase() {
        assertThrows(InvalidFigureParameterException.class, () -> new Cylinder(null, 5.0));
    }

    // ---------- № 4.2–4.4. Геттеры, toString, equals, hashCode ----------

    @Test
    void getBaseReturnsSameObjectPassedToConstructor() {
        Circle circle = new Circle(1.5);
        Rectangle rectangle = new Rectangle(2.0, 3.0);

        assertAll(
                () -> assertSame(circle, new Cylinder(circle, 2.0).getBase()),
                () -> assertSame(rectangle, new Cylinder(rectangle, 2.0).getBase())
        );
    }

    @Test
    void getHeightReturnsConstructorArgument() {
        assertAll(
                () -> assertEquals(2.0, new Cylinder(new Circle(1.0), 2.0).getHeight()),
                () -> assertEquals(0.75, new Cylinder(new Rectangle(1.0, 2.0), 0.75).getHeight())
        );
    }

    @Test
    void toStringContainsTypeBaseAndHeight() {
        String text = new Cylinder(new Circle(1.5), 4.0).toString();

        assertAll(
                () -> assertTrue(text.contains("Cylinder"), text),
                () -> assertTrue(text.contains("Circle"), text),
                () -> assertTrue(text.contains("1.5"), text),
                () -> assertTrue(text.contains("height"), text),
                () -> assertTrue(text.contains("4.0"), text)
        );
    }

    @Test
    void toStringContainsRectangleBaseParameters() {
        String text = new Cylinder(new Rectangle(2.0, 3.0), 4.5).toString();

        assertAll(
                () -> assertTrue(text.contains("Cylinder"), text),
                () -> assertTrue(text.contains("Rectangle"), text),
                () -> assertTrue(text.contains("2.0"), text),
                () -> assertTrue(text.contains("3.0"), text),
                () -> assertTrue(text.contains("4.5"), text)
        );
    }

    @Test
    void cylindersWithEqualBaseAndHeightAreEqualAndHaveSameHashCode() {
        Cylinder a = new Cylinder(new Circle(1.0), 2.0);
        Cylinder b = new Cylinder(new Circle(1.0), 2.0);

        assertAll(
                () -> assertEquals(a, a),
                () -> assertEquals(a, b),
                () -> assertEquals(b, a),
                () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void cylindersWithDifferentBaseOrHeightAreNotEqual() {
        Cylinder a = new Cylinder(new Circle(1.0), 2.0);

        assertAll(
                () -> assertNotEquals(a, new Cylinder(new Circle(1.0), 3.0)),
                () -> assertNotEquals(a, new Cylinder(new Circle(2.0), 2.0)),
                () -> assertNotEquals(a, new Cylinder(new Rectangle(1.0, 1.0), 2.0))
        );
    }

    @Test
    void cylinderIsNotEqualToNullOrOtherType() {
        Cylinder a = new Cylinder(new Circle(1.0), 2.0);

        assertAll(
                () -> assertNotEquals(null, a),
                () -> assertNotEquals(a, new Circle(1.0))
        );
    }
}
