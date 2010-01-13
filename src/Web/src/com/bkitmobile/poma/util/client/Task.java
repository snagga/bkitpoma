package com.bkitmobile.poma.util.client;

public abstract class Task {
	private int clockCircle = 0;
	private int lazyCircle = 0;
	private int lazyCircleCount = 0;
	private boolean pause = false;
	private boolean finish = false;
	
	public Task(int clockCircle){
		this.clockCircle = clockCircle;
	}
	
	public Task(int clockCircle, int lazyCircle){
		this.clockCircle = clockCircle;
		this.lazyCircle = lazyCircle;
	}
	
	public abstract void execute();
	
	public void setClockCircle(int clockCircle) {
		this.clockCircle = clockCircle;
	}
	public int getClockCircle() {
		return clockCircle;
	}

	public void finish() {
		finish = true;
	}

	public void notFinish() {
		finish = false;
	}
	
	public boolean isFinish() {
		return finish;
	}
	
	public void pause() {
		this.pause = true;
	}

	public void resume() {
		this.pause = false;
	}

	/**
	 * @param lazyCircle the lazyCircle to set
	 */
	public void setLazyCircle(int lazyCircle) {
		this.lazyCircle = lazyCircle;
	}

	/**
	 * @return the lazyCircle
	 */
	public int getLazyCircle() {
		return lazyCircle;
	}

	/**
	 * @param lazyCircleCount the lazyCircleCount to set
	 */
	public void setLazyCircleCount(int lazyCircleCount) {
		this.lazyCircleCount = lazyCircleCount;
	}

	/**
	 * @return the lazyCircleCount
	 */
	public int getLazyCircleCount() {
		return lazyCircleCount;
	}

	/**
	 * @return the pause
	 */
	public boolean isPause() {
		return pause;
	}
}
