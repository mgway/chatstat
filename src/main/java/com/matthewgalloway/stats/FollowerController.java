package com.matthewgalloway.stats;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.matthewgalloway.stats.db.InsertDatapointStub;
import com.matthewgalloway.stats.domain.Stream;
import com.matthewgalloway.stats.framework.DatabaseService;

@Controller
public class FollowerController {
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("beginViewersUpdate")
	private Queue queue;
	
	@Autowired
	private transient DatabaseService db;
	
	@Autowired
	private transient TwitchApiClient client;
		
	
	@MessageMapping("/hello")
	@SendTo("/topic/meta")
    public Stream handle(String streamerName) {
		
		if (streamerName.trim().isEmpty()) {
			throw new StreamException("Streamer name is empty");
		}
		streamerName = streamerName.toLowerCase();
		
		final Stream stream = client.getStreamData(streamerName);
		
		if (stream != null) {
			InsertDatapointStub stub = new InsertDatapointStub(streamerName);
			db.execute(stub);
			final long datapointId = stub.getId();
			
			this.jmsTemplate.send(this.queue, new MessageCreator() {
	            public Message createMessage(Session session) throws JMSException {
	                ObjectMessage message = session.createObjectMessage(stream);
	                message.setLongProperty("datapointId", datapointId);
	                return message;
	            }
	        });
			
			return stream;
		}
		
        throw new StreamException("Stream is offline");
    }
	
	@MessageExceptionHandler
	@SendTo(value="/topic/error")
    public String handleException(StreamException exception) {
        return exception.getMessage();
    }
	
	private class StreamException extends RuntimeException  {
		private static final long serialVersionUID = 1L;

		public StreamException(String s) {
			super(s);
		}
	} 
}
