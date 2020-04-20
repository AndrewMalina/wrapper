package test.producer.services;

import sdk.swagger.model.SetMessageInfoExpiryDateDto;
import sdk.swagger.model.SetMessageInfoLockStatusDto;
import test.producer.component.LoginSessionHolder;

import java.util.List;

public interface SetMessageInfoService {
    String setMessageInfoExpiryDate(List<SetMessageInfoExpiryDateDto> setMessageInfoExpiryDate, LoginSessionHolder loginSessionHolder) throws Exception;

    String setMessageInfoLockStatus(List<SetMessageInfoLockStatusDto> setMessageInfoLockStatus, LoginSessionHolder loginSessionHolder) throws Exception;
}
