package com.legarrec.pokedex.pokemon

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.legarrec.pokedex.R

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PokedexView(
    content: @Composable () -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // Red sidebars and dynamic content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            // Left red sidebar
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFED1E24))
                    .drawBehind {
                        val strokeWidth = 3.dp.toPx()
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width - strokeWidth / 2, 0f),
                            end = Offset(size.width - strokeWidth / 2, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            // Dynamic content or flapper image based on isOpen
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount > 0) {
                                isOpen = true
                            }
                        }
                    }
            ) {
                if (isOpen) {
                    content()
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.flapper),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFED1E24))
                    )
                }
            }

            // Right red sidebar
            Box(
                modifier = Modifier
                    .width(31.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFED1E24))
                    .drawBehind {
                        val strokeWidth = 5.dp.toPx()
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
            )
        }
        // Header Image
        Image(
            painter = painterResource(id = R.drawable.header),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Footer Image
        Image(
            painter = painterResource(id = R.drawable.footer),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

        // Yellow button to toggle isOpen state
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.TopStart)
                .offset(x = 40.dp, y = 25.dp)
        ) {
            Button(
                modifier = Modifier
                    .size(65.dp)
                    .align(Alignment.TopStart),
                onClick = { isOpen = !isOpen },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent
                ),
                elevation = null,
                content = {  },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPokedexView() {
    PokedexView {
        Text(
            text = "Dynamic Content Here",
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .wrapContentSize(Alignment.Center)
        )
    }
}