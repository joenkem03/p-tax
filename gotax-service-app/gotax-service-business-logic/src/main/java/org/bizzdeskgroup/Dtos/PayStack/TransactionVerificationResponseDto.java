package org.bizzdeskgroup.Dtos.PayStack;

public class TransactionVerificationResponseDto {
    private boolean status;
    private String message;
    private data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TransactionVerificationResponseDto{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

//
//{
//        "": true,
//        "": "Verification successful",
//        "":
//        }
