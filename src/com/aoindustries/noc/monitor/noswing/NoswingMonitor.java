/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.common.Monitor;
import com.aoindustries.noc.monitor.wrapper.WrappedMonitor;
import com.aoindustries.util.IdentityKey;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.swing.SwingUtilities;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingMonitor extends WrappedMonitor {

    static void checkNoswing() {
        if(SwingUtilities.isEventDispatchThread()) throw new AssertionError("Running in Swing event dispatch thread");
    }

    private static final Map<IdentityKey<Monitor>,NoswingMonitor> cache = new HashMap<IdentityKey<Monitor>,NoswingMonitor>();

    /**
     * One unique NoswingMonitor is created for each monitor (by identity equals).
     */
    public static NoswingMonitor getInstance(Monitor wrapped) throws RemoteException {
        // Don't double-wrap
        if(wrapped instanceof NoswingMonitor) return (NoswingMonitor)wrapped;
        IdentityKey<Monitor> key = new IdentityKey<Monitor>(wrapped);
        synchronized(cache) {
            NoswingMonitor server = cache.get(key);
            if(server==null) {
                server = new NoswingMonitor(wrapped);
                cache.put(key, server);
            }
            return server;
        }
    }

    private NoswingMonitor(Monitor wrapped) {
        super(wrapped);
    }

    @Override
    protected <T> T call(Callable<T> callable, boolean allowRetry) throws RemoteException {
        checkNoswing();
        return super.call(callable, allowRetry);
    }
}
