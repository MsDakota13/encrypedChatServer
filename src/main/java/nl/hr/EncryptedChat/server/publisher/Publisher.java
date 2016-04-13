package nl.hr.EncryptedChat.server.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Publisher {
	
	private static final String EXCHANGE_NAME = "ec.output";
	
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	private Channel channel;
	
	public Publisher(String host, String username, String password)  throws Exception {
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setPort(5671);
		factory.useSslProtocol();
	}
	
	public void sendMessage(String routingKey, String message) throws Exception {
		if(routingKey == null){
			throw new QueueNameException();
		}
		connection = factory.newConnection();
	    channel = connection.createChannel();
	    
	    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
	    channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
		
		System.out.println("[-] Message sent: " + message);
	    
	    channel.close();
	    connection.close();
	}

}
