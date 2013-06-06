package com.louishong.ibg.weixin.ai;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.louishong.ibg.weixin.command.WeixinShiftCommand;
import com.louishong.ibg.weixin.model.Text;
import com.louishong.ibg.weixin.model.WeixinMessage;
import com.louishong.database.IBGWeixinServerResponseDatabase;

public class TextAI {

	public static WeixinMessage input(WeixinMessage message, XMLEvent event, XMLEventReader eventReader) {
		try {
			// Downwards casting
			Text text = new Text(message);
			while (eventReader.hasNext()) {
				event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement textStartElement = event.asStartElement();

					// If the Element is CONTENT generate a response and put it
					// inside the text object
					if (textStartElement.getName().getLocalPart().equals(Text.CONTENT)) {
						event = eventReader.nextEvent();
						String inputString = event.asCharacters().getData();
						// Input the inputString into the textEngine to
						// generate a response
						text.setContent(textEngine(inputString));
					}
					// If the element is MSGID then set MSGID to the original
					// MESGID
					if (textStartElement.getName().getLocalPart().equals(Text.MSGID)) {
						event = eventReader.nextEvent();
						text.setMsgId(event.asCharacters().getData());
					}
				}
			}
			message = text;
			return message;
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static String textEngine(String inputString) {
		try {
			ResultSet dbResults = IBGWeixinServerResponseDatabase.searchResponse(inputString);
			boolean isDynamic = dbResults.getString("Dynamic").equals("1");
			String response = dbResults.getString("Responses");

			if (isDynamic) {
				Pattern pattern = Pattern.compile("(.*?):(.*)");
				Matcher matcher = pattern.matcher(response);
				matcher.find();
				String protocol = matcher.group(1);
				String command = matcher.group(2);

				if (protocol.equals("WeixinShift")) {
					try {
						return WeixinShiftCommand.execute(command);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			return response;
		} catch (SQLException e) {
			//If database doesn't contain the preset for the input, then return an apologies
			return "听布咚~听布咚~~对不起主人";
		}
	}

	public static WeixinMessage input(WeixinMessage message, XMLEvent event, XMLEventReader eventReader, int statusCode) {
		message.setMsgType("text");
		Text text = new Text(message);
		text.setContent("我的服务器出现故障了~~~~ 故障代码: " + statusCode);
		while (eventReader.hasNext()) {
			try {
				event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement textStartElement = event.asStartElement();
					if (textStartElement.getName().getLocalPart().equals(Text.MSGID)) {
						event = eventReader.nextEvent();
						text.setMsgId(event.asCharacters().getData());
					}
				}
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		message = text;
		return message;
	}

}
