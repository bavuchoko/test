package egovframework.common;

import egovframework.account.dto.Account;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethod {

    public static PaginationInfo getPagination(DefaultVO vo) {
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getRecordCountPerPage());
        paginationInfo.setPageSize(vo.getPageSize());
        return paginationInfo;
    }

    public static <T extends DefaultVO>T setVoInfo(T vo, ModelMap model){

        PaginationInfo paginationInfo = getPagination(vo);
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        Date d = new Date();
        // 이번달의 년,월 구하기
        int year = d.getYear();
        int month = d.getMonth();
        // 이번달의 첫날과 마지막날 구하기
        Date firstDay = new Date(year,month,1);
        Date lastDay = new Date(year,month+1,0);

        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
        vo.setToday(String.valueOf(today.format(d)));
        if(!StringUtils.hasText(vo.getStartDate())){
            vo.setStartDate(String.valueOf(today.format(firstDay)));
        }
        if(!StringUtils.hasText(vo.getEndDate())){
            vo.setEndDate(String.valueOf(today.format(lastDay)));
        }
        return vo;
    }

    public static <T extends DefaultVO> boolean validateUser(T vo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!"anonymousUser".equals(authentication.getPrincipal())){
            Account account = (Account)authentication.getPrincipal();
            if(account.getUserId() == vo.getUserId()) return true;
        }
        return false;
    }

}
