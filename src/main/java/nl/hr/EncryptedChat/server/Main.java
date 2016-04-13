/**
 * 
 */
package nl.hr.EncryptedChat.server;

import java.math.BigInteger;
import java.security.SecureRandom;

import nl.hr.EncryptedChat.server.poller.Recv;

/**
 * @author MrDemnoc
 *
 */
public class Main {
	private static final String QUEUE_NAME = "input";
	private static final String HOST_NAME = "188.166.37.53";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";
	private static final int MESSAGE_THRESHOLD = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startup();
	}
	
	private static void startup(){
		Recv backend = new Recv(QUEUE_NAME, HOST_NAME, USERNAME, PASSWORD, MESSAGE_THRESHOLD);
		try {
			backend.setup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
