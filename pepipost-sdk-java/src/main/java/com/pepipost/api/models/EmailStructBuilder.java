/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;

public class EmailStructBuilder {
    //the instance to build
    private EmailStruct emailStruct;

    /**
     * Default constructor to initialize the instance
     */
    public EmailStructBuilder() {
        emailStruct = new EmailStruct();
    }

    /**
     * Name of recipient
     */
    public EmailStructBuilder name(String name) {
        emailStruct.setName(name);
        return this;
    }

    /**
     * Email of recipient
     */
    public EmailStructBuilder email(String email) {
        emailStruct.setEmail(email);
        return this;
    }
    /**
     * Build the instance with the given values
     */
    public EmailStruct build() {
        return emailStruct;
    }
}