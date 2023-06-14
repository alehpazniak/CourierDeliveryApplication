package pl.aleh.util;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.aleh.exception.CourierInternalException;

@Slf4j
@Component
public class JsonConverterUtils {

  private static ObjectMapper mapper = new ObjectMapper();

  @Autowired
  public JsonConverterUtils(ObjectMapper mapper) {
    JsonConverterUtils.mapper = mapper;
  }

  public static String getAsString(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      log.debug("Unable to write object to JSON: {}", o, e);
      return o == null ? null : o.toString();
    }
  }

  public static <T> T getAsObject(String s, Class<T> clazz) {
    return getAsObject(s, clazz, false);
  }

  public static <T> T getAsObject(String s, Class<T> clazz, boolean ignoreErrors) {
    try {
      return mapper.readValue(s, clazz);
    } catch (Exception e) {
      if (ignoreErrors) {
        return null;
      }
      throw new CourierInternalException(format("Unable to read %s from JSON: %s", clazz, s), e);
    }
  }

  public static String prettyPrintXml(Object o) {
    try {
      var string = mapper.writeValueAsString(o);
      return mapper.readValue(string, Object.class).toString();
    } catch (Exception e) {
      log.error("Unable to write XML object as pretty printed JSON: {}", o, e);
      return o == null ? null : o.toString();
    }
  }

  public static String prettyPrint(final String jsonBody) {
    try {
      return mapper.readValue(jsonBody, Object.class).toString();
    } catch (Exception e) {
      return jsonBody;
    }
  }

}
