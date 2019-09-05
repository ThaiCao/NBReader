package com.example.newbiechen.nbreader.ui.component.widget.page.anim

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import com.example.newbiechen.nbreader.ui.component.widget.page.PageManager
import kotlin.math.abs

/**
 *  author : newbiechen
 *  date : 2019-09-05 16:21
 *  description :
 */

class SlidePageAnimation(view: View, pageManager: PageManager) : PageAnimation(view, pageManager) {
    // 图片在屏幕的可展示区域
    private var mFromSpaceRect = Rect(0, 0, mViewWidth, mViewHeight)
    // 选取图片的片段区域
    private var mFromBitmapRect = Rect(0, 0, mViewWidth, mViewHeight)

    private var mToSpaceRect = Rect(0, 0, mViewWidth, mViewHeight)

    private var mToBitmapRect = Rect(0, 0, mViewWidth, mViewHeight)

    override fun drawStatic(canvas: Canvas) {
        canvas.drawBitmap(getFromPage(), 0f, 0f, null)
    }

    override fun drawMove(canvas: Canvas) {
        var dis = 0
        when (mDirection) {
            Direction.PREVIOUS -> {
                dis = mTouchX - mStartX
                if (dis < 0) {
                    dis = 0
                    mStartX = mTouchX
                }

                mFromSpaceRect.right = mViewWidth - dis
                mFromBitmapRect.left = dis

                mToSpaceRect.left = mViewWidth - dis
                mToBitmapRect.right = dis

                canvas.drawBitmap(getFromPage(), mFromSpaceRect, mFromBitmapRect, null)
                canvas.drawBitmap(getToPage(), mToSpaceRect, mToBitmapRect, null)
            }
            Direction.NEXT -> {
                //左半边的剩余区域
                dis = mViewWidth - mStartX + mTouchX
                if (dis > mViewWidth) {
                    dis = mViewWidth
                }
                //计算bitmap截取的区域
                mFromSpaceRect.left = mViewWidth - dis
                //计算bitmap在canvas显示的区域
                mFromBitmapRect.right = dis
                //计算下一页截取的区域
                mToSpaceRect.right = mViewWidth - dis
                //计算下一页在canvas显示的区域
                mToBitmapRect.left = dis

                canvas.drawBitmap(getFromPage(), mFromSpaceRect, mFromBitmapRect, null)
                canvas.drawBitmap(getToPage(), mToSpaceRect, mToBitmapRect, null)
            }
        }
    }

    override fun startAnimInternal(isCancelAnim: Boolean) {
        super.startAnimInternal(isCancelAnim)
        var dx = 0
        when (mDirection) {
            Direction.PREVIOUS -> dx = if (mStatus == Status.AutoBackward) {
                -abs(mTouchX - mStartX)
            } else {
                mViewWidth - (mTouchX - mStartX)
            }
            Direction.NEXT -> dx = if (mStatus == Status.AutoBackward) {
                var distance = (mViewWidth - mStartX + mTouchX)
                if (distance > mViewWidth) {
                    distance = mViewWidth
                }
                mViewWidth - distance
            } else {
                -(mTouchX + (mViewWidth - mStartX))
            }
        }

        //滑动速度保持一致
        val duration = 400 * abs(dx) / mViewWidth
        mScroller.startScroll(mTouchX, 0, dx, 0, duration)
    }
}