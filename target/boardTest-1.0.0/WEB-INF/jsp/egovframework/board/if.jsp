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
<link href="/css/toggle.css" rel="stylesheet"/>
<script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
    var validate = true;




    function go_insert() {
        if(validator()){
            if(confirm("등록하시겠습니까?")){
                $("#mloader").show();
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

    function addMailAddred() {
        var html="";
        html += "<input type='text' id='mailList' class='mailList' name='mailList' placeholder='공백없이 , 로 이메일 추가'>";
        $(html).appendTo(".bottom_menu");
    }

    function removeMailAddred() {
        $("#mailList").remove();
    }

    function toggleMail() {
        if(!document.getElementById("toggle").checked){
            $(".toggle_msg").text("메일발송 ON")
            addMailAddred();

        }else {
            $(".toggle_msg").text("메일발송 OFF")
            removeMailAddred();
        }
    }
</script>

    <form id="frm" name="frm" method="POST" action="/board/create.do"  enctype="multipart/form-data">
        <input type="hidden" id="pageIndex" name="pageIndex" value="${vo.pageIndex}">
        <input type="hidden" id="boardKey" name="boardKey" value="${vo.boardKey}">
        <h2>게시글 쓰기</h2>


        <div id="tbl_main">
            <input type="text" class="board_title" name="title" placeholder="제목을 입력하세요" value="">
            <textarea class="board_body" name="body"></textarea>
        </div>

        <div class="file_box">
            <div id="file_container">


            </div>

            <a class="file_btn del" onclick="delRow();"> - 제거</a>
            <a class="file_btn add" onclick="addFile();"> + 추가</a>
        </div>
        <div class="toggle-button-cover">
        <div class="button-cover">

        </div>
        <div class="bottom_menu">
            <span class="toggle_msg">메일발송 OFF</span>
            <input type="checkbox" id="toggle" hidden name="mailSend">
            <label for="toggle" class="toggleSwitch" onclick="toggleMail()">
                <span class="toggleButton"></span>
            </label>
            <a class="btn" onclick="go_back()">목록으로</a>
            <a class="btn" onclick="go_insert()">저장</a>
        </div>

    </form>
</body>
</html>
