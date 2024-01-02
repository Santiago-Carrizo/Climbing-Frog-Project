package com.example.climbingfrog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Clase que instancia una plataforma nueva en el juego
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Plataforma {
    /**
     * Coordenadas del objeto
     */
    public PointF posicion;
    /**
     * Imagen del objeto
     */
    public Bitmap imagenP;
    /**
     * Rectangulo que marca lo que ocupa el objeto
     */
    Rect hitboxP;

    /**
     * Constructor
     * @param imagenP imagen que se le asigna a la plataforma
     * @param x posicionamiento horizontal del objeto
     * @param y posicionamiento vertical del objeto
     */
    public Plataforma(Bitmap imagenP,int x, int y){
        this.posicion= new PointF(x,y);
        this.imagenP=imagenP;
        setPlataforma();
    }

    /**
     * Posiciona la plataforma en las coordenadas indicadas
     */
    public void setPlataforma(){
        float x2=posicion.x;
        float y2=posicion.y;
        hitboxP= new Rect((int)(x2), (int)(y2), (int)(x2+imagenP.getWidth()), (int)(y2+imagenP.getHeight()));

    }

    /**
     * Dibuja la imagen de la plataforma
     * @param c
     */
    public void dibujar(Canvas c){
        c.drawBitmap(imagenP, posicion.x, posicion.y, null);
    }
}
