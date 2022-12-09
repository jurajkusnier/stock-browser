package com.juraj.stocksbrowser.ui.home.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.juraj.stocksbrowser.navigation.NavDestinations
import com.juraj.stocksbrowser.ui.common.SimpleTopAppBar
import com.juraj.stocksbrowser.ui.common.showErrorSnackBar
import com.juraj.stocksbrowser.ui.home.components.InstrumentListItem
import com.juraj.stocksbrowser.ui.home.components.SearchTextField
import com.juraj.stocksbrowser.ui.home.components.SearchTextFieldPlaceholder
import com.juraj.stocksbrowser.ui.home.components.ShimmerListItem
import com.juraj.stocksbrowser.ui.home.components.StockListHeaderItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme
import kotlinx.coroutines.CoroutineScope
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navController: NavController) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeScreenSideEffect.NavigateToDetails -> navController.navigate(
                NavDestinations.Details.uri(sideEffect.symbol, sideEffect.type)
            )
            HomeScreenSideEffect.NetworkError ->
                coroutineScope
                    .showErrorSnackBar(scaffoldState.snackbarHostState) {
                        viewModel.postIntent(HomeScreenIntent.Refresh)
                    }
        }
    }

    HomeScreen(viewState, scaffoldState, viewModel::postIntent)
}

@Composable
private fun HomeScreen(
    state: HomeScreenState,
    scaffoldState: ScaffoldState,
    action: (HomeScreenIntent) -> Unit,
) {
    var textFieldState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue()
        )
    }
    val lazyListState = rememberLazyListState()
    val scrolledListState by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0f } }
    val focusRequester = remember { FocusRequester() }

    Box(
        Modifier
            .fillMaxWidth()
    ) {
        Scaffold(topBar = {
            SimpleTopAppBar(scrolledListState || state.isSearching) {
                AnimatedVisibility(
                    visible = state.isSearching,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SearchTextField(
                        textFieldState,
                        focusRequester,
                        { textFieldValue ->
                            textFieldValue
                                .copy(text = textFieldValue.text.replace('\n', ' '))
                                .let {
                                    textFieldState = it
                                    action(HomeScreenIntent.Search(it.text))
                                }
                        }
                    ) {
                        textFieldState = TextFieldValue()
                        action(HomeScreenIntent.SetSearchState(false))
                    }

                    LaunchedEffect(state.isSearching) {
                        if (state.isSearching) {
                            focusRequester.requestFocus()
                        }
                    }
                }

                AnimatedVisibility(
                    visible = state.isSearching.not(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                "Stocks Browser",
                                style = MaterialTheme.typography.h1
                            )
                        }

                        SearchTextFieldPlaceholder {
                            action(HomeScreenIntent.SetSearchState(true))
                        }
                    }
                }
            }
        }, scaffoldState = scaffoldState) { paddingValue ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(paddingValue),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                LazyColumn(state = lazyListState) {
                    state.sections.toListItem().map { (listItem, showDivider) ->
                        addListItem(listItem) { action(HomeScreenIntent.OpenDetail(it)) }
                        if (showDivider) {
                            addDivider()
                        }
                    }
                }
            }
        }
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

private fun LazyListScope.addDivider() {
    item {
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

private fun LazyListScope.addListItem(
    listItem: ListItem,
    onClick: (ListItem.InstrumentItem) -> Unit
) {
    item {
        when (listItem) {
            is ListItem.HeaderItem -> StockListHeaderItem(listItem.readableType())
            is ListItem.InstrumentItem -> InstrumentListItem(listItem, onClick)
            ListItem.ShimmerItem -> ShimmerListItem()
        }
    }
}

private fun ListItem.HeaderItem.readableType(): String {
    return when (type) {
        HeaderType.Favorites -> "Favorites"
        HeaderType.MostPopularStocks -> "Most Popular Stocks"
        HeaderType.MostPopularEtfs -> "Most Popular ETFs"
    }
}

private fun Map<ScreenSection.Type, ScreenSection>.toListItem(): List<ListItemWithDivider> {
    return this.filter { (_, value) -> value.isVisible }
        .toList()
        .sortedBy { it.first.order }
        .flatMap { it.second.data }
        .windowed(size = 2, step = 1, true) { listItems ->
            ListItemWithDivider(
                listItem = listItems.first(),
                showDivider = listItems.size == 2 && listItems.first() is ListItem.InstrumentItem && listItems.last() is ListItem.InstrumentItem
            )
        }
}

private data class ListItemWithDivider(val listItem: ListItem, val showDivider: Boolean)

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Preview(showBackground = true, device = Devices.PIXEL_4, name = "Light Mode")
@Composable
fun HomeScreen_Preview() {
    StocksBrowserTheme {
        HomeScreen(
            HomeScreenState(
                isSearching = false,
                sections = mapOf(
                    ScreenSection.Type.MostPopularStocks to ScreenSection(
                        data = listOf(
                            ListItem.HeaderItem(HeaderType.Favorites),
                            ListItem.InstrumentItem(
                                symbol = "IBM",
                                name = "International Business Machines Corp",
                                lastSalePrice = "$14.5",
                                percentageChange = "0.5%",
                                deltaIndicator = DeltaIndicator.Up,
                                type = InstrumentType.Stock
                            )
                        ),
                        isVisible = true
                    ),
                )
            ),
            rememberScaffoldState()
        ) {}
    }
}
