package egovframework.account.web;

import egovframework.account.dto.Account;
import egovframework.account.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @RequestMapping(value="/loginPage.do")
    public String loginPage(HttpServletRequest request, HttpServletRequest res) {

        String url = request.getHeader("Referer");
        if (url != null && ! url.contains("/loginPage.do")) {
            request.getSession().setAttribute("ognURL", url);
        }

        return "login";
    }

    @RequestMapping(value="/if.do")
    public String joinPace() {
        return "join";
    }


    @RequestMapping(value="/create.do")
    public String createAccount(HttpServletRequest request, Account account) {
        account.roleSetter("ROLE_USER","ROLE_ADMIN");
        customUserDetailService.save(account);

        return "redirect:/board/list.do";
    }


}
