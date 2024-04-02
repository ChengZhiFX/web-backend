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
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.StudentsService;
import redlib.backend.service.utils.StudentsUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.StudentsVO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StudentsServiceImpl implements StudentsService {

    @Autowired
    private StudentsMapper studentsMapper;

    @Override
    public Page<StudentsVO> listByPage(StudentsQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new StudentsQueryDTO();
        }
        queryDTO.setStudentName(FormatUtils.makeFuzzySearchTerm(queryDTO.getStudentName()));

        Integer size = studentsMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
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
        Token token = ThreadContextHolder.getToken();
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
/*
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
        map.put("classId", "班级ID");
        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
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

        return workbook;
    }

 */
}
