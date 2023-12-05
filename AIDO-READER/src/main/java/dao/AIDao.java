package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.AIMapper;
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
import logic.SYSLOG;


@Repository
public class AIDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();

	public MBR selectOne(String ID, String PWD) {
		param.clear();
		param.put("ID", ID);
		param.put("PWD",PWD);
		List<MBR> mlist = template.getMapper(AIMapper.class).select(param); 
		if(mlist.size() != 0) {
			return mlist.get(0);	
		}else {
			return null;
		}
	}

	public List<OCR_EXAM> OcrEXAMList(Integer pageNum, int limit, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		//System.out.println(template.getMapper(AIMapper.class).ocrExamlist(param));
		return template.getMapper(AIMapper.class).ocrExamlistonly(param);
	}
	public List<OCR_EXAM> SNList(Integer pageNum, int limit, String searchcontent,Integer OCR_Cd, Integer bstor_cd) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("OCR_MST_CD", OCR_Cd);
		param.put("BSTOR_CD", bstor_cd);
		
//		System.out.println(OCR_Cd+","+limit+","+searchcontent+","+pageNum);
		return template.getMapper(AIMapper.class).ocrExamlist(param);
	}
	public List<OCR_EXAM> SNList2(Integer pageNum, int limit,Integer OCR_Cd, Integer bstor_cd) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("OCR_MST_CD", OCR_Cd);
		param.put("BSTOR_CD", bstor_cd);
		return template.getMapper(AIMapper.class).ocrExamlist2(param);
	}

	public List<OCR_EXAM_REC> OcrEXAMRec(String examCd, Integer rlimit, Integer rpageNum, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", examCd);
		param.put("rlimit", rlimit);
		param.put("rsearchcontent", rsearchcontent);
		param.put("rpageNum", (rpageNum-1)*rlimit);
		
		List<OCR_EXAM_REC> list = template.getMapper(AIMapper.class).ocrExamrec(param);
		return list;
	}

	public int examCount(String searchcontent, int i) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("OCR_MST_CD", i);
		return template.getMapper(AIMapper.class).getEXAMCount(param);
	}
	public int examCount(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).getEXAMCount(param);
	}

	public void javalog(Map<String, String> map) {
		param.clear();
		param.put("PID", map.get("PID"));
    	param.put("STEP_NM", map.get("STEP_NM"));
    	param.put("JOB_NM", map.get("JOB_NM"));
    	param.put("ARG_VAL", map.get("ARG_VAL"));
    	param.put("LOG_MSG", map.get("LOG_MSG"));
    	param.put("ST_END_DIV", map.get("ST_END_DIV"));
    	param.put("CMPLT_MSG", map.get("CMPLT_MSG"));
    	template.getMapper(AIMapper.class).javalog(param);
	}

	public int ocrcd(String exam_cd) {
		return template.getMapper(AIMapper.class).ocrcd(exam_cd);
	}

	public List<BSTOR> BSTORSN(Integer pageNum, int limit, String searchcontent, int ocrcd) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("OCR_CD", ocrcd);
		return template.getMapper(AIMapper.class).BSTORSN(param);
	}

