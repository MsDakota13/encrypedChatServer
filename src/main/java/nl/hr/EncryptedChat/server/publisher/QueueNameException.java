package nl.hr.EncryptedChat.server.publisher;

/**
 * 
 * @author MrDemnoc
 * 
 * Custom exception made to indicate null values in queue names
 *
 */

public class QueueNameException extends Exception {

	public QueueNameException(){
		
	}
	
	public QueueNameException(String message){
		super(message);
	}

}
