package com.siade.little_lemon.composables

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.siade.little_lemon.Onboarding
import com.siade.little_lemon.R
import com.siade.little_lemon.ui.theme.LittlelemonTheme
import com.siade.little_lemon.ui.theme.LittleLemonCharcoal
import com.siade.little_lemon.ui.theme.LittleLemonGreen
import com.siade.little_lemon.ui.theme.LittleLemonYellow

@Composable
fun Profile(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    val firstName = sharedPreferences.getString("first_name", "") ?: ""
    val lastName = sharedPreferences.getString("last_name", "") ?: ""
    val email = sharedPreferences.getString("email", "") ?: ""

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(vertical = 24.dp)
        )

        Text(
            text = "Profile information",
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(LittleLemonGreen)
                .padding(vertical = 40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ProfileField(label = "First name", value = firstName)
            ProfileField(label = "Last name", value = lastName)
            ProfileField(label = "Email", value = email)

            Column(modifier = Modifier.weight(1f)) {}

            Button(
                onClick = {
                    sharedPreferences.edit().clear().apply()
                    navController.navigate(Onboarding.route) {
                        popUpTo(0)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleLemonYellow,
                    contentColor = LittleLemonCharcoal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(text = "Log out", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = label,
            color = LittleLemonGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = LittleLemonCharcoal,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    LittlelemonTheme {
        Profile(navController = rememberNavController())
    }
}
