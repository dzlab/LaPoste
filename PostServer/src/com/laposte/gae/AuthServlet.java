package com.laposte.gae;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.*;
import com.laposte.gae.shared.*;

@SuppressWarnings("serial")
public class AuthServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setContentType("text/plain");

			StringBuilder url = new StringBuilder();

			url.append("https://www.google.com/accounts/ClientLogin");
			url.append("?Email=" + URLEncoder.encode(ServerConfig.GoogleAccount, "UTF-8"));
			url.append("&Passwd=" + URLEncoder.encode(ServerConfig.GoogleAccountPassword, "UTF-8"));
			url.append("&accountType=GOOGLE");
			url.append("&source=Test-C2DM");
			url.append("&service=ac2dm");

			String result = download(url.toString());

			resp.getWriter().println(result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String download(String url) throws java.io.IOException {
		InputStream s = null;
		InputStreamReader r = null;
		StringBuilder content = new StringBuilder();
		try {
			s = (java.io.InputStream) new URL(url).getContent();

			r = new InputStreamReader(s, "UTF-8");
/*
			char[] buffer = new char[4 * 1024];
			int n = 0;
			while (n >= 0) {
				n = r.read(buffer, 0, buffer.length);
				if (n > 0) {
					content.append(buffer, 0, n);
				}
			}
*/
			// Read the response
			BufferedReader reader = new BufferedReader(r);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Auth=")) {
					ServerConfig.AuthToken = line.substring(5);
				}
			}
			content.append(ServerConfig.AuthToken);
		} finally {
			if (r != null)
				r.close();
			if (s != null)
				s.close();
		}
		return content.toString();
	}
}
