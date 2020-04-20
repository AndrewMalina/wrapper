package test.producer.controllers;

import com.postx.sdk.message.MessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.GetMessageInfoApiController;
import sdk.swagger.model.GetMessageInfoDto;
import test.producer.component.LoginSessionHolder;
import test.producer.services.GetMessageInfoService;

@RestController
public class GetMessageInfoController extends GetMessageInfoApiController {

    private final GetMessageInfoService getMessageInfoService;

    private final LoginSessionHolder loginSessionHolder;

    @Autowired
    public GetMessageInfoController(GetMessageInfoService getMessageInfoService, LoginSessionHolder loginSessionHolder) {
        this.getMessageInfoService = getMessageInfoService;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public ResponseEntity<String> getMessageInfo(@RequestHeader(value = "Authorization") String authorization,
                                                 @RequestBody GetMessageInfoDto getMessageInfo) {
        String answer;

        try {
            answer = getMessageInfoService.getMessageInfo(loginSessionHolder, getMessageInfo.getSortedField(), getMessageInfo.getMaxResult(), getMessageInfo.getSortedAscending(), getMessageInfo.getStartAt());
        } catch (MessageException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
