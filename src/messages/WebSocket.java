package messages;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket")
@Stateless
public class WebSocket {

	/**
	 * Session id, session
	 */
	public static HashMap<String, Session> sessions = new HashMap<String, Session>();

	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) throws UnknownHostException {

		System.out.println(session.getRequestURI().toString());
		if (!sessions.containsKey(session.getId())) {
			sessions.put(session.getId(), session);
			System.out.println("Session added: " + session.getId());
		}

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Error: " + session.getId().toString());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, EncodeException {
		// TODO AOP
		//TODO  Make also w/ rest?
		System.out.println("Websocket recieved a message on");

	}
}