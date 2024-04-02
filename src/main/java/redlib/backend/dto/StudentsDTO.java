package redlib.backend.dto;

import lombok.Data;

@Data
public class StudentsDTO {
    private Integer id;

    private String studentName;

    private String studentNum;

    private Boolean gender;

    private String parentName;
    
    private String parentTel;

    private Integer classId;
}
