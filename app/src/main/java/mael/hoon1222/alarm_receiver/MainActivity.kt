package mael.hoon1222.alarm_receiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import mael.hoon1222.alarm_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initFirebase()
    }

    private fun initFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    binding.tvFireBaseToken.text = task.result
                    Log.d("마엘",task.result)
                }
            }
    }
}