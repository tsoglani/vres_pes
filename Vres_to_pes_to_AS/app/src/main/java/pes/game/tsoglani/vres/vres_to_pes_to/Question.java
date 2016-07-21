package pes.game.tsoglani.vres.vres_to_pes_to;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


public class Question extends ViewGroup implements Runnable{
private String question;
 private Bitmap mBitmap;
private int timer=0;
private VresPesActivity context;
private boolean haveEarlyFinish;
private float textSize=20;
private int backgroundColor=0;
private int foroundColor=0;
private int textColor=0;
private boolean showTimer=false;
private TextView txtView;
private static final int delayInt=10;
private int timeLeft;// gia ton ipologismo tis vathmologias na xrisimopoihsw to poso grigora apandithike
	public Question(VresPesActivity context) {
		super(context);
		this.context=context;
		// TODO Auto-generated constructor stub
	
		txtView= new TextView(context);
		txtView.setBackgroundColor(Color.TRANSPARENT);
		txtView.setTextColor(Color.WHITE);
	//	txtView.setTextSize(20);
		addView(txtView);
		setWillNotDraw(false);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	int countChild=this.getChildCount();
	for(int i=0;i<countChild;i++){
		View child= this.getChildAt(i);
		if(child instanceof TextView){
			child.layout(l, t, r, b);
		}
	}
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		if(backgroundColor==0){
		paint.setColor(Color.BLACK);
		}else{
			paint.setColor(backgroundColor);
		}
		
		canvas.drawPaint(paint);
		if(mBitmap!=null){
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		}
		
		
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, (int)((double)timer*getWidth()/(MainGameQuiz.totalTime*delayInt)), getHeight(), paint);
		/*
		 * if(foroundColor==0){
		paint.setColor(Color.WHITE);
		}else{
			paint.setColor(foroundColor);
		}
		paint.setTextSize(textSize);
		paint.setTextScaleX((float) 1.3);
		if(question!=null)
		canvas.drawText(question, 0, getHeight()/3, paint);
		
		Log.e("onDraw",Integer.toString(timer));
		*/
	}

	public void clear(){
		question="";
		setBackgroundColor(Color.BLACK);
	}
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
		if(question!=null){
		txtView.setText(question);
		}else{
			txtView.setText("");
		}
		}

	public void setEarlyStop(boolean timeIsUp)	{
		this.haveEarlyFinish=timeIsUp;
	}
	
	
	@Override
	public synchronized  void run() {
		try{
		timer=MainGameQuiz.totalTime*delayInt;
		haveEarlyFinish=false;
		//Log.e("timer",Integer.toString(timer));
		
		
		while(timer>=0){
			try {
			//	Log.e("timer = ",Integer.toString(timer));
				
					
					if(haveEarlyFinish){
					timeLeft=timer;
					timer=0;
					Log.e("early stop", "early stop");
					
					context.runOnUiThread(new Runnable(){
						@Override
						public void run() {
						
							setBackgroundColor(Color.WHITE);
							invalidate();
							
						}
					});
					break;
				}
				timer-=1;
				if(timer<=0){
					break;
				}
				Thread.sleep(1000/delayInt);
				context.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						if(showTimer){
							setQuestion(Integer.toString(timer/delayInt));
						}
					//	Log.e("onDraw",Integer.toString(timer));
						setBackgroundColor(Color.WHITE);
						invalidate();
					}
				});
				
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!haveEarlyFinish){
			context.playSound("finishtime.mp3");
			
			}
	
				
		}catch(Exception e){
			
		}catch(Error e){
			
		}
		
	}
	
	public void setTextSize(float txtSize){
		textSize=txtSize;
		txtView.setTextSize(txtSize);
	}
	public void setBkgrndColor(int color){
		this.backgroundColor=color;

	}
	
	
	public void setForoundColor(int color){
		this.foroundColor=color;
		txtView.setTextColor(color);
	}
	public int getTime(){
		return timer;
	}
	public void setShowTimer(boolean showTimer){
		this.showTimer=showTimer;
	}
	public void setTextColor(int textColor){
		this.textColor=textColor;
		txtView.setTextColor(textColor);
	}
	
	public void setBitmap(int id){
		try{
		this.mBitmap = BitmapFactory.decodeResource(getResources(),id);;
	}catch(OutOfMemoryError e){}
		}
	public void resetBitmap(){
		this.mBitmap =null;
		this.setBackgroundColor(Color.WHITE);
	}
	public int getTimeLeft(){
	return timeLeft/10;
	}
	
}
