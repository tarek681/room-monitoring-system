<?php


require 'Zugriffesdaten.php';

$ttn_post = file('php://input');
$data = json_decode($ttn_post[0]);
$Table=TABLE_NAME;

$sensor_bewegung = $data->payload_fields->B;
$sensor_licht = $data->payload_fields->Licht;
$sensor_pressure = $data->payload_fields->pressure;
$sensor_fenster = $data->payload_fields->F;
$sensor_temperature = $data->payload_fields->temperature;
$sensor_humidity = $data->payload_fields->humidity;
$sensor_temperature_2 = $data->payload_fields->temperature;
$sensor_battery = $data->payload_fields->batt;
$sensor_raw_payload = $data->payload_raw;
$gtw_id = $data->metadata->gateways[0]->gtw_id;
$gtw_rssi = $data->metadata->gateways[0]->rssi;
$gtw_snr = $data->metadata->gateways[0]->snr;

$ttn_app_id = $data->app_id;
$ttn_dev_id = $data->dev_id;
$ttn_time = $data->metadata->time;

$db_connect = mysqli_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD, DATABASE_NAME) or die(mysql_error());

$server_datetime = date("Y-m-d H:i:s");

mysqli_query($db_connect, "INSERT INTO `$Table` (`id`, `datetime`, `app_id`, `dev_id`, `ttn_timestamp`, `gtw_id`, `gtw_rssi`,"
        . " `gtw_snr`, `dev_raw_payload`, `dev_value_1`, `dev_value_2`, `dev_value_3`, `dev_value_4`, `dev_value_5`, `dev_value_6`, `dev_value_7`, `dev_value_8`) "
        . "VALUES (NULL, '$server_datetime', '$ttn_app_id', '$ttn_dev_id', '$ttn_time', '$gtw_id', '$gtw_rssi', '$gtw_snr',"
        . " '$sensor_raw_payload', '$sensor_temperature', '$sensor_temperature_2', '$sensor_humidity', '$sensor_battery', '$sensor_fenster', '$sensor_bewegung', '$sensor_licht', '$sensor_pressure');

");

if (WRITE_LOG == true) {
    file_put_contents('log.txt', $ttn_post[0] . PHP_EOL, FILE_APPEND);
}

?>