//	public List<OCR_EXAM> SNList(int OCR_MST_CD) {
//		return template.getMapper(AIMapper.class).ocrExamlist(OCR_MST_CD);
//	}

	public int SNCOUNT(String searchcontent, int OCR_Cd, int exam_cd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("OCR_Cd", OCR_Cd);
		param.put("EXAM_CD", exam_cd);
		return template.getMapper(AIMapper.class).sncount(param);
	}

	public List<OCR_EXAM_RC> ocr_rc_sn(String EXAMNO, String examCd, String bSTOR_CD, String img) {
		param.clear();
		param.put("examCd", examCd);
		param.put("EXAMNO", EXAMNO);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("OCR_IMG", img);
		//System.out.println("examCd"+ examCd+"EXAMNO"+ EXAMNO+"BSTOR_CD"+ bSTOR_CD);
		return template.getMapper(AIMapper.class).ocr_rc_sn(param);
	}

	public List<String> ocr_rc_exam_no(String bstor_cd,int exam_Cd, Integer rlimit, String rsearchcontent, Integer rpageNum) {
		param.clear();
		param.put("BSTOR_NO", bstor_cd);
		param.put("EXAM_CD", exam_Cd);
		param.put("rlimit", rlimit);
		param.put("rsearchcontent", rsearchcontent);
		param.put("rpageNum", (rpageNum-1)*rlimit);
		return template.getMapper(AIMapper.class).ocr_rc_exam_no(param);
	}

	public List<String> ocr_rc_sn_sttus(String stdn_no, String bSTOR_CD, int examCd) {
		param.clear();
		param.put("stdn_no", stdn_no);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXAM_CD", examCd);
		List<String> stre = template.getMapper(AIMapper.class).ocr_rc_sn_sttus(param);
		//System.out.println(stre);
		return stre;
	}

	public String stdn_nm(String string) {
		return template.getMapper(AIMapper.class).stdn_nm(string).get(0);
	}

	public List<OCR_EXAM_RC> ocr_rc_sn_one(String exam_no) {
		return template.getMapper(AIMapper.class).ocr_rc_sn_one(exam_no);
	}

	public int ocr_exam_count(String examCd, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", examCd);
		param.put("rsearchcontent", rsearchcontent);
		//System.out.println("dao : "+rsearchcontent);
		int a = template.getMapper(AIMapper.class).ocr_exam_count(param);
		//System.out.println(a);
		return a;
	}
	

	public int ocr_bstor_ocrcd(String examCd) {
		return template.getMapper(AIMapper.class).ocr_bstor_ocrcd(examCd);
	}

	public void insert_bstor_exam(String examCd, Integer bstor, int oCR_Cd, int cMPN_CD) {
		param.clear();
		param.put("exam_Cd", examCd);
		param.put("bstor_Cd", bstor);
		param.put("ocr_Cd", oCR_Cd);
		param.put("cmpn_Cd", cMPN_CD);
		template.getMapper(AIMapper.class).insert_bstor_exam(param);
		
	}

	public void update_ocr_exam(Integer exam_cd, String exam_nm, String schyr, Integer exam_kind) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("EXAM_NM", exam_nm);
		param.put("SCHYR", schyr);
		String kind ="";
		if(exam_kind == 1) {
			kind = "대학수학능력시험";
		}else if(exam_kind == 2) {
			kind = "모의고사";
		}else if(exam_kind == 3) {
			kind = "학력평가";
		}
		param.put("EXAM_KIND", kind);
		template.getMapper(AIMapper.class).update_ocr_exam(param);		
	}

	public String getbstor(String bstorCd) {
		return template.getMapper(AIMapper.class).getbstor(bstorCd);
	}

	public void chgpass(int mbr_NO, String newpass) {
		param.clear();
		param.put("MBR_NO", mbr_NO);
		param.put("PWD", newpass);
		template.getMapper(AIMapper.class).chgpass(param);
		
	}

	public int OCR_SN_STU_Count(int examCd, String bSTOR_CD, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", examCd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("CONTENT", rsearchcontent);
		return template.getMapper(AIMapper.class).OCR_SN_STU_Count(param);
	}

	public MBR selectemail(String customeremail) {
		param.clear();
		param.put("EMAIL_ADR",customeremail);
		MBR a = template.getMapper(AIMapper.class).selectemail(param).get(0);
		return a;
	}

	public void updateocr(String[] strings, String eXAMNO, String examCd, String bSTOR_CD,Integer MBR_NO) {
		param.clear();
		String a = "";
		if((strings[1].equals("") || strings[1]== null) &&(strings[2]== null || strings[2].equals(""))) {
			if(strings[0].equals("34") || strings[0].equals("4")) {
				int gr = Integer.parseInt(strings[3]);
				if(gr>=1 && gr <=9) {
					a = "SUCCESS";	
				}else {
					a = "WARNING";
				}
				param.put("STTUS",a);
				param.put("EXMN_NO",eXAMNO);
				param.put("EXAM_CD",examCd);
				param.put("BSTOR_CD",bSTOR_CD);
				param.put("MBR_NO",MBR_NO);
				template.getMapper(AIMapper.class).updatesuccess(param);
			}else { 
				if(strings[3] == null || strings[3].trim().equals("")) {
					int lsncd = -1;
					strings[3] = lsncd+"";
					a = "SUCCESS";
				}else {
					a = "WARNING";
				}
				param.put("LSN_CD",strings[3]);
				param.put("STTUS",a);
				param.put("EXMN_NO",eXAMNO);
				param.put("EXAM_CD",examCd);
				param.put("BSTOR_CD",bSTOR_CD);
				param.put("MBR_NO",MBR_NO);
				//System.out.println("aS");
				template.getMapper(AIMapper.class).updatesuccess(param);
			}
		}else {
			boolean stt =sttusupdate(Integer.parseInt(strings[3]),Integer.parseInt(strings[2]));
			
			if(stt) {
				a = "SUCCESS";
				
			}else {
				a = "WARNING";
				
			}
			//System.out.println(a);
			param.put("STTUS",a);
		}
		param.put("LSN_CD",strings[0]);
		param.put("STD_SC",strings[1]);
		param.put("PRCN_RANK",strings[2]);
		param.put("GRAD",strings[3]);
		param.put("EXMN_NO",eXAMNO);
		param.put("EXAM_CD",examCd);
		param.put("BSTOR_CD",bSTOR_CD);
		param.put("MBR_NO",MBR_NO);
//		System.out.println("EXMN_NO: "+eXAMNO+" EXAM_CD: "+examCd+" BSTOR_CD : "+bSTOR_CD +" STTUS : " + a );
//		System.out.println("LSN_CD : "+strings[0]+"STD_SC : "+strings[1]+"PRCN_RANK : "+strings[2]+"GRAD : "+strings[3]);
		template.getMapper(AIMapper.class).updateocr(param);
	}

	private boolean sttusupdate(int grade, int prcn) {
		param.clear();
		param.put("TARGET_DATA",grade);
		param.put("prcn", prcn);
		String str = template.getMapper(AIMapper.class).sttusupdate(param);
//		System.out.println(str);
		boolean a = false;
		if(str.equals("O")) a =true;
//		System.out.println("a : "+ a);
		return a;
				
	}

	public int ivalue(String exam_cd, int cMPN_CD, int bSTOR_CD, String sep) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("SEP",sep);
		return template.getMapper(AIMapper.class).ivalue(param);
	}

	public void insert_filename(String exam_cd, int cMPN_CD, int bSTOR_CD, String orgfile, String pid,String sep, String mstcd, String rEUPLD_YN) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("ORGIN_FILE_NM", orgfile);
		param.put("REUPLD_YN", rEUPLD_YN);
		param.put("PID", pid);
		param.put("OMR_OCR_DIV", sep);
		param.put("MST_CD", mstcd);
		template.getMapper(AIMapper.class).insert_filename(param);
	}

	public String pid(String exam_cd, int cMPN_CD, int bSTOR_CD, String mst_cd, String sep) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("MST_CD", mst_cd);
		param.put("OMR_OCR_DIV", sep);
		if(template.getMapper(AIMapper.class).pid(param).size() != 0) {
			return template.getMapper(AIMapper.class).pid(param).get(0);
		}else {
			return null;
		}
	}

	public int filecnt(String exam_cd, int cMPN_CD, int bSTOR_CD, String omrcd, String sep, String pid1) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("MST_CD", omrcd);
		param.put("OMR_OCR_DIV", sep);
		param.put("PID", pid1);
		return template.getMapper(AIMapper.class).filecnt(param);
	}

	public List<FILELOG> floglist(String exam_cd, int cMPN_CD, int bSTOR_CD, String omr_cd, String sep,String REUPLD_YN) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("MST_CD", omr_cd);
		param.put("OMR_OCR_DIV", sep);
		param.put("REUPLD_YN", REUPLD_YN);
		System.out.println(exam_cd+","+cMPN_CD+","+bSTOR_CD+","+omr_cd+","+sep);
		return template.getMapper(AIMapper.class).floglist(param);
	}

	public int timediff(String exam_cd, int cMPN_CD, int bSTOR_CD, String omr_cd, String sep, String pid1) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("MST_CD", omr_cd);
		param.put("OMR_OCR_DIV", sep);
		param.put("PID", pid1);
		return template.getMapper(AIMapper.class).timediff(param).get(0);
	}

	public int reupload(String ocr_img) {
		param.clear();
		param.put("OCR_IMG", ocr_img);
		System.out.println(ocr_img);
		int ss =template.getMapper(AIMapper.class).reupload(param);
//		System.out.println("이미지 갯수 검색 : " + ss );
		return ss;
	}

	public List<BSTOR> BSTORWARNINGSN(Integer pageNum, Integer limit, String searchcontent, int oCR_Cd,int exam_cd) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("OCR_CD", oCR_Cd);
		param.put("EXAM_CD", exam_cd);
		return template.getMapper(AIMapper.class).bstorwarning(param);
	}

	public String getbstorcd(String bSTOR_NM) {
		param.clear();
		param.put("BSTOR_NM", bSTOR_NM);
		return template.getMapper(AIMapper.class).getbstorcd(param);
	}

	public List<ALRAM> alarmlist(int CMPN_CD, int bSTOR_CD) {
		param.clear();
		param.put("CMPN_CD", CMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		return template.getMapper(AIMapper.class).alarmlist(param);
	}

	public void regcmpnbstor(String a) {
		param.clear();
		String as [] = a.split(",");
		param.put("BSTOR_CD", as[0]);
		param.put("BSTOR_NM", as[1]);
//		System.out.println(as[0]+","+as[1]);
		template.getMapper(AIMapper.class).regcmpnbstor(param);
	}

	public void regmbrbstor(String a) {
		param.clear();
		String as [] = a.split(",");
		String email = "eci"+as[0]+"@etoos.com";
		String pwd = as[0]+"eci";
//		System.out.println(email+","+pwd);
		param.put("BSTOR_CD", as[0]);
		param.put("EMAIL_ADR", email);
		param.put("PWD", pwd);
		template.getMapper(AIMapper.class).regmbrbstor(param);
		
	}

	public String ocr_rc_snimg(String eXAMNO, String examCd, String bSTOR_CD) {
		param.clear();
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXMN_NO", eXAMNO);
		param.put("EXAM_CD", examCd);
		return template.getMapper(AIMapper.class).ocr_rc_snimg(param);
	}

	public void updateocrinfo(String eXAMNO, String examCd, String bSTOR_CD, String std_nm, String schol,String img) {
		param.clear();
		param.put("EXMN_NO", eXAMNO );
		param.put("EXAM_CD", examCd);
		param.put("BSTOR_CD", bSTOR_CD );
		param.put("STDN_NM", std_nm);
		param.put("SCHOL", schol );
		//System.out.println("Examno : "+eXAMNO+"\nexamCd : "+examCd+"\nbSTOR_CD : "+bSTOR_CD+"\nstd_nm : "+std_nm+"\nschol : "+schol+"\nimg:"+img);
		template.getMapper(AIMapper.class).updateocrinfo(param);
	}

	public int omrexamCount(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).getOMREXAMCount(param);
	}

	public List<OMR_EXAM> OMRExamsubList(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,Integer cmpncd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);	
		return template.getMapper(AIMapper.class).OMRExamsubList(param);
	}

	public int OMR_Examlsn_Count(String searchcontent, Integer bstor_cd,Integer cmpncd) { 
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getOMREXAMlsnCount(param);
	}

	public List<OMR_EXAM> OMRExamList(Integer pageNum, Integer limit, String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		return template.getMapper(AIMapper.class).OMRExamList(param);
	}

	public OMR_EXAM OMRExamList(String examCd) {
		param.clear();
		param.put("EXAM_CD", examCd);
		return template.getMapper(AIMapper.class).OMRExamList(param).get(0);
	}

	
	public void insertmexrow(String examdt, String schyr, String exam_kind, String exam_nm, Integer cmpncd, Integer bstorcd,Integer mbrno, Integer eCIEXAMCD) {
		param.clear();
		param.put("EXAM_DT", examdt);
		param.put("SCHYR", schyr);
		param.put("EXAM_KIND", exam_kind);
		param.put("EXAM_NM", exam_nm);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MBR_NO", mbrno);
		param.put("ECI_EXAM_CD", eCIEXAMCD);
		template.getMapper(AIMapper.class).insertmexrow(param);
	}

	public void insert_m_filename(String exam_cd, int cMPN_CD, int bSTOR_CD, String orgfile, String matchingfile, String pid, String omr_mst_cd) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("ORGIN_FILE_NM", orgfile);
		param.put("CHG_FILE_NM", matchingfile);
		param.put("PID", pid);
		param.put("MST_CD", omr_mst_cd);
		template.getMapper(AIMapper.class).insert_m_filename(param);
				
	}

	public List<OMR_RECOG> getomrrecog(String exam_cd, String omr_cd, Integer bSTOR_CD, Integer rpageNum, Integer rlimit, String rsearchcontent, Integer cmpncd) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("OMR_MST_CD", omr_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("CMPN_CD", cmpncd);
		param.put("rlimit", rlimit);
		param.put("rsearchcontent", rsearchcontent);
		param.put("rpageNum", (rpageNum-1)*rlimit);
		return template.getMapper(AIMapper.class).getomrrecog(param);
	}

	public int omr_recog_cnt(String exam_cd, String omr_cd, Integer bSTOR_CD, String rsearchcontent, Integer cmpncd) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("OMR_MST_CD", omr_cd);
		String bstor_cd = bSTOR_CD+"";
		param.put("BSTOR_CD", bstor_cd);
		param.put("rsearchcontent", rsearchcontent);
		param.put("CMPN_CD", cmpncd);
		//System.out.println(exam_cd+","+omr_cd+","+bstor_cd+","+rsearchcontent);
		int test =template.getMapper(AIMapper.class).omr_recog_cnt(param);
		//System.out.println(test);
		return test;
	}

	public void sttuschg(int cMPN_CD, int bSTOR_CD, String exam_cd, String oMR_CD, Integer mBR_NO, int STTUS_CD) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("OMR_MST_CD", oMR_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("CMPN_CD", cMPN_CD);
		param.put("MBR_CD", mBR_NO);
		param.put("STTUS_CD", STTUS_CD);
		template.getMapper(AIMapper.class).sttuschg(param);
		
	}

	public int OMR_Exam_GRAD_Count(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub, String searchscore) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		if(searchscore.equals("NOT")){
			param.put("searchscore",1);
		}else if(searchscore.equals("EQ")){
			param.put("searchscore",2);
		}else{
			param.put("searchscore",3);
		}
		return template.getMapper(AIMapper.class).OMR_Exam_GRAD_Count(param);	
	}
	public int OMR_Exam_GRAD_Count1000(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor,
			String searchmock, String searchgrade, String searchsub, String searchscore) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		if(searchscore.equals("NOT")){
			param.put("searchscore",1);
		}else if(searchscore.equals("EQ")){
			param.put("searchscore",2);
		}else{
			param.put("searchscore",3);
		}
		System.out.println("b");
		return template.getMapper(AIMapper.class).OMR_Exam_GRAD_Count1000(param);	
	}

	public List<OMR_RECOG> OMRExamgradList(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd, Integer cmpncd,String searchbstor,String searchmock,String searchgrade, String searchsub, String searchscore) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		if(searchscore.equals("NOT")){
			param.put("searchscore",1);
		}else if(searchscore.equals("EQ")){
			param.put("searchscore",2);
		}else{
			param.put("searchscore",3);
		}
		System.out.println("PAGE : "+(pageNum-1)*limit+", limit : "+limit+", searchcontent : "+searchcontent+", searchbstor : "+searchbstor+"" +
				", searchmock : "+searchmock+", searchgrade : "+searchgrade+", searchsub : "+searchsub+", searchscore : " +searchscore);
		return template.getMapper(AIMapper.class).OMRExamgradList(param);
	}
	
	public List<OMR_RECOG> OMRExamgradList1000(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd, Integer cmpncd,String searchbstor,String searchmock,String searchgrade, String searchsub, String searchscore) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		if(searchscore.equals("NOT")){
			param.put("searchscore",1);
		}else if(searchscore.equals("EQ")){
			param.put("searchscore",2);
		}else{
			param.put("searchscore",3);
		}
		return template.getMapper(AIMapper.class).OMRExamgradList1000(param);
	}

	public List<OMR_GRDG> stdnolist(String exam_cd, String lsn_cd, int bSTOR_CD) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		return template.getMapper(AIMapper.class).stdnolist(param);
	}
	public List<OMR_GRDG> stdnolsnlist(String exam_cd, String lsn_cd, int bSTOR_CD) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		
		param.put("BSTOR_CD", bSTOR_CD);
		return template.getMapper(AIMapper.class).stdnolsnlist(param);
	}

	public List<OMR_GRDG> getgradlist(String exam_cd, String lsn_cd, int bSTOR_CD, String stdno, Integer omr_key, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXMN_NO", stdno);
		param.put("OMR_KEY", omr_key);
		param.put("rsearchcontent", rsearchcontent);
		return template.getMapper(AIMapper.class).getgradlist(param);
	}
	
	public List<OMR_GRDG> getgradnolsnlist(String exam_cd, String lsn_cd, int bSTOR_CD, String stdno, Integer omr_key,
			String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXMN_NO", stdno);
		param.put("OMR_KEY", omr_key);
		param.put("rsearchcontent", rsearchcontent);
		return template.getMapper(AIMapper.class).getgradnolsnlist(param);
	}

	public List<OMR_GRDG> getfullgradlist(String exam_cd, String lsn_cd, int bSTOR_CD, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("rsearchcontent", rsearchcontent);
		return template.getMapper(AIMapper.class).getfullgradlist(param);
	}

	public List<OMR_GRDG> getfullgradnolsnlist(String exam_cd, String lsn_cd, int bSTOR_CD, String rsearchcontent) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("rsearchcontent", rsearchcontent);
		return template.getMapper(AIMapper.class).getfullgradnolsnlist(param);
	}

	public String omr_img(String exmnno, String examcd, String lsncd, Integer bSTOR_CD, String omrimg) {
		param.clear();
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("LSN_CD", lsncd);
		param.put("EXMN_NO", exmnno);
		param.put("EXAM_CD", examcd);
		param.put("OMR_IMG", omrimg);
		return template.getMapper(AIMapper.class).omr_img(param);
	}

	public void scoring(Integer cMPN_CD, Integer bSTOR_CD, Integer exam_cd, Integer lsn_cd, Integer mBR_NO) {
		param.clear();
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("CMPN_CD", cMPN_CD);
		param.put("LSN_CD", lsn_cd);
		param.put("UPD_MBR_NO", mBR_NO);
		param.put("EXAM_CD", exam_cd);
		template.getMapper(AIMapper.class).scoring(param);
		
	}

	public void updategrdgrec(String omrkey, Integer bstorcd, String examcd, String lsncd, String markno, Integer mbrno, String queino) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("OMR_KEY", omrkey);
		param.put("LSN_CD", lsncd);
		param.put("MBR_CD", mbrno);
		param.put("EXAM_CD", examcd);
		param.put("MARK_NO", markno);
		param.put("QUEI_NO", queino);
