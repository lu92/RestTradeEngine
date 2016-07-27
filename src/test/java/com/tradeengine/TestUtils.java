package com.tradeengine;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.number.BigDecimalCloseTo;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
public class TestUtils {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private static final String CHARACTER = "a";

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static String convertObjectToJsonText(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }

    public static List<Integer> LocalDateTime2JSonFigure(LocalDateTime ldt)
    {
        List<Integer> datetime = new ArrayList<>(6);
        datetime.add(ldt.getYear());
        datetime.add(ldt.getMonthValue());
        datetime.add(ldt.getDayOfMonth());
        datetime.add(ldt.getHour());
        datetime.add(ldt.getMinute());
        datetime.add(ldt.getSecond());
        return datetime;
    }

    public static Matcher<BigDecimal> closeTo(double value, double precision) {
        return BigDecimalCloseTo.closeTo(new BigDecimal(value), new BigDecimal(precision));
    }

    public static Matcher<BigDecimal> closeTo(double value) {
        return closeTo(value, 1);
    }

    public static String convert(LocalDate localDate)
    {
        return localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfWeek().getValue();
    }
}
