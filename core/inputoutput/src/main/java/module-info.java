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

/** Utilities related to input and output of files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.core.inputoutput {
	requires org.eclipse.xtext.xbase.lib;
	requires org.arakhne.afc.core.vmutils;
	requires transitive java.activation;
	requires transitive java.xml;

	exports org.arakhne.afc.inputoutput.endian;
	exports org.arakhne.afc.inputoutput.filefilter;
	exports org.arakhne.afc.inputoutput.filetype;
	exports org.arakhne.afc.inputoutput.mime;
	exports org.arakhne.afc.inputoutput.path;
	exports org.arakhne.afc.inputoutput.stream;
	exports org.arakhne.afc.inputoutput.xml;
}