package com.leeroy.forwordpanel.forwordpanel.common;


public class WebThreadLocalManager {

    private static ThreadLocal<WebCurrentData> currentDataThreadLocal = new ThreadLocal<>();

    public static WebCurrentData get() {
        WebCurrentData currentData = currentDataThreadLocal.get();
        if (currentData == null) {
            currentDataThreadLocal.set(WebCurrentData.builder().build());
        }
        return currentDataThreadLocal.get();
    }

    public static void set(WebCurrentData currentData) {
        currentDataThreadLocal.set(currentData);
    }

    public static void remove() {
        currentDataThreadLocal.remove();
    }

    private WebThreadLocalManager() {
    }
}
