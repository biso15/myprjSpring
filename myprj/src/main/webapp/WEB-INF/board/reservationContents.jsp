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
            <a class="link-body-emphasis" href="#">
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
    <div class="card mb-3">
      <div class="row g-0">
        <div class="col-6">
          <div class="card-header">
            여행기간
          </div>
          <div class="card-body">
            <p class="card-text">${requestScope.rd.startday} ~ ${requestScope.rd.endday}</p>
          </div>
          <div class="card-header border-top">
            예약자
          </div>
          <div class="card-body">
            <p class="card-text">${requestScope.rd.name}, ${requestScope.rd.phone}</p>
          </div>
        </div>
        <div class="col-6 border-left">
          <div class="card-header">
            상품가격
          </div>
          <div class="card-body">
          	<p id="adult-detail" class="card-text"></p>
	        <p id="child-detail" class="card-text"></p>
	        <p class="child-detail m-0" id="totalprice"></p>
          </div>
        </div>
      </div>
    </div>

    <div class="detail pb-5">
      <div class="card text-center mb-3">
        <h3 class="card-title fw-bold mb-4">${requestScope.rd.title}</h3>
        <p class="card-text text-body-secondary pt-4 border-top-dashed">${requestScope.rd.summary}</p>
      </div>

      <div class="card contents mb-3 p-4">
          ${requestScope.rd.contents}
      </div>

      <div class="text-center">
        <button onclick="history.back();" class="btn btn-primary mb-3">목록</button>
      </div>
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
  </div>
</body>
</html>