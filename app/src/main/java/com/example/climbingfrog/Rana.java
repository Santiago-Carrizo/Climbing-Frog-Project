package com.example.climbingfrog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
/**
 * Clase que instancia una rana nueva en el juego
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Rana {

    /**
     * Coordenadas del objeto
     */
    public PointF posicion;
    /**
     * Imagen del objeto
     */
    public Bitmap imagen;
    /**
     * Rectangulo que marca lo que ocupa el objeto
     */
    Rect hitbox;
    /**
     * Esta variable marca si podemos desplazar la rana en el eje x
     */
    boolean control=true;
    /**
     * Numero que devuelve el sensor de giroscopio
     */
    int giroscopio;
    /**
     * Esta variable marca si la hitbox choca con la hitbox de la plataforma
     */
    boolean plat=false;
    /**
     * Esta variable marca si la rana esta desplazandose en negativo o en positivo en el eje y
     */
    boolean controlSubir=false;
    /**
     * Constructor
     * @param imagen imagen que se le asigna a la plataforma
     * @param x posicionamiento horizontal del objeto
     * @param y posicionamiento vertical del objeto
     */
    public Rana(Bitmap imagen, float x, float y){
        this.imagen=imagen;
        this.posicion= new PointF(x,y);
        setRectangulo();
    }

    /**
     * Funcion que marca el movimiento del objeto por la pantalla
     * @param alto altura de la pantalla en la que se encuentra el objeto
     * @param ancho anchura de la pantalla en la que se encuentra el objeto
     * @param velocidad pixeles que recorre el objeto en un periodo de tiempo
     */
    public void salto(int alto, int ancho, int velocidad){

        if (!control) {
            posicion.y -= velocidad;
            if(posicion.y<0+(int)(alto*0.1)){
                posicion.y += velocidad;
                if(posicion.y >= 0+(int)(alto*0.1)){
                    control=true;
                }
                controlSubir=true;
            }
            setRectangulo();
        }else{
            if(!plat){
                posicion.y+= velocidad;
                posicion.x-=giroscopio*25;

                if(posicion.x>0+(int)(ancho-imagen.getWidth())){
                    posicion.x=0+(int)(ancho-imagen.getWidth());
                }

                if(posicion.x<0){
                    posicion.x=0;
                }

                setRectangulo();
            }else{
                setRectangulo();
            }
        }
    }

    /**
     * Posiciona la rana en las coordenadas indicadas
     */
    public void setRectangulo(){
        float x2=posicion.x;
        float y2=posicion.y;
        hitbox= new Rect((int)(x2), (int)(y2), (int)(x2+imagen.getWidth()), (int)(y2+imagen.getHeight()));
    }

    /**
     * Dibuja la imagen de la plataforma
     * @param c
     */
    public void dibujar(Canvas c){
        c.drawBitmap(imagen, posicion.x, posicion.y, null);
    }


}
