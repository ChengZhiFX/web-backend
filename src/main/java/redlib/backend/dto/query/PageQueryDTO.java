package redlib.backend.dto.query;

import lombok.Data;

@Data
public class PageQueryDTO {
    private int current;
    private int pageSize;
    private String orderBy;
}
