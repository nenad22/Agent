package messages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.security.auth.spi.Users.User;

import com.google.gson.Gson;

import main.App;
import rest.AgentEndpoints;

@ServerEndpoint(value = "/websocket")
@Stateless
public class WebSocket {

	public static AgentEndpoints ae = new AgentEndpoints();
	public static Gson gson = new Gson();
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
		System.out.println("Websocket recieved a message on: " + App.me.getAlias() + "\n" + message);
		int hasJson = message.indexOf('{');
		String methodName = "";
		String json = "";
		Object arg = null;

		if (hasJson == -1) {
			methodName = message;
		} else {
			json = message.substring(hasJson);
			methodName = message.substring(0, hasJson);
			System.out.println(json);
		}
		if (!methodName.equals("aclm")) {
			Method method = AgentEndpoints.getMethod(methodName);
			Class[] classArgs = method.getParameterTypes();
			if (classArgs.length > 0)
				arg = gson.fromJson(json, classArgs[0]);
			try {
				Object ret = method.invoke(ae, arg);
				json = gson.toJson(ret);
				session.getBasicRemote().sendText(methodName + json);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			ae.sendMessageToAgent(json);
		}
	}

}