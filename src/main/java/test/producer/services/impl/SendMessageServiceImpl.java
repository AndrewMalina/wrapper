package test.producer.services.impl;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;
import test.producer.dto.EmailDto;
import test.producer.services.SendMessageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@PropertySource("classpath:local_offline_mbar_boot.properties")
public class SendMessageServiceImpl implements SendMessageService {
    private final static Logger logger = Logger.getLogger(SendMessageServiceImpl.class);

    private static String link;

    @Value("${send.messages.service.link}")
    public  void setLink(String link) {
        SendMessageServiceImpl.link = link;
    }

    @Override
    public Boolean sendMessage(EmailDto emailDto) {
        HttpURLConnection connection = null;
        Gson gson = new Gson();

        try {
            //Create connection
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(gson.toJson(emailDto).getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Accept", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(gson.toJson(emailDto));
            wr.close();


            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return new Boolean(response.toString());
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
