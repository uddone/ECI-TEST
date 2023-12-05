package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.ALRAM;
import logic.BSTOR;
import logic.FILELOG;
import logic.MBR;
import logic.OCR_EXAM;
import logic.OCR_EXAM_RC;
import logic.OCR_EXAM_REC;
import logic.OMR_EXAM;
import logic.OMR_EXAM_LSN;
import logic.OMR_EXAM_LSN_QUEI;
import logic.OMR_GRDG;
import logic.OMR_MST;
import logic.OMR_RECOG;

public interface AIMapper {
	
	@Select({"<script>",
		"	SELECT 	MEM.BSTOR_CD ,"
		+ " 		MEM.CMPN_CD ,MEM.DEL_YN,"
		+ " 		MEM.EMAIL_ADR ,"
		+ " 		MEM.JOIN_DTM ,"
		+ " 		MEM.LOAD_DTM ,"
		+ " 		MEM.MBR_MST_YN ,"
		+ " 		MEM.MBR_NO ,"
		+ " 		MEM.PWD ,"
		+ " 		MEM.TEL_NO ,"
		+ " 		MEM.UPD_DTM ,"
		+ " 		BSTOR.BSTOR_NM ,"
		+ " 		BSTOR.CMPN_NM ,"
		+ " 		PWDCOMPARE(#{PWD},PWD) as 'PWDCHK' "
		+ "    FROM	TB_CMPN_BSTOR_MST BSTOR "
		+ " 		INNER JOIN TB_MBR_MST MEM 	ON 		BSTOR.BSTOR_CD =  MEM.BSTOR_CD "
		+ "	   WHERE 1=1 ",
		"		 <if test='ID != null'> AND SUBSTRING(EMAIL_ADR,0, CHARINDEX('@', EMAIL_ADR)) = '${ID}' </if>",
		"</script>"})	
	List<MBR> select(Map<String, Object> param);

	@Select({
		"<script>",
		"	SELECT "
		+ "  		MST.USE_YN "
		+ " 		, MST.EXAM_CD "
		+ " 		, MST.EXAM_KIND "
		+ " 		, MST.EXAM_NM "
		+ " 		, MST.LOAD_DTM "
		+ " 		, MST.OCR_MST_CD "
		+ " 		, MST.REG_MBR_NO "
		+ " 		, MST.SCHYR "
		+ " 		, MST.UPD_DTM "
		+ " 		, MST.UPD_MBR_NO "
		+ "   FROM TB_OCR_EXAM_MST MST "
		+ "		   INNER JOIN TB_OCR_EXAM_BSTOR_MAPPG MAP 	ON	 MST.EXAM_CD = MAP.EXAM_CD "
		+ "  WHERE 1=1 "
		+ "    AND MST.USE_YN='Y' ",
		" 	   <if test='BSTOR_CD != null'> AND BSTOR_CD = #{BSTOR_CD} </if> ",
		" 	   <if test='searchcontent != null'> "
		+ "	   AND ("
		+ "		   MST.EXAM_KIND LIKE '%${searchcontent}%' "
		+ "		OR MST.SCHYR LIKE '%${searchcontent}%' "
		+ "		OR MST.EXAM_NM LIKE '%${searchcontent}%' "
		+ "		   ) </if>",
		" 	   <if test='OCR_MST_CD != null'> AND MAP.OCR_MST_CD = #{OCR_MST_CD} </if>",
		" 	 ORDER BY MAP.EXAM_CD DESC OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY ",
		/*"<if test='searchcontent != null'> AND EXAM_KIND like '%${searchcontent}%' OR SCHYR like '%${searchcontent}%' OR EXAM_NM like '%${searchcontent}%' </if>",
		"<if test='pageNum != null and limit != null'> AND EXAM_CD > #{pageNum}  </if>",
		"<if test='OCR_MST_CD != null'> AND OCR_MST_CD = #{OCR_MST_CD} </if>",*/
		"</script>"
	})
	List<OCR_EXAM> ocrExamlist(Map<String, Object> param);

	@Select({
			"<script>",
			"	SELECT "
					+ "  		MST.USE_YN "
					+ " 		, MST.EXAM_CD "
					+ " 		, MST.EXAM_KIND "
					+ " 		, MST.EXAM_NM "
					+ " 		, MST.LOAD_DTM "
					+ " 		, MST.OCR_MST_CD "
					+ " 		, MST.REG_MBR_NO "
					+ " 		, MST.SCHYR "
					+ " 		, MST.UPD_DTM "
					+ " 		, MST.UPD_MBR_NO "
					+ "   FROM TB_OCR_EXAM_MST MST "
					+ "		   INNER JOIN TB_OCR_EXAM_BSTOR_MAPPG MAP 	ON	 MST.EXAM_CD = MAP.EXAM_CD "
					+ "  WHERE 1=1 "
					+ "    AND MST.USE_YN='Y' ",
			" 	   <if test='BSTOR_CD != null'> AND BSTOR_CD = #{BSTOR_CD} </if> ",
			" 	   <if test='searchcontent != null'> "
					+ "	   AND ("
					+ "		   MST.EXAM_KIND LIKE '%${searchcontent}%' "
					+ "		OR MST.SCHYR LIKE '%${searchcontent}%' "
					+ "		OR MST.EXAM_NM LIKE '%${searchcontent}%' "
					+ "		   ) </if>",
			" 	   <if test='OCR_MST_CD != null'> AND MAP.OCR_MST_CD = #{OCR_MST_CD} </if>",
			" 	 ORDER BY MAP.EXAM_CD DESC OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY ",
		/*"<if test='searchcontent != null'> AND EXAM_KIND like '%${searchcontent}%' OR SCHYR like '%${searchcontent}%' OR EXAM_NM like '%${searchcontent}%' </if>",
		"<if test='pageNum != null and limit != null'> AND EXAM_CD > #{pageNum}  </if>",
		"<if test='OCR_MST_CD != null'> AND OCR_MST_CD = #{OCR_MST_CD} </if>",*/
			"</script>"
	})
	List<OCR_EXAM> ocrExamlist2(Map<String, Object> param);
	@Select({ "<script>",
		"	SELECT * "
		+ "   FROM TB_OCR_EXAM_MST "
		+ "  WHERE 1=1 "
		+ "    AND USE_YN='Y' ",
		" 	   <if test='searchcontent != null'> "
		+ "	   AND ("
		+ "		   EXAM_KIND LIKE '%${searchcontent}%' "
		+ "		OR SCHYR LIKE '%${searchcontent}%' "
		+ "		OR EXAM_NM LIKE '%${searchcontent}%' "
		+ "		   ) </if>",
		"    ORDER BY EXAM_CD DESC "
		+ " OFFSET #{pageNum} ROWS "
		+ "  FETCH NEXT #{limit} ROWS ONLY ",
		"</script>"
	})
	List<OCR_EXAM> ocrExamlistonly(Map<String, Object> param);
	
	@Select({
		"<script>",
		"	 	SELECT	CMPN.CMPN_CD "
		+ "			 	,CMPN.BSTOR_CD "
		+ "			 	,ISNULL(OCR.STTUS_CD,'5') STTUS_CD"
		+ "			 	,CMPN.CMPN_NM,CMPN.BSTOR_NM"
		+ "			 	,ISNULL(CD.CD_NM,'미실시') CD_NM "
		+ "			 	,OCR.USE_YN "
		+ "		 FROM 	TB_CMPN_BSTOR_MST CMPN "
		+ "	  		 	LEFT OUTER JOIN TB_OCR_EXAM_BSTOR_MAPPG OCR		ON		CMPN.BSTOR_CD = OCR.BSTOR_CD "
		+ "			 		 AND OCR.EXAM_CD = #{EXAM_CD} "
		+ "			 	LEFT OUTER JOIN TB_INTEGRATION_CD CD 			ON 		OCR.STTUS_CD = CD.CD "
		+ "			 		 AND CD.CD_GROUP_ID = 2  "
		+ " 	WHERE 	1=1 ",
		"		  <if test='rsearchcontent != null'> "
		+ "		  AND ( "
		+ "			 	CMPN.BSTOR_NM LIKE '%${rsearchcontent}%' "
		+ "		   OR 	ISNULL(CD.CD_NM,'미실시') LIKE '%${rsearchcontent}%' "
		+ "			  ) </if>",
		" 		ORDER BY STTUS_CD "
		+ "	   OFFSET #{rpageNum} ROWS "
		+ "     FETCH NEXT #{rlimit} ROWS ONLY ",
		"</script>"}) 
	List<OCR_EXAM_REC> ocrExamrec(Map<String, Object> param);

	@Select({
		"<script>",
		"		SELECT 	COUNT(*) "
		+ "		  FROM 	TB_OCR_EXAM_MST "
		+ "		 WHERE 	1=1 "
		+ "		   AND 	USE_YN = 'Y' ",
		"		   <if 	test='OCR_MST_CD != null'> "
		+ "		   AND 	OCR_MST_CD = #{OCR_MST_CD} </if>",		
		"		   <if	test='searchcontent != null'> "
		+ "		   AND  (	"
		+ "				EXAM_KIND LIKE '%${searchcontent}%' "
		+ "		    OR 	SCHYR LIKE '%${searchcontent}%' "
		+ "		    OR 	EXAM_NM LIKE '%${searchcontent}%' "
		+ "				) </if>",
		"</script>"
	})
	int getEXAMCount(Map<String, Object> param);

	@Insert({"<script>",
		"		INSERT INTO "
		+ "			   LOG_SYS_TXN(	LOG_DTM "
		+ "							,STEP_NM "
		+ "							,PID "
		+ "							,JOB_NM "
		+ "							,ARG_VAL "
		+ "							,LOG_MSG ",
		"						<if test='ST_END_DIV != null'> ,ST_END_DIV </if>",
		"						<if test='CMPLT_MSG != null'> ,CMPLT_MSG </if>",
		" 							) ",
		"			  VALUES 	( GETDATE() "
		+ "						  ,#{STEP_NM} "
		+ " 					  ,#{PID} "
		+ "						  ,#{JOB_NM}"
		+ "						  ,#{ARG_VAL}"
		+ "						  ,#{LOG_MSG}",
		"					  <if test='ST_END_DIV != null'> ,#{ST_END_DIV} </if>",
		"				  	  <if test='CMPLT_MSG != null'> ,#{CMPLT_MSG} </if>",
		"						) ",
		"</script>"})
	void javalog(Map<String, Object> param);

	@Select("	SELECT 	OCR_MST_CD "
			+ "   FROM 	TB_OCR_EXAM_MST "
			+ "  WHERE 	1=1 "
			+ "	   AND EXAM_CD = #{exam_cd}")
	int ocrcd(String exam_cd);

	@Select({"<script>",
		"	SELECT 	OCR.CMPN_CD,"
		+ "  		OCR.BSTOR_CD,"
		+ "  		OCR.EXAM_CD,"
		+ "  		OCR.STTUS_CD,"
		+ "  		CMPN.CMPN_NM,"
		+ "  		CMPN.BSTOR_NM,"
		+ "  		CD.CD_NM,"
		+ "  		CD.CD_GROUP_ID,"
		+ "  		OCR.REG_DTM,"
		+ "  		OCR.UPD_DTM,"
		+ "  		OCR.LOAD_DTM "
		+ "	   FROM TB_CMPN_BSTOR_MST CMPN "
		+ "			INNER JOIN TB_OCR_EXAM_BSTOR_MAPPG OCR 		ON 		CMPN.BSTOR_CD = OCR.BSTOR_CD "
		+ "			INNER JOIN TB_INTEGRATION_CD CD 			ON 		OCR.STTUS_CD = CD.CD "
		+ "	  WHERE CD.CD_GROUP_ID = 2 AND OCR.EXAM_CD = #{exam_cd} "
		+ "	  ORDER BY OCR.LOAD_DTM "
		+ "	 OFFSET #{pageNum} ROWS "
		+ "	  FETCH NEXT #{limit} ROWS ONLY",
		" </script>"})
	List<BSTOR> BSTORSN(Map<String, Object> param);

	@Select({"<script>",
		"		SELECT	COUNT(*) "
		+ "		  FROM 	("
		+ "		  		SELECT 	BST.BSTOR_NM "
		+ "			  	  FROM 	TB_OCR_EXAM_RC RC "
		+ "						INNER JOIN TB_CMPN_BSTOR_MST BST 	ON 		RC.CMPN_CD  = BST.CMPN_CD "
		+ "		   			 	  	  AND RC.BSTOR_CD = BST.BSTOR_CD "
		+ "			 	 WHERE 	1=1  "
		+ "			   	   AND 	RC.EXAM_CD = #{EXAM_CD}",
		"		 	   	   <if 	test='searchcontent != null'> AND BST.BSTOR_NM like '%${searchcontent}%' </if>",
		"			 	 GROUP 	BY BST.BSTOR_NM "
		+ "				) AS AD ",
		"</script>"})
			
	int sncount(Map<String, Object> param);

	@Select("		SELECT 	RC.*	 "
			+ "				,ISNULL(LSN.LSN_NM,'미응시') LSN_NM "
			+ "		  FROM	TB_OCR_EXAM_RC RC "
			+ "				LEFT OUTER JOIN TB_LSN_MST LSN		ON		 RC.LSN_CD = LSN.LSN_CD "
			+ "		 WHERE 	1=1 "
			+ "		   AND 	RC.EXMN_NO = #{EXAMNO} "
			+ "		   AND 	RC.OCR_IMG = #{OCR_IMG} "
			+ "		   AND 	RC.EXAM_CD=#{examCd} "
			+ "		   AND 	BSTOR_CD = #{BSTOR_CD} "
			+ "		   AND 	RC.LSN_CD > -1 "
			+ "		 ORDER 	BY RC.LSN_CD ")
	List<OCR_EXAM_RC> ocr_rc_sn(Map<String, Object> param);

	@Select("	SELECT	DISTINCT(EXMN_NO) STD_NO "
			+ "   FROM	TB_OCR_EXAM_RC "
			+ "  WHERE 	1=1 "
			+ "	   AND 	EXMN_NO != '-1' "
			+ "    AND 	BSTOR_CD = #{BSTOR_NO} "
			+ "    AND 	EXAM_CD =#{EXAM_CD} "
			+ "    AND	( "
			+ "			STDN_NM LIKE '%${rsearchcontent}%' "
			+ "		OR 	EXMN_NO LIKE '%${rsearchcontent}%' "
			+ "		OR 	STTUS like '%${rsearchcontent}%') "
			+ "  ORDER  BY STD_NO "
			+ " OFFSET 	#{rpageNum} ROWS "
			+ "  FETCH 	NEXT #{rlimit} ROWS ONLY ")
	List<String> ocr_rc_exam_no(Map<String, Object> param);

