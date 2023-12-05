<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function inputcheck(f){
	if(f.currentpass.value == ''){
		alert("비밀번호를 입력하세요");
		f.currentpass.focus();
		return false;
	}
	if(f.newpass.value == ''){
		alert("새 비밀번호를 입력하세요");
		f.newpass.focus();
		return false;
	}
	if(f.checkpass.value ==""){
		alert("확인 비밀번호를 입력하세요");
		f.checkpass.focus();
		return false;
	}
	if(f.newpass.value != f.checkpass.value){
		alert("확인 비밀번호가 일치하지 않습니다.");
		f.checkpass.focus();
		return false;
	}
	return true;
}
</script>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row">
                  <div class="col-lg-12">
                     <div id="chang-pwd">
                     <div>
                     	<button class="btn btn-primary" style="width: 250px; height: 50px;">비밀번호 변경</button>
                     </div>
                               <div class="iq-card">
                                 <div class="iq-card-header d-flex justify-content-between">
                                    <div class="iq-header-title">
                                       <h3 class="card-title">비밀번호 변경</h3>
                                    </div>
                                 </div>
                                 <div class="iq-card-body">
                                    <form action="MAIN_INI_ADM.ai" method="post" name="f" onsubmit="return inputcheck(this)">
                                       <div class="form-group">
                                          <label for="cpass">현재 비밀번호</label>
                                             <input type="Password" class="form-control" id="currentpass" name="currentpass">
                                          </div>
                                       <div class="form-group">
                                          <label for="npass">변경 비밀번호</label>
                                          <input type="Password" class="form-control" id="newpass" name="newpass">
                                       </div>
                                       <div class="form-group">
                                          <label for="vpass">변경 비밀번호 확인</label>
                                             <input type="Password" class="form-control" id="checkpass" name="checkpass">
                                       </div>
                                       <button type="submit" class="btn btn-primary mr-2" style="width: 150px">저장</button>
                                       <button type="reset" class="btn iq-bg-danger" style="width: 150px">취소</button>
                                    </form>
                                 </div>
                              </div>
                           </div>
                  </div>
               </div>
            </div>
         </div>
</body>
</html>