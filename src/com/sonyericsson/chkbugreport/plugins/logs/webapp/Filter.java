/*
 * Copyright (C) 2013 Sony Mobile Communications AB
 *
 * This file is part of ChkBugReport.
 *
 * ChkBugReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * ChkBugReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ChkBugReport.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sonyericsson.chkbugreport.plugins.logs.webapp;

import com.sonyericsson.chkbugreport.plugins.logs.LogLine;
import com.sonyericsson.chkbugreport.util.Util;
import com.sonyericsson.chkbugreport.util.db.DBField;
import com.sonyericsson.chkbugreport.util.db.DBField.Type;

import java.util.regex.Pattern;

public class Filter {

    public enum Action {
        HIDE,
        SHOW,
        COLOR,
    };

    @DBField(type = Type.ID)
    private int mId;
    @DBField(type = Type.INT)
    private int mGroupId;
    @DBField(type = Type.VARCHAR)
    private String mTag;
    @DBField(type = Type.VARCHAR)
    private String mMsg;
    @DBField(type = Type.VARCHAR)
    private String mLine;
    @DBField(type = Type.VARCHAR)
    private Action mAction;
    @DBField(type = Type.INT)
    private int mActionArg;

    private Pattern mPTag, mPMsg, mPLine;

    public Filter(String tag, String msg, String line, Action action, int actionArg) {
        mTag = tag;
        mMsg = msg;
        mLine = line;
        mAction = action;
        mActionArg = actionArg;
    }

    public int getId() {
        return mId;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public String getTag() {
        return mTag;
    }

    public String getMsg() {
        return mMsg;
    }

    public String getLine() {
        return mLine;
    }

    public Action getAction() {
        return mAction;
    }

    public int getActionArg() {
        return mActionArg;
    }

    public void setGroupId(int id) {
        mGroupId = id;
    }

    public boolean handle(LogLine sl) {
        int matches = 0, outOf = 0;
        if (!Util.isEmpty(mTag)) {
            if (mPTag == null) {
                mPTag = Pattern.compile(mTag);
            }
            outOf++;
            if (mPTag.matcher(sl.tag).find()) {
                matches++;
            }
        }
        if (!Util.isEmpty(mMsg)) {
            if (mPMsg == null) {
                mPMsg = Pattern.compile(mMsg);
            }
            outOf++;
            if (mPMsg.matcher(sl.msg).find()) {
                matches++;
            }
        }
        if (!Util.isEmpty(mLine)) {
            if (mPLine == null) {
                mPLine = Pattern.compile(mLine);
            }
            outOf++;
            if (mPLine.matcher(sl.line).find()) {
                matches++;
            }
        }
        if (matches == outOf && outOf > 0) {
            // The line matches, now decide what to do
            return mAction != Action.HIDE;
        }

        return true; // By default
    }

}
