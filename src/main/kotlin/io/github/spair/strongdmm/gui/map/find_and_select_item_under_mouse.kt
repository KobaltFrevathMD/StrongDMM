package io.github.spair.strongdmm.gui.map

import io.github.spair.strongdmm.gui.objtree.ObjectTreeView
import io.github.spair.strongdmm.logic.dmi.DmiProvider
import io.github.spair.strongdmm.logic.map.TileItem
import io.github.spair.strongdmm.logic.map.TileItemProvider
import io.github.spair.strongdmm.logic.render.RenderInstance
import io.github.spair.strongdmm.logic.render.RenderInstances

fun MapPipeline.findAndSelectItemUnderMouse(renderInstances: RenderInstances) {
    val instances = mutableListOf<RenderInstance>()

    renderInstances.values.forEach { plane ->
        plane.values.forEach { layer ->
            layer.forEach { ri ->
                if (xMouse in ri.locX..(ri.locX + ri.width) && yMouse in ri.locY..(ri.locY + ri.width)) {
                    instances.add(ri)
                }
            }
        }
    }

    var selectedItem: TileItem? = null

    instances.forEach { ri ->
        val pixelX = (xMouse - ri.locX).toInt()
        val pixelY = (ri.width - (yMouse - ri.locY)).toInt()

        val item = TileItemProvider.getByID(ri.tileItemID)
        val isOpaque = DmiProvider.getSpriteFromDmi(item.icon, item.iconState, item.dir)?.isOpaquePixel(pixelX, pixelY)
            ?: true   // When there is no sprite for item we are using placeholder which is always opaque

        if (isOpaque) {
            selectedItem = item
        }
    }

    selectedItem?.let {
        ObjectTreeView.findAndSelectItemInstance(it)
    }
}
