package miniprojectjo.domain;

import java.util.Date;
import lombok.Data;

@Data
public class GetBookByIdQuery {

    private Long id;
    private String bookName;
    private String category;
    private Boolean isBestSeller;
    private String authorName;
}
