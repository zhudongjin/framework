package com.hstypay.framework.cache;

import com.hstypay.framework.cache.support.ConfigLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CacheConfig {
    public static final String WHALIN_MEMCACHED_DRIVER="whalin-java-memcached";
    public static final String EHCACHE_DRIVER="ehcache";
    public static final String REDIS_DRIVER="jedis";

    /* =============cache的配置项================ */
    public static final String KEY_CACHE_FAIL_THROWEXP = "failThrowExeption";

    // 获取配置的key值
    // cache是否启用
    public static final String KEY_OPEN = "open";
    // cache客户端
    public static final String KEY_DRIVER = "driver";
    // 主机列表
    public static final String KEY_HOST_LIST = "hostList";
    // 主机权重
    public static final String KEY_HOST_WEIGHTS = "weights";
    // 协议binary/text
    public static final String KEY_PROTOCOL = "protocol";
    // 连接池
    public static final String KEY_CONN_POOL_SIZE = "connectionPoolSize";
    public static final String KEY_INIT_CONN_SIZE = "initConnPoolSize";
    public static final String KEY_MIN_CONN_SIZE = "minConnPoolSize";
    public static final String KEY_MAINT_SLEEP_TM = "maintSleepTime";
    public static final String KEY_MAX_IDEL_TM = "maxIdleTime";
    public static final String KEY_MAX_BUSY_TM = "maxBusyTime";
    public static final String KEY_CONN_TIMEOUT = "connectTimeout";
    public static final String KEY_OP_TIMEOUT = "operateTimeout";
    public static final String KEY_MAX_QUEUE_NOREPLY = "maxQueuedNoReply";
    public static final String KEY_WITH_NOREPLY = "withNoReply";
    public static final String KEY_FAILURE_MODE = "failureMode";
    public static final String KEY_ENCODE_KEY = "urlEncodeKey";
    public static final String KEY_COMPRESS_THD = "compressionThreshold";
    public static final String KEY_ALIVE_CHK = "aliveCheck";
    public static final String KEY_USE_TCP = "useTcp";
    public static final String KEY_USE_NAGLE = "useNagle";
    // 重连的时间间隔，毫秒
    public static final String KEY_HEAL_SESSION_INTERVAL = "healSessionInterval";

    public static final String KEY_AUTH_FLAG = "useAuth";
    public static final String KEY_AUTH_USER = "authInfo.user";
    public static final String KEY_AUTH_PWD = "authInfo.password";
    // 节点选择算法
    public static final String KEY_SESSION_SELECTOR = "sessionSelector";
    public static final String KEY_DATA_ASSTRING = "dataAsString";
    public static final String KEY_DATA_CHARSET = "dataCharset";
    public static final String KEY_TRACE_OPT = "traceOperation";
    public static final String KEY_PRINT_CATCHINFO = "printCatchInfo";

    //ehcache配置
    public static final String KEY_MAX_ELEMENTS_IN_MEM = "maxElementsInMemory";
    public static final String KEY_MAX_ELEMENTS_IN_DISK = "maxElementsOnDisk";
    public static final String KEY_ETERNAL = "eternal";
    public static final String KEY_OVERFLOA_TODISK = "overflowToDisk";
    public static final String KEY_DISK_PERSISTENT = "diskPersistent";
    public static final String KEY_IDLE_TM = "timeToIdleSeconds";
    public static final String KEY_LIVE_TM = "timeToLiveSeconds";
    public static final String KEY_BUFF_SIZE = "diskSpoolBufferSizeMB";
    public static final String KEY_EXPIRY_INTERVAL = "diskExpiryThreadIntervalSeconds";
    public static final String KEY_STORE_POLICY = "memoryStoreEvictionPolicy";
    public static final String KEY_DISK_PATH = "diskStorePath";

    //redis配置
    public static final String KEY_REDIS_POOL_MAX_TOTAL = "pool.maxTotal";
    public static final String KEY_REDIS_POOL_MAX_IDLE = "pool.maxIdle";
    public static final String KEY_REDIS_POOL_MAX_WAIT_MILLIS = "pool.maxWaitMillis";
    public static final String KEY_REDIS_POOL_TEST_ON_BORROW = "pool.testOnBorrow";
    public static final String KEY_REDIS_POOL_TEST_ON_RETURN = "pool.testOnReturn";
    public static final String KEY_REDIS_IP = "ip";
    public static final String KEY_REDIS_PORT = "port";
    public static final String KEY_REDIS_PASS = "pass";
    public static final String KEY_REDIS_TIMEOUT = "timeout";
    public static final String KEY_REDIS_DB_INDEX = "dbIndex";


	/* ================cache的配置项============= */

    private final Logger logger = LoggerFactory.getLogger(getClass());
    // 失败是否抛出异常
    private boolean failThrowExeption = true;
    // cache client名称
    private String clientName;
    // cache是否启用
    private boolean open = false;
    // cache客户端名
    private String driver;
    // 主机列表
    private String[] servers;
    // 主机权重
    private int[] weigths;
    // 访问协议类型，binary/text
    private String protocol = "text";
    // 连接池大小
    private int connectionPoolSize = 50;
    // 初始大小
    private int initConnSize = 10;
    // 最小连接大小
    private int minConnSize = 10;
    // 设置主线程睡眠时间，每maintSleep秒苏醒一次，维持连接池大小，毫秒
    private long maintSleepTime = 1000 * 30;
    // 最大空闲时间,毫秒
    private long maxIdleTime = 1000 * 60 * 60;
    /**
     * 最长租用时间，其使用主要有两点，一是自查线程会检查正在被租用的连接，
     * 如果发现已经被租用的时间超过这个值得，会将其从被租用的记录里剔除，并关闭这个连接；另一个应用是
     * 上层进行MUTIL操作时，读取所有的数据的时间不能超过这个时间。
     */
    private long maxBusyTime = 1000 * 60 * 60;

    // 访问失败是否访问列表中下一主机
    private boolean useFailover = true;
    // 是否使用socket的nagle算法
    private boolean useNagleAlg = false;
    // 节点选择器（多个节点时提供选择算法）
    private String sessionSelector;
    // 读取时超时时间，毫秒，默认5s
    private int operateTimeout = 5000;
    // 连接超时时间，毫秒,默认5s
    private int connectTimeout = 5000;
    // 是否对key进行url encode
    private boolean urlEncodeKey;
    // 超过域值则压缩存储,单位字节,默认16384(16K)
    private int compressionThreshold = 16384;
    // 是否使用NOREPLY模式
    private boolean withNoReply = false;
    // 是否检查连接可用
    private boolean aliveCheck = true;
    // 是否使用TCP传输
    private boolean useTcp = true;
    // 重连的时间间隔，毫秒
    private int healSessionInterval = -1;
    //是否启用SASL验证
    private boolean useAuth = false;
    private String user;
    private String password;
    //是否将数据作为String原始类型，如果为true,则写入cache或获取cache时直接按字串类型来处理
    private boolean dataAsString = false;
    private String dataCharset = "utf-8";
    private int maxQueuedNoReply;
    //是否打印操作的key和结果信息用于调试跟踪
    private boolean traceOperation = false;
    private boolean printCatchInfo = false;

    //ehcache配置
    private int maxElementsInMemory;
    private int maxElementsOnDisk;
    private boolean eternal;
    private boolean overflowToDisk;
    private boolean diskPersistent;
    private long timeToIdleSeconds;
    private long timeToLiveSeconds;
    private int diskSpoolBufferSizeMB;
    private long diskExpiryThreadIntervalSeconds;
    private String memoryStoreEvictionPolicy;
    private String diskStorePath;

    //redis配置
    private int maxTotal;
    private int maxIdle;
    private long maxWaitMillis;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private String redisIp;
    private int redisPort;
    private String redisPass;
    private int redisTimeout;
    private int redisDbIndex;

    // 配置信息
    private Properties confProperties;
    private String confFileName;

    public CacheConfig(String clientName) {
        setClientName(clientName);
        initConfig();
    }

    public CacheConfig(String configFileName, String clientName) {
        setConfFileName(configFileName);
        setClientName(clientName);
        initConfig();
    }

    public CacheConfig(Properties confProperties, String clientName) {
        setClientName(clientName);
        setConfProperties(confProperties);
        initConfig();
    }

    public void initConfig() {
        ConfigLoader cl = new ConfigLoader(getClientName());
        if (getConfProperties() != null)
            cl.setConfProperties(getConfProperties());
        else {
            cl.loadConfig(getConfFileName());
        }

        this.failThrowExeption = cl.getBoolConfItem(KEY_CACHE_FAIL_THROWEXP, true);
        this.driver = cl.getStringConfItem(KEY_DRIVER, null);
        this.open = cl.getBoolConfItem(KEY_OPEN, false);
        String hostList = cl.getStringConfItem(KEY_HOST_LIST, null);
        this.servers = StringUtils.split(hostList, ",");
        String weigthList = cl.getStringConfItem(KEY_HOST_WEIGHTS, null);
        String[] weigthArray = StringUtils.split(weigthList, ",");
        if (weigthArray != null && weigthArray.length > 0) {
            this.weigths = new int[weigthArray.length];
            for (int i = 0; i < weigthArray.length; ++i) {
                int w = 1;
                try {
                    w = Integer.parseInt(weigthArray[i]);
                } catch (Exception e) {
                    logger.error("parse host weigths({}) error:{}", weigthArray[i], e.toString());
                }

                weigths[i] = w;
            }
        }

        this.protocol= cl.getStringConfItem(KEY_PROTOCOL, "text");
        this.connectionPoolSize = cl.getIntConfItem(KEY_CONN_POOL_SIZE, 32);
        this.initConnSize = cl.getIntConfItem(KEY_INIT_CONN_SIZE, 10);
        this.minConnSize = cl.getIntConfItem(KEY_MIN_CONN_SIZE, 5);
        this.maintSleepTime = cl.getLongConfItem(KEY_MAINT_SLEEP_TM, 1000 * 30);
        this.maxIdleTime = cl.getLongConfItem(KEY_MAX_IDEL_TM, 1000 * 60 * 60);
        this.maxBusyTime = cl.getLongConfItem(KEY_MAX_BUSY_TM, 1000 * 30);
        this.useFailover = cl.getBoolConfItem(KEY_FAILURE_MODE, true);
        this.useNagleAlg = cl.getBoolConfItem(KEY_USE_NAGLE, false);
        this.sessionSelector = cl.getStringConfItem(KEY_SESSION_SELECTOR, null);
        this.operateTimeout = cl.getIntConfItem(KEY_OP_TIMEOUT, 5000);
        this.connectTimeout = cl.getIntConfItem(KEY_CONN_TIMEOUT, 5000);
        this.urlEncodeKey = cl.getBoolConfItem(KEY_ENCODE_KEY, true);
        this.compressionThreshold = cl.getIntConfItem(KEY_COMPRESS_THD, 16384);
        this.withNoReply = cl.getBoolConfItem(KEY_WITH_NOREPLY, false);
        this.aliveCheck = cl.getBoolConfItem(KEY_ALIVE_CHK, true);
        this.useTcp = cl.getBoolConfItem(KEY_USE_TCP, true);
        this.healSessionInterval = cl.getIntConfItem(KEY_HEAL_SESSION_INTERVAL, -1);
        this.useAuth = cl.getBoolConfItem(KEY_AUTH_FLAG, false);
        this.user = cl.getStringConfItem(KEY_AUTH_USER, null);
        this.password = cl.getStringConfItem(KEY_AUTH_PWD, null);
        this.dataAsString = cl.getBoolConfItem(KEY_DATA_ASSTRING, false);
        this.dataCharset = cl.getStringConfItem(KEY_DATA_CHARSET, "utf-8");
        this.maxQueuedNoReply = cl.getIntConfItem(KEY_MAX_QUEUE_NOREPLY, -1);
        this.traceOperation = cl.getBoolConfItem(KEY_TRACE_OPT, false);
        this.printCatchInfo = cl.getBoolConfItem(KEY_PRINT_CATCHINFO, false);

        //ehcache配置
        this.maxElementsInMemory = cl.getIntConfItem(KEY_MAX_ELEMENTS_IN_MEM, Integer.MAX_VALUE);
        this.maxElementsOnDisk = cl.getIntConfItem(KEY_MAX_ELEMENTS_IN_DISK, 0);
        this.eternal = cl.getBoolConfItem(KEY_ETERNAL, false);
        this.overflowToDisk = cl.getBoolConfItem(KEY_OVERFLOA_TODISK, false);
        this.diskPersistent = cl.getBoolConfItem(KEY_DISK_PERSISTENT, false);
        this.timeToIdleSeconds = cl.getLongConfItem(KEY_IDLE_TM, 0);
        this.timeToLiveSeconds = cl.getLongConfItem(KEY_LIVE_TM, 0);
        this.diskSpoolBufferSizeMB = cl.getIntConfItem(KEY_BUFF_SIZE, 30);
        this.diskExpiryThreadIntervalSeconds = cl.getLongConfItem(KEY_EXPIRY_INTERVAL, 120);
        this.memoryStoreEvictionPolicy = cl.getStringConfItem(KEY_STORE_POLICY, "LRU");
        this.diskStorePath = cl.getStringConfItem(KEY_DISK_PATH, null);

        // redis 配置
        this.maxTotal = cl.getIntConfItem(KEY_REDIS_POOL_MAX_TOTAL,100);
        this.maxIdle = cl.getIntConfItem(KEY_REDIS_POOL_MAX_IDLE, 10);
        this.maxWaitMillis = cl.getIntConfItem(KEY_REDIS_POOL_MAX_WAIT_MILLIS, 1000);
        this.testOnBorrow = cl.getBoolConfItem(KEY_REDIS_POOL_TEST_ON_BORROW, false);
        this.testOnReturn = cl.getBoolConfItem(KEY_REDIS_POOL_TEST_ON_RETURN, false);
        this.redisIp = cl.getStringConfItem(KEY_REDIS_IP, "127.0.0.1");
        this.redisPort = cl.getIntConfItem(KEY_REDIS_PORT, 0);
        this.redisPass = cl.getStringConfItem(KEY_REDIS_PASS, "");
        this.redisTimeout = cl.getIntConfItem(KEY_REDIS_TIMEOUT, 100000);
        this.redisDbIndex = cl.getIntConfItem(KEY_REDIS_DB_INDEX, 0);
    }

    public Properties getConfProperties() {
        return confProperties;
    }

    public void setConfProperties(Properties confProperties) {
        this.confProperties = confProperties;
    }

    public String getConfFileName() {
        return confFileName;
    }

    public void setConfFileName(String confFileName) {
        this.confFileName = confFileName;
    }

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

    public boolean isUseFailover() {
        return useFailover;
    }

    public void setUseFailover(boolean useFailover) {
        this.useFailover = useFailover;
    }

    public int[] getWeigths() {
        return weigths;
    }

    public void setWeigths(int[] weigths) {
        this.weigths = weigths;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public int getInitConnSize() {
        return initConnSize;
    }

    public void setInitConnSize(int initConnSize) {
        this.initConnSize = initConnSize;
    }

    public int getMinConnSize() {
        return minConnSize;
    }

    public void setMinConnSize(int minConnSize) {
        this.minConnSize = minConnSize;
    }

    public long getMaintSleepTime() {
        return maintSleepTime;
    }

    public void setMaintSleepTime(long maintSleepTime) {
        this.maintSleepTime = maintSleepTime;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getOperateTimeout() {
        return operateTimeout;
    }

    public void setOperateTimeout(int operateTimeout) {
        this.operateTimeout = operateTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isUrlEncodeKey() {
        return urlEncodeKey;
    }

    public void setUrlEncodeKey(boolean urlEncodeKey) {
        this.urlEncodeKey = urlEncodeKey;
    }

    public int getCompressionThreshold() {
        return compressionThreshold;
    }

    public void setCompressionThreshold(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    public boolean isWithNoReply() {
        return withNoReply;
    }

    public void setWithNoReply(boolean withNoReply) {
        this.withNoReply = withNoReply;
    }

    public boolean isAliveCheck() {
        return aliveCheck;
    }

    public void setAliveCheck(boolean aliveCheck) {
        this.aliveCheck = aliveCheck;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String cacheName) {
        this.clientName = cacheName;
    }

    public boolean isUseTcp() {
        return useTcp;
    }

    public void setUseTcp(boolean useTcp) {
        this.useTcp = useTcp;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public long getMaxBusyTime() {
        return maxBusyTime;
    }

    public void setMaxBusyTime(long maxBusyTime) {
        this.maxBusyTime = maxBusyTime;
    }

    public boolean isUseNagleAlg() {
        return useNagleAlg;
    }

    public void setUseNagleAlg(boolean useNagleAlg) {
        this.useNagleAlg = useNagleAlg;
    }

    public boolean isFailThrowExeption() {
        return failThrowExeption;
    }

    public void setFailThrowExeption(boolean failThrowExeption) {
        this.failThrowExeption = failThrowExeption;
    }

    public String getSessionSelector() {
        return sessionSelector;
    }

    public void setSessionSelector(String sessionSelector) {
        this.sessionSelector = sessionSelector;
    }

    public int getHealSessionInterval() {
        return healSessionInterval;
    }

    public void setHealSessionInterval(int healSessionInterval) {
        this.healSessionInterval = healSessionInterval;
    }

    public boolean isUseAuth() {
        return useAuth;
    }

    public void setUseAuth(boolean useAuth) {
        this.useAuth = useAuth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDataAsString() {
        return dataAsString;
    }

    public void setDataAsString(boolean dataAsString) {
        this.dataAsString = dataAsString;
    }

    public String getDataCharset() {
        return dataCharset;
    }

    public void setDataCharset(String dataCharset) {
        this.dataCharset = dataCharset;
    }

    public int getMaxQueuedNoReply() {
        return maxQueuedNoReply;
    }

    public void setMaxQueuedNoReply(int maxQueuedNoReply) {
        this.maxQueuedNoReply = maxQueuedNoReply;
    }

    public boolean isTraceOperation() {
        return traceOperation;
    }

    public void setTraceOperation(boolean traceOperation) {
        this.traceOperation = traceOperation;
    }

    public boolean isPrintCatchInfo() {
        return printCatchInfo;
    }

    public void setPrintCatchInfo(boolean printCatchInfo) {
        this.printCatchInfo = printCatchInfo;
    }

    public int getMaxElementsInMemory() {
        return maxElementsInMemory;
    }

    public void setMaxElementsInMemory(int maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
    }

    public int getMaxElementsOnDisk() {
        return maxElementsOnDisk;
    }

    public void setMaxElementsOnDisk(int maxElementsOnDisk) {
        this.maxElementsOnDisk = maxElementsOnDisk;
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public boolean isOverflowToDisk() {
        return overflowToDisk;
    }

    public void setOverflowToDisk(boolean overflowToDisk) {
        this.overflowToDisk = overflowToDisk;
    }

    public boolean isDiskPersistent() {
        return diskPersistent;
    }

    public void setDiskPersistent(boolean diskPersistent) {
        this.diskPersistent = diskPersistent;
    }

    public long getTimeToIdleSeconds() {
        return timeToIdleSeconds;
    }

    public void setTimeToIdleSeconds(long timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public long getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(long timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public int getDiskSpoolBufferSizeMB() {
        return diskSpoolBufferSizeMB;
    }

    public void setDiskSpoolBufferSizeMB(int diskSpoolBufferSizeMB) {
        this.diskSpoolBufferSizeMB = diskSpoolBufferSizeMB;
    }

    public long getDiskExpiryThreadIntervalSeconds() {
        return diskExpiryThreadIntervalSeconds;
    }

    public void setDiskExpiryThreadIntervalSeconds(long diskExpiryThreadIntervalSeconds) {
        this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
    }

    public String getMemoryStoreEvictionPolicy() {
        return memoryStoreEvictionPolicy;
    }

    public void setMemoryStoreEvictionPolicy(String memoryStoreEvictionPolicy) {
        this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
    }

    public String getDiskStorePath() {
        return diskStorePath;
    }

    public void setDiskStorePath(String diskStorePath) {
        this.diskStorePath = diskStorePath;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getRedisIp() {
        return redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPass() {
        return redisPass;
    }

    public void setRedisPass(String redisPass) {
        this.redisPass = redisPass;
    }

    public int getRedisTimeout() {
        return redisTimeout;
    }

    public void setRedisTimeout(int redisTimeout) {
        this.redisTimeout = redisTimeout;
    }

    public int getRedisDbIndex() {
        return redisDbIndex;
    }

    public void setRedisDbIndex(int redisDbIndex) {
        this.redisDbIndex = redisDbIndex;
    }
}