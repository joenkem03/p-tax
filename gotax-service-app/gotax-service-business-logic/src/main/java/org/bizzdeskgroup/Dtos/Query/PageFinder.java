package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class PageFinder {
    public int id;
    public int mdaId;
    public int officeId;
    public int from;
    public int recordsPerPage;
    public String orderBy;
    public int agentId;
    public int businessId;
    public int payerId;
    public int filter;
    public String filterBy;
    public String filterValue;
    public boolean status;
    public Timestamp startTransactionDate;
    public Timestamp endTransactionDate;
}
