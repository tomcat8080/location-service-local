/**
 * 
 */
package com.zank.search;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.beans.PropertyDescriptor;

/**
 * @author yan.liu
 *
 */
public class ServerConfig {

    private static final ServerConfig serverConfig = new ServerConfig();

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

    private ServerConfig() {
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration("server.properties");
            PropertyDescriptor[] propertiesArray = PropertyUtils.getPropertyDescriptors(ServerConfig.class);
            for (PropertyDescriptor propertyDescriptor : propertiesArray) {
                Class<?> type = propertyDescriptor.getPropertyType();
                String name = propertyDescriptor.getName();

                if (type == int.class) {
                    PropertyUtils.setProperty(this, name, configuration.getInt(name));
                } else if (type == String.class) {
                    PropertyUtils.setProperty(this, name, configuration.getString(name));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String mongoHost;
    private int mongoPort;
    private String mongoDbName;
    private String mongoColName;
    private String mongoKeyName;

    private int httpServerPort;
    private String redisSearchAction;

    private int redisPort;
    private String redisHost;
    private String redisQueueKey;
    private String redisQueueRemoveAction;
    private String redisQueueUpdateAction;

    public String getRedisQueueKey() {
        return redisQueueKey;
    }

    public void setRedisQueueKey(String redisQueueKey) {
        this.redisQueueKey = redisQueueKey;
    }

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }

    public String getMongoColName() {
        return mongoColName;
    }

    public void setMongoColName(String mongoColName) {
        this.mongoColName = mongoColName;
    }

    public int getHttpServerPort() {
        return httpServerPort;
    }

    public void setHttpServerPort(int httpServerPort) {
        this.httpServerPort = httpServerPort;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisSearchAction() {
        return redisSearchAction;
    }

    public void setRedisSearchAction(String redisSearchAction) {
        this.redisSearchAction = redisSearchAction;
    }

    public String getRedisQueueRemoveAction() {
        return redisQueueRemoveAction;
    }

    public void setRedisQueueRemoveAction(String redisQueueRemoveAction) {
        this.redisQueueRemoveAction = redisQueueRemoveAction;
    }

    public String getRedisQueueUpdateAction() {
        return redisQueueUpdateAction;
    }

    public void setRedisQueueUpdateAction(String redisQueueUpdateAction) {
        this.redisQueueUpdateAction = redisQueueUpdateAction;
    }


    public String getMongoKeyName() {
        return mongoKeyName;
    }

    public void setMongoKeyName(String mongoKeyName) {
        this.mongoKeyName = mongoKeyName;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "mongoHost='" + mongoHost + '\'' +
                ", mongoPort=" + mongoPort +
                ", mongoDbName='" + mongoDbName + '\'' +
                ", mongoColName='" + mongoColName + '\'' +
                ", mongoKeyName='" + mongoKeyName + '\'' +
                ", httpServerPort=" + httpServerPort +
                ", redisSearchAction='" + redisSearchAction + '\'' +
                ", redisPort=" + redisPort +
                ", redisHost='" + redisHost + '\'' +
                ", redisQueueKey='" + redisQueueKey + '\'' +
                ", redisQueueRemoveAction='" + redisQueueRemoveAction + '\'' +
                ", redisQueueUpdateAction='" + redisQueueUpdateAction + '\'' +
                '}';
    }

    public static void main(String[] args) {
        ServerConfig config = getServerConfig();
        System.out.println(config);
    }

}
