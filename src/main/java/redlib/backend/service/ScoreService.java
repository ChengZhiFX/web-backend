package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.AverageDTO;
import redlib.backend.vo.AverageVO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.ScoreVO;

import java.io.InputStream;
import java.util.List;

public interface ScoreService {
    Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO);
    Integer addScore(ScoreDTO scoreDTO);
    ScoreDTO getById(Integer id);
    Integer updateScore(ScoreDTO scoreDTO);
    void deleteByCodes(List<Integer> ids);
    void deleteById(Integer id);
    Workbook export(ScoreQueryDTO queryDTO);
    int importScores(InputStream inputStream, String fileName) throws Exception;
    List<AverageVO> getAverageOfClass(AverageDTO averageDTO);
    /*
    Integer getClassIdByStudentNum(String studentNum);
    List<Score> getByAcademicYearAndSemester(Short academicYear, Byte semester);
    List<Score> getByClass(Integer classId);
    Map<String, Double> calculateAverageScoreByClass(Short academicYear, Byte semester);
     */
}
