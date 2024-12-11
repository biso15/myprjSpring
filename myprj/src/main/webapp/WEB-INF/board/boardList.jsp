<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="ko" data-bs-theme="auto">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
  <meta name="generator" content="Hugo 0.122.0">
  <title>개인프로젝트</title>

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
          <!-- <li class="breadcrumb-item">
            <a class="link-body-emphasis fw-semibold text-decoration-none" href="#">Library</a>
          </li> -->
          <li class="breadcrumb-item active" aria-current="page">${requestScope.menu}</li>
        </ol>
      </nav>
    </div>
   
    <!-- 검색영역 -->
    <c:choose>
    <c:when test="${sessionScope.adminyn == 'Y' || requestScope.boardcode == 'free'}">
    <div class="d-flex justify-content-between">
    <a href="${pageContext.request.contextPath}/board/${requestScope.boardcode}/${requestScope.period}/boardWrite.do" class="btn btn-outline-primary">글쓰기</a>
    </c:when>
    <c:otherwise>
    <div class="d-flex justify-content-end">
    </c:otherwise>
    </c:choose>
      <form class="d-flex w-35" role="search" name="frm" action="${pageContext.request.contextPath}/board/${requestScope.boardcode}/${requestScope.period}/boardList.do" method="get">
        <select class="form-select me-2" name="searchType">
          <option value="title" selected>제목</option>
          <option value="contents">내용</option>
        </select>
        <div class="d-flex col-9">
          <input class="form-control me-2 w-auto search" type="search" name="keyword" placeholder="검색어를 입력해주세요." value="">
          <button class="btn btn-secondary" type="submit">검색</button>
        </div>
      </form>
    </div>

    <!-- 목록 --> 
    <div class="py-5 list">
      <table class="table">
        <colgroup>
          <col width="10%">
          <col width="">
          <col width="8%">
          <col width="10%">
          <col width="8%">
        </colgroup>
        <thead>
          <tr class="text-center">
            <th>순번</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${requestScope.blist}" var="bd" varStatus="status">
          <tr>
            <td class="text-center">${requestScope.pm.totalCount - status.index}</td>
            <td><a class="ellipsis" href="${pageContext.request.contextPath}/board/${bd.bidx}/boardContents.do">${bd.title}</a></td>
            <td class="text-center">${bd.name}</td>
            <td class="text-center">${fn:substringBefore(bd.date, ' ')}</td>  <!-- 뒤쪽의 시간부분 삭제 -->
            <td class="text-center">${bd.view}</td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      
	  <!-- 페이징 -->
      <c:set var="queryParam" value="keyword=${requestScope.pm.scri.keyword}&searchType=${requestScope.pm.scri.searchType}"></c:set>
      <ul class="pagination mt-5 justify-content-center">
        <c:if test="${requestScope.pm.prev == true}">
        <li class="page-item">
          <a class="page-link" href="${pageContext.request.contextPath}/board/boardList.do?page=${requestScope.pm.startPage - 1}&${queryParam}" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
		</c:if> 
		
        <c:forEach var="i" begin="${requestScope.pm.startPage}" end="${requestScope.pm.endPage}" step="1">
        <li class="page-item"><a class="page-link <c:if test="${i == requestScope.pm.scri.page}"> active</c:if>" href="${pageContext.request.contextPath}/board/${requestScope.boardcode}/${requestScope.period}/boardList.do?page=${i}&${queryParam}">${i}</a></li>
        </c:forEach>
        
        <c:if test="${requestScope.pm.next == true && requestScope.pm.endPage > 0}">
		<li class="page-item">
          <a class="page-link" href="${pageContext.request.contextPath}/board/boardList.do?page=${requestScope.pm.endPage + 1}&${queryParam}" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
		</c:if>        
      </ul>
    </div>
    
    <%@ include file="/WEB-INF/footer.jsp" %>   
  </div>
</body>
</html>