package test.producer.dto;

import test.producer.model.Attachment;

import java.io.Serializable;
import java.util.List;

public class DecrypteMsgDto implements Serializable {
    private final long serializationId = 4231441424241L;

    private String body;
    private List<Attachment> attachments;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
