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
public class Enableordisablesubacoount 
        implements java.io.Serializable {
    private static final long serialVersionUID = 1252843287458159685L;
    private String username;
    private Boolean disabled;
    /** GETTER
     * The username of the subaccount
     */
    @JsonGetter("username")
    public String getUsername ( ) { 
        return this.username;
    }
    
    /** SETTER
     * The username of the subaccount
     */
    @JsonSetter("username")
    public void setUsername (String value) { 
        this.username = value;
    }
 
    /** GETTER
     * Flag to indicate whether the subaccount should be enabled or disabled.
     */
    @JsonGetter("disabled")
    public Boolean getDisabled ( ) { 
        return this.disabled;
    }
    
    /** SETTER
     * Flag to indicate whether the subaccount should be enabled or disabled.
     */
    @JsonSetter("disabled")
    public void setDisabled (Boolean value) { 
        this.disabled = value;
    }
 
}
