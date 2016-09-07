package com.game.tsoglanakos.ac.vresToPesTo;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Info extends ViewGroup{
private Button goBack;
 private VresPesActivity context;
private ImageView imageView;

	public Info(VresPesActivity context) {
		super(context);
		setWillNotDraw(false);
        this.context=context;
        goBack=new Button(context);
        imageView= new ImageView(context);
        Drawable myIcon = getResources().getDrawable(R.drawable.info);
        imageView.setBackgroundDrawable(myIcon);
        goBack.setText("πίσω");
        addView(imageView);
        addView(goBack);
        goBack.setOnClickListener(goBackListener);
	}
	/**
	 * 
	 */
	OnClickListener goBackListener= new OnClickListener(){

		@Override
		public void onClick(View v) {
			context.goToMenu();	
		}
		
	};
/**
 * 
 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
       int size=this.getChildCount();
		
		for(int i=0;i<size;i++){
			View child=this.getChildAt(i);
			if(child==goBack){
				child.layout(0, getHeight()-getHeight()/10, getWidth()/5, getHeight());
			}
			if(child==imageView){
				child.layout(0,  getHeight()/2, getWidth(), getHeight());
			}
			
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		Paint paint= new Paint();
		int counter=0;
		paint.setColor(Color.WHITE);
		canvas.drawPaint(paint);
		paint.setColor(Color.LTGRAY);
	    paint.setTextSize(25);
	    String info1=null,info2=null,info3=null,info4=null,info5=null,info6=null,info7=null;
	    if(!Mix.isMixSelected){
	    if(MainGameQuiz.hasChosenQuizMode){
	     info1="το παιχνιδι παιζεται με 4  ";
	     info2="ή περισοτερους παιχτες ";
	     info3="ο καθε παιχτης κανει μια ερωτηση και ο  ";
	     info4=" αντιπαλος λεει 10 σχετικες λεξεις ή φρασεις . ";
	     info5="Η καθε μια απο τις απαντησεις περνει απο 1 μεχρι 5 βαθμους  ";
	     info6="νικητης ειναι ο παιχτης με τους περισοτερους βαθμους  ";
	    }else{
	    	info1="το παιχνιδι παιζεται με 2  ";
	    	 info2="ή περισοτερους παιχτες ";
	    	 info3="ο παίχτης που εχει σειρά προσπαθει με κινησεις ";
	    	 info4="να κανει τον συμπαίκτη του να βρει την ζητουμενη λεξη ";
	    	  info5="ή φράση .Η λαθος απάντηση τιμωρειτε με -5 βαθμους, ";
	    	  info6="η σωστη περνει 10 + βαθμους αναλογα με το χρονο που απάντησε ";
	    	  info7="νικητης ειναι ο παιχτης με τους περισοτερους βαθμους ";
	    }}
	    else{
	        info1="το παιχνιδι παιζεται με 4  ";
		     info2="ή περισοτερους παιχτες ";
		     info3="ειναι συνδιασμός παντομίμας και quiz  ";
		     info4="καθε φορά επιλέγεται τυχαια ενα απο τα δύο παιχνίδια ";
	    }
	    if(info1!=null)
	    canvas.drawText(info1, 0, getHeight()/20+counter++*30, paint);
	    if(info2!=null)
	    canvas.drawText(info2, 0, getHeight()/20+counter++*30, paint);
	    paint.setTextSize(15);
	    counter++;
	    paint.setColor(Color.BLACK);
	    if(info3!=null)
	    canvas.drawText(info3, 0, getHeight()/20+counter++*20, paint);
	    if(info4!=null)
	    canvas.drawText(info4, 0, getHeight()/20+counter++*20, paint);
	    if(info5!=null)
	    canvas.drawText(info5, 0, getHeight()/20+counter++*20, paint);
	    if(info6!=null)
	    canvas.drawText(info6, 0, getHeight()/20+counter++*20, paint);
	    if(info7!=null)
		    canvas.drawText(info7, 0, getHeight()/20+counter++*20, paint);
	    paint.setTextSize(10);
	    paint.setColor(Color.DKGRAY);
	    canvas.drawText(" ελπιζω να το ευχαριστηθειτε  ", getWidth()/10, getHeight()/20+8*20, paint);
	    paint.setTextSize(30);
	    paint.setColor(Color.BLUE);
	    canvas.drawText(" καλη διασκεδαση  ", 0, getHeight()/20+10*20, paint);
	    paint.setTextSize(15);
	    paint.setColor(Color.RED);
	    canvas.drawText(" directed by tsoglani ", 0, getHeight()/20+13*20, paint);
	}

}
