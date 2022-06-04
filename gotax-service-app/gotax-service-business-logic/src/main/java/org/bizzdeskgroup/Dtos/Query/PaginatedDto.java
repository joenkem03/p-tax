package org.bizzdeskgroup.Dtos.Query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "PaginatedDto")
public class PaginatedDto {

    private int status;
    private Object data;
    private long total;
    private double sum;
    private double service_charge;
    private double net_sum;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getService_charge() {
        return service_charge;
    }

    public void setService_charge(double service_charge) {
        this.service_charge = service_charge;
    }

    public double getNet_sum() {
        return net_sum;
    }

    public void setNet_sum(double net_sum) {
        this.net_sum = net_sum;
    }
}
