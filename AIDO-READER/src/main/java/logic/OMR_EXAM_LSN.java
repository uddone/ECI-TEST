package logic;

import java.util.Date;

public class OMR_EXAM_LSN {
	private int CMPN_CD;
	private int BSTOR_CD;
	private int EXAM_CD;
	private int ECI_LSN_CD;
	private int LSN_CD;
	private int OMR_MST_CD;
	private String LSN_NM;
	private String OMR_NM;
	private int OMR_RECOG_CNT;
	private int STTUS_CD;
	private int QUEI_NUM;
	private int TOT_DISMK;
	private float AVG_SC;
	private String USE_YN;
	private String DEL_YN;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	
	
	public String getOMR_NM() {
		return OMR_NM;
	}
	public void setOMR_NM(String oMR_NM) {
		OMR_NM = oMR_NM;
	}
	
	public int getECI_LSN_CD() {
		return ECI_LSN_CD;
	}
	public void setECI_LSN_CD(int eCI_LSN_CD) {
		ECI_LSN_CD = eCI_LSN_CD;
	}
	public String getLSN_NM() {
		return LSN_NM;
	}
	public void setLSN_NM(String lSN_NM) {
		LSN_NM = lSN_NM;
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
	public int getOMR_RECOG_CNT() {
		return OMR_RECOG_CNT;
	}
	public void setOMR_RECOG_CNT(int oMR_RECOG_CNT) {
		OMR_RECOG_CNT = oMR_RECOG_CNT;
	}
	public int getSTTUS_CD() {
		return STTUS_CD;
	}
	public void setSTTUS_CD(int sTTUS_CD) {
		STTUS_CD = sTTUS_CD;
	}
	public int getQUEI_NUM() {
		return QUEI_NUM;
	}
	public void setQUEI_NUM(int qUEI_NUM) {
		QUEI_NUM = qUEI_NUM;
	}
	public int getTOT_DISMK() {
		return TOT_DISMK;
	}
	public void setTOT_DISMK(int tOT_DISMK) {
		TOT_DISMK = tOT_DISMK;
	}
	public float getAVG_SC() {
		return AVG_SC;
	}
	public void setAVG_SC(float aVG_SC) {
		AVG_SC = aVG_SC;
	}
	public String getUSE_YN() {
		return USE_YN;
	}
	public void setUSE_YN(String uSE_YN) {
		USE_YN = uSE_YN;
	}
	public String getDEL_YN() {
		return DEL_YN;
	}
	public void setDEL_YN(String dEL_YN) {
		DEL_YN = dEL_YN;
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
		return "OMR_EXAM_LSN [CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", EXAM_CD=" + EXAM_CD + ", ECI_LSN_CD="
				+ ECI_LSN_CD + ", LSN_CD=" + LSN_CD + ", OMR_MST_CD=" + OMR_MST_CD + ", LSN_NM=" + LSN_NM + ", OMR_NM="
				+ OMR_NM + ", OMR_RECOG_CNT=" + OMR_RECOG_CNT + ", STTUS_CD=" + STTUS_CD + ", QUEI_NUM=" + QUEI_NUM
				+ ", TOT_DISMK=" + TOT_DISMK + ", AVG_SC=" + AVG_SC + ", USE_YN=" + USE_YN + ", DEL_YN=" + DEL_YN
				+ ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM + "]";
	}
	
}