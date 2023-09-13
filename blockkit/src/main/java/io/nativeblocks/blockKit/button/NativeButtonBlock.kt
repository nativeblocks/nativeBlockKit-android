package io.nativeblocks.blockKit.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.nativeblocks.blockKit.icon.NativeIcon
import io.nativeblocks.core.api.provider.block.BlockProps
import io.nativeblocks.core.api.provider.block.INativeBlock
import io.nativeblocks.core.frame.domain.model.NativeBlockDynamicPropertiesModel
import io.nativeblocks.core.frame.domain.model.NativeBlockModel
import io.nativeblocks.core.frame.domain.model.NativeVariableModel
import io.nativeblocks.core.util.blockProvideEvent
import io.nativeblocks.core.util.clickableEvents
import io.nativeblocks.core.util.fontFamilyMapper
import io.nativeblocks.core.util.fontWeightMapper
import io.nativeblocks.core.util.getVariableValue
import io.nativeblocks.core.util.shapeMapper
import io.nativeblocks.core.util.spacingMapper
import io.nativeblocks.core.util.toArgb
import io.nativeblocks.core.util.typographyBuilder
import io.nativeblocks.core.util.widthAndHeight
import io.nativeblocks.nativejson.NativeJsonPath

class NativeButtonBlock : INativeBlock {

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

        val contentPaddingStart = properties["contentPaddingStart"]?.value
        val contentPaddingTop = properties["contentPaddingTop"]?.value
        val contentPaddingEnd = properties["contentPaddingEnd"]?.value
        val contentPaddingBottom = properties["contentPaddingBottom"]?.value

        val shadow = properties["shadow"]?.value?.toIntOrNull()?.dp ?: 1.dp
        val enable = properties["enable"]?.value?.toBooleanStrictOrNull() ?: true

        val shape = shapeMapper(
            properties["shape"]?.value,
            properties["shapeRadiusTopStart"]?.value,
            properties["shapeRadiusTopEnd"]?.value,
            properties["shapeRadiusBottomStart"]?.value,
            properties["shapeRadiusBottomEnd"]?.value
        )

        val leadingIcon = properties["leadingIcon"]?.value
        val trailingIcon = properties["trailingIcon"]?.value

        val typography = typographyBuilder(
            fontFamily = fontFamilyMapper(properties["fontFamily"]?.value),
            fontWeight = fontWeightMapper(properties["fontWeight"]?.value),
            fontSize = properties["fontSize"]?.value?.toIntOrNull()?.sp ?: 14.sp
        )

        val backgroundColorValue = properties["backgroundColor"]?.value
        val backgroundColorOpacityValue = properties["backgroundColorOpacity"]?.value
        val backgroundDisableColorValue = properties["backgroundDisableColor"]?.value
        val backgroundDisableColorOpacityValue = properties["backgroundDisableColorOpacity"]?.value

        val foregroundColorValue = properties["foregroundColor"]?.value
        val foregroundColorOpacityValue = properties["foregroundColorOpacity"]?.value
        val foregroundDisableColorValue = properties["foregroundDisableColor"]?.value
        val foregroundDisableColorOpacityValue = properties["foregroundDisableColorOpacity"]?.value

        val borderColorValue = properties["borderColor"]?.value
        val borderColorOpacityValue = properties["borderColorOpacity"]?.value
        val borderDisableColorValue = properties["borderDisableColor"]?.value
        val borderDisableColorOpacityValue = properties["borderDisableColorOpacity"]?.value

        val tintColor = if (enable) {
            toArgb(foregroundColorValue, foregroundColorOpacityValue)
        } else {
            toArgb(foregroundDisableColorValue, foregroundDisableColorOpacityValue)
        }

        val borderColor = if (enable) {
            toArgb(borderColorValue, borderColorOpacityValue)
        } else {
            toArgb(borderDisableColorValue, borderDisableColorOpacityValue)
        }

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

