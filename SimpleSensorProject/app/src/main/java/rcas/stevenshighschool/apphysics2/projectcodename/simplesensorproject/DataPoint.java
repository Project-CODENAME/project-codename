package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import java.util.Date;


class DataPoint implements java.io.Serializable{
    //things in a data point
    private float t;
    private float a_y;
    private float a_x;
    private float a_z;
    private float p;
    double lat;
    double lon;
    double alt;
    private Date time;
    private float mag_x;
    private float mag_y;
    private float mag_z;
    private float rh;
    private float rot_x;
    private float rot_y;
    private float rot_z;
    private float g_x;
    private float g_y;
    private float g_z;
    //private float ext_p;
    //private float ext_t;
    //private float ard_alt;



    //allows for this to be used in our decoder--it's a specific value
    private static final long serialVersionUID= 862329458582895689L;

    //initializer
    public DataPoint(float t, float g_x, float g_y, float g_z, float rot_x, float rot_y, float rot_z, float rh, float mag_x, float mag_y, float mag_z, float a_x, float a_y, float a_z, float p, Date time){
        this.t = t;
        this.g_x = g_x;
        this.g_y = g_y;
        this.g_z = g_z;
        this.rot_x = rot_x;
        this.rot_y = rot_y;
        this.rot_z = rot_z;
        this.rh = rh;
        this.a_y = a_y;
        this.a_x=a_x;
        this.a_z=a_z;
        this.mag_x=mag_x;
        this.mag_y=mag_y;
        this.mag_z=mag_z;
        this.p=p;
        this.time=time;
        this.lat=-1;
        this.lon=-1;
        this.alt=-1;
        //this.ext_p = ext_p;
        //this.ext_t = ext_t;
        //this.ard_alt = ard_alt;
    }
}
