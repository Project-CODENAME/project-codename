void setup()  
 {  
  Serial.begin(9600);  
 }  
 void loop()  
 {  
  char c;
  if(Serial.available())  
  {  
   //random numbers for now 
   float lat = (rand() % 10)+44.1;
   float lon = (rand() % 10)+144.1;
   float alt = (rand() % 100)+1087.1;
   float p = (rand() % 400)+500.1;
   float BMP180tem = (rand() % 40)-20.1;
   float BMP180altEST = pressureToAltitude(p);
   float ST21tem = (rand() % 40)-20.1;
   float rh = rand()%20+0.1;
   
   Serial.write(makeMessage(lat,lon,alt,p,BMP180tem,BMP180altEST,ST21tem,rh);

   //determine how much time should pass before we pass on the information
   delay(5000); 
  }  
 }

String makeMessage(float lat, float lon, float alt, float p, float BMP180tem, float BMP180altEST, float ST21tem, float rh){
    String messageToBe = "";
    messageToBe = messageToBe + lat;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe+lon;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe+alt;
    messageToBe = messageToBe + p;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe + BMP180tem;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe + BMP180altEST;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe + ST21tem;
    messageToBe = messageToBe+"-";
    messageToBe = messageToBe + rh;
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
