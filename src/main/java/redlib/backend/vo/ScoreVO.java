package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreVO {
    private Integer id;
    private Long studentNum;
    private Byte chineseScore;
    private Byte mathScore;
    private Byte englishScore;
    private Date entryEvent;
    private Short academicYear;
    private Byte semester;
    private Integer classId;
}
