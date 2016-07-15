package me.vresPes;

import me.vresPes.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ChooseGameViewFromHomeMenu extends ViewGroup  {
	private Button[] buttons = new Button[3];
	private VresPesActivity context;
	//private boolean isReadyToPlayQuizGame;
	public ChooseGameViewFromHomeMenu(VresPesActivity context) {
		super(context);
		this.context = context;
		init();
	}

	public void init() {
		int txtSize=28;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(context);
			addView(buttons[i]);
			buttons[i].setBackgroundColor(context.getResources().getColor(R.color.transparent_black_percent_30));
			buttons[i].setTextColor(context.getResources().getColor(R.color.GreenYellow));//teal
			
		}
		buttons[0].setTextSize(txtSize);
		buttons[0].setText("Παιχνίδι γνώσεων-quiz\n");	
		buttons[0].append("(2+ ατομα)");
		buttons[1].setTextSize(txtSize+4);
		buttons[1].setText("Παντομίμα\n");
		buttons[1].append("(4+ ατομα)");
		buttons[2].setTextSize(txtSize);
		buttons[2].setText("Συνδιασμός των άνω\n");
		buttons[2].append("(4+ ατομα)");
		buttons[0].setOnClickListener(quizListener);
		buttons[1].setOnClickListener(quizListener);
		buttons[2].setOnClickListener(quizListener);
	}

	private OnClickListener quizListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == buttons[0]) {
				//isReadyToPlayQuizGame = true;
				MainGameQuiz.hasChosenQuizMode=true;
				Mix.isMixSelected=false;
				context.goToMenu();
				
			}
			if (v == buttons[1]) {
				MainGameQuiz.hasChosenQuizMode=false;
				Mix.isMixSelected=false;
				context.goToMenu();
				
			}

			if (v == buttons[2]) {
				Mix.isMixSelected=true;
				MainGameQuiz.hasChosenQuizMode=false;
				Log.e("buttons[2]","Συνδιασμός των άνω");
				context.goToMenu();
				
			}
		}
	};

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = this.getChildAt(i);
			if (child instanceof Button) {
				int number=4;
				int distanceFirstOneFromTop=getHeight()/20;
				child.layout(0,(i+1)*distanceFirstOneFromTop+ (i) * getHeight() / number, r,(i+1)*distanceFirstOneFromTop+ (i ) * getHeight()
						/ number+getHeight()/number);
			}
		}

	}
	
	
	




}
