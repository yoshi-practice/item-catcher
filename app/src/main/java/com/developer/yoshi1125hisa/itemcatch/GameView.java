package com.developer.yoshi1125hisa.itemcatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.logging.Handler;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    static final long FPS = 30;
    static final long FRAME_TIME = 1000 / FPS;

    SurfaceHolder holder;

    Thread thread;
    Present present;
    Bitmap presentImage;

    Player player;
    Bitmap playerImage;

    int screenWidth;
    int screenHeight;

    int score = 0;
    int life =10;

    class Present{
        private static final int WIDTH = 100;
        private static final int HEIGHT = 100;


        float x,y;

        public Present(){

            Random random = new Random();
            x = random.nextInt(screenWidth - WIDTH);
            y = 0;
        }

        public void update(){
            y += 15.0f;
        }

        public void reset(){
            Random random = new Random();
            x = random.nextInt(screenWidth - WIDTH);
            y = 0;
        }

    }

    class Player{

        final int WIDTH = 200;
        final int HEIGHT = 200;

        float x,y;

        public Player(){
            x = 0;
            y = screenHeight - HEIGHT;
        }

        public void move (float diffX){
            this.x += diffX;
            this.x  = Math.max(0,x);
            this.x = Math.min(screenWidth - WIDTH, x);
        }

        public boolean isEnter(Present present){
            if(present.x + Present.WIDTH > x && present.x <  + WIDTH && present.y + Present.HEIGHT > y&& present.y < y+ HEIGHT){
                return true;
            }
            return  false;
        }

    }



    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        Resources resources = context.getResources();
        presentImage = BitmapFactory.decodeResource(resources,R.drawable.a);
        playerImage = BitmapFactory.decodeResource(resources,R.drawable.a);



    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        holder = surfaceHolder;
        thread = new Thread(this);
        thread.start();



    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        screenHeight = height;
        screenWidth = width;



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    thread = null;
    }


    @Override
    public void run() {

        present = new Present();
        player = new Player();

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(100);

        while (thread != null){


            //Paint paint = new Paint();

            //paint.setColor(Color.BLUE);

            //paint.setStyle(Paint.Style.FILL);

            Canvas canvas = holder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            //canvas.drawCircle(100,200,50,paint);

            canvas.drawBitmap(presentImage,present.x,present.y,null);
            canvas.drawBitmap(playerImage,player.x,player.y,null);


            //当たり判定
            if (player.isEnter(present)){
                score += 10;
                present.reset();
            }else if(present.y > screenHeight){
                present.reset();
                life--;
            }else{
                present.update();
            }

            if(present.y > screenHeight){
                present.reset();
            }else {
                present.update();
            }

            canvas.drawText("SCORE："+score,50,150,textPaint);
            canvas.drawText("LIFE："+life,50,300,textPaint);

            if(life <= 0){
                canvas.drawText("GAME OVER",screenWidth /3 ,screenHeight / 2,textPaint);
                holder.unlockCanvasAndPost(canvas);
                break;
            }

            present.update();
            holder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(FRAME_TIME);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
