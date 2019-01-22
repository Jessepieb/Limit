package com.nabalexa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        try (
                Socket echoSocket = new Socket("localhost", 8000);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
        ){
            out.write('a');
            out.flush();
            String inString;
            while((inString = in.readLine()) != null){
                System.out.println("String: " + inString);
                if(inString.equals("done")){
                    System.out.println("true");
                }
            }
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
