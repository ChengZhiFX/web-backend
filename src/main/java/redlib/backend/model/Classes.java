package redlib.backend.model;

import lombok.Data;

/**
 * 描述:classes表的实体类
 * @version
 * @author:  Colorange
 * @创建时间: 2024-04-01
 */
@Data
public class Classes {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String className;

    /**
     * 
     */
    private String chineseTeacher;

    /**
     * 
     */
    private String mathTeacher;

    /**
     * 
     */
    private String englishTeacher;
}