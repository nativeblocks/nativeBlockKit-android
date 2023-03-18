package io.nativeblocks.blockit.basic

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.nativeblocks.uikit.block.NativeImage
import io.nativeblocks.uikit.block.NativeImageProperties

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NativeSideIconBlock(
    iconUrl: String?,
    foregroundColor: Color?,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
): @Composable (() -> Unit)? {
    if (iconUrl.isNullOrEmpty()) return null

    return {
        val modifier = Modifier
            .size(ButtonDefaults.IconSize)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(true),
                onClick = onClick,
                onLongClick = onLongClick
            )
        NativeImage(
            modifier = modifier,
            imageUrl = iconUrl,
            properties = NativeImageProperties(foregroundColor = foregroundColor)
        )
    }
}