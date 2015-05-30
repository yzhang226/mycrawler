-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.20 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 core_crawler 的数据库结构
CREATE DATABASE IF NOT EXISTS `core_crawler` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `core_crawler`;


-- 导出  表 core_crawler.alt_coin 结构
CREATE TABLE IF NOT EXISTS `alt_coin` (
  `id` int(11) NOT NULL,
  `my_topic_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `interest` tinyint(4) NOT NULL,
  `launch_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `publish_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `abbr_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `algo` varchar(50) COLLATE utf8_bin NOT NULL,
  `proof` varchar(50) COLLATE utf8_bin NOT NULL,
  `total_amount` bigint(20) NOT NULL,
  `block_time` int(11) NOT NULL,
  `half_blocks` int(11) NOT NULL,
  `half_days` int(11) NOT NULL,
  `block_reward` double NOT NULL,
  `difficulty_adjust` varchar(50) COLLATE utf8_bin NOT NULL,
  `pre_mined` bigint(20) NOT NULL,
  `mined_percentage` double NOT NULL,
  `pow_days` double NOT NULL,
  `pow_height` int(11) NOT NULL,
  `pow_amount` bigint(20) NOT NULL,
  `memo` varchar(50) COLLATE utf8_bin NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_alt_coin_my_topic` (`my_topic_id`),
  CONSTRAINT `FK_alt_coin_my_topic` FOREIGN KEY (`my_topic_id`) REFERENCES `my_topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 core_crawler.alt_coin_watcher_list 结构
CREATE TABLE IF NOT EXISTS `alt_coin_watcher_list` (
  `id` int(11) NOT NULL,
  `alt_coin_id` int(11) DEFAULT NULL,
  `symbol` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_alt_coin_watcher_list_alt_coin` (`alt_coin_id`),
  CONSTRAINT `FK_alt_coin_watcher_list_alt_coin` FOREIGN KEY (`alt_coin_id`) REFERENCES `alt_coin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 core_crawler.exchange_site 结构
CREATE TABLE IF NOT EXISTS `exchange_site` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `website_url` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `coin_stats_url` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `fee` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 core_crawler.my_topic 结构
CREATE TABLE IF NOT EXISTS `my_topic` (
  `id` int(11) NOT NULL,
  `board_id` mediumint(9) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `author` varchar(50) COLLATE utf8_bin NOT NULL,
  `title` varchar(50) COLLATE utf8_bin NOT NULL,
  `replies` int(11) NOT NULL,
  `views` int(11) NOT NULL,
  `content` blob NOT NULL,
  `last_post_time` int(11) NOT NULL,
  `publish_time` int(11) NOT NULL,
  `create_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 core_crawler.trade_statistics_one_minute 结构
CREATE TABLE IF NOT EXISTS `trade_statistics_one_minute` (
  `item_id` int(11) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `end_time` bigint(20) NOT NULL,
  `open` double DEFAULT NULL,
  `high` double DEFAULT NULL,
  `low` double DEFAULT NULL,
  `close` double DEFAULT NULL,
  `watched_vol` double DEFAULT NULL,
  `exchange_vol` double DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`item_id`,`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 core_crawler.watch_list_item 结构
CREATE TABLE IF NOT EXISTS `watch_list_item` (
  `id` int(11) NOT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `operator` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `watched_symbol` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `exchange_symbol` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `market_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
