package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.model.Score;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ScoreVO;
import java.time.LocalDate;
import static redlib.backend.utils.FormatUtils.camel2under;

public class ScoreUtils {
    /**
     * 规范并校验scoreDTO
     *
     * @param scoreDTO
     */
    public static void validateScore(ScoreDTO scoreDTO) {
        FormatUtils.trimFieldToNull(scoreDTO);
        Assert.notNull(scoreDTO, "输入数据不能为空");
        Assert.notNull(scoreDTO.getStudentNum(), "学号不能为空");
        if (scoreDTO.getChineseScore() == null) scoreDTO.setChineseScore(0);
        if (scoreDTO.getMathScore() == null) scoreDTO.setMathScore(0);
        if (scoreDTO.getEnglishScore() == null) scoreDTO.setEnglishScore(0);
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int isSpring = month<8? 1:0;
        int attendanceYear = scoreDTO.getStudentNum() / 100;
        if (year - attendanceYear + 1 - isSpring < 3) {
            scoreDTO.setEnglishScore(0);
        }
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

    public static String avgFormatOrderBy(String orderBy){
        orderBy = FormatUtils.trimToNull(orderBy);
        if (orderBy == null) {
            return null;
        }

        String[] pairs = orderBy.split(" ");
        if (pairs.length != 2) {
            return null;
        }

        String field = camel2under(pairs[0]);
        if ("averageChineseScore".equals(pairs[0])) field = "avg(chinese_score)";
        else if ("averageMathScore".equals(pairs[0])) field = "avg(math_score)";
        else if ("averageEnglishScore".equals(pairs[0])) field = "avg(english_score)";
        else if ("averageTotalScore".equals(pairs[0])) field = "avg(total_score)";
        String order = pairs[1];
        if ("ascend".equals(pairs[1])) {
            order = "asc";
        }
        if ("descend".equals(pairs[1])) {
            order = "desc";
        }

        return field + ' ' + order;
    }
}
