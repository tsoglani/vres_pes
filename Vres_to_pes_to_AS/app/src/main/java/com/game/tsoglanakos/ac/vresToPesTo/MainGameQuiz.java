package com.game.tsoglanakos.ac.vresToPesTo;

import java.util.ArrayList;
import java.util.Random;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainGameQuiz extends ViewGroup {

	public boolean isPlayerOneTurn = true;
	private int curentGameScore = 0;
	
	private String currentQuestionWithAnswer;
	private final char splitGrade = '_';
	private Question question;
	private Answers answers;
	private Button nextButton;
	private Button menuButton;
	private Bitmap mBitmap;
	private ArrayList<String> listOfQuestionsWithAnswers;
	private ArrayList<Integer> playerOneGrades;
	private ArrayList<Integer> playerTwoGrades;
	private int playerOneTotalScore = 0;
	private int playerTwoTotalScore = 0;
	private final int END_GAME = 0;
	private final int SHOW_GRADES = 1;
	private final int READY_TO_START = 2;
	private int state = READY_TO_START;
	private ImageView imgView;
	private boolean gameIsOver = false;
	private int currentRoundCounter = 1;
	public static int totalRounds = 5;
	public static int totalTime = 60;
	public static boolean isUsingRandomScore;
	
  public static boolean hasChosenQuizMode =true;
	private Thread thread;
	private VresPesActivity context;

	/**
	 * 
	 * @param context
	 */
	public MainGameQuiz(VresPesActivity context) {
		super(context);
		this.setWillNotDraw(false);
		this.context = context;
		init(context);
		
		answers.setVisibility(INVISIBLE);
		createNextQuestion();
	

	}
	
	
	
	
	/**
	 * 
	 * @param context
	 */
	private void init(VresPesActivity context) {
		nextButton = new Button(context);
		menuButton = new Button(context);
		question = new Question(context);
		answers = new Answers(context);
		
		if(!Mix.isMixSelected){
			listOfQuestionsWithAnswers = new ArrayList<String>();
		fillList();
		Log.e("Main game Quiz size = ",Integer.toString(listOfQuestionsWithAnswers.size()));
		}else{
			listOfQuestionsWithAnswers=	context.getMixView().getQuizes();
		}
		imgView = new ImageView(context);
		Drawable myIcon = getResources().getDrawable(R.drawable.winner);
		imgView.setBackgroundDrawable(myIcon);
		// next.setBackgroundColor(Color.YELLOW);
		nextButton.setText("Συνέχεια");
		menuButton.setText("Μενού");
		menuButton.setTextSize(15);
		nextButton.setTextSize(15);
		nextButton.setOnClickListener(nextButtonListener);
		menuButton.setOnClickListener(menuButtonListener);
		playerOneGrades = new ArrayList<Integer>();
		playerTwoGrades = new ArrayList<Integer>();
		thread = new Thread(question);
		addView(answers);
		addView(nextButton);
		addView(menuButton);
		addView(question);
	}
	
	
	

	/**
	 * fill the arraylist with the questions and answers
	 */
	public void fillList() {
		listOfQuestionsWithAnswers.removeAll(listOfQuestionsWithAnswers);
		
	createNewListOfQuestionsWithAnswers("Ποιες οι 10 εντολες ?","Ου ψευδομαρτυρήσεις_1","Τίμα τον πατέρα και την μητέρα σου_1", "Ου φονεύσεις._1","Ου μοιχεύσεις_1", "Ου κλέψεις_1","Ουκ επιθυμήσεις την γυναίκα του πλησίον σου_3","Ου ποιήσεις σε αυτώ είδωλον_5","Εγώ ειμί ο Κύριος ο Θεός σου_3","Μην επιθυμεις οτι εχει ο πλησίον σου_3"," Η έβδομη ημέρα είναι για τον Κύριο τον Θεό σου_5");
	createNewListOfQuestionsWithAnswers("10 Μικρόσωμες ράτσες σκύλων ?","Maltese_1", "Yorkshire terrier_3", "Κανίς-Poodle_1", "Τσιουάουα_1","Μίνι πίντσερ_5", "Πεκινουά_1", " Παπιγιόν_3", " Πομεράνιαν_3"," Λάσα Άπσο-Lhasa Apso_3", " Κέρν τεριέ_3", " Παγκ_3"," Μπόστον Τεριέ_3"," Γουάϊρ Φοξ τεριέ_3");
	createNewListOfQuestionsWithAnswers("10 Γνωστα ζωα του σινεμα ?", "Λάσι_1", "Γουίλι, η φάλαινα_3","Κλάιντ, ο ουρακοτάγκος _3", "Μαίλο _3","Ρεξ_1", "μπετοβεν_3", "σκουμπι ντου_1", "Marcel (the monkey from Friends)_5"," Skippy the kangaroo_5", " Rin Tin Tin_3"," Mr Ed ( the horse)_5", " Flipper the dolphin_3");
	createNewListOfQuestionsWithAnswers("10 Γνωστα ζωα καρτουν ?","Ρατα πλαν_1","Ψιψινέλ_1","Σνούπι _1", "Bugs Bunny_1","Garfield_1", "Simba_1", "Rafiki_3","Timon-Pumbba_1", "Abu( αλαντιν )_5", "Winnie _3","  Baloo (Το βιβλίο της ζούγκλας ) _5", "Bambi (το ελαφακι) _5", "Dumbo_1","Ιndefix_1", "Μilu_1");
	createNewListOfQuestionsWithAnswers("10 Διάσημα κομιξ που έγιναν ταινίες ?", "Αστερίξ & Οβελίξ_1", "Ποπάι _5", "Άρτσι _5","X-men _3","Spider-Man_1","Teenage Mutant Ninja Turtles-Χελονονιντζακια _3", "Batman_1", "  Thor_3","Iron Man_1", "Captain America_3", "Punisher _5", "Superman_1","Hulk _3","Fantastic 4 _5","Daredevil _5","Blade _5");
	createNewListOfQuestionsWithAnswers("10 Αιλουροειδη ζωα ?","Τίγρης _1", "Τσιταχ - Γατόπαρδος_3", "Λεοπάρδαλη_1", "Πούμα_1", "Πάνθηρας_1", "Ιαγουάριος_3","Λιονταρι_1", "  Λύγκας _5", "Αγριόγατα _3","Γάτα_1");
	createNewListOfQuestionsWithAnswers("10 Επιτραπεζια παιχνιδια ?", "Ντάμα﻿_1", "Τρίλιζα﻿_1", "Γκρινιάρης_5", "Φιδάκι_3","Τάβλι_1", "Σκάκι_1", "Monopoly_1", "Scrabble_1","  Trivial Pursuit  _1", "Pictionary _5", "Taboo _1", "Uno _1", "Titsou _1", "Ναυμαχία _3", "Μάντεψε ποιός _5","Mastermind _5", "Kρεμάλα _3", "Πόκερ _1", "Settlers of Catan _5", "Jenga _3");
	createNewListOfQuestionsWithAnswers("10 Γλωσσες προγραμματισμου ?","Java_1", "C_1", "C++﻿_1", "Objective C_3", "Python_1","Ruby_3", "Javascript_1", "Php_1", "C#_3", "  SQL  _1","Prolog_5", " Haskel _5", "  Pascal _3","  Visual Basic _3", "  Fortran _3","  Perl _5", "  Lua _5", "Assembly_3");
	createNewListOfQuestionsWithAnswers("10 Antivirus ?","Kaspersky ﻿_1", "F-Secure﻿_5", " Ad-Aware_3","McAfee _3", "Norton _1","Panda _3"," AVG_1", "Avast  _3","Avira _3", "Microsoft Security _5", "ZoneAlarm _5");
	//createNewListOfQuestionsWithAnswers("10 πολιτειες των ΗΠΑ ?","Αϊόβα_3", "Αλαμπάμα﻿_3", "Αλάσκα_1", "Αριζόνα_3", "Βερμόντ_3","Βιρτζίνια_3", "Βόρεια Ντακότα_1", "Νοτια Ντακότα_1","Γιούτα_3", "  Ιλινόι _5", " Ιντιάνα _3"," Καλιφόρνια _1", " Κάνσας _1", " Κολοράντο _1", " Μιζούρι _5", " Μασαχουσέτη _5"," Μίσιγκαν _3", " Μισισίπι _1", " Νέα Υόρκη _1"," Νέο Μεξικό _1", " Νιου Τζέρσεϊ _1", " Οκλαχόμα _5"," Ουάσινγκτον _1", " Οχάιο _3", " Τέξας _1"," Φλόριντα _3");
    createNewListOfQuestionsWithAnswers("10 Gνωστοι μαθηματικοι-φυσικοι  ?", " Isaac Newton _1"," Carl Gauss _3", " Leonhard Euler _3",  "Αριστοτελης _1", "Galileo Galilei_3", " James Clerk Maxwell _3", " Albert Einstein _1", " Daniel Bernoulli _5"," Joseph Fourier _3"," Jacob Bernoulli _3"," Pythagoras _3"," Archimedes _1","Thomas Edison _3");
    createNewListOfQuestionsWithAnswers("10 Tαινιες του Brad Pitt ?", " Moneyball  _1", " The Tree of Life ﻿_3"," Benjamin Button _3", " συμμορια των 11-12-13 _1",  "Babel  _3", "Tροια _1", " Twelve Monkeys  _1", " Se7en  _1", " Συνεντευξη με ενα βρικολακα _3"," Fight Club  _1"," Snatch _3"," Mr. & Mrs. Smith  _1"," Spy Game _3"," Burn After Reading _5","The Mexican_3");
	createNewListOfQuestionsWithAnswers("10 Κωμικoi Ηθοποιοί(Comedian Actors)?","charlie chaplin_1", " Jim Carrey _1", " Robin Williams ﻿_1"," Steve Martin _3", " Adam Sandler _1",  "Ben Stiller _3", "Dustin Hoffman_1", " Dan Aykroyd _3", " Eddie Murphy _1", " Owen Wilson _3"," Chuckie chan _1"," Charlie Sheen _1"," James Belushi _3"," Rowan Atkinson _3"," Will Smith _1"," Jennifer Aniston _1"," Danny DeVito _3"," Bill Murray _3"," Robert Downey Jr. _3"," Bob Saget _3"," Rodney Dangerfield _5"," Richard Pryor _5");
	createNewListOfQuestionsWithAnswers("10 latin χοροι ?", " cha-cha-cha _3", " Rumba ﻿_3"," Salsa _1", "  Samba _1",  "Mambo _1", " Merengue _5", " Bomba _3"," Bolero _5"," Jive _5"," Gato _5"," Zamba _3"," Morenada _5"," Tango  _1","  Argentine tango  _3");
    createNewListOfQuestionsWithAnswers("10 Παραδοσιακοί ελληνικοι χωροί ?", " Mαλεβιζιωτης _1", " Πεντοζάλης _3", " Σιγανός _1",  "Σούστα _3", "Τσάμικος _3", " Συρτός _3", " Γεροντικος _5", " Γαιτανακι _5"," Ζαραμο _5"," Πηδιχτός _1"," Καλαματιανός _1", "Χασαποσέρβικο_3", "Συρτάκι_1", "Χασάπικο_1");
    createNewListOfQuestionsWithAnswers("Ομαδες με champions league ?", "  Ρεάλ Μαδρίτης(9) _1", " Μίλαν(7) ﻿_1","  Μπάγερν(5) _1", "  Λίβερπουλ(5) _1",  " Μπαρτσελόνα(4) _1", " Άγιαξ(4) _1", "  Ίντερ(3) _1", " Μάντσεστερ Γιουν.(3) _1", " Μπενφίκα(2) _3","  Γιουβέντους(2) _3"," Νότιγχαμ Φόρεστ _5"," Πόρτο _1"," Σέλτικ _3"," Αμβούργο _3"," Στεάουα _5"," Ολιμπίκ Μαρσέιγ _3","  Μπορουσια Ντόρτμουντ _3"," Τσέλσι _1"," Φέγενορντ _3"," Άστον Βίλα _5","  Αϊντχόφεν _3","  Ερυθρός Αστέρας _5");
    createNewListOfQuestionsWithAnswers("Φιναλιστ ch. league χωρις κουπα?", " Ρεμς _5", "  Βαλένθια ﻿_1"," Φιορεντίνα _3", " Άιντραχτ Φρανκφ. _3",  " Παρτιζάν _5", " Παναθηναϊκός _1", "  Ατλέτικο Μαδρίτης _1", " Λιντς _5", " Σεντ Ετιέν _5"," Γκλάντμπαχ _5","  Ρόμα _1"," Σαμπντόρια _5"," Μπ. Λεβερκούζεν _1"," Μονακό _3"," Άρσεναλ _1");
	createNewListOfQuestionsWithAnswers("Ζωα που ειναι υπο εξαφανηση ?", " Ρινόκερος   _3", " Κριτηκός αίγαρος-κρι κρι ﻿_3"," Aρκούδα _3", " Λεμούριος _5",  "Ιαγουάρος _3", "  Λύγκας _3", " Τσακάλι _1", " Χελωνα Καρέττα καρέττα _1", " Ελάφι _3"," Διάβολος της Ταζμανίας _5"," Γατόπαρδος-Τσιτάχ _3"," Λεοπάρδαλη _1"," Τίγρης _1"," Πάντα _1");
	createNewListOfQuestionsWithAnswers("10 Γνωστά φίδια ?", " Coral Snake _3", " Taipan ﻿_5"," King Cobra _3"," Cobra _1", " Black Mamba _3",  "Sea Snake _3", " Tiger Snake_3", " Κροταλίας _1"," Οχιά _1"," Ανακόντα-Βόας _1","Πύθωνας _1");
	createNewListOfQuestionsWithAnswers("10 Απο τα πιο επικίνδυνα ζωα ?", " Βούβαλος _3", "  Δηλητηριώδης βέλος(βάτραχος) ﻿_5"," Πολική Αρκούδα _3", " Ελέφαντας _3",  " Κροκόδειλος _1", "  Λιοντάρι_1", " Λευκός Καρχαρίας _1", " Τσούχτρα _5", " Κόμπρα _3"," Κουνούπι _3"," Ιπποπόταμος _3","Δράκος του Κομόντο_5");
	createNewListOfQuestionsWithAnswers("10 Ομάδες με Γιουρόπα Λιγκ ?", " Γιουβέντους(3) _1", "  Ίντερ(3) ﻿_1"," Λίβερπουλ(3) _1", " Γκλάντμπαχ(2) _3",  "Τότεναμ(2) _1", " Φέγενορντ(2) _3", " Ρεάλ Μαδρίτης(2) _1", " Πάρμα(2) _3"," Πόρτο(2) _1"," Σεβίλλη(2) _1"," Ατλέτικο Μαδρίτης(2) _1"," Άντερλεχτ(1) _3"," Αϊντχόφεν(1) _1"," Σαχτάρ(1) _3"," ΤΣΣΚΑ Μόσχας(1) _3","  Βαλένθια(1) _1","  Λεβερκούζεν(1) _3","  Νάπολι(1) _3"," Άγιαξ(1) _1"," Μπάγερν(1) _1");
    createNewListOfQuestionsWithAnswers("10 Ταινιες του jonny depp ?", " Ο Ψαλιδοχέρης _3", "Εφιάλτης στο Δρόμο με τις Λεύκες ﻿_5","Once Upon a Time in Mexico _5", " Ψάχνοντας τη Χώρα του Ποτέ _5",  "Ο Τσάρλι και το Εργοστάσιο Σοκολάτας _1","Pirates of the Caribbean:The Legend of Jack Sparrow_3", "Οι Πειρατές της Καραϊβικής:Το Σεντούκι του Νεκρού_3", "Οι Πειρατές της Καραϊβικής:Στο Τέλος του Κόσμου _3", "Sweeney Todd:Ο Φονικός Κουρέας της Οδού Φλιτ _5","The Tourist _1","Οι Πειρατές της Καραϊβικής:Σε άγνωστα νερά_1"," Dark Shadows _1"," Rango _3","Η Αλίκη στη Χώρα των Θαυμάτων _3"," Secret Window _1","Ο Μύθος του Ακέφαλου Καβαλάρη _3"," Ed Wood _5");
    createNewListOfQuestionsWithAnswers("Χώρες με μεγαλa αποθέματα χρυσού?", " Ηνωμένες Πολιτείες(8.134 τόννοι) _1", " Γερμανία(3.401 τόννοι) ﻿_1"," Ιταλία (2.452 τόννοι) _3", " Γαλλία (2.435 τόννοι) _1",  "Κίνα (1.054 τόννοι) _1", " Ελβετία (1.040 τόννοι) _3", " Ρωσία (792 τόννοι) _1", "  Ιαπωνία (756 τόννοι) _5", " Ολλανδία (613 τόννοι) _3","  Ινδία (558 τόννοι) _5","  Ταιβάν( 423.6) _5","Πορτογαλία (382.5 tonnes)_3"," Τουρκία (375.7)_3"," Βελεζουέλα(365.8) _5"," Σαουδική Αραβια(322.9) _3"," United Kingdom(310.3) _1"," Λίβανο(286.8) _5"," Ισπανία (281,6)_3"," Αυστρία(281.6) _3","Βέλγιο(281.6)_3");
    createNewListOfQuestionsWithAnswers("10 Ηθοποιοι με πανω απο 1 oscar ?", " Marlon Brando _3", " Gary Cooper ﻿_5"," Bette Davis _5", "  Jane Fonda _3", " Jodie Foster _3", " Tom Hanks _1", " Dustin Hoffman _3"," Glenda Jackson _5"," Sean Penn _3"," Hilary Swank _3","  Elizabeth Taylor _3"," Spencer Tracy _5");
    createNewListOfQuestionsWithAnswers("Ταινίες με πανω απο 7 oscar ?", " Titanic (11) _1", " The Lord of the Rings: The Return of the King(11)﻿_3"," Ben Hur(11) _3", " West Side Story(10) _5",  "The English Patient (9) _3", " Gigi(9) _5", " The Last Emperor (9) _3", " Gone With The Wind (8) _5", " From Here to Eternity(8) _3"," On the Waterfront (8) _5"," My Fair Lady (8) _3"," Gandhi  _3"," Amadeus (8) _5"," Cabaret (8) _3"," Slumdog Millionaire(8) _1");
	createNewListOfQuestionsWithAnswers("Μουσικοί με πανω απο 4 Grammy ?", " Beyonce(5) _3", " Stevie Wonder(22) ﻿_1"," U2(22) _1", " Michael Jackson(8) _1",  "Alison Krauss(26) _5", " Adele(6) _3", " Bruce Springsteen(20) _3", " Ray Charles(17) _5", " The Dixie Chicks(13) _3"," Quincy Jones(27) _3"," Vince Gill(20) _3"," Aretha Franklin(20) _1"," Carlos Santana(8) _3");
    createNewListOfQuestionsWithAnswers("Ελληνικα φιλανθρωπικά ιδρύματα ?", " Αγκαλια _3", " Γιατροι του Κοσμου ﻿_5"," Γραμμη Ζωης _5", " Ελπίδα _1",  "Κανε Μια Ευχή _1", " Θεοφιλος _3", " ΜΗΤΕΡΑ _5", " Κιβωτος του κοσμου _3"," Το χαμογελο του παιδιου _1"," Μαζί για το παιδί _3"," Χατζηκυριακειο Ιδρυμα Παιδικης Προστασιας _5"," Φλογα _3", " Ινστιτουτο Ειδικης Αγωγης _5");
    createNewListOfQuestionsWithAnswers("10 Λόγοι που οι άνθρωποι κάνουν σεξ ?", " Για βελτίωση της διάθεσης  _3", " Από υποχρέωση ﻿_5"," Ενίσχυση της εξουσίας _3", " Για αύξηση αυτοεκτίμησης _5",  "Απο αγάπης _1", " Απο ζήλια _3", " Για να κερδίσει χρήματα _5", " Για βελτίωση της φήμης _5", " Για αποκτηση μωρου  _3"," Λόγω πίεσης από τον σύντροφο _5"," Για ευχαρίστηση _1"," Για τη μείωση της σεξουαλικής ορμής _5"," Από εκδίκηση _1"," Λόγω σεξουαλικής περιέργειας _1"," Για την εκδήλωση αγάπης _3"," Λόγο μέθης _5");
	createNewListOfQuestionsWithAnswers("Aξιοθέατα του κόσμου ?", "  Times Square(Νέα Υόρκη)_1", "  Central Park(Νέα Υόρκη) ﻿_1", " Καταρράκτες Νιαγάρα(Νέα Υόρκη και Οντάριο) _3", "  Disney World(Ορλάντο) _3", " Μεγάλο Παζάρι(Κωνσταντινούπολη) _5", " Απαγορευμένη πόλη(Πεκίνο) _5"," Σινικό τείχος(Κίνα) _3", " Machu Picchu (Περού) _3"," Αγιά Σοφιά (Κωνσταντινούπολη) _3"," Μετέωρα (Ελλάδα ) _5"," Big Ben (Λονδίνο ) _1", " Άγαλμα της Ελευθερίας(Νέα Υόρκη) _1"," Πύργος του Άιφελ (Παρίσι,Γαλλία) _1"," Ακρόπολη (Ελλάδα ) _1"," Παρθενώνας (Ελλάδα ) _1");
	createNewListOfQuestionsWithAnswers("10 Λέξεις για το dragonball ?", "χελονοτζίνι _3", " ανδροϊδή ﻿_5"," chi chi _3", " vegeta _1",  "vegitto _1", " trans _3", " sogokou _1", " sogohan-gohan _3", " grilin _1"," sogotten _5"," cell _1"," Bulma _5"," Frieza _3"," piccolo _1"," Master Roshi _3"," Mr. Satan  _5"," ο στρατός της κοκκ. κορδέλας _5"," Yamcha _5"," Tien Shinhan _5"," μαγικό ραβδί _3"," μαγικά φασόλια _3"," Chiaotzu _5"," Korin _5"," καμε χαμε κυμα _1"," fusion _3","μαγικό συνεφάκι_3");
	createNewListOfQuestionsWithAnswers("10 Πόκεμον ?", " Mew _3", " Mew two ﻿_3"," Miaouth _1", " Electabuzz _3",  "Venusaur _3", " Onix _3", " Snorlax _3", " Squirtle _1", " Alakazam _3"," Scizor _5"," Bulbasaur _1", " Raichu _3"," Kadabra _3", " Jigglypuff _3"," Mr. Mime _5"," Eevee _5"," Charizard  _1"," Pikachu  _1"," shyduck _3" ," charmander _3" ," charmilion _3");
	createNewListOfQuestionsWithAnswers("10 Σχολικά μαθήματα?", " Ιστορια _1", " Γεωγραφία ﻿_1"," Φυσική αγωγή _1", " Θρησκευτικά _1",  "Μουσική _3", " Άλγεβρα  _1", " Γεωμετρία _1", " Βιολογία _1"," Α΄ Ξένη Γλώσσα _3"," Τεχνολογία _1", " Αρχαία Ελληνικά _3", " Νεοελληνική Γλώσσα _1", " Νεοελληνική Λογοτεχνία _3"," ΣΕΠ _3"," Αρχές Οικονομίας _5"," Φυσική _1"," Προγραμματισμός _3"," Αρχές Οργάνωσης και Διοίκησης Επιχειρήσεων _5");
	createNewListOfQuestionsWithAnswers("Τα 10 πιο γρήγορα ζώα της ξηράς  ?", " Αλεπού  _1", " Κογιότ ﻿_3"," Κυνηγόσκυλο _3", "Ελάφι  _3",  "Άλογο _1", " Λιοντάρι _1", " Wildebeest _5", " Αντιλόπη _3", " Γατόπαρδος _3"," Γαζέλα _3");
	createNewListOfQuestionsWithAnswers("10 εταιρείες παραγωγής παιχνιδιών ?", " Activision Blizzard _3", " Sony ﻿_1"," Microsoft _1", " Electronic Arts (EA) _3", " Nintendo _1", " Ubisoft _3", " Konami _1"," Zynga _5"," Disney _3"," Facebook _3"," Capcom _5"," Sega _3"," Midway _5"," Namco _3"," Atari _3","Rockstar Games  _5","Bioware_5");
    createNewListOfQuestionsWithAnswers("10 θηλαστικά φυτοφάγα   ?", " Αγριόχοιρος _5", " Ιπποπόταμος ﻿_3"," Καμήλα _3", " Ελάφι _1", " Τάρανδος _1", " Ζαρκάδι _3", " Καμηλοπάρδαλη _3", " Κατσίκα _1"," Κριός _1"," Βόδι _1"," Βίσωνας _5"," Άλογο _1"," Ζέμπρα _1"," Ρινόκερος _3"," Κανγκουρό _5"," Βαμβουίνος _5"," Χιμπαντζής _1"," Μαιμού _1"," Σκίουρος _1"," Κάστορας _5");
	createNewListOfQuestionsWithAnswers("10 θηλαστικά σαρκοφάγα  ?", " Λύκος _1", " Σκύλος ﻿_1"," Λιοντάρι _1", " Νυφίτσα _3",  "Γάτα _1", " Λύγκας _3", " Τίγρης _1", " Τσακάλι _3", " Πάνθηρας _1"," Ιαγουάριος _1"," Μυρμιγκοφάγος _5"," Ορνιθόρρυγχος _5"," Ύαινα _1");
	createNewListOfQuestionsWithAnswers("10 θηλαστικά παμφάγα  ?", " Γουρούνι _1"," Ασβός _5", " Κουνάβι _3",  "Αρκούδα _1", " Αλεπού _3", " Ουρακοτάνγκος _3", " Γορίλας _1", " Ανθρωπος _1"," Αρουραίος _3"," Ινδικό χοιρίδιο _3"," Κοάλα _3");
	createNewListOfQuestionsWithAnswers("10 Γνωστά πτηνά ?", " Αετός _1", " Φασιανός ﻿_3"," Γεράκι _1", " Πάπια _1",  " Χήνα _3", " Κότα _1", " Πινγκουίνος _5", " Στρουθοκάμηλος _5", " Γλάρος _3"," Κούκος _3"," Κουκουβάγια _1"," Παπαγάλος _1"," Δρυοκολάπτης _5"," Κύκνος _1"," Γύπας _1"," Κόνδορας _3"," Πέρδικα _1"," Ορτύκι _3"," Παγώνι _3"," Μπεκάτσα _1"," Περιστέρι _1");
	createNewListOfQuestionsWithAnswers("10 Αρθρόποδα ζώα?", " Καραβίδα _3", " Γαρίδα ﻿_1"," Ακρίδα _1", " Αράχνη _1",  "Σαρανταποδαρούσα _1", " Σκορπιός _1", " Ψείρα _3", " Τσιμπούρι _3", " Καβούρι _3"," Πεταλούδα _1"," Μύγα _1"," Μυρμήγκι _1"," Κουνούπι _3"," Πασχαλίτσα _3"," Κατσαρίδα _1"," Σκαραβαίος _5"," Μελισσα _1"," Πυγολαμπίδα _5"," Σφήκα _1"," Τερμίτης _3"," Τζιτζίκι _3"," Κοριός _5");
    createNewListOfQuestionsWithAnswers("10 Αντικείμενα σε ενα μπάνιο ?", " Οδοντόκρεμα ﻿_1"," Οδοντόβουρτσα _3", " Ξυραφάκια _1",  "Χαλακι μπανιου _3", " Ντουζιέρα _1", " Σάουνα _1", " Πετσέτα _1", " Υδρομασάζ _3", " Καλαθι άπλητων _3"," Ρούχα _3"," Μπιντές _1"," Νιπτήρας _1"," βουρτσάκι λεκάνης _3"," Λεκάνη _1"," Κολωνια _3"," Χαρτί υγείας _1","Περιοδίκα-Εφημεδίδες _5");
	createNewListOfQuestionsWithAnswers("10 Σοκολάτες ?", " Ίον _1", " Smarties ﻿_3", "Kitkat _3",  " Toblerone _3", " M&M' _5", " Bounty _5", " 3Bit _3", "  Snickers _3"," Ferrero Rocher _1"," Σοκοφρέτα _ε"," Disney Family  _5"," Lacta  _1"," Milka _3"," Mirabell _3"," Gioconda _1"," Swiss _3"," Κiss _5"," Buenno _1");
	createNewListOfQuestionsWithAnswers("10 Eταιρείες παραγωγής γάλακτος ?", " Mεβγάλ _1", " Νεστλέ ﻿_3"," Όλυμπος _3", " Φάγε _1",  "Νουνού _1", " Δωδώνη _5", " Δέλτα _1", " Έβγα _1"," Ήπειρος _5"," Βίοτρος _5"," Βλάχας _3"," Κρι Κρι _5");
    createNewListOfQuestionsWithAnswers("10 Ταινίες (καρτούν) της disney  ?", " Τα 101 Σκυλιά της Δαλματίας _1", " Frankenweenie ﻿_5"," Toy Story 1-2-3 _1", " lion king- ο βασιλιάς των λιονταριών _1",  "Αλαντίν _1", " Μπαμπούλας Α.Ε._3", " Ψηλά στον Ουρανό _3", " Ρομπέν των Δασών _3"," Ο Ρατατούης _5"," Η Παναγία των Παρισίων _3", " ΓΟΥΟΛ·Υ _3"," Μπαμπούλες Πανεπιστημίου _3"," Η Πεντάμορφη και το τέρας _1"," Πινόκιο _1"," Ποκαχόντας _1"," Ηρακλής _3"," Μουλάν _3"," Ζουζούνια _3"," Η Μικρή Γοργόνα _1"," Το βιβλίο της ζούγκλας _5"," Η λαίδη και ο αλήτης _1", " Πήτερ Πάν _3",  "Dumbo (το ελεφαντάκι) _1","Η ωραία κοιμωμένη _3",  "Ψάχνωντας τον Νέμο _1",  "Η σταχτοπουτα _3",  "η χιονάτη και οι 7 νάνοι  _1",  "Οι αριστόγατες _5");
    createNewListOfQuestionsWithAnswers("Οι 10 χώρες που διασχίζει ο δούναβης?", " Γερμανία _1", " Αυστρία ﻿_3"," Σλοβακία _3", " Ουγγαρία _3",  "Κροατία _3", " Σερβία _1", " Βουλγαρία _1", " Ρουμανία _1", " Ουκρανία _1"," Μολδαβία _3"," Σλαβική Δημοκρατία _5"," Γιουγκοσλαβία _1");
    createNewListOfQuestionsWithAnswers("10 Κατασκευαστές κινητών τηλεφώνων ?","hawaii_5", " BlackBerry _3", " Lg ﻿_1"," Motorola _3", " Nokia _1",  "Samsung _1", " Sharp _5", " Sony Ericsson _1", " Htc _1", " Apple _1"," Hp _3"," Asus _3"," Toshiba _3");
    createNewListOfQuestionsWithAnswers("10 Site πορνογραφικού υλικού ?", " Xvideos.com _1", " 4Tube.com ﻿_3"," XNXX.com _3", " DrTuber.com _5",  "Eskimotube.com _3", " Fapdu.com _5", " KeezMovies.com _5", " Redtube.com _1", " Youporn.com _1"," Youjizz.com _1"," Naughty.com _3"," Tube8.com _3"," Porn.com _3"," Brazzers.com _1"," Videosz.com _5");
    createNewListOfQuestionsWithAnswers("10 Κονσόλες ( παιχνιδομηχανές ) ?", " Nintendo Wii _3", " Game boy ﻿_1"," Nintento 64 _3", " Play station 1-2-3 _1",  "Atari _3", " Sega mega drive _3", " Super nintendo _3", " PSP _3", " Nintendo 3DS _3","  XBOX 360 _1"," Nintendo Gamecube _3"," Sega Dreamcast _1"," Sega Genesis  _5"," Nintendo DS _1");
    createNewListOfQuestionsWithAnswers("10 Γεύσεις παγωτού ?", " Σοκολάτα _1", " Φράουλα ﻿_1"," Κρέμα-βανίλια _1", " Καρπούζι _1", " Λεμόνι _1", " Τσίχλα _3", " Γιαούρτι _3"," Σύκο _3"," Καφές _5"," Πεπόνι _1"," Πορτοκάλι _1"," Μόκα _5"," Ρούμι _5"," Μπανάνα _1"," Κανταΐφι _5"," Φυστίκι _3"," Βατόμουρο _3"," Ανανάς _3"," Καρύδα _1"," Μάνγκο _5");
    createNewListOfQuestionsWithAnswers("10 Ελληνικά τηλεοπτικά κανάλια ?", " Alpha _1", " ANT1  ﻿_1"," Extra 3_3", " Star Chanel _1",  "Mega Chanel _1", " Κανάλι 9 _3", " Κρητη TV _3"," Μακεδονία TV _3"," 902 TV _5"," Αρτ Channel _5"," Βουλή _3"," ΕΤ3 _1"," ΝΕΤ _1"," ΕΤ1 _1"," Mad TV _1"," Τηλεκρήτη _5");
    createNewListOfQuestionsWithAnswers("10 Ήρωες του 1821 ?", "Γεώργιος Καραϊσκάκης _3", " Γιάννης Μακρυγιάννης ﻿_5"," Αθανάσιος Διάκος _1", " Ρήγας Βελεστινλής-Φεραίος _3",  "Γρηγόριος Δικαίος - Παπαφλέσσας _3", " Κωνσταντίνος Κανάρης _3", " Λασκαρίνα Μπουμπουλίνα _5", " Νικήτας Σταματελόπουλος - Νικηταράς ο Τουρκοφάγος _3", " Κίτσος Τζαβέλας _5"," Ανδρέας Μιαούλης _1"," Μάρκος Μπότσαρης  _3"," Οδυσσέας Ανδρούτσος _3"," Νικόλαος Στουρνάρας _5"," Δημήτριος-Αλέξανδρος Υψηλάντης _3");
    createNewListOfQuestionsWithAnswers("10 Τρόποι αντισύληψης ?", " Διακεκομμένη συνουσία _3", " Προγραμματισμένη συνουσία ﻿_5"," Θερμομετρική μέθοδος _5", " Σπιράλ _3",  "Προφυλακτικό _1", " Χάπι της επόμενης _3", " Στείρωση της γυναίκας _3", " Στείρωση του άνδρα - βαζεκτομή _1", " Γυναικείο προφυλακτικό _3","Διάφραγμα  _1"," Κολπικοί σπόγγοι _3"," Σπερματοκτόνο  _1"," Αποχή απο το sex _3"," Φυσική μέθοδος _3");
    createNewListOfQuestionsWithAnswers("10 Διάσημοι Ευρωπαίοι συνθέτες κλασικής μουσικής ?", " Τζάκομο Πουτσίνι _1", " Τζουζέπε Βέρντι ﻿_3"," Ίγκορ Στραβίνσκι _5", " Τσαϊκόφσκι _3",  "Νικολό Παγκανίνι _5", " Ρίχαρντ Βάγκνερ  _5", " Ρόμπερτ Σούμαν _5", " Σούμπερτ _3"," Ντομένικο-Αλεσσάντρο Σκαρλάττι _5"," Αντόνιο Βιβάλντι _3"," Λούντβιχ βαν Μπετόβεν _1","Μότσαρτ _1","Μπαχ _1","Τζορτζ Γκέρσουιν _3");
    createNewListOfQuestionsWithAnswers("10 Τενόροι-σοπράνος ?", " Maria Callas _3", " Luciano Pavarotti ﻿_1"," Beniamino Gigli _3", " Franco Corelli _3",  "Plácido Domingo  _3", " Enrico Caruso _3", " Peter Pears _3", " Peter Schreier _5"," Andrea Bocelli _3","Joan Sutherland _5"," Leontyne Price _5"," Elisabeth Schwarzkopf _5"," Elisabeth Schumann _5"," Nicolai Gedda _5");
    createNewListOfQuestionsWithAnswers("10 Τρόποι κ μέσα (apps) επικοινωνίας ?", " Κουβέρτα και καπνό ( Ινδιάνοι ) _5", " Σήματα μορς ﻿_3"," Σταθερό τηλεφώνο _1"," Κινητό τηλεφώνο _1", " Facebook _1",  "Mail _1", " Νοηματική _3", " Δυο ποτήρια συνδεδεμένα με ενα σχοινι _5", " Sms _1", " Mms _3"," Skype _1"," Twitter _1"," Τηλεπάθεια _3"," Θυροτηλέφωνο _3"," Με γράμματα _3","Με συνομιλία _1"," Αφιερώσεις στο ραδιόφωνο _3","Ασύρματος - Wocky tocky _3");
    createNewListOfQuestionsWithAnswers("10 Μάρκες τηλεοράσεων ?", " Sony _1", " Samsung ﻿_1"," Philips _1", " LG _1",  "JVC _3", " Funai _5", " Toshiba _3", " Panasonic _3"," Pioneer _3"," Hitachi _3","  Sharp _5"," Schaub-lorenz _5"," Telefunken  _5"," F&U _5"," Τurbo-Χ. _5");
    createNewListOfQuestionsWithAnswers("10 Γεύσεις καφέ φίλτρου ?", " Φουντούκι _1"," Καραμέλα _1",  "Σοκολάτα _1", " Βανίλια _1", " Πορτοκάλι _3", " Κρέμα Φουντούκι _3", " Κανέλα _5"," Καρύδα  _3"," Κεράσι _3"," Βατόμουρο _3"," Μπανάνα _3"," Μπισκότο _5"," Amaretto _5");
    createNewListOfQuestionsWithAnswers("10 Εταιρείες καλλυντικών ?", " L’Oréal _1", " MAC ﻿_1"," Mary Kay _3", " Estee Lauder _3",  "Skinfood _3", " Artistry _3", " Procter & Gamble  _3", " Avon  _3"," Beiersdorf  _1"," Johnson & Johnson  _3"," Henkel  _3"," Coty _3"," Colgate-Palmolive  _1"," Yves Rocher  _5");
    createNewListOfQuestionsWithAnswers("10 Action games ?", " Crysis _5", " Call of Duty ﻿_1"," Counter-Strike _1", " Halo _1",  "Far Cry _3", " Half-Life _3", " Bioshock _5", " Grand Theft Auto-GTA _1"," Resident Evil _3"," Batman: Arkham City _5"," Tomb Raider _3","Splinter Cell _3"," Devil May Cry _5"," Castlevania _5"," Assassin's creed _3"," Prince of persia _5"," Left 4 dead _5");
    createNewListOfQuestionsWithAnswers("10 Strategy games ?", " Civilization ﻿_3"," The Sims _1", " Plants vs. Zombies _5", " Starcraft _3", " Age of empires _3"," Rome _5"," Black & White _1"," World of Goo _5"," Worms _3"," Dota _1"," Warcraft _1"," Lineage _1");
    createNewListOfQuestionsWithAnswers("10 Rpg games ?", " Final Fantasy _1", " Mass Effect  ﻿_5"," Diablo _1", " Fallout _3", " Pokemon _3"," Kingdom Hearts _5"," Dragon Quest _5","WOW -world of workraft _3"," The Legend Of Zelda _3"," Monster Hunter _5"," Paper Mario _3");
    createNewListOfQuestionsWithAnswers("10 Χρώματα ?", " Ερυθρό-λευκό _1", " Πορτοκαλί ﻿_1"," Κίτρινο _1", " Πράσινο _1",  "Γαλάζιο _1", " Μοβ _1", " Μαύρο _1", " Mangeta _5", " Γκρι _1"," Μπλε _1"," Καφέ _1"," Κόκκινο _1"," Ροζ _1","Χρυσό _3","Ασημένιο _3","Διάφανο-Transparent _3");
    createNewListOfQuestionsWithAnswers("10 Κατασκευαστές πλυντηρίων ?", " GE _5", " Whirlpool ﻿_3"," Maytag _3", " Kenmore _3",  " LG _1", " Frigidaire _3", " Samsung _1", " General Electric _3"," Speed Queen _5"," Miele _1"," Haier _5");
    createNewListOfQuestionsWithAnswers("10 Έντομα ?", " Πεταλούδα _1", " Τζιτζίκι ﻿_3", " Κατσαρίδα _1",  " Τερμίτης _3", " Ακρίδα _5", " Ψείρα _3"," Μερμήγκι _3"," Μύγα _1"," Ψύλλος _1"," Σφήκα _3"," Κουνούπι _1"," Πασχαλίτσα _3","Σκαθάρι _3"," Κλανοπαπαδιά _5"," Ψαράκι _5");
    createNewListOfQuestionsWithAnswers("10 Ατάκες που μας έλεγαν οταν ειμασταν μικροι για να φάμε ?", " Φάε γιατί θα σε πάρει ο γύφτος _3", " Φάε για να μεγαλώσει το πουλάκι σου ﻿_5", " Φάε γιατί θα σε φάει ο λύκος _3",  "στις άλλες χώρες τα παιδάκια πεινάνε, και εσύ παίζεις με το φαί σου _3", " Αν δεν το φας δεν θα δεις παιδικά-δεν θα παίξεις!! _3", "ελα, τη δύναμη σου _3", " φάε, και εγώ θα σου πάρω, αυτό που θες _5", " έλα, φάει μια μπουκιά για μένα _3"," φάε το φαΐ σου γιατί θα έρθει ο μπαμπούλας.. _3"," φάε όλο σου το φαΐ να μεγαλώσεις _3","Αν δεν φας όλο το φαγητό σου δεν έχει γλυκο - παγωτο_3","Φάε, γιατί θα έρθει ο Μούμος_5");
    createNewListOfQuestionsWithAnswers("Χαρακτήρες απο το είσαι το ταίρι μου ?", " Μίτσος _1", " Λάζαρος ﻿_1"," Νίκος _1", " Βίκυ Σεϊτανίδη _3",  "Γρηγόρης _1", " Στέλλα Παπαλιμνέου _3", " Βέρα _1", " Λυκούργος _3"," Παναγιότης Ζήγουρας _5"," Αννετα _3"," Μιλτιάδης  _3"," Τούλα _3"," Παπούς _1"," Αιμιλία _3"," Χρυσούλα _3"," Αλέξης _3");
    createNewListOfQuestionsWithAnswers("Χαρακτήρες απο τα εγκλήματα?", " Κορίνα Μητροπούλου _3", " Αλέκος Παπαδήμας ﻿_3"," Φλόρα _1", " Johnny _3",  "Μάχη _3", " Αριστείδης Μητρόπουλος _3", " Αχιλλέας Μητρόπουλος _3", " Πέπη _3", " Βαλάντης _3", " Βίκη _3"," Δρ. Παύλος _5"," Σοσσο Παπαδήμα _3"," Μιχαλάκης Ρούμπακας _5");	
    createNewListOfQuestionsWithAnswers("Χαρακτήρες απο το ευτυχισμένοι μαζί ?", " Διονύσης Μαυροτσούκαλος _1", " Ελένη ﻿_1"," Σπύρος Μαυροτσούκαλος _3", " Βίκη _1",  "Μάκης Κοτσάμπασης _3", " Μάρκος Μαυροτσούκαλος _1", " Εύα _1", " Γιαννάκης Μαυροτσούκαλος _3", " Φίφη _3"," Φώτης _1"," Θανάσης Μαυροτσούκαλος _3"," Μίτσος _1"," Ψυχολόγος Παύλος _5"," Geena _5"," Ρούλης _3"," Αλί _3");
    createNewListOfQuestionsWithAnswers("Χαρακτήρες της ελλάδος τα παιδιά?", " Σμήναρχος Κάκαλος _1", " Πλαπούτας ﻿_1"," Μπουμπού _3", " Καραβανέας _1",  "Βλάχος _1", " Τζιοβάνι Ντάλας _3", " Βούλα _3", " Κώστας Γκουσγκούνης _5", " Τέλης Τιπιτέλης _3"," Λίτσα _3"," Τζέλα η ΚΨΜιτζού _5"," Ταξίαρχος Τζαζλέας _5"," Σμηναγός Καραμπονάτσος _5"," Μπιτζιμπιτζίδης _5"," Γκιζέλα _5"," Επιθεωρητής Ηρακλής Πουαρές _5","Σερ Τζέφρυ Ντάγκλας_5","Σμήναρχος Γκέγκελος_5","Παρλαπίρλας_3");
    createNewListOfQuestionsWithAnswers("Χαρακτήρες Κωνσταντίνου και Ελένης ?", " Ελένη Βλαχάκη _1", " Κωνσταντίνος Κατακουζηνός ﻿_1"," Μάνθος Φουστάνος _1", " Πέγκυ Καρρά  _1",  "Ματίνα Μανταρινάκη _3", " Νικόλας _1", " Νίκος Γρέβιας (ο δικηγόρος του Κωνσταντίνου) _5", " Έλλη Ρούσου (η δικηγόρος της Ελένης) _5", " Φιόνα Μακρή _3"," Λίλα _3"," Τζοάννα _3"," Αναγνώστου (μαθητής του Κωνσταντίνου) _3","  Μπουκουβάλα (μαθήτρια του Κωνσταντίνου) _3"," Ιωάννου (μαθητής του Κωνσταντίνου) _3"," Νίτσα Βούπουρα _5"," Μανώλης _3"," Νίνο Πούτσο _5"," Τζώνυ _3"," Λάκης Φορτουνάκης _5"," Τζον Χάρις  _5"," Κλεομένης-Μένιος  _3"," Μίλτος Κηπουρός - Παπί _3"," Αστυνόμος Μπάμπης Μπάμπουρας _5");
    createNewListOfQuestionsWithAnswers("10 Τροπικά νησιά ?", " Seychelles _1", " Fiji ﻿_1",  "Maldives _1", " Cook island _5", " Bora Bora _3"," Bahamas _1"," Hawaii _3"," Bali _3"," Java-Indonesia _5"," Sumatra _3"," Taiwan _3"," Madagascar _3"," Crete _1"," Barbados _3"," Abu Dhabi _5");
    createNewListOfQuestionsWithAnswers("10 Μαθηματικοί-φυσικοί όροι  ?", " Διάνυσμα _3", " Πηλίκο ﻿_3"," Δύναμη _3", " ( εις το) Τετράγωνο _1",  "Τετραγωνική ρίζα _3", " Διαίρεση _1", " Επί - Πολλαπλασιασμός _1", " Μείων _1"," Σύν - Πρόσθεση_1", " Ολοκλήρωμα _3"," Παράγωγος _3"," Παραγοντικό _5","Κλάσμα_1","Ίσον_1","Διάφορο _3");
    createNewListOfQuestionsWithAnswers("10 Σχήματα ?", " Κύκλος _1", " Τετράγωνο ﻿_1"," Τρίγωνο _1", " Παραλληλόγραμμο _1",  "Πεντάγωνο _1", " Εξάγωνο _3", " Οβάλ _5", " Κύβος _3", " Πυραμίδα _3"," Σφαίρα _3"," Κώνος _1","Παραβολή_5","Υπερβολή_3");
    createNewListOfQuestionsWithAnswers("10 Χερσόνυσοι ?", " Ιταλική χερσόνησος _3", " Ιβηρική Χερσόνησος ﻿_1"," Σκανδιναβική Χερσόνησος _1", " Βαλκανική Χερσόνησος _3",    " Ινδική Χερσόνησος _3", " Κορεατική Χερσόνησος _3"," Χερσόνησος της Καμτσάτκας (Ρωσία) _5","  Σομαλική Χερσόνησος _5"," Χερσόνησος της Φλόριντα _3"," Χερσόνησος της Μικράς Ασίας _3"," Χερσόνησος του Αγίου Όρους _5");
    createNewListOfQuestionsWithAnswers("10 Ασκήσεις γυμναστικής ?", "Πεκ ντεκ _3"," Πιέσεις πάγκου (στήθος) ﻿_3", " Squat _5",  "Κάμψεις _1", " Κωπηλατική _1", " Προτάσεις-Εκτάσεις ώμων _3"," Πιέσεις τροχαλία (τρικέφαλα) _1","Kοιλιακούς_1","Ραχιαίους_1","Pull-Cross over_3","Βυθισεις_3","Μονόζυγο_3","Γαλλικές πιέσεις _3","Αερόβια _5");
 createNewListOfQuestionsWithAnswers("10 Εφευρέσεις-ανακαλύψεις του 20ου αιώνα ?", " Αντιβιοτικά _3", " Αεροπλάνο ﻿_1", " Ραδιόφωνο _1",  "Τηλεόραση _1", " Ραντάρ _3", " Θεωρία της σχετικότητας _5", " Ατομική ενέργεια _3", " Τρανζίστορ _5"," Ολοκληρωμένο κύκλωμα _3"," DNA _1"," RNA _1"," Λέηζερ _1"," Ίντερνετ _1"," Κλωνοποίηση _3");
 createNewListOfQuestionsWithAnswers("10 Πόλεμοι του 20ου αιώνα ?", " Α' Παγκόσμιος Πόλεμος ﻿_1"," Ισπανικός Εμφύλιος _3",  "Β' Παγκόσμιος Πόλεμος _1", " Πόλεμος της Αλγερίας _3", " Πόλεμος της Ινδοκίνας _3", " Πόλεμος της Κορέας _1", " Πόλεμος του Βιετνάμ _1"," Αραβοϊσραηλινοί πόλεμοι _5"," Πόλεμος της Βοσνίας _1"," Βομβαρδισμός της Γιουγκοσλαβίας _1"," Πόλεμος στα Βαλκάνια _1"," Πόλεμος του Κόλπου _3"," Πόλεμος του Αφγανιστάν _1");
 createNewListOfQuestionsWithAnswers("10 Ταινίες-σειρές με serial killers ?", " Psycho  _1", " Dexter  ﻿_1", " Saw _1",  "Zodiac _1", " Memories of Murder _3", " Boy A  _3", " Bonnie and Clyde _3"," Monster _3"," Helter Skelter _5"," Funny Games  _3"," Se7en  _1"," Hannibal  _1","Red Dragon  _3","Perfume_3 ","Jack the Ripper _3");
 createNewListOfQuestionsWithAnswers("10 Serial killers ?", " Jack the Ripper-Τζάκ ο αντεροβγάλτης _3", " Charles Manson ﻿_3"," Ed Gein _5", " The Zodiac _1", " Nannie Doss _5", " Mary Ann Cotton _5", " Juan Corona _3"," Andrei Chikatilo _5"," Gilles de Rais _5"," Harold Shipman _3"," Henry Lee Lucas _3");
  createNewListOfQuestionsWithAnswers("10 Τούρκικες σειρές ?", " Έρωτας και Τιμωρία ﻿_3"," Εζέλ _1", " Μενεξέ _3",  "Πειρασμός _1", " Ρώτα την Αγάπη _3", " Σουλεϊμάν ο Μεγαλοπρεπής  _1", " Στα Βάθη της Ανατολής _5", " Στη Καρδιά της Πόλης _5"," Τα σύνορα της αγάπης _3"," Το Αγιάζι του Έρωτα _3"," Χίλιες και Μία Νύχτες _1"," Merhamet _3"," Ο ταύρος _1"," Βορράς και Νότος _3"," Σίλα _3"," Ασημένια φεγγάρια _3","Σον _3","Ποιο είναι το φταίξιμο της Φατμαγκιούλ;_3");
  createNewListOfQuestionsWithAnswers("10 Ξένα τηλεοπτικά κανάλια ?", " Cinemax _3", " Discovery Channel ﻿_1",  "Cartoon Network _3", " National Geographic _3", " Animal Planet _1", " BBC  _1", " CNN _1"," Disney Channel _5"," MTV _1"," History Channel _3"," CBC _5"," Al jazeera _3");
 createNewListOfQuestionsWithAnswers("10 Δεισιδαιμονίες ?",  " Σπασμένοι καθρέφτες ﻿_1"," Χυμένο αλάτι _3", " 666 _1",  "Ο αριθμός 13 _1", " Η Μαύρη Χήρα _3", " Nα δει ο γαμπρός την νύφη με το νυφικό της πριν από τον γάμο _5"," Nα τσουγκράς με άδειο ποτήρι-ή- με νερό _3"," Nα αφήνεις τα μαχαιροπίρουνά σου σε θέση σταυρού πάνω στο πιάτο σου _5"," Nα περνάς κάτω από σκάλα _1"," Nα διώξεις πασχαλίτσα _5"," Aνοιχτό ψαλίδι _3"," Nα μετράς τα αστέρια _3","Mαύρη γάτα_1");
createNewListOfQuestionsWithAnswers("10 Πόλεις της Αγγλίας ?", " Λίβερπουλ‎ _1", " Λονδίνο ﻿_1"," Κέμπριτζ _1",  "Νιούκασλ _1", " Ρέντινγκ _3", " Οξφόρδη _1", " Σαουθάμπτον _3", " Πόρτσμουθ _3"," Νότιγχαμ _5"," Μίντλεσμπρο _3"," Λιντς _3"," Μάντσεστερ _1"," Μπέρμιγχαμ _3");
createNewListOfQuestionsWithAnswers("10 Πόλεις της Ιταλίας ?", " Γένοβα _3"," Βερόνα _3", " Τορίνο _1",  "Βενετία _1", " Έμπολι _5", " Κάλιαρι _3", " Κατάνια _5", " Λιβόρνο _3"," Λέτσε _3"," Μιλάνο _1"," Μπολόνια _1"," Μπρέσια _5"," Νάπολη _1"," Πάρμα _1"," Παλέρμο _1"," Ρώμη _1"," Σιένα _3");
createNewListOfQuestionsWithAnswers("10 Πόλεις της Γερμανίας ?", " Αννόβερο _1", " Βόλφσμπουργκ ﻿_3"," Βόννη _1",  "Λεβερκούζεν _1", " Μάιντς _3", " Μπόχουμ _3", " Ντίσελντορφ _5", " Ντόρτμουντ _1", " Φρανκφούρτη _3", " Ρόστοκ _5", " Νυρεμβέργη _5");
createNewListOfQuestionsWithAnswers("10 Ταινίες του Steven Spielberg ?","Στενές επαφές τρίτου τύπου_3", " Jurassic Park _1", " Η διάσωση του στρατιώτη Ράιαν ﻿_3", " Οι άνδρες με τα μάυρα _1",  "Ε.Τ. _1", " Gremlins _5", " Empire of the Sun _3", " Casper _5", " Catch Me If You Can _3"," Minority Report _3"," Munich - Μόναχο _3"," The Terminal _5"," Schindler's List-Η λίστα του Schindler_1"," The Adventures of Tintin- Οι περιπέτειες του Τεν Τεν _3"," Ιντιάνα Τζόουνς και Οι Κυνηγοί της Χαμένης Κιβωτού _5"," Τα σαγόνια του καρχαρία _1"," 1941 _3"," Always _3"," Hook _3"," Lincoln _1"," A.I.-Artificial Intelligence _3");
 createNewListOfQuestionsWithAnswers("10 Διάσημες ατάκες (ξένων) ταινιών ?", " This is Sparta (300) _3", " asta lavista baby (Terminator) ﻿_3"," Go ahead, make my day (Dirty hary) _5", " Are you talking to me (Taxi driver)? _3",  "I'll make him an offer he can't refuse (Godfather) _5", " I see dead people( H Έκτη Αίσθηση ) _3", " My precious (Lord of the rings) _1", " I am flying Jack (Titanic) _5", " It's alive! It's alive!(Frangenstain) _3"," Houston, we have a problem ( Apolon 13 ) _3"," My name is Bond. James Bond _3","  Run, Forrest, run _3"," Show me the money! (Jerry Maguire) _5"," Say “Hello” to my little friend (Scarface - o σημαδεμένος) _3"," Hakuna matata (Lion king) _3"," You can't handle the truth (A Few Good Men) _3"," To infinity and beyond - στο άπειρο και ακομα παραπέρα (toy story) _5");
 createNewListOfQuestionsWithAnswers("10 Απο τα πιο  διάσημα ροκ συγκροτήματα ?", " Guns and Roses _3", " Evanescence  ﻿_1"," Green Day _3", " HIM _1",  "Linkin Park _1", " AC/DC  _1", " Iron Maiden _1", " Red Hot Chili Peppers  _1", " The Kaiser Chiefs _5"," The Scorpions  _1"," Black sabbath _3"," The Doors _3","  R.E.M _1"," Rolling Stones  _1"," Nirvana _1"," System Of A Down  _3"," Radiohead _3"," Beatles _1"," Led Zeppelin _1"," Ramones _5"," Eagles _3"," Bon Jovi _1"," Cranberries _5"," Metallica _1"," Pink Floyd  _1"," Genesis  _5");
 createNewListOfQuestionsWithAnswers("10 Εταιρείες-μάρκες  καπνού ?", " Κούπερ _3"," Golden Virginia, _3", " Old Holborn _1", " Marlboro _1", " Pal Mall _3", " Craven _1"," Black devil _5"," Lucky strike _1"," Philip morris _1"," Van Helle _5"," Καρέλια _3"," Drum _1");
 createNewListOfQuestionsWithAnswers("10 Όπλα πρίν απο την πυρίτιδα ?", " Ρόπαλο _3", " Καταπέλτης ﻿_3"," Μαχαίρι _3", " Ακόντιο _1",  "Σπαθί-Ξίφος _1", " Υγρό πυρ _3", " Σφεντόνα _3", " Τσεκούρι _1", " Μπούμερανγκ _3"," Τόξο _1"," Πολιορκητικός κρυός _5"," Chakram _5");
createNewListOfQuestionsWithAnswers("10 Τρόποι-μέσα μεταφοράς ?", " Πόδια _1"," Τελεφερίκ _5"," Ποδήλατο _1", " Αμάξι ﻿_1"," Πατίνι _3", " Κάρο _3",  "Τράμ _1", " Μετρό _1", " Μηχανή-Μοτοσικλέτα _1", " Πλοίο _1", " Αεροπλάνο _1"," Ελικόπτερο _1"," Τζέτ σκι _3"," Άλογο _1"," skateboard _3"," rollers _3");
 createNewListOfQuestionsWithAnswers("10 Μάρκες μπύρας ?", " Χάρμα _3", " Magnus Magister ﻿_5"," Buckler  _3", " Amstel _1",  "Heineken _1", " Carib _3", " Mc Farland _3"," Murphy’s _5","Μύθος _1"," Alpha _3"," Fix _1"," Kaizer _3"," Fischer _3"," Βεργίνα _5");
 createNewListOfQuestionsWithAnswers("10 Λέξεις για το σκάκι ?", " Ρολόι _3", " Σκακιέρα ﻿_1"," Παίχτες _3", " Πιονάκι _1",  " Πύργος _1", " Ίππος-Άλογο _3", " Αξιοματικός _1", " Βασιλιάς  _1", " Βασίλισσα _1"," Κίνηση _3"," Elo _5"," Rocke _5"," Σαχ _1"," Ματ _1"," Πατ _3"," Γκάρι Κασπάροβ _3");
createNewListOfQuestionsWithAnswers("10 Θεότητες της ελληνικής μυθολογίας ?", " Ζευς - Δίας _1", " Ήρα ﻿_1"," Ποσειδώνας _1", " Δήμητρα _1",  "Εστία _5", " Απόλλων _3", " Αφροδίτη _1", " Άρης _1", " Άρτεμις _1"," Ήφαιστος _3"," Αθηνά _1"," Ερμής _1"," Παν _3"," Περσεφόνη _5"," Αίολος _3"," Διόνυσος _3"," Πλούτωνας _3");
createNewListOfQuestionsWithAnswers("10 Ελληνικά κόμματα ?", " Ανεξάρτητοι Έλληνες‎ _3", " Χρυσή Αυγή‎ ﻿_1","  Οικολόγοι Πράσινοι _3", " Νέα Δημοκρατία‎ _1",  "Κομμουνιστικό Κόμμα Ελλάδας-ΚΚΕ _1", " ΠΑΣΟΚ _1", " Πολιτική Άνοιξη _3", " Ένωση Κεντρώων  _5", " ΣΥ.ΡΙΖ.Α _1"," Δημοκρατική Αριστερά _3"," Κίνημα Δεν Πληρώνω  _5"," Παναθηναϊκό Κίνημα _3"," Συνασπισμός Ριζοσπαστικής Αριστεράς _5");
 createNewListOfQuestionsWithAnswers("10 Ζωα που τρώει ο άνθρωπος ?", " Άλογο _3", " Βάτραχος ﻿_5"," Φίδι _3", " Κουνέλι _1",  "Λαγός _1", " Γουρούνι _3", " Κότα _1", " Πρόβατο _1", " Κατσίκα _1"," Σαλιγκάρι _5"," Βόδι _3"," Αγελάδα _1"," Αράχνη _3");
 createNewListOfQuestionsWithAnswers("10 Κατοικίδια ζώα ?", " Σκύλος _1","Κουνέλι _1", " Γάτα ﻿_1"," Ινδικό χοιρίδιο _3", " Παπαγάλος _1",  "Χρυσόψαρο _1", " Ινγκουάνα _3", " Χαμαιλέων _5", " Χελώνα _3", " Καναρίνι _3"," Φίδι _3"," Ταραντούλα _3"," Χάμστερ _3"," Άλογο _5");
 createNewListOfQuestionsWithAnswers("10 Όργανα του σώματος ?", " Εγκέφαλος _1", " Καρδιά ﻿_1"," Σπλήνα _3", " Στομάχι _1",  "Λεπτό-Παχύ Έντερο _1", " Νεφρό _3", " Στεφανιαία _5", " Ήπαρ _1"," Πάγκρεας _3"," Προστάτης _3"," Συκώτι _1"," Διάφραγμα _3"," Αορτή _5"," Πνεύμονες _1"," Χοληδόχος κύστη _5");
  createNewListOfQuestionsWithAnswers("10 Βιβλία του Paulo Coelho ?", " Το χειρόγραφο της Άκρα _5", " Ο αλχημιστής ﻿_1"," Βαλκυρίες _3", " Άλεφ _1",  "Και ο Θεός έπλασε τη μητέρα _3", " Το εγχειρίδιο του πολεμιστή του φωτός _5", " Έντεκα λεπτά _3", " Αγάπη _3", " Το ημερολόγιο ενός μάγου _3"," Ο νικητής είναι μόνος _5"," Η Βερόνικα αποφασίζει να πεθάνει _5"," Μπρίντα _3"," Το πέμπτο βουνό _3"," Στις όχθες του ποταμού Πιέδρα κάθισα και έκλαψα _5","Οι εξομολογήσεις του προσκυνητή _5"," Οι ερωτικές επιστολές του προφήτη _5"," Η μάγισσα του Πορτομπέλο _5");
 createNewListOfQuestionsWithAnswers("10 Διάσημοι ζωγράφοι ?", " Picasso _1", " Salvador Dali ﻿_1"," Michelangelo _1", " Van Gogh _1",  " Leonardo da Vinci _1", " Toulouse Lautrec _3", " El Greco _1", " Wassily Kandinsky _5"," Renoir-Ρενουάρ _3"," Αμεντέο Μοντιλιάνι _5"," Κλοντ Μονέ _3"," Ρέμπραντ _3"," Μιχαήλ Αγγελος _1"," Μποντιτσέλι _5"," Χατζηκυριάκος Γκίκας _5");
 createNewListOfQuestionsWithAnswers("10 Πνευστά μουσικά όργανα ?", " Σαξόφωνο _3", " Φλογέρα ﻿_1"," Κλαρίνο _1", " Όμποε _5",  "Κλαρινέτο _3", " Πίκολο _5", " Φαγκότο _5", " Φλάουτο _1"," Τρομπέτα _1"," Τρομπόνι _3"," Κόρνο _5");
 createNewListOfQuestionsWithAnswers("10 Κρουστά μουσικά όργανα ?", " Μπαγκέτες _3", " Ταμπούρο ﻿_3"," Τύμπανο _1",  "Ξυλόφωνο _3", " Ντέφι _5", " Τουμπελέκι _1"," Καστανιέτες _5"," Καμπάνα _3"," Τρίγωνο _3","Ντραμς _1");
 createNewListOfQuestionsWithAnswers("10 Έγχορδα μουσικά όργανα ?", " Βιόλα _3", " Βιολί ﻿_1"," Βιολοντσέλο _3", " Κιθάρα _1", " Κοντραμπάσο _3", " Λύρα _1", " Λαούτο _3", " Μαντολίνο _1"," Μπάσο _1"," Μπαγλαμάς _1"," Μπαλαλάικα _5"," Μπουζούκι _1"," Σαντούρι _5");
 createNewListOfQuestionsWithAnswers("10 Πρωθυπουργοί στην Ελλάδα ?", " Ελευθέριος Βενιζέλος _3", " Δημήτριος Γούναρης ﻿_3"," Γεώργιος Κουντουριώτης _5", " Αντώνης Σαμαράς _1",  "Παναγιώτης Πικραμμένος _5", " Λουκάς Παπαδήμος _5", " Γεώργιος Α. Παπανδρέου _1", " Κωνσταντίνος Α. Καραμανλής _1", " Κωνσταντίνος Σημίτης _1"," Ανδρέας Παπανδρέου _1"," Κωνσταντίνος Μητσοτάκης _1"," Ιωάννης Γρίβας _3"," Γεώργιος Ράλλης _3"," Τζαννής Τζαννετάκης _5"," Κωνσταντίνος Γ. Καραμανλής _1"," Αλέξανδρος Μαυροκορδάτος _5"," Θεόδωρος Πάγκαλος ( Ο παλιός ) _3"," Δημήτριος Βούλγαρης _5"," Ιωάννης Καποδίστριας  _3");
 createNewListOfQuestionsWithAnswers("10 Αρχαία και νέα θαύματα του κόσμου ?", "  Το χρυσελεφάντινο άγαλμα του Δία _3", " Ο Ναός της Αρτέμιδος (στην Έφεσο) ﻿_3"," Το Μαυσωλείο της Αλικαρνασσού _5", "  Ο Κολοσσός της Ρόδου _1",  "Ο Φάρος της Αλεξάνδρειας _3", " Η πυραμίδα του Χέοπα στη Γκίζα ( της Αιγύπτου ) _3", " Οι κρεμαστοι κήποι της Βαβυλώνας _3", "  Σίτζεν Ίτζα, Μεξικό ( Γιουκατάν ) _5", " Χριστός Λυτρωτής , Βραζιλία ( Ρίο Ντε Τζανέιρο ) _3","  Σινικό Τείχος, Κίνα _3"," Μάτσου Πίτσου, Περού  _3","Πέτρα, Ιορδανία _5","Το Κολλοσαίο της Ρώμης, Ιταλία _1","Ταζ Μαχάλ, Ινδία ( Άνγκρα )_3");
createNewListOfQuestionsWithAnswers("10 Γνωστές μάρκες αυτοκινήτων της Ευρόπης ?", " Opel _1", " Audi ﻿_1"," Bentley _3", " Bugatti  _3",  "Lamborghini _3", " MAN _3", " Renault _3", " Porsche _3"," Seat _3"," Volkswagen-VW _1"," BMW _1"," Volvo  _3"," Škoda _3"," Citroën _1"," Rolls-Royce _3"," Smart _3");
createNewListOfQuestionsWithAnswers("10 Αρχαίοι Έλληνες φιλόσοφοι ?", " Σωκράτης _1", " Πυθαγόρας ﻿_3"," Πλάτων _1", " θαλής  _3",  "Ηράκλειτος _3", " Επίκουρος _5", " Αναξαγόρας _5"," Αριστοτέλης _1"," Αριστοφάνης _1"," Αρχιμήδης _1"," Ιπποκράτης _3","Σόλον _3","Δημόκριτος _5");
 createNewListOfQuestionsWithAnswers("10 Είδη ρουχισμού-ρούχων σε άνδρες ?", " Παπούτσια _1", " Παπιγιόν ﻿_3"," Γραβάτα _3", " Βερμούδα _3",  "Μποξεράκι _1", "Παντελόνι _1", " Κάλτσες _1", " Μπουφάν _1", " Ζακέτα _1"," Καμπαρντίνα _5"," Παλτό _3"," Πουκάμισο _1","  Μπλούζα _1"," Πουλόβερ _1"," Αντρικο Στρινγκ _5"," Γάντι _3"," Ζώνη _1"," Καπέλο  _1"," Φουλάρι-Σκούφος _3");
 createNewListOfQuestionsWithAnswers("10 Αντικείμενα σε ένα σαλόνι  ?", " Βιβλιοθήκη _3","Πίνακας ζωγραφικής _5","Κορνίζα _3","Τηλεόραση _1", " Τραπέζι ﻿_1"," Φωτιστικό _3",  "Καναμπές  _1", " Πούφ _5", " Πολυθρόνα _1"," Καρέκλα _1"," Ανάκληνδρο _5","Καθρεφτης _1");
 createNewListOfQuestionsWithAnswers("10 Νομοί της Ελλάδας ?", " Νομός Αττικής _1", " Νομός Λασιθίου ﻿_3"," Νομός Ρεθύμνης _3", " Νομός Ηρακλείου _1",  "Νομός Έβρου _1", " Νομός Εύβοιας _3", " Νομός Χαλκιδικής _1", " Νομός Ημαθίας _5"," Νομός Ξάνθης _1"," Νομός Αιτωλοακαρνανίας _3"," Νομός Αχαΐας _3"," Νομός Πρέβεζας _3"," Νομός Χανίων _1"," Νομός Ηλείας _3"," Νομός Άρτας _5");
createNewListOfQuestionsWithAnswers("10 Δουλειές του σπιτιού ?", " Πλύσιμο πιάτων _1", " Πλυντήριο ﻿_1"," Συμμάζεμα _3",  "Ηλεκτρική σκούπα-Σκούπισμα _1", "  Ξεσκόνισμα _1", " Σφουγγάρισμα _1", " Καθαρισμός μπάνιου _3"," Πλύσιμο κουρτίνων _3"," Καθάρισμα χαλιών _3"," Σιδέρωμα _1","Αλλαγή διακόσμησης _5","Μαγείρεμα _1");
createNewListOfQuestionsWithAnswers("Δουλειές που μπορείς να κάνεις από το σπίτι ?", " Ελεύθερος συγγραφέας _3", " Διακοσμητής εσωτερικού χώρου ﻿_5"," Εικονογράφος _3", " Μεταφραστής _1",  "Συνεντεύξεις για έρευνα αγοράς _5", " Φύλαξη παιδιών _3", " Δημιουργός εικόνας - Γραφίστας _3", " Δάσκαλος μουσικής _5", " Οικονόμος _3"," Προγραμματιστής _5"," Ροζ γραμμές _5","Μαγείρεμα _3");
 createNewListOfQuestionsWithAnswers("10 Γνωστοί ήχοι ζώων ?", " Μουου (Αγελάδα) _1", " Ου Ου Ου (Πίθηκος) _1", "Κουάξ (Βάτραχος) ﻿_3"," Νιάου- μιάου (Γάτα ) _1", " Γκαρ γκαρ (Γάιδαρος )_5",  " Γρου γρου-Χρου χρου (Γουρούνι) _5", " Τζι τζι (Τζίτζικας) _3", "Γαβ γαβ (Σκύλος) _1", "Μπεε (Πρόβατο)  _1","Μεεε (Κατσίκα)  _1", " Τσίου (Πουλί)  _3"," Πα-πα (Πάπια)_3","ζζζ (Μέλισσα - Κουνούπι) _3","Αούου (Λύκος) _1","Κα κα - Κο κο (Κότα)  _1","Κρα κρα (Κοράκι)_3","Κικιρίκου-Κιρικό (Κόκορας) _3"," Φρρρ-Γλου γλου (Γαλοπούλα) _3");
 createNewListOfQuestionsWithAnswers("10 Ταινίες του Stanley Kubrick ?", " Full Metal Jacket _3", " 2001: A Space Odyssey ﻿_5"," A Clockwork Orange- Κουρδιστό πορτοκάλι _3", " The Shining-Η λάμψη _1",  "Paths of Glory  _3", " Eyes Wide Shut -Μάτια ερμητικά κλειστά _3", " Spartacus  _3", " Lolita  _3", " The Killing  _3"," Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb  _5"," Killer's Kiss  _5","Fear and Desire  _5");
 createNewListOfQuestionsWithAnswers("10 Ταινίες του Martin Scorsese ?", "Hugo  _1", "Shutter Island  ﻿_1"," The Departed - O πληροφοριοδότης  _3", " Frankenstein  _3",  "The Aviator _3", " Casino  _3", " Goodfellas  _1", " Taxi Driver  _3", " The Last Temptation of Christ _5"," Cape Fear  _5"," The Young Victoria _5"," Νύφες _3"," The Age of Innocence-Τα χρόνια της αθωότητας _3");
 createNewListOfQuestionsWithAnswers("10 Άγγλοι-Αγγλίδες συγγραφείς ?",  " Κρίστοφερ Μάρλοου ﻿_5"," Ντάνιελ Ντεφόε _3", " Μπρους Ντίκινσον _1",  "Ουίλλιαμ Σαίξπηρ _1", " Καρλ Σούκερ _5", " Ντάγκλας Ντάκιν _3"," Ελέανορ Μαρξ _5","Χιου Λώρι _3"," Μπράιαν Ζακ _3"," Άλιστερ Κρόουλι _3"," Άρθουρ Κλαρκ _5");
 createNewListOfQuestionsWithAnswers("10 Γάλλοι-Γαλλίδες συγγραφείς ?", " Ζακ-Υβ Κουστώ _3", " Νοστράδαμος ﻿_3"," Καρδινάλιος Ρισελιέ _3", " Gustave Flaubert   _3",  "Jules Verne _3", " Μοντελιέ _3", " Charles Baudelaire _5", " Βολταίρος _3", " Victor Hugo _1"," Αλέξανδρος Δούμας  _1");
 createNewListOfQuestionsWithAnswers("10 Λέξεις για το Ducktails-Λιμνούπολη ?", " Ντόναλντ Ντακ _1", " Νταίζυ Ντακ ﻿_3"," Σκρουτζ Μακ Ντακ _1", " Χιούη, Λιού, Ντιούη _1",  "Γκαστόνε  _3", " Μουργολύκοι _1", " Ρόμπαξ _3", " Εξηνταβελόνης _3", " Κύρος Γρανάζης _3"," Μάτζικα Ντε Σπελ _3"," Λούντβιχ Φον Ντρέικ _5"," Παντάξιος _5"," Γλόμπος _3"," Οι Μικροί Εξερευνητές _5","Armstrong _5");
 createNewListOfQuestionsWithAnswers("10 Γνωστα ζωα με κέρατο/α ?", " Πρόβατο _1", " Κατσίκα ﻿_1"," Βούβαλος _3", " Ελάφι _1", " Αγελάδα -Ταύρος _1", " Ρινόκερος _3"," Βίσωνας _5"," Αλμπίνο _5"," Αντιλόπη _5","Αγριογούρουνο _3");
 createNewListOfQuestionsWithAnswers("10 Αντρικά ονόματα απο 'Α' ?",  " Αβραάμ ﻿_3", " Αγάπιος _5",  "Άγγελος _1", " Αδάμ _1", " Άδωνις _3", " Αθανάσιος _1"," Αιμίλιος _5"," Αλκίνοος _3"," Αλέξανδρος _1"," Αλκαίος _3"," Αλέκος _1"," Ανέστης _1"," Ανδρέας _1"," Αντώνης _1"," Αποστόλης _3"," Αργύρης _3"," Αρθούρος _5"," Αριστείδης _3"," Αριστοφάνης _3"," Αρσένης _5"," Αχιλλέας _3");
 createNewListOfQuestionsWithAnswers("10 Γυναικεία ονόματα απο 'Α' ?", " Αγγελική ﻿_1"," Αγλαΐα _5",  "Αθανασία _3", " Αθηνά _1", " Αικατερίνη _3", " Αιμιλία _3", " Αλέκα _3"," Αλεξάνδρα _1"," Αμαλία _3"," Αμάντα _5"," Αναστασία _1"," Ανδριάνα _3"," Ανθούλα _5"," Άννα _1"," Αννέτα-Αννίτα _3"," Αντωνία _1"," Αριάδνη _3"," Άρτεμις _5"," Ασημίνα _5"," Ασπασία _3"," Αφροδίτη _1");
createNewListOfQuestionsWithAnswers("10 Αποστολοι ?", " Ανδρέας  _1", " Σίμων o Πέτρος ﻿_5"," Ιάκωβος _5", " Ιωάννης (ευαγγελιστής)  _1",  "Φίλιππος _3", " Βαρθολομαίος - Ναθαναήλ _3", " Θωμάς _1", " Ματθαίος (ευαγγελιστής) _3", "Ιάκωβος _5"," Θαδδαίος _5"," Σίμων ο Κανανίτης _5"," Ματθία (μετά την Ανάσταση του Ιησού, οι μαθητές τον εξέλεξαν αντί του Ιούδα ) _5");
  createNewListOfQuestionsWithAnswers("10 Πολιούχοι άγιοι ?", " Αγ. Φανούριος (Ρόδος)  _5", " Αγ. Χαράλαμπος (Πύργος Ηλείας, Κέα) ﻿_5"," Αγ. Σπυρίδων (Κέρκυρα, Μεσολόγγι, Πειραιάς, Κίσαμος) _3", " Αγ. Σεραφείμ ( Καρδίτσα, Λιβαδειά) _5",  "Απόστολος Παύλος( Κόρινθος, Καβάλα) _1", " Αγ. Παντελεήμονας (Φλώρινα, Κάτω Νευροκόπι, Παρανέστι, Τήλος)_5", " Αγ. Νικόλαος (Αλεξανδρούπολη, Βόλος, Δελφοί, Κοζάνη, Πολύγυρος, Πάργα, Σητεία) _1", " Αγ. Νικήτας (Σέρρες, Νίσυρος) _3", " Αγ. Μηνάς ( Ηράκλειο, Καστοριά, Ελευθερούπολη) _3"," Αγ. Μαρκέλλα(Χίος) _5"," Άγ. Λουκάς (Λαμία, Έδεσσα) _1"," Αγ. Κωνσταντίνος ο Υδραίος (Ρόδος) _5"," Αγ. Ιωάννης (Ξάνθη, Πτολεμαΐδα) _1"," Αγ. Διονύσιος Αρεοπαγίτης (Αθήνα) _5"," Αγ. Γεώργιος (Εράτυρα, Βεύη Φλώρινας, Σουφλί, Γουμένισσα, Νεμέα, Νιγρίτα, Σιδηρόκαστρο) _1"," Αγ. Βαρβάρα (Δράμα, Αργυρούπολη) _3"," Αγ. Αρσένιος(Πάρος) _1"," Αγ. Αχίλλιος (Λάρισα ) _3"," Αγ. Αντώνιος (Βέροια, Περιστέρι Αττικής) _3"," Αγ. Αικατερίνη (Κατερίνη) _3"," Αγ. Αναστάσιος (Ναύπλιο)_3");
createNewListOfQuestionsWithAnswers("10 Γυναικεία ονόματα απο 'Ν'  ?", " Ντόρα ﻿_1"," Νάντια _3", " Ναταλία _3",  "Νατάσα _1", " Ναυσικά _3", " Νεκταρία _3", " Νεφέλη _5", " Νικηφορία _5"," Νικολέττα _1"," Νίνα _1"," Νινέτα _3"," Νίτσα _3"," Νταίζη _3"," Ντιάνα _3");
 createNewListOfQuestionsWithAnswers("10 Γυναικεία ονόματα απο 'Π'?",  " Παναγιώτα ﻿_1"," Πανδώρα _3", " Παρασκευή _3",  "Παρθένα _3", " Παυλίνα _3", " Πελαγία _1", " Πέπη _3"," Περσεφόνη _5"," Πηνελόπη _3"," Πηγή _3"," Πολυξένη _3"," Πωλίνα _5");
createNewListOfQuestionsWithAnswers("10 Γυναικεία ονόματα απο 'Μ'?", " Μάγδα _3", " Μαίρη _1", " Μαντώ _1",  "Μαριάννα _1", " Μαριγώ _5", " Μαριέτα _3", " Μαρίκα _3", " Μαριλένα _1"," Μαρίνα _1"," Μαρκέλλα _5"," Μάχη _3"," Μελίνα _1"," Μιρέλλα _3"," Μισέλ _3"," Μιχαέλα _3");
 createNewListOfQuestionsWithAnswers("10 Αντρικά ονόματα απο 'Ε'  ?", " Έκτορας ﻿_3"," Ελευθέριος _1", " Εμμανουήλ _1", " Επαμεινώνδας _5", " Ευάγγελος _1", " Ευγένιος _3", " Ευθύδημος _3"," Ευρυβιάδης _5"," Ευστάθιος _1"," Ευστράτιος _1"," Ευτύχης _3");
createNewListOfQuestionsWithAnswers("10 Αντρικά ονόματα απο 'Ν' ?", " Ναθαναήλ _3", " Νάσος ﻿_3", " Νεκτάριος _3",  "Νεόφυτος _3", " Νεόκριτος _3", " Νέστωρας _3", " Νικήστρατος _5", " Νικήτας _1"," Νικηφόρος _1"," Ντάνι _5"," Ντίνος _3");
 createNewListOfQuestionsWithAnswers("10 Χώρες απο 'Μ' ?", " Μαδαγασκάρη _1", " Μαλαισία ﻿_3"," Μαλδίβες  _3", " Μάλι _3",  "Μάλτα _1", " Μαρόκο _1", "  Μεξικό _1", " Μογγολία _3"," Μοζαμβίκη _5"," Μολδαβία _3"," Μονακό _1"," Μπανγκλαντές _3"," Μπαρμπάντος _3"," Μπαχάμες _1"," Μπουρούντι _3");
 createNewListOfQuestionsWithAnswers("10 Χώρες απο 'Α'  ?", " Αγγλία _1", " Αρμενία _1",  "Αργεντινή _1", " Ανδόρρα  _3", " Ανγκόλα _3", " Αζερμπαϊτζάν _3"," Αφγανιστάν  _3"," Αίγυπτος  _1"," Αιθιοπία _3"," Αϊτή  _5"," Ακτή Ελεφαντοστού _5"," Αλβανία _1"," Αλγερία  _1"," Αυστρία _3"," Αυστραλία _1");
 createNewListOfQuestionsWithAnswers("10 Χώρες απο 'Κ' ?", " Καζακστάν  _3", " Καμερούν ﻿_1", " Καναδάς _1",  "Κατάρ _5", " Κένυα  _3", " Κίνα _1", " Κύπρος _1", " Κροατία _1"," Κουβέιτ _5"," Κούβα _1"," Κόστα Ρίκα _5"," Κονγκό _3"," Κολομβία  _1");
 createNewListOfQuestionsWithAnswers("10 Χώρες απο 'Σ' ?",  " Σαουδική Αραβία ﻿_3"," Σενεγάλη  _3", " Σερβία _1",  "Σεϋχέλλες _5", " Σιγκαπούρη _3", " Σιέρα Λεόνε _5", " Σκωτία _3", " Σλοβακία _1"," Σλοβενία _1"," Σουδάν _3"," Σρι Λάνκα _5"," Συρία _3");
 createNewListOfQuestionsWithAnswers("10 Χώρες απο 'Γ'?", " Γαλλία _1", " Γερμανία  ﻿_1"," Γεωργία  _1", " Γιβραλτάρ  _5",  "Γκαμπόν _5", " Γκάνα  _3", " Γουατεμάλα _5", " Γουινέα _3"," Γουιάνα _5","  Γρενάδα _1");
 createNewListOfQuestionsWithAnswers("10 πόλεμοι που συμμετείχαν έλληνες (απο το 1821 εως σήμερα )?", " Α' Παγκόσμιος Πόλεμο‎ς _1", " Β' Παγκόσμιος Πόλεμο‎ς _1", " Βαλκανικοί πόλεμοι‎ ﻿_3"," Επανάσταση του 1821‎ _3", " Μακεδονικός Αγώνας‎ _5",  " Ελληνοτουρκικός πόλεμος _3", " Εμφύλιος Πόλεμος‎ _3", " Ελληνοϊταλικός πόλεμος  _1", " Μικρασιατική Εκστρατεία‎ _5", " Πόλεμος της Κορέας‎ _5"," Εισβολή στην Κύπρο‎ _3"," Εκστρατεία της Κριμαίας _5");
 createNewListOfQuestionsWithAnswers("10 Χώρες της Αμερικής?"," Αργεντινή _1", " Βραζιλία _1",  "Βολιβία _3", " Βενεζουέλα _5", " Γρενάδα _5"," Ελ Σαλβαδόρ _5"," ΗΠΑ _1"," Ισημερινός _3"," Καναδάς _1"," Κολομβία _1"," Κόστα Ρίκα _5"," Κούβα _3"," Μεξικό _1"," Ουρουγουάη _1"," Παραγουάη _1"," Περού _3"," Τζαμάικα _3","  Χιλή _1");
createNewListOfQuestionsWithAnswers("10 Παροιμιες-ατάκες με την λεξη φίλος ?", " Το νερό με τη φωτιά φίλοι δε γίνονται _5", " Το φίλο σου τον διαλέγεις, το συγγενή σου όχι! ﻿_3"," Φίλος που γίνηκε εχθρός ποτέ δεν ήταν φίλος. _5",  "Οι καλοί λογαριασμοί κάνουν τους καλούς φίλους. _3", " Ο καλός φίλος στην ανάγκη φαίνεται. _3", " Μπρος φίλος και πίσω σκύλος _5", " Κάλλιο δολερό εχθρό παρά δολερό φίλο _5", " Καινούριο φίλο έπιασες; Παλιό μη λησμονήσεις… _5"," Δείξε μου το φίλο σου, να σου πω ποιος είσαι _1"," Αγάπα το φίλο σου με τα ελαττώματά του _1","Ο φίλος της ανάγκης είναι αληθινός φίλος _3");
 createNewListOfQuestionsWithAnswers("10 Παροιμιες-ατάκες με την λεξη νερο ?", " Ρίχνω νερό στο κρασί μου _1", " Δεν δίνει του αγγέλου του νερό ﻿_3"," Είναι του γλυκού νερού _3", " Πήγε στη βρύση/ Έφτασε στην πηγή , και νερό δεν ήπιε _1",  "Έβαλε/Πήγε το νερό στ' αυλάκι _1", " Κάνει νερά _3", " Πίνω νερό στ’ όνομά του _3", " Πνίγεται σε μια κουταλιά νερό _3", " Ίσα βάρκα ίσα νερά _3"," Έκανε μια τρύπα στο νερό _5"," Μοιάζουν σαν δύο σταγόνες νερό _3"," Σαν τα κρύα τα νερά _3"," Τον φέρνω με τα / στα νερά μου _3"," Ήπιε το αμίλητο νερό _5"," Ό,τι είπαμε, νερό κι αλάτι _3"," Το αίμα νερό δεν γίνεται _3"," Είπαμε το νερό νεράκι _5");
createNewListOfQuestionsWithAnswers("10 Παροιμιες-ατάκες με την λέξη γλώσσα  ?", " Γλώσσα παπούτσι, μυαλό κουκούτσι ﻿_5"," Δε βάζει γλώσσα μέσα _3", " Η γλώσσα κάστρα καταλύει και κάστρα θεμελιώνει. _5",  "Η γλώσσα κόκαλα δεν έχει και κόκαλα τσακίζει _1", " Η γλώσσα του πάει ροδάνι. _5", " Η γλώσσα του στάζει μέλι. _3", " Θα σου κόψω τη γλώσσα _1", " Κατάπιε τη γλώσσα του _3"," Λύθηκε η γλώσσα του _3"," Μάλλιασε η γλώσσα μου _1"," Μου ’βγαλε γλώσσα _3"," Στέγνωσε η γλώσσα μου _3"," Φάε τη γλώσσα σου _1");
 createNewListOfQuestionsWithAnswers("10 Απο τις χώρες της Ε.Ε. - Ευρώπης ?", " Φινλανδία  _3", " Τσεχική Δημοκρατία ﻿_3"," Σουηδία  _1", " Σλοβακία _3",  "Σλοβενία _1", " Πορτογαλία _1", " Ρουμανία _1", " Πολωνία  _1", " Ουγγαρία  _3"," Μάλτα  _3"," Λουξεμβούργο _3"," Λιθουανία  _3"," Λεττονία  _3"," Κύπρος  _1"," Ιταλία _1"," Ισπανία  _1"," Ιρλανδία _3"," Ηνωμένο Βασίλειο _1"," Εσθονία _3"," Ελλάδα  _1"," Δανία _1"," Γερμανία  _1"," Γαλλία _1"," Βουλγαρία _1"," Βέλγιο  _1"," Αυστρία  _1");
 createNewListOfQuestionsWithAnswers("10 Ήρωες του Μίκυ Μάους  ?", " Μίκυ Μάους  _1", " Μίννι Μάους  ﻿_1"," Γκούφυ _1", " Σούπερ Γκούφυ  _3",  "Κλάραμπελ  _3", " Οράτιος (Χαλινάρης) _3", " Μαύρος Πητ _3", " Μαύρο Φάντασμα _5", " Πλούτο _1"," Μόρτιμερ  _5"," Ο’ Χάρα  _5"," Γάτζος  _5"," Μάγισσα Φούρκα _5"," Ήτα Βήτα  _5"," Ινδιάνα Γκούφυ _5");
 createNewListOfQuestionsWithAnswers("10 Χαρακτήρες κόμικς που αρχίζουν απο 'Μ' ?", " Μάτζικα Ντε Σπελ _3", " Μίκυ Μάους ﻿_1"," Μίννι Μάους _1",  "Μαύρο Φάντασμα _3", " Μαγκνέτο _3"," Μαύρη Χήρα _3"," Μίστερ Φαντάστικ _5"," Μουργόλυκοι _3"," Μπάτμαν _1"," Μπλέικ _3"," Μυστίκ _3","Μόρτιμερ _5");
 createNewListOfQuestionsWithAnswers("10 Παιδικές σειρές της 10ετίας του 80 ?", " Κάπου, Κάπως, Κάποτε _5", " Ροζ Πάνθηρας ﻿_1"," Μικρό μου Πόνυ _3",  "Sport Billy _5", " Αστυνόμος Σαΐνης _3", " Τα Στρουμφάκια _1", " Μάγια η Μέλισσα _3"," Κάντυ Κάντυ _5"," Χάιντι _5"," Thundercats _3"," G.I. Joe _5"," Transformers _1"," Dragonball _3"," Kabamaru _3"," Χελωνονιντζάκια _1","DuckTales _3");
 createNewListOfQuestionsWithAnswers("10 Τραγούδια για ζώα απο τα 'ζουζούνια' ?", " Το γουρουνάκι _3", " Το κουνούπι ﻿_5"," Μια ώραια πεταλούδα _3", " Το ζωηρό χταπόδι _5",  "Η καλή μας αγελάδα _1", " Η κουκουβάγια _3", " Το γατάκι _1", " Φύγε φυγε ποντικάκι _5", " Αχ κουνελάκι _3"," Τα έξι βατραχάκια _5"," Ήταν ενας γάιδαρος _1");
createNewListOfQuestionsWithAnswers("10 Τραγούδια για ζώα απο τους 'Mazoo and the Zoo' ?", " H Κότα _3", "  Η Αρκούδα ﻿_3"," Η Πεταλουδίτσα _5", "Η Ψιψίνα _3",  "Η Πάπια  _3", " Ο Παπαγάλος _1", "Η Καμήλα _1", " Πλάτωνας ο Σκύλος Μου _5", " Η Κότα η Βαρβάρα _5","  Καμήλα _1"," Ο γορίλας _1","Ο σκαντζόχοιρος _3","Το λιοντάρι _1","Ο ελέφαντας _3","Η κατσίκα _1","Η μαϊμού","Πιγκουίνος _5","Ιγκουάνα _3","Η αλεπού _1","Το άλογο _1","Ο γάιδαρος _1","Η αλεπού _3");
createNewListOfQuestionsWithAnswers("10 Τραγούδια  με πουλιά (ή είδη πουλιών) στον τίτλο ?", "  Για μας κελαηδούν τα πουλιά (Νίκος Γούναρης) _3", " Ακολούθα τα πουλιά ﻿(Ελένη Βιτάλη)_3"," Εγώ ειμαι αετός (Καζαντζίδης)  _3", " Είμαι αϊτός χωρίς φτερά (Γρηγόρης Μπιθικώτσης) _3",  "Ο αετός (Σφακιανάκης) _1", " Το πουλάκι τσίου _3", " Μαύρο Γεράκι (Κατερίνα Παπαδοπούλου -Στιβακτάκης Χρήστος) _5", " Γέλα πουλί μου (Αδελφοί Κατσιμίχα) _3", " Τα πουλιά πέταξαν μακριά (Γιάννης Κοκκάκης) _5"," Μια νυχτεριδα στη σκεπη (Μίκης Θεοδωράκης) _3"," Το τραγούδι των πουλιών (παραδοσιακό) _3","Θα ζήσω ελέυθερο πουλί (Χρηστάκης)_3","Καναρίνι μου γλυκό(Τσαλιγοπούλου) _3","Της Γερακινας Γιος (Τσιτσάνης)_5","Ένα το Χελιδόνι ( Μίκης Θεοδωράκης) _1");
 createNewListOfQuestionsWithAnswers("10 Ταινίες με ζόμπι ?", " Dawn of the Dead _3"," Resident Evil _1", " 28 Days Later-28 Μέρες Μετά _3",  "REC _1", " Dead snow _5", " Zombieland _5", " Shaun of the dead _3"," Night of the living dead-Η νύχτα των ζωντανών νεκρών _3","  The return of the living dead _5"," Grindhouse _3"," 28 Weeks Later _3"," House of the Dead  _3"," Walking Dead _3"," Zombie Planet  _3"," Zombies Gone Wild _5");
 createNewListOfQuestionsWithAnswers("10 Ελληνικά τραγούδια που περιέχουν στον τίτλο τους τη λέξη 'αγάπη'?", " Αν ήσουν αγάπη(Έλενα Παπαρίζου) _3", "Η αγάπη θέλει δύο ( Aλικη Βουγιουκλάκη & Δημήτρης Παπαμιχαήλ ) ﻿_5"," Αγάπη υπερβολική (Άννα Βίσση)_1", "Αγάπη Καλοκαιρινή (Θάνος Καλλίρης) _1",  " Να μ'αγαπάς , Παύλος Σιδηρόπουλος  _3", "Αγάπη(Δέσποινα Βανδή) _1", " Καινούργια αγάπη( Αntique) _5", " Αν είσαι η αγάπη (Γιάννης Πλούταρχος) _3", " Που είναι η αγάπη(Μιχάλης Χατζηγιάννης) _3"," Επικίνδυνη αγάπη(Νίκος Οικονομόπουλος) _3"," Η αγάπη έρχεται στο τέλος ( Αντώνης Ρέμος) _1"," Δεν τελειώνει έτσι η αγάπη(Τάμτα) _1"," Η αγάπη είναι θύελλα ( Bασίλης Καρράς & Πάολα) _3"," Να μ'αγαπάς , Σάκης Ρουβάς _3"," Πονάει η αγάπη ( Στέλιος Ρόκκος ) _3"," Αγάπη αχάριστη ( Γιώργος Τσαλίκης ) _1"," Χρήστος Δάντης- Πάει η αγάπη μου _3","'Εχω μια αγάπη (Πασχάλης Τερζής) _1");
createNewListOfQuestionsWithAnswers("10 Ομαδικά αθλήματα ?", " Αμερικανικό ποδόσφαιρο‎ _3", " Χειροσφαίριση‎ ﻿_5"," Χόκεϊ σε χόρτο‎ _1", " Χόκεϊ επί πάγου‎ _1",  "Υδατοσφαίριση‎ _3", " Ράγκμπι‎ _3", " Ποδόσφαιρο _1", "  Πετοσφαίριση‎ _5"," Μπέιζμπολ‎ _3"," Κωπηλασία‎ _3"," Κρίκετ‎ _3"," Καλλιτεχνικό πατινάζ‎ _5"," Καλαθοσφαίριση‎ - Μπάσκετ _1"," Συγχρονισμένη κολύμβηση _5","Ρυθμική γυμναστική _3","Διελκυστίνδα (δύο ομάδες, οι οποίες τραβούν ένα σχοινί) _5");
createNewListOfQuestionsWithAnswers("10 Ταινίες με Βαμπιρ  ?", " Dracula 2000 _3", " Queen of the Damned- Βασίλισσα των καταραμένων  ﻿_3"," Buffy the Vampire Slayer _3", " Van Helsing _1",  "Blade _1", " Underworld _1", " Frostbitten _5", " Vampyres _3", " Interview with the Vampire -Συνέντευξη με ενα βρικόλακα _3"," Ultraviolet _5"," Nosferatu _5"," Dracula _1"," Black Sunday _5"," Near Dark  _5"," Cronos _3","The Night Stalker  _5");
createNewListOfQuestionsWithAnswers("10 Ατομικα αθληματα ?", " Τοξοβολία‎ _1", " Σκάκι‎  ﻿_3", " Στίβος‎ _1",  " Σκοποβολή‎  _3", " Πολεμικές τέχνες _1", " Ορειβασία‎  _5", " Ξιφασκία‎  _3"," Ποδηλασία‎ _1"," Μηχανοκίνητος αθλητισμός‎ _5"," Κολύμβηση‎ _1"," Μπόουλινγκ‎  _1"," Μπιλιάρδο‎ _1"," Ιππασία‎  _3"," Επιτραπέζια αντισφαίριση‎ - Πινκ πονκ _1"," Γκολφ‎  _3"," Βελάκια‎  _5"," Άρση βαρών‎ _1");
createNewListOfQuestionsWithAnswers("10 Απο τις πόλεις που έγιναν Ολυμπιακοί αγώνες ?", " Μόναχο (1972) _3", "  Μόντρεαλ (1976) ﻿_5","  Μόσχα (1980) _3", " Λονδίνο (2012) _1",  " Αθήνα ( 2004 )_1", " Λος Άντζελες (1984) _1", " Σεούλ (1988) _3", " Βαρκελώνη (1992) _3", " Ατλάντα (1996) _3"," Σίδνεϋ (2000) _1"," Πεκίνο (2008) _1");
createNewListOfQuestionsWithAnswers("10 Έλληνες σεναριογράφοι ?", " Θόδωρος Αγγελόπουλος _1", " Έλενα Ακρίτα ﻿_3"," Παντελής Βούλγαρης _3", " Θανάσης Βαλτινός _3",  "Γιάννης Δαλιανίδης _1", " Ντίνος Δημόπουλος _3", " Κώστας Καραγιάννης _1", " Γιώργος Καπουτζίδης _1", " Αλέξανδρος Ρήγας _3"," Σταμάτης Τσαρουχάς _5"," Νίκος Φώσκολος _1"," Γιώργος Τζαβέλλας _5"," Γιάννης Σμαραγδής _3"," Αλέκος Σακελλάριος _1"," Χάρης Ρώμας _1","Ορέστης Λάσκος _5","Μιρέλλα Παπαοικονόμου _3","Νίκος Παναγιωτόπουλος  _3");
 createNewListOfQuestionsWithAnswers("10 Χώρες νησιά ?", " Αυστραλία _1", " Ινδονησία ﻿_3", " Κύπρος _1",  "Μαδαγασκάρη _3", " Ιαπωνία _1", " Νέα Ζηλανδία _1"," Ηνωμένο Βασίλειο _1"," Ισλανδία _3"," Αϊτή _3"," Φίτζι _3"," Μπαχάμες _1"," Τζαμάικα _1"," Νήσοι Φερόες _3"," Σεϋχέλλες _1"," Μπαρμπάντος _3"," Μαλδίβες _1","Νήσοι Κουκ _5");
createNewListOfQuestionsWithAnswers("10 Πράγματα που κάνεις για να ζεσταθείς ?", " Τριψιμο χεριων _3", " Φύσημα χεριών ﻿_3"," Φοράμε γάντια  _1", " Φοράμε γούνα  _1",  "Φοράμε παλτό _1", " Άνοιγμα καλοριφέρ _1", "  Άνοιγμα air condition _1", " Τρέξιμο-Γυμναστική _3", " Φοράμε κασκόλ _1"," Φοράμε σκούφι-σκούφο _3"," Σκεπαζόμαστε _3","Πλησίασμα χεριών σε αναπτήρα-φωτιά _5");
createNewListOfQuestionsWithAnswers("10 Τύποι προτύπων συμπίεσης (βιντεο, εικόνας και ήχου) ?",  " mpeg ﻿_3", " mjpeg _5", " jpeg _3", " gif _3"," avi _3"," mp3 _1"," mp4 _1","au _3","flv _3","rar _3","divx _3","wav _1","xvid _3");
createNewListOfQuestionsWithAnswers("10 Είδη καφέ ?", " Καππουτσίνο _1", " Εσπρέσσο φρέντο ﻿_1"," Εσπρέσσο _1",  "Μακκιάτο _5", " Latte _3", " Φίλτρου-Γαλικός _1", " Φροζίτο _3","Ελληνικός _1","Τούρκικος _1","Ντεκφεϊνέ _3","Στιγμιαίος - Φραπές _1","Irish Coffee _3");
createNewListOfQuestionsWithAnswers("10 Γνωστοί Browsers ?", " Google Chrome _1", " Mozilla Firefox	 ﻿_1"," Ιnternet Explorer _1", " Opera _1",  "Safari _3", " Maxthon _3", " SeaMonkey _5", " Deepnet Explorer _5"," Avant Browser _5"," Comodo Dragon  _5"," Chromium _5");
createNewListOfQuestionsWithAnswers("10 Μάρκες βότκας ?", " Cape North _3", " Absolut  _1",  "Beluga  _5", " Belvedere _3", " Smirnoff _1", " Zubrowka  _5", " Finlandia  _3"," Sobieski _3"," Wyborowa  _5"," Ursus  _1"," Serkova  _3");
createNewListOfQuestionsWithAnswers("10 Μάρκες ουίσκι ?", " John Walker _1",  " Dewar's  _1", " Chivas _1", " Cutty Sark _1", " Jameson  _3"," Jack Daniel's _3"," Famous Grouse-πέρδικα _3"," Cardhu _3","Ballantines  _3"," Dimple _3"," Haig  _3"," Black & White  _5");

createNewListOfQuestionsWithAnswers("10 Απο τους πιο ακριβοπληρωμένους ποδοσφαιριστες (το 2014)?", " Νειμάρ _1", " Λιονέλ Μέσι ﻿_1"," Κριστιάνο Ρονάλντο  _1", "  Νταβίδ Σίλβα _3",  " Σέρχιο Αγουέρο _1", "  Γουέιν Ρούνεϊ _1", "  Ζλάταν Ιμπραχίμοβιτς _1"," Φερνάντο Τόρες _3"," Γιάγια Τουρέ _3"," Ρανταμέλ Φαλκάο _3","  Γκάρεθ Μπέιλ _3"," Ρόμπι Φαν Πέρσι _3","  Φρανκ Ριμπερί _3"," Στίβεν Τζέραρντ _3","Φρανκ Λάμπαρντ _5"," Μπαστιάν Σβαϊστάιγκερ _5"," Λουίς Σουάρεζ _3" );
createNewListOfQuestionsWithAnswers("10 Ταινιες με θέμα τον β παγκόσμιο πόλεμο ?", " Ο Πιανίστας _1", " Η Γέφυρα του Ποταμού Κβάι ﻿_5"," Εχθρός προ των πυλών _3", " Αποκάλυψη _3",  "Αδωξοι Μπάσταρδη _1", " Η ζωή ειναι ωραία -la vida es bella _3", " Downfall _5", " The Great Escape - Η Μεγάλη Απόδραση  _5", " Η Λεπτή Κόκκινη Γραμμή _3"," Η Λίστα του Σίντλερ _1"," Το μαντολίνο του Λοχαγού Κορέλι _3"," Υπολοχαγός Νατάσσα _3"," Η δίκη της Νυρεμβέργης _5"," Hell and Back _5"," Τα κανόνια του Ναβαρόνε _3"," Γέφυρα πολύ μακριά-A Bridge Too Far _5","Βρώμικη Δωδεκάδα - The Dirty Dozen  _5");
createNewListOfQuestionsWithAnswers("10 Πράσινα λαχανικά ?", " Σπανάκι _3", " Μαρούλι ﻿_1", "  Σέλινο _1",  "Μαϊντανός _1", " Άνηθος _3", " Μάραθος _3", " Κρεμμύδι _1"," Πράσο _3","  Μπρόκολο _1"," Κουνουπίδι _1"," Αγκινάρα _3"," Φασόλια _1"," Κουκιά _5"," Αγγούρι _1"," Μπάμια _1"," Πιπεριά _1","Ρόκα _3");
createNewListOfQuestionsWithAnswers("10 Αντικείμενα συζητήσεων ( ανδρών ή γυναικών ) ?", " Κοινωνικά _1", " Αθλητικά ﻿_1"," Κουτσομπολιό _3", " Πολιτικά _1",  "Προγραμματισμός _3", " Gadget-Τεχνολογία _3", " Αντίθετο φύλο _1", " Πάρτυ _3", " Κρεπάλες _5"," Στρατός _3"," Θρησκείες-Θρησκευτικά _3"," Σειρες-Ταινίες _1"," Παιδικά - Καρτούν _5"," Μόδα _1"," Διακοπές _3","Sex _1");
createNewListOfQuestionsWithAnswers("10 Κόκκινα φρούτα ?", " Σταφύλι _1", " Βατόμουρο ﻿_3", " Δαμάσκηνο _3",  " Σύκο _3", " Φράουλα _1", " Κεράσι _1", " Μήλα _3"," Γκρέιπφρουτ _5"," Aχλάδι _1","Μούρα _1");
createNewListOfQuestionsWithAnswers("10 Σεξουαλικώς μεταδιδόμενο νοσήματα ?", " HIV-Eids _1", " Έρπης ﻿_3"," HPV _5", " Μαλακό έλκος _5",  "Χλαμύδια  _3", " Σύφιλη _3", " Μολυσματική τέρμινθος -  MCV _5", " Ψείρα καβούρι (καβούρια - ψείρες του εφηβαίου) _5", " Ψώρα  _3"," Τριχομονάδες _5"," Καντιντίαση _5","Γονόρροια _5");
createNewListOfQuestionsWithAnswers("10 Τηλεπαιχνίδια ?", " Ρουκ Ζουκ _3", " Μεγάλο Παζάρι ﻿_5"," Μπράβο Χάσατε _5", " Άνδρες έτοιμοι για Όλα _1",  "Βρές την φράση _3", " Ο πιο αδύναμος κρίκος _1", " Ο τροχός της τύχης _1", " Κόντρες _5", " Κόντρα Πλακέ _3"," Μέγκα Μπάνκα _5"," Τα τετράγωνα των αστέρων _5"," Ποιός θέλει να γίνει εκατομμυριούχος _1"," Τοις Μετρητοίς _3"," Money Drop _3"," Φάτους όλους _5"," Η εκδίκηση της ξανθιάς _3");
createNewListOfQuestionsWithAnswers("10 Πράγματα που ( μπορεί να ) κάνεις με το που ξυπνήσεις ?", " Είσοδο - Να μπω στο facebook _1", "  Είσοδο - Να μπω στο email _1"," Sex _3", " ( Πίνουμε ) Καφέ _1",  " Πρωινό _1", " Κλείνω ξυπνητήρι _3", " Ξανακοιμάμαι _3", " Πάω τουαλέτα _3", " Πάω δουλειά _3"," Διαβάζω-Διάβασμα _3"," Τσιγάρο _3"," Πλενω προσωπο - Πλυσιμο προσώπου _1"," (Κάνω) Μπάνιο _1");
createNewListOfQuestionsWithAnswers("10 Γνωστα πανεπιστήμια ?", " Stanford _1", " Massachusetts (Institute of Technology) ﻿_5"," Harvard _1", " Michigan _1",  "Oxford _1", " Yale  _3", " Columbia _5", " Pennsylvania _5", " (Imperial College of) London _3"," Johns Hopkins _5"," (University of) Tokyo _3"," Duke _3"," Cornell _5"," Dartmouth _3");
createNewListOfQuestionsWithAnswers("10 Νησιά-κράτη του ειρηνικού ωκεανού ?", " Παπούα -Νέα Γουινέα _3", " Νήσοι του Σολομώντα ﻿_5"," Βανουάτου _5", " Φίτζι _1",  "Πολυνησία  _3", " Χαβάη _1", " Παλάου _5", " Φιλιππίνες _3", " Νησί του Πάσχα _3"," Γκουάμ _5"," Μικρονησία _3");
createNewListOfQuestionsWithAnswers("10 Θρησκείες ?", " Δωδεκαθεϊσμός _3", " Σατανισμός ﻿_3"," Σαηεντολογία _3", " Χριστιανισμός _1",  "Ισλαμισμός _1", " Ινδουϊσμός _3", " Ιουδαϊσμός _1", " Βουδισμός _1", " Κομφουκιανισμός _5"," Σιντοϊσμός _5"," Ταοϊσμός _5"," Αζατρού _5"," Νεοπαγανισμός _5");
createNewListOfQuestionsWithAnswers("10 Απο τις πολυπληθεστερες πολεις στον κοσμο ?", " Τόκιο(Ιαπωνία, 32,5 εκατομμύρια ) _1", " Σεούλ ( Νότια Κορές, 20,6 εκατομύρια) ﻿_3"," Πόλη του Μεξικού ( Μεξικό, 20,5 εκατομύρια) _3", " Νέα Υόρκη (ΗΠΑ, 19,8 εκατομύρια) _1",  "Βομβάη (Ινδία, 19,2 εκατομύρια) _5", " Τζακάρτα ( Ινδονησία, 18,9 εκατομύρια ) _5", " Σάο Πάολο  ( Βραζιλία, 18,9 εκατομύρια) _3", " Νέο Δελχί (Ινδία, 18,7 εκατομύρια) _3", " Οσάκα ( Ιαπωνία 17,4 εκατομύρια) _5"," Σαγκάη ( Κίνα, 16,7 εκατομύρια ) _5"," Λος Άντζελες ( ΗΠΑ, 15,3 εκατομύρια ) _1"," Μόσχα ( Ρωσία, 15 εκατομύρια) _1"," Κάιρο (Αίγυπτος,  14,5 εκατομύρια) _3"," Μπουένος Άιρες ( Αργεντινή, 13,2 εκατομύρια ) _3"," Λονδίνο (Μεγάλη Βρετανία, 12,9 εκατομύρια) _1","  Πεκίνο ( Κίνα, 12,5 εκατομύρια ) _1"," Ρίο Ντε Τζανέιρο ( Βραζιλία, 10,6 εκατομμύρια ) _1"," Τεχεράνη ( Ιράν, 7,4 εκατομμύρια ) _5"," Λίμα ( Περού, 7,4 εκατομμύρια ) _3"," Κωνσταντινούπολη ( Τουρκία, 9,4 εκατομμύρια ) _3"," Παρίσι ( Γαλλία, 9,6 εκατομμύρια ) _1"," Τορόντο ( Καναδάς, 5 εκατομμύρια ) _3");
createNewListOfQuestionsWithAnswers("10 Ανδρικά περιοδικά ?", " Men's Health _3"," Max _1", " Nitro _1",  "DownTown _3", " Playboy _1", " Penhouse _3", " Money & Life _5"," Focus _5"," lifestyle _3"," Men _1"," Status _1"," Maxim _3");
createNewListOfQuestionsWithAnswers("10 Γυναικεία περιοδικά ?", " Glamour _1", " Celebrity ﻿_3"," Cosmopolitan _1", " Elle _5", " Super Κατερίνα _1", " Γυναίκα _3"," Marie Claire _1"," Madame Figaro _1"," Look _5"," Mirror _1");
createNewListOfQuestionsWithAnswers("10 Διάσημοι Rapper ?", " 50 Cent _1", " Dr. Dre ﻿_3"," Kanye West _3", " Snoop Dogg _1",  "Eminem _1", " Jay-Z _1", " Nas _3", " The Notorious B.I.G. _5", " 2Pac _1"," Ice Cube _5"," LL Cool J _3"," Scarface _3"," Eazy-E _5"," Redman _5"," Raekwon _5"," Kurupt _5"," Lil Wayne _3","Rakim _3");
createNewListOfQuestionsWithAnswers("10 Είδη μουσικής ?", "House _1"," Μπλουζ‎ _3", " Ροκ ﻿_1"," Ραπ _1", " Έθνικ _1",  "Τζάζ _1", " Μέταλ _1", " Πάνκ _3", " Λαϊκή _1", " Trance _1"," Ρέγκε _3"," Ρεγκετόν _3"," Χιπ χοπ _1"," Τριπ χοπ _3"," Κάντρι _5"," Dubstep _1"," Low Bap _3"," Disco _3"," Beatboxing _5");
createNewListOfQuestionsWithAnswers("10 Τραγούδια του Κιάμου ?", " Μόνιμα ερωτευμένος _3", " Απο δευτέρα ﻿_1"," Γύρνα σε εμένα _3", " Ολοκαίνουριος _1",  "Χαρτορίχτρα _1", "Απόψε φόρα τα καλά σου  _1", " Αρκετά _3", " Άσε με μια νύχτα μόνο  _3", " Έχω πονέσει γι' αυτή  _1"," Σαν ταινία παλιά _1"," Είσαι παντού _3"," Ένα μαγαζί απόψε _3"," Φωτιά με φωτιά _1"," Κακιά Συνήθεια _5"," Κάτσε και μέτρα _3"," Μου 'λειψες _5"," Προσεχώς _3"," Σφύριξα κι έληξες _1"," Τρελός για σένα _3"," Βρες λίγο χρόνο _1","Στη φωτιά το χέρι μου _1");
createNewListOfQuestionsWithAnswers("10 Συνθέτες και τραγουδιστές του ρεμπέτικου  ?", " Βαμβακάρης _3", " Τσιτσάνης ﻿_1"," Περιστέρης _5", " Χατζηχρήστος _5",  "Τσαουσάκης _1", " Μαρινέλλα _3", " Σωτηρία Μπέλλου _3", " Καζαντζίδης _1", " Παπαϊωάννου _5"," Γαβαλάς _3"," Γκόγκος-Μπαγιαντέρας _5","Ρόζα Εσκενάζυ _5","Μαρίκα Νίνου _3");
createNewListOfQuestionsWithAnswers("10 Χαρακτηρες απο το παρα 5 ?", " Ντάλια _1", " Ζουμπουλία ﻿_1"," Σπύρος _1", " Φώτης _1",  "Αγγέλα _3", " Άρης Παυρινός _5", " Ελευθερία Ασλάνογλου _5", " Ντίνα _5", " Σοφία Μπαξεβάνη _3"," Θεοπούλα  _1"," Θωμάς Βουλινός _3"," Φρίντα Παπαπαρασκευά _5"," Μαριλένα Δορκοφίκη _5"," Μάρθα-Ρίτσα _5"," Ανδρέας Καλογήρου _3");
createNewListOfQuestionsWithAnswers("10 Προέδροι των ΗΠΑ ?", " Τζορτζ Ουάσινγκτον _3", " Τόμας Τζέφερσον ﻿_3"," Αβραάμ Λίνκολν _1", " Θεόδωρος Ρούζβελτ _5",  "Φραγκλίνος Ρούζβελτ _3", " Τζον Κένεντι _1", " Ρίτσαρντ Νίξον _3", " Τζίμι Κάρτερ _5", " Τζορτζ Μπους _3"," Τζορτζ Μπους junior _1"," Μπαράκ Ομπάμα _1","Μπιλ Κλίντον _1");
createNewListOfQuestionsWithAnswers("10 Απο τα μεγαλύτερα ποτάμια του κόσμου ?", " Νείλος _1", " Αμαζόνιος ﻿_1"," Μισισιπής _3",  "Κόνγκος _3", " Νίγηρας _1", " Βόλγας _3"," Δούναβης _1"," Γενισέι _5"," Κίτρινος ποταμός _5"," Μαύρος ποταμός _5"," Παρανά _5");
createNewListOfQuestionsWithAnswers("10 Απο τα μεγαλύτερα ποτάμια της Ελλάδας ?", " Αχελώος _1", " Έβρος ﻿_1"," Πηνειός _3", " Νέστος _3",  "Στρυμόνας _3", " Θύαμις _5", " Άραχθος _5", " Ευρώτας _3", " Αλφειός _3"," Αλιάκμονας _3"," Αξιός _5");
createNewListOfQuestionsWithAnswers("10 Πράγματα που ( μπορεί να ) κάνεις πριν κοιμηθείς ?", " Προσευχή _3", " Πλύσιμο-Βουρτσισμα δοντιών ﻿_1"," Μπάνιο _1", " Κλείσιμο-log out facebool _3",  "Βάζω ταινία στο PC _5", " Τρώω _3", " Χασμουριέμαι _3", " Διάβασμα-Διαβάζω _3", " Παίζω με το κινητό _3"," Κλείσιμο PC _3"," Αλαγή μαξιλαροθήκης-Σεντονιού _5"," Βάζω ξυπνητίρι _5"," Φοράω-Βάζω πιτζάμες _1"," Σεξ _3"," Λέω καληνύχτα-Καληνυχτίζω _3");
createNewListOfQuestionsWithAnswers("10 Ορολογίες στο Basket ?", " Assist _1", " Layout ﻿_3"," Τρίποντο _3", " Διποντο _1",  "Air ball _3", " Ελεύθερες βολες _3", " Τεχνική ποινή _3", " Σούτ _5", " Time out _3"," Φάουλ _1"," Αντιαθλητικό φάουλ _3","Κάρφωμα _1","Pick and roll _5","Τάπα _1","Κλέψιμο _1");
createNewListOfQuestionsWithAnswers("10 Φαγητά με μακαρόνια ?", " Μακαρονάδα Φουρνου _3", " Σουφλέ ﻿_3"," Καρμπονάρα  _1", " Σαλάτα-Μακαρονοσαλάτα  _3",  "Μακαρόνια με κιμά _1", " Γαριδομακαρονάδα _5", " Παστίτσιο _1", " Τονομακαροναδα _3", " Αλ Όλιο _5","Αστακομακαρονάδα _3","Κοτομακαροναδα _3");
createNewListOfQuestionsWithAnswers("10 Είδη ζυμαρικών ?", " Τορτελίνια _3", " Ravioli ﻿_5"," Κανελόνια _3", " Λαζάνια _1",  "Ταλιατέλες _3", " Rigatoni _3", " Πένες _1", " Spaghetti _1"," Βίδες _3"," Farfalle-Φιογκάκια _3","Linguine _5","Nudles _3","Ρίζι _1"," Κριθαράκι _1","Κανελόνια _3","Χυλοπήτες _3");
createNewListOfQuestionsWithAnswers("10 Απο τα ακριβότερα συμβόλαια στο ΝΒΑ (στην ιστορια του NBA) ?", " Kobe Bryant (136.400.000 δολάρια για 7 χρόνια) _1", " Jermaine O' Neal (126.558.000 δολάρια για 7 χρόνια) ﻿_3"," Chris Webber (122.718.750 δολάρια για 7 χρόνια) _3", " Tim Duncan (122.007.704 δολάρια για 7 χρόνια) _1",  "Kevin Garnett (121.000.000 δολάρια για 6 χρόνια) _5", " Shaquille O' Neal (121.000.000 δολάρια για 7 χρόνια) _1", " LeBron James (109.837.500 δολάρια για 6 χρόνια) _3", " Chris Bosh (109.837.500 δολάρια για 6 χρόνια) _5", " Gilbert Arenas (111.000.000 δολάρια για 6 χρόνια) _3"," Rashard Lewis (118.000.000 δολάρια για 6 χρόνια) _5","  Joe Johnson (119.000.000 δολάρια για 6 χρόνια) _1","Juwan Howard 7 χρόνια 105.000.000 _5","Shawn Kemp 7 χρόνια 107.000.000 _5");
createNewListOfQuestionsWithAnswers("10 Πολεμικές τέχνες ?", " Κουνγκ Φου, _1", " Καράτε ﻿_1"," Ζίου Ζίτσου _3", " Τζούντο _1",  "Μάι Τάι _3", " Τάε Κβο Ντο _1", " Καποέιρα _3", " Πανκράτιον _3", " Κράβ Μάγκα _3"," Τσαπ Κουν Ντο _5"," Kick boxing _1"," Νινζούτσου _5"," Ντότζο _5"," Χαπκίντο _5"," Αϊκίντο _5"," Πέντσιχ Σιλάτ _5");
createNewListOfQuestionsWithAnswers("10 Διάσημοι πίκανες ζωγραφικής ?", " Μονα λιζα _1", " Guernica ﻿_3"," Η επιμονή της μνήμης _3", " Η Κραυγή _3",  "Έναστρη Νύχτα  _5", " Ηλίανθοι _3", " Δημιουργία του Αδάμ _1", " Η Γέννηση της Αφροδίτης _3", " Κορίτσι με το περιστέρι _5"," Το κορίτσι με το μαργαριταρένιο σκουλαρίκι _5"," Το κρυφό σχολειό _5","Μυσικός δείπνος _3","Ο Άνθρωπος του Βιτρούβιου _5","Ο Χριστός του Αγίου Ιωάννη του Σταυρού _5","Μαρία η Μαγδαληνή _5");
createNewListOfQuestionsWithAnswers("10 Έλληνες συνθέτες ?", " Μίκης Θεοδωράκης _1", " Δημήτρης Λάγιος ﻿_5"," Χρήστος Λεοντής _3", " Γιάννης Μαρκόπουλος _1",  "Δήμος Μούτσης _3", " Σταύρος Ξαρχάκος _1", " Γιάννης Σπανός _1", " Γιώργος Κατσαρός _5", " Μάριος Τόκας _3"," Νίκος Καρβέλας _3"," Σταύρος Κουγιουμτζής _5"," Απόστολος Καλδάρας _5"," Δημήτρης Παπαδημητρίου _3"," Φοίβος _1"," Βαγγέλης Παπαθανασίου _5");
createNewListOfQuestionsWithAnswers("10 Μπαχαρικά ?", " Κίμινο _3", " Πιπέρι ﻿_1"," Πάπρικα _3", " Ρίγανη _1",  "Γλυκόριζα _5", " Κρόκος Κοζάνης _5", " Γαρύφαλλο _3", " Κανέλα _1", " Πιπερόριζα - τζίντζερ _5"," Μαχλέπι _5"," Μοσχοκάρυδο _3"," Μαραθόσπορο-Γλυκάνισος _3"," Βανίλια _1"," Κάρυ _3"," Δυόσμος _3"," Δάφνη _3"," Θυμάρι _3"," Μπούκοβο _5");
createNewListOfQuestionsWithAnswers("10 Πράγματα που μπορείς να βάλεις σε μια αλμυρή κρέπα-τόστ ?", " Τυρί _1", " Ζαμπόν ﻿_1"," Γαλοπούλα _1", " Κοτόπουλο _3",  "Μπιφτέκι _3", " Μαρούλι _1", " Ντομάτα _1", " Ελιά-Πάστα ελιάς _3", " Σός -Κέτσαπ - Μουστάρδα _1"," Ζαμπονοσαλάτα _3"," Τυροσαλάτα _5"," Μπέικον _1"," Αυγά _3"," Φέτα-Μανούρι _3"," Nuggets _1"," Τόνο _1"," Σαλάμι αέρος _3"," Λουκάνικο _1"," Πιπεριά _1"," Ντομάτα Λιαστή _5"," Πατατάκια  _3"," Μανιτάρια _3");
createNewListOfQuestionsWithAnswers("10 Απο τα πιο δημοφιλή Ελληνικά αθλητικά σαιτ ?", " gazzetta.gr _1", " sport-fm.gr ﻿_3"," contra.gr _1", " sportdog.gr _1",  "onsports.gr _3", " sentragoal.gr _3", " sport.gr _3", " balleto.gr _5", " novasports.gr _3"," pamesports.gr _5"," xxsports.gr _5"," katimagiko.gr _5"," metrosport.gr _5"," athlitikanea.gr _3"," goalpost.gr _5"," woop.gr _5");
createNewListOfQuestionsWithAnswers("10 Γλυκά ζαχαροπλαστείου με σοκολάτα ?", "Μousse _1", " Mille - feuille σοκολάτας ﻿_3"," Τούρτα (σοκολάτας) _3", " Κορμός σοκολάτας _3",  "Κοκ _1", " Καριόκες _3", " Βραχάκια _5", " Βrownie _3"," Cheesecake (σοκολάτας)   _3"," Μuffins (σοκολάτας)  _3"," Παγωτό σοκολάτα _1"," Τρουφάκια _3"," Κέικ (σοκολάτας) _1"," Σουφλέ σοκολάτας _1"," Εκλέρ _5"," Προφιτερόλ _1");
createNewListOfQuestionsWithAnswers("10 Γλυκά ταψιού ?", " Ραβανί _3", " Εκμέκ ﻿_1"," Γαλακτομπούρεκο _1", " Κανταϊφι _1",  "Μπακλαβάς _1", " Γαλατόπητα _5", " Μπουρεκάκια _3", " Μπουγάτσα _3", " Πορτοκαλόπιτα _5"," Γιαουρτοπιτα _5"," Καρυδόπιτα _3"," Σάμαλι _5"," Φλογερες _3"," Σουλτάνα _5"," Τρίγωνα _3");
createNewListOfQuestionsWithAnswers("10 Υλικά για ζαχαροπλαστική ?", " Αλεύρι _1", " Κακάο ﻿_1"," Μαργαρίνη _3", " Ζάχαρη _1",  "Αυγά _1", " Γάλα _1", " Βανίλια _1", " Σόδα _3", " Αμμωνία _5"," Μπέικιν Πάουντερ _5"," Βούτυρο _1"," Σιμιγδάλη _5"," Καρύδα _3"," Κονιάκ _5"," Ξύσμα πορτοκάλι _3"," Σοκολάτα _1"," Αλάτι _1");
createNewListOfQuestionsWithAnswers("10 Έλληνες Ολυμπιονίκες ?", " Βούλα πατουλίδου _3", " Δημοσθένης Ταμπάκος ﻿_3"," Χρυσοπήγη Δεβετζή _3", " Φανή Χαλκιά _1",  " Πύρρος Δήμας _1", " Κατερίνα Θάνου _1", " Νίκος Κακλαμανάκης _1", " Σπύρος Λούης _5", " Κώστας Κεντέρης _1"," Μιρέλα Μανιάνη _3"," Μιχάλης Μουρούτσος _5"," Μπάμπης Χολήδης _5"," Κάχι Καχιασβίλι _1"," Λεονίδας Σαμπάνης _3"," Σοφία Μπεκατώρου _3"," Αλέξανδρος Νικολαϊδης _3","Κωνσταντίνος Β΄ _5","Ηλίας Ηλιάδης _3","Νίκη Μπακογιάννη _3","Αθανασία Τσουμελέκα _5");
createNewListOfQuestionsWithAnswers("10 Έλληνες ποιητές ?", " Αριστοτέλης Βαλαωρίτης _3", " Κώστας Βάρναλης ﻿_5"," Γεώργιος Βιζυηνός _3", " Νικιφόρος Βερτάκος _3",  "Οδυσέας Ελύτης _1", " Μάνος Ελευθερίου _5", " Κωνσταντίνος Καβάφης _1", " Νίκος Καββαδίας _3", " Γαλάτεια Καζαντζάκη  _5"," Νίκος Καζαντζάκης _1"," Βιτσέντζος Κορνάρος _3","Ρήγας Φερραίος _1","Κώστας Καρρυωτάκης _3","Γεράσιμος Μαρκοράς _5","Γιάννης Ρίτσος _3","Κωστής Παλαμάς _1");
createNewListOfQuestionsWithAnswers("10 Απο τις μεγαλύτερες οροσειρές της Γης είναι ?", " Ιμαλάια _1", " Άλπεις ﻿_1"," Άτλαντας _3", " Άνδεις _3",  "Καύκασος _3", " Κιλιμάντζαρο _3", " Βραχώδη όρη _1", " Αυστραλιανές Άλπεις _5", " Της Σουμάτρας _5"," Τιεν σαν _5"," Αλτάι _5");
createNewListOfQuestionsWithAnswers("10 Απο τους βαθμούς στο Πεζικό  ?", " Στρατιότης _1", " Υποδεκανέας ﻿_3"," Δεκανέας _1", " Λοχίας _1",  "Επιλοχίας _3", " Αρχιλοχίας _1", " Λοχαγός _1", " Ανθυπασπιστής _3", " Ανθυπολοχαγός _3"," Λοχαγός _1"," Ταγματάρχης _1"," Αντισυνταγματάρχης _3"," Συνταγματάρχης _1"," Ταξίαρχος _1"," Αντιστράτηγος _1"," Υποστράτηγος _1","Στρατηγός _1");
createNewListOfQuestionsWithAnswers("10 Πράγματα που φοράμε-βάζουμε για προστασία ?", " Προφυλακτικά _3", " Γάντια ﻿_1"," Μάσκα _3", " Κασκόλ _1",  "Σκούφο-Σκουφάκι _1", " Καπέλο _1", " Αντηλιακό _1", " Φυλαχτό _5", " Κράνος _1"," Ζωνη (αυτοκινητου) _1"," Ζώνη αγνότητας _5");
createNewListOfQuestionsWithAnswers("10 Απο τους βαθμούς στο Ναυτικό ?", " Ναύτης _1", " Υποδίοπος ﻿_3"," Δίοπος _3", " Κελευστής _1",  "Επικελευστής _3", " Αρχικελευστής _1", " Ανθυπασπιστής _1", " Σημαιοφόρος _3", " Ανθυποπλοίαρχος _3"," Υποπλοίαρχος _1"," Πλωτάρχης _5"," Αντιπλοίαρχος _3"," Πλοίαρχος _1"," Αρχιπλοίαρχος _3"," Υποναύαρχος _3"," Αντιναύαρχος _1"," Ναύαρχος _1");
createNewListOfQuestionsWithAnswers("10 Απο τους βαθμούς στην Αεροπορία ?", " Σμηνίτης _1", " Ανθυποσμηνίας ﻿_5"," Υποσμηνίας _3", " Σμηνίας _1",  "Επισμηνίας _1", " Αρχισμηνίας _1", " Ανθυπασπιστής _1", " Ανθυποσμηναγός _3", " Υποσμηναγός _1"," Σμηναγός _1"," Επισμηναγός _3", " Αντισμήναρχος _3", " Σμήναρχος _1", " Ταξίαρχος _1"," Υποπτέραρχος _1"," Αντιπτέραρχος _1"," Πτέραρχος _1");
//createNewListOfQuestionsWithAnswers("10 αντικείμενα που περιέχουν ηχείο-ηχεία ?", " Κινητό τηλέφωνο _1"," Τηλεόραση-TV _1", " Ραδιόφωνο _1",  "PC-Υπολογιστής _1", " Walkman-MP3 Player _3", " Ξυπνητήρι _3", "Σταθερο τηλέφωνο _3", " Αμάξι _3"," Μηχανή _5"," Φορητές παιχνιδομηχανές-Φορητές κονσόλες _5");
createNewListOfQuestionsWithAnswers("10 Γνωστά disco συγκροτήματα-καλλιτέχνες  ?", " The Pointer Sisters _5", " The Gang ﻿_5"," Village People _3", " Chic _3",  "Boney M _1", " Bee Gees  _3", " Abba _1", " Alphaville _3", " Diana Ross  _1"," Michael Jackson _1"," Donna Summer _3"," James Brown  _3","  Gloria Gaynor _1"," Weather Girls _5"," Blondie _3");
//////////

createNewListOfQuestionsWithAnswers("10 Υπερήρωες?", " Spiderman _1", " Thor ﻿_3"," Iron Man  _3", " Flash _3",  "Captain America _1", " Wonder Woman _3", " Catwoman _1", " Hulk _3", " The Green Lantern _5"," Wolverine _3"," Aqua Man _5");
createNewListOfQuestionsWithAnswers("10 Ναρκωτικά ?", " Ινδική κάνναβις-Χόρτο _1", " 'Εκσταση-Mdma ﻿_1"," Κοκαΐνη _1", " Κράκ _3",  "Μεθαμφεταμίνη -speed _3", " Ηρωίνη _1", " Rohypnol _5", " GHB _5", " LSD _1"," Khat _5"," crystal meth _3","Αμφεταμίνη _3");
createNewListOfQuestionsWithAnswers("10 Πρωτεύουσες της Ευρώπης απο Β ?", " Βέρνη _1", " Βαλέτα ﻿_3"," Βαντούζ _5", " Βαρσοβία _3",  "Βελιγράδι _1", " Βερολίνο _1", " Βιέννη _1", " Βίλνιους _5", " Βουδαπέστη _3"," Βουκουρέστι _3"," Βρυξέλλες _1");
createNewListOfQuestionsWithAnswers("10 Σήματα οδήγησης  ?", " στοπ _1", " Σήραγγα ﻿_5"," Κυκλικός κόμβος _3", " Φανάρι _1",  "Διέλευση ζώων _5", "Κινήσεως παιδιών _3", "Διάβασης πεζών _1", " Ολισθηρό οδόστρωμα _3", " Επικίνδυνη στένωση _5"," Επικίνδυνη στροφή _3"," Μονόδρομος-Υποχρεωτική κατεύθυνση _1"," Ανώτατο όριο _1","Απαγορεύεται η στάθμευση  _3","Απαγορεύεται η στροφή _1","Απαγορεύεται η είσοδος _1","Παραχώρηση προτεραιότητας _3");
createNewListOfQuestionsWithAnswers("10 Ζωδια ?", " Κριός _1", " Ταύρος ﻿_1"," Δίδυμοι _3", " Καρκίνος _1",  " Λέων _1", " Ζυγός _3", " Σκορπιός _1", " Τοξότης _1", " Αιγόκερως _3"," Υδροχόος _3"," Ιχθύεις _1"," Παρθενός _1");
createNewListOfQuestionsWithAnswers("10 Πληγές του φαραώ ?", " Τα νερά (του Νείλου) έγιναν αίμα _1", " Βάτραχοι ﻿_3"," Σκνίπες (Ο αέρας γέμισε) _3", "Αλογόμυγες (σε κάθε κατοικία) _5",  "Σκοτώθηκαν όλοι οι πρωτότοκοι γιοι  _3", " 3 ημέρες πυκνό σκοτάδι _3", " Ακρίδες _3", "Χαλάζι και φωτιά _3", " Άνθρωποι και ζώα γέμισαν εξανθήματα, _5"," Επιδημία έπληξε όλα τα κοπάδια _5");
createNewListOfQuestionsWithAnswers("10 Δημητριακα ?", " Cheerios _1", " Froot Loops ﻿_5"," Frosted Flakes _3", " Corn Flakes _1",  "Quakers _1", " Weetabix _5", " All-Bran _3", " Fitness _1", " Musli _3"," Cookie Crisp _3"," Special K _3");
createNewListOfQuestionsWithAnswers("10 Τροποι μαγειρέματος  ?", " Φούρνος _1", " Σχάρα ﻿_1"," Τηγάνι _1", " Φούρνο μικροκυμάτων _5",  "Χύτρα _1", " Ατμό _3", " Σοτάρισμα _5", " Σούβλα _3", " Wok _3"," Grill _3"," Robot _5");
createNewListOfQuestionsWithAnswers("10 Λέξεις για το χασις ?", " Μπάφος _1", " Φούντα ﻿_1"," Μπόνγκ _5", " Ινδική κάνναβη _1",  "Ολλανδία _1", " Ρέγκε _3", " Νος _3", " Ναρκωτικό _1", " Κέικ _3"," Στριφτό _3"," Τζιβάνα _3");
createNewListOfQuestionsWithAnswers("10 Στάσεις στο sex ?", " Doggy Style- Στα 4 _1", " 69 ﻿_1"," Στα όρθια _3", " Καθιστό-καρεκλάτο _3",  "Γυναίκα από πάνω _1", " Άντρας από πάνω _3", " Ιεραποστολική _3", " Στοματικό σεξ _1", " Πρωκτικό σεξ _3"," Του κουταλιού _5"," Κρεμαστή  _5");
createNewListOfQuestionsWithAnswers("10 Λέξεις για τους 300 ?", " Μάχη των Θερμοπυλών _3", " Πέρσες ﻿_1"," Σπαρτιάτες _1", " Λεωνίδας _1",  "Ξέρξης _3", " Εφιάλτης _3", " Θεσπιείς _5", " Αθάνατοι _5", " Ναυμαχία της Σαλαμίνας _5"," Μολών λαβέ _5" );
createNewListOfQuestionsWithAnswers("10 Τραγούδια του Σάκη Ρουβά ?", " Μην Aντιστέκεσαι _3", " Αίμα, Δάκρυα & Ιδρώτας ﻿_3"," Χίλια μίλια _1", " Να μ' αγαπάς _3",  "Σ'έχω ερωτευθεί _1", " Τώρα αρχίζουν τα δύσκολα _3", " Σπάσε το χρόνο _3", " Όλα γύρω σου γυρίζουν _1", " Θέλεις ή δεν θέλεις _3"," Για φαντάσου _3"," Σε θέλω σαν τρελός _3"," Cairo _1"," Δεν έχει σίδερα η καρδιά σου _3"," Υπάρχει Aγάπη Eδώ  _3"," Χρονια πολλά _1");
createNewListOfQuestionsWithAnswers("10 Πρόσοπα και θέματα ανέκδοτων ?", " Τοτός _1", " Μικρή Αννούλα ﻿_1"," Jack Norris _3", " Πόντιος _1",  "Γιορίκας-Κοστίκας _3", " Ξανθιά _1", " Ζώα _1", " Μπόμπος _5", " Πεθερά _3"," Ζουλού _5"," Ελληνας _1"," Θρησκευτικά _3"," Τριανταφυλλοπουλος _5","Τάσος Λειβαδίτης _5");
createNewListOfQuestionsWithAnswers("10 Αηδιαστικά πράγματα που κάνουμε ?", "Ξύσιμο-Σκάλισμα μύτης _3", " Κλανιά ﻿_1"," Ρέψιμο _3", "Φάγωμα νυχιών _1",  "Αυνανισμός _3", " Καθαρισμός δόντιών με νύχια _5", " Γλείψιμο δάχτυλων _3", " Φτέρνισμα _1", " Σκούπισμα χεριών στα ρούχα _3"," Μαδημα τριχών _5"," Να κάνει θόρυβο όταν μασάει _3");
createNewListOfQuestionsWithAnswers("10 Εντομα που σιχαινόμαστε ?", " Κατσαρίδα _1", " Καμπανός ﻿_1"," Τζιτζίκι _3", " Ποντίκι _1",  "Βρομούσα _5", " Σαρανταποδαρούσα _3", " Αράχνη _1", " Σφήκα _3", " Τσιμπούρι _5"," Ψιλός _3"," Σκαθάρι _3");
//createNewListOfQuestionsWithAnswers("10 προιόντα που καλλιεργούντε σε ένα θερμοκήπιο ?", " Ντομάτα _1", " Αγκούρι ﻿_1"," Πιπεριά _1", " Τριαντάφυλλο _5",  "Φράουλα _1", " Πεπόνι _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
createNewListOfQuestionsWithAnswers("10 Ηobbies ?", " Ποδόσφαιρο _1", " Μπάσκετ ﻿_1"," Ψάρεμα _3", " Γκόλφ _1",  "Ηλεκτονικά παιχνίδια _3", " Επιτραπέζια παιχνίδια _3", " Σκι _5", " Τένις _3", " Skateboard _3"," Πινκ πονκ _3"," Κολύμβηση _3");
createNewListOfQuestionsWithAnswers("10 Γνωστές πίτσες ?", " Μαργαρίτα _1", " Σπέσιαλ ﻿_1"," Family _3", " Ναπολιτάνα _1",  "4 τυριά _3", " Μεξικάνα _3", " Βεζούβιος _1", " Του αθλητή _5", " Vegeteria _3"," Ζαμπόν τυρι _5"," Γκουστόζα _5"," Γαλοπούλα _1");
createNewListOfQuestionsWithAnswers("10 Αρχαίοι πολιτισμοί?", " Βίκινγκς _3", " Αρχαία Ελλάδα ﻿_1"," Αζτέκοι _3", " Μασάι _1",  "Βαβυλώνα _1", " Αρχαία Ινδία _3", " Αρχαία Κίνα _3", " Αρχαία Αίγυπτος _5", " Ρώμη _3"," Πέρσες _3"," Μάγια _1"," Αρχαία Τουρκία _1"," Ατλαντίδα _3");
createNewListOfQuestionsWithAnswers("10 Άνδρες sex symbols ?", " Μάρλον Μπράντο _1", " Μπράντ Πιτ ﻿_1"," Ρίκι Μάρτιν _3", " Αντόνιο Μπαντέρας _3",  "Τομ Κρούζ _1", " Μικι Ρουρ _3", " Τζόνι Ντεπ _1", " Charlie Sean _5", " Leonardo DiCaprio _3"," Javier Bardem _5"," Αλέν Ντελόν _1"," Ryan Gosling _3"," Κριστιάνο Ρονάλντο _1"," Ντειβιντ Μπέκαμ _1"," Johnny Depp _1"," George Clooney _1"," James Dean _5");
createNewListOfQuestionsWithAnswers("10 Γυναίκες sex symbols ?", " Μέρλιν Μονρόε _1", " Σάρον Στόουν ﻿_1"," Kim Basinger _3", " Πάμελα Άντερσον _1",  "Britney Spears _3", " Jessica Alba _3", " Demi Moore _3", "  Halle Berry _3", " Brigitte Bardot _3"," Elizabeth Taylor _3"," Rita Hayworth _5"," Ursula Andress _5"," Monica Bellucci _1"," Angelina Jolie _1"," Kelly Brook _3"," Megan Fox _1"," Mila Kunis _1");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Α' ?", " Αγρονόμος _5", " Αισθητικός ﻿_1"," Ανθοκόμος _3", " Ανθοπώλης _5",  "Αστυφύλακας-Αστυνόμικός _1", " Αρχιτέκτονας _1", " Αξιωματικός Στρατού _3", " Αγρότης _1", " Αεροσυνοδός _3"," Αθλητής _5"," Ακροβάτης _1", " Αναισθησιολόγος _3"," Ανακριτής _5"," Αρχαιολόγος _3"," Αστροναύτης _1");
createNewListOfQuestionsWithAnswers("Τα 10 εξυπνότερα ζώα στον κόσμο ?", " Αράχνη _3", " Αρουραίος ﻿_5"," Kοράκι _3", " Σκύλος _1",  "Χταπόδι _3", " Παπαγάλος _3", " Ελέφαντας _1", " Μαϊμού _1", " Δελφίνι _1"," Χιμπατζής _1");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Δ' ?", " Δάσκαλος _1", " Δασοφύλακας ﻿_3"," Δημοτικός σύμβουλος _3", " Διπλωμάτης _3",  "Δύτης _3", " Δικηγόρος _3", " Δικαστής _1", " Διερμηνέας _5", " Διαφημιστής _3"," Διαιτολόγος _5"," Δημοσιογράφος _1"," Δεσμοφύλακας _5");
createNewListOfQuestionsWithAnswers("10 Eταιρείες ρούχων ?", " Gucci _1", " Ralph Lauren ﻿_3"," Hugo Boss _1", " Lacoste _3",  "Αrmani _3", " Calvin Klein _3", " Diesel _1", " Franklin Marshall _3", " Louis Vuitton _5"," Tommy Hilfiger _3"," Levis _1"," Burberry _5"," Dolce & Gabanna _1"," Holister _5"," Converse _1"," Versace _5"," Timberland _3"," Prada _1");
createNewListOfQuestionsWithAnswers("10 Πλανήτες & δορυφόροι ?", " Ερμής _1", " Αφροδίτη ﻿_1"," Γη _1", " Άρης _1",  "Δίας _1", " Κρόνος _1", " Ουρανός _1", " Ποσειδώνας _3"," Πλούτωνας _3"," Σελήνη _1");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Κ'  ?", " Καθαρίστρια _1", " Καλαθοσφαιριστής ﻿_5"," Καμαριέρα _3", " Καρδιολόγος _1",  "Καφετζής _3", " Κλειδαράς _1", " Κλητήρας _5", " Κλόουν _3", " Κοινωνιολόγος _3"," Κομπάρσος _5"," Κολυμβητής _3"," Κομμωτής-Κομμώτρια _1"," Κοσμηματοπόλης _1"," Κρεοπόλης _3"," Κριτικός τέχνης _5"," Κτηνίατρος _1"," Κτηνοτρόφος _3");
createNewListOfQuestionsWithAnswers("10 Εταιρείες laptop ?", " Asus _3", " Toshiba ﻿_3"," Acer _1", " Samsung _1",  " LG _1", " Apple _3", " Dell _1", " HP _1", " Gateway _5"," Lenovo _5"," Sony _1"," Turbo-x _3");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'M'?", " Μαθηματικός _1", " Μαιευτήρας ﻿_5"," Μακιγιέζ _3", " Μανάβης _1",  "Μάνατζερ _5", " Μελισσοκόμος _3", " Μέντιουμ _3", " Μεσίτης _3", " Μετεωρολόγος _3"," Μηχανικός _1"," Μικροβιολόγος _3","Μουσικός _1");
createNewListOfQuestionsWithAnswers("10 Αντικείμενα σε ένα κομμωτήριο ?", " Βούρτσα _1", " Αποστειρωτής ﻿_5"," Ψαλίδι _1", " Κουρευτική μηχανή _3",  "Ξυράφι _1", " Πινέλο _3", " Σεσουάρ _1", " Τοστιέρα _3", " Χτένα _3"," Καρέκλα _1"," Καθρεύτης _1"," Λουτήρας _3");
createNewListOfQuestionsWithAnswers("10 Sequel ταινίες ?", " The Godfather _1", " Terminator ﻿_3"," The Lord of the Rings _1", " Harry Potter _1",  "Saw _1", " Toy Story _3", " The Bourne _3", " Batman _1", " Spiderman _3"," Shrenk _5"," Rambo _1","Star Wars _3","Aliens _3","Superman _3");
createNewListOfQuestionsWithAnswers("10 Πράγματα που δεν μας αρέσουν πάνω μας ?", " Μύτη _1", " Στόμα ﻿_1"," Κοιλιά _1", " Γάμπες _3",  "Γοφοί _1", " Αστράγαλοι _5", " Αυτιά _3", " Μάτια _1", " Στήθος _3"," Φρύδια _3"," Βλεφαρίδες _1","Νύχια _3","Μαλιά _1","Μάγουλα _1","Πηγούνι _3");
//createNewListOfQuestionsWithAnswers("10 Προιοντα με ανθρακικο ?", " Κόκα κόλα _1", " Σπράιτ ﻿_3"," Φάντα _1", " Μπύρα _1",  "Σόδα _1", " Νερό _3", " Πέπσι _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Π' ?", " Παθολόγος _1", " Παιδαγωγός ﻿_3"," Παιδίατρος _1", " Παιδοχειρούργος _5",  "Παλαιοπώλης _3", " Παλαιστής _3", " Παρκαδόρος _5", " Περιπτεράς _1", " Ποδοσφαιριστής _1"," Προγραμματιστής _5"," Πιλότος _1"," Πλοίαρχος _3"," Πνευμονολόγος _5"," Ποδηλάτης _3"," Πολιτικός Μηχανικός _3"," Πορτιέρης _3"," Πυροσβέστης _1"," Πυροτεχνουργός _5"," Πυγμάχος _5");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Σ'  ?", " Σεισμολόγος _1", " Σεναριογράφος ﻿_1"," Σεξολόγος _3", " Σερβιτόρος _1",  "Σκηνοθέτης _1", " Σφουγγαράς _5", " Συνοριοφύλακας _5", " Συνθέτης _3", " Συμβολαιογράφος _3"," Στιχουργός _3"," Σταθμάρχης _3"," Σκιτσογράφος _3"," Σχεδιαστής _1"," Σχολιαστής _3"," Σύμβουλος _3");
createNewListOfQuestionsWithAnswers("10 Επαγγέλματα απο 'Τ'  ?", " Ταμίας _1", " Ταχυδακτυλουργός ﻿_5"," Τεχνικός _1", " Τηλεπαρουσιαστής _3",  "Τηλεφωνιτής _1", " Τραγουδιστής _3", " Ταξιθέτης _5", " Ταχυδρόμος _1", " Τενίστας _3"," Τουριστικός συνοδός _5"," Τορναδόρος _5");
createNewListOfQuestionsWithAnswers("10 Aλαντικά ?", " Γκουρμέτ _5", " Γαλοπούλα ﻿_5"," Πάριζα _1", " Μορταδέλα _3",  "Λούντζα _5", " Λουκάνικο _1", " Μπέικον _1", " Σαλάμι αέρος _3", " Ζαμπόν _1"," Παστουρμάς _3"," Μορταδέλα _1"," Ρολό κιμά _5"," Ρολό κοτόπουλο _3");
createNewListOfQuestionsWithAnswers("10 Talent show ?", " Ελλάδα έχεις ταλέντο _3", " X-factor ﻿_1"," The voice _1", " Super Idol _5",  "Dream Show _5", " Fame Story _3", " Greek Idol _3", " So You Think You Can Dance  _3", " Top Chef _3"," MasterChef _1");
createNewListOfQuestionsWithAnswers("10 Reality shows ?", " Bar _3", " Farm ﻿_3"," Big brother _1", " Survivor _1",  "Παταγωνία _3", " Αγρότης μόνος ψάχνει _5", " Mission _1", " Eurostar _5", " Fame Story _1"," Dream Show _1");
createNewListOfQuestionsWithAnswers("10 Eπαγγέλματα που ονειρευόμασταν να κάνουμε μικροί ?", " Αστροναύτης _1", " Ποδοσφαιριστής ﻿_1"," Μπασκετμπολίστας _1", " Πυροσβέστης _3",  "Αστυνομικός _1", " Ηθοποιός _3", " Τραγουδιστής _3", " Δικηγόρος _5", " Ιατρός _3"," Κτηνίατρος _5"," Καθηγητής _1");
createNewListOfQuestionsWithAnswers("10 Ψυχικές διαταραχές ?", " Αποπροσωποποίηση _5", " Αποπραγματοποίηση  ﻿_5"," Ψυχωσική διαταραχή  _3", " Κλεπτομανία  _1",  "Κρίση πανικού  _1", " Ναρκισσισμός _3", " Νεύρωση _3", " Σχιζοφρένεια _1", " Υστερία _1"," Σύνδρομο Γονικής Αποξένωσης  _5"," Μανία  _1");
////////////
createNewListOfQuestionsWithAnswers("10 Φοβίες ?", " Ακροφοβία - υψοφοβία _1", " Αγοραφοβία ﻿_3"," Αραχνοφοβία _1", " Αστραποφοβία _3",  " Κλειστοφοβία _1", " Οχλοφοβία _5", " Γλωσσοφοβία _3", " Μιασματοφοβία-Μικροβιοφοβία _3", " Νεκροφοβία _3"," Κοινωνική φοβία _5"," Φόβος για τα φίδια _3"," Φόβος της ασθένειας  _5");
createNewListOfQuestionsWithAnswers("10 Αρχαίες γραφές ?", " Γραμμική Α-Β ﻿_1"," Ιερογλυφικά _1", " Αρχαία Ελληνικά _1",  "Λατινικά _1", " Αραμαϊκά _3", " Περσικά _1", " Φοινικικά _3", " Πεσίτα ( Συριακά ) _3"," Τούρκικα _1"," Αρμενικά _3","Κυριλλικά _5");
createNewListOfQuestionsWithAnswers("10 ζώα για τα οποία στήνουμε παγίδες ?", " Κατσαρίδα _1", " Ποντίκι ﻿_1"," Λαγός _1", " Αρκούδα _3",  "Μύγα _1", " Γάτα _1", " Δάκο _5", " Κουνούπια _1", " Σφήκα _3"," Περιστέρι _3"," Αλεπού _3"," Αγριογούρουνο _1"," Άνθρωπο _3");
createNewListOfQuestionsWithAnswers("10 σπόρ με μπάλα ?", " Ποδόσφαιρο _1", " Μπάσκετ ﻿_1"," Βόλεϊ _1", " Πίνκ πόνκ _1",  "Μπιλιάρδο _3", " Μπόουλινγκ _3", " Γκόλφ _1", " Πόλο _3", " Μπέηζμπολ _3"," Μπάντμιντον _5"," Ποδοβόλεϊ _5","Χόκεϊ επι χόρτου _3","Ράγκμπι _3");
createNewListOfQuestionsWithAnswers("10 Ελληνικές σειρές των 90’s ?", " Λάμψη _1", " Χάϊ Ροκ  ﻿_5"," Οι μεν και οι δεν  _1", " Της Ελλάδος τα παιδιά _1",  "Δις εξαμαρτείν  _3", " Εμείς κι εμείς _3", " Λάβ Σόρρυ  _5", " Απαράδεκτοι _1", " Πάτερ ημών  _3"," Εκείνες κι εγώ  _3"," Δυό ξένοι  _1"," Και οι παντρεμένοι έχουν ψυχή _3"," Ο κακός Βεζίρης  _5"," Ψίθυροι καρδιάς  _3"," Εγκλήματα  _1","Κωνσταντίνου και Ελένης _1");
createNewListOfQuestionsWithAnswers("10 Ξένες σειρές των 90’s ?", " Baywatch _1", " Beverly Hills  ﻿_1"," Χ-files _3", " Τα φιλαράκια _1",  "Highlander _3", " Ζήνα _3", " Ηρακλής _3", " Mr. Bean _1", " Η νταντά _3"," Ο πρίγκιπας του Bel Air_5"," South park _3","Sopranos _5","Ρεξ _3");
createNewListOfQuestionsWithAnswers("10 Aντικείμενα σε ένα περίπτερο ?", " Τσιγάρα _1", " Χαρτομάντηλα ﻿_1"," Νερό _1", " Χυμός _1",  "Αναψυκτικά _1", " Προφυλακτικά _3", " Τσίχλα _1", " Σοκολάτα _1", " Γλυφιτζούρι _3"," Περιοδικά _3"," Εφημερίδες _1"," Αναπτήρας _3"," Νυχοκόπτης _3"," Μπαταρία _5"," Ζελεδάκια _5");
createNewListOfQuestionsWithAnswers("10 Aπο τις μεγαλύτερες πολεις της ελλαδας ?", " Αθήνα _1", " Θεσσαλονίκη	 ﻿_1"," Πάτρα	 _1", " Ηράκλειο _1",  "Βόλος _3", " Ρόδος _3", " Ιωάννινα _5", " Χανιά _3", " Χαλκίδα _5"," Λάρισα _3","Καβάλα _3");
createNewListOfQuestionsWithAnswers("10 Tαινιες με αριθμους στον τιτλο ?", " O αριθμός 23 _3", " 300 ﻿_3"," se7en _1", " 101 σκυλια της Δαλματίας _1",  "21 γραμμάρια _1", " 40 ετών παρθένος _3", " 40 μέρες και 40 νύχτες _3", " 12 χρόνια σκλάβος _1", " Η έκτη αίσθηση _3"," 7 χρόνια στο Θιβέτ _5"," 8 miles _1"," Million Dollar Baby  _5","Η συμορία των 11-12-13");
createNewListOfQuestionsWithAnswers("10 Mάρκες αρωμάτων ?", " Tommy Hilfiger _3", " Calvin Klein ﻿_1"," Dior _3", " Chanel _3",  "Gucci _1", " Prada _3", " Donna Karan _1", " Bulgari  _3", " Dsquared _5"," Ralph Lauren _3"," Versage _3"," Valentino _5"," Burberry _3","Boss _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");
//createNewListOfQuestionsWithAnswers("10 γνωστα ζωα απο καρτουν ?", " Simba _3", " Simba ﻿_5"," Simba _3", " Simba _1",  "Simba _1", " Simba _3", " Simba _1", " Simba _5", " Simba _3"," Simba _5"," Simba _1");


Log.e("listOfQuestions =",Integer.toString(listOfQuestionsWithAnswers.size()));

	}

	/**
	 * 
	 */
	private OnClickListener menuButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			context.gotoMenuWhilePlaying();
			question.setEarlyStop(true);
		}
	};

	/*
	 * 
	 */

	private OnClickListener nextButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!gameIsOver) {

				int coun = answers.getCurrentGameScore();

				if (state == END_GAME) {
					if (thread.isAlive()) {
						question.setEarlyStop(true);

					}
					if(Mix.isMixSelected&& context.getMixView()!=null){  // an einai energopoihmeni i mix epilogi
                    context.getMixView().removeView(MainGameQuiz.this);
                    context.getMixView().addButtons();
                    context.getMixView().setScore(answers.getCurrentGameScore());
                    
                    return;
					}
					answers.setVisibility(INVISIBLE);
					question.setQuestion("");
					if (isPlayerOneTurn) {
						playerOneGrades.add(answers.getCurrentGameScore());
						playerOneTotalScore += answers.getCurrentGameScore();
					} else {
						playerTwoGrades.add(answers.getCurrentGameScore());
						playerTwoTotalScore += answers.getCurrentGameScore();
						currentRoundCounter++;

					}

					

				}
				if (state == SHOW_GRADES) {

					isPlayerOneTurn = !isPlayerOneTurn;
					if (currentRoundCounter > totalRounds) {

						gameIsOver = true;
						gameOver();
						return;
					}

					createNextQuestion();
				}
				
				
				
				if (state == READY_TO_START) { // proeretiko kalou kakoy
        if(thread != null && thread.isAlive()){
	         question.setEarlyStop(true);
         }
					
						thread = new Thread(question);
						thread.start();
					

					answers.setVisibility(VISIBLE);
				}

				state++;

				state = state % 3;
			}

		 Log.e("state  =     ", Integer.toString(state));
			MainGameQuiz.this.setBackgroundColor(Color.WHITE);
		
			
			context.activateLight();
		}	
	};

	public void continueGame() {
Log.e("state = "+Integer.toString(state),Integer.toString(END_GAME));
		if (state == END_GAME) {
			if (thread != null && !thread.isAlive()) {
				question.setEarlyStop(true);
				
			}
		
				thread = new Thread(question);
				Log.e("", "gamieseeeee");
				
				thread.start();
			

			// state++;
			MainGameQuiz.this.setBackgroundColor(Color.WHITE);
		}
	}

	/**
	 * to prwto einai i erwtisi .. ta ypoleipa einai oi apandiseis me '_'
	 * (split) ksexwrizw apo tis apandiseis to string tis apandisis me to
	 * integer pou antiprosopeyei tin varitita kathe apandisis
	 * 
	 * @param str
	 */
	private void createNewListOfQuestionsWithAnswers(String... str) {
		String sum = new String();
		if(str.length<11){
			for(int i = 0; i < 50; i++){
				Log.e("errorrr","error");
			}
		}
		for (int i = 0; i < str.length; i++) {
			sum = sum + str[i] + "--";
		}

		listOfQuestionsWithAnswers.add(sum);
		
	
	}

	/**
	 * create a new question with answers
	 */
	public void createNextQuestion() {
		answers.clear();
		question.clear();
		currentQuestionWithAnswer = getRandomQuestionWithAnswers();
		ArrayList<String> allCurrentAnswers = new ArrayList<String>();
		String[] allAnswers = currentQuestionWithAnswer.split("--");

		for (int i = 0; i < allAnswers.length; i++) {

			if (i == 0) {
				question.setQuestion(allAnswers[i]);
			} else {
				if(!allAnswers[i].startsWith(" ")){
					allAnswers[i]=" "+allAnswers[i];	
				}
				allCurrentAnswers.add(allAnswers[i]);
				// answers.getAnswers()[i-1].setCurrentAnswer(allAnswers[i]);
			}
		}
		// for(int i=0;i<this.answers.getAnswers().length;i++){
		
		// vazw tis tyxaies erwtiseis st0 allCurrentAnswers
		for (int i = 0; i < this.answers.getAnswers().length; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(allCurrentAnswers.size());
			answers.getAnswers()[i].setCurrentAnswer(allCurrentAnswers.get(randNum));
			allCurrentAnswers.remove(randNum);
		}

	}

	/**
	 * get and remove a random question with the right answers from my list
	 * 
	 * @return
	 */
	public String getRandomQuestionWithAnswers() {
		Random rand = new Random();

		int randNum = rand.nextInt(listOfQuestionsWithAnswers.size());
		String questWithAns = listOfQuestionsWithAnswers.get(randNum);
		listOfQuestionsWithAnswers.remove(randNum);
		return questWithAns;
	}

	/**
 * 
 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int size = this.getChildCount();

		for (int i = 0; i < size; i++) {

			View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			if (child instanceof Question) {

				child.layout(0, 0, r, b / 9);

			}
			if (child instanceof Answers) {
				child.layout(0, b / 9, r, b - b / 12);
			}
			
		int sz= b-b / 2 + b / 3 + b / 11;
			if (child == nextButton) {
				child.layout(r / 2 + r / 10, b / 2 + b / 3 + b / 11, r, b);
				nextButton.setTextSize(nextButton.getHeight()/10
				);
				
			}
			
			
			if (child == menuButton) {
				child.layout(r / 20, b / 2 + b / 3 + b / 11, r / 3, b);
				menuButton.setTextSize(menuButton.getHeight()/10);
			
			}
		//	for(int j=0; j<20;j++){
			//	Log.e(Integer.toString(sz),Integer.toString(nextButton.getHeight()));
			//}
			if (child instanceof ImageView) {
				child.layout(0, 0, r, b - b / 6);
			}

		}
	}

	/**
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		if (!gameIsOver) {
			paint.setColor(Color.WHITE);
			canvas.drawPaint(paint);
			if (state == SHOW_GRADES) {
				paint.setTextSize(20);
				paint.setColor(Color.YELLOW);
				// canvas.drawText("SHOW_GRADES", getWidth() / 10, getHeight() /
				// 2,paint);
				drawScores(canvas, paint);
			}

			if (state == READY_TO_START) {
				paint.setColor(Color.RED);
				paint.setTextSize(50);

         try{
        	 mBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.einstein);//einstein or mime
				 float scaleX=(getWidth()/(float)(mBitmap.getWidth()+mBitmap.getWidth()/15));
				 float scaleY=(getHeight()/(float)(mBitmap.getHeight()+mBitmap.getHeight()/15));

				 mBitmap=getResizedBitmap(mBitmap,scaleX,scaleY);
				 canvas.drawBitmap(mBitmap, 0, getHeight()/20,paint);






         }catch(OutOfMemoryError e){
	
          }			
         if(Mix.isMixSelected){
        	 paint.setTextSize(getWidth()/10);
				paint.setColor(Color.BLUE);
				canvas.drawText("Σου έτυχε quiz ", getWidth() / 30,
						getHeight() / 5, paint);
			}else{
				canvas.drawText(
						"round " + Integer.toString(currentRoundCounter),
						getWidth() / 10, getHeight() - getHeight() / 3, paint);
				if (currentRoundCounter == totalRounds) {
					paint.setColor(Color.BLACK);
					paint.setTextSize(20);
					canvas.drawText("Τελευταιος γυρος", getWidth() / 5,
							getHeight() - getHeight() / 10, paint);
				}
				paint.setTextSize(getWidth()/10);
				paint.setColor(Color.BLUE);
				
				if (isPlayerOneTurn) {
					canvas.drawText("Σειρα του παιχτη 1", getWidth() / 30,
							getHeight() / 5, paint);
				} else {
					paint.setColor(Color.CYAN);
					canvas.drawText("Σειρα του παιχτη 2", getWidth() / 30,
							getHeight() / 5, paint);
				}
				}

			}
		} else {
		
			paint.setColor(Color.BLACK);
			paint.setTextSize(getWidth()/15);
			if (playerOneTotalScore > playerTwoTotalScore) {
				canvas.drawText("Ο παιχτης 1 ειναι ο νικητης", getWidth() / 10,
						getHeight() - getHeight() / 10, paint);
			} else if (playerOneTotalScore < playerTwoTotalScore) {
				canvas.drawText("Ο παιχτης 2 ειναι ο νικητης", getWidth() / 10,
						getHeight() - getHeight() / 10, paint);
			} else {
				canvas.drawText("Εχουμε ισοπαλια", getWidth() / 10, getHeight()
						- getHeight() / 10, paint);
			}
			
			paint.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(playerOneTotalScore)+"  -   "+Integer.toString(playerTwoTotalScore), getWidth()/2, getHeight() - getHeight() / 25, paint);
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

	/**
	 * 
	 * @param canvas
	 */
	private void drawScores(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLACK);
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
		paint.setColor(Color.DKGRAY);
		paint.setTextSize(getWidth()/2/20);
		canvas.drawText("Παιχτης 1 βαθμολογίες ", 0, getHeight() / 7, paint);
		paint.setColor(Color.LTGRAY);
		canvas.drawText("Παιχτης 2 βαθμολογίες ", getWidth() / 2, getHeight() / 7,
				paint);
		paint.setColor(Color.BLACK);
		canvas.drawLine(0, getHeight() / 6, getWidth(), getHeight() / 6, paint);
		int txtSize=10;
		paint.setTextSize(txtSize);
		
		for (int i = 0; i < playerOneGrades.size(); i++) {
			paint.setColor(Color.BLACK);

			canvas.drawText(Integer.toString(playerOneGrades.get(i)),
					getWidth() / 40, getHeight() / 5 + i * (txtSize+2), paint);
		}
		paint.setTextSize(2*txtSize);
		if (playerOneGrades.size() > 0) {
			int height= getHeight() / 5 + (playerOneGrades.size()) * (txtSize+2)-txtSize;
			canvas.drawLine(0, height, getWidth() / 2,height, paint);
			canvas.drawText(Integer.toString(playerOneTotalScore),
					getWidth() / 50, getHeight() / 5 + playerOneGrades.size()* (txtSize+2)+10, paint);
		}
		
		paint.setTextSize(txtSize);
		for (int i = 0; i < playerTwoGrades.size(); i++) {
			canvas.drawText(Integer.toString(playerTwoGrades.get(i)),
					getWidth() / 2 + getWidth() / 40, getHeight() / 5 + i * (txtSize+2),
					paint);
			// canvas.drawText(Integer.toString(playerTwoGrades.get(i)),
			// getWidth()/20, i*getHeight()/30+30, paint);
		}
		
		
		paint.setTextSize(2*txtSize);
		if (playerTwoGrades.size() > 0) {
			int height= getHeight() / 5 + (playerTwoGrades.size()) * (txtSize+2)-txtSize;
			canvas.drawLine(getWidth() / 2,height,getWidth(), height, paint);
			canvas.drawText(Integer.toString(playerTwoTotalScore), getWidth()/ 2 + getWidth() / 50,
					getHeight() / 5 + (playerTwoGrades.size()) * (txtSize+2)+10, paint);
		}

	}

	public void gameOver() {
		context.clapSound();
		gameIsOver=true;
		question.setEarlyStop(true);
		question.setVisibility(INVISIBLE);
		answers.setVisibility(INVISIBLE);
		nextButton.setVisibility(INVISIBLE);
		addView(imgView);
	}

	public void newGame() {
		isPlayerOneTurn = true;
		playerOneGrades.removeAll(playerOneGrades);
		playerTwoGrades.removeAll(playerTwoGrades);
		question.setVisibility(VISIBLE);
		answers.setVisibility(VISIBLE);
		nextButton.setVisibility(VISIBLE);
		this.removeView(imgView);
		gameIsOver = false;
		playerOneTotalScore = 0;
		playerTwoTotalScore = 0;
		state = READY_TO_START;
		fillList();
		currentRoundCounter = 1;
	}

	public int getCurentGameScore() {
		setCurentGameScore(answers.getCurrentGameScore());
		return curentGameScore;
	}

	public void setCurentGameScore(int score) {
		this.curentGameScore = score;

		if (score != answers.getCurrentGameScore()) {
			answers.setScore(score);
		}
	}

	public void resetScore() {
		setCurentGameScore(0);
	}

	public String getQuestion() {
		return question.getQuestion();
	}

	public void setQuestion(String quest) {
		question.setQuestion(quest);
	}

	 public boolean isGameOver() {
		return gameIsOver;
	}
	 public void setIsGameOver(boolean gameIsOver){
		 this.gameIsOver=gameIsOver;
	 }


public Question getTimer(){
	return question;
}

	public ArrayList<String> getListOfQuestionsWithAnswers() {
		return listOfQuestionsWithAnswers;
	}
public void release(){
	question.setEarlyStop(true);
}

	public void setListOfQuestionsWithAnswers(ArrayList<String> listOfQuestionsWithAnswers) {
		this.listOfQuestionsWithAnswers = listOfQuestionsWithAnswers;
	}

	
}
