#include <Wire.h>

void setup()  
 {  
  Serial.begin(9600);  
  pinMode(LED_BUILTIN, OUTPUT);
  Wire.begin(91);                // join i2c bus with address #8
  Wire.onReceive(receiveEvent); // register event
  
 }  
 float lat = 0.0;
 float lon = 0.0;
 float alt = 0.0;
 float p = 0.0;
 float temp = 0.0;
 float altEST = 0.0;
 float rh = 0.0;
 float course = 0.0;
 float gps_speed = 0.0;
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
  lat = Wire.read();
  lon = Wire.read();
  course = Wire.read();
  gps_speed = Wire.read();
  alt = Wire.read();
  p = Wire.read();
  rh = Wire.read();
  temp = Wire.read();
  altEST = pressureToAltitude(p);  
  digitalWrite(LED_BUILTIN, HIGH);
  if(Serial.available())  
  {  
   Serial.print(makeMessage()+"\n");
   //determine how much time should pass before we pass on the information
  }
}
