package com.juraj.stocksbrowser.ui.home.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.juraj.stocksbrowser.ui.home.SearchTextField
import com.juraj.stocksbrowser.ui.home.SearchTextField2
import com.juraj.stocksbrowser.ui.home.components.InstrumentListItem3
import com.juraj.stocksbrowser.ui.home.components.StockListHeaderItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navController: NavController) {
    val viewState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeScreenSideEffect.NavigateToDetails -> navController.navigate(
                NavDestinations.Details.uri(
                    sideEffect.symbol
                )
            )
        }
    }

    HomeScreen(viewState, viewModel::postIntent)
}

@Composable
private fun HomeScreen(
    state: HomeScreenState,
    action: (HomeScreenIntent) -> Unit,
) {
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }
    val scaffoldState = rememberScaffoldState()
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
                    SearchTextField2(
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

                        SearchTextField {
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
                    state.sections.flatMap { (_, value) ->
                        if (value.isVisible) value.data else emptyList()
                    }.windowed(size = 2, step = 1, true) { listItems ->
                        addListItem(listItems.first()) { action(HomeScreenIntent.OpenDetail(it.symbol)) }
                        if (listItems.size == 2 && listItems.first() is ListItem.InstrumentItem && listItems.last() is ListItem.InstrumentItem) {
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
            is ListItem.InstrumentItem -> InstrumentListItem3(listItem, onClick)
            ListItem.ShimmerItem -> TODO()
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

@Preview(showBackground = true, device = Devices.PIXEL_4)
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
                                deltaIndicator = DeltaIndicator.Up
                            )
                        ),
                        isVisible = true
                    ),
                )
            ),
        ) {}
    }
}