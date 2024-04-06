package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClassesQueryDTO extends PageQueryDTO {
    private Integer id;
    private String className;
    private String chineseTeacher;
    private String mathTeacher;
    private String englishTeacher;
}
