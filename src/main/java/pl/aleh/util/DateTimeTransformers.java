package pl.aleh.util;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.experimental.UtilityClass;
import pl.aleh.exception.CourierInternalException;

@UtilityClass
public class DateTimeTransformers {

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_SS_S = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S][.]";
  public static final DateTimeFormatter SQL_FORMAT = ofPattern(YYYY_MM_DD_HH_MM_SS);
  public static final DateTimeFormatter FORMAT_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  public static final DateTimeFormatter FORMAT_DDMMYYYYHHMM = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
  public static final DateTimeFormatter FORMAT_DATE_SPACE_TIME_ISO = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
  public static final DateTimeFormatter FORMAT_DDMMYYYY_HHMMSS = DateTimeFormatter.ofPattern(
      "dd-MM-yyyy HH:mm:ss[.SSS][.SS][.S][.]");
  public static final DateTimeFormatter FORMAT_DDMMYYYY_HHMM = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
  public static final DateTimeFormatter FORMAT_DDMMYYYY_HHMM_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  public static final DateTimeFormatter FORMAT_YYYYMMDDHHMM = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
  public static final DateTimeFormatter FORMAT_DD_MM_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  public static final DateTimeFormatter FORMAT_MMDDYYHHMM = DateTimeFormatter.ofPattern("MMddyyHHmm");
  public static final DateTimeFormatter FORMAT_YYYYMMDD_HHMMSS = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
  public static final DateTimeFormatter FORMAT_YYYYMMDD_HHMM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  public static final DateTimeFormatter FORMAT_MMMDDYYYY = DateTimeFormatter.ofPattern("MMM-dd-yyyy");
  public static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
  public static final DateTimeFormatter FORMAT_HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm[:ss]");
  public static final DateTimeFormatter YYYY_MM_DD_T_HH_MM_SS_Z = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  /**
   * This is the contracted datetime format for Violet to receive for tracking data
   *
   * @param instant - the datetime as an Instant
   * @return - Sql format String datetime in UTC
   */
  public static String instantToSqlFormat(final Instant instant) {
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).format(SQL_FORMAT);
  }

  public static OffsetDateTime formatToOffsetUtc(final String dateTime, final DateTimeFormatter formatter) {
    return toUtcInstant(dateTime, formatter).atOffset(ZoneOffset.UTC);
  }

  public static String format(final Instant instant, final DateTimeFormatter formatter) {
    return formatter.format(instant);
  }

  public static OffsetDateTime formatToOffsetUtc(
      final String dateTime, final DateTimeFormatter formatter, ZoneId zoneId
  ) {
    return toUtcInstant(dateTime, formatter, zoneId).atOffset(ZoneOffset.UTC);
  }

  public static Instant toUtcInstant(final String dateTime) {
    return dateTime == null ? null : toUtcInstant(LocalDateTime.parse(dateTime));
  }

  public static Instant toUtcInstant(final LocalDateTime localDateTime) {
    return localDateTime == null ? null : localDateTime.toInstant(ZoneOffset.UTC);
  }

  public static Instant toUtcInstant(final String dateTime, final DateTimeFormatter formatter) {
    return LocalDateTime.parse(dateTime, formatter).toInstant(ZoneOffset.UTC);
  }

  public static Instant toUtcInstant(final String dateTime, final DateTimeFormatter formatter, final ZoneId zoneId) {
    return LocalDateTime.parse(dateTime, formatter).atZone(zoneId).withZoneSameInstant(ZoneOffset.UTC).toInstant();
  }

  public static Instant isoToInstant(final String eventDate) {
    return OffsetDateTime.parse(eventDate).toInstant();
  }

  public static OffsetDateTime instantToOffset(final Instant date) {
    return date.atOffset(ZoneOffset.UTC);
  }

  public static Date getMidnightDateFromXmlGregorianCalendar(final XMLGregorianCalendar calendar) {
    var c = new GregorianCalendar(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
    // this bit is crucial to make sure we add timezone offset for proper conversion of the dates
    // to JSON: yyyy-MM-dd format which uses system default timezone
    c.add(Calendar.MILLISECOND, TimeZone.getDefault().getOffset(calendar.toGregorianCalendar().getTimeInMillis()));
    return c.getTime();
  }

  public static XMLGregorianCalendar getCurrentXmlGregorianCalendarDate(final ZoneId zoneId) {
    return toXmlGregorianCalendarDate(Instant.now(), zoneId);
  }

  public static XMLGregorianCalendar toXmlGregorianCalendarDate(final Instant instant, final ZoneId zoneId) {
    var dateTime = instant.atZone(zoneId);
    var gregorianCalendar = GregorianCalendar.from(dateTime);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      throw new CourierInternalException("Unable to get XMLGregorianCalendar from instant: " + instant, e);
    }
  }

  public static Timestamp toTimestamp(final Instant instant) {
    return instant == null ? null : Timestamp.from(instant);
  }

  public static Instant toInstant(final Timestamp ts) {
    return ts == null ? null : ts.toInstant();
  }

  public static OffsetDateTime getDateAsOffsetDateTime(final Date date) {
    return date == null ? null : date.toInstant().atOffset(ZoneOffset.UTC);
  }

  public static LocalTime parseOpenCloseTime(final String timestamp, final DateTimeFormatter formatter) {
    try {
      return LocalTime.parse(timestamp, formatter);
    } catch (DateTimeParseException e) {
      return null;
    }
  }
}
