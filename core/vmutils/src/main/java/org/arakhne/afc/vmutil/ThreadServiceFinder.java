/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.vmutil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class permits to centralize the identify of the thread service providers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class ThreadServiceFinder {

	private static final Deque<ThreadServiceProvider> SERVICES = new ArrayDeque<>();

	static {
		// Add the default provider
		SERVICES.push(new DefaultProvider());
	}

	private ThreadServiceFinder() {
		//
	}

	/**
     * Replies the current service provider.
	 *
     * @return the thread service provider or <code>null</code>
     */
	@Pure
    public static ThreadServiceProvider getProvider() {
    	try {
    		return SERVICES.peek();
    	} catch (EmptyStackException exception) {
    		return null;
    	}
    }

    /**
     * Add a preferred provider.
	 *
     * @param provider is the preferred thread service provider
     */
    public static void addPreferredProvider(ThreadServiceProvider provider) {
    	SERVICES.push(provider);
    }

    /**
     * Remove a provider.
     */
    public static void removeProvider() {
    	SERVICES.pop();
    }

    /** Thread service provider.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    static class DefaultProvider implements ThreadServiceProvider {

    	private ExecutorService executorService;

    	private ScheduledExecutorService scheduledExecutorService;

		@Override
		@Pure
		public ExecutorService getExecutorService() {
			if (this.executorService == null) {
				this.executorService = Executors.newCachedThreadPool();
			}
			return this.executorService;
		}

		@Override
		@Pure
		public ScheduledExecutorService getScheduledExecutorService() {
			if (this.scheduledExecutorService == null) {
				this.scheduledExecutorService = Executors.newScheduledThreadPool(3);
			}
			return this.scheduledExecutorService;
		}

    }

}
