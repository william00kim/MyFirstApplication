package org.example.ada.diary

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dairy.*
import kotlinx.android.synthetic.main.activity_dairy.Btn_back
import kotlinx.android.synthetic.main.activity_dday2.*
import org.example.ada.MainActivity
import org.example.ada.R
import java.time.LocalDate
import java.util.*

class DairyActivity :AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    val today: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dairy)

        auth = Firebase.auth

        loaddata()

        Btn_back.setOnClickListener {
            finish()
        }

        Btn_sort.setOnClickListener {
            changeText()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeText() {

        auth = Firebase.auth

        val currentUser = auth.currentUser?.uid!!

        val intent = Intent(this, MainActivity::class.java)
        var resultMyThink = ed_mythink.text.toString()

        db.collection("UserId").document(currentUser).get().addOnSuccessListener { res ->
            val chatName = res.data?.get("chatName").toString()
            val myId = res.data?.get("UserName").toString()
            val inputdata = hashMapOf(
                "Name" to myId,
                "Data" to resultMyThink,
                "Date" to today.toString()
            )

            db.collection("UserDiary").document(chatName).collection(myId).document(today.toString()).set(inputdata)
                .addOnSuccessListener {
                    Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"알수 없는 오류 발생", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loaddata() {
        TodayDate.text = today.toString()
    }
}