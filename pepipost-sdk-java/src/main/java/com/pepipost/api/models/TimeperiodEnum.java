/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public enum TimeperiodEnum {
    DAILY, //TODO: Write general description for this element
    WEEKLY, //TODO: Write general description for this element
    MONHTLY; //TODO: Write general description for this element

    private static TreeMap<String, TimeperiodEnum> valueMap = new TreeMap<String, TimeperiodEnum>();
    private String value;

    static {
        DAILY.value = "daily";
        WEEKLY.value = "weekly";
        MONHTLY.value = "monhtly";

        valueMap.put("daily", DAILY);
        valueMap.put("weekly", WEEKLY);
        valueMap.put("monhtly", MONHTLY);
    }

    /**
     * Returns the enum member associated with the given string value
     * @return The enum member against the given string value */
    @com.fasterxml.jackson.annotation.JsonCreator
    public static TimeperiodEnum fromString(String toConvert) {
        return valueMap.get(toConvert);
    }

    /**
     * Returns the string value associated with the enum member
     * @return The string value against enum member */
    @com.fasterxml.jackson.annotation.JsonValue
    public String value() {
        return value;
    }
        
    /**
     * Get string representation of this enum
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Convert list of TimeperiodEnum values to list of string values
     * @param toConvert The list of TimeperiodEnum values to convert
     * @return List of representative string values */
    public static List<String> toValue(List<TimeperiodEnum> toConvert) {
        if(toConvert == null)
            return null;
        List<String> convertedValues = new ArrayList<String>();
        for (TimeperiodEnum enumValue : toConvert) {
            convertedValues.add(enumValue.value);
        }
        return convertedValues;
    }
} 