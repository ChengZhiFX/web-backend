package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Students;

import java.util.List;
import java.util.Map;

public interface StudentsMapper {
    Integer count(StudentsQueryDTO queryDTO);
    List<Students> list(@Param("queryDTO") StudentsQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);
    Students selectByPrimaryKey(Integer id);
    Students selectByStudentNum(Integer studentNum);
    int insert(Students record);
    int updateByPrimaryKey(Students record);
    void deleteByCodes(@Param("codeList") List<Integer> codeList);
    void deleteByPrimaryKey(Integer id);
    List<Map<Object, Object>> listNumToNameMaps();
}