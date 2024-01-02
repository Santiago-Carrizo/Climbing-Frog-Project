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
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;
/**
 * Escena resultante al pulsar el boton de ayuda en el menu principal
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Help extends ControlEscenas{
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
     * Imagen del fondo del tutorial
     */
    private Bitmap fondot;
    /**
     * Imagen de la plataforma
     */
    private Bitmap plat;
    /**
     * Imagen de la plataforma con flecha arriba
     */
    private Bitmap platA;
    /**
     * Imagen de la plataforma con flecha abajo
     */
    private Bitmap platB;
    /**
     * Imagen de la rana
     */
    private Bitmap rana;
    /**
     * Imagen de la rana cargando
     */
    private Bitmap ranaR;

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
    public Help(Context context, int numEscena, int anp, int alp) {
        super(context,  anp, alp, numEscena);

        this.numEscena=numEscena;
        boton=new Paint();
        texto=new Paint();
        boton.setColor(Color.TRANSPARENT);
        op1=new Rect(0,0,anchoPantalla/4,altoPantalla/20);


        plat= escala(R.drawable.ejemplat3,anchoPantalla/10,anchoPantalla/10);
        platA= escala(R.drawable.ejemplat2,anchoPantalla/10,anchoPantalla/10);
        platB= escala(R.drawable.ejemplat,anchoPantalla/10,anchoPantalla/10);
        rana= escala(R.drawable.ejemrana2,anchoPantalla/10,anchoPantalla/10);
        ranaR= escala(R.drawable.ejemranar,anchoPantalla/10,anchoPantalla/10);
        atrasImg= escala(R.drawable.botonatras,(int)(anchoPantalla/4),(int)(altoPantalla/20));
        fondot= escala(R.drawable.fondot,anchoPantalla,altoPantalla);

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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dibujar(Canvas c){
        c.drawColor(Color.GRAY);

        super.dibujar(c);
        c.drawBitmap(fondot,0,0,null);

        c.drawRect(op1,boton);
        c.drawBitmap(atrasImg,0,0,null);

        c.drawBitmap(rana,anchoPantalla/15,(int)(altoPantalla/10*1.5),null);
        c.drawBitmap(plat,anchoPantalla/15,(int)(altoPantalla/10*3.5),null);
        c.drawBitmap(ranaR,anchoPantalla/15,(int)(altoPantalla/10*4.5),null);
        c.drawBitmap(platB,anchoPantalla/15,(int)(altoPantalla/10*5.5),null);
        c.drawBitmap(platA,anchoPantalla/15,(int)(altoPantalla/10*6.5),null);


        texto.setTypeface(typeface);
        texto.setTextSize(anchoPantalla/20);
        texto.setColor(context.getColor(R.color.black));

        c.drawText(context.getString(R.string.explicacionRana1),anchoPantalla/6,(int)(altoPantalla/15*2),texto);
        c.drawText(context.getString(R.string.explicacionRana2),anchoPantalla/6,(int)(altoPantalla/15*2.5),texto);
        c.drawText(context.getString(R.string.explicacionRana3),anchoPantalla/6,(int)(altoPantalla/15*3),texto);
        c.drawText(context.getString(R.string.explicacionRana4),anchoPantalla/6,(int)(altoPantalla/15*3.5),texto);
        c.drawText(context.getString(R.string.explicacionRana5),anchoPantalla/6,(int)(altoPantalla/15*4),texto);
        c.drawText(context.getString(R.string.explicacionPlat1),anchoPantalla/6,(int)(altoPantalla/15*5),texto);
        c.drawText(context.getString(R.string.explicacionPlat2),anchoPantalla/6,(int)(altoPantalla/15*5.5),texto);
        c.drawText(context.getString(R.string.explicacionPlat3),anchoPantalla/6,(int)(altoPantalla/15*6),texto);
        c.drawText(context.getString(R.string.explicacionRanaR1),anchoPantalla/6,(int)(altoPantalla/15*7),texto);
        c.drawText(context.getString(R.string.explicacionRanaR2),anchoPantalla/6,(int)(altoPantalla/15*7.5),texto);
        c.drawText(context.getString(R.string.explicacionPlatA1),anchoPantalla/6,(int)(altoPantalla/15*8.5),texto);
        c.drawText(context.getString(R.string.explicacionPlatA2),anchoPantalla/6,(int)(altoPantalla/15*9),texto);
        c.drawText(context.getString(R.string.explicacionPlatB1),anchoPantalla/6,(int)(altoPantalla/15*10),texto);
        c.drawText(context.getString(R.string.explicacionPlatB2),anchoPantalla/6,(int)(altoPantalla/15*10.5),texto);
        c.drawText(context.getString(R.string.explicacionObj1),anchoPantalla/6,(int)(altoPantalla/15*11.5),texto);
        c.drawText(context.getString(R.string.explicacionObj2),anchoPantalla/6,(int)(altoPantalla/15*12),texto);
        c.drawText(context.getString(R.string.explicacionObj3),anchoPantalla/6,(int)(altoPantalla/15*12.5),texto);
        c.drawText(context.getString(R.string.explicacionObj4),anchoPantalla/6,(int)(altoPantalla/15*13),texto);
        c.drawText(context.getString(R.string.suerte),anchoPantalla/3,(int)(altoPantalla/15*14),texto);
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
