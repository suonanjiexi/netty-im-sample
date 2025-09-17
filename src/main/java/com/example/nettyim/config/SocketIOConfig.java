package com.example.nettyim.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * SocketIO配置
 */
@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    
    @Value("${socketio.host:localhost}")
    private String host;
    
    @Value("${socketio.port:8081}")
    private Integer port;
    
    @Value("${socketio.bossCount:1}")
    private Integer bossCount;
    
    @Value("${socketio.workCount:100}")
    private Integer workCount;
    
    @Value("${socketio.allowCustomRequests:true}")
    private Boolean allowCustomRequests;
    
    @Value("${socketio.upgradeTimeout:1000000}")
    private Integer upgradeTimeout;
    
    @Value("${socketio.pingTimeout:6000000}")
    private Integer pingTimeout;
    
    @Value("${socketio.pingInterval:25000}")
    private Integer pingInterval;
    
    // Redis配置
    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;
    
    @Value("${spring.data.redis.port:6379}")
    private Integer redisPort;
    
    @Value("${spring.data.redis.database:0}")
    private Integer redisDatabase;
    
    @Value("${socketio.cluster.enabled:false}")
    private Boolean clusterEnabled;
    
    @Bean
    @Primary
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setDatabase(redisDatabase)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(5)
                .setConnectTimeout(3000)
                .setTimeout(3000);
        
        return Redisson.create(config);
    }
    
    @Bean
    public SocketIOServer socketIOServer(RedissonClient redissonClient) {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        
        // 支持跨域
        config.setOrigin("*");
        
        // 如果启用集群模式，使用Redis存储
        if (clusterEnabled) {
            config.setStoreFactory(new RedissonStoreFactory(redissonClient));
        }
        
        return new SocketIOServer(config);
    }
}