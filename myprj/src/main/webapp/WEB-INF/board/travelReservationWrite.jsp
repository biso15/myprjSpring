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
  <meta name="robots" content="noindex, nofollow">
  <title>개인프로젝트</title>
  <script src="https://code.jquery.com/jquery-latest.min.js"></script>

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
		    aspectRatio: 1.4,
		    // height: "100%",
		    titleFormat : function(date) {
			 	return date.date.year + '년 ' + (parseInt(date.date.month) + 1) + '월';
			 },
			events : "${pageContext.request.contextPath}/board/" + ${requestScope.bidx} + "/getCalendarAll.do",
/* 			dateClick: function(info) {
			    document.querySelector('#startday').value = info.dateStr;
			    const period = ${requestScope.bv.period};
			    
			    // startday에 period를 더해서 endday를 계산 후 입력
			    const date = new Date(info.dateStr);
			    date.setDate(date.getDate() + period - 1);  // 1 빼야됨 주의
			    const endday = date.toISOString().split('T')[0];  // 날짜만 추출
			    document.querySelector('#endday').value = endday;
			    
			    document.querySelector('#fromTo').textContent = info.dateStr + " ~ " + endday;
			 } */
		  });
		
	    calendar.render();
	    
	    calendarEl.addEventListener('click', function (e) {
	    
		    const fcDay = e.target.closest('.fc-day');  // closest() : 주어진 선택자와 일치하는 요소를 찾을 때까지, 자기 자신을 포함해 위쪽(부모 방향, 문서 루트까지)으로 문서 트리를 순회한다.
			if (fcDay) {  // .fc-day를 클릭한 경우
				
		    	const fcDayArr = document.querySelectorAll('.fc-day');
		    	fcDayArr.forEach((fcDay) => fcDay.classList.remove("select"));
				fcDay.classList.add("select");				

            	const fcDayData = fcDay.getAttribute('data-date');  // 클릭한 날짜의 date 속성 가져오기
				document.querySelector('#startday').value = fcDayData;
            	console.log(fcDayData)
            	
				const fcEvent = fcDay.querySelector('.fc-event');  // 자식 요소에 event가 있는지 찾는다.
            	
				if(fcEvent) {  // 해당 날짜의 event가 있을 때
	    			const events = calendar.getEvents().filter(event => event.startStr === fcDayData);  // 모든 event의 startStr과 fcDayData를 비교해서 일치하는 event를 찾는다.
	    			
	    			if (events.length > 0) {  // events는 항상 배열
	    				// 첫 번째 이벤트만 처리
	    				const event = events[0];
	    				
	   		        	// 가격을 천 단위 구분자로 포맷
	   		        	formattedAdultPrice = Number(event.extendedProps.adultprice);
	                    formattedChildPrice = Number(event.extendedProps.childprice);
	                    
	   		        	// 여행기간과 가격 정보 텍스트 업데이트
	   		        	document.querySelector('#fromTo').textContent = event.extendedProps.fromTo;
	   		        	document.querySelector('#adultprice').value = formattedAdultPrice.toLocaleString();
	   		        	document.querySelector('#childprice').value = formattedChildPrice.toLocaleString();
	   		        	
	    		    }
	            } else {
				    const period = ${requestScope.bv.period};
				    
				    // startday에 period를 더해서 endday를 계산 후 입력
				    const date = new Date(fcDayData);
				    date.setDate(date.getDate() + period - 1);  // 1 빼야됨 주의
				    const endday = date.toISOString().split('T')[0];  // 날짜만 추출
				    document.querySelector('#endday').value = endday;
				    
				    document.querySelector('#fromTo').textContent = fcDayData + " ~ " + endday;
   		        	document.querySelector('#adultprice').value = "0";
   		        	document.querySelector('#childprice').value = "0";
	            }
	        }
	    });
	    
	  
      function priceChange() {
    	const price = this.value;
    	
    	// 숫자인지 확인
    	const numberCheck = /^\d{1,15}$/;
   	    if (numberCheck.test(price) == false) {
   		    alert("숫자만 입력해주세요");
   		    this.focus();
   		    return;
   	    }
   	    
   		// 가격 입력시 천 단위 구분자로 포맷
    	this.value = Number(price).toLocaleString();
      }
   	
      document.querySelector('#adultprice').addEventListener("change", priceChange);
      document.querySelector('#childprice').addEventListener("change", priceChange);
   	
	
	
		// 등록하기
		function check() {
		
		  // 유효성 검사하기
		  let fm = document.frm;  // 문자객체 안에 form 객체 생성하기
		  if (fm.startday.value == "") {
			  alert("여행기간을 선택해주세요");
			  return;
		  } else if (fm.adultprice.value == "") {
			  alert("성인 가격을 입력해주세요");
			  fm.adultprice.focus();
			  return;
		  } else if (fm.childprice.value == "") {
			  alert("아동 가격을 입력해주세요");
			  fm.childprice.focus();
			  return;
		  }
		  
		  let ans = confirm("저장하시겠습니까?");
			  if (ans == true) {
	
				const startday = $("#startday").val();
				const endday = $("#endday").val();
				const adultprice = $("#adultprice").val().replace(',', '');
				const childprice = $("#childprice").val().replace(',', '');
				
				$.ajax({
					type: "post",  // 전송방식
					url: "${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/travelReservationWriteAction.do",
					dataType: "json",
					data: {"startday": startday, "endday": endday, "adultprice": adultprice, "childprice": childprice},
					success: function(result) {
						alert("저장되었습니다.");
						calendar.getEventSources().forEach(source => source.refetch());
					},
					error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
						alert("전송실패");
					
						console.log("Error Status: " + status);
					    console.log("Error Detail: " + error);
					    console.log("Response: " + xhr.responseText);
					}
				});
			}
			
			return;
		}
		
		document.querySelector('#save').addEventListener("click", check);

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
            <a class="link-body-emphasis" href="${pageContext.request.contextPath}">
              <svg class="bi" width="16" height="16"><use xlink:href="#house-door-fill"></use></svg>
              <span class="visually-hidden">Home</span>
            </a>
          </li>
          <li class="breadcrumb-item active" aria-current="page">${requestScope.menu}</li>
        </ol>
      </nav>
    </div>

    <!-- 컨텐츠 -->
    <form class="write pb-5" name="frm">
      <input type="hidden" name="startday" class="form-control" id="startday">
      <input type="hidden" name="endday" class="form-control" id="endday">
    	
      <div class="card mb-3 d-flex">
        <div class="row">
          <div class="col-6">
            <div class="row g-0 border rounded shadow-sm p-4 h-100">
              <div id="calendar"></div>
            </div>
          </div>

          <div class="col-6">            
            <div class="row g-0 border rounded shadow-sm p-4">
              <p class="fw-bold mb-1">1. 여행기간</p>
              <div class="pl-4">
                <p id="fromTo">달력에서 날짜를 선택해 주세요.</p>
              </div>

              <p class="fw-bold mb-1 pt-3 border-top-dashed">2. 상품가격(1인)</p>
              <div class="row">
                <div class="col-6 pl-4">
                  <label for="adultprice" class="form-label">성인👩</label>
                  <input type="text" class="form-control" id="adultprice" value="0" name="adultprice">
                </div>
                <div class="col-6 pr-4">
                  <label for="childprice" class="form-label">아동👶</label>
                  <input type="text" class="form-control" id="childprice" value="0" name="childprice">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button type="button" class="btn btn-primary mb-3" id="save">확인</button>
        <button type="button" class="btn btn-primary mb-3" onclick="history.back();">뒤로</button>
      </div>
    </form>
    
    <%@ include file="/WEB-INF/footer.jsp" %>	
</body>
</html>