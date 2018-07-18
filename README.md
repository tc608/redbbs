# redbbs
        
    2018-07-01更新
    1、优化帖子阅读计数，让帖子阅读数更准确
    2、更新最新redkale-plugins.jar，加入redis验权
    3、栏目页用户昵称点击跳转失败，栏目下帖子a标签中加入title属性
    4、修改本周热贴，查询一周内访问量最多的帖子

    2018-06-30更新  
    1、修改servlet层中BaseServlet中共享request的重大bug 
    2、将servlet中页面统一使用HttpScope进行渲染
    3、优化帖子阅读计数，让帖子阅读数更准确
    4、更新最新redkale-plugins.jar，加入redis验权

    2018-06-17更新  
    社区升级
    1、表字段统一小写，
    2、表模块划分
    3、表状态等字段统一smallint
    4、部分字段值重新定义

 ### 项目介绍
 redbbs是基于redkale实现java bbs论坛系统; 
 简单易使用、可扩展、高性能；
 修改配置文件，运行启动脚本便可快速实现论坛系统的搭建;
 
 ### 实现功能
 ##### 用户模块   
    1.会员注册  
    2.登录  
    3.用户中心{修改头像/账号资料}  
    4.个人主页  
 ##### 帖子模块
    1.帖子发布
    2.帖子评论
    3.帖子收藏
    4.帖子删除/置顶/加精（管理员功能）

 ### 快速搭建
 1. 下载项目    
 2. 将数据库脚本导入数据库
 3. 修改配置 
    > 数据库配置：persistence.xml  
    redis/端口配置 :application.xml
 4. 运行启动脚本
    > bin/start.bat
 5. 浏览器访问
    > http://localhost,   
     访问效果[redbbs地址 www.1216.top](http://www.1216.top)
 
    源码下载:
    下载redbbs源码，或者zip压缩包(建议使用git拉取，方便第一时间获取最新升级)  
   
   
 ### 使用到的相关技术和相关人员
 redkale  -- 后端主要框架  
 enjoy    -- 数据渲染  
 layui    -- 前端界面 
 
 
 ### 欢迎加入 
    redbbs 交流群：527523235
    redkale交流群：527523235  
 