package strongdmm.service

import strongdmm.application.Service
import strongdmm.byond.dmm.MapPos
import strongdmm.byond.dmm.TileItem
import strongdmm.event.Event
import strongdmm.event.EventBus
import strongdmm.event.service.*
import strongdmm.util.OUT_OF_BOUNDS

class ClipboardService : Service {
    private var tileItems: Array<Array<List<TileItem>>>? = null
    private var currentMapPos: MapPos = MapPos(OUT_OF_BOUNDS, OUT_OF_BOUNDS)

    init {
        EventBus.sign(ReactionEnvironmentService.EnvironmentReset::class.java, ::handleEnvironmentReset)
        EventBus.sign(ReactionCanvasService.MapMousePosChanged::class.java, ::handleMapMousePosChanged)
        EventBus.sign(TriggerClipboardService.Cut::class.java, ::handleCut)
        EventBus.sign(TriggerClipboardService.Copy::class.java, ::handleCopy)
        EventBus.sign(TriggerClipboardService.Paste::class.java, ::handlePaste)
    }

    private fun handleEnvironmentReset() {
        tileItems = null
    }

    private fun handleMapMousePosChanged(event: Event<MapPos, Unit>) {
        currentMapPos = event.body
    }

    private fun handleCut() {
        EventBus.post(TriggerClipboardService.Copy())
        EventBus.post(TriggerMapModifierService.DeleteTileItemsInSelectedArea())
    }

    private fun handleCopy() {
        EventBus.post(TriggerMapHolderService.FetchSelectedMap { selectedMap ->
            EventBus.post(TriggerLayersFilterService.FetchFilteredLayers { filteredLayers ->
                EventBus.post(TriggerToolsService.FetchSelectedArea { selectedArea ->
                    val width = selectedArea.x2 - selectedArea.x1 + 1
                    val height = selectedArea.y2 - selectedArea.y1 + 1
                    val tileItems = Array(width) { Array(height) { emptyList<TileItem>() } }

                    for ((xLocal, x) in (selectedArea.x1..selectedArea.x2).withIndex()) {
                        for ((yLocal, y) in (selectedArea.y1..selectedArea.y2).withIndex()) {
                            val tile = selectedMap.getTile(x, y, selectedMap.zSelected)
                            tileItems[xLocal][yLocal] = tile.getFilteredTileItems(filteredLayers)
                        }
                    }

                    this.tileItems = tileItems
                })
            })
        })
    }

    private fun handlePaste() {
        if (!currentMapPos.isOutOfBounds() && tileItems != null) {
            EventBus.post(TriggerMapModifierService.FillSelectedMapPositionWithTileItems(tileItems!!))
        }
    }
}
