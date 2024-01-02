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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;

/**
 * Escena que ejecuta el juego principal ademas de establecer algunos valores
 * sobre ojetos creados en otras clases
 * @see Plataforma
 * @see Rana
 * @see Fondo
 * @author Santiago Carrizo Marchena
 * @version 10/03/2023
 */
public class Game extends ControlEscenas{
    /**
     * Numero de la escena
     */
    int numEscena;
    /**
     * Vidas dentro del juego
     */
    public int nVidas=3;
    /**
     * Tiempo en la plataforma
     */
    public int tiempoPlat=0;

    /**
     * Ancho del objeto rana
     */
    static double dimRanaAnch;
    /**
     * Alto del objeto rana
     */
    static double dimRanaAlt;
    /**
     * Ancho del objeto plataforma
     */
    static double dimPlatAnch;
    /**
     * Alto del objeto plataforma
     */
    static double dimPlatAlt;
    /**
     * Pixeles que se mueve la rana por segundo
     */
    static int velocidad;
    /**
     * Puntos establecidos como objetivo para ganar
     */
    static int puntos;

    /**
     * Imagen de la rana
     */
    public Bitmap ranaImg;
    /**
     * Array donde se encuentra la imagen de fondo para desplazarse y sustituirse
     */
    private Fondo[] fondo;

    /**
     * Imagen de la plataforma
     */
    private Bitmap platImg;
    /**
     * Imagen de las vidas
     */
    private Bitmap vidaImgL;
    /**
     * Imagen del agua
     */
    private Bitmap aguaImg;
    /**
     * Imagen de fondo
     */
    private Bitmap bitmapFondo;
    /**
     * Imagen del boton que gestiona la musica
     */
    private Bitmap btnMusica;
    /**
     * Imagen del boton que gestiona el sonido
     */
    private Bitmap btnSonido;
    /**
     * Imagen para regresar al menu principal
     */
    private Bitmap btnMenu;
    /**
     * Imagen del boton de pausar
     */
    private Bitmap btnPausa;
    /**
     * Imagen del boton de despausar
     */
    private Bitmap btnX;

    /**
     * Clase Rana
     */
    static Rana rana;
    /**
     * Clase Plataforma
     */
    static Plataforma plataforma;

    private AudioManager audioManager;
    private SoundPool efectos;
    /**
     * Sonido que realiza al saltar la rana
     * Sonido que realiza la rana al croar tras cierto tiempo
     * Sonido que realiza la rana al chocar con el agua
     */
    private int sonidoSalto,sonidoRana,sonidoAgua;
    final private int maxSonidosSimultaneos=10;
    /**
     * volumen
     */
    int v;

    public MediaPlayer mediaPlayer;

    /**
     * Paint que dibuja todos los Bitmap
     */
    Paint paint;
    /**
     * Paint que dibuja todos los textos
     */
    Paint texto;
    /**
     * Recuadro donde se contiene el texto de la puntuacion
     */
    Rect cuadrPuntuacion;
    /**
     * Recuadro donde se contiene el texto de las vidas y su bitmap
     */
    Rect cuadrVidas;
    /**
     * Recuadro que marca lo que ocupa el agua
     */
    Rect hitboxAgua;

    /**
     * Numero de puntos obtenidos en el juego
     */
    static int puntuacion=0;

    /**
     * Esta variable indica si se ha pulsado la pantalla
     */
    boolean pulsar=false;
    /**
     * Esta variable indica si el juego esta en ejecucion
     */
    public boolean juegoFuncionando=false;
    /**
     * Velocidad establecida en la seleccion de nivel
     */
    int velocidadEstablecida;
    /**
     * Random que marca la flecha de la roca
     */
    double random=6;
    /**
     * Esta variable indica si se ha caido al agua la ultima vez pulsado
     */
    boolean ultAgua=false;

    Vibrator vibrator;
    Typeface typeface;

    private SensorManager sensorManager;
    private Sensor sensor;

    /**
     * Esta variable indica si se esta realizando una pulsacion larga
     */
    private boolean LPress=false;
    /**
     * Esta variable indica si se te agotaron las vidas
     */
    private boolean perdiste=false;
    /**
     * Esta variable indica si llegaste a los puntos requeridos
     */
    private boolean ganaste=false;

    GestureDetectorCompat detectorGestos;

