/*!
 huwei 2013.11.8
 */
$(function(){
    ;(function(){   
  var   CalendarData=new   Array(20);  
  var   madd=new   Array(12);  
  var   TheDate=new   Date();  
  var   tgString="甲乙丙丁戊己庚辛壬癸";  
  var   dzString="子丑寅卯辰巳午未申酉戌亥";  
  var   numString="一二三四五六七八九十";  
  var   monString="正二三四五六七八九十冬腊";  
  var   weekString="日一二三四五六";  
  var   sx="鼠牛虎兔龙蛇马羊猴鸡狗猪";  
  var   cYear;  
  var   cMonth;  
  var   cDay;  
  var   cHour;  
  var   cDateString;  
  var   DateString;  
  var   Browser=navigator.appName;  
   
  function   init()  
  {    
      CalendarData[0]=0x41A95;  
      CalendarData[1]=0xD4A;  
      CalendarData[2]=0xDA5;  
      CalendarData[3]=0x20B55;  
      CalendarData[4]=0x56A;  
      CalendarData[5]=0x7155B;  
      CalendarData[6]=0x25D;  
      CalendarData[7]=0x92D;  
      CalendarData[8]=0x5192B;  
      CalendarData[9]=0xA95;  
      CalendarData[10]=0xB4A;  
      CalendarData[11]=0x416AA;  
      CalendarData[12]=0xAD5;  
      CalendarData[13]=0x90AB5;  
      CalendarData[14]=0x4BA;  
      CalendarData[15]=0xA5B;  
      CalendarData[16]=0x60A57;  
      CalendarData[17]=0x52B;  
      CalendarData[18]=0xA93;  
      CalendarData[19]=0x40E95;  
      madd[0]=0;  
      madd[1]=31;  
      madd[2]=59;  
      madd[3]=90;  
      madd[4]=120;  
      madd[5]=151;  
      madd[6]=181;  
      madd[7]=212;  
      madd[8]=243;  
      madd[9]=273;  
      madd[10]=304;  
      madd[11]=334;  
    }  
   
  function   GetBit(m,n)  
  {    
        return   (m>>n)&1;  
  }  
   
  function   e2c()  
  {      
      var   total,m,n,k;  
      var   isEnd=false;  
      var   tmp=TheDate.getYear();  
      if   (tmp<1900)     tmp+=1900;  
      total=(tmp-2001)*365  
          +Math.floor((tmp-2001)/4)  
          +madd[TheDate.getMonth()]  
          +TheDate.getDate()  
          -23;  
      if   (TheDate.getYear()%4==0&&TheDate.getMonth()>1)  
          total++;  
      for(m=0;;m++)  
      {      
          k=(CalendarData[m]<0xfff)?11:12;  
          for(n=k;n>=0;n--)  
          {  
              if(total<=29+GetBit(CalendarData[m],n))  
              {    
                  isEnd=true;  
                  break;  
              }  
              total=total-29-GetBit(CalendarData[m],n);  
          }  
          if(isEnd)break;  
      }  
      cYear=2001   +   m;  
      cMonth=k-n+1;  
      cDay=total;  
      if(k==12)  
      {  
          if(cMonth==Math.floor(CalendarData[m]/0x10000)+1)  
              cMonth=1-cMonth;  
          if(cMonth>Math.floor(CalendarData[m]/0x10000)+1)  
              cMonth--;  
      }  
      cHour=Math.floor((TheDate.getHours()+3)/2);  
  }  
   
  function   GetcDateString()  
  {   var   tmp="";  
      //tmp+=tgString.charAt((cYear-4)%10);       //年干  
      //tmp+=dzString.charAt((cYear-4)%12);       //年支  
      //tmp+="年(";  
      // tmp+=sx.charAt((cYear-4)%12);  
      // tmp+=")   ";  
      if(cMonth<1)  
      {    
        tmp+="闰";  
          tmp+=monString.charAt(-cMonth-1);  
      }  
      else  
          tmp+=monString.charAt(cMonth-1);  
      tmp+="月";  
      tmp+=(cDay<11)?"初":((cDay<20)?"十":((cDay<30)?"廿":"卅"));  
      // if(cDay%10!=0||cDay==10)  
      //     tmp+=numString.charAt((cDay-1)%10);  
      // tmp+="    ";  
      // if(cHour==13)tmp+="夜";  
      //     tmp+=dzString.charAt((cHour-1)%12);  
      // tmp+="时";  
      cDateString=tmp;  
      return   tmp;  
  }  
   
  function   GetDateString()  
  {    
      var   tmp="";  
      var   t1=TheDate.getYear();  
      if   (t1<1900)t1+=1900;  
      tmp+=t1  
                +"年"  
                +(TheDate.getMonth()+1)+"月"  
                +TheDate.getDate()+"日 "  
                +TheDate.getHours()+":"  
                +((TheDate.getMinutes()<10)?"0":"")  
                +TheDate.getMinutes()
                //+" 星期"+weekString.charAt(TheDate.getDay());    
      DateString=tmp;  
      return   tmp;  
  }  
   
  init();  
  e2c();  
  GetDateString();  
  GetcDateString();  
  var strs = ("最后访问时间："+DateString); //'+" 农历 "+cDateString
  if($('.topxx').length){
     var times = $('.topxx')[0].getElementsByTagName('p')[0];
     times.innerHTML = strs;
     $('.topxx p').eq(0).css({'width':'47%','margin-left':'25px'})
  }  
  
    })() ;  
  ///////////////side menu////////////////
  ;(function($){
        $('.main-lbox li').click(function(event){ 
            var _this = $(this); 
            //当前点击的 菜单是否是子菜单
			if(_this.parent().parent('li').length){//是
				// 是否已展开
				if(_this.is('.haver')){//是
					//关闭当前点击的节点并隐藏子节点
				    _this.removeClass();
				    var cul = _this.children('ul'); 
					if(cul.length){
						cul.children('li').removeClass();
						cul.hide();
					}
				}else{//否
				    _this.addClass('haver').siblings().removeClass().children('ul').hide().children('li').removeClass(); 
				    var cul = _this.children('ul'); 
					if(cul.length){
						 cul.show();
					}
				}
			}else{//否
				// 展开点击的节点并收缩其他兄弟节点
				_this.addClass('haver').siblings().removeClass().children('ul').hide().children('li').removeClass(); 
				//当前节点下的子节点集
				var cul = _this.children('ul');
				if(cul.length){
					if(_this.is('.hsvadd')){
					    cul.hide();
					   _this.removeClass('hsvadd')
					}else{
					   cul.show();
					   _this.addClass('hsvadd')
				    }
				}
			}  
			 
			event.stopPropagation();
        });
      
    })(jQuery);
  //////////////////varea///////////////
  ;(function($){
      $('.varea li').click(function(event){
		    var e=event||window.event;
			var target=e.target||e.srcElement;
           var _this = $(this); 
          // _this.addClass('haver').siblings().removeClass('haver').children('dl').hide();
		   if(_this.is('.hsvad')){ 
						   _this.removeClass('haver');
						   _this.find('dl').hide();
						   _this.removeClass('hsvad')
						}else{ 
						   _this.addClass('haver').siblings().removeClass('haver').children('dl').hide();
						   _this.find('dl').eq(0).show();
						   _this.addClass('hsvad')
					    }
           var cdl = _this.children('dl');
            if(cdl.length){
                 cdl.find('dd').click(function(event){
					 var e=event||window.event;
			         var target=e.target||e.srcElement;
                     var _this = $(target).parent();
					 if(_this.parent('.lastj').length){
					     _this.addClass('haver').siblings().removeClass('haver');
					    // e.stopPropagation();
					 }else{
					      if(_this.is('.hsvadd')){
						    _this.removeClass('haver')
							 var cdl = _this.children('dl');
							 if(cdl.length){
								  cdl.hide();
							  }
						   _this.removeClass('hsvadd');
						  // e.stopPropagation();
						}else{
						    _this.addClass('haver')
							 var cdl = _this.children('dl');
							 if(cdl.length){
								  cdl.show();
							  }
						   _this.addClass('hsvadd');
						  // e.stopPropagation();
					    }
					 };
					 
                    e.stopPropagation();
                    
                 });
            }
			e.stopPropagation();
      });
  })(jQuery);
  ///////////searchmore/////////////
 /* ;(function($){
    $('#searchmore').click(function(){
         var oul = $('.liebtop ul').eq(1),
             _this = $(this);
            
            if(_this.is('.hsv')){
						   oul.addClass('haver').hide();
						   _this.removeClass('hsv')
						}else{
						   oul.addClass('haver').show();
						   _this.addClass('hsv')
					    }

            
    })
  })(jQuery);*/

  //////////////mapbox//////////////////
  $('.mapbox h3').each(function(i){
            $(this).click(function(){
                  if($(this).is('.haver')){
                     $(this).removeClass('haver').next('.mapxx').hide();
                  }else{
                    $(this).addClass('haver').next('.mapxx').show();
                  }
                   
              }); 
  });
  /////////////tag//////////////
  $('.bqlie li').click(function(){
      $(this).addClass('haver').siblings().removeClass('haver');
      var i = $(this).index();
      $('.biaoq').hide();
      $('.biaoq').eq(i).show();
  });
  /////////////////tagname/////////////
  $('.tagname li').click(function(){
          $(this).addClass('haver').siblings().removeClass('haver');
          var i = $(this).index(); 
          $('.mboxm').hide();
          $('.mboxm').eq(i).show();
  });
 
});
/////////////////////////////////////////////
  //////////////butleft ////////////
