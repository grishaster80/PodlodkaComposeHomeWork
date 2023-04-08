package com.debug.sample

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackdropScreen() {
    BoxWithConstraints(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxSize()
    ) {
        val backdropState = rememberBackdropScaffoldState(BackdropValue.Revealed)
        Log.e("@@@","offset is "+backdropState.offset.value)
        Log.e("@@@","maxHeight is "+maxHeight.value)
        val initialTopMargin = remember {backdropState.offset.value - maxHeight.value*2 + CARD_HEIGHT}
        Log.e("@@@","initialTopMargin is "+initialTopMargin)
        val dynamicTopMargin = remember {
            derivedStateOf {
                Log.e("@@@","offset is "+backdropState.offset.value)
                Log.e("@@@","maxHeight is "+maxHeight.value)
                val currentMargin = backdropState.offset.value - maxHeight.value*2 + CARD_HEIGHT
                if(currentMargin.absoluteValue > 260) {
                    -260f
                } else {
                    currentMargin
                }
            }
        }
        val dynamicAlpha = remember {
            derivedStateOf {
                val currentAlpha = backdropState.offset.value/(maxHeight.value*3f)
                if(currentAlpha < 0.4f) {
                    0f
                } else {
                    currentAlpha
                }
            }
        }
        val dynamicScale = remember {
            derivedStateOf {
                minOf((32 * initialTopMargin/(initialTopMargin * dynamicTopMargin.value)).absoluteValue, 1f)
            }
        }
        val dynamicScaleTmp = remember {
            derivedStateOf {
                (4 * initialTopMargin/(initialTopMargin * dynamicTopMargin.value)).absoluteValue
            }
        }
        BackdropScaffold(
            appBar = {},
            backLayerContent = { ScreenContent(dynamicTopMargin, dynamicAlpha, dynamicScale, dynamicScaleTmp) },
            frontLayerContent = {},
            scaffoldState = backdropState,
        )
    }
}

@Composable
fun ScreenContent(topSpacerDynamicHeight: State<Float>, dynamicAlpha: State<Float>, dynamicScale: State<Float>, tmp: State<Float>) {
    Log.e("@@@", "tmp is " + tmp.value)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = topSpacerDynamicHeight.value
                }
                .scale(dynamicScale.value)
                .background(color = Color.Green, shape = RoundedCornerShape(16.dp))
                .width(CARD_WIDTH.dp)
                .height(CARD_HEIGHT.dp)
        ) {}
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = dynamicAlpha.value
                }
                .background(color = Color.Blue, shape = RoundedCornerShape(16.dp))
                .width(CARD_WIDTH.dp)
                .height(CARD_HEIGHT.dp)
        ) {}
    }
}

private const val CARD_HEIGHT = 100f
private const val CARD_WIDTH = 150f