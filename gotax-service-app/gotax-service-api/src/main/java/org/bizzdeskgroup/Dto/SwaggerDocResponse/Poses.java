package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.PosDto;

import java.util.List;

public class Poses extends Paginated {
    private List<PosDto> data;

    public List<PosDto> getData() {
        return data;
    }

    public void setData(List<PosDto> data) {
        this.data = data;
    }
}
