package net.edt;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Provider<LocalDate> localDateProvider = request -> LocalDate.now();
        Converter<String, LocalDate> stringLocalDateConverter = ctx -> {
            String source = ctx.getSource();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return source == null ? null : LocalDate.parse(source, format);
        };

        Converter<LocalDate, String> localDateStringConverter = ctx -> ctx.getSource().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modelMapper.createTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
        modelMapper.addConverter(stringLocalDateConverter, String.class, LocalDate.class);
        modelMapper.addConverter(localDateStringConverter, LocalDate.class, String.class);

        return modelMapper;
    }

}
