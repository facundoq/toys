/* TiledZelda, a top-down 2d action-rpg game written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of TiledZelda.

    TiledZelda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TiledZelda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TiledZelda.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.tiledzelda.main.logic;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Dec 26, 2008
 */
public class TimerTest extends TestCase {
	Timer timer;

	protected void setUp() throws Exception {
		timer = new Timer();
	}

	protected void tearDown() throws Exception {
		timer = null;
	}

	public void testCreate() {
		MockNotifiable mockNotifiable = new MockNotifiable();
		timer.notifyMeIn(200, mockNotifiable);
		timer.timePassed(0);
		timer.timePassed(199);
		Assert.assertFalse("Should be not notified yet", mockNotifiable.wasNotified());
		timer.timePassed(200);
		Assert.assertTrue("Should have already be notified", mockNotifiable.wasNotified());
		timer.timePassed(250);

	}

	protected class MockNotifiable implements Notifiable {

		protected boolean wasNotified;

		public MockNotifiable() {
			wasNotified = false;
		}

		public void setWasNotified(boolean notified) {
			this.wasNotified = notified;
		}

		public boolean wasNotified() {
			return wasNotified;
		}

		public void notified() {
			Assert.assertFalse("Was already notified", this.wasNotified());
			wasNotified = true;
		}

	}
}
