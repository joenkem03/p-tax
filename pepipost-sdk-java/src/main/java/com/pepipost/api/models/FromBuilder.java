/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;

public class FromBuilder {
    //the instance to build
    private From from;

    /**
     * Default constructor to initialize the instance
     */
    public FromBuilder() {
        from = new From();
    }

    public FromBuilder email(String email) {
        from.setEmail(email);
        return this;
    }

    public FromBuilder name(String name) {
        from.setName(name);
        return this;
    }
    /**
     * Build the instance with the given values
     */
    public From build() {
        return from;
    }
}