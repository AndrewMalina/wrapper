package test.producer.data;

import com.postx.sdk.keyserver.LoginSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum SessionHashMapSingleton {

    INSTANCE;

    private ConcurrentMap<String, LoginSession> loginSessionMap = new ConcurrentHashMap<>();;

    public ConcurrentMap<String, LoginSession> getLoginSessionMap() {
        return loginSessionMap;
    }

    public void setLoginSessionMap(String userName, LoginSession loginSession) {
        this.loginSessionMap.put(userName, loginSession);
    }
}
