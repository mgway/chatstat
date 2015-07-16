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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.matthewgalloway.stats.db.InsertDatapointCommand;
import com.matthewgalloway.stats.domain.Datapoint;
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
	
	@Autowired
	private SimpMessagingTemplate wsTemplate;
	
	
	@MessageMapping("/hello")
    public void handle(String streamerName) {
		
		if (streamerName.trim().isEmpty()) {
			throw new StreamException("Streamer name is empty");
		}
		streamerName = streamerName.toLowerCase();
		
		Datapoint datapoint = client.getStreamData(streamerName);
		
		if (datapoint != null) {
			db.execute(new InsertDatapointCommand(datapoint));
			
			this.jmsTemplate.convertAndSend(this.queue, datapoint);
			this.wsTemplate.convertAndSend("/topic/" + streamerName + "/meta", datapoint);
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
