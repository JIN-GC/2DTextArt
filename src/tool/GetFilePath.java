package tool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetFilePath {

	public static String getWinDesktop() throws IOException {

		String path = "";
		String cmdline = "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v Desktop";
		String line = "";
		Process win = Runtime.getRuntime().exec(cmdline);
		BufferedReader br = new BufferedReader(new InputStreamReader(win.getInputStream()));
		while ((line = br.readLine()) != null) {
			if (line.indexOf("Desktop") != -1) {
				path = line.substring(line.indexOf("C"), line.length());
			}
		}
		return path + "\\";
	}
	


}
