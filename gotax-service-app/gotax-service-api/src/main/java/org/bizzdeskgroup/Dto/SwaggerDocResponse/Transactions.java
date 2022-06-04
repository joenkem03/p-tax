package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.PosDto;
import org.bizzdeskgroup.Dtos.Query.TransactionDto;

import java.util.List;

public class Transactions extends Paginated {
    private List<TransactionDto> data;

    public List<TransactionDto> getData() {
        return data;
    }

    public void setData(List<TransactionDto> data) {
        this.data = data;
    }
}
