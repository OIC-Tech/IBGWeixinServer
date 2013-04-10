package com.louishong.ibg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/")
public class WeixinVerify extends HttpServlet {

    /**
     * 
     */
    
    PrintWriter out;
    
    private static final long serialVersionUID = 7277121887678658652L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	System.out.println(request.getRemoteHost());
	
	// Set Response Type
	response.setContentType("text/xml;charset=UTF-8");
	try {
	    request.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// Get Request Parameters
	String requestSignature = request.getParameter("signature");
	String requestTimeStamp = request.getParameter("timestamp");
	String requestNonce = request.getParameter("nonce");
	String requestEchoStr = request.getParameter("echostr");
	
	System.out.println(requestSignature);
	System.out.println(requestTimeStamp);
	System.out.println(requestNonce);
	System.out.println(requestEchoStr);
	
	//Return XML
	out.print(requestEchoStr);
	
	out.flush();
	out.close();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	
    }

}
