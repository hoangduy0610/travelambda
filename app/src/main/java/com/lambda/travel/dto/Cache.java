package com.lambda.travel.dto;

import com.lambda.travel.model.Location;

import java.util.ArrayList;
import java.util.List;

public class Cache {
    public static String userName = "";
    public static String avatarCache = "";

    public static List<Location> locationsCache = new ArrayList<>();
    public static List<String> locationLabelsCache = new ArrayList<>();
    public static List<String> locationValuesCache = new ArrayList<>();
}
