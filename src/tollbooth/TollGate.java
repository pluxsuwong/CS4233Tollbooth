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

package tollbooth;

import tollbooth.gatecontroller.GateController;

/**
 * The TollGate contains everything about a tollgate in a tollbooth.
 * @version Feb 3, 2016
 */
public class TollGate
{
	private boolean isResponding;
	private int numberOfOpens;
	private int numberOfCloses;
	private final int openRetries;
	private final int closeRetries;
	private final int resetRetries;
	private final GateController controller;
	private final SimpleLogger logger;
	
	/**
	 * Constructor that takes the actual gate controller and the logger.
	 * @param controller the GateController object.
	 * @param logger the SimpleLogger object.
	 */
	public TollGate(GateController controller, SimpleLogger logger)
	{
		isResponding = true;
		numberOfOpens = 0;
		numberOfCloses = 0;
		openRetries = 3;
		closeRetries = 3;
		resetRetries = 3;
		this.controller = controller;
		this.logger = logger;
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{
		if (isResponding) {
			for (int i = 0; i < openRetries; i++) {
				try {
					if (!controller.isOpen()) {
						controller.open();
						if (i > 0) {
							LogMessage successMessage = new LogMessage("open: successful");
							logger.accept(successMessage);
						}
						numberOfOpens += 1;
					}
					break;
				} catch (TollboothException e) {
					LogMessage failureMessage = new LogMessage("open: malfunction", e);
					logger.accept(failureMessage);
					if (i == openRetries - 1) {
						isResponding = false;
						failureMessage = new LogMessage("open: unrecoverable malfunction",
								failureMessage.getCause());
						logger.accept(failureMessage);
						throw new TollboothException("open: unrecoverable malfunction",
								failureMessage.getCause());
					}
				}
			}
		} else {
			final LogMessage notRespondingMessage = new LogMessage("open: will not respond");
			logger.accept(notRespondingMessage);
		}
	}
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		if (isResponding) {
			for (int i = 0; i < closeRetries; i++) {
				try {
					if (controller.isOpen()) {
						controller.close();
						if (i > 0) {
							LogMessage successMessage = new LogMessage("close: successful");
							logger.accept(successMessage);
						}
						numberOfCloses += 1;
					}
					break;
				} catch (TollboothException e) {
					LogMessage failureMessage = new LogMessage("close: malfunction", e);
					logger.accept(failureMessage);
					if (i == closeRetries - 1) {
						isResponding = false;
						failureMessage = new LogMessage("close: unrecoverable malfunction",
								failureMessage.getCause());
						logger.accept(failureMessage);
						throw new TollboothException("close: unrecoverable malfunction",
								failureMessage.getCause());
					}
				}
			}
		} else {
			final LogMessage notRespondingMessage = new LogMessage("close: will not respond");
			logger.accept(notRespondingMessage);
		}
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		for (int i = 0; i < closeRetries; i++) {
			try {
				if (controller.isOpen()) {
					controller.reset();
				}
				final LogMessage successMessage = new LogMessage("reset: successful");
				logger.accept(successMessage);
				isResponding = true;
				break;
			} catch (TollboothException e) {
				LogMessage failureMessage = new LogMessage("reset: malfunction", e);
				logger.accept(failureMessage);
				if (i == resetRetries - 1) {
					isResponding = false;
					failureMessage = new LogMessage("reset: unrecoverable malfunction",
							failureMessage.getCause());
					logger.accept(failureMessage);
					throw new TollboothException("reset: unrecoverable malfunction",
							failureMessage.getCause());
				}
			}
		}
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
		if (isResponding) {
			return controller.isOpen();
		} else {
			throw new TollboothException("malfunction: please reset tollbooth");
		}
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		return numberOfOpens;
	}
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		return numberOfCloses;
	}
	
}
