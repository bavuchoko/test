package egovframework.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultVO {

    private int pageIndex = 1;
    private int pageUnit = 15;
    private int pageSize = 10;
    private int firstIndex = 0;
    private int lastIndex = 1;
    private int recordCountPerPage = 10;
    private String today;
    private String startDate;
    private String endDate;


    private int userId ;
}
