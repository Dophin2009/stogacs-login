package net.edt.web.bean;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperBean {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Provider and converters for LocalDate
        Provider<LocalDate> localDateProvider = request -> LocalDate.now();
        Converter<String, LocalDate> stringLocalDateConverter = ctx -> {
            String source = ctx.getSource();
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
            return source == null ? null : LocalDate.parse(source, format);
        };

        Converter<LocalDate, String> localDateStringConverter = ctx -> ctx.getSource()
                .format(DateTimeFormatter.ISO_DATE);

        modelMapper.createTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
        modelMapper.addConverter(stringLocalDateConverter, String.class, LocalDate.class);
        modelMapper.addConverter(localDateStringConverter, LocalDate.class, String.class);

        // Provider and converters for LocalDateTime
        Provider<LocalDateTime> localDateTimeProvider = request -> LocalDateTime.now();
        Converter<String, LocalDateTime> stringLocalDateTimeConverter = ctx -> {
            String source = ctx.getSource();
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            return source == null ? null : LocalDateTime.parse(source, format);

        };

        Converter<LocalDateTime, String> localDateTimeStringConverter = ctx -> ctx.getSource()
                .format(DateTimeFormatter.ISO_DATE_TIME);

        modelMapper.createTypeMap(String.class, LocalDateTime.class).setProvider(localDateTimeProvider);
        modelMapper.addConverter(stringLocalDateTimeConverter, String.class, LocalDateTime.class);
        modelMapper.addConverter(localDateTimeStringConverter, LocalDateTime.class, String.class);

        return modelMapper;
    }

}
