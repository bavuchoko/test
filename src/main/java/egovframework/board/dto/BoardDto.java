package egovframework.board.dto;

import egovframework.common.DefaultVO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto extends DefaultVO {

    public int tempKey;
    public int boardKey;
    public String title     = "";
    public String body      = "";
    public String fileId    = "";
    public String userName;
    public String userNickname;
    public String wdate ;
}
