package com.louishong.ibg.weixin.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.louishong.database.ProfileWrapper;
import com.louishong.database.WeixinShiftWrapper;

public class WeixinShiftCommand {

	public static String execute(String command) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		// // Check if the command is "Today"
		// if (command.equals("Today")) {
		// ResultSet results = shiftWrapper.getWeixinShift();
		// ArrayList<String> names = new ArrayList<String>();
		//
		// while (results.next()) {
		// String name = results.getString("ChineseName");
		//
		// boolean isToday = shiftWrapper.isShiftDaysAfter(name, 0);
		// if (isToday && !names.contains(name)) {
		// names.add(name);
		// }
		// }

		WeixinShiftWrapper shiftWrapper = new WeixinShiftWrapper();
		ProfileWrapper profileWrapper = new ProfileWrapper();
		ArrayList<Integer> UIDs = shiftWrapper.getTodaysShifts();

		String stringResult = "����";
		for (Integer UID : UIDs) {
			stringResult += profileWrapper.getNickName(UID);
			stringResult += ", ";
		}
		if (!UIDs.isEmpty()) {
			stringResult = stringResult.substring(0, stringResult.length() - 2);
			stringResult += "��΢��~";
		} else {
			stringResult += "û������΢��~";
		}
		return stringResult;

	}
}
