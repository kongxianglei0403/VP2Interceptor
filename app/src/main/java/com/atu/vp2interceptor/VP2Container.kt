package com.atu.vp2interceptor

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 *Create by atu on 2021/1/29
 */
class VP2Container @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyle: Int = 0): RelativeLayout(context,attributeSet,defaultStyle) {

    private var vp2: ViewPager2? = null
    private var disallowParentInterceptorDownEvent = true
    private var startX = 0
    private var startY = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (index in 0 until childCount){
            val view = getChildAt(index)
            if (view is ViewPager2)
                vp2 = view
        }
        if (vp2 == null)
            throw IllegalStateException("VP2Container must has a child is viewpager2")
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val notNeedInterceptor = !vp2!!.isUserInputEnabled
                || (vp2!!.adapter != null && vp2?.adapter!!.itemCount <= 1)
        if (notNeedInterceptor)
            return super.onInterceptTouchEvent(ev)
        when(ev.action){
            MotionEvent.ACTION_DOWN ->{//按下的时候 请求父布局不要拦截事件
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(!disallowParentInterceptorDownEvent)
            }
            MotionEvent.ACTION_MOVE ->{
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY = abs(endY - startY)
                if (vp2!!.orientation == ViewPager2.ORIENTATION_HORIZONTAL){//水平滑动
                    dealHActionMove(endX,disX,disY)
                }
                else if (vp2!!.orientation == ViewPager2.ORIENTATION_VERTICAL){
                    dealVActionMove(endY,disX,disY)
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{}
        }

        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 处理水平冲突的问题
     */
    private fun dealHActionMove(endX: Int, disX: Int, disY: Int) {
        if (vp2?.adapter == null) return
        if (disX > disY){
            val currentItem = vp2?.currentItem
            val itemCount = vp2?.adapter!!.itemCount
            if (currentItem == 0 && endX - startX > 0){
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else{
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount -1
                        || endX - startX >= 0)
            }
        }
        else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    /**
     * 处理垂直滑动冲突
     */
    private fun dealVActionMove(endY: Int, disX: Int, disY: Int) {
        if (vp2?.adapter == null) return
        if (disY > disX){
            val currentItem = vp2?.currentItem
            val itemCount = vp2?.adapter!!.itemCount
            if (currentItem == 0 && endY - startY > 0){
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else {
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount - 1
                        || endY - startY >= 0)
            }
        }
        else if (disX > disY){
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

}