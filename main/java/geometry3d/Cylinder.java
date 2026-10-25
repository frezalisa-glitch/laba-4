package geometry3d;

import exceptions.InvalidFigureParameterException;
import geometry2d.Figure;

import java.util.Locale;
import java.util.Objects;

/** Обобщённый цилиндр: плоское основание любой формы и высота. */
public class Cylinder {

    private final Figure base;
    private final double height;

    public Cylinder(Figure base, double height) {
        if (base == null) {
            throw new InvalidFigureParameterException("Основание цилиндра не может быть null");
        }
        this.base = base;
        this.height = InvalidFigureParameterException.requirePositiveFinite("Высота цилиндра", height);
    }

    public Figure getBase() {
        return base;
    }

    public double getHeight() {
        return height;
    }

    /** Объём цилиндра: площадь основания, умноженная на высоту. */
    public double volume() {
        return base.area() * height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cylinder other)) {
            return false;
        }
        return base.equals(other.base) && Double.compare(height, other.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, height);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Cylinder(base=%s, height=%s)", base, height);
    }
}
