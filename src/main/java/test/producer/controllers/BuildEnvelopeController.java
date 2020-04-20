package test.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.BuildEnvelopeApiController;
import sdk.swagger.model.EnvelopeDto;
import test.producer.Utils.EnvelopeParameters;
import test.producer.component.LoginSessionHolder;
import test.producer.services.BuildEnvelopeService;
import test.producer.services.GetEnvelopeConfigService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class BuildEnvelopeController extends BuildEnvelopeApiController {

    private final BuildEnvelopeService buildEnvelopeService;

    private final GetEnvelopeConfigService getEnvelopeConfigService;

    private final LoginSessionHolder loginSessionHolder;

    @Autowired
    public BuildEnvelopeController(BuildEnvelopeService buildEnvelopeService, GetEnvelopeConfigService getEnvelopeConfigService, LoginSessionHolder loginSessionHolder) {
        this.buildEnvelopeService = buildEnvelopeService;
        this.getEnvelopeConfigService = getEnvelopeConfigService;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public ResponseEntity<String> buildEnvelope(@RequestHeader(value = "authorization")
                                                        String authorization,
                                                @RequestBody EnvelopeDto envelope) {
        EnvelopeParameters envelopeParameters = new EnvelopeParameters(getEnvelopeConfigService.getEnvelopeConfig(),
                loginSessionHolder.getLoginSession(),
                envelope.getToEmail(),
                envelope.getCcEmail(),
                envelope.getBccEmail(),
                envelope.getSubject(),
                envelope.getIsBodyFormatPlainText(),
                envelope.getAttachment(),
                envelope.getBodyText());

        Boolean answer;

        try {
            answer = buildEnvelopeService.buildEnvelope(envelopeParameters);
        } catch (MessagingException | IllegalArgumentException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (answer == null) {
            return new ResponseEntity<>("Error sending a message", HttpStatus.BAD_GATEWAY);
        }
        if (answer) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error sending a message", HttpStatus.BAD_GATEWAY);
        }

    }

}
