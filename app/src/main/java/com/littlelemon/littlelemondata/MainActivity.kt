package com.littlelemon.littlelemondata

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.littlelemondata.ui.theme.LittleLemonDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LittleLemonDataTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }

        }
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("Little Lemon", MODE_PRIVATE)
        val lastCount = sharedPreferences.getInt("StartCount", 0)
        val newCount = lastCount + 1
        Log.d("Count", newCount.toString())
        sharedPreferences.edit().putInt("StartCount", newCount).apply()
        Toast.makeText(this, "You have opened this app $newCount times", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleLemonDataTheme {
        Greeting("Android")
    }
}