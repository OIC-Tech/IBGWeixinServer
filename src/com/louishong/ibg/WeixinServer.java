package com.louishong.ibg;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

@WebServlet("/")
public class WeixinServer extends HttpServlet {

    /**
     * 
     */
    
    private static final long serialVersionUID = 7277121887678658652L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	System.out.println(request.getRemoteHost());
	PrintWriter out;
	ServletInputStream streamIn;
	
	DocumentBuilderFactory DocBuilderFactory;
	DocumentBuilder DocBuilder;
	Document doc = null;
	
	try {
		out = response.getWriter();
	} catch (IOException e1) {
		e1.printStackTrace();
		return;
	}
	
	try {
		streamIn = request.getInputStream();
		DocBuilderFactory = DocumentBuilderFactory.newInstance();
		DocBuilder = DocBuilderFactory.newDocumentBuilder();
		doc = DocBuilder.parse(streamIn);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	out.println(doc.getElementById("list"));
	System.out.println(doc.getElementById("list"));

    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	
    }

}
