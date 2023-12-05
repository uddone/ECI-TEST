package logic;


public class ALRAM {
  private int DATEDIF;
  
  private String EXAM_NM;

  private String SUB_NM;

  private String BSTOR_NM;

  public String getBSTOR_NM() {
    return BSTOR_NM;
  }

  public void setBSTOR_NM(String BSTOR_NM) {
    this.BSTOR_NM = BSTOR_NM;
  }

  public int getDATEDIF() {
    return this.DATEDIF;
  }
  
  public void setDATEDIF(int dATEDIF) {
    this.DATEDIF = dATEDIF;
  }
  
  public String getEXAM_NM() {
    return this.EXAM_NM;
  }
  
  public void setEXAM_NM(String eXAM_NM) {
    this.EXAM_NM = eXAM_NM;
  }

  public String getSUB_NM() {
    return SUB_NM;
  }

  public void setSUB_NM(String SUB_NM) {
    this.SUB_NM = SUB_NM;
  }

  @Override
  public String toString() {
    return "ALRAM{" +
            "DATEDIF=" + DATEDIF +
            ", EXAM_NM='" + EXAM_NM + '\'' +
            ", SUB_NM='" + SUB_NM + '\'' +
            ", BSTOR_NM='" + BSTOR_NM + '\'' +
            '}';
  }
}
