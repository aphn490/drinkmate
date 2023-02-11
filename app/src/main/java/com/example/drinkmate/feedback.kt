
package com.example.drinkmate

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class feedback : AppCompatActivity() {
    //creating private vars
    private lateinit var email:EditText;
    private lateinit var subject:EditText;
    private lateinit var feedback:EditText;
    private lateinit var send: Button;
    private var db:FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        //settings variables based on input
        email = findViewById(R.id.email);
        subject = findViewById(R.id.subject);
        feedback = findViewById(R.id.feedback);
        send = findViewById(R.id.send_button);

        //activates when send button is clicked
        send.setOnClickListener{
            //converting email, subject, and feedback to strings
            var em = email.text.toString()
            var sub = subject.text.toString()
            var fdback = feedback.text.toString()
            //putting all the strings into a data class
            val user = UserFeedback(em, sub, fdback)
            //if all fields are non empty and email is valid it will add the feedback
            //to database
            if (em.isNotEmpty() && sub.isNotEmpty() && fdback.isNotEmpty() && isValidEmail(em)){
                db.collection("Feedback").add(user)
                Toast.makeText(this, "Feedback received",Toast.LENGTH_SHORT).show()
            }
            //possible error messages and will not allow it invalid inputs into the database
            else{
                if (isValidEmail(em) == false){
                    Toast.makeText(this, "Please enter a valid email",Toast.LENGTH_SHORT).show()
                }
                else if (em.isNotEmpty() or sub.isNotEmpty() or fdback.isNotEmpty()){
                    Toast.makeText(this, "Please fill in all the fields",Toast.LENGTH_SHORT).show()
                }
            }
            //firebase code to input it into our database
        }
    }
}
//class to hold data of feedback and to be inserted into firebase
data class UserFeedback(
    var email: String? = null,
    var subject: String? = null,
    var feedback: String? = null
)
//makes sure that input is an email
fun isValidEmail(target: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}