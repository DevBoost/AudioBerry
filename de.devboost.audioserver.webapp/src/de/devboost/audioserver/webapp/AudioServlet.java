package de.devboost.audioserver.webapp;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

public class AudioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final static Set<AudioServiceMessageInbound> inbounds = new LinkedHashSet<AudioServiceMessageInbound>();
	
	public static void addInbound(AudioServiceMessageInbound inbound) {
		inbounds.add(inbound);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String text = req.getParameter("text");
		if (text != null) {
			byte[] waveFile = getWaveFile(text);
			for (AudioServiceMessageInbound inbound : inbounds) {
				String waveBase64 = Base64.encodeBase64String(waveFile);
				String message = "{ text : \"" + text + "\" , audio : \"" + waveBase64 + "\"}";
				inbound.push(message.getBytes("UTF-8"));
			}
		}
		
		resp.getWriter().write("OK");
		super.doGet(req, resp);
	}

	private byte[] getWaveFile(String text) {
		return new byte[20];
	}
}
