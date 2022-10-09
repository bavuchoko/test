package egovframework.board.service;

import egovframework.board.dto.BoardDto;
import egovframework.fileManager.FileVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BoardService {


    int insertUpdateBoard(HttpServletRequest request, BoardDto boardDto, FileVO fileVO) throws Exception;

    BoardDto getBoard(BoardDto boardDto);

    List selectBoardList(BoardDto boardDto);

    int selectBoardListCnt(BoardDto vo);

    void deleteBoard(BoardDto vo, FileVO fileVO) throws Exception;
}
