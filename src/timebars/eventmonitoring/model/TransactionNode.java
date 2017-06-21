package timebars.eventmonitoring.model;

import java.sql.Timestamp;


public class TransactionNode {
	private String query;
	private String SCN;
	private Timestamp timeStamp;
	private String osUser;
	private String sessionId;
	private String xid;
	private String actionName;
	private String startSCN;
	private String isoLevel;
	//可以增加新的属性
	
	public TransactionNode(String query, String sCN, Timestamp timeStamp, String xid, String actionName, String startSCN) {
		super();
		this.query = query;
		SCN = sCN;
		this.timeStamp = timeStamp;
		this.xid = xid;
		this.actionName = actionName;
		this.startSCN = startSCN;
	}
	
	public TransactionNode(Timestamp timeStamp,String xid,String startSCN) {
		super();
		this.query = null;
		SCN = null;
		this.timeStamp = timeStamp;
		this.xid = xid;
		this.actionName = null;
		this.startSCN = startSCN;
	}
	
	public TransactionNode(String xid, String isoLevel) {
		super();
		this.query = null;
		SCN = null;
		this.timeStamp = null;
		this.xid = xid;
		this.actionName = null;
		this.startSCN = null;
		this.isoLevel = isoLevel;
	}
	
	public TransactionNode(String query, String sCN, Timestamp timeStamp,
			String osUser, String sessionId, String xid, String actionName) {
		super();
		this.query = query;
		SCN = sCN;
		this.timeStamp = timeStamp;
		this.osUser = osUser;
		this.sessionId = sessionId;
		this.xid = xid;
		this.actionName = actionName;
		this.startSCN = null;
	}
	public String getOsUser() {
		return osUser;
	}
	public void setOsUser(String osUser) {
		this.osUser = osUser;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSCN() {
		return SCN;
	}
	public void setSCN(String sCN) {
		SCN = sCN;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getTransactionId(){
		return this.xid;
	}
	
	public void setTransactionId(String xid){
		this.xid = xid;
	}
	
	public String getActionName(){
		return this.actionName;
	}
	
	public void setActionName(String actionName){
		this.actionName = actionName;
	}
	
	public String getStartSCN() {
		return startSCN;
	}
	
	public void setStartSCN(String startSCN) {
		this.startSCN = startSCN;
	}
	
	public String getIsoLevel() {
		return isoLevel;
	}
	
	public void setIsoLevel(String isoLevel) {
		this.isoLevel = isoLevel;
	}
}
