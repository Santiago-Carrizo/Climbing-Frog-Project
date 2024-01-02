package com.example.climbingfrog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Debug;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
/**
 * Clase cuya funcion erradica en asignar a cada escena un valor y unos valores ademas de controlar el cambio entre escenas
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class GameSV extends SurfaceView implements SurfaceHolder.Callback{

    SurfaceHolder surfaceHolder;
    Context context;
    /**
     * Llama a la clase Hilo
     */
    Hilo hilo;
    /**
     * Indica si el programa esta funcionando
     */
    boolean funcionando = true;
    /**
     * Establece un valor base al ancho de la pantalla
     */
    static public int anchoPantalla=1;
    /**
     * Establece un valor base al alto de la pantalla
     */
    static public int altoPantalla=1;
    /**
     * Administra la escena en la que se encuentra y a cual se quiere cambiar
     */
    ControlEscenas escenaActual;
    /**
     * Escena llamada
     */
    int nuevaEscena;

    /**
     * Constructor
     * @param context
     */
    public GameSV(Context context) {
        super(context);

        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;

        hilo = new Hilo();

    }

    /**
     * Creacion de la escena
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    /**
     * Cambio a la escena
     * @param holder
     * @param format
     * @param width ancho de la pantalla
     * @param height alto de la pantalla
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        anchoPantalla = width;
        altoPantalla = height;
        escenaActual=new MenuPrinc(context, 1 , anchoPantalla, altoPantalla);

        if (hilo.getState() == Thread.State.NEW) hilo.start();
        if (hilo.getState() == Thread.State.TERMINATED) {
            hilo=new Hilo();
            hilo.start();
        }
    }

    /**
     * Cierre de la escena
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.funcionando=false;
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * OnTouchEvent
     * @param event
     * @return devuelve true para que se apliquen las funciones
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

            int pointerIndex = event.getActionIndex();
            int pointerID = event.getPointerId(pointerIndex);
            int accion = event.getActionMasked();
        nuevaEscena=escenaActual.onTouchEvent(event);
            cambiaEscena(nuevaEscena);

        return true;
    }

    /**
     * Metodo llamado cada vez que se devuelve un numero de escena diferente
     * que se utiliza para cambiar a la escena con el numero asignado
     * @param cambiaEscenaint numero de la escena a la que se quiere cambiar
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cambiaEscena(int cambiaEscenaint){
        if (escenaActual.numEscena!=cambiaEscenaint){
            switch (nuevaEscena){
                case 1: escenaActual=new MenuPrinc(context, 1, anchoPantalla, altoPantalla); break;
                case 2: escenaActual=new Game(context, 2, anchoPantalla, altoPantalla);
                    Log.i("a",anchoPantalla+" and "+altoPantalla);
                    break;
                case 3: escenaActual=new SelectorNivel(context, 3, anchoPantalla, altoPantalla);break;
                case 4: escenaActual= new Perder(context, 4, anchoPantalla,altoPantalla); break;
                case 5: escenaActual= new Ganar(context, 5, anchoPantalla,altoPantalla); break;
                case 6: escenaActual= new Opciones(context, 6, anchoPantalla,altoPantalla); break;
                case 7: escenaActual= new Help(context,7,anchoPantalla,altoPantalla); break;
                case 8: escenaActual= new Creditos(context,8,anchoPantalla,altoPantalla); break;
                case 9: escenaActual= new Ranking(context,9,anchoPantalla,altoPantalla); break;
            }
        }
    }

    /**
     * Esta clase pone en funcionamiento el proyecto y administra las funciones de cambio de escena
     * mientras el programa este en funcionamiento
     */
    class Hilo extends Thread {
        @Override
        public void run() {
            super.run();
            while (funcionando) {
                Canvas c = null;
                try {
                    if (!surfaceHolder.getSurface().isValid()) continue;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        c = surfaceHolder.lockHardwareCanvas();
                    }
                    if (c == null) c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        escenaActual.actualizarFisica();
                        c.drawColor(Color.RED);
                        if(nuevaEscena==2){

                            for (int i = 0; i < 1; i++) {
                                escenaActual.dibujar(c);
                            }
                        }else{
                            escenaActual.dibujar(c);
                        }

                    }
                } finally {
                    try{
                        surfaceHolder.unlockCanvasAndPost(c);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        }

    }
}
