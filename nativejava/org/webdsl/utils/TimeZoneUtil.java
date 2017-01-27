package org.webdsl.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.webdsl.logging.Logger;


public class TimeZoneUtil {
	private static TimeZone serverTimeZone = TimeZone.getDefault();
	static{
		Logger.info("Server time zone is set to: " + serverTimeZone.getDisplayName() );
	}
	
	public static TimeZone getServerTimeZone(){
		return serverTimeZone;
	}
	
	public static TimeZone getTimeZone(String timezoneId){
		return TimeZone.getTimeZone(timezoneId);
	}

	private static Date transformBetweenTimeZones(Date d, TimeZone localTimeZone, boolean toServerTimeZone) {
		if (localTimeZone != serverTimeZone) {
			long epochTime = d.getTime();
			int serverOffset = serverTimeZone.getOffset(epochTime);
			int localOffset = localTimeZone.getOffset(epochTime);
			int diff = localOffset - serverOffset;
			return toServerTimeZone ? new Date(epochTime - diff) : new Date(epochTime + diff);
		} else {
			return d;
		}
	}

	public static Date toServerTime(Date d, TimeZone timezone) {
		return transformBetweenTimeZones(d, timezone, true);
	}

	public static Date toLocalTime(Date d, TimeZone timezone) {
		return transformBetweenTimeZones(d, timezone, false);
	}
	

	public static List<String> timeZoneLabels() {
		return Arrays.asList("", "(GMT-12:00) AoE (Anywhere On Earth)","(GMT-11:00) Midway Island, Samoa", "(GMT-10:00) Hawaii-Aleutian", "(GMT-10:00) Hawaii",
				"(GMT-09:30) Marquesas Islands", "(GMT-09:00) Gambier Islands", "(GMT-09:00) Alaska",
				"(GMT-08:00) Tijuana, Baja California", "(GMT-08:00) Pitcairn Islands",
				"(GMT-08:00) Pacific Time (US & Canada)", "(GMT-07:00) Mountain Time (US & Canada)",
				"(GMT-07:00) Chihuahua, La Paz, Mazatlan", "(GMT-07:00) Arizona",
				"(GMT-06:00) Saskatchewan, Central America", "(GMT-06:00) Guadalajara, Mexico City, Monterrey",
				"(GMT-06:00) Easter Island", "(GMT-06:00) Central Time (US & Canada)",
				"(GMT-05:00) Eastern Time (US & Canada)", "(GMT-05:00) Cuba",
				"(GMT-05:00) Bogota, Lima, Quito, Rio Branco", "(GMT-04:30) Caracas", "(GMT-04:00) Santiago",
				"(GMT-04:00) La Paz", "(GMT-04:00) Faukland Islands", "(GMT-04:00) Brazil",
				"(GMT-04:00) Atlantic Time (Goose Bay)", "(GMT-04:00) Atlantic Time (Canada)",
				"(GMT-03:30) Newfoundland", "(GMT-03:00) UTC-3", "(GMT-03:00) Montevideo",
				"(GMT-03:00) Miquelon, St. Pierre", "(GMT-03:00) Greenland", "(GMT-03:00) Buenos Aires",
				"(GMT-03:00) Brasilia", "(GMT-02:00) Mid-Atlantic", "(GMT-01:00) Cape Verde Is.", "(GMT-01:00) Azores",
				"(GMT) Greenwich Mean Time : Belfast", "(GMT) Greenwich Mean Time : Dublin",
				"(GMT) Greenwich Mean Time : Lisbon", "(GMT) Greenwich Mean Time : London", "(GMT) Monrovia, Reykjavik",
				"(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna",
				"(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague",
				"(GMT+01:00) Brussels, Copenhagen, Madrid, Paris", "(GMT+01:00) West Central Africa",
				"(GMT+01:00) Windhoek", "(GMT+02:00) Beirut", "(GMT+02:00) Cairo", "(GMT+02:00) Gaza",
				"(GMT+02:00) Harare, Pretoria", "(GMT+02:00) Jerusalem", "(GMT+02:00) Minsk", "(GMT+02:00) Syria",
				"(GMT+03:00) Moscow, St. Petersburg, Volgograd", "(GMT+03:00) Nairobi", "(GMT+03:30) Tehran",
				"(GMT+04:00) Abu Dhabi, Muscat", "(GMT+04:00) Yerevan", "(GMT+04:30) Kabul", "(GMT+05:00) Ekaterinburg",
				"(GMT+05:00) Tashkent", "(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "(GMT+05:45) Kathmandu",
				"(GMT+06:00) Astana, Dhaka", "(GMT+06:00) Novosibirsk", "(GMT+06:30) Yangon (Rangoon)",
				"(GMT+07:00) Bangkok, Hanoi, Jakarta", "(GMT+07:00) Krasnoyarsk",
				"(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "(GMT+08:00) Irkutsk, Ulaan Bataar",
				"(GMT+08:00) Perth", "(GMT+08:45) Eucla", "(GMT+09:00) Osaka, Sapporo, Tokyo", "(GMT+09:00) Seoul",
				"(GMT+09:00) Yakutsk", "(GMT+09:30) Adelaide", "(GMT+09:30) Darwin", "(GMT+10:00) Brisbane",
				"(GMT+10:00) Hobart", "(GMT+10:00) Vladivostok", "(GMT+10:30) Lord Howe Island",
				"(GMT+11:00) Solomon Is., New Caledonia", "(GMT+11:00) Magadan", "(GMT+11:30) Norfolk Island",
				"(GMT+12:00) Anadyr, Kamchatka", "(GMT+12:00) Auckland, Wellington",
				"(GMT+12:00) Fiji, Kamchatka, Marshall Is.", "(GMT+12:45) Chatham Islands", "(GMT+13:00) Nuku'alofa",
				"(GMT+14:00) Kiritimati");
	}

