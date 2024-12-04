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

	  if (fm.password.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
	  } else if (fm.passwordcheck.value == "") {
		  alert("비밀번호 확인을 입력해주세요");
		  fm.passwordcheck.focus();
		  return;		  
	  } else if(fm.password.value != fm.passwordcheck.value) {
		  alert("비밀번호가 다릅니다.");
		  fm.password.value = "";
		  fm.passwordcheck.value = "";		  
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
	  } else if (fm.email.value == "") {
		  alert("이메일을 입력해주세요");
		  fm.email.focus();
		  return;
	  } else if (email.test(fm.email.value) == false) {
		  alert("이메일 형식이 올바르지 않습니다");
		  fm.membermail.value = "";
		  fm.membermail.focus();
		  return;
	  }
	  	  
	  let ans = confirm("수정하시겠습니까?");	  
	  if (ans == true) {
		  fm.action="${pageContext.request.contextPath}/member/memberMypageAction.do";
		  fm.method="post";
		  fm.submit();
	  }
	  
	  return;
  }
  
  function cancel() {
	  
	  let ans = confirm("탈퇴하시겠습니까?");	  
	  if (ans == true) {
		  location.href = "${pageContext.request.contextPath}/member/memberDeleteAction.do";
	  }
	  
	  return;
  }
  
  </script>

   <%@ include file="/WEB-INF/header.jsp" %>
    
    <div class="d-flex align-items-center justify-content-between mb-4">
      <h2>마이페이지</h2>
      <!-- 네비게이션 -->
      <nav aria-label="breadcrumb">
        <svg xmlns="http://www.w3.org/2000/svg" class="d-none">
          <symbol id="house-door-fill" viewBox="0 0 16 16">
            <path d="M6.5 14.5v-3.505c0-.245.25-.495.5-.495h2c.25 0 .5.25.5.5v3.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5z"/>
          </symbol>
        </svg>
        <ol class="breadcrumb breadcrumb-chevron p-3 justify-content-end">
          <li class="breadcrumb-item">
            <a class="link-body-emphasis" href="#">
              <svg class="bi" width="16" height="16"><use xlink:href="#house-door-fill"></use></svg>
              <span class="visually-hidden">Home</span>
            </a>
          </li>
          <!-- <li class="breadcrumb-item">
            <a class="link-body-emphasis fw-semibold text-decoration-none" href="#">Library</a>
          </li> -->
          <li class="breadcrumb-item active" aria-current="page">마이페이지</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->
    <form class="mypage pb-5" name="frm">
      <div class="card mb-3">
        <div class="row">
          <div class="mb-3 col-4">
            <label for="id" class="form-label">아이디</label>
            <p class="mb-0 text">${requestScope.mv.getId()}</p>
          </div>
          <div class="mb-3 col-4">
            <label for="password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password" name="password" >
            <div class="invalid-feedback">비밀번호를 입력해주세요.</div>
          </div>
          <div class="mb-3 col-4">
            <label for="passwordcheck" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="passwordcheck">
            <div class="invalid-feedback">비밀번호 확인을 해주세요.</div>
          </div>

          <div class="mb-3 col-4">
            <label for="name" class="form-label">이름</label>
            <p class="mb-0 text">${requestScope.mv.getName()}</p>
          </div>
          <div class="mb-3 col-4">
            <label for="birthday" class="form-label">생년월일</label>
            <p class="mb-0 text">${requestScope.mv.getBirthday()}</p>
          </div>
          <div class="mb-3 col-4">
            <label for="phone" class="form-label">연락처</label>
            <input type="tel" class="form-control" id="phone" name="phone" value="${requestScope.mv.getPhone()}">
            <div class="invalid-feedback">연락처를 입력해주세요.</div>
          </div>

          <div class="mb-3 col-4">
            <label for="email" class="form-label">이메일</label>
            <input type="email" class="form-control" id="email" name="email" value="${requestScope.mv.getEmail()}">
            <div class="invalid-feedback">이메일을 입력해주세요.</div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button class="btn btn-primary mb-3" type="button" onClick="check()">수정하기</button>
        <button class="btn btn-primary mb-3" type="button" onClick="cancel()">탈퇴하기</button>
      </div>
    </form>

	<%@ include file="/WEB-INF/footer.jsp" %>
</body>
</html>
