<%--
  Created by IntelliJ IDEA.
  User: pjs
  Date: 2022-10-07
  Time: 오후 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/egovframework/script.jsp"/>

<script>
    function go_back() {
        window.history.back();
    }
    function confirm_join() {
        if(validator()){
            if(confirm("등록하시겠습니까?")){
                document.acfrm.action = "/account/create.do";
                document.acfrm.submit();
            }
        }
    }
var isMail =false;
    function validator() {
        with (document.acfrm){
            if (username.value == ""){
                alert("아이디를 입력하세요.");
                username.focus();
                return false;
            }
            if (userNickname.value == ""){
                alert("사용자명을 입력하세요.");
                userNickname.focus();
                return false;
            }
            if (password.value == ""){
                alert("비밀번호를 입력하세요.");
                password.focus();
                return false;
            }
            if (password.value == ""){
                alert("비밀번호를 확인해주세요.");
                passwordConfirm.focus();
                return false;
            }
            if (password.value != passwordConfirm.value){
                alert("비밀번호가 일치하지 않습니다.");
                passwordConfirm.focus();
                return false;
            }
            if(!isMail){
                alert("이메일 형식으로 입력하세요.");
                return false;
            }
        }
        return true;
    }

    function verifyEmail() {
        // 이메일 검증 스크립트 작성
        var emailVal = $("#username").val();

        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        // 검증에 사용할 정규식 변수 regExp에 저장

        if (emailVal.match(regExp) != null) {
            isMail= true;
        }
        else {
            alert('이메일 형식으로 입력하세요');
            isMail=false;
        }
    };
</script>

<h2>가입페이지</h2>

<div id="body_container">

    <form id="acfrm" name="acfrm" action="/account/create.do" >
        <input type="text" class="logininput" id="username" name="username" placeholder="아이디" onblur="verifyEmail()">
        <input type="text" class="logininput" name="userNickname" placeholder="사용자이름">
        <input type="password" class="logininput" name="password" placeholder="비밀번호">
        <input type="password" class="logininput" name="passwordConfirm" placeholder="비밀번호확인">

        <a class="login_btn btn_color last btn-pointer" onclick="confirm_join()">가입하기</a>
        <a class="got_back btn-pointer" onclick="go_back()">&#8701 뒤로가기</a>
    </form>
</div>
</body>
</html>
