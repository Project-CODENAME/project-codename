package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class parsing a file containing a serialized ArrayList of DataPoints into a .csv and .kml (although the kml no longer
 * functions as the phone is no longer taking internal GPS). The .csv file is the most important one and should be the
 * one used for data analysis.
 *
 * @author Alan Zhu
 * @version idk
 * @since Fall/Winter 2016
 */
public class Main {

    /**
     * this does the parsing
     *
     * @param args name of the file only
     */
    public static void main(String[] args) {
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
        } catch (Exception c) {
            //generic exception catch
            c.printStackTrace();
            return;
        }
        try {
            //prints to csv--don't worry about this stuff
            SimpleDateFormat sdf = new SimpleDateFormat("hhmmddMM");
            PrintWriter pw = new PrintWriter(new File("data.csv"));
            StringBuilder sb = new StringBuilder();

            // headers
            sb.append("n");
            sb.append(',');
            sb.append("a_x");
            sb.append(',');
            sb.append("a_y");
            sb.append(',');
            sb.append("a_z");
            sb.append(',');
            sb.append("p");
            sb.append(',');
            sb.append("time");
            sb.append(',');
            sb.append("g_x");
            sb.append(',');
            sb.append("g_y");
            sb.append(',');
            sb.append("g_z");
            sb.append(',');
            sb.append("rot_x");
            sb.append(',');
            sb.append("rot_y");
            sb.append(',');
            sb.append("rot_z");
            sb.append(',');
            sb.append("t");
            sb.append(',');
            sb.append("rh");
            sb.append(',');
            sb.append("mag_x");
            sb.append(',');
            sb.append("mag_y");
            sb.append(',');
            sb.append("mag_z");
            sb.append(',');
            sb.append("ext_lat");
            sb.append(',');
            sb.append("ext_lon");
            sb.append(',');
            sb.append("ext_alt");
            sb.append(',');
            sb.append("ext_p");
            sb.append(',');
            sb.append("ext_temp");
            sb.append(',');
            sb.append("ext_altEST");
            sb.append(',');
            sb.append("ext_rh");
            sb.append(',');
            sb.append("course");
            sb.append(',');
            sb.append("gps_speed");
            sb.append(',');
            sb.append("battery_percentage");
            sb.append(',');
            sb.append("battery_temp");
            sb.append(',');
            sb.append("a_actual_x");
            sb.append(',');
            sb.append("a_actual_y");
            sb.append(',');
            sb.append("a_actual_z");
            sb.append('\n');

            // headers for .kml file
            Namespace ns = Namespace.getNamespace("http://www.opengis.net/kml/2.2");
            Document kml = new Document();
            //Document ext_kml = new Document();
            Element root = new Element("kml", ns);
            //Document ext_root = new Element("kml", ns);
            kml.setRootElement(root);
            //Document ext_kml.setRootElement(ext_root);
            root.addNamespaceDeclaration(ns);
            //Document ext_root.addNamespaceDeclaration(ns);
            Element doc = new Element("Document");
            //Element ext_doc = new Element("Document");
            int iter = 0;

            Date standard = new Date();
            for (DataPoint pnt : dataPoint) {
                //also prints it to command line, not necessary in production
                if (iter == 0) {
                    standard = pnt.time;
                }
                System.out.println(pnt.time);
                System.out.println("A_X: " + pnt.a_x + ", A_Y: " + pnt.a_y + ", A_Z:" + pnt.a_z + ", P: " + pnt.p);
                System.out.println("lat: " + pnt.lat + ", lon: " + pnt.lon + ", alt:" + pnt.alt);
                System.out.println("====");

                // time after first in some unit (decaseconds?)
                sb.append((pnt.time.getTime() - standard.getTime()) / 10000);

                // prints to .csv
                iter++;
                sb.append(',');
                sb.append(pnt.a_x);
                sb.append(',');
                sb.append(pnt.a_y);
                sb.append(',');
                sb.append(pnt.a_z);
                sb.append(',');
                sb.append(pnt.p);
                sb.append(',');
                sb.append(pnt.time);
                sb.append(',');
                sb.append(pnt.g_x);
                sb.append(',');
                sb.append(pnt.g_y);
                sb.append(',');
                sb.append(pnt.g_z);
                sb.append(',');
                sb.append(pnt.rot_x);
                sb.append(',');
                sb.append(pnt.rot_y);
                sb.append(',');
                sb.append(pnt.rot_z);
                sb.append(',');
                sb.append(pnt.t);
                sb.append(',');
                sb.append(pnt.rh);
                sb.append(',');
                sb.append(pnt.mag_x);
                sb.append(',');
                sb.append(pnt.mag_y);
                sb.append(',');
                sb.append(pnt.mag_z);
                sb.append(',');
                sb.append(pnt.ext_lat);
                sb.append(',');
                sb.append(pnt.ext_lon);
                sb.append(',');
                sb.append(pnt.ext_alt);
                sb.append(',');
                sb.append(pnt.ext_p);
                sb.append(',');
                sb.append(pnt.ext_temp);
                sb.append(',');
                sb.append(pnt.ext_altEST);
                sb.append(',');
                sb.append(pnt.ext_rh);
                sb.append(',');
                sb.append(pnt.course);
                sb.append(',');
                sb.append(pnt.gps_speed);
                sb.append(',');
                sb.append(pnt.battery_percent);
                sb.append(',');
                sb.append(pnt.battery_temp);
                sb.append(',');
                sb.append(pnt.a_actual_x);
                sb.append(',');
                sb.append(pnt.a_actual_y);
                sb.append(',');
                sb.append(pnt.a_actual_z);
                sb.append('\n');

                // prints to .kml
                Element child = new Element("Placemark");
                child.addContent(new Element("name").addContent(dataPoint.indexOf(pnt) + ""));
                child.addContent(new Element("description").addContent(pnt.time + ""));
                Element point = new Element("Point");
                point.addContent(new Element("coordinates").addContent(pnt.lon + "," + pnt.lat + "," + pnt.alt));
                point.addContent(new Element("altitudeMode").addContent("absolute"));
                child.addContent(point);
                doc.addContent(child);

                // idk what this was for
                /*
                 Element ext_child = new Element("Placemark");
                 ext_child.addContent(new Element("name").addContent(dataPoint.indexOf(pnt)+""));
                 ext_child.addContent(new Element("description").addContent(pnt.time+""));
                 ext_Element point = new Element("Point");
                 ext_point.addContent(new Element("coordinates").addContent(pnt.ext_lon+","+pnt.ext_lat+","+pnt.ext_alt));
                 ext_point.addContent(new Element("altitudeMode").addContent("absolute"));
                 ext_child.addContent(ext_point);
                 ext_doc.addContent(ext_child);
                 */
            }

            // write .csv
            pw.write(sb.toString());
            pw.close();

            // write .kml
            root.addContent(doc);
            XMLOutputter outter = new XMLOutputter();
            outter.setFormat(Format.getPrettyFormat());
            outter.output(kml, new FileWriter(new File("data.kml")));

            /**
             ext_root.addContent(doc);
             XMLOutputter ext_outter = new XMLOutputter();
             ext_outter.setFormat(Format.getPrettyFormat());
             ext_outter.output(ext_kml, new FileWriter(new File("dataext.kml")));
             */
        } catch (Exception e) {
            // in case ANYTHING goes wrong.
            e.printStackTrace();
        }
    }
}
