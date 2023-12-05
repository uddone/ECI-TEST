package logic;

//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.amazonaws.services.devicefarm.model.SampleType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.AIDao;

@Service	//@Component + service(Controller와 Dao 중간 역할)
public class AIService {
	@Autowired
	private AIDao aiDao;
	
	public MBR selectOne(String ID, String PWD) {
		return aiDao.selectOne(ID,PWD);
	}

	public List<OCR_EXAM> getOcrExamList(Integer pageNum, int limit, String searchcontent) {
		return aiDao.OcrEXAMList(pageNum, limit, searchcontent);
	}

	public List<OCR_EXAM_REC> OcrExamRec(String examCd, int rlimit, int rpageNum, String rsearchcontent) {
		return aiDao.OcrEXAMRec(examCd,rlimit,rpageNum,rsearchcontent);
	}

	public void javalog(Map<String, String> map) {
		aiDao.javalog(map);
	}

	public int OCR_EXAM_Count(String searchcontent, int i) {
		return aiDao.examCount(searchcontent,i);
	}
	public int OCR_EXAM_Count(String searchcontent) {
		return aiDao.examCount(searchcontent);
	}
/*
	public int execPython(String[] command) throws java.io.IOException, InterruptedException {
	        CommandLine commandLine = CommandLine.parse(command[0]);
	        commandLine.addArgument(command[1]);
	       
	        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
	        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
	        DefaultExecutor executor = new DefaultExecutor();
	        executor.setStreamHandler(pumpStreamHandler);
	        int result = executor.execute(commandLine);
	        System.out.println("result: " + result);
	        System.out.println("output: " + outputStream.toString());
			return result;
	}*/
/*
	public List<File> conv(File file, int i, String exam_cd, int CMPN_CD, int BSTOR_CD) {
		List<File> list = new ArrayList<File>();
		try (PDDocument document = PDDocument.load(file)){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                	String filename = null;
                	char cha =(char) (i/1000+97);
                	filename = cha+"";
                	filename +=exam_cd+""+CMPN_CD+""+BSTOR_CD+"_";
                	//System.out.println(filename);
                	if(i/10 == 0) {
                		filename += "000"+i;
                	}else if(i/10 < 10) {
                		filename += "00"+i;
                	}else if(i/100 < 10) {
                		filename += "0"+i;
                	}else {
                		filename += i;
                	}
                String fileName = "C:/properties/temp/OMR/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+ filename +"_"+ page + "p.png";
                ImageIOUtil.writeImage(bim, fileName,300);
                list.add(new File(fileName));
            }
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
		return list;
	}*/

	public List<OCR_EXAM> getSNExamList(Integer pageNum, Integer limit, String searchcontent, Integer OCR_Cd, Integer bstor_cd) {
		return aiDao.SNList(pageNum, limit, searchcontent,OCR_Cd,bstor_cd);
	}

	public List<OCR_EXAM> getSNExamList2(Integer pageNum, Integer limit, Integer OCR_Cd, Integer bstor_cd) {
		return aiDao.SNList2(pageNum, limit,OCR_Cd,bstor_cd);
	}

	public int ocrcd(String exam_cd) {

		return aiDao.ocrcd(exam_cd);
	}

	public List<BSTOR> getBSTORList(Integer pageNum, int limit, String searchcontent, int ocrcd) {
		return aiDao.BSTORSN(pageNum, limit, searchcontent,ocrcd);
	}


	public int SNCount(String searchcontent, int OCR_Cd, int exam_cd) {
		return aiDao.SNCOUNT(searchcontent,OCR_Cd,exam_cd);
	}

	public List<OCR_EXAM_RC> ocr_rc_sn(String EXAMNO, String examCd, String bSTOR_CD, String img) {
		return aiDao.ocr_rc_sn(EXAMNO,examCd,bSTOR_CD,img);
	}
	public String ocr_rc_snimg(String EXAMNO, String examCd, String bSTOR_CD) {
		return aiDao.ocr_rc_snimg(EXAMNO,examCd,bSTOR_CD);
	}

	public List<String> ocr_rc_exam_no(String bstor_cd ,int exam_Cd, Integer rlimit, String rsearchcontent, Integer rpageNum) {
		return aiDao.ocr_rc_exam_no(bstor_cd,exam_Cd,rlimit,rsearchcontent,rpageNum);
	}

	public List<String> ocr_rc_sn_sttus(String stdn_no, String bSTOR_CD, int examCd) {
		return aiDao.ocr_rc_sn_sttus(stdn_no,bSTOR_CD,examCd);
	}

