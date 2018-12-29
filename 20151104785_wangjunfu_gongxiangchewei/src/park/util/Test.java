package park.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Test {
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		long millionSeconds = sdf.parse("2016/12/25 08:48").getTime();//����
		long currentTime = sdf.parse("2016/12/25 09:48").getTime();//System.currentTimeMillis();
		System.out.println((currentTime - millionSeconds)/3600000.0);
		double hour = (double)((currentTime - millionSeconds)/3600000.0 == 0 ? (currentTime - millionSeconds)/3600000.0 : (currentTime - millionSeconds)/3600000.0+1);
		System.out.println(hour);
	}
}
