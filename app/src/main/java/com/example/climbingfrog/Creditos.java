package com.example.climbingfrog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
 * Escena donde se nos permite ver los equipos que trabajaron en el proyecto
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Creditos extends ControlEscenas{
    /**
     * Numero de la escena
     */
    int numEscena;
    /**
     * Boton para ir atras
     */
    Rect op1;
    /**
     * Paint de los botones
     */
    Paint boton;
    /**
     * Paint de los textos
     */
    Paint texto;
    /**
     * Imagen del boton atras
     */
    private Bitmap atrasImg;
    /**
     * Imagen del fondo
     */
    private Bitmap fondoM;

    Typeface typeface;
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
    public Creditos(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        boton=new Paint();
        texto=new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(0,0,anchoPantalla/4,altoPantalla/20);

        atrasImg= escala(R.drawable.botonatras,(int)(anchoPantalla/4),(int)(altoPantalla/20));
        fondoM= escala(R.drawable.fondot,anchoPantalla,altoPantalla);

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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dibujar(Canvas c){
        c.drawColor(Color.GRAY);

        super.dibujar(c);
        c.drawBitmap(fondoM,0,0,null);

        c.drawRect(op1,boton);
        c.drawBitmap(atrasImg,0,0,null);

        texto.setTypeface(typeface);
        texto.setTextSize(anchoPantalla/20);
        texto.setColor(context.getColor(R.color.black));
        c.drawText(context.getString(R.string.programador)+":",anchoPantalla/7,(int)(altoPantalla/10*2),texto);
        c.drawText("- Santiago Carrizo Marchena",anchoPantalla/7,(int)(altoPantalla/10*2.5),texto);
        c.drawText(context.getString(R.string.artista)+":",anchoPantalla/7,(int)(altoPantalla/10*3),texto);
        c.drawText("- Santiago Carrizo Marchena",anchoPantalla/7,(int)(altoPantalla/10*3.5),texto);
        c.drawText(context.getString(R.string.compositor)+":",anchoPantalla/7,(int)(altoPantalla/10*4),texto);
        c.drawText("- VGOScore",anchoPantalla/7,(int)(altoPantalla/10*4.5),texto);
        c.drawText("- Jonny Easton",anchoPantalla/7,(int)(altoPantalla/10*5),texto);
        c.drawText(context.getString(R.string.fuentes)+":",anchoPantalla/7,(int)(altoPantalla/10*5.5),texto);
        c.drawText("- Soundbible",anchoPantalla/7,(int)(altoPantalla/10*6),texto);
        c.drawText("- DaFont",anchoPantalla/7,(int)(altoPantalla/10*6.5),texto);

    }
    /**
     * @deprecated
     */
    public void actualizaFisica(){

    }
    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return si se pulsa el boton atras regresa a las opciones
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
                    return 6;
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
