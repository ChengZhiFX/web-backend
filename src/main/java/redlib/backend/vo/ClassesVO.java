package redlib.backend.vo;

import lombok.Data;

@Data
public class ClassesVO {
    private Integer id;
    private String className;
    private String chineseTeacher;
    private String mathTeacher;
    private String englishTeacher;
    private Integer totalStudents;
}
