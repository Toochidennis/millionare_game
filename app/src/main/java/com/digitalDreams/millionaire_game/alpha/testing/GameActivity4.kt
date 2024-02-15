package com.digitalDreams.millionaire_game.alpha.testing

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.digitalDreams.millionaire_game.AdManager
import com.digitalDreams.millionaire_game.CountDownActivity
import com.digitalDreams.millionaire_game.DBHelper
import com.digitalDreams.millionaire_game.ExitGameDialog
import com.digitalDreams.millionaire_game.FailureActivity
import com.digitalDreams.millionaire_game.R
import com.digitalDreams.millionaire_game.Utils
import com.digitalDreams.millionaire_game.Utils.ARABIC_KEY
import com.digitalDreams.millionaire_game.WinnersActivity
import com.digitalDreams.millionaire_game.WrongAnswerDialog
import com.digitalDreams.millionaire_game.alpha.AudioManager
import com.digitalDreams.millionaire_game.alpha.AudioManager.playFailureSound
import com.digitalDreams.millionaire_game.alpha.AudioManager.playSuccessSound
import com.digitalDreams.millionaire_game.alpha.Constants
import com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_LONG
import com.digitalDreams.millionaire_game.alpha.Constants.DELAY_INTERVAL_MEDIUM
import com.digitalDreams.millionaire_game.alpha.Constants.FAILED
import com.digitalDreams.millionaire_game.alpha.Constants.FROM_PROGRESS
import com.digitalDreams.millionaire_game.alpha.Constants.GREEN
import com.digitalDreams.millionaire_game.alpha.Constants.ORANGE
import com.digitalDreams.millionaire_game.alpha.Constants.PASSED
import com.digitalDreams.millionaire_game.alpha.Constants.PREF_NAME
import com.digitalDreams.millionaire_game.alpha.Constants.RED
import com.digitalDreams.millionaire_game.alpha.Constants.generateAmount
import com.digitalDreams.millionaire_game.alpha.Constants.getBackgroundDrawable
import com.digitalDreams.millionaire_game.alpha.Constants.getLabelFromList
import com.digitalDreams.millionaire_game.alpha.Constants.getLanguageText
import com.digitalDreams.millionaire_game.alpha.Constants.getRandomSuggestion
import com.digitalDreams.millionaire_game.alpha.ExplanationBottomSheetDialog
import com.digitalDreams.millionaire_game.alpha.activity.ProgressActivity2
import com.digitalDreams.millionaire_game.alpha.adapters.OnOptionsClickListener
import com.digitalDreams.millionaire_game.alpha.models.OptionsModel
import com.digitalDreams.millionaire_game.alpha.testing.database.DatabaseProvider
import com.digitalDreams.millionaire_game.alpha.testing.database.Question
import com.digitalDreams.millionaire_game.alpha.testing.database.QuestionDao
import com.digitalDreams.millionaire_game.databinding.ActivityGame3Binding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class GameActivity4 : AppCompatActivity(), OnOptionsClickListener {

    private lateinit var exitButton: RelativeLayout
    private lateinit var timerContainer: RelativeLayout
    private lateinit var amountContainer: RelativeLayout
    private lateinit var questionTextView: TextView
    private lateinit var amountWonTextView: TextView
    private lateinit var questionProgressTextView: TextView
    private lateinit var countdownTextView: TextView
    private lateinit var minus2QuestionsButton: RelativeLayout
    private lateinit var askComputerButton: RelativeLayout
    private lateinit var takeAPollButton: RelativeLayout
    private lateinit var resetQuestionButton: RelativeLayout
    private lateinit var askComputerContainer: RelativeLayout
    private lateinit var suggestionTextView: TextView
    private lateinit var optionTextView: TextView
    private lateinit var questionCardView: CardView
    private lateinit var lifeLineDescriptionTextView1: TextView
    private lateinit var lifeLineDescriptionTextView2: TextView
    private lateinit var lifeLineDescriptionTextView3: TextView
    private lateinit var lifeLineDescriptionTextView4: TextView
    private lateinit var votingContainer: LinearLayout
    private lateinit var progressBarA: ProgressBar
    private lateinit var progressBarB: ProgressBar
    private lateinit var progressBarC: ProgressBar
    private lateinit var progressBarD: ProgressBar
    private lateinit var textViewA: TextView
    private lateinit var textViewB: TextView
    private lateinit var textViewC: TextView
    private lateinit var textViewD: TextView
    private lateinit var minus2QuestionsImageView: ImageView
    private lateinit var askComputerImageView: ImageView
    private lateinit var takeAPollImageView: ImageView
    private lateinit var refreshImageView: ImageView
    private lateinit var refreshVideoImageView: ImageView
    private lateinit var optionsRecyclerView: RecyclerView
    private lateinit var adView: AdView

    private lateinit var optionsAdapter2: OptionsAdapter2
    private lateinit var sharedPreferences: SharedPreferences
    private var question: Question? = null
    private lateinit var questionDao: QuestionDao
    private lateinit var dbHelper: DBHelper

    private var options = mutableListOf<OptionsModel>()
    private var amounts = mutableListOf<Int>()
    private var amountWonList = mutableListOf<Int>()
    private var questions = mutableListOf<Question>()

    private var questionIndex = 0
    private var numberOfFailure = 0
    private var numberOfPassed = 0
    private var numberOfAnswered = 0
    private var optionsClickable = true
    private var hasAskedComputer = false
    private var hasTakenPoll = false
    private var hasMinus2Question = false
    private var amountWonText = 0
    private var selectedAnswer: String? = null
    private var startTimeMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null

    private var _binding: ActivityGame3Binding? = null

    private val binding = _binding!!

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var gameActivity: GameActivity4
            private set
    }


    override fun onStart() {
        super.onStart()
        initializeAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initializeGame()
        } else {
            resumeGame()
        }

        gameActivity = this
    }

    private fun initializeGame() {
        startCountDownActivity()
        initializeViews()
        initializeDatabase()
        loadQuestions()
        setRootViewBackgroundColor()
        handleViewClicks()
        startTimeMillis = System.currentTimeMillis()
    }

    private fun resumeGame() {
        initializeViews()
        initializeDatabase()
        setRootViewBackgroundColor()
        handleViewClicks()
        restoreSavedProgress()
    }

    private fun initializeViews() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())

        setContentView(R.layout.activity_game3)

        exitButton = findViewById(R.id.exitBtn)
        timerContainer = findViewById(R.id.timer_container)
        countdownTextView = findViewById(R.id.time)
        amountContainer = findViewById(R.id.amount_container)
        amountWonTextView = findViewById(R.id.amount_won_text)
        questionTextView = findViewById(R.id.question_text)
        questionCardView = findViewById(R.id.question_card_view)
        minus2QuestionsButton = findViewById(R.id.minus_two_questions)
        askComputerButton = findViewById(R.id.ask_computer)
        takeAPollButton = findViewById(R.id.take_a_poll)
        resetQuestionButton = findViewById(R.id.reset_question)
        minus2QuestionsImageView = findViewById(R.id.bad1)
        askComputerImageView = findViewById(R.id.bad2)
        takeAPollImageView = findViewById(R.id.bad3)
        askComputerContainer = findViewById(R.id.ask_answer_container)
        suggestionTextView = findViewById(R.id.suggestion_text)
        optionTextView = findViewById(R.id.option_text)
        votingContainer = findViewById(R.id.voting_container)
        progressBarA = findViewById(R.id.progress_bar1)
        progressBarB = findViewById(R.id.progress_bar2)
        progressBarC = findViewById(R.id.progress_bar3)
        progressBarD = findViewById(R.id.progress_bar4)
        textViewA = findViewById(R.id.progress_text1)
        textViewB = findViewById(R.id.progress_text2)
        textViewC = findViewById(R.id.progress_text3)
        textViewD = findViewById(R.id.progress_text4)
        lifeLineDescriptionTextView1 = findViewById(R.id.option_description_1)
        lifeLineDescriptionTextView2 = findViewById(R.id.option_description_2)
        lifeLineDescriptionTextView3 = findViewById(R.id.option_description_3)
        lifeLineDescriptionTextView4 = findViewById(R.id.option_description_4)
        refreshImageView = findViewById(R.id.refresh_imageview)
        refreshVideoImageView = findViewById(R.id.video_imageview)
        optionsRecyclerView = findViewById(R.id.options_recyclerView)
        questionProgressTextView = findViewById(R.id.question_progress)
        adView = findViewById(R.id.adView)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        adView.loadAd(createAdRequest())
    }

    private fun initializeDatabase() {
        val appDatabase = DatabaseProvider.getInstance(this)
        questionDao = appDatabase.questionDao()
        dbHelper = DBHelper(this)
    }

    private fun initializeAds() {
        AdManager.loadInterstitialAd(this)
        AdManager.loadRewardedAd(this)
    }

    private fun setRootViewBackgroundColor() {
        val startColor = sharedPreferences.getInt(
            "start_color",
            ContextCompat.getColor(this, R.color.purple_500)
        )
        val endColor =
            sharedPreferences.getInt("end_color", ContextCompat.getColor(this, R.color.purple_dark))
        val rootView = findViewById<RelativeLayout>(R.id.rootView)
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(startColor, endColor)
        )
        rootView.background = gradientDrawable
    }

    private fun getAmounts() {
        val gameLevel = sharedPreferences.getString("game_level", "1")
        val level = gameLevel?.toInt()
        amounts = level?.let { generateAmount(it) }!!
    }

    private fun handleViewClicks() {
        minus2QuestionsButton.setOnClickListener { hideTwoQuestions() }
        resetQuestionButton.setOnClickListener { skipQuestion() }
        askComputerButton.setOnClickListener { askComputer() }
        takeAPollButton.setOnClickListener { takeAPoll() }
        exitButton.setOnClickListener { showExitDialog() }

        amountContainer.setOnClickListener {
            startActivity(
                Intent(this, ProgressActivity2::class.java)
                    .putIntegerArrayListExtra("amount_won", ArrayList(amountWonList))
                    .putExtra("should_use_timer", false)
            )
        }
    }

    private fun loadQuestions() {
        val languageCode = sharedPreferences.getString("language", "")
        val language = getLanguageText(this, languageCode)
        questions.clear()

        CoroutineScope(Dispatchers.Default).launch {
            for (level in 1..15) {
                val question = questionDao.getQuestionByLanguageAndLevel(
                    language = language!!,
                    level = level.toString()
                )
                questions.add(question)
            }

            withContext(Dispatchers.Main) {
                showQuestion()
            }
        }
    }

    private fun showQuestion() {
        updateAmountWon()
        parseQuestion()
        animateViews()
        startCountdownTimerIfGameModeIsTimed()
        saveGameProgress()
    }

    private fun parseQuestion() {
        question = questions[questionIndex]

        question?.let {
            questionTextView.text = it.question
            val option = it.options

            options.clear()
            options.add(OptionsModel(option.a))
            options.add(OptionsModel(option.b))
            options.add(OptionsModel(option.c))
            options.add(OptionsModel(option.d))

            inflateOptions()
        }
    }

    private fun inflateOptions() {
        optionsAdapter2 = OptionsAdapter2(options, this)
        optionsRecyclerView.apply {
            hasFixedSize()
            adapter = optionsAdapter2
        }
    }

    override fun onOptionClick(position: Int, itemView: View) {
        if (optionsClickable) {
            itemView.background = getBackgroundDrawable(ORANGE, itemView)
            itemView.postDelayed({ handleUserSelection(position, itemView) }, DELAY_INTERVAL_LONG)
            optionsClickable = false
            updateFirstTimeState()
        }
    }

    private fun handleUserSelection(position: Int, itemView: View) {
        selectedAnswer = options[position].optionText.trim()
        val correctAnswer = question?.correctAnswer?.trim()

        if (selectedAnswer.equals(correctAnswer, ignoreCase = true)) {
            itemView.background = getBackgroundDrawable(GREEN, itemView)
            playSuccessSound(this)
            showExplanationDialog(PASSED)
        } else {
            itemView.background = getBackgroundDrawable(RED, itemView)
            playFailureSound(this)
            showExplanationDialog(FAILED)
        }
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java)
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(
                    VibrationEffect.createOneShot(
                        DELAY_INTERVAL_MEDIUM,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(DELAY_INTERVAL_MEDIUM)
            }
        }
    }

    private fun showExplanationDialog(from: String) {
        vibrate()
        cancelTimer()

        val explanationDialog = ExplanationBottomSheetDialog(this, question)

        if (from == PASSED) {
            explanationDialog.show()

            explanationDialog.setOnDismissListener { dialog ->
                dialog.dismiss()

                Handler(Looper.getMainLooper()).postDelayed(
                    { startProgressActivity() },
                    DELAY_INTERVAL_LONG
                )
            }

            question?.let {
                saveHistory(
                    questionId = it.questionId,
                    answer = selectedAnswer ?: "",
                    correctAnswer = it.correctAnswer.trim(),
                    highScore = amountWonText.toString(),
                    isCorrect = true
                )
            }

        } else {
            startFailureActivity(explanationDialog)

            question?.let {
                saveHistory(
                    questionId = it.questionId,
                    answer = selectedAnswer ?: "",
                    correctAnswer = it.correctAnswer.trim(),
                    highScore = amountWonText.toString(),
                    isCorrect = false
                )
            }
        }
    }

    private fun startProgressActivity() {
        amountWonText = amounts[numberOfPassed]
        amountWonList.add(amountWonText)

        if (numberOfPassed == 14) {
            startWinnersActivity()
        } else {
            updateRefreshQuestionState(false)
            updateShouldContinueGame(true)
            updateProgressState(true)

            startActivity(
                Intent(this, ProgressActivity2::class.java)
                    .putIntegerArrayListExtra("amount_won", ArrayList(amountWonList))
                    .putExtra("should_use_timer", true)
            )

            questionIndex++
            numberOfPassed++
            numberOfAnswered++
        }

        saveNewAmountWon()
        optionsClickable = true
        updateMusicState(true)
    }

    private fun startFailureActivity(explanationBottomSheetDialog: ExplanationBottomSheetDialog) {
        if (numberOfFailure < 1) {
            showFailureDialog()
            numberOfFailure++
        } else {
            AudioManager.pauseBackgroundMusic()
            explanationBottomSheetDialog.show()

            explanationBottomSheetDialog.setOnDismissListener { dialog: DialogInterface ->
                dialog.dismiss()

                numberOfFailure = 0
                startActivity(Intent(this, FailureActivity::class.java))
            }
            updateRefreshQuestionState(true)
            updateShouldContinueGame(false)
        }
        optionsClickable = true
        updateProgressState(false)
        clearSavedProgress()
    }

    private fun startWinnersActivity() {
        val intent = Intent(this, WinnersActivity::class.java)
        intent.putExtra("isWon", true)
        intent.putExtra("isShowAd", false)
        startActivity(intent)
        saveTotalAmountWon(amountWonText)
    }

    private fun showFailureDialog() {
        AudioManager.pauseBackgroundMusic()
        val wrongAnswerDialog = WrongAnswerDialog(this, question)
        wrongAnswerDialog.setCancelable(false)
        wrongAnswerDialog.show()
    }

    private fun updateAmountWon() {
        val formattedAmount =
            String.format(Locale.getDefault(), "$%s", Constants.prettyCount(amountWonText))
        amountWonTextView.text = formattedAmount
        updateQuestionProgress()
    }

    private fun updateQuestionProgress() {
        val progress = String.format(
            Locale.getDefault(), "%d/%s",
            questionIndex + 1,
            resources.getString(R.string._15)
        )
        questionProgressTextView.text = progress
    }

    private fun saveNewAmountWon() {
        sharedPreferences.edit().apply {
            putString("amountWon", amountWonText.toString())
            putBoolean("hasOldWinningAmount", true)
            putInt("noOfCorrect", numberOfPassed)
            putInt("noOfAnswered", numberOfAnswered)
            apply()
        }
    }

    private fun saveTotalAmountWon(totalAmountWon: Int) {
        sharedPreferences.edit().apply {
            putInt("totalAmountWon", totalAmountWon)
            apply()
        }
    }

    private fun shouldContinueGame(): Boolean {
        return getSharedPreferences(Constants.APPLICATION_DATA, MODE_PRIVATE)
            .getBoolean(Constants.SHOULD_CONTINUE_GAME, true)
    }

    private fun isShouldRefreshQuestion(): Boolean {
        return getSharedPreferences(Constants.APPLICATION_DATA, MODE_PRIVATE)
            .getBoolean(Constants.SHOULD_REFRESH_QUESTION, false)
    }

    private fun isFromProgress(): Boolean {
        return sharedPreferences.getBoolean(FROM_PROGRESS, false)
    }

    private fun updateProgressState(progressState: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(FROM_PROGRESS, progressState)
        }.apply()
    }

    private fun shouldPlayMusic(): Boolean {
        return sharedPreferences.getBoolean(Constants.SOUND, false)
    }

    private fun updateMusicState(soundState: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(Constants.SOUND, soundState)
        }.apply()
    }

    private fun updateRefreshQuestionState(refreshState: Boolean) {
        getSharedPreferences(Constants.APPLICATION_DATA, MODE_PRIVATE)
            .edit().apply {
                putBoolean(Constants.SHOULD_REFRESH_QUESTION, refreshState)
                apply()
            }
    }

    private fun updateShouldContinueGame(savedState: Boolean) {
        getSharedPreferences(Constants.APPLICATION_DATA, MODE_PRIVATE)
            .edit().apply {
                putBoolean(Constants.SHOULD_CONTINUE_GAME, savedState)
                apply()
            }
    }

    private fun gameMode(): String? {
        return sharedPreferences.getString("game_mode", "0")
    }

    private fun isFirstTime(): Boolean = sharedPreferences.getBoolean("is_first_time", false)

    private fun updateFirstTimeState() {
        sharedPreferences.edit().apply {
            putBoolean("is_first_time", false)
            apply()
        }
    }

    private fun saveGameProgress() {
        getSharedPreferences("progress", MODE_PRIVATE).edit().apply {
            putInt("questionIndex", questionIndex)
            putInt("numberOfPassed", numberOfPassed)
            putInt("numberOfFailure", numberOfFailure)
            putInt("numberOfAnswered", numberOfAnswered)
            putBoolean("hasAskedComputer", hasAskedComputer)
            putBoolean("hasTakenPoll", hasTakenPoll)
            putBoolean("isMinus2Questions", hasMinus2Question)
            putInt("amountWonText", amountWonText)
            putLong("startTimeMillis", startTimeMillis)

            val gson = Gson()
            val json = gson.toJson(questions)
            putString("question_list", json)

            apply()
        }
    }

    private fun restoreSavedProgress() {
        val sharedPreferences = getSharedPreferences("progress", MODE_PRIVATE)
        with(sharedPreferences) {
            questionIndex = getInt("questionIndex", 0)
            numberOfPassed = getInt("numberOfPassed", 0)
            numberOfFailure = getInt("numberOfFailure", 0)
            numberOfAnswered = getInt("numberOfAnswered", 0)
            hasAskedComputer = getBoolean("hasAskedComputer", false)
            hasTakenPoll = getBoolean("hasTakenPoll", false)
            hasMinus2Question = getBoolean("isMinus2Questions", false)
            amountWonText = getInt("amountWonText", 0)
            startTimeMillis = getLong("startTimeMillis", 0)
            val json = getString("question_list", "")

            val gson = Gson()
            val type = object : TypeToken<MutableList<Question>>() {}.type
            questions = gson.fromJson(json, type) ?: mutableListOf()

            disableLifeLines()

            if (questions.isNotEmpty()) {
                showQuestion()
            } else {
                loadQuestions()
            }
        }
    }

    private fun clearSavedProgress() {
        getSharedPreferences("progress", MODE_PRIVATE)
            .edit().apply {
                clear()
                apply()
            }
    }

    private fun startCountDownActivity() {
        startActivity(Intent(this, CountDownActivity::class.java))
    }

    private fun saveHistory(
        questionId: String,
        answer: String,
        correctAnswer: String,
        highScore: String,
        isCorrect: Boolean
    ) {
        try {
            val dateFormat: DateFormat = SimpleDateFormat("EEE, d MMM, HH:mm", Locale.getDefault())
            val datePlayed = dateFormat.format(Calendar.getInstance().time)

            dbHelper.saveHistory(
                questionId, answer,
                correctAnswer, question?.reason,
                datePlayed, datePlayed,
                highScore, isCorrect
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun skipQuestion() {
        if (Utils.isOnline(this)) {
            try {
                AdManager.showRewardedAd(this)

                AdManager.rewardedAd.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            showToast()
                            optionsClickable = true
                        }

                        override fun onAdDismissedFullScreenContent() {
                            updateRefreshQuestionState(true)
                            updateShouldContinueGame(true)
                            updateProgressState(false)
                            optionsClickable = true
                        }
                    }
            } catch (e: java.lang.Exception) {
                showToast()
            }
        } else {
            showToast()
        }
    }

    private fun hideTwoQuestions() {
        val correctAnswer = question?.correctAnswer
        if (correctAnswer != null) {
            optionsAdapter2.hideRandomOptions(correctAnswer)
        }
        minus2QuestionsImageView.visibility = View.VISIBLE
        minus2QuestionsButton.isClickable = false
        hasMinus2Question = true
        updateSuggestionAndPollVisibility()
    }

    private fun askComputer() {
        askComputerContainer.visibility = View.VISIBLE
        votingContainer.visibility = View.GONE
        askComputerButton.isClickable = false
        askComputerImageView.visibility = View.VISIBLE
        hasAskedComputer = true

        val suggestion = getRandomSuggestion(this)
        val label = getCorrectLabel()
        suggestionTextView.text = suggestion
        optionTextView.text = label
    }

    private fun takeAPoll() {
        votingContainer.visibility = View.VISIBLE
        takeAPollButton.isClickable = false
        askComputerContainer.visibility = View.GONE
        takeAPollImageView.visibility = View.VISIBLE
        hasTakenPoll = true

        val correctOptions = intArrayOf(60, 65, 75)
        val label = getCorrectLabel()
        val index = Random().nextInt(correctOptions.size)
        val progress = correctOptions[index]
        updateProgressBar(label, progress)

        Log.d("response", "label: $label progress: $progress")
    }

    private fun updateProgressBar(label: String, progress: Int) {
        val remainingPercentage = 100 - progress
        val incorrectProgress = remainingPercentage / 3

        val progressBars = arrayOf(progressBarA, progressBarB, progressBarC, progressBarD)
        val textViews = arrayOf(textViewA, textViewB, textViewC, textViewD)

        // Detect the language being used (Latin or Arabic)
        val isArabic: Boolean = isArabic()

        // Determine the starting character based on the language
        val startingChar = if (isArabic) 'أ' else 'A'

        for (i in progressBars.indices) {
            // Compare characters dynamically
            animateProgressBar(
                progressBars[i],
                textViews[i],
                if (label == (startingChar + i).toString())
                    progress
                else
                    incorrectProgress
            )
        }
    }

    private fun isArabic(): Boolean {
        val languageCode = sharedPreferences.getString("language", "")
        return languageCode == ARABIC_KEY
    }

    /**
     * Animates progress bars and text views based on user actions.
     *
     * @param progressBar The progress bar to be animated.
     * @param textView    The text view associated with the progress bar.
     * @param progress    The progress value to be animated.
     */
    private fun animateProgressBar(progressBar: ProgressBar, textView: TextView, progress: Int) {
        val anim = ObjectAnimator.ofInt(progressBar, "progress", progress)
        anim.setDuration(DELAY_INTERVAL_LONG)
        anim.start()

        ValueAnimator.ofInt(0, progress).apply {
            duration = DELAY_INTERVAL_LONG
            addUpdateListener { animator ->
                textView.text = String.format(
                    Locale.getDefault(),
                    "%s%%",
                    animator.animatedValue
                )
            }
            start()
        }
    }

    private fun getCorrectLabel(): String {
        val correctAnswer = question?.correctAnswer?.trim()

        for (i in options.indices) {
            val optionModel = options[i]
            if (optionModel.optionText.trim().equals(correctAnswer, ignoreCase = true)) {
                return getLabelFromList(this, i)
            }
        }

        return ""
    }

    private fun updateSuggestionAndPollVisibility() {
        if (hasTakenPoll) {
            votingContainer.visibility = View.GONE
        }
        if (hasAskedComputer) {
            askComputerContainer.visibility = View.GONE
        }
    }

    private fun enableLifeLines() {
        minus2QuestionsButton.isClickable = true
        minus2QuestionsImageView.visibility = View.GONE
        askComputerButton.isClickable = true
        askComputerImageView.visibility = View.GONE
        takeAPollButton.isClickable = true
        takeAPollImageView.visibility = View.GONE

        amountWonList.clear()
        numberOfFailure = 0
        numberOfPassed = 0
        questionIndex = 0
        amountWonText = 0
    }

    private fun disableLifeLines() {
        if (hasTakenPoll) {
            takeAPollButton.isClickable = false
            takeAPollImageView.visibility = View.VISIBLE
        }
        if (hasAskedComputer) {
            askComputerButton.isClickable = false
            askComputerImageView.visibility = View.VISIBLE
        }
        if (hasMinus2Question) {
            minus2QuestionsButton.isClickable = false
            minus2QuestionsImageView.visibility = View.VISIBLE
        }

        updateRefreshQuestionState(false)
        updateShouldContinueGame(true)
        updateProgressState(false)
    }

    private fun animateViews() {
        val views = arrayOf(
            minus2QuestionsButton, lifeLineDescriptionTextView1, askComputerButton,
            lifeLineDescriptionTextView2, takeAPollButton, lifeLineDescriptionTextView3,
            resetQuestionButton, lifeLineDescriptionTextView4, questionCardView,
            questionTextView, exitButton, amountContainer, questionProgressTextView
        )

        val delays =
            longArrayOf(1000, 1000, 1200, 1200, 1400, 1400, 1600, 1600, 500, 550, 500, 500, 500)


        for (i in views.indices) {
            fadeViewIn(views[i], delays[i])
        }
    }

    private fun fadeViewIn(view: View, delay: Long) {
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(800)
            .setStartDelay(delay)
            .start()
    }

    @SuppressLint("VisibleForTests")
    private fun createAdRequest() = AdRequest.Builder().build()

    /**
     * Handles the display of various dialogs such as the exit dialog, failure dialog, and explanation dialog.
     */
    private fun showExitDialog() {
        AudioManager.darkBlueBlink(this, exitButton)
        ExitGameDialog(this, amountWonText.toString()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(30000, DELAY_INTERVAL_LONG) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountdownText(millisUntilFinished)
            }

            override fun onFinish() {
                vibrate()
                showFailureDialog()
                cancelTimer()
            }
        }.start()
    }

    private fun updateCountdownText(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / DELAY_INTERVAL_LONG
        val timeRemaining = seconds.toString()
        countdownTextView.text = timeRemaining
    }

    private fun startCountdownTimerIfGameModeIsTimed() {
        if (gameMode() == "1") {
            timerContainer.visibility = View.VISIBLE
            cancelTimer()
            startCountdownTimer()
        } else {
            timerContainer.visibility = View.GONE
        }
    }

    private fun cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    private fun showToast() {
        Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
    }

    private fun formatDuration(durationMillis: Long): String {
        var seconds = durationMillis / 1000
        var minutes = seconds / 60
        seconds %= 60
        val hours = minutes / 60
        minutes %= 60

        return if (hours > 0) {
            String.format(
                Locale.getDefault(),
                resources.getString(R.string.d_hours_02d_minutes_02d_seconds),
                hours, minutes, seconds
            )
        } else if (minutes > 0) {
            String.format(
                Locale.getDefault(),
                resources.getString(R.string.d_minutes_02d_seconds),
                minutes, seconds
            )
        } else {
            String.format(
                Locale.getDefault(),
                resources.getString(R.string.d_seconds),
                seconds
            )
        }
    }

    private fun disposeResources() {
        AudioManager.stopBackgroundMusic()
        AudioManager.releaseMusicResources()
        updateMusicState(false)
        AdManager.disposeAds()
        cancelTimer()

        val durationMillis = System.currentTimeMillis() - startTimeMillis
        val durationString: String = formatDuration(durationMillis)

        sharedPreferences.edit().apply {
            putString("duration", durationString)
            apply()
        }

        updateShouldContinueGame(savedState = false)
    }

    override fun onResume() {
        super.onResume()

        if (!isFirstTime()) {
            if (shouldContinueGame()) {
                if (isFromProgress()) {
                    showQuestion()
                    Log.d("response", "I ran")
                } else if (isShouldRefreshQuestion()) {
                    loadQuestions()
                    updateRefreshQuestionState(false)
                    updateShouldContinueGame(true)
                } else {
                    startCountdownTimerIfGameModeIsTimed()
                }
            } else {
                enableLifeLines()
                loadQuestions()
            }
        }

        if (shouldPlayMusic()) {
            AudioManager.playBackgroundMusic(this)
            updateMusicState(false)
        }

        updateSuggestionAndPollVisibility()
        getAmounts()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("amountWonList", ArrayList(amountWonList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        amountWonList = savedInstanceState.getIntegerArrayList("amountWonList")!!
    }

    override fun onPause() {
        super.onPause()
        if (!shouldPlayMusic()) {
            AudioManager.pauseBackgroundMusic()
            updateMusicState(true)
        }

        updateRefreshQuestionState(false)
        updateShouldContinueGame(true)
        updateProgressState(false)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeResources()
        clearSavedProgress()
    }
}