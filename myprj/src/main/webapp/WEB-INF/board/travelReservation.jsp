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
  <script src="https://code.jquery.com/jquery-latest.min.js"></script>
  
  <!-- 캘린더 -->
  <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
  <script>
  // 캘린더    
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
    titleFormat : function(date) {
	 	return date.date.year + '년 ' + (parseInt(date.date.month) + 1) + '월';
	 },
	events : [ 
 		<c:if test="${!empty requestScope.clist}">
    	<c:forEach items="${requestScope.clist}" var="cv" varStatus="status">
            {
            	start : "${cv.startday}",
    	   	 	display: 'list-item',
    	   	 	backgroundColor: '#0d6efd;',
    	   	 	extendedProps: {
    	   	 		fromTo : "${cv.startday} ~ ${cv.endday}",
    	   	 		adultprice: "${cv.adultprice}",
    	        	childprice: "${cv.childprice}"
    	        }
             },
        </c:forEach>
    	</c:if>
		],
		eventSources: [{ // 구글 캘린더 API 키를 발급받은 경우 공휴일 데이터 추가
			googleCalendarId: "",
			backgroundColor: "transparent",
			borderColor: "transparent",
			className: "kr-holiday",
			textColor: "red"
		}]
    });
    
    calendar.render();

    
    let formattedAdultPrice = 0;
    let formattedChildPrice = 0;
    
 	// 이벤트 위임 방식으로 .fc-day 클릭 처리 -> calendar.render() 실행 후 처리하기 위해서
    calendarEl.addEventListener('click', function (e) {
    	
		// 클릭한 요소가 .fc-day인지 확인
		const fcDay = e.target.closest('.fc-day');  // closest() : 주어진 선택자와 일치하는 요소를 찾을 때까지, 자기 자신을 포함해 위쪽(부모 방향, 문서 루트까지)으로 문서 트리를 순회한다.
		if (fcDay) {  // .fc-day를 클릭한 경우
			
	    	const fcDayArr = document.querySelectorAll('.fc-day');
	    	fcDayArr.forEach((fcDay) => fcDay.classList.remove("select"));			
			fcDay.classList.add("select");
			
			const fcEvent = fcDay.querySelector('.fc-event');  // 자식 요소에 event가 있는지 찾는다.
          
			if(fcEvent) {  // 해당 날짜의 event가 있을 때   		  
    			const fcEventData = fcDay.getAttribute('data-date');  // 클릭한 날짜의 date 속성 가져오기
    			const events = calendar.getEvents().filter(event => event.startStr === fcEventData);  // 모든 event의 startStr과 fcDayData를 비교해서 일치하는 event를 찾는다.
    			
    			if (events.length > 0) {  // events는 항상 배열    				
    				// 첫 번째 이벤트만 처리
    				const event = events[0];
    				
   		        	// 가격을 천 단위 구분자로 포맷
   		        	formattedAdultPrice = Number(event.extendedProps.adultprice);
                    formattedChildPrice = Number(event.extendedProps.childprice);
                       
   		        	// 여행기간과 가격 정보 텍스트 업데이트
   		        	document.querySelector('#startday').value = fcEventData;
   		        	document.querySelector('#fromTo').textContent = event.extendedProps.fromTo;
   		        	document.querySelector('#price').textContent = "성인👩 " +  formattedAdultPrice.toLocaleString() + "원 │ 아동👶 " + formattedChildPrice.toLocaleString() + "원";
   		        	
   		        	priceUpdate();
    		    }
            } else {            	
            	document.querySelector('#startday').value = "";
	        	document.querySelector('#fromTo').textContent = "달력에서 날짜를 선택해 주세요."
	        	document.querySelector('#price').textContent = "성인👩 0원 │ 아동👶 0원";
	        	document.querySelector('#adultnumber').value = "0";
		        document.querySelector('#childnumber').value = "0";
            }
        }
      });
 	

    // 총금액 업데이트    
    function priceUpdate() {    	
    	const totalAdultPrice = document.querySelector('#adultnumber').value * formattedAdultPrice;
		const totalChildPrice = document.querySelector('#childnumber').value * formattedChildPrice;
		
    	const totalprice = document.querySelector('#totalprice')
    	totalprice.textContent = (totalAdultPrice + totalChildPrice).toLocaleString() + "원";
    }
   	
   	adultnumber.addEventListener("change", priceUpdate);
   	childnumber.addEventListener("change", priceUpdate);
   	
});
  
