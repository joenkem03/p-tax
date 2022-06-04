package org.bizzdeskgroup.Dto.SwaggerDocResponse;

public class Paginated {

    private int status;
    private long total;
    private int from;
    private int to;
    private int per_page;
    private long last_page;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public Object getData() {
//        return data;
//    }

//    public void setData(Object data) {
//        this.data = data;
//    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public long getLast_page() {
        return last_page;
    }

    public void setLast_page(long last_page) {
        this.last_page = last_page;
    }
}
