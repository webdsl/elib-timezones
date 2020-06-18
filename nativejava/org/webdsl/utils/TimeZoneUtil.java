package org.webdsl.utils;

import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.webdsl.logging.Logger;

public class TimeZoneUtil {
  private static TimeZone serverTimeZone = TimeZone.getDefault();
  private static List<String> timeZoneIds;
  private static List<String> timeZoneLabels;
  private static final long timeZoneOffsetExpiresAfterMs = 30 * 60 * 1000;
  static {
    Logger.info("Server time zone is set to: " + serverTimeZone.getDisplayName());
    timeZoneIds = Arrays.asList("", "Etc/GMT+12", "Pacific/Midway", "America/Adak", "Etc/GMT+10", "Pacific/Marquesas",
        "Pacific/Gambier", "America/Anchorage", "America/Ensenada", "Etc/GMT+8", "America/Los_Angeles",
        "America/Denver", "America/Chihuahua", "America/Dawson_Creek", "America/Belize", "America/Cancun",
        "Chile/EasterIsland", "America/Chicago", "America/New_York", "America/Havana", "America/Bogota",
        "America/Caracas", "America/Santiago", "America/La_Paz", "Atlantic/Stanley", "America/Campo_Grande",
        "America/Goose_Bay", "America/Glace_Bay", "America/St_Johns", "America/Araguaina", "America/Montevideo",
        "America/Miquelon", "America/Godthab", "America/Argentina/Buenos_Aires", "America/Sao_Paulo", "America/Noronha",
        "Atlantic/Cape_Verde", "Atlantic/Azores", "Etc/UTC", "Europe/Belfast", "Europe/Dublin", "Europe/Lisbon", "Europe/London",
        "Africa/Abidjan", "Europe/Amsterdam", "Europe/Belgrade", "Europe/Brussels", "Africa/Algiers", "Africa/Windhoek",
        "Asia/Beirut", "Africa/Cairo", "Asia/Gaza", "Africa/Blantyre", "Asia/Jerusalem", "Europe/Minsk",
        "Asia/Damascus", "Europe/Moscow", "Africa/Addis_Ababa", "Asia/Tehran", "Asia/Dubai", "Asia/Yerevan",
        "Asia/Kabul", "Asia/Yekaterinburg", "Asia/Tashkent", "Asia/Kolkata", "Asia/Katmandu", "Asia/Dhaka",
        "Asia/Novosibirsk", "Asia/Rangoon", "Asia/Bangkok", "Asia/Krasnoyarsk", "Asia/Hong_Kong", "Asia/Irkutsk",
        "Australia/Perth", "Australia/Eucla", "Asia/Tokyo", "Asia/Seoul", "Asia/Yakutsk", "Australia/Adelaide",
        "Australia/Darwin", "Australia/Brisbane", "Australia/Hobart", "Asia/Vladivostok", "Australia/Lord_Howe",
        "Etc/GMT-11", "Asia/Magadan", "Pacific/Norfolk", "Asia/Anadyr", "Pacific/Auckland", "Etc/GMT-12",
        "Pacific/Chatham", "Pacific/Tongatapu", "Pacific/Kiritimati");

    timeZoneLabels = Arrays.asList("", "(GMT-12:00) AoE (Anywhere On Earth)", "(GMT-11:00) Midway Island, Samoa",
        "(GMT-10:00) Hawaii-Aleutian", "(GMT-10:00) Hawaii", "(GMT-09:30) Marquesas Islands",
        "(GMT-09:00) Gambier Islands", "(GMT-09:00) Alaska", "(GMT-08:00) Tijuana, Baja California",
        "(GMT-08:00) Pitcairn Islands", "(GMT-08:00) Pacific Time (US & Canada)",
        "(GMT-07:00) Mountain Time (US & Canada)", "(GMT-07:00) Chihuahua, La Paz, Mazatlan", "(GMT-07:00) Arizona",
        "(GMT-06:00) Saskatchewan, Central America", "(GMT-06:00) Guadalajara, Mexico City, Monterrey",
        "(GMT-06:00) Easter Island", "(GMT-06:00) Central Time (US & Canada)", "(GMT-05:00) Eastern Time (US & Canada)",
        "(GMT-05:00) Cuba", "(GMT-05:00) Bogota, Lima, Quito, Rio Branco", "(GMT-04:30) Caracas",
        "(GMT-04:00) Santiago", "(GMT-04:00) La Paz", "(GMT-04:00) Faukland Islands", "(GMT-04:00) Manaus, Amazonas, Brazil",
        "(GMT-04:00) Atlantic Time (Goose Bay)", "(GMT-04:00) Atlantic Time (Canada)", "(GMT-03:30) Newfoundland",
        "(GMT-03:00) UTC-3", "(GMT-03:00) Montevideo", "(GMT-03:00) Miquelon, St. Pierre", "(GMT-03:00) Greenland",
        "(GMT-03:00) Buenos Aires", "(GMT-03:00) Brasilia, Distrito Federal, Brazil", "(GMT-02:00) Mid-Atlantic", "(GMT-01:00) Cape Verde Is.",
        "(GMT-01:00) Azores", "(UTC) Coordinated Universal Time", "(GMT) Greenwich Mean Time : Belfast", "(GMT) Greenwich Mean Time : Dublin",
        "(GMT) Greenwich Mean Time : Lisbon", "(GMT) Greenwich Mean Time : London", "(GMT) Monrovia, Reykjavik",
        "(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna",
        "(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague",
        "(GMT+01:00) Brussels, Copenhagen, Madrid, Paris", "(GMT+01:00) West Central Africa", "(GMT+01:00) Windhoek",
        "(GMT+02:00) Beirut", "(GMT+02:00) Cairo", "(GMT+02:00) Gaza", "(GMT+02:00) Harare, Pretoria",
        "(GMT+02:00) Jerusalem", "(GMT+02:00) Minsk", "(GMT+02:00) Syria",
        "(GMT+03:00) Moscow, St. Petersburg, Volgograd", "(GMT+03:00) Nairobi", "(GMT+03:30) Tehran",
        "(GMT+04:00) Abu Dhabi, Muscat", "(GMT+04:00) Yerevan", "(GMT+04:30) Kabul", "(GMT+05:00) Ekaterinburg",
        "(GMT+05:00) Tashkent", "(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "(GMT+05:45) Kathmandu",
        "(GMT+06:00) Astana, Dhaka", "(GMT+06:00) Novosibirsk", "(GMT+06:30) Yangon (Rangoon)",
        "(GMT+07:00) Bangkok, Hanoi, Jakarta", "(GMT+07:00) Krasnoyarsk",
        "(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "(GMT+08:00) Irkutsk, Ulaan Bataar", "(GMT+08:00) Perth",
        "(GMT+08:45) Eucla", "(GMT+09:00) Osaka, Sapporo, Tokyo", "(GMT+09:00) Seoul", "(GMT+09:00) Yakutsk",
        "(GMT+09:30) Adelaide", "(GMT+09:30) Darwin", "(GMT+10:00) Brisbane", "(GMT+10:00) Hobart",
        "(GMT+10:00) Vladivostok", "(GMT+10:30) Lord Howe Island", "(GMT+11:00) Solomon Is., New Caledonia",
        "(GMT+11:00) Magadan", "(GMT+11:30) Norfolk Island", "(GMT+12:00) Anadyr, Kamchatka",
        "(GMT+12:00) Auckland, Wellington", "(GMT+12:00) Fiji, Kamchatka, Marshall Is.", "(GMT+12:45) Chatham Islands",
        "(GMT+13:00) Nuku'alofa", "(GMT+14:00) Kiritimati");
    tryRenewTimeZoneData();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() { 
          @Override
           public void run() { 
                   TimeZoneUtil.tryRenewTimeZoneData();
          }
    };
    timer.schedule(task, timeZoneOffsetExpiresAfterMs, timeZoneOffsetExpiresAfterMs);
  }

