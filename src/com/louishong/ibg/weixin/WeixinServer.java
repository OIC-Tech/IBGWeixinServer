package com.louishong.ibg.weixin;

import com.louishong.ibg.weixin.model.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

@WebServlet("/")
public class WeixinServer extends HttpServlet {

	private static final long serialVersionUID = 7277121887678658652L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(request.getRemoteHost());
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

		// Return XML
		out.print(requestEchoStr);

		out.flush();
		out.close();

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		
		// Get Writer
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Set stream up with the Stax Parser
		try {
			InputStream requestInputStream = request.getInputStream();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = factory
					.createXMLEventReader(requestInputStream);

			// init Item Text
			WeixinMessage message = new WeixinMessage();

			// Loop through each node and inserts data into the object message
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					// get element as StartElement and inputs into the
					// WeixinMessage
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart().equals(WeixinMessage.TO_USER_NAME)) {
						event = eventReader.nextEvent();
						message.setToUserName(event.asCharacters().getData());
						continue;
					}
				}

				if (event.isStartElement()) {
					// get element as StartElement and inputs into the
					// WeixinMessage
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart().equals(WeixinMessage.FROM_USER_NAME)) {
						event = eventReader.nextEvent();
						message.setFromUserName(event.asCharacters().getData());
						continue;
					}
				}

				if (event.isStartElement()) {
					// get element as StartElement and inputs into the
					// WeixinMessage
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart().equals(WeixinMessage.CREATE_TIME)) {
						event = eventReader.nextEvent();
						message.setCreateTime(event.asCharacters().getData());
						continue;
					}
				}

				if (event.isStartElement()) {
					// get element as StartElement and checks what type it is
					// makes the object according to the type and converts
					// objects
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart().equals(WeixinMessage.MSG_TYPE)) {
						event = eventReader.nextEvent();
						String type = event.asCharacters().getData();
						message.setMsgType(type);

						if (type.equals(WeixinMessage.TEXT)) {
							Text text = new Text(message);
							
							while (eventReader.hasNext()) {
								event = eventReader.nextEvent();
								if (event.isStartElement()) {
									StartElement textStartElement = event
											.asStartElement();
									if (textStartElement.getName()
											.getLocalPart().equals(Text.CONTENT)) {
										event = eventReader.nextEvent();
										text.setContent(event
												.asCharacters().getData());
									}
									if (textStartElement.getName()
											.getLocalPart().equals(Text.MSGID)) {
										event = eventReader.nextEvent();
										text.setMsgId(event
												.asCharacters().getData());
									}
								}
							}
							message = text;
							out.println(((Text)message).toString());
						}
					}
				}

			}
			out.println();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

	}

}
