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

    function go_join() {
        location.href = "/account/if.do";
    }

    function validator() {
        with (document.acfrm){
            if (username.value == ""){
                alert("아이디를 입력하세요.");
                username.focus();
                return false;
            }
            if (password.value == ""){
                alert("비밀번호를 입력하세요.");
                password.focus();
                return false;
            }
        }
    }
</script>

<h2>로그인페이지</h2>

<div id="body_container">

    <form id="acfrm" name="acfrm" action="/login" method="POST" >
     <input type="text" class="logininput" name="username" placeholder="아이디를 입력하세요">
     <input type="password" class="logininput" name="password" placeholder="비밀번호를 입력하세요">
     <input type="submit" class="logininput btn_color btn-pointer" value="로그인">
     <a class="login_btn btn_color last btn-pointer" onclick="go_join()">가입하기</a>
     <a class="got_back btn-pointer" onclick="go_back()">&#8701 뒤로가기</a>
    </form>
</div>
</body>
</html>
