package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AverageQueryDTO extends PageQueryDTO{
    private Integer academicYear;
    private Integer semester;
}
