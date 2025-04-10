<?php


require 'Zugriffesdaten.php';

$table=TABLE_NAME;
$db_connect = mysqli_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD, DATABASE_NAME) or die(mysql_error());
$sel_data = mysqli_query($db_connect, "SELECT * FROM `$table` ORDER BY `$table`.`id` DESC");

$mysql_row = mysqli_fetch_array($sel_data);
$row_cnt = mysqli_num_rows($sel_data);

$dev_name = $mysql_row["dev_id"];
$datetime = $mysql_row["datetime"];
$gateway = $mysql_row["gtw_id"];
$rssi = $mysql_row["gtw_rssi"];
$temperature = $mysql_row["dev_value_1"];
$humidity = $mysql_row["dev_value_3"];
$battery = $mysql_row["dev_value_4"];
$fenster = $mysql_row["dev_value_5"];
$bewegung = $mysql_row["dev_value_6"];
$licht = $mysql_row["dev_value_7"];
$pressure = $mysql_row["dev_value_8"];

if($fenster == 0){
$fenster ="ist zu";
} else {
$fenster ="ist auf";

}

if($bewegung == 0){
$bewegung ="nein, es gab seit mind 5 min. keine Bewegung";
} else {
$bewegung ="ja, es gab vor mind. 5 min. eine Bewegung";
}

if($licht == 0){
$licht ="ist momentan aus";
} else {
$licht ="ist momentan an";
}







if ($row_cnt > 0) {
    $show_table = "";
} else {
    $show_table = "display: none;";
    echo 'Error: No values in database!';
}

?>
<html>
    <head>
        <style>
            #ttnvalues {
                font-family: Arial, Helvetica, sans-serif;
                border-collapse: collapse;
                width: 1000px;
                text-align: center;
            }

            #ttnvalues td, #ttnvalues th {
                border: 3px solid #ddd;
                padding: 20px;
            }

            #ttnvalues tr:nth-child(even){background-color: #f2f2f2;}

            #ttnvalues tr:hover {background-color: #ddd;}

            #ttnvalues th {
                padding-top: 12px;
                padding-bottom: 12px;
                text-align: center;
                background-color: #6699ff;
                color: white;
            }
        </style>
    </head>
    <body>

        <table id="ttnvalues" style="width:100%">
            <tr>
                <th><?php echo $dev_name; ?></th>
                <th>Value</th>
            </tr>
            <tr>
                <td>Time</td>
                <td><?php echo $datetime; ?></td>
            </tr>
            <tr>
                <td>Fenster</td>
                <td><?php echo $fenster; ?></td>
            </tr>
             <tr>
                <td>Luftdruck</td>
                <td><?php echo $pressure; ?> hPa</td>
            </tr>
             <tr>
                <td>Bewegung</td>
                <td><?php echo $bewegung; ?></td>
            </tr>
             <tr>
                <td>Licht</td>
                <td><?php echo $licht; ?></td>
            </tr>
            <tr>
                <td>Temperatur</td>
                <td><?php echo $temperature; ?> &deg;C</td>
            </tr>
            <tr>
                <td>Feuchtigkeit</td>
                <td><?php echo $humidity; ?> %</td>
            </tr>
            <tr>
                <td>Batterie_Spanung</td>
                <td><?php echo $battery; ?> V</td>
            </tr>
            <tr>
                <td>Gateway</td>
                <td><?php echo $gateway; ?></td>
            </tr>
            <tr>
                <td>Signal_Stärke</td>
                <td><?php echo $rssi; ?> dBm</td>
            </tr>
        </table>
    </body>
</html>