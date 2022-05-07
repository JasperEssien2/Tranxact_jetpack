package com.crypto.tranxact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange
import com.crypto.tranxact.ui.theme.Purple50
import com.crypto.tranxact.ui.theme.TranxactTheme

class MainActivity : ComponentActivity() {
    private val viewModel = viewModels<MainViewModel>(factoryProducer = ::ViewModelFactory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.value.getAssets()
        viewModel.value.getExchanges()

        setContent {
            TranxactTheme {

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp(viewModel.value)
                }
            }
        }
    }

}

@Composable
fun MyApp(viewModel: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tranxact", textAlign = TextAlign.Center)
                },

                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center),

                elevation = 8.dp
            )
        },
        content = {

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize(),
            ) {
                AssetList(viewModel.assetStateFlow.collectAsState().value)
                ExchangeList(viewModel.exchangeStateFlow.collectAsState().value)
            }
        },
    )
}


@Composable
fun AssetList(uiState: UIState<List<Asset>>) {
    CaptionCompose(
        caption = "Assets",
        content = { assets ->
            LazyColumn {

                items(assets) { asset ->
                    ItemAsset(asset = asset)
                }

            }
        },
        uiState = uiState,
        fraction = 0.75f,
    )

}

@Composable
fun ItemAsset(asset: Asset) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = asset.id,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.padding(all = 2.dp))
            Text(
                text = asset.name, fontSize = 14.sp, color = Color(0xff525252)
            )
        }

        Text(text = "$ ${String.format("%.2f", asset.priceUSD)}", fontSize = 13.sp)
    }
}

@Composable
fun ExchangeList(uiState: UIState<List<Exchange>>) {

    CaptionCompose(
        caption = "Exchanges",
        content = { exchanges ->
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {

                items(exchanges) { exchange ->
                    ItemExchange(exchange = exchange)
                }

            }
        },
        uiState = uiState,
        fraction = 1f,
    )
}

@Composable
fun ItemExchange(exchange: Exchange) {

    Card(
        shape = RoundedCornerShape(size = 40f),
        backgroundColor = Purple50,
        modifier = Modifier
            .width(110.dp)
            .height(110.dp)
            .padding(all = 4.dp)


    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(all = 8.dp),
        ) {
            Text(
                text = exchange.id,
                fontSize = 20.sp,
            )

            Text(
                text = exchange.name, fontSize = 13.sp, color = Color(0xff444444)
            )
        }
    }
}

@Composable
fun <T> CaptionCompose(
    caption: String,
    content: @Composable (data: T) -> Unit,
    fraction: Float,
    uiState: UIState<T>,
) {

    Column(
        horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxHeight(fraction = fraction),
    ) {
        Text(

            text = caption,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp),
        )

        when {
            uiState.data != null -> content(uiState.data)
            uiState.error != null -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = uiState.error)
            }
            uiState.isLoading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }

}
