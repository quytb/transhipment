package com.havaz.transport.core.constant;

public enum EnvironmentProfile {

    HASON_HAIVAN_LOCAL("hason_haivan-local"),

    HASON_HAIVAN_TEST("hason_haivan-test"),

    HASON_HAIVAN_PROD("hason_haivan-prod"),

    VUNG_TAU_LOCAL("vungtau-local"),

    VUNG_TAU_TEST("vungtau-test"),

    VUNG_TAU_PROD("vungtau-prod"),

    HAI_VAN_LOCAL("haivan-local"),

    HAI_VAN_TEST("haivan-test"),

    HAI_VAN_PROD("haivan-prod");

    private String profile;

    EnvironmentProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public static EnvironmentProfile find(String profile) {
        EnvironmentProfile[] values = EnvironmentProfile.values();
        for (EnvironmentProfile value : values) {
            if (value.getProfile().equalsIgnoreCase(profile)) {
                return value;
            }
        }
        return null;
    }
}
