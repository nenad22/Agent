package messages;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
//TODO change queue make a new one
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/FromUserToChat") })
public class JMConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		//TODO establish protocol
		// TODO find agent
		// TODO pass on the message
		ObjectMessage tmsg = (ObjectMessage) message;
		try {
			Object messageText = tmsg.getObject();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
