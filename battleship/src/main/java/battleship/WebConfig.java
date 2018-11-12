package battleship;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import battleship.middlewares.converters.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringTo2DArrayConverter());
        registry.addConverter(new StringToArrayListConverter());
        registry.addConverter(new StringToMapStringIntegerConverter());
    }
}
