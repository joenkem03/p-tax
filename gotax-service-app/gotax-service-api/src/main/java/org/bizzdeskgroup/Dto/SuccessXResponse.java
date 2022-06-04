package org.bizzdeskgroup.Dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class SuccessXResponse {

    private int status;
    private Object data;
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
