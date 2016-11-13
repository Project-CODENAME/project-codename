void setup()  
 {  
  Serial.begin(9600);  
 }  
 void loop()  
 {  
  char c;
  if(Serial.available())  
  {  
   c = Serial.read();  
   Serial.print(c);  
  }  
 }
