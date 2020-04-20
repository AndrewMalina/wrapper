package test.producer.services;

import test.producer.component.LoginSessionHolder;

public interface GetMessageInfoService {
    String getMessageInfo(LoginSessionHolder loginSessionHolder, String sortedField, Integer maxResult, Boolean sortedAscending, Integer startAt) throws Exception;
}
