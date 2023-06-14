package pl.aleh.courier.inpostpl.transformer;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import java.time.DayOfWeek;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InpostPlBusinessHoursParser extends InpostBusinessHoursParser {

  private static final String DAY_REGEXP = "(PN|PON|WT|ŚR|CZ|CZW|PT|SB|ND)";
  private static final Map<String, DayOfWeek> DAYS_MAP = Map.of("PN", MONDAY, "PON", MONDAY, "WT", TUESDAY,
      "ŚR", WEDNESDAY, "CZ", THURSDAY, "CZW", THURSDAY, "PT", FRIDAY, "SB", SATURDAY, "ND", SUNDAY);

  public InpostPlBusinessHoursParser() {
    super(DAY_REGEXP, DAYS_MAP);
  }
}
