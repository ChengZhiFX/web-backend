package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.AverageDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Score;

import java.util.List;
import java.util.Map;

public interface ScoreMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Score record);
    int insertSelective(Score record);
    Score selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Score record);
    int updateByPrimaryKey(Score record);
    Integer count(ScoreQueryDTO queryDTO);
    List<Score> list(@Param("queryDTO") ScoreQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);
    void deleteByCodes(@Param("codeList") List<Integer> codeList);
    List<Map> getAverageOfClass(AverageDTO averageDTO);
}