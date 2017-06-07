.. java:import:: android.content BroadcastReceiver

.. java:import:: android.content Context

.. java:import:: android.content Intent

BootCompletedReceiver
=====================

.. java:package:: rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject
   :noindex:

.. java:type:: public class BootCompletedReceiver extends BroadcastReceiver

   Starts our service on boot, which in turn starts our main activity.

   :author: Alan Zhu

Methods
-------
onReceive
^^^^^^^^^

.. java:method:: @Override public void onReceive(Context context, Intent intent)
   :outertype: BootCompletedReceiver

   Recieves a the BootCompleted message and responds to it by starting \ :java:ref:`PersistenceService`\

   :param context: Context upon which to act upon (?)
   :param intent: idk

