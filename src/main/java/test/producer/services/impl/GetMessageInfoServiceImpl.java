package test.producer.services.impl;

import com.google.gson.Gson;
import com.postx.sdk.message.MessageException;
import com.postx.sdk.message.MessageInformation;
import com.postx.sdk.message.RecipientInformation;
import com.postx.sdk.message.local.LocalGetMessageInformation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import test.producer.component.LoginSessionHolder;
import test.producer.dto.RecipientInformationDto;
import test.producer.services.GetMessageInfoService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@PropertySource("classpath:local_offline_mbar_boot.properties")
public class GetMessageInfoServiceImpl implements GetMessageInfoService {

    private final static Logger logger = Logger.getLogger(GetMessageInfoServiceImpl.class);

    private final static String clientType = "CRES";

    @Override
    public String getMessageInfo(LoginSessionHolder loginSessionHolder,
                                 String sortedField, Integer maxResult,
                                 Boolean sortedAscending,
                                 Integer startAt) throws Exception {

        LocalGetMessageInformation localGetMessageInformation = new LocalGetMessageInformation();

        Gson gson = new Gson();
        try {
            setInfo(loginSessionHolder, localGetMessageInformation);

            if (sortedField != null) {
                localGetMessageInformation.setSortField(sortedField);
            }
            if (maxResult != null) {
                localGetMessageInformation.setMaxResults(maxResult);
            }
            if (sortedAscending != null) {
                localGetMessageInformation.setSortAscending(sortedAscending);
            }
            if (startAt != null) {
                localGetMessageInformation.setStartAt(startAt);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        MessageInformation messageInformation;
        try {
            messageInformation = localGetMessageInformation.getMessageInformation();
            if (messageInformation.getStatus() != 0) {
                logger.error(messageInformation.getErrorMessage());
                throw new Exception(messageInformation.getErrorMessage());
            }
        } catch (MessageException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        List<RecipientInformation> recipients = new ArrayList<>();
        if (messageInformation != null) {
            recipients = Arrays.asList(messageInformation.getRecipients());
        }

        List<RecipientInformationDto> recipientInformationDtoList = new ArrayList<>();
        for (RecipientInformation recipient : recipients) {
            RecipientInformationDto dto =
                    new RecipientInformationDto(recipient.getMessageId(),
                            recipient.getEmail(),
                            recipient.getSubject(),
                            recipient.getCreateDate(),
                            recipient.getFirstOpenDate(),
                            recipient.getExpiryDate(),
                            recipient.isLocked(),
                            recipient.getStatusMessage(),
                            recipient.isExpired(),
                            recipient.getFailedAttempts());
            recipientInformationDtoList.add(dto);
        }
        return gson.toJson(recipientInformationDtoList);
    }

    private void setInfo(LoginSessionHolder loginSessionHolder,
                         LocalGetMessageInformation localGetMessageInformation) throws Exception {
        localGetMessageInformation.setEnvelopeConfigContent(
                new GetEnvelopeConfigImpl().getEnvelopeConfig());
        localGetMessageInformation.setLoginSession(loginSessionHolder.getLoginSession());
        localGetMessageInformation.setClientType(clientType);
    }
}
