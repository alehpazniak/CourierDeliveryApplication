package pl.aleh.courier.inpostpl.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import pl.aleh.courier.CourierClientConfiguration;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "inpostpl")
public class InpostPlClientConfiguration extends CourierClientConfiguration {

  private String baseUrl;
  private String trackingUrlFormat;

}
