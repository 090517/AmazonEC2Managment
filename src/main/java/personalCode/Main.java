package personalCode;

import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.RunInstancesRequest;

//Create an ec2 instance, run java program, 

public class Main {
	CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
	csgr.withGroupName("JavaSecurityGroup").withDescription("My security group");
	
	
	
	
	
	RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

	runInstancesRequest.withImageId("ami-4b814f22");
	runInstancesRequest.withInstanceType("m1.small")
	runInstancesRequest.withMinCount(1)
	runInstancesRequest.withMaxCount(1)
	runInstancesRequest.withKeyName("my-key-pair")
	runInstancesRequest.withSecurityGroups("my-security-group");
			
}
