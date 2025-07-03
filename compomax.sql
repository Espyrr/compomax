CREATE DATABASE  IF NOT EXISTS `compomax` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `compomax`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: compomax
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_boleta`
--

DROP TABLE IF EXISTS `tb_boleta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_boleta` (
  `idboleta` int NOT NULL AUTO_INCREMENT,
  `fch_boleta` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idusuario` int NOT NULL,
  `moneda` enum('SOLES','DOLARES') DEFAULT 'SOLES',
  `subtotal` decimal(10,2) DEFAULT NULL,
  `igv` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`idboleta`),
  KEY `idusuario` (`idusuario`),
  CONSTRAINT `tb_boleta_ibfk_1` FOREIGN KEY (`idusuario`) REFERENCES `tb_usuarios` (`idusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_boleta`
--

LOCK TABLES `tb_boleta` WRITE;
/*!40000 ALTER TABLE `tb_boleta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_boleta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_categorias`
--

DROP TABLE IF EXISTS `tb_categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_categorias` (
  `idcategoria` int NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_categorias`
--

LOCK TABLES `tb_categorias` WRITE;
/*!40000 ALTER TABLE `tb_categorias` DISABLE KEYS */;
INSERT INTO `tb_categorias` VALUES (1,'Procesadores'),(2,'Tarjeta de video'),(3,'Mainboard'),(4,'Memorias Ram'),(5,'Almacenamiento'),(6,'Fuentes de poder'),(7,'Cases'),(8,'Monitores'),(9,'Perifericos PC');
/*!40000 ALTER TABLE `tb_categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_det_boleta`
--

DROP TABLE IF EXISTS `tb_det_boleta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_det_boleta` (
  `iddetalle` int NOT NULL AUTO_INCREMENT,
  `idboleta` int NOT NULL,
  `idproducto` char(5) NOT NULL,
  `cantidad` int NOT NULL,
  `precio` decimal(8,2) NOT NULL,
  `importe` decimal(10,2) GENERATED ALWAYS AS ((`cantidad` * `precio`)) STORED,
  PRIMARY KEY (`iddetalle`),
  KEY `idboleta` (`idboleta`),
  KEY `idproducto` (`idproducto`),
  CONSTRAINT `tb_det_boleta_ibfk_1` FOREIGN KEY (`idboleta`) REFERENCES `tb_boleta` (`idboleta`),
  CONSTRAINT `tb_det_boleta_ibfk_2` FOREIGN KEY (`idproducto`) REFERENCES `tb_productos` (`idprod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_det_boleta`
--

LOCK TABLES `tb_det_boleta` WRITE;
/*!40000 ALTER TABLE `tb_det_boleta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_det_boleta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_estados`
--

DROP TABLE IF EXISTS `tb_estados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_estados` (
  `idestado` int NOT NULL,
  `descripcion` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`idestado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_estados`
--

LOCK TABLES `tb_estados` WRITE;
/*!40000 ALTER TABLE `tb_estados` DISABLE KEYS */;
INSERT INTO `tb_estados` VALUES (1,'Con stock'),(2,'Sin Stock');
/*!40000 ALTER TABLE `tb_estados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_productos`
--

DROP TABLE IF EXISTS `tb_productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_productos` (
  `idprod` char(5) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `precio` decimal(8,2) DEFAULT NULL,
  `idcategoria` int DEFAULT NULL,
  `estado` int DEFAULT NULL,
  PRIMARY KEY (`idprod`),
  KEY `idcategoria` (`idcategoria`),
  CONSTRAINT `tb_productos_ibfk_1` FOREIGN KEY (`idcategoria`) REFERENCES `tb_categorias` (`idcategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_productos`
--

LOCK TABLES `tb_productos` WRITE;
/*!40000 ALTER TABLE `tb_productos` DISABLE KEYS */;
INSERT INTO `tb_productos` VALUES ('P0001','MSI, GEFORCE RTX 3050 8GB GDRR6 VENTUS 2X',12,920.00,2,NULL),('P0002','AMD, RYZEN 5 5500 6N/12H 3.9Ghz',100,344.90,1,NULL),('P0003','MSI, AMD AM4 B550 PRO VDH WIFI',0,340.00,3,NULL),('P0004','MEM. RAM DDR4 8GB 3200MHZ RGB XPG D35',25,89.50,4,NULL),('P0005','MSI SPATIUM M450 M.2 1TB GEN 4X4 NVME 3600MB/s',4,225.00,5,NULL),('P0006','XPG, 650W 80 PLUS BRONZE PYLON ATX',23,230.50,6,1),('P0007','CASE DEEPCOOL, CC360 ARGB WHITE/ MID TOWER SIN FUENTE',84,214.90,7,NULL),('P0008','MONITOR MSI, G244F 24P 170HZ 1MS FAST IPS FREESYNC 110%sRGB',10,550.50,8,NULL),('P0009','KIT GAMING ANTRYX GC-3100 BLACK, TECLADO SWITCH BLUE Y MOUSE',100,149.90,9,1),('P0010','VGA ASUS RTX 4060 DUAL OC 8GB GDDR',200,1312.00,2,1),('P0011','AMD, RYZEN 5 5600 6N/12H 3.9Ghz PCLE 4.0 BULK',6,469.90,1,1),('P0012','ASUS AMD RADEON RX 9070 XT 16GB GDDR6 256BITS TUF GAMING OC ',5,3650.00,2,NULL),('P0013','MSI MAG A650BN 650W 80 PLUS BRONZE',17,249.90,6,1),('P0014','XFX MERCURY AMD RADEON RX 9070 XT 16GB GDDR6 256BITS MAGNETIC RGB OC',7,3530.00,2,NULL),('P0015','ASUS GEFORCE RTX 5090 32GB GDDR7 512BITS ROG ASTRAL OC',3,12639.99,2,NULL),('P0016','INTEL CORE I5-12400F 2.50GHZ/4.40GHZ 18MB LGA1700',4,499.90,1,1),('P0017','FUENTE DE PODER ASUS ROG STRIX 1000G AURA GAMING, 80 PLUS GOLD, FULL MODULAR, 1000W',23,929.90,6,NULL),('P0018','ASUS GEFORCE RTX 5080 16GB GDDR7 256BITS ROG ASTRAL OC',8,6800.00,2,NULL),('P0019','PROCESADOR INTEL CORE ULTRA 9 285K, Cache 36MB, Hasta 5.7 Ghz',21,2599.00,1,NULL),('P0020','MEM. RAM G.SKILL TRIDENT Z5 ROYAL SILVER, INTEL XMP, 32GB (16x2) DDR5 6400 MHZ, CL30',41,725.50,4,NULL),('P0021','MEM. RAM SODIMM KINGSTON FURY IMPACT 16GB DDR5 4800 MHZ.',9,182.00,4,NULL),('P0022','MEM. RAM CORSAIR VENGEANCE LPX, 8GB DDR4 3200 MHz',20,74.60,4,NULL),('P0023','MEM. RAM G.SKILL TRIDENT Z5 NEO RGB BLACK, AMD EXPO, 32GB (16x2) DDR5 6000 MHz. CL30',7,620.89,4,NULL),('P0024','MEM. RAM KINGSTON HYPERX IMPACT SODIMM 16GB DDR4 3200 MHZ.',17,140.50,4,NULL),('P0025','MEM. RAM G.SKILL RIPJAWS S5 BLACK, INTEL XMP, AMD EXPO, 32GB (16x2) DDR5 6000 MHz. CL36',23,443.26,4,NULL),('P0026','MEM. RAM KINGSTON FURY BEAST, 16GB DDR4 3200 MHZ.',14,139.69,4,NULL),('P0027','MEM. RAM CORSAIR VENGEANCE RGB RS, 16GB DDR4 3200 MHz',10,142.84,4,NULL),('P0028','MEM. RAM G.SKILL RIPJAWS M5 RGB BLACK, INTEL XMP, 32GB (16x2) DDR5 6400 MHZ. CL36',5,520.29,4,NULL),('P0029','SSD KINGSTON FURY RENEGADE, 1TB M.2 PCIe 4.0 NVME',6,399.89,5,NULL),('P0030','SSD HIKSEMI FUTURE Lite 512GB M.2 PCIe 4.0 NVME',5,142.78,5,NULL),('P0031','SSD WESTERN DIGITAL GREEN SN350, 1TB M.2 NVME PCIe',3,220.00,5,NULL),('P0032','SSD MSI SPATIUM M560, 1TB M.2 PCIe 5.0 NVMe',20,462.70,5,NULL),('P0033','SSD HP S650 240GB SATA 2.5\"',8,76.50,5,NULL),('P0034','SSD KINGSTON KC3000, 1TB M.2 PCIe 4.0 NVMe',2,386.10,5,NULL),('P0035','SSD KINGSTON KC3000, 2TB M.2 PCIe 4.0 NVMe',13,658.49,5,NULL),('P0036','SSD KINGSTON NV3, 1TB, M.2, 2280, NVMe PCIe 4.0 x4',23,245.00,5,NULL),('P0037','MONITOR GAMING MSI G2422C, 23.8\" CURVO VA, FHD, 180Hz, 1ms.',13,500.47,8,NULL),('P0038','MONITOR XIAOMI G27i 27\" FHD 1920x1080/165HZ/1MS',10,457.90,8,NULL),('P0039','MONITOR XIAOMI G27Qi 27\" RADEON FREESYNC 2K/180HZ/1MS',12,635.14,8,NULL),('P0040','MONITOR SAMSUNG LS32CG552ELXPE, Odyssey G5, 32\" CURVO VA, QHD, 165Hz, 1ms, FreeSync',2,824.50,8,NULL),('P0041','MONITOR LG 24MS500-B, 23.8\" IPS, FHD, 100Hz, 5ms.',17,299.90,8,NULL),('P0042','MONITOR GAMING LG UltraGear 24GS50F-B, 24\" VA, FHD, 180Hz, 1ms, FreeSync',10,416.21,8,NULL),('P0043','MONITOR ANTRYX XTREME IPX2725QGTP, 27\" FAST IPS, 2K, 180Hz, 1ms, G-Sync',15,686.00,8,NULL),('P0044','MONITOR SAMSUNG LS22D300GALXPE, Essential S3, 22\" IPS, FHD, 100Hz, 5ms',12,246.40,8,NULL),('P0045','MONITOR LG 27US500-W, 27\" IPS, UHD 4K, 60Hz, 5ms',29,899.90,8,NULL),('P0046','VGA ASUS PRIME GEFORCE RTX 5070 12GB GDDR7 OC EDITION',7,3024.19,2,NULL),('P0047','VGA MSI GEFORCE RTX 5070 Ti 16GB GDDR7 OC VENTUS 3X',21,4101.77,2,NULL),('P0048','VGA XFX SWIFT RADEON RX 9070 XT WHITE 16GB GDDR6',8,3299.00,2,NULL),('P0049','VGA ASUS PRIME RADEON RX 9060 XT 8GB GDDR6 OC Edition',18,1664.20,2,NULL),('P0050','PROCESADOR AMD RYZEN 5 5600X',13,555.55,1,NULL),('P0051','PROCESADOR AMD RYZEN 3 3200G',20,250.90,1,NULL),('P0052','PROCESADOR INTEL CORE I5-11400F',12,475.12,1,NULL),('P0053','PROCESADOR INTEL CORE I5-12600KF',23,710.37,1,NULL),('P0054','PROCESADOR INTEL CORE i7-12700F',3,1057.00,1,NULL),('P0055','PROCESADOR AMD RYZEN 9 9950X3D, Cache 128 MB, Hasta 5.7 Ghz, AM5',5,3105.83,1,NULL),('P0056','MONITOR XIAOMI G34WQi 34\" CURVO FREESYNC  WQHD /180HZ/1MS/',3,1030.92,8,NULL),('P0057','AURICULARES RAZER KRAKEN V4, INALAMBRICO/BT, BLACK',6,615.91,9,NULL),('P0058','AURICULAR GAMING LOGITECH G535 INALAMBRICO LIGHTSPEED, BLUE',3,360.00,9,NULL),('P0059','KIT TECLADO + MOUSE LOGITECH MK120 USB',18,52.50,9,NULL),('P0060','TECLADO MECANICO LOGITECH PRO RGB BLACK',27,364.90,9,NULL),('P0061','TECLADO GAMING LOGITECH G515 TKL, LIGHTSYNC RGB, WHITE',18,364.90,9,NULL),('P0062','MOUSE GAMING LOGITECH G502 LIGHTSPEED WIRELESS BLACK',19,345.00,9,NULL),('P0063','CASE ASUS TUF GAMING GT501, BLACK, RGB, V/TEMPLADO',13,700.00,7,NULL),('P0064','CASE ASUS TUF GAMING GT502 HORIZON WHITE SIN FUENTE VIDRIO TEMPLADO MID TOWER',11,782.31,7,NULL),('P0065','CASE ASUS TUF GAMING GT502 BLACK SIN FUENTE VIDRIO TEMPLADO MID TOWER',15,782.31,7,NULL),('P0066','PLACA ASUS ROG MAXIMUS Z890 HERO WIFI ATX DDR5 LGA 1851',13,2920.00,3,NULL),('P0067','PLACA ASUS ROG MAXIMUS Z890 HERO ATX DDR5 LGA 1851',3,3086.00,3,NULL),('P0068','PLACA ASUS ROG MAXIMUS Z890 APEX ATX DDR5 LGA 1851',6,3160.00,3,NULL),('P0069','PLACA MSI PRO B550M-P GEN3 M.ATX DDR4 AMD AM4 ',25,315.50,3,NULL),('P0070','PLACA ASUS PRIME H610M-K D4 M.ATX DDR4 LGA 1700',12,290.00,3,NULL),('P0071','FUENTE DE PODER COOLER MASTER V750 V2 750W 80 PLUS GOLD FULL MODULAR',2,531.00,6,NULL),('P0072','FUENTE DE PODER ANTRYX XPII V2 700W 80 PLUS WHITE',3,231.90,6,NULL);
/*!40000 ALTER TABLE `tb_productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_tipos`
--

DROP TABLE IF EXISTS `tb_tipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_tipos` (
  `idtipo` int NOT NULL,
  `descripcion` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`idtipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_tipos`
--

LOCK TABLES `tb_tipos` WRITE;
/*!40000 ALTER TABLE `tb_tipos` DISABLE KEYS */;
INSERT INTO `tb_tipos` VALUES (1,'administrativo'),(2,'cliente');
/*!40000 ALTER TABLE `tb_tipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_usuarios`
--

DROP TABLE IF EXISTS `tb_usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_usuarios` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) DEFAULT NULL,
  `apellido` varchar(25) DEFAULT NULL,
  `usuario` char(45) NOT NULL,
  `clave` char(5) DEFAULT NULL,
  `fnacim` date DEFAULT NULL,
  `tipo` int DEFAULT '2',
  PRIMARY KEY (`idusuario`),
  KEY `tipo` (`tipo`),
  CONSTRAINT `tb_usuarios_ibfk_1` FOREIGN KEY (`tipo`) REFERENCES `tb_tipos` (`idtipo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_usuarios`
--

LOCK TABLES `tb_usuarios` WRITE;
/*!40000 ALTER TABLE `tb_usuarios` DISABLE KEYS */;
INSERT INTO `tb_usuarios` VALUES (1,'Jose','Alvarado','U001@gmail.com','10001','1997-10-20',1),(2,'Pablo','Escobar','U002@gmail.com','10002','2025-06-30',2),(3,'Andres','Hurtado','U003@gmail.com','10003','2025-06-30',2),(4,'Francis','Lopez','U004@hotmail.com','10004','2025-06-30',2),(5,'administrador','juan','admin@compomax.com','10001','2006-03-10',1);
/*!40000 ALTER TABLE `tb_usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-03 14:52:25
