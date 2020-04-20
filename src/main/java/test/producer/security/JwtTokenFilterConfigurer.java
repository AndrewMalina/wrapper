package test.producer.security;

import org.apache.log4j.Logger;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.producer.component.LoginSessionHolder;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static Logger logger = Logger.getLogger(JwtTokenFilterConfigurer.class);


    private JwtTokenProvider jwtTokenProvider;

    private LoginSessionHolder loginSessionHolder;

    public JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider, LoginSessionHolder loginSessionHolder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider, loginSessionHolder);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
