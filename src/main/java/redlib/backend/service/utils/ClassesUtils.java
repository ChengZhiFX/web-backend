package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.model.Classes;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ClassesVO;
import java.time.LocalDate;

public class ClassesUtils {
    /**
     * 规范并校验classesDTO
     *
     * @param classesDTO
     */
    public static void validateClasses(ClassesDTO classesDTO) {
        FormatUtils.trimFieldToNull(classesDTO);
        Assert.notNull(classesDTO, "输入数据不能为空");
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int isSpring = month<8? 1:0;
        int attendanceYear = classesDTO.getId() / 100;
        int classOrder = classesDTO.getId() % 100;
        if (classesDTO.getClassName() == null) {
            classesDTO.setClassName(numberToChinese(year - attendanceYear + 1 - isSpring) + "年级" + classOrder + '班');
        }
        if (year - attendanceYear + 1 - isSpring < 3) {
            classesDTO.setEnglishTeacher(null);
        }
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param classes 实体对象
     * @return VO对象
     */
    public static ClassesVO convertToVO(Classes classes) {
        ClassesVO classesVO = new ClassesVO();
        BeanUtils.copyProperties(classes, classesVO);
        return classesVO;
    }

    public static char numberToChinese(int num) {
        char[] chineseNumbers = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
        if (num < 1 || num > 9) {
            return '0';
        }
        return chineseNumbers[num];
    }
}
