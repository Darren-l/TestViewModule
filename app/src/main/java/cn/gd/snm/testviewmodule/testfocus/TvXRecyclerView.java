package cn.gd.snm.testviewmodule.testfocus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * ================================================
 *
 * @author 作    者：qiuxin（邱信）
 * 版    本：1.0
 * 创建日期：2020/1/2
 * 描    述：跟tvmianrecyclerview增加了快速滑动不丢焦点
 * 修订历史：
 * ================================================
 */
public class TvXRecyclerView extends RecyclerView implements View.OnFocusChangeListener {


    private int mSelectedItemOffsetStart;

    private int mSelectedItemOffsetEnd;
    private View mChild;
    /**
     * 滑动次数
     */
    public int mScrollNumber = 0;
    /**
     * 是否在滑动中
     */
    public boolean mIsScrolling;
    /**
     * 最左边的view是否消耗掉按键，屏幕的上下左右
     */
    private boolean mIsHandLastLeftKey = true, mIsHandLastRightKey = true, mIsHandLastUpKey = true, mIsHandLastDownKey = true;

    /**
     * 快速滑动不获焦开关
     */
    private boolean mIsLongPressDoesNotGainFocus = false;
    /**
     * 按钮事件
     */
    private int mKeyEventCode;

    /**
     * 左右滑动的时候不进行rv拖动,默认为进行居中滑动
     */
    private boolean mLeftRightNoScroll = false;
    /**
     * 子adapter当前选中的position
     */
    private int mChildAdapterPosition;

    private int mDyOffset;
    private final Rect mTempRect = new Rect();
    /**
     * 是否需要焦点记忆
     */
    private boolean mIsFocusMemory = false;
    /**
     * 需要限制的速度
     */
    public int mTimeIntervalBetween = 0;
    private long mLastKeyDownTime = 0;

    /**
     * 设置按键速度限速
     */
    public void setTimeIntervalBetween(int mTimeIntervalBetween) {
        this.mTimeIntervalBetween = mTimeIntervalBetween;
    }


    /**
     * 设置居中偏移量
     *
     * @param dyOffset
     */
    public void setDyOffset(int dyOffset) {
        mDyOffset = dyOffset;
    }

    public int getChildAdapterPosition() {
        return mChildAdapterPosition;
    }

    public void setLongPressDoesNotGainFocus(boolean longPressDoesNotGainFocus) {
        mIsLongPressDoesNotGainFocus = longPressDoesNotGainFocus;
    }

    public void setFocusMemory(boolean focusMemory) {
        mIsFocusMemory = focusMemory;
    }

    public void setLeftRightNoScroll(boolean leftRightNoScroll) {
        mLeftRightNoScroll = leftRightNoScroll;
    }

    public void setHandLastKey(boolean mIsHandLastLeftKey, boolean mIsHandLastRightKey, boolean mIsHandLastUpKey, boolean mIsHandLastDownKey) {
        this.mIsHandLastLeftKey = mIsHandLastLeftKey;
        this.mIsHandLastRightKey = mIsHandLastRightKey;
        this.mIsHandLastUpKey = mIsHandLastUpKey;
        this.mIsHandLastDownKey = mIsHandLastDownKey;
    }

    public TvXRecyclerView(Context context) {
        super(context);
        init();
    }

    public TvXRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvXRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setPreserveFocusAfterLayout(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setChildrenDrawingOrderEnabled(true);
        // 自身不作onDraw处理
        setWillNotDraw(true);
        setHasFixedSize(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);

        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setFocusableInTouchMode(true);

        //启用子视图排序功能
        //取消item动画
        setItemAnimator(null);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            mChild = child;
            mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
            mSelectedItemOffsetStart /= 2;
            mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
        }
        super.requestChildFocus(child, focused);

    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (mIsFocusMemory) {
            View view = getLayoutManager().findViewByPosition(mChildAdapterPosition);
            if (hasFocus() || mChildAdapterPosition < 0 || view == null) {
                super.addFocusables(views, direction, focusableMode);
            } else if (view.isFocusable()) {
                //将当前的view放到Focusable views列表中，再次移入焦点时会取到该view,实现焦点记忆功能
                views.add(view);
            } else {
                super.addFocusables(views, direction, focusableMode);
            }
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View focusedView = getFocusedChild();
        if (null != focusedView) {
            int position = indexOfChild(focusedView);
            if (i == childCount - 1) {
                //这是最后一个需要刷新的item
                if (position > i) {
                    position = i;
                }
                return position;
            } else if (position == i) {
                //这是原本要在最后一个刷新的item
                return childCount - 1;
            }
        }
        return i;
    }

    @Override
    public void onDraw(Canvas c) {
        mChildAdapterPosition = indexOfChild(getFocusedChild());
        super.onDraw(c);
    }

