package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import java.util.Date;

/**
 * Class that will be logged as a separate file
 */

class DataPoint implements java.io.Serializable{
    //things in a data point
    public float a_y;
    public float a_x;
    public float a_z;
    public float p;
    public double lat;
    public double lon;
    public double alt;
    public Date time;
    public float mag_x;
    public float mag_y;
    public float mag_z;
    public float rh;
    public float rot_x;
    public float rot_y;
    public float rot_z;
    /**

     */
    /**
     * public float ext_p;
     * public float ext_t;
     */

    //allows for this to be used in our decoder--it's a specific value
    private static final long serialVersionUID= 862329458582895689L;

    //initializer
    public DataPoint(float rot_x, float rot_y, float rot_z, float rh, float mag_x, float mag_y, float mag_z, float a_x, float a_y, float a_z, float p, Date time){
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
    }
}
