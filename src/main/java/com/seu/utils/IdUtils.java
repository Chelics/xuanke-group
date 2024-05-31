package com.seu.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IdUtils类
 * 用于转换ID数组为,分隔的String或反之
 */
public class IdUtils {
    /*public static String listToString(List<Integer> idsList) {
        return idsList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }*/

    public static List<Integer> stringToList(String idsString) {
        if (idsString == null || idsString.isEmpty()) {
            return List.of(); // 返回一个空的不可变列表
        }
        String[] idsArray = idsString.split(",");
        return Arrays.stream(idsArray)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
