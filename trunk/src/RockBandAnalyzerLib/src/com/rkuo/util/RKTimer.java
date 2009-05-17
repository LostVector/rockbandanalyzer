package com.rkuo.util;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 14, 2009
 * Time: 10:32:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RKTimer {

    Long    _msecsBefore;
    Long    _msecsAfter;

    public RKTimer() {
        return;
    }

    public void Start() {
        _msecsBefore = System.currentTimeMillis();
    }

    public Long Stop() {
        _msecsAfter = System.currentTimeMillis();
        return _msecsAfter - _msecsBefore;
    }
}
