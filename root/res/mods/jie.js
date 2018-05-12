/**

 @Name: 求解板块

 */
 
layui.define(['fly','laypage'], function(exports){

  var $ = layui.jquery;
  var layer = layui.layer;
  var util = layui.util;
  var laytpl = layui.laytpl;
  var form = layui.form;
  var laypage = layui.laypage;
  var fly = layui.fly;

  var editor;
  var gather = {}, dom = {
    jieda: $('#jieda')
    ,content: $('#L_content')
    ,jiedaCount: $('#jiedaCount')
  };

  var jie = {
      createEditer : function(){
          editor = fly.editer();
      }
  }

  //监听专栏选择
  form.on('select(column)', function(obj){
    var value = obj.value
    ,elemQuiz = $('#LAY_quiz')
    ,tips = {
      tips: 1
      ,maxWidth: 250
      ,time: 10000
    };
    elemQuiz.addClass('layui-hide');
    if(value === '0'){
      layer.tips('下面的信息将便于您获得更好的答案', obj.othis, tips);
      elemQuiz.removeClass('layui-hide');
    } else if(value === '99'){
      layer.tips('系统会对【分享】类型的帖子予以飞吻奖励，但我们需要审核，通过后方可展示', obj.othis, tips);
    }
  });

  //提交回答
  fly.form['/jie/reply/'] = function(data, required){
    var tpl = '<li>\
      <div class="detail-about detail-about-reply">\
        <a class="fly-avatar" href="/u/{{ layui.cache.mine.userId }}" target="_blank">\
          <img src="{{= d.user.avatar}}" alt="{{= d.user.username}}">\
        </a>\
        <div class="fly-detail-user">\
          <a href="/u/{{ layui.cache.user.uid }}" target="_blank" class="fly-link">\
            <cite>{{d.user.username}}</cite>\
          </a>\
        </div>\
        <div class="detail-hits">\
          <span>刚刚</span>\
        </div>\
      </div>\
      <div class="detail-body jieda-body photos">\
        {{ d.content}}\
      </div>\
    </li>'
    data.content = fly.content(data.content);
    laytpl(tpl).render($.extend(data, {
      user: layui.cache.user
    }), function(html){
      required[0].value = '';
      dom.jieda.find('.fly-none').remove();
      dom.jieda.append(html);
      
      var count = dom.jiedaCount.text()|0;
      dom.jiedaCount.html(++count);
    });
  };

  //求解管理
  gather.jieAdmin = {
    //删求解
    del: function(div){
      layer.confirm('确认删除该求解么？', function(index){
        layer.close(index);
        fly.json('/os/content/set', {
            id: div.data('id')
            ,v: -1
            ,field:"status"
        }, function(res){
            location.href= "/";
        });
      });
    }
    
    //设置置顶、状态
    ,set: function(div){
      var othis = $(this);
      fly.json('/os/content/set', {
        id: div.data('id')
        ,v: othis.attr('v')
        ,field: othis.attr('field')
      }, function(res){
        location.reload();
      });
    }

    //收藏
    ,collect: function(div){
      var othis = $(this), type = othis.data('type');
      fly.json('/os/content/collect', {
          contentId: div.data('id')
          ,ok: type === 'add'? 1:-1
      }, function(res){
          if(type === 'add'){
              othis.data('type', 'remove').html('取消收藏').addClass('layui-btn-danger');
              layer.msg("收藏成功");
          } else if(type === 'remove'){
              othis.data('type', 'add').html('收藏').removeClass('layui-btn-danger');
              layer.msg("已取消收藏");
          }
      });
    }
  };

  $('body').on('click', '.jie-admin', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jieAdmin[type] && gather.jieAdmin[type].call(this, othis.parent());
  });

  //异步渲染
  var asyncRender = function(){
    var div = $('.fly-admin-box'), jieAdmin = $('#LAY_jieAdmin');
    //查询帖子是否收藏
    if(jieAdmin[0] && layui.cache.user.uid != -1){
      fly.json('/collection/find/', {
        cid: div.data('id')
      }, function(res){
        jieAdmin.append('<span class="layui-btn layui-btn-xs jie-admin '+ (res.data.collection ? 'layui-btn-danger' : '') +'" type="collect" data-type="'+ (res.data.collection ? 'remove' : 'add') +'">'+ (res.data.collection ? '取消收藏' : '收藏') +'</span>');
      });
    }
  }();

  //解答操作
  gather.jiedaActive = {
    zan: function(li){ //赞
      var othis = $(this), ok = othis.hasClass('zanok');
      fly.json('/os/comment/support', {
          commentId: li.data('id')
          ,ok: ok?-1:1
      }, function(res){
          var zans = othis.find('em').html()|0;
          othis[ok ? 'removeClass' : 'addClass']('zanok');
          othis.find('em').html(ok ? (--zans) : (++zans));
      });
    }
    ,reply: function(li){ //回复
      var val = dom.content.val();
      var aite = '@'+ li.find('.fly-detail-user cite').text().replace(/\s/g, '');
      dom.content.focus()
      if(val.indexOf(aite) !== -1) return;
      dom.content.val(aite +' ' + val);
      $("input[name='pid']").val(li.data('id'));
    }
    ,accept: function(li){ //采纳
      var othis = $(this);
      layer.confirm('是否采纳该回答为最佳答案？', function(index){
        layer.close(index);
        fly.json('/api/jieda-accept/', {
          id: li.data('id')
        }, function(res){
          if(res.status === 0){
            $('.jieda-accept').remove();
            li.addClass('jieda-daan');
            li.find('.detail-about').append('<i class="iconfont icon-caina" title="最佳答案"></i>');
          } else {
            layer.msg(res.msg);
          }
        });
      });
    }
    ,edit: function(li){ //编辑
      fly.json('/jie/getDa/', {
        id: li.data('id')
      }, function(res){
        var data = res.rows;
        layer.prompt({
          formType: 2
          ,value: data.content
          ,maxlength: 100000
          ,title: '编辑回帖'
          ,area: ['728px', '300px']
          ,success: function(layero){
            fly.layEditor({
              elem: layero.find('textarea')
            });
          }
        }, function(value, index){
          fly.json('/jie/updateDa/', {
            id: li.data('id')
            ,content: value
          }, function(res){
            layer.close(index);
            li.find('.detail-body').html(fly.content(value));
          });
        });
      });
    }
    ,del: function(li){ //删除
      layer.confirm('确认删除该回答么？', function(index){
        layer.close(index);
        fly.json('/api/jieda-delete/', {
          id: li.data('id')
        }, function(res){
          if(res.status === 0){
            var count = dom.jiedaCount.text()|0;
            dom.jiedaCount.html(--count);
            li.remove();
            //如果删除了最佳答案
            if(li.hasClass('jieda-daan')){
              $('.jie-status').removeClass('jie-status-ok').text('求解中');
            }
          } else {
            layer.msg(res.msg);
          }
        });
      });    
    }
  };

  $('.jieda-reply span').on('click', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jiedaActive[type].call(this, othis.parents('li'));
  });


  form.on('submit(jie-add)', function(data){
      var bean = {};
      ["contentId","title", "content", "type"].forEach(function (value) {
        bean[value] = data.field[value];
      });
      bean["content"] = editor.txt.html();
      console.log(bean);

      fly.json("/os/content/save",{
          bean:JSON.stringify(bean)
      },function (res) {
          layer.msg("发布成功",{time:2000},function () {
              var cache_key = "content_" + $("input[name='contentId']").val();
              localStorage.removeItem(cache_key);
              location.href = "/user";
          });
      });
      return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });
  form.on('submit(jie-reply)', function(data){
      var bean = {};
      ["contentId","pid", "content"].forEach(function (value) {
        bean[value] = data.field[value];
      });
      bean["content"] = editor.txt.html();
      console.log(bean);

      fly.json("/os/comment/save",{
          bean:JSON.stringify(bean)
      },function (res) {
          layer.msg("回复成功",{time:2000},function () {
              //location.href = "/";
              location.reload();
          });
      });
      return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });

    $("."+ layui.cache.actived).addClass("tab-this");

    function getUrl(curr){
        var args=new Object();
        var query=location.search.substring(1);//获取查询串
        var pairs=query.split("&");//在逗号处断开
        for(var i=0;i<pairs.length;i++){
            var pos=pairs[i].indexOf('=');//查找name=value
            if(pos==-1){//如果没有找到就跳过
                continue;
            }
            var argname=pairs[i].substring(0,pos);//提取name
            var value=pairs[i].substring(pos+1);//提取value
            args[argname]=unescape(value);//存为属性
        }
        if(curr)args.curr = curr;

        var url = location.href;
        url = url.substring(0, url.indexOf("?"));

        var search = "?"
        for(x in args){
            console.log(search);
            search = search + x+ "=" +args[x] +"&";
        }
        return url+search;//返回对象
    }

    if(layui.cache.curr){
      layui.laypage.render({
          elem:"jie-laypage"
          ,curr:layui.cache.curr
          ,count: layui.cache.total
          ,limit:20
          ,jump: function(obj, first){
              var curr = obj.curr;
              if(!first){
                  location.href = getUrl(curr);
              }
          }
      });
  }

  exports('jie', jie);
});