/*
 * noc-monitor-noswing - Ensures no Monitoring API calls performed on the Swing event dispatch thread.
 * Copyright (C) 2012, 2020, 2021  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of noc-monitor-noswing.
 *
 * noc-monitor-noswing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * noc-monitor-noswing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with noc-monitor-noswing.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.aoindustries.noc.monitor.noswing;

import com.aoindustries.noc.monitor.callable.CallableMonitor;
import com.aoindustries.noc.monitor.common.Monitor;
import com.aoindustries.util.IdentityKey;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.swing.SwingUtilities;

/**
 * @author  AO Industries, Inc.
 */
public class NoswingMonitor extends CallableMonitor {

	static void checkNoswing() {
		if(SwingUtilities.isEventDispatchThread()) throw new AssertionError("Running in Swing event dispatch thread");
	}

	private static final Map<IdentityKey<Monitor>, NoswingMonitor> cache = new HashMap<>();

	/**
	 * One unique NoswingMonitor is created for each monitor (by identity equals).
	 */
	public static NoswingMonitor getInstance(Monitor wrapped) throws RemoteException {
		// Don't double-wrap
		if(wrapped instanceof NoswingMonitor) return (NoswingMonitor)wrapped;
		IdentityKey<Monitor> key = new IdentityKey<>(wrapped);
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