    /**
     * Rectangulo que marca el boton de pausa
     */
    Rect recPause;
    /**
     * Rectangulo que marca el boton de volver al menu
     */
    Rect menu;
    /**
     * Rectangulo que marca el boton de despausar
     */
    Rect cancelPausa;
    /**
     * Marca si el juego esta pausado o no
     */
    boolean pausa=false;
    /**
     * Rectangulo que marca el boton de pausa
     */
    Rect musica;
    /**
     * Rectangulo que marca el boton de pausa
     */
    Rect sonido;

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
    public Game(Context context, int numEscena, int anp, int alp){
        super(context,anp,alp,numEscena);

        recPause= new Rect(anchoPantalla/3,0,anchoPantalla/3*2,altoPantalla/20);
        menu= new Rect(anchoPantalla/4,altoPantalla/10*6,anchoPantalla/4*3,altoPantalla/10*7);
        cancelPausa= new Rect(0,0,anchoPantalla/10,anchoPantalla/10);
        musica=new Rect(anchoPantalla/5,altoPantalla/10*4,anchoPantalla/5*2,altoPantalla/10*5);
        sonido=new Rect(anchoPantalla/5*3,altoPantalla/10*4,anchoPantalla/5*4,altoPantalla/10*5);
        btnMusica= escala(R.drawable.botonmusica,(int)(anchoPantalla/5),(int)(altoPantalla/10));
        btnSonido= escala(R.drawable.botonsonido,(int)(anchoPantalla/5),(int)(altoPantalla/10));
        btnMenu= escala(R.drawable.boton,(int)(anchoPantalla/2),(int)(altoPantalla/10));
        btnPausa= escala(R.drawable.boton,(int)(anchoPantalla/3),(int)(altoPantalla/20));
        btnX =escala(R.drawable.cruz,(int)(anchoPantalla/10),anchoPantalla/10);

        this.numEscena=numEscena;
        paint=new Paint();
        texto= new Paint();

        puntuacion=0;

        ranaImg = escala(R.drawable.ejemrana,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
        platImg = escala(R.drawable.ejemplat,(int)(GameSV.anchoPantalla*dimPlatAnch),(int)(GameSV.altoPantalla*dimPlatAlt));
        aguaImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.agua);
        bitmapFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        aguaImg= escala(R.drawable.agua,anchoPantalla,(int)(GameSV.altoPantalla-GameSV.altoPantalla*0.1));

        detectorGestos = new GestureDetectorCompat(context, new Game.DetectorDeGestos());

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        bitmapFondo = Bitmap.createScaledBitmap( bitmapFondo, GameSV.anchoPantalla, bitmapFondo.getHeight(), true);
        plataforma = new Plataforma(platImg,0,(int)(0.8*GameSV.altoPantalla));
        rana = new Rana(ranaImg,plataforma.posicion.x,plataforma.posicion.y-ranaImg.getHeight());
        cuadrPuntuacion= new Rect(GameSV.anchoPantalla-GameSV.anchoPantalla/3, 0, GameSV.anchoPantalla, GameSV.altoPantalla/25);
        cuadrVidas= new Rect(0,0,(int)(GameSV.anchoPantalla/6),(int)(GameSV.altoPantalla/25));
        vidaImgL = escala(R.drawable.corlleno,(int)(cuadrVidas.width()/2),(int)(cuadrVidas.height()));
        hitboxAgua = new Rect(0,(int)(GameSV.altoPantalla-GameSV.altoPantalla*0.1),GameSV.anchoPantalla,GameSV.altoPantalla);

        fondo = new Fondo[2];
        fondo[0] = new Fondo(bitmapFondo, GameSV.altoPantalla);
        fondo[1] = new Fondo(bitmapFondo, 0, fondo[0].posicion.y - bitmapFondo.getHeight());

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        typeface = context.getResources().getFont(R.font.sweetssmile);
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

        sonidoAgua=efectos.load(context, R.raw.aguasonido,1);
        sonidoRana=efectos.load(context, R.raw.croacksonido,1);
        sonidoSalto=efectos.load(context, R.raw.saltosonido,1);

        v= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer= MediaPlayer.create(context, R.raw.musica);
        mediaPlayer.setVolume(v/2,v/2);
        if(Opciones.musica){
            mediaPlayer.start();
        }
    }

