/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;

public class SendBuilder {
    //the instance to build
    private Send send;

    /**
     * Default constructor to initialize the instance
     */
    public SendBuilder() {
        send = new Send();
    }

    /**
     * email address which recipients can reply to.
     */
    public SendBuilder replyTo(String replyTo) {
        send.setReplyTo(replyTo);
        return this;
    }

    /**
     * Email address representing the sender of the mail
     */
    public SendBuilder from(From from) {
        send.setFrom(from);
        return this;
    }

    /**
     * Subject line of the email
     */
    public SendBuilder subject(String subject) {
        send.setSubject(subject);
        return this;
    }

    /**
     * ID of the template to be used for sending the mail
     */
    public SendBuilder templateId(Long templateId) {
        send.setTemplateId(templateId);
        return this;
    }

    /**
     * content in text/plain format
     */
    public SendBuilder content(List<Content> content) {
        send.setContent(content);
        return this;
    }

    /**
     * attachment information
     */
    public SendBuilder attachments(List<Attachments> attachments) {
        send.setAttachments(attachments);
        return this;
    }

    /**
     * to recipient with some personalized data like to address, attachments and attributes
     */
    public SendBuilder personalizations(List<Personalizations> personalizations) {
        send.setPersonalizations(personalizations);
        return this;
    }

    public SendBuilder settings(Settings settings) {
        send.setSettings(settings);
        return this;
    }

    /**
     * define custom tags to organize your emails
     */
    public SendBuilder tags(List<String> tags) {
        send.setTags(tags);
        return this;
    }

    public SendBuilder lintPayload(Boolean lintPayload) {
        send.setLintPayload(lintPayload);
        return this;
    }

    /**
     * schedule the time of email delivery
     */
    public SendBuilder schedule(Long schedule) {
        send.setSchedule(schedule);
        return this;
    }

    /**
     * Global bcc can be defined here
     */
    public SendBuilder bcc(List<EmailStruct> bcc) {
        send.setBcc(bcc);
        return this;
    }
    /**
     * Build the instance with the given values
     */
    public Send build() {
        return send;
    }
}