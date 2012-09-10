/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.common.SingleResultListener;
import com.aoindustries.noc.monitor.common.SingleResultNode;
import com.aoindustries.noc.monitor.common.SingleResult;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingSingleResultNode extends NoswingNode implements SingleResultNode {

    final private SingleResultNode wrapped;

    NoswingSingleResultNode(SingleResultNode wrapped) {
        super(wrapped);
        NoswingMonitor.checkNoswing();
        this.wrapped = wrapped;
    }

    @Override
    public void addSingleResultListener(SingleResultListener singleResultListener) throws RemoteException {
        NoswingMonitor.checkNoswing();
        wrapped.addSingleResultListener(singleResultListener);
    }

    @Override
    public void removeSingleResultListener(SingleResultListener singleResultListener) throws RemoteException {
        NoswingMonitor.checkNoswing();
        wrapped.removeSingleResultListener(singleResultListener);
    }

    @Override
    public SingleResult getLastResult() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getLastResult();
    }
}
