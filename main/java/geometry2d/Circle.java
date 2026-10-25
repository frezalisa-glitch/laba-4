package geometry2d;

import exceptions.InvalidFigureParameterException;

import java.util.Locale;

/** Круг с заданным радиусом. */
public class Circle implements Figure {

    private final double radius;

    public Circle(double radius) {
        this.radius = InvalidFigureParameterException.requirePositiveFinite("Радиус", radius);
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Circle other)) {
            return false;
        }
        return Double.compare(radius, other.radius) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(radius);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Circle(radius=%s)", radius);
    }
}
