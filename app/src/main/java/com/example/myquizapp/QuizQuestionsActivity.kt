package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

// View.OnClickListener allows us to click on items inside of the activity
class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    // Allows the app to know which answer the user has selected,
    // as well as giving each part of the app a null value so that they can be changed by user
    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mUserName: String? = null
    private var mCorrectAnswers: Int = 0

    private var progressBar : ProgressBar? = null
    private var tvProgress : TextView? = null
    private var tvQuestion : TextView? = null
    private var ivImage : ImageView? = null

    private var tvOptionOne : TextView? = null
    private var tvOptionTwo : TextView? = null
    private var tvOptionThree : TextView? = null
    private var tvOptionFour : TextView? = null
    private var btnSubmit : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        // Gets the intent that got to this activity and the position of the username
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        // Locates each private var in the xml through ID
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.iv_image)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        btnSubmit = findViewById(R.id.btn_submit)

        // Passes each option/exports the onclick so they are able to be clicked
        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        // Assigns the questions list
        mQuestionsList = Constants.getQuestions()

        // Refactored the lines below into a function labeled as setQuestion()
        setQuestion()

    }

    private fun setQuestion() {

        /*
        // Displays in the Logcat what the size of the questions list is
        Log.i("QuestionsList size is", "${questionsList.size}")
        // i is individual question in the list
        for (i in questionsList) {
            Log.e("Questions", i.question)
        }

         */
        // Starting at current position then giving the individual
        // question the user is looking at
        // mQuestionsList = 1

        // Resets for each question
        defaultOptionsView()
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        // Gives the question of the question object
        ivImage?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition / ${progressBar?.max}"
        tvQuestion?.text = question.question
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour

        // If the questions list is finished, the code will display FINISH,
        // or else it will display SUBMIT
        if (mCurrentPosition == mQuestionsList!!.size){
                btnSubmit?.text = "FINISH"
        }else{
            btnSubmit?.text = "SUBMIT"
        }
    }

    // Creates a function that inputs the options from the Text View
    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        tvOptionOne?.let{
            options.add(0, it)
        }
        tvOptionTwo?.let{
            options.add(1, it)
        }
        tvOptionThree?.let{
            options.add(2, it)
        }
        tvOptionFour?.let{
            options.add(3, it)
        }

        // Gives a color/typeface to all of the options
        // and then gives them the border created in drawable xml
        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }

    // Connects the option selected to its corresponding number,
    // as well as gives it a color, bold typeface, and background when selected
    private fun selectedOptionView(tv:TextView, selectedOptionNum: Int){
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.default_option_border_bg
        )
    }

    // When an option is called it will make sure that the correct option is selected
    // Passing a text view in order to change it accordingly
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_option_one -> {
                tvOptionOne?.let{
                    selectedOptionView(it, 1)
                }
            }

            R.id.tv_option_two -> {
                tvOptionTwo?.let{
                    selectedOptionView(it, 2)
                }
            }

            R.id.tv_option_three -> {
                tvOptionThree?.let{
                    selectedOptionView(it, 3)
                }
            }

            R.id.tv_option_four -> {
                tvOptionFour?.let{
                    selectedOptionView(it, 4)
                }
            }

            // If the answer is selected it overrides the current position and adds 1
            // As long as questions are left, the next question will be asked
            R.id.btn_submit ->{
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }
                        // Sends the user to a results screen once options are exhausted
                        else ->{
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            // Sees how many questions there are in total
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(intent)
                            // Clicking the finish button will take user back to start screen
                            finish()
                            // Toast.makeText(this, "You made it to the end", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    // Displays the appropriate background when the
                    // selected option is correct or incorrect
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        // If right answer is chosen this adds 1 to the number of correct answers
                        mCorrectAnswers++
                    }
                    // No matter if the user is wrong or right,
                    // the correct option will always turn green
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    // Checks the position of the question and
                    // once button is submitted it gives appropriate text to user on button
                    if(mCurrentPosition == mQuestionsList!!.size){
                        btnSubmit?.text = "FINISH"
                    }else{
                        btnSubmit?.text = "GO TO NEXT QUESTION"
                    }

                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    // Calls up the correct or wrong drawables depending
    // on if user submits the right answer
    private fun answerView(answer: Int, drawableView : Int){
        when(answer){
            1 -> {
                tvOptionOne?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                tvOptionThree?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}