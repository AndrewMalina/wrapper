package test.producer.services;

import test.producer.component.LoginSessionHolder;

public interface DecryptEnvelopeService {
    String decryptEnvelope(String loginSession, String password, String envelope, LoginSessionHolder loginSessionHolder) throws Exception;
}
