package com.zank.search;

import com.zank.search.handler.UserGeoSearchHandler;
import com.zank.search.handler.UserIndexUpdateHandler;
import com.zank.zcf.command.Command;
import com.zank.zcf.command.executor.SimpleCommandExecutor;
import com.zank.zcf.dao.redis.IRedisDao;
import com.zank.zcf.dao.redis.factory.RedisDaoFactory;
import com.zank.zcf.server.HttpServer;
import com.zank.zcf.server.QueueServer;

/**
 * Hello world!
 *
 */
public class Server
{
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );

        ServerConfig config = ServerConfig.getServerConfig();

        // dao
        IRedisDao redisUserDao = RedisDaoFactory.getRedisDao(config.getRedisHost(), config.getRedisPort());

//        Command map = new Command(config.getRedisQueueUpdateAction(), config.getRedisQueueUpdateAction());
//        map.setAction(config.getRedisQueueUpdateAction());
//        map.setParam("id", 1);
//        redisUserDao.lpush(config.getRedisQueueKey(), map.toJSON());

        SimpleCommandExecutor executor = new SimpleCommandExecutor();
        executor.registerHandler(config.getRedisQueueRemoveAction(), new UserIndexUpdateHandler());
        executor.registerHandler(config.getRedisSearchAction(), new UserGeoSearchHandler());
        executor.registerHandler(config.getRedisQueueUpdateAction(), new UserIndexUpdateHandler());

        QueueServer cmdQueueServer = new QueueServer(5, executor, redisUserDao, config.getRedisQueueKey());
        cmdQueueServer.start();

        HttpServer http = new HttpServer(config.getHttpServerPort(), executor);
        http.start();
    }
}
