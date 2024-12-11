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
  
  <!-- 에디터 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/ckeditor5Builder/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/ckeditor5Builder/ckeditor5/ckeditor5.css">
  
	<script>
	
	// 등록하기
	function check(action) {
	
	  // 유효성 검사하기
	  let fm = document.frm;  // 문자객체 안에 form 객체 생성하기
	  
	  if (fm.title.value == "") {
		  alert("제목을 입력해주세요");
		  fm.title.focus();
		  return;
	  }
	  	  
	  let ans = confirm("저장하시겠습니까?");	  	  
 	  if (ans == true) {
 		  
		  function cleanHTML(html) {
		      const parser = new DOMParser();
		      const doc = parser.parseFromString(html, 'text/html');
		
		      // ck-widget__resizer 클래스 제거
		      doc.querySelectorAll('.ck.ck-reset_all.ck-widget__resizer').forEach(el => el.remove());
		      doc.querySelectorAll('.ck.ck-reset_all.ck-widget__type-around').forEach(el => el.remove());
		      doc.querySelectorAll('.ck-fake-selection-container').forEach(el => el.remove());
		      return doc.body.innerHTML;
		  }
		
	  	  // 데이터 로드 후 처리
 		  const ckContent = document.querySelector(".ck-content");
	  	  if(ckContent) {
		      const cleanedHTML = cleanHTML(ckContent.innerHTML);
	 		  fm.contents.value = cleanedHTML;	  		  
	  	  }
 		  
 		  fm.action="${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/boardModifyAction.do";
		  fm.method="post";
		  fm.enctype="multipart/form-data";
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
          <!-- <li class="breadcrumb-item">
            <a class="link-body-emphasis fw-semibold text-decoration-none" href="#">Library</a>
          </li> -->
          <li class="breadcrumb-item active" aria-current="page">${requestScope.menu}</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->
    <form class="write pb-5" name="frm">
      <input type="hidden" class="isFileChange" name="isFileChange" value="false">  <!-- bidx값이 수정할때 필요해서 hidden으로 안보이게 한 input에 넣어서 controller로 보낸다. -->
	  <input type="file" name="attachfile" class="form-control d-none">
      <div class="card mb-3">
        <div class="row">
          <div class="mb-3">
            <label for="title" class="form-label">제목</label>
            <input type="text" class="form-control" id="title" value="${requestScope.bv.title}" name="title">
          </div>
          <label class="form-label">내용</label>
          <c:choose>
    	  <c:when test="${requestScope.bv.boardcode == 'notice'}">
          <div class="main-container">
			<div class="editor-container editor-container_classic-editor" id="editor-container">
			  <div class="editor-container__editor">
   			    <div id="editor">${requestScope.bv.contents}</div>
			  </div>
			</div>
		  </div>
		  <textarea class="d-none" name="contents" id="contents"></textarea>
		  </c:when>
	      <c:when test="${requestScope.bv.boardcode == 'free'}">
	      <div>
	      	<textarea class="form-control" rows="10" name="contents">${requestScope.bv.contents}</textarea>
	      </div>
	      </c:when>
	      </c:choose>
        </div>
      </div>

      <div class="text-center">
        <button type="button" class="btn btn-primary mb-3" onclick="check()">수정</button>
        <button type="button" class="btn btn-primary mb-3" onclick="history.back();">목록</button>
      </div>
    </form>

    <%@ include file="/WEB-INF/footer.jsp" %>
 
  <!-- 에디터 -->
  <script type="importmap">
  {
	"imports": {
	  "ckeditor5": "${pageContext.request.contextPath}/resources/ckeditor5Builder/ckeditor5/ckeditor5.js",
	  "ckeditor5/": "${pageContext.request.contextPath}/resources/ckeditor5Builder/ckeditor5/"
	}
  }
  </script>		
  <script type="module" src="${pageContext.request.contextPath}/resources/ckeditor5Builder/main.js"></script>
</body>
</html>