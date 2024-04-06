package redlib.backend.dto;

import lombok.Data;

@Data
public class ScoreDTO {
    private Integer id;
    private Integer studentNum;
    private Integer chineseScore;
    private Integer mathScore;
    private Integer englishScore;
    private Integer academicYear;
    private Integer semester;
    private Integer classId;
}
