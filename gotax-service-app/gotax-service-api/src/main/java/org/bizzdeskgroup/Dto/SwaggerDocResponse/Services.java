package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.MdaOfficeDto;
import org.bizzdeskgroup.Dtos.Query.MdaServiceDto;

import java.util.List;

public class Services extends Paginated {
    private List<MdaServiceDto> data;

    public List<MdaServiceDto> getData() {
        return data;
    }

    public void setData(List<MdaServiceDto> data) {
        this.data = data;
    }
}
