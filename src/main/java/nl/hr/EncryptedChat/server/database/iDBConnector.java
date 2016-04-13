/**
 * 
 */
package nl.hr.EncryptedChat.server.database;

/**
 * @author MrDemnoc
 *
 */
public interface iDBConnector {
	
	public String getUser(String key);
	public void addUser(String key, String routingKey);
	public void removeUser(String key);
	public void updateUser(String key, String routingKey);
	public boolean containsRoutingKey(String routingKey);

}
