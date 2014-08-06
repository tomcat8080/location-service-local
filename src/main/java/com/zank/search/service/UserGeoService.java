package com.zank.search.service;

import java.util.List;
import java.util.Map;

/**
 * Created by tomcat8080 on 14-8-4.
 */
public interface UserGeoService {

    /**
     * nearby search
     * @param lat
     * @param lng
     * @param start
     * @param size
     * @return
     */
    public List<Map<String, Object>> nearBy(double lng, double lat, int start, int size);

    /**
     * nearby search, default size 24
     * @param lat
     * @param lng
     * @param start
     * @return
     */
    public List<Map<String, Object>> nearBy(double lng, double lat, int start);

    /**
     * update user location
     * @param record
     */
    public boolean update(Map<String, Object> record);

    /**
     * remove user location
     * @param id
     */
    public boolean remove(Object id);
}
