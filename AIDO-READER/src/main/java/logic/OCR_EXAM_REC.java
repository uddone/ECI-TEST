package logic;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class OCR_EXAM_REC {
	private int EXAM_CD;
	private int CMPN_CD;
	private int BSTOR_CD;
	private int STTUS_CD;
	private String CMPN_NM;
	private String BSTOR_NM;
	private String CD_NM;
	private Date REG_DTM;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	private String USE_YN;
	
	
	
	public String getUSE_YN() {
		return USE_YN;
	}
	public void setUSE_YN(String uSE_YN) {
		USE_YN = uSE_YN;
	}
	public int getEXAM_CD() {
		return EXAM_CD;
	}
	public void setEXAM_CD(int eXAM_CD) {
		EXAM_CD = eXAM_CD;
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
	public int getSTTUS_CD() {
		return STTUS_CD;
	}
	public void setSTTUS_CD(int sTTUS_CD) {
		STTUS_CD = sTTUS_CD;
	}
	public String getCMPN_NM() {
		return CMPN_NM;
	}
	public void setCMPN_NM(String cMPN_NM) {
		CMPN_NM = cMPN_NM;
	}
	public String getBSTOR_NM() {
		return BSTOR_NM;
	}
	public void setBSTOR_NM(String bSTOR_NM) {
		BSTOR_NM = bSTOR_NM;
	}
	public String getCD_NM() {
		return CD_NM;
	}
	public void setCD_NM(String cD_NM) {
		CD_NM = cD_NM;
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
	public Date getLOAD_DTM() {
		return LOAD_DTM;
	}
	public void setLOAD_DTM(Date lOAD_DTM) {
		LOAD_DTM = lOAD_DTM;
	}
	@Override
	public String toString() {
		return "OCR_EXAM_REC [EXAM_CD=" + EXAM_CD + ", CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", STTUS_CD="
				+ STTUS_CD + ", CMPN_NM=" + CMPN_NM + ", BSTOR_NM=" + BSTOR_NM + ", CD_NM=" + CD_NM + ", REG_DTM="
				+ REG_DTM + ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM + ", USE_YN=" + USE_YN + "]";
	}
	
}