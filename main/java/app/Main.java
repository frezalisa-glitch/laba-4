package app;

import geometry2d.Figure;
import geometry3d.Cylinder;
import storage.FigureStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Читает фигуры из файла, считает площади, периметры и объёмы
 * и сохраняет отчёт в файл.
 *
 * <p>Запуск: {@code java -jar java-labs-1.0.0.jar [входной файл] [файл результатов]}.
 * По умолчанию {@code data/figures.txt} и {@code data/results.txt}.
 */
public class Main {

    public static void main(String[] args) {
        Path input = Path.of(args.length > 0 ? args[0] : "data/figures.txt");
        Path output = Path.of(args.length > 1 ? args[1] : "data/results.txt");

        FigureStorage.LoadResult result;
        try {
            result = FigureStorage.load(input);
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл " + input + ": " + e.getMessage());
            System.exit(1);
            return;
        }

        List<String> report = new ArrayList<>();
        report.add("Плоские фигуры:");
        for (Figure figure : result.figures()) {
            report.add(String.format(Locale.ROOT, "  %s: площадь = %.4f, периметр = %.4f",
                    figure, figure.area(), figure.perimeter()));
        }
        report.add("Цилиндры:");
        for (Cylinder cylinder : result.cylinders()) {
            report.add(String.format(Locale.ROOT, "  %s: объём = %.4f", cylinder, cylinder.volume()));
        }
        if (!result.errors().isEmpty()) {
            report.add("Пропущенные строки:");
            for (String error : result.errors()) {
                report.add("  " + error);
            }
        }

        report.forEach(System.out::println);

        try {
            FigureStorage.save(output, report);
            System.out.println("Результаты сохранены в " + output);
        } catch (IOException e) {
            System.err.println("Не удалось сохранить файл " + output + ": " + e.getMessage());
            System.exit(1);
        }
    }
}
