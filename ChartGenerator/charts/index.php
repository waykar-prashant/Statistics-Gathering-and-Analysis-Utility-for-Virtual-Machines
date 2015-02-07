<?php 
require_once("../inc/session.php");
require_once("../inc/db_con.inc.php");
require_once("../inc/functions.php");
include '../db_details.php';

$config=new Config_Details;
$hostname=$config->host;
$username=$config->username;
$password=$config->password;
$db=$config->db;
$con = mysql_connect($hostname,$username,$password);
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db($db, $con);

/*include "../inc/session.php";
	include "../inc/db_con.inc.php";
	include "../inc/functions.php";*/
// START FORM PROCESSING

// START FORM PROCESSING
if(isset($_POST['submitted'])){ // Form has been submitted.


//$ox = GetObj( $r );
//echo "<script type=\"text/javascript\">alert('".$ox->username."')
	$errorstring = " ";
	// perform validations on the form data
	$username = trim(mysql_prep($_POST['username']));
	$password = trim(mysql_prep($_POST['password']));
	
	if(!empty($username)){// Validate the username:
		
	}
	else{
		$username = FALSE;
		$Error = 1;
		$errorstring .= '<p class="error">You forgot to enter your username!</p>';
	}
	
	if(!empty($password)){ // Validate the password:
		
	}
	else{	
		$password = FALSE;
		$Error = 2;
		$errorstring .= '<p class="error">You forgot to enter your password!</p>';
	}
	
	//if($username && $password)	{// If everything's OK.
	if($username && $password){
		//global $link;
		
		//$sql="SELECT * FROM users WHERE username = '".$mfcID."'";

$result = mysql_query("SELECT * FROM `o0pyx_users` WHERE `username`='".$username."' and `usertype`='deprecated'");

	if($row = mysql_fetch_array($result))
	{$r = Query("SELECT * FROM `o0pyx_users` WHERE `username`='".$username."' and `usertype`='deprecated'");
		
			/*$ox = GetObj($r);*/
		$joomla_user = $row['username'];
      
      $pass_array = explode(':',$row['password']);

      $joomla_pass = $pass_array[0];

      $joomla_salt = $pass_array[1];
	  if($joomla_pass == md5($password.$joomla_salt)){

			$_SESSION = GetArrar($r);
			$_SESSION['password'] = $password;
			redirect_to("main.php");
	 }else{// No match was made.
		
			$Error = 3;
			$errorstring .= '<p class="error">Either the username and password entered do not match those in account.</p>';			
		}
		}else{// No match was made.
		
			$Error = 3;
			$errorstring .= '<p class="error">Either the username and password entered do not match those in account.</p>';			
		}
	}else { // If everything wasn't OK.
		$Error = 4;
		$errorstring .= '<p class="error">Please try again.</p>';
	}
	
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include "../inc/admin_header.inc.php"; ?>
<link href="admin.css" rel="stylesheet" type="text/css">
</head>

<body>
<table id="main_body" height="100%" width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FBFBFB">
  <tr>
    <td height="34" colspan="2" class="row2"><table id="header_table" width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
                    
                    <td width="83%"><span class="heading">ADMINISTRATION</span></td>
                    <td width="17%">&nbsp;</td>
            </tr>
       </table></td>
  </tr>
  <tr>
    <td width="35%" align="left" valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="97%">&nbsp;</td>
          <td width="3%">&nbsp;</td>
        </tr>
      </table></td>
    <td align="left" valign="top"><table id="system_login" width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FBFBFB">
        <tr>
          <td class="row4" style="
    background: none;
"><h2>Administration Login</h2> </td>
        </tr>
        <tr>
          <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>Please enter administrator login name and password to access the system.
                  <?php
	if(isset($Error))
	{
		echo $errorstring;
	}

?></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td><form name="form1" method="post" action="<?php echo $_SERVER['PHP_SELF']; ?>">
                <input name="submitted" id="submitted" value="1234" type="hidden" />
                    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="25%" class="FormRow">User Name:</td>
                        <td width="75%" class="FormRow"><input name="username" maxlength="30" type="text" class="TextBox" id="username"></td>
                      </tr>
                      <tr>
                        <td class="FormRow">Password: </td>
                        <td class="FormRow"><input name="password"  maxlength="50" type="password" class="TextBox" id="password"></td>
                      </tr>
                      <tr>
                        <td class="FormRow">&nbsp;</td>
                        <td class="FormRow"><input name="Submit" type="submit" class="INPUT" value="Submit">
                          <input name="Submit2" type="reset" class="INPUT" value="Reset"></td>
                      </tr>
                    </table>
                    
                  </form></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td width="2%">&nbsp;</td>
                <td width="98%">&nbsp;</td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
      </table></td>
  </tr>
</table>
<?php include "../inc/admin_footer.inc.php"; ?>
</body>
</html>
