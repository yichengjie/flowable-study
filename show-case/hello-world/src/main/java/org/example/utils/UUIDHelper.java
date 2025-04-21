package org.example.utils;

import java.util.UUID;

public class UUIDHelper {

    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
