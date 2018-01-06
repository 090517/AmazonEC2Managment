package personalCode;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.simpleworkflow.flow.annotations.Wait;

import com.jcraft.jsch.*;

public class test {

	public static void main(String[] args) {

		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withRegion(Regions.US_WEST_1).build();

		String ami_id = "ami-3e68685e";

		/*
		 * runInstancesRequest.withImageId("ami-4b814f22") .withInstanceType("m1.small")
		 * .withMinCount(1) .withMaxCount(1) .withKeyName("my-key-pair")
		 * .withSecurityGroups("my-security-group");
		 */

		RunInstancesRequest run_request = new RunInstancesRequest().withImageId(ami_id)
				.withInstanceType(InstanceType.T2Micro).withMaxCount(1).withMinCount(1).withKeyName("key2").withSecurityGroups("DefaultAllIn");

		RunInstancesResult run_response = ec2.runInstances(run_request);

		String Reservation_id = run_response.getReservation().getReservationId();
		String Instance_id = run_response.getReservation().getInstances().get(0).getInstanceId();

		System.out.println(Reservation_id);
		System.out.println(Instance_id);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DescribeInstancesRequest DescribeRequest = new DescribeInstancesRequest();
		DescribeRequest.withInstanceIds(Instance_id);

		DescribeInstancesResult result = ec2.describeInstances(DescribeRequest);
		result.getReservations().get(0).getInstances().get(0).getPublicIpAddress();

		String DNSAddress = result.getReservations().get(0).getInstances().get(0).getPublicDnsName();
		String PublicIPAddress = result.getReservations().get(0).getInstances().get(0).getPublicIpAddress();
		System.out.println(DNSAddress);
		System.out.println(PublicIPAddress);

		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			JSch jsch = new JSch();
			String user = "ec2-user";
			String host = DNSAddress;
			int port = 22;
			String privateKey = "C:\\Users\\Michael\\OneDrive\\workspace\\Amazon\\Keys\\key2.pem";
			jsch.addIdentity(privateKey);
			Session session = jsch.getSession(user, host, port);

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
			
	        ((ChannelExec)channel).setCommand("java -jar bjTest.Jar");
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);

			// Enable agent-forwarding.
			// ((ChannelShell)channel).setAgentForwarding(true);

			channel.setInputStream(System.in);
			/*
			 * // a hack for MS-DOS prompt on Windows. channel.setInputStream(new
			 * FilterInputStream(System.in){ public int read(byte[] b, int off, int
			 * len)throws IOException{ return in.read(b, off, (len>1024?1024:len)); } });
			 */

			channel.setOutputStream(System.out);

			/*
			 * // Choose the pty-type "vt102". ((ChannelShell)channel).setPtyType("vt102");
			 */

			/*
			 * // Set environment variable "LANG" as "ja_JP.eucJP".
			 * ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
			 */

			// channel.connect();
			channel.connect(3 * 1000);
		} catch (Exception e) {
			System.out.println(e);
		}

		/*
		 * 
		 * StopInstancesRequest SRequest = new
		 * StopInstancesRequest().withInstanceIds(Instance_id);
		 * 
		 * ec2.stopInstances(SRequest);
		 * 
		 * TerminateInstancesRequest TRequest = new
		 * TerminateInstancesRequest().withInstanceIds(Instance_id);
		 * 
		 * ec2.terminateInstances(TRequest);
		 * 
		 */

	}
}
