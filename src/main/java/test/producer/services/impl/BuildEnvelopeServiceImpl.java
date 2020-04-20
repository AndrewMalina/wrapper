package test.producer.services.impl;

import com.google.common.io.Files;
import com.postx.sdk.envelope.Envelope;
import com.postx.sdk.envelope.local.LocalEnvelopeBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import sdk.swagger.model.BuildEnvelopeAttachmentDto;
import test.producer.Utils.EnvelopeParameters;
import test.producer.dto.EmailDto;
import test.producer.services.BuildEnvelopeService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;


@Service
public class BuildEnvelopeServiceImpl implements BuildEnvelopeService {
    private final static Logger logger = Logger.getLogger(BuildEnvelopeService.class);

    private final static String dataParameter = "user";
    private final static String regexForEmail = "[,;]";
    private final static String clientType = "CRES";
    private final static String plainTextFormat = "plain";
    private final static String htmlTextFormat = "html";

    @Override
    public Boolean buildEnvelope(EnvelopeParameters envelopeParameters) throws Exception {

        String envelopeString = null;
        String body = null;
        LocalEnvelopeBuilder localEnvelopeBuilder;
        EmailDto emailDto = new EmailDto();

        try {
            localEnvelopeBuilder = new LocalEnvelopeBuilder();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(System.getProperties()));
        try {
            buildMessage(mimeMessage, envelopeParameters);

        } catch (MessagingException | IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        try {
            if (localEnvelopeBuilder != null) {
                localEnvelopeBuilder.setFrom(envelopeParameters.getLoginSession().getData().get(dataParameter).toString());
                if (envelopeParameters.getToEmail() != null) {
                    for (String to : envelopeParameters.getToEmail().split(regexForEmail)) {
                        localEnvelopeBuilder.addTo(to);
                    }
                }
                if (envelopeParameters.getEnvelopeConfig() != null) {
                    localEnvelopeBuilder.setEnvelopeConfigContent(envelopeParameters.getEnvelopeConfig());
                }
                if (envelopeParameters.getLoginSession() != null) {
                    localEnvelopeBuilder.setLoginSession(envelopeParameters.getLoginSession());
                }
                if (envelopeParameters.getBccEmail() != null) {
                    for (String bcc : envelopeParameters.getBccEmail().split(regexForEmail)) {
                        localEnvelopeBuilder.addBCC(bcc);
                    }
                }
                if (envelopeParameters.getCcEmail() != null) {
                    for (String cc : envelopeParameters.getCcEmail().split(regexForEmail)) {
                        localEnvelopeBuilder.addCC(cc);
                    }
                }
                localEnvelopeBuilder.setMimeMessage(mimeMessage);
                localEnvelopeBuilder.setClientType(clientType);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        try {
            if (localEnvelopeBuilder != null) {
                Envelope envelope = localEnvelopeBuilder.build();
                envelopeString = new String(envelope.getEnvelope());
                if (envelope.getMessageBodyHTML() != null) {
                    body = envelope.getMessageBodyHTML();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }


        setEmailDto(emailDto, envelopeParameters, envelopeString, body);

        return sendMessage(emailDto);
    }

    private Boolean sendMessage(EmailDto emailDto) {
        return new SendMessageServiceImpl().sendMessage(emailDto);      //todo Autowired
    }

    private void setEmailDto(EmailDto emailDto, EnvelopeParameters envelopeParameters, String envelopeString, String body) {
        emailDto.setFrom(envelopeParameters.getLoginSession().getData().get(dataParameter).toString());
        if (!envelopeParameters.getToEmail().isEmpty()) {
            emailDto.setRecipients(Arrays.asList(envelopeParameters.getToEmail().split(regexForEmail)));
        }
        if (!envelopeParameters.getBccEmail().isEmpty()) {
            emailDto.setBcc(Arrays.asList(envelopeParameters.getBccEmail().split(regexForEmail)));
        }
        if (!envelopeParameters.getCcEmail().isEmpty()) {
            emailDto.setCc(Arrays.asList(envelopeParameters.getCcEmail().split(regexForEmail)));
        }
        if (!envelopeParameters.getSubject().isEmpty()) {
            emailDto.setSubject(envelopeParameters.getSubject());
        }
        emailDto.setSecurdoc(envelopeString);
        emailDto.setText(Base64.getEncoder().encodeToString(body.getBytes()));
    }

    private void buildMessage(MimeMessage mimeMessage, EnvelopeParameters envelopeParameters) throws Exception {
        String[] toAddress = null;
        if (envelopeParameters.getToEmail() != null) {
            toAddress = envelopeParameters.getToEmail().split(regexForEmail);
        }
        mimeMessage.setFrom(new InternetAddress(envelopeParameters.getLoginSession().getData().get(dataParameter).toString()));
        if (toAddress != null) {
            for (String recipient : toAddress) {
                mimeMessage.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipient));
            }
        }
        if (envelopeParameters.getSubject() != null) {
            mimeMessage.setSubject(envelopeParameters.getSubject(), "utf-8");
        }
        final MimeBodyPart messageBodyPart = new MimeBodyPart();
        // Now set the actual message
        if (envelopeParameters.getIsBodyFormatPlainText()) {
            messageBodyPart.setText(envelopeParameters.getBody(), "utf-8", plainTextFormat);
        } else {
            messageBodyPart.setText(envelopeParameters.getBody(), "utf-8", htmlTextFormat);
        }
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        // Set text message part
        multipart.addBodyPart(messageBodyPart);
        // Part two is attachment

        if (envelopeParameters.getFile() != null) {
            try {
                buildAttachments(envelopeParameters, multipart);
            } catch (Exception e) {
                throw e;
            }
        }

        // Send the complete message parts
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }

    private void buildAttachments(EnvelopeParameters envelopeParameters, Multipart multipart) throws Exception {
        for (BuildEnvelopeAttachmentDto buildEnvelopeAttachmentDto : envelopeParameters.getFile()) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            File file = Files.createTempDir();

            File attach = new File(file.getPath() + "/" + buildEnvelopeAttachmentDto.getFileName());
            if (buildEnvelopeAttachmentDto.getFileContent() != null) {
                Files.write(org.apache.commons.codec.binary.Base64.decodeBase64(buildEnvelopeAttachmentDto.getFileContent()), attach);
            } else {
                throw new Exception("Bad attachment!");
            }
            DataSource source = new FileDataSource(attach);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attach.getName());
            multipart.addBodyPart(messageBodyPart);
        }
    }
}
