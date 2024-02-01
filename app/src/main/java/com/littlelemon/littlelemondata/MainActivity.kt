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
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
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
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        val playerDao = db.playerDao()
        val players = playerDao.getAll()
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

@Entity
data class Player(
    @PrimaryKey val id: Int,
    val name: String,
    val age: Int,
)
@Dao
interface PlayerDao {
    @Query("SELECT * FROM player")
    fun getAll(): List<Player>

    @Insert
    fun insertAll(vararg players: Player)

    @Delete
    fun delete(player: Player)
}

@Database(entities = [Player::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}











