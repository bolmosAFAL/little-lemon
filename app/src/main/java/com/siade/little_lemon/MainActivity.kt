package com.siade.little_lemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.siade.little_lemon.ui.theme.LittlelemonTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    private val menuUrl =
        "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "little_lemon.db")
            .build()
    }

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true },
                contentType = ContentType("text", "plain")
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            if (database.menuDao().isEmpty()) {
                val menuItems = fetchMenu()
                saveMenuToDatabase(menuItems)
            }
        }

        setContent {
            LittlelemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val databaseMenuItems by database.menuDao().getAll()
                        .observeAsState(emptyList())

                    val navController = rememberNavController()
                    MyNavigation(
                        navController = navController,
                        menuItems = databaseMenuItems,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        val response: MenuNetwork = httpClient.get(menuUrl).body()
        return response.menu
    }

    private suspend fun saveMenuToDatabase(menuItems: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItems.map { it.toMenuItemRoom() }
        database.menuDao().insertAll(*menuItemsRoom.toTypedArray())
    }
}
