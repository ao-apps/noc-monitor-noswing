/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.common.AlertLevel;
import com.aoindustries.noc.monitor.common.SingleResultNode;
import com.aoindustries.noc.monitor.common.Node;
import com.aoindustries.noc.monitor.common.RootNode;
import com.aoindustries.noc.monitor.common.TableMultiResultNode;
import com.aoindustries.noc.monitor.common.TableResultNode;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingNode implements Node {

    final private Node wrapped;

    NoswingNode(Node wrapped) {
        NoswingMonitor.checkNoswing();
        this.wrapped = wrapped;
    }

    @Override
    public Node getParent() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return new NoswingNode(wrapped.getParent());
    }

    @Override
    public List<? extends Node> getChildren() throws RemoteException {
        NoswingMonitor.checkNoswing();
        List<? extends Node> children = wrapped.getChildren();
        // Wrap
        List<Node> localWrapped = new ArrayList<Node>(children.size());
        for(Node child : children) {
            localWrapped.add(wrap(child));
        }
        return Collections.unmodifiableList(localWrapped);
    }

    @SuppressWarnings("unchecked")
    static Node wrap(Node node) {
        if(node instanceof SingleResultNode) return new NoswingSingleResultNode((SingleResultNode)node);
        if(node instanceof TableResultNode) return new NoswingTableResultNode((TableResultNode)node);
        if(node instanceof TableMultiResultNode) return new NoswingTableMultiResultNode((TableMultiResultNode)node);
        if(node instanceof RootNode) return new NoswingRootNode((RootNode)node);
        return new NoswingNode(node);
    }

    @Override
    public AlertLevel getAlertLevel() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getAlertLevel();
    }

    @Override
    public String getAlertMessage() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getAlertMessage();
    }

    @Override
    public boolean getAllowsChildren() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getAllowsChildren();
    }

    @Override
    public String getId() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getId();
    }

    @Override
    public String getLabel() throws RemoteException {
        NoswingMonitor.checkNoswing();
        return wrapped.getLabel();
    }
    
    @Override
    public boolean equals(Object O) {
        // Does this incur a round-trip to the server? assert !SwingUtilities.isEventDispatchThread() : "Running in Swing event dispatch thread";
        // The request times were 1-3 ns, I don't think it does
        if(O==null) return false;
        if(!(O instanceof Node)) return false;

        // Unwrap this
        Node thisNode = this;
        while(thisNode instanceof NoswingNode) thisNode = ((NoswingNode)thisNode).wrapped;

        // Unwrap other
        Node otherNode = (Node)O;
        while(otherNode instanceof NoswingNode) otherNode = ((NoswingNode)otherNode).wrapped;

        // Check equals
        return thisNode.equals(otherNode);
    }

    @Override
    public int hashCode() {
        NoswingMonitor.checkNoswing();
        return wrapped.hashCode();
    }
}
