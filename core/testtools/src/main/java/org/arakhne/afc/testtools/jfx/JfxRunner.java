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

import java.util.concurrent.CountDownLatch;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * This basic class runner ensures that JavaFx is running and then wraps all the runChild() calls in a
 * Platform.runLater(). runChild() is called for each test that is run. By wrapping each call in the Platform.runLater()
 * this ensures that the request is executed on the JavaFx thread.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class JfxRunner extends BlockJUnit4ClassRunner {

	private static final String ERROR_ON_TIMEOUT = "@TestInJfxThread does not work with timeouts in the " //$NON-NLS-1$
			+ "@Test Annotation. A possible Workaround might be a timeouted CompletableFuture."; //$NON-NLS-1$

	/**
	 * Constructs a new JavaFxJUnit4ClassRunner with the given parameters.
	 *
	 * @param clazz
	 *            The class that is to be run with this Runner
	 * @throws InitializationError
	 *             Thrown by the BlockJUnit4ClassRunner in the super()
	 */
	public JfxRunner(final Class<?> clazz) throws InitializationError {
		super(clazz);
		if (Platform.isSupported(ConditionalFeature.GRAPHICS)) {
			TestJfxApplication.startJavaFx();
		}
	}

	@SuppressWarnings("synthetic-access")
	@Override
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		// Create a latch which is only removed after the super runChild()
		// method
		// has been implemented.
		final CountDownLatch latch = new CountDownLatch(1);

		// Check whether the method should run in FX-Thread or not.
		final TestInJfxThread performMethodInFxThread = method.getAnnotation(TestInJfxThread.class);
		if (performMethodInFxThread != null) {
			final Test annotation = method.getAnnotation(Test.class);
			final long timeout = annotation.timeout();

			if (timeout > 0) {
				throw new UnsupportedOperationException(ERROR_ON_TIMEOUT);
			}

			Platform.runLater(() -> {
				JfxRunner.super.runChild(method, notifier);
				latch.countDown();
			});
		} else {
			JfxRunner.super.runChild(method, notifier);
			latch.countDown();
		}

		// Decrement the latch which will now proceed.
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
