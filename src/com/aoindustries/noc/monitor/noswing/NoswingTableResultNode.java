/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.common.TableResult;
import com.aoindustries.noc.monitor.common.TableResultListener;
import com.aoindustries.noc.monitor.common.TableResultNode;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingTableResultNode extends NoswingNode implements TableResultNode {

    final private TableResultNode wrapped;

    NoswingTableResultNode(TableResultNode wrapped) {
        super(wrapped);
        NoswingMonitor.checkNoswing();
        this.wrapped = wrapped;
    }

    @Override
    public void addTableResultListener(TableResultListener tableResultListener) throws RemoteException {
        NoswingMonitor.checkNoswing();
        wrapped.addTableResultListener(tableResultListener);
    }

    @Override
    public void removeTableResultListener(TableResultListener tableResultListener) throws RemoteException {
        NoswingMonitor.checkNoswing();
        wrapped.removeTableResultListener(tableResultListener);
    }

    @Override
    public TableResult getLastResult() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getLastResult();
    }
}
