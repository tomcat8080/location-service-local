package com.zank.search.service.impl;

import com.mongodb.CommandResult;
import com.zank.search.ServerConfig;
import com.zank.search.service.UserGeoService;
import com.zank.util.DistanceUtils;
import com.zank.zcf.dao.mongo.IMongoDao;
import com.zank.zcf.dao.mongo.factory.MongoDaoFactory;
import com.zank.zcf.util.HostPort;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomcat8080 on 14-8-4.
 */
public class MongoUserGeoServiceImpl implements UserGeoService {

    private static final String ID = "uid";
    private static final int DEFAULT_SIZE = 24;
    private final IMongoDao mongoDao;
    private ServerConfig config = ServerConfig.getServerConfig();

    public MongoUserGeoServiceImpl() {
        mongoDao = MongoDaoFactory.getMongoDao(new HostPort(config.getMongoHost(), config.getMongoPort()), config.getMongoDbName(), config.getMongoColName());
    }

    @Override
    public List<Map<String, Object>> nearBy(double lng, double lat, int start, int size) {
        Map<String, Object> cond = new HashMap<String, Object>();
        Map<String, Object> loc = new HashMap<String, Object>();
        loc.put("$nearSphere", new double[]{lng, lat});
        cond.put("loc", loc);
        Map<String, Object> sort = new HashMap<String, Object>();

        if (size == 0) {
            size = DEFAULT_SIZE;
        }

        List<Map<String, Object>> list = mongoDao.findList(cond, sort, start, size + start);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();


        for (Map<String, Object> record : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Double> location = (Map<String, Double>) record.get("loc");
            double lng1 = location.get("lng");
            double lat1 = location.get("lat");

            double distance = DistanceUtils.getDistance(lng1, lat1, lng, lat);
            map.put("distance", distance);
            map.put("lat", lat1);
            map.put("lng", lng1);
            map.put(ID, record.get(ID));
            map.put("dateline", record.get("dateline"));
            result.add(map);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> nearBy(double lng, double lat, int start) {
        return nearBy(lng, lat, start, DEFAULT_SIZE);
    }

    @Override
    public boolean update(Map<String, Object> record) {
        Map<String,Object> cnd = new HashMap<String, Object>();
        cnd.put(ID, record.get(ID));
        mongoDao.update(cnd, record, true, true);
        return true;
    }

    @Override
    public boolean remove(Object id) {
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put(ID, id);
        mongoDao.delete(cond);
        return true;
    }

    public static void main(String[] args) {
        MongoUserGeoServiceImpl service = new MongoUserGeoServiceImpl();
        List<Map<String, Object>> list = service.nearBy(121.417397,31.204075, 0, 24);

        for (Map<String, Object> user : list) {
            System.out.println(user);
        }

    }
}
