package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.MdaDto;
import org.bizzdeskgroup.Dtos.Query.MdaOfficeDto;

import java.util.List;

public class Offices extends Paginated {
    private List<MdaOfficeDto> data;

    public List<MdaOfficeDto> getData() {
        return data;
    }

    public void setData(List<MdaOfficeDto> data) {
        this.data = data;
    }
}
