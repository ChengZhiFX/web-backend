package redlib.backend.model;

import lombok.Data;


@Data
public class Students {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String studentName;

    /**
     * 
     */
    private String studentNum;

    /**
     * 
     */
    private Boolean gender;

    /**
     * 
     */
    private String parentName;

    /**
     * 
     */
    private String parentTel;

    /**
     * 
     */
    private Integer classId;
}