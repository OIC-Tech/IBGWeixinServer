package com.louishong.ibg.weixin.model;

public class Text extends WeixinMessage{
	public final static String CONTENT = "Content";
	public final static String MSGID = "MsgId";
	
	private String Content;
	private String MsgId;
	
	public Text(WeixinMessage message) {
	    ToUserName = message.getToUserName();
		FromUserName = message.getFromUserName();
		CreateTime = message.getCreateTime();
		MsgType = message.getMsgType();
	}
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
	public String toString() {
		return Content;
		
	}
	
}
