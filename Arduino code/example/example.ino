#include <SoftwareSerial.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include "bluetooth.h"
#include <Adafruit_BMP085_U.h>


Bluetooth *blue = new Bluetooth("ExtSensorsRobot");
Adafruit_BMP085_Unified bmp = Adafruit_BMP085_Unified(10085);

void setup(){
  Serial.begin(9600);
  blue->setupBluetooth();
  if(!bmp.begin())
  {
    //rip 
  }
}


void loop(){
  sensors_event_t event;
  bmp.getEvent(&event);
  if (event.pressure){
    if(Serial.available()){

    //getting altitude with temperature
    float temperature;
    bmp.getTemperature(&temperature);
 
    /* Then convert the atmospheric pressure, SLP and temp to altitude    */
    /* Update this next line with the current SLP for better results ON THE DAY OF      */
    float seaLevelPressure = 1030.7;

    /*Because Arduino C++ is terrible*/
    String messageToBe = "";
    messageToBe = messageToBe + event.pressure;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe+event.temperature;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe+bmp.pressureToAltitude(seaLevelPressure,
                                        event.pressure,
                                        temperature);
    messageToBe = messageToBe+"#";
    // Length (with one extra character for the null terminator)
    int str_len = messageToBe.length() + 1; 

    // Prepare the character array (the buffer) 
    char char_array[str_len];

    // Copy it over 
    messageToBe.toCharArray(char_array, str_len);
    blue->Send(char_array); 
    }
  }
  delay(1000);
}
