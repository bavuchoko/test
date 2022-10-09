package egovframework.board.service;

import egovframework.board.dto.BoardDto;
import egovframework.board.repository.BoardRepository;
import egovframework.common.EgovStringUtil;
import egovframework.fileManager.EgovFileMngService;
import egovframework.fileManager.EgovFileMngUtil;
import egovframework.fileManager.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BarodServiceImpl implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EgovFileMngUtil egovFileMngUtil;

    @Autowired
    EgovFileMngService egovFileMngService;

    @Override
    public void insertUpdateBoard(HttpServletRequest request, BoardDto boardDto, FileVO fileVO) throws Exception {
        boardDto.setFileId(this.fileUpload(request, fileVO));
        boardRepository.insertUpdateBoard(boardDto);
    }

    //파일업로드(수정업로드 포함)
    private String fileUpload(HttpServletRequest request, FileVO fileVO) throws Exception {
        int sn= 0;
        if(StringUtils.hasText(fileVO.getAtchFileId())){
            List<FileVO> pastFileList = egovFileMngService.selFileList(fileVO);
            List noMatchList= pastFileList.stream().filter(e->
                 Arrays.stream(fileVO.getFileSn().split(","))
                         .noneMatch(Predicate.isEqual(e.getFileSn()))).collect(Collectors.toList());
            noMatchList.forEach(e-> {
                try {
                    egovFileMngService.delFile((FileVO)e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            sn = Integer.parseInt(fileVO.getFileSn().split(",")[fileVO.getFileSn().split(",").length-1])+1;
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        List<FileVO> result = null;

        String savedFileId = "";
        if(!files.isEmpty()){
            if(fileVO.getAtchFileId() != null && !fileVO.getAtchFileId().equals("")){
                egovFileMngService.delFileCom(fileVO.getAtchFileId());
            }
            result = egovFileMngUtil.parseFileInf(files, "files_", sn, fileVO.getAtchFileId(), "boardFiles");
            savedFileId = egovFileMngService.insertFileInfs(result);
        }
        return savedFileId;
    }

    @Override
    public BoardDto getBoard(BoardDto boardDto) {
        return boardRepository.selectBoard(boardDto);
    }

    @Override
    public List selectBoardList(BoardDto boardDto) {
        return boardRepository.selectBoardList(boardDto);
    }

    @Override
    public int selectBoardListCnt(BoardDto vo) {
        return boardRepository.selectBoardListCnt(vo);
    }
}
