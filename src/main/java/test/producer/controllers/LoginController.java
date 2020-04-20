package test.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sdk.swagger.controllers.LoginApiController;
import sdk.swagger.model.UserDto;
import test.producer.services.LoginService;

import javax.ws.rs.NotAuthorizedException;

@RestController
public class LoginController extends LoginApiController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public ResponseEntity<String> getLoginSession(@RequestBody UserDto user) {
        String loginAnswer;
        try {
            loginAnswer = loginService.login(user.getUsername(), user.getPassword());
        } catch (NotAuthorizedException exception) {
            return new ResponseEntity<>(
                    exception.getChallenges().get(0).toString(),
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(loginAnswer, HttpStatus.OK);
    }
}
