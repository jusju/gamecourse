package caveat.object;

public class CaveImage {

	private int xSize;

	private int ySize;

	private int frames;

	private int frameIndex;

	private int frameSpeed; // frames per second

	private double currentFrame;

	public CaveImage(int xSize, int ySize, int frameIndex, int frames) {
		this(xSize, ySize, frameIndex, frames, 0);
	}

	public CaveImage(int xSize, int ySize, int frameIndex, int frames, int framespeed) {
		setxSize(xSize);
		setySize(ySize);
		setFrames(frames);
		setCurrentFrame((int) (Math.random()*frames));
		setFrameIndex(frameIndex);
		setFrameSpeed(framespeed);
	}

	public void setFrames(int frames) {
		this.frames = frames;
	}

	public int getFrames() {
		return frames;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getCurrentFrame() {
		return (int) currentFrame;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getxSize() {
		return xSize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public int getySize() {
		return ySize;
	}

	public void setFrameSpeed(int frameSpeed) {
		this.frameSpeed = frameSpeed;
	}

	public int getFrameSpeed() {
		return frameSpeed;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setNextFrame(long delta) {
		if (frameSpeed != 0) {
			currentFrame += Double.valueOf(delta * frameSpeed) / 1000;
			if ((int) currentFrame >= frames) {
				currentFrame = 0;
			}
		}
	}
}
