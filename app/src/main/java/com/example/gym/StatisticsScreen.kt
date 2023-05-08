package com.example.gym.database

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.DatabaseViewModel
import com.example.gym.ExerciseStatistic
import com.example.gym.SessionStatistic
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlin.random.Random

@Composable
fun StatisticsScreen(
    repoModel: DatabaseViewModel,
    modifier: Modifier = Modifier
) {
    val sessions = repoModel.retrieveSessionsWithinMonthFromDB().collectAsState(initial = listOf())
//    var statistics by remember {
//        mutableStateOf(mutableListOf<SessionStatistic>())
//    }
    var foundRoutines by remember {
        mutableStateOf(mutableListOf<String>())
    }
//    var routinesMap by remember {
//        mutableStateOf(mutableMapOf<String, List<String>>())
//    }
    var entryModels by remember {
        mutableStateOf(emptyList<ChartEntryModel>())
    }
//    val entries by remember { mutableStateOf(mutableListOf<List<FloatEntry>>()) }
    LaunchedEffect(key1 = sessions.value) {
        var statistics = mutableListOf<SessionStatistic>()
//        var foundRoutines = mutableListOf<String>()
        sessions.value.forEach { entry ->
                val index = foundRoutines.indexOf(entry.routineName)
                if (index != -1) {
                    for (i in 0 until entry.repCounts.size) {
                        statistics[index].exerciseStatistics[i].repCounts.add(entry.repCounts[i].toFloat())
                    }
                } else {
                    foundRoutines.add(entry.routineName)
                    val routine = repoModel.retrieveRoutineByName(entry.routineName)
                    val exerciseStatistics = routine.exercises.mapIndexed { index, exercise ->
                        ExerciseStatistic(
                            exerciseName = exercise.name,
                            repCounts = mutableListOf(entry.repCounts[index].toFloat())
                        )
                    }
                    statistics.add(
                        SessionStatistic(
                            routineName = entry.routineName,
                            exerciseStatistics = exerciseStatistics.toMutableList()
                        )
                    )
                }
        }
        var newModels = mutableListOf<ChartEntryModel>()
        statistics.forEach { statistic ->
            val entries = mutableListOf<List<FloatEntry>>()
            statistic.exerciseStatistics.forEach {
                entries.add(it.repCounts.mapIndexed { index, count -> FloatEntry(index.toFloat(), count) })
            }
            newModels.add(entryModelOf(*entries.toTypedArray()))
        }
        entryModels = newModels
        Log.d("Statistics Screen", statistics.toString())
    }
    val test1 = listOf(entriesOf(1f, 4f, 6f), entriesOf(5f, 10f, 4f))
//    val test2 = listOf(entriesOf(1f, 4f, 6f), entriesOf(5f, 10f, 4f))
//    val test3 = listOf(entriesOf(1f, 4f, 6f), entriesOf(5f, 10f, 4f))
//
//    val chartEntryModel = entryModelOf(*entries.toTypedArray())
    val chartEntryModel2 = entryModelOf(*test1.toTypedArray())
//    val chartEntryModels2 by remember {
//        mutableStateOf(listOf(entryModelOf(*test1.toTypedArray()), entryModelOf(*test2.toTypedArray()), entryModelOf(*test3.toTypedArray())))
//    }
    Column(
        modifier = modifier
    ) {
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(48.dp)
                ){
            Log.d("From outsidee Lazy", "Hello")
            itemsIndexed(entryModels) { index, item ->
                Log.d("From Lazy", "Hello")
//                val entries by remember { mutableStateOf(mutableListOf<List<FloatEntry>>()) }
//                LaunchedEffect(key1 = item) {
//                    statistics[index].exerciseStatistics.forEach {
//                        entries.add(it.repCounts.mapIndexed { index, count -> FloatEntry(index.toFloat(), count) })
//                    }
//                }
//                val chartEntryModel = entryModelOf(*entries.toTypedArray())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = foundRoutines[index])
                    ProvideChartStyle(rememberChartStyle(randomListColors(item.entries.size))) {
                        val defaultLines = currentChartStyle.lineChart.lines
                        Chart(
                            chart = lineChart(
                                remember(defaultLines) {
                                    defaultLines.map { defaultLine -> defaultLine.copy(lineBackgroundShader = null) }
                                },
                            ),
                            model = item,
                            startAxis = startAxis(
                                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside
                            ),
                            bottomAxis = bottomAxis(),
                            modifier = Modifier.width(360.dp)
                        )
                    }
                }
            }
        }
        Chart(
            chart = LineChart(),
            model = chartEntryModel2,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
        )
    }
}

fun randomListColors(numberOfColors: Int): List<Color> {
    var colors = mutableListOf<Color>()
    for (i in 0 until numberOfColors) {
        colors.add(randomColor())
    }
    return colors
}

fun randomColor(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

@Composable
internal fun rememberChartStyle(columnChartColors: List<Color>, lineChartColors: List<Color>): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color(defaultColors.axisLineColor),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}

@Composable
internal fun rememberChartStyle(chartColors: List<Color>) =
    rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)

@Composable
private fun rememberLegend(chartColors: List<Color>, exerciseNames: List<String>) = verticalLegend(
    items = chartColors.mapIndexed { index, chartColor ->
        verticalLegendItem(
            icon = shapeComponent(Shapes.pillShape, chartColor),
            label = textComponent(
                color = currentChartStyle.axis.axisLabelColor,
                textSize = legendItemLabelTextSize,
            ),
            labelText = exerciseNames[index],
        )
    },
    iconSize = legendItemIconSize,
    iconPadding = legendItemIconPaddingValue,
    spacing = legendItemSpacing,
    padding = legendPadding,
)

private val legendItemLabelTextSize = 12.sp
private val legendItemIconSize = 8.dp
private val legendItemIconPaddingValue = 10.dp
private val legendItemSpacing = 4.dp
private val legendTopPaddingValue = 8.dp
private val legendPadding = dimensionsOf(top = legendTopPaddingValue)
