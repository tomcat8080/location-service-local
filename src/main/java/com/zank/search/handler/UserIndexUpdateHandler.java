package com.zank.search.handler;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zank.search.ServerConfig;
import com.zank.search.service.UserGeoService;
import com.zank.search.service.impl.MongoUserGeoServiceImpl;
import com.zank.zcf.command.Command;
import com.zank.zcf.command.handler.AbstractCommandHandler;
import com.zank.zcf.util.LogUtils;
import org.apache.log4j.Logger;

public class UserIndexUpdateHandler extends AbstractCommandHandler {
    private static Logger logger = Logger.getLogger(UserIndexUpdateHandler.class);

    private UserGeoService service = new MongoUserGeoServiceImpl();

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);

    public UserIndexUpdateHandler() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> cond = new HashMap<String, Object>();
                Map<String, Object> dateline = new HashMap<String, Object>();

                dateline.put("$lt", System.currentTimeMillis() - 1000 * 60 * 60 * 24);
                cond.put("dateline", dateline);
                service.remove(cond);
            }
        }, 1, 1, TimeUnit.DAYS);

    }

    @Override
    public boolean handle(Command t) {
        try {
            LogUtils.info(logger, "handler {0}", t);
            String id = t.getStringParam(ServerConfig.getServerConfig().getMongoKeyName());
            String action = t.getAction();

            Map<String, Object> user = t.getParams();
            LogUtils.debug(logger, "LocationCommand-user {0}", user);

            if ("update".equalsIgnoreCase(action)) {
                return update(user);
            }

            if ("remove".equalsIgnoreCase(action)) {
                return remove(user);
            }

            return true;
        } catch (Exception e) {
            LogUtils.error(logger, e, "handle excute error!");
            return false;
        }
    }

    /**
     * remove user by id
     * @param record
     * @return
     */
    public boolean remove(Map<String, Object> record) {
        return service.remove(record.get(ServerConfig.getServerConfig().getMongoKeyName()));
    }

    /**
     * update user by id
     * @param record
     * @return
     */
    public boolean update(Map<String, Object> record) {
        return service.update(record);
    }

}
