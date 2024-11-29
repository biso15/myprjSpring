<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">

</head>
<body>

<div class="container d-flex flex-column">
	<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
      <a href="${pageContext.request.contextPath}" class="d-flex align-items-center link-body-emphasis text-decoration-none">
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="32" fill="currentColor" class="bi bi-suitcase" viewBox="0 0 16 16">
          <path d="M6 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 6 5m2 0a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m2 0a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 10 5"/>
          <path d="M6.5 0a.5.5 0 0 0-.5.5V3H5a2 2 0 0 0-2 2v8a2 2 0 0 0 1.031 1.75A1.003 1.003 0 0 0 5 16a1 1 0 0 0 1-1h4a1 1 0 1 0 1.969-.25A2 2 0 0 0 13 13V5a2 2 0 0 0-2-2h-1V.5a.5.5 0 0 0-.5-.5zM9 3H7V1h2zm3 10a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1z"/>
        </svg>
      </a>

      <ul class="nav me-auto justify-content-center">
          <li><a href="${pageContext.request.contextPath}/board/travel/1/boardList.do" class="nav-link px-3 link-body-emphasis">당일치기</a></li>
          <li><a href="${pageContext.request.contextPath}/board/travel/2/boardList.do" class="nav-link px-3 link-body-emphasis">1박2일</a></li>
          <li><a href="${pageContext.request.contextPath}/board/travel/3/boardList.do" class="nav-link px-3 link-body-emphasis">2박3일</a></li>
          <li><a href="${pageContext.request.contextPath}/board/travel/4/boardList.do" class="nav-link px-3 link-body-emphasis">3박4일</a></li>
          <li><a href="${pageContext.request.contextPath}/board/free/0/boardList.do" class="nav-link px-3 link-body-emphasis">자유게시판</a></li>
          <li><a href="${pageContext.request.contextPath}/board/notice/0/boardList.do" class="nav-link px-3 link-body-emphasis">공지사항</a></li>
          <c:if test="${!empty sessionScope.midx}">
          <li><a href="${pageContext.request.contextPath}/board/reservation/0/boardList.do" class="nav-link px-3 link-body-emphasis">예약확인</a></li>
          </c:if>
      </ul>

      <ul class="nav">
      	<c:if test="${empty sessionScope.midx}">
        <li class="nav-item"><a href="${pageContext.request.contextPath}/member/memberLogin.do" class="nav-link link-body-emphasis px-2">로그인</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/member/memberJoin.do" class="nav-link link-body-emphasis px-2">회원가입</a></li>
        </c:if>
        <c:if test="${!empty sessionScope.midx}">
        <li class="nav-item"><a href="${pageContext.request.contextPath}/member/memberMypage.do" class="nav-link link-body-emphasis px-2">마이페이지</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/member/memberLogout.do" class="nav-link link-body-emphasis px-2">로그아웃</a></li>
        </c:if>
      </ul>

      <!-- <div class="text-end">
          <button type="button" class="btn btn-primary">로그인</button>
          <button type="button" class="btn btn-outline-primary">회원가입</button>
          <button type="button" class="btn btn-outline-primary">마이페이지</button>
      </div> -->
    </header>