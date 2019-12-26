package com.havaz.transport.api.common;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtils {

    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    public static List<Integer> convertListStringToInt(List<String> listString) {
        List<Integer> returnList = new ArrayList<>();
        for (String s : listString) {
            returnList.add(Integer.parseInt(s));
        }
        return returnList;
    }

    public static Pageable convertPagingFormToPageable(PagingForm pagingForm) {

        Pageable pageable;
        Sort sortable = null;
        int page = pagingForm.getPage();
        int size = pagingForm.getSize();
        String sortType = (pagingForm.getSortType() != null ? pagingForm.getSortType() : "");
        String sortBy = (pagingForm.getSortBy() != null ? pagingForm.getSortBy() : "");
        if (!StringUtils.isEmpty(sortType)) {
            if (sortType.equals("ASC")) {
                sortable = Sort.by(sortBy).ascending();
            }
            if (sortType.equals("DESC")) {
                sortable = Sort.by(sortBy).descending();
            }
        }
        if (sortable != null) {
            pageable = PageRequest.of((page > Constant.ZERO ? page : Constant.CONFIGURATION_NO_1) - Constant.CONFIGURATION_NO_1, (size > Constant.ZERO ? size : Constant.CONFIGURATION_NO_10), sortable);
        } else {
            pageable = PageRequest.of((page > Constant.ZERO ? page : Constant.CONFIGURATION_NO_1) - Constant.CONFIGURATION_NO_1, (size > Constant.ZERO ? size : Constant.CONFIGURATION_NO_10));
        }
        return pageable;
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

    public static <T> PageCustom<T> convertPageImplToPageCustom(Page<T> page, String sortBy, String sortType) {
        PageCustom<T> pageCustom = new PageCustom<T>();
        pageCustom.setContent(page.getContent());
        pageCustom.setPage(page.getNumber() + Constant.CONFIGURATION_NO_1);
        pageCustom.setSize(page.getSize());
        pageCustom.setTotalElement((int) page.getTotalElements());
        pageCustom.setTotalPage(page.getTotalPages());
        pageCustom.setSortType(sortType);
        pageCustom.setSortBy(sortBy);

        return pageCustom;
    }

    public static String convertMaLenh(int idLenh, int kieuLenh) {
        if (kieuLenh == LenhConstants.LENH_DON) {
            return String.format("%s%06d", LenhConstants.MA_LENH_DON, idLenh);
        } else {
            return String.format("%s%06d", LenhConstants.MA_LENH_TRA, idLenh);
        }
    }

    // Chuyển số giờ sang giây
    public static int getSeconds(int time) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        long seconds;
        if (hour + time/60 < Constant.HOURS_OF_DAY) {
            seconds = TimeUnit.MINUTES.toSeconds(time);

        } else {
            seconds = TimeUnit.MINUTES.toSeconds(Constant.HOURS_OF_DAY - hour);
        }
        return (int) seconds;
    }

    //Lam tron so den "places" chu so thap phan
    public static double lamTron(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffHours;
    }

    public static Coordinate[] getCoordinate(File file) {
        try {
            List<Coordinate> coordinateList = new ArrayList<>();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Placemark");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
//                    if(eElement.getElementsByTagName("name").item(0).getTextContent().equalsIgnoreCase("Vùng 1")) {
                    String tokens[];
                    String s = eElement.getElementsByTagName("coordinates").item(0)
                            .getTextContent();
                    tokens = String.valueOf(s).split("\n");

                    List<String> doubleList = new ArrayList<>();
                    for (int i = 1; i < tokens.length - 1; i++) {
                        String tk[];
                        tk = tokens[i].split(",");
                        doubleList.add(tk[0]);
                        doubleList.add(tk[1]);
                    }

                    for (int i = 0; i < doubleList.size(); i = i + 2) {
                        int j = i + 1;
                        double x = Double.parseDouble(doubleList.get(i));
                        double y = Double.parseDouble(doubleList.get(j));
                        Coordinate coordinate = new Coordinate(x, y);
                        coordinateList.add(coordinate);
                    }
                    System.out.println();
                    for (Coordinate coordinate : coordinateList) {
                        System.out.println(coordinate.getX() + " " + coordinate.getY());
                    }
                }
            }

            Coordinate[] coordinates = new Coordinate[coordinateList.size()];
            coordinates = coordinateList.toArray(coordinates);
            return coordinates;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error(e.getMessage(), e);
            throw new TransportException(e);
        }
    }

    public static double distance(Point point_a, Point point_b) {
        double lat1 = point_a.getLat();
        double lon1 = point_a.getLng();
        double lat2 = point_b.getLat();
        double lon2 = point_b.getLng();
        if(lat1 == 0 || lon1 == 0 || lat2 == 0 || lon2 == 0)
            return 0;
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }
    public static List<Integer> convertStringToArrayInteger(String arr) {
        if (org.apache.logging.log4j.util.Strings.isEmpty(arr)) {
            return new ArrayList<>();
        }
        return Arrays.stream(Stream.of(arr.split(",")).mapToInt(Integer::parseInt).toArray()).boxed()
                .collect(Collectors.toList());
    }
}
