package redlib.backend.dto;

import lombok.Data;

@Data
public class ClassesDTO {
    private Integer id;
    private String className;
    private String chineseTeacher;
    private String mathTeacher;
    private String englishTeacher;
}
