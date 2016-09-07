package com.game.tsoglanakos.ac.vresToPesTo;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Mix extends ViewGroup {
	private int state = 0;
	private boolean isReadyToDrawScore=true;
	private int round=0;
	private Button next, menu;
	private boolean isTeamATurn = true;
	public static boolean isMixSelected = false;
	private int teamAColor = Color.BLUE, teamBColor = Color.GRAY;
	private VresPesActivity context;
	private MainGameQuiz quiz;
	private PandomimaGameView pandomime;
	private ArrayList <String> quizQuestions = new ArrayList<String>();
    private ArrayList<String> pantomimes = new ArrayList<String>();
    private ArrayList<Integer> teamAScore = new ArrayList<Integer>();
    private ArrayList<Integer> teamBScore = new ArrayList<Integer>();
    private int scoreATeam,scoreBTeam;
    private Button correct;
	private Button wrong;
	private Bitmap mBitmap=null;
	private Bitmap gameOverBitmap=null;
	private boolean isGameOver=false;
	public Mix(VresPesActivity context) {
		super(context);
		this.context = context;
		setWillNotDraw(false);
		fillPandomimeList();
		fillQuizesList();
		init();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private void init() {
		next = new Button(context);
		menu = new Button(context);
		wrong = new Button(context);
		correct= new Button(context);
		correct.setText("Την βρηκε !");
		wrong.setText("Δεν την βρηκε !");
		correct.setOnClickListener(correctButtonListener);
		wrong.setOnClickListener(correctButtonListener);
		correct.setBackgroundResource(R.drawable.crct);
		wrong.setBackgroundResource(R.drawable.wrg);
		next.setText("Συνέχεια");
		menu.setText("Μενού");
		menu.setOnClickListener(menuButtonListener);
		addButtons();
		next.setOnClickListener(nextListener);
		
		
	}
	OnClickListener menuButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			context.gotoMenuWhilePlaying();
			
			
		}};
	
	OnClickListener correctButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			removeView(v);
			isReadyToDrawScore=true;
			int correctAnswerScore=10;
			if(pandomime!=null){ // logika tha isxyei panda 
			 correctAnswerScore=(int)(PandomimaGameView.scoreMulti*pandomime.getRemainingTime()+10);
			 }
			if(correct==v){
				if(isTeamATurn){
					teamAScore.add(correctAnswerScore);
					scoreATeam+=+correctAnswerScore;
				}else{
					teamBScore.add(correctAnswerScore);
					scoreBTeam+=+correctAnswerScore;
					//round++;
				}
			}else{
				if(isTeamATurn){
					teamAScore.add(-5);
					scoreATeam+=-5;
				}else{
					teamBScore.add(-5);
					scoreBTeam+=-5;
					//round++;
				}
			}
		
		Mix.this.removeAllViews();
		Mix.this.addButtons();
			//isTeamATurn=!isTeamATurn;
		
			setBackgroundColor(Color.WHITE);

		}
		
		
	};
			
			

	private void fillQuizesList(){
		isMixSelected=false;
		quiz= new MainGameQuiz(context);
		quizQuestions= quiz.getListOfQuestionsWithAnswers();
		isMixSelected=true;
		
	}
	private void fillPandomimeList(){
		isMixSelected=false;
		pandomime= new PandomimaGameView(context);
		pantomimes= pandomime.getAllPandomimes();
		isMixSelected=true;
		
	}

	
	private OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			state++;
			state = state % 2;
		
			if (state == 0) {
				isTeamATurn = !isTeamATurn;
				if(isTeamATurn){
					round++;
				}

			}
			if (state == 1) {
				Random rand= new Random();
				Boolean isPandomimaMode=rand.nextBoolean();
				if(isPandomimaMode){
					addPandomimeMode();
				}else{
					addQuizMode();
				}

			}
			if(round>=MainGameQuiz.totalRounds){
				isGameOver=true;
			}
			
			if(isGameOver){
                context.clapSound();
				removeView(next);
				removeView(pandomime);
				removeView(quiz);
			}
				
			//Log.e(Integer.toString(state), "fucking state");
			setBackgroundColor(Color.WHITE);
			
			
			context.activateLight();
		}
	};

	
	public void setScore(int score){
		if(this.isTeamATurn){
			teamAScore.add(score);
			scoreATeam+=score;
		}else{
			teamBScore.add(score);
			scoreBTeam+=score;
		}
		Log.e("score", Integer.toString(score));
	}
	
	/**
	 * 
	 */
	public void addQuizMode() {
		isReadyToDrawScore=true;
		quiz = new MainGameQuiz(context);
		addView(quiz);
		removeButtons();
	}
	/**
	 * 
	 */
	public void addPandomimeMode() {
		isReadyToDrawScore=false;
		pandomime= new PandomimaGameView(context);
		addView(pandomime);
		
		removeButtons();
	}
	
	public void addCorrectWrongButons(){
		addView(correct);
		addView(wrong);
		addView(menu);
	}
	
	/**
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int countChild = getChildCount();
		for (int i = 0; i < countChild; i++) {
			View child = getChildAt(i);
			if (child instanceof ViewGroup) {
				child.layout(l, t, r, b);
			}
			if (child == next) {
				child.layout(r / 2 + r / 10, b / 2 + b / 3 + b / 11, r, b);
				next.setTextSize(next.getHeight()/10);
			}
			if (child == menu) {
				child.layout(r / 20, b / 2 + b / 3 + b / 11, r / 3, b);
				menu.setTextSize(menu.getHeight()/10);
			}
            if (child == correct) {
				
				child.layout(getWidth()/5,getHeight()/10, r/2+r/3, getHeight() /2);
			}
			if (child == wrong) {
				child.layout(getWidth()/5,getHeight()/2, r/2+r/3, getHeight() / 2+getHeight() / 3);
			}
		}
	}
/**
 * 
 */
	public void removeButtons() {

		removeView(next);
		removeView(menu);
	}