    /**
     * Esta funcion administra todas las variables que cambian en el transcurso
     * del programa como son las variables de colisiones, tiempo, punto, etc
     */
    public void actualizarFisica(){


        velocidadEstablecida=GameSV.altoPantalla/velocidad;
        sensorManager.registerListener(giroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(!pausa){
            rana.salto(GameSV.altoPantalla,GameSV.anchoPantalla,velocidadEstablecida);
        }


        //Colision con la plataforma
        if(rana.hitbox.intersect(plataforma.hitboxP)){


            if(!LPress){
                if(tiempoPlat<100){
                    ranaImg=escala(R.drawable.ejemrana,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                    rana.imagen=ranaImg;
                }else{
                    ranaImg=escala(R.drawable.ejemrana2,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                    rana.imagen=ranaImg;

                }

                if(tiempoPlat==200){
                    //Se reinicia
                    tiempoPlat=0;
                    if(Opciones.sonido){
                        efectos.play(sonidoRana,v,v,1,0,1);
                    }
                }
            }

            rana.plat=true;
            pulsar=false;
            plataforma = new Plataforma(platImg,(int)(plataforma.posicion.x),(int)(0.8*GameSV.altoPantalla));

            rana = new Rana(ranaImg,rana.posicion.x,plataforma.posicion.y-ranaImg.getHeight());

            fondo[0] = new Fondo(bitmapFondo, GameSV.altoPantalla);
            fondo[1] = new Fondo(bitmapFondo, 0, fondo[0].posicion.y - bitmapFondo.getHeight());

            if(!perdiste && !ganaste && !pausa){
                tiempoPlat++;
            }

            if(puntos!=0){
                if(puntuacion==puntos){
                    ganaste=true;
                }
            }
            rana.controlSubir=false;
        }else{
            if(!pausa){
                if(rana.controlSubir){
                    ranaImg=escala(R.drawable.ranadown,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                    rana.imagen=ranaImg;
                }else{
                    ranaImg=escala(R.drawable.ranaup,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                    rana.imagen=ranaImg;
                }
            }

        }

        //Colision con el agua
        if(rana.hitbox.intersect(hitboxAgua)){
            rana.plat=true;
            pulsar=false;
            ultAgua=true;
            plataforma = new Plataforma(platImg,0,(int)(0.8*GameSV.altoPantalla));
            rana = new Rana(ranaImg,plataforma.posicion.x,plataforma.posicion.y-ranaImg.getHeight());
            puntuacion--;

            fondo[0] = new Fondo(bitmapFondo, GameSV.altoPantalla);
            fondo[1] = new Fondo(bitmapFondo, 0, fondo[0].posicion.y - bitmapFondo.getHeight());

            rana.controlSubir=false;
            nVidas--;
            if(nVidas==0){
                perdiste=true;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            }else {
                vibrator.vibrate(300);
            }
            if(Opciones.sonido){
                efectos.play(sonidoAgua,v,v,1,0,1);
            }
        }

        if(!rana.plat && !pausa){
            if(rana.controlSubir) {

                fondo[0].mover(-(GameSV.altoPantalla/80)/2);
                fondo[1].mover(-(GameSV.altoPantalla/80)/2);

                if (fondo[0].posicion.y > GameSV.altoPantalla) {
                    fondo[0].posicion.y = fondo[1].posicion.y - fondo[0].imagen.getHeight();
                }
                if (fondo[1].posicion.y > GameSV.altoPantalla) {
                    fondo[1].posicion.y = fondo[0].posicion.y - fondo[1].imagen.getHeight();
                }

            } else {

                fondo[0].mover((GameSV.altoPantalla/80)/2);
                fondo[1].mover((GameSV.altoPantalla/80)/2);

                if (fondo[0].posicion.y > GameSV.altoPantalla) {
                    fondo[0].posicion.y = fondo[1].posicion.y + fondo[0].imagen.getHeight();
                }
                if (fondo[1].posicion.y > GameSV.altoPantalla) {
                    fondo[1].posicion.y = fondo[0].posicion.y + fondo[1].imagen.getHeight();
                }
            }
        }

    }

    /**
     * Dibuja los objetos, imagenes y textos
     * @param c canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dibujar(Canvas c) {



        if(!pulsar){
            c.drawBitmap(bitmapFondo, 0, 0, null);
        }else{
            c.drawBitmap(fondo[0].imagen, fondo[0].posicion.x, fondo[0].posicion.y, null);
            c.drawBitmap(fondo[1].imagen, fondo[1].posicion.x, fondo[1].posicion.y, null);
        }

        rana.dibujar(c);
        paint.setColor(Color.GREEN);

        texto.setTypeface(typeface);


        plataforma.dibujar(c);
        c.drawBitmap(aguaImg,hitboxAgua.left,hitboxAgua.top,null);

        paint.setAlpha(0);
        c.drawRect(rana.hitbox,paint);

        c.drawRect(plataforma.hitboxP,paint);
        c.drawRect(hitboxAgua,paint);

        paint.setColor(context.getColor(R.color.verdeCuadro));
        c.drawRect(cuadrPuntuacion,paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(cuadrPuntuacion.height()/2);
        paint.setTypeface(typeface);
        c.drawText(context.getString(R.string.puntuacion)+" "+puntuacion, cuadrPuntuacion.left,cuadrPuntuacion.bottom,paint);

        paint.setColor(context.getColor(R.color.verdeCuadro));
        c.drawRect(cuadrVidas,paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(cuadrVidas.height());
        c.drawBitmap(vidaImgL,cuadrVidas.width()-vidaImgL.getWidth(),cuadrVidas.height()-vidaImgL.getHeight(),paint);
        c.drawText(""+nVidas,cuadrVidas.left,cuadrVidas.bottom,paint);


        if(pausa){
            texto.setColor(Color.BLACK);
            c.drawRect(cancelPausa,paint);
            c.drawBitmap(btnMusica,anchoPantalla/5,altoPantalla/10*4,null);
            c.drawBitmap(btnSonido,anchoPantalla/5*3,altoPantalla/10*4,null);
            c.drawBitmap(btnMenu,anchoPantalla/4,altoPantalla/10*6,null);
            c.drawBitmap(btnX,0,0,null);
            texto.setTextSize(btnMenu.getHeight()/4);
            c.drawText(context.getString(R.string.volver),menu.left+menu.width()/10,menu.top+menu.height()/2,texto);
            texto.setTextSize(anchoPantalla/10);
            c.drawText(context.getString(R.string.pausa),anchoPantalla/4,altoPantalla/5,texto);

            if(Opciones.sonido){
                texto.setColor(Color.GREEN);
                c.drawText(context.getString(R.string.on),(int)(anchoPantalla/5*3.2),(int)(altoPantalla/10*5.5),texto);
            }else{
                texto.setColor(Color.RED);
                c.drawText(context.getString(R.string.off),(int)(anchoPantalla/5*3.2),(int)(altoPantalla/10*5.5),texto);
            }

            if(Opciones.musica){
                texto.setColor(Color.GREEN);
                c.drawText(context.getString(R.string.on),(int)(anchoPantalla/5*1.2),(int)(altoPantalla/10*5.5),texto);
            }else{
                texto.setColor(Color.RED);
                c.drawText(context.getString(R.string.off),(int)(anchoPantalla/5*1.2),(int)(altoPantalla/10*5.5),texto);
            }
        }else{
            c.drawBitmap(btnPausa,anchoPantalla/3,0,null);
            texto.setColor(Color.BLACK);
            texto.setTextSize(recPause.height());
            c.drawText(context.getString(R.string.pausa),anchoPantalla/3,altoPantalla/20,texto);
        }



        c.save();
    }

    /**
     * OnTouchEvent
     * @param event MotionEvent
     * @return en caso de perder todas las vidas devuelve la escena de Perder
     * @see Perder
     * @return en caso de conseguir los puntos necesarios devuelve la escena de Ganar
     * @see Ganar
     * @return devuelve la misma escena por lo que no cambia ademas de que ejecuta actualizarFisica
     */
    public int onTouchEvent(MotionEvent event) {
            juegoFuncionando=true;
            detectorGestos.onTouchEvent(event);
            int x=(int)event.getX();
            int y=(int)event.getY();
            int accion = event.getActionMasked();
        Log.i("in","entra -> "+accion);
            switch (accion){
                case MotionEvent.ACTION_DOWN:
                    Log.i("in","entra");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_UP:

                case MotionEvent.ACTION_POINTER_UP:

                    if(recPause.contains(x,y)){
                        if(!pausa){
                            mediaPlayer.pause();
                            efectos.pause(sonidoRana);
                            efectos.pause(sonidoSalto);
                            efectos.pause(sonidoAgua);
                            pausa=true;
                        }
                    }

                    if(cancelPausa.contains(x,y)){
                        if(pausa){
                            if(Opciones.musica){
                                mediaPlayer.start();
                            }
                            efectos.resume(sonidoRana);
                            efectos.resume(sonidoSalto);
                            efectos.resume(sonidoAgua);
                            pausa=false;
                        }
                    }

                    if(menu.contains(x,y)){
                        if(pausa)
                        return 1;
                    }

                    if(musica.contains(x,y)){
                        if(pausa){
                            if(Opciones.musica){
                                Opciones.musica=false;
                                editor.putBoolean("musica",false);
                                editor.apply();
                            }else {
                                Opciones.musica=true;
                                editor.putBoolean("musica",true);
                                editor.apply();
                            }
                        }
                    }

                    if(sonido.contains(x,y)){
                        if(pausa){
                            if(Opciones.sonido){
                                Opciones.sonido=false;
                                editor.putBoolean("sonido",false);
                                editor.apply();
                            }else {
                                Opciones.sonido=true;
                                editor.putBoolean("sonido",true);
                                editor.apply();
                            }
                        }
                    }

                    if(!pausa && !cancelPausa.contains(x,y)){
                        if(!ganaste){
                            if(!perdiste){
                                if(!pulsar){
                                    if(Opciones.sonido){
                                        efectos.play(sonidoSalto,v,v,1,0,1);
                                    }
                                    if(random<=5){
                                        if(!LPress){
                                            actualizarFisica();
                                            rana.control=false;
                                            rana.plat=false;
                                            pulsar=true;
                                            puntuacion++;
                                            plataforma = new Plataforma(platImg,GameSV.anchoPantalla,GameSV.altoPantalla);
                                        }else{
                                            actualizarFisica();
                                            rana.control=false;
                                            rana.plat=false;
                                            pulsar=true;
                                            puntuacion++;
                                            plataforma = new Plataforma(platImg,(int)((GameSV.anchoPantalla-platImg.getWidth())*Math.random()),(int)(0.8*GameSV.altoPantalla));
                                        }
                                    }else{
                                        if(!LPress){
                                            actualizarFisica();
                                            rana.control=false;
                                            rana.plat=false;
                                            pulsar=true;
                                            puntuacion++;
                                            plataforma = new Plataforma(platImg,(int)((GameSV.anchoPantalla-platImg.getWidth())*Math.random()),(int)(0.8*GameSV.altoPantalla));
                                        }else{
                                            actualizarFisica();
                                            rana.control=false;
                                            rana.plat=false;
                                            pulsar=true;
                                            puntuacion++;

                                            plataforma = new Plataforma(platImg,GameSV.anchoPantalla,GameSV.altoPantalla);
                                        }
                                    }
                                    LPress=false;

                                    if(!ultAgua){
                                        random= Math.random()*10+1;
                                    }

                                    Log.i("random",random+"");
                                    if(random>5){
                                        platImg=escala(R.drawable.ejemplat,(int)(GameSV.anchoPantalla*dimPlatAnch),(int)(GameSV.altoPantalla*dimPlatAlt));
                                        plataforma= new Plataforma(platImg,(int)plataforma.posicion.x,(int)plataforma.posicion.y);
                                    }else{
                                        platImg=escala(R.drawable.ejemplat2,(int)(GameSV.anchoPantalla*dimPlatAnch),(int)(GameSV.altoPantalla*dimPlatAlt));
                                        plataforma= new Plataforma(platImg,(int)plataforma.posicion.x,(int)plataforma.posicion.y);
                                    }
                                    ultAgua=false;
                                }
                                ranaImg=escala(R.drawable.ejemrana,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                                rana.imagen=ranaImg;
                            }else{
                                mediaPlayer.stop();
                                return 4;
                            }
                        }else{
                            mediaPlayer.stop();
                            return 5;
                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        int aux=super.onTouchEvent(event);
        if (aux!=this.numEscena && aux!=-1) return aux;
        return this.numEscena;
    }

    /**
     * Expansion del onTouchEvent para otras funciones
     */
    class DetectorDeGestos extends GestureDetector.SimpleOnGestureListener{
        /**
         * Funcion que se activa con una pulsacion larga
         * @param e evento
         */
        public void onLongPress(MotionEvent e) {
            if(!pulsar){
                if(!perdiste){
                    LPress=true;
                    ranaImg=escala(R.drawable.ejemranar,(int)(GameSV.anchoPantalla*dimRanaAnch),(int)(GameSV.anchoPantalla*dimRanaAlt));
                    rana.imagen=ranaImg;
                    Log.i("long","presiona largo");

                }
            }
        }
    }

    /**
     * Receptor de diferentes sensores hardware del dispositivo para su implementacion
     */
    public SensorEventListener giroListener = new SensorEventListener() {
        /**
         * Receptor del sensor que en este caso es el giroscopio
         * @param sensorEvent
         */
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            rana.giroscopio=(int)(sensorEvent.values[2]);
        }

        /**
         * Precision del sensor
         * @param sensor
         * @param i
         */
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

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
