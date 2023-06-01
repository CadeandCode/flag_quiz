package com.example.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Checks if the property inside et_name/text is empty
        // and displays appropriate text
        val btnstart : Button = findViewById(R.id.btn_start)
        val etname: EditText = findViewById(R.id.et_name)
        btnstart.setOnClickListener {

            if (etname.text.isEmpty()){
                Toast.makeText(this,
                    "Please enter your name", Toast.LENGTH_LONG).show()
            }else{
                // The intent allows the user to move from the main activity
                // to quiz activity once an input has been given
                val intent = Intent(this, QuizQuestionsActivity::class.java)
                // Intent can retrieve info from another activity
                // Whatever is entered into et_name will be put into the username
                intent.putExtra(Constants.USER_NAME, etname.text.toString())
                startActivity(intent)
                // Closes current activity
                finish()
            }
        }

    }
}