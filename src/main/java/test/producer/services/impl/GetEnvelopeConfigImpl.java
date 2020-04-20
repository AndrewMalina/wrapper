package test.producer.services.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;
import test.producer.services.GetEnvelopeConfigService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@PropertySource("classpath:local_offline_mbar_boot.properties")
public class GetEnvelopeConfigImpl implements GetEnvelopeConfigService {
    private final static Logger logger = Logger.getLogger(GetEnvelopeConfigImpl.class);

    private static String link;

    @Value("${envelope.config.link}")
    public void setLink(String link) {
        GetEnvelopeConfigImpl.link = link;
    }

    @Override
    public String getEnvelopeConfig() {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setUseCaches(false);
            connection.setDoOutput(false);

            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}