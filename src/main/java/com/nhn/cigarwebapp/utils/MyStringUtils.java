package com.nhn.cigarwebapp.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class MyStringUtils {

    public static String normalizeFullName(String str){
        String fullName = str;
        fullName = StringUtils.normalizeSpace(fullName);
        fullName = WordUtils.capitalize(fullName);

        return fullName;
    }

}
