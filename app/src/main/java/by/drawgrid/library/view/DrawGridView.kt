package by.drawgrid.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 *Created on 28.04.2018.
 */
class DrawGridView : View {

    constructor(context: Context?) : super(context) {
        initView(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    fun initView(context: Context?, attrs: AttributeSet?) {

    }
}