	//sttus
	@Select("	SELECT STTUS "
			+ "	  FROM TB_OCR_EXAM_RC "
			+ "	 WHERE EXMN_NO = #{stdn_no} "
			+ "	   AND EXAM_CD = #{EXAM_CD} "
			+ "	   AND BSTOR_CD = #{BSTOR_CD} "
			+ "	   AND LSN_CD >-1 ")
	List<String> ocr_rc_sn_sttus(Map<String, Object> param);

	@Select("	SELECT STDN_NM "
			+ "   FROM TB_OCR_EXAM_RC "
			+ "  WHERE EXMN_NO = #{a}")	
	List<String> stdn_nm(String string);

	@Select("	SELECT 	* "
			+ "	  FROM 	TB_OCR_EXAM_RC "
			+ "  WHERE 	EXMN_NO = #{exam_no} "
			+ "  ORDER 	BY LSN_CD")
	List<OCR_EXAM_RC> ocr_rc_sn_one(String exam_no);

	//ocr시험별 지점 갯수
	@Select({"<script>",
		"		SELECT 	COUNT(*) "
		+ "		  FROM 	TB_CMPN_BSTOR_MST CMPN "
		+ "				LEFT OUTER JOIN TB_OCR_EXAM_BSTOR_MAPPG OCR 	ON 		CMPN.BSTOR_CD = OCR.BSTOR_CD "
		+ "					 AND OCR.EXAM_CD = #{EXAM_CD} "
		+ "				LEFT OUTER JOIN TB_INTEGRATION_CD CD 			ON 		OCR.STTUS_CD = CD.CD "
		+ "					 AND CD.CD_GROUP_ID = 2  "
		+ "		 WHERE 	1 =1 ",
		"		   <if test='rsearchcontent != null'> "
		+ "		   AND BSTOR_NM LIKE '%${rsearchcontent}%' "
		+ "			OR CD.CD_NM LIKE '%${rsearchcontent}%' </if>",
		"</script>"})
	int ocr_exam_count(Map<String, Object> param);

	@Select("	SELECT 	OCR_MST_CD "
			+ "	  FROM 	TB_OCR_EXAM_MST "
			+ "	 WHERE 	EXAM_CD = #{examCd}")
	int ocr_bstor_ocrcd(String examCd);

	//체크시 지점 추가
	@Insert("insert into TB_OCR_EXAM_BSTOR_MAPPG values(#{exam_Cd},#{cmpn_Cd},#{bstor_Cd},#{ocr_Cd},'1','N',GETDATE(),GETDATE(),GETDATE())")
	void insert_bstor_exam(Map<String, Object> param);

	//시험정보 수정
	@Update("update TB_OCR_EXAM_MST set EXAM_NM = #{EXAM_NM},SCHYR=#{SCHYR},EXAM_KIND=#{EXAM_KIND},UPD_DTM=GETDATE() where EXAM_CD=#{EXAM_CD}")
	void update_ocr_exam(Map<String, Object> param);

	//지점 리스트
	@Select("select BSTOR_NM from TB_CMPN_BSTOR_MST where BSTOR_CD = #{bstorCd}")
	String getbstor(String bstorCd);

	//비밀번호 변경
	@Update("update TB_MBR_MST set PWD = PWDENCRYPT(#{PWD}),UPD_DTM=GETDATE() where MBR_NO = #{MBR_NO}")
	void chgpass(Map<String, Object> param);

	//수능 학생수
	@Select({"<script>",
		"SELECT COUNT(*) FROM "
		+ " ("
		+ "SELECT EXMN_NO "
		+ "      ,STDN_NM"
		+ "	  ,MAX(STTUS) AS STTUS"
		+ "  FROM TB_OCR_EXAM_RC RC"
		+ " WHERE 1=1 "
		+ "	AND BSTOR_CD = #{BSTOR_CD}  "
		+ "	AND EXAM_CD = #{EXAM_CD} ",
		"<if test='rsearchcontent != null'> and (EXMN_NO like '%${rsearchcontent}%' or STDN_NM like '%${rsearchcontent}%' or STTUS like '%${rsearchcontent}%') </if>"
		+ " "
		+ " GROUP BY EXMN_NO"
		+ "         ,STDN_NM"
		+ " "
		+ "  ) AS STUCNT",
		"</script>"})
	int OCR_SN_STU_Count(Map<String, Object> param);

	//메일
	@Select("select * from TB_MBR_MST where EMAIL_ADR = '${EMAIL_ADR}'")
	List<MBR> selectemail(Map<String, Object> param);

	//수능 업데이트 ,STTUS=#{STTUS}
	@Update("UPDATE TB_OCR_EXAM_RC SET STD_SC = #{STD_SC}, PRCN_RANK=#{PRCN_RANK}, GRAD=#{GRAD}, STTUS=#{STTUS}, UPD_DTM=GETDATE(), UPD_MBR_NO=#{MBR_NO} WHERE 1=1 and EXMN_NO > -1 AND EXMN_NO = #{EXMN_NO} AND EXAM_CD=#{EXAM_CD} and BSTOR_CD = #{BSTOR_CD} AND LSN_CD = #{LSN_CD}")
	void updateocr(Map<String, Object> param);

	//수능 상태값 업데이트
	@Select("SELECT CASE WHEN #{prcn} BETWEEN 100-COND_MAX AND 100-COND_MIN THEN 'O' ELSE 'X' END OX "
			+ "FROM TB_OCR_EXAM_WARN_MST WHERE 1=1 AND TARGET_COL_NM = 'GRAD' AND TARGET_DATA = #{TARGET_DATA}")
	String sttusupdate(Map<String, Object> param);

	@Select("SELECT COUNT(*) FROM LOG_FILE_CHG_TXN WHERE EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND OMR_OCR_DIV = #{SEP}")
	int ivalue(Map<String, Object> param);

	@Insert("INSERT INTO LOG_FILE_CHG_TXN VALUES(GETDATE(),#{OMR_OCR_DIV},#{MST_CD},#{PID},#{EXAM_CD},#{CMPN_CD},#{BSTOR_CD},#{ORGIN_FILE_NM},#{REUPLD_YN})")
	void insert_filename(Map<String, Object> param);

	@Select("select PID from LOG_FILE_CHG_TXN WHERE EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND OMR_OCR_DIV = #{OMR_OCR_DIV} AND MST_CD =#{MST_CD}  order by LOG_DTM desc")
	List<String> pid(Map<String, Object> param);

	@Select("select COUNT(*) from LOG_FILE_CHG_TXN WHERE EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND PID = #{PID} AND OMR_OCR_DIV =#{OMR_OCR_DIV} and MST_CD=#{MST_CD} ")
	int filecnt(Map<String, Object> param);

	@Select({"<script>",
			"SELECT * " +
			"  FROM LOG_FILE_CHG_TXN "+
			" WHERE OMR_OCR_DIV=#{OMR_OCR_DIV} "+
			"   AND EXAM_CD = #{EXAM_CD} "+
			" 	AND CMPN_CD = #{CMPN_CD} " +
			"	AND BSTOR_CD = #{BSTOR_CD} " +
			"	AND MST_CD = #{MST_CD} ",
			"	<if test='REUPLD_YN != null'> AND REUPLD_YN = #{REUPLD_YN} </if>",
			" 	<if test='searchcontent != null'> AND ORIGIN_FILE_NM LIKE '%${searchcontent}%'</if>",
			" 	<if test='PID != null'> AND PID ='${PID}'</if>",
			"</script>"})
	List<FILELOG> floglist(Map<String, Object> param);

	@Select("select DATEDIFF(n,LOG_DTM,GETDATE()) "
			+ " from LOG_FILE_CHG_TXN "
			+ " where 1=1	"
			+ " and OMR_OCR_DIV=#{OMR_OCR_DIV}	"
			+ " and EXAM_CD = #{EXAM_CD}	"
			+ " and CMPN_CD = #{CMPN_CD}	"
			+ " and BSTOR_CD = #{BSTOR_CD}	"
			+ " and PID = #{PID} AND MST_CD=#{MST_CD}")
	List<Integer> timediff(Map<String, Object> param);

	@Select("select count(*) from TB_OCR_EXAM_RC where 1=1 and OCR_IMG = #{OCR_IMG} AND EXMN_NO <> -1 ")
	int reupload(Map<String, Object> param);

	@Select({
		"<script>",
		"SELECT "
		+ "       BST.BSTOR_NM"
		+ "	  ,COUNT(DISTINCT CASE WHEN RC.STTUS = 'WARNING' THEN RC.EXMN_NO ELSE NULL END) AS WARN_CNT"
		+ "  FROM TB_OCR_EXAM_RC RC"
		+ "  INNER JOIN TB_CMPN_BSTOR_MST BST ON RC.CMPN_CD  = BST.CMPN_CD"
		+ "								  AND RC.BSTOR_CD = BST.BSTOR_CD"
		+ " WHERE 1=1 ",
		"<if test='EXAM_CD != null'> AND RC.EXAM_CD = #{EXAM_CD} </if>",
		"<if test='searchcontent != null'> AND BST.BSTOR_NM like '%${searchcontent}%' </if>",
		 " GROUP BY BST.BSTOR_NM"
		+ " ORDER BY 2 DESC, BST.BSTOR_NM OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY",
		"</script>"	})
	List<BSTOR> bstorwarning(Map<String, Object> param);

	@Select("SELECT BSTOR_CD FROM TB_CMPN_BSTOR_MST WHERE BSTOR_NM ='${BSTOR_NM}'")
	String getbstorcd(Map<String, Object> param);

	@Update("UPDATE TB_OCR_EXAM_RC SET STD_SC = STD_SC, PRCN_RANK=PRCN_RANK, GRAD=GRAD, STTUS=#{STTUS}, UPD_DTM=GETDATE(), UPD_MBR_NO=#{MBR_NO} WHERE 1=1 and EXMN_NO > -1 AND EXMN_NO = #{EXMN_NO} AND EXAM_CD=#{EXAM_CD} and BSTOR_CD = #{BSTOR_CD} AND LSN_CD = #{LSN_CD}")
	void updatesuccess(Map<String, Object> param);

	@Insert("insert into TB_CMPN_BSTOR_MST values(1,#{BSTOR_CD},'이투스',#{BSTOR_NM},GETDATE())")
	void regcmpnbstor(Map<String, Object> param);

	@Insert("insert into TB_MBR_MST values(1,#{BSTOR_CD},#{EMAIL_ADR},PWDENCRYPT(#{PWD}),NULL,GETDATE(),'N','N',GETDATE(),GETDATE())")
	void regmbrbstor(Map<String, Object> param);

	@Select("select "
			+ "		OCR_IMG "
			+ "	from TB_OCR_EXAM_RC "
			+ "	where BSTOR_CD = #{BSTOR_CD} "
			+ "		and EXAM_CD = #{EXAM_CD} "
			+ "		and EXMN_NO = #{EXMN_NO} "
			+ "	order by REG_DTM desc OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY ")
	String ocr_rc_snimg(Map<String, Object> param);

	@Update("update TB_OCR_EXAM_RC set SCHOL = #{SCHOL}, STDN_NM = #{STDN_NM},UPD_DTM=GETDATE() where EXAM_CD = #{EXAM_CD} and EXMN_NO = #{EXMN_NO} and  BSTOR_CD = #{BSTOR_CD}")
	void updateocrinfo(Map<String, Object> param);

	@Select({"<script>",
		"select count(*) from ( "
		+ " select mst.OMR_MST_CD,MAX(mst.STTUS_CD) as STTUS_CD, mst.CMPN_CD,mst.EXAM_CD,exam.EXAM_NM,exam.EXAM_DT,exam.SCHYR,exam.EXAM_KIND, cards.OMR_NM  "
		+ " from TB_OMR_EXAM_MST exam  "
		+ " 	join TB_OMR_EXAM_LSN_MST mst on exam.EXAM_CD = mst.EXAM_CD  "
		+ " 	join TB_INTEGRATION_CD cd on cd.CD = mst.STTUS_CD  and cd.CD_GROUP_ID = 1 "
		+ " 	join TB_OMR_MST cards on cards.OMR_MST_CD = mst.OMR_MST_CD "
		+ " 	join TB_LSN_MST lsn on lsn.LSN_CD=mst.LSN_CD      "
		+ " where 1=1"
		+ "             and  mst.CMPN_CD = #{CMPN_CD} "
		+ "             and  exam.USE_YN = 'Y' "
		+ "         	and  mst.BSTOR_CD = #{BSTOR_CD} ",
		" <if test='searchcontent != null'> AND (exam.EXAM_KIND like '%${searchcontent}%' OR exam.SCHYR like '%${searchcontent}%' OR exam.EXAM_NM like '%${searchcontent}%' OR lsn.LSN_NM like '%${searchcontent}%' OR cd.CD_NM like '%${searchcontent}%' ) </if>",
		" GROUP BY mst.OMR_MST_CD,mst.CMPN_CD,mst.EXAM_CD,exam.EXAM_NM,exam.EXAM_DT,exam.SCHYR,exam.EXAM_KIND,cards.OMR_NM ) as DsntOmrCnt ",
		"</script>"})
	int getOMREXAMlsnCount(Map<String, Object> param);

	@Select({"<script>",
		"SELECT "
		+ "				AD.OMR_MST_CD"
		+ "			  , AD.STTUS_CD "
		+ "			  , AD.CMPN_CD "
		+ "			  , AD.EXAM_CD "
		+ "			  , AD.EXAM_NM "
		+ "			  , AD.EXAM_DT"
		+ "			  , AD.SCHYR"
		+ "			  , AD.EXAM_KIND"
		+ "			  , CD.CD_NM "
		+ "			  , AD.OMR_NM "
		+ "		   FROM ( "
		+ "				SELECT "
		+ "						MST.OMR_MST_CD"
		+ "						, MAX(mst.STTUS_CD) as STTUS_CD"
		+ "						, MST.CMPN_CD"
		+ "						, MST.EXAM_CD"
		+ "						, EXAM.EXAM_NM"
		+ "						, EXAM.EXAM_DT"
		+ "						, EXAM.SCHYR"
		+ "						, EXAM.EXAM_KIND"
		+ "						, CARDS.OMR_NM "
		+ "				   FROM TB_OMR_EXAM_MST EXAM "
		+ "						INNER JOIN TB_OMR_EXAM_LSN_MST MST	ON	EXAM.EXAM_CD = MST.EXAM_CD "
		+ "						INNER JOIN TB_INTEGRATION_CD CD		ON	CD.CD = MST.STTUS_CD  "
		+ "														   AND	CD.CD_GROUP_ID = 1 "
		+ "						INNER JOIN TB_OMR_MST CARDS			ON	CARDS.OMR_MST_CD = MST.OMR_MST_CD "
		+ "						INNER JOIN TB_LSN_MST LSN			ON	LSN.LSN_CD=MST.LSN_CD "
		+ "				  WHERE 1=1 "
		+ "					AND  MST.CMPN_CD = #{CMPN_CD} "
		+ "					AND  EXAM.USE_YN = 'Y' "
		+ "		            AND  MST.BSTOR_CD = #{BSTOR_CD} ",
		"					<if test='searchcontent != null'> AND ("
		+ "														  EXAM.EXAM_KIND like '%${searchcontent}%' "
		+ "													   OR EXAM.SCHYR like '%${searchcontent}%' "
		+ "													   OR EXAM.EXAM_NM like '%${searchcontent}%' "
		+ "													   OR CARDS.OMR_NM like '%${searchcontent}%' "
		+ "													   OR CD.CD_NM like '%${searchcontent}%' "
		+ "														  ) </if>",
		"				  GROUP BY "
		+ "							MST.OMR_MST_CD"
		+ "						  , MST.CMPN_CD"
		+ "						  , MST.EXAM_CD"
		+ "						  , EXAM.EXAM_NM"
		+ "						  , EXAM.EXAM_DT"
		+ "						  , EXAM.SCHYR"
		+ "						  , EXAM.EXAM_KIND"
		+ "						  , CARDS.OMR_NM "
		+ "				) AS AD "
		+ "		INNER JOIN TB_INTEGRATION_CD CD		ON CD.CD = AD.STTUS_CD "
		+ "										   AND CD.CD_GROUP_ID = 1 "
		+ "		ORDER BY AD.EXAM_CD DESC"
		+ "				,AD.OMR_NM "
		+ "				OFFSET #{pageNum} ROWS "
		+ "				 FETCH NEXT #{limit} ROWS ONLY ",
		"	</script>"})
	List<OMR_EXAM> OMRExamsubList(Map<String, Object> param);

