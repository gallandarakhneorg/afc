/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.agentmotion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AgentMotion")
@SuppressWarnings("all")
public class AgentMotionTest extends AbstractMathTestCase {

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("testClone_1")
		@Test
		public void testClone_1() {
			double x = getRandom().nextDouble();
			double y = getRandom().nextDouble();
			double r = getRandom().nextDouble();
			AgentMotion m = new AgentMotion(x, y, r);
			AgentMotion clone = m.clone();
			assertFpVectorEquals(x, y, clone.getLinear());
			assertEpsilonEquals(r, clone.getAngular());
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsTest {

		@DisplayName("testEquals_1")
		@Test
		public void testEquals_1() {
			double x = getRandom().nextDouble();
			double y = getRandom().nextDouble();
			double r = getRandom().nextDouble();
			AgentMotion m = new AgentMotion(x, y, r);

			AgentMotion clone = m.clone();
			AgentMotion m2 = new AgentMotion(x, y, r);
			AgentMotion m3 = new AgentMotion(x, y, r + 1);

			assertTrue(clone.equals(m));
			assertTrue(m.equals(clone));
			assertTrue(m2.equals(m));
			assertTrue(m.equals(m2));
			assertFalse(m3.equals(m));
			assertFalse(m.equals(m3));
		}
	}

	@DisplayName("getAngular")
	@Nested
	public class GetAngular {

		@DisplayName("getAngular_1")
		@Test
		public void getAngular_1() {
			double x = getRandom().nextDouble();
			double y = getRandom().nextDouble();
			double r = getRandom().nextDouble();
			AgentMotion m = new AgentMotion(x, y, r);
			assertEpsilonEquals(r, m.getAngular());
		}
	}

	@DisplayName("getLinear")
	@Nested
	public class GetLinear {

		@DisplayName("getLinear_1")
		@Test
		public void getLinear_1() {
			double x = getRandom().nextDouble();
			double y = getRandom().nextDouble();
			double r = getRandom().nextDouble();
			AgentMotion m = new AgentMotion(x, y, r);
			assertFpVectorEquals(x, y, m.getLinear());
		}
	}

	@DisplayName("this += AgentMotion")
	@Nested
	public class OperatorAddAgentMotion {

		@DisplayName("operator_addAgentMotion_1")
		@Test
		public void operator_addAgentMotion_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_add(m2);

			assertFpVectorEquals(x1 + x2, y1 + y2, m1.getLinear());
			assertEpsilonEquals(r1 + r2, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("this += double")
	@Nested
	public class OperatorAddDouble {

		@DisplayName("operator_addDouble_1")
		@Test
		public void operator_addDouble_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_add(r2);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1 + r2, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {

		@DisplayName("operator_addVector2D_1")
		@Test
		public void operator_addVector2D_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_add(new Vector2d(x2, y2));

			assertFpVectorEquals(x1 + x2, y1 + y2, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("this - AgentMotion")
	@Nested
	public class OperatorMinusAgentMotion {

		@DisplayName("operator_minusAgentMotion_1")
		@Test
		public void operator_minusAgentMotion_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_minus(m2);

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1 - x2, y1 - y2, o.getLinear());
			assertEpsilonEquals(r1 - r2, o.getAngular());
		}
	}

	@DisplayName("this - double")
	@Nested
	public class OperatorMinusDouble {

		@DisplayName("operator_minusDouble_1")
		@Test
		public void operator_minusDouble_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_minus(r2);

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1, y1, o.getLinear());
			assertEpsilonEquals(r1 - r2, o.getAngular());
		}
	}

	@DisplayName("this - Vector3D")
	@Nested
	public class OperatorMinusVector2D {

		@DisplayName("operator_minusVector2D_1")
		@Test
		public void operator_minusVector2D_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_minus(new Vector2d(x2, y2));

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1 - x2, y1 - y2, o.getLinear());
			assertEpsilonEquals(r1, o.getAngular());
		}
	}

	@DisplayName("this + AgentMotion")
	@Nested
	public class OperatorPlusAgentMotion {

