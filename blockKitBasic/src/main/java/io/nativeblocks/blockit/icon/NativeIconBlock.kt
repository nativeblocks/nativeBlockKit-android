package io.nativeblocks.blockit.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.util.blockProvideEvent
import io.nativeblocks.core.util.clickableEvents
import io.nativeblocks.core.util.getVariableValue
import io.nativeblocks.core.util.isHttpUrl
import io.nativeblocks.core.util.shapeMapper
import io.nativeblocks.core.util.spacingMapper
import io.nativeblocks.core.util.toArgb
import io.nativeblocks.core.util.widthAndHeight
import io.nativeblocks.nativejson.NativeJsonPath

class NativeIconBlock : INativeBlock {

    @Composable
    override fun BlockView(blockProps: BlockProps) {
        val visibility = blockProps.variables?.get(blockProps.block?.visibility)
        if ((visibility?.value ?: "true") == "false") {
            return
        }

        val variable = blockProps.variables?.get(blockProps.block?.key)
        val magic = blockProps.magics?.get(blockProps.block?.key)

        val value = if (blockProps.block?.jsonPath.isNullOrEmpty()) {
            variable?.value.orEmpty()
        } else {
            val query =
                blockProps.block?.jsonPath?.getVariableValue("index", "${blockProps.listItemIndex}")
            NativeJsonPath().query(variable?.value.orEmpty(), query).toString()
        }

        val properties = blockProps.block?.properties ?: mapOf()

        val width = properties["width"]?.value
        val height = properties["height"]?.value

        val paddingStart = properties["paddingStart"]?.value
        val paddingTop = properties["paddingTop"]?.value
        val paddingEnd = properties["paddingEnd"]?.value
        val paddingBottom = properties["paddingBottom"]?.value

        val shadow = properties["shadow"]?.value
        val contentDescription = properties["contentDescription"]?.value

        val shape = shapeMapper(
            properties["shape"]?.value,
            properties["shapeRadiusTopStart"]?.value,
            properties["shapeRadiusTopEnd"]?.value,
            properties["shapeRadiusBottomStart"]?.value,
            properties["shapeRadiusBottomEnd"]?.value
        )

        val onClick = blockProvideEvent(blockProps, magic.orEmpty(), "onClick")
        val onLongClick = blockProvideEvent(blockProps, magic.orEmpty(), "onLongClick")
        val onDoubleClick = blockProvideEvent(blockProps, magic.orEmpty(), "onDoubleClick")

        val modifier = Modifier
            .widthAndHeight(width, height)
            .shadow(
                elevation = shadow?.toIntOrNull()?.dp ?: 0.dp,
                shape = shape
            )
            .clickableEvents(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(true),
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick
            )
            .padding(spacingMapper(listOf(paddingStart, paddingTop, paddingEnd, paddingBottom)))

        val tintColor = toArgb(
            properties["foregroundColor"]?.value,
            properties["foregroundColorOpacity"]?.value
        )

        NativeIcon(
            modifier = modifier,
            imageUrl = value,
            contentDescription = contentDescription.orEmpty(),
            shape = shape,
            tintColor = if (tintColor == Color.Unspecified) null else tintColor,
            backgroundColor = toArgb(
                properties["backgroundColor"]?.value,
                properties["backgroundColorOpacity"]?.value
            ),
            scaleType = ContentScale.Fit
        )
    }
}

@Composable
internal fun NativeIcon(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    @DrawableRes placeHolder: Int? = null,
    placeHolderContent: @Composable (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    tintColor: Color? = null,
    backgroundColor: Color? = null,
    scaleType: ContentScale = ContentScale.None,
    contentDescription: String = "",
) {
    val modifierResult = Modifier
        .background(color = backgroundColor ?: Color.Transparent.copy(alpha = 0.0f))
        .clip(shape)
        .then(modifier)

    if (imageUrl?.isHttpUrl() == true) {
        AsyncImage(
            model = imageUrl.trim(),
            placeholder = placeHolder?.let { plc ->
                painterResource(id = plc)
            },
            error = placeHolder?.let { plc ->
                painterResource(id = plc)
            },
            fallback = placeHolder?.let { plc ->
                painterResource(id = plc)
            },
            contentDescription = contentDescription,
            contentScale = scaleType,
            colorFilter = tintColor?.let { color ->
                ColorFilter.tint(color = color)
            },
            modifier = modifierResult
        )
    } else {
        placeHolderContent?.invoke()
    }
}
