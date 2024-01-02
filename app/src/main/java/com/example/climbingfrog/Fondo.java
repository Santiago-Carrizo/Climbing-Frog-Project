package com.example.climbingfrog;

import android.graphics.Bitmap;
import android.graphics.PointF;
/**
 * Clase que instancia el fondo del juego
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Fondo {
    /**
     * Coordenadas del objeto
     */
    public PointF posicion;
    /**
     * Imagen del objeto
     */
    public Bitmap imagen;

    /**
     * Constructor
     * @param imagen imagen principal que se le asigna al fondo del juego
     * @param x posicionamiento horizontal de la imagen
     * @param y posicionamiento vertical de la imagen
     */
    public Fondo(Bitmap imagen, float x, float y) { // Constructores
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }

    /**
     * Constructor creado para el uso de una segunda imagen
     * @param imagen la misma imagen pero utilizada para crear efecto de desplazamiento
     * @param altoPantalla altura de la pantalla
     */
    public Fondo(Bitmap imagen, int altoPantalla) {
        this(imagen, 0, altoPantalla - imagen.getHeight());
    }

    /**
     * Metodo que mueve la imagen para crear un efecto de movimiento
     * @param velocidad pixeles a los que se desplaza la imagen
     */
    public void mover(int velocidad) { // Desplazamiento
        posicion.y += velocidad;
    }
}
