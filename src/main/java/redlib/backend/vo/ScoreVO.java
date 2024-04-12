package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreVO {
    private Integer id;
    private String studentName;
    private Integer studentNum;
    private Integer chineseScore;
    private Integer mathScore;
    private Integer englishScore;
    private Integer totalScore;
    private Date entryEvent;
    private Integer academicYear;
    private Integer semester;
    private Integer classId;
}
