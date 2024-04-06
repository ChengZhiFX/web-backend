package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:score表的实体类
 * @version
 * @author:  Colorange
 * @创建时间: 2024-04-06
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
    private Long studentNum;

    /**
     * 
     */
    private Byte chineseScore;

    /**
     * 
     */
    private Byte mathScore;

    /**
     * 
     */
    private Byte englishScore;

    /**
     * 
     */
    private Date entryEvent;

    /**
     * 
     */
    private Short academicYear;

    /**
     * 
     */
    private Byte semester;

    /**
     * 
     */
    private Integer classId;
}