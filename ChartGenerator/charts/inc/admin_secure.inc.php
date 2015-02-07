<?php

	//	BigBadWolfe
	//	24-12-2004
	//	b0inked !!!
	if(!session_id("ADMIN"))				//!session_is_registered("ADMIN")
	{
		header("location:index.php?Error=2");
		exit;
	}
?>