package strongdmm.ui.menubar

import imgui.type.ImBoolean
import strongdmm.byond.TYPE_AREA
import strongdmm.byond.TYPE_MOB
import strongdmm.byond.TYPE_OBJ
import strongdmm.byond.TYPE_TURF
import strongdmm.byond.dmm.MapPath
import strongdmm.event.Event
import strongdmm.event.EventBus
import strongdmm.event.service.*
import strongdmm.event.ui.ProviderInstanceLocatorPanelUi
import strongdmm.service.action.ActionStatus

class EventController(
    private val state: State
) {
    init {
        EventBus.sign(ReactionEnvironmentService.EnvironmentLoadStarted::class.java, ::handleEnvironmentLoadStarted)
        EventBus.sign(ReactionEnvironmentService.EnvironmentLoadStopped::class.java, ::handleEnvironmentLoadStopped)
        EventBus.sign(ReactionEnvironmentService.EnvironmentChanged::class.java, ::handleEnvironmentChanged)
        EventBus.sign(ReactionEnvironmentService.EnvironmentReset::class.java, ::handleEnvironmentReset)
        EventBus.sign(ReactionMapHolderService.SelectedMapChanged::class.java, ::handleSelectedMapChanged)
        EventBus.sign(ReactionMapHolderService.SelectedMapClosed::class.java, ::handleSelectedMapClosed)
        EventBus.sign(ReactionActionService.ActionStatusChanged::class.java, ::handleActionStatusChanged)
        EventBus.sign(ReactionLayersFilterService.LayersFilterRefreshed::class.java, ::handleLayersFilterRefreshed)

        EventBus.sign(ProviderInstanceLocatorPanelUi.DoInstanceLocatorOpen::class.java, ::handleProviderDoInstanceLocatorOpen)
        EventBus.sign(ProviderCanvasService.DoFrameAreas::class.java, ::handleProviderDoFrameAreas)
        EventBus.sign(ProviderCanvasService.DoSynchronizeMapsView::class.java, ::handleProviderDoSynchronizeMapsView)
        EventBus.sign(ProviderRecentFilesService.RecentEnvironments::class.java, ::handleProviderRecentEnvironments)
        EventBus.sign(ProviderRecentFilesService.RecentMaps::class.java, ::handleProviderRecentMaps)
    }

    private fun handleEnvironmentLoadStarted() {
        state.isLoadingEnvironment = true
    }

    private fun handleEnvironmentLoadStopped() {
        state.isLoadingEnvironment = false
    }

    private fun handleEnvironmentChanged() {
        state.isEnvironmentOpened = true
    }

    private fun handleEnvironmentReset() {
        state.isEnvironmentOpened = false
    }

    private fun handleSelectedMapChanged() {
        state.isMapOpened = true
    }

    private fun handleSelectedMapClosed() {
        state.isMapOpened = false
    }

    private fun handleActionStatusChanged(event: Event<ActionStatus, Unit>) {
        state.isUndoEnabled = event.body.hasUndoAction
        state.isRedoEnabled = event.body.hasRedoAction
    }

    private fun handleLayersFilterRefreshed(event: Event<Set<String>, Unit>) {
        state.isAreaLayerActive.set(!event.body.contains(TYPE_AREA))
        state.isTurfLayerActive.set(!event.body.contains(TYPE_TURF))
        state.isObjLayerActive.set(!event.body.contains(TYPE_OBJ))
        state.isMobLayerActive.set(!event.body.contains(TYPE_MOB))
    }

    private fun handleProviderDoInstanceLocatorOpen(event: Event<ImBoolean, Unit>) {
        state.providedDoInstanceLocatorOpen = event.body
    }

    private fun handleProviderDoFrameAreas(event: Event<ImBoolean, Unit>) {
        state.providedDoFrameAreas = event.body
    }

    private fun handleProviderDoSynchronizeMapsView(event: Event<ImBoolean, Unit>) {
        state.providedDoSynchronizeMapsView = event.body
    }

    private fun handleProviderRecentEnvironments(event: Event<List<String>, Unit>) {
        state.providedRecentEnvironments = event.body
    }

    private fun handleProviderRecentMaps(event: Event<List<MapPath>, Unit>) {
        state.providedRecentMaps = event.body
    }
}
