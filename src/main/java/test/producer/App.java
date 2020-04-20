package test.producer;

import com.postx.sdk.common.local.LocalConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Component
@PropertySource("classpath:local_offline_mbar_boot.properties")
public class App extends SpringBootServletInitializer {
    private final static Logger logger = Logger.getLogger(App.class);

    @Value("${config.path}")
    String configPath;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    void init() {
        try {
            LocalConfig.setConfigPath(configPath);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}