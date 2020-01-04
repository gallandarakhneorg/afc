/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.bootique.applicationdata2.modules;

import java.lang.reflect.Field;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.ProvisionListener;
import io.bootique.meta.application.ApplicationMetadata;

import org.arakhne.afc.bootique.applicationdata2.annotations.DefaultApplicationName;

/** Module for the compiler application metadata version 2.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class ApplicationMetadata2Module extends AbstractModule {

	@Override
	protected void configure() {
		binder().bindListener(new BindingMatcher(), new ApplicationProvisionListener(binder().getProvider(Injector.class)));
	}

	/** Listener on application provision.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	private static class ApplicationProvisionListener implements ProvisionListener {

		private final Provider<Injector> injector;

		/** Constructor.
		 *
		 * @param injector the injector.
		 */
		ApplicationProvisionListener(Provider<Injector> injector) {
			this.injector = injector;
		}

		@Override
		public <T> void onProvision(ProvisionInvocation<T> provision) {
			final T object = provision.provision();
			if (object instanceof ApplicationMetadata) {
				final ApplicationMetadata metadata = (ApplicationMetadata) object;
				final Injector inj = this.injector.get();
				String name;
				try {
					name = inj.getInstance(Key.get(String.class, DefaultApplicationName.class));
				} catch (Throwable exception) {
					name = null;
				}
				if (!Strings.isNullOrEmpty(name)) {
					setName(metadata, name);
				}
			}
		}

		/** Change the application name.
		 *
		 * @param metadata the metadata to change.
		 * @param name the new name.
		 */
		private static void setName(ApplicationMetadata metadata, String name) {
			try {
				final Field field = ApplicationMetadata.class.getDeclaredField("name"); //$NON-NLS-1$
				field.setAccessible(true);
				field.set(metadata, name);
			} catch (Exception exception) {
				//
			}
		}

	}

	/** Matcher of sub types.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	private static class BindingMatcher extends AbstractMatcher<Binding<?>> {

		/** Constructor.
		 */
		BindingMatcher() {
			//
		}

		@Override
		public boolean matches(Binding<?> binding) {
			return ApplicationMetadata.class.isAssignableFrom(binding.getKey().getTypeLiteral().getRawType());
		}

	}

}
