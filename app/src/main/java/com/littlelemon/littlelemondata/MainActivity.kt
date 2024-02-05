package com.littlelemon.littlelemondata

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.littlelemon.littlelemondata.ui.theme.LittleLemonDataTheme
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    private val inventoryRepository by lazy { InventoryRepository(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LittleLemonDataTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val onUpdateItem: (Item) -> Unit = { item ->
                        runBlocking(IO) {
                            inventoryRepository.updateItem(item)
                        }
                    }
                    val items = inventoryRepository.getAllItems().observeAsState(emptyList())
                    InventoryList(items = items.value, onUpdateItem = onUpdateItem)
                }
            }
        }

    }
//    override fun onStart() {
//        super.onStart()
//        val sharedPreferences = getSharedPreferences("Little Lemon", MODE_PRIVATE)
//        val lastCount = sharedPreferences.getInt("StartCount", 0)
//        val newCount = lastCount + 1
//        Log.d("Count", newCount.toString())
//        sharedPreferences.edit().putInt("StartCount", newCount).apply()
//        Toast.makeText(this, "You have opened this app $newCount times", Toast.LENGTH_SHORT).show()
//    }
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
@Composable
fun InventoryList(items: List<Item>, onUpdateItem: (item: Item) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn{
            itemsIndexed(items) { _, item ->
                InventoryItem(item, onUpdateItem)
            }
        }
    }
}

@Composable
fun InventoryItem(item: Item, onUpdateItem: (item: Item) -> Unit) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.name)
        TextButton(onClick = {
            onUpdateItem(item.copy(quantity = item.quantity + 1))
        }) {
            Text(text = "+")
        }
        Text(text = item.quantity.toString())
        TextButton(onClick = {
            onUpdateItem(item.copy(quantity = item.quantity - 1))
        }) {
            Text(text = "-")
        }
    }
}


@Entity
data class Item(
    @PrimaryKey val id: Int,
    val name: String,
    val quantity: Int,
)
@Dao
interface InventoryDao {
    @Query("SELECT * FROM Item order by name")
    fun getAllItems(): LiveData<List<Item>>

    @Update
    fun updateItem(item: Item)
}

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
}

class InventoryRepository(context: Context){
    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "inventory.db"
    ).createFromAsset("database/inventory.db").build()

    fun getAllItems() = database.inventoryDao().getAllItems()

    fun updateItem(item: Item) = database.inventoryDao().updateItem(item)

}











