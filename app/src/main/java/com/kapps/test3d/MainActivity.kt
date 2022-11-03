package com.kapps.test3d

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapps.test3d.ui.theme.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = orange.toArgb()
            window.navigationBarColor = orange.toArgb()
            Test3DTheme {
                var showDescription by remember {
                    mutableStateOf(false)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .padding(30.dp)
                    ,
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            "Preferred Programming Languages",
                            fontWeight = FontWeight.Bold,
                            color = white,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                        BarChart(
                            listOf(
                                BarchartInput(28,"Kotlin", orange),
                                BarchartInput(15,"Swift", brightBlue),
                                BarchartInput(11,"Ruby", green),
                                BarchartInput(7,"Cobol", purple),
                                BarchartInput(14,"C++", blueGray),
                                BarchartInput(9,"C", redOrange),
                                BarchartInput(21,"Python", darkGray)
                            ),
                            modifier = Modifier
                                .fillMaxWidth(),
                            showDescription = showDescription
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                "Show description",
                                color = white,
                                fontWeight = FontWeight.SemiBold
                            )
                            Switch(
                                checked = showDescription,
                                onCheckedChange = {
                                    showDescription = it
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = orange,
                                    uncheckedThumbColor = white
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BarChart(
    inputList:List<BarchartInput>,
    modifier: Modifier = Modifier,
    showDescription:Boolean
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ){
        val listSum by remember {
            mutableStateOf(inputList.sumOf { it.value })
        }
        inputList.forEach { input ->
            val percentage = input.value/listSum.toFloat()
            Bar(
                modifier = Modifier
                    .height(120.dp * percentage * inputList.size)
                    .width(40.dp),
                primaryColor = input.color,
                percentage = input.value/listSum.toFloat(),
                description = input.description,
                showDescription = showDescription
            )
        }
    }
}

@Composable
fun Bar(
    modifier: Modifier = Modifier,
    primaryColor:Color,
    percentage:Float,
    description: String,
    showDescription: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = Modifier.fillMaxSize()
        ){
            val width = size.width
            val height = size.height
            val barWidth = width / 5 * 3
            val barHeight = height / 8 * 7
            val barHeight3DPart = height - barHeight
            val barWidth3DPart = (width - barWidth)*(height*0.002f)

            var path = Path().apply {
                moveTo(0f,height)
                lineTo(barWidth,height)
                lineTo(barWidth,height-barHeight)
                lineTo(0f,height-barHeight)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(gray, primaryColor)
                )
            )
            path = Path().apply {
                moveTo(barWidth,height-barHeight)
                lineTo(barWidth3DPart+barWidth,0f)
                lineTo(barWidth3DPart+barWidth,barHeight)
                lineTo(barWidth,height)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, gray)
                )
            )
            path = Path().apply {
                moveTo(0f,barHeight3DPart)
                lineTo(barWidth,barHeight3DPart)
                lineTo(barWidth+barWidth3DPart,0f)
                lineTo(barWidth3DPart,0f)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(gray, primaryColor)
                )
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${(percentage*100).roundToInt()} %",
                    barWidth/5f,
                    height + 55f,
                    android.graphics.Paint().apply {
                        this.color = white.toArgb()
                        textSize = 11.dp.toPx()
                        isFakeBoldText = true
                    }
                )
            }
            if(showDescription){
                drawContext.canvas.nativeCanvas.apply {
                    rotate(-50f, pivot = Offset(barWidth3DPart-20,0f)){
                        drawText(
                            description,
                            0f,
                            0f,
                            android.graphics.Paint().apply {
                                this.color = white.toArgb()
                                textSize = 14.dp.toPx()
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
        }
    }

}

data class BarchartInput(
    val value:Int,
    val description:String,
    val color:Color
)


