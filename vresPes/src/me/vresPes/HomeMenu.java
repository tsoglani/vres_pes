package me.vresPes;

import me.vresPes.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class HomeMenu extends ViewGroup{
private ChooseGameViewFromHomeMenu chooseGame;
private static Bitmap mBitmap;
public static int BtnTxtColor=Color.GREEN; 
	public HomeMenu(VresPesActivity context) {
		super(context);
		chooseGame= new ChooseGameViewFromHomeMenu(context);
		startingMenu();
		this.setWillNotDraw(false);
	}

	public void startingMenu(){
		addView(chooseGame);
		mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.questionmark2);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount=getChildCount();
		for(int i=0;i<childCount;i++){
			View child= this.getChildAt(i);
		//	if(child instanceof ChooseGameView){
				child.layout(0, 0, r, b);	
			
				
		//	}
			
		}
		
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint paint= new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawPaint(paint);
		if(mBitmap!=null){
		canvas.drawBitmap(mBitmap, 0,0, paint);
	}
		}
	
}
