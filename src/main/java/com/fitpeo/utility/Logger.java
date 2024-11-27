package com.fitpeo.utility;

public class Logger {
    public static void log(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}

