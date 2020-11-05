package com.backendless.persistence.offline.visitor.bcknd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static final String dateFormats = "EEE MMM dd HH:mm:ss zzz yyyy;yyyy-MM-dd'T'HH:mm:ss.SSS'Z';MM/dd/yyyy HH:mm:ss 'GMT'z;MM.dd.yyyy HH:mm:ss 'GMT'z;MM-dd-yyyy HH:mm:ss 'GMT'z;MM/dd/yyyy HH:mm:ss z;MM.dd.yyyy HH:mm:ss z;MM.dd.yyyy HH:mm:ss;MM-dd-yyyy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM.dd.yyyy;MM-dd-yyyy;MM/dd/yyyy HH:mm:ss 'GMT'Z;MM/dd/yyyy HH:mm;MM/dd/yyyy;dd/MMM/yyyy;dd-MMM-yyyy;EEEEE, d MMMMM yyyy;yyyy/MM/d/HH:mm:ss;yyyy-MM-dd'T'HH:mm:ss;EEEEE, MMMMM d, yyyy;MMMMM d, yyyy;yyyy M d;yyyyMMMd;yyyy-MMM-d;yyyy-M-d, E;'Date' yyyy-MM-dd;yyyy-MM-dd'T'HH:mm:ss;yyyy-MM-dd'T'HH:mmZ;yyyy-MM-dd;yyyy-'W'w;yyyy-DDD;d MMMMM yyyy, HH'h' mm'm' ss's'";
    private final List<SimpleDateFormat> formatters;

    public DateUtil() {
        formatters = new ArrayList<>();
        for (String pattern : dateFormats.split(";")) {
            formatters.add(new SimpleDateFormat(pattern));
        }
    }

    public String getTimestampFromString(String value) throws IllegalArgumentException {
        for (SimpleDateFormat formatter : formatters) {
            try {
                return String.valueOf(formatter.parse(value).getTime());
            } catch (ParseException ignored) { }
        }

        throw new IllegalArgumentException("Invalid date format");
    }

}

