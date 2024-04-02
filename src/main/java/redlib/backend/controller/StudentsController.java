package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.StudentsService;
import redlib.backend.vo.StudentsVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class StudentsController {
    @Autowired
    private StudentsService studentsService;
    @PostMapping("listStudents")
    @Privilege("page")
    public Page<StudentsVO> listStudents(@RequestBody StudentsQueryDTO queryDTO) {
        return studentsService.listByPage(queryDTO);
    }
    @PostMapping("addAStudent")
    @Privilege("add")
    public Integer addAStudent(@RequestBody StudentsDTO studentsDTO) {
        return studentsService.addStudent(studentsDTO);
    }
    @PostMapping("updateAStudent")
    @Privilege("update")
    public Integer updateAStudent(@RequestBody StudentsDTO studentsDTO) {
        return studentsService.updateStudent(studentsDTO);
    }
    @GetMapping("getAStudent")
    @Privilege("update")
    public StudentsDTO getAStudent(Integer id) {
        return studentsService.getById(id);
    }
    @PostMapping("deleteAStudent")
    @Privilege("delete")
    public void deleteAStudent(Integer id) {
        studentsService.deleteById(id);
    }
    @PostMapping("deleteStudents")
    @Privilege("delete")
    public void deleteStudents(@RequestBody List<Integer> ids) {
        studentsService.deleteByCodes(ids);
    }
/*
    @PostMapping("exportStudents")
    @Privilege("page")
    public void exportStudents(@RequestBody StudentsQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = studentsService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }
 */
}
