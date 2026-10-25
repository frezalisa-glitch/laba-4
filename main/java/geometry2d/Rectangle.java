package geometry2d;

import exceptions.InvalidFigureParameterException;

import java.util.Locale;
import java.util.Objects;

/** Прямоугольник с заданными шириной и высотой. */
public class Rectangle implements Figure {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = InvalidFigureParameterException.requirePositiveFinite("Ширина", width);
        this.height = InvalidFigureParameterException.requirePositiveFinite("Высота", height);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rectangle other)) {
            return false;
        }
        return Double.compare(width, other.width) == 0
                && Double.compare(height, other.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Rectangle(width=%s, height=%s)", width, height);
    }
}
