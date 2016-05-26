package gui;

import java.sql.Timestamp;


public class TransactionNode {
	private String query;
	private String SCN;
	private Timestamp timeStamp;
	private String osUser;
	private String sessionId;
	private String xid;
	private String actionName;
	//可以增加新的属性
	
	public TransactionNode(String query, String sCN, Timestamp timeStamp, String xid, String actionName) {
		super();
		this.query = query;
		SCN = sCN;
		this.timeStamp = timeStamp;
		this.xid = xid;
		this.actionName = actionName;
	}
	
	public TransactionNode(Timestamp timeStamp,String xid) {
		super();
		this.query = null;
		SCN = null;
		this.timeStamp = timeStamp;
		this.xid = xid;
		this.actionName = null;
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
}
