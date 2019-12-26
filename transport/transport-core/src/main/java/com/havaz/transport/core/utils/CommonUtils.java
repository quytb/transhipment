package com.havaz.transport.core.utils;

import com.havaz.transport.core.exception.TransportException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static List<Integer> convertListStringToInt(List<String> listString) {
        List<Integer> returnList = new ArrayList<>();
        for (String s : listString) {
            returnList.add(Integer.parseInt(s));
        }
        return returnList;
    }

    /**
     * Generate topic for havaz
     *
     * @param merchantId
     * @param tripId
     * @param laiXeId
     */
    public static String generateTopic(String merchantId, int tripId, int laiXeId) {
        String topic = null;
        if (merchantId != null && tripId > 0 && laiXeId > 0) {
            topic = merchantId + Strings.UNDER_LINE + tripId + Strings.UNDER_LINE + laiXeId;
        }
        return topic;
    }

    //Lam tron so den "places" chu so thap phan
    public static double lamTron(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static String hashMD5(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void sendSMS(String phone, String message) {
        SpeedSMSAPI api = SpringUtil.getBean(SpeedSMSAPI.class);
        try {
            api.sendSMS(phone, message , 2, "AUTOMATIC");
        } catch (IOException e) {
            throw new TransportException("Failure to send SMS", e);
        }
    }
}
