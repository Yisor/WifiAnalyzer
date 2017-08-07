package com.komect.network.databinding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import com.komect.network.R;

/**
 * 通用的顶部导航栏设置参数
 * Author by hf
 * Create on 16/9/13
 */
public class TopBarConfig {
    public static TopBarConfig getInstance4Activity(Activity activity) {
        TopBarConfig config = new TopBarConfig(activity);
        config.bgColor = activity.getResources().getColor(R.color.bgBlue);
        switch (activity.getClass().getSimpleName()) {
            case "MainActivity":
                config.titleTxt = activity.getString(R.string.main_title);
                config.rightMenuVisibility = true;
                break;
            default:
                config.titleTxt = activity.getString(R.string.app_name);
                break;
            //TODO 不同页面的导航栏参数配置在这里添加
        }
        return config;
    }

    private boolean rightTxtVisibility = false;//右侧文字按钮可见性
    private boolean rightMenuVisibility= false;//右侧menu按钮可见性
    private boolean rightIvVisibility = false;//右侧刷新按钮可见性
    private String titleTxt;//标题文案
    private String rightTxt;//右侧文案
    private int titleTxtColor;//标题文案颜色
    private int rightTxtColor;//右侧文案颜色


    private boolean leftIconVisibility = false;//true 显示menu icon；false显示back icon
    private int bgColor;

    public TopBarConfig(Context context) {
        titleTxtColor = Color.WHITE;
        rightTxtColor = Color.WHITE;
        rightTxtColor = titleTxtColor;
    }

    public boolean isRightTxtVisibility() {
        return rightTxtVisibility;
    }

    public void setRightTxtVisibility(boolean rightTxtVisibility) {
        this.rightTxtVisibility = rightTxtVisibility;
    }

    public String getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
    }

    public String getRightTxt() {
        return rightTxt;
    }

    public void setRightTxt(String rightTxt) {
        this.rightTxt = rightTxt;
    }

    public int getTitleTxtColor() {
        return titleTxtColor;
    }

    public void setTitleTxtColor(int titleTxtColor) {
        this.titleTxtColor = titleTxtColor;
    }

    public int getRightTxtColor() {
        return rightTxtColor;
    }

    public void setRightTxtColor(int rightTxtColor) {
        this.rightTxtColor = rightTxtColor;
    }

    public boolean isLeftIconVisibility() {
        return leftIconVisibility;
    }

    public void setLeftIconVisibility(boolean leftIconVisibility) {
        this.leftIconVisibility = leftIconVisibility;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isRightMenuVisibility() {
        return rightMenuVisibility;
    }

    public void setRightMenuVisibility(boolean rightMenuVisibility) {
        this.rightMenuVisibility = rightMenuVisibility;
    }

    public boolean isRightIvVisibility() {
        return rightIvVisibility;
    }

    public void setRightIvVisibility(boolean rightIvVisibility) {
        this.rightIvVisibility = rightIvVisibility;
    }
}
