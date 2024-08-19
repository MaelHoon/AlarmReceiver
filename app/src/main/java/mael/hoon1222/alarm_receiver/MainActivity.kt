package mael.hoon1222.alarm_receiver

import android.annotation.SuppressLint
import android.content.Intent
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

        updateResult(true)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
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

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent : Boolean = false){
        binding.tvResultValue.text = (intent.getStringExtra("notificationType")?:"앱 런처") + if(isNewIntent){
            "(으)로 갱신 했습니다."
        }else{
            "(으)로 실행 했습니다."
        }
    }
}