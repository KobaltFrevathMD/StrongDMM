package strongdmm.event.service

import strongdmm.event.Event

abstract class ReactionLayersFilterService {
    class LayersFilterRefreshed(dmeItemTypes: Set<String>) : Event<Set<String>, Unit>(dmeItemTypes, null)
}
