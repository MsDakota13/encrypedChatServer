package nl.hr.EncryptedChat.server.database;

public class Connector implements iDBConnector {
	
	private iDBConnector connector;
	
	public Connector(iDBConnector connector){
		this.connector = connector;
	}
	
	public String getUser(String key){
		return connector.getUser(key);
	}
	
	public void addUser(String key, String channelName){
		connector.addUser(key, channelName);
	}
	
	public void updateUser(String key, String channelName){
		connector.updateUser(key, channelName);
	}
	
	public void removeUser(String key){
		connector.removeUser(key);
	}
	
	public boolean containsRoutingKey(String routingKey){
		return connector.containsRoutingKey(routingKey);
	}

}
