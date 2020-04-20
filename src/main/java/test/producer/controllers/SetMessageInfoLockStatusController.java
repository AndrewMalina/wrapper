package test.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.SetMessageInfoLockStatusApiController;
import sdk.swagger.model.SetMessageInfoLockStatusDto;
import test.producer.component.LoginSessionHolder;
import test.producer.services.SetMessageInfoService;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

@RestController
public class SetMessageInfoLockStatusController extends SetMessageInfoLockStatusApiController {

    private final SetMessageInfoService setMessageInfoService;

    private final LoginSessionHolder loginSessionHolder;

    @Autowired
    public SetMessageInfoLockStatusController(SetMessageInfoService setMessageInfoService, LoginSessionHolder loginSessionHolder) {
        this.setMessageInfoService = setMessageInfoService;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public ResponseEntity<String> setMessageInfoLockStatus(@RequestBody List<SetMessageInfoLockStatusDto> setMessageInfoLockStatus,
                                                           @RequestHeader(value = "authorization") String authorization) {
        String answer;
        try {
            answer = setMessageInfoService.setMessageInfoLockStatus(setMessageInfoLockStatus, loginSessionHolder);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(e.getChallenges().get(0).toString(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
