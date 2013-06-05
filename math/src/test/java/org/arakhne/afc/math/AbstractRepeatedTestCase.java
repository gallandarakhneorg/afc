/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math;

import org.arakhne.util.ref.AbstractTestCase;

import junit.framework.TestResult;

/**
 * Permit to repeat tests. Instead of <code>junit.extensions.RepeatedTest</code>, this
 * class provides function to known at which step we are.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractRepeatedTestCase extends AbstractTestCase {

	private final int testCount;
	private int testStep;
	
	/**
	 */
	public AbstractRepeatedTestCase() {
		this(-1);
	}

	/**
	 * @param testCount the count of tests to repeat.
	 */
	public AbstractRepeatedTestCase(int testCount) {
		int tc = testCount;
		if (tc<=0) {
			tc = this.RANDOM.nextInt(206)+50;
		}
		this.testCount = tc;
		this.testStep = -1;
	}
	
	/** Replies the current iteration index.
	 * 
	 * @return the current iteration index or <code>-1</code>.
	 */
	public int getIterationIndex() {
		return this.testStep;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(TestResult result) {
		for (int i= 0; i < this.testCount; i++) {
			this.testStep = i;
			if (result.shouldStop())
				break;
			super.run(result);
			if (result.failureCount()>0 || result.errorCount()>0)
				break;
		}
		this.testStep = -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int countTestCases() {
		return this.testCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "test "+(getIterationIndex()+1)+"/"+this.testCount;  //$NON-NLS-1$//$NON-NLS-2$
	}

}
