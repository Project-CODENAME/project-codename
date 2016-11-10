package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

/**
 * Taken from https://github.com/aron-bordin/Android-with-Arduino-Bluetooth
 * I honestly don't totally understand this code yet
 */

public class BluetoothAndroid extends Thread {

    //bluetooth variables
    private BluetoothAdapter mBlueAdapter = null;
    private BluetoothSocket mBlueSocket = null;
    private BluetoothDevice mBlueRobo = null;

    //messages streams
    OutputStream mOut;
    InputStream mIn;

    //private booleans--for some reason
    private boolean robotFound = false;
    private boolean connected = false;
    private int REQUEST_BLUE_ATIVAR = 10;

    //characteristics of the conversation
    private String robotName;
    private List<String> mMessages = new ArrayList<String>();
    private String TAG = "BluetoothConnector";
    private char DELIMITER = '#';

    private static BluetoothAndroid __blue = null;

    //static factory method - if we already have an instance return it, otherwise make a new one
    public static BluetoothAndroid getInstance(String n){
        return __blue == null ? new BluetoothAndroid(n) : __blue;
    }

    //similar static factory method - only if there's not argument
    public static BluetoothAndroid getInstance(){
        return __blue == null ? new BluetoothAndroid() : __blue;
    }

    //initializer
    private  BluetoothAndroid(String Name){
        __blue = this;
        try {
            for(int i = 0; i < 2048; i++){
                mMessages.add("");
            }
            robotName = Name;

            //gets Bluetooth adapter
            mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBlueAdapter == null) {
                LogError("\t\t[#]Phone does not support bluetooth!!");
                return;
            }
            if (!isBluetoothEnabled()) {
                LogError("[#]Bluetooth is not activated!!");
            }

            //scans for a bluetooth device with that robotName
            Set<BluetoothDevice> paired = mBlueAdapter.getBondedDevices();
            if (paired.size() > 0) {
                for (BluetoothDevice d : paired) {
                    if (d.getName().equals(robotName)) {
                        mBlueRobo = d;
                        robotFound = true;
                        break;
                    }
                }
            }

            //otherwise there's a problem
            if (!robotFound)
                LogError("\t\t[#]There is not robot paired!!");

        }catch (Exception e){
            //there's a bigger problem here
            LogError("\t\t[#]Erro creating Bluetooth! : " + e.getMessage());
        }

    }

    //This is for if you just call new() without arguments
    BluetoothAndroid(){
        this("Arduino-Robot");
    }

    //function to check if bluetooth is enabled
    public boolean isBluetoothEnabled(){
        return mBlueAdapter.isEnabled();
    }

    //connect to the robot
    public boolean Connect(){
        if(!robotFound)
            return false;
        try{
            LogMessage("\t\tConncting to the robot...");

            //wtf? mac address? anyway, connect to the socket and get streams
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mBlueSocket = mBlueRobo.createRfcommSocketToServiceRecord(uuid);
            mBlueSocket.connect();
            mOut = mBlueSocket.getOutputStream();
            mIn = mBlueSocket.getInputStream();
            connected = true;
            this.start();
            LogMessage("\t\t\t" + mBlueAdapter.getName());
            LogMessage("\t\tOk!!");
            return true;

        }catch (Exception e){
            //fuuuuuuuuu...
            LogError("\t\t[#]Error while connecting: " + e.getMessage());
            return false;
        }
    }

    public void run(){
        //reads the messages
        while (true) {
            if(connected) {
                try {
                    byte ch, buffer[] = new byte[1024];
                    int i = 0;

                    String s = "";

                    //adds characters to a message until it hits a delimiter(#)
                    while((ch=(byte)mIn.read()) != DELIMITER){
                        buffer[i++] = ch;
                    }
                    buffer[i] = '\0';

                    final String msg = new String(buffer);

                    MessageReceived(msg.trim());
                    LogMessage("[Blue]:" + msg);

                } catch (IOException e) {
                    //oh no
                    LogError("->[#]Failed to receive message: " + e.getMessage());
                }
            }
        }
    }

    private void MessageReceived(String msg){
        try {
            //add the message received to the array of mMessages
            mMessages.add(msg);
            try {
                //calls itself
                this.notify();
            }catch (IllegalMonitorStateException e){
                //......
            }
        } catch (Exception e){
            //oh, shit
            LogError("->[#] Failed to receive message: " + e.getMessage());
        }
    }

    public boolean hasMensagem(int i){
        try{
            String s = mMessages.get(i);
            if(s.length() > 0)
                return true;
            else
                return false;
        } catch (Exception e){
            return false;
        }
    }

    //get messages, called from outside
    public String getMessage(int i){
        return mMessages.get(i);
    }

    //clear messages, called from outside
    public void clearMessages(){
        mMessages.clear();
    }

    //counts messages, called from outside
    public int countMessages(){
        return mMessages.size();
    }

    //gets last message, called from outside
    public String getLastMessage(){
        if(countMessages() == 0)
            return "";
        return mMessages.get(countMessages()-1);
    }

    //sends last message, called from outside
    public void SendMessage(String msg){
        try {
            if(connected) {
                mOut.write(msg.getBytes());
            }

        } catch (IOException e){
            LogError("->[#]Error while sending message: " + e.getMessage());
        }
    }

    //debug logger
    private void LogMessage(String msg){
        Log.d(TAG, msg);
    }

    //error logger
    private void LogError(String msg){
        Log.e(TAG, msg);
    }

    //does delimiter stuff, called from outside
    public void setDelimiter(char d){
        DELIMITER = d;
    }
    public char getDelimiter(){
        return DELIMITER;
    }

}