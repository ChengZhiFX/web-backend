package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ScoreMapper;
import redlib.backend.dto.AverageDTO;
import redlib.backend.vo.AverageVO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.model.Token;
import redlib.backend.service.ScoreService;
import redlib.backend.service.utils.ScoreUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.ScoreVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

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

    @Override
    public Workbook export(ScoreQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "流水号");
        map.put("studentNum", "学号");
        map.put("chineseScore", "语文成绩");
        map.put("mathScore", "数学成绩");
        map.put("englishScore", "英语成绩");
        map.put("entryEvent", "录入时间");
        map.put("academicYear", "学年");
        map.put("semester", "学期");
        map.put("classId", "班级号");
        final AtomicBoolean finalPage = new AtomicBoolean(false);
        return XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<ScoreVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);
    }

    @Override
    public int importScores(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("流水号", "id");
        map.put("学号", "studentNum");
        map.put("语文成绩", "chineseScore");
        map.put("数学成绩", "mathScore");
        map.put("英语成绩", "englishScore");
        map.put("学年", "academicYear");
        map.put("学期", "semester");
        map.put("班级号", "classId");
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (scoreDTO) -> {
            addScore(scoreDTO);
            row.incrementAndGet();
        }, map, ScoreDTO.class);
        return row.get();
    }

    @Override
    public List<AverageVO> getAverageOfClass(AverageDTO averageDTO) {
        List<Map> list = scoreMapper.getAverageOfClass(averageDTO);
        List<AverageVO> voList = new ArrayList<>();
        for (Map map : list) {
            AverageVO vo = new AverageVO();
            vo.setClassId((Integer) map.get("class_id"));
            vo.setAverageChineseScore(((BigDecimal) map.get("avg(chinese_score)")).floatValue());
            vo.setAverageMathScore(((BigDecimal) map.get("avg(math_score)")).floatValue());
            vo.setAverageEnglishScore(((BigDecimal) map.get("avg(english_score)")).floatValue());
            voList.add(vo);
        }
        return voList;
    }
/*
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
