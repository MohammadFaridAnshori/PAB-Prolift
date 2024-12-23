package profile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore

    //1. 22523190 Joaquin Brilliant Pramustya
    //2. 22523124 Mohammad Farid Anshori
    //3. 22523016 M. Da’il Falah
    //4. 22523171 Nurandra Satya Adhi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val backArrow: TextView = findViewById(R.id.backArrow)
        val emailField: EditText = findViewById(R.id.emailField)
        val usernameField: EditText = findViewById(R.id.usernameField)
        val nameField: EditText = findViewById(R.id.nameField)
        val passwordField: EditText = findViewById(R.id.passwordField)
        val confirmPasswordField: EditText = findViewById(R.id.confirmPasswordField)
        val mobileField: EditText = findViewById(R.id.mobileField)
        val dobField: EditText = findViewById(R.id.dobField) // Tambahkan field DOB
        val nextButton: Button = findViewById(R.id.nextButton)

        // Tombol Back
        backArrow.setOnClickListener {
            Toast.makeText(this, "Back clicked", Toast.LENGTH_SHORT).show()
            finish() // Menutup aktivitas dan kembali ke aktivitas sebelumnya
        }

        // DatePickerDialog untuk Date of Birth
        dobField.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format tanggal: DD/MM/YYYY
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    dobField.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Tombol Next
        nextButton.setOnClickListener {
            val email = emailField.text.toString()
            val username = usernameField.text.toString()
            val name = nameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val mobile = mobileField.text.toString()
            val dob = dobField.text.toString() // Ambil nilai DOB

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty() || username.isEmpty() || name.isEmpty() || password.isEmpty() || mobile.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Validasi panjang nomor telepon
                if (mobile.length < 10 || mobile.length > 15) {
                    Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Data valid, simpan ke Firestore
                val user = hashMapOf(
                    "email" to email,
                    "username" to username,
                    "name" to name,
                    "mobile" to mobile,
                    "dob" to dob, // Tambahkan DOB ke Firestore
                    "password" to password // Untuk keamanan, gunakan hashing seperti bcrypt
                )

                db.collection("user")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("USER_DOCUMENT_ID", documentReference.id)
                        editor.apply()

                        Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                        // Pindah ke aktivitas berikutnya
                        val intent = Intent(this, SignUpActivity2::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
