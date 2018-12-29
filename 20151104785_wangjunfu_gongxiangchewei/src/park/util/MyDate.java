package park.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
	public static String getMyDate(){
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = simpleDateFormat.format(date);
		return currentTime;
	}
	
	public static String getMyDate_2(){
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = simpleDateFormat.format(date);
		return currentTime;
	}
}
