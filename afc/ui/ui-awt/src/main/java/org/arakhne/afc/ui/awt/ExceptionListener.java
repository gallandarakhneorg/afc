/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.ui.awt;

import java.util.EventListener;

/** Listener on exceptions. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ExceptionListener extends EventListener {

	/** Invoked when an exception was thrown.
	 * <p>
	 * If all of the listeners reply <code>false</code>, the default
	 * catching behavior is executed: the exception is forwaded to the VM.
	 * 
	 * @param exception is the thrown exception
	 * @return <code>true</code> if the exception is treated in the listener;
	 * <code>false</code> if the exception is not treated in the listener.
	 */
	public boolean exceptionThrown(Throwable exception);

}
