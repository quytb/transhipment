package com.havaz.transport.api.common;

public enum NotificationType {
    ADD_CMD_NOW(1), ADD_CMD(2), NEW_CMD_NOW(3), NEW_CMD(4), CANCEL_CMD(5), UPDATE_CMD(6);
    private final int type;

    NotificationType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
