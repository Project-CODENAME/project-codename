import serial
import time

ser=serial.Serial('COM8', 9600, timeout=0)
while True:
    try:
        response=input("message: ")
        ser.write(bytes(response, 'utf-8'))
        data = ser.readline().decode('utf-8')
        if data=='': continue

        print(data)
        time.sleep(1)
    except serial.SerialTimeoutException:
        print('fuck')
        time.sleep(1)

