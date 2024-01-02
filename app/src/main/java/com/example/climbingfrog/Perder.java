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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

/**
 * Escena resultante al jugar los niveles normales o el infinito y perder todas las vidas
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Perder extends ControlEscenas{
    /**
     * Numero de la escena
     */
    int numEscena;
    /**
     * boton de regreso al menu principal
     */
    Rect op1;
    /**
     * Paint de los botones
     * Paint de los textos
     */
    Paint boton,texto;
    Typeface typeface;

    /**
     * Imagen del boton que va a la escena de menu principal
     */
    private Bitmap botonImg;
    /**
     * Imagen del fondo
     */
    private Bitmap fondoM;
    /**
     * Imagen decorativa de la rana
     */
    private Bitmap estado;

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
     * Puntos conseguidos la ultima vez jugada al juego en infinito
     * Puntos del primero en el ranking
     * Puntos del segundo en el ranking
     * Puntos del tercero en el ranking
     * Puntos del cuarto en el ranking
     * Puntos del quinto en el ranking
     */
    int ultimaPunt,mejor1,mejor2,mejor3,mejor4,mejor5;

    /**
     * Constructor
     * @param context
     * @param numEscena numero de escena en la que se encuentra
     * @param anp ancho de la pantalla
     * @param alp alto de la pantalla
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Perder(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        texto=new Paint();
        boton=new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(anchoPantalla/4,altoPantalla/10*6,anchoPantalla/4*3,altoPantalla/10*7);

        botonImg= escala(R.drawable.boton,(int)(anchoPantalla/2),(int)(altoPantalla/10));
        fondoM= escala(R.drawable.fondom,anchoPantalla,altoPantalla);
        estado= escala(R.drawable.ranatriste,(anchoPantalla/2),(int)(altoPantalla/4));

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

        ultimaPunt=Game.puntuacion;

        //En caso de jugar en modo infinito se guarda el resultado y se muestra la puntuacion
        if(MenuPrinc.infinito){
            SharedPreferences preferences = context.getSharedPreferences("file",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();


            if(ultimaPunt>preferences.getInt("mejor5",0)){
                mejor5= ultimaPunt;
                editor.putInt("mejor5",mejor5);
                editor.apply();
            }

            if(ultimaPunt>preferences.getInt("mejor4",0)){
                int temp= mejor4;
                mejor4= ultimaPunt;
                mejor5=temp;
                editor.putInt("mejor4",mejor4);
                editor.putInt("mejor5",mejor5);
                editor.apply();
            }

            if(ultimaPunt>preferences.getInt("mejor3",0)){
                int temp= mejor3;
                mejor3= ultimaPunt;
                mejor4=temp;
                editor.putInt("mejor3",mejor3);
                editor.putInt("mejor4",mejor4);
                editor.apply();
            }

            if(ultimaPunt>preferences.getInt("mejor2",0)){
                int temp= mejor2;
                mejor2= ultimaPunt;
                mejor3=temp;
                editor.putInt("mejor2",mejor2);
                editor.putInt("mejor3",mejor3);
                editor.apply();
            }

            if(ultimaPunt>preferences.getInt("mejor1",0)){
                int temp= mejor1;
                mejor1= ultimaPunt;
                mejor2=temp;
                editor.putInt("mejor1",mejor1);
                editor.putInt("mejor2",mejor2);
                editor.apply();
            }

        }
    }

    /**
     * Dibuja los componentes, imagenes y textos
     * @param c canvas
     */
    public void dibujar(Canvas c){
        c.drawColor(Color.BLUE);

        super.dibujar(c);
        c.drawBitmap(fondoM,0,0,null);
        c.drawRect(op1,boton);
        c.drawBitmap(botonImg,anchoPantalla/4,altoPantalla/10*6,null);

        texto.setColor(Color.BLACK);
        texto.setTypeface(typeface);
        texto.setTextSize(botonImg.getHeight()/4);
        c.drawText(context.getString(R.string.volver),op1.left+op1.width()/12,op1.top+op1.height()/2,texto);

        if(MenuPrinc.infinito){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                texto.setColor(context.getColor(R.color.textos));
            }
            texto.setTextSize(anchoPantalla/10);
            c.drawText(context.getString(R.string.puntuacion)+": "+Game.puntuacion,anchoPantalla/4,altoPantalla/4,texto);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                texto.setColor(context.getColor(R.color.textos));
            }
            texto.setTextSize(anchoPantalla/10);
            c.drawText(context.getString(R.string.perder),anchoPantalla/3,altoPantalla/10*2,texto);

            c.drawBitmap(estado,anchoPantalla/4,altoPantalla/10*2,null);
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
     * @return si se pulsa el boton de menu principal devuelve su escena
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
                    MenuPrinc.infinito=false;
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

