/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.testtools.jfx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.runners.model.InitializationError;

/**
 * This is the application which starts JavaFx. It is controlled through the
 * startJavaFx() method.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class TestJfxApplication extends Application {

	/** The lock that guarantees that only one JavaFX thread will be started. */
	private static final ReentrantLock LOCK = new ReentrantLock();

	/** Started flag. */
	private static AtomicBoolean started = new AtomicBoolean();

	/** Start JavaFX.
	 *
	 * @throws InitializationError if the JavaFX cannot be initialized.
	 */
	public static void startJavaFx() throws InitializationError {
		try {
			// Lock or wait. This gives another call to this method time to
			// finish
			// and release the lock before another one has a go
			LOCK.lock();

			if (!started.get()) {
				// start the JavaFX application
				final ExecutorService executor = Executors.newSingleThreadExecutor();
				final Future<?> jfxLaunchFuture = executor.submit(() -> TestJfxApplication.launch());

				while (!started.get()) {
					try {
						jfxLaunchFuture.get(1, TimeUnit.MILLISECONDS);
					} catch (InterruptedException | TimeoutException e) {
						// continue waiting until success or error
					}
					Thread.yield();
				}
			}
		} catch (ExecutionException e) {
			throw new InitializationError(e);
		} finally {
			LOCK.unlock();
		}
	}

	/** Launch the test application.
	 */
	protected static void launch() {
		Application.launch();
	}

	@Override
	public void start(Stage stage) {
		started.set(Boolean.TRUE);
	}

}
