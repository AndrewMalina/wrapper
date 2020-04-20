package test.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.DecryptEnvelopeApiController;
import sdk.swagger.model.EnvelopeDecryptDto;
import test.producer.component.LoginSessionHolder;
import test.producer.services.DecryptEnvelopeService;

import javax.ws.rs.NotAuthorizedException;
import java.security.InvalidParameterException;

@RestController
public class DecryptEnvelopeController extends DecryptEnvelopeApiController {

    private final DecryptEnvelopeService decryptEnvelopeService;

    private final LoginSessionHolder loginSessionHolder;

    @Autowired
    public DecryptEnvelopeController(DecryptEnvelopeService decryptEnvelopeService, LoginSessionHolder loginSessionHolder) {
        this.decryptEnvelopeService = decryptEnvelopeService;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public ResponseEntity<String> decryptEnvelope(@RequestHeader(value = "authorization") String authorization,
                                                  @RequestBody EnvelopeDecryptDto envelopeDecrypt) {
        String answer;
        try {
            answer = decryptEnvelopeService.decryptEnvelope(envelopeDecrypt.getToEmail(), envelopeDecrypt.getPassword(), envelopeDecrypt.getBodyText(), loginSessionHolder);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(e.getChallenges().get(0).toString(), HttpStatus.UNAUTHORIZED);
        } catch (InvalidParameterException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
