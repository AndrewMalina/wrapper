package test.producer.services.impl;

import com.postx.sdk.message.SetInformationResponse;
import com.postx.sdk.message.local.LocalSetMessageInformation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import sdk.swagger.model.SetMessageInfoExpiryDateDto;
import sdk.swagger.model.SetMessageInfoLockStatusDto;
import test.producer.component.LoginSessionHolder;
import test.producer.services.SetMessageInfoService;

import java.util.List;
import java.util.Objects;


@Service
public class SetMessageInfoServiceImpl implements SetMessageInfoService {
    private final static Logger logger = Logger.getLogger(SetMessageInfoServiceImpl.class);

    private final static String clientType = "CRES";
    private final static String dataParameter = "user";

    @Override
    public String setMessageInfoLockStatus(List<SetMessageInfoLockStatusDto> setMessageInfoLockStatus, LoginSessionHolder loginSessionHolder) throws Exception {

        SetInformationResponse messageInformation = null;
        try {
            for (SetMessageInfoLockStatusDto dto : setMessageInfoLockStatus) {
                LocalSetMessageInformation localSetMessageInformation = new LocalSetMessageInformation();
                setInfo(dto.getMessageId(), dto.getRecipient(), loginSessionHolder, localSetMessageInformation);

                localSetMessageInformation.setLockStatus(dto.getLockStatus());
                localSetMessageInformation.setStatusMessage(dto.getLockStatusMsg());
                messageInformation = localSetMessageInformation.setMessageInformation();
                checkException(messageInformation);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return String.valueOf(Objects.requireNonNull(messageInformation).getStatus());
    }

    @Override
    public String setMessageInfoExpiryDate(List<SetMessageInfoExpiryDateDto> setMessageInfoExpiryDate, LoginSessionHolder loginSessionHolder) throws Exception {

        SetInformationResponse messageInformation = null;
        try {
            for (SetMessageInfoExpiryDateDto dto : setMessageInfoExpiryDate) {
                LocalSetMessageInformation localSetMessageInformation = new LocalSetMessageInformation();
                setInfo(dto.getMessageId(), dto.getRecipient(), loginSessionHolder, localSetMessageInformation);


                localSetMessageInformation.setExpiryDate(dto.getDate());

                messageInformation = localSetMessageInformation.setMessageInformation();

                checkException(messageInformation);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return String.valueOf(Objects.requireNonNull(messageInformation).getStatus());
    }

    private void checkException(SetInformationResponse messageInformation) {
        if (messageInformation.getErrorMessage() != null) {
            logger.error(messageInformation.getErrorMessage());
            throw new IllegalArgumentException(messageInformation.getErrorMessage());
        }
    }

    private void setInfo(String msgId, String recipient, LoginSessionHolder loginSessionHolder, LocalSetMessageInformation localSetMessageInformation) throws Exception {
        localSetMessageInformation.setMessageID(msgId);
        localSetMessageInformation.addRecipient(recipient);
        localSetMessageInformation.setEnvelopeConfigContent(new GetEnvelopeConfigImpl().getEnvelopeConfig());
        localSetMessageInformation.setUsername(loginSessionHolder.getLoginSession().getData().get(dataParameter).toString());
        localSetMessageInformation.setClientType(clientType);
        localSetMessageInformation.setLoginSession(loginSessionHolder.getLoginSession());
    }
}
