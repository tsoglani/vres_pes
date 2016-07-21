package pes.game.tsoglani.vres.vres_to_pes_to;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class VresPesActivity extends Activity {
    private FrameLayout frameLayout;
    private MainGameQuiz quiz;
    private MyMenu menu;
    private Info info;
    private HomeMenu homeMenu;
    private Spinner spinner1, spinner2;
    private Button btnSubmit;
    private PandomimaGameView pandomimaView;
    private 	Mix mix;
    private PowerManager pm ;
    private Database db;
    static PowerManager.WakeLock wl ;
    public static boolean isAddShowing=true;
    private Properties prop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        //MainGameQuiz.hasChosenQuizMode=false;
        goToHomeMenu();
        try{
            if(db.getAllContacts().get(2)==1){
                isAddShowing=true;
            }else{
                isAddShowing=false;
            }
        }catch(Exception e){}

        db.close();
        // getFrameLayout().addView(menu);
        // setContentView(getFrameLayout());

        // newQuizGameStart();
    }

    public void goToHomeMenu() {
        getFrameLayout().removeAllViews();
        getFrameLayout().addView(homeMenu);
        setContentView(getFrameLayout());
    }

//    public void playSound() {
//        AssetFileDescriptor afd;
//        try {
//            afd = getAssets().openFd("finishtime.mp3");
//
//            MediaPlayer player = new MediaPlayer();
//            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
//                    afd.getLength());
//            player.prepare();
//            player.start();
//            vibrate();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    protected void playSound(String fname){
        int resID=getResources().getIdentifier(fname, "raw", getPackageName());

        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        mediaPlayer.start();
    }

//    public void playSound2() {
//        try{
//            vibrate();
//            MediaPlayer mPlayer = MediaPlayer.create(this, R.drawable.finishtime);
//            mPlayer.start();
//        }catch(Exception e){
//            playSound() ;
//        }
//    }
    /**
     *
     */
    public void playWrongSound() {
        try{


playSound("wrong.mp3");
            //	vibrate();
//            MediaPlayer mPlayer = MediaPlayer.create(this, R.drawable.wrong);
//            mPlayer.start();
        }catch(Exception e){
//            playWrongSound2() ;
        }
    }
//    /**
//     *
//     */
//    private void playWrongSound2() {
//        AssetFileDescriptor afd;
//        try {
//            afd = getAssets().openFd("wrong.mp3");
//
//            MediaPlayer player = new MediaPlayer();
//            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
//                    afd.getLength());
//            player.prepare();
//            player.start();
//            vibrate();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void playCorrectSound(){
        try{
//            MediaPlayer mPlayer = MediaPlayer.create(this, R.drawable.corect);
//            mPlayer.start();
        playSound("corect.mp3");
        }catch(Exception e){
//            playCorrectSound2() ;
            e.printStackTrace();
        }
    }
