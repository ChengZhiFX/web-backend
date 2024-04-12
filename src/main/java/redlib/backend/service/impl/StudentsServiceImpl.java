package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.StudentsMapper;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.service.StudentsService;
import redlib.backend.service.utils.StudentsUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.StudentsVO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentsServiceImpl implements StudentsService {

    @Autowired
    private StudentsMapper studentsMapper;

    @Override
    public Page<StudentsVO> listByPage(StudentsQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "查询参数不能为空");
        FormatUtils.trimFieldToNull(queryDTO);
        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));
        queryDTO.setStudentName(FormatUtils.makeFuzzySearchTerm(queryDTO.getStudentName()));

        Integer size = studentsMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }
        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Students> list = studentsMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<StudentsVO> voList = new ArrayList<>();
        for (Students students : list) {
            // Students对象转VO对象
            StudentsVO vo = StudentsUtils.convertToVO(students);
            voList.add(vo);
        }
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }
    @Override
    public Integer addStudent(StudentsDTO studentsDTO) {
        // 创建实体对象，用以保存到数据库
        Students students = new Students();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(studentsDTO, students);
        // 调用DAO方法保存到数据库表
        studentsMapper.insert(students);
        return students.getId();
    }
    @Override
    public StudentsDTO getById(Integer id) {
        Assert.notNull(id, "id不能为空");
        Students students = studentsMapper.selectByPrimaryKey(id);
        Assert.notNull(students, "id不存在");
        StudentsDTO dto = new StudentsDTO();
        BeanUtils.copyProperties(students, dto);
        return dto;
    }
    @Override
    public Integer updateStudent(StudentsDTO studentsDTO) {
        Assert.notNull(studentsDTO.getId(), "id不能为空");
        Students students = studentsMapper.selectByPrimaryKey(studentsDTO.getId());
        Assert.notNull(students, "没有找到，Id为：" + studentsDTO.getId());
        BeanUtils.copyProperties(studentsDTO, students);
        studentsMapper.updateByPrimaryKey(students);
        return students.getId();
    }
    @Override
    public void deleteById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "id不能为空");
        studentsMapper.deleteByPrimaryKey(id);
    }
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "id列表不能为空");
        studentsMapper.deleteByCodes(ids);
    }

    @Override
    public Workbook export(StudentsQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "ID");
        map.put("studentName", "姓名");
        map.put("studentNum", "学号");
        map.put("gender", "性别");
        map.put("parentName", "家长姓名");
        map.put("parentTel", "家长电话");
        map.put("classId", "班级号");
        final AtomicBoolean finalPage = new AtomicBoolean(false);
        return XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<StudentsVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);
    }
    
    @Override
    public int importStudents(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("姓名", "studentName");
        map.put("学号", "studentNum");
        map.put("性别(男1女0)", "gender");
        map.put("家长姓名", "parentName");
        map.put("家长电话", "parentTel");
        map.put("班级号", "classId");
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (studentsDTO) -> {
            addStudent(studentsDTO);
            row.incrementAndGet();
        }, map, StudentsDTO.class);
        return row.get();
    }

    @Override
    public Workbook exportTemplate() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("studentName", "姓名");
        map.put("studentNum", "学号");
        map.put("gender", "性别(男1女0)");
        map.put("parentName", "家长姓名");
        map.put("parentTel", "家长电话");
        map.put("classId", "班级号");
        return XlsUtils.exportToExcel(page -> new ArrayList<StudentsDTO>(), map);
    }

    @Override
    public int getTotalOfStudents(Integer classId) {
        StudentsQueryDTO queryDTO = new StudentsQueryDTO();
        queryDTO.setClassId(classId);
        return studentsMapper.count(queryDTO);
    }

    @Override
    public StudentsDTO getByStudentNum(Integer studentNum) {
        Students students = studentsMapper.selectByStudentNum(studentNum);
        StudentsDTO dto = new StudentsDTO();
        if(students != null) BeanUtils.copyProperties(students, dto);
        return dto;
    }
}
