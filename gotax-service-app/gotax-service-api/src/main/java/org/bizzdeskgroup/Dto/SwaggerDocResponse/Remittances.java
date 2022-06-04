package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.PosTransactionDto;
import org.bizzdeskgroup.Dtos.Query.RemittanceDto;

import java.util.List;

public class Remittances extends Paginated {
    private List<RemittanceDto> data;

    public List<RemittanceDto> getData() {
        return data;
    }

    public void setData(List<RemittanceDto> data) {
        this.data = data;
    }
}