/**
 * 
 */
	public void addButtons() {

		addView(next);
		try {
		addView(menu);
	}catch(OutOfMemoryError e ){
		
	}
		}
	
	public void continueGame(){
		if(quiz!=null){
			Log.e("quiz "," mode ");
		quiz.continueGame();
		}
		if(pandomime!=null)
	pandomime.continuePandomima();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();



		if (state == 0&&!isGameOver) {
			try{
				mBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.micman);
					float scaleX=(getWidth()/(float)(mBitmap.getWidth()));
					float scaleY=(getHeight()/(float)(mBitmap.getHeight()+mBitmap.getHeight()/10));

					mBitmap=getResizedBitmap(mBitmap,scaleX,scaleY);
					canvas.drawBitmap(mBitmap, 0, 0,paint);


			}catch(OutOfMemoryError e){}
			paint.setTextSize(20);
			if (isTeamATurn) {
				paint.setColor(teamAColor);
				canvas.drawText("Ομαδα 'Α'", getWidth() / 10, getHeight() / 15,
						paint);
			} else {
				paint.setColor(teamBColor);
				canvas.drawText("Ομαδα 'Β'", getWidth() / 10, getHeight() / 15,
						paint);
			}
			paint.setTextSize(getWidth()/30);
			paint.setColor(Color.BLACK);
			canvas.drawText("(Πάτα συνέχεια , για να εμφανιστει η ερωτηση)",
					getWidth() / 10, getHeight() / 10, paint);

		}
		
		else if ((state == 1)&&isReadyToDrawScore){
			this.drawScores(canvas, paint);
		}
		
		
		if(this.isGameOver){
			try{
				if(gameOverBitmap==null){
				gameOverBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.winner);
				}
			canvas.drawBitmap(gameOverBitmap, 0, 0,paint);
			}catch(OutOfMemoryError e){}
			paint.setTextSize(getWidth()/15);
			if (scoreATeam > scoreBTeam) {
				canvas.drawText("Ο παιχτης 1 ειναι ο νικητης : " , getWidth() / 10,
						getHeight() - getHeight() / 6, paint);
				
				
			} else if (scoreATeam < scoreBTeam) {
				canvas.drawText("Ο παιχτης 2 ειναι ο νικητης", getWidth() / 10,
						getHeight() - getHeight() / 6, paint);
			} else {
				canvas.drawText("Εχουμε ισοπαλια", getWidth() / 10, getHeight()
						- getHeight() / 6, paint);
			
			}
			paint.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(scoreATeam)+"   Vs   "+Integer.toString(scoreBTeam),getWidth() / 10,
					getHeight() - getHeight() / 8, paint);
			Log.e("score = ",Integer.toString(scoreATeam)+"   _   "+Integer.toString(scoreBTeam));
			
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, float scaleX, float scaleY) {
		int width = bm.getWidth();
		int height = bm.getHeight();
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleX, scaleY);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(
				bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		System.gc();
		return resizedBitmap;
	}
	public ArrayList<String> getQuizes(){
		return quizQuestions;
	}
	
	public ArrayList<String> getPandomimes(){
		return pantomimes;
	}
	
	
	
	/**
	 * 
	 * @param canvas
	 */
	private void drawScores(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLACK);
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
		paint.setColor(Color.DKGRAY);
		paint.setTextSize(getWidth()/2/17);
		canvas.drawText("Παιχτης 1 βαθμολογίες ", 0, getHeight() / 7, paint);
		paint.setColor(Color.LTGRAY);
		canvas.drawText("Παιχτης 2 βαθμολογίες ", getWidth() / 2, getHeight() / 7,
				paint);
		paint.setColor(Color.BLACK);
		canvas.drawLine(0, getHeight() / 6, getWidth(), getHeight() / 6, paint);
		int txtSize=10;
		paint.setTextSize(txtSize);
		
		for (int i = 0; i < teamAScore.size(); i++) {
			paint.setColor(Color.BLACK);

			canvas.drawText(Integer.toString(teamAScore.get(i)),
					getWidth() / 40, getHeight() / 5 + i * (txtSize+2), paint);
		}
		paint.setTextSize(2*txtSize);
		if (teamAScore.size() > 0) {
			int height= getHeight() / 5 + (teamAScore.size()) * (txtSize+2)-txtSize;
			canvas.drawLine(0, height, getWidth() / 2,height, paint);
			canvas.drawText(Integer.toString(this.scoreATeam),
					getWidth() / 50, getHeight() / 5 + teamAScore.size()* (txtSize+2)+10, paint);
		}
		
		paint.setTextSize(txtSize);
		for (int i = 0; i < teamBScore.size(); i++) {
			canvas.drawText(Integer.toString(teamBScore.get(i)),
					getWidth() / 2 + getWidth() / 40, getHeight() / 5 + i * (txtSize+2),
					paint);
			// canvas.drawText(Integer.toString(playerTwoGrades.get(i)),
			// getWidth()/20, i*getHeight()/30+30, paint);
		}
		
		
		paint.setTextSize(2*txtSize);
		if (teamBScore.size() > 0) {
			int height= getHeight() / 5 + (teamBScore.size()) * (txtSize+2)-txtSize;
			canvas.drawLine(getWidth() / 2,height,getWidth(),height, paint);
			canvas.drawText(Integer.toString(this.scoreBTeam), getWidth()/ 2 + getWidth() / 50,
					getHeight() / 5 + (teamBScore.size()) * (txtSize+2)+10, paint);
		}

	}
	public boolean isTeamATurn(){
		return isTeamATurn;
	}
	
	public void setIsGameOver(boolean gameOver){
		this.isGameOver=gameOver;
	}
	public boolean getIsGameOver(){
		return isGameOver;
	}
}
