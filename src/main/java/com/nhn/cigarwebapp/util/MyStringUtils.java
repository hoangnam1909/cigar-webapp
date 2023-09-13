package com.nhn.cigarwebapp.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

public class MyStringUtils {

    public static String normalizeFullName(String str){
        String fullName = str;
        fullName = StringUtils.normalizeSpace(fullName);
        fullName = WordUtils.capitalize(fullName);

        return fullName;
    }

}
