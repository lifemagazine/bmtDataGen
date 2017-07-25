package bmtDataGen;

public class CertModel {
	
	private long seq;
	private String id;
	private String juminNum;
	private String country;
	private String state;
	private String location;
	private String organization;
	private String organizationUnit;
	private String companyName;
	private String subjectName;
	private String privPem;
	private String pubPem;
	private String hashMsg;
	private String signMsg;
	private String signMsg4Login;
	
	public String getInsertSql() {
		StringBuilder sb = new StringBuilder("insert into bmt_qry_rsa (");
		sb.append("seq, id, jumin_num, country, state, location, organization, orginization_unit, company_name, subject_name, pri_pem, pub_pem, hash_msg, sign_msg, sign_msg_login) values (");
		sb.append(seq).append(", '");
		sb.append(id).append("', '");
		sb.append(juminNum).append("', '");
		sb.append(country).append("', '");
		sb.append(state).append("', '");
		sb.append(location).append("', '");
		sb.append(organization).append("', '");
		sb.append(organizationUnit).append("', '");
		sb.append(companyName).append("', '");
		sb.append(subjectName).append("', '");
		sb.append(privPem).append("', '");
		sb.append(pubPem).append("', '");
		sb.append(hashMsg).append("', '");
		sb.append(signMsg).append("', '");
		sb.append(signMsg4Login).append("')");
		return sb.toString();
	}
	
	public String getUpdateSql() {
		
		return null;
	}
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJuminNum() {
		return juminNum;
	}
	public void setJuminNum(String juminNum) {
		this.juminNum = juminNum;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationUnit() {
		return organizationUnit;
	}
	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getPrivPem() {
		return privPem;
	}
	public void setPrivPem(String privPem) {
		this.privPem = privPem;
	}
	public String getPubPem() {
		return pubPem;
	}
	public void setPubPem(String pubPem) {
		this.pubPem = pubPem;
	}
	public String getHashMsg() {
		return hashMsg;
	}
	public void setHashMsg(String hashMsg) {
		this.hashMsg = hashMsg;
	}
	public String getSignMsg() {
		return signMsg;
	}
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	public String getSignMsg4Login() {
		return signMsg4Login;
	}
	public void setSignMsg4Login(String signMsg4Login) {
		this.signMsg4Login = signMsg4Login;
	}
}
