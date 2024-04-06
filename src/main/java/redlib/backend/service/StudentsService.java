package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.StudentsVO;

import java.io.InputStream;
import java.util.List;

public interface StudentsService {
    Page<StudentsVO> listByPage(StudentsQueryDTO queryDTO);
    Integer addStudent(StudentsDTO studentsDTO);
    StudentsDTO getById(Integer id);
    Integer updateStudent(StudentsDTO studentsDTO);
    void deleteByCodes(List<Integer> ids);
    void deleteById(Integer id);
    Workbook export(StudentsQueryDTO queryDTO);
    int importStudents(InputStream inputStream, String fileName) throws Exception;
    Workbook exportTemplate();
    int getTotalOfStudents(Integer classId);
}
