package io.nativeblocks.blockKit

import io.nativeblocks.blockKit.button.NativeButtonBlock
import io.nativeblocks.blockKit.container.NativeContainerBlock
import io.nativeblocks.blockKit.icon.NativeIconBlock
import io.nativeblocks.blockKit.image.NativeImageBlock
import io.nativeblocks.blockKit.list.NativeListBlock
import io.nativeblocks.blockKit.text.NativeTextBlock
import io.nativeblocks.core.api.NativeblocksManager

object NativeblocksBlockHelper {

    fun provideBlocks() {
        NativeblocksManager.getInstance()
            .provideBlock(
                blockType = "NATIVE_CONTAINER",
                block = NativeContainerBlock()
            )
            .provideBlock(
                blockType = "NATIVE_TEXT",
                block = NativeTextBlock()
            )
            .provideBlock(
                blockType = "NATIVE_BUTTON",
                block = NativeButtonBlock()
            )
            .provideBlock(
                blockType = "NATIVE_IMAGE",
                block = NativeImageBlock()
            )
            .provideBlock(
                blockType = "NATIVE_ICON",
                block = NativeIconBlock()
            )
            .provideBlock(
                blockType = "NATIVE_LIST",
                block = NativeListBlock()
            )
    }

}