	@Select({"<script>",
		"select count(*) from TB_OMR_EXAM_MST where 1=1 AND DEL_YN='N'",
		"<if test='searchcontent != null'> and (EXAM_NM  like '%${searchcontent}%' OR EXAM_DT like '%${searchcontent}%' OR SCHYR like '%${searchcontent}%' OR EXAM_KIND like '%${searchcontent}%')</if>",
		"</script>"})
	int getOMREXAMCount(Map<String, Object> param);

	@Select({"<script>",
		"select * from TB_OMR_EXAM_MST where 1=1 AND DEL_YN='N'",
		"<if test='EXAM_CD != null'> AND EXAM_CD = #{EXAM_CD} </if>",
		"<if test='searchcontent != null'> AND (EXAM_KIND like '%${searchcontent}%' OR SCHYR like '%${searchcontent}%' OR EXAM_NM like '%${searchcontent}%') </if>",
		"<if test='EXAM_CD == null'> order by EXAM_DT desc OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY </if>",
		"</script>"})
	List<OMR_EXAM> OMRExamList(Map<String, Object> param);

	@Insert("insert into TB_OMR_EXAM_MST values(#{CMPN_CD},#{BSTOR_CD},#{ECI_EXAM_CD},#{EXAM_NM},#{EXAM_DT},#{SCHYR},#{EXAM_KIND},1,'Y','N',#{MBR_NO},#{MBR_NO},GETDATE(),GETDATE())")
	void insertmexrow(Map<String, Object> param);

	@Insert("INSERT INTO LOG_FILE_CHG_TXN VALUES(GETDATE(),'OMR',#{MST_CD},#{PID},#{EXAM_CD},#{CMPN_CD},#{BSTOR_CD},#{ORGIN_FILE_NM},#{CHG_FILE_NM})")
	void insert_m_filename(Map<String, Object> param);

	@Select({"<script>",
		"select REG.* , LSN.LSN_NM from TB_OMR_RECOG REG JOIN  TB_LSN_MST LSN ON REG.LSN_CD = LSN.LSN_CD "
		+ " where REG.EXAM_CD = #{EXAM_CD} and REG.OMR_MST_CD = #{OMR_MST_CD} and REG.BSTOR_CD = #{BSTOR_CD} and REG.CMPN_CD = #{CMPN_CD} AND REG.DEL_YN = 'N' ",
		"<if test='rsearchcontent != null'> and (REG.EXMN_NO like '%${rsearchcontent}%' or LSN.LSN_NM like '%${rsearchcontent}%' or REG.STDN_NM like '%${rsearchcontent}%') </if>",
		" order by REG.ERR_CNT desc,REG.EXMN_NO OFFSET #{rpageNum} ROWS FETCH NEXT #{rlimit} ROWS ONLY ",
		"</script>"})
	List<OMR_RECOG> getomrrecog(Map<String, Object> param);

	@Select({"<script>",
		"select count(*) from TB_OMR_RECOG REG JOIN TB_LSN_MST LSN ON REG.LSN_CD = LSN.LSN_CD "
		+ " where REG.EXAM_CD = #{EXAM_CD} and REG.OMR_MST_CD = #{OMR_MST_CD} and REG.BSTOR_CD = #{BSTOR_CD} and REG.CMPN_CD = #{CMPN_CD}  AND REG.DEL_YN = 'N'  ",
		" <if test='rsearchcontent != null'> and (REG.EXMN_NO like '%${rsearchcontent}%' or LSN.LSN_NM like '%${rsearchcontent}%' or REG.STDN_NM like'%${rsearchcontent}%' ) </if>",
		//" <if test='rsearchcontent != null'> and (REG.EXMN_NO like '%나%' or LSN.LSN_NM like '%나%' or REG.STDN_NM like '%나%' or REG.STTUS like '%나%' ) </if>",
		"</script>"})
	int omr_recog_cnt(Map<String, Object> param);

	@Update("update TB_OMR_EXAM_LSN_MST set STTUS_CD = #{STTUS_CD} , UPD_DTM=GETDATE(),UPD_MBR_NO=#{MBR_NO} where BSTOR_CD = #{BSTOR_CD} and CMPN_CD = #{CMPN_CD} and EXAM_CD = #{EXAM_CD} and OMR_MST_CD = #{OMR_MST_CD} ")
	void sttuschg(Map<String, Object> param);


	@Select({"<script>",
				"  SELECT COUNT(*)" +
				"    FROM (   " +
				" 			SELECT 	REG.EXAM_CD   " +
				"					,BSTOR.BSTOR_NM   " +
				"					,EXAM.EXAM_NM   " +
				"     				,ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') AS SUB_NM  " +
				"     				,ISNULL(LSNMAP.LSN_NM,'선택과목없음') AS LSN_NM   " +
				"     				,ISNULL(LSNMAP.LSN_CD,-1) AS LSN_CD   " +
				"     				,REG.CMPN_CD   " +
				"     				,REG.BSTOR_CD   " +
				"    			 	,EXAM.SCHYR   " +
				"     				,ISNULL(LSNMAP.LSN_GRP_CD,0) AS SUB_CD  " +
				"     				,SUM(CASE WHEN REG.ERR_CNT > 0 THEN 1 ELSE 0 END) AS ERR_CNT   " +
				"     				,SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) AS CRA_YN     " +
				"     				,COUNT(*) AS CRA_CNT   " +
				"   		   FROM TB_OMR_RECOG REG         " +
				"					LEFT OUTER JOIN TB_LSN_MAPPG LSNMAP ON REG.LSN_CD = LSNMAP.LSN_CD   " +
				"					INNER JOIN TB_OMR_EXAM_MST EXAM		ON REG.EXAM_CD = EXAM.EXAM_CD   " +
				"					INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON BSTOR.BSTOR_CD = REG.BSTOR_CD   "+
				"	      	  WHERE 1=1  "+
				"	        	AND REG.CMPN_CD = #{CMPN_CD} "+
				"	       		AND REG.DEL_YN = 'N' "+
				"				AND EXAM.USE_YN = 'Y' ",
				"				<if test='searchbstor != null'> AND REG.BSTOR_CD 	IN 	${searchbstor} </if> ",
				"				<if test='searchmock != null'> and reg.EXAM_CD 	in 	${searchmock} </if>",
				"				<if test='searchgrade != null'> and EXAM.SCHYR  	in 	${searchgrade} </if>",
				"				<if test='searchsub != null'> AND ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') in ${searchsub} </if>",
				"				<if test='searchcontent != null'> AND ("+
				"													BSTOR.BSTOR_NM like '%${searchcontent}%' "+
				"												OR EXAM.EXAM_NM like '%${searchcontent}%' "+
				"												OR ISNULL(LSNMAP.LSN_NM,'선택과목없음') like '%${searchcontent}%' "+
				"												OR EXAM.SCHYR like '%${searchcontent}%'"+
				"												) </if>" ,
				"		   	  GROUP BY 	REG.EXAM_CD  " +
				"						,BSTOR.BSTOR_NM  " +
				"						,EXAM.EXAM_NM  " +
				"						,LSNMAP.LSN_GRP_NM " +
				"						,LSNMAP.LSN_NM  " +
				"						,LSNMAP.LSN_CD  " +
				"						,REG.CMPN_CD  " +
				"						,REG.BSTOR_CD  " +
				"						,EXAM.SCHYR " +
				"						,LSNMAP.LSN_GRP_CD ",
				"				<if test='searchscore == 1' > having COUNT(*) != SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
				"				<if test='searchscore == 2' > having COUNT(*) = SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
				" 		) AS AD   "+
				"		LEFT OUTER JOIN TB_OMR_EXAM_LSN_MST OELMST 	ON 	OELMST.EXAM_CD = AD.EXAM_CD    " +
				"		 				AND OELMST.LSN_CD = AD.LSN_CD    " +
				"		 				AND OELMST.BSTOR_CD = AD.BSTOR_CD    " +
				"		LEFT OUTER JOIN ( " +
				"      					SELECT 	B.BSTOR_NM " +
				"     							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END AS LSN_GRP_CD " +
				"     							,COUNT(DISTINCT A.OMR_IMG) AS LSN_GRP_CNT " +
				"        				  FROM 	TB_OMR_RECOG A " +
				"     							INNER JOIN TB_CMPN_BSTOR_MST B	ON	A.BSTOR_CD = B.BSTOR_CD " +
				"     							INNER JOIN TB_LSN_MAPPG C		ON	A.LSN_CD = C.LSN_CD  " +
				"     							INNER JOIN TB_OMR_EXAM_MST D	ON	D.EXAM_CD = A.EXAM_CD  " +
				"       				 WHERE 	1=1  " +
						"   					   <if test='searchmock != null'> AND A.EXAM_CD in ${searchmock} </if> " ,
				"   					   AND 	A.DEL_YN = 'N'  " +
				"     					   AND 	D.USE_YN = 'Y'  " +
				"       			  GROUP BY 	A.OMR_MST_CD " +
				"     							,B.BSTOR_NM " +
				"     							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END  " +
				"     					) AS MCNT ON MCNT.LSN_GRP_CD = CASE WHEN AD.SUB_CD IN (22250,22300,22330) THEN 22250 ELSE AD.SUB_CD END    " +
				"     AND AD.BSTOR_NM = MCNT.BSTOR_NM  ",
				"</script>"})
	int OMR_Exam_GRAD_Count(Map<String, Object> param);


