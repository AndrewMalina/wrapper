package test.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.ExitApiController;
import test.producer.component.LoginSessionHolder;
import test.producer.services.LogOutService;

@RestController
public class ExitController extends ExitApiController {
    private final LogOutService loginService;
    private final LoginSessionHolder loginSessionHolder;

    @Autowired
    public ExitController(LogOutService loginService, LoginSessionHolder loginSessionHolder) {
        this.loginService = loginService;
        this.loginSessionHolder = loginSessionHolder;
    }

    @Override
    public ResponseEntity<String> exit(@RequestHeader String authorization) {
        loginService.logOut(loginSessionHolder);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
