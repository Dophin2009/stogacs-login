package net.edt.web.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperBean {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        ZoneId utcZone = ZoneId.ofOffset("UTC", ZoneOffset.UTC);

        // Provider and converters for LocalDate
        modelMapper.createTypeMap(Long.class, LocalDate.class).setProvider(request -> LocalDate.now());
        modelMapper.addConverter(ctx -> {
            Long source = ctx.getSource();
            return source == null ? null : Instant.ofEpochSecond(source).atZone(utcZone).toLocalDate();
        }, Long.class, LocalDate.class);
        modelMapper.addConverter(ctx -> ctx.getSource().toEpochDay(), LocalDate.class, Long.class);

        // Provider and converters for LocalDateTime
        modelMapper.createTypeMap(Long.class, LocalDateTime.class).setProvider(request -> LocalDateTime.now());
        modelMapper.addConverter(ctx -> {
            Long source = ctx.getSource();
            return source == null ? null : Instant.ofEpochSecond(source).atZone(utcZone).toLocalDateTime();

        }, Long.class, LocalDateTime.class);
        modelMapper.addConverter(ctx -> ctx.getSource().toEpochSecond(ZoneOffset.UTC), LocalDateTime.class, Long.class);

        // Provider and converters for LocalDate
        modelMapper.createTypeMap(String.class, LocalDate.class).setProvider(request -> LocalDate.now());
        modelMapper.addConverter(ctx -> {
            String source = ctx.getSource();
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
            return source == null ? null : LocalDate.parse(source, format);
        }, String.class, LocalDate.class);
        modelMapper.addConverter(ctx -> ctx.getSource().format(DateTimeFormatter.ISO_DATE),
                                 LocalDate.class, String.class);

        // Provider and converters for LocalDateTime
        modelMapper.createTypeMap(String.class, LocalDateTime.class).setProvider(request -> LocalDateTime.now());
        modelMapper.addConverter(ctx -> {
            String source = ctx.getSource();
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            return source == null ? null : LocalDateTime.parse(source, format);

        }, String.class, LocalDateTime.class);
        modelMapper.addConverter(ctx -> ctx.getSource().format(DateTimeFormatter.ISO_DATE_TIME),
                                 LocalDateTime.class, String.class);

        return modelMapper;
    }

}
