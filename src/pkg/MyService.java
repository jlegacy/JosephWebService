package pkg;

import org.apache.axis2.context.MessageContext;

import com.ibm.as400.access.*;

public class MyService {
	public MyService() {
	}

	public Boolean Configure(String param, String val) {
		return true;
	}

	public Boolean StartMe() {
		return true;
	}

	public Boolean StopMe() {
		return true;
	}

	public String CallAS400PGM(String param) {

		System.out.println("My WebService being invoked");

		AS400 as400System = new AS400("AC4TDAN1", "CGJLEGAC", "Fridayzx6");
		String message = "";

		// The ProgramCall class allows a user to call an iSeries server
		// program,
		// pass parameters to it (input and output), and access data returned in
		// the
		// output parameters after the program runs. Use ProgramCall to call
		// programs.
		ProgramCall program = new ProgramCall(as400System);

		try {
			// Initialize the name of the program to run.
			String programName = "/QSYS.LIB/CGJLEGAC.LIB/TESTWEB.PGM";
			// Set up the 1 parameters.
			ProgramParameter[] parameterList = new ProgramParameter[2];
			
			// Parameter 1 is the First Name
			AS400Text textData = new AS400Text(20, as400System);
			parameterList[0] = new ProgramParameter(textData.toBytes(param));

			parameterList[1] = new ProgramParameter(20);
			// Set the program name and parameter list.
			program.setProgram(programName, parameterList);

			// Run the program.
			if (program.run() != true) {
				// Report failure.
				System.out.println("Program failed!");
				// Show the messages.
				AS400Message[] messageList = program.getMessageList();
				for (int i = 0; i < messageList.length; ++i) {
					// Show each message.
					System.out.println(messageList[i].getText());
					// Load additional message information.
					messageList[i].load();
					// Show help text.
					System.out.println(messageList[i].getHelp());
				}
			}

			// Else no error, get output data.
			else {
				textData = new AS400Text(20, as400System);
				message = (String) textData.toObject(parameterList[1]
						.getOutputData());

			}
		} catch (Exception e) {
			System.out.println("Program " + program.getProgram()
					+ " issued an exception!");
			e.printStackTrace();
		}

		// Done with the server.
		as400System.disconnectAllServices();

		return message;
	}

	public String showRemoteAddress() {
		return (String)(MessageContext.getCurrentMessageContext()).getProperty(MessageContext.REMOTE_ADDR);
	}

}
