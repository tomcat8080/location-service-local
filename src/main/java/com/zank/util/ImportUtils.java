package com.zank.util;

import com.mongodb.util.JSON;
import com.zank.search.ServerConfig;
import com.zank.zcf.dao.mongo.IMongoDao;
import com.zank.zcf.dao.mongo.factory.MongoDaoFactory;
import com.zank.zcf.dao.redis.IRedisDao;
import com.zank.zcf.dao.redis.factory.RedisDaoFactory;
import com.zank.zcf.util.HostPort;
import com.zank.zcf.util.JsonUtils;
import com.zank.zcf.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomcat8080 on 14-8-1.
 */
public class ImportUtils {

    public static void importToMongoFromTxt(String file) throws Exception {
        ServerConfig config = ServerConfig.getServerConfig();

        IMongoDao mongoDao = MongoDaoFactory.getMongoDao(new HostPort(config.getMongoHost(), config.getMongoPort()), config.getMongoDbName(), config.getMongoColName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        List<String> lines = FileUtils.readLines(new File(ImportUtils.class.getClassLoader().getResource(file).getFile()));
        List<String> lines = FileUtils.readLines(new File(file));
        for (String line : lines) {
//            System.out.println(line);
            String[] array = line.split(",");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", array[0]);
            map.put("dateline", sdf.parse(array[3]).getTime()/1000);
            Map<String, Double> loc = new HashMap<String, Double>();
            map.put("loc", loc);
            loc.put("lat", Double.parseDouble(array[1]));
            loc.put("lng", Double.parseDouble(array[2]));
            Map<String, Object> cond = new HashMap<String, Object>();
            cond.put("uid", array[0]);
            mongoDao.update(cond, map);
            System.out.println(map);
        }
    }

    public static void importToRedisFromTxt(String fileName) throws Exception {
        ServerConfig config = ServerConfig.getServerConfig();

        IRedisDao redisDao = RedisDaoFactory.getRedisDao(config.getRedisHost(), config.getRedisPort());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        LineIterator lines = FileUtils.lineIterator(new File(ImportUtils.class.getClassLoader().getResource(fileName).getFile()));
        LineIterator lines = FileUtils.lineIterator(new File(fileName));
        StringBuilder sb = new StringBuilder();
        while (lines.hasNext()) {
            String line = lines.nextLine();
            sb.append(line);

            if (StringUtils.equalsIgnoreCase("},", line)) {
                sb.deleteCharAt(sb.length() - 1);

                Map<String, Object> map = JsonUtils.toMap(sb.toString());

                System.out.println(sb);
                System.out.println(map);

                redisDao.set("user:" + map.get("uid") + ":profile", JsonUtils.toJSON(map));

                sb = new StringBuilder();

            }


        }
    }

    public static void main (String[] args) throws Exception {
        if (args.length == 0) {
            importToMongoFromTxt("snowball_location.txt");
//          importToRedisFromTxt("snowball_user.json");
            return;
        }

        if (StringUtils.equalsIgnoreCase("redis", args[0])) {
            importToRedisFromTxt(args[1]);
        }

        if (StringUtils.equalsIgnoreCase("mongo", args[0])) {
            importToMongoFromTxt(args[1]);
        }

    }
}
