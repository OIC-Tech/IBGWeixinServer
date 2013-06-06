package com.louishong.ibg.weixin.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.louishong.database.WeixinShiftWrapper;

public class WeixinShiftCommand {

	public static String execute(String command) throws Exception {

		// Check if the command is "Today"
		if (command.equals("Today")) {
			// Get the database with the columns ChineseName NextShift Period
			ResultSet results = WeixinShiftWrapper.getWeixinShift();
			ArrayList<String> names = new ArrayList<String>();

			while (results.next()) {
				String name = results.getString("ChineseName");

				boolean isToday = WeixinShiftWrapper.isShiftDaysAfter(name, 0);
				if (isToday) {
					names.add(name);
				}
			}

			String stringResult = "今天";
			for (String name : names) {
				stringResult += name;
				stringResult += ", ";
			}
			stringResult = stringResult.substring(0, stringResult.length() - 2);
			stringResult += "做微报~";
			
			return stringResult;

		}
		throw (new Exception("False Command"));
	}
}
