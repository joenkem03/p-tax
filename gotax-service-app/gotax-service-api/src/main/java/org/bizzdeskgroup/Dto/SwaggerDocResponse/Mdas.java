package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.MdaDto;
import org.bizzdeskgroup.Dtos.Query.TransactionDto;

import java.util.List;

public class Mdas extends Paginated {
    private List<MdaDto> data;

    public List<MdaDto> getData() {
        return data;
    }

    public void setData(List<MdaDto> data) {
        this.data = data;
    }
}
