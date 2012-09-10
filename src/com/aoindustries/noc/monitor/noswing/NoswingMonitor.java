/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.common.Monitor;
import com.aoindustries.noc.monitor.common.RootNode;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.SwingUtilities;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingMonitor implements Monitor {

    static void checkNoswing() {
        if(SwingUtilities.isEventDispatchThread()) throw new AssertionError("Running in Swing event dispatch thread");
    }

    final private Monitor wrapped;

    public NoswingMonitor(Monitor wrapped) throws RemoteException, NotBoundException {
        checkNoswing();
        this.wrapped = wrapped;
    }

    @Override
    public RootNode login(Locale locale, String username, String password) throws RemoteException, IOException, SQLException {
        checkNoswing();
        return new NoswingRootNode(wrapped.login(locale, username, password));
    }
}
