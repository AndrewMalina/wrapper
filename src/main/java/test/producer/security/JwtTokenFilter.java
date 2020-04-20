package test.producer.security;

import com.postx.sdk.keyserver.LoginSession;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import test.producer.component.LoginSessionHolder;
import test.producer.data.SessionHashMapSingleton;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.util.Map;

public class JwtTokenFilter extends GenericFilterBean {
    private final static Logger logger = Logger.getLogger(JwtTokenFilter.class);


    private JwtTokenProvider jwtTokenProvider;

    private LoginSessionHolder loginSessionHolder;

    JwtTokenFilter(JwtTokenProvider jwtTokenProvider, LoginSessionHolder loginSessionHolder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String str = jwtTokenProvider.getUsername(token);
                Map<String, LoginSession> loginSessionMap = SessionHashMapSingleton.INSTANCE.getLoginSessionMap();

                LoginSession loginSession = loginSessionMap.get(str);
                if(loginSession == null){
                    ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
                } else {
                    loginSessionHolder.setLoginSession(loginSession);
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    filterChain.doFilter(req, res);
                }
            } else {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage(), e);
            try{
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
            } catch(IOException ioe) {
                throw new NotAuthorizedException("Token exception");
            }
        }
    }
}
