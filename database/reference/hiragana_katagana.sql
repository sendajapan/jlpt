-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jun 29, 2026 at 03:10 AM
-- Server version: 8.0.46
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jlpt`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hiragana`
--

DROP TABLE IF EXISTS `tbl_hiragana`;
CREATE TABLE IF NOT EXISTS `tbl_hiragana` (
  `id` int NOT NULL AUTO_INCREMENT,
  `furagana` varchar(3) NOT NULL,
  `english` varchar(3) NOT NULL,
  `sound` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `position` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tbl_hiragana`
--

INSERT INTO `tbl_hiragana` (`id`, `furagana`, `english`, `sound`, `position`) VALUES
(1, 'あ', 'A', '', 1),
(2, 'い', 'i', NULL, 2),
(3, 'う', 'u', NULL, 3),
(4, 'え', 'e', NULL, 4),
(5, 'お', 'o', NULL, 5),
(6, 'か', 'ka', NULL, 6),
(7, 'き', 'ki', NULL, 7),
(8, 'く', 'ku', NULL, 8),
(9, 'け', 'ke', NULL, 9),
(10, 'こ', 'ko', NULL, 10),
(11, 'さ', 'sa', NULL, 11),
(12, 'し', 'shi', NULL, 12),
(13, 'す', 'su', NULL, 13),
(14, 'せ', 'se', NULL, 14),
(15, 'そ', 'so', NULL, 15),
(16, 'た', 'ta', NULL, 16),
(17, 'ち', 'chi', NULL, 17),
(18, 'つ', 'tsu', NULL, 18),
(19, 'て', 'te', NULL, 19),
(20, 'と', 'to', NULL, 20),
(21, 'な', 'na', NULL, 21),
(22, 'に', 'ni', NULL, 22),
(23, 'ぬ', 'nu', NULL, 23),
(24, 'ね', 'ne', NULL, 24),
(25, 'の', 'no', NULL, 25),
(26, 'は', 'ha', NULL, 26),
(27, 'ひ', 'hi', NULL, 27),
(28, 'ふ', 'hu', NULL, 28),
(29, 'へ', 'he', NULL, 29),
(30, 'ほ', 'ho', NULL, 30),
(31, 'ま', 'ma', NULL, 31),
(32, 'み', 'mi', NULL, 32),
(33, 'む', 'mu', NULL, 33),
(34, 'め', 'me', NULL, 34),
(35, 'も', 'mo', NULL, 35),
(36, 'や', 'ya', NULL, 36),
(37, 'ゆ', 'yu', NULL, 38),
(38, 'よ', 'yo', NULL, 40),
(39, 'ら', 'ra', NULL, 41),
(40, 'り', 'ri', NULL, 42),
(41, 'る', 'ru', NULL, 43),
(42, 'れ', 're', NULL, 44),
(43, 'ろ', 'ro', NULL, 45),
(44, 'わ', 'wa', NULL, 46),
(45, 'を', 'wo', NULL, 50),
(46, 'ん', 'n', NULL, 51),
(47, 'が', 'ga', NULL, 61),
(48, 'ぎ', 'gi', NULL, 62),
(49, 'ぐ', 'gu', NULL, 63),
(50, 'げ', 'ge', NULL, 64),
(51, 'ご', 'go', NULL, 65),
(52, 'ざ', 'za', NULL, 66),
(53, 'じ', 'ji', NULL, 67),
(54, 'ず', 'zu', NULL, 68),
(55, 'ぜ', 'ze', NULL, 69),
(56, 'ぞ', 'zo', NULL, 70),
(57, 'だ', 'da', NULL, 71),
(58, 'ぢ', 'di', NULL, 72),
(59, 'づ', 'du', NULL, 73),
(60, 'で', 'de', NULL, 74),
(61, 'ど', 'do', NULL, 75),
(62, 'ば', 'ba', NULL, 76),
(63, 'び', 'bi', NULL, 77),
(64, 'ぶ', 'bu', NULL, 78),
(65, 'べ', 'be', NULL, 79),
(66, 'ぼ', 'bo', NULL, 80),
(67, 'ぱ', 'pa', NULL, 81),
(68, 'ぴ', 'pi', NULL, 82),
(69, 'ぷ', 'pu', NULL, 83),
(70, 'ぺ', 'pe', NULL, 84),
(71, 'ぽ', 'po', NULL, 85),
(72, 'きゃ', 'kya', NULL, 101),
(73, 'きゅ', 'kyu', NULL, 102),
(74, 'きょ', 'kyo', NULL, 103),
(75, 'ぎゃ', 'gya', NULL, 106),
(76, 'ぎゅ', 'gyu', NULL, 107),
(77, 'ぎょ', 'gyo', NULL, 108),
(78, 'しゃ', 'sha', NULL, 111),
(79, 'しゅ', 'shu', NULL, 112),
(80, 'しょ', 'sho', NULL, 113),
(81, 'じゃ', 'ja', NULL, 116),
(82, 'じゅ', 'ju', NULL, 117),
(83, 'じょ', 'jo ', NULL, 118),
(84, 'ちゃ', 'cha', NULL, 121),
(85, 'ちゅ', 'chu', NULL, 122),
(86, 'ちょ', 'cho', NULL, 123),
(87, 'にゃ', 'nya', NULL, 126),
(88, 'にゅ', 'nyu', NULL, 127),
(89, 'にょ', 'nyo', NULL, 128),
(90, 'ひゃ', 'hya', NULL, 131),
(91, 'ひゅ', 'hyu', NULL, 132),
(92, 'ひょ', 'hyo', NULL, 133),
(93, 'びゃ', 'bya', NULL, 136),
(94, 'びゅ', 'byu', NULL, 137),
(95, 'びょ', 'byo', NULL, 138),
(96, 'みゃ', 'mya', NULL, 141),
(97, 'みゅ', 'myu', NULL, 142),
(98, 'みょ', 'myo', NULL, 143),
(99, 'ぴゃ', 'pya', NULL, 146),
(100, 'ぴゅ', 'pyu', NULL, 147),
(101, 'ぴょ', 'pyo', NULL, 148),
(102, 'りゃ', 'rya', NULL, 151),
(103, 'りゅ', 'ryu', NULL, 152),
(104, 'りょ', 'ryo', NULL, 153),
(105, 'ア', 'a', NULL, 1),
(106, 'イ', 'i', NULL, 2),
(107, 'ウ', 'u', NULL, 3),
(108, 'エ', 'e', NULL, 4),
(109, 'オ', 'o', NULL, 5),
(110, 'カ', 'ka', NULL, 6),
(111, 'キ', 'ki', NULL, 7),
(112, 'ク', 'ku', NULL, 8),
(113, 'ケ', 'ke', NULL, 9),
(114, 'コ', 'ko', NULL, 10),
(115, 'サ', 'sa', NULL, 11),
(116, 'シ', 'shi', NULL, 12),
(117, 'ス', 'su', NULL, 13),
(118, 'セ', 'se', NULL, 14),
(119, 'ソ', 'so', NULL, 15),
(120, 'タ', 'ta', NULL, 16),
(121, 'チ', 'chi', NULL, 17),
(122, 'ツ', 'tsu', NULL, 18),
(123, 'テ', 'te', NULL, 19),
(124, 'ト', 'to', NULL, 20),
(125, 'ナ', 'na', NULL, 21),
(126, 'ニ', 'ni', NULL, 22),
(127, 'ヌ', 'nu', NULL, 23),
(128, 'ネ', 'ne', NULL, 24),
(129, 'ノ', 'no', NULL, 25),
(130, 'ハ', 'ha', NULL, 26),
(131, 'ヒ', 'hi', NULL, 27),
(132, 'フ', 'hu', NULL, 28),
(133, 'ヘ', 'he', NULL, 29),
(134, 'ホ', 'ho', NULL, 30),
(135, 'マ', 'ma', NULL, 31),
(136, 'ミ', 'mi', NULL, 32),
(137, 'ム', 'mu', NULL, 33),
(138, 'メ', 'me', NULL, 34),
(139, 'モ', 'mo', NULL, 35),
(140, 'ヤ', 'ya', NULL, 36),
(141, 'ユ', 'yu', NULL, 38),
(142, 'ヨ', 'yo', NULL, 40),
(143, 'ラ', 'ra', NULL, 41),
(144, 'リ', 'ri', NULL, 42),
(145, 'ル', 'ru', NULL, 43),
(146, 'レ', 're', NULL, 44),
(147, 'ロ', 'ro', NULL, 45),
(148, 'ワ', 'wa', NULL, 46),
(149, 'ヲ', 'wo', NULL, 50),
(150, 'ン', 'n', NULL, 51),
(151, 'ガ', 'ga', NULL, 61),
(152, 'ギ', 'gi', NULL, 62),
(153, 'グ', 'gu', NULL, 63),
(154, 'ゲ', 'ge', NULL, 64),
(155, 'ゴ', 'go', NULL, 65),
(156, 'ザ', 'za', NULL, 66),
(157, 'ジ', 'ji', NULL, 67),
(158, 'ズ', 'zu', NULL, 68),
(159, 'ゼ', 'ze', NULL, 69),
(160, 'ゾ', 'zo', NULL, 70),
(161, 'ダ', 'da', NULL, 71),
(162, 'ヂ', 'di', NULL, 72),
(163, 'ヅ', 'du', NULL, 73),
(164, 'デ', 'de', NULL, 74),
(165, 'ド', 'do', NULL, 75),
(166, 'バ', 'ba', NULL, 76),
(167, 'ビ', 'bi', NULL, 77),
(168, 'ブ', 'bu', NULL, 78),
(169, 'ベ', 'be', NULL, 79),
(170, 'ボ', 'bo', NULL, 80),
(171, 'パ', 'pa', NULL, 81),
(172, 'ピ', 'pi', NULL, 82),
(173, 'プ', 'pu', NULL, 83),
(174, 'ペ', 'pe', NULL, 84),
(175, 'ポ', 'po', NULL, 85),
(176, 'キャ', 'kya', NULL, 101),
(177, 'キュ', 'kyu', NULL, 102),
(178, 'キョ', 'kyo', NULL, 103),
(179, 'ギャ', 'gya', NULL, 106),
(180, 'ギュ', 'gyu', NULL, 107),
(181, 'ギョ', 'gyo', NULL, 108),
(182, 'シャ', 'sha', NULL, 111),
(183, 'シュ', 'shu', NULL, 112),
(184, 'ショ', 'sho', NULL, 113),
(185, 'ジャ', 'ja', NULL, 116),
(186, 'ジュ', 'ju', NULL, 117),
(187, 'ジョ', 'jo ', NULL, 118),
(188, 'チャ', 'cha', NULL, 121),
(189, 'チュ', 'chu', NULL, 122),
(190, 'チョ', 'cho', NULL, 123),
(191, 'ニャ', 'nya', NULL, 126),
(192, 'ニュ', 'nyu', NULL, 127),
(193, 'ニョ', 'nyo', NULL, 128),
(194, 'ヒヤ', 'hya', NULL, 131),
(195, 'ヒュ', 'hyu', NULL, 132),
(196, 'ヒョ', 'hyo', NULL, 133),
(197, 'ビャ', 'bya', NULL, 136),
(198, 'ビュ', 'byu', NULL, 137),
(199, 'ビョ', 'byo', NULL, 138),
(200, 'ピャ', 'mya', NULL, 141),
(201, 'ピュ', 'myu', NULL, 142),
(202, 'ピョ', 'myo', NULL, 143),
(203, 'ミャ', 'pya', NULL, 146),
(204, 'ミュ', 'pyu', NULL, 147),
(205, 'ミョ', 'pyo', NULL, 148),
(206, 'リャ', 'rya', NULL, 151),
(207, 'リュ', 'ryu', NULL, 152),
(208, 'リョ', 'ryo', NULL, 153);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_katakana`
--

DROP TABLE IF EXISTS `tbl_katakana`;
CREATE TABLE IF NOT EXISTS `tbl_katakana` (
  `id` int NOT NULL AUTO_INCREMENT,
  `furagana` varchar(3) NOT NULL,
  `english` varchar(3) NOT NULL,
  `sound` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `position` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
