.. java:import:: java.util Date

DataPoint
=========

.. java:package:: rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject
   :noindex:

.. java:type::  class DataPoint implements java.io.Serializable

   Class containing all of the sensor values to be recorded.

   :author: Alan Zhu, Joshua Morin-Baxter

Fields
------
a_actual_x
^^^^^^^^^^

.. java:field:: public float a_actual_x
   :outertype: DataPoint

a_actual_y
^^^^^^^^^^

.. java:field:: public float a_actual_y
   :outertype: DataPoint

a_actual_z
^^^^^^^^^^

.. java:field:: public float a_actual_z
   :outertype: DataPoint

a_x
^^^

.. java:field:: public float a_x
   :outertype: DataPoint

a_y
^^^

.. java:field:: public float a_y
   :outertype: DataPoint

a_z
^^^

.. java:field:: public float a_z
   :outertype: DataPoint

alt
^^^

.. java:field:: public double alt
   :outertype: DataPoint

battery_percent
^^^^^^^^^^^^^^^

.. java:field:: public int battery_percent
   :outertype: DataPoint

battery_temp
^^^^^^^^^^^^

.. java:field:: public int battery_temp
   :outertype: DataPoint

course
^^^^^^

.. java:field:: public float course
   :outertype: DataPoint

ext_alt
^^^^^^^

.. java:field:: public float ext_alt
   :outertype: DataPoint

ext_altEST
^^^^^^^^^^

.. java:field:: public float ext_altEST
   :outertype: DataPoint

ext_lat
^^^^^^^

.. java:field:: public float ext_lat
   :outertype: DataPoint

ext_lon
^^^^^^^

.. java:field:: public float ext_lon
   :outertype: DataPoint

ext_p
^^^^^

.. java:field:: public float ext_p
   :outertype: DataPoint

ext_rh
^^^^^^

.. java:field:: public float ext_rh
   :outertype: DataPoint

ext_temp
^^^^^^^^

.. java:field:: public float ext_temp
   :outertype: DataPoint

g_x
^^^

.. java:field:: public float g_x
   :outertype: DataPoint

g_y
^^^

.. java:field:: public float g_y
   :outertype: DataPoint

g_z
^^^

.. java:field:: public float g_z
   :outertype: DataPoint

gps_speed
^^^^^^^^^

.. java:field:: public float gps_speed
   :outertype: DataPoint

lat
^^^

.. java:field:: public double lat
   :outertype: DataPoint

lon
^^^

.. java:field:: public double lon
   :outertype: DataPoint

mag_x
^^^^^

.. java:field:: public float mag_x
   :outertype: DataPoint

mag_y
^^^^^

.. java:field:: public float mag_y
   :outertype: DataPoint

mag_z
^^^^^

.. java:field:: public float mag_z
   :outertype: DataPoint

p
^

.. java:field:: public float p
   :outertype: DataPoint

rh
^^

.. java:field:: public float rh
   :outertype: DataPoint

rot_x
^^^^^

.. java:field:: public float rot_x
   :outertype: DataPoint

rot_y
^^^^^

.. java:field:: public float rot_y
   :outertype: DataPoint

rot_z
^^^^^

.. java:field:: public float rot_z
   :outertype: DataPoint

t
^

.. java:field:: public float t
   :outertype: DataPoint

time
^^^^

.. java:field:: public Date time
   :outertype: DataPoint

Constructors
------------
DataPoint
^^^^^^^^^

.. java:constructor:: public DataPoint(float t, float g_x, float g_y, float g_z, float rot_x, float rot_y, float rot_z, float rh, float mag_x, float mag_y, float mag_z, float a_x, float a_y, float a_z, float p, Date time)
   :outertype: DataPoint

   Basic initializer.