	public static List<String> timeZoneIds() {
		return Arrays.asList("","Etc/GMT+12", "Pacific/Midway", "America/Adak", "Etc/GMT+10", "Pacific/Marquesas", "Pacific/Gambier",
				"America/Anchorage", "America/Ensenada", "Etc/GMT+8", "America/Los_Angeles", "America/Denver",
				"America/Chihuahua", "America/Dawson_Creek", "America/Belize", "America/Cancun", "Chile/EasterIsland",
				"America/Chicago", "America/New_York", "America/Havana", "America/Bogota", "America/Caracas",
				"America/Santiago", "America/La_Paz", "Atlantic/Stanley", "America/Campo_Grande", "America/Goose_Bay",
				"America/Glace_Bay", "America/St_Johns", "America/Araguaina", "America/Montevideo", "America/Miquelon",
				"America/Godthab", "America/Argentina/Buenos_Aires", "America/Sao_Paulo", "America/Noronha",
				"Atlantic/Cape_Verde", "Atlantic/Azores", "Europe/Belfast", "Europe/Dublin", "Europe/Lisbon",
				"Europe/London", "Africa/Abidjan", "Europe/Amsterdam", "Europe/Belgrade", "Europe/Brussels",
				"Africa/Algiers", "Africa/Windhoek", "Asia/Beirut", "Africa/Cairo", "Asia/Gaza", "Africa/Blantyre",
				"Asia/Jerusalem", "Europe/Minsk", "Asia/Damascus", "Europe/Moscow", "Africa/Addis_Ababa", "Asia/Tehran",
				"Asia/Dubai", "Asia/Yerevan", "Asia/Kabul", "Asia/Yekaterinburg", "Asia/Tashkent", "Asia/Kolkata",
				"Asia/Katmandu", "Asia/Dhaka", "Asia/Novosibirsk", "Asia/Rangoon", "Asia/Bangkok", "Asia/Krasnoyarsk",
				"Asia/Hong_Kong", "Asia/Irkutsk", "Australia/Perth", "Australia/Eucla", "Asia/Tokyo", "Asia/Seoul",
				"Asia/Yakutsk", "Australia/Adelaide", "Australia/Darwin", "Australia/Brisbane", "Australia/Hobart",
				"Asia/Vladivostok", "Australia/Lord_Howe", "Etc/GMT-11", "Asia/Magadan", "Pacific/Norfolk",
				"Asia/Anadyr", "Pacific/Auckland", "Etc/GMT-12", "Pacific/Chatham", "Pacific/Tongatapu",
				"Pacific/Kiritimati");
	}
}