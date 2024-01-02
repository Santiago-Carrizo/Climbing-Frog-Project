package com.example.climbingfrog;

import android.annotation.SuppressLint;
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
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

/**
 * Escena en la que podemos activar o desactivar algunos parametros del juego
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Opciones extends ControlEscenas{
    int numEscena;
    /**
     *op1 activa o desactiva la musica
     *op2 activa o desactiva los sonidos
     *op3 inicia la escena de creditos
     *op4 regresa al menu principal
     */
    Rect op1,op2, op3,op4;
    /**
     * Paint de los botones
     * Paint de los textos
     * Paint de los indicadores del sonido y musica
     */
    Paint boton,texto,textoMarcador;
    Typeface typeface;

    /**
     * Imagen del boton atras
     * Imagen del boton musica
     * Imagen del boton sonido
     * Imagen del boton Creditos
     */
    private Bitmap atrasImg,musicaImg,sonidoImg,botonImg;
    /**
     * Imagen del fondo
     */
    private Bitmap fondoM;
    /**
     * Imagen decorativa de un engranaje
     */
    private Bitmap engranaje;
    /**
     * musica activa
     */
    static boolean musica;
    /**
     * sonidos activos
     */
    static boolean sonido;

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
    public Opciones(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        texto= new Paint();
        boton=new Paint();
        textoMarcador= new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(0,0,anchoPantalla/4,altoPantalla/20);
        op2=new Rect(anchoPantalla/5,altoPantalla/10*4,anchoPantalla/5*2,altoPantalla/10*5);
        op3=new Rect(anchoPantalla/5*3,altoPantalla/10*4,anchoPantalla/5*4,altoPantalla/10*5);
        op4=new Rect(anchoPantalla/3,altoPantalla/10*6,anchoPantalla/3*2,altoPantalla/10*7);

        botonImg= escala(R.drawable.boton,(int)(anchoPantalla/3),(int)(altoPantalla/10));
        atrasImg= escala(R.drawable.botonatras,(int)(anchoPantalla/4),(int)(altoPantalla/20));
        musicaImg= escala(R.drawable.botonmusica,(int)(anchoPantalla/5),(int)(altoPantalla/10));
        sonidoImg= escala(R.drawable.botonsonido,(int)(anchoPantalla/5),(int)(altoPantalla/10));
        fondoM= escala(R.drawable.fondom,anchoPantalla,altoPantalla);
        engranaje= escala(R.drawable.engranaje,altoPantalla/4,altoPantalla/4);

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





        Log.i("cancion", "Control musica: "+MenuPrinc.controlMusica+" Musica sonando: "+MenuPrinc.mediaPlayer.isPlaying());
    }
    /**
     * Dibuja los componentes, imagenes y textos
     * @param c canvas
     */
    public void dibujar(Canvas c){
        c.drawColor(Color.GRAY);

        super.dibujar(c);
        c.drawBitmap(fondoM,0,0,null);

        c.drawRect(op1,boton);
        c.drawRect(op2,boton);
        c.drawRect(op3,boton);
        c.drawRect(op4,boton);

        c.drawBitmap(atrasImg,0,0,null);
        c.drawBitmap(musicaImg,anchoPantalla/5,altoPantalla/10*4,null);
        c.drawBitmap(sonidoImg,anchoPantalla/5*3,altoPantalla/10*4,null);
        c.drawBitmap(botonImg,anchoPantalla/3,altoPantalla/10*6,null);
        c.drawBitmap(engranaje,(int)(anchoPantalla/5*1.25),altoPantalla/8,null);

        textoMarcador.setTypeface(typeface);
        textoMarcador.setTextSize(botonImg.getHeight()/3);
        texto.setTypeface(typeface);
        texto.setTextSize(botonImg.getHeight()/3);
        c.drawText(context.getString(R.string.btnCreditos),op4.left+op4.width()/10,op4.top+op4.height()/2,texto);
        if(sonido){
            textoMarcador.setColor(Color.GREEN);
            c.drawText(context.getString(R.string.on),(int)(anchoPantalla/5*3.2),(int)(altoPantalla/10*5.5),textoMarcador);
        }else{
            textoMarcador.setColor(Color.RED);
            c.drawText(context.getString(R.string.off),(int)(anchoPantalla/5*3.2),(int)(altoPantalla/10*5.5),textoMarcador);
        }

        if(musica){
            textoMarcador.setColor(Color.GREEN);
            c.drawText(context.getString(R.string.on),(int)(anchoPantalla/5*1.2),(int)(altoPantalla/10*5.5),textoMarcador);
        }else{
            textoMarcador.setColor(Color.RED);
            c.drawText(context.getString(R.string.off),(int)(anchoPantalla/5*1.2),(int)(altoPantalla/10*5.5),textoMarcador);
        }

    }

    /**
     * @deprecated
     */
    public void actualizaFisica(){

    }

    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return si se pulsa el boton de creditos conduce a su escena
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
                    if(Opciones.sonido)
                    efectos.play(sonidoBoton,v,v,1,0,1);
                    return 1;
                }else if(op2.contains(x,y)){
                    if(musica){
                        musica=false;
                        editor.putBoolean("musica",false);
                        editor.apply();
                        Log.i("cancion", "Control musica: "+MenuPrinc.controlMusica+" Musica sonando: "+MenuPrinc.mediaPlayer.isPlaying());
                        MenuPrinc.mediaPlayer.stop();
                    }else {
                        musica=true;
                        editor.putBoolean("musica",true);
                        editor.apply();
                        MenuPrinc.controlMusica=false;
                        Log.i("cancion", "Control musica: "+MenuPrinc.controlMusica+" Musica sonando: "+MenuPrinc.mediaPlayer.isPlaying());

                    }
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                }else if(op3.contains(x,y)){
                    if(sonido){
                        sonido=false;
                        editor.putBoolean("sonido",false);
                        editor.apply();
                    }else {
                        sonido=true;
                        editor.putBoolean("sonido",true);
                        editor.apply();
                    }
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                }else if(op4.contains(x,y)){
                    if(Opciones.sonido)
                        efectos.play(sonidoBoton,v,v,1,0,1);
                    return 8;
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
