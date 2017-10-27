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

package org.arakhne.afc.gis.bus.network;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.text.TextUtil;

/** Several utilities on bus network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class BusNetworkUtilities {

	/** Implementation of a string comparator which is
	 * case-insensitive and ignore accent signs.
	 */
	public static final Comparator<String> NAME_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			final String s1 = TextUtil.removeAccents(o1);
			final String s2 = TextUtil.removeAccents(o2);
			return s1.compareToIgnoreCase(s2);
		}
	};

	private BusNetworkUtilities() {
		//
	}

	/** Replies all the bus itinerary halts in the given bus line.
	 *
	 * @param busLine the bus line.
	 * @return the iterator on bus itinerary halts.
	 */
	@Pure
	public static Iterator<BusItineraryHalt> getBusHalts(BusLine busLine) {
		assert busLine != null;
		return new LineHaltIterator(busLine);
	}

	/** Replies all the bus itinerary halts in the given bus network.
	 *
	 * @param busNetwork the bus network.
	 * @return the iterator on bus itinerary halts.
	 */
	@Pure
	public static Iterator<BusItineraryHalt> getBusHalts(BusNetwork busNetwork) {
		assert busNetwork != null;
		return new NetworkHaltIterator(busNetwork);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class LineHaltIterator implements Iterator<BusItineraryHalt> {

		private final Iterator<BusItinerary> itineraries;

		private Iterator<BusItineraryHalt> halts;

		private BusItineraryHalt next;

		LineHaltIterator(BusLine line) {
			this.itineraries = line.busItineraryIterator();
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while ((this.halts == null || !this.halts.hasNext()) && this.itineraries.hasNext()) {
				final BusItinerary it = this.itineraries.next();
				this.halts = it.busHaltIterator();
			}
			if (this.halts != null && this.halts.hasNext()) {
				this.next = this.halts.next();
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public BusItineraryHalt next() {
			final BusItineraryHalt n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	} // class NetworkLineIterator

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class NetworkHaltIterator implements Iterator<BusItineraryHalt> {

		private final Iterator<BusLine> lines;

		private Iterator<BusItineraryHalt> halts;

		private BusItineraryHalt next;

		NetworkHaltIterator(BusNetwork network) {
			this.lines = network.busLineIterator();
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while ((this.halts == null || !this.halts.hasNext()) && this.lines.hasNext()) {
				final BusLine li = this.lines.next();
				this.halts = new LineHaltIterator(li);
			}
			if (this.halts.hasNext()) {
				this.next = this.halts.next();
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public BusItineraryHalt next() {
			final BusItineraryHalt n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	} // class NetworkHaltIterator

}
