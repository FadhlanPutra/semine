package com.lann.semine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lann.semine.ui.theme.SemineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SemineTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppPlaceholder(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Layar kosong sementara sampai Fase 1. Satu-satunya teks diambil dari resource.
@Composable
private fun AppPlaceholder(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_name),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPlaceholderPreview() {
    SemineTheme {
        AppPlaceholder()
    }
}
