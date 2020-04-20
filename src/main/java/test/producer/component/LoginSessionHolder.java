package test.producer.component;

import com.postx.sdk.keyserver.LoginSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginSessionHolder {
    private LoginSession loginSession;

    public LoginSession getLoginSession() {
        return loginSession;
    }

    public void setLoginSession(LoginSession loginSession) {
        this.loginSession = loginSession;
    }
}
