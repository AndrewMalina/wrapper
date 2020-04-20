package test.producer.services.impl;

import com.postx.sdk.keyserver.LoginSession;
import com.postx.sdk.keyserver.local.LocalLogin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.producer.data.SessionHashMapSingleton;
import test.producer.model.Role;
import test.producer.security.JwtTokenProvider;
import test.producer.services.LoginService;

import javax.ws.rs.NotAuthorizedException;
import java.util.Collections;

@Service
public class LoginServiceImpl implements LoginService {
    private final static Logger logger = Logger.getLogger(LoginServiceImpl.class);

    private final static String status = "status";

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(String username, String password) {
        LocalLogin localLogin = new LocalLogin();
        try {
            if (username != null && password != null) {
                localLogin.setEnvelopeConfigContent(new GetEnvelopeConfigImpl().getEnvelopeConfig());
                localLogin.setUser(username);
                localLogin.setPassword(password);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        LoginSession loginSession = localLogin.authenticate();

        if (loginSession.getStatus() != 0) {
            throw new NotAuthorizedException(loginSession.getErrorMessage());
        }

        SessionHashMapSingleton.INSTANCE.setLoginSessionMap(username, loginSession);

        return jwtTokenProvider.createToken(username, Collections.singletonList(Role.ROLE_CLIENT));
    }
}
