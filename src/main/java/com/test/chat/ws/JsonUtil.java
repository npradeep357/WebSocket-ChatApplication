package com.test.chat.ws;

import com.google.gson.Gson;

/**
 * Utility class to convert to/from JSON Strings from/to Java Objects
 */
public class JsonUtil {

    private static final Gson gson;

    static {
        // static operations goes here
        gson = new Gson();
    }

    private JsonUtil() {
    }

    public static final <T> String toJson(
            T t) {
        return gson.toJson(t);
    }

    public static final <T> T fromJson(
            String jsonStr,
            Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

}
