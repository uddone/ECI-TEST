<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <style type="text/css">
      .listbox{
            position : relative; left: 10px; margin: 10px;
            width: 200px; background-color: #EEEEEE; color: #FF00FF;
            border: 2px solid #000;
      }
      .namelist{  margin: 0px; padding: 0px; list-style: none;    }
      .hover{     background-color: cyan; color: blue;}
</style>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
      $(function(){

            $("#id").keyup(function(){
                  var data = "id=" + $("#id").val();
                  $("li").hover(function(){
                                    $(this).addClass("hover");
                              },function(){
                                    $(this).removeClass("hover");
                              })
                              $("li").click(function(){
                                    $("#id").val($(this).text());
                                    $(".listbox").hide();
                              })
                        })
                  })
</script>      
</head>
<body>
      <div class="col-md-12" style="height: 220px; padding-top: 20px" >
            <div class="iq-card" style="width: 100%; height: 100%;" >
                  <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                        	<form class="position-relative">
                              <div class="form-group mb-0">
                                 <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                 <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                              </div>
                              <br>
                           </form>
                              <div class="listbox">
                                    <div class="namelist">
                                          <li>사과</li>
                                          <li>멜론</li>
                                          <li>바나나</li>      
                                    </div>      
                              </div>
                              <div>
                                    <button class="btn btn-primary" style="width: 30%">선택</button>
                                    <button class="btn btn-primary" style="width: 30%; margin-left: 2% ">닫기</button> 
                              </div>
                        </div>
                  </div>
            </div>
      </div>
 
</body>
</html>