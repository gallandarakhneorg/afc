/* 
 * $Id$
 * 
 * Copyright (C) 2005-2010 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.vmutil;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class permits to centralize the identify of the thread service providers. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ThreadServiceFinder {

	private static final Stack<ThreadServiceProvider> services = new Stack<>();
	
	static {
		// Add the default provider
		services.push(new DefaultProvider());
	}
	
    /**
     * Replies the current service provider.
	 * 
     * @return the thread service provider or <code>null</code>
     */
    public static ThreadServiceProvider getProvider() {
    	try {
    		return services.peek();
    	}
    	catch(EmptyStackException exception) {
    		return null;
    	}
    }

    /**
     * Add a preferred provider.
	 * 
     * @param provider is the preferred thread service provider
     */
    public static void addPreferredProvider(ThreadServiceProvider provider) {
    	services.push(provider);
    }

    /**
     * Remove a provider.
	 * 
     * @param provider is the preferred thread service provider
     */
    public static void removeProvider(ThreadServiceProvider provider) {
    	services.remove(provider);
    }

    /**
     * Remove a provider.
     */
    public static void removeProvider() {
    	services.pop();
    }

    /**
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    static class DefaultProvider implements ThreadServiceProvider {

    	private ExecutorService executorService = null;
    	
    	private ScheduledExecutorService scheduledExecutorService = null;
    	
		/** {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public ExecutorService getExecutorService() {
			if (this.executorService==null) {
				this.executorService = Executors.newCachedThreadPool();
			}
			return this.executorService;
		}

		/** {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public ScheduledExecutorService getScheduledExecutorService() {
			if (this.scheduledExecutorService==null) {
				this.scheduledExecutorService = Executors.newScheduledThreadPool(3);
			}
			return this.scheduledExecutorService;
		}

    }
    
}
