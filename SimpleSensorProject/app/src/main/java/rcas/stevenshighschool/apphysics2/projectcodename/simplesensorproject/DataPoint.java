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
    /**
     * public float ext_p;
     * public float ext_t;
     */

    //allows for this to be used in our decoder--it's a specific value
    private static final long serialVersionUID= 862329458582895689L;

    //initializer
    public DataPoint(float a_x, float a_y, float a_z, float p, Date time){
        this.a_y=a_y;
        this.a_x=a_x;
        this.a_z=a_z;
        this.p=p;
        this.time=time;
    }
}
