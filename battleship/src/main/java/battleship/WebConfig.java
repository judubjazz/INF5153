package battleship;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import battleship.converters.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToShipConverter());
        registry.addConverter(new StringToGridConverter());
        registry.addConverter(new StringTo2DArrayConverter());
        registry.addConverter(new StringToArrayListConverter());
//        registry.addConverter(new StringToMapConverter());
    }
}
