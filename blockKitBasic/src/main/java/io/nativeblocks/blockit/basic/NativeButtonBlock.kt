package io.nativeblocks.blockit.basic

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.util.getVariableValue
import io.nativeblocks.nativejson.NativeJsonPath
import io.nativeblocks.uikit.block.NativeButton
import io.nativeblocks.uikit.block.NativeButtonProperties
import io.nativeblocks.uikit.theme.NativeTheme

class NativeButtonBlock : INativeBlock {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun BlockView(blockProps: BlockProps) {
        val visibility = blockProps.variables?.find { it.key == blockProps.block?.visibility }
        if ((visibility?.value ?: "true") == "false") {
            return
        }

        val variable = blockProps.variables?.find { it.key == blockProps.block?.key }
        val magic = blockProps.magics?.find { it.key == blockProps.block?.key }

        val value = if (blockProps.block?.jsonPath.isNullOrEmpty()) {
            variable?.value.orEmpty()
        } else {
            val query = blockProps.block?.jsonPath?.getVariableValue("index", "${blockProps.index}")
            NativeJsonPath().query(variable?.value.orEmpty(), query).toString()
        }

        val width = blockProps.block?.properties?.find { it.key == "width" }?.value
        val height = blockProps.block?.properties?.find { it.key == "height" }?.value

        val paddingStart = blockProps.block?.properties?.find { it.key == "paddingStart" }?.value
        val paddingTop = blockProps.block?.properties?.find { it.key == "paddingTop" }?.value
        val paddingEnd = blockProps.block?.properties?.find { it.key == "paddingEnd" }?.value
        val paddingBottom = blockProps.block?.properties?.find { it.key == "paddingBottom" }?.value

        val modifier = NativeTheme
            .sizeMapper(width, height)
            .combinedClickable(interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(true),
                onClick = {
                    blockProps.onHandleMagic?.invoke(
                        blockProps.index ?: -1, magic, "ON_CLICK"
                    )
                },
                onLongClick = {
                    blockProps.onHandleMagic?.invoke(
                        blockProps.index ?: -1, magic, "ON_LONG_CLICK"
                    )
                })

        NativeButton(
            modifier = modifier,
            contentPadding = NativeTheme.spacingMapper(
                listOf(
                    paddingStart.orEmpty(),
                    paddingTop.orEmpty(),
                    paddingEnd.orEmpty(),
                    paddingBottom.orEmpty(),
                )
            ),
            buttonText = value,
            properties = NativeButtonProperties(
                foregroundColor = NativeTheme.rgbColorBuilder(
                    blockProps.block?.properties?.find { it.key == "foregroundColor" }?.value,
                    blockProps.block?.properties?.find { it.key == "foregroundColorOpacity" }?.value
                ),
                backgroundColor = NativeTheme.rgbColorBuilder(
                    blockProps.block?.properties?.find { it.key == "backgroundColor" }?.value,
                    blockProps.block?.properties?.find { it.key == "backgroundColorOpacity" }?.value
                ),
                boarderColor = NativeTheme.rgbColorBuilder(
                    blockProps.block?.properties?.find { it.key == "borderColor" }?.value,
                    blockProps.block?.properties?.find { it.key == "borderColorOpacity" }?.value
                ),
                textStyle = NativeTheme.typographyBuilder(
                    NativeTheme.fontFamilyMapper(blockProps.block?.properties?.find { it.key == "textFontFamily" }?.value),
                    NativeTheme.fontWeightMapper(blockProps.block?.properties?.find { it.key == "textFontWeight" }?.value),
                    blockProps.block?.properties?.find { it.key == "textFontSize" }?.value?.toIntOrNull()?.sp
                        ?: 14.sp
                ),
                shape = NativeTheme.shapeMapper(
                    blockProps.block?.properties?.find { it.key == "shape" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusTopStart" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusTopEnd" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusBottomStart" }?.value,
                    blockProps.block?.properties?.find { it.key == "shapeRadiusBottomEnd" }?.value
                ),
                shadow = blockProps.block?.properties?.find { it.key == "shadow" }?.value?.toIntOrNull()?.dp
                    ?: 2.dp,
                leadingIcon = NativeSideIconBlock(
                    iconUrl = blockProps.block?.properties?.find { it.key == "leadingIcon" }?.value,
                    foregroundColor = NativeTheme.rgbColorBuilder(
                        blockProps.block?.properties?.find { it.key == "foregroundColor" }?.value,
                        blockProps.block?.properties?.find { it.key == "foregroundColorOpacity" }?.value
                    ),
                    onClick = {
                        blockProps.onHandleMagic?.invoke(
                            blockProps.index ?: -1, magic, "ON_CLICK"
                        )
                    },
                    onLongClick = {
                        blockProps.onHandleMagic?.invoke(
                            blockProps.index ?: -1, magic, "ON_LONG_CLICK"
                        )
                    }
                ),
                trailingIcon = NativeSideIconBlock(
                    iconUrl = blockProps.block?.properties?.find { it.key == "trailingIcon" }?.value,
                    foregroundColor = NativeTheme.rgbColorBuilder(
                        blockProps.block?.properties?.find { it.key == "foregroundColor" }?.value,
                        blockProps.block?.properties?.find { it.key == "foregroundColorOpacity" }?.value
                    ),
                    onClick = {
                        blockProps.onHandleMagic?.invoke(
                            blockProps.index ?: -1, magic, "ON_CLICK"
                        )
                    },
                    onLongClick = {
                        blockProps.onHandleMagic?.invoke(
                            blockProps.index ?: -1, magic, "ON_LONG_CLICK"
                        )
                    }
                ),
                enable = blockProps.block?.properties?.find { it.key == "enable" }?.value.toBoolean()
            )
        ) {
            blockProps.onHandleMagic?.invoke(blockProps.index ?: -1, magic, "ON_CLICK")
        }
    }
}