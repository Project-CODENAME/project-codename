package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject.*;
import java.util.Date;

public class Main {

    //this is the singular function
    public static void main(String[] args) {
        //
        ArrayList<DataPoint> dataPoint;
        try {
            //deserializes data from file provided in args[0]
            FileInputStream fileIn = new FileInputStream(args[0]);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //reads object, converts to our array
            Object e = in.readObject();
            in.close();
            fileIn.close();
            System.out.println(e.getClass());
            System.out.println(e.toString());
            dataPoint = (ArrayList<DataPoint>) e;
        }catch (Exception c) {
            //generic exception catch
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
        try {
            //prints to csv--don't worry about this stuff
            PrintWriter pw = new PrintWriter(new File("data.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("time");
            sb.append(',');
            sb.append("a_x");
            sb.append(',');
            sb.append("a_y");
            sb.append(',');
            sb.append("a_z");
            sb.append(',');
            sb.append("p");
            sb.append('\n');


            for (DataPoint pnt : dataPoint) {
                //also prints it to command line, not necessary in production
                System.out.println(pnt.time);
                System.out.println("A_X: " + pnt.a_x + ", A_Y: " + pnt.a_y + ", A_Z:" + pnt.a_z + ", P: " + pnt.p);
                System.out.println("lat: " + pnt.lat + ", lon: " + pnt.lon + ", alt:" + pnt.alt);
                System.out.println("====");
                sb.append(pnt.time);
                sb.append(',');
                sb.append(pnt.a_x);
                sb.append(',');
                sb.append(pnt.a_y);
                sb.append(',');
                sb.append(pnt.a_z);
                sb.append(',');
                sb.append(pnt.p);
                sb.append('\n');
            }
            pw.write(sb.toString());
            pw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
