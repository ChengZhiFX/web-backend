package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.query.ClassesQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.ClassesVO;

import java.io.InputStream;
import java.util.List;

public interface ClassesService {
    Page<ClassesVO> listByPage(ClassesQueryDTO queryDTO);
    Integer addClass(ClassesDTO classesDTO);
    ClassesDTO getById(Integer id);
    Integer updateClass(ClassesDTO classesDTO);
    void deleteById(Integer id);
    void deleteByCodes(List<Integer> ids);
    Workbook export(ClassesQueryDTO queryDTO);
    int importClasses(InputStream inputStream, String fileName) throws Exception;
}