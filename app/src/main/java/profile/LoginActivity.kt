package profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textForgotPassword: TextView
    private lateinit var buttonSignUp: Button

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //1. 22523190 Joaquin Brilliant Pramustya
        //2. 22523124 Mohammad Farid Anshori
        //3. 22523016 M. Da’il Falah
        //4. 22523171 Nurandra Satya Adhi

        // Hubungkan elemen UI dengan ID-nya
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textForgotPassword = findViewById(R.id.textForgotPassword)
        buttonSignUp = findViewById(R.id.buttonsignup)

        // Tombol Login
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // Validasi login dengan Firestore
                db.collection("user")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password) // Pastikan password sesuai (gunakan hashing di produksi)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        } else {
                            // Login berhasil, simpan data pengguna jika perlu
                            val documentId = documents.documents[0].id
                            val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("USER_DOCUMENT_ID", documentId) // Simpan document ID
                            editor.apply()

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish() // Tutup LoginActivity
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Forgot Password
        textForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }

        // Tombol Sign Up
        buttonSignUp.setOnClickListener {
            // Pindah ke SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