;(function(){
    obj = {'speed':10,'space':3,'pagewidth':5,'list2':'List2','list1':'List1','ISL_Cont':'ISL_Cont','LeftB':'LeftB','RightB':'RightB'};
	obj2 = {'speed':10,'space':3,'pagewidth':5,'list2':'List22','list1':'List12','ISL_Cont':'ISL_Cont2','LeftB':'LeftB2','RightB':'RightB2'};
	obj3 = {'speed':10,'space':3,'pagewidth':5,'list2':'List23','list1':'List13','ISL_Cont':'ISL_Cont3','LeftB':'LeftB3','RightB':'RightB3'};
	obj4 = {'speed':10,'space':3,'pagewidth':5,'list2':'List24','list1':'List14','ISL_Cont':'ISL_Cont4','LeftB':'LeftB4','RightB':'RightB4'};
	var imgw ;
	if($('table.vlist2').length){
	    imgw =parseInt($('#List1 li').width(),10); 
	     MoveAuto(obj);
		 MoveAuto(obj2);
		 MoveAuto(obj3);
		 MoveAuto(obj4);
	}else{return false;}; 
function MoveAuto(obj){
   //图片滚动列表
	var Speed = obj.speed; //速度(毫秒)
	var Space = obj.space; //每次移动(px)
	var PageWidth = obj.pagewidth; //翻页宽度
	var fill = 0; //整体移位
	var MoveLock = false;
	var MoveTimeObj;
	var Comp = 0;
	var AutoPlayObj = null,addtime=null,addtimer=null;
	var ow = parseInt($('.divcover').width(),10);
	var ns = $('#'+obj.list1+' img').length; 
	if(ow>ns*imgw||!ns){return false;}
	GetObj(obj.list2).innerHTML = GetObj(obj.list1).innerHTML;
	GetObj(obj.ISL_Cont).scrollLeft = fill;
	GetObj(obj.ISL_Cont).onmouseover = function(){clearInterval(AutoPlayObj);}
	GetObj(obj.ISL_Cont).onmouseout = function(){AutoPlay();}
	AutoPlay();
	function GetObj(objName){if(document.getElementById){return eval('document.getElementById("'+objName+'")')}else{return eval('document.all.'+objName)}}
	function AutoPlay(){ //自动滚动
	 clearInterval(AutoPlayObj);
	 AutoPlayObj = setInterval(function(){ISL_GoDown();ISL_StopDown2();},30); //间隔时间
	};
	function ISL_GoUp(){ //上翻开始
	 if(MoveLock) return;
	 clearInterval(AutoPlayObj);
	 MoveLock = true;
	 MoveTimeObj = setInterval(function(){ISL_ScrUp();},Speed);
	};
	function ISL_StopUp(){ //上翻停止
	 clearInterval(MoveTimeObj);
	 if(GetObj(obj.ISL_Cont).scrollLeft % PageWidth - fill != 0){
	  Comp = fill - (GetObj(obj.ISL_Cont).scrollLeft % PageWidth);
	  CompScr();
	 }else{
	  MoveLock = false;
	 };
	 if(addtime){clearTimeout(addtime)}
	 addtime = setTimeout(AutoPlay,3000) ;
	};
	function ISL_ScrUp(){ //上翻动作
	 if(GetObj(obj.ISL_Cont).scrollLeft <= 0){GetObj(obj.ISL_Cont).scrollLeft = GetObj(obj.ISL_Cont).scrollLeft + GetObj(obj.list1).offsetWidth}
	 GetObj(obj.ISL_Cont).scrollLeft -= Space ;
	};
	function ISL_GoDown(){ //下翻
	 clearInterval(MoveTimeObj);
	 if(MoveLock) return;
	 clearInterval(AutoPlayObj);
	 MoveLock = true;
	 ISL_ScrDown();
	 MoveTimeObj = setInterval(function(){ISL_ScrDown()},Speed);
	};
	function ISL_StopDown(){ //下翻停止
	 clearInterval(MoveTimeObj);
	 if(GetObj(obj.ISL_Cont).scrollLeft % PageWidth - fill != 0 ){
	  Comp = PageWidth - GetObj(obj.ISL_Cont).scrollLeft % PageWidth + fill;
	  CompScr();
	 }else{
	  MoveLock = false;
	 }
	 if(addtimer){clearTimeout(addtimer)}
	 addtimer = setTimeout(AutoPlay,3000) ; 
	};
	function ISL_StopDown2(){ //下翻停止
	 clearInterval(MoveTimeObj);
	 if(GetObj(obj.ISL_Cont).scrollLeft % PageWidth - fill != 0 ){
	  Comp = PageWidth - GetObj(obj.ISL_Cont).scrollLeft % PageWidth + fill;
	  CompScr();
	 }else{
	  MoveLock = false;
	 } 
	  AutoPlay();
	};
	function ISL_ScrDown(){ //下翻动作
	 if(GetObj(obj.ISL_Cont).scrollLeft >= GetObj(obj.list1).scrollWidth){GetObj(obj.ISL_Cont).scrollLeft = GetObj(obj.ISL_Cont).scrollLeft - GetObj(obj.list1).scrollWidth;}
	 GetObj(obj.ISL_Cont).scrollLeft += Space ;
	} ;
	function CompScr(){
	 var num;
	 if(Comp == 0){MoveLock = false;return;}
	 if(Comp < 0){ //上翻
	  if(Comp < -Space){
	   Comp += Space;
	   num = Space;
	  }else{
	   num = -Comp;
	   Comp = 0;
	  }
	  GetObj(obj.ISL_Cont).scrollLeft -= num;
	  setTimeout(function(){CompScr()},Speed);
	 }else{ //下翻
	  if(Comp > Space){
	   Comp -= Space;
	   num = Space;
	  }else{
	   num = Comp;
	   Comp = 0;
	  }
	  GetObj(obj.ISL_Cont).scrollLeft += num;
	  setTimeout(function(){CompScr()},Speed);
	 }
	};

	GetObj(obj.LeftB).onmousedown=ISL_GoUp;
	GetObj(obj.LeftB).onmouseup=ISL_StopUp;
	GetObj(obj.LeftB).onmouseout=ISL_StopUp;
	GetObj(obj.RightB).onmousedown=ISL_GoDown;
	GetObj(obj.RightB).onmouseup=ISL_StopDown ;
	GetObj(obj.RightB).onmouseout=ISL_StopDown;
};
})();
////////////////////////////////
//////table
;$(function(){ 
	var ntd = $('.show-td tr').not('.mb-ta-head');
    if(ntd.length){
	   ntd.each(function(i){
		   var _this = $(this); 
	      if(i%2==0){
		     _this.addClass('mb-ta-colorjg');
		  }
	   });
	}
});

