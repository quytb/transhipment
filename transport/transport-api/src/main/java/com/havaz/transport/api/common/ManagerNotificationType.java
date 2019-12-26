package com.havaz.transport.api.common;

public enum ManagerNotificationType {
    NEW_TICKET(1), CHANGE_TRIP(2), DRIVER_REJECT_PICKUP(3), DRIVER_CANCEL_CMD(4), DRIVER_CONFIRMED_PICKUP(5),
    CLIENT_CANCEL_TRANSPORT(6), CLIENT_UPDATE_INFO(7);

    private final int type;

    public int getType() {
        return type;
    }

    ManagerNotificationType(int type) {
        this.type = type;
    }
}
