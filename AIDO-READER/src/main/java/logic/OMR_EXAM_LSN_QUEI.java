package logic;

import java.util.Date;

public class OMR_EXAM_LSN_QUEI {
	private int CMPN_CD;
	private int EXAM_CD;
	private int LSN_CD;
	private int OMR_MST_CD;
	private int QUEI_NO;
	private String QUE_AREA;
	private String QUE_DTL_AREA;
	private String QUE_TYPE;
	private int CRA_NO;
	private int DISMK;
	
	
	public int getDISMK() {
		return DISMK;
	}
	public void setDISMK(int dISMK) {
		DISMK = dISMK;
	}
	public int getCMPN_CD() {
		return CMPN_CD;
	}
	public void setCMPN_CD(int cMPN_CD) {
		CMPN_CD = cMPN_CD;
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
	public int getQUEI_NO() {
		return QUEI_NO;
	}
	public void setQUEI_NO(int qUEI_NO) {
		QUEI_NO = qUEI_NO;
	}
	public String getQUE_AREA() {
		return QUE_AREA;
	}
	public void setQUE_AREA(String qUE_AREA) {
		QUE_AREA = qUE_AREA;
	}
	public String getQUE_DTL_AREA() {
		return QUE_DTL_AREA;
	}
	public void setQUE_DTL_AREA(String qUE_DTL_AREA) {
		QUE_DTL_AREA = qUE_DTL_AREA;
	}
	public String getQUE_TYPE() {
		return QUE_TYPE;
	}
	public void setQUE_TYPE(String qUE_TYPE) {
		QUE_TYPE = qUE_TYPE;
	}
	public int getCRA_NO() {
		return CRA_NO;
	}
	public void setCRA_NO(int cRA_NO) {
		CRA_NO = cRA_NO;
	}
	@Override
	public String toString() {
		return "OMR_EXAM_LSN_QUEI [CMPN_CD=" + CMPN_CD + ", EXAM_CD=" + EXAM_CD + ", LSN_CD=" + LSN_CD + ", OMR_MST_CD="
				+ OMR_MST_CD + ", QUEI_NO=" + QUEI_NO + ", QUE_AREA=" + QUE_AREA + ", QUE_DTL_AREA=" + QUE_DTL_AREA
				+ ", QUE_TYPE=" + QUE_TYPE + ", CRA_NO=" + CRA_NO + ", DISMK=" + DISMK + "]";
	}
}