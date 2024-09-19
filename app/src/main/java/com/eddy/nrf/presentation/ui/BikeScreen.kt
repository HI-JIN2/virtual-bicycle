package com.eddy.nrf.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eddy.nrf.R
import com.eddy.nrf.presentation.ui.component.AnimatedImage
import com.eddy.nrf.presentation.ui.component.Pas
import com.eddy.nrf.presentation.ui.component.Speed
import com.eddy.nrf.presentation.ui.component.VerticalSlider
import com.eddy.nrf.presentation.ui.theme.DarkPrimary
import com.eddy.nrf.presentation.ui.theme.NRFTheme
import com.eddy.nrf.presentation.ui.theme.Primary
import com.eddy.nrf.presentation.ui.theme.Typography
import kotlinx.coroutines.delay
import java.time.LocalTime


@Composable
fun BikeScreen(
    bikeViewModel: BikeViewModel = BikeViewModel()
) { //todo uistate를 주입 받는 방법은? 메소드도 파라미터로 넘기고

    val bikeUiState by bikeViewModel.uiState.collectAsStateWithLifecycle() //이걸로해야 값을 계속 관찰할 수 있음~

    var proportionalFactorSliderValue by remember { mutableStateOf(1f) }
    var targetBatterySliderValue by remember { mutableStateOf(100f) }

    Column(
        modifier = Modifier.fillMaxSize() // 화면 전체 크기를 채움
    ) {
        Box(
            modifier = Modifier
                .weight(1f)  // 남은 공간을 모두 차지
                .fillMaxWidth()  // 너비는 부모의 너비로 채움
                .background(Primary)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedImage(
                    modifier = Modifier
                        .weight(1f),
                    speed = bikeUiState.speed
                )
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())// 화면 전체 크기를 채움
                ) {
                    //비례값 조정
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .size(100.dp, 320.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        VerticalSlider(
                            modifier = Modifier.size(20.dp, 5.dp),
                            value = proportionalFactorSliderValue,
                            onValueChange = {
                                proportionalFactorSliderValue = it
                                bikeViewModel.changeProportionalFactor(it)
                            },
                            valueRange = 0f..1.5f,
//                            steps = 2, //간격 없음!
                        )

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(DarkPrimary)
                                .size(70.dp) //얘의 부모의 너비가 100이어서 가로가 짤린거임~!!@!!!
                                .aspectRatio(1f)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "비례값",
                                color = Color.White,
                                style = Typography.displaySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            text =
                            String.format(
                                "%.1f",
                                proportionalFactorSliderValue
                            ),
                            color = Color.White,
                            style = Typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    //배터리 조정
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .size(100.dp, 320.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        VerticalSlider(
                            modifier = Modifier.size(20.dp, 10.dp),
                            value = targetBatterySliderValue,
                            onValueChange = {
                                targetBatterySliderValue = it
                                bikeViewModel.changeTargetBattery(it)
                            },
                            valueRange = 0f..100.0F,
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(DarkPrimary)
                                .size(70.dp) //얘의 부모의 너비가 100이어서 가로가 짤린거임~!!@!!!
                                .aspectRatio(1f)
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "배터리\n목표값",
                                style = Typography.displaySmall,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            text =
                            String.format(
                                "%.1f",
                                targetBatterySliderValue
                            ),
                            style = Typography.bodySmall,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())// 화면 전체 크기를 채움
                    ,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically // 세로 방향으로 중앙 정렬
                    ) {
                        Text(
                            text = String.format("%.0f", bikeUiState.battery) + "%",
                            color = Color.White,
                            style = Typography.bodyMedium,
                        )
                        Image(
                            painter = painterResource(id = R.drawable.img_battery),  // 리소스 이미지 사용
                            contentDescription = "Example Image",
                            colorFilter = ColorFilter.tint(
                                Color.White,
                                BlendMode.SrcIn
                            ), // 색상 및 혼합 모드 설정
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Speed(
                        modifier = Modifier.size(200.dp),
                        speed = bikeUiState.speed
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Primary)
                            .padding(top = 10.dp, end = 20.dp, start = 20.dp, bottom = 10.dp),
                    ) {
                        Text(
                            text = "ODO  " + String.format("%.2f", bikeUiState.distance) + "km",
                            color = Color.White,
                            style = Typography.bodyMedium,
                        )
                    }

                    Pas(
                        modifier = Modifier.padding(end = 10.dp),
                        selectedValue = bikeUiState.gear,
//                    selected.toFloat(), //사용자로 하여금 바꾸고 싶은 값은 uistate로 하면 안됨
                        onSelect = { newIndex ->
//                        selected = newIndex
                            bikeViewModel.changeGear(newIndex)
                        },
                        viewModel = bikeViewModel
                    )

                }


            }
        }
    }
}

@Composable
fun realTimeClock(): String {
    var currentTime by remember { mutableStateOf(LocalTime.now()) } // 현재 시간 상태

    // LaunchedEffect를 사용하여 일정 시간마다 상태 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now() // 현재 시간 갱신
            delay(1000L) // 1초마다 시간 업데이트
        }
    }

    val timeText = "${currentTime.hour}:${currentTime.minute}:${currentTime.second}"

    return timeText
}


@Preview(
    showBackground = true,
    name = "Galaxy Tab A9+",
    device = "spec:width=1800dp,height=1100dp,dpi=260"
//    widthDp = 1920, heightDp = 1200
)
@Composable
fun BikePreview() {
    NRFTheme {
        BikeScreen()
    }
}