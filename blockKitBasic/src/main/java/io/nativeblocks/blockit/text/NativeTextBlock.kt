package io.nativeblocks.blockit.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.util.blockProvideEvent
import io.nativeblocks.core.util.clickableEvents
import io.nativeblocks.core.util.fontFamilyMapper
import io.nativeblocks.core.util.fontWeightMapper
import io.nativeblocks.core.util.getVariableValue
import io.nativeblocks.core.util.spacingMapper
import io.nativeblocks.core.util.textAlignmentMapper
import io.nativeblocks.core.util.textDecorationMapper
import io.nativeblocks.core.util.textOverflowMapper
import io.nativeblocks.core.util.toArgb
import io.nativeblocks.core.util.typographyBuilder
import io.nativeblocks.core.util.widthAndHeight
import io.nativeblocks.nativejson.NativeJsonPath

class NativeTextBlock : INativeBlock {

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

        val onClick = blockProvideEvent(blockProps, magic.orEmpty(), "onClick")
        val onLongClick = blockProvideEvent(blockProps, magic.orEmpty(), "onLongClick")
        val onDoubleClick = blockProvideEvent(blockProps, magic.orEmpty(), "onDoubleClick")

        val modifier = Modifier
            .widthAndHeight(width, height)
            .clickableEvents(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(true),
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick
            )
            .padding(spacingMapper(listOf(paddingStart, paddingTop, paddingEnd, paddingBottom)))

        val typography = typographyBuilder(
            fontFamily = fontFamilyMapper(properties["fontFamily"]?.value),
            fontWeight = fontWeightMapper(properties["fontWeight"]?.value),
            fontSize = properties["fontSize"]?.value?.toIntOrNull()?.sp ?: 14.sp
        )

        Text(
            text = value,
            modifier = modifier,
            color = toArgb(
                properties["foregroundColor"]?.value,
                properties["foregroundColorOpacity"]?.value
            ),
            fontSize = typography.fontSize,
            fontStyle = typography.fontStyle,
            fontWeight = typography.fontWeight,
            fontFamily = typography.fontFamily,
            letterSpacing = properties["letterSpacing"]?.value?.toIntOrNull()?.sp
                ?: TextUnit.Unspecified,
            textDecoration = textDecorationMapper(properties["textDecoration"]?.value),
            textAlign = textAlignmentMapper(properties["textAlign"]?.value),
            lineHeight = properties["lineHeight"]?.value?.toIntOrNull()?.sp
                ?: TextUnit.Unspecified,
            overflow = textOverflowMapper(properties["lineHeight"]?.value),
            softWrap = true,
            maxLines = properties["maxLines"]?.value?.toIntOrNull() ?: Int.MAX_VALUE,
            minLines = properties["minLines"]?.value?.toIntOrNull() ?: 1,
        )
    }
}
