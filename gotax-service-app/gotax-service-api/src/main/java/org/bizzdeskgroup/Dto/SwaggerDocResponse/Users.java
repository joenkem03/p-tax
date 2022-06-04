package org.bizzdeskgroup.Dto.SwaggerDocResponse;

import org.bizzdeskgroup.Dtos.Query.RemittanceDto;
import org.bizzdeskgroup.Dtos.Query.UserDto;

import java.util.List;

public class Users extends Paginated {
    private List<UserDto> data;

    public List<UserDto> getData() {
        return data;
    }

    public void setData(List<UserDto> data) {
        this.data = data;
    }
}
