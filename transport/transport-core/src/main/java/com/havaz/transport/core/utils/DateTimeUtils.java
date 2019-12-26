package com.havaz.transport.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {

    private static final Logger log = LoggerFactory.getLogger(DateTimeUtils.class);

    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_DD_MM_YYYY = "dd-MM-yyyy";

    private DateTimeUtils() { }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    public static String convertLocalDateToString(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static LocalDate convertStringToLocalDate(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            log.warn("dateStr is null or empty, return null");
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            log.warn("dateStr is null or empty, return null");
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }
}
