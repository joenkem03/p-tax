/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;

public class DeletesubacoountBuilder {
    //the instance to build
    private Deletesubacoount deletesubacoount;

    /**
     * Default constructor to initialize the instance
     */
    public DeletesubacoountBuilder() {
        deletesubacoount = new Deletesubacoount();
    }

    /**
     * The username of the subaccount
     */
    public DeletesubacoountBuilder username(String username) {
        deletesubacoount.setUsername(username);
        return this;
    }
    /**
     * Build the instance with the given values
     */
    public Deletesubacoount build() {
        return deletesubacoount;
    }
}