package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.PosTransactionDto;
import org.bizzdeskgroup.Dtos.Query.TransactionDto;

import java.util.List;

public class PosTransactions extends Paginated {
    private List<PosTransactionDto> data;

    public List<PosTransactionDto> getData() {
        return data;
    }

    public void setData(List<PosTransactionDto> data) {
        this.data = data;
    }
}
