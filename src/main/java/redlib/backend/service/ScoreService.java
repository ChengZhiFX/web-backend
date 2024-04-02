package redlib.backend.service;

import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Classes;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.vo.ScoreVO;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO);
    Integer addScore(ScoreDTO scoreDTO);
    ScoreDTO getById(Integer id);
    Integer updateScore(ScoreDTO scoresDTO);
    void deleteByCodes(List<Integer> ids);
    void deleteById(Integer id);
    /*
    Workbook export(ScoresQueryDTO queryDTO);
    Integer getClassIdByStudentNum(String studentNum);
    List<Score> getScoresByAcademicYearAndSemester(Short academicYear, Byte semester);
    List<Score> getScoresByClass(Integer classId);
    Map<String, Double> calculateAverageScoreByClass(Short academicYear, Byte semester);

     */
}
