package server.db;

/**
* Redis get|set implementation
**/

import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.Map;

public class RedisConnector {
    private Jedis cli;

    /**
     * constructor: connect to Redis server and authorization
     */
    public RedisConnector(String host, int port, String password) {
        cli = new Jedis(host, port, 5000);
        cli.auth(password);
        try {
            cli.connect();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * calculate keys count
     */
    public long getKeysCount() {
        return cli.dbSize();
    }

    /**
     * get all keys that begin with "begin" string
     */
    public Set<String> getAllKeys(String begin) {
        return cli.keys(begin+"*");
    }

    /**
     * set field values as map
     */
    public boolean setAll(String field, Map<String, String> m) {
        String r = cli.hmset(field, m);
        return (r.equals("OK"));
    }

    /**
     * set field values as map and set expire the session
     */
    public boolean setAll(String field, Map<String, String> m, int expire) {
        boolean rez = false;
        String r = cli.hmset(field, m);
        long er = cli.expire(field, expire);
        rez = (r.equals("OK") && er>0);
        return rez;
    }

    /**
     * set one of session values
     */
    public boolean set(String field, String key, String value) {
        boolean rez = false;
        Map m = getAll(field);
        if (m!=null) {
            m.put(key, value);
            rez = setAll(field, m);
        }
        return rez;
    }

    /**
     * set or update field values and set expire
     */
    public boolean set(String field, String key, String value, int expire) {
        boolean rez = false;
        Map m = getAll(field);
        if (m!=null) {
            m.put(key, value==null ? "" : value);
            rez = setAll(field, m, expire);
        }
        return rez;
    }

    /**
     * check if field exists
     */
    public boolean isExists(String field) {
        return cli.exists(field);
    }

    /**
     * get all field values
     */
    public Map<String, String> getAll(String field) {
        return cli.hgetAll(field);
    }

    /**
     * get field values and prolongs session
     */
    public Map<String, String> getAll(String field, int expire) {
        Map m = cli.hgetAll(field);
        cli.hmset(field, m);
        cli.expire(field, expire);
        return m;
    }

    /**
     * get one of field values
     */
    public String get(String field, String key) {
        return cli.hget(field, key);
    }

    /**
     * delete field with all data
     */
    public boolean del(String field) {
        Long del = cli.del(field);
        return del > 0;
    }

    /**
     * close connection
     */
    public void close() {
        if (cli.isConnected()) {
            cli.disconnect();
        }
    }
}

