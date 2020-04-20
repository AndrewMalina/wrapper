package test.producer.services.impl;

import com.google.gson.Gson;
import com.postx.sdk.envelope.Payload;
import com.postx.sdk.envelope.TOC;
import com.postx.sdk.envelope.local.LocalEnvelopeDecrypter;
import com.postx.sdk.keyserver.LoginSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import test.producer.component.LoginSessionHolder;
import test.producer.dto.DecrypteMsgDto;
import test.producer.model.Attachment;
import test.producer.services.DecryptEnvelopeService;

import javax.ws.rs.NotAuthorizedException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class DecryptEnvelopeServiceImpl implements DecryptEnvelopeService {
    private final static Logger logger = Logger.getLogger(DecryptEnvelopeServiceImpl.class);

    private final static String dataParameter = "user";

    @Override
    public String decryptEnvelope(String toEmail, String password, String body, LoginSessionHolder loginSessionHolder) throws Exception {
        LocalEnvelopeDecrypter localEnvelopeDecrypter = new LocalEnvelopeDecrypter();

        if (password == null && toEmail == null) {
            if (loginSessionHolder != null) {
                LoginSession loginSession = loginSessionHolder.getLoginSession();
                try {
                    localEnvelopeDecrypter.setLoginSession(loginSession);
                    localEnvelopeDecrypter.setRecipient(loginSession.getData()
                            .get(dataParameter).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (body != null) {
                    localEnvelopeDecrypter.setEnvelope(body.getBytes());
                }
                return decrypt(localEnvelopeDecrypter);
            } else {
                throw new InvalidParameterException("Bad credential");
            }
        }

        if (toEmail != null && password == null) {
            throw new NotAuthorizedException("unauthorized exception");
        }

        if (toEmail == null) {
            throw new NotAuthorizedException("unauthorized exception");
        }
        localEnvelopeDecrypter.setPassword(password);
        localEnvelopeDecrypter.setRecipient(toEmail);
        if (body != null) {
            localEnvelopeDecrypter.setEnvelope(body.getBytes());
        }
        return decrypt(localEnvelopeDecrypter);
    }

    private String decrypt(LocalEnvelopeDecrypter localEnvelopeDecrypter) throws Exception {
        Payload payload;
        try {
            payload = localEnvelopeDecrypter.decrypt();
        } catch (Exception e) {
            throw new InvalidParameterException("Bad secure doc.");
        }
        TOC[] data = payload.getData();
        if (data == null) {
            logger.error(payload.getErrorMessage());
            throw new RuntimeException(payload.getErrorMessage());
        }
        if (data[0].getData() == null || data[1].getData() == null) {
            return payload.getErrorMessage();
        }
        try {
            return buildResponce(data);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    private String buildResponce(TOC[] data) throws UnsupportedEncodingException {
        Gson gson = new Gson();

        List<TOC> list = Arrays.asList(data);

        List<Attachment> attachments = new ArrayList<>();
        DecrypteMsgDto responce = new DecrypteMsgDto();
        responce.setBody(new String(list.get(list.size() - 1).getData(), "utf-16").replace("$[0]", new String(list.get(0).getData(), "utf-16")));

        for (int i = 1; i < (list.size() - 1); i++) {
            Attachment attachment = new Attachment();

            attachment.setFileName(list.get(i).getOrigFileName());
            attachment.setFileContent(Base64.encodeBase64String((list.get(i).getData())));
            attachments.add(attachment);
        }
        responce.setAttachments(attachments);
        return gson.toJson(responce);
    }
}
