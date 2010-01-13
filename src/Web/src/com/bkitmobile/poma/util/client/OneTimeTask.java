/**
 * 
 */
package com.bkitmobile.poma.util.client;

/**
 * @author CondorHero89
 *
 */
public abstract class OneTimeTask extends Task {

	/**
	 * @param lazyCircle
	 */
	public OneTimeTask(int lazyCircle) {
		super(1, lazyCircle);
	}

	@Override
	public void execute() {
		executeOne();
		this.finish();
	}

	public abstract void executeOne();
}
