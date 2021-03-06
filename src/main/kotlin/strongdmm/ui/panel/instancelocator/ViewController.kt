package strongdmm.ui.panel.instancelocator

import strongdmm.byond.dmm.MapArea
import strongdmm.byond.dmm.MapPos
import strongdmm.byond.dmm.TileItem
import strongdmm.event.EventBus
import strongdmm.event.service.TriggerInstanceService
import strongdmm.event.service.TriggerToolsService
import strongdmm.event.ui.TriggerSearchResultPanelUi
import strongdmm.ui.panel.searchresult.model.SearchResult

class ViewController(
    private val state: State
) {
    fun doSearch() {
        val type = state.searchType.get().trim()

        if (type.isEmpty()) {
            return
        }

        val tileItemId = type.toLongOrNull()
        val searchRect = MapArea(state.searchX1.get(), state.searchY1.get(), state.searchX2.get(), state.searchY2.get())

        val openSearchResult = { it: List<Pair<TileItem, MapPos>> ->
            if (it.isNotEmpty()) {
                EventBus.post(TriggerSearchResultPanelUi.Open(SearchResult(type, tileItemId != null, it)))
            }
        }

        if (tileItemId != null) {
            EventBus.post(TriggerInstanceService.FindInstancePositionsById(Pair(searchRect, tileItemId), openSearchResult))
        } else {
            EventBus.post(TriggerInstanceService.FindInstancePositionsByType(Pair(searchRect, type), openSearchResult))
        }
    }

    fun doSelection() {
        EventBus.post(TriggerToolsService.FetchSelectedArea { selectedArea ->
            state.searchX1.set(selectedArea.x1)
            state.searchY1.set(selectedArea.y1)
            state.searchX2.set(selectedArea.x2)
            state.searchY2.set(selectedArea.y2)
        })
    }

    fun doReset() {
        state.searchX1.set(1)
        state.searchY1.set(1)
        state.searchX2.set(state.mapMaxX)
        state.searchY2.set(state.mapMaxY)
    }
}
