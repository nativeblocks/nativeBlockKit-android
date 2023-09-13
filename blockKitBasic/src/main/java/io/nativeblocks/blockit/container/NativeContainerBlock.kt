package io.nativeblocks.blockit.container

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.frame.domain.model.NativeBlockDynamicPropertiesModel
import io.nativeblocks.core.util.blockProvideEvent
import io.nativeblocks.core.util.clickableEvents
import io.nativeblocks.core.util.findAlignment
import io.nativeblocks.core.util.findAlignmentHorizontal
import io.nativeblocks.core.util.findAlignmentVertical
import io.nativeblocks.core.util.findArrangementHorizontal
import io.nativeblocks.core.util.findArrangementVertical
import io.nativeblocks.core.util.shapeMapper
import io.nativeblocks.core.util.spacingMapper
import io.nativeblocks.core.util.toArgb
import io.nativeblocks.core.util.widthAndHeight

class NativeContainerBlock : INativeBlock {
    @Composable
    override fun BlockView(blockProps: BlockProps) {
        val visibility = blockProps.variables?.get(blockProps.block?.visibility)
        if ((visibility?.value ?: "true") == "false") {
            return
        }

        val properties = blockProps.block?.properties ?: mapOf()
        val magic = blockProps.magics?.get(blockProps.block?.key)

        val width = properties["width"]?.value
        val height = properties["height"]?.value

        val paddingStart = properties["paddingStart"]?.value
        val paddingTop = properties["paddingTop"]?.value
        val paddingEnd = properties["paddingEnd"]?.value
        val paddingBottom = properties["paddingBottom"]?.value

        val shadow = properties["shadow"]?.value
        val scrollable = properties["scrollable"]?.value

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

        var modifier = Modifier
            .widthAndHeight(width, height)
            .shadow(
                elevation = shadow?.toIntOrNull()?.dp ?: 0.dp,
                shape = shape
            )
            .background(
                color = toArgb(
                    properties["backgroundColor"]?.value,
                    properties["backgroundColorOpacity"]?.value
                ),
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

        when (properties["direction"]?.value) {
            "Y" -> { // VERTICAL
                if (scrollable.toBoolean()) {
                    modifier = modifier.then(Modifier.verticalScroll(rememberScrollState()))
                }
                VerticalContainer(modifier, blockProps, properties)
            }

            "X" -> { // HORIZONTAL
                if (scrollable.toBoolean()) {
                    modifier = modifier.then(Modifier.horizontalScroll(rememberScrollState()))
                }
                HorizontalContainer(modifier, blockProps, properties)
            }

            "Z" -> { // FLAT
                OverlayContainer(modifier, blockProps, properties)
            }
        }
    }
}

@Composable
private fun VerticalContainer(
    modifier: Modifier,
    blockProps: BlockProps,
    properties: Map<String, NativeBlockDynamicPropertiesModel>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = findArrangementVertical(properties["verticalArrangement"]?.value),
        horizontalAlignment = findAlignmentHorizontal(properties["horizontalAlignment"]?.value)
    ) {
        blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
    }
}

@Composable
private fun HorizontalContainer(
    modifier: Modifier,
    blockProps: BlockProps,
    properties: Map<String, NativeBlockDynamicPropertiesModel>,
) {
    Row(
        modifier = modifier,
        verticalAlignment = findAlignmentVertical(properties["verticalAlignment"]?.value),
        horizontalArrangement = findArrangementHorizontal(properties["horizontalArrangement"]?.value)
    ) {
        blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
    }
}

@Composable
private fun OverlayContainer(
    modifier: Modifier,
    blockProps: BlockProps,
    properties: Map<String, NativeBlockDynamicPropertiesModel>,
) {
    Box(
        modifier = modifier,
        contentAlignment = findAlignment(properties["contentAlignment"]?.value)
    ) {
        blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
    }
}