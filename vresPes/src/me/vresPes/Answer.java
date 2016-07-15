package me.vresPes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class Answer extends ViewGroup {

	private boolean isCorrect = false;
	private String currentAnswer;
	private int points = 0;
	private int currentColor=Color.GRAY;
    private TextView txtView;
	
	public Answer(Context context) {
		super(context);
		txtView= new TextView(context);
		setWillNotDraw(false);
          addView(txtView);
         // txtView.setVisibility(VISIBLE);
          txtView.setTextSize(12);
         // txtView.setTextColor(Color.BLACK);
         // txtView.setBackgroundColor(Color.TRANSPARENT);
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isCorrect = !isCorrect;
Log.e(Boolean.toString(isCorrect),Boolean.toString(isCorrect));
if(isCorrect){
	//setBackgroundColor(Answers.correctColor);
	currentColor = Answers.correctColor;
}else{
	//setBackgroundColor(Color.GRAY);
	currentColor = Color.GRAY;
}
				setBackgroundColor(currentColor);
				return false;
			}
		});

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int countChild= getChildCount();

		for(int i=0; i<countChild;i++){
			View child= getChildAt(i);
			child.layout(0, 0, getWidth() - getWidth() / 11,getHeight());
		}
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		
		
		/*
		if (isCorrect) {
			paint.setColor(Answers.correctColor);
			currentColor = Answers.correctColor;
		} else {
			paint.setColor(Color.GRAY);
			currentColor = Color.GRAY;
		}
		//paint.setColor(currentColor);
		//canvas.drawPaint(paint);
		*/
		
		paint.setColor(Color.YELLOW);
		canvas.drawCircle(getWidth() - getWidth() / 15, getHeight()/2, getHeight()/3, paint);
		paint.setColor(Color.GRAY);
		canvas.drawCircle(getWidth() - getWidth() / 15,  getHeight()/2, getHeight()/3-3, paint);
		paint.setColor(Color.BLACK);
	  paint.setTextSize(getHeight()/3);        //////////////////-----------------
		canvas.drawText(Integer.toString(points), getWidth() - getWidth() / 13,
				 getHeight()/2+ getHeight()/14, paint);
		
		
		
		//paint.setColor(Color.BLACK);
	//	paint.setTextSize(18);
		//if (currentAnswer != null) {
		//	canvas.drawText(currentAnswer, 0, 20, paint);
	//	}
		
	}

	public String getCurrentAnswer() {
		return currentAnswer;
	}

	public void setCurrentAnswer(String curAnswer) {
		String[] nameWithValue = curAnswer.split("_");
		if (nameWithValue.length > 1) {
			this.currentAnswer = nameWithValue[0];
			try{
			this.points = Integer.parseInt(nameWithValue[1]);
			}catch(Exception e){
				
				for(int i=0 ; i<=20 ;i++){
					Log.e(" errorrrrrr" +currentAnswer,curAnswer);
				}
				randomScore();
			}
		} else {
		//	randomScore();
			this.currentAnswer = curAnswer;
		}
		txtView.setText(currentAnswer);
		
		
		if (MainGameQuiz.isUsingRandomScore) {
			randomScore();
		}

	}

	private void randomScore() {
		double randomNum = (Math.random() * 3);
		if (randomNum <= 1) {
			this.points = 1;
		} else if (randomNum <= 2) {
			this.points = 3;
		} else {
			this.points = 5;
		}

	}

	public int getPoints() {
		return points;
	}

	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public int getBackgroundColor() {
		return currentColor;
	}
	public void setBackgroundColor(int color){
		super.setBackgroundColor(color);
		currentColor=color;
	}

}
