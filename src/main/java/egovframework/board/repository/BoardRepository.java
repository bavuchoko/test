package egovframework.board.repository;


import egovframework.board.dto.BoardDto;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository extends EgovAbstractDAO {
    public void insertUpdateBoard(BoardDto boardDto) {
        insert("board.insertUpdateBoard", boardDto);
    }

    public BoardDto selectBoard(BoardDto boardDto) {
        return (BoardDto) select("board.selectBoard", boardDto);
    }

    public List selectBoardList(BoardDto boardDto) {
        return list("board.selectBoardList", boardDto);
    }

    public int selectBoardListCnt(BoardDto vo) {
        return (int)select("board.selectBoardListCnt", vo);
    }
}
