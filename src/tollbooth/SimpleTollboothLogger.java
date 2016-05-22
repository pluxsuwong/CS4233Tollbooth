/*******************************************************************************
 * This file was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2016 Peerapat Luxsuwong
 *******************************************************************************/

package tollbooth;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The SimpleTollboothLogger is class that was written to allow for
 * instantiation of the SimpleLogger interface.
 * 
 * @version March 20, 2016
 */
public class SimpleTollboothLogger implements SimpleLogger
{

	private final Queue<LogMessage> log;
	
	public SimpleTollboothLogger()
	{
		log = new LinkedList<LogMessage>();
	}
	
	/**
	 * Add the provided message to the LogMessage queue
	 * @param message the descriptive message
	 */
	@Override
	public void accept(LogMessage message)
	{
		log.add(message);
	}

	/**
	 * Remove and return the next message from the LogMessage queue
	 * @return the LogMessage object or null if there is no object in the queue
	 */
	@Override
	public LogMessage getNextMessage()
	{
		final LogMessage nextMessage = log.poll();
		return nextMessage;
	}

}
