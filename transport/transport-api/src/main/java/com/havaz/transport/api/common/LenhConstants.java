package com.havaz.transport.api.common;

import java.util.HashMap;
import java.util.Map;

public class LenhConstants {

    public static final int LENH_STATUS_DA_DIEU = 1;
    public static final int LENH_STATUS_DANG_CHAY = 2;
    public static final int LENH_STATUS_DA_BI_HUY = 3;
    public static final int LENH_STATUS_DA_HOAN_TAT = 4;
    public static final int LENH_STATUS_DA_TAO = 5;
    public static final int XE_TRUNG_TAM_IS_XE_TRUNG_CHUYEN = 4;

    public static final int LENH_DON = 1;
    public static final int LENH_TRA = 2;

    public static final Map<Integer, String> GET_MSG_STATUS;
    static {
        GET_MSG_STATUS = new HashMap<>();
        GET_MSG_STATUS.put(LENH_STATUS_DA_TAO, "Đã tạo");
        GET_MSG_STATUS.put(LENH_STATUS_DA_DIEU, "Đã điều");
        GET_MSG_STATUS.put(LENH_STATUS_DANG_CHAY, "Đang chạy");
        GET_MSG_STATUS.put(LENH_STATUS_DA_BI_HUY, "Đã bị hủy");
        GET_MSG_STATUS.put(LENH_STATUS_DA_HOAN_TAT, "Đã hoàn tất");
    }

    public static final Map<Integer, String> GET_KIEU_LENH;
    static {
        GET_KIEU_LENH = new HashMap<>();
        GET_KIEU_LENH.put(LENH_DON, "Lệnh đón");
        GET_KIEU_LENH.put(LENH_TRA, "Lệnh trả");
    }

    public static final String MA_LENH_DON = "LD";
    public static final String MA_LENH_TRA = "LT";

    // Set History type to display on mobile screen
    public static final String HST_LENH_TYPE_IS_FINISHED = "Số lệnh đã hoàn thành:";
    public static final String HST_LENH_TYPE_IS_CANCELED = "Số lệnh đã hủy:";
    public static final String HST_CUST_TYPE_IS_PICKED_UP = "Số khách đã đón:";
    public static final String HST_CUST_TYPE_IS_CANCELED = "Số khách đã hủy đón:";
}
