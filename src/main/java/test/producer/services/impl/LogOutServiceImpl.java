package test.producer.services.impl;

import org.springframework.stereotype.Service;
import test.producer.component.LoginSessionHolder;
import test.producer.data.SessionHashMapSingleton;
import test.producer.services.LogOutService;

@Service
public class LogOutServiceImpl implements LogOutService {
    @Override
    public void logOut(LoginSessionHolder loginSessionHolder) {
        if (loginSessionHolder.getLoginSession() != null) {
            SessionHashMapSingleton.INSTANCE.getLoginSessionMap().remove(loginSessionHolder.getLoginSession().getData().get("user").toString());
        }
    }
}
