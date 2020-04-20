package test.producer.services;

import test.producer.dto.EmailDto;

public interface SendMessageService {
    Boolean sendMessage(EmailDto emailDto);
}
