package redlib.backend.vo;

import lombok.Data;

@Data
public class StudentsVO {
    private Integer id;
    private String studentName;
    private Integer studentNum;
    private Integer gender;
    private String parentName;
    private String parentTel;
    private Integer classId;
}
