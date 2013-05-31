package com.louishong.ibg.weixin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.louishong.database.SQLiteBase;

public class WeixinShiftWrapper {

	public static String sDriver = "org.sqlite.JDBC";

	// Where the database is stored on Louis's Computer
	public static String sUrl = "jdbc:sqlite:/Users/honglouis/Documents/Github Repo/OICWebsite/db/Profiles.sqlite";

	// Where the file is stored on The server
	// public static String sUrl =
	// "jdbc:sqlite:C:\\OIC\\database\\Profiles.sqlite";

	public static SQLiteBase sqlBase = new SQLiteBase(sDriver, sUrl);

	public static ResultSet getWeixinShift() throws SQLException {
		return sqlBase.executeQuery("SELECT * FROM WeixinShift");
	}

	public static LocalDate dateConverter(String stringDate) {
		String dateFormatter = "yy-MM-dd";
		return LocalDate.parse(stringDate, DateTimeFormat.forPattern(dateFormatter));
	}

//	public static String dateFormatter(LocalDate date) {
//		return date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth();
//
//	}

	public static ArrayList<LocalDate> getNextShifts(String name) throws SQLException, NullPointerException {
		ResultSet results = getWeixinShift();
		ArrayList<LocalDate> weixinShifts = new ArrayList<LocalDate>();

		try {
			while (results.next()) {
				String resultName = results.getString("ChineseName");
				if (resultName.equals(name)) {
					weixinShifts.add(dateConverter(results.getString("NextShift")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return weixinShifts;
	}

	public static boolean isShiftToday(String name) {
		LocalDate today = new LocalDate();
		try {
			ArrayList<LocalDate> userWeixinShifts = getNextShifts(name);
			Iterator<LocalDate> shiftIterator = userWeixinShifts.iterator();

			while (shiftIterator.hasNext()) {
				LocalDate shift = shiftIterator.next();
				if (shift.getYear() == today.getYear()) {
					if (shift.getMonthOfYear() == today.getMonthOfYear()) {
						if (shift.getDayOfMonth() == today.getDayOfMonth()) {
							return true;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void setWeixinShift(String name, LocalDate oldDate, LocalDate newDate){
//		String stringNewDate = dateFormatter(newDate);
//		String stringOldDate = dateFormatter(oldDate);
			try {
				sqlBase.executeQuery(String.format("UPDATE WeixinShift SET NextShift='%s' WHERE ChineseName='%s' AND NextShift='%s'", newDate, name, oldDate));
			} catch (SQLException e) {
			}
	}

	public static ResultSet getDatabase() throws SQLException {
		return sqlBase.executeQuery("SELECT * FROM WeixinShift");

	}

	public static void updateDatebase() throws SQLException {
		ResultSet results = getDatabase();
		while (results.next()) {
			System.out.println("Encountering: " + results.getString("ChineseName"));
			LocalDate nextShift;
			int shiftPeriod;
			try {
				nextShift = dateConverter(results.getString("NextShift"));
				shiftPeriod = Integer.parseInt(results.getString("ShiftPeriod"));
			} catch (Exception e) {
				System.out.println("NO SHIFTS!");
				continue;
			}

			LocalDate today = new LocalDate();
			if (nextShift.isBefore(today)) {
				setWeixinShift(results.getString("ChineseName"), nextShift, nextShift.plusDays(shiftPeriod));
				System.out.println("Success: " + results.getString("ChineseName"));
			}
			System.out.println();
			
		}
	}
}
