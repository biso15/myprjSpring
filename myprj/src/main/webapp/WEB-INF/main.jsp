<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
  <meta name="generator" content="Hugo 0.122.0">
  <title>개인프로젝트</title>

  <!-- 슬라이드 -->
  <script src="//code.jquery.com/jquery-3.3.1.min.js"></script>
  <script src="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script> 
  <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css" />
  <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css" />

  <script>
	// 메세지
	const msg = "${requestScope.msg}";
	if (msg != null && msg != "") {
	  alert(msg);
	}
	console.log(msg);
  </script>
	
    <%@ include file="header.jsp" %>

    <div class="main pb-5 pt-4">
      <div class="mb-4">
        <h2>인기여행지 TOP10</h2>
      </div>

      <!-- 목록 -->
      <div class="slide">
      	<c:forEach items="${requestScope.tlist}" var="bv" varStatus="status">	
      	<div class="px-2">
          <a href="${pageContext.request.contextPath}/board/${bv.bidx}/boardContents.do">
            <div class="card shadow-sm">
              <img src="${pageContext.request.contextPath}/board/displayFile.do?fileName=${bv.thumbnail}&type=thumbnail" alt="thumbnail">
              <div class="card-body">
                <p class="card-text ellipsis fw-bold">${bv.title}</p>
                <small class="text-body-secondary ellipsis ellipsis3">${bv.summary}</small>
              </div>
            </div>
          </a>
        </div>
		</c:forEach>		
      </div>

      <div class="mt-5 mb-4">
        <h2>공지사항</h2>
      </div>

      <!-- 목록 -->
      <div class="album notice">
        <div class="row g-3">
          <c:forEach items="${requestScope.nlist}" var="bv" varStatus="status">  
          <div class="col">
            <a class="card shadow-sm" href="#">
              <div class="card-body">
                <p class="card-text ellipsis ellipsis2">${bv.title}</p>
                <small class="text-body-secondary">${bv.date}</small>
              </div>
            </a>
          </div>
		  </c:forEach>
        </div>
      </div>
    </div>

    <%@ include file="footer.jsp" %>    
  </div>

  <script>
  $(document).ready(function(){
    $('.slide').slick({
      centerMode: true,
      centerPadding: '60px',
      slidesToShow: 4
    });
  });
  </script>
</body>
</html>