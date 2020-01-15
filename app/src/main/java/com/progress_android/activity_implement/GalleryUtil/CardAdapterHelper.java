package com.progress_android.activity_implement.GalleryUtil;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import jameson.io.library.util.ScreenUtil;

/**
 * adapter中调用onCreateViewHolder, onBindViewHolder
 * Created by jameson on 9/1/16.
 */
public class CardAdapterHelper {
    private int mPagePadding = 15;
    private int mShowLeftCardWidth = 0;
    private String TAG = "CardAdapterhelper";

    public void onCreateViewHolder(ViewGroup parent,  View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        //Log.d(TAG, "lp.width="+lp.width + " lp.height="+lp.height);
        //lp.width = parent.getWidth() - ScreenUtil.dip2px(itemView.getContext(), 2 * (mPagePadding + mShowLeftCardWidth));
        //Log.d(TAG, "update lp.width="+lp.width + " lp.height="+lp.height);
        //itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
        //int padding = ScreenUtil.dip2px(itemView.getContext(), mPagePadding);
        //Log.d(TAG, "itemView.paddingLeft="+itemView.getPaddingLeft()+" paddingRight="+itemView.getPaddingRight());
        //itemView.setPadding(padding, 0, padding, 0);
        //Log.d(TAG, "update itemView.paddingLeft="+itemView.getPaddingLeft()+" paddingRight="+itemView.getPaddingRight());
        //int leftMarin = position == 0 ? padding + ScreenUtil.dip2px(itemView.getContext(), mShowLeftCardWidth) : 0;
        //int rightMarin = position == itemCount - 1 ? padding + ScreenUtil.dip2px(itemView.getContext(), mShowLeftCardWidth) : 0;
        //setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        //Log.d(TAG, "itemView.MarginLeft="+lp.leftMargin+" MarginRight="+lp.rightMargin+" MarginBottom="+lp.bottomMargin + " MarginTop="+lp.topMargin);
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
            //Log.d(TAG, "update itemView.MarginLeft="+lp.leftMargin+" MarginRight="+lp.rightMargin+" MarginBottom="+lp.bottomMargin + " MarginTop="+lp.topMargin);
        }
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }
}
