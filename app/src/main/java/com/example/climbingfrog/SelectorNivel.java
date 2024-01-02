package com.example.climbingfrog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;
/**
 * Escena donde se nos permite seleccionar la dificultad de los niveles normales
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class SelectorNivel extends ControlEscenas{
    int numEscena;
    /**
     * op1 es el boton del modo facil que implementa sus correspondientes valores
     * op1 es el boton del modo medio que implementa sus correspondientes valores
     * op1 es el boton del modo dificil que implementa sus correspondientes valores
     */
    Rect op1,op2, op3,op4;
    /**
     * Paint de los botones
     * Paint de los textos
     */
    Paint boton,texto;
    Typeface typeface;

    /**
     * Imagen del boton que selecciona el nivel
     */
    private Bitmap botonImg;
    /**
     * Imagen del boton atras
     */
    private Bitmap atrasImg;
    /**
     * Imagen del fondo del menu de seleccion de nivel
     */
    private Bitmap fondoSN;

    private AudioManager audioManager;
    private SoundPool efectos;
    /**
     * Sonido del boton atras
     */
    private int sonidoBoton;
    final private int maxSonidosSimultaneos=10;
    /**
     * Audio manager
     */
    int v;

    /**
     * Constructor
     * @param context
     * @param numEscena numero de escena en la que se encuentra
     * @param anp ancho de la pantalla
     * @param alp alto de la pantalla
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SelectorNivel(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        texto=new Paint();
        boton=new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(anchoPantalla/3,altoPantalla/10*2,anchoPantalla/3*2,altoPantalla/10*3);
        op2=new Rect(anchoPantalla/3,altoPantalla/10*4,anchoPantalla/3*2,altoPantalla/10*5);
        op3=new Rect(anchoPantalla/3,altoPantalla/10*6,anchoPantalla/3*2,altoPantalla/10*7);
        op4=new Rect(0,0,anchoPantalla/4,altoPantalla/20);

        botonImg= escala(R.drawable.boton,(int)(anchoPantalla/3),(int)(altoPantalla/10));
        atrasImg= escala(R.drawable.botonatras,(int)(anchoPantalla/4),(int)(altoPantalla/20));
        fondoSN= escala(R.drawable.fondosn,anchoPantalla,altoPantalla);

        audioManager=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb=new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(maxSonidosSimultaneos);
            this.efectos=spb.build();
        } else {
            this.efectos=new SoundPool(maxSonidosSimultaneos, AudioManager.STREAM_MUSIC, 0);
        }

        typeface = context.getResources().getFont(R.font.sweetssmile);
        sonidoBoton=efectos.load(context, R.raw.sonidoboton,1);

        v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * Dibuja los componentes, imagenes y textos
     * @param c canvas
     */
    public void dibujar(Canvas c){
        c.drawColor(Color.BLUE);

        super.dibujar(c);
        c.drawBitmap(fondoSN,0,0,null);

        c.drawRect(op1,boton);
        c.drawRect(op2,boton);
        c.drawRect(op3,boton);
        c.drawRect(op4,boton);


        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*2,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*4,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*6,null);
        c.drawBitmap(atrasImg,0,0,null);

        texto.setTypeface(typeface);
        texto.setTextSize(botonImg.getHeight()/3);
        c.drawText(context.getString(R.string.btnFacil),op1.left+op1.width()/4,op1.top+op1.height()/2,texto);
        c.drawText(context.getString(R.string.btnNormal),op2.left+op2.width()/7,op2.top+op2.height()/2,texto);
        c.drawText(context.getString(R.string.btnDificil),op3.left+op3.width()/7,op3.top+op3.height()/2,texto);
    }

    public void actualizaFisica(){

    }

    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return dependiendo del boton que pulsemos devuelve la escena del juego con diferentes valores en Game
     * @see Game
     * @return devuelve la misma escena por lo que no cambia
     */
    @SuppressLint("SuspiciousIndentation")
    int onTouchEvent(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();
        int aux=super.onTouchEvent(event);

        if (aux!=this.numEscena && aux!=-1) return aux;
        int accion = event.getActionMasked();
        switch (accion) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (op1.contains(x,y)){
                    MenuPrinc.mediaPlayer.stop();
                    MenuPrinc.controlMusica=false;
                    Game.dimRanaAlt=0.1;
                    Game.dimRanaAnch=0.1;
                    Game.dimPlatAlt=0.05;
                    Game.dimPlatAnch=0.4;
                    Game.velocidad=100;
                    Game.puntos=5;
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 2;
                }else if (op2.contains(x,y)){
                    MenuPrinc.mediaPlayer.stop();
                    MenuPrinc.controlMusica=false;
                    Game.dimRanaAlt=0.1;
                    Game.dimRanaAnch=0.1;
                    Game.dimPlatAlt=0.05;
                    Game.dimPlatAnch=0.35;
                    Game.velocidad=80;
                    Game.puntos=5;
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 2;
                }else if (op3.contains(x,y)){
                    MenuPrinc.mediaPlayer.stop();
                    MenuPrinc.controlMusica=false;
                    Game.dimRanaAlt=0.1;
                    Game.dimRanaAnch=0.1;
                    Game.dimPlatAlt=0.05;
                    Game.dimPlatAnch=0.3;
                    Game.velocidad=60;
                    Game.puntos=5;
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 2;
                }else if (op4.contains(x,y)){
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 1;
                }
                break;
        }

        return this.numEscena;
    }

    /**
     * Se encarga de redimensionar las imagenes para que se ajusten a cada dispositivo
     * @param res imagen que se quiere redimensionar
     * @param nuevoAncho ancho que le queremos asignar
     * @param nuevoAlto alto que le queremos asignar
     * @return imagen con nuevas dimensiones
     */
    public Bitmap escala(int res, int nuevoAncho, int nuevoAlto){
        Bitmap bitmapAux= BitmapFactory.decodeResource(context.getResources(), res);
        return bitmapAux.createScaledBitmap(bitmapAux,nuevoAncho,nuevoAlto , true);
    }
}