	public String stdn_nm(String string) {
		System.out.println("no : " + string);
		return aiDao.stdn_nm(string);
	}

	public List<OCR_EXAM_RC> ocr_rc_sn_one(String exam_no) {

		return aiDao.ocr_rc_sn_one(exam_no);
	}

	public int OCR_EXAMRec_Count(String examCd, String rsearchcontent) {
		return aiDao.ocr_exam_count(examCd,rsearchcontent);
	}

	public int ocr_bstor_ocrcd(String examCd) {

		return aiDao.ocr_bstor_ocrcd(examCd);
	}

	public void insert_bstor_exam(String examCd, Integer bstor, Integer oCR_Cd, Integer cMPN_CD) {
		aiDao.insert_bstor_exam(examCd,bstor,oCR_Cd,cMPN_CD);
		
	}

	public void update_ocr_exam(Integer exam_cd, String exam_nm, String schyr, Integer exam_kind) {
		aiDao.update_ocr_exam(exam_cd,exam_nm,schyr,exam_kind);
	}

	public String getbstor(String bstorCd) {

		return aiDao.getbstor(bstorCd);
	}

	public void chgpass(int mbr_NO, String newpass) {
		aiDao.chgpass(mbr_NO,newpass);
		
	}

	public int OCR_SN_STU_Count(int examCd, String bSTOR_CD, String rsearchcontent) {
		return aiDao.OCR_SN_STU_Count(examCd,bSTOR_CD,rsearchcontent);
	}
	public MBR selectemail(String customeremail) {

		return aiDao.selectemail(customeremail);
	}

	public void updateocr(String[] strings, String EXAMNO, String examCd, String bSTOR_CD,Integer MBR_NO) {
		aiDao.updateocr(strings,EXAMNO,examCd,bSTOR_CD,MBR_NO);
		
	}

	public int ivalue(String exam_cd, int cMPN_CD, int bSTOR_CD, String sep) {
		return aiDao.ivalue(exam_cd,cMPN_CD,bSTOR_CD,sep);
	}

	public void insert_filename(String exam_cd, int cMPN_CD, int bSTOR_CD, String orgfile, String pid,String sep, String mstcd, String REUPLD_YN) {
		aiDao.insert_filename( exam_cd,  cMPN_CD,  bSTOR_CD,  orgfile,pid,sep,mstcd,REUPLD_YN);
	}

	public String pid(String exam_cd, int cMPN_CD, int bSTOR_CD, String mst_cd, String sep) {
		return aiDao.pid( exam_cd,  cMPN_CD,  bSTOR_CD,mst_cd,sep);
	}

	public int filecnt(String exam_cd, int cMPN_CD, int bSTOR_CD, String omrcd, String sep, String pid1) {
		return aiDao.filecnt( exam_cd,  cMPN_CD,  bSTOR_CD,omrcd,sep,pid1);
	}

	public List<FILELOG> floglist(String exam_cd, int cMPN_CD, int bSTOR_CD,String omr_cd, String sep,String REUPLD_YN) {
		return aiDao.floglist(exam_cd,  cMPN_CD,  bSTOR_CD, omr_cd,sep,REUPLD_YN);
	}

	public int timediff(String exam_cd, int cMPN_CD, int bSTOR_CD, String omr_cd, String sep, String pid1) {
		return aiDao.timediff(exam_cd,  cMPN_CD,  bSTOR_CD,omr_cd,sep,pid1);
	}

	public int reupload(String ocr_img) {

		return aiDao.reupload(ocr_img);
	}
//지점별 오류갯수
	public List<BSTOR> getBSTORWANING(Integer pageNum, Integer limit, String searchcontent, int oCR_Cd,int exam_cd) {
		return aiDao.BSTORWARNINGSN(pageNum, limit, searchcontent,oCR_Cd,exam_cd);
	}

	public String getbstorcd(String bSTOR_NM) {

		return aiDao.getbstorcd(bSTOR_NM);
	}

	public List<ALRAM> alarmlist(int cMPN_NO, int bSTOR_CD) {

		return aiDao.alarmlist(cMPN_NO,bSTOR_CD);
	}
	public void regcmpnbstor(String a) {
		aiDao.regcmpnbstor(a);
		
	}
	public void regmbrbstor(String a) {

		aiDao.regmbrbstor(a);
	}

	public void updateocrinfo(String eXAMNO, String examCd, String bSTOR_CD, String std_nm, String schol,String img) {
		aiDao.updateocrinfo(eXAMNO,examCd,bSTOR_CD,std_nm,schol,img);
	}
/*	public List<OMR_EXAM_LSN> getnewlsnlist(String examcd, int cmpncd) {
	    return aiDao.getnewlsnlist(examcd, cmpncd);
	  }*/

