package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Students;

import java.util.List;

public interface StudentsMapper {
    Integer count(StudentsQueryDTO queryDTO);
    List<Students> list(@Param("queryDTO") StudentsQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);
    Students selectByPrimaryKey(Integer id);
    int insert(Students record);
    int updateByPrimaryKey(Students record);
    void deleteByCodes(@Param("codeList") List<Integer> codeList);
    void deleteByPrimaryKey(Integer id);
}