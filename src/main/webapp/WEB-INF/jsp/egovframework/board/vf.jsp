<%--
  Created by IntelliJ IDEA.
  User: pjs
  Date: 2022-10-08
  Time: 오전 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/egovframework/script.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript">


    function go_back() {
        location.href = "/board/list.do";
    }
    function fn_egov_downFile(atchFileId, fileSn){
        if(confirm("파일을 다운로드 하시겠습니까?")){
            window.open("/cmm/fms/FileDown.do?atchFileId="+atchFileId+"&fileSn="+fileSn);
        }
    }

    function go_update(boardKey) {
        location.href = "/board/update.do?boardKey="+boardKey;
    }
    function go_delete(boardKey) {
        document.frm.boardKey =boardKey;
        document.frm.action = "/board/delete.do";
        document.frm.submit();

    }

</script>

    <form id="frm" name="frm" method="POST" action="/board/view.do" >
        <input type="hidden" id="pageIndex" name="pageIndex" value="${vo.pageIndex}">
        <input type="hidden" id="boardKey" name="boardKey" value="${vo.boardKey}">
        <input type="hidden" name="atchFileId" value="${fileList[0].atchFileId}" />
        <h2>게시글 보기</h2>


        <div id="tbl_main">
           제목<p>${vo.title}</p>

            <pre>${vo.body}</pre>
        </div>

        <div class="file_box">
            <div id="file_container">
                <c:forEach var="file" items="${fileList}" varStatus="i">
                    <p>
                        <span>
                            <c:out value="${i.index + 1}" />
                        </span>
                        <span class="file-name">
                            <c:if test="${file.fileExtsn ne 'pdf'}">
                                <img style='width: 30px; height:30px;' id='preImage_${i.index}' src='<c:url value="/cmm/fms/getImage.do"/>?atchFileId=${fileList[0].atchFileId}+&fileSn=${file.fileSn}' onclick="viewFile(this.src)" />
                            </c:if>
                            <c:if test="${file.fileExtsn eq 'pdf'}">
                                <img style='width: 30px; height:30px;' id='preImage_${i.index}' src='/images/common/pdf.png' onclick="viewFileViewPage('${file.atchFileId}','${file.fileExtsn}')" />
                            </c:if>
                            <a href="javascript:fn_egov_downFile('${file.atchFileId}','${file.fileSn}')">
                                <c:out value="${file.orignlFileNm}"/>&nbsp;[<c:out value="${file.fileMg}"/>&nbsp;byte]
                            </a>
                        </span>
                    </p>
                </c:forEach>
                <c:if test="${empty fileList}">
                    <tr>
                        <td colspan="2"><span  class="nofile">등록된 파일이 없습니다.</span></td>
                    </tr>
                </c:if>
            </div>

        </div>

        <div class="bottom_menu">
            <a class="btn" onclick="go_back()">목록으로</a>
            <c:if test="${hasAuthority}">
                <a class="btn" onclick="go_update(${vo.boardKey})">수정</a>
                <a class="btn" onclick="go_delete(${vo.boardKey})">삭제</a>
            </c:if>
        </div>
    </form>
</body>
</html>
