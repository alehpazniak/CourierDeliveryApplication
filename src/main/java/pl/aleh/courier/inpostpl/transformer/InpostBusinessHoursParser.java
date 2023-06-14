package pl.aleh.courier.inpostpl.transformer;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import pl.aleh.domain.clickandcollect.BusinessHours;
import pl.aleh.util.BusinessHoursParser;

public class InpostBusinessHoursParser extends BusinessHoursParser {

  private static final String DAYS_SEPARATOR = "-";
  private static final String TIME_REGEXP = "(([0-9]{1,2}):([0-9]{2})-([0-9]{1,2}):([0-9]{2})|([0-9]{1,2})-([0-9]{1,"
      + "2}))";
  private static final String LUNCH_REGEXP = "unused";
  private static final Pattern TIME_WITH_MINUTE_PATTERN = Pattern.compile("([0-9]{1,2}):([0-9]{2})");

  protected InpostBusinessHoursParser(final String dayRegex, final Map<String, DayOfWeek> daysMap) {
    super(dayRegex, DAYS_SEPARATOR, TIME_REGEXP, EMPTY, LUNCH_REGEXP, daysMap);
  }

  protected InpostBusinessHoursParser(final String dayRegex, final Map<String, DayOfWeek> daysMap, final String lunchRegex,
      final String dayTimeSeparator, String timeRegexp) {
    super(dayRegex, DAYS_SEPARATOR, timeRegexp, dayTimeSeparator, lunchRegex, daysMap);
  }

  @Override
  public List<BusinessHours> parseOpeningHours(final String openingHours) {
    var openingTime = openingHours;
    if (openingTime == null) {
      return List.of();
    }
    openingTime = openingTime.replace(SPACE, EMPTY);
    if (openingTime.contains("24/7")) {
      return buildListOfBusinessHours(MONDAY, SUNDAY, "00:00", "23:59");
    }
    if (openingTime.matches(TIME_REGEXP)) {
      var hours = openingTime.split(DAYS_SEPARATOR);
      return buildListOfBusinessHours(MONDAY, SUNDAY, addZeroMinutes(hours[0]), addZeroMinutes(hours[1]));
    } else {
      return super.parseOpeningHours(openingTime);
    }
  }

  @Override
  protected String addZeroMinutes(final String time) {
    var matcher = TIME_WITH_MINUTE_PATTERN.matcher(time);
    if (matcher.find()) {
      return time;
    } else {
      return time + ":00";
    }
  }

}
