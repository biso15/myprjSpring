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
  <!-- 캘린더 -->
  <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
  <script>
  document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
    windowResizeDelay : 10,
    headerToolbar: {
      left: 'prev',
      center: 'title',
      right: 'next today'
    },
    aspectRatio: 1.5
    });

    calendar.render();
  });  
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
    <form class="detail pb-5">
      <div class="card text-center mb-3">
        <h3 class="card-title fw-bold mb-4">${bv.getTitle()}</h3>
        <p class="card-text text-body-secondary pt-4 border-top-dashed">${bv.getSummary()}</p>
      </div>
      
      <div class="card mb-3 d-flex">
        <div class="row">
          <div class="col-6">
            <div class="row g-0 border rounded shadow-sm p-4 h-100">
              <div id="calendar"></div>
            </div>
          </div>

          <div class="col-6">            
            <div class="row g-0 border rounded shadow-sm p-4">
              <p class="fw-bold mb-1">1. 여행기간</h5>
              <div class="pl-4">
                <p>2024-11-12 ~ 2024-11-14</p>
              </div>

              <p class="fw-bold mb-1 pt-3 border-top-dashed">2. 상품가격(1인)</p>
              <div class="pl-4">
                <p>성인👩 10,000원 │ 아동👶 5,000원</p>
              </div>

              <p class="fw-bold mb-1 pt-3 border-top-dashed">3. 신청인원</p>
              <div class="row mb-3">
                <div class="col-6 pl-4">
                  <label for="adultnumber" class="form-label">성인👩</label>
                  <input type="number" class="form-control" id="adultnumber" min="0" value="0">
                </div>
                <div class="col-6 pr-4">
                  <label for="childnumber" class="form-label">아동👶</label>
                  <input type="number" class="form-control" id="childnumber" min="0" value="0">
                </div>
              </div>
              
              <p class="fw-bold mb-1 pt-3 border-top-dashed">4. 총금액</p>
              <div class="pl-4">
                <p>0원</p>
              </div>
              
              <p class="fw-bold mb-1 pt-3 border-top-dashed">5. 예약자</p>
              <div class="row">
                <div class="col-6 pl-4">
                  <label for="name" class="form-label">이름</label>
                  <input type="text" class="form-control" id="reservationname" required="" value="노지혜" placeholder="홍길동">
                  <div class="invalid-feedback">이름을 입력해주세요.</div>
                </div>
                <div class="col-6 pr-4">
                  <label for="phone" class="form-label">연락처</label>
                  <input type="text" class="form-control" id="reservationphone" required="" value="01012341234" placeholder="01012345678">
                  <div class="invalid-feedback">연락처를 입력해주세요.</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button class="btn btn-primary mb-3" type="submit">예약하기</button>
      </div>
    </form>

    <%@ include file="/WEB-INF/footer.jsp" %>
</body>
</html>