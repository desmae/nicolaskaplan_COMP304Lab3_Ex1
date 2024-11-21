package com.example.nicolaskaplan_comp304lab_ex1
// 301261925 - Nicolas Kaplan (C)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.FavoriteLocation
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.AppDatabase
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.RetrofitClient
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.WeatherRepository
import com.example.nicolaskaplan_comp304lab_ex1.ui.theme.Nicolaskaplan_COMP304Lab_Ex1Theme
import com.example.nicolaskaplan_comp304lab_ex1.viewmodel.WeatherViewModel
import com.example.nicolaskaplan_comp304lab_ex1.viewmodel.WeatherViewModelFactory

@Composable
fun WeatherAppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: WeatherViewModel,
    apiKey: String
) {
    NavHost(navController = navController, startDestination = "favorites") {
        composable("favorites") {
            FavoriteLocationsScreen(navController = navController, viewModel = viewModel)
        }
        composable("weather/{name}/{lat}/{lon}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
            val lat = backStackEntry.arguments?.getString("lat") ?: "0.0"
            val lon = backStackEntry.arguments?.getString("lon") ?: "0.0"
            WeatherScreen(navController, name, lat, lon, viewModel, apiKey)
        }
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private val weatherViewModel: WeatherViewModel by viewModels {
        database = AppDatabase.getDatabase(applicationContext)
        val favoriteLocationDao = database.favoriteLocationDao()
        WeatherViewModelFactory(WeatherRepository(RetrofitClient.weatherService, favoriteLocationDao), favoriteLocationDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nicolaskaplan_COMP304Lab_Ex1Theme {
                val navController = rememberNavController()
                WeatherAppNavHost(
                    navController = navController,
                    viewModel = weatherViewModel,
                    apiKey = "57698a3fbd9075e17c84412762a41dde"
                )
            }
        }
    }
}
@Composable
fun FavoriteLocationsScreen(navController: NavHostController, viewModel: WeatherViewModel) {

    val mockLocations = listOf(
        FavoriteLocation(name = "Toronto", lat = 43.6532, lon = -79.3832),
        FavoriteLocation(name = "Moscow", lat = 55.7558, lon = 37.6173),
        FavoriteLocation(name = "Hong Kong", lat = 22.3193, lon = 114.1694),
        FavoriteLocation(name = "Auckland", lat = -36.8485, lon = 174.7633),
        FavoriteLocation(name = "London", lat = 51.5074, lon = 0.1278)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(mockLocations) { location ->
            Button(
                onClick = {
                    navController.navigate("weather/${location.name}/${location.lat}/${location.lon}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Weather for ${location.name}")
            }
        }
    }
}
@Composable
fun WeatherScreen(
    navController: NavHostController,
    name: String,
    lat: String,
    lon: String,
    viewModel: WeatherViewModel,
    apiKey: String
) {
    val weatherData by viewModel.weatherData.observeAsState()
    val isError by viewModel.isError.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getWeather(lat, lon, apiKey)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isError == true -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Failed to fetch weather: ${errorMessage ?: "unknown error"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.getWeather(lat, lon, apiKey) }) {
                        Text("Retry")
                    }
                }
            }
            weatherData == null -> {
                Text("Fetching weather for $name...")
            }
            else -> {
                val current = weatherData?.main
                val description = weatherData?.weather?.firstOrNull()?.description ?: "N/A"

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${current?.temp?.toInt()}°C",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text("Feels like ${current?.feels_like?.toInt()}°C")
                    Text(description)
                    Spacer(modifier = Modifier.height(16.dp))

                    // back
                    Button(onClick = { navController.navigateUp() }) {
                        Text("Back to Favorites")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // save
//                    Button(onClick = {
//                        viewModel.saveFavoriteLocation(
//                            FavoriteLocation(name = name, lat = lat.toDouble(), lon = lon.toDouble())
//                        )
//                    }) {
//                        Text("Save $name as Favorite")
//                    }
                }
            }
        }
    }
}



