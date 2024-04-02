package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:score���ʵ����
 * @version
 * @author:  Colorange
 * @����ʱ��: 2024-04-01
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