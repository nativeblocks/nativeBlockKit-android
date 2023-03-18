package io.nativeblocks.blockit.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.uikit.theme.NativeTheme

class NativeContainerBlock : INativeBlock {

    @Composable
    override fun BlockView(blockProps: BlockProps) {
        val visibility = blockProps.variables?.find { it.key == blockProps.block?.visibility }
        if ((visibility?.value ?: "true") == "false") {
            return
        }

        val width = blockProps.block?.properties?.find { it.key == "width" }?.value
        val height = blockProps.block?.properties?.find { it.key == "height" }?.value

        val paddingStart = blockProps.block?.properties?.find { it.key == "paddingStart" }?.value
        val paddingTop = blockProps.block?.properties?.find { it.key == "paddingTop" }?.value
        val paddingEnd = blockProps.block?.properties?.find { it.key == "paddingEnd" }?.value
        val paddingBottom = blockProps.block?.properties?.find { it.key == "paddingBottom" }?.value

        var modifier = NativeTheme
            .sizeMapper(width, height)
            .clip(
                shape = NativeTheme.shapeMapper(
                    blockProps.block?.properties?.find { it.key == "shape" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusTopStart" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusTopEnd" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusBottomStart" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusBottomEnd" }?.value
                )
            )
            .background(
                NativeTheme.rgbColorBuilder(
                    blockProps.block?.properties?.find { it.key == "backgroundColor" }?.value,
                    blockProps.block?.properties?.find { it.key == "backgroundColorOpacity" }?.value
                )
            )
            .padding(
                NativeTheme.spacingMapper(
                    listOf(
                        paddingStart ?: "0",
                        paddingTop ?: "0",
                        paddingEnd ?: "0",
                        paddingBottom ?: "0",
                    )
                )
            )

        val scrollable = blockProps.block?.properties?.find { it.key == "scrollable" }?.value

        when (blockProps.block?.properties?.find { it.key == "direction" }?.value) {
            "Y" -> { // VERTICAL
                if (scrollable.toBoolean()) {
                    modifier = modifier.then(Modifier.verticalScroll(rememberScrollState()))
                }
                Column(
                    modifier = modifier,
                    verticalArrangement = NativeTheme.findArrangementVertical(blockProps.block?.properties?.find { it.key == "verticalArrangement" }?.value),
                    horizontalAlignment = NativeTheme.findAlignmentHorizontal(blockProps.block?.properties?.find { it.key == "horizontalAlignment" }?.value)
                ) {
                    blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
                }
            }
            "X" -> { // HORIZONTAL
                if (scrollable.toBoolean()) {
                    modifier = modifier.then(Modifier.horizontalScroll(rememberScrollState()))
                }
                Row(
                    modifier = modifier,
                    verticalAlignment = NativeTheme.findAlignmentVertical(blockProps.block?.properties?.find { it.key == "verticalAlignment" }?.value),
                    horizontalArrangement = NativeTheme.findArrangementHorizontal(blockProps.block?.properties?.find { it.key == "horizontalArrangement" }?.value)
                ) {
                    blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
                }
            }
            "Z" -> { // FLAT
                if (scrollable.toBoolean()) {
                    modifier = modifier.then(Modifier.verticalScroll(rememberScrollState()))
                }
                Box(
                    modifier = modifier,
                    contentAlignment = NativeTheme.findAlignment(blockProps.block?.properties?.find { it.key == "contentAlignment" }?.value)
                ) {
                    blockProps.onChangeBlocks?.invoke(blockProps.block?.subBlocks.orEmpty(), -1)
                }
            }
        }
    }
}