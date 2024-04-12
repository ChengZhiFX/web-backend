package redlib.backend.vo;

import lombok.Data;

@Data
public class AverageVO {
    private Integer classId;
    private String className;
    private Float averageChineseScore;
    private Float averageMathScore;
    private Float averageEnglishScore;
    private Float averageTotalScore;
}
