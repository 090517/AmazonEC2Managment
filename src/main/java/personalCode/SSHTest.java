package personalCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.*;

public class SSHTest {
	public static void main(String[] arg) {

		try {
			JSch jsch = new JSch();

			String user = "ec2-user";
			String host = "54.183.13.42";
			int port = 22;
			String privateKey = "C:\\Users\\Michael\\OneDrive\\workspace\\Amazon\\Keys\\key2.pem";

			jsch.addIdentity(privateKey);
			System.out.println("identity added ");

			Session session = jsch.getSession(user, host, port);
			System.out.println("session created.");

			// disabling StrictHostKeyChecking may help to make connection but makes it
			// insecure
			// see
			// http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no
			//
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();

			Channel channel = session.openChannel("shell");

			// Enable agent-forwarding.
			// ((ChannelShell)channel).setAgentForwarding(true);


			/*
			channel.setInputStream(System.in);			
			channel.setOutputStream(System.out);
			
			channel.setInputStream(new FileInputStream(new File("C:\\Users\\Michael\\OneDrive\\workspace\\Amazon\\testRun.txt")));
			
			channel.connect(1000);
			
			*/
			
			

	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
			channel.setOutputStream(stream);

	        channel.setInputStream(new FileInputStream(new File("C:\\Users\\Michael\\OneDrive\\workspace\\Amazon\\testRun.txt")));
			channel.connect(1000);

	
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}