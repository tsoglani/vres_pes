package me.vresPes;


import java.util.ArrayList;
import java.util.List;

import me.vresPes.R;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class Properties  {
	private VresPesActivity context;
	  //private   RadioGroup radioGroup;
	 // private RadioButton radioButtonView;
	//  private  RadioButton radioButtonView2;
	  private Spinner spinner1, spinner2;
	  private Button btnSubmit;
	  private Button canselButton;
	  private CheckBox checkBoxSetRandomPointsPerAnswer, adsCheckbox;
	  private Database db;
	public Properties(VresPesActivity context) {
		this.context=context;
		db= new Database(context);
		init();
		try{
		ArrayList<Integer>array=db.getAllContacts();
		MainGameQuiz.totalRounds=array.get(0);
				MainGameQuiz.totalTime=array.get(1);
		}catch(Exception e){
			
		}	
		// TODO Auto-generated constructor stub
		addItemsOnSpinner2();
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();

	}

	//add items into spinner dynamically
	public void addItemsOnSpinner2() {
		
		List<String> list = new ArrayList<String>();
		list.add("5");
		list.add("7");
		list.add("10");
		list.add("13");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
		if(MainGameQuiz.totalRounds==5){spinner2.setSelection(0);}
		if(MainGameQuiz.totalRounds==7){spinner2.setSelection(1);}
		if(MainGameQuiz.totalRounds==10){spinner2.setSelection(2);}
		if(MainGameQuiz.totalRounds==13){spinner2.setSelection(3);}
	}
	private void init(){
		spinner1 = (Spinner) context.findViewById(R.id.spinner1);
		spinner2 = (Spinner) context.findViewById(R.id.spinner2);
		spinner1 = (Spinner) context.findViewById(R.id.spinner1);
		spinner2 = (Spinner) context.findViewById(R.id.spinner2);
		adsCheckbox=(CheckBox)context.findViewById(R.id.checkBox1);
		btnSubmit = (Button) context.findViewById(R.id.btnSubmit);
		canselButton=(Button)context.findViewById(R.id.button1);
		checkBoxSetRandomPointsPerAnswer= (CheckBox)context.findViewById(R.id.checkBoxRandomPoints);
		checkBoxSetRandomPointsPerAnswer.setChecked(MainGameQuiz.isUsingRandomScore);
	}

	public void addListenerOnSpinnerItemSelection(){
		
	
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	//get the selected dropdown list value
	public void addListenerOnButton() {

		
		if(MainGameQuiz.totalTime==30){spinner1.setSelection(0);}
		if(MainGameQuiz.totalTime==60){spinner1.setSelection(1);}
		if(MainGameQuiz.totalTime==90){spinner1.setSelection(2);}
		if(MainGameQuiz.totalTime==120){spinner1.setSelection(3);}
		if(MainGameQuiz.totalTime==150){spinner1.setSelection(4);}
		
	
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(context,
						"επελεξες : " + 
						"\n ο καθε γυρος εχει χρονο : " + String.valueOf(spinner1.getSelectedItem()) +" δεπτερόλεπτα "+
						"\n το συνολο των γυρων να ειναι : " + String.valueOf(spinner2.getSelectedItem())+
						"\n η επιλογη να εχεις τυχαιες βαθμολογιες ανα απαντηση ειναι : "+checkBoxSetRandomPointsPerAnswer.isChecked(),
						Toast.LENGTH_SHORT).show();
				
				MainGameQuiz.totalTime=Integer.parseInt((String)spinner1.getSelectedItem());
				MainGameQuiz.totalRounds=Integer.parseInt((String)spinner2.getSelectedItem());
				MainGameQuiz.isUsingRandomScore=checkBoxSetRandomPointsPerAnswer.isChecked();
                Database db= new Database(context);
                
                VresPesActivity.isAddShowing=adsCheckbox.isChecked();
                db.addContact(MainGameQuiz.totalRounds,MainGameQuiz.totalTime, adsCheckbox.isChecked());
				
				db.close();
				context.getMyMenu().makeContinueButtonVisible(false);
				context.backFromProperties();
			}

		});
		canselButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			context.backFromProperties();
				
			}

		});
	}




	
	
	 
	
	 
	 

	


}