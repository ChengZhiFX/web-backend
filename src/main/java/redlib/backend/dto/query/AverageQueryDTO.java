package redlib.backend.dto.query;

import lombok.Data;

@Data
public class AverageQueryDTO extends PageQueryDTO{
    private Integer academicYear;
    private Integer semester;
}