  public static TimeZone getServerTimeZone() {
    return serverTimeZone;
  }

  public static TimeZone getTimeZone(String timezoneId) {
    return TimeZone.getTimeZone(timezoneId);
  }

  public static int offsetRelativeToServer(TimeZone localTimeZone, Date d) {
    return timeZoneOffsetDiff(serverTimeZone, localTimeZone, d);
  }

  public static int timeZoneOffsetDiff(TimeZone from, TimeZone to, Date atDate) {
    long epochTime = atDate.getTime();
    int baseOffset = from == null ? 0 : from.getOffset(epochTime);
    int toOffset = to == null ? 0 : to.getOffset(epochTime);
    return toOffset - baseOffset;
  }

  public static Date transformBetweenTimeZones(Date d, TimeZone from, TimeZone to) {
    if (d != null) {
      int diff = timeZoneOffsetDiff(from, to, d);
      return diff == 0 ? d : new Date(d.getTime() + diff);
    } else {
      return d;
    }
  }

  // Returns the first matching time zone that has the given offset (in
  // milliseconds) compared to UTC at the specified `atDate`
  // Returns null when no matching time zone was found
  public static TimeZone offsetMillisToTimeZone(Date atDate, Integer offsetMillis) {
    // Integer offsetMillisNoDST = offsetMillis + (3600*1000);
    // //we might be dealing with an offset based on a date with DST enabled,
    // need to check the offset (not the `rawOffset`) for the given `atDate`
    // List<String> timeZoneIds = new ArrayList<String>();
    // timeZoneIds.addAll( Arrays.asList(
    // TimeZone.getAvailableIDs(offsetMillisNoDST)) );
    // timeZoneIds.addAll( Arrays.asList(
    // TimeZone.getAvailableIDs(offsetMillis)) );

    // Use only supported time zone ids that are available in this elib

    long epochTime = atDate.getTime();
    for (String tzid : timeZoneIds) {
      if (!tzid.isEmpty()) {
        TimeZone tz = TimeZone.getTimeZone(tzid);
        Integer offsetAtDate = tz.getOffset(epochTime);
        if (offsetAtDate.equals(offsetMillis)) {
          return tz;
        }
      }
    }
    return null;
  }

