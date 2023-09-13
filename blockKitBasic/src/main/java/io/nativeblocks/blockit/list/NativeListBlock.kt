package io.nativeblocks.blockit.list

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.util.blockProvideEvent
import io.nativeblocks.core.util.clickableEvents
import io.nativeblocks.core.util.findAlignmentHorizontal
import io.nativeblocks.core.util.findAlignmentVertical
import io.nativeblocks.core.util.findArrangementHorizontal
import io.nativeblocks.core.util.findArrangementVertical
import io.nativeblocks.core.util.shapeMapper
import io.nativeblocks.core.util.spacingMapper
import io.nativeblocks.core.util.toArgb
import io.nativeblocks.core.util.widthAndHeight
import io.nativeblocks.nativejson.NativeJsonPath

class NativeListBlock : INativeBlock {

    @Composable
    override fun BlockView(blockProps: BlockProps) {
        val visibility = blockProps.variables?.get(blockProps.block?.visibility)
        if ((visibility?.value ?: "true") == "false") {
            return
        }

        val variable = blockProps.variables?.get(blockProps.block?.key)
        val properties = blockProps.block?.properties ?: mapOf()
        val magic = blockProps.magics?.get(blockProps.block?.key)

        val list: List<*> = try {
            val query = blockProps.block?.jsonPath
            NativeJsonPath().query(variable?.value.orEmpty(), query) as List<*>
        } catch (e: Exception) {
            e.printStackTrace()
            listOf<Any>()
        }

        val width = properties["width"]?.value
        val height = properties["height"]?.value

        val paddingStart = properties["paddingStart"]?.value
        val paddingTop = properties["paddingTop"]?.value
        val paddingEnd = properties["paddingEnd"]?.value
        val paddingBottom = properties["paddingBottom"]?.value

        val shape = shapeMapper(
            properties["shape"]?.value,
            properties["shapeRadiusTopStart"]?.value,
            properties["shapeRadiusTopEnd"]?.value,
            properties["shapeRadiusBottomStart"]?.value,
            properties["shapeRadiusBottomEnd"]?.value
        )

        val shadow = properties["shadow"]?.value

        val onClick = blockProvideEvent(blockProps, magic.orEmpty(), "onClick")
        val onLongClick = blockProvideEvent(blockProps, magic.orEmpty(), "onLongClick")
        val onDoubleClick = blockProvideEvent(blockProps, magic.orEmpty(), "onDoubleClick")

        val modifier = Modifier
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
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = findArrangementVertical(properties["verticalArrangement"]?.value),
                    horizontalAlignment = findAlignmentHorizontal(properties["horizontalAlignment"]?.value)
                ) {
                    itemsIndexed(items = list) { itemIndex, _ ->
                        blockProps.onChangeBlocks?.invoke(
                            blockProps.block?.subBlocks.orEmpty(), itemIndex
                        )
                    }
                }
            }

            "X" -> { // HORIZONTAL
                LazyRow(
                    modifier = modifier,
                    verticalAlignment = findAlignmentVertical(properties["verticalAlignment"]?.value),
                    horizontalArrangement = findArrangementHorizontal(properties["horizontalArrangement"]?.value)
                ) {
                    itemsIndexed(items = list) { itemIndex, _ ->
                        blockProps.onChangeBlocks?.invoke(
                            blockProps.block?.subBlocks.orEmpty(), itemIndex
                        )
                    }
                }

            }
        }
    }
}