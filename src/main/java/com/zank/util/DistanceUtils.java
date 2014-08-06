package com.zank.util;

import java.math.BigDecimal;

/**
 * Created by tomcat8080 on 14-8-4.
 */
public class DistanceUtils {
    private final static double PI = 3.14159265358979323; // 圆周率
    private final static double R = 6371229; // 地球的半径

    public static double getDistance(double longt1, double lat1, double longt2,double lat2) {
        double x, y;
        double distance;
        x = (longt2 - longt1) * PI * R
                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
        y = (lat2 - lat1) * PI * R / 180;
        distance = ((int)Math.hypot(x, y)) / 1000.00;
        return distance;
    }
}
