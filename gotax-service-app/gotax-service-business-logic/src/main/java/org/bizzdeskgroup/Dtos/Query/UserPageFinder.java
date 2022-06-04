package org.bizzdeskgroup.Dtos.Query;

import java.util.List;

public class UserPageFinder {
    public List<String> roleTypes;
    public int from;
    public int recordsPerPage;
    public String orderBy;
//    public String filter;
    public int businessId;
    public int mdaId;
    public boolean status;
}
