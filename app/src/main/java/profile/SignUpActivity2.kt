package profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity2 : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        //1. 22523190 Joaquin Brilliant Pramustya
        //2. 22523124 Mohammad Farid Anshori
        //3. 22523016 M. Da’il Falah
        //4. 22523171 Nurandra Satya Adhi

        // Hubungkan elemen UI dengan ID-nya
        val backArrow: TextView = findViewById(R.id.backArrow)
        val ageField: EditText = findViewById(R.id.ageField)
        val weightField: EditText = findViewById(R.id.weightField)
        val heightField: EditText = findViewById(R.id.heightField)
        val targetField: EditText = findViewById(R.id.targetField)
        val genderField: EditText = findViewById(R.id.genderField)
        val createButton: Button = findViewById(R.id.createButton)

        // Ambil Document ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val documentId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

        if (documentId == null) {
            Toast.makeText(this, "User ID not found. Please sign up again.", Toast.LENGTH_SHORT).show()
            finish() // Kembali jika ID tidak ditemukan
            return
        }

        // Tombol Back (Kembali)
        backArrow.setOnClickListener {
            Toast.makeText(this, "Back clicked", Toast.LENGTH_SHORT).show()
            finish() // Menutup aktivitas ini
        }

        // Tombol Create
        createButton.setOnClickListener {
            val age = ageField.text.toString().trim()
            val weight = weightField.text.toString().trim()
            val height = heightField.text.toString().trim()
            val target = targetField.text.toString().trim()
            val gender = genderField.text.toString().trim()

            if (age.isEmpty() || weight.isEmpty() || height.isEmpty() || target.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Simpan data tambahan ke Firestore
                val additionalData = hashMapOf(
                    "age" to age,
                    "weight" to weight,
                    "height" to height,
                    "target" to target,
                    "gender" to gender
                )

                db.collection("user").document(documentId)
                    .update(additionalData as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show()
                        // Arahkan kembali ke LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
