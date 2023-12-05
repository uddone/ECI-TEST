package logic;

import java.util.Date;

public class SYSLOG {
	private Date LOG_DTM; 
	private String STEP_NM;
	private String PID;
	private String JOB_NM;
	private String ARG_VAL;
	private String LOG_MSG;
	private String ST_END_DIV;
	private String CMPLT_MSG;
	
	
	public Date getLOG_DTM() {
		return LOG_DTM;
	}
	public void setLOG_DTM(Date lOG_DTM) {
		LOG_DTM = lOG_DTM;
	}
	public String getSTEP_NM() {
		return STEP_NM;
	}
	public void setSTEP_NM(String sTEP_NM) {
		STEP_NM = sTEP_NM;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public String getJOB_NM() {
		return JOB_NM;
	}
	public void setJOB_NM(String jOB_NM) {
		JOB_NM = jOB_NM;
	}
	public String getARG_VAL() {
		return ARG_VAL;
	}
	public void setARG_VAL(String aRG_VAL) {
		ARG_VAL = aRG_VAL;
	}
	public String getLOG_MSG() {
		return LOG_MSG;
	}
	public void setLOG_MSG(String lOG_MSG) {
		LOG_MSG = lOG_MSG;
	}
	public String getST_END_DIV() {
		return ST_END_DIV;
	}
	public void setST_END_DIV(String sT_END_DIV) {
		ST_END_DIV = sT_END_DIV;
	}
	public String getCMPLT_MSG() {
		return CMPLT_MSG;
	}
	public void setCMPLT_MSG(String cMPLT_MSG) {
		CMPLT_MSG = cMPLT_MSG;
	}
	@Override
	public String toString() {
		return "SYSLOG [LOG_DTM=" + LOG_DTM + ", STEP_NM=" + STEP_NM + ", PID=" + PID + ", JOB_NM=" + JOB_NM
				+ ", ARG_VAL=" + ARG_VAL + ", LOG_MSG=" + LOG_MSG + ", ST_END_DIV=" + ST_END_DIV + ", CMPLT_MSG="
				+ CMPLT_MSG + "]";
	}

	
}