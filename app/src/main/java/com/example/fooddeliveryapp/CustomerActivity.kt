package com.example.fooddeliveryapp
 import android.content.ContentValues.TAG
 import android.content.Intent
 import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
 import android.widget.Toast
 import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
 import com.google.firebase.auth.PhoneAuthOptions
 import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
 import com.google.firebase.firestore.firestore
 import java.util.concurrent.TimeUnit

class CustomerActivity : AppCompatActivity() {
    private lateinit var name : EditText
    private lateinit var phn : EditText
    private lateinit var register: Button
    private lateinit var sendOtp : Button
    private lateinit var otp : EditText
    private lateinit var login : Button
    private lateinit var auth : FirebaseAuth
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var  storedVerificationId : String
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                Toast.makeText(applicationContext,"OTP Sent", Toast.LENGTH_SHORT).show()

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
            }
        }
        name = findViewById(R.id.etUserName)
        phn = findViewById(R.id.etPhone)
        register = findViewById(R.id.registerBtn)
        sendOtp = findViewById(R.id.sentOtpBtn)
        otp = findViewById(R.id.etOtp)
        login = findViewById(R.id.loginBtn)
        auth = Firebase.auth
        register.setOnClickListener {
            db.collection("Users").whereEqualTo("phone",phn.text.toString()).get()
                .addOnSuccessListener { document->
                    if(!document.isEmpty){
                        Toast.makeText(applicationContext,"User Already Exist", Toast.LENGTH_SHORT).show()
                    }else{
                        val user = hashMapOf(
                            "name" to name.text.toString(),
                            "phone" to phn.text.toString(),
                            "type" to "customer"
                        )
                        db.collection("Users").add(user)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,"Registration success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext,"Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                    }

                }

        }

        sendOtp.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phn.text.toString()) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        login.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp.text.toString())
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        val user = task.result?.user
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(applicationContext,"Login Failed", Toast.LENGTH_SHORT).show()

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
        }
    }

}