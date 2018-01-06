/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package personalCode;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateTagsResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;

/**
 * Creates an EC2 instance
 */
public class CreateInstance {
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
	}
}
