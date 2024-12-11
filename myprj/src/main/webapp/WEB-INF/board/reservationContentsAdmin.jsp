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
			  fm.action="${pageContext.request.contextPath}/reservation/${requestScope.rd.ridx}/reservationDeleteAction.do";
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
          <!-- <li class="breadcrumb-item">
            <a class="link-body-emphasis fw-semibold text-decoration-none" href="#">Library</a>
          </li> -->
          <li class="breadcrumb-item active" aria-current="page">${requestScope.menu}</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->   
    <div class="list">
	    <table class="table table-sm table-bordered">
	      <colgroup>
	        <col width="20%">
	      </colgroup>
	      <tbody>
	      <tr>
	        <th scope="row" class="text-center p-3">제목</th>
	        <td class="p-3"><a href="${pageContext.request.contextPath}/board/${requestScope.rd.bidx}/boardContents.do" class="ellipsis">${requestScope.rd.title}</a></td>
	      </tr>
	      <tr>
	        <th scope="row" class="text-center p-3">여행기간</th>
	        <td class="p-3">${requestScope.rd.startday} ~ ${requestScope.rd.endday}</td>
	      </tr>
	      <tr>
	        <th scope="row" class="text-center p-3">예약자</th>
	        <td class="p-3">${requestScope.rd.name}, ${requestScope.rd.phone}</td>
	      </tr>
	      <tr>
	        <th scope="row" class="text-center p-3">상품가격</th>
	        <td class="p-3">
	          <p id="adult-detail"></p>
	          <p id="child-detail"></p>
	          <p class="m-0" id="totalprice"></p>
	        </td>
	      </tr>
	      </tbody>
	    </table>
	</div>

    <div class="text-center">
	  <form name="frm">
	   	  <input type="hidden" name="cidx" value="${requestScope.rd.cidx}">
	   	  <input type="hidden" name="bidx" value="${requestScope.rd.bidx}">
	      <a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/reservation/${requestScope.rd.ridx}/${requestScope.rd.cidx}/${requestScope.rd.bidx}/reservationModify.do">수정</a>
	      <button type="button" class="btn btn-primary mb-3" onclick="del();">삭제</button>
	      <button type="button" class="btn btn-primary mb-3" onclick="history.back();">목록</button>
	  </form>
    </div>
    
    <%@ include file="/WEB-INF/footer.jsp" %>   

	<script>
	const adultDetail = document.querySelector("#adult-detail");
	const childDetail = document.querySelector("#child-detail");
	const totalprice = document.querySelector("#totalprice");

	const adultnumber = ${requestScope.rd.adultnumber};
	const adultprice = ${requestScope.rd.adultprice};
	const formattedAdultPrice = Number(adultprice).toLocaleString();

	const childnumber = ${requestScope.rd.childnumber};
	const childprice = ${requestScope.rd.childprice};
	const formattedChildPrice = Number(childprice).toLocaleString();
	
	adultDetail.textContent = "성인👩 " + formattedAdultPrice + "원 × ${requestScope.rd.adultnumber}명 = " + (adultnumber * adultprice).toLocaleString() + "원";
	childDetail.textContent = "아동👶 " + formattedChildPrice + "원 × ${requestScope.rd.childnumber}명 = " + (childnumber * childprice).toLocaleString() + "원";
	totalprice.textContent = "총금액 " + (adultnumber * adultprice + childnumber * childprice).toLocaleString() + "원";
	
	</script>
</body>
</html>