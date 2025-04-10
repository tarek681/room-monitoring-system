-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 28. Nov 2020 um 14:55
-- Server-Version: 8.0.21-0ubuntu0.20.04.4
-- PHP-Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `raumueberwachung`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Raum1`
--

CREATE TABLE `Raum1` (
  `id` int NOT NULL,
  `datetime` datetime NOT NULL,
  `app_id` text NOT NULL,
  `dev_id` text NOT NULL,
  `ttn_timestamp` text NOT NULL,
  `gtw_id` text NOT NULL,
  `gtw_rssi` float NOT NULL,
  `gtw_snr` float NOT NULL,
  `dev_raw_payload` text NOT NULL,
  `dev_value_1` float NOT NULL,
  `dev_value_2` float NOT NULL,
  `dev_value_3` float NOT NULL,
  `dev_value_4` float NOT NULL,
  `dev_value_5` float NOT NULL,
  `dev_value_6` float NOT NULL,
  `dev_value_7` float NOT NULL,
  `dev_value_8` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `data`
--
ALTER TABLE `Raum1`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Raum1`
--
ALTER TABLE `Raum1`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
