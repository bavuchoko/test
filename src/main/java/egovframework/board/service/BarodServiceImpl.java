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
    public int insertUpdateBoard(HttpServletRequest request, BoardDto boardDto, FileVO fileVO) throws Exception {
        boardDto.setFileId(this.fileUpload(request, fileVO));
        return boardRepository.insertUpdateBoard(boardDto);
    }

    //파일업로드(수정업로드 포함)
    private String fileUpload(HttpServletRequest request, FileVO fileVO) throws Exception {
        //fileVO => 기존에 업로드 되었던 파일 리스트
        int sn= 0;
        if(StringUtils.hasText(fileVO.getAtchFileId())){
            //기존의 파일아이디와 sn 목록을 조회
            List<FileVO> pastFileList = egovFileMngService.selFileList(fileVO);
            //뷰에서 넘겨준 기존에 있던 파일리스트와 비교하여
            List noMatchList= pastFileList.stream().filter(e->
                 Arrays.stream(fileVO.getFileSn().split(","))
                         //뷰에서 넘어온 파일리스트의 sn과 비교해 기존리스트에 있는 sn 이 비어있을 경우
                         .noneMatch(Predicate.isEqual(e.getFileSn()))).collect(Collectors.toList());
            noMatchList.forEach(e-> {
                try {
                    //그 sn 에 해당하는 파일의 db정보를 삭제
                    egovFileMngService.delFile((FileVO)e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            //추가될 파일의 sn을 새로 할당
            sn = Integer.parseInt(fileVO.getFileSn().split(",")[fileVO.getFileSn().split(",").length-1])+1;
        }

        //files => 새롭게 추가된 파일
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


    @Override
    public void deleteBoard(BoardDto vo, FileVO fileVO) throws Exception {
        List<FileVO> pastFileList = egovFileMngService.selFileList(fileVO);
        pastFileList.forEach(e-> {
            try {
                egovFileMngService.delFile(e);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        egovFileMngService.delFileCom(fileVO.getAtchFileId());
        boardRepository.deleteBoard(vo);
    }
}
