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
import java.util.ArrayList;
import java.util.Random;

import org.tiledzelda.exceptions.GameError;

/**
 * Manages a series of images (frames) and the amount of time to display each frame. Each AnimationFrame knows the percentage of time it will be displayed. The totalDuration specifies how long the
 * entire animation lasts, in milliseconds. The amount of time (in milliseconds) each frame must be displayed is calculated as (percentage * totalDuration ) / 100
 * 
 * @author Facundo Manuel Quiroga
 */

public class Animation {

	protected ArrayList<AnimationFrame> frames;
	protected ArrayList<Long> framesDuration;
	protected int currentFrameIndex;
	protected long currentTime;
	protected long frameTime;
	protected long totalDuration;
	protected boolean loops;

	/**
	 * Convenience factory method for creating an animation with just one frame
	 */
	public static Animation newAnimation(Image image, long duration) {
		ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();
		AnimationFrame animationFrame = new AnimationFrame(image, 100);
		frames.add(animationFrame);
		return new Animation(frames, duration, false);
	}

	/**
	 * Convenience factory method for creating an animation with just two frames
	 */
	public static Animation newAnimation(AnimationFrame animationFrame, AnimationFrame animationFrame2, long duration) {
		ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();
		frames.add(animationFrame);
		frames.add(animationFrame2);
		return new Animation(frames, duration, false);
	}

	public static Animation newAnimation(ArrayList<AnimationFrame> animationFrames, int duration) {
		return new Animation(animationFrames, duration, false);
	}

	/**
	 * Creates a new Animation with totalDuration specified in milliseconds
	 */
	public Animation(ArrayList<AnimationFrame> frames, long totalDuration, boolean loops) {
		if (!framesPercentagesSum100(frames)) {
			throw new IllegalArgumentException("Frames percentages sum should be equal to 100");
		}
		this.frames = frames;
		this.setTotalDuration(totalDuration);
		this.loops = loops;
		reset();

	}

	protected boolean framesPercentagesSum100(ArrayList<AnimationFrame> frames) {
		int sumOfPercentages = 0;
		for (AnimationFrame animationFrame : frames) {
			sumOfPercentages += animationFrame.getPercentage();
		}
		return sumOfPercentages == 100;
	}

	/**
	 * Creates a duplicate of this animation. The list of frames are shared between the two Animations, but each Animation can be animated independently.
	 */
	public Animation clone() {
		return new Animation(new ArrayList<AnimationFrame>(frames), totalDuration, this.shouldLoop());
	}

	/**
	 * Starts this animation over from the beginning.
	 */
	public void reset() {
		this.currentTime = 0;
		this.currentFrameIndex = 0;
		this.frameTime = 0;
	}

	/**
	 * Update the animation, .
	 */
	public void update(long elapsedTime) {
		int numberOfFrames = frames.size();
		if (numberOfFrames > 1) {
			frameTime += elapsedTime;
			currentTime += elapsedTime;
			checkIfFrameTimeEnded();
		}
	}

	protected boolean currentFrameIsLastFrame() {
		return currentFrameIndex == this.getNumberOfFrames() - 1;
	}

	protected void checkIfFrameTimeEnded() {
		// if the animation is not in it's last frame or should loop
		// and if the current frame's time duration has passed, advance to the next frame
		if (!this.currentFrameIsLastFrame() || this.shouldLoop()) {
			long currentFrameDuration = this.getFrameDuration(currentFrameIndex);
			if (frameTime >= currentFrameDuration) {
				frameTime = frameTime - currentFrameDuration;
				currentFrameIndex = (currentFrameIndex + 1) % this.getNumberOfFrames();
				// if the time elapsed was greater than the frame to which we advanced
				// see if we should advance to another frame
				if (frameTime >= this.getFrameDuration(currentFrameIndex)) {
					this.checkIfFrameTimeEnded();
				}
			}
		}
	}

	protected int getNumberOfFrames() {
		return this.frames.size();
	}

	public long getFrameDuration(int frameIndex) {
		return this.framesDuration.get(frameIndex);
	}

	public boolean shouldLoop() {
		return this.loops;
	}

	/**
	 * Gets this Animation's current image. Returns null if this animation has no images.
	 */
	public Image getImage() {
		if (frames.isEmpty()) {
			throw new GameError("Animation - getImage - Empty Frame!");
		}
		return getFrame(currentFrameIndex).image;

	}

	protected AnimationFrame getFrame(int i) {
		return frames.get(i);
	}

	protected long getCurrentTime() {
		return currentTime;
	}

	protected void setCurrentTime(long animTime) {
		this.currentTime = animTime;
	}

	protected int getCurrentFrameIndex() {
		return currentFrameIndex;
	}

	protected void setCurrentFrameIndex(int currFrameIndex) {
		this.currentFrameIndex = currFrameIndex;
	}

	protected ArrayList<AnimationFrame> getFrames() {
		return frames;
	}

	protected void setFrames(ArrayList<AnimationFrame> frames) {
		this.frames = frames;
	}

	protected long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
		this.framesDuration = new ArrayList<Long>();
		long sumOfDurations = 0;
		for (AnimationFrame animationFrame : this.getFrames()) {
			long duration = (animationFrame.getPercentage() * totalDuration) / 100;
			framesDuration.add(duration);
			sumOfDurations += duration;
		}

		// check if the sum of all durations is equal to the totalDuration
		// if so, add the remainder evenly between the all the frames
		// If a part of the remainder cannot be evenly distributed, do it sort-of-randomly. 
		int remainder = (int) (totalDuration - sumOfDurations);
		if (remainder > 0) {

			int numberOfFrames = framesDuration.size();
			int evenlyDistributedTime = remainder / numberOfFrames;
			int unevenRemainder = remainder - (evenlyDistributedTime * numberOfFrames);
			for (int i = 0; i < numberOfFrames; i++) {
				framesDuration.set(i, framesDuration.get(i) + evenlyDistributedTime);
			}
			int index = new Random().nextInt(numberOfFrames);
			while (unevenRemainder > 0) {
				unevenRemainder--;
				framesDuration.set(index, framesDuration.get(index) + 1);
				index = (index + 1) % numberOfFrames;
			}
		}
	}

}
