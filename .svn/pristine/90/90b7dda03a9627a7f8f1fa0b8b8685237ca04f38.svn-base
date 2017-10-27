var x0=0,y0=0,x1=0,y1=0;
//var offx=6,offy=6;
var moveable=false;
var hover='orange',normal='slategray';//color;
var xwinindex=10000;//z-index;
var imgpath = "../images";
//开始拖动;
function startDrag(obj)
{
        if(event.button==1)
        {
                //锁定标题栏;
                obj.setCapture();
                //定义对象;
                var win = obj.parentNode;
                //记录鼠标和层位置;
                x0 = event.clientX;
                y0 = event.clientY;
                x1 = parseInt(win.style.left);
                y1 = parseInt(win.style.top);
                moveable = true;
        }
}
//拖动;
function drag(obj, width)
{
        if(moveable)
        {
                var win = obj.parentNode;
                if(x1 + event.clientX - x0 + + (width-2*14-4)>0) {
	                win.style.left = x1 + event.clientX - x0;
                }
                if(y1 + event.clientY - y0 + 5>0) {
	                win.style.top  = y1 + event.clientY - y0;
	            }
        }
}
//停止拖动;
function stopDrag(obj)
{
        if(moveable)
        {
                var win = obj.parentNode;
                var msg = obj.nextSibling;
                obj.releaseCapture();
                moveable = false;
        }
}
//获得焦点;
function getFocus(obj)
{
        if(obj.style.zIndex!=xwinindex)
        {
                xwinindex = xwinindex + 2;
                var idx = xwinindex;
                obj.style.zIndex=idx;
                obj.nextSibling.style.zIndex=idx-1;
        }
}
//最小化;
function min(obj)
{
        var win = obj.parentNode.parentNode;
        var sha = win.nextSibling;
        var tit = obj.parentNode;
        var msg = tit.nextSibling;
        var flg = msg.style.display=="none";
        if(flg)
        {
                win.style.height  = parseInt(msg.style.height) + parseInt(tit.style.height) + 2*2;
                sha.style.height  = win.style.height;
                msg.style.display = "block";
                obj.innerHTML = "0";
        }
        else
        {
                win.style.height  = parseInt(tit.style.height) + 2*2;
                sha.style.height  = win.style.height;
                obj.innerHTML = "2";
                msg.style.display = "none";
        }
}
//关闭;
function cls(obj)
{
        var win = obj.parentNode.parentNode;
        win.style.visibility = "hidden";
}
function xwinshow()
{
        var win = document.getElementById("xMsg" +this.id);
        win.style.visibility = "visible";
}
function xwincls()
{
        var win = document.getElementById("xMsg" +this.id);
        win.style.visibility = "hidden";
}
function xwinresize(width, height)
{
		var win = document.getElementById("xMsg" +this.id);
		win.style.width = width;
		win.style.height = height;
		var frm = document.getElementById("xMsgBackFrame" +this.id);
		frm.width = width;
		frm.height = height;
		var ttitle = document.getElementById("xMsgTitle" +this.id);
		ttitle.style.width = width;
		var tbar = document.getElementById("xMsgTitleBar" +this.id);
		tbar.style.width = width - 3;
		var tbarcon = document.getElementById("xMsgTitleBarCon" +this.id);
		tbarcon.style.width = width - 6;
		var msgcon = document.getElementById("xMsgCon" +this.id);
		msgcon.style.width = width;
		msgcon.style.height = height - 21;
		var infrm = document.getElementById("xMsgFrame" + this.id);
		if(infrm){
			infrm.width = width - 10;
			infrm.height = height - 29;
		}
}
//创建一个对象;
function xWin(id,w,h,l,t,tit,msg)
{
        xwinindex = xwinindex+2;
        this.id      = id;
        this.width   = w;
        this.height  = h;
        this.left    = l;
        this.top     = t;
        this.zIndex  = xwinindex;
        this.title   = tit;
        this.message = msg;
        this.obj     = null;
        this.bulid   = bulid;
        this.close	 = xwincls;
        this.show	 = xwinshow;
        this.resize  = xwinresize;
        this.bulid();
}
//初始化;
function bulid()
{
        var str = ""
                + "<div id=xMsg" + this.id + " "
                + "style='"
                + "z-index:" + this.zIndex + ";"
                + "width:" + this.width + ";"
                + "height:" + this.height + ";"
                + "left:" + this.left + ";"
                + "top:" + this.top + ";"
                + "background-color:" + normal + ";"
                + "color:" + normal + ";"
                + "font-size:12px;"
                //+ "font-family:Verdana;"
                + "position:absolute;"
                + "cursor:default;"
                //+ "border:2px solid " + normal + ";"
                + "' >"
                //+ "onresize='xwinresize(this)'>"
                	+"<iframe id=xMsgBackFrame" + this.id 
                	+" width=" +this.width
                	+" height=" +this.height
                	+" frameborder=no></iframe>"
                        + "<div id=xMsgTitle" + this.id
                        + " style='"
                        + "z-index:1;width:" + this.width + ";"
                        + "height:21;"
                        + "position:absolute;"
                        + "left:0;top:0;"
                        + "background: url("+imgpath+"/hd-sprite.gif) no-repeat 0 -41px;"
                        + "padding-left:3px;margin:0;"
                        + "' "
                        + "onmousedown='startDrag(this)' "
                        + "onmouseup='stopDrag(this)' "
                        + "onmousemove='drag(this," + this.width + ")' "
                        //+ "ondblclick='min(this.childNodes[1])'"
                        + ">"
                        	+ "<div id=xMsgTitleBar" + this.id
                        	+ " style='width:" + (this.width - 3) + ";"
                        	+ "height:21;"
                        	+ "background:url("+imgpath+"/hd-sprite.gif) no-repeat right 0;"
                        	+ "padding-right:3px;'>"
                        		+ "<div id=xMsgTitleBarCon" + this.id
                        		+ " style='background:url("+imgpath+"/hd-sprite.gif) repeat-x 0 -82px;"
                        		+ "background-color:navy;"
                        		+ "width:" + (this.width - 6) + ";"
                        		+ "height:21;"
                        		+ "color:white;"
                        		+ "overflow:hidden;"
                        		+ "padding:5px;"
                        		+ "white-space: nowrap;"
                        		+ "font-weight: bold;' "
                        		+ "unselectable=on>"
                        		+ this.title + "</div>"
                        	+ "</div>"
                        + "</div>"
                        + "<div style='z-index:1;width:15;height:15;right:5;top:3;position:absolute;cursor:default;background:transparent;'>"
                        	+ "<img src='"+imgpath+"/close.gif' title='关闭' onclick='cls(this)'>"
                        + "</div>"
                                + "<div style='"
                                + "width:100%;"
                                + "height:" + (this.height-21) + ";"
                                + "background:url("+imgpath+"/gradient-bg.gif);"
                                + "border:1px solid #6593cf;"
                                + "border-top:0 none;"
                                + "padding:4px;"
                                + "position:absolute;"
                                + "color:black;"
                                + "top:21px;"
                                + "left:0;"
                                + "overflow:hidden;"
                                + "' id='xMsgCon" + this.id + "'>" + this.message + "</div>"
                + "</div>"
        document.body.insertAdjacentHTML("beforeEnd",str);
}

