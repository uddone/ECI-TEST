package logic;

import java.util.Date;

public class FILELOG {
	private Date LOG_DTM; 
	private String OMR_OCR_DIV;
	private String PID ;
	private int MST_CD;
	private int EXAM_CD;
	private String EXAM_NM;
	
	private int CMPN_CD;
	private int BSTOR_CD;
	private String BSTOR_NM;

	private String ORIGIN_FILE_NM;
	private String CHG_FILE_NM;

	private String SCHYR;
	private String LSN_GRP_NM;

	private int GRP_CNT;




	public int getGRP_CNT() {
		return GRP_CNT;
	}
	public void setGRP_CNT(int gRP_CNT) {
		GRP_CNT = gRP_CNT;
	}

	public String getBSTOR_NM() {
		return BSTOR_NM;
	}
	public void setBSTOR_NM(String bSTOR_NM) {
		BSTOR_NM = bSTOR_NM;
	}


	public String getEXAM_NM() {
		return EXAM_NM;
	}
	public void setEXAM_NM(String eXAM_NM) {
		EXAM_NM = eXAM_NM;
	}
	public String getSCHYR() {
		return SCHYR;
	}
	public void setSCHYR(String sCHYR) {
		SCHYR = sCHYR;
	}
	public String getLSN_GRP_NM() {
		return LSN_GRP_NM;
	}
	public void setLSN_GRP_NM(String lSN_GRP_NM) {
		LSN_GRP_NM = lSN_GRP_NM;
	}

	public int getMST_CD() {
		return MST_CD;
	}
	public void setMST_CD(int mST_CD) {
		MST_CD = mST_CD;
	}
	public void setORIGIN_FILE_NM(String oRIGIN_FILE_NM) {
		ORIGIN_FILE_NM = oRIGIN_FILE_NM;
	}
	public Date getLOG_DTM() {
		return LOG_DTM;
	}
	public void setLOG_DTM(Date lOG_DTM) {
		LOG_DTM = lOG_DTM;
	}
	public String getOMR_OCR_DIV() {
		return OMR_OCR_DIV;
	}
	public void setOMR_OCR_DIV(String oMR_OCR_DIV) {
		OMR_OCR_DIV = oMR_OCR_DIV;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
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
	public String getORIGIN_FILE_NM() {
		return ORIGIN_FILE_NM;
	}
	public void setORGIN_FILE_NM(String oRIGIN_FILE_NM) {
		ORIGIN_FILE_NM = oRIGIN_FILE_NM;
	}
	public String getCHG_FILE_NM() {
		return CHG_FILE_NM;
	}
	public void setCHG_FILE_NM(String cHG_FILE_NM) {
		CHG_FILE_NM = cHG_FILE_NM;
	}
	@Override
	public String toString() {
		return "FILELOG [LOG_DTM=" + LOG_DTM + ", OMR_OCR_DIV=" + OMR_OCR_DIV + ", PID=" + PID + ", MST_CD=" + MST_CD
				+ ", EXAM_CD=" + EXAM_CD + ", EXAM_NM=" + EXAM_NM + ", CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD
				+ ", BSTOR_NM=" + BSTOR_NM + ", ORIGIN_FILE_NM=" + ORIGIN_FILE_NM + ", CHG_FILE_NM=" + CHG_FILE_NM
				+ ", SCHYR=" + SCHYR + ", LSN_GRP_NM=" + LSN_GRP_NM + "]";
	}
	
	
}