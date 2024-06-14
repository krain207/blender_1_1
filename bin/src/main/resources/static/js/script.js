// gnb
menustart = function(DivName, menuseq) {
	$("#" + DivName + ">ul>li").each(function(idx, item) {
		item = $(item);
		item.find(">div").hide();

		if(idx == menuseq) {
			var img = $(item).find("img");
			if(img.attr("src").indexOf("_on.gif") == -1)
				img.attr("src", img.attr("src").replace(".gif", "_over.gif"));
			
			img.attr("isSelected", 1);
			item.attr("isSelected", 1);
			item.find(">div").show();
		}
	});

	$("#" + DivName + ">ul>li").hover(
		function(event) {
			onHover(event.currentTarget);
		},
		function(event) {
			onOut(event.currentTarget);
		}
	);

	$("#" + DivName + ">ul>li>a").focus(
		function(event) {
			onHover(event.currentTarget);
		}
	);
};

function onHover(item) {
	if(item.tagName == 'A')
		item = item.parentNode;

	item = $(item);

	item.parent().find(">li>div").hide();
	item.parent().find(">li>a>img").each(
		function(idx, item) {
			item = $(item);
			item.attr("src", item.attr("src").replace("_over.gif", ".gif"));
		}
	);


	item.find(">div").show();
	item.attr("isSelected", 1);
	var img = item.find(">a>img");

	if(img.attr("src").indexOf("_on.gif") == -1)
		img.attr("src", img.attr("src").replace(".gif", "_over.gif"));
}

function onOut(item) {
	item = $(item);

	item.parent().find(">li>a>img").each(
		function(idx, item) {
			item = $(item);
			if(item.attr("isSelected") == 1) {
				if(item.attr("src").indexOf("_on.gif") == -1)
					item.attr("src", item.attr("src").replace(".gif", "_over.gif"));
				item.parent().parent().find(">div").show();
			} else {
				item.attr("src", item.attr("src").replace("_over.gif", ".gif"));
				item.parent().parent().find(">div").hide();
			}
		}
	);
}



//quick
var com = {};
com.eitetu = {};

com.eitetu.SwitchBanner = function(btn, target) {
	var len = btn.length;
	var item = null;
	var button = null;
	var self = this;
	this.items = new Array();
	this.buttons = new Array();
	for(var i=0; i<len; i++) {
		item = $(target[i]);
		button = $(btn[i]);
		button.click(function() {
			self.__changeHandler(this);
		});

		this.items.push(item);
		this.buttons.push(button);
	}
	this.selectedIndex = -1;
	this.next();
};

com.eitetu.SwitchBanner.prototype = {
	items:null,
	buttons:null,
	selectedIndex:null,
	__interval:null,
	start:function() {
		if(this.__interval == null) {
			var self = this;
			this.__interval = setInterval(function() {
				self.next();
			}, 3000);
		}
	},
	stop:function() {
		if(this.__interval != null) {
			clearInterval(this.__interval);
		}
	},
	next:function() {
		if((this.items.length - 2) < this.selectedIndex)
			this.selectedIndex = -1;
		
		this.selectedIndex++;
		this.__changeHandler(this.buttons[this.selectedIndex]);
	},
	prev:function() {
		if(this.selectedIndex < 1)
			this.selectedIndex = this.items.length;
		
		this.selectedIndex--;

		this.__changeHandler(this.button[this.selectedIndex]);
	},
	__changeHandler:function(target) {
		target = $(target);
		var len = this.buttons.length;
		var img;
		for(var i=0; i<len; i++) {
			img = this.buttons[i].find("img:first-child");
			if(target.attr("id") == this.buttons[i].attr("id")) {
				if(img.attr("src").indexOf("_on.gif") == -1) {
					img.attr("src",img.attr("src").replace(".gif", "_over.gif"));
				}

				this.items[i].show();
				this.selectedIndex = i;
			} else {
				img.attr("src",img.attr("src").replace("_over.gif", ".gif"));
				this.items[i].hide();
			}
		}
	}
};


//quick�߰�
function scrollquick(name,bottomlimit){
	var topdis=50;
	if(!document.getElementById(name)){
		return;
	}
	var obj = $("#"+name);
	var btmlimit=parseInt(document.documentElement.scrollHeight-obj.height()-bottomlimit);
	var menuYloc = parseInt(obj.css("top").substring(0, obj.css("top").indexOf("px")));
	$(window).scroll(function() {
		if(menuYloc<$(document).scrollTop()){
			
		}else{
			topdis = menuYloc;
		}
		offset = topdis+parseInt($(document).scrollTop()) + "px";
		if(parseInt(offset)>btmlimit||menuYloc>btmlimit){
			return;
		}
		obj.animate({ top: offset }, { duration: 500, queue: false });
	});
}


function fn_show(id){
		dis = document.getElementById(id).style.display;
		if(dis =="none"){
			document.getElementById(id).style.display = "";
		}else{
			document.getElementById(id).style.display = "none";
		}
	}	


function makeWin(url, winname, width, height, scrolltype) {
    xposition = 0; yposition = 0;
    if (parseInt(navigator.appVersion) >= 4) {
        xposition = (screen.width - width) / 2;
        yposition = (screen.height - height) / 2;
    }
    args = "width=" + width + "," + "height=" + height + "," + "location=0," + "menubar=0," + "resizable=0,"
         + "scrollbars=" + scrolltype + "," + "status=0," + "titlebar=0," + "toolbar=0," + "hotkeys=0,"
		 + "screenx=" + xposition + ","  //NN Only
		 + "screeny=" + yposition + ","  //NN Only
		 + "left=" + xposition + ","     //IE Only
		 + "top=" + yposition;           //IE Only

    newWin = window.open(url, winname, args)
    newWin.focus();
}

function fn_weblist(op){
		for(i=1; i<3; i++){
			if(i==op){
				document.getElementById('cam_move_'+i).src= "/pmo_web/data/img/sub04/h/h_vod1_btn"+i+"on.gif";
				document.getElementById('h_vod_view'+i).style.display = "";
			}else{
				document.getElementById('cam_move_'+i).src= "/pmo_web/data/img/sub04/h/h_vod1_btn"+i+".gif";	
				document.getElementById('h_vod_view'+i).style.display = "none";
			}
		}
	}	  

	function fn_weblist2(op){
		for(i=1; i<4; i++){
			if(i==op){
				document.getElementById('cam_move2_'+i).src= "/pmo_web/data/img/sub04/h/h_vod2_btn"+i+"on.gif";
				document.getElementById('h_vod_view'+i+'_1').style.display = "";
			}else{
				document.getElementById('cam_move2_'+i).src= "/pmo_web/data/img/sub04/h/h_vod2_btn"+i+".gif";	
				document.getElementById('h_vod_view'+i+'_1').style.display = "none";
			}
		}
	}	  