//		System.out.println(omrkey+","+bstorcd+","+examcd+","+lsncd+","+markno+","+mbrno+","+queino);
		template.getMapper(AIMapper.class).updategrdgrec(param);
	}

	public void deletegrdg(String OMR_KEY, Integer bstorcd, String examcd, String lsncd, Integer mbrno) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("OMR_KEY", OMR_KEY);
		param.put("LSN_CD", lsncd);
		param.put("UPD_MBR_NO", mbrno);
		param.put("EXAM_CD", examcd);
		template.getMapper(AIMapper.class).deletegradg(param);
		if(lsncd.equals("-1")) {
			template.getMapper(AIMapper.class).deletegradgnolsn(param);
		}
	}

	public int reuploadomr(String imgname) {
		param.clear();
		param.put("OMR_IMG", imgname);
		return template.getMapper(AIMapper.class).reuploadomr(param);
	}

	public String getomrfilename(String examcd, String omrcd, String filename, Integer bstorcd, Integer cmpncd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("MST_CD", omrcd);
		param.put("ORIGIN_FILE_NM", filename);
		param.put("BSTOR_CD", bstorcd);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getomrfilename(param);
	}

	public OMR_EXAM getomrexam(String examdt, String schyr, String exam_kind, String exam_nm, int cmpncd, int bstorcd,int mbrno) {
		param.clear();
		param.put("EXAM_DT", examdt);
		param.put("SCHYR", schyr);
		param.put("EXAM_KIND", exam_kind);
		param.put("EXAM_NM", exam_nm);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MBR_NO", mbrno);
		return template.getMapper(AIMapper.class).getomrexam(param).get(0);
	}

	public List<OMR_EXAM_LSN> getlsnlist(String examcd, int cmpncd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getlsnlist(param);
	}

	public void insertmlsn(OMR_EXAM_LSN lsn, int cmpncd, int bstorcd, int mbrno, int examcd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("LSN_CD", lsn.getLSN_CD());
		param.put("OMR_MST_CD", lsn.getOMR_MST_CD());
		param.put("QUEI_NUM", lsn.getQUEI_NUM());
		param.put("TOT_DISMK", lsn.getTOT_DISMK());
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).insertmlsn(param);
	}

	public List<Integer> getlsncdlist() {
		return template.getMapper(AIMapper.class).getlsncdlist();
	}

	public void insertmlsn2(int lsn, int cmpncd, int bstorcd, int mbrno, Integer examcd, int omrcd, int quei) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("LSN_CD", lsn);
		param.put("OMR_MST_CD", omrcd);
		param.put("QUEI_NUM", quei);
		param.put("TOT_DISMK", 100);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).insertmlsn(param);
	}

	public List<OMR_MST> mmstlist() {
		return template.getMapper(AIMapper.class).mmstlist();
	}

	public List<OMR_EXAM_LSN> getnewlsnlist(String examcd, int cmpncd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getnewlsnlist(param);
	}

	public void insertmlsnrow(String examcd, String lsn_cd, String quei, String omr_kind, String tot, int cmpncd,
			int bstorcd, int mbrno, Integer omrcd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("LSN_CD", lsn_cd);
		param.put("OMR_MST_CD", omrcd);
		param.put("QUEI_NUM", quei);
		param.put("TOT_DISMK", tot);
		param.put("MBR_NO", mbrno);
		param.put("OMR_MST_CD", omrcd);		
		template.getMapper(AIMapper.class).insertmlsnrow(param);
	}
	public void updatemlsnrow(String examcd, String lsn_cd, String quei, String omr_kind, String tot, int cmpncd,
			int bstorcd, int mbrno, Integer omrcd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("LSN_CD", lsn_cd);
		param.put("OMR_MST_CD", omrcd);
		param.put("QUEI_NUM", quei);
		param.put("TOT_DISMK", tot);
		param.put("MBR_NO", mbrno);
		param.put("OMR_MST_CD", omrcd);		
		template.getMapper(AIMapper.class).updatemlsnrow(param);
	}

	public Integer getomrcd(String omr_kind) {
		param.clear();
		param.put("OMR_NM", omr_kind);
		return template.getMapper(AIMapper.class).getomrcd(param);
	}

	public Integer getomrcdbylsn(String lsn_cd, String exam_cd) {
		param.clear();
		param.put("LSN_CD", lsn_cd);
		param.put("EXAM_CD", exam_cd);
		return template.getMapper(AIMapper.class).getomrcdbylsn(param);
	}

	public void insertqueimst(Integer qUEI_NO, Integer cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd,
			Integer cRA_NO, Integer dISMK, String qUE_AREA, String qUE_DTL_AREA, String qUE_TYPE, Integer mbrno) {
		param.clear();
		param.put("QUEI_NO", qUEI_NO);
		param.put("CMPN_CD", cmpncd);
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("OMR_MST_CD", omr_mst_cd);
		param.put("CRA_NO", cRA_NO);
		param.put("DISMK", dISMK);
		param.put("QUE_AREA", qUE_AREA);
		param.put("QUE_DTL_AREA", qUE_DTL_AREA);
		param.put("QUE_TYPE", qUE_TYPE);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).insertqueimst(param);
		
	}

	public List<OMR_EXAM_LSN_QUEI> getqlist(Integer cmpncd, String exam_cd, String lsn_cd) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		return template.getMapper(AIMapper.class).getqlist(param);
	}

	public OMR_EXAM_LSN_QUEI selectquei(int cmpncd, String exam_cd, String lsn_cd, Integer qUEI_NO) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("QUEI_NO", qUEI_NO);
		return template.getMapper(AIMapper.class).selectquei(param);
	}
	
	public List<Integer> getBlist() {
	    return template.getMapper(AIMapper.class).getBlist();
	  }
	  
	  public void updatequeimst(Integer qUEI_NO, int cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd, Integer cRA_NO, Integer dISMK, String qUE_AREA, String qUE_DTL_AREA, String qUE_TYPE, int mbrno) {
	    this.param.clear();
	    this.param.put("QUEI_NO", qUEI_NO);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    this.param.put("EXAM_CD", exam_cd);
	    this.param.put("LSN_CD", lsn_cd);
	    this.param.put("OMR_MST_CD", omr_mst_cd);
	    this.param.put("CRA_NO", cRA_NO);
	    this.param.put("DISMK", dISMK);
	    this.param.put("QUE_AREA", qUE_AREA);
	    this.param.put("QUE_DTL_AREA", qUE_DTL_AREA);
	    this.param.put("QUE_TYPE", qUE_TYPE);
	    this.param.put("MBR_NO", Integer.valueOf(mbrno));
	    template.getMapper(AIMapper.class).updatequeimst(this.param);
	  }
	  
	  public void update_omr_exam(String exam_cd, String exam_nm, String schyr, String exam_kind, String exam_dt, int cmpncd, int bstorcd, int mbrno, int eci_exam_cd) {
	    this.param.clear();
	    this.param.put("EXAM_CD", exam_cd);
	    this.param.put("EXAM_NM", exam_nm);
	    this.param.put("SCHYR", schyr);
	    this.param.put("EXAM_KIND", exam_kind);
	    this.param.put("EXAM_DT", exam_dt);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    this.param.put("MBR_NO", Integer.valueOf(mbrno));
	    param.put("ECI_EXAM_CD", eci_exam_cd); 
	    template.getMapper(AIMapper.class).update_omr_exam(this.param);
	  }
	  
	  public void update_omr_lsn(String exam_cd, String lsncd, String queino, String omrcd, String dismk, int cmpncd, int mbrno) {
	    this.param.clear();
	    this.param.put("EXAM_CD", exam_cd);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("QUEI_NUM", queino);
	    this.param.put("OMR_MST_CD", omrcd);
	    this.param.put("TOT_DISMK", dismk);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    this.param.put("MBR_NO", Integer.valueOf(mbrno));
	    template.getMapper(AIMapper.class).update_omr_lsn(this.param);
	  }
	  
	  public void update_omr_quei(String exam_cd, String lsncd, String queino, String cra, String qarea, String qdtlarea, String qtype, String dismk, int cmpncd, int mbrno) {
	    this.param.clear();
	    this.param.put("EXAM_CD", exam_cd);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("QUEI_NO", queino);
	    this.param.put("CRA_NO", cra);
	    this.param.put("DISMK", dismk);
	    this.param.put("QUE_AREA", qarea);
	    this.param.put("QUE_DTL_AREA", qdtlarea);
	    this.param.put("QUE_TYPE", qtype);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    this.param.put("MBR_NO", Integer.valueOf(mbrno));
	    template.getMapper(AIMapper.class).update_omr_quei(this.param);
	  }
	  
	  public List<ALRAM> omralarmlist(int CMPN_CD, int bSTOR_CD) {
	    this.param.clear();
	    this.param.put("CMPN_CD", Integer.valueOf(CMPN_CD));
	    this.param.put("BSTOR_CD", Integer.valueOf(bSTOR_CD));
	    return template.getMapper(AIMapper.class).omralarmlist(this.param);
	  }
	  
	  public OMR_GRDG getstdno(String omrimg, String lsn_cd) {
	    this.param.clear();
	    this.param.put("OMR_IMG", omrimg);
	    this.param.put("LSN_CD", lsn_cd);
	    return template.getMapper(AIMapper.class).getstdno(this.param);
	  }
	  
	  public int gettdiff(FILELOG fl) {
	    this.param.clear();
	    this.param.put("OMR_OCR_DIV", fl.getOMR_OCR_DIV());
	    this.param.put("CHG_FILE_NM", fl.getCHG_FILE_NM());
	    return template.getMapper(AIMapper.class).gettdiff(this.param).get(0);
	  }
	  
	  public OMR_RECOG getrecogobjt(String imgnm) {
	    this.param.clear();
	    this.param.put("OMR_IMG", imgnm);
	    List<OMR_RECOG> RLIST = template.getMapper(AIMapper.class).getrecogobjt(this.param);
	    if (RLIST.size() != 0)
	      return RLIST.get(0); 
	    return null;
	  }
	  
	  public int omrcount(String searchcontent) {
	    this.param.clear();
	    this.param.put("searchcontent", searchcontent);
	    return template.getMapper(AIMapper.class).omrcount(this.param);
	  }
	  
	  public List<OMR_MST> OMRList(Integer pageNum, Integer limit, String searchcontent) {
	    this.param.clear();
	    this.param.put("searchcontent", searchcontent);
	    this.param.put("pageNum", Integer.valueOf((pageNum.intValue() - 1) * limit.intValue()));
	    this.param.put("limit", limit);
	    return template.getMapper(AIMapper.class).OMRList(this.param);
	  }
	  
	  public OMR_EXAM_LSN getlsnobj(String examcd, String lsncd, int cmpncd) {
	    this.param.clear();
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    return template.getMapper(AIMapper.class).getlsnlist(this.param).get(0);
	  }
	  
	  public void dellsn(String examcd, String lsncd, int cmpncd, int mbrno) {
	    this.param.clear();
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    param.put("MBR_NO", mbrno);
	    template.getMapper(AIMapper.class).dellsn(this.param);
	  }
	  
	  public void delex(String examcd, int cmpncd, int mbrno) {
	    this.param.clear();
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    param.put("MBR_NO", mbrno);
	    template.getMapper(AIMapper.class).delex(this.param);
	  }
	  
	  public void delquei(String examcd, String lsncd, int cmpncd, String queino) {
	    this.param.clear();
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("CMPN_CD", Integer.valueOf(cmpncd));
	    this.param.put("QUEI_NO", queino);
	    template.getMapper(AIMapper.class).delquei(this.param);
	  }
	  
	  public void examuseyn(String examcd, String useyn, Integer cmpncd, Integer mbrno) {
	    this.param.clear();
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("USE_YN", useyn);
	    this.param.put("CMPN_CD", cmpncd);
	    param.put("MBR_NO", mbrno); 
	    template.getMapper(AIMapper.class).examuseyn(this.param);
	  }
	  
	  public void updategrdgstdinfo(String omrkey, Integer bstorcd, String examcd, String lsncd, String stdno, String sex, Integer mbrno, String birth, String stdnm) {
	    this.param.clear();
	    this.param.put("BSTOR_CD", bstorcd);
	    this.param.put("OMR_KEY", omrkey);
	    this.param.put("LSN_CD", lsncd);
	    this.param.put("MBR_NO", mbrno);
	    this.param.put("EXAM_CD", examcd);
	    this.param.put("STDN_NM", stdnm);
	    this.param.put("SEX", sex);
	    this.param.put("BTHDAY", birth);
	    this.param.put("EXMN_NO", stdno);
	    System.out.println(stdno+","+sex+","+birth+","+stdnm+","+bstorcd+","+omrkey+","+lsncd+","+mbrno+","+examcd);

		  template.getMapper(AIMapper.class).updategrdgstdinfo(this.param);
	    template.getMapper(AIMapper.class).updaterecogstdinfo(this.param);
	 }

	public List<BSTOR> filtblist(Integer limit, Integer pageNum, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtblist(param);
	}

	public List<OMR_EXAM> filtexamlist(Integer limit, Integer pageNum, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtexamlist(param);
	}

	public List<Integer> getbstorlist() {		
		return template.getMapper(AIMapper.class).getbstorlist();
	}

	public List<Integer> getsmocklist() {
		return template.getMapper(AIMapper.class).getsmocklist();
	}

	public List<String> getsgragelist() {
		return template.getMapper(AIMapper.class).getsgragelist();
	}

	public List<String> getssublist() {
		return template.getMapper(AIMapper.class).getssublist();
	}

	public void deletegrdgexam(String bSTOR_CD, String examcd, String lsncd, Integer mbrno) {
		param.clear();
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXAM_CD", examcd);
		param.put("MBR_NO", mbrno);
		param.put("LSN_CD", lsncd);
		template.getMapper(AIMapper.class).deletegrdgexam(param);
		if(lsncd.equals("-1")) {
			template.getMapper(AIMapper.class).DelSecGrdgExam(param);		
		}
	}

	public List<OMR_EXAM> filtglist(Integer limit, Integer pageNum, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtglist(param);
	}

	public List<OMR_RECOG> filtsubjlist(Integer limit, Integer pageNum, String searchcontent) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtsubjlist(param);
	}

	public String getrecogobjtkey(String omrkey) {
		param.clear();
		param.put("OMR_KEY", omrkey);
		return template.getMapper(AIMapper.class).getrecogobjtkey(param);
	}

	public void updategrdgerr(String omrkey, Integer bstorcd, String examcd, String lsncd, String eRRYN, Integer mbrno,String queino) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("OMR_KEY", omrkey);
		param.put("LSN_CD", lsncd);
		param.put("MBR_CD", mbrno);
		param.put("EXAM_CD", examcd);
		param.put("ERR_YN", eRRYN);
		param.put("QUEI_NO", queino);
		template.getMapper(AIMapper.class).updategrdgerr(param);
	}

	public FILELOG getfilelog(String chgfile, String exam_cd, Integer cmpncd, Integer bstorcd, String omr_cd,
			String sep) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("CMPN_CD", cmpncd);
		param.put("CHG_FILE_NM", chgfile);
		param.put("EXAM_CD", exam_cd);
		param.put("SEP", sep);
		param.put("MST_CD",omr_cd);
		return template.getMapper(AIMapper.class).getfilelog(param);
	}

	public int filtblistcnt(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtblistcnt(param);
	}

	public int filtEXAMcnt(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtEXAMcnt(param);
	}

	public int filtgcnt(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtgcnt(param);
	}

	public int filtsubjcnt(String searchcontent) {
		param.clear();
		param.put("searchcontent", searchcontent);
		return template.getMapper(AIMapper.class).filtsubjcnt(param);
	}

	public void delocrex(String examcd, Integer mbrno) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).delocrex(param);
	}

	public OCR_EXAM selectcex(String examCd) {
		param.clear();
		param.put("EXAM_CD", examCd);
		return template.getMapper(AIMapper.class).selectcex(param);
	}

	public void insertcexrow(String schyr, Integer exam_kind, String exam_nm, int mbrno) {
		param.clear();
		param.put("SCHYR", schyr);
		param.put("OCR_MST_CD", exam_kind);
		String kind ="";
		if(exam_kind == 1) {
			kind = "대학수학능력시험";
		}else if(exam_kind == 2) {
			kind = "모의고사";
		}else if(exam_kind == 3) {
			kind = "학력평가";
		}
		param.put("EXAM_KIND", kind);
		param.put("EXAM_NM", exam_nm);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).insertcexrow(param);
	}

	public List<OMR_GRDG> sellsnlist(String bstorcd, String examcd, String lsncd) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("EXAM_CD", examcd);
		param.put("LSN_CD", lsncd);
		if(lsncd.equals("-1")) {
			return template.getMapper(AIMapper.class).nolsnlist(param);
		}else {
			return template.getMapper(AIMapper.class).sellsnlist(param);
		}
	}

	public String getnewocrexamcd(String schyr, Integer exam_kind, String exam_nm) {
		param.clear();
		param.put("SCHYR",schyr);
		String kind ="";
		if(exam_kind == 1) {
			kind = "대학수학능력시험";
		}else if(exam_kind == 2) {
			kind = "모의고사";
		}else if(exam_kind == 3) {
			kind = "학력평가";
		}
		param.put("EXAM_KIND",kind);
		param.put("EXAM_NM",exam_nm);
		return template.getMapper(AIMapper.class).getnewocrexamcd(param);
	}

	public List<Integer> getocrexBlist(String examcd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		return template.getMapper(AIMapper.class).getocrexBlist(param);
	}

	public void update_bstor_exam(String examcd, Integer bstorcd, Integer cmpncd) {
		param.clear();
		param.put("EXAM_CD",examcd);
		param.put("BSTOR_CD",bstorcd);
		param.put("CMPN_CD", cmpncd);
		template.getMapper(AIMapper.class).update_bstor_exam(param);
	}

	public List<OCR_EXAM_RC> getcStuList(int examCd, String bSTOR_CD, String rsearchcontent, int rpageNum, int rlimit) {
		param.clear();
		param.put("EXAM_CD",examCd);
		param.put("BSTOR_CD",bSTOR_CD);
		param.put("rlimit", rlimit);
		param.put("rsearchcontent", rsearchcontent);
		param.put("rpageNum", (rpageNum-1)*rlimit);
		return template.getMapper(AIMapper.class).getcStuList(param);
	}

	public void examocruseyn(String examcd, String bstorcd, String useyn, Integer cmpncd, Integer mbrno) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("BSTOR_CD", bstorcd);
		param.put("USE_YN", useyn);
		param.put("CMPN_CD", cmpncd);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).examocruseyn(param);
	}

	public List<FILELOG> getflist(String examcd, Integer cmpncd, Integer bstorcd, String ocrcd, String sep, String pid1) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MST_CD", ocrcd);
		param.put("OMR_OCR_DIV", sep);
		param.put("PID", pid1);
		System.out.println(examcd+","+cmpncd+","+bstorcd+","+ocrcd+","+sep+","+pid1);
		return template.getMapper(AIMapper.class).getflist(param);
	}

	public void updatenolsn(String omrkey, String lsncd, Integer mbrno) {
		param.clear();
		param.put("OMR_KEY", omrkey);
		param.put("LSN_CD", lsncd);
		param.put("MBR_NO", mbrno);
		template.getMapper(AIMapper.class).updatenolsngrdg(param);
		System.out.println("A");
		template.getMapper(AIMapper.class).updatenolsnrecog(param);
		System.out.println("B");
	}

	public List<BSTOR> getbstlist(Integer pageNum, Integer limit, String searchcontent, int cmpncd) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
