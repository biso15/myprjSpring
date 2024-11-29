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
	// 메세지
	const msg = "${requestScope.msg}";
	if (msg != null && msg != "") {
	  alert(msg);
	}

	function check() {	  

	  // 유효성 검사하기
	  let fm = document.frm;
	  
	  if (fm.id.value == "") {
		  alert("아이디를 입력해주세요");
		  fm.id.focus();
		  return;
	  } else if (fm.password.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
	  }
	  
	  fm.action = "${pageContext.request.contextPath}/member/memberLoginAction.do";
	  fm.method = "post";
	  fm.submit();
	  
	  return;
    }
  </script>
</HEAD>

<BODY> 
<%@ include file="/WEB-INF/header.jsp" %>
    <div class="d-flex align-items-center justify-content-between mb-4">
      <h2>로그인</h2>
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
          <li class="breadcrumb-item active" aria-current="page">로그인</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->
    <form class="login w-100 m-auto align-items-center" name="frm">
      <div class="form-floating">
        <input type="text" class="form-control" id="id" name="id" placeholder="Id">
        <label for="id">아이디</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        <label for="password">비밀번호</label>
      </div>  
      <button class="btn btn-primary w-100 py-2 mt-1" type="button" onclick="check()">로그인</button>

      <div class="mt-4 mb-3 d-flex justify-content-between">
        <a href="${pageContext.request.contextPath}/member/memberFindId.do">아이디 찾기</a>
        <a href="${pageContext.request.contextPath}/member/memberFindPw.do">비밀번호 찾기</a>
        <a href="${pageContext.request.contextPath}/member/memberJoin.do">회원가입</a>
      </div>
    </form>
        
    <%@ include file="/WEB-INF/footer.jsp" %>
 </BODY>
</HTML>