	@Select({"<script>",
			"  SELECT COUNT(*)" +
					"    FROM (   " +
					" 			SELECT 	REG.EXAM_CD   " +
					"					,BSTOR.BSTOR_NM   " +
					"					,EXAM.EXAM_NM   " +
					"     				,ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') AS SUB_NM  " +
					"     				,ISNULL(LSNMAP.LSN_NM,'선택과목없음') AS LSN_NM   " +
					"     				,ISNULL(LSNMAP.LSN_CD,-1) AS LSN_CD   " +
					"     				,REG.CMPN_CD   " +
					"     				,REG.BSTOR_CD   " +
					"    			 	,EXAM.SCHYR   " +
					"     				,ISNULL(LSNMAP.LSN_GRP_CD,0) AS SUB_CD  " +
					"     				,SUM(CASE WHEN REG.ERR_CNT > 0 THEN 1 ELSE 0 END) AS ERR_CNT   " +
					"     				,SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) AS CRA_YN     " +
					"     				,COUNT(*) AS CRA_CNT   " +
					"   		   FROM TB_OMR_RECOG REG         " +
					"					LEFT OUTER JOIN TB_LSN_MAPPG LSNMAP ON REG.LSN_CD = LSNMAP.LSN_CD   " +
					"					INNER JOIN TB_OMR_EXAM_MST EXAM		ON REG.EXAM_CD = EXAM.EXAM_CD   " +
					"					INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON BSTOR.BSTOR_CD = REG.BSTOR_CD   "+
					"	      	  WHERE 1=1  "+
					"	        	AND REG.CMPN_CD = #{CMPN_CD} "+
					"	       		AND REG.DEL_YN = 'N' "+
					"				AND EXAM.USE_YN = 'Y' " +
					"				AND REG.BSTOR_CD >=1000 ",
			"				<if test='searchbstor != null'> AND REG.BSTOR_CD 	IN 	${searchbstor} </if> ",
			"				<if test='searchmock != null'> and reg.EXAM_CD 	in 	${searchmock} </if>",
			"				<if test='searchgrade != null'> and EXAM.SCHYR  	in 	${searchgrade} </if>",
			"				<if test='searchsub != null'> AND ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') in ${searchsub} </if>",
			"				<if test='searchcontent != null'> AND ("+
					"													BSTOR.BSTOR_NM like '%${searchcontent}%' "+
					"												OR EXAM.EXAM_NM like '%${searchcontent}%' "+
					"												OR ISNULL(LSNMAP.LSN_NM,'선택과목없음') like '%${searchcontent}%' "+
					"												OR EXAM.SCHYR like '%${searchcontent}%'"+
					"												) </if>" ,
			"		   	  GROUP BY 	REG.EXAM_CD  " +
					"						,BSTOR.BSTOR_NM  " +
					"						,EXAM.EXAM_NM  " +
					"						,LSNMAP.LSN_GRP_NM " +
					"						,LSNMAP.LSN_NM  " +
					"						,LSNMAP.LSN_CD  " +
					"						,REG.CMPN_CD  " +
					"						,REG.BSTOR_CD  " +
					"						,EXAM.SCHYR " +
					"						,LSNMAP.LSN_GRP_CD ",
			"				<if test='searchscore == 1' > having COUNT(*) != SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
			"				<if test='searchscore == 2' > having COUNT(*) = SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
			" 		) AS AD   "+
					"		LEFT OUTER JOIN TB_OMR_EXAM_LSN_MST OELMST 	ON 	OELMST.EXAM_CD = AD.EXAM_CD    " +
					"		 				AND OELMST.LSN_CD = AD.LSN_CD    " +
					"		 				AND OELMST.BSTOR_CD = AD.BSTOR_CD    " +
					"		LEFT OUTER JOIN ( " +
					"      					SELECT 	B.BSTOR_NM " +
					"     							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END AS LSN_GRP_CD " +
					"     							,COUNT(DISTINCT A.OMR_IMG) AS LSN_GRP_CNT " +
					"        				  FROM 	TB_OMR_RECOG A " +
					"     							INNER JOIN TB_CMPN_BSTOR_MST B	ON	A.BSTOR_CD = B.BSTOR_CD " +
					"     							INNER JOIN TB_LSN_MAPPG C		ON	A.LSN_CD = C.LSN_CD  " +
					"     							INNER JOIN TB_OMR_EXAM_MST D	ON	D.EXAM_CD = A.EXAM_CD  " +
					"       				 WHERE 	1=1  " ,
					"   					   <if test='searchmock != null'> AND A.EXAM_CD in ${searchmock} </if> " ,
					"   					   AND 	A.DEL_YN = 'N'  " +
					"     					   AND 	D.USE_YN = 'Y'  " +
					"       			  GROUP BY 	A.OMR_MST_CD " +
					"     							,B.BSTOR_NM " +
					"     							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END  " +
					"     					) AS MCNT ON MCNT.LSN_GRP_CD = CASE WHEN AD.SUB_CD IN (22250,22300,22330) THEN 22250 ELSE AD.SUB_CD END    " +
					"     AND AD.BSTOR_NM = MCNT.BSTOR_NM  ",
			"</script>"})
	int OMR_Exam_GRAD_Count1000(Map<String, Object> param);

//grdglist
	@Select({"<script>",
		  "SELECT AD.*  "
		  + "     ,ISNULL(OELMST.AVG_SC,0) AS AVG_SC    "
		  + "	  ,MCNT.LSN_GRP_CNT  "
		  + "FROM  (  "
		  + "		SELECT  "
		  + "			  	REG.EXAM_CD  "
		  + "			    ,BSTOR.BSTOR_NM  "
		  + "			    ,EXAM.EXAM_NM  "
		  + "			    ,ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') AS SUB_NM "
		  + "				,ISNULL(LSNMAP.LSN_NM,'선택과목없음') AS LSN_NM  "
		  + "				,ISNULL(LSNMAP.LSN_CD,-1) AS LSN_CD  "
		  + "				,REG.CMPN_CD  "
		  + "				,REG.BSTOR_CD  "
		  + "				,EXAM.SCHYR  "
		  + "				,ISNULL(LSNMAP.LSN_GRP_CD,0) AS SUB_CD 	"
		  + "				,SUM(CASE WHEN REG.ERR_CNT > 0 THEN 1 ELSE 0 END) AS ERR_CNT  "
		  + "				,SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) AS CRA_YN  "
		  + "				,COUNT(*) AS CRA_CNT"
		  + "	       FROM TB_OMR_RECOG REG        "
		  + "					LEFT OUTER JOIN TB_LSN_MAPPG LSNMAP ON 	REG.LSN_CD=LSNMAP.LSN_CD  "
		  + "		    		INNER JOIN TB_OMR_EXAM_MST EXAM		ON 	REG.EXAM_CD=EXAM.EXAM_CD  "
		  + "		        	INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON 	BSTOR.BSTOR_CD=REG.BSTOR_CD  "
		  + "	      WHERE 1=1  "
		  + "	        AND REG.CMPN_CD = #{CMPN_CD} "
		  + "	      	AND REG.DEL_YN = 'N' "
		  + "	       	AND EXAM.USE_YN = 'Y' " ,
		  "				<if test='searchbstor != null'> AND REG.BSTOR_CD 	in 	${searchbstor} </if> ",
		  "				<if test='searchmock != null'> AND REG.EXAM_CD 	in 	${searchmock} </if>",
		  "				<if test='searchgrade != null'> AND EXAM.SCHYR  	in 	${searchgrade} </if>",
		  "				<if test='searchsub != null'> AND ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') in ${searchsub} </if>",
		  "				<if test='searchcontent != null'> AND ("
		  + "												  BSTOR.BSTOR_NM like '%${searchcontent}%' "
		  + "											   OR EXAM.EXAM_NM like '%${searchcontent}%' "
		  + "											   OR ISNULL(LSNMAP.LSN_NM,'선택과목없음') like '%${searchcontent}%' "
		  + "											   OR EXAM.SCHYR like '%${searchcontent}%'"
		  + "			) </if>" ,
		  "		      GROUP BY REG.EXAM_CD  ,BSTOR.BSTOR_NM  ,EXAM.EXAM_NM  ,LSNMAP.LSN_GRP_NM ,LSNMAP.LSN_NM  ,LSNMAP.LSN_CD  ,REG.CMPN_CD  ,REG.BSTOR_CD  ,EXAM.SCHYR ,LSNMAP.LSN_GRP_CD ",
		  "		 		<if test='searchscore == 1' > having COUNT(*) != SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
		  "				<if test='searchscore == 2' > having COUNT(*) = SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
		  " 		) AS AD   "
		  + "  LEFT OUTER JOIN TB_OMR_EXAM_LSN_MST OELMST ON OELMST.EXAM_CD = AD.EXAM_CD    "
		  + " 		  AND OELMST.LSN_CD = AD.LSN_CD    AND OELMST.BSTOR_CD = AD.BSTOR_CD    "
		  + "	LEFT OUTER JOIN (			"
		  + "				 SELECT	B.BSTOR_NM"
		  + "						,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END AS LSN_GRP_CD"
		  + "						,COUNT(DISTINCT A.OMR_IMG) AS LSN_GRP_CNT"
		  + "				   FROM TB_OMR_RECOG A"
		  + "						INNER JOIN TB_CMPN_BSTOR_MST B	ON		A.BSTOR_CD	=	B.BSTOR_CD"
		  + "						INNER JOIN TB_LSN_MAPPG C		ON		A.LSN_CD = C.LSN_CD "
		  + "						INNER JOIN TB_OMR_EXAM_MST D	ON		D.EXAM_CD = A.EXAM_CD "
		  + "			 	  WHERE 1=1 ",
		  "							<if test='searchmock != null'> AND A.EXAM_CD 	in 	${searchmock} </if>",
		  "						AND A.DEL_YN = 'N' "
		  + "					AND D.USE_YN = 'Y' "
		  + "				  GROUP BY	A.OMR_MST_CD"
		  + "							,B.BSTOR_NM"
		  + "							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END "
		  + "				) AS MCNT ON  MCNT.LSN_GRP_CD = "
		  + " 									CASE WHEN AD.SUB_CD IN (22250,22300,22330) THEN 22250 " +
				  "									ELSE AD.SUB_CD END " +
				  "					  AND AD.BSTOR_NM = MCNT.BSTOR_NM "
			+ "	ORDER BY AD.BSTOR_NM "
		  + "		  	,AD.EXAM_NM"
		  + "			,AD.SCHYR,AD.SUB_CD"
		  + "			,AD.LSN_NM "
		  + " OFFSET #{pageNum} ROWS "
		  + " FETCH NEXT #{limit} ROWS ONLY ",
		  "</script>"})
	List<OMR_RECOG> OMRExamgradList(Map<String, Object> param);

	@Select({"<script>",
			"SELECT AD.*  "
					+ "     ,ISNULL(OELMST.AVG_SC,0) AS AVG_SC    "
					+ "	  ,MCNT.LSN_GRP_CNT  "
					+ "FROM  (  "
					+ "		SELECT  "
					+ "			  	REG.EXAM_CD  "
					+ "			    ,BSTOR.BSTOR_NM  "
					+ "			    ,EXAM.EXAM_NM  "
					+ "			    ,ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') AS SUB_NM "
					+ "				,ISNULL(LSNMAP.LSN_NM,'선택과목없음') AS LSN_NM  "
					+ "				,ISNULL(LSNMAP.LSN_CD,-1) AS LSN_CD  "
					+ "				,REG.CMPN_CD  "
					+ "				,REG.BSTOR_CD  "
					+ "				,EXAM.SCHYR  "
					+ "				,ISNULL(LSNMAP.LSN_GRP_CD,0) AS SUB_CD 	"
					+ "				,SUM(CASE WHEN REG.ERR_CNT > 0 THEN 1 ELSE 0 END) AS ERR_CNT  "
					+ "				,SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) AS CRA_YN  "
					+ "				,COUNT(*) AS CRA_CNT"
					+ "	       FROM TB_OMR_RECOG REG        "
					+ "					LEFT OUTER JOIN TB_LSN_MAPPG LSNMAP ON 	REG.LSN_CD=LSNMAP.LSN_CD  "
					+ "		    		INNER JOIN TB_OMR_EXAM_MST EXAM		ON 	REG.EXAM_CD=EXAM.EXAM_CD  "
					+ "		        	INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON 	BSTOR.BSTOR_CD=REG.BSTOR_CD  "
					+ "	      WHERE 1=1  "
					+ "	        AND REG.CMPN_CD = #{CMPN_CD} "
					+ "	      	AND REG.DEL_YN = 'N' "
					+ "	       	AND EXAM.USE_YN = 'Y' " +
					"			AND REG.BSTOR_CD >= 1000 " ,
			"				<if test='searchbstor != null'> AND REG.BSTOR_CD 	in 	${searchbstor} </if> ",
			"				<if test='searchmock != null'> AND REG.EXAM_CD 	in 	${searchmock} </if>",
			"				<if test='searchgrade != null'> AND EXAM.SCHYR  	in 	${searchgrade} </if>",
			"				<if test='searchsub != null'> AND ISNULL(LSNMAP.LSN_GRP_NM,'선택과목없음') in ${searchsub} </if>",
			"				<if test='searchcontent != null'> AND ("
					+ "												  BSTOR.BSTOR_NM like '%${searchcontent}%' "
					+ "											   OR EXAM.EXAM_NM like '%${searchcontent}%' "
					+ "											   OR ISNULL(LSNMAP.LSN_NM,'선택과목없음') like '%${searchcontent}%' "
					+ "											   OR EXAM.SCHYR like '%${searchcontent}%'"
					+ "			) </if>" ,
			"		      GROUP BY REG.EXAM_CD  ,BSTOR.BSTOR_NM  ,EXAM.EXAM_NM  ,LSNMAP.LSN_GRP_NM ,LSNMAP.LSN_NM  ,LSNMAP.LSN_CD  ,REG.CMPN_CD  ,REG.BSTOR_CD  ,EXAM.SCHYR ,LSNMAP.LSN_GRP_CD ",
			"		 		<if test='searchscore == 1' > having COUNT(*) != SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
			"				<if test='searchscore == 2' > having COUNT(*) = SUM(CASE WHEN REG.GRDG_YN = 'N' THEN 0 ELSE 1 END) </if>",
			" 		) AS AD   "
					+ "  LEFT OUTER JOIN TB_OMR_EXAM_LSN_MST OELMST ON OELMST.EXAM_CD = AD.EXAM_CD    "
					+ " 		  AND OELMST.LSN_CD = AD.LSN_CD    AND OELMST.BSTOR_CD = AD.BSTOR_CD    "
					+ "	LEFT OUTER JOIN (			"
					+ "				 SELECT	B.BSTOR_NM"
					+ "						,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END AS LSN_GRP_CD"
					+ "						,COUNT(DISTINCT A.OMR_IMG) AS LSN_GRP_CNT"
					+ "				   FROM TB_OMR_RECOG A"
					+ "						INNER JOIN TB_CMPN_BSTOR_MST B	ON		A.BSTOR_CD	=	B.BSTOR_CD"
					+ "						INNER JOIN TB_LSN_MAPPG C		ON		A.LSN_CD = C.LSN_CD "
					+ "						INNER JOIN TB_OMR_EXAM_MST D	ON		D.EXAM_CD = A.EXAM_CD "
					+ "			 	  WHERE 1=1 ",
			"							<if test='searchmock != null'> AND A.EXAM_CD 	in 	${searchmock} </if>",
			"						AND A.DEL_YN = 'N' "
					+ "					AND D.USE_YN = 'Y' "
					+ "				  GROUP BY	A.OMR_MST_CD"
					+ "							,B.BSTOR_NM"
					+ "							,CASE WHEN C.LSN_GRP_CD IN (22250,22300,22330) THEN 22250 ELSE C.LSN_GRP_CD END "
					+ "				) AS MCNT ON  MCNT.LSN_GRP_CD = "
					+ " 									CASE WHEN AD.SUB_CD IN (22250,22300,22330) THEN 22250 " +
					"									ELSE AD.SUB_CD END " +
					"					  AND AD.BSTOR_NM = MCNT.BSTOR_NM "
					+ "	ORDER BY AD.BSTOR_NM "
					+ "		  	,AD.EXAM_NM"
					+ "			,AD.SCHYR,AD.SUB_CD"
					+ "			,AD.LSN_NM "
					+ " OFFSET #{pageNum} ROWS "
					+ " FETCH NEXT #{limit} ROWS ONLY ",
			"</script>"})
	List<OMR_RECOG> OMRExamgradList1000(Map<String, Object> param);

	@Select(" SELECT EXMN_NO " +
			"  		 ,OMR_KEY " +
			"  		 ,OMR_MST_CD  " +
			"   FROM TB_OMR_RECOG  " +
			"  WHERE EXAM_CD = #{EXAM_CD}  " +
			"    AND LSN_CD = #{LSN_CD}  " +
			" 	 AND BSTOR_CD =#{BSTOR_CD}  " +
			" 	 AND DEL_YN='N'  " +
			"  ORDER BY OMR_MST_CD " +
			"			,OMR_KEY "+
			"   		,ERR_CNT DESC " +
			"   		,LSN_CD ")
	List<OMR_GRDG> stdnolist(Map<String, Object> param);

	@Select(" SELECT EXMN_NO " +
			"  		 ,OMR_KEY " +
			"  		 ,OMR_MST_CD  " +
			"   FROM TB_OMR_RECOG  " +
			"  WHERE EXAM_CD = #{EXAM_CD}  " +
			"    AND (LSN_CD = -1 OR LSN_CD = -2)  " +
			" 	 AND BSTOR_CD =#{BSTOR_CD}  " +
			" 	 AND DEL_YN='N'  " +
			"  ORDER BY OMR_MST_CD " +
			"			,OMR_KEY "+
			"   		,ERR_CNT DESC " +
			"   		,LSN_CD ")
	List<OMR_GRDG> stdnolsnlist(Map<String, Object> param);

	@Select({"<script>",
		"SELECT "
			+ "       REG.OMR_KEY ,REG.LSN_CD"
			+ " ,REG.OMR_IMG "
			+ "	  ,REG.EXMN_NO"
			+ "	  ,REG.STDN_NM"
			+ "	  ,REG.TOT_SC"
			+ "	  ,REG.SEX"
			+ "	  ,REG.BTHDAY"
			+ "	  ,REG.LSN_SEQ"
			+ "	  ,GRDG.QUEI_NO"
			+ "	  ,GRDG.MARK_NO"
			+ "	  ,GRDG.ERR_YN"
			+ "	  ,GRDG.CRA_YN "
			+ "	  ,GRDG.OMR_MST_CD "
			+ "	  ,REG.ERR_CNT"
			+ "  FROM TB_OMR_RECOG REG WITH(NOLOCK)"
			+ "  INNER JOIN TB_OMR_GRDG GRDG ON REG.OMR_KEY = GRDG.OMR_KEY"
			+ "  WHERE 1=1 "
			+ "   AND REG.EXAM_CD = #{EXAM_CD} "
			+ "   AND REG.LSN_CD = #{LSN_CD} "
			+ "   AND REG.BSTOR_CD= #{BSTOR_CD} "
			+ "   AND REG.OMR_KEY= #{OMR_KEY} ",
			"<if test='rsearchcontent != null'> and (REG.EXMN_NO like '%${rsearchcontent}%'  OR REG.STDN_NM like '%${rsearchcontent}%' )</if>",
			" ORDER BY	REG.OMR_MST_CD " +
					"			,REG.ERR_CNT DESC   " +
					"         	,REG.OMR_KEY   " +
					"      		,LSN_CD  " +
					"         	,GRDG.QUEI_NO","</script>"})
	List<OMR_GRDG> getgradlist(Map<String, Object> param);

