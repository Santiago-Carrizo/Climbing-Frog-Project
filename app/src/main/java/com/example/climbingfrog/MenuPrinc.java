package com.example.climbingfrog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;
/**
 * Escena inicial del proyecto y desde donde se puede acceder a todos las demas escenas de menus
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class MenuPrinc extends ControlEscenas{
    int numEscena;
    /**
    *op1 lleva a la escena de seleccion de nivel
    *op2 lleva a la escena de juego en modo infinito
    *op3 lleva al ranking donde se muestran los datos almacenados
    *op4 lleva a la escena
     */
    Rect op1,op2, op3, op4, op5;
    /**
     * Paint de los botones
     * Paint de los textos
     */
    Paint boton,texto;
    Typeface typeface;

    /**
     * Esta variable registra si el juego entra en modo infinito o no
     */
    static boolean infinito=false;

    /**
     * Imagen del boton Jugar
     */
    private Bitmap botonImg;
    /**
     * Imagen donde aparece el titulo del juego
     */
    private Bitmap tituloImg;
    /**
     * Imagen del boton de opciones
     */
    private Bitmap opcionesImg;
    /**
     * Imagen del boton del tutorial
     */
    private Bitmap helpImg;
    /**
     * Imagen del fondo del menu
     */
    private Bitmap fondoM;

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

    public static MediaPlayer mediaPlayer;
    public static boolean controlMusica=false;

    SharedPreferences preferences = context.getSharedPreferences("file",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor= preferences.edit();

    /**
     * Constructor
     * @param context
     * @param numEscena numero de escena en la que se encuentra
     * @param anp ancho de la pantalla
     * @param alp alto de la pantalla
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public MenuPrinc(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        texto=new Paint();
        boton=new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(anchoPantalla/3,altoPantalla/10*4,anchoPantalla/3*2,altoPantalla/10*5);
        op2=new Rect(anchoPantalla/3,altoPantalla/10*6,anchoPantalla/3*2,altoPantalla/10*7);
        op3=new Rect(anchoPantalla/3,altoPantalla/10*8,anchoPantalla/3*2,altoPantalla/10*9);
        op4 = new Rect(anchoPantalla/30,altoPantalla/30,anchoPantalla/30*5,altoPantalla/30*3);
        op5 = new Rect(anchoPantalla-anchoPantalla/30*5,altoPantalla/30,(anchoPantalla-anchoPantalla/30),altoPantalla/30*3);

        botonImg= escala(R.drawable.boton,anchoPantalla/3,altoPantalla/10);
        tituloImg= escala(R.drawable.titulo,(int)(anchoPantalla/1.5),altoPantalla/4);
        opcionesImg= escala(R.drawable.botonop,anchoPantalla/30*4,altoPantalla/30*2);
        helpImg= escala(R.drawable.botonhp,anchoPantalla/30*4,altoPantalla/30*2);
        fondoM= escala(R.drawable.fondom,anchoPantalla,altoPantalla);

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

        if(preferences.getBoolean("sonido",true)){
            Opciones.sonido=true;
        }else{
            Opciones.sonido=false;
        }

        if(preferences.getBoolean("musica",true)){
            Opciones.musica=true;
        }else{
            Opciones.musica=false;
        }

        v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer= MediaPlayer.create(context, R.raw.musicamenu);
        mediaPlayer.setVolume(v/2,v/2);
        if(Opciones.musica){
            if(!controlMusica){
                mediaPlayer.start();
                controlMusica=true;
            }
        }

        Log.i("cancion", "Control musica: "+MenuPrinc.controlMusica+" Musica sonando: "+MenuPrinc.mediaPlayer.isPlaying());
    }

    /**
     * Dibuja los componentes, imagenes y textos
     * @param c canvas
     */
    public void dibujar(Canvas c){
        c.drawColor(Color.RED);

        super.dibujar(c);
        boton.setColor(Color.TRANSPARENT);
        c.drawRect(op1,boton);
        c.drawRect(op2,boton);
        c.drawRect(op3,boton);
        c.drawRect(op4,boton);
        c.drawRect(op5,boton);

        c.drawBitmap(fondoM,0,0,null);
        c.drawBitmap(tituloImg,anchoPantalla/6,altoPantalla/10,null);
        c.drawBitmap(opcionesImg,anchoPantalla-anchoPantalla/30*5,altoPantalla/30,null);
        c.drawBitmap(helpImg,anchoPantalla/30,altoPantalla/30,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*4,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*6,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*8,null);

        texto.setTypeface(typeface);
        texto.setTextSize(botonImg.getHeight()/3);
        c.drawText(context.getString(R.string.btnJugar),op1.left+op1.width()/4,op1.top+op1.height()/2,texto);
        c.drawText(context.getString(R.string.btnInfinito),op2.left+op2.width()/7,op2.top+op2.height()/2,texto);
        c.drawText(context.getString(R.string.btnLadeboard),op3.left+op3.width()/7,op3.top+op3.height()/2,texto);
    }

    /**
     * @deprecated
     */
    public void actualizaFisica(){

    }

    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return si se pulsa el primer boton devuelve la escena de seleccion de nivel
     * @return si se pulsa el segundo boton devuelve la escena de Game con modo infinito
     * @see Game
     * @return si se pulsa el tercer boton devuelve la escena de ranking
     * @return si se pulsa el boton de arriba a la izquierda devuelve la escena de ayuda
     * @return si se pulsa el boton de arriba a la derecha devuelve la escena de opciones
     * @return devuelve la misma escena por lo que no cambia
     */
    int onTouchEvent(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();
        int aux=super.onTouchEvent(event);

        if (aux!=this.numEscena && aux!=-1) return aux;
        int accion = event.getActionMasked();
        switch (accion) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (op1.contains(x, y)) {
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 3;
                } else if (op2.contains(x, y)){
                    mediaPlayer.stop();
                    controlMusica=false;
                    Game.dimRanaAlt=0.1;
                    Game.dimRanaAnch=0.1;
                    Game.dimPlatAlt=0.05;
                    Game.dimPlatAnch=0.35;
                    Game.velocidad=80;
                    Game.puntos = 0;
                    infinito=true;
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 2;
                }else if (op3.contains(x, y)){
                    Game.dimRanaAlt=0.1;
                    Game.dimRanaAnch=0.1;
                    Game.dimPlatAlt=0.05;
                    Game.dimPlatAnch=0.35;
                    Game.velocidad=80;
                    Game.puntos = 0;
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                    return 9;
                }else if(op5.contains(x,y)){
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                    return 6;
                }else if(op4.contains(x,y)){
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                    return 7;
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
        Bitmap bitmapAux=BitmapFactory.decodeResource(context.getResources(), res);
        return bitmapAux.createScaledBitmap(bitmapAux,nuevoAncho,nuevoAlto , true);
    }

}
