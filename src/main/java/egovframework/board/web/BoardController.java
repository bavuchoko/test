package egovframework.board.web;

import egovframework.account.dto.Account;
import egovframework.board.dto.BoardDto;
import egovframework.board.service.BoardService;
import egovframework.common.CommonMethod;
import egovframework.common.EgovStringUtil;
import egovframework.config.MailSender;
import egovframework.fileManager.EgovFileMngService;
import egovframework.fileManager.FileVO;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/board")
public class BoardController {


    @Autowired
    BoardService boardService;

    @Autowired
    MailSender mailSender;

    @Autowired
    EgovFileMngService egovFileMngService;


    @RequestMapping(value = "/list.do")
    public String list(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) {

        //페이징과 기간검색을 위한 공통메서드 & 제너릭 메서드
        PaginationInfo paginationInfo = CommonMethod.getPagination(vo);
        vo = CommonMethod.setVoInfo(vo, model);

        //리스트 & 카운트
        List list = boardService.selectBoardList(vo);
        int cnt = boardService.selectBoardListCnt(vo);
        paginationInfo.setTotalRecordCount(cnt);
        model.addAttribute("list", list);
        model.addAttribute("paginationInfo", paginationInfo);
        redirectAttributes.addFlashAttribute("vo", vo);

        return "board/lf";
    }


    //등록페이지
    @RequestMapping(value = "/create.do", method = RequestMethod.GET)
    public String createPage(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) throws Exception {

        model.addAttribute("vo",vo);
        redirectAttributes.addFlashAttribute("vo", vo);
        return "board/if";
    }


    //등록기능
    @RequestMapping(value = "/create.do", method = RequestMethod.POST)
    public String create(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("vo") BoardDto vo,
            @ModelAttribute("fileVO") FileVO fileVO,
            ModelMap model) throws Exception {

        //게시글의 작성자 세팅
        //Todo : 공통함수화필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();
        vo.setUserId(account.getUserId());


        //파일업로드 & 게시글insert
        vo.setBoardKey(boardService.insertUpdateBoard(request, vo, fileVO));
        redirectAttributes.addFlashAttribute("vo", vo);


        if(vo.isMailSend()){
            String url ="http://localhost:8080/";
            String sender ="test.com";
            String[] mailList = EgovStringUtil.split(vo.getMailList(),",");
            mailSender.sendMail(vo, url, sender, mailList);
        }

        return "redirect:/board/list.do";
    }


    @RequestMapping(value = "/view.do", method = RequestMethod.GET)
    public String viewPage(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) throws Exception {

        vo = boardService.getBoard(vo);
        model.addAttribute("vo",vo);
        redirectAttributes.addFlashAttribute("vo", vo);

        boolean hasAuthority = CommonMethod.validateUser(vo) ? true :false;

        FileVO fileVO = new FileVO();
        fileVO.setAtchFileId(vo.getFileId());

        List<FileVO> result = egovFileMngService.selectFileInfs(fileVO);
        model.addAttribute("fileList", result);
        model.addAttribute("hasAuthority", hasAuthority);

        return "board/vf";
    }

    
    //수정페이지
    @RequestMapping(value = "/update.do", method = RequestMethod.GET)
    public String updatePage(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) throws Exception {


        vo = boardService.getBoard(vo);

        if(!CommonMethod.validateUser(vo)) {
            HttpSession session = request.getSession();
            session.setAttribute("msg","수정할 권한이 없습니다.");
            return "redirect:/board/list.do";
        }

        model.addAttribute("vo",vo);
        redirectAttributes.addFlashAttribute("vo", vo);

        FileVO fileVO = new FileVO();
        fileVO.setAtchFileId(vo.getFileId());

        List<FileVO> result = egovFileMngService.selectFileInfs(fileVO);
        model.addAttribute("fileList", result);

        return "board/uf";
    }


    //수정기능
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String update(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("fileVO") FileVO fileVO,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) throws Exception {

        BoardDto vo2 = boardService.getBoard(vo);
        if(!CommonMethod.validateUser(vo2)) {
            HttpSession session = request.getSession();
            session.setAttribute("msg","수정할 권한이 없습니다.");
            return "redirect:/board/list.do";
        }

        model.addAttribute("vo",vo);
        redirectAttributes.addFlashAttribute("vo", vo);

        //게시글의 작성자 세팅
        //Todo : 공통함수화필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!"anonymousUser".equals(authentication.getPrincipal())){
            Account account = (Account)authentication.getPrincipal();
            vo.setUserId(account.getUserId());
        }

        boardService.insertUpdateBoard(request, vo, fileVO);

        return "redirect:/board/list.do";
    }


    //수정기능
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    public String delete(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("fileVO") FileVO fileVO,
            @ModelAttribute("vo") BoardDto vo,
            ModelMap model) throws Exception {

        BoardDto vo2 = boardService.getBoard(vo);
        if(!CommonMethod.validateUser(vo2)) {
            HttpSession session = request.getSession();
            session.setAttribute("msg","삭제할 권한이 없습니다.");
            return "redirect:/board/list.do";
        }

        model.addAttribute("vo",vo);
        redirectAttributes.addFlashAttribute("vo", vo);



        boardService.deleteBoard(vo, fileVO);

        return "redirect:/board/list.do";
    }
    
}