        Button(
            modifier = modifier,
            onClick = { onClick?.invoke() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = toArgb(
                    backgroundColorValue, backgroundColorOpacityValue
                ),
                contentColor = toArgb(
                    foregroundColorValue, foregroundColorOpacityValue
                ),
                disabledBackgroundColor = toArgb(
                    backgroundDisableColorValue, backgroundDisableColorOpacityValue
                ),
                disabledContentColor = toArgb(
                    foregroundDisableColorValue, foregroundDisableColorOpacityValue
                ),
            ),

            elevation = ButtonDefaults.elevation(
                defaultElevation = shadow,
                pressedElevation = shadow,
                disabledElevation = 0.dp,
                hoveredElevation = shadow,
                focusedElevation = shadow,
            ),
            shape = shape,
            border = BorderStroke(1.dp, borderColor),
            enabled = enable
        ) {
            Row(
                modifier = Modifier.padding(
                    spacingMapper(
                        listOf(
                            contentPaddingStart,
                            contentPaddingTop,
                            contentPaddingEnd,
                            contentPaddingBottom
                        )
                    )
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let { leadingIcon ->
                    NativeIcon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        imageUrl = leadingIcon,
                        tintColor = tintColor
                    )
                    if (value.isNotEmpty()) {
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    }
                }
                if (value.isNotEmpty()) {
                    Text(
                        text = value,
                        modifier = Modifier,
                        color = toArgb(foregroundColorValue, foregroundColorOpacityValue),
                        fontSize = typography.fontSize,
                        fontStyle = typography.fontStyle,
                        fontWeight = typography.fontWeight,
                        fontFamily = typography.fontFamily,
                        textAlign = TextAlign.Center,
                        letterSpacing = properties["letterSpacing"]?.value?.toIntOrNull()?.sp
                            ?: TextUnit.Unspecified,
                    )
                }
                trailingIcon?.let { trailingIcon ->
                    if (value.isNotEmpty()) {
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    }
                    NativeIcon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        imageUrl = trailingIcon,
                        tintColor = tintColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NativeButtonBlockPreview() {
    NativeButtonBlock().BlockView(
        blockProps = BlockProps(
            variables = mapOf("text" to NativeVariableModel("text", "text", "STRING")),
            block = NativeBlockModel(
                key = "text",
                properties = mapOf(
                    "width" to NativeBlockDynamicPropertiesModel(
                        key = "width",
                        value = "wrap",
                        type = "STRING"
                    ),
                    "height" to NativeBlockDynamicPropertiesModel(
                        key = "height",
                        value = "wrap",
                        type = "STRING"
                    ),
                    "paddingStart" to NativeBlockDynamicPropertiesModel(
                        key = "paddingStart",
                        value = "0",
                        type = "STRING"
                    ),
                    "paddingTop" to NativeBlockDynamicPropertiesModel(
                        key = "paddingTop",
                        value = "0",
                        type = "STRING"
                    ),
                    "paddingEnd" to NativeBlockDynamicPropertiesModel(
                        key = "paddingEnd",
                        value = "0",
                        type = "STRING"
                    ),
                    "paddingBottom" to NativeBlockDynamicPropertiesModel(
                        key = "paddingBottom",
                        value = "0",
                        type = "STRING"
                    ),
                    "contentPaddingStart" to NativeBlockDynamicPropertiesModel(
                        key = "contentPaddingStart",
                        value = "8",
                        type = "STRING"
                    ),
                    "contentPaddingTop" to NativeBlockDynamicPropertiesModel(
                        key = "contentPaddingTop",
                        value = "8",
                        type = "STRING"
                    ),
                    "contentPaddingEnd" to NativeBlockDynamicPropertiesModel(
                        key = "contentPaddingEnd",
                        value = "8",
                        type = "STRING"
                    ),
                    "contentPaddingBottom" to NativeBlockDynamicPropertiesModel(
                        key = "contentPaddingBottom",
                        value = "8",
                        type = "STRING"
                    ),
                    "shadow" to NativeBlockDynamicPropertiesModel(
                        key = "shadow",
                        value = "2",
                        type = "STRING"
                    ),
                    "enable" to NativeBlockDynamicPropertiesModel(
                        key = "enable",
                        value = "true",
                        type = "STRING"
                    ),
                    "shape" to NativeBlockDynamicPropertiesModel(
                        key = "shape",
                        value = "rectangle",
                        type = "STRING"
                    ),
                    "shapeRadiusTopStart" to NativeBlockDynamicPropertiesModel(
                        key = "shapeRadiusTopStart",
                        value = "8",
                        type = "STRING"
                    ),
                    "shapeRadiusTopEnd" to NativeBlockDynamicPropertiesModel(
                        key = "shapeRadiusTopEnd",
                        value = "8",
                        type = "STRING"
                    ),
                    "shapeRadiusBottomStart" to NativeBlockDynamicPropertiesModel(
                        key = "shapeRadiusBottomStart",
                        value = "8",
                        type = "STRING"
                    ),
                    "shapeRadiusBottomEnd" to NativeBlockDynamicPropertiesModel(
                        key = "shapeRadiusBottomEnd",
                        value = "8",
                        type = "STRING"
                    ),
                    "leadingIcon" to NativeBlockDynamicPropertiesModel(
                        key = "leadingIcon",
                        value = "https://gitlab.com/uploads/-/system/project/avatar/10142792/1_P1Jrr84Hq9L1wzipxC1Rdg.png?width=96",
                        type = "STRING"
                    ),
                    "trailingIcon" to NativeBlockDynamicPropertiesModel(
                        key = "trailingIcon",
                        value = "https://gitlab.com/uploads/-/system/project/avatar/10142792/1_P1Jrr84Hq9L1wzipxC1Rdg.png?width=96",
                        type = "STRING"
                    ),
                    "fontFamily" to NativeBlockDynamicPropertiesModel(
                        key = "fontFamily",
                        value = "default",
                        type = "STRING"
                    ),
                    "fontWeight" to NativeBlockDynamicPropertiesModel(
                        key = "fontWeight",
                        value = "normal",
                        type = "STRING"
                    ),
                    "fontSize" to NativeBlockDynamicPropertiesModel(
                        key = "fontSize",
                        value = "22",
                        type = "STRING"
                    ),
                    "backgroundColor" to NativeBlockDynamicPropertiesModel(
                        key = "backgroundColor",
                        value = "#fafafa",
                        type = "STRING"
                    ),
                    "backgroundColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "backgroundColorOpacity",
                        value = "100",
                        type = "STRING"
                    ),
                    "backgroundDisableColor" to NativeBlockDynamicPropertiesModel(
                        key = "backgroundDisableColor",
                        value = "#fafafa",
                        type = "STRING"
                    ),
                    "backgroundDisableColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "backgroundDisableColorOpacity",
                        value = "50",
                        type = "STRING"
                    ),
                    "foregroundColor" to NativeBlockDynamicPropertiesModel(
                        key = "foregroundColor",
                        value = "#212121",
                        type = "STRING"
                    ),
                    "foregroundColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "foregroundColorOpacity",
                        value = "100",
                        type = "STRING"
                    ),
                    "foregroundDisableColor" to NativeBlockDynamicPropertiesModel(
                        key = "foregroundDisableColor",
                        value = "#212121",
                        type = "STRING"
                    ),
                    "foregroundDisableColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "foregroundDisableColorOpacity",
                        value = "50",
                        type = "STRING"
                    ),
                    "borderColor" to NativeBlockDynamicPropertiesModel(
                        key = "borderColor",
                        value = "#212121",
                        type = "STRING"
                    ),
                    "borderColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "borderColorOpacity",
                        value = "100",
                        type = "STRING"
                    ),
                    "borderDisableColor" to NativeBlockDynamicPropertiesModel(
                        key = "borderDisableColor",
                        value = "#212121",
                        type = "STRING"
                    ),
                    "borderDisableColorOpacity" to NativeBlockDynamicPropertiesModel(
                        key = "borderDisableColorOpacity",
                        value = "100",
                        type = "STRING"
                    ),
                    "letterSpacing" to NativeBlockDynamicPropertiesModel(
                        key = "letterSpacing",
                        value = "1.25",
                        type = "STRING"
                    ),
                )
            )
        )
    )
}