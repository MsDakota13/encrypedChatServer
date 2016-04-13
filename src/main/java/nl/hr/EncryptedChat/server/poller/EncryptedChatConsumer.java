package nl.hr.EncryptedChat.server.poller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import nl.hr.EncryptedChat.server.database.Connector;
import nl.hr.EncryptedChat.server.database.implementations.TreeMapDB;
import nl.hr.EncryptedChat.server.publisher.Publisher;
import nl.hr.EncryptedChat.server.publisher.QueueNameException;

import org.apache.commons.lang3.StringUtils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class EncryptedChatConsumer extends DefaultConsumer {
	
	private Channel channel;
	private final Connector connector = new Connector(new TreeMapDB());
	private final Publisher publisher;
	private SecureRandom random = new SecureRandom();

	public EncryptedChatConsumer(Channel channel, String host, String username, String password) throws Exception {
		super(channel);
		this.channel = channel;
		publisher = new Publisher(host, username, password);
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		  
		//split message
		List<String> messageEntry = Arrays.asList(new String(body, "UTF-8").split(":"));
		String toKey = messageEntry.get(0);
		String message = messageEntry.get(1);
		String fromKey = messageEntry.get(2);
		
		System.out.println("[+] Message recieved: " + fromKey + ":" + message + ":" + toKey);
			
		//check toKey and fromKey
		if (toKey.equals(fromKey)) {
			
			handleUsers(fromKey, message, toKey);
			
		} else {
			//get channel form db according to the toKey and send message
			try {
				publisher.sendMessage(connector.getUser(toKey), message + ":" + fromKey);
			} catch (QueueNameException e) {
				//handle delivery to non exsistant user
			} catch (Exception e) {
				
			}
		}
			
		channel.basicAck(envelope.getDeliveryTag(), false);
	}
	
	private void handleUsers(String fromKey, String message, String toKey){
		
		//check db for user
		if (connector.getUser(fromKey) != null) {
			//if exists
			List<String> messageEntry = Arrays.asList(message.split(";"));
			String command = messageEntry.get(0);
			String routingKey = messageEntry.get(1);
			switch (command) {
				case "update":
					//create and update channel key
					updateUser(fromKey, message, toKey, routingKey);
					break;
				case "remove":
					//handle removing users from connected database
					removeUser(fromKey, message, toKey, routingKey);
					break;
				case "deleteChannel":
					//handle deleting a channel
					deleteChannel(fromKey, message, toKey, routingKey);
					break;
				default:
					break;
			}
		} else {
			//if it does not exist
			connector.addUser(fromKey, message);
			try {
				publisher.sendMessage(connector.getUser(toKey), "ack:" + fromKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateUser(String fromKey, String message, String toKey, String routingKey){
		String newRoutingKey;
		do {
			newRoutingKey = StringUtils.substring(new BigInteger(500, random).toString(32), 0, 64);
		}while(connector.containsRoutingKey(newRoutingKey));
		try {
			publisher.sendMessage(connector.getUser(toKey), newRoutingKey + ":" + fromKey);
			connector.updateUser(fromKey, newRoutingKey);
		} catch (Exception e) {
			
		}
	}
	
	private void removeUser(String fromKey, String message, String toKey, String routingKey){
		if(connector.getUser(fromKey).equals(routingKey)){
			connector.removeUser(fromKey);
		}
	}
	
	private void deleteChannel(String fromKey, String message, String toKey, String routingKey){
		if(connector.getUser(fromKey).equals(routingKey)){
			try {
				channel.queueDelete(fromKey + routingKey);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