	public int OMR_EXAM_Count(String searchcontent) {

		return aiDao.omrexamCount(searchcontent);
	}
	public List<OMR_EXAM> OMRExamsubList(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,Integer cmpncd) {
		return aiDao.OMRExamsubList(pageNum,limit,searchcontent,bstor_cd,cmpncd);
	}

	public int OMR_Examlsn_Count(String searchcontent, Integer bstor_cd,Integer cmpncd) {
		return aiDao.OMR_Examlsn_Count(searchcontent,bstor_cd,cmpncd);
	}

	public List<OMR_EXAM> OMRExamList(Integer pageNum, Integer limit, String searchcontent) {
		return aiDao.OMRExamList(pageNum,limit,searchcontent);
	}

	public OMR_EXAM selectmex(String examCd) {

		return aiDao.OMRExamList(examCd);
	}

	public void insertmexrow(String examdt, String schyr, String exam_kind, String exam_nm, Integer cmpncd, Integer bstorcd, Integer mbrno, Integer eCIEXAMCD) {
		aiDao.insertmexrow(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno,eCIEXAMCD);
	}

	public void insert_m_filename(String exam_cd, int cMPN_CD, int bSTOR_CD, String orgfile, String matchingfile,
			String pid, String omr_mst_cd) {
		aiDao.insert_m_filename( exam_cd,  cMPN_CD,  bSTOR_CD,  orgfile,matchingfile,pid,omr_mst_cd);
	}
/*
	public List<File> conve(File file, int i, String exam_cd, int CMPN_CD, int BSTOR_CD, String omr_mst_cd) {
		List<File> list = new ArrayList<File>();
		try (PDDocument document = PDDocument.load(file)){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                	String filename = null;
                	char cha =(char) (i/1000+97);
                	filename = cha+"";
                	filename +=exam_cd+""+CMPN_CD+""+BSTOR_CD+""+omr_mst_cd+"_";
                	//System.out.println(filename);
                	if(i/10 == 0) {
                		filename += "000"+i;
                	}else if(i/10 < 10) {
                		filename += "00"+i;
                	}else if(i/100 < 10) {
                		filename += "0"+i;
                	}else {
                		filename += i;
                	}
                String fileName = "C:/properties/temp/OMR/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd+"/"+ filename +"_"+ page + "p.png";
                ImageIOUtil.writeImage(bim, fileName,300);
                list.add(new File(fileName));
            }
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
		return list;
	}
*/
	public List<OMR_RECOG> getomrrecog(String exam_cd, String omr_cd, int bSTOR_CD, int rpageNum, int rlimit, String rsearchcontent, Integer cmpncd) {
		return aiDao.getomrrecog(exam_cd,omr_cd,bSTOR_CD,rpageNum,rlimit,rsearchcontent,cmpncd);
	}

	public int omr_recog_cnt(String exam_cd, String omr_cd, Integer bSTOR_CD, String rsearchcontent, Integer cmpncd) {
		return aiDao.omr_recog_cnt(exam_cd,omr_cd,bSTOR_CD,rsearchcontent,cmpncd);
	}

	public void sttuschg(int cMPN_CD, int bSTOR_CD, String exam_cd, String oMR_CD, Integer mBR_NO, int sttus) {
		aiDao.sttuschg(cMPN_CD,bSTOR_CD,exam_cd,oMR_CD,mBR_NO,sttus);
	}

