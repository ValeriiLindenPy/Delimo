package rs.delimo.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            builder
                    .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER))
                    .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER))
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .timeZone(TimeZone.getTimeZone("Europe/Belgrade"));
        };
    }
}
