package logic;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class OCR_EXAM_RC {
	private String EXMN_NO;
	private String EXAM_NM;
	private int LSN_CD;
	private int CMPN_CD;
	private int BSTOR_CD;
	private int EXAM_CD;
	private int OCR_MST_CD;
	private String OCR_IMG;
	private String SCHOL;
	private String STDN_NM;
	private int PAGE_NO;
	private int STD_SC;
	private int PRCN_RANK;
	private int GRAD;
	private String STTUS;
	private String EXTRA_DATA;
	private Date REG_DTM;
	private Date UPD_DTM;
	private int REG_MBR_NO;
	private int UPD_MBR_NO;
	private Date LOAD_DTM;
	private String LSN_NM;
	private int DATEDIF;
	
	public String getLSN_NM() {
		return LSN_NM;
	}
	public void setLSN_NM(String lSN_NM) {
		LSN_NM = lSN_NM;
	}
	
	public String getEXTRA_DATA() {
		return EXTRA_DATA;
	}
	public void setEXTRA_DATA(String eXTRA_DATA) {
		EXTRA_DATA = eXTRA_DATA;
	}
	public String getEXMN_NO() {
		return EXMN_NO;
	}
	public void setEXMN_NO(String eXMN_NO) {
		EXMN_NO = eXMN_NO;
	}
	public int getLSN_CD() {
		return LSN_CD;
	}
	public void setLSN_CD(int lSN_CD) {
		LSN_CD = lSN_CD;
	}
	public int getCMPN_CD() {
		return CMPN_CD;
	}
	public void setCMPN_CD(int cMPN_CD) {
		CMPN_CD = cMPN_CD;
	}
	public int getBSTOR_CD() {
		return BSTOR_CD;
	}
	public void setBSTOR_CD(int bSTOR_CD) {
		BSTOR_CD = bSTOR_CD;
	}
	public int getEXAM_CD() {
		return EXAM_CD;
	}
	public void setEXAM_CD(int eXAM_CD) {
		EXAM_CD = eXAM_CD;
	}
	public int getOCR_MST_CD() {
		return OCR_MST_CD;
	}
	public void setOCR_MST_CD(int oCR_MST_CD) {
		OCR_MST_CD = oCR_MST_CD;
	}
	public String getOCR_IMG() {
		return OCR_IMG;
	}
	public void setOCR_IMG(String oCR_IMG) {
		OCR_IMG = oCR_IMG;
	}
	public String getSCHOL() {
		return SCHOL;
	}
	public void setSCHOL(String sCHOL) {
		SCHOL = sCHOL;
	}
	public String getSTDN_NM() {
		return STDN_NM;
	}
	public void setSTDN_NM(String sTDN_NM) {
		STDN_NM = sTDN_NM;
	}
	public int getPAGE_NO() {
		return PAGE_NO;
	}
	public void setPAGE_NO(int pAGE_NO) {
		PAGE_NO = pAGE_NO;
	}
	public int getSTD_SC() {
		return STD_SC;
	}
	public void setSTD_SC(int sTD_SC) {
		STD_SC = sTD_SC;
	}
	public int getPRCN_RANK() {
		return PRCN_RANK;
	}
	public void setPRCN_RANK(int pRCN_RANK) {
		PRCN_RANK = pRCN_RANK;
	}
	public int getGRAD() {
		return GRAD;
	}
	public void setGRAD(int gRAD) {
		GRAD = gRAD;
	}
	public String getSTTUS() {
		return STTUS;
	}
	public void setSTTUS(String status) {
		STTUS = status;
	}
	public Date getREG_DTM() {
		return REG_DTM;
	}
	public void setREG_DTM(Date rEG_DTM) {
		REG_DTM = rEG_DTM;
	}
	public Date getUPD_DTM() {
		return UPD_DTM;
	}
	public void setUPD_DTM(Date uPD_DTM) {
		UPD_DTM = uPD_DTM;
	}
	public int getREG_MBR_NO() {
		return REG_MBR_NO;
	}
	public void setREG_MBR_NO(int rEG_MBR_NO) {
		REG_MBR_NO = rEG_MBR_NO;
	}
	public int getUPD_MBR_NO() {
		return UPD_MBR_NO;
	}
	public void setUPD_MBR_NO(int uPD_MBR_NO) {
		UPD_MBR_NO = uPD_MBR_NO;
	}
	public Date getLOAD_DTM() {
		return LOAD_DTM;
	}
	public void setLOAD_DTM(Date lOAD_DTM) {
		LOAD_DTM = lOAD_DTM;
	}
	public int getDATEDIF() {
		return DATEDIF;
	}
	public void setDATEDIF(int dATEDIF) {
		DATEDIF = dATEDIF;
	}
	public String getEXAM_NM() {
		return EXAM_NM;
	}
	public void setEXAM_NM(String eXAM_NM) {
		EXAM_NM = eXAM_NM;
	}
	@Override
	public String toString() {
		return "OCR_EXAM_RC [EXMN_NO=" + EXMN_NO + ", EXAM_NM=" + EXAM_NM + ", LSN_CD=" + LSN_CD + ", CMPN_CD="
				+ CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", EXAM_CD=" + EXAM_CD + ", OCR_MST_CD=" + OCR_MST_CD
				+ ", OCR_IMG=" + OCR_IMG + ", SCHOL=" + SCHOL + ", STDN_NM=" + STDN_NM + ", PAGE_NO=" + PAGE_NO
				+ ", STD_SC=" + STD_SC + ", PRCN_RANK=" + PRCN_RANK + ", GRAD=" + GRAD + ", STTUS=" + STTUS
				+ ", EXTRA_DATA=" + EXTRA_DATA + ", REG_DTM=" + REG_DTM + ", UPD_DTM=" + UPD_DTM + ", REG_MBR_NO="
				+ REG_MBR_NO + ", UPD_MBR_NO=" + UPD_MBR_NO + ", LOAD_DTM=" + LOAD_DTM + ", LSN_NM=" + LSN_NM
				+ ", DATEDIF=" + DATEDIF + "]";
	}
}