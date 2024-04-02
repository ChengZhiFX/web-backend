package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ScoreMapper;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.ScoreService;
import redlib.backend.service.utils.ScoreUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.vo.ScoreVO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private AdminService adminService;
    @Override
    public Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ScoreQueryDTO();
        }

        Integer size = scoreMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Score> list = scoreMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());


        List<ScoreVO> voList = new ArrayList<>();
        for (Score score : list) {
            // Score对象转VO对象
            ScoreVO vo = ScoreUtils.convertToVO(score);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Integer addScore(ScoreDTO scoreDTO) {
        Token token = ThreadContextHolder.getToken();
        // 创建实体对象，用以保存到数据库
        Score score = new Score();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(scoreDTO, score);
        score.setEntryEvent(new Date());
        // 调用DAO方法保存到数据库表
        scoreMapper.insert(score);
        return score.getId();
    }
    @Override
    public ScoreDTO getById(Integer id) {
        Assert.notNull(id, "id不能为空");
        Score score = scoreMapper.selectByPrimaryKey(id);
        Assert.notNull(score, "id不存在");
        ScoreDTO dto = new ScoreDTO();
        BeanUtils.copyProperties(score, dto);
        return dto;
    }
    @Override
    public Integer updateScore(ScoreDTO scoreDTO) {
        Assert.notNull(scoreDTO.getId(), "id不能为空");
        Score score = scoreMapper.selectByPrimaryKey(scoreDTO.getId());
        Assert.notNull(score, "没有找到，Id为：" + scoreDTO.getId());
        BeanUtils.copyProperties(scoreDTO, score);
        scoreMapper.updateByPrimaryKey(score);
        return score.getId();
    }
    @Override
    public void deleteById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "id不能为空");
        scoreMapper.deleteByPrimaryKey(id);
    }
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "id列表不能为空");
        scoreMapper.deleteByCodes(ids);
    }
/*
    @Override
    public List<Score> getScoresByAcademicYearAndSemester(Short academicYear, Byte semester) {
        return scoreMapper.getScoresByAcademicYearAndSemester(academicYear, semester);
    }

    @Override
    public List<Score> getScoresByClass(Integer classId) {
        return null;
    }

    @Override
    public Map<String, Double> calculateAverageScoreByClass(Short academicYear, Byte semester) {
        List<Score> scores = getScoresByAcademicYearAndSemester(academicYear, semester);
        Map<String, List<Score>> scoresByClass = scores.stream().collect(Collectors.groupingBy(Score::getClassName));
        Map<String, Double> averageScores = new HashMap<>();
        scoresByClass.forEach((className, classScores) -> {
            double avgScore = classScores.stream().mapToDouble(Score::getTotalScore).average().orElse(0.0);
            averageScores.put(className, avgScore);
        });
        return averageScores;
    }

 */
}
