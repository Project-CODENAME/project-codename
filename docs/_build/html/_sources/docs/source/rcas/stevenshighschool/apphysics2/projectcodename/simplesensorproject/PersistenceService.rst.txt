.. java:import:: android.app ActivityManager

.. java:import:: android.app Service

.. java:import:: android.content Intent

.. java:import:: android.content.pm PackageManager

.. java:import:: android.os IBinder

PersistenceService
==================

.. java:package:: rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject
   :noindex:

.. java:type:: public class PersistenceService extends Service

   This service ensures that if the app is backgrounded, it will start back up again within 5 seconds, given that it's not the system using one of its extreme measures, so that measurements are taken as long as possible. This means that if you install the app on your phone - it's really hard to get rid of until you adb uninstall it.

   :author: Alan Zhu

Constructors
------------
PersistenceService
^^^^^^^^^^^^^^^^^^

.. java:constructor:: public PersistenceService()
   :outertype: PersistenceService

   Default, necessary constructor

Methods
-------
onBind
^^^^^^

.. java:method:: @Override public IBinder onBind(Intent intent)
   :outertype: PersistenceService

   Not useful.

   :param intent: intent that started the service/binder (?)
   :return: null

onStart
^^^^^^^

.. java:method:: @Override public void onStart(Intent intent, int startId)
   :outertype: PersistenceService

   This is the old onStart method that will be called on the pre-2.0 platform. On 2.0 or later we override onStartCommand() so this method will not be called. This came with the Android tutorial and I'm not quite sure why we still have it.

   :param intent: intent that started the service
   :param startId: honestly I'm not sure, but it's not important for us really

onStartCommand
^^^^^^^^^^^^^^

.. java:method:: @Override public int onStartCommand(Intent intent, int flags, int startId)
   :outertype: PersistenceService

   Starts a thread that checks if the app is in the foreground and puts it there if not.

   :param intent: intent that started the service
   :param flags: any flags, although we really don't use them
   :param startId: not sure, not important
   :return: START_STICKY, so that our service is kept (hopefully)

