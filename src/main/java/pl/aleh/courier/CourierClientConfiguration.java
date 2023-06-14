package pl.aleh.courier;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Configuration
public class CourierClientConfiguration {

  @Value(value = "${courier.configuration.readTimeout}")
  private Duration readTimeout;
  @Value(value = "${courier.configuration.useHttpClientWiretap}")
  private boolean useHttpClientWiretap;
  @Value(value = "${courier.configuration.retryNumber}")
  private long retryNumber;
  @Value(value = "${courier.configuration.retryBackoff}")
  private Duration retryBackoff;
}
