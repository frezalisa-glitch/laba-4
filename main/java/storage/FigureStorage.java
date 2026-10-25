package storage;

import exceptions.InvalidFigureParameterException;
import geometry2d.Circle;
import geometry2d.Figure;
import geometry2d.Rectangle;
import geometry3d.Cylinder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Чтение фигур из текстового файла и сохранение результатов расчётов.
 *
 * <p>Формат входного файла, одна фигура в строке:
 * <pre>
 * CIRCLE &lt;радиус&gt;
 * RECTANGLE &lt;ширина&gt; &lt;высота&gt;
 * CYLINDER CIRCLE &lt;радиус&gt; &lt;высота&gt;
 * CYLINDER RECTANGLE &lt;ширина&gt; &lt;высота основания&gt; &lt;высота&gt;
 * </pre>
 * Пустые строки и строки, начинающиеся с {@code #}, пропускаются.
 */
public final class FigureStorage {

    /** Результат чтения файла: плоские фигуры, цилиндры и ошибки разбора. */
    public record LoadResult(List<Figure> figures, List<Cylinder> cylinders, List<String> errors) {
    }

    private FigureStorage() {
    }

    public static LoadResult load(Path file) throws IOException {
        List<Figure> figures = new ArrayList<>();
        List<Cylinder> cylinders = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).strip();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            try {
                String[] parts = line.split("\\s+");
                if (parts[0].equalsIgnoreCase("CYLINDER")) {
                    cylinders.add(parseCylinder(parts));
                } else {
                    figures.add(parseFigure(parts, 0, parts.length));
                }
            } catch (InvalidFigureParameterException | IllegalStateException e) {
                errors.add("Строка " + (i + 1) + " («" + line + "»): " + e.getMessage());
            }
        }
        return new LoadResult(figures, cylinders, errors);
    }

    public static void save(Path file, List<String> lines) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private static Cylinder parseCylinder(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalStateException("для цилиндра нужны тип основания, его размеры и высота");
        }
        Figure base = parseFigure(parts, 1, parts.length - 1);
        double height = parseNumber(parts[parts.length - 1]);
        return new Cylinder(base, height);
    }

    /** Разбирает фигуру из parts[from..to). */
    private static Figure parseFigure(String[] parts, int from, int to) {
        String type = parts[from].toUpperCase(Locale.ROOT);
        int argc = to - from - 1;
        return switch (type) {
            case "CIRCLE" -> {
                expectArgs(type, argc, 1);
                yield new Circle(parseNumber(parts[from + 1]));
            }
            case "RECTANGLE" -> {
                expectArgs(type, argc, 2);
                yield new Rectangle(parseNumber(parts[from + 1]), parseNumber(parts[from + 2]));
            }
            default -> throw new IllegalStateException("неизвестный тип фигуры " + parts[from]);
        };
    }

    private static void expectArgs(String type, int actual, int expected) {
        if (actual != expected) {
            throw new IllegalStateException(type + " ожидает параметров: " + expected + ", получено: " + actual);
        }
    }

    private static double parseNumber(String text) {
        try {
            return Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("не число: " + text);
        }
    }
}
