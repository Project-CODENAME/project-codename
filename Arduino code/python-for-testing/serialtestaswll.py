import serial

ser=serial.Serial('COM8', 9600, timeout=0)
while True:
    response=input("message: ")
    ser.write(bytes(response, 'utf-8'))
