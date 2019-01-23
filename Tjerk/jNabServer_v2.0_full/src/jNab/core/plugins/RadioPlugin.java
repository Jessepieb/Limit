package jNab.core.plugins;

import jNab.core.choreography.*;
import jNab.core.events.*;
import jNab.core.protocol.*;
import jNab.ext.helperPlugins.*;
import jNab.ext.persistency.Serializer;
import jNab.core.events.ClickEventListener;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class RadioPlugin extends AbstractPlugin implements ClickEventListener {

	private static String PLUGIN_NAME = "Radio_Plugin";
    private static String[] PARAMETERS = {};
    
	public RadioPlugin() {
		super(PLUGIN_NAME, PARAMETERS);
	}
	
	private static String[] channel = {
	        "http://radio.flex.ru:8000/radionami",
	        "http://stream-uk1.radioparadise.com/mp3-32",
	        "https://stream.gal.io/arrow"
	    };
	    
	private static void removeSecurity() {
	        for (int i = 0; i < channel.length; i++) {
	            if(channel[i].substring(0,5).equals("https")) {
	                channel[i] = "http" + channel[i].substring(5);
	            }
	        }
	    }
	
	private static void playRadioStream ( String spec ) throws IOException, JavaLayerException
    {
        // Connection
        URLConnection urlConnection = new URL ( spec ).openConnection ();

        // If you have proxy
        //        Properties systemSettings = System.getProperties ();
        //        systemSettings.put ( "proxySet", true );
        //        systemSettings.put ( "http.proxyHost", "host" );
        //        systemSettings.put ( "http.proxyPort", "port" );
        // If you have proxy auth
        //        BASE64Encoder encoder = new BASE64Encoder ();
        //        String encoded = encoder.encode ( ( "login:pass" ).getBytes () );
        //        urlConnection.setRequestProperty ( "Proxy-Authorization", "Basic " + encoded );

        // Connecting
        urlConnection.connect ();

        // Playing
        System.out.println("Playing");
        Player player = new Player ( urlConnection.getInputStream () );
        player.play ();
    }

	@Override
	public void onSingleClick() {
	        
		 //Possible if it works: loadChannelsFromFIle();
        removeSecurity();
        
        try
        {
            playRadioStream ( channel[1] );
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
        }
        catch ( JavaLayerException e )
        {
            e.printStackTrace ();
        }
	}

	@Override
	public void onDoubleClick() {
		// TODO Auto-generated method stub
		
	}

}
