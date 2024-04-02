package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ClassesMapper;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.query.ClassesQueryDTO;
import redlib.backend.model.Classes;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.ClassesService;
import redlib.backend.service.utils.ClassesUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.ClassesVO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesMapper classesMapper;
    @Override
    public Page<ClassesVO> listByPage(ClassesQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ClassesQueryDTO();
        }
        Integer size = classesMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }
        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Classes> list = classesMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<ClassesVO> voList = new ArrayList<>();
        for (Classes classes : list) {
            ClassesVO vo = ClassesUtils.convertToVO(classes);
            voList.add(vo);
        }
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }
    @Override
    public Integer addClass(ClassesDTO classesDTO) {
        Token token = ThreadContextHolder.getToken();
        // 校验输入数据正确性
        ClassesUtils.validateClasses(classesDTO);
        // 创建实体对象，用以保存到数据库
        Classes classes = new Classes();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(classesDTO, classes);
        // 调用DAO方法保存到数据库表
        classesMapper.insert(classes);
        return classes.getId();
    }

    @Override
    public ClassesDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "id不能为空");
        Classes classes = classesMapper.selectByPrimaryKey(id);
        Assert.notNull(classes, "id不存在");
        ClassesDTO dto = new ClassesDTO();
        BeanUtils.copyProperties(classes, dto);
        return dto;
    }

    @Override
    public Integer updateClass(ClassesDTO classesDTO) {
        // 校验输入数据正确性
        Token token = ThreadContextHolder.getToken();
        ClassesUtils.validateClasses(classesDTO);
        Assert.notNull(classesDTO.getId(), "部门id不能为空");
        Classes classes = classesMapper.selectByPrimaryKey(classesDTO.getId());
        Assert.notNull(classes, "没有找到部门，Id为：" + classesDTO.getId());
        BeanUtils.copyProperties(classesDTO, classes);
        classesMapper.updateByPrimaryKey(classes);
        return classes.getId();
    }
    @Override
    public void deleteById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "id不能为空");
        classesMapper.deleteByPrimaryKey(id);
    }
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "部门id列表不能为空");
        classesMapper.deleteByCodes(ids);
    }
    /*
    @Override
    public Workbook export(ClassesQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "部门ID");
        map.put("classesName", "部门名称");
        map.put("contact", "联系人");
        map.put("contactPhone", "手机号");
        map.put("description", "描述");
        map.put("updatedAt", "更新时间");
        map.put("createdByDesc", "创建人");
        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<ClassesVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }
    */
}
