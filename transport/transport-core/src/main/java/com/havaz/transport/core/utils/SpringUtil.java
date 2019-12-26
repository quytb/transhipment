package com.havaz.transport.core.utils;

import com.havaz.transport.core.constant.EnvironmentProfile;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class SpringUtil {

    private SpringUtil() {}

    public static Object getBean(String name) {
        return ApplicationContextProvider.getApplicationContext().getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ApplicationContextProvider.getApplicationContext().getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) {
        return ApplicationContextProvider.getApplicationContext().getBean(name, args);
    }


    public static <T> T getBean(Class<T> requiredType) {
        return ApplicationContextProvider.getApplicationContext().getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        return ApplicationContextProvider.getApplicationContext().getBean(requiredType, args);
    }

    public static boolean containsBean(String beanName) {
        return ApplicationContextProvider.getApplicationContext().containsBean(beanName);
    }

    public static boolean isActiveProfiles(EnvironmentProfile...profiles) {
        Environment env = getBean(Environment.class);
        Set<String> set = new HashSet<>(Arrays.asList(env.getActiveProfiles()));
        for (EnvironmentProfile profile : profiles) {
            if (set.contains(profile.getProfile())) {
                return true;
            }
        }
        return false;
    }
}