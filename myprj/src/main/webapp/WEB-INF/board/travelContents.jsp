<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ko" data-bs-theme="auto">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
  <meta name="generator" content="Hugo 0.122.0">
  <title>개인프로젝트</title>  
  
  <script>
	function del() {
		
        let fm = document.frm;
		let ans = confirm("삭제하시겠습니까?");
	  	  if (ans == true) {
			  fm.action="${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/boardDeleteAction.do";
			  fm.method="post";
			  fm.submit();
		}
		
		return;		
	}
  </script>
  <%@ include file="/WEB-INF/header.jsp" %>
    
    <div class="d-flex align-items-center justify-content-between mb-4">
      <h2>${requestScope.menu}</h2>
      <!-- 네비게이션 -->
      <nav aria-label="breadcrumb">
        <svg xmlns="http://www.w3.org/2000/svg" class="d-none">
          <symbol id="house-door-fill" viewBox="0 0 16 16">
            <path d="M6.5 14.5v-3.505c0-.245.25-.495.5-.495h2c.25 0 .5.25.5.5v3.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5z"/>
          </symbol>
        </svg>
        <ol class="breadcrumb breadcrumb-chevron p-3 justify-content-end">
          <li class="breadcrumb-item">
            <a class="link-body-emphasis" href="${pageContext.request.contextPath}">
              <svg class="bi" width="16" height="16"><use xlink:href="#house-door-fill"></use></svg>
              <span class="visually-hidden">Home</span>
            </a>
          </li>
          <li class="breadcrumb-item active" aria-current="page">${requestScope.menu}</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->
    <div class="detail pb-5">
      <div class="card text-center mb-3">
        <h3 class="card-title fw-bold mb-4">${requestScope.bv.getTitle()}</h3>
        <p class="card-text text-body-secondary pt-4 border-top-dashed">${requestScope.bv.getSummary()}</p>
      </div>

      <div class="card contents mb-3 p-4">
          ${requestScope.bv.getContents()}
      </div>

      <div class="text-center">
	    <form name="frm">
   		<input type="hidden" name="boardcode" value="${requestScope.bv.boardcode}">
   		<input type="hidden" name="period" value="${requestScope.bv.period}">
      		<c:choose>
	    	<c:when test="${sessionScope.adminyn == 'Y'}">
        	<a href="${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/boardModify.do" class="btn btn-primary mb-3">글수정</a>
	        <button type="button" class="btn btn-primary mb-3" onClick="del()">글삭제</button>
        	<a href="${pageContext.request.contextPath}/calendar/${requestScope.bv.bidx}/calendarWrite.do" class="btn btn-primary mb-3">일정관리</a>
	        </c:when>
    		<c:otherwise>
        	<a href="${pageContext.request.contextPath}/reservation/${requestScope.bv.bidx}/reservationWrite.do" class="btn btn-primary mb-3">예약</a>
        	</c:otherwise>
    		</c:choose>
        	<button type="button" onclick="history.back();" class="btn btn-primary mb-3">목록</button>
	    </form>
      </div>
    </div>

    <%@ include file="/WEB-INF/footer.jsp" %>
  </div>
</body>
</html>
