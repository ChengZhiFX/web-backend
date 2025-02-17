package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:score表的实体类
 * @version
 * &#064;author:   Colorange
 * &#064;创建时间:  2024-04-06
 */
@Data
public class Score {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer studentNum;

    /**
     * 
     */
    private Integer chineseScore;

    /**
     * 
     */
    private Integer mathScore;

    /**
     * 
     */
    private Integer englishScore;

    /**
     * 
     */
    private Integer totalScore;

    /**
     *
     */
    private Date entryEvent;

    /**
     * 
     */
    private Integer academicYear;

    /**
     * 
     */
    private Integer semester;

    /**
     * 
     */
    private Integer classId;
}