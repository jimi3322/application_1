package com.app.yun.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.yun.R
import kotlinx.android.synthetic.main.activity_m.*

class MActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m)

        sendRequestBtn.setOnClickListener{
            val intent = Intent(this, DeviceListActivity::class.java)
            startActivity(intent)
        }
        sendRealtimeRequest.setOnClickListener{
            val intent = Intent(this, RealTimeActivity::class.java)
            startActivity(intent)
        }
        articleRequest.setOnClickListener{
            val intent = Intent(this, ArticlaListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.title_m_menu,menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.change_item -> startActivity(Intent(this, ChangePasswordActivity::class.java))

        }
        return true
    }
}
