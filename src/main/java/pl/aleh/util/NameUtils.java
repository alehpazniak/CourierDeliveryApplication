package pl.aleh.util;

import static org.apache.commons.lang3.StringUtils.SPACE;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class NameUtils {

  public static Name parsName(final String name) {
    var fullName = new Name();
    if (StringUtils.isNotBlank(name)) {
      var firstAndLastName = name.trim().split(SPACE, 2);
      fullName.setFirstName(firstAndLastName[0]);
      fullName.setLastName(firstAndLastName.length > 1 ? firstAndLastName[1] : firstAndLastName[0]);
      return fullName;
    }
    return fullName;
  }

}
