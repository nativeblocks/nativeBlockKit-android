package io.nativeblocks.blockit

import io.nativeblocks.blockit.button.NativeButtonBlock
import io.nativeblocks.blockit.container.NativeContainerBlock
import io.nativeblocks.blockit.icon.NativeIconBlock
import io.nativeblocks.blockit.image.NativeImageBlock
import io.nativeblocks.blockit.list.NativeListBlock
import io.nativeblocks.blockit.text.NativeTextBlock
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