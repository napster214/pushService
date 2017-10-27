	//  日历 JavaScript 页面脚本控件，适用于微软的 IE （5.0以上）浏览器
	//  主调用函数是 getSelectDate(this,[object])和getSelectDate(this),[object]是控件输出的控件名，举两个例子：
	//  一、<input name="txt"><input type="button" value="选择日期" onclick="getSelectDate(this,document.all.txt)">
	//  二、<input onfocus="getSelectDate(this)">
	//  本日历的年份限制是（1000 - 9999）
	//  按ESC键关闭该控件
	//  在年和月的显示地方点击时会分别出年与月的下拉框
	//  控件外任意点击一点即可关闭该控件
	//===================================================== 参数设定部分 =======================================================
	var bMoveable = true;							//设置日历是否可以拖动
	var versionInfo = "JavaScript Date Picker";
	//==================================================== Web页面显示部分 =====================================================
	var strFrame;		//存放日历层的HTML代码
	document.writeln('<iframe id="IDDateLayer" frameborder="0" style="position:absolute;width:146;height:212;z-index:9998;display:none"></iframe>');
	strFrame = '<style>';
	strFrame += 'input.button{border-right:#020AAD 1px solid;border-top:#020AAD 1px solid;border-left:#020AAD 1px solid;';
	strFrame += 'border-bottom:#020AAD 1px solid;background-color:#FFF8EC;font-family:宋体;}';
	strFrame += 'td{font-size:9pt;font-family:宋体;}';
	strFrame += '</style>';
	strFrame += '<script>';
	strFrame += '	var datelayerx,datelayery;';/*存放日历控件的鼠标位置*/
	strFrame += '	var bDrag;';/*标记是否开始拖动*/
	strFrame += '	function document.onmousemove()';/*在鼠标移动事件中，如果开始拖动日历，则移动日历*/
	strFrame += '	{';
	strFrame += '		if(bDrag && window.event.button == 1)';
	strFrame += '		{';
	strFrame += '			var DateLayer = parent.document.all.IDDateLayer.style;';
	strFrame += '			DateLayer.posLeft += window.event.clientX-datelayerx;';/*由于每次移动以后鼠标位置都恢复为初始的位置，因此写法与div中不同*/
	strFrame += '			DateLayer.posTop += window.event.clientY-datelayery;';
	strFrame += '		}';
	strFrame += '	}';
	strFrame += '	function dragStart()';/*开始日历拖动*/
	strFrame += '	{';
	strFrame += '		var DateLayer = parent.document.all.IDDateLayer.style;';
	strFrame += '		datelayerx = window.event.clientX;';
	strFrame += '		datelayery = window.event.clientY;';
	strFrame += '		bDrag = true;';
	strFrame += '	}';
	strFrame += '	function dragEnd()';/*结束日历拖动*/
	strFrame += '	{';
	strFrame += '		bDrag = false;';
	strFrame += '	}';
	strFrame += '</script>';
	strFrame += '<div style="z-index:9999;position:absolute;left:0;top:0;" onselectstart="return false">';
	strFrame += '<span id="tmpSelectYearLayer" style="z-index:9999;position:absolute;top:3; left:19;display:none"></span>';
	strFrame += '<span id="tmpSelectMonthLayer" style="z-index:9999;position:absolute;top:3; left:78;display:none"></span>';
	strFrame += '<table border="1" cellspacing="0" cellpadding="0" width="142" height="160" bordercolor="#020AAD" bgcolor="#020AAD">';
	strFrame += '<tr>';
	strFrame += '<td width="142" height="23" bgcolor="#FFFFFF">';
	strFrame += '<table border="0" cellspacing="1" cellpadding="0" width="140" height="23">';
	strFrame += '<tr align="center">';
	strFrame += '<td width="16" align="center" bgcolor="#020AAD" style="cursor:hand;color:#FFFFFF" onclick="parent.IDPrevM()" title="向前翻 1 月"><b>&lt;</b>';
	strFrame += '</td>';
	strFrame += '<td width="60" align="center" style="cursor:default" onmouseover="style.backgroundColor=\'#FFD700\'" onmouseout="style.backgroundColor=\'white\'" ';
	strFrame += 'onclick="parent.selectYearInnerHTML(this.innerText.substring(0,4))" title="点击这里选择年份">';
	strFrame += '<span id="IDYearHead"></span>';
	strFrame += '</td>';
	strFrame += '<td width="48" align="center" style="cursor:default" onmouseover="style.backgroundColor=\'#FFD700\'" onmouseout="style.backgroundColor=\'white\'" ';
	strFrame += 'onclick="parent.selectMonthInnerHTML(this.innerText.length==3?this.innerText.substring(0,1):this.innerText.substring(0,2))" title="点击这里选择月份">';
	strFrame += '<span id="IDMonthHead"></span>';
	strFrame += '</td>';
	strFrame += '<td width="16" bgcolor="#020AAD" align="center" style="cursor:hand;color:#FFFFFF" onclick="parent.IDNextM()" title="向后翻 1 月"><b>&gt;</b>';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '</table>';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '<tr>';
	strFrame += '<td width="142" height="18">';
	strFrame += '<table border="1" cellspacing="0" cellpadding="0" bgcolor="#020AAD" ' + (bMoveable ? 'onmousedown="dragStart()" onmouseup="dragEnd()"' : '');
	strFrame += ' bordercolorlight="#020AAD" bordercolordark="#FFFFFF" width="140" height="20" style="cursor:' + (bMoveable ? 'move' : 'default') + '">';
	strFrame += '<tr align="center" valign="bottom">';
	strFrame += '<td style="color:#FFFFFF">日</td>';
	strFrame += '<td style="color:#FFFFFF">一</td><td style="color:#FFFFFF">二</td>';
	strFrame += '<td style="color:#FFFFFF">三</td><td style="color:#FFFFFF">四</td>';
	strFrame += '<td style="color:#FFFFFF">五</td><td style="color:#FFFFFF">六</td>';
	strFrame += '</tr>';
	strFrame += '</table>';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '<tr>';
	strFrame += '<td width="142" height="120">';
	strFrame += '<table border="1" cellspacing="2" cellpadding="0" bordercolorlight="#020AAD" bordercolordark="#FFFFFF" bgcolor="#FFF8EC" width="140" height="120">';

	var n = 0;
	for(j = 0;j < 5;j++)
	{
		strFrame += '<tr align="center">';
		for(i = 0;i < 7;i++)
		{
			strFrame += '<td width="20" height="20" id="IDDay' + n + '" style="font-size:12px" onclick="parent.getClickDay(this.innerText,0)"></td>';
			n++;
		}
		strFrame += '</tr>';
	}
	strFrame += '<tr align="center">';
	for(i = 35;i < 39;i++)
	{
		strFrame += '<td width="20" height="20" id="IDDay' + i + '" style="font-size:12px" onclick="parent.getClickDay(this.innerText,0)"></td>';
	}
	strFrame += '<td colspan="2" align="right"><span onclick="parent.clearValue()" style="cursor:hand" title="' + versionInfo + '">取消</span>&nbsp;</td>';
	strFrame += '<td colspan="1" align="right"><span onclick="parent.closeLayer2()" style="cursor:hand" title="' + versionInfo + '">Ｘ</span>&nbsp;</td>';
	strFrame += '</tr>';
	strFrame += '</table>';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '<tr>';
	strFrame += '<td>';
	strFrame += '<table border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#FFFFFF">';
	strFrame += '<tr>';
	strFrame += '<td align="left">';
	strFrame += '<input type="button" class="button" value="<<" title="向前翻 1 年" onclick="parent.preYear()" ';
	strFrame += ' onfocus="this.blur()" style="font-size:12px; height:20px"><input class="button" title="向前翻 1 月" type="button" ';
	strFrame += ' value="< " onclick="parent.IDPrevM()" onfocus="this.blur()" style="font-size:12px; height:20px">';
	strFrame += '</td>';
	strFrame += '<td align=center>';
	strFrame += '<input type=button class=button value=今天 onclick="parent.IDToday()" onfocus="this.blur()" title="当前日期" style="font-size:12px; height:20px; cursor:hand">';
	strFrame += '</td>';
	strFrame += '<td align=right>';
	strFrame += '<input type=button class=button value=" >" onclick="parent.IDNextM()" onfocus="this.blur()" title="向后翻 1 月" class=button style="font-size:12px; height:20px">';
	strFrame += '<input type=button class=button value=">>" title="向后翻 1 年" onclick="parent.nextYear()" onfocus="this.blur()" style="font-size:12px; height:20px">';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '</table>';
	strFrame += '</td>';
	strFrame += '</tr>';
	strFrame += '</table>';
	strFrame += '</div>';

	window.frames.IDDateLayer.document.writeln(strFrame);
	window.frames.IDDateLayer.document.close();//解决ie进度条不结束的问题

	//==================================================== Web 页面显示部分 ======================================================
	var outObject;
	var outButton;		//点击的按钮
	var outDate="";		//存放对象的日期
	var odatelayer = window.frames.IDDateLayer.document.all;//存放日历对象
	var outType = null;
	
	function getSelectDate(tt,obj,type)//主调函数
	{
		/*
		if(arguments.length >  2)
		{
			alert("对不起！传入本控件的参数太多！");
			return;
		}*/
		if(type){
			outType = type;
		}else{
			outType = null;
		}
		
		if(arguments.length == 0)
		{
			alert("对不起！您没有传回本控件任何参数！");
			return;
		}
		var dads = document.all.IDDateLayer.style;
		var th = tt;
		var ttop = tt.offsetTop;     	//TT控件的定位点高
		var thei = tt.clientHeight;  	//TT控件本身的高
		var tleft = tt.offsetLeft;    	//TT控件的定位点宽
		var ttyp = tt.type;         	//TT控件的类型
		while(tt = tt.offsetParent)
		{
			ttop += tt.offsetTop;
			tleft += tt.offsetLeft;
		}
		//dads.top  = (ttyp == "image")? ttop + thei : ttop+thei+6;
		//dads.left = tleft;
		ttop = (ttyp == "image")? ttop + thei : ttop+thei+6;
		var bodyheight = document.body.offsetHeight>document.body.scrollHeight?document.body.offsetHeight:document.body.scrollHeight;
		if(ttop + 212>bodyheight){
			ttop = bodyheight - 212;
		}
		dads.top = ttop;
		var bodywidth = document.body.offsetWidth>document.body.scrollWidth?document.body.offsetWidth:document.body.scrollWidth;
		if(tleft + 146>bodywidth){
			tleft = bodywidth - 146;
		}
		dads.left = tleft;
		
		outObject = (arguments.length == 1) ? th : obj;
		outButton = (arguments.length == 1) ? null : th;	//设定外部点击的按钮
		//根据当前输入框的日期显示日历的年月
		var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/;
		var r = outObject.value.match(reg);
		if(r != null)
		{
			r[2] = r[2] - 1;
			var d = new Date(r[1], r[2],r[3]);
			if(d.getFullYear() == r[1] && d.getMonth() == r[2] && d.getDate() == r[3])
			{
				outDate = d;		//保存外部传入的日期
			}
			else
			{
				outDate = "";
			}
			IDSetDay(r[1],r[2] + 1);
		}
		else
		{
			outDate = "";
			IDSetDay(new Date().getFullYear(), new Date().getMonth() + 1);
		}
		dads.display = '';
		event.returnValue = false;
	}

	var MonHead = new Array(12);    		   //定义阳历中每个月的最大天数
    MonHead[0] = 31; MonHead[1] = 28; MonHead[2] = 31; MonHead[3] = 30; MonHead[4]  = 31; MonHead[5]  = 30;
    MonHead[6] = 31; MonHead[7] = 31; MonHead[8] = 30; MonHead[9] = 31; MonHead[10] = 30; MonHead[11] = 31;

	var IDTheYear = new Date().getFullYear();	//定义年的变量的初始值
	var IDTheMonth = new Date().getMonth() + 1;	//定义月的变量的初始值
	var IDWDay = new Array(39);               	//定义写日期的数组

	function onclick() //任意点击时关闭该控件	//ie6的情况可以由下面的切换焦点处理代替
	{
		with(window.event)
		{
			if(srcElement.getAttribute("Author")==null && srcElement != outObject && srcElement != outButton)
			{
				closeLayer();
			}
		}
	}

	function onkeyup()		//按Esc键关闭，切换焦点关闭
	{
	    if(window.event.keyCode == 27)
	    {
			if(outObject)outObject.blur();
			{
				closeLayer();
			}
		}
		else if(document.activeElement)
		{
			if(document.activeElement.getAttribute("Author")==null && document.activeElement != outObject && document.activeElement != outButton)
			{
				closeLayer();
			}
		}
	}

	function writeHead(yy,mm)  //往 head 中写入当前的年与月
	{
		odatelayer.IDYearHead.innerText  = yy + " 年";
		odatelayer.IDMonthHead.innerText = mm + " 月";
	}

	function selectYearInnerHTML(strYear) //年份的下拉框
	{
		if(strYear.match(/\D/)!=null)
		{
			alert("年份输入参数不是数字！");
			return;
		}
		var m = (strYear) ? strYear : new Date().getFullYear();
		if(m < 1000 || m > 9999)
		{
			alert("年份值不在 1000 到 9999 之间！");
			return;
		}
		var n = m - 10;
		if(n < 1000) n = 1000;
		if(n + 26 > 9999) n = 9974;
		var s = "<select name=tmpSelectYear style='font-size:12px' ";
		s += "onblur='document.all.tmpSelectYearLayer.style.display=\"none\"' ";
		s += "onchange='document.all.tmpSelectYearLayer.style.display=\"none\";";
		s += "parent.IDTheYear = this.value;";
		s += "parent.IDSetDay(parent.IDTheYear,parent.IDTheMonth)'>\r\n";
		var selectInnerHTML = s;
		for(var i = n-100; i < n + 26; i++)
		{
			if(i == m)
			{
				selectInnerHTML += "<option value='" + i + "' selected>" + i + "年" + "</option>\r\n";
			}
			else
			{
				selectInnerHTML += "<option value='" + i + "'>" + i + "年" + "</option>\r\n";
			}
		}
		selectInnerHTML += "</select>";
		odatelayer.tmpSelectYearLayer.style.display = "";
		odatelayer.tmpSelectYearLayer.innerHTML = selectInnerHTML;
		odatelayer.tmpSelectYear.focus();
	}

	function selectMonthInnerHTML(strMonth) //月份的下拉框
	{
		if(strMonth.match(/\D/)!=null)
		{
			alert("月份输入参数不是数字！");
			return;
		}
		var m = (strMonth) ? strMonth : new Date().getMonth() + 1;
		var s = "<select name=tmpSelectMonth style='font-size:12px' ";
		s += "onblur='document.all.tmpSelectMonthLayer.style.display=\"none\"' ";
		s += "onchange='document.all.tmpSelectMonthLayer.style.display=\"none\";";
		s += "parent.IDTheMonth = this.value;";
		s += "parent.IDSetDay(parent.IDTheYear,parent.IDTheMonth)'>\r\n";
		var selectInnerHTML = s;
		for(var i = 1; i < 13; i++)
		{
			if(i == m)
			{
				selectInnerHTML += "<option value='"+i+"' selected>" + i + "月" + "</option>\r\n";
			}
			else
			{
				selectInnerHTML += "<option value='"+i+"'>" + i + "月" + "</option>\r\n";
			}
		}
		selectInnerHTML += "</select>";
		odatelayer.tmpSelectMonthLayer.style.display = "";
		odatelayer.tmpSelectMonthLayer.innerHTML = selectInnerHTML;
		odatelayer.tmpSelectMonth.focus();
	}

	function closeLayer()               //这个层的关闭
	{
		document.all.IDDateLayer.style.display = "none";
	}
	
	function closeLayer2()               //这个层的关闭
	{
		document.all.IDDateLayer.style.display = "none";
		try{
			if(outType != null){
				extOper(outType);
			}
		}catch(Exception){
		}
	}

	function isPinYear(year)            //判断是否闰平年
	{
		if(0 == year % 4 && ((year % 100 != 0) || (year % 400 == 0)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	function getMonthCount(year,month)  //闰年二月为29天
	{
		var c = MonHead[month - 1];
		if((month == 2) && isPinYear(year))
		{
			c++;
		}
		return c;
	}

	function getWeek(day,month,year)     //求某天的星期几
	{
		var dt = new Date(year,month - 1,day).getDay() / 7;
		return dt;
	}

	function preYear()  //往前翻 Year
	{
		if(IDTheYear > 999 && IDTheYear <10000)
		{
			IDTheYear--;
		}
		else
		{
			alert("年份超出范围（1000-9999）！");
		}
		IDSetDay(IDTheYear,IDTheMonth);
	}

	function nextYear()  //往后翻 Year
	{
		if(IDTheYear > 999 && IDTheYear <10000)
		{
			IDTheYear++;
		}
		else
		{
			alert("年份超出范围（1000-9999）！");
		}
		IDSetDay(IDTheYear,IDTheMonth);
	}

	function IDToday()  //Today Button
	{
		var today;
		IDTheYear = new Date().getFullYear();
		IDTheMonth = new Date().getMonth() + 1;
		today = new Date().getDate();
		//IDSetDay(IDTheYear,IDTheMonth);
		if(outObject)
		{
			outObject.value = IDTheYear + "-" + IDTheMonth + "-" + today;
		}
		closeLayer2();
	}

	function IDPrevM()  //往前翻月份
	{
		if(IDTheMonth > 1)
		{
			IDTheMonth--;
		}
		else
		{
			IDTheYear--;
			IDTheMonth = 12;
		}
		IDSetDay(IDTheYear,IDTheMonth);
	}

	function IDNextM()  //往后翻月份
	{
		if(IDTheMonth == 12)
		{
			IDTheYear++;
			IDTheMonth = 1;
		}
		else
		{
			IDTheMonth++;
		}
		IDSetDay(IDTheYear,IDTheMonth);
	}

	function IDSetDay(yy,mm)   //主程序
	{
		writeHead(yy,mm);
		//设置当前年月的公共变量为传入值
		IDTheYear=yy;
		IDTheMonth=mm;

		for(var i = 0; i < 39; i++)
		{
			IDWDay[i] = "";//将显示框的内容全部清空
		}
		var day1 = 1,day2 = 1,firstday = new Date(yy,mm-1,1).getDay();  //某月第一天的星期几
		//上个月的最后几天
		for(i = 0;i < firstday;i++)
		{
			IDWDay[i] = getMonthCount(mm == 1 ? yy - 1 : yy , mm == 1 ? 12 : mm - 1) - firstday + i + 1;
		}
		for(i = firstday;day1 < getMonthCount(yy,mm) + 1;i++)
		{
			IDWDay[i] = day1;
			day1++;
		}
		for(i = firstday + getMonthCount(yy,mm);i < 39;i++)
		{
			IDWDay[i] = day2;
			day2++;
		}
		for(i = 0; i < 39; i++)
		{
			var da = eval("odatelayer.IDDay" + i)     //书写新的一个月的日期星期排列
			if(IDWDay[i] != "")
			{
				//初始化边框
				da.borderColorLight = "#020AAD";
				da.borderColorDark = "#FFFFFF";
				if(i<firstday)		//上个月的部分
				{
					da.innerHTML="<b><font color='gray'>" + IDWDay[i] + "</font></b>";
					da.title = (mm == 1 ? 12 : mm - 1) + "月" + IDWDay[i] + "日";
					da.onclick = Function("getClickDay(this.innerText,-1)");
					if(!outDate)
					{
						da.style.backgroundColor = ((mm == 1 ? yy - 1 : yy) == new Date().getFullYear() && (mm == 1 ? 12 : mm - 1) == new Date().getMonth()+1 && IDWDay[i] == new Date().getDate()) ? "#FFD700":"#E0E0E0";
					}
					else
					{
						da.style.backgroundColor =((mm == 1 ? yy - 1 : yy) == outDate.getFullYear() && (mm == 1 ? 12 : mm - 1)== outDate.getMonth() + 1 && IDWDay[i]==outDate.getDate())? "#00FFFF" : (((mm == 1 ? yy - 1 : yy) == new Date().getFullYear() && (mm == 1 ? 12 : mm - 1) == new Date().getMonth() + 1 && IDWDay[i] == new Date().getDate()) ? "#FFD700":"#E0E0E0");
						//将选中的日期显示为凹下去
						if((mm == 1 ? yy - 1 : yy) == outDate.getFullYear() && (mm == 1 ? 12 : mm - 1) == outDate.getMonth() + 1 && IDWDay[i] == outDate.getDate())
						{
							da.borderColorLight = "#FFFFFF";
							da.borderColorDark = "#020AAD";
						}
					}
				}
				else if(i >= firstday + getMonthCount(yy,mm))		//下个月的部分
				{
					da.innerHTML = "<b><font color='gray'>" + IDWDay[i] + "</font></b>";
					da.title = (mm == 12 ? 1 : mm + 1) + "月" + IDWDay[i] + "日";
					da.onclick = Function("getClickDay(this.innerText,1)");
					if(!outDate)
					{
						da.style.backgroundColor = ((mm == 12 ? yy + 1 : yy) == new Date().getFullYear() && (mm == 12 ? 1 : mm + 1) == new Date().getMonth() + 1 && IDWDay[i] == new Date().getDate()) ? "#FFD700":"#E0E0E0";
					}
					else
					{
						da.style.backgroundColor =((mm == 12 ? yy + 1 : yy) == outDate.getFullYear() && (mm == 12 ? 1 : mm + 1)== outDate.getMonth() + 1 && IDWDay[i] == outDate.getDate()) ? "#00FFFF" : (((mm == 12 ? yy + 1 : yy) == new Date().getFullYear() && (mm == 12 ? 1 : mm + 1) == new Date().getMonth() + 1 && IDWDay[i] == new Date().getDate()) ? "#FFD700":"#E0E0E0");
						//将选中的日期显示为凹下去
						if((mm == 12 ? yy + 1 : yy) == outDate.getFullYear() && (mm == 12 ? 1 : mm + 1) == outDate.getMonth() + 1 && IDWDay[i]==outDate.getDate())
						{
							da.borderColorLight = "#FFFFFF";
							da.borderColorDark = "#020AAD";
						}
					}
				}
				else//本月的部分
				{
					da.innerHTML = "<b>" + IDWDay[i] + "</b>";
					da.title = mm + "月" + IDWDay[i] + "日";
					da.onclick = Function("getClickDay(this.innerText,0)");		//给td赋予onclick事件的处理
					//如果是当前选择的日期，则显示亮蓝色的背景；如果是当前日期，则显示暗黄色背景
					if(!outDate)
					{
						da.style.backgroundColor = (yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && IDWDay[i] == new Date().getDate())? "#FFD700":"#E0E0E0";
					}
					else
					{
						da.style.backgroundColor = (yy == outDate.getFullYear() && mm == outDate.getMonth() + 1 && IDWDay[i] == outDate.getDate()) ? "#00FFFF" : ((yy == new Date().getFullYear() && mm == new Date().getMonth() + 1 && IDWDay[i] == new Date().getDate()) ? "#FFD700" : "#E0E0E0");
						//将选中的日期显示为凹下去
						if(yy==outDate.getFullYear() && mm== outDate.getMonth() + 1 && IDWDay[i] == outDate.getDate())
						{
							da.borderColorLight = "#FFFFFF";
							da.borderColorDark = "#020AAD";
						}
					}
				}
				da.style.cursor = "hand";
			}
			else
			{
				da.innerHTML = "";
				da.style.backgroundColor = "";
				da.style.cursor = "default";
			}
		}
	}

	function clearValue()
	{
		outObject.value = "";
		closeLayer2();
	}

	function getClickDay(n,ex)  //点击显示框选取日期，主输入函数
	{
		var yy = IDTheYear;
		var mm = parseInt(IDTheMonth) + ex;//ex表示偏移量，用于选择上个月份和下个月份的日期
		//判断月份，并进行对应的处理
		if(mm < 1)
		{
			yy--;
			mm = 12+mm;
		}
		else if(mm > 12)
		{
			yy++;
			mm = mm-12;
		}
		if(mm < 10)
		{
			mm = "0" + mm;
		}
		if(outObject)
		{
			if(!n)
			{
				//outObject.value = "";
				return;
			}
			if(n < 10)
			{
				n = "0" + n;
			}
			outObject.value = yy + "-" + mm + "-" + n ; //注：在这里你可以输出改成你想要的格式
			closeLayer2();
		}
		else
		{
			closeLayer();
			alert("您所要输出的控件对象并不存在！");
		}
	}

	function getToday(strSplit)
	{
		var result = "";
		var d = new Date();
		var year = d.getFullYear();
		var month = d.getMonth() + 1;
		month = (month < 10) ? "0" + month : month;
		var day = d.getDate();
		day = (day < 10) ? "0" + day : day;
		result = year + strSplit + month + strSplit + day;
		return result;
	}

	function getMonthBegin(strSplit)
	{
		var result = "";
		var d = new Date();
		var year = d.getFullYear();
		var month = d.getMonth() + 1;
		month = (month < 10) ? "0" + month : month;
		var day = d.getDate();
		day = (day < 10) ? "0" + day : day;
		result = year + strSplit + month + strSplit + day;
		return result;
	}

	function getMonthEnd(strSplit)
	{
		var result = "";
		var d = new Date();
		var year = d.getFullYear();
		var month = d.getMonth() + 1;
		month = (month < 10) ? "0" + month : month;
		var day = d.getDate();
		day = (day < 10) ? "0" + day : day;
		result = year + strSplit + month + strSplit + day;
		return result;
	}
