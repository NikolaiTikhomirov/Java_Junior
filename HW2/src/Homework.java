import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Homework {
    @RandomDate
    private Date date;
    @RandomDate
    private LocalDate localDate;
    @RandomDate
    private LocalDateTime localDateTime;
    @RandomDate
    private Instant instant;

    public Homework() {
    }

    public Date getDate() {
        return date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public Instant getInstant() {
        return instant;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "date=" + date +
                ", localDate=" + localDate +
                ", localDateTime=" + localDateTime +
                ", instant=" + instant +
                '}';
    }
}
