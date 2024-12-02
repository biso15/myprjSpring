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
	 		 events : [
		 		<c:if test="${!empty requestScope.clist}">
		    	<c:forEach items="${requestScope.clist}" var="cv" varStatus="status">
	            {
	            	start : "${cv.startday}",
	    	   	 	display: 'list-item',
	    	   	 	backgroundColor: '#0d6efd;',
	    	   	 	extendedProps: {
	    	   	 		period : "${cv.startday} ~ ${cv.endday}",
	    	   	 		adultprice: "${cv.adultprice}",
	    	        	childprice: "${cv.childprice}"
	    	        }
	             },
		        </c:forEach>
		    	</c:if>
			],
	    	
			dateClick: function(info) {
			    document.querySelector('#startday').value = info.dateStr;
			    const period = ${bv.period};
			    
			    // startday에 period를 더해서 endday를 계산 후 입력	
			    const date = new Date(info.dateStr);
			    date.setDate(date.getDate() + period - 1);  // 1 빼야됨 주의
			    const endday = date.toISOString().split('T')[0];  // 날짜만 추출
			    document.querySelector('#endday').value = endday;
			    
			    document.querySelector('#fromTo').textContent = info.dateStr + " ~ " + endday;
		        
			 }
		  });
		
	    calendar.render();
	  
	  
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
				const adultprice = $("#adultprice").val();
				const childprice = $("#childprice").val();
				
				$.ajax({
					type: "post",  // 전송방식
					url: "${pageContext.request.contextPath}/calendar/${bidx}/travelReservationWriteAction.do",
					dataType: "json",
					data: {"startday": startday, "endday": endday, "adultprice": adultprice, "childprice": childprice},
					success: function(result) {
						alert("전송성공");
						// calendar.getEvents();
						calendar.refetchEvents();
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
	
	<%-- // 캘린더 
	$.ajax({
		type: "get",  // 전송방식
		url: "${pageContext.request.contextPath}/comment/commentList.aws?bidx=${requestScope.bv.getBidx()%>",
		url: "${pageContext.request.contextPath}/comment/${requestScope.bv.bidx}/" +block+ "/commentList.aws",  /* RestFul 방식(Rest api 사용) */
		dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
		success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
			// alert("전송성공 테스트");			
			
			if(result.moreView == "N") {
				$("#moreBtn").css("display", "none");
				$("#blockPre").val($("#block").val());
			} else {
				$("#moreBtn").css("display", "block");
				$("#blockPre").val($("#block").val() - 1);
			}
			
			let nextBlock = result.nextBlock;	
			$("#block").val(nextBlock);
			
			var str = "<table class='replyTable'><tr><th>번호</th><th>작성자</th><th>내용</th><th>날짜</th><th>DEL</th></tr>";

			var strTr = "";
			var index = result.length;
			
			$(result.clist).each(function(index) {
				var btnn = "";
				if (this.midx == "${sessionScope.midx}") {
					if(this.delyn == "N") {
						btnn = "<button type='button' class='btn' onclick='commentDel(" + this.cidx + ")'>삭제</button>";
					}
				}
				
				var index = index + 1;
				strTr += "<tr><td class='cidx'>" + index-- + "</td>" + 
						"<td class='cwriter'>" + this.cwriter + "</td>" + 
						"<td class='ccontents'>" + this.ccontents + "</td>" + 
						"<td class='writeday'>" + this.writeday + "</td>" + 
						"<td class='delyn'>" + btnn + "</td></tr>";
			});
			
			str = str + strTr + "</table>";
			
			$("#commentListView").html(str);

		},
	    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
			alert("전송실패 테스트");
		    /* console.log("Error Status: " + status);
		    console.log("Error Detail: " + error);
		    console.log("Response: " + xhr.responseText); */
		}
	}); --%>
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