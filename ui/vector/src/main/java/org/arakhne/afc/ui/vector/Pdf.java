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

package org.arakhne.afc.ui.vector;



/** Wrapper on a PDF API able to extract bitmaps from PDF documents.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface Pdf {
	
	/** Replies the image of the current page.
	 * 
	 * @return the image or <code>null</code> if the image cannot
	 * be obtained.
	 */
	public Image getImage();
	
	/** Set the dimension of the viewer.
	 * 
	 * @param dimension
	 */
	public void setViewerSize(Dimension dimension);
	
	/** Replies the current page number that is replied by {@link #getImage()}.
	 * 
	 * @return the current page number.
	 */
	public int getPageNumber();
	
	/** Set the current page number that is replied by {@link #getImage()}.
	 * 
	 * @param pageno is the current page number.
	 * @param observer is the observer of the image loading.
	 * @return <code>true</code> if the page number has changed.
	 */
	public boolean setPageNumber(int pageno, ImageObserver observer);

	/** Replies the total number of pages in the PDF document.
	 * 
	 * @return the total number of pages.
	 */
	public int getPageCount();
	
	/** Stop any asynchronous page loading.
	 */
	public void stopPageLoading();
	
	/** Release any internal resources.
	 */
	public void release();
	
	/** Replies the width of the PDF page.
	 * 
	 * @return the width or Float.NaN.
	 */
	public float getPageWidth();

	/** Replies the height of the PDF page.
	 * 
	 * @return the height or Float.NaN.
	 */
	public float getPageHeight();

}
