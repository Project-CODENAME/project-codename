import csv
from dateutil import parser
from datetime import timedelta
with open('K0DNM_APRS.csv', 'r') as csvfile, open('data.csv', 'r') as datafile, open('newdata.csv', 'w') as writefile:
    aprsreader = csv.reader(csvfile)
    datareader = csv.reader(datafile)
    datawriter = csv.writer(writefile)
    i = 0
    j = 0
    for row in aprsreader:
        for rowData in datareader:
            try:
                if float(row[1]) < 5:
                    break
            except:
                break
            if j < i:
                pass
            j+=1
            try:
                if abs((parser.parse(row[0])-parser.parse(rowData[6])-timedelta(hours=6)).seconds) < 60 or abs((parser.parse(row[0])-parser.parse(rowData[6])-timedelta(hours=6)).seconds-86400) < 60:
                    print("line")
                    datawriter.writerow(rowData+row)
                    i = j
                    break
                else:
                    datawriter.writerow(rowData)
            except:
                break
