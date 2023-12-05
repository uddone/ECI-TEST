package logic;

import java.util.Date;

public class OMR_EXAM {
	private int CMPN_CD;
	private int BSTOR_CD;
	private int EXAM_CD;
	private int LSN_CD;
	private int OMR_MST_CD;
	private String LSN_NM;
	private String EXAM_NM;
	private String EXAM_DT;
	private String SCHYR;
	private String EXAM_KIND;
	private String CD_NM;
	private String OMR_NM;
	private int ECI_EXAM_CD;
	private String USE_YN;
	
	
	public int getECI_EXAM_CD() {
		return ECI_EXAM_CD;
	}
	public void setECI_EXAM_CD(int eCI_EXAM_CD) {
		ECI_EXAM_CD = eCI_EXAM_CD;
	}
	public String getUSE_YN() {
		return USE_YN;
	}
	public void setUSE_YN(String uSE_YN) {
		USE_YN = uSE_YN;
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
	public int getLSN_CD() {
		return LSN_CD;
	}
	public void setLSN_CD(int lSN_CD) {
		LSN_CD = lSN_CD;
	}
	public int getOMR_MST_CD() {
		return OMR_MST_CD;
	}
	public void setOMR_MST_CD(int oMR_MST_CD) {
		OMR_MST_CD = oMR_MST_CD;
	}
	public String getLSN_NM() {
		return LSN_NM;
	}
	public void setLSN_NM(String lSN_NM) {
		LSN_NM = lSN_NM;
	}
	public String getEXAM_NM() {
		return EXAM_NM;
	}
	public void setEXAM_NM(String eXAM_NM) {
		EXAM_NM = eXAM_NM;
	}
	public String getEXAM_DT() {
		return EXAM_DT;
	}
	public void setEXAM_DT(String eXAM_DT) {
		EXAM_DT = eXAM_DT;
	}
	public String getSCHYR() {
		return SCHYR;
	}
	public void setSCHYR(String sCHYR) {
		SCHYR = sCHYR;
	}
	public String getEXAM_KIND() {
		return EXAM_KIND;
	}
	public void setEXAM_KIND(String eXAM_KIND) {
		EXAM_KIND = eXAM_KIND;
	}
	public String getCD_NM() {
		return CD_NM;
	}
	public void setCD_NM(String cD_NM) {
		CD_NM = cD_NM;
	}
	public String getOMR_NM() {
		return OMR_NM;
	}
	public void setOMR_NM(String oMR_NM) {
		OMR_NM = oMR_NM;
	}
	@Override
	public String toString() {
		return "OMR_EXAM [CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", EXAM_CD=" + EXAM_CD + ", LSN_CD=" + LSN_CD
				+ ", OMR_MST_CD=" + OMR_MST_CD + ", LSN_NM=" + LSN_NM + ", EXAM_NM=" + EXAM_NM + ", EXAM_DT=" + EXAM_DT
				+ ", SCHYR=" + SCHYR + ", EXAM_KIND=" + EXAM_KIND + ", CD_NM=" + CD_NM + ", OMR_NM=" + OMR_NM
				+ ", ECI_EXAM_CD=" + ECI_EXAM_CD + ", USE_YN=" + USE_YN + "]";
	}
	
}