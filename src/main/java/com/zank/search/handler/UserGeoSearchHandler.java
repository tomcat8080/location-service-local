package com.zank.search.handler;

import com.zank.search.service.UserGeoService;
import com.zank.search.service.impl.MongoUserGeoServiceImpl;
import com.zank.util.ImportUtils;
import com.zank.zcf.command.Command;
import com.zank.zcf.command.Response;
import com.zank.zcf.command.handler.MultiCommandHandler;
import com.zank.zcf.dao.mongo.factory.MongoDaoFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tomcat8080 on 14-8-1.
 */
public class UserGeoSearchHandler extends MultiCommandHandler {

    private UserGeoService service = new MongoUserGeoServiceImpl();

    public UserGeoSearchHandler() {

    }

    public void importMongo(Command cmd) throws Exception {
        ImportUtils.importToMongoFromTxt(cmd.getStringParam("file"));
    }

    public void importRedis(Command cmd) throws Exception {
        ImportUtils.importToRedisFromTxt(cmd.getStringParam("file"));
    }

    public void searchByLocation(Command cmd) {
        Response response = new Response();

        double lng = cmd.getDoubleParam("lng");
        double lat = cmd.getDoubleParam("lat");
        int start = cmd.getIntParam("start");
        int size = cmd.getIntParam("size");

        List<Map<String, Object>> result = service.nearBy(lng, lat, start, size);

        response.addValue("status", "success");
        response.addValue("result", result);
        response.addValue("size", result.size());

        cmd.setResponse(response);
    }
}
