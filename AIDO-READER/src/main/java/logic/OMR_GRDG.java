package logic;

import java.util.Date;


public class OMR_GRDG {
	private int OMR_KEY;
	private int QUEI_NO;
	private int LSN_CD;
	private int LSN_SEQ;
	private int CMPN_CD;
	private int BSTOR_CD;
	private String EXMN_NO;
	private String SEX;
	private String BTHDAY;
	private String OMR_IMG;
	private String MARK_NO;
	private String STDN_NM;  
	private String ERR_YN;
	private String CRA_YN;
	private int TOT_SC;
	private int REG_MBR_NO;
	private int UPD_MBR_NO;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	private int OMR_MST_CD;
	private String FREQ;
	
	
	public int getLSN_SEQ() {
		return LSN_SEQ;
	}
	public void setLSN_SEQ(int lSN_SEQ) {
		LSN_SEQ = lSN_SEQ;
	}
	public String getFREQ() {
		return FREQ;
	}
	public void setFREQ(String fREQ) {
		FREQ = fREQ;
	}
	public int getOMR_KEY() {
		return OMR_KEY;
	}
	public void setOMR_KEY(int oMR_KEY) {
		OMR_KEY = oMR_KEY;
	}
	public int getQUEI_NO() {
		return QUEI_NO;
	}
	public void setQUEI_NO(int qUEI_NO) {
		QUEI_NO = qUEI_NO;
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
	public String getEXMN_NO() {
		return EXMN_NO;
	}
	public void setEXMN_NO(String eXMN_NO) {
		EXMN_NO = eXMN_NO;
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
	public String getOMR_IMG() {
		return OMR_IMG;
	}
	public void setOMR_IMG(String oMR_IMG) {
		OMR_IMG = oMR_IMG;
	}
	public String getMARK_NO() {
		return MARK_NO;
	}
	public void setMARK_NO(String mARK_NO) {
		MARK_NO = mARK_NO;
	}
	public String getSTDN_NM() {
		return STDN_NM;
	}
	public void setSTDN_NM(String sTDN_NM) {
		STDN_NM = sTDN_NM;
	}
	public String getERR_YN() {
		return ERR_YN;
	}
	public void setERR_YN(String eRR_YN) {
		ERR_YN = eRR_YN;
	}
	public String getCRA_YN() {
		return CRA_YN;
	}
	public void setCRA_YN(String cRA_YN) {
		CRA_YN = cRA_YN;
	}
	public int getTOT_SC() {
		return TOT_SC;
	}
	public void setTOT_SC(int tOT_SC) {
		TOT_SC = tOT_SC;
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
	public int getOMR_MST_CD() {
		return OMR_MST_CD;
	}
	public void setOMR_MST_CD(int oMR_MST_CD) {
		OMR_MST_CD = oMR_MST_CD;
	}
	@Override
	public String toString() {
		return "OMR_GRDG [OMR_KEY=" + OMR_KEY + ", QUEI_NO=" + QUEI_NO + ", LSN_CD=" + LSN_CD + ", LSN_SEQ=" + LSN_SEQ
				+ ", CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", EXMN_NO=" + EXMN_NO + ", SEX=" + SEX
				+ ", BTHDAY=" + BTHDAY + ", OMR_IMG=" + OMR_IMG + ", MARK_NO=" + MARK_NO + ", STDN_NM=" + STDN_NM
				+ ", ERR_YN=" + ERR_YN + ", CRA_YN=" + CRA_YN + ", TOT_SC=" + TOT_SC + ", REG_MBR_NO=" + REG_MBR_NO
				+ ", UPD_MBR_NO=" + UPD_MBR_NO + ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM + ", OMR_MST_CD="
				+ OMR_MST_CD + ", FREQ=" + FREQ + "]";
	}

	
	
}
