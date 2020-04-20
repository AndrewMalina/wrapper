package test.producer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class EmailDto {
    @JsonProperty
    private String from = null;
    @JsonProperty("recipients")
    private List<String> recipients = new ArrayList<>();

    @JsonProperty("cc")
    private List<String> cc = new ArrayList<>();

    @JsonProperty("bcc")
    private List<String> bcc = new ArrayList<>();

    @JsonProperty("subject")
    private String subject = null;

    @JsonProperty("securdoc")
    private String securdoc = null;

    @JsonProperty("text")
    private String text = null;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSecurdoc() {
        return securdoc;
    }

    public void setSecurdoc(String securdoc) {
        this.securdoc = securdoc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