//		System.out.println(cmpncd+","+pageNum+","+limit+","+searchcontent);
		return template.getMapper(AIMapper.class).getbstlist(param);
	}
	public List<BSTOR> getbst1000list(Integer pageNum, Integer limit, String searchcontent, int cmpncd) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
//		System.out.println(cmpncd+","+pageNum+","+limit+","+searchcontent);
		return template.getMapper(AIMapper.class).getbst1000list(param);
	}

	

	public int getbstcnt(String searchcontent, int cmpncd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getbstcnt(param);
	}
	public int getbst1000cnt(String searchcontent, int cmpncd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getbst1000cnt(param);
	}
	
	public int filecntless1minute(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String omrcd, String sep,
			String pid1) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("MST_CD", omrcd);
		param.put("OMR_OCR_DIV", sep);
		param.put("PID", pid1);
		return template.getMapper(AIMapper.class).filecntless1minute(param);
	}

	public Integer pidsys(String arg_val, String job_nm, String step_nm, String pid1) {
		param.clear();
		param.put("ARG_VAL", arg_val);
		param.put("JOB_NM", job_nm);
		param.put("STEP_NM", step_nm);
		param.put("PID", pid1);
		//System.out.println(arg_val+","+job_nm+","+step_nm+","+pid1);
		return template.getMapper(AIMapper.class).pidsys(param);
	}

	public Integer pidsys2(String arg_val, String job_nm, String step_nm) {
		param.clear();
		param.put("ARG_VAL", arg_val);
		param.put("JOB_NM", job_nm);
		param.put("STEP_NM", step_nm);
		System.out.println("ww argval : "+arg_val+" ww");
		return template.getMapper(AIMapper.class).pidsys(param);
	}

	public String freq(String exam_cd, Integer bSTOR_CD, Integer omr_MST_CD) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("OMR_MST_CD", omr_MST_CD);
		return template.getMapper(AIMapper.class).freq(param);
	}

	public void deletelsnquei(int cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("EXAM_CD", exam_cd);
		param.put("LSN_CD", lsn_cd);
		param.put("OMR_MST_CD", omr_mst_cd);
		template.getMapper(AIMapper.class).deletelsnquei(param);
	}

	public List<OMR_EXAM_LSN> getOELM_OBJ(String examcd, String lsn_cd, int cmpncd) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("LSN_CD", lsn_cd);
		param.put("CMPN_CD", cmpncd);
		return template.getMapper(AIMapper.class).getOELM_OBJ(param);
	}

	public Integer getlsncd(Integer edulcd) {
		return template.getMapper(AIMapper.class).getlsncd(edulcd);
	}

	public Integer getomrcdbylsnonly(String LSN_CD) {
		return template.getMapper(AIMapper.class).getomrcdbylsnonly(LSN_CD);
	}

	public Integer getmno_bst(Integer bSTOR_CD) {
		param.clear();
		param.put("BSTOR_CD",bSTOR_CD);
//		System.out.println("B :" +bSTOR_CD);
		return template.getMapper(AIMapper.class).getmno_bst(param);
	}

	public OMR_RECOG getregobjt(String omrkey) {
		return template.getMapper(AIMapper.class).getregobjt(omrkey);
	}

	public void updateavg(OMR_RECOG regobj, Integer mbrno, String blcd) {
		param.clear();
		param.put("CMPN_CD", regobj.getCMPN_CD());
		param.put("BSTOR_CD", regobj.getBSTOR_CD());
		param.put("EXAM_CD", regobj.getEXAM_CD());
		param.put("LSN_CD", blcd);
		param.put("MBR_CD", mbrno);
		
		template.getMapper(AIMapper.class).updateavg(param);
		System.out.println("c");
	}

	public void utc(Integer excd, Integer cmpncd, Integer bstorcd, Integer omrcd, String pid, String arg_val) {
		param.clear();
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("EXAM_CD", excd);
		param.put("MST_CD", omrcd);
		param.put("PID", pid);
		param.put("ARG_VAL", arg_val);
		template.getMapper(AIMapper.class).utc(param);
	}

	public List<String> pid3(Integer excd, Integer cmpncd, Integer bstorcd, Integer omrcd, String sep) {
		param.clear();
		param.put("EXAM_CD", excd);
		param.put("CMPN_CD", cmpncd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MST_CD", omrcd);
		param.put("OMR_OCR_DIV",sep);
		return template.getMapper(AIMapper.class).pid3(param);
	}

	public Integer reupdsttus(String bSTOR_CD, String examcd, int omrmstcd) {
		param.clear();
		param.put("BSTOR_CD",bSTOR_CD);
		param.put("EXAM_CD",examcd );
		param.put("OMR_MST_CD",omrmstcd);
		return template.getMapper(AIMapper.class).reupdsttus(param);
	}

	public void update_updsttus(String bSTOR_CD, String examcd, int omrmstcd) {
		param.clear();
		param.put("BSTOR_CD",bSTOR_CD);
		param.put("EXAM_CD",examcd );
		param.put("OMR_MST_CD",omrmstcd);
		template.getMapper(AIMapper.class).update_updsttus(param);
	}

	public List<Integer> getomrcdnolsn(String bSTOR_CD, String examcd) {
		param.clear();
		param.put("BSTOR_CD", bSTOR_CD);
		param.put("EXAM_CD", examcd);
		return template.getMapper(AIMapper.class).getomrcdnolsn(param);
	}

	public OMR_EXAM OMRExamobj(String exam_cd) {
		return template.getMapper(AIMapper.class).OMRExamobj(exam_cd);
	}

	public String getchgfilename(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String mst_cd, String pid, String dIV) {
		param.clear();
		param.put("EXAM_CD",exam_cd);
		param.put("BSTOR_CD",bSTOR_CD);
		param.put("CMPN_CD",cMPN_CD);
		param.put("MST_CD",mst_cd);
		param.put("OMR_OCR_DIV",dIV);
		param.put("PID",pid);
//		System.out.println("ADD: " + exam_cd +","+bSTOR_CD+","+cMPN_CD+","+mst_cd+","+dIV+","+pid);
		return template.getMapper(AIMapper.class).getchgfilename(param);
	}

	public int REUPLD_CNT(String searchbstor, String searchcontent, String searchgrade, String searchmock,
			String searchsub, Integer bstor_cd, Integer cmpncd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		return template.getMapper(AIMapper.class).REUPLD_CNT(param);	
	}

	public int REUPLD_CNT1000(String searchbstor, String searchcontent, String searchgrade, String searchmock,
			String searchsub, Integer bstor_cd, Integer cmpncd) {
		param.clear();
		param.put("searchcontent", searchcontent);
		param.put("BSTOR_CD", bstor_cd);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		return template.getMapper(AIMapper.class).REUPLD_CNT1000(param);	
	}

	public List<FILELOG> RELPLD_LIST(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,
			Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub,
			String searchscore) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		param.put("searchscore", searchscore);
		return template.getMapper(AIMapper.class).RELPLD_LIST(param);	
	}

	public List<FILELOG> RELPLD_LIST1000(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,
			Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub,
			String searchscore) {
		param.clear();
		param.put("pageNum", (pageNum-1)*limit);
		param.put("limit", limit);
		param.put("searchcontent", searchcontent);
		param.put("CMPN_CD", cmpncd);
		param.put("searchbstor", searchbstor);
		param.put("searchmock", searchmock);
		param.put("searchgrade", searchgrade);
		param.put("searchsub", searchsub);
		param.put("searchscore", searchscore);
		return template.getMapper(AIMapper.class).RELPLD_LIST1000(param);	
	}

	public FILELOG SeqToImg(Integer seq) {
		return template.getMapper(AIMapper.class).SeqToImg(seq);
	}

	public void UPDATE_REUPLD_YN(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String pid1, String rEUPLD_YN) {
		param.clear();
		param.put("EXAM_CD", exam_cd);
		param.put("CMPN_CD", cMPN_CD);
		param.put("BSTOR_CD", bSTOR_CD);
		if(! pid1.equals("0")){
			param.put("PID", pid1);
		}
		param.put("REUPLD_YN", rEUPLD_YN);

		template.getMapper(AIMapper.class).UPDATE_REUPLD_YN(param);
	}

	public List<FILELOG> SeqlistToFlist(String seqlist) {
		param.clear();
		param.put("SEQLIST", seqlist);
		return template.getMapper(AIMapper.class).SeqlistToFlist(param);	
	}

	public void REUPLD_STTUS(String rEUPLD_LIST) {
		param.clear();
		param.put("REUPLD_LIST", rEUPLD_LIST);
		template.getMapper(AIMapper.class).REUPLD_STTUS(param);	
	}

