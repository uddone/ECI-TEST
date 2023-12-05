package logic;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class OCR_EXAM {
	private int EXAM_CD;
	private int CMPN_CD;
	private String EXAM_NM;
	private String SCHYR;
	private String EXAM_KIND;
	private String DEL_YN;
	private int REG_MBR_NO;
	private int UPD_MBR_NO;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	
	public int getCMPN_CD() {
		return CMPN_CD;
	}
	public void setCMPN_CD(int cMPN_CD) {
		CMPN_CD = cMPN_CD;
	}
	
	public String getSCHYR() {
		return SCHYR;
	}
	public void setSCHYR(String sCHYR) {
		SCHYR = sCHYR;
	}
	
	public String getDEL_YN() {
		return DEL_YN;
	}
	public void setDEL_YN(String dEL_YN) {
		DEL_YN = dEL_YN;
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
	public Date getUPD_DTM() {
		return UPD_DTM;
	}
	public void setUPD_DTM(Date uPD_DTM) {
		UPD_DTM = uPD_DTM;
	}
	public Date getLOAD_DTM() {
		return LOAD_DTM;
	}
	public void setLOAD_DTM(Date lOAD_DTM) {
		LOAD_DTM = lOAD_DTM;
	}
	public int getEXAM_CD() {
		return EXAM_CD;
	}
	public void setEXAM_CD(int eXAM_CD) {
		EXAM_CD = eXAM_CD;
	}
	public String getEXAM_NM() {
		return EXAM_NM;
	}
	public void setEXAM_NM(String eXAM_NM) {
		EXAM_NM = eXAM_NM;
	}
	public String getEXAM_KIND() {
		return EXAM_KIND;
	}
	public void setEXAM_KIND(String eXAM_KIND) {
		EXAM_KIND = eXAM_KIND;
	}
	@Override
	public String toString() {
		return "OCR_EXAM [EXAM_CD=" + EXAM_CD + ", CMPN_CD=" + CMPN_CD + ", EXAM_NM=" + EXAM_NM + ", SCHYR=" + SCHYR
				+ ", EXAM_KIND=" + EXAM_KIND + ", DEL_YN=" + DEL_YN + ", REG_MBR_NO=" + REG_MBR_NO + ", UPD_MBR_NO="
				+ UPD_MBR_NO + ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM + "]";
	}
		
}