  public static Date toServerTime(Date d, TimeZone timezone) {
    return transformBetweenTimeZones(d, timezone, serverTimeZone);
  }

  public static Date toLocalTime(Date d, TimeZone timezone) {
    return transformBetweenTimeZones(d, serverTimeZone, timezone);
  }

  public static Date toUTCTime(Date d, TimeZone timezone) {
    return transformBetweenTimeZones(d, serverTimeZone, null);
  }

  public static List<String> timeZoneLabels() {
    return timeZoneLabels;
  }

  public static List<String> timeZoneIds() {
    return timeZoneIds;
  }

  public static String displayName(TimeZone tz) {
    String name = idToNameMap.get(tz.getID());
    return name != null ? name.replaceFirst("\\(GMT.*\\)\\s", "") : tz.getDisplayName();
  }

  public static String fullDisplayName(TimeZone tz) {
    String name = idToNameMap.get(tz.getID());
    return name != null ? name : tz.getDisplayName();
  }

  private static Map<String, String> idToNameMap;

  private static void initIdToNameMap() {
    Map<String, String> newMap = new HashMap<String, String>();
    List<String> ids = timeZoneIds();
    List<String> labels = timeZoneLabels();
    for (int idx = 0; idx < ids.size(); idx++) {
      newMap.put(ids.get(idx), labels.get(idx));
      System.out.println("Label1:" + labels.get(idx));
    }
    idToNameMap = newMap;
  }

  private static List<Integer> timeZoneOffsetMinutes;
  
  private static void tryRenewTimeZoneData() {
      List<Integer> newOffsetList = new ArrayList<Integer>();
      List<String> newLabelList = new ArrayList<String>();
      
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("XXX"); //get GMT offset, i.e. +01:00
      
      for (int idx = 0; idx < timeZoneIds().size(); idx++) {
        TimeZone tz = TimeZone.getTimeZone( timeZoneIds().get(idx) );
        Integer minuteOffset = tz.getOffset(date.getTime()) / (60 * 1000);
        newOffsetList.add(minuteOffset);
        
        sdf.setTimeZone(tz);
        String offsetSuffix = sdf.format(date).replace("Z", "");
        String labelFixed = timeZoneLabels.get(idx).replaceAll("\\(GMT[^\\)]*\\)", "(GMT" + offsetSuffix + ")");
        newLabelList.add(labelFixed);
        
      }
      timeZoneOffsetMinutes = newOffsetList;
      timeZoneLabels = newLabelList;
      initIdToNameMap();
  }
  
  // offsets in minutes for the timezone ids in `timeZoneIds()` based on current
  // time (or at most 30 minutes)
  public static List<Integer> timeZoneOffsetMinutes() {
    return timeZoneOffsetMinutes;
  }
  
}