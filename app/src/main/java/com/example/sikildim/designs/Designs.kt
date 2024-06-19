package com.example.sikildim.designs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sikildim.R
import com.example.sikildim.api.NetworkResponse
import com.example.sikildim.api.WeatherModel
import com.example.sikildim.viewmodel.WeatherViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Page1 (viewModel: WeatherViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.main))
    )
    

        var city = remember { mutableStateOf("") }

        val weatherData = viewModel.weatherData.observeAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedTextField(
                    value = city.value,
                    onValueChange = {
                        city.value=it
                    },
                    label = {Text(text = "Search for any location")
                    }
                )
                IconButton(onClick = {
                    viewModel.getData(city.value)
                    keyboardController?.hide()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search for any location"
                    )

                }
            }
            when (val result = weatherData.value){
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }
                is NetworkResponse.Succes -> {
                    WeatherDetails(data = result.data)
                }
                null ->{}
            }

    }

}
@Composable
fun Box() {
    Box(modifier = Modifier.fillMaxSize()){
        colorResource(id = R.color.main)
    }
}

@Composable
fun WeatherDetails(data : WeatherModel) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.size(16.dp))
        Row (modifier = Modifier.
            fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom){
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = "${data.current.temp_c}°C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.size(32.dp))
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition Icon")
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Card (modifier = Modifier.background(color = colorResource(id = R.color.secondary),
            shape = MaterialTheme.shapes.medium),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column (
               modifier = Modifier.fillMaxWidth()
                   .background(color = colorResource(id = R.color.secondary))
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .background(color = colorResource(id = R.color.secondary)),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherCard("Humiditiy", "%${data.current.humidity}")
                    WeatherCard("Wind", "${data.current.wind_kph} km/h")
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .background(color = colorResource(id = R.color.secondary)),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherCard("UV", "${data.current.uv}")
                    WeatherCard("Participation", "${data.current.precip_mm} mm")
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .background(color = colorResource(id = R.color.secondary)),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherCard("Local Time", data.location.localtime.split(" ")[1])
                    WeatherCard("Local Data", data.location.localtime.split(" ")[0])
                }


            }
        }
    }
    
}
@Composable
fun WeatherCard(key : String, value : String) {
    Column (
        modifier = Modifier
            .padding(all = 16.dp),
             horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}

// 27.32 video

@Preview
@Composable
fun Page1Preview() {
    //Page1(weatherViewModel)
}