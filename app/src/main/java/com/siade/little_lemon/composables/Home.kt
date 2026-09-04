package com.siade.little_lemon.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.siade.little_lemon.MenuItemRoom
import com.siade.little_lemon.Profile
import com.siade.little_lemon.R
import com.siade.little_lemon.ui.theme.LittlelemonTheme
import com.siade.little_lemon.ui.theme.LittleLemonCloud
import com.siade.little_lemon.ui.theme.LittleLemonGreen
import com.siade.little_lemon.ui.theme.LittleLemonYellow

@Composable
fun Home(
    navController: NavHostController,
    menuItems: List<MenuItemRoom>,
    modifier: Modifier = Modifier
) {
    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val categories = menuItems.map { it.category }.distinct()

    val filteredMenuItems = menuItems
        .filter { searchPhrase.isBlank() || it.title.contains(searchPhrase, ignoreCase = true) }
        .filter { selectedCategory.isBlank() || it.category.equals(selectedCategory, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Header(navController = navController)
        HeroSection(
            searchPhrase = searchPhrase,
            onSearchPhraseChange = { searchPhrase = it }
        )
        MenuBreakdown(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategoryClick = { category ->
                selectedCategory = if (selectedCategory == category) "" else category
            }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        MenuItems(items = filteredMenuItems)
    }
}

@Composable
private fun Header(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon logo",
            modifier = Modifier.height(50.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(50.dp)
                .clip(RoundedCornerShape(50))
                .clickable { navController.navigate(Profile.route) }
        )
    }
}

@Composable
private fun HeroSection(
    searchPhrase: String,
    onSearchPhraseChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LittleLemonGreen)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Little Lemon",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = LittleLemonYellow
        )
        Text(
            text = "Chicago",
            fontSize = 24.sp,
            color = Color.White
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "We are a family-owned Mediterranean restaurant, " +
                        "focused on traditional recipes served with a modern twist.",
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.hero_image),
                contentDescription = "Hero image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        TextField(
            value = searchPhrase,
            onValueChange = onSearchPhraseChange,
            placeholder = { Text("Enter Search Phrase") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Composable
private fun MenuBreakdown(
    categories: List<String>,
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "ORDER FOR DELIVERY!",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Button(
                    onClick = { onCategoryClick(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) LittleLemonGreen else LittleLemonCloud,
                        contentColor = if (isSelected) Color.White else LittleLemonGreen
                    )
                ) {
                    Text(text = category.replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}

@Composable
fun MenuItems(items: List<MenuItemRoom>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        items.forEach { item ->
            MenuItem(item = item)
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MenuItem(item: MenuItemRoom) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.description,
                color = LittleLemonGreen,
                maxLines = 2,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "$${item.price}",
                color = LittleLemonGreen,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        GlideImage(
            model = item.image,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    LittlelemonTheme {
        Home(
            navController = rememberNavController(),
            menuItems = emptyList()
        )
    }
}
