package egovframework.board.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardController {

    @RequestMapping("main.do")
    @ResponseBody
    public String main() {
        return "hello";
    }
}
