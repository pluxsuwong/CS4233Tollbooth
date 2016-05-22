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

import static org.junit.Assert.*;
import org.junit.Test;
import tollbooth.gatecontroller.*;

/**
 * Test cases for the Tollbooth, TollGate class.
 * @version Feb 3, 2016
 */
public class TollboothTest
{

	@Test
	public void createNewTollGateWithNoController()
	{
		assertNotNull(new TollGate(null, null));
	}
	
	@Test
	public void createNewTollGateWithAController()
	{
		assertNotNull(new TollGate(new TestGateController(), null));
	}
	
	@Test
	public void newGateControllerIsClosed() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final TollGate gate = new TollGate(controller, null);
		assertFalse(gate.isOpen());
	}

	@Test
	public void closedGateControllerIsOpenAfterOpenMessage() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
		assertEquals(null, logger.getNextMessage());
	}
	
	@Test
	public void numberOfOpensIncreasesOnCleanOpen() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		gate.open();
		assertEquals(1, gate.getNumberOfOpens());
	}
	
	@Test
	public void openGateControllerNoChangeAfterOpenMessage() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
		gate.open();
		assertEquals(null, logger.getNextMessage());
	}
	
	@Test
	public void numberOfOpensUnchangedOnOpenOnOpen() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		gate.open();
		assertEquals(1, gate.getNumberOfOpens());
		gate.open();
		assertEquals(1, gate.getNumberOfOpens());
	}
	
	@Test
	public void closedGateOpensAfterOneMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(1, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
	@Test(expected=TollboothException.class)
	public void exceptionThrownOnOpenMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(1, 0);
		controller.open();
	}
	
	@Test
	public void exceptionLoggedInMessageOnOpenMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(1, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertTrue(message.hasCause());
		message = logger.getNextMessage();
		assertFalse(message.hasCause());
	}
	
	@Test
	public void numberOfOpensIncreasesOnDirtyOpen() throws TollboothException
	{
		final GateController controller = new TestGateController(1, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		gate.open();
		assertEquals(1, gate.getNumberOfOpens());
	}
	
	@Test
	public void closedGateOpensAfterTwoMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(2, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}

	@Test(expected=TollboothException.class)
	public void openGateThrowsExceptionAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(3, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
	}
	
	@Test
	public void closedGateStopsOpenAttemptsAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(3, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		try {
			gate.open();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			assertEquals("open: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("open: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("open: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("open: unrecoverable malfunction", message.getMessage());
		}
	}
	
	@Test
	public void closedGateIsInWillNotRespondStateAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(3, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		try {
			gate.open();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			assertEquals("open: unrecoverable malfunction", message.getMessage());
			gate.open();
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());
			gate.open();
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());	
		}
	}
	
	@Test
	public void numberOfOpensUnchangedOnFailedOpen() throws TollboothException
	{
		final GateController controller = new TestGateController(3, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		try {
			gate.open();
		} catch (TollboothException e) {
			assertEquals(0, gate.getNumberOfOpens());
			gate.open();
			assertEquals(0, gate.getNumberOfOpens());
		}
	}
	
	//-------------------------------------------------------------------------------
	
	@Test
	public void openGateControllerIsClosedAfterCloseMessage() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
		assertEquals(null, logger.getNextMessage());
		gate.close();
		assertFalse(gate.isOpen());
		assertEquals(null, logger.getNextMessage());
	}

	@Test
	public void numberOfClosesIncreasesOnCleanClose() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertEquals(0, gate.getNumberOfCloses());
		gate.close();
		assertEquals(1, gate.getNumberOfCloses());
	}
	
	@Test
	public void closedGateControllerNoChangeAfterCloseMessage() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
		gate.close();
		assertEquals(null, logger.getNextMessage());
	}
	
	@Test
	public void numberOfClosesUnchangedOnCloseOnClose() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfCloses());
		gate.close();
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	@Test
	public void openGateClosesAfterOneMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 1);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: successful", message.getMessage());
	}
	
	@Test(expected=TollboothException.class)
	public void exceptionThrownOnCloseMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 1);
		controller.open();
		controller.close();
	}
	
	@Test
	public void exceptionLoggedInMessageOnCloseMalfunction() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 1);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertTrue(message.hasCause());
		message = logger.getNextMessage();
		assertFalse(message.hasCause());
	}

	@Test
	public void numberOfClosesIncreasesOnDirtyClose() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 1);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertEquals(0, gate.getNumberOfCloses());
		gate.close();
		assertEquals(1, gate.getNumberOfCloses());
	}
	
	@Test
	public void openGateClosesAfterTwoMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 2);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: successful", message.getMessage());
	}

	@Test(expected=TollboothException.class)
	public void closeGateThrowsExceptionAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		gate.close();
	}
	
	@Test
	public void openGateStopsCloseAttemptsAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		try {
			gate.close();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			assertEquals("close: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("close: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("close: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("close: unrecoverable malfunction", message.getMessage());
		}
	}

	@Test
	public void openGateIsInWillNotRespondStateAfterThreeMalfunctions() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		try {
			gate.close();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			assertEquals("close: unrecoverable malfunction", message.getMessage());
			gate.close();
			message = logger.getNextMessage();
			assertEquals("close: will not respond", message.getMessage());
			gate.close();
			message = logger.getNextMessage();
			assertEquals("close: will not respond", message.getMessage());
		}
	}
	
	@Test
	public void numberOfClosesUnchangedOnFailedClose() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertEquals(0, gate.getNumberOfCloses());
		try {
			gate.close();
		} catch (TollboothException e) {
			assertEquals(0, gate.getNumberOfCloses());
			gate.close();
			assertEquals(0, gate.getNumberOfCloses());
		}
	}
	
	//--------------------------------------------------------------------------------
	
	@Test
	public void resetOpenGate() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
		assertEquals(1, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
		gate.reset();
		assertFalse(gate.isOpen());
		LogMessage message = logger.getNextMessage();
		assertEquals("reset: successful", message.getMessage());
		assertEquals(1, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	@Test
	public void resetClosedGate() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
		gate.reset();
		assertFalse(gate.isOpen());
		LogMessage message = logger.getNextMessage();
		assertEquals("reset: successful", message.getMessage());
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	@Test
	public void resetNonResponsiveOpenGateSuccessful() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
		try {
			gate.close();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			assertEquals("close: unrecoverable malfunction", message.getMessage());
			gate.close();
			message = logger.getNextMessage();
			assertEquals("close: will not respond", message.getMessage());
			gate.open();
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());
			assertEquals(1, gate.getNumberOfOpens());
			assertEquals(0, gate.getNumberOfCloses());
			gate.reset();
			assertFalse(gate.isOpen());
			message = logger.getNextMessage();
			assertEquals("reset: successful", message.getMessage());
			assertEquals(1, gate.getNumberOfOpens());
			assertEquals(0, gate.getNumberOfCloses());
			gate.open();
			assertEquals(2, gate.getNumberOfOpens());
		}
	}
	
	@Test
	public void resetNonResponsiveOpenGateBarelySuccessful() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3, 2);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
		gate.open();
		assertTrue(gate.isOpen());
		try {
			gate.close();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			assertEquals("close: unrecoverable malfunction", message.getMessage());
			gate.close();
			message = logger.getNextMessage();
			assertEquals("close: will not respond", message.getMessage());
			gate.open();
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());
			gate.reset();
			assertFalse(gate.isOpen());
			message = logger.getNextMessage();
			assertEquals("reset: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("reset: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("reset: successful", message.getMessage());
		}
	}
	
	@Test
	public void resetNonResponsiveOpenGateUnsuccessful() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 3, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
		gate.open();
		assertTrue(gate.isOpen());
		try {
			gate.close();
		} catch (TollboothException e) {
			LogMessage message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			message = logger.getNextMessage();
			assertEquals("close: unrecoverable malfunction", message.getMessage());
			gate.close();
			message = logger.getNextMessage();
			assertEquals("close: will not respond", message.getMessage());
			gate.open();
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());
			try {
				gate.reset();
			} catch (TollboothException e2) {
				message = logger.getNextMessage();
				assertEquals("reset: malfunction", message.getMessage());
				message = logger.getNextMessage();
				assertEquals("reset: malfunction", message.getMessage());
				message = logger.getNextMessage();
				assertEquals("reset: malfunction", message.getMessage());
				message = logger.getNextMessage();
				assertEquals("reset: unrecoverable malfunction", message.getMessage());
			}
		}
	}
	
	@Test
	public void badResetOnWorkingGatePutsGateInNotRespondingMode() throws TollboothException
	{
		final GateController controller = new TestGateController(0, 0, 3);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		try {
			gate.reset();
		} catch (TollboothException e) {
			gate.open();
			LogMessage message = logger.getNextMessage();
			assertEquals("reset: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("reset: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("reset: malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("reset: unrecoverable malfunction", message.getMessage());
			message = logger.getNextMessage();
			assertEquals("open: will not respond", message.getMessage());
		}
	}
	
	@Test(expected=TollboothException.class)
	public void isOpenThrowsExceptionWhenGateIsUnresponsive() throws TollboothException
	{
		final GateController controller = new TestGateController(3, 0, 0);
		final SimpleLogger logger = new SimpleTollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		try {
			gate.open();
		} catch (TollboothException e) {
			assertFalse(gate.isOpen());
		}
	}
}
