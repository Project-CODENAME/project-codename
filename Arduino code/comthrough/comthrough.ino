void setup()  
 {  
  Serial.begin(9600);  
  pinMode(LED_BUILTIN, OUTPUT);
 }  
 float lat = 0.0;
 float lon = 0.0;
 float alt = 0.0;
 float p = 0.0;
 float BMP180temp = 0.0;
 float BMP180altEST = 0.0;
 float ST21temp = 0.0;
 float rh = 0.0;
 void loop()  
 {  
  char c;
  digitalWrite(LED_BUILTIN, LOW);
  delay(1000); 
  digitalWrite(LED_BUILTIN, HIGH);
  delay(1000);
  if(Serial.available())  
  {  
   //random numbers for now 
   lat = (rand() % 10)+44.1;
   lon = (rand() % 10)+144.1;
   alt = (rand() % 100)+1087.1;
   p = (rand() % 400)+500.1;
   BMP180temp = (rand() % 40)-20.1;
   BMP180altEST = pressureToAltitude(p);
   ST21temp = (rand() % 40)-20.1;
   rh = rand()%20+0.1;
   Serial.print(makeMessage()+"\n");
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
    messageToBe += BMP180temp;
    messageToBe += "-";
    messageToBe += BMP180altEST;
    messageToBe += "-";
    messageToBe += ST21temp;
    messageToBe += "-";
    messageToBe += rh;
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