//以下为显示信息函数
function showmsg(title,content,width,height)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	if(width==null || width==0) width=200;
	if(height==null || height==0) height=160;
	var left = (document.body.clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (document.body.clientHeight - height) / 2;
	if(top<0) top=0;
	var w=new xWin(id ,width,height,left,top,title,content);
	return w;
}
//以下是显示弹出窗口函数
function showwin(title,url,width,height)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	if(width==null || width==0) width=200;
	if(height==null || height==0) height=160;
	var left = (document.body.clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (document.body.clientHeight - height) / 2;
	if(top<0) top=0;
	var w=new xWin(id ,width,height,left,top,title,"<iframe id='xMsgFrame" + id 
		+ "' frameborder=0 scrolling=auto src='"
		+url+"' width="+(width-10)+" height="+(height-29)+" ></iframe>");
	return w;
}

function setWinUrl(w, fr, url) {
	if(fr==null || w==null) return;
	fr.src = "";
	fr.src = url;
	w.show();
}
//刷新窗口
function refreshwin(w)
{
	if(w==null) return ;
	fr = document.getElementById("xMsgFrame" + w.id);
	if(fr==null) return;
	try {
		setWinUrl(w, fr, fr.src);
	}catch(e) {
		
	}
}
//更改窗口URL
function navwin(w, url, newtitle)
{
	if(w==null) return ;
	var fr = document.getElementById("xMsgFrame" + w.id);
	if(fr==null) return;
	try {
		setWinUrl(w, fr, url);
		//w.show();
		if(newtitle) {
			var vt = document.getElementById("xMsgTitleBarCon" + w.id);
			if(vt) vt.innerText = newtitle;
		}
	}catch(e) {
		
	}
}
//更改窗口内容
function updatewin(w, content, newtitle)
{
	if(w==null) return ;
	fr = document.getElementById("xMsgCon" + w.id);
	var vt = document.getElementById("xMsgTitleBarCon" + w.id);
	if(fr==null || vt==null) return;
	try {
		fr.innerHTML=content;
		if(newtitle) vt.innerText=newtitle;
		w.show();
	}catch(e) {
		
	}
}
//取消按钮
function cancelxwininput(w) {
	if(w) {
		w.close();
	}
}
//确定按钮
function okxwininput(w, func) {
	if(w) {
		var inp = document.getElementById("xWinInputbox" + w.id);
		w.close();
		func(inp.value);
	}
}
//msgbox框
function msgbox(title, msg)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var left = (document.body.clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (document.body.clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<p id='xWinMsgboxPrompt" + id +  " align='left' style='padding:0;'>" + msg 
		+ "</p><p align='center'  style='padding-bottom:0;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'><p>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var okinp = document.getElementById("xWinMsgboxOk"+id);
	okinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}
//录入框
function inputbox(title,promptstr,defaultstr, handler)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var left = (document.body.clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (document.body.clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<p id='xWinInputboxPrompt" + id +  " align='left' style='padding:0;'>" + promptstr 
		+ "</p><p align='center' style='padding:0;'><input type='text' id='xWinInputbox" + id 
		+ "' value='" + defaultstr + "'></p>" 
		+ "<p align='center'  style='padding-bottom:0;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'>"
		+ "&nbsp;&nbsp;<input type='button' id='xWinInputboxCancel"+id+"' class='Button_Silver' value='取消'><p>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var inp = document.getElementById("xWinInputbox" + w.id);
	inp.style.posWidth = width - 30;
	var okinp = document.getElementById("xWinInputboxOk"+id);
	okinp.attachEvent("onclick", function(){okxwininput(w, handler);});
	var cancelinp = document.getElementById("xWinInputboxCancel"+id);
	cancelinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}

function inputboxmulti(title,promptstr,defaultstr, handler)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var left = (document.body.clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (document.body.clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<div id='xWinInputboxPrompt" + id +  " align='left' style='display:block;padding:5;'>" + promptstr 
		+ "</div><div align='center' style='display:block;padding:5;'><textarea type='text' id='xWinInputbox" + id 
		+ "' style='width:"+(width-40)+";height:"+(height-95)+";overflow-x:hidden;overflow-y:auto;line-height:120%;'>" + defaultstr + "</textarea></div>" 
		+ "<div align='center'  style='display:block;padding-bottom:5;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'>"
		+ "&nbsp;&nbsp;<input type='button' id='xWinInputboxCancel"+id+"' class='Button_Silver' value='取消'><div>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var inp = document.getElementById("xWinInputbox" + w.id);
	inp.style.posWidth = width - 30;
	var okinp = document.getElementById("xWinInputboxOk"+id);
	okinp.attachEvent("onclick", function(){okxwininput(w, handler);});
	var cancelinp = document.getElementById("xWinInputboxCancel"+id);
	cancelinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}

