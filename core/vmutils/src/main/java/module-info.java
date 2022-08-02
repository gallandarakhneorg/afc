/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import java.net.spi.URLStreamHandlerProvider;

/** Low-level and JRE related utilities.
 *
 * @uses URLStreamHandlerProvider only for test purpose
 * @provides URLStreamHandlerProvider implementations of "resource" and "file" schemes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.core.vmutils {
	requires org.eclipse.xtext.xbase.lib;
	requires com.google.common;
	requires java.activation;

	exports org.arakhne.afc.vmutil;
	exports org.arakhne.afc.vmutil.annotations;
	exports org.arakhne.afc.vmutil.asserts;
	exports org.arakhne.afc.vmutil.caller;
	exports org.arakhne.afc.vmutil.file;
	exports org.arakhne.afc.vmutil.json;
	exports org.arakhne.afc.vmutil.locale;
	exports org.arakhne.afc.vmutil.resource;

	provides URLStreamHandlerProvider
	with org.arakhne.afc.vmutil.resource.HandlerProvider,
		org.arakhne.afc.vmutil.file.HandlerProvider;
}