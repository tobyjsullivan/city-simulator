package citysim;

import java.util.UUID;


public class ErrorHandler {
	public static void FailFromException(Exception e, String message, UUID objectId) {
		System.out.println("ERROR: " + message + objectId);
		System.exit(1);
	}
}
