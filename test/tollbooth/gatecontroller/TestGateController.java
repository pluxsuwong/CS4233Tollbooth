/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * Description
 * @version Feb 15, 2016
 */
public class TestGateController implements GateController
{
	int openMalfunctions;
	int closeMalfunctions;
	int resetMalfunctions;
	boolean isOpen;
	
	/**
	 * Constructor for the test gate controller, with 3 provided parameters.
	 * @param openMalfunctions the number of open malfunctions to be mocked 
	 * @param closeMalfunctions the number of close malfunctions to be mocked
	 * @param resetMalfunctions whether or not the reset mechanism will malfunction
	 */
	public TestGateController(int openMalfunctions, int closeMalfunctions, int resetMalfunctions)
	{
		this.openMalfunctions = openMalfunctions;
		this.closeMalfunctions = closeMalfunctions;
		isOpen = false;
		this.resetMalfunctions = resetMalfunctions;
	}
	
	/**
	 * Constructor for the test gate controller, with open and close parameters.
	 * @param openMalfunctions the number of open malfunctions to be mocked 
	 * @param closeMalfunctions the number of close malfunctions to be mocked
	 */
	public TestGateController(int openMalfunctions, int closeMalfunctions)
	{
		this.openMalfunctions = openMalfunctions;
		this.closeMalfunctions = closeMalfunctions;
		isOpen = false;
		resetMalfunctions = 0;
	}
	
	/**
	 * Constructor for the test gate controller.
	 */
	public TestGateController()
	{
		openMalfunctions = 0;
		closeMalfunctions = 0;
		isOpen = false;
		resetMalfunctions = 0;
	}
	
	/*
	 * @see tollbooth.gatecontroller.GateController#open()
	 */
	@Override
	public void open() throws TollboothException
	{
		if (openMalfunctions == 0) {
			isOpen = true;
		} else {
			openMalfunctions -= 1;
			throw new TollboothException("open: malfunction");
		}
	}


	/*
	 * @see tollbooth.gatecontroller.GateController#close()
	 */
	@Override
	public void close() throws TollboothException
	{
		if (closeMalfunctions == 0) {
			isOpen = false;
		} else {
			closeMalfunctions -= 1;
			throw new TollboothException("close: malfunction");
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#reset()
	 */
	@Override
	public void reset() throws TollboothException
	{
		if (resetMalfunctions == 0) {
			isOpen = false;
		} else {
			resetMalfunctions -= 1;
			throw new TollboothException("reset: malfunction");
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#isOpen()
	 */
	@Override
	public boolean isOpen() throws TollboothException
	{
		return isOpen;
	}

}