	public int OMR_Exam_GRAD_Count(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub, String searchscore) {
		return aiDao.OMR_Exam_GRAD_Count(searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}
	
	public int OMR_Exam_GRAD_Count1000(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor,
			String searchmock, String searchgrade, String searchsub, String searchscore) {
		return aiDao.OMR_Exam_GRAD_Count1000(searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}


	public List<OMR_RECOG> OMRExamgradList(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd, Integer cmpncd,String searchbstor,String searchmock,String searchgrade, String searchsub, String searchscore) {
		return aiDao.OMRExamgradList(pageNum,limit,searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}
	public List<OMR_RECOG> OMRExamgradList1000(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd, Integer cmpncd,String searchbstor,String searchmock,String searchgrade, String searchsub, String searchscore) {
		return aiDao.OMRExamgradList1000(pageNum,limit,searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}

	public List<OMR_GRDG> stdnolist(String exam_cd, String lsn_cd, int bSTOR_CD) {
		if(lsn_cd.equals("-1")) {
			return aiDao.stdnolsnlist(exam_cd,lsn_cd,bSTOR_CD);
		}else {
			return aiDao.stdnolist(exam_cd,lsn_cd,bSTOR_CD);
		}
	}

	public List<OMR_GRDG> getgradlist(String exam_cd, String lsn_cd, int bSTOR_CD, String stdno, Integer omr_key, String rsearchcontent) {
		if(lsn_cd.equals("-1")) {
			return aiDao.getgradnolsnlist(exam_cd,lsn_cd,bSTOR_CD,stdno,omr_key,rsearchcontent);
		}else {
			return aiDao.getgradlist(exam_cd,lsn_cd,bSTOR_CD,stdno,omr_key,rsearchcontent);	
		}
		
	}
	public String omr_img(String exmnno, String examcd, String lsncd, Integer bSTOR_CD, String omrimg) {
		return aiDao.omr_img(exmnno,examcd,lsncd,bSTOR_CD,omrimg);
	}

	public void scoring(Integer cMPN_CD, Integer bSTOR_CD, Integer exam_cd, Integer lsn_cd, Integer mBR_NO) {
		aiDao.scoring(cMPN_CD,bSTOR_CD,exam_cd,lsn_cd,mBR_NO);
	}

	public void updategrdgrec(String omrkey, Integer bstorcd, String examcd, String lsncd, String markno, Integer mbrno, String queino) {
//		System.out.println("chga");
		aiDao.updategrdgrec(omrkey, bstorcd, examcd, lsncd, markno,mbrno,queino);
	}

	public void deletegrdg(String OMR_KEY, Integer bstorcd, String examcd, String lsncd, Integer mbrno) {
		aiDao.deletegrdg(OMR_KEY,bstorcd,examcd,lsncd,mbrno);
	}

	public int reuploadomr(String imgname) {
		return aiDao.reuploadomr(imgname);
	}

	public String getomrfilename(String examcd, String omrcd, String filename, Integer bstorcd, Integer cmpncd) {
		return aiDao.getomrfilename(examcd,omrcd,filename,bstorcd,cmpncd);
	}

	public OMR_EXAM getomrexam(String examdt, String schyr, String exam_kind, String exam_nm, int cmpncd, int bstorcd, int mbrno ) {
		return aiDao.getomrexam(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno);
	}

	public List<OMR_EXAM_LSN> getlsnlist(String examcd,int cmpncd) {

		return aiDao.getlsnlist(examcd,cmpncd);
	}

	public void insertmlsn(OMR_EXAM_LSN lsn, int cmpncd, int bstorcd, int mbrno, int examcd) {
		aiDao.insertmlsn(lsn,cmpncd,bstorcd,mbrno,examcd);
	}

	public List<Integer> getlsncdlist() {

		return aiDao.getlsncdlist();
	}
	public void insertmlsn2(int lsn, int cmpncd, int bstorcd, int mbrno, Integer examcd, int omrcd, int quei) {
		aiDao.insertmlsn2(lsn,cmpncd,bstorcd,mbrno,examcd,omrcd,quei);
	}

	public List<OMR_MST> mmstlist() {

		return aiDao.mmstlist();
	}

	public List<OMR_EXAM_LSN> getnewlsnlist(String examcd, int cmpncd) {

		return aiDao.getnewlsnlist(examcd,cmpncd);
	}

	public void insertmlsnrow(String examcd, String lsn_cd, String quei, String omr_kind, String tot, int cmpncd,
			int bstorcd, int mbrno, Integer omrcd) {
		aiDao.insertmlsnrow(examcd,lsn_cd,quei,omr_kind,tot,cmpncd,bstorcd,mbrno,omrcd);
	}
	public void updatemlsnrow(String examcd, String lsn_cd, String quei, String omr_kind, String tot, int cmpncd,
			int bstorcd, int mbrno, Integer omrcd) {
		aiDao.updatemlsnrow(examcd,lsn_cd,quei,omr_kind,tot,cmpncd,bstorcd,mbrno,omrcd);
	}
	public Integer getomrcd(String omr_kind) {

		return aiDao.getomrcd(omr_kind);
	}

	public Integer getomrcdbylsn(String lsn_cd, String exam_cd) {

		return aiDao.getomrcdbylsn(lsn_cd,exam_cd);
	}

	public void insertqueimst(Integer qUEI_NO, Integer cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd,
			Integer cRA_NO, Integer dISMK, String qUE_AREA, String qUE_DTL_AREA, String qUE_TYPE, Integer mbrno) {
		aiDao.insertqueimst(qUEI_NO,cmpncd,exam_cd,lsn_cd,omr_mst_cd,cRA_NO,dISMK,qUE_AREA,qUE_DTL_AREA,qUE_TYPE,mbrno); 
	}

	public List<OMR_EXAM_LSN_QUEI> getqlist(Integer cmpncd, String exam_cd, String lsn_cd) {
		return aiDao.getqlist(cmpncd,exam_cd,lsn_cd);
	}

	public OMR_EXAM_LSN_QUEI selectquei(int cmpncd, String exam_cd, String lsn_cd, Integer qUEI_NO) {
		return aiDao.selectquei(cmpncd,exam_cd,lsn_cd,qUEI_NO);
	}
	public List<Integer> getBlist() {

		return aiDao.getBlist();
	}
	public void updatequeimst(Integer qUEI_NO, int cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd, Integer cRA_NO, Integer dISMK, String qUE_AREA, String qUE_DTL_AREA, String qUE_TYPE, int mbrno) {
	    aiDao.updatequeimst(qUEI_NO, cmpncd, exam_cd, lsn_cd, omr_mst_cd, cRA_NO, dISMK, qUE_AREA, qUE_DTL_AREA, qUE_TYPE, mbrno);
	}
	public void update_omr_exam(String exam_cd, String exam_nm, String schyr, String exam_kind, String exam_dt, int cmpncd, int bstorcd, int mbrno, int eci_exam_cd) {
		aiDao.update_omr_exam(exam_cd, exam_nm, schyr, exam_kind, exam_dt, cmpncd, bstorcd, mbrno,eci_exam_cd);
	}
	public void update_omr_lsn(String exam_cd, String lsncd, String queino, String omrcd, String dismk, int cmpncd, int mbrno) {
	    aiDao.update_omr_lsn(exam_cd, lsncd, queino, omrcd, dismk, cmpncd, mbrno);
	}
	public void update_omr_quei(String exam_cd, String lsncd, String queino, String cra, String dismk, String qarea, String qdtlarea, String qtype, int cmpncd, int mbrno) {
	    aiDao.update_omr_quei(exam_cd, lsncd, queino, cra, qarea, qdtlarea, qtype, dismk, cmpncd, mbrno);
	}
	public List<ALRAM> omralarmlist(int CMPN_CD, int bSTOR_CD) {

		return aiDao.omralarmlist(CMPN_CD, bSTOR_CD);
	}

	public OMR_GRDG getstdno(String omrimg, String lsn_cd) {

		return aiDao.getstdno(omrimg, lsn_cd);
	}

	public int gettdiff(FILELOG fl) {
	    return aiDao.gettdiff(fl);
	}

	public OMR_RECOG getrecogobjt(String imgnm) {
	    return aiDao.getrecogobjt(imgnm);
	}
	public int OMR_Count(String searchcontent) {

		return aiDao.omrcount(searchcontent);
	}
	public List<OMR_MST> OMRList(Integer pageNum, Integer limit, String searchcontent) {
	    return aiDao.OMRList(pageNum, limit, searchcontent);
	}
	public OMR_EXAM_LSN getlsnobj(String examcd, String lsncd, int cmpncd) {
	    return aiDao.getlsnobj(examcd, lsncd, cmpncd);
	}
	public void dellsn(String examcd, String lsncd, int cmpncd, int mbrno) {

		aiDao.dellsn(examcd, lsncd, cmpncd,mbrno);
	}
	public List<File> conv(File file, int i, String exam_cd, int CMPN_CD, int BSTOR_CD) {
		List<File> list = new ArrayList<File>();
		try (PDDocument document = PDDocument.load(file)){
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page){
			//	BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				
				String fileName = "C:/properties/temp/OCR/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+ file.getName() +"_"+ page + "p.jpg";
//				System.out.println(fileName);
				//ImageIOUtil.writeImage(bim, fileName,300);
				list.add(new File(fileName));
			}
			document.close();
		} catch (IOException e){
			System.err.println("Exception while trying to create pdf document - " + e);
		}
		return list;
	}
	public List<File> conve(File file, int i, String exam_cd, int CMPN_CD, int BSTOR_CD, String omr_mst_cd) {
		List<File> list = new ArrayList<File>();
		try (PDDocument document = PDDocument.load(file)){
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page){
			//	BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				String filename = null;
				char cha =(char) (i/1000+97);
				filename = cha+"";
				filename +=exam_cd+""+CMPN_CD+""+BSTOR_CD+""+omr_mst_cd+"_";
		              	//System.out.println(filename);
				if(i/10 == 0) {
					filename += "000"+i;
				}else if(i/10 < 10) {
					filename += "00"+i;
				}else if(i/100 < 10) {
					filename += "0"+i;
				}else {
					filename += i;
				}
				String fileName = "C:/properties/temp/OMR/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd+"/"+ filename +"_"+ page + "p.jpg";
				//ImageIOUtil.writeImage(bim, fileName,300);
				list.add(new File(fileName));
			}
			document.close();
		}catch (IOException e){
			System.err.println("Exception while trying to create pdf document - " + e);
		}
		System.out.println(list.toString());

		return list;
	}
	public void delex(String examcd, int cmpncd, int mbrno) {

		aiDao.delex(examcd, cmpncd,mbrno);
	}
	public void delquei(String examcd, String lsncd, int cmpncd, String queino) {
		aiDao.delquei(examcd, lsncd, cmpncd, queino);
	}
	public void examuseyn(String examcd, String useyn, Integer cmpncd, Integer mbrno) {
	    aiDao.examuseyn(examcd, useyn, cmpncd,mbrno);
	}
	public void updategrdgstdinfo(String omrkey, Integer bstorcd, String examcd, String lsncd, String stdno, String sex, Integer mbrno, String birth, String stdnm) {
		if(stdno.trim().equals("")) {
			stdno = "-1";
		}
		if(sex.trim().equals("")) {
			sex = "-1";
		}
		if(birth.trim().equals("")) {
			birth = "-1";
		}
		if(stdnm.trim().equals("")) {
			stdnm = "-1";
		}
		aiDao.updategrdgstdinfo(omrkey, bstorcd, examcd, lsncd, stdno, sex, mbrno,birth,stdnm);
	}

	public List<BSTOR> filtblist(Integer limit, Integer pageNum, String searchcontent) {
		return aiDao.filtblist(limit,pageNum,searchcontent);
	}

	public List<OMR_EXAM> filtEXAMlist(Integer limit, Integer pageNum, String searchcontent) {
		return aiDao.filtexamlist(limit,pageNum,searchcontent);
	}

	public List<Integer> getbstorlist() {

		return aiDao.getbstorlist();
	}

	public List<Integer> getsmocklist() {

		return aiDao.getsmocklist();
	}

	public List<String> getsgragelist() {

		return aiDao.getsgragelist();
	}

	public List<String> getssublist() {

		return aiDao.getssublist();
	}

	public void deletegrdgexam(String bSTOR_CD, String examcd, String lsncd, Integer mbrno) {
		aiDao.deletegrdgexam(bSTOR_CD,examcd,lsncd,mbrno);
	}

	public List<OMR_EXAM> filtglist(Integer limit, Integer pageNum, String searchcontent) {
		return aiDao.filtglist(limit,pageNum,searchcontent);
	}

	public List<OMR_RECOG> filtsubjlist(Integer limit, Integer pageNum, String searchcontent) {
		return aiDao.filtsubjlist(limit,pageNum,searchcontent);
	}

	public String getrecogobjtkey(String omrkey) {
		return aiDao.getrecogobjtkey(omrkey);
	}

	public void updategrdgerr(String omrkey, Integer bstorcd, String examcd, String lsncd, String ERRYN, Integer mbrno, String queino) {
//		System.out.println("chgb");
		aiDao.updategrdgerr(omrkey, bstorcd, examcd, lsncd, ERRYN,mbrno,queino);
	}

	public FILELOG getfilelog(String chgfile, String exam_cd, Integer cmpncd, Integer bstorcd, String omr_cd, String sep) {
		return aiDao.getfilelog( chgfile,  exam_cd,  cmpncd,  bstorcd,  omr_cd,  sep) ;
	}

	public int filtblistcnt(String searchcontent) {

		return aiDao.filtblistcnt(searchcontent);
	}

	public int filtEXAMcnt(String searchcontent) {

		return aiDao.filtEXAMcnt(searchcontent);
	}

	public int filtgcnt(String searchcontent) {

		return aiDao.filtgcnt(searchcontent);
	}

	public int filtsubjcnt(String searchcontent) {

		return aiDao.filtsubjcnt(searchcontent);
	}

	public void delocrex(String examcd, Integer mbrno) {

		aiDao.delocrex(examcd,mbrno);
	}
//ocr 시험 정보
	public OCR_EXAM selectcex(String examCd) {

		return aiDao.selectcex(examCd);
	}

	public void insertcexrow(String schyr, Integer exam_kind, String exam_nm, int mbrno) {
		aiDao.insertcexrow(schyr,exam_kind,exam_nm,mbrno);
	}

	public List<OMR_GRDG> sellsnlist(String bstorcd, String examcd, String lsncd) {
		return aiDao.sellsnlist(bstorcd,examcd,lsncd);
	}

	public String getnewocrexamcd(String schyr, Integer exam_kind, String exam_nm) {
		return aiDao.getnewocrexamcd(schyr,exam_kind,exam_nm);
	}

	public List<Integer> getocrexBlist(String examcd) {
		return aiDao.getocrexBlist(examcd);
	}

	public void update_bstor_exam(String examcd, Integer bstorcd, Integer cmpncd) {
		 aiDao.update_bstor_exam(examcd,bstorcd,cmpncd);
	}

	public List<OCR_EXAM_RC> getcStuList(int examCd, String bSTOR_CD, String rsearchcontent, int rpageNum, int rlimit) {
		return aiDao.getcStuList(examCd,bSTOR_CD,rsearchcontent,rpageNum,rlimit);
	}

	public void examocruseyn(String examcd, String bstorcd, String useyn, Integer cmpncd, Integer mbrno) {
		aiDao.examocruseyn(examcd,bstorcd,useyn, cmpncd,mbrno);
	}

	public List<FILELOG> getflist(String examcd, Integer cmpncd, Integer bstorcd, String ocrcd, String sep, String pid1) {
		return aiDao.getflist(examcd,cmpncd,bstorcd,ocrcd,sep,pid1);
	}

	public void updatenolsn(String omrkey, String lsncd, Integer mbrno) {
		aiDao.updatenolsn(omrkey,lsncd,mbrno) ;
	}

	public List<BSTOR> getbstlist(Integer pageNum, Integer limit, String searchcontent, int cmpncd) {
		return aiDao.getbstlist(pageNum, limit, searchcontent,cmpncd);
	}

	public List<BSTOR> getbst1000list(Integer pageNum, Integer limit, String searchcontent, int cmpncd) {
		return aiDao.getbst1000list(pageNum, limit, searchcontent,cmpncd);
	}
	
	public int getbstcnt(String searchcontent, int cmpncd) {
		return aiDao.getbstcnt(searchcontent,cmpncd);
	}
	public int getbst1000cnt(String searchcontent, int cmpncd) {
		return aiDao.getbst1000cnt(searchcontent,cmpncd);
	}
	
	public int filecntless1minute(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String omr_mst_cd, String sep,
			String pid1) {
		return aiDao.filecntless1minute( exam_cd,  cMPN_CD,  bSTOR_CD,omr_mst_cd,sep,pid1);
	}

	public Integer pidsys(String arg_val, String job_nm, String step_nm, String pid1) {
		return aiDao.pidsys(arg_val,job_nm,step_nm,pid1);
	}

	public Integer pidsys2(String arg_val, String job_nm, String step_nm) {
		return aiDao.pidsys2(arg_val,job_nm,step_nm);
	}

	public String freq(String exam_cd, Integer bSTOR_CD, Integer omr_MST_CD) {
		return aiDao.freq(exam_cd,bSTOR_CD,omr_MST_CD);
	}

	public void deletelsnquei(int cmpncd, String exam_cd, String lsn_cd, Integer omr_mst_cd) {
		aiDao.deletelsnquei(cmpncd,exam_cd,lsn_cd,omr_mst_cd);
	}

	public List<OMR_EXAM_LSN> getOELM_OBJ(String examcd, String lsn_cd, int cmpncd) {
		return aiDao.getOELM_OBJ(examcd,lsn_cd,cmpncd);
	}

	public Integer getlsncd(Integer edulcd) {
		return aiDao.getlsncd(edulcd);
	}

	public Integer getomrcdbylsnonly(String lcd) {
		return aiDao.getomrcdbylsnonly(lcd);
	}

	public Integer getmno_bst(Integer bSTOR_CD) {
		return aiDao.getmno_bst(bSTOR_CD);
	}

	public OMR_RECOG getregobjt(String omrkey) {
		return aiDao.getregobjt(omrkey);
	}

	public void updateavg(OMR_RECOG regobj, Integer mbrno, String blcd) {
		aiDao.updateavg(regobj,mbrno,blcd);
	}

	public void utc(Integer excd, Integer cmpncd, Integer bstorcd, Integer omrcd, String pid, String arg_val) {
		aiDao.utc(excd,cmpncd,bstorcd,omrcd,pid,arg_val); 
	}

	public List<String> pid3(Integer excd, Integer cmpncd, Integer bstorcd, Integer omrcd, String sep) {
		return aiDao.pid3(excd,cmpncd,bstorcd,omrcd,sep);
	}

	public Integer reupdsttus(String bSTOR_CD, String examcd, int omrmstcd) {
		return aiDao.reupdsttus(bSTOR_CD,examcd,omrmstcd);
	}

	public void update_updsttus(String bSTOR_CD, String examcd, int omrmstcd) {
		aiDao.update_updsttus(bSTOR_CD,examcd,omrmstcd);
	}

	public List<Integer> getomrcdnolsn(String bSTOR_CD, String examcd) {
		return aiDao.getomrcdnolsn(bSTOR_CD,examcd);
	}

	public OMR_EXAM OMRExamobj(String exam_cd) {
		return aiDao.OMRExamobj(exam_cd);
	}

	public String getchgfilename(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String mst_cd, String pid, String DIV) {
		return aiDao.getchgfilename(exam_cd,cMPN_CD,bSTOR_CD,mst_cd,pid,DIV);
	}

	public int REUPLD_CNT(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor, String searchmock,
			String searchgrade, String searchsub) {
		return aiDao.REUPLD_CNT(searchbstor,searchcontent,searchgrade,searchmock,searchsub,bstor_cd,cmpncd);
	}

	public int REUPLD_CNT1000(String searchcontent, Integer bstor_cd, Integer cmpncd, String searchbstor,
			String searchmock, String searchgrade, String searchsub) {
		return aiDao.REUPLD_CNT1000(searchbstor,searchcontent,searchgrade,searchmock,searchsub,bstor_cd,cmpncd);
	}

	public List<FILELOG> RELPLD_LIST(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,
			Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub,
			String searchscore) {
		return aiDao.RELPLD_LIST(pageNum,limit,searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}

	public List<FILELOG> RELPLD_LIST1000(Integer pageNum, Integer limit, String searchcontent, Integer bstor_cd,
			Integer cmpncd, String searchbstor, String searchmock, String searchgrade, String searchsub,
			String searchscore) {
		return aiDao.RELPLD_LIST1000(pageNum,limit,searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
	}

	public FILELOG SeqToImg(Integer seq) {
		return aiDao.SeqToImg(seq);
	}

	public void UPDATE_REUPLD_YN(String exam_cd, int cMPN_CD, Integer bSTOR_CD, String pid1, String REUPLD_YN) {
		aiDao.UPDATE_REUPLD_YN(exam_cd,cMPN_CD,bSTOR_CD,pid1,REUPLD_YN);
	}

	public List<FILELOG> SeqlistToFlist(String seqlist) {
		return aiDao.SeqlistToFlist(seqlist);
	}

	public void REUPLD_STTUS(String rEUPLD_LIST) {
		aiDao.REUPLD_STTUS(rEUPLD_LIST);
	}

//	public void TOSCALL_FAIL() {
//		aiDao.TOSCALL_FAIL();
//	}

	public List<FILELOG> RECALL_LIST() {
		return aiDao.RECALL_LIST();
	}

	public OCR_EXAM_RC getrcobjt(String imgnm) {
		return aiDao.getrcobjt(imgnm);
	}

	public int reuploadocr(String OCR_IMG) {
		return aiDao.reuploadocr(OCR_IMG);
	}

	public List<FILELOG> floglist(String examcd, Integer cmpn_cd, Integer bstorcd, String mstcd, String sep, String REUPLD_YN, String searchcontent) {
		return aiDao.floglist(examcd, cmpn_cd, bstorcd, mstcd, sep, REUPLD_YN, searchcontent);
	}
	public List<FILELOG> floglist(String pid1,String examcd, Integer cmpn_cd, Integer bstorcd, String mstcd, String sep) {
		return aiDao.floglist(pid1, examcd, cmpn_cd, bstorcd, mstcd, sep);
	}

	public List<OMR_GRDG> sellsnlist(String bstorcd, String examcd, String lsncd, String omrkey) {
		return aiDao.sellsnlist(bstorcd,examcd,lsncd,omrkey);
	}
	public Integer pidtime(String pid1) {
		return aiDao.pidtime(pid1);
	}


	public List<OMR_GRDG> getfullgradlist(String exam_cd, String lsn_cd, int bSTOR_CD, String rsearchcontent) {
		if(lsn_cd.equals("-1")) {
			return aiDao.getfullgradnolsnlist(exam_cd,lsn_cd,bSTOR_CD,rsearchcontent);
		}else {
			return aiDao.getfullgradlist(exam_cd,lsn_cd,bSTOR_CD,rsearchcontent);
		}

	}

    public Integer minocrcd(int ocrcd) {
		return aiDao.minocrcd(ocrcd);
    }
}