-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: redbbs
-- ------------------------------------------------------
-- Server version	5.7.21

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `act_log`
--

LOCK TABLES `act_log` WRITE;
/*!40000 ALTER TABLE `act_log` DISABLE KEYS */;
INSERT INTO `act_log` VALUES (1,1,13,100003,1512025122957,'',1),(2,1,13,100003,1512025740325,'',1),(3,1,13,100003,1512025795043,'',1),(4,1,13,100003,1512025901312,'',1),(5,1,13,100003,1512025965326,'',1),(6,1,13,100003,1512026040433,'',1),(7,1,2,100003,1512028418702,'',1),(8,1,8,100003,1512028432175,'',1),(9,1,4,100003,1512028464602,'',1),(10,2,3,100003,1512034501549,'',-1),(11,2,2,100003,1512032642934,'',-1),(12,2,1,100003,1512032796896,'',1),(13,1,11,100003,1512032884321,'',1),(14,1,10,100003,1512032895493,'',1),(15,2,8,100003,1512034565868,'',1),(16,1,12,100002,1512045337969,'',1),(17,2,9,100001,1512101602962,'',1),(18,1,111,100001,1512914577786,'',1),(19,1,18,100001,1512915224895,'',1),(20,1,20,100003,1521166811155,'',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='[评论表]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,100003,0,0,1,'face[围观] face[围观] ',1511939728047,0,1),(2,100003,0,0,2,'face[嘻嘻] face[可怜] ',1511940602654,1,1),(3,100003,13,0,1,'@nick face[哈哈] ',1511940625623,0,1),(4,100003,13,0,1,'@nickface[白眼] ',1511940683270,1,1),(5,100003,13,0,1,'@nick1111',1511940700471,0,1),(6,100003,0,0,1,'img[/tem/20171129162348.jpg] ',1511943836477,0,1),(7,100003,0,0,2,'img[/tem/20171129163431.jpg] ',1511944482613,0,1),(8,100003,0,0,2,'face[生病] ',1511944517640,1,1),(9,100003,0,0,1,'face[太开心] ',1511944551078,0,1),(10,100003,0,0,1,'face[哈哈] face[哈哈] face[哈哈] ',1511944648519,1,1),(11,100003,0,0,1,'face[哈哈] ',1511944795212,1,1),(12,100002,0,0,1,'gfd d',1512045330899,1,1),(13,100002,12,0,1,'@荣培晓fg hr',1512045347046,0,1),(14,100002,3,0,1,'@nick  htr yt',1512045359531,0,1),(15,100003,7,0,2,'@nick ',1512098913721,0,1),(16,100003,0,0,8,'img[http://img.1216.top/bbs/20171201120132.gif] ',1512100903683,0,1),(17,100003,0,0,2,'face[哈哈] img[/tem/20171203113715.png] ',1512272244158,0,1),(18,100001,0,0,8,'123',1512914569848,1,1),(19,100001,0,0,15,'<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/3c/pcmoren_wu_org.png\" alt=\"[污]\" data-w-e=\"1\"><br></p>',1519814792066,0,1),(20,100003,0,0,12,'<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/3c/pcmoren_wu_org.png\" alt=\"[污]\" data-w-e=\"1\"><span style=\"font-weight: bold;\"></span>评论一个<br></p>',1521166738268,1,1),(21,100003,0,0,15,'<p><br></p>',1521210812583,0,1),(22,100003,0,0,15,'<p>&nbsp;</p>',1521211118233,0,1),(23,100003,22,0,15,'<p class=\"at_user\">@nick&nbsp;</p>',1521212557156,0,1),(24,100003,23,0,15,'<p class=\"at_user\">@nick&nbsp;<img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/40/pcmoren_tian_org.png\" alt=\"[舔屏]\" data-w-e=\"1\"></p>',1521212576515,0,1),(25,100003,0,0,18,'<p>查看官方demo里面有，<a href=\"https://gitee.com/redkale/redkale-oss\" target=\"_blank\">https://gitee.com/redkale/redkale-oss</a><br></p>',1524015328722,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='[内容表]';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
INSERT INTO `content` VALUES (1,100001,'被历史误判！时间还其公道','','<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/6a/laugh.gif\" alt=\"[哈哈]\" data-w-e=\"1\"><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[坏笑]\" data-w-e=\"1\"><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/19/heia_org.gif\" alt=\"[偷笑]\" data-w-e=\"1\"><br></p><p><img src=\"http://img.1216.top/bbs/20171201120729.jpg\" style=\"max-width:100%;\"><br></p>',1505031204000,9,50,11,521,0,0,0,-1),(2,100001,'故宫武英殿举行赵孟頫书画特展','','img[/tem/20171203231840.jpg]\r\n',1505042514000,2,0,5,164,0,0,0,-1),(3,100001,'Redkale 技术详解 01 -- 双亲委托模型','','        Redkale 里大量使用了双亲委托模型，序列化的ConvertFactory、依赖注入的ResourceFactory、服务管理的WatchFactory均采用双亲委托模型。用于优先加载自定义的处理类，同时也保证两个同级的子Factory不会相互干扰。<br>\r\n',1511682960591,1,0,0,95,1,0,0,-1),(4,100003,'回收木头打造彩色鸟屋 让鸟儿找到栖息之地','','\r\nimg[http://www.shouyihuo.com/uploads/allimg/171128/4_171128095818_1.jpg]\r\n\r\nface[浮云]\r\nimg[http://www.shouyihuo.com/uploads/allimg/171128/4_171128095832_2.jpg]',1511858330764,9,20,0,170,0,0,0,-1),(5,100003,'2017-11-29','','img[/tem/20171129232304.png] ',1511968992737,1,0,0,30,0,0,0,-1),(6,100003,'儿童画','','img[/tem/20171130173239.jpg] ',1512034378751,3,0,0,1,0,0,0,-1),(7,100003,'儿童画','','img[/tem/20171130173239.jpg] ',1512034389038,3,0,0,2,0,0,0,-1),(8,100003,'儿童画-九岁','','img[http://img.1216.top/bbs/20171201120301.jpg] ',1512034430071,9,20,2,145,0,0,0,-1),(9,100001,'全家福-培晓作','','img[http://img.1216.top/bbs/20171201120447.jpg] ',1512054964558,9,0,0,124,1,0,0,-1),(10,100002,'培晓艺术','','\r\nimg[http://img.1216.top/bbs/20171204184735.png] ',1512384522858,9,0,0,39,1,1,0,-1),(11,100001,'社区功能进一步完善，已可以投入使用','','<blockquote><b>\n                                        进一步完善，加入 求助/分享/...各个栏目\n[pre]\n社区基本功能已完成，可以简单的投入使用了\n欢迎围观、欢迎注册体验</b>，<img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/58/mb_org.gif\" alt=\"[太开心]\" data-w-e=\"1\"></blockquote><p><br></p>',1512921121862,0,50,0,201,0,0,0,1),(12,100002,'阿狸日常','','<p><img src=\"http://img.1216.top/bbs/20171211003708.jpg\" style=\"max-width:100%;\"><br></p><p><img src=\"http://img.1216.top/bbs/20171211003725.jpg\" style=\"max-width:100%;\"><br></p><p><img src=\"http://img.1216.top/bbs/20171211003738.jpg\" style=\"max-width:100%;\"><br></p><p>&nbsp;<img src=\"http://img.1216.top/bbs/20171211003746.jpg\" style=\"max-width: 100%;\"></p>',1512923899949,0,20,1,183,1,0,0,1),(13,100003,'现在版本nginx支不支持TCP的SSL','','<blockquote><b>不知道现在版本nginx支不支持TCP的SSL， 如果支持的话那就不考虑实现SSL了&nbsp;&nbsp;</b></blockquote><p><img src=\"http://img.1216.top/redbbs/20180103021052.png\" style=\"max-width:100%;\"><br></p><p><br></p>',1514916271430,0,30,0,55,0,0,0,1),(14,100003,'http协议基本认证 Authorization','','<p><b>阅读目录</b><br>什么是HTTP基本认证<br>HTTP基本认证的过程<br>HTTP基本认证的优点<br>每次都要进行认证<br>HTTP基本认证和HTTPS一起使用就很安全<br>HTTP OAuth认证<br>其他认证<br>客户端的使用<br>&nbsp;<a href=\"http://blog.csdn.net/u011181633/article/details/43229387\" target=\"_blank\" rel=\"nofollow\">&gt;&gt;&gt;http协议基本认证 Authorization</a>&nbsp;<br></p>',1514917144371,0,20,0,53,0,0,0,1),(15,100003,'redkale1.8.9 内存进一步优化','','<p>\n                                        </p><p>\n                                        </p><h2>同样的请求，50线程请求2000次， <b>redkale</b>1.8.8占内存最高值750M， 1.8.9降到500M&nbsp;&nbsp;</h2><p><b>【1.8.8】</b>&nbsp;&nbsp;<br></p><p><img src=\"http://img.1216.top/redbbs/20180103022314.png\" style=\"max-width:100%;\"><br></p><p><img src=\"http://img.1216.top/redbbs/20180103022323.png\" style=\"max-width:100%;\"><br></p><p><b>&nbsp;&nbsp;【1.8.9】</b><br></p><p><img src=\"http://img.1216.top/redbbs/20180103022427.png\" style=\"max-width:100%;\"><b><br></b></p><p><img src=\"http://img.1216.top/redbbs/20180103022435.png\" style=\"max-width:100%;\"><br></p><p><br></p>',1514917498015,0,20,5,134,1,0,0,1),(16,100003,'系统思维--读书笔记','','<p><span style=\"font-weight: bold;\">《系统架构》 </span><span style=\"text-decoration-line: underline;\">复杂系统的产品设计与开发</span></p><p>-----------------------------------------</p><p><span style=\"color: rgb(77, 128, 191); font-weight: bold;\">什么是系统思维？</span></p><p>&nbsp;&nbsp;&nbsp;&nbsp;简单说，就是把某个疑问、状态、难题等明确的视为一个系统，是为一组相互关联的实体。（与 批判、分析、创新思维&nbsp; 思维模式并列）</p><p><span style=\"color: rgb(77, 128, 191); font-weight: bold;\">如果完成一次系统思维的过程？</span></p><p></p><ol><li>&nbsp; &nbsp; 1、确定<span style=\"color: rgb(139, 170, 74);\">系统</span>及其形式与功能<br></li><li>&nbsp; &nbsp; 2、确定系统中的<span style=\"color: rgb(139, 170, 74);\">实体</span> 及其形式与功能，以及<span style=\"color: rgb(139, 170, 74);\">系统所处的环境</span> 和 <span style=\"color: rgb(139, 170, 74);\">系统的边界</span><br></li><li>&nbsp; &nbsp; 3、确定各个<span style=\"background-color: rgb(255, 255, 255); color: rgb(139, 170, 74);\">实体间的关系</span>和<span style=\"background-color: rgb(255, 255, 255);\">位于<span style=\"color: rgb(139, 170, 74);\">边界处</span>的关系</span>，并确定这些<span style=\"color: rgb(139, 170, 74); background-color: rgb(255, 255, 255);\">关系的形式和功能</span><br></li><li>&nbsp; &nbsp; 4、确定系统的<span style=\"color: rgb(139, 170, 74);\">涌现属性</span><br></li></ol><p></p><p><span style=\"font-weight: bold; color: rgb(77, 128, 191);\">系统的定义：</span></p><p>&nbsp; &nbsp; 系统是由一组实体和这些实体之间的关系所构成的集合，其功能要大于这些实体各自功能之和。<span style=\"font-weight: bold; color: rgb(77, 128, 191);\"><br></span></p><p><span style=\"font-weight: bold; color: rgb(77, 128, 191);\">架构定义：</span><span style=\"font-weight: bold; color: rgb(77, 128, 191);\"><br></span></p><p>&nbsp; &nbsp; 对系统中的实体和实体间的关系所进行的抽象描述<span style=\"font-weight: bold; color: rgb(77, 128, 191);\"><br></span></p><p><span style=\"color: rgb(77, 128, 191);\"><span style=\"font-weight: bold;\">涌现是什么</span><span style=\"font-weight: bold;\">？</span></span></p><p>&nbsp; &nbsp; -系统运作时所表现、呈现或浮现出的东西<span style=\"color: rgb(77, 128, 191);\"><span style=\"font-weight: bold;\"><br></span></span></p><p>&nbsp; &nbsp; -各实体拼合成一个系统时，实体间的交互会把功能、行为、性能和其他内在属性涌现出来；系统的价值来源于涌现物的赋予，能够涌现预期属性的系统是成功的系统，反之亦然；另外除了<span style=\"background-color: rgb(139, 170, 74);\">性能</span>外系统涌现涌现的属性包括：<span style=\"background-color: rgb(139, 170, 74);\">可靠性、可维护性、可操作性、安全性、健壮性</span>等；</p><p><span style=\"color: rgb(77, 128, 191);\"><span style=\"font-weight: bold;\">涌现原则：</span></span></p><p>&nbsp; &nbsp; 整体大于其各部分之和。 ——亚里士多德</p><p><br></p><p>。。。。未完</p><p><span style=\"font-weight: bold; color: rgb(77, 128, 191);\"><br></span></p><p><br></p>',1522378656136,0,20,0,56,0,0,0,1),(17,100013,'Idea在已经存在的mavn项目建立子模块遇到的问题','','<p>idea 提示：\'F:/workspace/xxxx/pom.xml\' already exists in VFS</p><p>尝试方法：</p><p>&nbsp; &nbsp; 1，查看A项目下是否已经xxx.xml或xxx.iml文件，将其删除<br></p><p>&nbsp; &nbsp; 2，打开Project Structure 查看是否已经存在其子模块，将其删除<br></p><p>&nbsp; &nbsp; 3，点击项目右键，选择Show in Exploer 在本地文件夹里打开，查看是否存在其子模块文件夹，将其删除。<br></p><p>&nbsp; &nbsp; 4，点击File 选择 Invalidate Caches /Restarts... 选项，清理一缓存。<br></p><p>&nbsp;&nbsp;<br></p><p>导致问题原因：</p><p>&nbsp; &nbsp; 可能由于在建立 了模块时，选择了与其父模块一样的文件夹导致。<br></p><p><img src=\"http://img.1216.top/redbbs/20180412111841.png\" style=\"max-width:100%;\"><br></p>',1523503127379,0,20,0,59,0,0,0,1),(18,100014,'redkale处理session','','<p>redkale在作为http服务器使用时，是如何处理session这块内容的？还是采用token方式验证，去掉session呢</p>',1523549060544,0,10,1,57,0,0,0,1),(19,100013,'maven将某个子模块下的资源移到到其它子模块下','','<p>描述：</p><p>&nbsp; &nbsp; 如何使用maven将A模块下的资源文件在打包时复制到B模块下。<br></p><p>配置信息：</p><p>&nbsp; &nbsp; 需要在B模块下的pom.xml文件配置如下信息：</p><pre><code>&lt;build&gt;<br>        &lt;resources&gt;<br>            &lt;resource&gt;<br>                &lt;directory&gt;../A模块项目名称/src/main/resources&lt;/directory&gt;<br>                &lt;filtering&gt;true&lt;/filtering&gt;<br>                &lt;includes&gt; &lt;!-- 包含的文件 --&gt;<br>                    &lt;include&gt;*&lt;/include&gt;<br>                    &lt;include&gt;*/*&lt;/include&gt;<br>                &lt;/includes&gt;<br>                &lt;excludes&gt;&lt;!-- 排除某些文件 --&gt;<br>                    &lt;exclude&gt;*.properties&lt;/exclude&gt;<br>                &lt;/excludes&gt;<br>            &lt;/resource&gt;<br>        &lt;/resources&gt;<br>    &lt;/build&gt;</code></pre><p><br></p>',1523841741041,0,20,0,49,0,0,0,1),(20,100013,'vmware中启动FreeDos进入BIOS系统','','<p>找到FreeDos系统在硬盘上的虚拟文件的目录。修改后辍为.vmxf配置文件。添加如下内容：</p><p><span style=\"font-weight: bold;\">bios.forceSetupOnce = \"TRUE\"&nbsp;</span></p><p><span style=\"font-weight: bold;\">bios.bootDelay = \"10000\"&nbsp;</span></p><p>保存重启。<span style=\"font-weight: bold;\">注：</span>每次启动完后，添加得这两个配置会消失，需要重新添加。</p>',1524128820960,0,20,0,50,0,0,0,1),(21,100013,'MongoDB提示：not master and slaveOk=false','','<div yne-bulb-block=\"paragraph\"><span style=\"color: rgb(194, 79, 74);\">出现的原因：mongo集群，你连接到了某一个SECONDARY主机上，然后此主机没有读取权限导致的。</span></div><blockquote><span style=\"color: rgb(70, 172, 200); font-weight: bold;\">解决办法：</span><br>1、在mongo shell中执行<code>rs.slaveOk()</code> 来获取读取的权限。https://docs.mongodb.com/manual/reference/method/rs.slaveOk/<br>2、在连接时使用指定PRIMARY主机（使用集群方式连接）。如果使用MongoDB管理软件请查看是否有相关配置信息。如果使用命令行方式可以如下命令格式进行连接：<br><code>mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]<br></code>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<br>如：<code>mongo mongodb://192.168.176.133:27018,192.168.176.135:27017/database?replicaSet=repset2<br></code><span style=\"font-family: monospace;\"><br><br></span>其中：192.168.176.133和192.168.176.135这两个ip是SECONDARY主机地址，连接后会自动切换到PRIMARY主机上（在连接时会自动查询配置信息，然后连接到PRIMARY主机上）replicaSet是集群名。</blockquote><p></p><p>链接地址：https://docs.mongodb.com/manual/reference/connection-string/index.html</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <span style=\"color: rgb(194, 79, 74);\">注：如果在连接过程中出现host2或某个域名出现未知主机异常<code>UnknownHostException</code>时。请确定此集群在配置时进是否使用主机名映射。也就是否需要在本地修改hosts文件来添加映射关系。</span></p><p></p><p>&nbsp;</p><p></p><p>&nbsp; &nbsp;</p><p></p><p><br></p>',1526022672039,0,20,0,43,0,0,0,1),(22,100001,'社区功能更新记录--社区建议评论此贴','','<p><br></p><pre><code><span style=\"background-color: rgb(238, 236, 224); color: rgb(139, 170, 74);\">2018-06-12更新</span><br>1. 用户资料加入[个人博客地址、码云/GitHub地址]（部分用户开放设置）<br>2. 加入用户信息接口UF/UI,优化用户信息设置代码</code></pre><p><br></p><pre><code><span style=\"background-color: rgb(238, 236, 224); color: rgb(139, 170, 74);\">2018-06-10更新</span><br>1. 个人中心-收藏数据不对 <br>2. 管理员查询不到用户未公开帖子<br>3. 注册/资料修改 成功后未提示<br>4. 首页帖子用户头像链接地址错误</code></pre><p><br></p><pre><code><span style=\"font-weight: bold; background-color: rgb(238, 236, 224); color: rgb(139, 170, 74);\">2018-05-12更新</span><br>1. 文章编辑加入未保存浏览器缓存<br>2. 文章编辑标题加入长度检查</code></pre><p><br></p>',1526140381293,0,30,0,133,1,3,0,1),(23,100013,'maven web工程添加本地类库的方式','','<p>方式一：</p><p>&nbsp; &nbsp; 在webapp/WEB-INF目录下建立 一个lib文件，直接将jar复制到里面。然后修改pom.xml文件。<br></p><pre><code>&lt;dependency&gt;  \n    &lt;groupId&gt;com.alipay.api&lt;/groupId&gt;  \n    &lt;artifactId&gt;alipay-sdk-java20170324180803&lt;/artifactId&gt;<br>    &lt;version&gt;20170324180803&lt;/version&gt;<br>    &lt;scope&gt;system&lt;/scope&gt;<br>    &lt;systemPath&gt;${project.basedir}/src/main/webapp/WEB-INF/lib/alipay-sdk-java20170324180803.jar&lt;/systemPath&gt;<br>&lt;/dependency&gt;</code></pre><p>其中<span style=\"font-weight: bold;\">&lt;scope&gt;</span>和<span style=\"font-weight: bold;\">&lt;systemPath&gt;</span>标签最为重要,&lt;scope&gt;其值必须为<span style=\"font-weight: bold;\">system</span>,&lt;systemPath&gt;为jar的位置。其它标签内容可自行定义。</p><p>方式二：使用maven-war-plugin插件</p><pre><code><p>&lt;dependency&gt;  <br>    &lt;groupId&gt;com.alipay.api&lt;/groupId&gt;  <br>    &lt;artifactId&gt;alipay-sdk-java20170324180803&lt;/artifactId&gt;<br>    &lt;version&gt;20170324180803&lt;/version&gt;<br>    &lt;scope&gt;system&lt;/scope&gt;<br>    &lt;systemPath&gt;${project.basedir}/local-lib/alipay-sdk-java20170324180803.jar&lt;/systemPath&gt;<br>&lt;/dependency&gt;</p><p>&lt;plugin&gt;<br>    &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;<br>    &lt;artifactId&gt;maven-war-plugin&lt;/artifactId&gt;<br>    &lt;version&gt;2.3&lt;/version&gt;<br>    &lt;configuration&gt;<br>    &lt;warName&gt;${project.artifactId}&lt;/warName&gt;<br>    &lt;webResources&gt;<br>        &lt;resource&gt;<br>            &lt;directory&gt;local-lib/&lt;/directory&gt;<br>            &lt;targetPath&gt;WEB-INF/lib&lt;/targetPath&gt;<br>            &lt;includes&gt;<br>                &lt;include&gt;**/*.jar&lt;/include&gt;<br>            &lt;/includes&gt;<br>        &lt;/resource&gt;<br>    &lt;/webResources&gt;<br>    &lt;/configuration&gt;<br>&lt;/plugin&gt;</p></code></pre><p>注：local-lib文件夹是在项目的根目录建立的。</p><p><br></p><p>方式三：使用resource标签，将jar移动到指定的目录</p><pre><code><p>&lt;dependency&gt;  <br>    &lt;groupId&gt;com.alipay.api&lt;/groupId&gt;  <br>    &lt;artifactId&gt;alipay-sdk-java20170324180803&lt;/artifactId&gt;<br>    &lt;version&gt;20170324180803&lt;/version&gt;<br>    &lt;scope&gt;system&lt;/scope&gt;<br>    &lt;systemPath&gt;${project.basedir}/local-lib/alipay-sdk-java20170324180803.jar&lt;/systemPath&gt;<br>&lt;/dependency&gt;</p>&lt;resource&gt;<br>    &lt;directory&gt;${basedir}/local-lib&lt;/directory&gt;<br>    &lt;includes&gt;<br>        &lt;include&gt;*.jar&lt;/include&gt;<br>    &lt;/includes&gt;<br>    &lt;targetPath&gt;${build.directory}/${build.finalName}/WEB-INF/lib&lt;/targetPath&gt;<br>&lt;/resource&gt;</code></pre><p>注：其中<span style=\"font-weight: bold;\">&lt;targetPath&gt;</span>标签内容占主要。也需要将方式一中的&lt;dependency&gt;标签内容添加到pom中。</p><p>说明：${变量名}这种是mave内置的一些变量。直接使用即可。其主要目标就是将jar移动到编译后的WEB-INF/lib目录下即可</p>',1527130015404,0,20,0,18,0,0,0,1),(24,100013,'mavn多工程只打包某一个工程','','<p>命令：</p><pre><code>maven package -DskipTests -pl sub-model-proejct -ad</code></pre><p>说明：</p><p>&nbsp; &nbsp; 1，执行命令的目录为父目录。<br></p><p>&nbsp; &nbsp; 2，选项说明<br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -DskipTests：跳过测试<br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -pl：构建指定的反应堆项目而不是所有项目,后期跟项目名。也就是要构建项目中的pom文件中的<span style=\"font-weight: bold;\">&lt;artifactId&gt;</span>标签中的内容</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -am：如果指定了项目列表，还可以建立列表所需的项目（就是如果项目依赖于其它子项目也会一起构建）<br></p><p>&nbsp; &nbsp; &nbsp;3，maven版本在2.1版本以上。<br></p><p>&nbsp; &nbsp; &nbsp;4，maven相关更多命令可以查看mvn --help或到官网查看。<br></p><p>&nbsp; &nbsp; &nbsp;5，如果使用mvn package&nbsp;命令来打包如果某一个项目模块下出现问题后，其实同级模块也无法进行打包。而这种方式只有在其依赖的项目模块出现问题后才会失败。<br></p><p>&nbsp; &nbsp;</p><p><br></p>',1527130651108,0,20,0,22,0,1,0,1),(25,100003,'fastJson 处理含有下划线字段的坑','','<p><code>fastJson </code>处理含有下划线字段的坑</p><p><span style=\"font-weight: bold;\">场景：</span></p><p>&nbsp; &nbsp; 在<code>ES</code>或者<code>mongodb</code>数据库中，存入数据后，数据库会自动为我们的数据创建<code>_id<br></code>字段，但是如果在查询的时候使用<code>fastjson</code>做数据解析，</p><p><span style=\"color: rgb(194, 79, 74); font-weight: bold;\">问题:</span></p><blockquote><span style=\"color: rgb(194, 79, 74);\">“_id”会被映射到“id”&nbsp;&nbsp;</span><br></blockquote><p><span style=\"color: rgb(139, 170, 74); font-weight: bold;\">解决办法：</span></p><blockquote><span style=\"color: rgb(139, 170, 74);\">将JavaBean中的字段多加一个“_”&nbsp; 也就是“_</span><span style=\"color: rgb(194, 79, 74);\">_</span><span style=\"color: rgb(139, 170, 74);\">”</span></blockquote>',1527225649538,0,20,0,36,0,0,0,1),(26,100003,'Java的poi实现Excel导入导出【导出支持多个sheet,导入支持高低版本的excel】','','<p><span style=\"color: rgb(123, 91, 161);\"><span style=\"font-weight: bold;\">本贴类型：</span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工具源码分享</span></p><p><span style=\"font-weight: bold;\">场景：</span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在很多企业管理系统中都会碰到，excel的poi导入导出功能；需求很大，解决方案也很多，(不过我没用过别人的，喜欢用自己写的^_^,没毛病)，</p><p><span style=\"font-weight: bold; color: rgb(139, 170, 74);\">工具源码：</span></p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、<a href=\"http://www.jfinal.com/share/183\" target=\"_blank\">Excel导出支持多sheet</a><span style=\"color: rgb(139, 170, 74);\">:&nbsp; &nbsp;</span>&nbsp;<a href=\"http://www.jfinal.com/share/183\" target=\"_blank\">&gt;&gt;&gt;快速使用&lt;&lt;&lt;&lt;</a>&nbsp;&nbsp;<span style=\"color: rgb(139, 170, 74);\"><br>&nbsp; &nbsp; &nbsp; &nbsp;</span> 2、<a href=\"http://www.jfinal.com/share/277\" target=\"_blank\">excel简单导入代码 [支持2007前后版本]</a>&nbsp;&nbsp;<a href=\"http://www.jfinal.com/share/277\" target=\"_blank\">&gt;&gt;&gt;快速使用&lt;&lt;&lt;&lt;</a></p><p><br></p><p><span style=\"font-weight: bold;\"><span style=\"color: rgb(139, 170, 74);\">功能描述：&nbsp;</span>&nbsp;</span><br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; 导出：支持多sheet ，支持导出list&lt;bean&gt;  list&lt;Map&gt;  写的比较糙，有相关需求的随便看看，参考着自己个儿改改用<br>&nbsp; &nbsp; &nbsp; &nbsp; 导入：根据配置，导入得到List&lt;Map&gt;数据，可以自己实现List&lt;JavaBean&gt;的扩展（比如使用反射），支持2007前后版本Excel的处理</p><p>&nbsp;</p><p><br></p><p>如有使用不明白的地方，可联系本人<span style=\"color: rgb(139, 170, 74);\">qq:168_52_537 </span>(去掉下划线)</p><p><br></p>',1527226866587,0,20,0,48,1,1,0,1),(27,100003,'Redkale 序列教程汇总','','<p><span style=\"color: rgb(139, 170, 74); font-weight: bold;\">Redkale 入门教程:</span></p><blockquote><a href=\"http://redkale.org/course02_rest.html\" target=\"_blank\">&nbsp;&nbsp;&nbsp;&nbsp;02 -- REST敏捷开发</a>&nbsp;<label>2017-06<br></label><a href=\"http://redkale.org/course01_hello.html\" target=\"_blank\">&nbsp;&nbsp;&nbsp;&nbsp;01 -- Hello Word！</a>&nbsp;<label>2017-06</label></blockquote><p><label><br></label></p><p><label></label></p><p><span style=\"font-weight: bold; color: rgb(139, 170, 74);\">Redkale 技术详解:</span></p><p><a href=\"http://redkale.org/article_convert.html\" target=\"_blank\">03 -- Convert高性能序列化</a>&nbsp;<label>2016-03</label><br><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Convert是个重复造轮子的组件，却是个飞速的轮子。</label></p><p><a href=\"http://redkale.org/article_creator.html\" target=\"_blank\">02 -- Creator构建对象</a>&nbsp;<label>2016-02</label><br><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;org.redkale.util.Creator是采用ASM技术来实现代替反射构造函数的对象构建类。</label></p><p><a href=\"http://redkale.org/article_parents.html\" target=\"_blank\">01 -- 双亲委托模型</a>&nbsp;<label>2016-02</label><br><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Redkale 里大量使用了双亲委托模型，序列化的ConvertFactory、依赖注入的ResourceFactory、服务管理的WatchFactory均采用双亲委托模型。</label></p><p><br></p>',1527388626376,0,20,0,27,0,1,0,1),(28,100003,'读取项目配置文件,JDK自带方法【JDK】','','<p>配置文件内容如下：<br></p><pre><code>user=lxy<br>pwd=123</code></pre><p>非常简单的Java代码（一看就会有木有）:</p><pre><code>Properties prop = new Properties();<br>File file = new File(\"conf/cfg.txt\");<br>prop.load(new FileInputStream(file));<br><br>prop.forEach((k,v)-&gt;{<br>    System.out.println(k+ \"==\"+ v);<br>});</code></pre><p><span style=\"font-weight: bold;\">配置文件是我们项目中必不可少的东西，</span><br>在设计的代码结构的过程中，公共部分通常封装、抽取为方法；<br>其他部分呢，要么来源数据库，要么来源用户输入，要么来源于文件（配置文件），运行代码再加载配置；这时候这段代码就能为我们很好的工作&nbsp;<br></p><p>作者：lxy<br>QQ&nbsp; :168_52_537</p><p><br></p>',1527439605223,0,20,0,41,0,1,0,1),(29,100013,'idea下的vim插件配置文件','','<p>windows:当前用户目录下的<span style=\"background-color: rgb(241, 241, 241); font-family: &quot;Courier New&quot;; font-size: 12px; white-space: pre-wrap;\">_ideavimrc</span></p><p>Linux:：当前用户目录下的<span style=\"background-color: rgb(241, 241, 241); font-family: &quot;Courier New&quot;; font-size: 12px; white-space: pre-wrap;\">.ideavimrc</span></p><p><br></p>',1528422298022,0,20,0,10,0,0,0,1),(30,100018,'Java学习 set classpath的小结','','<p>Java学习 set classpath的小结<br>2018年05月24日 13:52:15<br>阅读数：5<br>我们知道javac命令在编译的时候查找类是按照classpath的路径去寻找，如果找不到，就在当前路径下寻找，如果还是找不到，就会报错。<br><br>我们可以使用set classpath来查看我们的路径是什么<br><br><br><br>这其中有三个路径，第一个是‘.’就是当前路径，后两个都是系统类包路径<br><br>如果我们临时需要设置一个路径，我们可以开启一个命令行窗口<br><br>使用set classpath = 路径名就修改了。<br><br><br><br>如果我们只是想在原有路径基础上，再加上一个路径我们可以这样：<br><br><br><br>%classpath%就是原有的路径，前面加上分号与追加的路径隔开。<br><br>注意：<br><br>我们所使用的set classpath=路径名<br><br>是临时设置的，只在当前的黑屏幕cmd有用，一旦退出，恢复以往路径。<br><br>个人分类： java<br><br><br><br><br><br><br>D:\\hao zun cheng\\Documents\\java演示操作&gt;cd/<br><br>D:\\&gt;java Hello<br>Hello World!<br><br>D:\\&gt;c:<br><br>C:\\&gt;set classpath=d:<br><br>C:\\&gt;java hello<br>错误: 找不到或无法加载主类 hello<br><br>C:\\&gt;set classpath<br>CLASSPATH=d:<br><br>C:\\&gt;java Hello<br>Hello World!<br><br>C:\\&gt;<br></p>',1528569052027,0,30,0,3,0,0,0,3),(31,100018,'java基础','','<p>111.0<br><br>    java基础<br>       软件开发<br>    软件：一序列按照特殊顺序组织的计算机数据和指令的集合。软件;系统软件（windos,linux,Dos），应用软件(qq,扫雷)<br>    开发：软件的开发，制作软件<br>。                       软件实现了人机交互<br>       人机交互<br>     图形化界面<br>     命令行方式（需要一个控制台，输入特定的指令来完成操作<br>   计算机语言<br>   人机沟通的方式c,,,c++,,,java...  <br>    java语言是面向internet的编程语言。<br><br>     java语言的三种技术架构（技术分支）：java EE    java SE   java ME .<br>        <br><br>   java语言的关键特性：1 简洁有效 2 可移植性 3 面向对象 4 解释型 5 适合分布式运算<br><br><br>       java的跨平台性：<br><br>          java可以在不同的平台运行(windois    linux   mac [苹果系统])<br>                                 通过JVM（java虚拟机）jvm不能跨平台，不同的系统装不同的jvm.<br><br>                               JVM是JAVA虚拟机,它将.class字节码文件编译成机器语言,以便机器识别!<br><br><br>         JDK与JRE(JDK开发工具包，JRE运行方式)    <br><br><br><br>        dos中常用的命令：[help]<br>    dir:<br>    md:   rd:   del:  cd:   cd..    cd\\    exit   cls(清屏)<br>      <br>  环境变量的配置<br> Hello World 的演示<br> set classpath的使用<br></p>',1528569113683,0,30,0,3,0,0,0,3),(32,100018,'java语言基础','','<p>111.1<br>java语言基础<br>                组成 ：段     大括号  主体<br><br>public class TestJava{ //public:表示该类公有，整个程序的可以访问<br>                         如果将一个类声明成public,则文件名和这个类名要一致<br>                            在一个java文件里，最多只能有一个public类，否则.java的文件便无法命名。  <br><br>	public static void main(String[]args){ //程序运行的起点，主函数main()   main()method主方法<br>		int num = 3;// 声明变量num,赋值为3.  使用变量前必须先声明<br>                System.out.println(num);//标准输出   打印到标准输出设备-显示器<br><br>                }<br><br><br>		}<br><br><br><br><br> 1.关键字<br><br>   2.标识符<br>    在程序中定义的一些名称。<br>由26个英文字母大小写，数字：0—9，符号：_$ 组成。<br>  定义标识符的规则：1 数字不可以开头2 不可以使用关键字<br><br>   3.注释<br>  java中的注释格式：<br>                    单行注释：//<br>                    <br>                    多行注释：/*        * /<br>             <br>                    文档注释：/**            */<br><br><br>     操作：   HEllo WOrld! 的演示<br><br><br>           可用于程序的检测<br><br><br>语法错误  语义错误<br><br><br><br>  提高程序的可读性：注释  缩进</p>',1528569196783,0,30,0,2,0,0,0,3),(33,100018,'java基础 常量与变量','','<p>111.2<br>   java基础     常量与变量<br>  <br><br>    1.常量<br>     代表不能改变的数值。<br><br>     常量的分类：<br>        1 整数常量    所有整数<br><br>        2小数常量     所有小数<br><br>        3布尔（boolean）型常量    两个   true   false(真假)<br><br>        4字符常量   将一个数字，字母或符号用单引号（\'\'）标识。<br><br>        5字符串常量   一个或多个字符 用双引号（“”）标识。<br><br>        6  null常量（空常量）   一个   null<br>    <br>    对于整数有四种表现形式：   （进制表示计数方式）<br><br>        二进制      八进制（用0开头）     十进制    十六进制（用0x开头表示）<br><br><br>        进制的转换，运算<br><br>           <br><br>     2. 变量<br>      内存中的一个存储区域。<br>      该区域有自己的名称（变量名）和类型（数据类型）。<br>      该区域的数据可以在同一类型范围内不断变化。<br><br>  定义变量是为了用来不断的存放同一类型的常量，并可重复使用。<br><br>     使用变量注意：<br>                  变量的作用范围（一对{}之间有效）<br>                   初始化值<br><br>      定义变量的格式：  数据类型  变量名=初始化值 <br><br>             ▲ 声明一个变量，并给这个变量赋值。▼先声明才能使用！<br><br>变量的三种设置方式： <br>     <br>                    在声明变量时设置/  int num = 3;<br>		    声明后在设置    /  int num ; num = 3;<br>		    在程序的任何位置声明并设置 /<br><br><br><br><br><br>       数据类型：<br><br><br>     基本数据类型    数值型：整型（byte  int  short  long  ） 浮点型（double双精度浮点型  folate）<br>                     布尔型boolean:  true   false<br>                     字符型 char<br><br>    引用数据类型    类（class）  数组   接口（interface）<br>    <br>             整数默认 int    小数默认  double</p>',1528569270573,0,30,0,5,0,0,0,3),(34,100018,'运算符','','<p>111.3<br>   运算符<br>  1.算术运算符   +   -   *   /   %（取余，模运算）(  +  字符串之间的+，用于连接作用：\"a+b=\"+(a+b)+\",b=\"+b   ,    3+\"2\" )   a++  a--：递增递减<br><br><br>  2.赋值运算<br><br>  3.比较运算  ==   !=<br>        比较运算符，运算完后的结果必须是true或false。<br><br>  4.逻辑运算<br>        用于连接两个boolean类型的表达式。（2&lt;x&lt;5,    x&gt;2 &amp;  x&lt;5）<br><br>        &amp;:与    |：或<br>    &amp;运算特点：只要有一边为false,结果为false<br>               只有两边都为true,结果才为true.  true &amp; true= true<br><br>        <br>    |运算特点：只要有一边为true,结果为true<br>               只有两边都为false,结果才为false.<br><br><br><br>  ^ 异或，true^true=false,和或有点不同<br><br>   ^的运算特点：^符号的两边结果如果相同，结果是false<br>                       两边的结果不同，结果是true.<br><br>  !非运算，判断事物的另一面。<br><br>           !true=false; !false=true.<br><br>&amp;&amp; 双与与 运算，左边为false 右边不在做运算（和&amp;相比更高效）<br>||<br><br><br><br>  5.位运算  用于二进制运算的符号（6&amp;3    6^3^3=6 一个数异或同一个数两次，结果还是这个数）<br><br>         &amp;运算   |运算 ^运算  反码运算~6=-7（-7+1=-6）<br><br>       移位运算  &lt;&lt;左移  &gt;&gt;右移   &gt;&gt;&gt;无符号右移<br>        3&lt;&lt;2  3左移两位3*2*2（移几位就这个数乘以二的几次方）右移改除  同理  》》：对于高位出现的<br>空位，是什么就用什么补，负数还是负数<br><br><br><br>&gt;&gt;&gt;:无符号右移，数据进行右移时，高位出现的空位，无论原高位是什么，空位的用0补。负数变正数。<br> <br><br><br>6.三元运算<br><br>    格式    （条件表达式）？表达式1：表达式2；<br><br>                如果条件为true,运算结果为表达式1.<br>                如果条件为false,运算结果为表达式2.<br><br><br><br><br>表达式：<br> 由操作数与运算符组成。操作数可以是常量 ，变量，也可以是方法。<br><br><br><br><br><br><br><br><br><br><br><br></p>',1528569322974,0,30,0,4,0,0,0,3),(35,100018,'if','','<p>111.4   程序流程控制   ---判断结构  选择结构   循环结构  顺序结构<br>        判断结构 If语句 三种格式：if(条件表达式)<br>                                       {执行语句}<br><br>                                  if(条件表达式) &nbsp; &nbsp; if(条件表达式)<br>                                       {执行语句}; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;{执行语句};             <br>                                  else &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; else if(条件表达式)<br>                                       {执行语句}; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {执行语句};<br>                                                         .....<br>                                                        else (条件表达式)<br>                                                                 {执行语句}; <br><br><br>局部代码块可以定义一个局部变量的生命周期。</p>',1528569488211,0,30,0,5,0,0,0,3),(36,100013,'Idea的设置','','<p>一，开启自动导入包功能</p><p>&nbsp; &nbsp;&nbsp;<img src=\"http://img.1216.top/redbbs/20180611143743.png\" style=\"max-width: 100%;\"><br></p>',1528699074773,0,20,0,10,0,0,0,1),(37,100001,'这些设计模式你都知道吗','','<p><img src=\"http://img.1216.top/redbbs/20180612122848.png\" style=\"max-width:100%;\"><br></p><p md-src-pos=\"0..8\"><pre宋体\';font-size:6.4pt;\">一、六大设计原则<br>1. 单一职责原则<br>2. 里氏替换原则<br>3. 依赖倒置原则<br>4. 接口隔离原则<br>5. 迪米特法则<br>6. 开闭原则<br><br>二、设计模式<br>7. 单列模式<br>8. 工厂方法模式<br>9. 抽象工厂模式<br>10. 模板方法模式<br>11. 建造者模式<br>12. 代理模式<br>13. 原型模式<br>14. 中介者模式<br>15. 命令模式<br>16. 责任链模式<br>17. 装饰模式<br>18. 策略模式<br>19. 适配器模式<br>20. 迭代器模式<br>21. 组合模式<br>22. 观察者模式<br>23. 门面模式<br>24. 备忘录模式<br>25. 访问者模式<br>26. 状态模式<br>27. 解释器模式<br>28. 享元模式<br>29. 桥梁模式</pre宋体\';font-size:6.4pt;\"><br></p>',1528777827596,0,20,0,12,0,0,0,1);
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
-- Table structure for table `demo`
--

DROP TABLE IF EXISTS `demo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demo` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `remark` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demo`
--

LOCK TABLES `demo` WRITE;
/*!40000 ALTER TABLE `demo` DISABLE KEYS */;
/*!40000 ALTER TABLE `demo` ENABLE KEYS */;
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
  `roleId` int(2) NOT NULL DEFAULT '0',
  `site` varchar(128) DEFAULT '' COMMENT '[个人博客地址]',
  `git` varchar(128) DEFAULT '' COMMENT '[码云/GitHub]',
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
INSERT INTO `user` VALUES (100001,'@1216.top','fcea920f7412b5da7be0cf42b8c93759',1,'18515190967','绝尘','http://img.1216.top/art/u93.png','','admin@1216.top',1,'http://1216.top','https://gitee.com/tc608',1507677533071,'','云南',1),(100002,'237809797@qq.com','fcea920f7412b5da7be0cf42b8c93759',2,'13121880915','晓','http://img.1216.top/art/u36.png','','237809797@qq.com',0,NULL,'',1509196823551,'','',1),(100003,'237809796@qq.com','fcea920f7412b5da7be0cf42b8c93759',1,'','nick','http://img.1216.top/bbs/20171203103651.gif','','237809796@qq.com',1,NULL,'',1511851218332,'态度决定高度。','',1),(100004,'vip@qq.com','25d55ad283aa400af464c76d713c07ad',1,'','qq','/res/images/avatar/3.jpg','','vip@qq.com',0,NULL,'',1512954927558,'','',1),(100005,'12@qq.com','e10adc3949ba59abbe56e057f20f883e',1,'','123','/res/images/avatar/13.jpg','','12@qq.com',0,NULL,'',1512957064362,'','',1),(100006,'10000@qq.com','0d124d13699173f26a6519631cfe6e52',1,'','na','/res/images/avatar/9.jpg','','10000@qq.com',0,NULL,'',1512987234013,'','',1),(100007,'syy@qq.com','6fb327cfe8bd8268d7e1a8468d88e8d2',1,'','syy','/res/images/avatar/13.jpg','','syy@qq.com',0,NULL,'',1513305537321,'','',1),(100008,'555@qq.com','5b1b68a9abf4d2cd155c81a9225fd158',1,'','555','/res/images/avatar/19.jpg','','555@qq.com',0,NULL,'',1513355350396,'','',1),(100009,'2442669938@qq.com','25f9e794323b453885f5181f1b624d0b',1,'','iLvc','/res/images/avatar/10.jpg','','2442669938@qq.com',0,NULL,'',1513512637440,'','',1),(100010,'316034712@qq.com','af73fabc0e20d29ce37ad7bb66e7a4ff',1,'','OoxiaobinoO','/res/images/avatar/4.jpg','','316034712@qq.com',0,NULL,'',1513840912828,'','',1),(100011,'dsfds@qq.com','614ac7c72e24ff3f38f9f3fc7bdb85ad',1,'','znm','/res/images/avatar/0.jpg','','dsfds@qq.com',0,NULL,'',1519973503109,'','',1),(100012,'aaa@qq.com','d9f6e636e369552839e7bb8057aeb8da',1,'','aaa','/res/images/avatar/11.jpg','','aaa@qq.com',0,NULL,'',1519973543625,'','',1),(100013,'ceilingsliuce@163.com','09528b3dc71191355493ffdc7d2ad592',1,'','darkleo','http://img.1216.top/redbbs/20180416092232.jpg','','ceilingsliuce@163.com',1,NULL,'',1523251835728,'原本安静的环境，突然变得喧闹了。','帝都',1),(100014,'qkmc@outlook.com','f0dc2ec24679fae04aeb9b2c86b5ffdf',1,'','mrruan','http://img.1216.top/redbbs/20180413000231.jpg','','qkmc@outlook.com',0,NULL,'',1523548897975,'','',1),(100015,'540825202@qq.com','d2f30ddd1f6c85aca59040a1c9d62cc4',1,'','欧夏晴','/res/images/avatar/8.jpg','','540825202@qq.com',0,NULL,'',1525757972569,'','',1),(100016,'yjikai@163.com','c9993d00fd9a06389e4339c8ace53c14',1,'','JiMoer','/res/images/avatar/16.jpg','','yjikai@163.com',0,NULL,'',1526123922579,'','',1),(100017,'1403852318@qq.com','6132f7de6ec97fe73bb18db72116c40a',1,'','孟人二','/res/images/avatar/2.jpg','','1403852318@qq.com',0,'','',1528180576775,'','',1),(100018,'1015336092@qq.com','4607e782c4d86fd5364d7e4508bb10d9',1,'','cheng','/res/images/avatar/1.jpg','','1015336092@qq.com',0,'','',1528567851030,'以夢为馬，不负韶华。','',1),(100019,'3333@qq.com','fcea920f7412b5da7be0cf42b8c93759',1,'','11','/res/images/avatar/5.jpg','','3333@qq.com',0,'','',1528849962001,'','',1);
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

-- Dump completed on 2018-06-13  9:55:11