//예약하기
function check() {

  // 유효성 검사하기
  let fm = document.frm;  // 문자객체 안에 form 객체 생성하기
  if (fm.startday.value == "") {
	  alert("여행기간을 선택해주세요");
	  return;
  } else if (fm.adultnumber.value == "") {
	  alert("성인 인원을 입력해주세요");
	  fm.adultnumber.focus();
	  return;
  } else if (fm.childnumber.value == "") {
	  alert("아동 인원을 입력해주세요");
	  fm.childnumber.focus();
	  return;
  } else if (fm.name.value == "") {
	  alert("예약자 이름을 입력해주세요");
	  fm.name.focus();
	  return;
  } else if (fm.phone.value == "") {
	  alert("예약자 연락처를 입력해주세요");
	  fm.phone.focus();
	  return;
  }
  
  let ans = confirm("신청하시겠습니까?");
	  if (ans == true) {
		  fm.action="${pageContext.request.contextPath}/board/${requestScope.bv.bidx}/travelReservationAction.do";
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
    <form class="detail pb-5" name="frm">
      <input type="hidden" class="form-control" id="startday" name="startday">
      <div class="card text-center mb-3">
        <h3 class="card-title fw-bold mb-4">${requestScope.bv.getTitle()}</h3>
        <p class="card-text text-body-secondary pt-4 border-top-dashed">${requestScope.bv.getSummary()}</p>
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
                <p id="fromTo">달력에서 날짜를 선택해 주세요.</p>
              </div>

              <p class="fw-bold mb-1 pt-3 border-top-dashed">2. 상품가격(1인)</p>
              <div class="pl-4">
                <p id="price">성인👩 0원 │ 아동👶 0원</p>
              </div>

              <p class="fw-bold mb-1 pt-3 border-top-dashed">3. 신청인원</p>
              <div class="row mb-3">
                <div class="col-6 pl-4">
                  <label for="adultnumber" class="form-label">성인👩</label>
                  <input type="number" class="form-control" id="adultnumber" name="adultnumber" min="0" value="0">
                </div>
                <div class="col-6 pr-4">
                  <label for="childnumber" class="form-label">아동👶</label>
                  <input type="number" class="form-control" id="childnumber" name="childnumber" min="0" value="0">
                </div>
              </div>
              
              <p class="fw-bold mb-1 pt-3 border-top-dashed">4. 총금액</p>
              <div class="pl-4">
                <p id="totalprice">0원</p>
              </div>
              
              <p class="fw-bold mb-1 pt-3 border-top-dashed">5. 예약자</p>
              <div class="row">
                <div class="col-6 pl-4">
                  <label for="name" class="form-label">이름</label>
                  <input type="text" class="form-control" id="reservationname" required="" value="${requestScope.mv.name}" name="name" placeholder="홍길동">
                  <div class="invalid-feedback">이름을 입력해주세요.</div>
                </div>
                <div class="col-6 pr-4">
                  <label for="phone" class="form-label">연락처</label>
                  <input type="text" class="form-control" id="reservationphone" required="" value="${requestScope.mv.phone}" name="phone" placeholder="01012345678">
                  <div class="invalid-feedback">연락처를 입력해주세요.</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button class="btn btn-primary mb-3" type="button" onClick="check()">예약하기</button>
      </div>
    </form>

    <%@ include file="/WEB-INF/footer.jsp" %>
</body>
</html>