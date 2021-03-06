package strongdmm.ui.tilepopup

import strongdmm.byond.dmm.GlobalTileItemHolder
import strongdmm.byond.dmm.Tile
import strongdmm.byond.dmm.TileItem
import strongdmm.event.EventBus
import strongdmm.event.service.TriggerActionService
import strongdmm.event.service.TriggerFrameService
import strongdmm.event.ui.TriggerObjectPanelUi
import strongdmm.service.action.undoable.ReplaceTileAction
import strongdmm.service.preferences.prefs.enums.NudgeMode
import strongdmm.util.extension.getOrPut

class ViewControllerNudgeFlow(
    private val state: State
) {
    fun doNudge(isXAxis: Boolean, tile: Tile, tileItem: TileItem, tileItemIdx: Int, pixelNudge: IntArray) {
        GlobalTileItemHolder.tmpOperation {
            tile.nudge(isXAxis, tileItem, tileItemIdx, pixelNudge[0], state.providedPreferences.nudgeMode)
        }

        EventBus.post(TriggerFrameService.RefreshFrame())
    }

    fun getNudgeValueToShow(isXAxis: Boolean, tileItem: TileItem, tileItemIdx: Int): Pair<Int, IntArray> {
        return if (state.providedPreferences.nudgeMode == NudgeMode.PIXEL) {
            if (isXAxis) {
                state.pixelXNudgeArrays.getOrPut(tileItemIdx) { tileItem.pixelX to intArrayOf(tileItem.pixelX) }
            } else {
                state.pixelYNudgeArrays.getOrPut(tileItemIdx) { tileItem.pixelY to intArrayOf(tileItem.pixelY) }
            }
        } else {
            if (isXAxis) {
                state.pixelXNudgeArrays.getOrPut(tileItemIdx) { tileItem.stepX to intArrayOf(tileItem.stepX) }
            } else {
                state.pixelYNudgeArrays.getOrPut(tileItemIdx) { tileItem.stepY to intArrayOf(tileItem.stepY) }
            }
        }
    }

    fun applyNudgeChanges(isXAxis: Boolean, tile: Tile, tileItem: TileItem, tileItemIdx: Int, pixelNudge: IntArray, initialValue: Int) {
        GlobalTileItemHolder.tmpOperation {
            tile.nudge(isXAxis, tileItem, tileItemIdx, initialValue, state.providedPreferences.nudgeMode)
        }

        EventBus.post(
            TriggerActionService.QueueUndoable(
                ReplaceTileAction(tile) {
                    tile.nudge(isXAxis, tileItem, tileItemIdx, pixelNudge[0], state.providedPreferences.nudgeMode)
                }
            ))

        EventBus.post(TriggerFrameService.RefreshFrame())
        EventBus.post(TriggerObjectPanelUi.Update())

        // to properly create a reverse action
        if (isXAxis) {
            state.pixelXNudgeArrays.clear()
        } else {
            state.pixelYNudgeArrays.clear()
        }
    }
}
