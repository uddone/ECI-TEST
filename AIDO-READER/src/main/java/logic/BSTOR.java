package logic;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class BSTOR {
	private int BSTOR_CD;
	private int CMPN_CD;
	private String CMPN_NM;
	private String BSTOR_NM;
	private Date LOAD_DTM;
	private int WARN_CNT;
	
	public int getBSTOR_CD() {
		return BSTOR_CD;
	}
	public void setBSTOR_CD(int bSTOR_CD) {
		BSTOR_CD = bSTOR_CD;
	}
	public int getCMPN_CD() {
		return CMPN_CD;
	}
	public void setCMPN_CD(int cMPN_CD) {
		CMPN_CD = cMPN_CD;
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
	public Date getLOAD_DTM() {
		return LOAD_DTM;
	}
	public void setLOAD_DTM(Date lOAD_DTM) {
		LOAD_DTM = lOAD_DTM;
	}
	public int getWARN_CNT() {
		return WARN_CNT;
	}
	public void setWARN_CNT(int wARN_CNT) {
		WARN_CNT = wARN_CNT;
	}
	@Override
	public String toString() {
		return "BSTOR [BSTOR_CD=" + BSTOR_CD + ", CMPN_CD=" + CMPN_CD + ", CMPN_NM=" + CMPN_NM + ", BSTOR_NM="
				+ BSTOR_NM + ", LOAD_DTM=" + LOAD_DTM + ", WARN_CNT=" + WARN_CNT + "]";
	}
	
	
}