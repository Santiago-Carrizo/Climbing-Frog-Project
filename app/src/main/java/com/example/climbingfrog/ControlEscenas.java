package com.example.climbingfrog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Esta clase se encarga de instanciar el contexto, el ancho y el alto de la pantalla para que todas las escenas lo hereden
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */

public class ControlEscenas {
    /**
     * Numero de la escena por defecto
     */
    int numEscena=-1;
    /**
     * Alto de la pantalla actual
     */
    int altoPantalla;
    /**
     * Ancho de la pantalla actual
     */
    int anchoPantalla;
    Context context;


    /**
     * Constructor
     * @param context
     * @param anchoPantalla ancho de la pantalla ajustado
     * @param altoPantalla alto de la pantalla ajustado
     * @param numEscena le da un valor a cada escena que lo herede
     */
    public ControlEscenas( Context context, int anchoPantalla, int altoPantalla, int numEscena) {
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        this.context = context;

        this.numEscena=numEscena;
    }

    /**
     * @param c canvas
     */
    public void dibujar(Canvas c){
    }

    /**
     * @deprecated
     */
    public void actualizarFisica(){
    }

    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return valor por defecto
     */
    int onTouchEvent(MotionEvent event){

        return -1;
    }
}
