import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  1. Создать аннотацию RandomDate со следующими возможностями:
 *  1.1 Если параметры не заданы, то в поле должна вставляться рандомная дата в диапазоне min, max.
 *  1.2 Аннотация должна работать с полем типа java.util.Date.
 *  1.3 Должна генерить дату в диапазоне [min, max)
 *  1.4 ** Научиться работать с полями LocalDateTime, LocalDate, Instant, ... (классы java.time.*)
 *
 *  Реализовать класс RandomDateProcessor по аналогии с RandomIntegerProcessor, который обрабатывает аннотацию.
 */

public class RandomDateProcessor {

    public static void run (Object obj) {
        for (Field declaredField : obj.getClass().getDeclaredFields()) {
            RandomDate annotation = declaredField.getDeclaredAnnotation(RandomDate.class);
            if (annotation != null){
                long min = annotation.min();
                long max = annotation.max();
                declaredField.setAccessible(true);
                long randomeDate = ThreadLocalRandom.current().nextLong(min, max);
                Date newRandomeDate = new Date(randomeDate);
                if (declaredField.getType().equals(Date.class)) {
                    try {
                        declaredField.set(obj, newRandomeDate);
                    } catch (IllegalAccessException e) {
                        System.err.println("Не удалось подставить рандомное значение: " + e);
                    }
                }
                if (declaredField.getType().equals(LocalDate.class)) {
                    try {
                        declaredField.set(obj, newRandomeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } catch (IllegalAccessException e) {
                        System.err.println("Не удалось подставить рандомное значение: " + e);
                    }
                }
                if (declaredField.getType().equals(LocalDateTime.class)) {
                    try {
                        declaredField.set(obj, LocalDateTime.ofInstant(newRandomeDate.toInstant(), ZoneId.systemDefault()));
                    } catch (IllegalAccessException e) {
                        System.err.println("Не удалось подставить рандомное значение: " + e);
                    }
                }
                if (declaredField.getType().equals(Instant.class)) {
                    try {
                        declaredField.set(obj, newRandomeDate.toInstant());
                    } catch (IllegalAccessException e) {
                        System.err.println("Не удалось подставить рандомное значение: " + e);
                    }
                }
            }
        }
    }
}
