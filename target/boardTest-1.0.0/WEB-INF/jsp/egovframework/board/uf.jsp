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


    function go_update() {
        if(validator()){
            if(confirm("저장 하시겠습니까?")){
                document.frm.submit();
            }
        }
    }

    function validator() {
        with (document.frm){
            if (title.value == ""){
                alert("제목을 입력하세요.");
                username.focus();
                return false;
            }
            if (body.value == ""){
                alert("본문을 입력하세요.");
                username.focus();
                return false;
            }
        }
        return true;
    }

    var fileIndex = parseInt("${fn:length(fileList)}");

    function addFile(){
        var innerStr = "";

        // 구분(행삭제)
        innerStr += "	<p id='fr_"+fileIndex+"' class='file_tr'>";
        innerStr += "		<span>";
        innerStr += "			<a class='del_x' onclick='delRow(this);'>X</a>";
        innerStr += "		</span>";
        // 파일명
        innerStr += "		<span>";
        innerStr += "		<img style='width: 30px; height:30px;' id='preImage_"+fileIndex+"' src='/images/common/question-sign.png' onclick='viewFile(this.src)' />";

        innerStr +=	"			<input type='hidden' id='fileKey' name='fileKey' value='0' />";
        innerStr +="			<input type='hidden' id='fileIndex' name='fileIndex' value='"+fileIndex+"' />";
        innerStr +="			<input type='file' id='filename_"+fileIndex+"' name='filename"+fileIndex+"' class='upload-hidden'  data-index='"+fileIndex+"' style='width:300px' onchange='uploadFile(this)'/>";
        innerStr += "           <input class='file-name' id='file-name"+fileIndex+"' value='파일선택' disabled='disabled'>";
        innerStr += "		<label for='filename_"+fileIndex+"' >파일선택</label>";
        innerStr += "		</span>";

        innerStr += "	</p>";

        $(innerStr).appendTo("#file_container");

        fileIndex++;
    }
    function delRow(obj){
        if(obj) {
            var tr = $(obj).parent().parent();
            tr.remove();
        }else{
            console.log(fileIndex)
            fileIndex--;
            $("#fr_"+fileIndex).remove();
        }
    }
    function go_back() {
        location.href = "/board/list.do";
    }

</script>

    <form id="frm" name="frm" method="POST" action="/board/update.do"  enctype="multipart/form-data">
        <input type="hidden" id="pageIndex" name="pageIndex" value="${vo.pageIndex}">
        <input type="hidden" id="boardKey" name="boardKey" value="${vo.boardKey}">
        <input type="hidden" name="atchFileId" value="${fileList[0].atchFileId}" />
        <h2>게시글 수정하기</h2>


        <div id="tbl_main">
            <input type="text" class="board_title" name="title" placeholder="제목을 입력하세요" value="${vo.title}">
            <textarea class="board_body" name="body">${vo.body}</textarea>
        </div>

        <div class="file_box">
            <div id="file_container">

                <c:forEach var="file" items="${fileList}" varStatus="i">
                    <p id='fr_${i.index}' class='file_tr'>
                    	<span>
                            <a class='del_x' onclick='delRow(this);'>X</a>
                        </span>
                    	<span>
                            <c:if test="${file.fileExtsn ne 'pdf'}">
                                <img style='width: 30px; height:30px;' id='preImage_${i.index}' src='<c:url value="/cmm/fms/getImage.do"/>?atchFileId=${fileList[0].atchFileId}+&fileSn=${file.fileSn}' onclick="viewFile(this.src)" />
                            </c:if>
                            <c:if test="${file.fileExtsn eq 'pdf'}">
                                <img style='width: 30px; height:30px;' id='preImage_${i.index}' src='/images/common/pdf.png' onclick="viewFileViewPage('${file.atchFileId}','${file.fileExtsn}')" />
                            </c:if>

                            <input type='hidden' id='fileKey' name='fileKey' value='0' />
                            <input type='hidden' id='fileIndex' name='fileIndex' value='${i.index}' />
                            <input type='file' id='filename_${i.index}' name='filename${i.index}' class='upload-hidden'  data-index='${i.index}' style='width:300px' onchange='uploadFile(this,${i.index})'/>
                            <input type="hidden" name="fileSn" id="fileSn_${i.index}" value="${file.fileSn}" />
                            <input class='file-name' id='file-name${i.index}' value='${file.orignlFileNm}' disabled='disabled'>
                        	<label for='filename_${i.index}' >파일선택</label>
                        </span>
                    </p>
                </c:forEach>
                <c:if test="${empty fileList}">
                    <tr>
                        <td colspan="2"><span  class="nofile">등록된 파일이 없습니다.</span></td>
                    </tr>
                </c:if>

            </div>

            <a class="file_btn del" onclick="delRow();"> - 제거</a>
            <a class="file_btn add" onclick="addFile();"> + 추가</a>
        </div>

        <div class="bottom_menu">
            <a class="btn" onclick="go_back()">목록으로</a>
            <a class="btn" onclick="go_update()">수정완료</a>
        </div>
    </form>
</body>
</html>
