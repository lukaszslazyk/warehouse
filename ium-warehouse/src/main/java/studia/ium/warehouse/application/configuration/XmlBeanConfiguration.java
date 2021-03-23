package studia.ium.warehouse.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:beans.xml")
public class XmlBeanConfiguration {
}
