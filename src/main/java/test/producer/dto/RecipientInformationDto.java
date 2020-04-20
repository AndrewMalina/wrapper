package test.producer.dto;

import java.io.Serializable;

public class RecipientInformationDto implements Serializable {
    private static final long serialversionUID = 129948768348L;

    private String msgId;
    private String to;
    private String subject;
    private long send;
    private long opened;
    private long expires;
    private boolean locked;
    private String reason;
    private boolean isExpired;
    private int failedAttempts;

    public RecipientInformationDto(String msgId, String to, String subject, long send, long opened, long expires, boolean locked, String reason, boolean isExpired, int failedAttempts) {
        this.msgId = msgId;
        this.to = to;
        this.subject = subject;
        this.send = send;
        this.opened = opened;
        this.expires = expires;
        this.locked = locked;
        this.reason = reason;
        this.isExpired = isExpired;
        this.failedAttempts = failedAttempts;
    }
}
