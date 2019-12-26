package com.havaz.transport.api.utils;

import com.havaz.transport.core.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;

public class DateTimeUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

    public static int[] convertThangNam(String target){
        String[] ngayThangString = target.split(Strings.SLASH);

        try {
            int thang = Integer.valueOf(ngayThangString[0]);
            int nam = Integer.valueOf((ngayThangString[1]));

            return new int[]{thang, nam};
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                                              "Khong the convert " + target + " sang MM/yyyy");
        }
    }

    public static String addTimeFormatHHMM(String fromTime, Integer timeAdd) {
        LocalTime time = LocalTime.parse(fromTime);
        time = time.plusMinutes(timeAdd);
        return time.toString();
    }
}
