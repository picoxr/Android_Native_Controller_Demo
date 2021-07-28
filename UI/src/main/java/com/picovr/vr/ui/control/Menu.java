package com.picovr.vr.ui.control;

import android.util.Log;

import com.picovr.vr.ui.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhc on 16-7-14.
 */
public class Menu {

    protected List<MenuItem> mMenuItemList;
    protected List<MenuPage> mMenuPages;
    protected int mCurPageNum = 0;

    protected int mPageRowNum;
    protected int mPageColNum;
    protected ViewGroup mBaseView;

    public Menu(int colNum, int rowNum) {
        mPageRowNum = colNum;
        mPageColNum = rowNum;
        mMenuPages = new ArrayList<>();
        mBaseView = new ViewGroup();
    }

    public void addMenuItem(MenuItem item) {
        mMenuItemList.add(item);
    }

    public void addAllMenuItem(List<MenuItem> menus) {
        mMenuItemList.addAll(menus);
    }

    public void removeMenuItem(MenuItem item) {
        mMenuItemList.remove(item);
    }

    protected int indexOfPage(MenuItem item) {
        int index = mMenuItemList.indexOf(item);
        int pageIndex = (index + 1) % (mPageColNum * mPageRowNum);
        return pageIndex;
    }

    protected int getPageCount() {
        return (int)Math.ceil((double)mMenuItemList.size()/(mPageColNum*mPageRowNum));
    }

    public List<MenuItem> getMenuItemsOnPage(int pageNum) {
        int pageItemCount = mPageColNum * mPageRowNum;
        int showItemCount = mMenuItemList.size() - pageNum * pageItemCount;
        if(showItemCount > pageItemCount) {
            showItemCount = pageItemCount;
        }
        int curPageItemStartIndex = pageNum * pageItemCount;
        int curPageItemEndIndex = curPageItemStartIndex + showItemCount;
        return mMenuItemList.subList(curPageItemStartIndex, curPageItemEndIndex);
    }

    public void init() {
        prepareMenuPages();

//        for (MenuItem item : mMenuItemList) {
//            item.onMeasure();
//            item.onDraw();
//        }
    }

    public void prepareMenuPages() {
        for (MenuPage page : mMenuPages) {
            page.onMeasure();
            page.onDraw();
        }
    }

    public int nextPage() {
        Log.d("RockVR.Menu", "===== nextPage() =====");
        int pageCount = getPageCount();
        if (mCurPageNum >= pageCount-1) {
            mCurPageNum = pageCount-1;
            Log.d("RockVR.Menu", "already last page");
            return mCurPageNum;
        }
        MenuPage oldMenuPage = mMenuPages.get(mCurPageNum);
        mCurPageNum++;
        Log.d("RockVR.Menu", "cur page: " + (mCurPageNum+1) + "/" + pageCount);
        MenuPage newMenuPage = mMenuPages.get(mCurPageNum);
        oldMenuPage.hidden();
        newMenuPage.show();
        Log.d("RockVR.Menu", "replace page done");
        return mCurPageNum;
    }

    public int prevPage() {
        Log.d("RockVR.Menu", "===== prevPage() =====");
        int pageCount = getPageCount();
        if (mCurPageNum <= 0 ) {
            mCurPageNum = 0;
            Log.d("RockVR.Menu", "already first page");
            return mCurPageNum;
        }
        MenuPage oldMenuPage = mMenuPages.get(mCurPageNum);
        mCurPageNum--;
        Log.d("RockVR.Menu", "cur page: " + (mCurPageNum+1) + "/" + pageCount);
        MenuPage newMenuPage = mMenuPages.get(mCurPageNum);
        oldMenuPage.hidden();
        newMenuPage.show();
        Log.d("RockVR.Menu", "replace page done");
        return mCurPageNum;
    }

    public void updateMenuItemPos() {

    }


}
