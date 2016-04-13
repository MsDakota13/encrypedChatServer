/**
 * 
 */
package nl.hr.EncryptedChat.server.database.implementations;

import java.util.TreeMap;

import nl.hr.EncryptedChat.server.database.iDBConnector;

/**
 * @author MrDemnoc
 *
 */
public class TreeMapDB implements iDBConnector {
	
	private TreeMap<String, String> users = new TreeMap<String, String>();

	/* (non-Javadoc)
	 * @see nl.hr.EncryptedChat.server.database.iDBConnector#getUser(java.lang.String)
	 */
	public String getUser(String key) {
		if(users.containsKey(key)){
			return users.get(key);			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see nl.hr.EncryptedChat.server.database.iDBConnector#addUser(java.lang.String, java.lang.String)
	 */
	public void addUser(String key, String routingKey) {
		users.put(key,  routingKey);
	}

	/* (non-Javadoc)
	 * @see nl.hr.EncryptedChat.server.database.iDBConnector#removeUser(java.lang.String)
	 */
	public void removeUser(String key) {
		users.remove(key);
	}

	/* (non-Javadoc)
	 * @see nl.hr.EncryptedChat.server.database.iDBConnector#updateUser(java.lang.String, java.lang.String)
	 */
	public void updateUser(String key, String routingKey) {
		users.replace(key, routingKey);
	}

	@Override
	public boolean containsRoutingKey(String routingKey) {
		return users.containsValue(routingKey);
	}

}
