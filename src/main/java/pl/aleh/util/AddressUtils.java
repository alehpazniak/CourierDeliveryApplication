package pl.aleh.util;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

import lombok.experimental.UtilityClass;
import pl.aleh.domain.delivery.Address;
import pl.aleh.domain.delivery.DeliveryAddress;

@UtilityClass
public class AddressUtils {

  public static String getClickAndCollectLocationId(final DeliveryAddress deliveryAddress) {
    var clickAndCollect = deliveryAddress.getClickAndCollect();
    return clickAndCollect == null ? null : clickAndCollect.getClickAndCollectLocationId();
  }

}
