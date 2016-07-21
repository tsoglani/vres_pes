package pes.game.tsoglani.vres.vres_to_pes_to;

import java.util.ArrayList;
import java.util.Random;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class PandomimaGameView extends ViewGroup {
	private VresPesActivity context;
	private boolean isTimeToShowScore = false;
	private int scoreATeam;
	private int scoreBTeam;
	private boolean isFirstTeamTurn = true;
	private String typeOfQuestion;
	private String currentQuestion;
	private Question screen;
	private Question timer;
	private Thread thread;
	private int round = 0;
	private final int END_GAME = 0;
	private final int YOU_FOUND_OR_NOT = 1;
	private final int SHOW_TURN = 2;
	private final int READY_TO_START = 3;
	private int state = 2;
	private boolean readyToDrawWhoTurnIs;
	private ArrayList<Integer> teamAScore;
	private ArrayList<Integer> teamBScore;
	public static final String PANDOMIMA_GIA_DOULEIA = "παροιμιες";
	public static final String PANDOMIMA_GIA_TAINIES = "ταινίες";
	public static final String PANDOMIMA_SINDIASTIKI = "συνδιαστικες";
	private Button continueButton;
	private Button menuButton;
	private Button[] choseTypeOfQuestion = new Button[5];
	private Bitmap gameOverBitmap;
	private Bitmap teamABitmap;
	private Bitmap teamBBitmap;
	private Button correct;
	private Button wrong;
	private static ArrayList<String> proverbs = new ArrayList<String>();
	private static ArrayList<String> movies = new ArrayList<String>();
	private static ArrayList<String> songs = new ArrayList<String>();
	private static ArrayList<String> xxx = new ArrayList<String>();
	private ArrayList<String> allArraysElements = new ArrayList<String>();
	private ArrayList<String> selectedArray;
	public boolean isGameOver;
	public static final float scoreMulti = (float) 0.4;

	public PandomimaGameView(VresPesActivity context) {
		super(context);
		this.context = context;
		// MainGameQuiz.hasChosenQuizMode=false;
		init();
	}

	/**
 * 
 */
	private void init() {
		teamAScore = new ArrayList<Integer>();
		teamBScore = new ArrayList<Integer>();
		screen = new Question(context);
		correct = new Button(context);
		wrong = new Button(context);
		screen.setTextSize(25);
		screen.setBkgrndColor(Color.WHITE);
		screen.setForoundColor(Color.BLACK);
		// screen.setBitmap(R.drawable.pantomima);
		continueButton = new Button(context);
		menuButton = new Button(context);
		continueButton.setText("Συνέχεια");
		menuButton.setText("Μενού");
		correct.setText("Την βρηκε !");
		wrong.setText("Δεν την βρηκε !");
		correct.setOnClickListener(correctButtonListener);
		wrong.setOnClickListener(correctButtonListener);
		menuButton.setOnClickListener(canselListener);
		continueButton.setOnClickListener(continueButtonListener);
		setWillNotDraw(false);
		correct.setBackgroundResource(R.drawable.crct);
		wrong.setBackgroundResource(R.drawable.wrg);

		if (!Mix.isMixSelected) {
			for (int i = 0; i < choseTypeOfQuestion.length; i++) {
				choseTypeOfQuestion[i] = new Button(context);
				addView(choseTypeOfQuestion[i]);
				choseTypeOfQuestion[i]
						.setOnClickListener(choseTypeOfQuestionListener);
			}
			choseTypeOfQuestion[0].setText("παντομιμες με παροιμιες-φρασεις");
			choseTypeOfQuestion[1].setText("παντομιμες με ταινίες");
			choseTypeOfQuestion[2].setText("παντομιμες με διαφορα");
			choseTypeOfQuestion[3].setText("παντομιμες με τραγουδια");
			choseTypeOfQuestion[4].setText("παντομιμες με αισθησιακες ταινιες");

			fillMoviesArray();
			fillxxxArray();
			fillProverbArray();
			fillSongsArray();
			fillallArraysElements();
			Log.e("movies size", Integer.toString(this.movies.size()));
			Log.e("xxx movies size", Integer.toString(this.xxx.size()));
			Log.e("songs size", Integer.toString(this.songs.size()));
			Log.e("proverbs size", Integer.toString(this.proverbs.size()));
			Log.e("all except xxx size",
					Integer.toString(this.allArraysElements.size()));
		} else {
			selectedArray = context.getMixView().getPandomimes(); // tin prwti
																	// fora p
																	// ekteleite
																	// exei idi
																	// timi to
																	// context.getMixView().getPandomimes()
			readyToDrawWhoTurnIs = true;
			addComponentWhoNeededToStartPlaying();
			state = 2;
			addView(screen);

			if (context.getMixView().isTeamATurn()) {
				screen.setQuestion("ομάδα Α σου ετυχε η παντομίμα");
			} else {
				screen.setQuestion("ομάδα Β σου ετυχε η παντομίμα");
			}
			screen.setBitmap(R.drawable.pantomima);
			addView(timer);
			// showReadyToPlay();
		}

		Log.e(Integer.toString(allArraysElements.size()), "fillProverbArray");
	}

	OnClickListener correctButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			removeView(v);
			int correctAnswerScore = (int) (scoreMulti * timer.getTimeLeft()) + 10;
			if (correct == v) {
				context.playCorrectSound();
				if (isFirstTeamTurn) {
					teamAScore.add(correctAnswerScore);
					scoreATeam += +correctAnswerScore;
				} else {
					teamBScore.add(correctAnswerScore);
					scoreBTeam += +correctAnswerScore;
					round++;
				}
			} else {
				context.playWrongSound();
				if (isFirstTeamTurn) {
					teamAScore.add(-5);
					scoreATeam += -5;
				} else {
					teamBScore.add(-5);
					scoreBTeam += -5;
					round++;
				}
			}
			if (round >= MainGameQuiz.totalRounds) {
				gameOver();
				// Log.e("round>=MainGameQuiz.totalRounds  ",Boolean.toString(round>=MainGameQuiz.totalRounds));
				return;
			}
			// Log.e("round=",Integer.toString(MainGameQuiz.totalRounds));
			// Log.e("isFirstTeamTurn=",Boolean.toString(isFirstTeamTurn));
			PandomimaGameView.this.removeView(screen);
			isTimeToShowScore = true;
			isFirstTeamTurn = !isFirstTeamTurn;
			showGrades();
			setBackgroundColor(Color.WHITE);

		}

	};

	OnClickListener continueButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			state++;
			state = state % 4;
			readyToDrawWhoTurnIs = state == SHOW_TURN;
			if (state == END_GAME) {
				showQuestion();
				// Log.e("END_GAME","END_GAME");
			}

			if (state == YOU_FOUND_OR_NOT) {
				if (Mix.isMixSelected) {
					context.getMixView().removeView(PandomimaGameView.this);
					context.getMixView().addCorrectWrongButons();

				} else {
					isPlayerAnswerWriteView();
					screen.resetBitmap();
				}
				//

			}
			if (state == SHOW_TURN) {

				showChangingTurn();
			}
			if (state == READY_TO_START) {
				showReadyToPlay();
				Log.e("READY_TO_START", "READY_TO_START");

			}
			Log.e(Integer.toString(state), "pandomima");

			//
			context.activateLight();
			invalidate();
			postInvalidate();
			// Log.e("state =",Integer.toString(state));
		}

	};

	private void showChangingTurn() {
		isTimeToShowScore = false;
		// readyToDrawWhoTurnIs=true;
		this.setBackgroundColor(Color.WHITE);
	}

	OnClickListener choseTypeOfQuestionListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == choseTypeOfQuestion[0]) {
				selectedArray = proverbs;
				// fillProverbArray();
			} else if (v == choseTypeOfQuestion[1]) {
				selectedArray = movies;
				// fillMoviesArray();
			} else if (v == choseTypeOfQuestion[2]) {
				selectedArray = allArraysElements;
				// fillallArraysElements();
			} else if (v == choseTypeOfQuestion[3]) {
				selectedArray = songs;
				// fillSongsArray();
			} else if (v == choseTypeOfQuestion[4]) {
				enterOnxxx();
				return;
				// fillSongsArray();
			}
			removeButtonsTypeOfQuestion();
			readyToDrawWhoTurnIs = true;
			addComponentWhoNeededToStartPlaying();
			// showReadyToPlay();
		}
	};

	private void gameOver() {
		removeAllViews();
		timer.setEarlyStop(true); // kalou kakou to evala
		addView(menuButton);
		context.clapSound();
		isGameOver = true;
		setBackgroundColor(Color.WHITE);
	}

	private void addComponentWhoNeededToStartPlaying() {
		if (thread != null && timer != null && thread.isAlive()) {
			timer.setEarlyStop(true);

		}
		timer = new Question(context);
		timer.setShowTimer(true);
		addView(continueButton);
		addView(menuButton);

		// addView(timer);
	}

	/**
 * 
 */
	private void showReadyToPlay() {
		try {
			addView(screen);
		} catch (Exception e) {
		}

		screen.setBitmap(R.drawable.pantomima);
		Log.e("showReadyToPlay", "showReadyToPlay");
		screen.setQuestion("Πατα συνέχεια για να αρχισει ο χρονος");
		currentQuestion = getQuestion();
		timer.setQuestion(currentQuestion);

		removeView(correct);
		removeView(wrong);

		try {
			addView(timer);
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 */
	private void isPlayerAnswerWriteView() {
		if (thread != null && timer != null && thread.isAlive()) {
			timer.setEarlyStop(true);
		}
		screen.setQuestion("");
		timer.setQuestion("");
		removeView(continueButton);
		removeView(timer);

		addView(correct);
		addView(wrong);

	}

	/**
 * 
 */
	private void showGrades() {
		removeView(correct);
		removeView(wrong);
		addView(continueButton);
		// addView(canselButton);
	}

	/**
	 * 
	 */
	private void showQuestion() {
		screen.setQuestion(currentQuestion);
		timer.setQuestion("");
		// screen.setQuestion(getQuestion());
		thread = new Thread(timer);
		thread.start();
	}

	/**
	 * 
	 * @return
	 */
	private String getQuestion() {
		Random rand = new Random();
		int randomNumber = rand.nextInt(selectedArray.size());
		String question = selectedArray.get(randomNumber);
		selectedArray.remove(randomNumber);
		Log.e(question,
				" = my question , is in list number _ "
						+ Integer.toString(randomNumber));
		return question;
	}

	/**
	 * 
	 */
	public void removeButtonsTypeOfQuestion() {
		this.removeAllViews();
		for (int i = 0; i < choseTypeOfQuestion.length; i++) {
			this.removeView(choseTypeOfQuestion[i]);
		}
	}

	/**
	 * 
	 */
	public void fillxxxArray() {
		addxxx("Η Πεντάμορφη και το Πέος");
		addxxx("Στο τρένο θα στον σφυρίξω δυο φορές ");
		addxxx("Τα κανόνια του Bυζαρόνε ");
		addxxx("Πουτανικός ");
		addxxx("Τα μαύρα παπάρια του έρωτα ");
		addxxx("Ξέρω ποιον ξεπέταξες πέρυσι το καλοκαίρι ");
		addxxx("Η στύση σας προωθείται ");
		addxxx("Ποκαχώνοντας ");
		addxxx("Πολύ σκληρός για να χωρέσει ");
		addxxx("Η σαραντάρα κι ο π@π@ρης ");
		addxxx("Βαθύτερα δεν γίνεται");
		addxxx("Η νύφη τον έφαγε ");
		addxxx(" Μπούτια ερμητικά κλειστά ");
		addxxx("Το κλαρίνο του λοχαγού Κωλέρι");
		addxxx("Γ@μελί ");
		addxxx("Χίλιες και μια πίπες");
		addxxx("Σε τεντωμένο κ@υλί ");
		addxxx("Κλεοπάρτα");
		addxxx("Ο μύθος του ακέφαλου κ@βλιάρη ");
		addxxx("Θέλω και τη γιαγιά σου ");
		addxxx("Επιστροφή στη χώρα του πρωκτέ");
		addxxx("Ο γάντζος του πειρατή");
		addxxx("Πρώτο τραβελι πίστα");
		addxxx("Ο μαύρος που τον έλεγαν άλογο ");
		addxxx("Μπαίνε-τον: ενωμένα χρώματα του σεξ");
		addxxx("Ασπρο Λούκι, μαύρο Παλ@ύκι");
		addxxx("Το παλαμάρι του βαρκάρη");
		addxxx("Αποστολή στον βόρειο κώλο");
		addxxx("Αν έχεις κώλο διάβαινε");
		addxxx("Αυτός, αυτή και τα μπανιστήρια");
		addxxx("Αχ και Βαχ με τον Σεβάχ.");
		addxxx("Οι τρείς χαρχάλες");
		addxxx("Φονικό Πέος");
		addxxx("Η κυρία κι ο σκύλος");
		addxxx("Σας παρακαλώ, αλλάξτε μου τα φώτα");
		addxxx("Το σκληρό πουλί της νιότης");
		addxxx("Κατούρα να φύγουμε");
		addxxx("Τετραπλό Σάντουϊτς");
		addxxx("Τα Βυζιά Δολοφόνοι");
		addxxx("Οι 8 μεγάλοι Μήτσοι");
		addxxx("Ο τροχός της στύσης");
		addxxx("Ραντεβού στα διπλά");
		addxxx("Σκύψε Ευλογημένη");
		addxxx("Οι κυρίες θέλουν κλαρίνα");
		addxxx("Ο Βούρδουλας και τα βάρδουλα");
		addxxx("Συγχώρεσε με που πονώ");
		addxxx("Ιντεραράπικαν, μεγάλη και σίγουρη");
		addxxx("Άνοιξε από πίσω");
		addxxx("Εμείς οι βλάχοι, όπου λάχει");
		addxxx("Ψ@λιές και πιστολιές κάτω απο τις ελιές");
		addxxx("Του κώλου τα 9μερα ");
		addxxx("Φταις δεν φταις θα γίνει ο κώλος σου κεφτές ");
		addxxx("Πάρτυ με ούζα");
		addxxx("Τσιμπούκι@ με θέα στην Καλλιθέα");
		addxxx("Στο Βόλο θα σε πάρω απ’ τον κ*λο");
		addxxx(" Στην Πάργα πέφτει γ@μησι αλάργα");
		addxxx(" Το ακρωτήρι του κ*λου");
		addxxx("Σούφρ@ και παραλλαγή");
		addxxx("Ταρζαν, η ψ0λη της ζουγκλας");
		addxxx("Ο καυληρακλής");
		addxxx("Παλούκι λουκ ,ταξιδι στην αγρια στύση");
		addxxx("Ο καλός ο καπετάνιος στην παρτούζα φαίνεται");
		addxxx("Ο Ζορό ζορίζεται");
		addxxx("Η Χιονάτη και οι 7 βαρβάτοι");
		addxxx("Αγάπη μου μεγέθυνα το πουλί μου");
		addxxx(" Ο Χάρι Καπότερ και η κάμαρα με τους δον*τές");
		addxxx("Ο Π**ς που δε λύγισε ποτέ");
		addxxx("Από μπροστά παρθένα κι από πίσω μπαίνουν τρένα");
		addxxx("Τα κορίτσια που απ’ τον κ** παίρνουνε τον κόσμο όλο");
		addxxx("Τα τ***κια του διανοούμενου");
		addxxx("Από δαχτυλίδι μου τον έκανες βραχιόλι");
		addxxx("Τζουράσικ fuck");
		addxxx("Η κωλ0τρυπιδίτσ@ και ο κακός ψώλ0ς");
		addxxx("Οι κ@υλ0ν0σ0κόμες ");
		addxxx("Πίσω, μαύρη και λυσσάρα! ");
		addxxx("Aεροκωλομαχίες");
		addxxx("Ο κύκλος των τουρλωμένων πισινών ");
		addxxx("Buttman");
		addxxx("Ο Ρομπέν των πρωκτών ");
		addxxx("Το φιλί της γυναίκας αρπάχτρας ");
		addxxx("Εφιάλτης στο δρόμο με τα πέη ");
		addxxx(" Γ@μάτε Κιντ ");
		addxxx("Hothika");
		addxxx("Ο Αλί Μπαμπά και οι 40 μαύροι");
		addxxx("βαλε βγαλε το π@λουκι σου μεγαλε");
		addxxx("Θέλεις στρίμωγμα;");
		addxxx("Αδελφομαζωματα παρτουζοσκορπισματα");
		addxxx("Τον φωναζανε αλισια και τον επαιρνε στα ισια");
		addxxx("Αν εχεις κωλο διαβαινε");
		addxxx("Ο κουμπαρος κι`η κουμπαρα και του νεγρου η παπαρα");
		addxxx("Βρηκαν κωλο σαν χαβουζα και τον πηρανε παρτουζα");
		addxxx("Ηταν σκληρος και τον φωναζαν νυχτα");
		addxxx("Βαλε λαδι κ`ελα βραδυ");
		addxxx("Ενα σκληρο καβλι για καληνυχτα");
		addxxx("Δεν καθοσουν να στον παρω κι`ειδα το χριστο φανταρο");
		addxxx("Παρατα το τηγανι να στον χωσω μανι μανι");
		addxxx("Τα θελω ολα κι`ας με φωναζουν ψωλα");
		addxxx("Οπως λάχει εμείς οι βλαχοι");
		addxxx("Κουνουσε το κωλο του και επεφταν καποτες");
		addxxx("Καλλιο δυο στο κωλομερι παρα δεκα και καρτερει");
		addxxx("Κοναν ο καβλαροs");
		addxxx("Η αορατη ψ0λη");
		addxxx("Συλλεκτηs πρωκτων");
		addxxx("Η μέρα της στύσης");
		addxxx("Στη σκια του ξεκώλακα");
		addxxx("Ο Τρομπέν των δασών");
		addxxx("Χωράω κι εγώ");
		addxxx("Ιστορία μιας τρύπας");
		addxxx("Πες αλεύρι …ο αράπης σε γυρεύει");
		addxxx("Ταρζάν, η ψ@λή της ζούγκλας ");
		addxxx("Χτυποκ@ύλια στο Μπέβερλι Χιλς ");
		addxxx("Πουστ@ρ Wars Επεισόδιο 1: Η αόρατη ψ0λή ");
		addxxx("Γ@μημένος τη 4η Ιουλίου ");
		addxxx("Ο Φαλλός στην χώρα των Αιδοίων ");
		addxxx("Αφηνιασμένα καπούλια ");
		addxxx("Η πεοβατούσα ");
		addxxx("Τόλμη κι Αιμομιξία");
		addxxx("Χωράω κι εγώ");
		addxxx("Τα πρώτα Ψωλ0βρόχια");
		addxxx("Οι Σαββατοξεσκισμένες");
		addxxx("Γ@μα πουρά και αγνάντευε");
		addxxx("Τι σου 'κανα και χύν3ις ?");
		addxxx("Μπρός Γκρεμός και πίσω σπέρμα");
		addxxx("Είναι γ@μάτες ? γάμ@ τες");
		addxxx("Η πιραμίδα του Πέοπα");
		addxxx("Ο κόμης του Μόντε Χιστο");
		addxxx("Πουροτεχνήματα");
		addxxx("Ο έμπειρος της Βενετίας");
		addxxx("Ο Πε0χέρης");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
		// addxxx("");
	}

	public void fillSongsArray() {
		addSong("Πιο ερωτας πεθαινεις");
		addSong("Σαν ταινία παλιά");
		addSong("Με αεροπλάνα και βαπόρια");
		addSong("Το βαλς των χαμένων ονείρων");
		addSong("Γυρίζω τις πλάτες μου στο μέλλον ");
		addSong("Εισητήριο στην τσέπη σου");
		addSong("Όνειρο ήτανε");
		addSong("Ταξίδι στη βροχή");
		addSong("Σαν ναυαγοί");
		addSong("Ολοι τρέντι");
		addSong("Πινω μπάφους και παίζω προ");
		addSong("Δεν κάνει κρύο ");
		addSong("Θα είμαι κοντά σου όταν με θες");
		addSong("Στο ασανσέρ που συναντιόμαστε");
		addSong("Η μελωδία της μοναξιάς");
		addSong("Όνειρο ζω");
		addSong("Μαγικό χαλί");
		addSong("Ο μαύρος γάτος");
		addSong("Μ' αρέσει να μην λεω πολλά");
		addSong("Εγώ δεν είμαι ποιητής");
		addSong("Κανείς εδω δεν τραγουδά");
		addSong("Πες Μου Γιατί");
		addSong("Φύλακας άγγελος");
		addSong("Για τις παλιές αγάπες μη μιλάς");
		addSong("Έπαψες αγάπη να θυμίζεις ");
		addSong("Ένα τσιγγανάκι είπε");
		addSong("Γύφτισσα μέρα");
		addSong(" Αν είσαι ένα αστέρι");
		addSong("Θα βρω τη δύναμη");
		addSong("Μια φωτοβολίδα");
		addSong("Ελα ψιχούλα μου");
		addSong("Φτιάξε καρδιά μου το δικό σου παραμύθι");
		addSong("Γέλα πουλί μου γέλα");
		addSong("Γυρίζω τις πλάτες μου στο μέλλον");
		addSong("Ταξιδεύω το εγω μου");
		addSong("Το party δεν σταματά ");
		addSong("Δεν ταιριάζετε σου λέω");
		addSong("Στο αντίθετο ρεύμα ");
		addSong("Απόψε δεν πάμε σπίτι");
		addSong("Να την προσέχεις");
		addSong("Ενα τσιγγανάκι είπε");
		addSong("Αγωνία");
		addSong("Για Το Καλό Μου ");
		addSong("Γαρύφαλε");
		addSong("Βρέχει Φωτιά Στη Στράτα Μου ");
		addSong("Δεν Κοιμάμαι Τώρα Πια Τα Βράδια");
		addSong("Δεν Είμαι Τρομοκράτης");
		addSong("Είμαι Αετός Χωρίς Φτερά");
		addSong("Ένας Μάγκας Στο Βοτανικό");
		addSong("Η Δουλειά Κάνει Τους Άνδρες");
		addSong("Θα Πιω Απόψε Το Φεγγάρι ");
		addSong("Θάλασσα Πλατιά");
		addSong("Λιωμένο Παγώτο ");
		addSong("Μ'αρέσει Να Μη Λέω Πολλά");
		addSong("Μαύρος Γάτος");
		addSong("Μη Γυρίσεις ");
		addSong("Μη Μου Μιλάς Για Καλοκαίρια ");
		addSong("Μην Αντιστέκεσαι ");
		addSong("Μου Έφαγες Όλα Τα Δακτυλίδια");
		addSong("Νεοέλληνας");
		addSong("Μωρό Μου Φάλτσο");
		addSong("Ο Πιο Καλός Ο Μαθήτης");
		addSong("Ο Πιο Καλός Τραγουδιστής");
		addSong("Οι Θαλασσιές Οι Χάντρες");
		addSong("Ο Ύμνος Των Μαυρών Σκυλιών");
		addSong("Όσο Έχω Φωνή");
		addSong("Όταν Το Τηλεφώνο Χτυπήσει");
		addSong("Πες Μου Που Πουλάν Καρδιές");
		addSong("Πότε Βούδας Πότε Κούδας ");
		addSong("Σου Το'πα Μια Και Δυο Και Τρεις ");
		addSong("Στη Μαμά Μου Θα Το Πω ");
		addSong("Στη Ντισκοτέκ");
		addSong("Στου Παράδεισου Την Πόρτα");
		addSong("Συννεφούλα");
		addSong("Τα Κορίτσια Ξενυχτάνε");
		addSong("Τα Μαύρα Μάτια Σου ");
		addSong("Τα Παραμύθια Της Γιαγιάς ");
		addSong("Το Κορίτσι Του Φίλου Μου");
		addSong("Το Πλουσιόπαιδο ");
		addSong("Το Σωφεράκι ");
		addSong("Το Φιλαράκι ");
		addSong("Κλεις τα μάτια και άκου");
		addSong("Τρελοκόριτσο");
		addSong("Το φλερτ");
		addSong("Το κορίτσι του Μάη");
		addSong("Και τώρα ναι");
		addSong("Εσένα που σε ξέρω τόσο λίγο");
		addSong("Ένα αγόρι για πάρτι");
		addSong("Δεν θα σε μάρτυς");
		addSong("Δεν είσαι εντάξει");
		addSong("Κρίση");
		addSong("Δεν θα μάθεις ποτέ");
		addSong("Φεύγω για το επτά");
		addSong("Είσαι παντού");
		addSong("Το προσκλητήριο");
		addSong("Δεν υπάρχουν άγγελοι");
		addSong("Στην υγεία της αχάριστης");
		addSong("Μου’χεις κάνει την ζωή μου κόλαση");
		addSong("Εδώ σε θέλω καρδιά μου");
		addSong("Γύρνα σε μένα");
		addSong("Παραστράτημα");
		addSong("Συνοδεύομαι");
		addSong("Εννοείται");
		addSong("Ενα πιτσιρίκι");
		addSong("Χαρμάνης ειμαι απο το πρωί");
		addSong("Πριγκιπέσσα");
		addSong("Λατέρνα");
		addSong("Εξομολόγηση");
		addSong("Δείξε μου τον τρόπο");
		addSong("Ο καθρέφτης");
		addSong("Τα λαδάδικα");
		addSong("Το δωμάτιο");
		addSong(" Πρωινό τσιγάρο");
		addSong("Μπήκαν στην πόλη οι οχτροί");
		addSong("Κάγκελα παντού");
		addSong(" Η πιο μεγάλη ώρα είναι τώρα");
		addSong("Αυτόν τον κόσμο τον καλό");
		addSong("Μην ορκίζεσαι ");
		addSong("Σαν δυο σταγόνες βροχής ");
		addSong("Το φιλί της ζωής ");
		addSong("O κερατάς");
		addSong("Πάνω στην τρέλα μου");
		addSong("GUCCI forema");
		addSong("Ο τέλειος άνδρας");
		addSong("Έκανα τη νύχτα μέρα");
		addSong("Ζιγγολο");
		addSong("Φετα και Ψωμι");
		addSong("Θα σου πάρω μπακλαβα");
		addSong("Αν έχω αγάπη");
		addSong("Το βαπόρι απ' τη Περσία");
		addSong("Το μπαγλαμαδάκι");
		addSong(" Η φωνή του αργιλέ");
		addSong("Της μαστούρας ο σκοπός");
		addSong("Σαν φουμάρω τσιγαρίκι");
		addSong("Νύχτωσε χωρίς φεγγάρι");
		addSong("Ο λαθρέμπορος");
		addSong("Οι παλιές αγάπες πάνε στον παράδεισο");
		addSong("Δεν θα δακρύσω πια για σένα");
		addSong("Ολα σε θυμίζουν");
		addSong("Έχω πετάξει μαζί σου");
		addSong("Όταν ενας άντρας κλαίει");
		addSong("Τα πουλιά της δυστυχίας");
		addSong("Κρίση");
		addSong("Ο ήλιος που έγινε βροχή");
		addSong("Ολες οι γυναίκες μαζί");
		addSong("Η άμαξα μες την βροχή");
		addSong("Αίμα δάκρυα κι ιδρώτας");
		addSong("κυνηγητό");
		addSong("Γραμματα που σκίζω");
		addSong("Στου ουρανού την άκρη");
		addSong("Σαν πέσει η νύχτα");
		addSong("Που να γυρίζεις");
		addSong("Οταν θα λενε STOP");
		addSong("Απόψε θέλω να πιω");
		addSong("Μη με ονειρευτείς");
		addSong("Ματώνω");
		addSong("Τι τρέχει εδώ");
		addSong("Απόψε λεω να μην κοιμηθούμε");
		addSong("Θυμάμε οσα ειχε πεί");
		addSong("Για σένα τα σπάω");
		addSong("μια φωτιά στην άμμο");
		// addSong("");
		// addSong("");
		// addSong("");
		// addSong("");
	}

	/**
 * 
 */
	public void fillProverbArray() {
		addProverb("Οι καλοί λογαριασμοί κάνουν τους καλούς φίλους.");
		addProverb("Όλα τα δάχτυλα δεν είναι ίσα");
		addProverb("Το πολύ το κύριε ελέησον το βαριέται κι ο παπάς");
		addProverb("Από την πόλη έρχομαι και στην κορφή κανέλα");
		addProverb("Μεγάλη μπουκιά φάε, μεγάλο λόγο μην πεις");
		addProverb("Βαράτε με κι ας κλαίω");
		addProverb("Δείξε μου το φίλου σου, να σου πω ποιος είσαι");
		addProverb("Η καλή νοικοκυρά είναι δούλα και κυρά");
		addProverb("Και ο άγιος φοβέρα θέλει");
		addProverb("Η καθαριότητα είναι μισή αρχοντιά");
		addProverb("Ό,τι δίνεις παίρνεις");
		addProverb("Η τρέλα δεν πάει στα βουνά");
		addProverb("Στη βράση κολλάει το σίδερο");
		addProverb("Έβαλε την ουρά στα σκέλια");
		addProverb("Έφτασε ο κόμπος στο χτένι");
		addProverb("Τα ράσα δεν κάνουν τον παπά");
		addProverb("Ο πνιγμένος απ' τα μαλλιά του πιάνεται");
		addProverb("Δώσε θάρρος στο χωριάτη να σ ανέβει στο κρεβάτι");
		addProverb("Πήγε για μαλλί και βγήκε κουρεμένος");
		addProverb("Ψάχνει ψύλλους στ' άχυρα.");
		addProverb("Του παιδιού μου το παιδί, μου είναι δυό φορές παιδί");
		addProverb("Τα λόγια σου με χόρτασαν και το ψωμί σου φάτο");
		addProverb("Όπως έστρωσες θα κοιμηθείς");
		addProverb("Όπoιος βιάζεται σκoντάφτει");
		addProverb("Ρόδα είναι και γυρίζει");
		addProverb("Ο πεινασμένος καρβέλια ονειρεύεται");
		addProverb("Δεν υπάρχει καπνός χωρίς φωτιά");
		addProverb("Άλλος έχει το όνομα κι άλλος έχει τη χάρη");
		addProverb("Καθαρός ουρανός, αστραπές δε φοβάται");
		addProverb("Ιδού η Ρόδος, ιδού και το πήδημα");
		addProverb("Το καλό το παλικάρι ξέρει κι άλλο μονοπάτι");
		addProverb("Κάθε αρχή και δύσκολη.");
		addProverb("Κάθε εμπόδιο σε καλό");
		addProverb("Καλομελέτα κι έρχεται");
		addProverb("Παπούτσι από τον τόπο σου κι ας είναι μπαλωμένο");
		addProverb("Από μικρό κι από τρελό μαθαίνεις την αλήθεια");
		addProverb("Πνίγεσαι σε μια κουταλιά νερό");
		addProverb("Μην το πεις ούτε του παπά");
		addProverb("Κάλλιο πέντε και στο χέρι, παρά δέκα και καρτέρι");
		addProverb("Τρώγοντας έρχεται η όρεξη");
		addProverb("Το αίμα νερό δε γίνεται");
		addProverb("Κάλλιο αργά παρά ποτέ");
		addProverb("Ο τρελός είδε το μεθυσμένο και φοβήθηκε");
		addProverb("Μπάτε σκύλοι αλέστε");
		addProverb("Κύλησε ο τέντζερης και βρήκε το καπάκι");
		addProverb("Κάνε το καλό και ρίξτο στο γυαλό");
		addProverb("Η πολλή δουλειά τρώει τον αφέντη");
		addProverb("Σηκωθήκαν τα ποδια να χτυπήσουν το κεφάλι");
		addProverb("Όποιος δεν έχει μυαλό έχει πόδια");
		addProverb("Τώρα στα γεράματα, μάθε γέρο γράμματα");
		addProverb("Αγκάθια έχει στον κώλο του");
		addProverb("Αγαπά ο Θεός τον κλέφτη, αγαπά και τον νοικοκύρη");
		addProverb("Άνοιξε η γη και τον κατάπιε");
		addProverb("Άνθρωπος αγράμματος, ξύλο απελέκητο");
		addProverb("Βγάζει απ’ τη  μύγα ξίγκι");
		addProverb("Βουνό με βουνό δεν σμίγει");
		addProverb("Δεν έχει η γλώσσα κόκαλα και κόκαλα τσακίζει");
		addProverb("Δε δίνει του αγγέλου του νερό");
		addProverb("Δάσκαλε που δίδασκες και νόμο δεν εκράτεις");
		addProverb("Είναι διαβόλου κάλτσα");
		addProverb("Εγώ λέω στο σκύλο μου κι ο σκύλος στην ουρά του");
		addProverb("Εμπρός γκρεμός και πίσω ρέμα");
		addProverb("Ο γέρος πάει ή από πέσιμο ή από χέσιμο");
		addProverb("Η γριά η κότα έχει το ζουμί");
		addProverb("Πέρσι έκλασε, φέτος βρόμισε");
		addProverb("Το καλό πράγμα αργεί να γίνει");
		addProverb("Το γοργόν και χάριν έχει");
		addProverb("Όποιος νύχτα περπατεί, λάσπες και σκατά πατεί");
		addProverb("Τα πάχη μου, τα κάλλη μου");
		addProverb("Η καμήλα την καμπούρα της δεν την βλέπει");
		addProverb("Οι πουτάνες κι οι τρελές έχουν τις τύχες τις καλές");
		addProverb("Γυναίκα και καρπούζι η τύχη τα διαλέγει");
		addProverb("Χαζό παιδί, χαρά γεμάτο");
		addProverb("Χέσε ψηλά κι αγνάντευε");
		addProverb("Πρωτομυριστής και πρωτοκλαστής");
		addProverb("Ένας κούκος δεν φέρνει την Άνοιξη");
		addProverb("Οποιος γελά τελευταίος, γελά καλύτερα");
		addProverb("Φασούλι το φασούλι, γεμίζει το σακούλι");
		addProverb("Μάζευε κι ας ειν' και ρώγες");
		addProverb("Το έξυπνο πουλί από τη μύτη πιάνεται");
		addProverb("Πάρ' τον στο γάμο σου να σου πει και του χρόνου");
		addProverb("Στους στραβούς κυβερνάει ο μονόφθαλμος");
		addProverb("Νηστικό αρκούδι δε χορεύει");
		addProverb("Βοήθα με να σε βοηθώ ν' ανεβούμε το βουνό");
		addProverb("Μεγάλη μπουκιά μη φας και μεγάλο λόγο μη πεις");
		addProverb("Αν είσαι και παπάς με την αράδα σου θα πάς");
		addProverb("Αν δεν παινέψεις το σπίτι σου θα πέσει να σε πλακώσει");
		addProverb("Αν έχεις τύχη διάβαινε");
		addProverb("Από το στόμα σου και στου θεού στ’ αυτί");
		addProverb("Γουρούνι στο σακί");
		addProverb("Είπε ο γάιδαρος τον πετεινό κεφάλα");
		addProverb("Εκεί που είσαι ήμουνα και δω που είμαι θα ' ρθεις");
		addProverb("Έκλασε η νύφη, σχόλασε ο γάμος");
		addProverb("Έχω πολλά ράμματα για τη γούνα σου");
		addProverb("Ή μικρός παντρέψου, ή μικρός καλογερέψου");
		addProverb("Κάθ' ενός η πορδή, μόσχος του μυρίζει");
		addProverb("Κάλλιο να σου βγει το μάτι παρά το όνομα");
		addProverb("Κάποιου χαρίζανε ένα γάιδαρο και τον κοίταγε στα δόντια");
		addProverb("Κώλος κλασμένος γιατρός χεσμένος");
		addProverb("Με πορδές αυγά δε βάφονται");
		addProverb("Λείπει ο γάτος χορεύουν τα ποντίκια");
		addProverb("Ο βρεγμένος τη βροχή δεν τη φοβάται");
		addProverb("Ο καλός ο μύλος όλα τ’ αλέθει ");
		addProverb("Ο παπάς πρώτα βλογάει τα γένια του");
		addProverb("Ο λύκος στην αναμπουμπούλα χαίρεται");
		addProverb("Ο λόγος σου με χόρτασε και τα ψωμί σου φάτο");
		addProverb("Όλα του γάμου δύσκολα κι η νύφη γκαστρωμένη");
		addProverb("Οποίος μπαίνει στο χορό, χορεύει");
		addProverb("Οποίος πηδάει πολλά παλούκια ένα θα μπει στον κώλο του");
		addProverb("Όποιος σκάβει το λάκκο τ' αλλουνού, πέφτει ο ίδιος μέσα");
		addProverb("Όπου λαλούν πολλοί κοκκόροι αργεί να ξημερώσει");
		addProverb("Όπου ακούς πολλά κεράσια κράτα και μικρό καλάθι");
		addProverb("Ούτε ψύλλος στον κόρφο του");
		addProverb("Πέσε πίτα να σε φάω");
		addProverb("Πιάσ' τ' αυγό και κούρεψ' το");
		addProverb("Πιάστηκε σαν τον ποντικό στη φάκα");
		addProverb("Που πας ξιπόλητος στ' αγκάθια");
		addProverb("Σκυλί που γαβγίζει δεν δαγκώνει");
		addProverb("Στις εννιά του μακαρίτη άλλον έβαλε στο σπίτι");
		addProverb("Τι είν' ο κάβουρας τι είν' το ζουμί του");
		addProverb("Το σιγανό ποτάμι να φοβάσαι");
		addProverb("Αλλού τον τρώει, αλλού ξύνεται.");
		addProverb("Δε μου κάνει ούτε κρύο ούτε ζέστη");
		addProverb("Δεν ιδρώνει το αυτί του");
		addProverb("Θα βάλω τον σκύλο μου να κλαίει");
		addProverb("Πεινάω σαν λύκος");
		addProverb("Σώθηκε απ’ το στόμα του λύκου");
		addProverb("Με αυτό το πλευρό να κοιμάσε");
		addProverb("Θα γίνουμε απο 2 χωριά χωριάτες");
		addProverb("Ότι θυμάσαι χαίρεσαι");
		addProverb("Εγώ θα βγάλω το φιδι απο την τρύπα?");
		addProverb("Στο τέλος ξυρίζουν τον γαμπρό ");
		addProverb("Στου κουφού την πόρτα όσο θέλεις βρόντα");
		addProverb("Σαν θέλει η νύφη και ο γαμπρός");
		addProverb("Όπως έστρωσε θα κοιμηθεί");
		addProverb("Να πέσει φωτιά να με κάψει");
		addProverb("Απο το ένα μπαίνει και απο το άλλο βγαίνει");
		addProverb("Τα έφαγε τα ψωμιά του");
		addProverb("Με έλουσε κρύος ιδρώτας");
		addProverb("Καθε εμπόδιο για καλό");
		addProverb("Βγάζω την μπέρμπελη");
		addProverb("Έχει ο καιρός γυρίσματα");
		addProverb("Κυριακή κοντή γιορτή");
		addProverb("Την έκανε με ελαφρά πηδηματάκια");
		addProverb("Έφαγε πολλά χαστούκια απο την ζωή");
		addProverb("Με κόλλησε στον τοίχο");
		addProverb("Τα είπε χαρτί και καλαμάρι");
		addProverb("Μια χαρά και δυο τρομάρες ");
		addProverb("Πήραν τα μυαλά του αέρα");
		addProverb("Καλόμαθε η γριά στα σύκα");
		addProverb("Να τη πιεις στο ποτήρι");
		addProverb("Παω στο άγνωστο με βάρκα την ελπίδα");
		addProverb("Κουτουλάω απο την νύστα");
		addProverb("Πιασ' το αβγό και κούρεφ΄το");
		addProverb("Όταν εσυ πήγαινες εγώ ερχόμουν");
		addProverb("Κάλιο γαϊδουρόδερνε παρά γαϊδουρογύρευε");
		addProverb("Κουνήσου απο την θέση σου");
		addProverb("Μου ήρθε ο ουρανός σφοντύλι");
		addProverb("κακο σκυλι ψοφο δεν εχει");
		addProverb("Πίσω εχει η αχλάδα την ουρά");

	}

	/**
	 * 
	 */
	public void fillMoviesArray() {
		addMovie("Ο καλος ο κακος και ο ασχημος");
		addMovie("Στην φωλια του κουκου");
		addMovie("Ο ελαφοκυνηγος");
		addMovie("Λατερνα φτωχεια και φιλοτιμο");
		addMovie("Fight club");
		addMovie("Μυστικό παράθυρο");
		addMovie("Ψυχώ");
		addMovie("Αποκαλυψη τωρα");
		addMovie("Πίσω στο μέλλον");
		addMovie("Η ζωή ειναι ωραία");
		addMovie("Οι ζωές των άλλων");
		addMovie("Ο μονομαχος");
		addMovie("Σινεμά ο παράδεισος");
		addMovie("Η αιωνια λιακαδα ενοσ καθαρου μυαλου");
		addMovie("Μερικοί το προτιμούν καυτό");
		addMovie("Αδωξοι μπάσταρδη");
		addMovie("Καμία πατριδα για τους μελοθάνατους");
		addMovie("Ασυμβιβαστη γενια");
		addMovie("Η έκτη αισθηση");
		addMovie("Καζίνο");
		addMovie("Ο εξολοθρευτής");
		addMovie("Η ανατομία ενος εγκλήματος");
		addMovie("Οι τρεις ηλίθιοι");
		addMovie("Κάθε φορά , πρώτη φορά");
		addMovie("Ο βασιλιάς των λιονταριών ");
		addMovie("Η πενταμορφη και το τέρας");
		addMovie("Η λίμνη των κύκνων");
		addMovie("Το φάντασμα της όπερας");
		addMovie("Η νύχτα πριν τα χριστούγεννα");
		addMovie("Οσα παίρνει ο άνεμος");
		addMovie("Ο εξορκιστης");
		addMovie("Συνήθεις ύποπτοι");
		addMovie("Αμαρτολή πόλη");
		addMovie("Πως να εκπαιδεύσετε τον δράκο σας");
		addMovie("Στη φωλιά του κούκου ");
		addMovie("Η σιωπή των αμνών");
		addMovie("Τα σταφύλια της οργής");
		addMovie("Η νύχτα του κυνηγού");
		addMovie("Άγριες γράουλες");
		addMovie("Πολύ σκληρός για να πεθάνει");
		addMovie("Ημέρα εκπαίδευσης");
		addMovie("Επικινδυνες αποστολες ");
		addMovie("Η κατάρα");
		addMovie("Ξέρω τι έκανες πέρισι το καλοκαίρι");
		addMovie("Ανάλυσε το");
		addMovie("Ο άρχοντας των δαχτυλιδιών");
		addMovie("Εγώ ο απαισιότατος");
		addMovie("Το νησί των καταραμένων");
		addMovie("Η αρπαγή");
		addMovie("Η λάμψη");
		addMovie("Το πράσινο μίλι");
		addMovie("Ενας υπέροχος άνθροπος");
		addMovie("Κουρδιστό πορτοκάλι");
		addMovie("Η συμμορία των 11");
		addMovie("Το πέμπτο στοιχείο");
		addMovie("12 θυμωμένοι άντρες");
		addMovie("O νομοταγής πολίτης");
		addMovie("Πιάσε με αν μπορείς ");
		addMovie("Ο πιανίστας");
		addMovie("Παρθένος ετών 40");
		addMovie("Ματωμένο διαμάντι");
		addMovie("Ο πληροφοριοδότης");
		addMovie("Το φαινόμενο της πεταλούδας");
		addMovie("Το αγόρι με τη ριγέ πιτζάμα");
		addMovie("Ο ψαλιδοχέρης");
		addMovie("Μάτια ερμητικά κλειστά");
		addMovie("Βλέπω τον θάνατο σου");
		addMovie("Ο βράκος");
		addMovie("Ο κόκκινος δράκος");
		addMovie("Μόνος στο  σπίτι");
		addMovie("Το νησί");
		addMovie("Ο ναυαγός");
		addMovie("Συνέντευξη με ενα βρικόλακα");
		addMovie("21 γραμμάρια");
		addMovie("Ο δικηγόρος του διαβόλου");
		addMovie("Ο ελαφοκυνηγός");
		addMovie("Το ορφανοτροφείο");
		addMovie("Ο ακέφαλος καβαλάρης");
		addMovie("Ο δρόμος της απώλειας");
		addMovie("Γάμος...αλά Ελληνικά");
		addMovie("Εγώ, αυτή και ο εαυτός μου");
		addMovie("Ο ανθρωπος ελέφαντας");
		addMovie("Η κόρη μου η σοσιαλίστρια");
		addMovie("Η αρχόντισσα και ο αλήτης");
		addMovie("Ο άνθρωπος που έσπαγε πλάκα");
		addMovie("Ο άνθρωπος που γύρισε απο τη ζέστη");
		addMovie("Ο Στρίγγλος που έγινε αρνάκι ");
		addMovie("Καπετάν φάντης μπαστούνι");
		addMovie("Ο τρελός τα 'χει 400");
		addMovie("Πατέρα κάτσε φρόνιμα");
		addMovie("Το πιο λαμπρό αστέρι ");
		addMovie("Μοντέρνα Σταχτοπούτα. . .");
		addMovie("Η σωφερίνα ");
		addMovie("Για ποιόν χτυπάει η... κουδούνα ");
		addMovie("Ο ψεύτης ");
		addMovie("Ο άνθρωπος που έτρεχε πολύ");
		addMovie("Πάρε κόσμε");
		addMovie("Η κατάρα της μάνας");
		addMovie("Ρόδα, τσάντα και κοπάνα");
		addMovie("Αγάπησα μια πολυθρόνα");
		addMovie("Ζητείται τίμιος");
		addMovie("Το άγγιγμα του κακού");
		addMovie("ο ασυμβίβαστος");
		addMovie("Η πόλλη των αγγέλων");
		addMovie("Εφιάλτης στο δρόμο με τις λεύκες");
		addMovie("Ο ταχυδρόμος χτυπάει παντα 3 φορές");
		addMovie("Τα στίγματα");
		addMovie("Καλύτερα δεν γίνεται");
		addMovie("Ο κόκκινος δράκος");
		addMovie("Η Οδύσσεια ενός ξεριζωμένου");
		addMovie("περιφρόνηση");
		addMovie("Τα χρονια της αθωότητας");
		addMovie("Πέρα από τα σύνορα");
		addMovie("Μικρά εγκλήματα μεταξύ φίλων");
		addMovie("Μια πορνογραφική σχέση");
		addMovie("Τα όμορφα χωριά όμορφα καίγονται");
		addMovie("Η θάλασσα μέσα μου");
		addMovie("Ο πρίγκηπας της παλίρροιας");
		addMovie("Όσα χάσαμε στις φλόγες");
		addMovie("Eξομολογήσεις εραστών");
		addMovie("Περηφάνεια και προκατάληψη");
		addMovie("Έχετε κάνει κράτηση?");
		addMovie("Η νύχτα των ζωντανών νεκρών");
		addMovie(" Ο ηλίθιος και ο πανηλίθιος");
		addMovie("Λουκουμάδες με μέλι");
		addMovie("Εδώ και τώρα αγγούρια");
		addMovie("Ο νάνος και οι επτά Χιονάτες");
		addMovie("Ερωτιάρης από κούνια");
		addMovie("Υπάρχει και φιλότιμο");
		addMovie("Το κανόνι και τ'αηδόνι");
		addMovie("Τσιμπήστε μας και αφήστε μας");
		addMovie("Ένας απένταρος λεφτάς");
		addMovie("Εθνική παπάδων");
		addMovie("Ξενοδοχείο για Τέρατα");
		addMovie("Η Πηγή της ζωής");
		addMovie("Ο Δικτάτορας");
		addMovie("Η απολυτη ευφυια");
		addMovie("Μέχρι την ακρη του κόσμου");
		addMovie("Παιχνίδι Θανάτου");
		addMovie("Αόρατος εραστής");
		addMovie("Ο τελευταιος σαμουράϊ");
		addMovie("Το στοιχειωμένο σπίτι");
		addMovie("Η αδερφή μου και εγώ");
		addMovie("Οι φωνές των νεκρών");
		addMovie("Μανιακός δολοφόνος");
		addMovie("Το κορίτσι της διπλανής πόρτας");
		addMovie("Βρέχει Κεφτέδες");
		addMovie("Μπαμπά μην τρέχεις");
		addMovie("Ο φυγάς");
		addMovie("Ο εφιάλτης");
		addMovie("Σε τεντωμένο σκοινί");
		addMovie("Το βαρύ πεπόνι");
		addMovie("Το κυνήγι του λαγού");
		addMovie("Η στρίγγλα που έγινε αρνάκι");
		addMovie("Η Απαγωγή");
		addMovie("Ένα βότσαλο στην λίμνη");
		addMovie("Το μάτι της γάτα");
		addMovie("Η αποζημίωση");
		addMovie("Ο δολοφόνος με το τσεκούρι");
		addMovie("Ο βιολιστής στην στέγη");
		addMovie("Το μαντολινο του λοχαγού Κορέλι");
		addMovie("στη σκια των τεσσαρων γιγαντων");
		addMovie("Τα κόκκινα φανάρια");
		addMovie("Με κομμένη την ανάσα");
		addMovie("Ο πόλεμος των κόσμων ");
		addMovie("Το λιοντάρι τον χειμώνα");
		addMovie("Πόλεμος και ειρήνη");
		addMovie("Σκοτεινοί δρόμοι");
		addMovie("Το όνομα του ρόδου");
		addMovie("Το σκηνικό του τρόμου");
		addMovie("Προσοχή ... δαγκώνει");
		addMovie("ένας υπεροχος ανθρωπος");
		addMovie("Ο πόλεμος των άστρων");
		addMovie("Μάτριξ");
		addMovie("Οδηγός ταξί-Taxi Driver");
		addMovie("Με Διπλή Ταυτότητα ");
		addMovie("Τραγουδώντας στην βροχή");
		addMovie("κλέφτης ποδηλάτων");
		addMovie("Ο στρατηγός");
		addMovie("Η μελωδία της ευτυχίας");
		addMovie("Το πράγμα");
		addMovie("Πετα τη μαμα απ'το τρενο");
		addMovie("Τρελες σφαιρες");
		addMovie("Η μεγαλη των μπατσων σχολη");
		addMovie("Φτάνει να είμαστε μαζί");
		addMovie("Έχετε κάνει κράτηση;");
		addMovie("Το όνομα του ρόδου");
		addMovie("Μια αιωνιότητα και μια ημέρα");
		addMovie("Ο άνθρωπος που δεν ήταν εκεί");
		addMovie("Ο μακρύς δρόμος του γυρισμού");
		addMovie("Eξομολογήσεις εραστών");
		addMovie("Το άλλοθι");
		addMovie("Οι Μαχητές του Δράκου");
		addMovie("Η Κατάρα του Χρυσού Λουλουδιού");
		addMovie("Η Ωραιότερη Ιστορία του Κόσμο");
		addMovie("Το Τερατόσπιτο ");
		addMovie("Η πρώτη φορά που πέθανα");
		addMovie("Ένοχη Μνήμη");
		addMovie("Απόλυτος τρόμος");
		addMovie("Θεός για μια εβδομάδα ");
		addMovie(" Ζωές σε άσπρο μαύρο");
		addMovie("Θα Ζήσω");
		addMovie("Αν...");
		addMovie(" Για πάντα νέοι");
		addMovie("Ένας για όλες");
		addMovie("Άγρυπνος");
		addMovie("Η έπαυλη");
		addMovie("Τρέξε γρήγορα");
		addMovie("Αλεξίσφαιροι Ντετέκτιβ");
		addMovie("Καρχαριομάχος");
		addMovie("Η 7η πύλη της Κολάσεως");
		addMovie("Απόγνωση");
		addMovie(" Διπλά ψέματα");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
		// addMovie("");
	}

	/**
	 * 
	 * @param proverb
	 */
	public void addProverb(String proverb) {
		if (!proverbs.contains(proverb)) {
			proverbs.add(proverb);
		}
	}

	/**
	 * 
	 * @param song
	 */
	public void addSong(String song) {
		if (!songs.contains(song)) {
			songs.add(song);
		}
	}

	/**
	 * 
	 * @param movie
	 */
	public void addMovie(String movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
		}
	}

	public void addxxx(String title) {
		if (!xxx.contains(title)) {
			xxx.add(title);
		}
	}

	/**
	 * 
	 */
	public static void fillallArraysElements(ArrayList<String> allArraysElements) {
		for (String movie : movies) {
			allArraysElements.add(movie);
		}

		for (String proverb : proverbs) {
			allArraysElements.add(proverb);
		}
		for (String song : songs) {
			allArraysElements.add(song);
		}
	}

	private void fillallArraysElements() {
		for (String movie : movies) {
			allArraysElements.add(movie);
		}

		for (String proverb : proverbs) {
			allArraysElements.add(proverb);
		}
		for (String song : songs) {
			allArraysElements.add(song);
		}
	}

	/**
	 * 
	 */
	private OnClickListener canselListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			context.gotoMenuWhilePlaying();
			timer.setEarlyStop(true);

			// canselButton.setVisibility(INVISIBLE);
			// continueButton.setVisibility(INVISIBLE);

		}
	};

	public void continuePandomima() {
		if (state == END_GAME) {
			if (thread == null || !thread.isAlive()) {
				timer.setEarlyStop(true);
			}
			thread = new Thread(timer);
			thread.start();
		}

		// state++;
		setBackgroundColor(Color.WHITE);

	}

	/**
 * 
 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int countChild = getChildCount();
		for (int i = 0; i < countChild; i++) {
			View child = getChildAt(i);
			if (child == continueButton) {
				child.layout(getWidth() / 2 + getWidth() / 10, getHeight()
						- getHeight() / 7, r - getWidth() / 30, b - getHeight()
						/ 30);

				continueButton.setTextSize(continueButton.getHeight() / 5);
			}
			if (child == menuButton) {
				child.layout(getWidth() / 10, getHeight() - getHeight() / 7,
						getWidth() / 2 - getWidth() / 30, b - getHeight() / 30);
				menuButton.setTextSize(menuButton.getHeight() / 5);

			}

			if ((child instanceof Question) && child == screen) {
				child.layout(0, getHeight() / 7, r, getHeight() - getHeight()
						/ 7);
			}
			if (child == timer) {
				child.layout(0, 0, r, getHeight() / 7);
			}

			if (child == correct) {

				child.layout(getWidth() / 5, getHeight() / 10, r / 2 + r / 3,
						getHeight() / 2);
			}
			if (child == wrong) {
				child.layout(getWidth() / 5, getHeight() / 2, r / 2 + r / 3,
						getHeight() / 2 + getHeight() / 3);
			}

			for (int j = 0; j < choseTypeOfQuestion.length; j++) {
				if (child == choseTypeOfQuestion[j]) {
					child.layout(getWidth() / 10, (j) * getHeight() / 6
							+ getHeight() / 15,
							getWidth() / 2 + getWidth() / 3, (j) * getHeight()
									/ 6 + getHeight() / 6 + getHeight() / 15);
				}
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
		Bitmap mBitmap = null;

		if (isTimeToShowScore) {
			canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(),
					paint);
			paint.setTextSize(getWidth() / 2 / 17);
			canvas.drawText("παιχτης 1 βαθμολογια", 0, getHeight() / 30, paint);
			paint.setTextSize(10);

			for (int i = 0; i < teamAScore.size(); i++) {
				paint.setColor(Color.BLACK);

				canvas.drawText(Integer.toString(teamAScore.get(i)),
						getWidth() / 30, getHeight() / 15 + i * 32, paint);
			}

			paint.setTextSize(20);

			if (teamAScore.size() > 0) {
				canvas.drawLine(0, getHeight() / 15 + teamAScore.size() * 32
						- 30, getWidth() / 2,
						getHeight() / 15 + teamAScore.size() * 32 - 30, paint);
				canvas.drawText(Integer.toString(this.scoreATeam),
						getWidth() / 80, getHeight() / 15 + teamAScore.size()
								* 32, paint);
			}
			// paint.setTextSize(10);

			paint.setTextSize(getWidth() / 2 / 17);
			canvas.drawText("παιχτης 2 βαθμολογια", getWidth() / 2,
					getHeight() / 30, paint);
			paint.setTextSize(10);

			for (int i = 0; i < teamBScore.size(); i++) {
				canvas.drawText(Integer.toString(teamBScore.get(i)), getWidth()
						/ 2 + getWidth() / 30, getHeight() / 15 + i * 32, paint);
				// canvas.drawText(Integer.toString(playerTwoGrades.get(i)),
				// getWidth()/20, i*getHeight()/30+30, paint);
			}
			paint.setTextSize(20);
			if (teamBScore.size() > 0) {
				canvas.drawLine(getWidth() / 2,
						getHeight() / 15 + teamBScore.size() * 32 - 30,
						getWidth(), getHeight() / 15 + teamBScore.size() * 32
								- 30, paint);
				canvas.drawText(Integer.toString(this.scoreBTeam), getWidth()
						/ 2 + getWidth() / 80,
						getHeight() / 15 + (teamBScore.size()) * 32, paint);
			}

		}
		if (readyToDrawWhoTurnIs) {
			paint.setTextSize(35);
			String txt1 = null;
			// String txt2=" ειστε έτοιμοι";
			// String txt2="πάτα συνέχεια";
			String txt3 = " ( εμφάνηση λέξης πανω )";
			int forGroundColor = 0;
			if (isFirstTeamTurn) {
				txt1 = "Ομάδα Α, πάτα συνέχεια  ";
				forGroundColor = Color.BLUE;
				try {
//					if (teamABitmap == null) {
	teamABitmap  = BitmapFactory
			.decodeResource(getResources(),
										R.drawable.teama);
//					}
					mBitmap = teamABitmap;
				} catch (OutOfMemoryError e) {
				}
			} else {
				txt1 = "Ομάδα Β, πάτα συνέχεια ";
				forGroundColor = Color.DKGRAY;
				try {

						teamBBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.bteam3);

					mBitmap = teamBBitmap;
				} catch (OutOfMemoryError e) {
				}
			}
				float scaleX=(getWidth()/(float)(mBitmap.getWidth()+mBitmap.getWidth()/10));
				float scaleY=(getHeight()/(float)(2*mBitmap.getHeight()));

				mBitmap=getResizedBitmap(mBitmap,scaleX,scaleY);
				canvas.drawBitmap(mBitmap, 0, getHeight() / 4, paint);

			paint.setTextSize(getWidth() / 15);
			paint.setColor(forGroundColor);
			canvas.drawText(txt1, 0, getHeight() / 10, paint);
			// canvas.drawText(txt2, 0, getHeight()/7, paint);
			paint.setTextSize(20);
			canvas.drawText(txt3, 0, getHeight() / 5, paint);
			// canvas.drawText(txt4, 0, getHeight()/4, paint);
		}

		if (isGameOver) {
			try {
				if (gameOverBitmap == null) {
					gameOverBitmap = BitmapFactory.decodeResource(
							getResources(), R.drawable.wnr);
				}
				mBitmap = gameOverBitmap;
				canvas.drawBitmap(mBitmap, 0, 0, paint);
			} catch (OutOfMemoryError e) {
			}
			paint.setTextSize(20);
			canvas.drawText(
					"Ο Παιχτης 1 εχει  : " + Integer.toString(scoreATeam)
							+ " βαθμους", getWidth() / 20, getHeight() / 7,
					paint);
			canvas.drawText(
					"Ο Παιχτης 2 εχει : " + Integer.toString(scoreBTeam)
							+ " βαθμους", getWidth() / 20, getHeight() / 4,
					paint);
			paint.setTextSize(getWidth() / 15);
			if (scoreATeam > scoreBTeam) {
				canvas.drawText(" O παιχτης 1 ειναι ο νικητης   ",
						getWidth() / 20, getHeight() / 2, paint);
			} else if (scoreATeam < scoreBTeam) {
				canvas.drawText(" O παιχτης 2 ειναι ο νικητης   ",
						getWidth() / 20, getHeight() / 2, paint);
			} else {
				canvas.drawText("  ισοπαλια ", getWidth() / 20,
						getHeight() / 2, paint);
			}
		}

	}

	public Bitmap getResizedBitmap(Bitmap bm, float scaleX, float scaleY) {
		Bitmap resizedBitmap;
		try{
		int width = bm.getWidth();
		int height = bm.getHeight();
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleX, scaleY);

		// "RECREATE" THE NEW BITMAP
		 resizedBitmap = Bitmap.createBitmap(
				bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		}catch (Exception e){
e.printStackTrace();
		resizedBitmap=bm;
		}
		System.gc();
		return resizedBitmap;
	}

	public boolean isGameOver() {
		return this.isGameOver;
	}

	public void setIsGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	private void enterOnxxx() {
		AlertDialog.Builder alert;
		alert = new AlertDialog.Builder(context);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setTitle("Entering Activity");
		alert.setMessage("Είσαι πάνω απο 18?");

		alert.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedArray = xxx;
				removeButtonsTypeOfQuestion();
				readyToDrawWhoTurnIs = true;
				addComponentWhoNeededToStartPlaying();

			}

		});

		alert.setNegativeButton("Οχι", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});

		alert.show();
	}

	public ArrayList<String> getAllPandomimes() {
		return allArraysElements;
	}

	public int getRemainingTime() {
		return timer.getTimeLeft();
	}

	public void release() {
		timer.setEarlyStop(true);
	}
}
