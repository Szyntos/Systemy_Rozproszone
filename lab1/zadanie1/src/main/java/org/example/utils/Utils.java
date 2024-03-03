package org.example.utils;

public class Utils {
    public static String extractWithoutPrefix(String prefix, String line){
        String extracted = line.substring(line.indexOf(prefix) + prefix.length());
        if (line.contains(prefix)) {
            return extracted;
        } else {
            System.out.println("Failed Extraction");
        }
        return "Error";

    }
}
