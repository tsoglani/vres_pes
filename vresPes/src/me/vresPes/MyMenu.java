package me.vresPes;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyMenu extends ViewGroup {
	private Button newGameButton;
	private Button continueButton;
	private Button optionsButton;
	private Button infoButton;
	private Button exitButton;
	private Button homeMenuButton;
	private VresPesActivity context;
 
	public MyMenu(VresPesActivity context) {
		super(context);
		this.context = context;
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(VresPesActivity context) {
		  Database db= new Database(context) ;
		try{
			if(db.getAllContacts().get(2)==1){
				VresPesActivity.isAddShowing=true;
			}else{
				VresPesActivity.isAddShowing=false;
			}
			}catch(Exception e){}
			
			db.close();
		
		
		newGameButton = new Button(context);
		continueButton = new Button(context);
		optionsButton = new Button(context);
		infoButton = new Button(context);
		exitButton = new Button(context);
		homeMenuButton = new Button(context);
		continueButton.setText("Συνέχεια");
		newGameButton.setText("Νεο παιχνίδι");
		optionsButton.setText("Ρυθμισεις");
		infoButton.setText("Οδηγιες");
		homeMenuButton.setText("Αρχικη σελιδα");
		exitButton.setText("Εξοδος");
		addView(continueButton);
		addView(newGameButton);
		addView(optionsButton);
		//if(MainGameQuiz.hasChosenQuizMode)
		addView(infoButton);
		addView(homeMenuButton);
		addView(exitButton);
		continueButton.setVisibility(INVISIBLE);
		newGameButton.setOnClickListener(newGameButtonListener);
		continueButton.setOnClickListener(continueButtonListener);
		exitButton.setOnClickListener(exitButtonListener);
		infoButton.setOnClickListener(infoButtonListener);
		optionsButton.setOnClickListener(optionsButtonListener);
		homeMenuButton.setOnClickListener(homeMenuButtonListener);
	}
	
	
	
	/**
	 * 
	 */
	private OnClickListener optionsButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			context.showProperties();
		}
	};

	
	
	private OnClickListener homeMenuButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		context.goToHomeMenu();
		
		}
	};
	/**
	 * 
	 * @param isVisible
	 */
	public void makeContinueButtonVisible(boolean isVisible) {
		if (isVisible) {
			continueButton.setVisibility(VISIBLE);
		} else {
			continueButton.setVisibility(INVISIBLE);
		}
	}

	/**
	 * 
	 */
	private OnClickListener newGameButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			
			
			
			Log.e("MainGameQuiz.hasChosenQuizMode =",Boolean.toString(MainGameQuiz.hasChosenQuizMode));
			if(Mix.isMixSelected){
				
				context.newMixGameStart();
				return;
			}
			
			if(MainGameQuiz.hasChosenQuizMode){
			context.newQuizGameStart();
			//Log.e("newGameButtonListener","newGameButtonListener");
			
			}
			else{
				context.newPandomimaGameStart();
			}
			try{
			if(VresPesActivity.isAddShowing){
				AdBuddiz.showAd(context); 
			}}catch(Exception e){}
		}
	};
	/**
	 * 
	 */
	private OnClickListener continueButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(Mix.isMixSelected){
				context.continueMixGame();
				return;
			}
			if(MainGameQuiz.hasChosenQuizMode){
			context.continueQuizGame();
		}else{
			
				context.continuePandomimaGame();
			}
			
			
		}
	};
	/**
	 * 
	 */
	private OnClickListener exitButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			 VresPesActivity.wl.release();
			System.exit(0);
		}
	};
	
	/**
	 * 
	 */
	private OnClickListener infoButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			context.showInfo();
		}
	};

	/**
 * 
 */
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// if (child== continueButton) {
			child.layout(0, i * getHeight() / 7, getWidth(), (i + 1)
					* getHeight() / 7);
			// }

		}

	}

}
