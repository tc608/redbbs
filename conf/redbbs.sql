-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: db_redbbs
-- ------------------------------------------------------
-- Server version	5.7.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `act_log`
--

DROP TABLE IF EXISTS `act_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `act_log` (
  `logid` int(11) NOT NULL AUTO_INCREMENT COMMENT '[日志id]',
  `cate` int(1) NOT NULL COMMENT '[日志类型]',
  `tid` int(11) DEFAULT NULL COMMENT '[目标数据id]',
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '[用户id]',
  `createTime` bigint(20) DEFAULT NULL COMMENT '[创建时间]',
  `remark` varchar(128) DEFAULT NULL COMMENT '[说明]',
  `status` int(1) DEFAULT '1' COMMENT '[状态]-1删除 1正常',
  PRIMARY KEY (`logid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `act_log`
--

LOCK TABLES `act_log` WRITE;
/*!40000 ALTER TABLE `act_log` DISABLE KEYS */;
INSERT INTO `act_log` VALUES (1,1,13,100003,1512025122957,'',1),(2,1,13,100003,1512025740325,'',1),(3,1,13,100003,1512025795043,'',1),(4,1,13,100003,1512025901312,'',1),(5,1,13,100003,1512025965326,'',1),(6,1,13,100003,1512026040433,'',1),(7,1,2,100003,1512028418702,'',1),(8,1,8,100003,1512028432175,'',1),(9,1,4,100003,1512028464602,'',1),(10,2,3,100003,1512034501549,'',-1),(11,2,2,100003,1512032642934,'',-1),(12,2,1,100003,1512032796896,'',1),(13,1,11,100003,1512032884321,'',1),(14,1,10,100003,1512032895493,'',1),(15,2,8,100003,1512034565868,'',1),(16,1,12,100002,1512045337969,'',1),(17,2,9,100001,1512101602962,'',1),(18,1,111,100001,1512914577786,'',1),(19,1,18,100001,1512915224895,'',1);
/*!40000 ALTER TABLE `act_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `commentId` int(11) NOT NULL AUTO_INCREMENT COMMENT '[评论id]',
  `userId` int(11) NOT NULL COMMENT '[评论用户id]',
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '[评论父id]',
  `cate` int(2) NOT NULL DEFAULT '1' COMMENT '[评论的类型]',
  `contentId` int(11) NOT NULL COMMENT '[被评论内容的id]',
  `content` text COMMENT '[评论内容]',
  `createTime` bigint(20) DEFAULT NULL COMMENT '[创建时间]',
  `supportNum` int(11) DEFAULT '0' COMMENT '[支持数]',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '[状态]1正常，-1删除',
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='[评论表]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,100003,0,0,1,'face[围观] face[围观] ',1511939728047,0,1),(2,100003,0,0,2,'face[嘻嘻] face[可怜] ',1511940602654,1,1),(3,100003,13,0,1,'@nick face[哈哈] ',1511940625623,0,1),(4,100003,13,0,1,'@nickface[白眼] ',1511940683270,1,1),(5,100003,13,0,1,'@nick1111',1511940700471,0,1),(6,100003,0,0,1,'img[/tem/20171129162348.jpg] ',1511943836477,0,1),(7,100003,0,0,2,'img[/tem/20171129163431.jpg] ',1511944482613,0,1),(8,100003,0,0,2,'face[生病] ',1511944517640,1,1),(9,100003,0,0,1,'face[太开心] ',1511944551078,0,1),(10,100003,0,0,1,'face[哈哈] face[哈哈] face[哈哈] ',1511944648519,1,1),(11,100003,0,0,1,'face[哈哈] ',1511944795212,1,1),(12,100002,0,0,1,'gfd d',1512045330899,1,1),(13,100002,12,0,1,'@荣培晓fg hr',1512045347046,0,1),(14,100002,3,0,1,'@nick  htr yt',1512045359531,0,1),(15,100003,7,0,2,'@nick ',1512098913721,0,1),(16,100003,0,0,8,'img[http://img.1216.top/bbs/20171201120132.gif] ',1512100903683,0,1),(17,100003,0,0,2,'face[哈哈] img[/tem/20171203113715.png] ',1512272244158,0,1),(18,100001,0,0,8,'123',1512914569848,1,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content` (
  `contentId` int(11) NOT NULL AUTO_INCREMENT COMMENT '[内容id]',
  `userId` int(11) NOT NULL COMMENT '[用户id]',
  `title` varchar(64) NOT NULL COMMENT '[标题]',
  `digest` varchar(256) DEFAULT '' COMMENT '[摘要]',
  `content` text COMMENT '[内容]',
  `createTime` bigint(20) NOT NULL COMMENT '[创建时间]',
  `cate` int(11) DEFAULT NULL COMMENT '[类别]',
  `type` int(11) NOT NULL COMMENT '[内容栏目]10求助，20分享，30建议，40公告，50动态',
  `replyNum` int(11) NOT NULL DEFAULT '0' COMMENT '[评论数]',
  `viewNum` int(11) NOT NULL DEFAULT '0' COMMENT '[阅读量]',
  `wonderful` int(1) NOT NULL DEFAULT '0' COMMENT '[精] 0否，1是',
  `top` int(1) NOT NULL DEFAULT '0' COMMENT '[置顶]0否，1是',
  `solved` int(11) NOT NULL DEFAULT '0' COMMENT '[结帖]大于0结帖',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '[状态]1结帖 2未结帖 -1删除',
  PRIMARY KEY (`contentId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='[内容表]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
INSERT INTO `content` VALUES (1,100001,'被历史误判！时间还其公道','','          face[嘻嘻] face[亲亲] face[晕]\r\n[pre]\r\nimg[http://img.1216.top/bbs/20171201120729.jpg]\r\n[/pre]',1505031204000,9,50,11,472,0,0,0,1),(2,100001,'故宫武英殿举行赵孟頫书画特展','','img[/tem/20171203231840.jpg]\r\n',1505042514000,2,0,5,137,0,0,0,-1),(3,100001,'Redkale 技术详解 01 -- 双亲委托模型','','        Redkale 里大量使用了双亲委托模型，序列化的ConvertFactory、依赖注入的ResourceFactory、服务管理的WatchFactory均采用双亲委托模型。用于优先加载自定义的处理类，同时也保证两个同级的子Factory不会相互干扰。<br>\r\n',1511682960591,1,0,0,92,1,0,0,-1),(4,100003,'回收木头打造彩色鸟屋 让鸟儿找到栖息之地','','\r\nimg[http://www.shouyihuo.com/uploads/allimg/171128/4_171128095818_1.jpg]\r\n\r\nface[浮云]\r\nimg[http://www.shouyihuo.com/uploads/allimg/171128/4_171128095832_2.jpg]',1511858330764,9,20,0,170,0,0,0,-1),(5,100003,'2017-11-29','','img[/tem/20171129232304.png] ',1511968992737,1,0,0,30,0,0,0,-1),(6,100003,'儿童画','','img[/tem/20171130173239.jpg] ',1512034378751,3,0,0,1,0,0,0,-1),(7,100003,'儿童画','','img[/tem/20171130173239.jpg] ',1512034389038,3,0,0,2,0,0,0,-1),(8,100003,'儿童画-九岁','','img[http://img.1216.top/bbs/20171201120301.jpg] ',1512034430071,9,20,2,115,0,0,0,-1),(9,100001,'全家福-培晓作','','img[http://img.1216.top/bbs/20171201120447.jpg] ',1512054964558,9,0,0,116,1,0,0,-1),(10,100002,'培晓艺术','','\r\nimg[http://img.1216.top/bbs/20171204184735.png] ',1512384522858,9,0,0,36,1,1,0,-1),(11,100001,'社区功能进一步完善，已可以投入使用','','进一步完善，加入 求助/分享/...各个栏目\r\n[pre]\r\n社区基本功能已完成，可以简单的投入使用了\r\n欢迎围观、欢迎注册体验，face[围观]\r\n[/pre]\r\n ',1512921121862,0,50,0,140,0,2,0,1),(12,100002,'阿狸日常','','img/[http://img.1216.top/bbs/20171211003708.jpg]\r\n\r\nimg/[http://img.1216.top/bbs/20171211003725.jpg]\r\n\r\nimg/[http://img.1216.top/bbs/20171211003738.jpg]\r\n\r\nimg/[http://img.1216.top/bbs/20171211003746.jpg] ',1512923899949,0,20,0,129,1,1,0,1),(13,100003,'现在版本nginx支不支持TCP的SSL','','[pre]\n不知道现在版本nginx支不支持TCP的SSL， 如果支持的话那就不考虑实现SSL了\n[/pre]\nimg[http://img.1216.top/redbbs/20180103021052.png] ',1514916271430,0,30,0,10,0,0,0,1),(14,100003,'http协议基本认证 Authorization','','阅读目录\n什么是HTTP基本认证\nHTTP基本认证的过程\nHTTP基本认证的优点\n每次都要进行认证\nHTTP基本认证和HTTPS一起使用就很安全\nHTTP OAuth认证\n其他认证\n客户端的使用\n a(http://blog.csdn.net/u011181633/article/details/43229387)[http协议基本认证 Authorization] \n\n ',1514917144371,0,20,0,8,0,0,0,1),(15,100003,'redkale1.8.9 内存进一步优化','','[pre]\n同样的请求，50线程请求2000次， 1.8.8占内存最高值750M， 1.8.9降到500M\n[/pre]\nimg[http://img.1216.top/redbbs/20180103022314.png] \nimg[http://img.1216.top/redbbs/20180103022323.png] \n【1.8.8】\n\nimg[http://img.1216.top/redbbs/20180103022427.png] \nimg[http://img.1216.top/redbbs/20180103022435.png] \n【1.8.9】',1514917498015,0,50,0,29,1,2,0,1);
/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_item`
--

DROP TABLE IF EXISTS `content_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_item` (
  `itemId` int(11) NOT NULL AUTO_INCREMENT COMMENT '[章节id]',
  `contentId` int(11) NOT NULL COMMENT '[内容id]',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '[状态]',
  PRIMARY KEY (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_item`
--

LOCK TABLES `content_item` WRITE;
/*!40000 ALTER TABLE `content_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `content_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dict`
--

DROP TABLE IF EXISTS `dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pId` int(11) NOT NULL DEFAULT '0' COMMENT '[节点父id]',
  `name` varchar(64) NOT NULL COMMENT '[名称]',
  `code` varchar(64) NOT NULL COMMENT '[code编码]',
  `alias` varchar(64) DEFAULT NULL COMMENT '[别名]',
  `createTime` bigint(20) NOT NULL,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '[状态]1正常，-1删除',
  `type` int(2) NOT NULL COMMENT '[类型]1正常，-1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dict`
--

LOCK TABLES `dict` WRITE;
/*!40000 ALTER TABLE `dict` DISABLE KEYS */;
/*!40000 ALTER TABLE `dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyna_attr`
--

DROP TABLE IF EXISTS `dyna_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyna_attr` (
  `tid` int(11) NOT NULL COMMENT '[目标数据id]',
  `cate` int(11) NOT NULL COMMENT '[类型]1文章, 2xx, 3...,',
  `attr` varchar(32) NOT NULL,
  `value` text COMMENT '[属性值]',
  PRIMARY KEY (`tid`,`cate`,`attr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='[动态属性表]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyna_attr`
--

LOCK TABLES `dyna_attr` WRITE;
/*!40000 ALTER TABLE `dyna_attr` DISABLE KEYS */;
INSERT INTO `dyna_attr` VALUES (1,1,'content','格列柯作品\r\n“矫饰”一词源于意大利语Maniera，意为“风格”，在中文翻译上很容易让人与“矫情”等负面词汇联系到一起，因此很多人对这个画派产生了误解。\r\n事实上，这个画派也的确很长一段时间内在西方美术史上饱受非议，它曾被认为是文艺复兴渐趋衰落后出现的一种故意追求造作形式的画派。直至后来经过很长一段时间的研究，矫饰主义才被客观地界定为是一个全新、独立且有着很大影响力的流派。'),(1,1,'cover','http://upload.art.ifeng.com/2017/0908/thumb_320_200_1504838478153.png'),(2,1,'content','<p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">在文艺复兴和巴洛克风格之间，有一个容易被忽视的流派，他们追求变化和夸张，突破了作品一定要“完美、优雅”的限制，它被称为矫饰主义。</p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>何为矫饰主义</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">14-17世纪的文艺复兴时期，在一般大众心目中，它几乎一度成为了西方艺术的代名词，《蒙娜丽莎》《最后的晚餐》《大卫》……这些名作都出自这个时期，它崇尚的是一种均衡稳定、优雅和谐之美。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909859.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">达·芬奇《最后的晚餐》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">在文艺复兴晚期，画家们对这种理性美感到了“审美疲劳”，且觉得在这种画法上很难再超越文艺复兴的那些大师们，根本无法施展自己的拳脚、展现自己的才华。<strong>于是，他们便有意识地创造新风格，组成了一个新的画派，创作那些惊人的、富有幻</strong><strong>想的作品——“矫</strong><strong>饰主义”就由此出现了。</strong></p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909329.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909217.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>格列柯作品</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">“矫饰”一词源于意大利语Maniera，意为“风格”，在中文翻译上很容易让人与“矫情”等负面词汇联系到一起，因此很多人对这个画派产生了误解。</p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">事实上，这个画派也的确很长一段时间内在西方美术史上饱受非议，它曾被认为是文艺复兴渐趋衰落后出现的一种故意追求造作形式的画派。直至后来经过很长一段时间的研究，矫饰主义才被客观地界定为是一个全新、独立且有着很大影响力的流派。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909334.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">布龙齐诺《托雷多的肖像》，1560年</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">矫饰主义的特征十分明显，主要表现为拉长的人体比例、不平衡的姿势、丰富夸张的表情，以及缺乏清晰的透视。在绘画的色彩上，矫饰主义一改文艺复兴时期的沉闷色调，色彩更加丰富、艳丽，还常常有一些怪异的光影效果。除此之外，在绘画题材上也多是偷窥、阴谋等相对晦涩的话题。</p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>总的来说，矫饰主义就是毫无章法地去创造和追求一种“视觉的惊奇”。</strong></p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909855.jpg\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">帕米贾尼诺《圣保罗的皈依》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>布龙齐诺作品</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">画家帕米贾尼诺的这幅《长颈圣母》可以说是矫饰主义画作的典范之一。在这幅画中，我们依旧可以感受到拉斐尔式圣母的优美，却又有着截然不同的艺术处理。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909500.jpg\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">帕米贾尼诺《长颈圣母》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">画面中人物的肢体都被拉长，即使是圣母手中的婴儿，也被拉到了4、5岁儿童身高的长度。艺术史家贡布里希认为，帕米贾尼诺故意加长了圣母的脖子，以形成优雅的姿态。画家似乎有意阻止我们用日常经验的标准去衡量画中的一切。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909907.jpg\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">帕米贾尼诺《长颈圣母》局部</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">其实，在拉斐尔和米开朗基罗的中后期作品中，我们也可以看到矫饰主义的影子。他们在绘画、雕塑中追求更多的肢体动作上的变化和夸大的情绪反应，这些都与早期的矫饰主义艺术家的作品有着很高的相似之处。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909805.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">米开朗基罗《昼》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">说到矫饰主义，就不得不提到艺术家蓬托尔莫。他于1494年出生在意大利，是一位虔诚的宗教画家，他曾前往佛罗伦萨学画。起初，在他的早期作品中还可以看到达·芬奇、米开朗基罗等大师的影子。在他遗留下来的日记中人们发现，他对自己的工作充满着热情，但是内心呈现的状态却是十分孤独、敏感的。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909281.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">蓬托尔莫《Madonna and Child with the Young St John the Baptist》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">1512年，蓬托尔莫在画家沙托的门下工作时，遇到了画家罗索，由于年龄和兴趣爱好都相仿，二人很快便成为了朋友。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909485.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>蓬托尔莫作品</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">罗索也曾在佛罗伦萨等城市工作，且艺术上受到米开朗基罗很大的影响。但是后来，罗索和蓬托尔莫二人都背离了文艺复兴兴盛时期的艺术原则，共同转向了对矫饰主义作品的创作。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909130.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>罗索作品</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">蓬托尔莫的作品《基督被接下十字架》被誉为矫饰主义发展的核心之作。在这幅作品中，虽然表达的主题十分神圣，但是人物塞满了整个画面，人物的比例被明显拉长，且蓬托尔莫刻意压缩了画面的透视深度。画中人物的神情，每个人的脸上都带有一种焦虑感和紧张感。可以说，整幅作品与古典主义那种稳重、理性的风格完全迥异。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909920.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">蓬托尔莫《基督下十字架》</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">但不可否认的是，蓬托尔莫的画作在色彩和设计上总能给人留下深刻的印象，比如下面这幅著名的油画《在埃及的约瑟夫》。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909994.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">《在埃及的约瑟夫（Joseph in Egypt）》，1515-1518年</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">另外一位被称作现代绘画之父——格列柯，也同样是矫饰主义的代表人物。他的画作以弯曲瘦长的身形为特色，用色怪诞而变幻无常。在作品《天使报喜》中，人物同样遵循了矫饰主义一贯的风格——即身体被拉长。除此之外，人物背后的光影表现也一反传统绘画，明暗对比强烈，使画面充满了神秘的光芒。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909558.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">埃尔·格列柯《天使报喜》，布面油画，66.5×91cm，1595年</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>风格影响</strong></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">矫饰主义风格不仅仅体现在绘画上，作为一种风潮，它在建筑和雕塑中也同样有所体现。文艺复兴时期的建筑讲究秩序和比例，拥有严谨的立面和平面构图以及从古典建筑中继承下来的柱式系统。当时的建筑师们认为，古典柱式构图体现着和谐与理性，并同人体美有着相通之处。</p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909918.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">维琴察圆厅别墅</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">矫饰主义建筑中最典型的一个例子就是位于罗马郊区的法尔尼斯别墅。</p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><strong>我们可以明显地看出，矫饰主义建筑追求的是一种怪诞的效果，不求整体的宏伟和雄壮，且有意地与建筑周围简陋的环境形成鲜明的对比。</strong></p><p style=\"text-indent:0em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;text-align:center;\"><img src=\"http://upload.art.ifeng.com/2017/0908/1504838909505.png\"border=\"0\"alt=\"\"style=\"width:700px;height:auto;\"/></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"font-size:12px;color:#888888;\">法尔尼斯别墅</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\">矫饰主义从16世纪下半叶至17世纪初，总共持续了仅仅半个世纪，无论是与前面的文艺复兴相比，还是与后面的巴洛克风格相比，都好像“差点意思”。<strong>但正是因为有矫饰主义艺术家们对突破和超越的追求，才为巴洛克风格搭建了一个稳固的阶梯。</strong>这世界上，没有绝对的稳定优雅之美，那些“矫揉造作”或许更能带来新的灵感。</p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"color:#FF9900;\">有盐APP给大家送福利啦，现在加入<strong>“有盐1001种生活”微信群</strong>，就可以：</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"color:#FF9900;\">随时抢到DIY、插花、陶艺、音乐、戏剧、亲子等活动优惠券和大红包哦！！</span></p><p style=\"text-indent:2em;font-size:16px;color:#353535;font-family:&quot;background-color:#FFFFFF;\"><span style=\"color:#FF9900;\">第一时间Get各种好玩又不贵的活动！！</span></p></p>'),(2,1,'cover','http://upload.art.ifeng.com/2017/0908/1504837717810.jpg'),(100001,1,'abc','def');
/*!40000 ALTER TABLE `dyna_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `im_group`
--

DROP TABLE IF EXISTS `im_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `im_group` (
  `groupId` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(64) DEFAULT NULL,
  `ownUserId` int(11) NOT NULL COMMENT '[所属用户]',
  `userIds` varchar(256) DEFAULT '' COMMENT '[好友id]',
  PRIMARY KEY (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='[好友分组]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `im_group`
--

LOCK TABLES `im_group` WRITE;
/*!40000 ALTER TABLE `im_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `im_partner`
--

DROP TABLE IF EXISTS `im_partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `im_partner` (
  `partnerId` int(11) NOT NULL AUTO_INCREMENT,
  `partnerName` varchar(32) DEFAULT NULL,
  `ownUserId` int(11) DEFAULT NULL COMMENT '[所属人]',
  `createTime` date DEFAULT NULL,
  `userIds` varchar(256) NOT NULL DEFAULT '' COMMENT '[群成员id]',
  PRIMARY KEY (`partnerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='[群组]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `im_partner`
--

LOCK TABLES `im_partner` WRITE;
/*!40000 ALTER TABLE `im_partner` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `msg`
--

DROP TABLE IF EXISTS `msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `msg` (
  `msgId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `content` text,
  `sendUserid` int(11) DEFAULT NULL,
  `acceptUserid` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `receiveTime` bigint(20) DEFAULT NULL,
  `sendEmail` bigint(20) DEFAULT NULL,
  `sendSms` bigint(20) DEFAULT NULL,
  `sendPush` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '[未发送，已发送，已接收]',
  PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `msg`
--

LOCK TABLES `msg` WRITE;
/*!40000 ALTER TABLE `msg` DISABLE KEYS */;
/*!40000 ALTER TABLE `msg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL COMMENT '[用户id]',
  `username` varchar(32) NOT NULL COMMENT '[登录名]',
  `password` varchar(64) NOT NULL COMMENT '[密码]',
  `sex` int(2) DEFAULT '1' COMMENT '[性别]默认1 1男，2女',
  `phone` varchar(32) NOT NULL COMMENT '[电话号码]',
  `nickname` varchar(64) DEFAULT '' COMMENT '[昵称]',
  `avatar` varchar(128) DEFAULT '' COMMENT '[头像地址]',
  `realname` varchar(32) DEFAULT '' COMMENT '[真实姓名]',
  `email` varchar(32) DEFAULT '' COMMENT '[邮箱]',
  `createTime` bigint(20) NOT NULL COMMENT '[创建时间]',
  `sign` varchar(256) NOT NULL DEFAULT '' COMMENT '[签名]',
  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '[所在城市]',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '[状态]',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (100001,'lxy208@126.com','fcea920f7412b5da7be0cf42b8c93759',1,'18515190967','绝尘','http://img.1216.top/art/u93.png','','lxy208@126.com',1507677533071,'','',1),(100002,'237809797@qq.com','fcea920f7412b5da7be0cf42b8c93759',2,'13121880915','晓','http://img.1216.top/art/u36.png','','237809797@qq.com',1509196823551,'','',1),(100003,'237809796@qq.com','fcea920f7412b5da7be0cf42b8c93759',2,'','nick','http://img.1216.top/bbs/20171203103651.gif','','237809796@qq.com',1511851218332,'态度决定高度。','北京',1),(100004,'vip@qq.com','25d55ad283aa400af464c76d713c07ad',1,'','qq','/res/images/avatar/3.jpg','','vip@qq.com',1512954927558,'','',1),(100005,'12@qq.com','e10adc3949ba59abbe56e057f20f883e',1,'','123','/res/images/avatar/13.jpg','','12@qq.com',1512957064362,'','',1),(100006,'10000@qq.com','0d124d13699173f26a6519631cfe6e52',1,'','na','/res/images/avatar/9.jpg','','10000@qq.com',1512987234013,'','',1),(100007,'syy@qq.com','6fb327cfe8bd8268d7e1a8468d88e8d2',1,'','syy','/res/images/avatar/13.jpg','','syy@qq.com',1513305537321,'','',1),(100008,'555@qq.com','5b1b68a9abf4d2cd155c81a9225fd158',1,'','555','/res/images/avatar/19.jpg','','555@qq.com',1513355350396,'','',1),(100009,'2442669938@qq.com','25f9e794323b453885f5181f1b624d0b',1,'','iLvc','/res/images/avatar/10.jpg','','2442669938@qq.com',1513512637440,'','',1),(100010,'316034712@qq.com','af73fabc0e20d29ce37ad7bb66e7a4ff',1,'','OoxiaobinoO','/res/images/avatar/4.jpg','','316034712@qq.com',1513840912828,'','',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-07  9:18:24
