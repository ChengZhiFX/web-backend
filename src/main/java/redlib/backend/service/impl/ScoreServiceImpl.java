package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ScoreMapper;
import redlib.backend.dto.query.AverageQueryDTO;
import redlib.backend.service.ClassesService;
import redlib.backend.service.StudentsService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.AverageVO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.service.ScoreService;
import redlib.backend.service.utils.ScoreUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.ScoreVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private StudentsService studentsService;
    @Autowired
    private ClassesService classesService;

    @Override
    public Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "查询参数不能为空");
        FormatUtils.trimFieldToNull(queryDTO);
        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));

        Integer size = scoreMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }
        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Score> list = scoreMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<ScoreVO> voList = new ArrayList<>();
        for (Score score : list) {
            // Score对象转VO对象
            ScoreVO vo = ScoreUtils.convertToVO(score);
            vo.setStudentName(studentsService.getByStudentNum(vo.getStudentNum()).getStudentName());
            voList.add(vo);
        }
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Integer addScore(ScoreDTO scoreDTO) {
        Assert.notNull(studentsService.getByStudentNum(scoreDTO.getStudentNum()).getId(), "对应学号学生不存在");
        ScoreQueryDTO queryDTO = new ScoreQueryDTO();
        queryDTO.setCurrent(1);
        queryDTO.setPageSize(10);
        queryDTO.setStudentNum(scoreDTO.getStudentNum());
        queryDTO.setAcademicYear(scoreDTO.getAcademicYear());
        queryDTO.setSemester(scoreDTO.getSemester());
        Assert.isTrue(scoreMapper.count(queryDTO) < 1, "此学生该学期成绩已存在");
        Score score = new Score();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(scoreDTO, score);
        score.setClassId(studentsService.getByStudentNum(scoreDTO.getStudentNum()).getClassId());
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
        Assert.notNull(studentsService.getByStudentNum(scoreDTO.getStudentNum()).getId(), "对应学号学生不存在");
        Assert.notNull(scoreDTO.getId(), "id不能为空");
        Score score = scoreMapper.selectByPrimaryKey(scoreDTO.getId());
        Assert.notNull(score, "没有找到，Id为：" + scoreDTO.getId());
        BeanUtils.copyProperties(scoreDTO, score);
        score.setClassId(studentsService.getByStudentNum(scoreDTO.getStudentNum()).getClassId());
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
    public Page<AverageVO> getAverageOfClass(AverageQueryDTO averageQueryDTO) {
        List<Map> list = scoreMapper.getAverageOfClass(averageQueryDTO);
        if (averageQueryDTO == null) {
            averageQueryDTO = new AverageQueryDTO();
        }
        int size = list.size();
        PageUtils pageUtils = new PageUtils(averageQueryDTO.getCurrent(), averageQueryDTO.getPageSize(), size);
        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }
        List<AverageVO> voList = new ArrayList<>();
        for (Map map : list) {
            AverageVO vo = new AverageVO();
            vo.setClassId((Integer) map.get("class_id"));
            Float averageChineseScore = ((BigDecimal) map.get("avg(chinese_score)")).setScale(1, RoundingMode.HALF_UP).floatValue();
            Float averageMathScore = ((BigDecimal) map.get("avg(math_score)")).setScale(1, RoundingMode.HALF_UP).floatValue();
            Float averageEnglishScore = ((BigDecimal) map.get("avg(english_score)")).setScale(1, RoundingMode.HALF_UP).floatValue();
            Float averageTotalScore = averageChineseScore + averageMathScore + averageEnglishScore;
            vo.setAverageChineseScore(averageChineseScore);
            vo.setAverageMathScore(averageMathScore);
            vo.setAverageEnglishScore(averageEnglishScore);
            vo.setAverageTotalScore(averageTotalScore);
            vo.setClassName(classesService.getById(vo.getClassId()).getClassName());
            voList.add(vo);
        }
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Workbook exportTemplate() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("studentNum", "学号");
        map.put("chineseScore", "语文成绩");
        map.put("mathScore", "数学成绩");
        map.put("englishScore", "英语成绩");
        map.put("academicYear", "学年");
        map.put("semester", "学期");
        map.put("classId", "班级号");
        return XlsUtils.exportToExcel(page -> new ArrayList<ScoreDTO>(), map);
    }
}
