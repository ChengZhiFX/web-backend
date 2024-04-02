package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.vo.StudentsVO;

import java.util.List;

public interface StudentsService {
    Page<StudentsVO> listByPage(StudentsQueryDTO queryDTO);
    Integer addStudent(StudentsDTO studentsDTO);
    StudentsDTO getById(Integer id);
    Integer updateStudent(StudentsDTO studentsDTO);
    void deleteByCodes(List<Integer> ids);
    void deleteById(Integer id);
//    Workbook export(StudentsQueryDTO queryDTO);
}
