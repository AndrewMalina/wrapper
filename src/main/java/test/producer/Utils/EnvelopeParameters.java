package test.producer.Utils;


import com.postx.sdk.keyserver.LoginSession;
import sdk.swagger.model.BuildEnvelopeAttachmentDto;

import java.util.List;

public class EnvelopeParameters {

    private String envelopeConfig;
    private LoginSession loginSession;
    private String toEmail;
    private String ccEmail;
    private String bccEmail;
    private String subject;
    private Boolean isBodyFormatPlainText;
    private List<BuildEnvelopeAttachmentDto> attachmentDto;
    private String body;

    public EnvelopeParameters(String envelopeConfig,
                              LoginSession loginSession,
                              String toEmail,
                              String ccEmail,
                              String bccEmail,
                              String subject,
                              Boolean isBodyFormatPlainText,
                              List<BuildEnvelopeAttachmentDto> attachmentDto,
                              String body) {
        this.envelopeConfig = envelopeConfig;
        this.loginSession = loginSession;
        this.toEmail = toEmail;
        this.ccEmail = ccEmail;
        this.bccEmail = bccEmail;
        this.subject = subject;
        this.isBodyFormatPlainText = isBodyFormatPlainText;
        this.attachmentDto = attachmentDto;
        this.body = body;
    }

    public String getEnvelopeConfig() {
        return envelopeConfig;
    }

    public LoginSession getLoginSession() {
        return loginSession;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public String getSubject() {
        return subject;
    }

    public Boolean getIsBodyFormatPlainText() {
        return isBodyFormatPlainText;
    }

    public List<BuildEnvelopeAttachmentDto> getFile() {
        return this.attachmentDto;
    }

    public String getBody() {
        return body;
    }
}