	@Select({"<script>",
		"SELECT "
			+ "       REG.OMR_KEY ,REG.LSN_CD"
			+ " ,REG.OMR_IMG "
			+ "	  ,REG.EXMN_NO "
			+ "	  ,REG.STDN_NM "
			+ "	  ,REG.SEX "
			+ "	  ,REG.BTHDAY "
			+ "	  ,REG.TOT_SC "
			+ "	  ,REG.LSN_SEQ"
			+ "	  ,GRDG.QUEI_NO "
			+ "	  ,GRDG.MARK_NO "
			+ "	  ,GRDG.ERR_YN "
			+ "	  ,GRDG.CRA_YN "
			+ "	  ,GRDG.OMR_MST_CD "
			+ "	  ,REG.ERR_CNT "
			+ "  FROM TB_OMR_RECOG REG WITH(NOLOCK) "
			+ "  INNER JOIN TB_OMR_GRDG GRDG ON REG.OMR_KEY = GRDG.OMR_KEY "
			+ "  WHERE 1=1 "
			+ "   AND REG.EXAM_CD = #{EXAM_CD} "
			+ "   AND ( REG.LSN_CD = -1 or REG.LSN_CD = -2 ) "
			+ "   AND REG.BSTOR_CD= #{BSTOR_CD} "
			+ "   AND REG.OMR_KEY= #{OMR_KEY} ",
			"<if test='rsearchcontent != null'> and (REG.EXMN_NO like '%${rsearchcontent}%'  OR REG.STDN_NM like '%${rsearchcontent}%' )</if>",
			" ORDER BY	REG.OMR_MST_CD " +
					"			,REG.ERR_CNT DESC   " +
					"         	,REG.OMR_KEY   " +
					"      		,LSN_CD  " +
					"         	,GRDG.QUEI_NO"
			,"</script>"})
	List<OMR_GRDG> getgradnolsnlist(Map<String, Object> param);
	@Select("select OMR_IMG from TB_OMR_GRDG "
			+ " where LSN_CD=#{LSN_CD} "
			+ " and BSTOR_CD = #{BSTOR_CD} "
			+ " and EXAM_CD = #{EXAM_CD} "
			+ " and EXMN_NO = #{EXMN_NO} "
			+ " order by LOAD_DTM desc OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY ")
	String omr_img(Map<String, Object> param);

	@Select("EXEC UP_OMR_GRDG_ADM #{CMPN_CD},#{BSTOR_CD},#{EXAM_CD},#{LSN_CD},#{UPD_MBR_NO}")
	void scoring(Map<String, Object> param);

	@Update("update TB_OMR_GRDG "
			+ " set MARK_NO = #{MARK_NO} , UPD_MBR_NO = #{MBR_NO} , UPD_DTM = GETDATE() "
			+ " where OMR_KEY = #{OMR_KEY} "
			+ " and EXAM_CD = #{EXAM_CD} "
			+ " and LSN_CD = #{LSN_CD} "
			+ " and BSTOR_CD = #{BSTOR_CD} "
			+ " and QUEI_NO = #{QUEI_NO}")
	void updategrdgrec(Map<String, Object> param);

	@Update({"update TB_OMR_RECOG set DEL_YN = 'Y',UPD_MBR_NO=#{UPD_MBR_NO}, UPD_DTM = GETDATE() where OMR_KEY =#{OMR_KEY} and EXAM_CD = #{EXAM_CD} and BSTOR_CD = #{BSTOR_CD} and LSN_CD = #{LSN_CD}"})
	void deletegradg(Map<String, Object> param);

	@Select("select count(*) "
			+ " from TB_OMR_RECOG "
			+ " where 1=1 "
			+ " and OMR_IMG = #{OMR_IMG}")
	int reuploadomr(Map<String, Object> param);

	@Select("SELECT CHG_FILE_NM FROM LOG_FILE_CHG_TXN "
			+ " where EXAM_CD = #{EXAM_CD} "
			+ " AND CMPN_CD = #{CMPN_CD} "
			+ " AND BSTOR_CD = #{BSTOR_CD} "
			+ " AND MST_CD = #{MST_CD} "
			+ " AND OMR_OCR_DIV= 'OMR' "
			+ " AND ORIGIN_FILE_NM = #{ORIGIN_FILE_NM}")
	String getomrfilename(Map<String, Object> param);

	@Select("select * from TB_OMR_EXAM_MST "
			+ " where CMPN_CD = #{CMPN_CD} "
			+ " and BSTOR_CD = #{BSTOR_CD} "
			+ " and EXAM_NM = #{EXAM_NM} "
			+ " and EXAM_DT = #{EXAM_DT} "
			+ " and SCHYR = #{SCHYR} "
			+ " and EXAM_KIND = #{EXAM_KIND} "
			+ " and REG_MBR_NO = #{MBR_NO} ")
	List<OMR_EXAM> getomrexam(Map<String, Object> param);
	
	@Select({"<script>", 
		"select lm.ECI_LSN_CD"
		+ "		 ,lmst.LSN_NM"
		+ "		 ,mmst.OMR_NM"
		+ "		 ,oelmst.CMPN_CD"
		+ "		 ,oelmst.EXAM_CD"
		+ "		 ,oelmst.LSN_CD"
		+ "		 ,oelmst.OMR_MST_CD"
		+ "		 ,oelmst.QUEI_NUM"
		+ "		 ,oelmst.TOT_DISMK"
		+ "		 ,oelmst.USE_YN"
		+ "		 ,oelmst.DEL_YN "
		+ "    from TB_OMR_EXAM_LSN_MST oelmst  "
		+ "	join TB_LSN_MST lmst on oelmst.LSN_CD = lmst.LSN_CD  "
		+ "	join TB_OMR_MST mmst on mmst.OMR_MST_CD = oelmst.OMR_MST_CD  "
		+ "	join TB_LSN_MAPPG LM on lmst.LSN_CD = LM.LSN_CD "
		+ "   where EXAM_CD = #{EXAM_CD} "
		+ "     AND CMPN_CD =#{CMPN_CD} "
		+ "	 and oelmst.DEL_YN='N'  ",
		"		  <if test='LSN_CD != null'> and oelmst.LSN_CD = #{LSN_CD} </if>" ,
		"   group by lm.ECI_LSN_CD"
		+ "			,lmst.LSN_NM"
		+ "			, mmst.OMR_NM"
		+ "			, oelmst.CMPN_CD"
		+ "			, oelmst.EXAM_CD"
		+ "			, oelmst.LSN_CD"
		+ "			, oelmst.OMR_MST_CD"
		+ "			, oelmst.QUEI_NUM"
		+ "			, oelmst.TOT_DISMK"
		+ "			, oelmst.USE_YN"
		+ "			, oelmst.DEL_YN   "
		+ "    ORDER BY 1 ASC ",
		"</script>"})
	  List<OMR_EXAM_LSN> getlsnlist(Map<String, Object> param);

	@Insert("insert into TB_OMR_EXAM_LSN_MST "
			+ " values(#{CMPN_CD},#{BSTOR_CD},#{EXAM_CD},#{LSN_CD},#{OMR_MST_CD},0,1,#{QUEI_NUM},#{TOT_DISMK},0,'Y','N',#{MBR_NO},#{MBR_NO},GETDATE(),GETDATE())")
	void insertmlsn(Map<String, Object> param);

	@Select("select LSN_CD from  TB_LSN_MST where LSN_CD <= 34")
	List<Integer> getlsncdlist();

	@Select("select * from TB_OMR_MST where USE_YN = 'Y' ")
	List<OMR_MST> mmstlist();

	@Select("select lmst.* from TB_LSN_MST lmst "
			+ "	left outer join (select lsn_cd from TB_OMR_EXAM_LSN_MST where EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND DEL_YN = 'N' ) as oelmst on oelmst.LSN_CD = lmst.LSN_CD "
			+ " where oelmst.LSN_CD is null")
	List<OMR_EXAM_LSN> getnewlsnlist(Map<String, Object> param);

	@Select("SELECT OMR_MST_CD FROM TB_OMR_MST where OMR_NM = #{OMR_NM}")
	Integer getomrcd(Map<String, Object> param);

	@Insert("insert into TB_OMR_EXAM_LSN_MST "
			+ " values(#{CMPN_CD},#{BSTOR_CD},#{EXAM_CD},#{LSN_CD},#{OMR_MST_CD},0,1,#{QUEI_NUM},#{TOT_DISMK},0,'Y','N',#{MBR_NO},#{MBR_NO},GETDATE(),GETDATE())")
	void insertmlsnrow(Map<String, Object> param);
	
	@Select("SELECT OMR_MST_CD from TB_OMR_EXAM_LSN_MST WHERE EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} order by omr_mst_cd offset 0 rows fetch next 1 rows only ")
	Integer getomrcdbylsn(Map<String, Object> param);

	@Insert("INSERT INTO TB_OMR_EXAM_LSN_QUEI_MST "
			+ " VALUES (#{QUEI_NO},#{CMPN_CD},#{EXAM_CD},#{LSN_CD},#{OMR_MST_CD},#{CRA_NO},#{DISMK},#{QUE_AREA},#{QUE_DTL_AREA},#{QUE_TYPE},#{MBR_NO},#{MBR_NO},GETDATE())")
	void insertqueimst(Map<String, Object> param);

	@Select("select * from TB_OMR_EXAM_LSN_QUEI_MST WHERE CMPN_CD = #{CMPN_CD} AND EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} order by QUEI_NO")
	List<OMR_EXAM_LSN_QUEI> getqlist(Map<String, Object> param);

	@Select("select * from TB_OMR_EXAM_LSN_QUEI_MST WHERE CMPN_CD = #{CMPN_CD} AND EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} and QUEI_NO = #{QUEI_NO}")
	OMR_EXAM_LSN_QUEI selectquei(Map<String, Object> param);
	
	@Select("select BSTOR_CD from TB_CMPN_BSTOR_MST  order by BSTOR_CD ")
	List<Integer> getBlist();
	  
	@Update("update TB_OMR_EXAM_LSN_QUEI_MST "
			+ " set CRA_NO=#{CRA_NO}, DISMK=#{DISMK}, QUE_AREA=#{QUE_AREA}, QUE_DTL_AREA=#{QUE_DTL_AREA}, QUE_TYPE=#{QUE_TYPE}, UPD_MBR_NO=#{MBR_NO}, LOAD_DTM=GETDATE() "
			+ " where QUEI_NO=#{QUEI_NO} and CMPN_CD=#{CMPN_CD} and EXAM_CD=#{EXAM_CD} and LSN_CD=#{LSN_CD} and OMR_MST_CD=#{OMR_MST_CD} ")
	void updatequeimst(Map<String, Object> paramMap);
	  
	@Update("update TB_OMR_EXAM_MST "
			+ " set EXAM_NM=#{EXAM_NM}, EXAM_DT=#{EXAM_DT}, SCHYR=#{SCHYR}, EXAM_KIND=#{EXAM_KIND}, UPD_MBR_NO=#{MBR_NO}, UPD_DTM=GETDATE(), ECI_EXAM_CD=#{ECI_EXAM_CD} "
			+ " where EXAM_CD=#{EXAM_CD} and CMPN_CD=#{CMPN_CD} ")
	void update_omr_exam(Map<String, Object> paramMap);
	  
	@Update("update TB_OMR_EXAM_LSN_MST set OMR_MST_CD=#{OMR_MST_CD},QUEI_NUM=#{QUEI_NUM},TOT_DISMK=#{TOT_DISMK},UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE() "
			+ " where EXAM_CD=#{EXAM_CD} and CMPN_CD=#{CMPN_CD} AND LSN_CD=#{LSN_CD}")
	void update_omr_lsn(Map<String, Object> paramMap);
	  
	@Update(" update TB_OMR_EXAM_LSN_QUEI_MST set CRA_NO=#{CRA_NO},DISMK=#{DISMK},QUE_AREA=#{QUE_AREA},QUE_DTL_AREA=#{QUE_DTL_AREA},QUE_TYPE=#{QUE_TYPE},UPD_MBR_NO=#{MBR_NO} ,LOAD_DTM=GETDATE() "
			+ " where CMPN_CD=#{CMPN_CD} and EXAM_CD=#{EXAM_CD} and LSN_CD=#{LSN_CD} and QUEI_NO=#{QUEI_NO}")
	void update_omr_quei(Map<String, Object> paramMap);
	  
	@Select("SELECT DISTINCT EXAM.EXAM_NM " +
			"  , CASE WHEN RC.OMR_MST_CD = 6 THEN '국어' ELSE CASE WHEN RC.OMR_MST_CD = 7 THEN '수학' ELSE CASE WHEN RC.OMR_MST_CD = 3 THEN '영어' ELSE CASE WHEN RC.OMR_MST_CD = 8 THEN '한국사' ELSE '탐구' END END END END AS SUB_NM " +
			"  , BST.BSTOR_NM " +
			"  , DATEDIFF(n,RC.LOAD_DTM,GETDATE()) as DATEDIF   " +
			"  FROM TB_OMR_RECOG RC  " +
			"  INNER JOIN TB_OMR_EXAM_MST EXAM  ON RC.EXAM_CD  = EXAM.EXAM_CD  " +
			"  INNER JOIN TB_OMR_MST MST   ON MST.OMR_MST_CD = RC.OMR_MST_CD  " +
			"  INNER JOIN TB_CMPN_BSTOR_MST BST ON BST.BSTOR_CD = RC.BSTOR_CD " +
			"  WHERE 1=1  " +
			"    AND DATEDIFF(d,rc.LOAD_DTM,GETDATE()) < 5  " +
			"  ORDER BY 4 ASC")
	List<ALRAM> omralarmlist(Map<String, Object> paramMap);
	  
	@Select("select distinct(EXAM_NM), DATEDIFF(n,rc.LOAD_DTM,GETDATE()) DATEDIF "
			+ " from TB_OCR_EXAM_RC RC "
			+ " join TB_OCR_EXAM_MST EXAM ON RC.EXAM_CD = EXAM.EXAM_CD "
			+ " where 1=1 "
			+ " AND DATEDIFF(d,REG_DTM,GETDATE()) < 5 order by 2 ")
	List<ALRAM> alarmlist(Map<String, Object> paramMap);
	  
