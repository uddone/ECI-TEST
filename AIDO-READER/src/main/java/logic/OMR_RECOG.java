package logic;

import java.util.Date;


public class OMR_RECOG {
	private int OMR_KEY;
	private int EXAM_CD;
	private String EXAM_NM;
	private int LSN_CD;
	private String SUB_NM;
	private String LSN_NM;
	private int OMR_MST_CD;
	private int CMPN_CD;
	private int BSTOR_CD;
	private String BSTOR_NM;
	private String SCHYR;
	private String EXMN_NO;
	private String OMR_IMG;
	private String STDN_NM;
	private String SEX;
	private String BTHDAY;
	private int TOT_SC;
	private int ERR_CNT;
	private int CRA_CNT;
	private String GRDG_YN;
	private int REG_MBR_NO;
	private int UPD_MBR_NO;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	private String TRM_YN;
	private String RET_VAL;
	private float AVG_SC;
	private int CRA_YN;
	private int LSN_GRP_CNT;
	
	
	
	public int getLSN_GRP_CNT() {
		return LSN_GRP_CNT;
	}
	public void setLSN_GRP_CNT(int lSN_GRP_CNT) {
		LSN_GRP_CNT = lSN_GRP_CNT;
	}
	public int getCRA_CNT() {
		return CRA_CNT;
	}
	public void setCRA_CNT(int cRA_CNT) {
		CRA_CNT = cRA_CNT;
	}
	public String getGRDG_YN() {
		return GRDG_YN;
	}
	public void setGRDG_YN(String gRDG_YN) {
		GRDG_YN = gRDG_YN;
	}
	public int getOMR_KEY() {
		return OMR_KEY;
	}
	public void setOMR_KEY(int oMR_KEY) {
		OMR_KEY = oMR_KEY;
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
	public int getLSN_CD() {
		return LSN_CD;
	}
	public void setLSN_CD(int lSN_CD) {
		LSN_CD = lSN_CD;
	}
	public String getSUB_NM() {
		return SUB_NM;
	}
	public void setSUB_NM(String sUB_NM) {
		SUB_NM = sUB_NM;
	}
	public String getLSN_NM() {
		return LSN_NM;
	}
	public void setLSN_NM(String lSN_NM) {
		LSN_NM = lSN_NM;
	}
	public int getOMR_MST_CD() {
		return OMR_MST_CD;
	}
	public void setOMR_MST_CD(int oMR_MST_CD) {
		OMR_MST_CD = oMR_MST_CD;
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
	public String getBSTOR_NM() {
		return BSTOR_NM;
	}
	public void setBSTOR_NM(String bSTOR_NM) {
		BSTOR_NM = bSTOR_NM;
	}
	public String getSCHYR() {
		return SCHYR;
	}
	public void setSCHYR(String sCHYR) {
		SCHYR = sCHYR;
	}
	public String getEXMN_NO() {
		return EXMN_NO;
	}
	public void setEXMN_NO(String eXMN_NO) {
		EXMN_NO = eXMN_NO;
	}
	public String getOMR_IMG() {
		return OMR_IMG;
	}
	public void setOMR_IMG(String oMR_IMG) {
		OMR_IMG = oMR_IMG;
	}
	public String getSTDN_NM() {
		return STDN_NM;
	}
	public void setSTDN_NM(String sTDN_NM) {
		STDN_NM = sTDN_NM;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	public String getBTHDAY() {
		return BTHDAY;
	}
	public void setBTHDAY(String bTHDAY) {
		BTHDAY = bTHDAY;
	}
	public int getTOT_SC() {
		return TOT_SC;
	}
	public void setTOT_SC(int tOT_SC) {
		TOT_SC = tOT_SC;
	}
	public int getERR_CNT() {
		return ERR_CNT;
	}
	public void setERR_CNT(int eRR_CNT) {
		ERR_CNT = eRR_CNT;
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
	public String getTRM_YN() {
		return TRM_YN;
	}
	public void setTRM_YN(String tRM_YN) {
		TRM_YN = tRM_YN;
	}
	public String getRET_VAL() {
		return RET_VAL;
	}
	public void setRET_VAL(String rET_VAL) {
		RET_VAL = rET_VAL;
	}
	public float getAVG_SC() {
		return AVG_SC;
	}
	public void setAVG_SC(float aVG_SC) {
		AVG_SC = aVG_SC;
	}
	public int getCRA_YN() {
		return CRA_YN;
	}
	public void setCRA_YN(int cRA_YN) {
		CRA_YN = cRA_YN;
	}
	@Override
	public String toString() {
		return "OMR_RECOG [OMR_KEY=" + OMR_KEY + ", EXAM_CD=" + EXAM_CD + ", EXAM_NM=" + EXAM_NM + ", LSN_CD=" + LSN_CD
				+ ", SUB_NM=" + SUB_NM + ", LSN_NM=" + LSN_NM + ", OMR_MST_CD=" + OMR_MST_CD + ", CMPN_CD=" + CMPN_CD
				+ ", BSTOR_CD=" + BSTOR_CD + ", BSTOR_NM=" + BSTOR_NM + ", SCHYR=" + SCHYR + ", EXMN_NO=" + EXMN_NO
				+ ", OMR_IMG=" + OMR_IMG + ", STDN_NM=" + STDN_NM + ", SEX=" + SEX + ", BTHDAY=" + BTHDAY + ", TOT_SC="
				+ TOT_SC + ", ERR_CNT=" + ERR_CNT + ", CRA_CNT=" + CRA_CNT + ", GRDG_YN=" + GRDG_YN + ", REG_MBR_NO="
				+ REG_MBR_NO + ", UPD_MBR_NO=" + UPD_MBR_NO + ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM
				+ ", TRM_YN=" + TRM_YN + ", RET_VAL=" + RET_VAL + ", AVG_SC=" + AVG_SC + ", CRA_YN=" + CRA_YN
				+ ", LSN_GRP_CNT=" + LSN_GRP_CNT + "]";
	}
	
  }
