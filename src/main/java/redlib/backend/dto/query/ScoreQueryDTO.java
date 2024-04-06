package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScoreQueryDTO extends PageQueryDTO {
    private Integer studentNum;
    private Integer academicYear;
    private Integer semester;
    private Integer classId;
}
