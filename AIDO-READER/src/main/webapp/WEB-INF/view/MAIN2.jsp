<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 화면2</title>
	<script src="js/jquery.min.js"></script>
	<script type="text/javascript">
		console.log(${ID})
	</script>
</head>
<body>
	<div id="content-page" class="content-page">
		<div class="container-fluid" align="center">
		<div align="center" class="iq-card iq-border-radius-20" style="width: 100%">
		<div style="height: 50px; padding-left: 10px;" align="left"  ><h3>가이드 영상</h3></div>
		<div class="iq-card" style="width: 95%; height: 87%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;"> 
							<video width="100%" controls>
  								<source src="aido_guide.mp4" type="video/mp4">
							</video>
						</div>
					</div>
				</div>
			</div>
			<!-- <div class="row">
			
				<div class="col-sm-6 col-md-6 col-lg-3">
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height">
                         <div class="iq-card-body">
                           <div class="d-flex align-items-center justify-content-between">
                              <h6>누적 모의고사 수</h6>
                              <span class="iq-icon"><i class="ri-information-fill"></i></span>
                           </div>
                           <div class="iq-customer-box d-flex align-items-center justify-content-between mt-3">
                              <div class="d-flex align-items-center">
                                 <div class="rounded-circle iq-card-icon iq-bg-primary mr-2"> <i class="ri-inbox-fill"></i></div>
                                 <h2>352</h2>
                              </div>
                              <div class="iq-map text-primary font-size-32"><i class="ri-bar-chart-grouped-line"></i></div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-sm-6 col-md-6 col-lg-3">
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height">
                        <div class="iq-card-body">
                           <div class="d-flex align-items-center justify-content-between">
                              <h6>올해 진행된 모의고사 수</h6>
                              <span class="iq-icon"><i class="ri-information-fill"></i></span>
                           </div>
                           <div class="iq-customer-box d-flex align-items-center justify-content-between mt-3">
                              <div class="d-flex align-items-center">
                                 <div class="rounded-circle iq-card-icon iq-bg-danger mr-2"><i class="ri-radar-line"></i></div>
                                 <h2>37</h2></div>
                               <div class="iq-map text-danger font-size-32"><i class="ri-bar-chart-grouped-line"></i></div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-sm-6 col-md-6 col-lg-3">
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height">
                        <div class="iq-card-body">
                           <div class="d-flex align-items-center justify-content-between">
                              <h6>등록된 OMR 수</h6>
                              <span class="iq-icon"><i class="ri-information-fill"></i></span>
                           </div>
                           <div class="iq-customer-box d-flex align-items-center justify-content-between mt-3">
                              <div class="d-flex align-items-center">
                                 <div class="rounded-circle iq-card-icon iq-bg-warning mr-2"><i class="ri-price-tag-3-line"></i></div>
                                 <h2>32</h2></div>
                               <div class="iq-map text-warning font-size-32"><i class="ri-bar-chart-grouped-line"></i></div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="col-sm-6 col-md-6 col-lg-3">
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height">
                        <div class="iq-card-body">
                           <div class="d-flex align-items-center justify-content-between">
                              <h6>학인 필요 OMR 수</h6>
                              <span class="iq-icon"><i class="ri-information-fill"></i></span>
                           </div>
                           <div class="iq-customer-box d-flex align-items-center justify-content-between mt-3">
                              <div class="d-flex align-items-center">
                                 <div class="rounded-circle iq-card-icon iq-bg-info mr-2"><i class="ri-refund-line"></i></div>
                                 <h2>27</h2></div>
                               <div class="iq-map text-info font-size-32"><i class="ri-bar-chart-grouped-line"></i></div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div> -->
               <!-- 
               <div class="row"> 
               	<div style="width: 49%; ">
                  
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height overflow-hidden">
                        <div class="iq-card-header d-flex justify-content-between">
                           <div class="iq-header-title">
                              <h4 class="card-title">전월 대비 평균 점수</h4>
                           </div>
                           <div class="iq-card-header-toolbar d-flex align-items-center">
                              <div class="dropdown">
                                 <span class="dropdown-toggle" id="dropdownMenuButton" data-toggle="dropdown">
                                 <i class="ri-more-fill"></i>
                                 </span>
                                 <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton" style="">
                                    <a class="dropdown-item" href="#"><i class="ri-eye-fill mr-2"></i>View</a>
                                    <a class="dropdown-item" href="#"><i class="ri-delete-bin-6-fill mr-2"></i>Delete</a>
                                    <a class="dropdown-item" href="#"><i class="ri-pencil-fill mr-2"></i>Edit</a>
                                    <a class="dropdown-item" href="#"><i class="ri-printer-fill mr-2"></i>Print</a>
                                    <a class="dropdown-item" href="#"><i class="ri-file-download-fill mr-2"></i>Download</a>
                                 </div>
                              </div>
                           </div>
                        </div>
                        <div id="home-chart-02"></div>
                     </div>
                  
                  </div>
                  <div style="width: 49%; margin-left: 1%">
                  
                     <div class="iq-card iq-card-block iq-card-stretch iq-card-height overflow-hidden">
                        <div class="iq-card-header d-flex justify-content-between">
                           <div class="iq-header-title">
                              <h4 class="card-title">최근 진행한 모의고사</h4>
                           </div>
                           <div class="iq-card-header-toolbar d-flex align-items-center">
                              <div class="dropdown">
                                 <span class="dropdown-toggle" id="dropdownMenuButton1" data-toggle="dropdown" >
                                 <i class="ri-more-fill"></i>
                                 </span>
                                 <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton1" style="">
                                    <a class="dropdown-item" href="#"><i class="ri-eye-fill mr-2"></i>View</a>
                                    <a class="dropdown-item" href="#"><i class="ri-delete-bin-6-fill mr-2"></i>Delete</a>
                                    <a class="dropdown-item" href="#"><i class="ri-pencil-fill mr-2"></i>Edit</a>
                                    <a class="dropdown-item" href="#"><i class="ri-printer-fill mr-2"></i>Print</a>
                                    <a class="dropdown-item" href="#"><i class="ri-file-download-fill mr-2"></i>Download</a>
                                 </div>
                              </div>
                           </div>
                        </div>
                        <div class="iq-card-body">
                          <ul class="suggestions-lists m-0 p-0">
                           <li class="d-flex mb-4 align-items-center">
                              <div class="profile-icon iq-bg-success"><span><i class="ri-check-fill"></i></span></div>
                              <div class="media-support-info ml-3">
                                 <a href="list.ai"><h6>2020년 3월 모의 고사</h6></a>
                              </div>
                           </li>
                           <li class="d-flex mb-4 align-items-center">
                              <div class="profile-icon iq-bg-warning"><span><i class="ri-check-fill"></i></span></div>
                              <div class="media-support-info ml-3">
                                 <h6>2020년 4월 모의 고사</h6>
                              </div>
                           </li>
                           <li class="d-flex mb-4 align-items-center">
                              <div class="profile-icon iq-bg-success"><span><i class="ri-check-fill"></i></span></div>
                              <div class="media-support-info ml-3">
                                 <h6>2020년 6월 모의 평가</h6>
                              </div>
                           </li>
                           <li class="d-flex mb-4 align-items-center">
                              <div class="profile-icon iq-bg-danger"><span><i class="ri-check-fill"></i></span></div>
                              <div class="media-support-info ml-3">
                                 <h6>2020년 9월 모의 평가</h6>
                              </div>
                           </li>
                        </ul>
                       </div>
                     </div>
                  
                  </div>
               </div>-->
            </div>
         </div>
</body>
</html>