	@Select("select EXMN_NO,OMR_KEY from TB_OMR_RECOG where OMR_IMG=#{OMR_IMG} AND LSN_CD=#{LSN_CD} and DEL_YN='N' ")
	OMR_GRDG getstdno(Map<String, Object> paramMap);
	  
	@Select("select DATEDIFF(n,LOG_DTM,GETDATE()) from LOG_FILE_CHG_TXN where OMR_OCR_DIV = #{OMR_OCR_DIV} AND CHG_FILE_NM = #{CHG_FILE_NM}")
	List<Integer> gettdiff(Map<String, Object> paramMap);
	  
	@Select("select * from TB_OMR_RECOG where OMR_IMG=#{OMR_IMG}")
	List<OMR_RECOG> getrecogobjt(Map<String, Object> paramMap);
	  
	@Select({"<script>", 
		"select count(*) from TB_OMR_MST where 1=1 ", 
		"<if test='searchcontent != null'> and OMR_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int omrcount(Map<String, Object> paramMap);
	  
	@Select({"<script>", 
		"select * from TB_OMR_MST where 1=1 and USE_YN = 'Y'", 
		"<if test='searchcontent != null'> and OMR_NM like '%${searchcontent}%' </if>", 
		"ORDER BY OMR_MST_CD OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY ", 
		"</script>"})
	List<OMR_MST> OMRList(Map<String, Object> paramMap);
	  
	@Update("update TB_OMR_EXAM_LSN_MST set DEL_YN = 'Y' , UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE() where EXAM_CD = #{EXAM_CD} and LSN_CD = #{LSN_CD} and CMPN_CD = #{CMPN_CD}")
	void dellsn(Map<String, Object> paramMap);
	  
	@Update("update  TB_OMR_EXAM_MST set DEL_YN = 'Y', UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE()  where EXAM_CD = #{EXAM_CD} and CMPN_CD = #{CMPN_CD}")
	void delex(Map<String, Object> paramMap);
	
	@Update("update TB_OMR_EXAM_MST set USE_YN = #{USE_YN}, UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE()  where EXAM_CD = #{EXAM_CD} and CMPN_CD = #{CMPN_CD}")
	void examuseyn(Map<String, Object> paramMap);
//	--  
	@Update("update TB_OMR_GRDG set EXMN_NO=#{EXMN_NO}, UPD_MBR_NO = #{MBR_NO} , UPD_DTM = GETDATE()  "
			+ " where OMR_KEY = #{OMR_KEY} and EXAM_CD = #{EXAM_CD}  and BSTOR_CD = #{BSTOR_CD}")
	void updategrdgstdinfo(Map<String, Object> paramMap);
	//and LSN_CD = #{LSN_CD}
	  
	@Update("update TB_OMR_RECOG set STDN_NM = #{STDN_NM}, EXMN_NO=#{EXMN_NO} , BTHDAY=#{BTHDAY} , SEX=#{SEX} ,UPD_DTM=GETDATE(), UPD_MBR_NO =#{MBR_NO} "
			+ " where OMR_KEY = #{OMR_KEY} and EXAM_CD = #{EXAM_CD} and BSTOR_CD = #{BSTOR_CD}")
	void updaterecogstdinfo(Map<String, Object> paramMap);
//and LSN_CD = #{LSN_CD}

	@Select({"<script>",
		" select DISTINCT(BSTOR.BSTOR_NM),REG.BSTOR_CD "
		+ " from TB_OMR_RECOG REG "
		+ " INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON 	BSTOR.BSTOR_CD = REG.BSTOR_CD ",
		"<if test='searchcontent != null'> and BSTOR.BSTOR_NM like '%${searchcontent}%' </if>", 
		" order by BSTOR.BSTOR_NM OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY ",
		"</script>"})
	List<BSTOR> filtblist(Map<String, Object> param);

	@Select({"<script>",
		" select DISTINCT(EXAM.EXAM_NM),REG.EXAM_CD "
		+ " from TB_OMR_RECOG REG "
		+ " INNER JOIN TB_OMR_EXAM_MST EXAM	ON 	EXAM.EXAM_CD = REG.EXAM_CD ",
		"<if test='searchcontent != null'> and EXAM.EXAM_NM like '%${searchcontent}%' </if>", 
		" order by REG.EXAM_CD DESC OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY ",
		"</script>"})
	List<OMR_EXAM> filtexamlist(Map<String, Object> param);

	@Select("select distinct BSTOR_CD from TB_OMR_RECOG")
	List<Integer> getbstorlist();

	@Select("select distinct EXAM_CD from TB_OMR_RECOG where DEL_YN = 'N'")
	List<Integer> getsmocklist();

	@Select("select distinct SCHYR from TB_OMR_EXAM_MST")
	List<String> getsgragelist();

	@Select("select distinct lsnmp.LSN_GRP_NM from TB_LSN_MAPPG lsnmp join TB_OMR_RECOG reg on reg.LSN_CD = lsnmp.LSN_CD ")
	List<String> getssublist();

	@Update("update TB_OMR_RECOG set DEL_YN = 'Y' ,UPD_DTM=GETDATE(),UPD_MBR_NO=#{MBR_NO} where BSTOR_CD = #{BSTOR_CD} and LSN_CD = #{LSN_CD} and EXAM_CD = #{EXAM_CD}")
	void deletegrdgexam(Map<String, Object> param);

	@Update("update TB_OMR_RECOG set DEL_YN = 'Y' ,UPD_DTM=GETDATE(),UPD_MBR_NO=#{MBR_NO} where BSTOR_CD = #{BSTOR_CD} and LSN_CD = '-2' and EXAM_CD = #{EXAM_CD}")
	void DelSecGrdgExam(Map<String, Object> param);

	
	@Select({"<script>",
		" select distinct (SCHYR) "
		+ " from TB_OMR_EXAM_MST oemst "
		+ " join TB_OMR_RECOG reg on oemst.EXAM_CD = reg.EXAM_CD ",
		"<if test='searchcontent != null'> and SCHYR like '%${searchcontent}%' </if>",
		"</script>"})
	List<OMR_EXAM> filtglist(Map<String, Object> param);

	@Select({"<script>",
		" select distinct(LSN_GRP_NM) as SUB_NM "
		+ " from TB_LSN_MAPPG lmp "
		+ " join TB_OMR_RECOG reg on lmp.LSN_CD = reg.LSN_CD ",
		"<if test='searchcontent != null'> and LSN_GRP_NM like '%${searchcontent}%' </if>",
		" order by SUB_NM ",
		"</script>"})
	List<OMR_RECOG> filtsubjlist(Map<String, Object> param);

	@Select("select OMR_IMG from TB_OMR_RECOG where OMR_KEY = #{OMR_KEY} ")
	String getrecogobjtkey(Map<String, Object> param);

	@Select("update TB_OMR_GRDG "
			+ " set ERR_YN = #{ERR_YN} , UPD_MBR_NO = #{MBR_NO} , UPD_DTM = GETDATE() "
			+ " where OMR_KEY = #{OMR_KEY} "
			+ " and EXAM_CD = #{EXAM_CD} "
			+ " and LSN_CD = #{LSN_CD} "
			+ " and BSTOR_CD = #{BSTOR_CD} "
			+ " and QUEI_NO = #{QUEI_NO}")
	void updategrdgerr(Map<String, Object> param);

	@Select("select ORIGIN_FILE_NM,CHG_FILE_NM from LOG_FILE_CHG_TXN WHERE MST_CD = #{MST_CD}  AND EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND CHG_FILE_NM = #{CHG_FILE_NM}")
	FILELOG getfilelog(Map<String, Object> param);

	@Select({"<script>",
		"select count(DISTINCT(BSTOR.BSTOR_NM)) "
		+ "		from TB_OMR_RECOG REG "
		+ "		 INNER JOIN TB_CMPN_BSTOR_MST BSTOR	ON 	BSTOR.BSTOR_CD = REG.BSTOR_CD ",
		"<if test='searchcontent != null'> and BSTOR.BSTOR_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int filtblistcnt(Map<String, Object> param);

	@Select({"<script>",
		"select count(DISTINCT(SCHYR)) "
		+ "		from TB_OMR_EXAM_MST oemst "
		+ "	 join TB_OMR_RECOG reg on oemst.EXAM_CD = reg.EXAM_CD ",
		"<if test='searchcontent != null'> and SCHYR like '%${searchcontent}%' </if>",
		"</script>"})
	int filtEXAMcnt(Map<String, Object> param);

	@Select({"<script>",
		"select count(DISTINCT(EXAM.EXAM_NM)) "
		+ "		from TB_OMR_RECOG REG "
		+ "		 INNER JOIN TB_OMR_EXAM_MST EXAM	ON 	EXAM.EXAM_CD = REG.EXAM_CD",
		"<if test='searchcontent != null'> and EXAM.EXAM_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int filtgcnt(Map<String, Object> param);

	@Select({"<script>",
		"select count(DISTINCT(LSN_GRP_NM)) "
		+ "		from TB_LSN_MAPPG lmp "
		+ "		join TB_OMR_RECOG reg on lmp.LSN_CD = reg.LSN_CD ",
		"<if test='searchcontent != null'> and LSN_GRP_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int filtsubjcnt(Map<String, Object> param);

	@Update("UPDATE TB_OCR_EXAM_MST SET USE_YN = 'N' ,UPD_MBR_NO = #{MBR_NO},UPD_DTM = GETDATE() WHERE EXAM_CD =#{EXAM_CD} ")
	void delocrex(Map<String, Object> param);

	@Select("SELECT * FROM TB_OCR_EXAM_MST WHERE EXAM_CD = #{EXAM_CD}")
	OCR_EXAM selectcex(Map<String, Object> param);

	@Insert("INSERT INTO TB_OCR_EXAM_MST VALUES(#{OCR_MST_CD},#{EXAM_NM},#{SCHYR},#{EXAM_KIND},'N',#{MBR_NO},#{MBR_NO},GETDATE(),GETDATE())")
	void insertcexrow(Map<String, Object> param);

	@Select("SELECT * FROM TB_OMR_RECOG WHERE EXAM_CD = #{EXAM_CD} AND BSTOR_CD = #{BSTOR_CD} AND (LSN_CD = -1 or LSN_CD = -2) AND DEL_YN = 'N'  ORDER BY OMR_MST_CD ")
	List<OMR_GRDG> nolsnlist(Map<String, Object> param);

	@Select("SELECT * " +
			"  FROM TB_OMR_RECOG " +
			" WHERE 1=1" +
			"   AND OMR_KEY IN ${OMR_KEY} " +
			" ORDER BY OMR_MST_CD ")
	List<OMR_GRDG> sellsnlist(Map<String, Object> param);

	@Select("SELECT EXAM_CD FROM TB_OCR_EXAM_MST WHERE SCHYR =#{SCHYR} AND EXAM_KIND =#{EXAM_KIND}  AND EXAM_NM =#{EXAM_NM}  ORDER BY LOAD_DTM DESC OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY  ")
	String getnewocrexamcd(Map<String, Object> param);

	@Select("SELECT BSTOR_CD FROM TB_OCR_EXAM_BSTOR_MAPPG WHERE EXAM_CD = #{EXAM_CD}")
	List<Integer> getocrexBlist(Map<String, Object> param);

	@Update("UPDATE TB_OCR_EXAM_BSTOR_MAPPG SET USE_YN = 'N' ,UPD_DTM=GETDATE() WHERE BSTOR_CD = #{BSTOR_CD} ,EXAM_CD =#{EXAM_CD},CMPN_CD=#{CMPN_CD}")
	void update_bstor_exam(Map<String, Object> param);

	@Select({"<script>",
			"SELECT EXMN_NO"
			+ "      ,STDN_NM"
			+ "	  ,MAX(STTUS) AS STTUS"
			+ "  FROM TB_OCR_EXAM_RC RC"
			+ " WHERE 1=1 "
			+ "	AND BSTOR_CD = #{BSTOR_CD}  "
			+ "	AND EXAM_CD = #{EXAM_CD} ",
			"<if test='rsearchcontent != null'> and (EXMN_NO like '%${rsearchcontent}%' or STDN_NM like '%${rsearchcontent}%' or STTUS like '%${rsearchcontent}%') </if>"
			+ " "
			+ " GROUP BY EXMN_NO"
			+ "         ,STDN_NM"
			+ " "
			+ " ORDER BY "
			+ " STTUS DESC,"
			+ " CAST(EXMN_NO AS BIGINT)"
			+ "         ,STDN_NM OFFSET #{rpageNum} ROWS FETCH NEXT #{rlimit} ROWS ONLY ",
			"</script>"})
	List<OCR_EXAM_RC> getcStuList(Map<String, Object> param);

	@Update("update TB_OCR_EXAM_BSTOR_MAPPG set USE_YN = #{USE_YN}, UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE()  where EXAM_CD = #{EXAM_CD} and CMPN_CD = #{CMPN_CD} and BSTOR_CD=#{BSTOR_CD} ")
	void examocruseyn(Map<String, Object> param);

	@Select({"<script>",
		"SELECT * FROM LOG_FILE_CHG_TXN "
			+ "	WHERE 1=1 "//,
//			"<if test='OMR_OCR_DIV == \"OCR\"'> AND CONCAT(CHG_FILE_NM,'.jpg') NOT IN ( SELECT RIGHT(OCR_IMG,CHARINDEX('/', reverse(OCR_IMG))-1) FROM TB_OCR_EXAM_RC) </if>",
//			"<if test='OMR_OCR_DIV ==\"OMR\"'> AND CONCAT(CHG_FILE_NM,'.jpg') NOT IN ( SELECT RIGHT(OMR_IMG,CHARINDEX('/', reverse(OMR_IMG))-1) FROM TB_OMR_RECOG) </if>",
			+ "		AND EXAM_CD = #{EXAM_CD} "
			+ "		AND BSTOR_CD = #{BSTOR_CD} "
			+ "		AND CMPN_CD = #{CMPN_CD} "
			+ "		AND MST_CD = #{MST_CD}  "
			+ "		AND PID = #{PID} "
			+ "		AND OMR_OCR_DIV = #{OMR_OCR_DIV} ",
			"</script>"})
	List<FILELOG> getflist(Map<String, Object> param);
			 
	@Update("update TB_OMR_GRDG set LSN_CD = #{LSN_CD},UPD_MBR_NO=#{MBR_NO},UPD_DTM=GETDATE(),CRA_YN='Z' where OMR_KEY = #{OMR_KEY}")
	void updatenolsngrdg(Map<String, Object> param);
	
	@Update("update TB_OMR_RECOG set LSN_CD = #{LSN_CD} , TOT_SC = 0 , GRDG_YN = 'N' , UPD_MBR_NO=#{MBR_NO} , UPD_DTM=GETDATE() where OMR_KEY = #{OMR_KEY}")
	void updatenolsnrecog(Map<String, Object> param);

	@Select({"<script>",
		"select * from TB_CMPN_BSTOR_MST where 1=1 AND CMPN_CD = #{CMPN_CD} ",
		"<if test='searchcontent != null'> and BSTOR_NM like '%${searchcontent}%' </if>",
		" order by BSTOR_CD OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY  ",
		"</script>"})
	List<BSTOR> getbstlist(Map<String, Object> param);
	
	@Select({"<script>",
		"select * from TB_CMPN_BSTOR_MST where 1=1 AND CMPN_CD = #{CMPN_CD} and BSTOR_CD >= 1000 ",
		"<if test='searchcontent != null'> and BSTOR_NM like '%${searchcontent}%' </if>",
		" order by BSTOR_CD OFFSET #{pageNum} ROWS FETCH NEXT #{limit} ROWS ONLY  ",
		"</script>"})
	List<BSTOR> getbst1000list(Map<String, Object> param);
	
	@Select({"<script>",
		"select count(*) from TB_CMPN_BSTOR_MST where 1=1 AND CMPN_CD = #{CMPN_CD} ",
		"<if test='searchcontent != null'> and BSTOR_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int getbstcnt(Map<String, Object> param);
	
	@Select({"<script>",
		"select count(*) from TB_CMPN_BSTOR_MST where 1=1 and BSTOR_CD >= 1000 AND CMPN_CD = #{CMPN_CD} ",
		"<if test='searchcontent != null'> and BSTOR_NM like '%${searchcontent}%' </if>",
		"</script>"})
	int getbst1000cnt(Map<String, Object> param);
	
	@Select("select COUNT(*) from LOG_FILE_CHG_TXN WHERE EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND PID = #{PID} AND OMR_OCR_DIV =#{OMR_OCR_DIV} and MST_CD=#{MST_CD} AND DATEDIFF(n,LOG_DTM,GETDATE()) < 2")
	int filecntless1minute(Map<String, Object> param);

	@Select({"<script>"
			," SELECT DATEDIFF(n,LOG_DTM,GETDATE()) " +
			"    FROM LOG_SYS_TXN " +
			"   WHERE ARG_VAL LIKE #{ARG_VAL} " +
			"     AND STEP_NM = #{STEP_NM} " +
			"     AND JOB_NM = #{JOB_NM} "
			,"    <if test='PID !=NULL'> AND PID =#{PID} </if>"
			,"  ORDER BY LOG_DTM DESC  " +
			"  OFFSET 0 ROWS " +
			"   FETCH NEXT 1 ROWS ONLY "
			,"</script>"})
	Integer pidsys(Map<String, Object> param);

	@Select("select LEFT(EXMN_NO,5) as FREQ from TB_OMR_GRDG where 1=1 and BSTOR_CD = #{BSTOR_CD} and EXAM_CD = #{EXAM_CD} group by LEFT(EXMN_NO,5) ORDER BY COUNT(*) DESC offset 0 rows fetch next 1 rows only")
	String freq(Map<String, Object> param);

	@Delete("DELETE FROM TB_OMR_EXAM_LSN_QUEI_MST WHERE EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} AND OMR_MST_CD = #{OMR_MST_CD} AND CMPN_CD = #{CMPN_CD} ")
	void deletelsnquei(Map<String, Object> param);

	
	@Update("update TB_OMR_EXAM_LSN_MST set DEL_YN = 'N' , QUEI_NUM =#{QUEI_NUM} ,TOT_DISMK=#{TOT_DISMK} ,OMR_MST_CD =#{OMR_MST_CD}, UPD_DTM = GETDATE() WHERE EXAM_CD = #{EXAM_CD} AND BSTOR_CD = #{BSTOR_CD} AND CMPN_CD = #{CMPN_CD} AND LSN_CD = #{LSN_CD}")
	void updatemlsnrow(Map<String, Object> param);

	@Select("SELECT * FROM TB_OMR_EXAM_LSN_MST WHERE EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} and CMPN_CD = #{CMPN_CD}")
	List<OMR_EXAM_LSN> getOELM_OBJ(Map<String, Object> param);

	@Select("select LSN_CD from TB_LSN_MAPPG where ECI_LSN_CD = #{ECI_LSN_CD}")
	Integer getlsncd(Integer edulcd);

	@Delete("DELETE FROM TB_OMR_EXAM_LSN_QUEI_MST WHERE EXAM_CD = #{EXAM_CD} AND LSN_CD = #{LSN_CD} AND CMPN_CD = #{CMPN_CD} AND QUEI_NO =#{QUEI_NO} ")
	void delquei(Map<String, Object> param);

	@Select("select distinct OMR_MST_CD from TB_OMR_EXAM_LSN_MST where LSN_CD = #{lSN_CD} order by OMR_MST_CD desc offset 0 rows  fetch next 1 rows only ")
	Integer getomrcdbylsnonly(String lSN_CD);



	@Select("select * from TB_OMR_RECOG where OMR_KEY = #{omrkey} ")
	OMR_RECOG getregobjt(String omrkey);

	@Update("UPDATE TB_OMR_EXAM_LSN_MST "
			+ "   SET AVG_SC = B.AVG_SC "
			+ "      ,UPD_MBR_NO = #{MBR_NO} "
			+ "	  	 ,UPD_DTM = GETDATE() "
			+ "  FROM TB_OMR_EXAM_LSN_MST A "
			+ "  	INNER JOIN ( "
			+ "				SELECT  "
			+ "					   CMPN_CD "
			+ "					  ,BSTOR_CD "
			+ "					  ,EXAM_CD "
			+ "					  ,LSN_CD "
			+ "					  ,OMR_MST_CD "
			+ "					  ,AVG(TOT_SC) AS AVG_SC "
			+ "				  FROM TB_OMR_RECOG REC "
			+ "				 WHERE 1=1  "
			+ "				   AND REC.CMPN_CD	=	#{CMPN_CD} "
			+ "				   AND REC.BSTOR_CD	=	#{BSTOR_CD} "
			+ "				   AND REC.EXAM_CD	=	#{EXAM_CD} "
			+ "				   AND REC.LSN_CD	=	#{LSN_CD} "
			+ "				   AND REC.DEL_YN = 'N' "
			+ "				 GROUP BY CMPN_CD "
			+ "						 ,BSTOR_CD "
			+ "						 ,EXAM_CD "
			+ "						 ,LSN_CD "
			+ "						 ,OMR_MST_CD "
			+ "			) B ON 1=1 "
			+ "			   AND A.CMPN_CD	= B.CMPN_CD "
			+ "			   AND A.BSTOR_CD	= B.BSTOR_CD "
			+ "			   AND A.EXAM_CD	= B.EXAM_CD "
			+ "			   AND A.LSN_CD		= B.LSN_CD "
			+ "			   AND A.OMR_MST_CD = B.OMR_MST_CD ;")
	void updateavg(Map<String, Object> param);

	@Update("UPDATE LOG_SYS_TXN "
			+ "	SET PID = #{PID} "
			+ "	where 1=1 "
			+ " 	AND  ARG_VAL like #{ARG_VAL} "
			+ "		and JOB_NM = 'TOS_CALL' "
			+ "		and ("
			+ "			DATEDIFF( "
			+ "					 n,( "
			+ "						select LOG_DTM "
			+ "							FROM LOG_FILE_CHG_TXN "
			+ "							WHERE 1=1 "
			+ "								AND EXAM_CD = #{EXAM_CD} "
			+ "								AND CMPN_CD = #{CMPN_CD} "
			+ "								AND BSTOR_CD = #{BSTOR_CD} "
			+ "								AND MST_CD = #{MST_CD} "
			+ "								AND OMR_OCR_DIV = 'OMR' "
			+ "								and PID =#{PID} "
			+ "							order by LOG_DTM "
			+ "								offset 0 rows "
			+ "								fetch next 1 rows only "
			+ "						),LOG_DTM "
			+ "					) = 0 "
			+ "			or "
			+ "			DATEDIFF( "
			+ "								 n,( "
			+ "									select LOG_DTM "
			+ "										FROM LOG_FILE_CHG_TXN "
			+ "										WHERE 1=1 "
			+ "											AND EXAM_CD = #{EXAM_CD} "
			+ "											AND CMPN_CD = #{CMPN_CD} "
			+ "											AND BSTOR_CD = #{BSTOR_CD} "
			+ "											AND MST_CD = #{MST_CD} "
			+ "											AND OMR_OCR_DIV = 'OMR' "
			+ "											and PID =#{PID} "
			+ "										order by LOG_DTM "
			+ "											offset 0 rows "
			+ "											fetch next 1 rows only "
			+ "									),LOG_DTM "
			+ "								) = 1 "
			+ "			)")
	void utc(Map<String, Object> param);

	@Select("SELECT distinct(PID) FROM LOG_FILE_CHG_TXN WHERE EXAM_CD = #{EXAM_CD} AND CMPN_CD = #{CMPN_CD} AND BSTOR_CD = #{BSTOR_CD} AND MST_CD = #{MST_CD} AND OMR_OCR_DIV = #{OMR_OCR_DIV}")
	List<String> pid3(Map<String, Object> param);

	@Select("SELECT COUNT(*) FROM TB_OMR_RECOG WHERE EXAM_CD =  #{EXAM_CD} AND BSTOR_CD = #{BSTOR_CD} AND OMR_MST_CD = #{OMR_MST_CD} AND DEL_YN = 'N' ")
	Integer reupdsttus(Map<String, Object> param);

	@Update("update TB_OMR_EXAM_LSN_MST set STTUS_CD = 1 where EXAM_CD =  #{EXAM_CD} AND BSTOR_CD = #{BSTOR_CD} AND OMR_MST_CD = #{OMR_MST_CD}")
	void update_updsttus(Map<String, Object> param);

	@Select("SELECT distinct OMR_MST_CD from TB_OMR_RECOG WHERE EXAM_CD = #{EXAM_CD} and BSTOR_CD = #{BSTOR_CD} AND (LSN_CD = -1 or LSN_CD = -2) ")
	List<Integer> getomrcdnolsn(Map<String, Object> param);

	@Update({"update TB_OMR_RECOG set DEL_YN = 'Y',UPD_MBR_NO=#{UPD_MBR_NO}, UPD_DTM = GETDATE() where OMR_KEY =#{OMR_KEY} and EXAM_CD = #{EXAM_CD} and BSTOR_CD = #{BSTOR_CD} and LSN_CD = -2"})
	void deletegradgnolsn(Map<String, Object> param);

	@Select("select * from TB_OMR_EXAM_MST where EXAM_CD = #{exam_cd}")
	OMR_EXAM OMRExamobj(String exam_cd);

	@Select("  SELECT CHG_FILE_NM "
			+ "  FROM LOG_FILE_CHG_TXN "
		    + " WHERE 1=1 "
			+ "   AND EXAM_CD = #{EXAM_CD}  "
			+ "   AND BSTOR_CD = #{BSTOR_CD} "
			+ "   AND CMPN_CD = #{CMPN_CD} "
			+ "   AND OMR_OCR_DIV = #{OMR_OCR_DIV} "
			+ "   AND MST_CD = #{MST_CD} "
			+ "   AND PID = #{PID} "
			+ " ORDER BY LOG_DTM DESC "
			+ " OFFSET 0 ROWS "
			+ " FETCH NEXT 1 ROWS ONLY ")
	String getchgfilename(Map<String, Object> param);

	@Select({"<script>",
			"  SELECT COUNT(*) " +
			"	 FROM ( " +
			"  		SELECT  " +
			"		    A.EXAM_CD     " +
			"          ,A.BSTOR_CD    " +
			"          ,B.EXAM_NM    " +
			"          ,B.SCHYR    " +
			"          ,C.BSTOR_NM   " +
			"     	   ,A.MST_CD  " +
			"          ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END AS LSN_GRP_NM    " +
			"          ,COUNT(*)  AS GRP_CNT  " +
			"    FROM  LOG_FILE_CHG_TXN A    " +
			"   	   INNER  JOIN TB_OMR_EXAM_MST B  ON A.EXAM_CD = B.EXAM_CD    " +
			"  		   INNER JOIN TB_CMPN_BSTOR_MST C ON C.BSTOR_CD = A.BSTOR_CD    " +
			"    WHERE 1=1  ",
			"      <if test='searchcontent != null'>    " +
			"      AND ( " +
			"		      B.EXAM_NM LIKE '%${searchcontent}%'    " +
			"          OR B.SCHYR LIKE '%${searchcontent}%'    " +
			"          OR C.BSTOR_NM LIKE '%${searchcontent}%'   " +
			"          ) </if> ",
			"   	   <if test='searchbstor != null '>	AND A.BSTOR_CD 	IN ${searchbstor} 	</if>",
			"   	   <if test='searchmock != null '>	AND A.EXAM_CD 	IN ${searchmock} 	</if>",
			"   	   <if test='searchgrade != null '>	AND B.SCHYR 	IN ${searchgrade} 	</if>",
			"      AND A.REUPLD_YN = 'Y'    " +
			"      AND B.USE_YN = 'Y'    " +
			"      AND B.DEL_YN = 'N'    " +
			"    GROUP BY A.EXAM_CD     " +
			"         	  ,A.BSTOR_CD    " +
			"         	  ,B.EXAM_NM    " +
			"         	  ,B.SCHYR    " +
			"         	  ,C.BSTOR_NM    " +
			"     	  	  ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END     " +
			"         	  ,A.MST_CD  " +
			" 			) AS AD",
			"</script>"})
	int REUPLD_CNT(Map<String, Object> param);

	@Select({"<script>",
			"  SELECT COUNT(*) " +
			"	 FROM ( " +
			"  		SELECT  " +
			"		    A.EXAM_CD     " +
			"          ,A.BSTOR_CD    " +
			"          ,B.EXAM_NM    " +
			"          ,B.SCHYR    " +
			"          ,C.BSTOR_NM   " +
			"     	   ,A.MST_CD  " +
			"          ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
			"        	CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END AS LSN_GRP_NM    " +
			"          ,COUNT(*)  AS GRP_CNT  " +
			"    FROM  LOG_FILE_CHG_TXN A    " +
			"   	   INNER  JOIN TB_OMR_EXAM_MST B  ON A.EXAM_CD = B.EXAM_CD    " +
			"  		   INNER JOIN TB_CMPN_BSTOR_MST C ON C.BSTOR_CD = A.BSTOR_CD    " +
			"    WHERE 1=1  ",
			"      <if test='searchcontent != null'>    " +
			"      AND ( " +
			"		      B.EXAM_NM LIKE '%${searchcontent}%'    " +
			"          OR B.SCHYR LIKE '%${searchcontent}%'    " +
			"          OR C.BSTOR_NM LIKE '%${searchcontent}%'   " +
			"          ) </if> ",
			"   	   <if test='searchbstor != null '>	AND A.BSTOR_CD 	IN ${searchbstor} 	</if>",
			"   	   <if test='searchmock != null '>	AND A.EXAM_CD 	IN ${searchmock} 	</if>",
			"   	   <if test='searchgrade != null '>	AND B.SCHYR 	IN ${searchgrade} 	</if>",
			"      AND A.REUPLD_YN = 'Y'    " +
			"      AND B.USE_YN = 'Y'    " +
			"      AND B.DEL_YN = 'N'    " +
			"	   AND A.BSTOR_CD >= 1000 " +
			"    GROUP BY A.EXAM_CD     " +
			"         	  ,A.BSTOR_CD    " +
			"         	  ,B.EXAM_NM    " +
			"         	  ,B.SCHYR    " +
			"         	  ,C.BSTOR_NM    " +
			"     	  	  ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
			"         		CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END     " +
			"         	  ,A.MST_CD  " +
			" 			) AS AD",
			"</script>"})
	int REUPLD_CNT1000(Map<String, Object> param);

	@Select({"<script>",
		"  SELECT * " +
		"	 FROM ( " +
		"  		SELECT  " +
		"		    A.EXAM_CD     " +
		"          ,A.BSTOR_CD    " +
		"          ,B.EXAM_NM    " +
		"          ,B.SCHYR    " +
		"          ,C.BSTOR_NM   " +
		"     	   ,A.MST_CD  " +
		"          ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
		"        	CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
		"        	CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
		"        	CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
		"        	CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END AS LSN_GRP_NM    " +
		"          ,COUNT(*)  AS GRP_CNT  " +
		"    FROM  LOG_FILE_CHG_TXN A    " +
		"   	   INNER  JOIN TB_OMR_EXAM_MST B  ON A.EXAM_CD = B.EXAM_CD    " +
		"  		   INNER JOIN TB_CMPN_BSTOR_MST C ON C.BSTOR_CD = A.BSTOR_CD    " +
		"    WHERE 1=1  ",
		"      <if test='searchcontent != null'>    " +
		"      AND ( " +
		"		      B.EXAM_NM LIKE '%${searchcontent}%'    " +
		"          OR B.SCHYR LIKE '%${searchcontent}%'    " +
		"          OR C.BSTOR_NM LIKE '%${searchcontent}%'   " +
		"          ) </if> ",
		"   	   <if test='searchbstor != null '>	AND A.BSTOR_CD 	IN ${searchbstor} 	</if>",
		"   	   <if test='searchmock != null '>	AND A.EXAM_CD 	IN ${searchmock} 	</if>",
		"   	   <if test='searchgrade != null '>	AND B.SCHYR 	IN ${searchgrade} 	</if>",
		"      AND A.REUPLD_YN = 'Y'    " +
		"      AND B.USE_YN = 'Y'    " +
		"      AND B.DEL_YN = 'N'    " +
		"    GROUP BY A.EXAM_CD     " +
		"         	  ,A.BSTOR_CD    " +
		"         	  ,B.EXAM_NM    " +
		"         	  ,B.SCHYR    " +
		"         	  ,C.BSTOR_NM    " +
		"     	  	  ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
		"         		CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
		"         		CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
		"         		CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
		"         		CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END     " +
		"         	  ,A.MST_CD  " +
		" 			) AS AD"
		+ "  ORDER BY AD.EXAM_CD DESC ,AD.BSTOR_CD,AD.LSN_GRP_NM "
		+ "	OFFSET #{pageNum} ROWS  "
		+ " FETCH NEXT #{limit} ROWS ONLY",
		"</script>"})
	List<FILELOG> RELPLD_LIST(Map<String, Object> param);

	@Select({"<script>",
			"  SELECT * " +
					"	 FROM ( " +
					"  		SELECT  " +
					"		    A.EXAM_CD     " +
					"          ,A.BSTOR_CD    " +
					"          ,B.EXAM_NM    " +
					"          ,B.SCHYR    " +
					"          ,C.BSTOR_NM   " +
					"     	   ,A.MST_CD  " +
					"          ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
					"        	CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
					"        	CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
					"        	CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
					"        	CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END AS LSN_GRP_NM    " +
					"          ,COUNT(*)  AS GRP_CNT  " +
					"    FROM  LOG_FILE_CHG_TXN A    " +
					"   	   INNER  JOIN TB_OMR_EXAM_MST B  ON A.EXAM_CD = B.EXAM_CD    " +
					"  		   INNER JOIN TB_CMPN_BSTOR_MST C ON C.BSTOR_CD = A.BSTOR_CD    " +
					"    WHERE 1=1  ",
			"      <if test='searchcontent != null'>    " +
					"      AND ( " +
					"		      B.EXAM_NM LIKE '%${searchcontent}%'    " +
					"          OR B.SCHYR LIKE '%${searchcontent}%'    " +
					"          OR C.BSTOR_NM LIKE '%${searchcontent}%'   " +
					"          ) </if> ",
			"   	   <if test='searchbstor != null '>	AND A.BSTOR_CD 	IN ${searchbstor} 	</if>",
			"   	   <if test='searchmock != null '>	AND A.EXAM_CD 	IN ${searchmock} 	</if>",
			"   	   <if test='searchgrade != null '>	AND B.SCHYR 	IN ${searchgrade} 	</if>",
			"      AND A.REUPLD_YN = 'Y'    " +
					"      AND B.USE_YN = 'Y'    " +
					"      AND B.DEL_YN = 'N'    " +
					"	   AND A.BSTOR_CD >= 1000 " +
					"    GROUP BY A.EXAM_CD     " +
					"         	  ,A.BSTOR_CD    " +
					"         	  ,B.EXAM_NM    " +
					"         	  ,B.SCHYR    " +
					"         	  ,C.BSTOR_NM    " +
					"     	  	  ,CASE WHEN A.MST_CD IN (6,10) THEN '국어' ELSE    " +
					"         		CASE WHEN A.MST_CD IN (7,11) THEN '수학' ELSE    " +
					"         		CASE WHEN A.MST_CD IN (3,12) THEN '영어' ELSE    " +
					"         		CASE WHEN A.MST_CD IN (8,13) THEN '한국사' ELSE    " +
					"         		CASE WHEN A.MST_CD IN (9,14) THEN '탐구' ELSE '제2외국어/한문' END END END END END     " +
					"         	  ,A.MST_CD  " +
					" 			) AS AD"
					+ "  ORDER BY AD.EXAM_CD DESC ,AD.BSTOR_CD,AD.LSN_GRP_NM "
					+ "	OFFSET #{pageNum} ROWS  "
					+ " FETCH NEXT #{limit} ROWS ONLY",
			"</script>"})
	List<FILELOG> RELPLD_LIST1000(Map<String, Object> param);

	@Select("	SELECT * "
			+ "	  FROM LOG_FILE_CHG_TXN "
			+ "  WHERE 1=1 "
			+ "	   AND CHG_FILE_NM = #{seq}")
	FILELOG SeqToImg(Integer seq);

	@Update({"<script>",
			"UPDATE LOG_FILE_CHG_TXN  "
			+ "	SET REUPLD_YN = ${REUPLD_YN} "
			+ "  WHERE 1=1 "
			+ "    AND REUPLD_YN = 'U' ",
			"      <if test='PID != null '> AND PID = ${PID} </if> ",
			"	   AND EXAM_CD = ${EXAM_CD} "
			+ "	   AND CMPN_CD = ${CMPN_CD} "
			+ "	   AND BSTOR_CD = ${BSTOR_CD} ",
			"</script>"})
	void UPDATE_REUPLD_YN(Map<String, Object> param);

	@Select(" SELECT * "
			+ "   FROM LOG_FILE_CHG_TXN  "
			+ "  WHERE 1=1 "
			+ "    AND CHG_FILE_NM IN ${SEQLIST} ")
	List<FILELOG> SeqlistToFlist(Map<String, Object> param);

	@Select(" UPDATE LOG_FILE_CHG_TXN  "
			+ "	 SET REUPLD_YN = 'N' "
			+ "WHERE 1=1 "
			+ "	 AND CHG_FILE_NM IN ${REUPLD_LIST} ")
	void REUPLD_STTUS(Map<String, Object> param);

	@Select("SELECT   MST_CD " +
			"         ,EXAM_CD " +
			"         ,CMPN_CD " +
			"         ,BSTOR_CD " +
			"     FROM " +
			"         LOG_FILE_CHG_TXN " +
			"    WHERE   1=1" +
			"      AND   REUPLD_YN = 'U' " +
			"      AND   DATEDIFF(n,LOG_DTM,GETDATE()) > 2 " +
			"   GROUP BY MST_CD " +
			"          ,EXAM_CD " +
			"          ,CMPN_CD " +
			"          ,BSTOR_CD")
	List<FILELOG> RECALL_LIST();

	@Select(" SELECT *  " +
			"   FROM TB_OCR_EXAM_RC " +
			"  WHERE 1=1 " +
			"    AND OCR_IMG = #{OCR_IMG} ")
	OCR_EXAM_RC getrcobjt(Map<String, Object> param);

	@Select("select count(DISTINCT OCR_IMG) "
			+ " from TB_OCR_EXAM_RC "
			+ " where 1=1 "
			+ " and OCR_IMG = #{OCR_IMG}")
	int reuploadocr(String OCR_IMG);

	@Select("SELECT MBR_NO " +
			"  FROM TB_MBR_MST " +
			" WHERE BSTOR_CD = #{BSTOR_CD} " +
			" ORDER BY MBR_NO ASC " +
			" OFFSET 0 ROWS " +
			"  FETCH NEXT 1 ROWS ONLY")
	Integer getmno_bst(Map<String, Object> param);

	@Select("	SELECT 	MEM.BSTOR_CD ,"
			+ " 		MEM.CMPN_CD , "
			+ "			MEM.DEL_YN,"
			+ " 		MEM.EMAIL_ADR ,"
			+ " 		MEM.JOIN_DTM ,"
			+ " 		MEM.LOAD_DTM ,"
			+ " 		MEM.MBR_MST_YN ,"
			+ " 		MEM.MBR_NO ,"
			+ " 		MEM.PWD ,"
			+ " 		MEM.TEL_NO ,"
			+ " 		MEM.UPD_DTM ,"
			+ " 		BSTOR.BSTOR_NM ,"
			+ " 		BSTOR.CMPN_NM ,"
			+ " 		PWDCOMPARE(#{PWD},PWD) as 'PWDCHK' "
			+ "    FROM	TB_CMPN_BSTOR_MST BSTOR "
			+ " 		INNER JOIN TB_MBR_MST MEM 	ON 		BSTOR.BSTOR_CD =  MEM.BSTOR_CD "
			+ "	   WHERE MBR_NO =#{MBR_NO} ")
	MBR getmbrobj(Map<String, Object> param);
   @Select(" SELECT DATEDIFF(n,LOG_DTM,GETDATE())" +
		   "   FROM LOG_FILE_CHG_TXN" +
		   "  WHERE PID = #{PID}" +
		   "  ORDER BY LOG_DTM DESC "+
		   " OFFSET 0 ROWS " +
		   "  FETCH NEXT 1 ROWS ONLY")

	Integer pidtime(Map<String, Object> param);

	@Select({"<script>",
			"SELECT	REG.OMR_KEY ,REG.LSN_CD   " +
			"      	,REG.OMR_IMG    " +
			"       ,REG.EXMN_NO    " +
			"       ,REG.STDN_NM    " +
			"       ,REG.SEX    " +
			"       ,REG.BTHDAY    " +
			"       ,REG.TOT_SC    " +
			"       ,REG.LSN_SEQ   " +
			"       ,GRDG.QUEI_NO    " +
			"       ,GRDG.MARK_NO    " +
			"       ,GRDG.ERR_YN    " +
			"       ,GRDG.CRA_YN    " +
			"       ,GRDG.OMR_MST_CD    " +
			"       ,REG.ERR_CNT    " +
			"  FROM	TB_OMR_RECOG REG WITH(NOLOCK)    " +
			"       INNER JOIN TB_OMR_GRDG GRDG ON REG.OMR_KEY = GRDG.OMR_KEY    " +
			" WHERE 1=1    " +
			"   AND REG.EXAM_CD = #{EXAM_CD}  " +
			"   AND REG.LSN_CD  = #{LSN_CD}  " +
			"	AND REG.DEL_YN = 'N' " +
			"   AND REG.BSTOR_CD= #{BSTOR_CD}   " ,
			"<if test='rsearchcontent != null'> and (REG.EXMN_NO like CONCAT('%',#{rsearchcontent},'%')  OR REG.STDN_NM like CONCAT('%',#{rsearchcontent},'%'))</if>",
			" ORDER BY	REG.OMR_MST_CD " +
			"			,REG.ERR_CNT DESC   " +
			"         	,REG.OMR_KEY   " +
			"      		,LSN_CD  " +
			"         	,GRDG.QUEI_NO","</script>"})
	List<OMR_GRDG> getfullgradlist(Map<String, Object> param);

	@Select({"<script>",
			"SELECT	REG.OMR_KEY ,REG.LSN_CD   " +
			"      	,REG.OMR_IMG    " +
			"       ,REG.EXMN_NO    " +
			"       ,REG.STDN_NM    " +
			"       ,REG.SEX    " +
			"       ,REG.BTHDAY    " +
			"       ,REG.TOT_SC    " +
			"       ,REG.LSN_SEQ   " +
			"       ,GRDG.QUEI_NO    " +
			"       ,GRDG.MARK_NO    " +
			"       ,GRDG.ERR_YN    " +
			"       ,GRDG.CRA_YN    " +
			"       ,GRDG.OMR_MST_CD    " +
			"       ,REG.ERR_CNT    " +
			"  FROM	TB_OMR_RECOG REG WITH(NOLOCK)    " +
			"       INNER JOIN TB_OMR_GRDG GRDG ON REG.OMR_KEY = GRDG.OMR_KEY    " +
			" WHERE 1=1    " +
			"   AND REG.EXAM_CD = #{EXAM_CD}  " +
			"	AND REG.DEL_YN = 'N' " +
			"   AND ( REG.LSN_CD = -1 or REG.LSN_CD = -2 )     " +
			"   AND REG.BSTOR_CD= #{BSTOR_CD}   ",
			"<if test='rsearchcontent != null'> and (REG.EXMN_NO like CONCAT('%',#{rsearchcontent},'%')  OR REG.STDN_NM like CONCAT('%',#{rsearchcontent},'%'))</if>",
			" ORDER BY	REG.OMR_MST_CD " +
			"			,REG.ERR_CNT DESC   " +
			"         	,REG.OMR_KEY   " +
			"      		,LSN_CD  " +
			"         	,GRDG.QUEI_NO",
			"</script>"})
	List<OMR_GRDG> getfullgradnolsnlist(Map<String, Object> param);

	@Select("SELECT MIN(EXAM_CD) " +
			"  FROM TB_OCR_EXAM_MST " +
			"  WHERE 1=1 " +
			"   AND OCR_MST_CD = #{OCR_CD} " +
			" 	AND USE_YN = 'Y'")
	Integer minocrcd(Map<String, Object> param);
}