//	public void TOSCALL_FAIL() {
//		template.getMapper(AIMapper.class).TOSCALL_FAIL();
//	}

	public List<FILELOG> RECALL_LIST() {
		return template.getMapper(AIMapper.class).RECALL_LIST();
	}

	public OCR_EXAM_RC getrcobjt(String imgnm) {
		param.clear();
		param.put("OCR_IMG",imgnm);
		return template.getMapper(AIMapper.class).getrcobjt(param);
	}

	public int reuploadocr(String OCR_IMG) {
		return template.getMapper(AIMapper.class).reuploadocr(OCR_IMG);
	}

	public List<FILELOG> floglist(String examcd, Integer cmpn_cd, Integer bstorcd, String mstcd, String sep, String REUPLD_YN, String searchcontent) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpn_cd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MST_CD", mstcd);
		param.put("OMR_OCR_DIV", sep);
		param.put("REUPLD_YN", REUPLD_YN);
		param.put("searchcontent", searchcontent);
		System.out.println(examcd+" , "+cmpn_cd+" , "+bstorcd+" , "+mstcd+" , "+sep+" , "+REUPLD_YN+" , "+searchcontent);
		return template.getMapper(AIMapper.class).floglist(param);
	}

	public List<FILELOG> floglist(String pid1,String examcd, Integer cmpn_cd, Integer bstorcd, String mstcd, String sep) {
		param.clear();
		param.put("EXAM_CD", examcd);
		param.put("CMPN_CD", cmpn_cd);
		param.put("BSTOR_CD", bstorcd);
		param.put("MST_CD", mstcd);
		param.put("OMR_OCR_DIV", sep);
		param.put("PID", pid1);
		return template.getMapper(AIMapper.class).floglist(param);
	}

	public List<OMR_GRDG> sellsnlist(String bstorcd, String examcd, String lsncd, String omrkey) {
		param.clear();
		param.put("BSTOR_CD", bstorcd);
		param.put("EXAM_CD", examcd);
		param.put("LSN_CD", lsncd);
		param.put("OMR_KEY", omrkey);
		return template.getMapper(AIMapper.class).sellsnlist(param);
	}

	public Integer pidtime(String pid1) {
		param.clear();
		param.put("PID", pid1);
		return template.getMapper(AIMapper.class).pidtime(param);
	}

	public Integer minocrcd(int ocrcd) {
		param.clear();
		param.put("OCR_CD",ocrcd);
		return  template.getMapper(AIMapper.class).minocrcd(param);
	}
}

