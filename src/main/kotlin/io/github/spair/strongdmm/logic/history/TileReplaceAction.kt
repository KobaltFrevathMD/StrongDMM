package io.github.spair.strongdmm.logic.history

import io.github.spair.strongdmm.gui.mapcanvas.Frame
import io.github.spair.strongdmm.logic.map.Dmm
import io.github.spair.strongdmm.logic.map.Tile

class TileReplaceAction(private val map: Dmm, tile: Tile) : Undoable {

    private val x = tile.x
    private val y = tile.y
    private val tileObjects = tile.tileItems.toList()

    override fun doAction(): Undoable {
        val tile = map.getTile(x, y)!!
        val reverseAction = TileReplaceAction(map, tile)
        tile.tileItems.clear()
        tile.tileItems.addAll(tileObjects)
        Frame.update(true)
        return reverseAction
    }
}