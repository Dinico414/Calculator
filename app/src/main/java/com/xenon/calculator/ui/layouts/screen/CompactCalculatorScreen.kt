//package com.xenon.calculator.ui.layouts.screen
//
//import android.content.res.Configuration.UI_MODE_NIGHT_NO
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.xenon.calculator.ui.layouts.ButtonLayout
//import com.xenon.calculator.viewmodel.CalculatorViewModel
//
//@Composable
//fun CompactCalculatorScreen(viewModel: CalculatorViewModel) {
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Display area (takes up remaining space or a fixed portion)
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f) // Takes up space not used by ButtonLayout
//                .padding(15.dp) // Add padding around the display area
//                .clip(RoundedCornerShape(20.dp)) // Rounded corners
//                .background(MaterialTheme.colorScheme.secondaryContainer) // Background color
//        ) {
//            DisplaySection(
//                currentInput = viewModel.currentInput, // Access directly
//                result = viewModel.result,           // Access directly
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp) // Inner padding for the text content
//            )
//        }
//
//        // Button Layout - constrained to max 75% of screen height
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(max = screenHeight * 0.75f) // Max 75% of screen height
//        ) {
//            ButtonLayout(
//                viewModel = viewModel,
//                isTablet = false, // Replace with actual logic
//                isLandscape = false, // Replace with actual logic
//                modifier = Modifier.fillMaxSize() // ButtonLayout will fill the Box
//            )
//        }
//    }
//}
//
//@Composable
//fun DisplaySection(currentInput: String, result: String, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.End,
//        verticalArrangement = Arrangement.Bottom // Align text to the bottom
//    ) {
//        // Workings Text (Current Input)
//        Text(
//            text = currentInput,
//            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 36.sp), // Adjust size as needed
//            color = MaterialTheme.colorScheme.onSecondaryContainer, // Text color on screen
//            textAlign = TextAlign.End,
//            maxLines = 3,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        // Result Text
//        Text(
//            text = result,
//            style = MaterialTheme.typography.displaySmall.copy(fontSize = 48.sp), // Adjust size as needed
//            color = MaterialTheme.colorScheme.onSecondaryContainer,
//            textAlign = TextAlign.End,
//            maxLines = 1,
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DisplaySectionPreview() {
//    MaterialTheme { // It's good practice to wrap previews in your theme
//        DisplaySection(currentInput = "123 + 456", result = "579")
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun CalculatorScreenPreview() {
//    val fakeViewModel = remember { CalculatorViewModel() }
//    MaterialTheme { // It's good practice to wrap previews in your theme
//        CalculatorScreen(viewModel = fakeViewModel)
//    }
//}
//
//@Preview(showBackground = true, name = "Calculator Screen Phone Portrait")
//@Composable
//fun CalculatorScreenPhonePortraitPreview() {
//    val fakeViewModel = remember { CalculatorViewModel() }
//    MaterialTheme { // It's good practice to wrap previews in your theme
//        CalculatorScreen(viewModel = fakeViewModel)
//    }
//}
//
//@Preview(showBackground = true, name = "Calculator Screen Tablet Landscape", device = Devices.TABLET, uiMode = UI_MODE_NIGHT_NO, widthDp = 1280, heightDp = 800)
//@Composable
//fun CalculatorScreenTabletLandscapePreview() {
//    val fakeViewModel = remember { CalculatorViewModel() }
//    MaterialTheme { // It's good practice to wrap previews in your theme
//        CalculatorScreen(viewModel = fakeViewModel)
//    }
//}