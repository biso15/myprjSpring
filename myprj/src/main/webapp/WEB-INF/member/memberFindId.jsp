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
  
  const email = /[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[0-9a-zA-Z]$/i;
  const phone = /^\d{1,15}$/;  // 9 ~ 11자의 숫자만 사용
  
  function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;

	  if (fm.name.value == "") {
		  alert("이름을 입력해주세요");
		  fm.name.focus();
		  return;
	  } else if (fm.birthday.value == "") {
		  alert("생년월일을 입력해주세요");
		  fm.birthday.focus();
		  return;
	  } else if (fm.phone.value == "") {
		  alert("연락처를 입력해주세요");
		  fm.phone.focus();
		  return;
	  } else if (phone.test(fm.phone.value) == false) {
		  alert("연락처는 숫자만 입력해 주세요");
		  fm.membermail.value = "";
		  fm.membermail.focus();
		  return;
	  } 
	  	  	    
	  fm.action="${pageContext.request.contextPath}/member/memberFindIdAction.do";
	  fm.method="post";
	  fm.submit();
	  
	  return;
  }
  </script>
  
 <%@ include file="/WEB-INF/header.jsp" %>
 
   <div class="d-flex align-items-center justify-content-between mb-4">
      <h2>아이디 찾기</h2>
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
          <li class="breadcrumb-item active" aria-current="page">아이디 찾기</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->     
    <div class="findid w-100 m-auto align-items-center">
      <form name="frm">
        <div class="mb-3">
          <label for="name" class="form-label">이름</label>
          <input type="text" class="form-control" id="name" name="name" placeholder="" value="" required="">
        </div>

        <div class="mb-3">
          <label for="birthday" class="form-label">생년월일</label>
          <input type="date" class="form-control" id="birthday" name="birthday" placeholder="" value="" required="">
        </div>

        <div class="mb-3">
          <label for="phone" class="form-label">연락처</label>
          <input type="tel" class="form-control" id="phone" name="phone" placeholder="" value="" required="">
        </div>

        <button class="btn btn-primary w-100 py-2 mt-1" type="button" onClick="check()">아이디 찾기</button>
      </form>
    </div>
        
    <%@ include file="/WEB-INF/footer.jsp" %>
 </body>
</html>
