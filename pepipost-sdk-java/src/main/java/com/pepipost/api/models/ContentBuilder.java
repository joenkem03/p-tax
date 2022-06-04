/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;

public class ContentBuilder {
    //the instance to build
    private Content content;

    /**
     * Default constructor to initialize the instance
     */
    public ContentBuilder() {
        content = new Content();
    }

    public ContentBuilder type(TypeEnum type) {
        content.setType(type);
        return this;
    }

    /**
     * HTML content to be sent in your email
     */
    public ContentBuilder value(String value) {
        content.setValue(value);
        return this;
    }
    /**
     * Build the instance with the given values
     */
    public Content build() {
        return content;
    }
}