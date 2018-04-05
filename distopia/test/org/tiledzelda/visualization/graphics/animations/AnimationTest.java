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

package org.tiledzelda.visualization.graphics.animations;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Feb 24, 2009
 */
public class AnimationTest extends TestCase {

	Image firstImage;
	Image secondImage;
	AnimationFrame firstAnimationFrame;
	AnimationFrame secondAnimationFrame;
	ArrayList<AnimationFrame> animationFrames;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		firstImage = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
		secondImage = new BufferedImage(200, 100, BufferedImage.TYPE_3BYTE_BGR);
		firstAnimationFrame = new AnimationFrame(firstImage, 50);
		secondAnimationFrame = new AnimationFrame(secondImage, 50);
		animationFrames = new ArrayList<AnimationFrame>();
		animationFrames.add(firstAnimationFrame);
		animationFrames.add(secondAnimationFrame);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNew() {
		Animation.newAnimation(firstAnimationFrame, secondAnimationFrame, 100);

	}

	public void testUpdate() {
		Animation animation = Animation.newAnimation(firstAnimationFrame, secondAnimationFrame, 100);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(48);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(49);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(secondImage, animation.getImage());

	}

	public void testUpdateLoopingAnimation() {

		Animation animation = new Animation(animationFrames, 100, true);
		//test first loop
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(48);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(49);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(firstImage, animation.getImage());
		//same test after first loop
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(48);
		Assert.assertEquals(firstImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(49);
		Assert.assertEquals(secondImage, animation.getImage());
		animation.update(1);
		Assert.assertEquals(firstImage, animation.getImage());
	}
}
