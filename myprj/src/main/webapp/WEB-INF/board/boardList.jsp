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
    <div class="d-flex justify-content-between">
      <c:if test="${sessionScope.adminyn.equals('Y')}">
      <a herf="${pageContext.request.contextPath}/board/${requestScope.boardcode}/${requestScope.period}/boardWrite.do" class="btn btn-outline-primary">글쓰기</a>
      </c:if>
      <form class="d-flex w-35" role="search" name="frm" action="${pageContext.request.contextPath}/board/${requestScope.boardcode}/${requestScope.period}/boardList.do" method="get">
        <select class="form-select me-2" name="searchType">
          <option value="title" selected>제목</option>
          <option value="summary">부제목</option>
          <option value="contents">내용</option>
        </select>
        <div class="d-flex col-9">
          <input class="form-control me-2 w-auto search" type="search" name="keyword" placeholder="검색어를 입력해주세요." value="" required="">
          <button class="btn btn-secondary" type="submit">검색</button>
        </div>
      </form>
    </div>

    <!-- 목록 -->
    <div class="album py-5">
      <div class="row row-cols-md-4 g-3">
        <c:forEach items="${requestScope.blist}" var="bv" varStatus="status">
        <a class="col" href="${pageContext.request.contextPath}/board/${bv.bidx}/boardContents.do">
          <div class="card shadow-sm">
            <img src="${pageContext.request.contextPath}/board/displayFile.aws?fileName=${requestScope.bv.filename}" alt="thumbnail">
            <div class="card-body">
              <p class="card-text ellipsis">${bv.title}</p>
              <small class="text-body-secondary ellipsis ellipsis3">${bv.summary}</small>
            </div>
          </div>
        </a>
        </c:forEach>
      </div>
      
      <!-- 페이징 -->
      <ul class="pagination mt-5 justify-content-center">
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item"><a class="page-link active" href="#">1</a></li>
        <li class="page-item"><a class="page-link" href="#">2</a></li>
        <li class="page-item"><a class="page-link" href="#">3</a></li>
        <li class="page-item"><a class="page-link" href="#">4</a></li>
        <li class="page-item"><a class="page-link" href="#">5</a></li>
        <li class="page-item"><a class="page-link" href="#">6</a></li>
        <li class="page-item"><a class="page-link" href="#">7</a></li>
        <li class="page-item"><a class="page-link" href="#">8</a></li>
        <li class="page-item"><a class="page-link" href="#">9</a></li>
        <li class="page-item"><a class="page-link" href="#">10</a></li>
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </div>
    
    
    <%@ include file="/WEB-INF/footer.jsp" %>   
  </div>









<%-- 

		<c:forEach items="${requestScope.blist}" var="bv" varStatus="status">  <!-- status.index는 0부터 시작 -->
		<tr>
			<td>${requestScope.pm.totalCount - ((status.index) + (requestScope.pm.scri.page-1) * requestScope.pm.scri.perPageNum)}</td>
			<td class="title"><a href="${pageContext.request.contextPath}/board/boardContents.aws?bidx=${bv.bidx}">
				<c:forEach var="i" begin="1" end="${bv.level_}" step="1">
					<c:if test="${i > 1}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${i == bv.level_}">
						└ &nbsp;
					</c:if>
				</c:forEach>
				${bv.subject}
			</a></td>
			<td>${bv.writer}</td>
			<td class="viewcnt">${bv.viewcnt}</td>
			<td class="recom">${bv.recom}</td>
			<td>${bv.writeday}</td>
		</tr>
		</c:forEach>		
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
		<c:set var="queryParam" value="keyword=${requestScope.pm.scri.keyword}&searchType=${requestScope.pm.scri.searchType}"></c:set>	
 		<ul>
 		<c:if test="${requestScope.pm.prev == true}">
			<li><a href="${pageContext.request.contextPath}/board/boardList.aws?page=${requestScope.pm.startPage - 1}&${queryParam}">◀</a></li>
		</c:if>
			
		<c:forEach var="i" begin="${requestScope.pm.startPage}" end="${requestScope.pm.endPage}" step="1">
			<li<c:if test="${i == requestScope.pm.scri.page}"> class="on"</c:if>>
			<a href="${pageContext.request.contextPath}/board/boardList.aws?page=${i}&${queryParam}">${i}</a></li>
		</c:forEach>
		
 		<c:if test="${requestScope.pm.next == true && requestScope.pm.endPage > 0}">  <!-- && requestScope.pm.endPage > 0 : 게시물이 0개일 경우 endPage가 0이 됨. 이때는 버튼이 없어야 함 -->
			<li><a href="${pageContext.request.contextPath}/board/boardList.aws?page=${requestScope.pm.endPage + 1}&${queryParam}">▶</a></li>
		</c:if>
		</ul>
	</div>
</section> --%>

</body>
</html>