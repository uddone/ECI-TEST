package logic;

import java.util.Date;

public class OMR_MST {
	private int OMR_MST_CD;
	private String OMR_NM;
	private String OMR_IMG;
	private String CRD_VAL;
	private String USE_YN;
	private Date LOAD_DTM;
	public int getOMR_MST_CD() {
		return OMR_MST_CD;
	}
	public void setOMR_MST_CD(int oMR_MST_CD) {
		OMR_MST_CD = oMR_MST_CD;
	}
	public String getOMR_NM() {
		return OMR_NM;
	}
	public void setOMR_NM(String oMR_NM) {
		OMR_NM = oMR_NM;
	}
	public String getOMR_IMG() {
		return OMR_IMG;
	}
	public void setOMR_IMG(String oMR_IMG) {
		OMR_IMG = oMR_IMG;
	}
	public String getCRD_VAL() {
		return CRD_VAL;
	}
	public void setCRD_VAL(String cRD_VAL) {
		CRD_VAL = cRD_VAL;
	}
	public String getUSE_YN() {
		return USE_YN;
	}
	public void setUSE_YN(String uSE_YN) {
		USE_YN = uSE_YN;
	}
	public Date getLOAD_DTM() {
		return LOAD_DTM;
	}
	public void setLOAD_DTM(Date lOAD_DTM) {
		LOAD_DTM = lOAD_DTM;
	}
	@Override
	public String toString() {
		return "OMR_MST [OMR_MST_CD=" + OMR_MST_CD + ", OMR_NM=" + OMR_NM + ", OMR_IMG=" + OMR_IMG + ", CRD_VAL="
				+ CRD_VAL + ", USE_YN=" + USE_YN + ", LOAD_DTM=" + LOAD_DTM + "]";
	}
	
}