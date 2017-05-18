package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import java.util.Date;


class DataPoint implements java.io.Serializable {
    /**
     * Declares each variable that takes a value from an internal sensor
     */
    public float t;
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
    public float g_x;
    public float g_y;
    public float g_z;
    public float ext_lat;
    public float ext_lon;
    public float ext_alt;
    public float ext_p;
    public float ext_temp;
    public float ext_altEST;
    public float ext_rh;
    public float course;
    public float gps_speed;
    public int battery_percent;
    public int battery_temp;
    public float a_actual_x;
    public float a_actual_y;
    public float a_actual_z;

    /** Declares each variable that takes a value from an external sensor */
    //public float ext_p;
    //public float ext_t;
    //private float ard_alt;


    /**
     * Specific value that corresponds to the decoder (enabling deserialization of data)
     */
    private static final long serialVersionUID = 862329458582895689L;

    /**
     * Initializer.  Each variable must be listed twice, in the row of variables and in the column
     * that begins with this.
     */
    public DataPoint(float t, float g_x, float g_y, float g_z, float rot_x, float rot_y, float rot_z, float rh, float mag_x, float mag_y, float mag_z, float a_x, float a_y, float a_z, float p, Date time) {
        this.t = t;
        this.g_x = g_x;
        this.g_y = g_y;
        this.g_z = g_z;
        this.rot_x = rot_x;
        this.rot_y = rot_y;
        this.rot_z = rot_z;
        this.rh = rh;
        this.a_y = a_y;
        this.a_x = a_x;
        this.a_z = a_z;
        this.mag_x = mag_x;
        this.mag_y = mag_y;
        this.mag_z = mag_z;
        this.p = p;
        this.time = time;
        this.lat = -1;
        this.lon = -1;
        this.alt = -1;
        //this.ext_p = ext_p;
        //this.ext_t = ext_t;
        //this.ard_alt = ard_alt;
    }
}
