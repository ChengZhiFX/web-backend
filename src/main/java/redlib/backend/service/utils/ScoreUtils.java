package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.model.Score;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ScoreVO;

public class ScoreUtils {
    /**
     * 规范并校验scoreDTO
     *
     * @param scoreDTO
     */
    public static void validateScore(ScoreDTO scoreDTO) {
        FormatUtils.trimFieldToNull(scoreDTO);
        Assert.notNull(scoreDTO, "输入数据不能为空");
        Assert.hasText(scoreDTO.getStudentNum(), "名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param score 实体对象
     * @return VO对象
     */
    public static ScoreVO convertToVO(Score score) {
        ScoreVO scoreVO = new ScoreVO();
        BeanUtils.copyProperties(score, scoreVO);
        return scoreVO;
    }
}
