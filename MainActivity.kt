// ...existing code...

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is MainActivityState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MainActivityState.Success -> {
            val data = (state as MainActivityState.Success).data
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = data)
            }
        }
        is MainActivityState.Error -> {
            val message = (state as MainActivityState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = message, color = MaterialTheme.colors.error)
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    val viewModel = MainActivityViewModel()
    MainScreen(viewModel = viewModel)
}

// ...existing code...