//
//    private void playCorrectSound2() {
//        AssetFileDescriptor afd;
//        try {
//            afd = getAssets().openFd("corect.mp3");
//
//            MediaPlayer player = new MediaPlayer();
//            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
//                    afd.getLength());
//            player.prepare();
//            player.start();
//            vibrate();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for ... milliseconds
        v.vibrate(2000);
    }

    public void clapSound() {
        try{
//            vibrate();
//            MediaPlayer mPlayer = MediaPlayer.create(this, R.drawable.clp);
//            mPlayer.start();
            playSound("clp.mp3");

        }catch(Exception ex){
//            AssetFileDescriptor afd;
//            try {
//                afd = getAssets().openFd("clp.mp3");
//
//                MediaPlayer player = new MediaPlayer();
//                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
//                        afd.getLength());
//                player.prepare();
//                player.start();
//                vibrate();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * init the components
     */
    private void init() {
        setFrameLayout(new FrameLayout(this));
        db= new Database(this);
        // properties= new Properties(this);
        menu = new MyMenu(this);
        info = new Info(this);
        homeMenu = new HomeMenu(this);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        activateLight();
    }


    public void activateLight(){
        if(!VresPesActivity.wl.isHeld()){
            VresPesActivity.wl.acquire();
        }
    }
    /**
     *
     */
    public void goToMenu() {
        getFrameLayout().removeAllViews();
        menu= new MyMenu(this);
        getFrameLayout().addView(menu);
    }

    public void newPandomimaGameStart() {

        pandomimaView = new PandomimaGameView(this);
        getFrameLayout().removeAllViews();
        getFrameLayout().addView(pandomimaView);
        setContentView(getFrameLayout());

    }

    /**
     *
     */
    public void showInfo() {
        getFrameLayout().removeView(menu);
        getFrameLayout().addView(info);
    }

    /**
     *
     */
    public void newQuizGameStart() {
        //try{
        getFrameLayout().removeAllViews();
        quiz = new MainGameQuiz(this);
        //MainGameQuiz.hasChosenQuizMode=true;
        getFrameLayout().addView(quiz);
        setContentView(getFrameLayout());
        //}catch(Exception e){}
    }


    public void newMixGameStart(){
        getFrameLayout().removeAllViews();
        mix  = new Mix(this);
        //MainGameQuiz.hasChosenQuizMode=true;
        getFrameLayout().addView(mix);
        setContentView(getFrameLayout());
    }

    /**
     *
     */
    public void continueQuizGame() {
        getFrameLayout().removeView(menu);
        getFrameLayout().addView(quiz);
        quiz.continueGame();
    }
    public void continueMixGame(){
        getFrameLayout().removeView(menu);
        getFrameLayout().addView(mix);
        mix.continueGame();
    }

    public void continuePandomimaGame(){
        //	Log.e("continuePandomimaGame","continuePandomimaGame");
        getFrameLayout().removeAllViews();
        getFrameLayout().addView(pandomimaView);
        pandomimaView.continuePandomima();
    }

    /**
     *
     */
    public void gotoMenuWhilePlaying() {
        getFrameLayout().removeAllViews();
        menu= new MyMenu(this);
        getFrameLayout().addView(menu);


        //menu.makeContinueButtonVisible(!game.isGameOver());

        if(mix!=null&&mix.getIsGameOver()){
            mix.setIsGameOver(false);
            menu.makeContinueButtonVisible(false);
        }
        else if (pandomimaView!=null&&pandomimaView.isGameOver()||quiz!=null&&quiz.isGameOver()) {
            menu.makeContinueButtonVisible(false);
            if(pandomimaView!=null){
                pandomimaView.setIsGameOver(false);
                pandomimaView.release();
            }
            if(quiz!=null){
                quiz.setIsGameOver(false);
                quiz.release();
            }
        }else{
            menu.makeContinueButtonVisible(true);
        }



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_vres_pes, menu);
//
//        return true;
//    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public void showProperties() {

        setContentView(R.layout.activity_vres_pes);
        prop=	new Properties(this);

    }

    public void backFromProperties() {

        setContentView(getFrameLayout());
    }

    public MyMenu getMyMenu() {

        return menu;
    }
    public Mix getMixView(){
        return mix;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert;
        alert=  new AlertDialog.Builder(this);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle("Closing Activity");
        alert.setMessage("Είσαι σίγουρος οτι θες να κλείσεις την εφαρμογή?");

        alert.setPositiveButton("Ναι", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wl.release();
                System.exit(0);
            }

        });
        alert.setNegativeButton("Οχι", null);
        alert.show();
    }

    //@Override
//	public void onAttachedToWindow()    {
    //   super.onAttachedToWindow();
    // Log.e("Go to ","Main menu");
    //  wl.release();
    //}


//    @Override
//    public void onAttachedToWindow(){  //  gia to kentriko koumpi
//        Log.i("TESTE", "onAttachedToWindow");
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        super.onAttachedToWindow();
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i("wl.release()", "HOME button pressed");
            if(wl.isHeld()){
                wl.release();
            }
            gotoHomeScreen();
            if(quiz!=null){
                quiz.getTimer().setEarlyStop(true);
            }
            //    return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void gotoHomeScreen(){

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
