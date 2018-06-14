package by.drawgrid.library.model.kotlin

import android.content.Context
import android.graphics.Canvas
import by.drawgrid.library.view.kotlin.GridCalculator


abstract class BaseElement {

    var id: Int = 0
    var type: Int = 0
    internal var active: Boolean = false
    internal var dpi = 1f

    var gridCalculator: GridCalculator? = null

    constructor(type: Int, context: Context?) {
        val dm = context!!.getResources().displayMetrics
        dpi = dm.density
        this.type = type
    }

    abstract fun draw(canvas: Canvas)

    abstract fun getColor(): Int

    abstract fun setColor(color: Int)

    abstract fun getWidthLine(): Float

    abstract fun setWidthLine(widthLine: Float)

    fun isActive(): Boolean {
        return active
    }

    fun setActive(active: Boolean) {
        this.active = active
    }
}