package com.shnaki.kmpexample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Root composable — the single Compose UI shared across Desktop, Android,
 * Web (Wasm), and iOS.
 *
 * Try changing the UI here and see the same change reflected on every platform!
 */
@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var count by remember { mutableIntStateOf(0) }
            val platform = getPlatform()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Kotlin Multiplatform",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Running on: ${platform.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.displaySmall,
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = { count++ }) {
                    Text("Click me!")
                }
            }
        }
    }
}
