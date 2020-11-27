package thbt.webng.com.game.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HighScoreUtil {

	public static int getSmallestScore() throws IOException,
			NumberFormatException {
		String value = getValues("http://localhost/thbt/games/lines/smallestScore.asp");
		return Integer.parseInt(value);
	}

	public static int getHighestScore() {
		try {
			String value = getValues("http://localhost/thbt/games/lines/highestScore.asp");
			if (value == null || "".equals(value)) {
				return 0;
			}

			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return 0;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getTopHighScore() {
		try {
			return getValues("http://localhost/thbt/games/lines/topHighScore.asp");
		} catch (IOException e) {
			return "<html>Error occur while connect to server<html>";
		}
	}

	public static String sendHighScore(String name, int score, String playTime) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("score", Integer.toString(score));
		map.put("playTime", playTime);
		return doPost("http://localhost/thbt/games/lines/addHighScore.asp", map);
	}

	private static String getValues(String urlPath) throws IOException {
		StringBuffer buffer = new StringBuffer();

		try {
			URL url = new URL(urlPath);
			InputStream inputStream = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			String str;
			while ((str = reader.readLine()) != null) {
				buffer.append(str);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	private static String doPost(String urlString,
			Map<String, String> nameValuePairMap) {
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());

			boolean isFirst = true;
			for (String key : nameValuePairMap.keySet()) {
				String value = nameValuePairMap.get(key);

				if (isFirst) {
					isFirst = false;
				} else {
					writer.print("&");
				}

				writer.print(key + "=" + URLEncoder.encode(value, "UTF-8"));
			}
			writer.close();

			Scanner scanner = new Scanner(urlConnection.getInputStream());
			while (scanner.hasNext()) {
				buffer.append(scanner.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
}
