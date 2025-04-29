package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static String getUsername() {
        return "system";
    }
}
