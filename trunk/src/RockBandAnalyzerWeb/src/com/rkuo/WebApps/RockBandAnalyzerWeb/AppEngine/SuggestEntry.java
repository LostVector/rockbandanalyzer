package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 6, 2009
 * Time: 5:22:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class SuggestEntry {

    public String getSuggestKeyword() {
        return suggestKeyword;
    }

    public void setSuggestKeyword(String suggestKeyword) {
        this.suggestKeyword = suggestKeyword;
    }

    private String suggestKeyword;

    public List<Long> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Long> songIds) {
        this.songIds = songIds;
    }

    private List<Long> songIds;

    public SuggestEntry() {
        suggestKeyword = "";
        songIds = new ArrayList<Long>();
        return;
    }
}
