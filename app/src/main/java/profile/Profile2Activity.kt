package profile

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class Profile2Activity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore
    private lateinit var documentId: String // ID pengguna yang login

    //1. 22523190 Joaquin Brilliant Pramustya
    //2. 22523124 Mohammad Farid Anshori
    //3. 22523016 M. Da’il Falah
    //4. 22523171 Nurandra Satya Adhi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        // Ambil Document ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        documentId = sharedPreferences.getString("USER_DOCUMENT_ID", null) ?: ""

        if (documentId.isEmpty()) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show()
            finish() // Tutup aktivitas jika ID tidak ditemukan
            return
        }

        // Input Fields
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etMobile = findViewById<EditText>(R.id.etMobile)
        val etDob = findViewById<EditText>(R.id.etDob)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etTarget = findViewById<EditText>(R.id.etTarget)

        // Buttons
        val btnUpdateProfile = findViewById<Button>(R.id.btnUpdateProfile)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Load Existing Data from Firestore
        db.collection("user").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val tvHeaderName = findViewById<TextView>(R.id.tvHeaderName)
                    val tvHeaderEmail = findViewById<TextView>(R.id.tvHeaderEmail)
                    val tvHeaderDob = findViewById<TextView>(R.id.tvHeaderDob)
                    val tvHeaderWeight = findViewById<TextView>(R.id.tvHeaderWeight)
                    val tvHeaderAge = findViewById<TextView>(R.id.tvHeaderAge)
                    val tvHeaderHeight = findViewById<TextView>(R.id.tvHeaderHeight)
                    val tvTarget = findViewById<TextView>(R.id.target)
                    val dob = document.getString("dob") ?: ""
                    if (dob.isNotEmpty()) {
                        val age = calculateAge(dob)
                        tvHeaderAge.text = "$age Years Old"
                    } else {
                        tvHeaderAge.text = "Unknown Age"
                    }

                    tvHeaderName.text = document.getString("username") ?: "Unknown"
                    tvHeaderEmail.text = document.getString("email") ?: "Unknown"
                    tvHeaderDob.text = "Birthday: ${document.getString("dob") ?: "Unknown"}"
                    tvHeaderWeight.text = "${document.getString("weight") ?: "0"} Kg"
                    tvHeaderHeight.text = "${document.getString("height") ?: "0"} CM"
                    tvTarget.text = document.getString("target")
                    // Tampilkan data ke input field
                    etFullName.setText(document.getString("username"))
                    etEmail.setText(document.getString("email"))
                    etMobile.setText(document.getString("mobile"))
                    etDob.setText(document.getString("dob"))
                    etWeight.setText(document.getString("weight"))
                    etHeight.setText(document.getString("height"))
                    etTarget.setText(document.getString("target"))
                } else {
                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Update Data in Firestore
        btnUpdateProfile.setOnClickListener {
            val updatedData = mapOf(
                "fullName" to etFullName.text.toString(),
                "email" to etEmail.text.toString(),
                "mobile" to etMobile.text.toString(),
                "dob" to etDob.text.toString(),
                "weight" to etWeight.text.toString(),
                "height" to etHeight.text.toString(),
                "target" to etTarget.text.toString()
            )

            db.collection("user").document(documentId)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Tombol Back
        btnBack.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove("USER_DOCUMENT_ID") // Hapus USER_DOCUMENT_ID
            editor.apply()

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

            // Kembali ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Tutup aktivitas saat ini
        }
    }
    fun calculateAge(dob: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return try {
            val birthDate = dateFormat.parse(dob) ?: return 0
            val today = Calendar.getInstance()
            val birth = Calendar.getInstance()
            birth.time = birthDate

            var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: Exception) {
            0
        }
    }
}
