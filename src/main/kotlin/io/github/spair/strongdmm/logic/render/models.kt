package io.github.spair.strongdmm.logic.render

class RenderInstance(
    val locX: Float,
    val locY: Float,
    val textureId: Int,
    val u1: Float = 0f,
    val v1: Float = 0f,
    val u2: Float = 1f,
    val v2: Float = 1f,
    val width: Int = 32,
    val height: Int = 32,
    val color: Color = DefaultColor,
    val tileItemID: Int
)

open class Color(val red: Float = 1f, val green: Float = 1f, val blue: Float = 1f, val alpha: Float = 1f)
object DefaultColor : Color()
