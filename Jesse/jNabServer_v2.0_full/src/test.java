import jNab.core.server.*;
import jNab.core.bunny.*;
import jNab.core.plugins.*;
import java.io.File;

/**
  * This is a small, quick and dirty example, of how to integrate jNabServer
  * into a larger program. MicroServer is created and Nabaztag is created and
  * a plugin registered to it. All this information is read from files when
  * jNab is used. Configration server is not started as all configuration is
  * done with Java code.
  */
public class test {

	/**
	  * Assumes two or three command line arguments: nabaztag serial,
	  * server ip and optional server port (default 80).
	  */
	public static void main(String[] args) {
		if (args.length < 2)
			System.exit(0);
		String nabaztagSerial = args[0];
		String serverAddress = args[1];
		int serverPort = 80;
		if (args.length > 2)
			serverPort = Integer.parseInt(args[2]);
		File resourcesPath = new File("./files");
		File pluginsPath = new File("./files/plugins");
		MicroServer microServer = new MicroServer(serverAddress, serverPort, resourcesPath, pluginsPath);
		// Enabling all logging features, using stderr and stdout as logging stream
		microServer.setDebugLoggingStream(System.out);
		microServer.setErrorLoggingStream(System.err);
		microServer.setInfoLoggingStream(System.out);

		Bunny nabaztag = new Bunny(nabaztagSerial);
		microServer.getBurrow().addBunny(nabaztag);

		AbstractPlugin plugin = new EarPlugin(microServer.getChoregraphyLibrary());
		nabaztag.addPlugin(plugin);

		// Starting micro server
		microServer.start();
	}

}
