package nl.hr.EncryptedChat.server.poller;

import com.rabbitmq.client.*;

public class Recv {
	
	  private final String QUEUE_NAME;
	  private final String HOST_NAME;
	  private final String USERNAME;
	  private final String PASSWORD;
	  private final int MESSAGE_THRESHOLD;
	  
	  public Recv(String queueName, String hostName, String username, String password, int messageThreshold){
		  QUEUE_NAME = queueName;
		  HOST_NAME = hostName;
		  USERNAME = username;
		  PASSWORD = password;
		  MESSAGE_THRESHOLD = messageThreshold;
	  }

	  /**
	   * @throws Exception
	   */
	  public void setup() throws Exception {
		  //setup connection factory
		  ConnectionFactory factory = new ConnectionFactory();
		  factory.setHost(HOST_NAME);
		  factory.setUsername(USERNAME);
		  factory.setPassword(PASSWORD);
		  factory.setPort(5671);
		  factory.useSslProtocol();
		  
		  //setup channel and consumer
		  final Connection connection = factory.newConnection();
		  final Channel channel = connection.createChannel();
		  channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		  channel.basicQos(MESSAGE_THRESHOLD);
		  final Consumer consumer = new EncryptedChatConsumer(channel, HOST_NAME, USERNAME, PASSWORD);
		  channel.basicConsume(QUEUE_NAME, false, consumer);
	  }

}
