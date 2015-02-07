<?php
	$hostname = "ec2-54-69-158-97.us-west-2.compute.amazonaws.com";
	$dbuser = "komal";
	$dbpassword = "123";
	$dbname = "test";

	
	$conn = new mysqli($hostname,$dbuser,$dbpassword, $dbname); 
	// Check connection
	if (mysqli_connect_error()){
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
?>