		@DisplayName("operator_plusAgentMotion_1")
		@Test
		public void operator_plusAgentMotion_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_plus(m2);

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1 + x2, y1 + y2, o.getLinear());
			assertEpsilonEquals(r1 + r2, o.getAngular());
		}
	}

	@DisplayName("this + double")
	@Nested
	public class OperatorPlusDouble {

		@DisplayName("operator_plusDouble_1")
		@Test
		public void operator_plusDouble_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_plus(r2);

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1, y1, o.getLinear());
			assertEpsilonEquals(r1 + r2, o.getAngular());
		}
	}

	@DisplayName("this + Vector2D")
	@Nested
	public class OperatorPlusVector2D {

		@DisplayName("operator_plusVector2D_1")
		@Test
		public void operator_plusVector2D_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			AgentMotion o = m1.operator_plus(new Vector2d(x2, y2));

			assertNotNull(o);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());

			assertFpVectorEquals(x1 + x2, y1 + y2, o.getLinear());
			assertEpsilonEquals(r1, o.getAngular());
		}
	}

	@DisplayName("this -= AgentMotion")
	@Nested
	public class OperatorRemoveAgentMotion {

		@DisplayName("operator_removeAgentMotion_1")
		@Test
		public void operator_removeAgentMotion_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_remove(m2);

			assertFpVectorEquals(x1 - x2, y1 - y2, m1.getLinear());
			assertEpsilonEquals(r1 - r2, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("this -= double")
	@Nested
	public class OperatorRemoveDouble {

		@DisplayName("operator_removeDouble_1")
		@Test
		public void operator_removeDouble_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_remove(r2);

			assertFpVectorEquals(x1, y1, m1.getLinear());
			assertEpsilonEquals(r1 - r2, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {

		@DisplayName("operator_removeVector2D_1")
		@Test
		public void operator_removeVector2D_1() {
			double x1 = getRandom().nextDouble();
			double y1 = getRandom().nextDouble();
			double r1 = getRandom().nextDouble();
			double x2 = getRandom().nextDouble();
			double y2 = getRandom().nextDouble();
			double r2 = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(x1, y1, r1);
			AgentMotion m2 = new AgentMotion(x2, y2, r2);

			m1.operator_remove(new Vector2d(x2, y2));

			assertFpVectorEquals(x1 - x2, y1 - y2, m1.getLinear());
			assertEpsilonEquals(r1, m1.getAngular());

			assertFpVectorEquals(x2, y2, m2.getLinear());
			assertEpsilonEquals(r2, m2.getAngular());
		}
	}

	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("setAgentMotion_1")
		@Test
		public void setAgentMotion_1() {
			double x = getRandom().nextDouble();
			double y = getRandom().nextDouble();
			double r = getRandom().nextDouble();
			AgentMotion m1 = new AgentMotion(-15, -16, -17);
			AgentMotion m2 = new AgentMotion(x, y, r);

			m1.set(m2);

			assertFpVectorEquals(x, y, m1.getLinear());
			assertEpsilonEquals(r, m1.getAngular());

			assertFpVectorEquals(x, y, m2.getLinear());
			assertEpsilonEquals(r, m2.getAngular());
		}
	}

	@DisplayName("setAngular")
	@Nested
	public class SetAngular {
		
		@DisplayName("(AgentMotion)")
		@Nested
		public class WithAgentMotion {

			@DisplayName("setAngularAgentMotion_1")
			@Test
			public void setAngularAgentMotion_1() {
				double x = getRandom().nextDouble();
				double y = getRandom().nextDouble();
				double r = getRandom().nextDouble();
				AgentMotion m1 = new AgentMotion(-15, -16, -17);
				AgentMotion m2 = new AgentMotion(x, y, r);

				m1.setAngular(m2);

				assertFpVectorEquals(-15, -16, m1.getLinear());
				assertEpsilonEquals(r, m1.getAngular());

				assertFpVectorEquals(x, y, m2.getLinear());
				assertEpsilonEquals(r, m2.getAngular());
			}
		}

		@DisplayName("(double)")
		@Nested
		public class WithDouble {

			@DisplayName("setAngularDouble_1")
			@Test
			public void setAngularDouble_1() {
				double r = getRandom().nextDouble();
				AgentMotion m = new AgentMotion(-15, -16, -17);

				m.setAngular(r);

				assertFpVectorEquals(-15, -16, m.getLinear());
				assertEpsilonEquals(r, m.getAngular());
			}
		}
	}

	@DisplayName("setLinear")
	@Nested
	public class SetLinear {
		
		@DisplayName("(AgentMotion)")
		@Nested
		public class WithAgentMotion {
			@Test
			public void setLinearAgentMotion() {
				double x = getRandom().nextDouble();
				double y = getRandom().nextDouble();
				double r = getRandom().nextDouble();
				AgentMotion m1 = new AgentMotion(-15, -16, -17);
				AgentMotion m2 = new AgentMotion(x, y, r);
				
				m1.setLinear(m2);
		
				assertFpVectorEquals(x, y, m1.getLinear());
				assertEpsilonEquals(-17, m1.getAngular());
		
				assertFpVectorEquals(x, y, m2.getLinear());
				assertEpsilonEquals(r, m2.getAngular());
			}
		}

		@DisplayName("(double)")
		@Nested
		public class WithDouble {
			@Nested
			public class EqualsTest {
				@Test
				public void setLinearDoubleDouble() {
					double x = getRandom().nextDouble();
					double y = getRandom().nextDouble();
					AgentMotion m = new AgentMotion(-15, -16, -17);
					
					m.setLinear(x, y);
			
					assertFpVectorEquals(x, y, m.getLinear());
					assertEpsilonEquals(-17, m.getAngular());
				}
			}
		}

		@DisplayName("(Vector2D)")
		@Nested
		public class WithVector2D {
			@Nested
			public class EqualsTest {
				@Test
				public void setLinearVector2D() {
					double x = getRandom().nextDouble();
					double y = getRandom().nextDouble();
					AgentMotion m = new AgentMotion(-15, -16, -17);
					
					m.setLinear(new Vector2d(x, y));
			
					assertFpVectorEquals(x, y, m.getLinear());
					assertEpsilonEquals(-17, m.getAngular());
				}
			}
		}
	}

}
