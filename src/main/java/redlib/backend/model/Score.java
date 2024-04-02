package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:score表的实体类
 * @version
 * @author:  Colorange
 * @创建时间: 2024-04-01
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
    private String studentNum;

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