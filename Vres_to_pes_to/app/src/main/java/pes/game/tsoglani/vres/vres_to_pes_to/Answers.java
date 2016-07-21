package pes.game.tsoglani.vres.vres_to_pes_to;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class Answers extends ViewGroup {
	private Answer []answers= new Answer[10]; 
	private int score;
	public static final int correctColor=Color.GREEN;
	public Answers(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		init(context);
	}
	
	private void init(Context context){
	
		for(int i=0 ;i<answers.length;i++){
			answers[i]= new Answer(context);
			addView(answers[i]);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		int size=this.getChildCount();
		
		for(int i=0;i<size;i++){
			View child= this.getChildAt(i);
			for(int j=0 ; j<answers.length;j++){
			if(child ==answers[j]){
			
				child.layout(0, j*b/12+j*3+2, r,  j*b/12+j*3+b/12+2);
				
			}
			}
			
		}
		Log.e("height =", Integer.toString(getHeight()));
	}

	
	
	public Answer[] getAnswers() {
		return answers;
	}

	public void setAnswers(Answer[] answers) {
		this.answers = answers;
	}

	
	public int getCurrentGameScore(){
		 score=0;
		for(int i=0 ; i <answers.length;i++){
			if(answers[i].getBackgroundColor()==Answers.correctColor){
				score+=answers[i].getPoints();
			}
		}
		
		return score;
	}
	
	public void clear(){	
		for(int i=0 ; i <answers.length;i++){
			answers[i].setIsCorrect(false);
			answers[i].setBackgroundColor(Color.GRAY);
			answers[i].setCurrentAnswer("");
			Log.e("clear","clear");
			
		}
	}
	/**
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

}