/*
 * 调整mian页面div初始高度
 */
$(function(){
	if($(".main-rbox").length > 0){
		var div_height = $(".main-rbox").height();
		var win_height = document.documentElement.clientHeight;
		var height = div_height > win_height ? div_height : win_height;
		$(".main-rbox").height(height);
	}
});


function ShowTime(){
	var timeString;
	var intYear,intMonth,intDay,intHours,intMinutes,intSeconds;
var today;
today=new Date();
intYear=today.getYear();
intMonth=today.getMonth()+1;
intDay=today.getDate();
intHours=today.getHours();
intMinutes=today.getMinutes();
intSeconds=today.getSeconds();

if(intMonth<10)
{
   intMonth = "0" + intMonth;
}
if(intDay<10)
{
   intDay = "0" + intDay;
}
if(intHours<10)
{
   intHours = "0" + intHours;
}
if(intMinutes<10)
{
   intMinutes = "0" + intMinutes;
}
if(intSeconds<10)
{
   intSeconds = "0" + intSeconds;
}
timeString = "当前时间："+String(intYear) + "-" + String(intMonth) + "-" + String(intDay) + " " + String(intHours) + ":" + String(intMinutes) + ":" + String(intSeconds);
document.getElementById("ShowTime").innerHTML = timeString;
window.setTimeout("ShowTime()",1000);
}

