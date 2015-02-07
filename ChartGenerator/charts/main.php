<?php
	//include "inc/session.php";
	//include "inc/db_con.inc.php";
	//include "inc/functions.php";
	//confirm_logged_in(); 	
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php include "inc/admin_header.inc.php"; ?>
<link href="admin.css" rel="stylesheet" type="text/css">
</head>

<body onload="update_db();">
<table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FBFBFB">
  <tr>
    <td height="34" colspan="2" class="row2"><?php include "inc/header_table.inc.php"; ?></td>
  </tr>
  <tr>
    <td width="18%" align="left" valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="97%"><?php include "inc/admin_menu.inc.php"; ?></td>
          <td width="3%">&nbsp;</td>
        </tr>
      </table></td>
    <td align="left" valign="top"><table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FBFBFB">
        <tr>
          <td class="row4" colspan="2">Welcome To Administration</td>
        </tr>
      
        <!--tr>
          <td colspan="2"  id="noti"><?php// include "get_records.php"; ?></td>
        </tr-->
      </table></td>
  </tr>
</table>
<?php include "inc/admin_footer.inc.php"; ?>
</body>
</html>