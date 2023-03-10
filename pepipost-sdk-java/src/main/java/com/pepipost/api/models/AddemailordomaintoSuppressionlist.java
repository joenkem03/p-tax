/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.models;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.ALWAYS)
public class AddemailordomaintoSuppressionlist 
        implements java.io.Serializable {
    private static final long serialVersionUID = 9093449744132410663L;
    private String domain;
    private String email;
    /** GETTER
     * Add the domain to be suppressed here. We will not deliver emails to recipients email addresses with this domain.<br>\nComma separate the values to suppress multiple domains..
     */
    @JsonGetter("domain")
    public String getDomain ( ) { 
        return this.domain;
    }
    
    /** SETTER
     * Add the domain to be suppressed here. We will not deliver emails to recipients email addresses with this domain.<br>\nComma separate the values to suppress multiple domains..
     */
    @JsonSetter("domain")
    public void setDomain (String value) { 
        this.domain = value;
    }
 
    /** GETTER
     * Add an email address to be suppressed here. We will not deliver emails to this email address.<br>\nComma separate the values to suppress multiple email addresses
     */
    @JsonGetter("email")
    public String getEmail ( ) { 
        return this.email;
    }
    
    /** SETTER
     * Add an email address to be suppressed here. We will not deliver emails to this email address.<br>\nComma separate the values to suppress multiple email addresses
     */
    @JsonSetter("email")
    public void setEmail (String value) { 
        this.email = value;
    }
 
}