    public boolean isNeedAutScro = true;
    public boolean onceScroDir = false;

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = getWidth() - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;

        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);

        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);


        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();


        int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }

        try {
            if ((dx != 0 || dy != 0)) {
                if (mLeftRightNoScroll && (mKeyEventCode == KeyEvent.KEYCODE_DPAD_LEFT || mKeyEventCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                    return true;
                } else {
                    mScrollNumber++;
                    Log.i("DebugCheck", "DebugCheck scrollBy...");
                    if (isNeedAutScro) {
                        if (immediate) {
                            scrollBy(dx, dy + mDyOffset);
                        } else {
                            if (onceScroDir) {
                                Log.i("DebugCheck", "DebugCheck scrollBy11...");
                                scrollBy(dx, dy + mDyOffset);
                                onceScroDir = false;
                            } else {
                                Log.i("DebugCheck", "DebugCheck scrollBy22...");
                                smoothScrollBy(dx, dy + mDyOffset, new DecelerateInterpolator(1.5f));

                            }
                        }
                    }
                    isNeedAutScro = true;
                }
                // 重绘
                // .是为了选中item置顶，具体请参考getChildDrawingOrder方法
                postInvalidate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LayoutManager manager = getLayoutManager();
        if (manager != null) {
            LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
            return layout.getOrientation() == LinearLayoutManager.VERTICAL;

        }
        return false;
    }

    public static int LONG_PRESS_EVENT = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            int keyCode = event.getKeyCode();
            mKeyEventCode = keyCode;
            // TODO: 2020/4/27 长按焦点拦截记录
            if (mIsLongPressDoesNotGainFocus) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    LONG_PRESS_EVENT = event.getRepeatCount();
                }
            }


            View mVideoFocusView = getFocusedChild();
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null || mVideoFocusView == null) {
                return false;
            }
            mChildAdapterPosition = layoutManager.getPosition(mVideoFocusView);
            // LogUtils.e("mChildAdapterPosition" + mChildAdapterPosition);

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                long current = System.currentTimeMillis();
                if (current - mLastKeyDownTime < mTimeIntervalBetween) {
                    return true;
                }
                mLastKeyDownTime = current;

                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                        || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    View nextFocusView = null;
                    try {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_LEFT:
                                if (mIsHandLastLeftKey) {
                                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), View.FOCUS_LEFT);
                                    if (nextFocusView == null) {
                                        // LogUtils.e("->> keyEvent拦截======左");
                                        return true;
                                    }
                                }
                                break;
                            case KeyEvent.KEYCODE_DPAD_RIGHT:
                                if (mIsHandLastRightKey) {
                                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), View.FOCUS_RIGHT);
                                    if (nextFocusView == null) {
                                        // LogUtils.e("->> keyEvent拦截======右");
                                        if (mOnEdgeInterceptListener != null) {
                                            mOnEdgeInterceptListener.onEdgeRight();
                                        }
                                        return true;
                                    }
                                }
                                break;
                            case KeyEvent.KEYCODE_DPAD_DOWN:
                                if (mIsHandLastDownKey) {
                                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), View.FOCUS_DOWN);
                                    if (nextFocusView == null) {
                                        // LogUtils.e("->> keyEvent拦截======下");
                                        return true;
                                    }
                                }
                                break;
                            case KeyEvent.KEYCODE_DPAD_UP:
                                if (mIsHandLastUpKey) {
                                    nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), View.FOCUS_UP);
                                    if (nextFocusView == null) {
                                        // LogUtils.e("->> keyEvent拦截======上");
                                        if (mOnEdgeInterceptListener != null) {
                                            mOnEdgeInterceptListener.onEdgeTop();
                                        }
                                        return true;
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_SETTLING:
                // TODO: 2020/4/23 滑动中
                mIsScrolling = true;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                mIsScrolling = false;
                // TODO: 2020/4/23 滑动停止
                mScrollNumber = 0;
                break;
            default:
                break;
        }
    }

    public boolean isScrolling() {
        return getScrollState() != SCROLL_STATE_IDLE;
    }

    /**
     * 滚动到并选中某个位置
     *
     * @param position
     * @param isRequestFocus 是否选中获焦
     * @param isDelayed      是否需要延时(滑动距离过长的时候稍微加上延时能够更好的聚焦)
     */
    public void setSpeedinessScrollingSelectPosition(final int position, boolean isRequestFocus, boolean isDelayed) {

        mChildAdapterPosition = position;

        // TODO: 2020/6/23  滚动到第几个栏目修改
        scrollToPosition(position);

        if (isRequestFocus) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        View view = getLayoutManager().findViewByPosition(position);
                        view.requestFocus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, isDelayed ? 400 : 0);
        }
    }


    /**
     * 判断是否已经滑动到底部
     *
     * @param recyclerView
     * @return
     */
    public boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1) {
            return true;
        } else {
            return false;
        }
    }





    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // LogUtils.e(hasFocus);
        //判断是否开启焦点记忆
        if (hasFocus) {
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            if (mChildAdapterPosition > 0) {
                View viewByPosition = getLayoutManager().findViewByPosition(mChildAdapterPosition);
                if (viewByPosition != null) {
                    viewByPosition.requestFocus();
                }
            }
        } else {
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

    private class TvSmoothScroller extends LinearSmoothScroller {
        private boolean mIsSmooth;
        private int mOffset;

        public TvSmoothScroller(Context context, int offset) {
            super(context);
            mOffset = offset;
        }

        @Override
        protected void onTargetFound(View targetView, State state, Action action) {
            if (null != getLayoutManager()) {
                getDecoratedBoundsWithMargins(targetView, mTempRect);
                mOffset = (getLayoutManager().canScrollHorizontally() ? (getFreeWidth() - mTempRect.width())
                        : (getFreeHeight() - mTempRect.height())) / 2;
            }
            super.onTargetFound(targetView, state, action);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            int dt = boxStart - viewStart + mOffset - mDyOffset;
            return dt;
        }

    }

    /**
     * 边沿拦截回调接口
     */
    private OnEdgeInterceptListener mOnEdgeInterceptListener;

    public void setOnEdgeInterceptListener(OnEdgeInterceptListener listener) {
        this.mOnEdgeInterceptListener = listener;
    }
    public interface OnEdgeInterceptListener{
        /**
         * 如果已经达到右边边沿,回调给监听者
         */
        void onEdgeRight();

        /**
         * 同理上边界
         */
        void onEdgeTop();
    }
}

