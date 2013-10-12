package de.devboost.audioserver.webapp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class AudioServiceMessageInbound extends MessageInbound {
	
	private final static Logger logger = Logger.getLogger(AudioServiceMessageInbound.class.getName());

	public AudioServiceMessageInbound(int byteBufferMaxSize, int charBufferMaxSize) {

		super();
		AudioServlet.addInbound(this);
		setByteBufferMaxSize(byteBufferMaxSize);
		setCharBufferMaxSize(charBufferMaxSize);
	}

	/**
	 * Converts the given {@link ByteBuffer} to a byte array.
	 * 
	 * @param byteBuffer the buffer to be converted
	 * @return the buffer's contents
	 */
	public byte[] toByteArray(ByteBuffer byteBuffer) {
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes, 0, bytes.length);
		return bytes;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
		logger.entering(AudioServiceMessageInbound.class.getName(), "onBinaryMessage");
		
		// TODO byte[] bytes = toByteArray(byteBuffer);
	}

	public static Charset charset = Charset.forName("UTF-8");

	public String toString(ByteBuffer byteBuffer) {
		CharsetDecoder decoder = charset.newDecoder();
		String data = "";
		try {
			int oldPosition = byteBuffer.position();
			data = decoder.decode(byteBuffer).toString();
			// reset buffer's position to its original so it is not altered:
			byteBuffer.position(oldPosition);
			return data;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't convert byte buffer to string.", e);
			return null;
		}
	}

	@Override
	protected void onTextMessage(CharBuffer messageBuffer) throws IOException {
		logger.entering(AudioServiceMessageInbound.class.getName(), "onTextMessage");

		//String message = messageBuffer.toString();
		push("{ text : \"example text\" , audio : \"\" }");
	}

	public void push(String message) {
		try {
			WsOutbound wsOutbound = getWsOutbound();
			wsOutbound.writeTextMessage(CharBuffer.wrap(message.toCharArray()));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can't push message.", e);
		}
	}
}
