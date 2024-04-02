package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ScoreService;
import redlib.backend.vo.ScoreVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/score")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @PostMapping("listScores")
    @Privilege("page")
    public Page<ScoreVO> listScores(@RequestBody ScoreQueryDTO queryDTO) {
        return scoreService.listByPage(queryDTO);
    }
    @PostMapping("addAScore")
    @Privilege("add")
    public Integer addAScore(@RequestBody ScoreDTO scoreDTO) {
        return scoreService.addScore(scoreDTO);
    }

    @PostMapping("updateAScore")
    @Privilege("update")
    public Integer updateAScore(@RequestBody ScoreDTO scoreDTO) {
        return scoreService.updateScore(scoreDTO);
    }

    @GetMapping("getAScore")
    @Privilege("update")
    public ScoreDTO getAScore(Integer id) {
        return scoreService.getById(id);
    }
    @PostMapping("deleteAScore")
    @Privilege("delete")
    public void deleteAScore(Integer id) {
        scoreService.deleteById(id);
    }
    @PostMapping("deleteScores")
    @Privilege("delete")
    public void deleteScores(@RequestBody List<Integer> ids) {
        scoreService.deleteByCodes(ids);
    }
/*
    @PostMapping("exportScore")
    @Privilege("page")
    public void exportScore(@RequestBody ScoreQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = scoreService.export(queryDTO);
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
