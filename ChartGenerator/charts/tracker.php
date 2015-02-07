<?php
include_once('inc/db_con.inc.php');
$status = "";

if(isset($_GET['time_range'])){
	$time_range = $_GET['time_range'];
	$status = $time_range;
}else
	$time_range = "all";

if($time_range=="all")
	$sql = "SELECT * FROM vmstats where vmname='T12_VM001_Ubuntu32' limit 10";
else if($time_range=="hour")
	$sql = "SELECT * FROM vmstats WHERE vmname='T12_VM001_Ubuntu32' AND time_stamp > date_sub(now(), interval 10 hour)";
else if($time_range=="day")
	$sql = "SELECT * FROM vmstats WHERE vmname='T12_VM001_Ubuntu32' AND time_stamp >= curdate()";
else if($time_range=="week")
	$sql = "SELECT * FROM vmstats WHERE vmname='T12_VM001_Ubuntu32' AND time_stamp > date_sub(now(), interval 1 week)";

$result = mysqli_query($conn, $sql);
$data[0] = array('name','counts');		
$ind = 1; 
//get the total number of records
$total_records = mysqli_num_rows($result);
$counter = 0;
if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) {
		if($ind % ceil($total_records/10) == 0){
			$data[++$counter] = array($row['time_stamp'],(int)$row['cpu_usage']);
		}
		$ind++;
    }
} else {
    echo "No Records Found!!";
}
$jsonTable = json_encode($data);
mysqli_close($conn);


?>

   <title>Kometschuh.de Tracker</title>
    <!-- Load jQuery -->
    <script language="javascript" type="text/javascript" 
        src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js">
    </script>
    <!-- Load Google JSAPI -->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
	
		function showCharts(value){
			window.location.href = "tracker.php?time_range="+value;
			document.getElementById('order_status').value = value;
			document.cookie = value;
		}

        google.load("visualization", "1", { packages: ["corechart"] });
        google.setOnLoadCallback(drawChart);

        function drawChart() {
			var data = google.visualization.arrayToDataTable(<?=$jsonTable?>, false); 
			// 'false' means that the first row contains labels, not data.
            var options = {
                title: 'Kometschuh.de Trackerdaten',
				dateFormat: 'dd.MM.yy',
				title: 'CPU USAGE',
				//pointSize: 5,
				hAxis: {
					title: 'Time stamp (hr:min)',
					baselineColor: '#6AB5D1',
					gridlines: {color: "#6AB5D1"},
					textPosition:'out',
					slantedText: true
				}
            };

            var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }

    </script>
<form id="myForm" method="POST">
<select name="order_status" id="order_status" onChange="showCharts(this.value)">

<option>-Select-</option>

<option value="all" <?php if($status=="all") echo " selected" ?>>EVERY 5 min</option>
<option value="hour" <?php if($status=="pending") echo " selected" ?>>HOURLY</option>
<option value="day" <?php if($status=="success") echo " selected" ?>>1 DAY</option>
<option value="week" <?php if($status=="failed") echo " selected" ?>>1 WEEK</option>
</select>&nbsp;&nbsp;    
</form>         
 
    <div id="chart_div" style="width: 900px; height: 500px;">
    </div>