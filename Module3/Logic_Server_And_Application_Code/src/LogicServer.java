import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LogicServer {

	public String sendMessage(String message) {

		String HOST = "localhost";
		int PORT = 1337;
		
		//String HOST = "10.1.36.163";
		//int PORT = 8081;


		String result = "";

		try {
			Socket echoSocket = new Socket(HOST, PORT);
			if(echoSocket==null)
				System.out.println("Yes");
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
			out.println(message);
			result = in.readLine();
			echoSocket.close();

		} catch (UnknownHostException e) {
			System.err.println("Host address is not resolved " + HOST);
			result = null;
		} catch (IOException e) {
			//System.err.println("I/O Connection cannot be established with "
					//+ HOST);
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}