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
  <script src="https://code.jquery.com/jquery-latest.min.js"></script>

  <script>
  	// 게시글 삭제
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
	
	
	// 댓글 삭제
	function commentDel(cidx) {
		
		let ans = confirm("삭제하시겠습니까?");
		
		if(ans == true) {
			$.ajax({
				type: "get",  // 전송방식
				url: "${pageContext.request.contextPath}/comment/" + cidx + "/commentDeleteAction.do",
				dataType: "json",
				success: function(result) {
					// alert("전송성공");
					alert("삭제가 완료되었습니다.");
					$.boardCommentList();
				},
				error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
					alert("전송실패");
				}
			});
		}
		
		return;
	}

	// 댓글 리스트 새로고침
	$.boardCommentList = function() {

		$.ajax({
			type: "get",  // 전송방식
			url: "${pageContext.request.contextPath}/comment/${requestScope.bv.bidx}/commentList.do",
			dataType: "json",
			success: function(result) {
				// alert("전송성공 테스트");
				
				var str = "";
				var strTr = "";
				$(result.clist).each(function() {
					var btn = "";
					if (this.midx == "${sessionScope.midx}" || ${sessionScope.adminyn == "Y"}) {
						btn = "<button type='button' class='btn btn-primary btn-comment' onclick='commentDel(" + this.cidx + ")'>댓글 삭제</button>";
					}
			        
					strTr += "<div class='d-flex gap-2'><div class='row p-3 border m-0 d-flex justify-content-between align-items-center flex-fill'>" + 
							"<strong class='fw-semibold col-1'>" + this.name + "</strong>" + 
							"<span class='d-block flex-fill col-8'>" + this.contents + "</span>" + 
							"<span class='small col-2 text-right'>" + this.date.split(' ')[0] + "</span>" + 
							"</div>" + btn + "</div>";
				});
				
				str = str + strTr;
				
				$(".list-group").html(str);

			},
		    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				alert("전송실패 테스트");
			    console.log("Error Status: " + status);
			    console.log("Error Detail: " + error);
			    console.log("Response: " + xhr.responseText);
			}
		});
	}
	

	$(document).ready(function() {
		$.boardCommentList();
	
	 // 댓글 작성
	 	$("#cmtBtn").click(function() {
			let contents = $("#contents").val();
			
			if (contents == "") {
				alert("내용을 입력해주세요");
				$("#contents").focus();
				return;
			}
			
			$.ajax({
				type: "post",  // 전송방식
				url: "${pageContext.request.contextPath}/comment/commentWriteAction.do",
				dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
				data: {"contents": contents, "bidx": "${requestScope.bv.bidx}", "midx": "${sessionScope.midx}"},
				success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
					
					// alert("전송성공 테스트");
					if(result.value == 1) {
						$("#contents").val("");
					}

					alert("등록이 완료되었습니다.");
					$.boardCommentList();
					
				},
			    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
					alert("전송실패 테스트");
				    /* console.log("Error Status: " + status);
				    console.log("Error Detail: " + error);
				    console.log("Response: " + xhr.responseText);  */
				}
			});
		})

	})
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
    <form class="detail pb-5" name="frm">
   	  <input type="hidden" name="boardcode" value="${requestScope.bv.boardcode}">
   	  <input type="hidden" name="period" value="${requestScope.bv.period}">
      <div class="card text-center mb-3">
        <h3 class="card-title fw-bold mb-4">${requestScope.bv.title}</h3>
        <div class="card-text text-body-secondary pt-4 border-top-dashed">
          ${requestScope.bv.contents}
        </div>
      </div>      
      
      <c:if test="${requestScope.bv.boardcode == 'free'}">
      <div class="d-flex mb-2 gap-2">        
        <textarea class="form-control flex-fill" rows="2" name="contents" id="contents"></textarea>
        <button type="button" class="btn btn-primary btn-comment" id="cmtBtn">댓글 등록</a>
      </div>
      <div class="list-group d-grid gap-2 mb-3"></div>
	  </c:if>
	  
      <div class="text-center">
    	<c:if test="${sessionScope.adminyn == 'Y' || sessionScope.midx == requestScope.bv.midx}">
        <a href="${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/boardModify.do" class="btn btn-primary mb-3">수정</a>
        </c:if>
        <c:if test="${sessionScope.adminyn == 'Y'}">
        <button type="button" class="btn btn-primary mb-3" onClick="del()">삭제</button>
    	</c:if>
        <button type="button" class="btn btn-primary mb-3" onclick="history.back();">목록</button>
      </div>
    </form>

    <%@ include file="/WEB-INF/footer.jsp" %>   
  </div>
</body>
</html>