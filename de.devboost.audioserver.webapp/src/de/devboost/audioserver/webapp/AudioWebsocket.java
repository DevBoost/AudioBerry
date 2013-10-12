package de.devboost.audioserver.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class AudioWebsocket extends WebSocketServlet {

	private static final long serialVersionUID = -896237665605303127L;
	
	private volatile int byteBufSize;
    private volatile int charBufSize;

    @Override
    public void init() throws ServletException {
        super.init();
        byteBufSize = getInitParameterIntValue("byteBufferMaxSize", 2097152);
        charBufSize = getInitParameterIntValue("charBufferMaxSize", 2097152);
    }

    public int getInitParameterIntValue(String name, int defaultValue) {
        String val = this.getInitParameter(name);
        int result;
        if(null != val) {
            try {
                result = Integer.parseInt(val);
            }catch (Exception x) {
                result = defaultValue;
            }
        } else {
            result = defaultValue;
        }

        return result;
    }

    @Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
    	return new AudioServiceMessageInbound(byteBufSize, charBufSize);
	}

}
