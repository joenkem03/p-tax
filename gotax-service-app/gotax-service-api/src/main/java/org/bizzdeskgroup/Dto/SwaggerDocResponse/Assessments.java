package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.AssessmentDto;

import java.util.List;

public class Assessments extends Paginated {
    private List<AssessmentDto> data;

    public List<AssessmentDto> getData() {
        return data;
    }

    public void setData(List<AssessmentDto> data) {
        this.data = data;
    }
}
