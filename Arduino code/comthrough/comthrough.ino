#include <Wire.h>

/*
 * This was a failed attempt to connect the Phone and Tracksoar together through an Arduino Micro interacting with the two through Serial
 * and I2C respectively. It could be revived in the future by the community, but we'll see.
 * It should work - if you can make it work.
 */

void setup()  
 {  
  Serial.begin(9600);  
  pinMode(LED_BUILTIN, OUTPUT);
  Wire.begin(0x21);                // join i2c bus with address #8
  Wire.onReceive(receiveEvent); // register event
 }  
 String lat = "";
 String lon = "";
 String alt = "";
 String p = "";
 String temp = "";
 String altEST = "";
 String rh = "";
 String course = "";
 String gps_speed = "";
 void loop()  
 {  
  digitalWrite(LED_BUILTIN, LOW);
  delay(10000); 
  if(Serial.available())  
  {  
   Serial.print("non");
   //determine how much time should pass before we pass on the information
  }  
 }

String makeMessage(){
    String messageToBe = String("");
    messageToBe += lat;
    messageToBe += "-";
    messageToBe += lon;
    messageToBe += "-";
    messageToBe += alt;
    messageToBe += "-";
    messageToBe += p;
    messageToBe += "-";
    messageToBe += temp;
    messageToBe += "-";
    messageToBe += altEST;
    messageToBe += "-";
    messageToBe += rh;
    messageToBe += "-";
    messageToBe += course;
    messageToBe += "-";
    messageToBe += gps_speed;
    messageToBe +="#";
    return messageToBe;
}
float pressureToAltitude(float atmospheric)
{
  // Equation taken from BMP180 datasheet (page 16):
  //  http://www.adafruit.com/datasheets/BST-BMP180-DS000-09.pdf

  // Note that using the equation from wikipedia can give bad results
  // at high altitude.  See this thread for more information:
  //  http://forums.adafruit.com/viewtopic.php?f=22&t=58064

  //TODO change 101325 to the sea level pressure on day of launch!
  return 44330.0 * (1.0 - pow(atmospheric / 101325, 0.1903));
}

void receiveEvent(int howMany) {
  Serial.print("test");
  lat = Wire.read();
  lon = Wire.read();
  course = Wire.read();
  gps_speed = Wire.read();
  alt = Wire.read();
  p = Wire.read();
  rh = Wire.read();
  temp = Wire.read();
//  altEST = pressureToAltitude(p);  
  digitalWrite(LED_BUILTIN, HIGH);
  if(Serial.available())  
  {  
   Serial.print(makeMessage()+"\n");
   //determine how much time should pass before we pass on the information
  }
}
