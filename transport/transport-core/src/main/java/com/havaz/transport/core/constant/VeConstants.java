package com.havaz.transport.core.constant;

import java.util.HashMap;
import java.util.Map;

public class VeConstants {

    //TRUNG CHUYỂN ĐÓN
    public static final int TC_STATUS_CHUA_DIEU = 0;
    public static final int TC_STATUS_DA_DIEU = 1;
    public static final int TC_STATUS_DANG_DI_DON = 2;
    public static final int TC_STATUS_DA_DON = 3;
    public static final int TC_STATUS_DA_HUY = 4;
    public static final int TC_STATUS_XE_TUYEN_DON = 10;
    public static final int TC_STATUS_XE_TUYEN_HUY_DON = 11;
    public static final int TC_STATUS_XE_NGOAI_DON = 12;
    public static final int IS_HAVAZ_NOW = 1;
    public static final int TAO_MOI_VE = 1;
    public static final int CAP_NHAT_VE = 2;
    public static final int XUONG_XE = 3;
    public static final int HUY_VE = 4;
    public static final int CHUYEN_CHO = 5;

    //TRUNG CHUYỂN TRẢ
    public static final int TC_STATUS_DANG_DI_TRA = 2;
    public static final int TC_STATUS_DA_TRA = 3;
    public static final int TC_STATUS_DA_HUY_TRA = 4;
    public static final int TC_STATUS_XE_TUYEN_TRA = 10;
    public static final int TC_STATUS_XE_TUYEN_HUY_TRA = 11;
    public static final int TC_STATUS_XE_NGOAI_TRA = 12;

    //Ve action
    public static final Map<Integer, String> VE_ACTION;
    static {
        VE_ACTION = new HashMap<>();
        VE_ACTION.put(TAO_MOI_VE, "TAO_MOI_VE");
        VE_ACTION.put(CAP_NHAT_VE, "CAP_NHAT_VE");
        VE_ACTION.put(XUONG_XE, "XUONG_XE");
        VE_ACTION.put(HUY_VE, "HUY_VE");
        VE_ACTION.put(CHUYEN_CHO, "CHUYEN_CHO");
    }

    public static final Map<Integer, String> GET_MSG_STATUS;
    static {
        GET_MSG_STATUS = new HashMap<>();
        GET_MSG_STATUS.put(TC_STATUS_CHUA_DIEU, "Chưa điều");
        GET_MSG_STATUS.put(TC_STATUS_DA_DIEU, "Đã điều");
        GET_MSG_STATUS.put(TC_STATUS_DANG_DI_DON, "Đang đi đón");
        GET_MSG_STATUS.put(TC_STATUS_DA_DON, "Đã đón");
        GET_MSG_STATUS.put(TC_STATUS_DA_HUY, "Đã hủy");
        GET_MSG_STATUS.put(TC_STATUS_XE_TUYEN_DON, "Xe tuyến đón");
        GET_MSG_STATUS.put(TC_STATUS_XE_NGOAI_DON, "Xe ngoài đón");
        GET_MSG_STATUS.put(TC_STATUS_XE_TUYEN_HUY_DON, "Xe tuyến hủy");
    }

    public static final Map<Integer, String> GET_MSG_STATUS_TRA;
    static {
        GET_MSG_STATUS_TRA = new HashMap<>();
        GET_MSG_STATUS_TRA.put(TC_STATUS_CHUA_DIEU, "Chưa điều");
        GET_MSG_STATUS_TRA.put(TC_STATUS_DA_DIEU, "Đã điều");
        GET_MSG_STATUS_TRA.put(TC_STATUS_DANG_DI_TRA, "Đang đi trả");
        GET_MSG_STATUS_TRA.put(TC_STATUS_DA_TRA, "Đã trả");
        GET_MSG_STATUS_TRA.put(TC_STATUS_DA_HUY_TRA, "Đã hủy trả");
        GET_MSG_STATUS.put(TC_STATUS_XE_TUYEN_DON, "Xe tuyến trả");
        GET_MSG_STATUS.put(TC_STATUS_XE_NGOAI_DON, "Xe ngoài trả");
        GET_MSG_STATUS_TRA.put(TC_STATUS_XE_TUYEN_HUY_TRA, "Xe tuyến hủy");
    }
}
