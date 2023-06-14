package pl.aleh.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.aleh.util.DateTimeTransformers.parseOpenCloseTime;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.aleh.domain.clickandcollect.BusinessHours;

public class BusinessHoursParser {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("H['.'][':']mm");
  private final Map<String, DayOfWeek> daysMap;
  private final String daySeparator;

  private final Pattern patternDayTime;
  private final Pattern patternDaysTime;
  private final Pattern patternLunch;
  private final Pattern patternDays;
  private final Pattern patternDay;
  private final Pattern patternHours;

  protected BusinessHoursParser(
      String dayRegexp, String daySeparator, String timeRegexp,
      String dayTimeSeparator, String lunchRegexp, Map<String, DayOfWeek> daysMap
  ) {
    this.daysMap = daysMap;
    this.daySeparator = daySeparator;
    var daysRegexp = dayRegexp + daySeparator + dayRegexp;
    patternDayTime = Pattern.compile(dayRegexp + dayTimeSeparator + timeRegexp);
    patternDaysTime = Pattern.compile(daysRegexp + dayTimeSeparator + timeRegexp);
    patternLunch = Pattern.compile(lunchRegexp);
    patternDays = Pattern.compile(daysRegexp);
    patternDay = Pattern.compile(dayRegexp);
    patternHours = Pattern.compile(timeRegexp);
  }


  public List<BusinessHours> parseOpeningHours(final String openingHours) {
    var openingTime = StringUtils.deleteWhitespace(openingHours);
    var businessHours = new ArrayList<BusinessHours>();
    Optional<String[]> lunchHours = getLunchHours(openingTime);
    while (true) {
      // Check for day-day:hh:mm-hh:mm
      var matcher = patternDaysTime.matcher(openingTime);
      if (matcher.find()) {
        var parsedBusinessHours = getBusinessHoursForInterval(matcher, lunchHours);
        businessHours.addAll(parsedBusinessHours.getValue());
        openingTime = openingTime.replaceAll(parsedBusinessHours.getKey(), EMPTY);
      } else {
        // Check for day:hh:mm-hh:mm
        matcher = patternDayTime.matcher(openingTime);
        if (matcher.find()) {
          var parsedBusinessHours = getBusinessHoursForDay(matcher, lunchHours);
          businessHours.addAll(parsedBusinessHours.getValue());
          openingTime = openingTime.replaceAll(parsedBusinessHours.getKey(), EMPTY);
        } else {
          break;
        }
      }
    }
    return businessHours;
  }

  protected Pair<String, List<BusinessHours>> getBusinessHoursForDay(final Matcher matcher, final Optional<String[]> lunchHours) {
    var dayAndHours = matcher.group();
    var daysAndHoursArray = getDaysAndHours(dayAndHours);

    var day = daysMap.get(daysAndHoursArray[0]);
    var openHours = addZeroMinutes(daysAndHoursArray[1]);
    var closeHours = addZeroMinutes(daysAndHoursArray[2]);

    var businessHours = buildListOfBusinessHours(day, day, openHours, closeHours, lunchHours);
    return Pair.of(dayAndHours, businessHours);
  }

  protected Pair<String, List<BusinessHours>> getBusinessHoursForInterval(final Matcher matcher, final Optional<String[]> lunchHours) {
    var daysAndHours = matcher.group();
    var daysAndHoursArray = getDaysAndHours(daysAndHours);

    var startDay = daysMap.get(daysAndHoursArray[0]);
    var endDay = daysMap.get(daysAndHoursArray[1]);
    var openHours = addZeroMinutes(daysAndHoursArray[2]);
    var closeHours = addZeroMinutes(daysAndHoursArray[3]);

    var businessHours = buildListOfBusinessHours(startDay, endDay, openHours, closeHours, lunchHours);
    return Pair.of(daysAndHours, businessHours);
  }

  protected Optional<String[]> getLunchHours(final String openingTime) {
    var lunchMatcher = patternLunch.matcher(openingTime);
    if (lunchMatcher.find()) {
      var lunch = lunchMatcher.group();
      return extractLunchHours(lunch);
    }
    return Optional.empty();
  }

  private Optional<String[]> extractLunchHours(final String lunch) {
    var matcherHours = patternHours.matcher(lunch);
    if (matcherHours.find()) {
      return Optional.of(matcherHours.group().split(daySeparator));
    }
    return Optional.empty();
  }

  protected String[] getDaysAndHours(final String daysAndHours) {
    var matcherDays = patternDays.matcher(daysAndHours);
    var matcherDay = patternDay.matcher(daysAndHours);
    var matcherHours = patternHours.matcher(daysAndHours);
    var days = new String[2];
    var hours = new String[2];
    if (matcherDays.find()) {
      days = matcherDays.group().split(daySeparator);
    } else {
      if (matcherDay.find()) {
        days = new String[1];
        days[0] = matcherDay.group();
      }
    }
    if (matcherHours.find()) {
      hours = matcherHours.group().split(daySeparator);
    }
    return ArrayUtils.addAll(days, hours);
  }

  protected List<BusinessHours> buildListOfBusinessHours(
      final DayOfWeek startDay, final DayOfWeek endDay, final String openTime,
      final String closeTime
  ) {
    return buildListOfBusinessHours(startDay, endDay, openTime, closeTime, Optional.empty());
  }

  protected List<BusinessHours> buildListOfBusinessHours(
      final DayOfWeek startDay, final DayOfWeek endDay, final String openTime,
      final String closeTime, final Optional<String[]> lunchHours
  ) {
    if (lunchHours.isEmpty()) {
      return IntStream.rangeClosed(startDay.getValue(), endDay.getValue())
          .mapToObj(
              dayOfWeek -> new BusinessHours(dayOfWeek, parseOpenCloseTime(openTime, FORMATTER), parseOpenCloseTime(closeTime, FORMATTER)))
          .collect(Collectors.toList());
    } else {
      var hours = lunchHours.get();
      return IntStream.rangeClosed(startDay.getValue(), endDay.getValue())
          .mapToObj(
              dayOfWeek -> List.of(
                  new BusinessHours(dayOfWeek, parseOpenCloseTime(openTime, FORMATTER), parseOpenCloseTime(hours[0], FORMATTER)),
                  new BusinessHours(dayOfWeek, parseOpenCloseTime(hours[1], FORMATTER), parseOpenCloseTime(closeTime, FORMATTER))))
          .flatMap(List::stream)
          .collect(Collectors.toList());
    }
  }

  protected String addZeroMinutes(final String time) {
    return time;
  }
}
