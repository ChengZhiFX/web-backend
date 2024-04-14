package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.model.Students;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.StudentsVO;

public class StudentsUtils {
    /**
     * 规范并校验studentsDTO
     *
     * @param studentsDTO
     */
    public static void validateStudents(StudentsDTO studentsDTO) {
        FormatUtils.trimFieldToNull(studentsDTO);
        Assert.notNull(studentsDTO, "输入数据不能为空");
        Assert.hasText(studentsDTO.getStudentName(), "姓名不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param students 实体对象
     * @return VO对象
     */
    public static StudentsVO convertToVO(Students students) {
        StudentsVO studentsVO = new StudentsVO();
        BeanUtils.copyProperties(students, studentsVO);
        return studentsVO;
    }
}
