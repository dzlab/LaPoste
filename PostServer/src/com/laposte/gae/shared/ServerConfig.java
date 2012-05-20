package com.laposte.gae.shared;

public class ServerConfig {
	
	/** History table of letters' state changes */
	public static final String HISTORY_TABLE = "history";
	
	// To get AuthToken you running /GetAuthFromGoogle and take the auth string
	// and put here
	public static String AuthToken = "YourFirstAuthToken";

	// To get your registration id you running the android application for this
	// project and see in the LogCat console in Eclipse What registration id you
	// got and put it here
	public static String DeviceRegistrationID = "APA91bFqRGI84TwJzVqNCmRhZTxMi5C4znDBrTDjvJ1vAlF3nP2OLYOPcqTwH96erWpClg9JE4G5pKunBawyzvqjmpZKuQjOoSDPCvgdt28yyxe0atXEQ0K09QOooP3wTRl8fIOnfE0nzkCPqsfFkS6zK0lZZ2snNw";

	// Your Google Account, this is the "Role account", to your knowledge there
	// has been a bug that if the role account is the same as the device syncing
	// with its not getting the messages, so try to create a new google account
	// for c2dm is my tip
	public static String GoogleAccount = "droidxmann@gmail.com";

	// Your Google Account Password for the "Role account"
	public static String GoogleAccountPassword = "droidxmann00";

}
