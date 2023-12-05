package logic;

import java.util.Date;
import java.io.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class MBR implements Serializable{
	private int CMPN_CD;
	private int BSTOR_CD;
	private int MBR_NO;
	private String EMAIL_ADR;
	private String PWD;
	private String TEL_NO;
	private Date JOIN_DTM;
	private String MBR_MST_YN ;
	private String DEL_YN ;
	private Date UPD_DTM;
	private Date LOAD_DTM;
	private String BSTOR_NM;
	private String CMPN_NM;
	private String NAME;
	private String PWDCHK;
	
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
	public int getMBR_NO() {
		return MBR_NO;
	}
	public void setMBR_NO(int mBR_NO) {
		MBR_NO = mBR_NO;
	}
	public String getEMAIL_ADR() {
		return EMAIL_ADR;
	}
	public void setEMAIL_ADR(String eMAIL_ADR) {
		EMAIL_ADR = eMAIL_ADR;
	}
	public String getPWD() {
		return PWD;
	}
	public void setPWD(String pWD) {
		PWD = pWD;
	}
	public String getTEL_NO() {
		return TEL_NO;
	}
	public void setTEL_NO(String tEL_NO) {
		TEL_NO = tEL_NO;
	}
	public Date getJOIN_DTM() {
		return JOIN_DTM;
	}
	public void setJOIN_DTM(Date jOIN_DTM) {
		JOIN_DTM = jOIN_DTM;
	}
	public String getMBR_MST_YN() {
		return MBR_MST_YN;
	}
	public void setMBR_MST_YN(String mBR_MST_YN) {
		MBR_MST_YN = mBR_MST_YN;
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
	public String getBSTOR_NM() {
		return BSTOR_NM;
	}
	public void setBSTOR_NM(String bSTOR_NM) {
		BSTOR_NM = bSTOR_NM;
	}
	public String getCMPN_NM() {
		return CMPN_NM;
	}
	public void setCMPN_NM(String cMPN_NM) {
		CMPN_NM = cMPN_NM;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getPWDCHK() {
		return PWDCHK;
	}
	public void setPWDCHK(String pWDCHK) {
		PWDCHK = pWDCHK;
	}
	@Override
	public String toString() {
		return "MBR [CMPN_CD=" + CMPN_CD + ", BSTOR_CD=" + BSTOR_CD + ", MBR_NO=" + MBR_NO + ", EMAIL_ADR=" + EMAIL_ADR
				+ ", PWD=" + PWD + ", TEL_NO=" + TEL_NO + ", JOIN_DTM=" + JOIN_DTM + ", MBR_MST_YN=" + MBR_MST_YN
				+ ", DEL_YN=" + DEL_YN + ", UPD_DTM=" + UPD_DTM + ", LOAD_DTM=" + LOAD_DTM + ", BSTOR_NM=" + BSTOR_NM
				+ ", CMPN_NM=" + CMPN_NM + ", NAME=" + NAME + ", PWDCHK=" + PWDCHK + "]";
	}
	
}