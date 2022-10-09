<%--
  Created by IntelliJ IDEA.
  User: pjs
  Date: 2022-10-08
  Time: 오전 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/egovframework/script.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String msg = (String)session.getAttribute("msg"); %>
<script type="text/javascript">

    $(document).ready(function(){
        var msg = '<%=msg%>';
        if(msg != "null"){
            alert(msg)
        }
    <% session.removeAttribute("msg"); %>
    });

    function go_insert() {
        location.href = "/board/create.do";
    }

    // 검색
    function serching(pageNo) {
        document.frm.pageIndex.value = pageNo;
        document.frm.submit();
    }

    // 검색
    function goView(boardKey) {
        location.href = "/board/view.do?boardKey="+boardKey;
    }


</script>

    <form id="boardfrm" name="frm" method="post" action="/board/list.do">
        <input type="hidden" id="pageIndex" name="pageIndex" value="${vo.pageIndex}">
        <h2>게시판</h2>

        <div class="top_menu">
            <sec:authorize access="isAuthenticated()">
                <a class="btn" href="/logout">로그아웃</a>
                <a class="btn" onclick="go_insert()">글쓰기</a>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <a class="btn" href="/account/loginPage.do">로그인</a>
            </sec:authorize>
        </div>

        <div id="tbl_main">

            <table>
                <thead >
                <tr>
                    <th style="width: 10%">순번</th>
                    <th style="width: 70%">제목</th>
                    <th style="width: 10%">등록자</th>
                    <th style="width: 10%">등록일</th>

                </tr>
                </thead>

                <tbody>

                <c:forEach var="list" items="${list}" varStatus="i">
                    <tr onclick="goView(${list.boardKey})">
                        <td>${paginationInfo.totalRecordCount+1 - (i.count+(vo.pageIndex-1)*10)}</td>
                        <td>${list.title}</td>
                        <td>${list.userNickname}</td>
                        <td>${fn:substring(list.wdate,0,10)}</td>
                    </tr>
                </c:forEach>

                <c:if test="${empty list }">
                    <tr>
                        <td colspan="4"> 내역이 없습니다. </td>
                    </tr>
                </c:if>
                </tbody>

            </table>
        </div>
        <div class="tbl_paging">
	  	    <span>
                <ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="serching" />
            </span>
        </div>
    </form>
</body>
</html>
