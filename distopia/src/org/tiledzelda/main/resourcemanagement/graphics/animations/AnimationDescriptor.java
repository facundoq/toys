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

package org.tiledzelda.main.resourcemanagement.graphics.animations;

import java.util.ArrayList;

/**
 * @author Facundo Manuel Quiroga Feb 12, 2009
 */
public class AnimationDescriptor {

	protected ArrayList<AnimationFrameDescriptor> animationFrameDescriptors;
	protected boolean loops;

	public AnimationDescriptor(ArrayList<AnimationFrameDescriptor> animationFrameDescriptors, boolean loops) {
		super();
		this.animationFrameDescriptors = animationFrameDescriptors;
		this.loops = loops;
	}

	protected ArrayList<AnimationFrameDescriptor> getAnimationFrameDescriptors() {
		return this.animationFrameDescriptors;
	}

	protected void setAnimationFrameDescriptors(ArrayList<AnimationFrameDescriptor> animationFrameDescriptors) {
		this.animationFrameDescriptors = animationFrameDescriptors;
	}

	public boolean loops() {
		return this.loops;
	}

	protected void setLoops(boolean loops) {
		this.loops = loops;
	}

}