package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentsQueryDTO extends PageQueryDTO {
    private String studentName;
    private String studentNum;
    private Integer gender;
    private Integer classId;
}
