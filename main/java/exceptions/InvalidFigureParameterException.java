package exceptions;

/**
 * Выбрасывается, когда фигуре передан недопустимый параметр:
 * ноль, отрицательное число, NaN, бесконечность или null.
 */
public class InvalidFigureParameterException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public InvalidFigureParameterException(String message) {
        super(message);
    }

    /**
     * Проверяет, что размер фигуры — конечное положительное число.
     *
     * @param name  название параметра для сообщения об ошибке
     * @param value проверяемое значение
     * @return то же значение, если оно допустимо
     */
    public static double requirePositiveFinite(String name, double value) {
        if (Double.isNaN(value)) {
            throw new InvalidFigureParameterException(name + " не может быть NaN");
        }
        if (Double.isInfinite(value)) {
            throw new InvalidFigureParameterException(name + " не может быть бесконечностью: " + value);
        }
        if (value <= 0) {
            throw new InvalidFigureParameterException(name + " должен быть больше нуля: " + value);
        }
        return value;
    }
}
