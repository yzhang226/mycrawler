
//if (typeof Date.prototype.format != 'function') {
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
	// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
	Date.prototype.format = function (fmt) {
	    var o = {
	        "M+": this.getMonth() + 1, 
	        "d+": this.getDate(), // day
	        "h+": this.getHours(),
	        "m+": this.getMinutes(), 
	        "s+": this.getSeconds(), 
	        "q+": Math.floor((this.getMonth() + 3) / 3), // Quarter
	        "S": this.getMilliseconds() 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	};
//}

if (typeof String.prototype.contains != 'function') { 
	String.prototype.contains = function(it) { 
		return this.indexOf(it) != -1; 
	}; 
}

if (typeof String.prototype.trim != 'function') {
	String.prototype.trim = function(){
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
}
if (typeof String.prototype.startsWith != 'function') {
      String.prototype.startsWith = function (str){
         return this.slice(0, str.length) == str;
      };
}
if (typeof String.prototype.endsWith != 'function') {
   String.prototype.endsWith = function (str){
      return this.slice(-str.length) == str;
   };
}

if (typeof Array.prototype.contains != 'function') {
	Array.prototype.contains = function(obj) {
	    var i = this.length;
	    while (i--) {
	        if (this[i] === obj) return true;
	    }
	    return false;
	};
}

if(typeof Array.prototype.indexOf != 'function') {
    Array.prototype.indexOf = function(what, i) {
        i = i || 0;
        var L = this.length;
        while (i < L) {
            if(this[i] === what) return i;
            ++i;
        }
        return -1;
    };
}

if(typeof Array.prototype.remove != 'function') {
	Array.prototype.remove = function() {
	    var what, a = arguments, L = a.length, ax;
	    while (L && this.length) {
	        what = a[--L];
	        while ((ax = this.indexOf(what)) !== -1) {
	            this.splice(ax, 1);
	        }
	    }
	    return this;
	};
}

function StringBuilder(value) {
 this.strings = new Array("");
 this.append(value);
};

StringBuilder.prototype.append = function (value) {
 if (value) {
     this.strings.push(value);
 }
 return this;
};

StringBuilder.prototype.clear = function () {
 this.strings.length = 0;
};

StringBuilder.prototype.toString = function () {
 return this.strings.join("");
};

jQuery.validator.addMethod("website_url", function(val, elem) {
    if (val.length == 0) { return true; }
 
    return /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test(val);
}, 'Please enter a valid URL.');

//'ipv4': IPv4 Address Validator
jQuery.validator.addMethod('ipv4', function(value, elem) {
    // var ipv4 = /^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$/;    
    var ipv4 = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/; 
    return value.match(ipv4);
}, 'Invalid IPv4 address');

// 'netmask': IPv4 Netmask Validator
jQuery.validator.addMethod('netmask', function(value, elem) {
    var mask = /^[1-2]{1}[2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-9]{1,3}$/;    
    return value.match(mask);
}, 'Invalid IPv4 netmask');

var tableId;
var selectedObjIds = new Array();

(function($) {
	$.fn.tcgtable = function(options) {
		
		var settings = $.extend({
			rowSelect : "single",
			needPagination : "true"
		}, options);
		
		var isMultiple = settings.rowSelect == 'multiple';
		var isNeedPagination = settings.needPagination == 'true';
		
		tableId = this.attr('id');
		
		var thead = this.children('thead');
		var tbody = this.children('tbody');
		var upColumnsDiv = $('#__columns');
		var popupColsDiv = $('#pop_up_cols');
		
		
		// alert(thead.children().length + ", " + tbody.children().length);
		
	    // var trs = $("#__grid__ tr[class='odd gradeX']");
		var tbodyRows = tbody.children('tr');
	   
	    // init id, color of each rows in table 
	    var tr, tds, td, tdId, tdText;
	    for (var i=0; i<tbodyRows.length; i++) {
		   tr = $(tbodyRows[i]);
		   tr.attr('id', '_'+ i + '_');
		   
		   tr.css('background-color', i % 2 == 0 ? even_color : odd_color);
		   
		   tds = tr.children();
		   for (var j=0; j<tds.length; j++) {
			   $(tds[j]).attr('id', '_'+j+'_'+i);
		   }
	    }
	    
    	var fields = thead.children("#_head_fields_").first().children();
    	if (fields.length > 0) {
    		for ( var i = 0; i < fields.length; i++) {
		    	$(fields[i]).attr('id', "field_" + i);
		    }
		    $('#_sortAscLink').click(function(e) {
		    	sortByField('asc');
		    });
		    $('#_sortDescLink').click(function(e) {
		    	sortByField('desc');
		    });
    	} else {// _head_fields_ cursor: not-allowed;
    		$('#_sortAscLink').css('cursor', 'auto');
    		$('#_sortDescLink').css('cursor', 'auto');
    	}
		    
	 	// init id, sort of each columns in table 
		var oldTdId, colspan, secIndex = 0, subColNo = -1;
	   	var headTds = thead.children("#_table_head_").first().children();
		for ( var i = 0; i < headTds.length; i++) {
			td = $(headTds[i]);
			oldTdId = td.attr('id');
			tdId = '_' + i;
			td.attr('id', tdId);
			
			tdText = td.text();
			td.text('');
			
			colspan = $(td).attr('colspan');
			if (!isNullOrUndefined(colspan)) {
				var secTds = td.parent().next().children();
				var oldIndex = secIndex;
				var n = 0;
				secIndex = secIndex + Number(colspan);
				for (var j=oldIndex; j<secIndex; j++) {
					$(secTds[j]).attr('id', tdId + "_sec_"+ n++);
					if (td.css('display') == 'none') {
						$(secTds[j]).css("display", "none");
					} else {
						$(secTds[j]).css("background-color", "#D3E2F4");
					}
				}
			}
			
			if (td.css('display') == 'none') {
				td.css('display', 'none');
			} else {
				td.css('background-color', '#D3E2F4');
			}
			
			subColNo++;
			if (td.css('display') != 'none' && tdText != '') {
				var colDiv = $('<div></div>');
				var subColNos = subColNo;
				
				if (!isNullOrUndefined(colspan)) {
					for (var n=1; n<Number(colspan); n++) {
						subColNo++;
						subColNos = subColNos + "," + subColNo;
					}
				}
				
				var onclk = 'changeColsStatus(this, "' + i + ',' + subColNos + '")';
				// alert('onclk is ' + onclk);
				var colBox = $('<i></i>', {'id': '0', 'onclick': onclk, 'class': 'icon-check'});
				var colSpan = $('<span></span>');
				colSpan.text(tdText);
				colDiv.append(colBox); colDiv.append(colSpan);
				
				upColumnsDiv.append(colDiv);
			}
			
			var div1 = $('<div></div>', {
				id : tdId + '_div1',
				style: "overflow: hidden; position: relative; width: 100%;"
			});

			var div11 = $('<div></div>', {
				id : tdId + '_div11',
				style : 'overflow: hidden; float: left; width: 90%; position: relative;'
			});
			var span111 = $('<span></span>', {
				id : tdId + '_span111', 
				style : 'width: 100%;'
			});
			span111.text(tdText);
			// display: block; 
			var div12 = $('<div></div>', {
				id : tdId + '_div12',
				style: "overflow: hidden; float: right; width: 10%;  position: relative; "
			});
			var img121 = $('<img />', {
				id : tdId + '_img121',
				src : img_path + '/sort_desc.gif',
				style : 'width: 100%;',
				'class': 'test'
			});
			
			div11.append(span111);
			div12.append(img121);
			
			div1.append(div11); div1.append(div12);
			td.append(div1);

			img121.css('visibility', 'hidden');
			
			if (oldTdId != '_un00_') {
			div1.click(function(e) {
				e.stopPropagation();
				var div12 = $($(this).children()[1]);
				
				var top = $(this).parent().offset().top + $(this).parent().outerHeight();
				var left = div12.offset().left;
				
				
				if (left + $('#pop-up').outerWidth( true ) > $(document).width()) {
					left = left - $('#pop-up').outerWidth( true ) + div12.outerWidth( true );
				}
				
				$("div#pop-up").css('top', top).css('left', left);//  + $(this).width() 
				
				$('#clickedTdId').val($(this).parent().attr('id'));
				
				$('div#pop-up').show();
				$('div#__columns').hide();
			});
			
			div1.mouseover(function (e) {
				var right = $('#' + $(this).children()[1].id);
				$(this).parent().css('background-color', '#D5E5FA');
				$('#' + right.children()[0].id).removeAttr("style");
			});
			div1.mouseout(function (e) {
				var right = $('#' + $(this).children()[1].id);
				$(this).parent().css('background-color', '#D3E2F4');
				$('#' + right.children()[0].id).css('visibility', 'hidden');
			});
			}
		}
		
		if (isMultiple) {
			var hearderBox = createHeaderCheckbox();
			hearderBox.insertBefore('#'+$(headTds[0]).attr('id'));
			
			 for (var i=0; i<tbodyRows.length; i++) {
				var rowBox = createRowCheckbox();
				rowBox.insertBefore('#' + $($(tbodyRows[i]).children()[0]).attr('id'));
			}
			
		}
		
		
	   
	    // init click event of each row in talbe $("#__grid__ tr[class='odd gradeX']")
		tbodyRows.click(function() {
			
		 var trs = $(this).parent().children();
		 
		 for (var i=0; i<trs.length; i++) {
			 if (i % 2 == 0) {
				 trs[i].style.backgroundColor = even_color;
			 } else {
				 trs[i].style.backgroundColor = odd_color;
			 }
		 }
		 
		 var lastRowIdInput = $('#lastRowId');
		 var lastColorInput = $('#lastColor');
//		 var clickedObjIdInput = $('#clickedObjId');
		 
		 var lastRowId = lastRowIdInput.val();
		 var lastColor = lastColorInput.val();
		 
		 var selectedId = $(this).attr('id');
		 var currentSelectedColor = $(this).css('background-color');
		 
		 if (lastRowId == '') {// no rows be selected 
			 lastRowIdInput.val(selectedId);
			 lastColorInput.val(currentSelectedColor);
//			 clickedObjIdInput.val($(this).children()[0].innerHTML);
			 
			 $(this).removeAttr("style");
			 $(this).css('background-color', selected_color);
		 } else {
			 if (selectedId == lastRowId) {// select the same row 
				 lastRowIdInput.val('');
				 lastColorInput.val('');
//				 clickedObjIdInput.val('');
				 
				 $(this).removeAttr("style");
				 $(this).css('background-color', lastColor);
			 } else {// select another row 
				 lastRowIdInput.val(selectedId);
				 lastColorInput.val(currentSelectedColor);
//				 clickedObjIdInput.val($(this).children()[0].innerHTML);
				 
				 $(this).removeAttr("style");
				 $(this).css('background-color', selected_color);
			 }
		 }
		 });
		
		if (isNeedPagination) {
			var paginationLinks = $("div[class='pagination pagination-centered'] ul li a");
		    // alert("paginationLinks.length is " + paginationLinks.length);
		    for (var i=0; i<paginationLinks.length-1; i++) {
		    	var pLink = $(paginationLinks[i]);
		    	var pageNum = pLink.attr('id');
		    	if (!isNullOrUndefined(pageNum)) {
		    		pageNum = Number(pageNum);
		    		if (pageNum > 0) {
		    			var url = combineFullUrl(null, $('#order').val(), $('#orderBy').val(), pageNum, $('#statusId').val());
		    			
		    			pLink.attr('href', url);
		    		}
		    	}
		    }
		}
	    
	   
	    popupColsDiv.mouseover(function (e) {
	    	var colsDiv = $("div#__columns");
			var p = $(this).offset();
			var top = p.top - colsDiv.height()/2;
			var left = $(this).parent().offset().left + $(this).parent().outerWidth( true ); // + $(this).width();
			
			if (left + colsDiv.outerWidth( true ) > $(document).width()) {
				left = $(this).parent().offset().left - colsDiv.outerWidth( true );
			}
			
			colsDiv.css('top', top).css('left', left);//  + $(this).width()
			colsDiv.show();
		});
		
	    popupColsDiv.mouseout(function (e) {
			// $('div#__columns').hide();
		});
		
		upColumnsDiv.mouseover(function (e) {
			e.stopPropagation();
			// $('div#__columns').hide();
		});
		
		$(document).click(function() {
			$('div#pop-up').hide();
			$('div#__columns').hide();
		});
		
		$('#pop-up').click(function(event) {
			event.stopPropagation();
		});
		upColumnsDiv.click(function(event) {
			event.stopPropagation();
		});
		
		function createHeaderCheckbox() {
			// <i class='${card.rechargeSupported ? "icon-check" : "icon-check-empty" }'> td.css('background-color', '#D3E2F4');
			var th = $('<th></th>', {style: 'background-color: #D3E2F4'}).append($('<i></i>', {'class': 'icon-check-empty', 'value': 0}));
			th.unbind('click').click(function() {
				var box = $($(this).children()[0]);
				var boxValue = box.attr('value');
				if (boxValue == 0) {// 
					box.attr('class', 'icon-check');
					box.attr('value', 1);
					var uncheckeds = tcgtableObj().children('tbody').find('i[head="1"][class="icon-check-empty"]');
					for (var i=0; i<uncheckeds.length; i++) {
						var un = $(uncheckeds[i]);
						un.attr('class', 'icon-check');
						un.attr('value', 1);
						selectedObjIds.push(un.parent().parent().attr('objid'));
					}
				} else {
					box.attr('class', 'icon-check-empty');
					box.attr('value', 0);
					var checkeds = tcgtableObj().children('tbody').find('i[head="1"][class="icon-check"]');
					for (var i=0; i<checkeds.length; i++) {
						var un = $(checkeds[i]);
						un.attr('class', 'icon-check-empty');
						un.attr('value', 0);
						selectedObjIds.remove(un.parent().parent().attr('objid'));
					}
				}
				
				 $('#clickedObjId').val(selectedObjIds.join());
			});
			
			return th;
		}
		
		function createRowCheckbox() {
			// <i class='${card.rechargeSupported ? "icon-check" : "icon-check-empty" }'>
			var td = $('<td></td>').append($('<i></i>', {'class': 'icon-check-empty', value: 0, head: 1}));
			td.unbind('click').click(function() {
				var box = $($(this).children()[0]);
				var boxValue = box.attr('value');
				if (boxValue == 0) {// 
					box.attr('class', 'icon-check');
					box.attr('value', 1);
					selectedObjIds.push(box.parent().parent().attr('objid'));
					
					
					var checkeds = tcgtableObj().children('tbody').find('i[class="icon-check-empty"]');
					if (isNullOrUndefined(checkeds) || checkeds.length == 0) {
						var headerBox = tcgtableObj().children('thead').find('i[class="icon-check-empty"]');
						if (!isNullOrUndefined(headerBox) && headerBox.length == 1) {
							headerBox.attr('class', 'icon-check');
							headerBox.attr('value', 1);
						}
					}
				} else {
					box.attr('class', 'icon-check-empty');
					box.attr('value', 0);
					selectedObjIds.remove(box.parent().parent().attr('objid'));
					
//					var checkeds = tcgtableObj().children('tbody').find('i[class="icon-check"]');
//					if (isNullOrUndefined(checkeds) || checkeds.length == 0) {
					var headerBox = tcgtableObj().children('thead').find('i[class="icon-check"]');
					if (!isNullOrUndefined(headerBox) && headerBox.length == 1) {
						headerBox.attr('class', 'icon-check-empty');
						headerBox.attr('value', 0);
					}
//					}
				}
				
				 $('#clickedObjId').val(selectedObjIds.join());
			});
			return td;
		}

		
//		return this.css({
//			color : settings.color,
//			backgroundColor : settings.backgroundColor
//		});
	};
}(jQuery));


function tcgtableObj() {
	return $('#' + tableId);
}

function changeColsStatus(box, columnNums) {
	// var rowNum = $("#__grid__ tr[class='odd gradeX']").length;
	var rowNum = tcgtableObj().children('tbody').first().children().length;
	var isShow = Number($(box).attr('id')) == 1 ? true : false;
	
	var colNos = columnNums.split(',');
	for (var i=1; i<colNos.length; i++) {
		var colNo = Number(colNos[i]);
		if (isShow) {
			showTdContent(colNo, rowNum);
		} else {
			hideTdContent(colNo, rowNum);
		}
	}
	
	var col_header_no = colNos[0];
	if (isShow) {
		showHeader(col_header_no);
		checkBox(box);
	} else {
		hideHeader(col_header_no);
		uncheckBox(box);
	}
}

function checkBox(box) {
	$(box).attr('id', 0);
	$(box).removeClass('icon-check-empty').addClass('icon-check');
}

function uncheckBox(box) {
	$(box).attr('id', 1);
	$(box).removeClass('icon-check').addClass('icon-check-empty');
}

function showTdContent(colNo, rowNum) {
	for (var i=0; i<rowNum; i++) {
		$(tdIdContentJq(colNo, i)).removeAttr("style");
	}
}

function hideTdContent(colNo, rowNum) {
	for (var i=0; i<rowNum; i++) {
		$(tdIdContentJq(colNo, i)).css('display', 'none');
	}
}

function showHeader(col_header_no) {
	var tdHeader = $(tdIdHeaderJq(col_header_no));
	tdHeader.removeAttr("style");
	tdHeader.css('background-color', '#D3E2F4');
	
	var colspan = tdHeader.attr('colspan');
	if (!isNullOrUndefined(colspan)) {
		colspan = Number(colspan);
		for (var j=0; j<colspan; j++) {
			$(tdIdHeaderSubJq(col_header_no, j)).removeAttr("style");
			$(tdIdHeaderSubJq(col_header_no, j)).css('background-color', '#D3E2F4');
		}
	}
}

function hideHeader(col_header_no) {
	var tdHeader = $(tdIdHeaderJq(col_header_no));
	tdHeader.removeAttr("style");
	tdHeader.css('display', 'none');
	
	var colspan = tdHeader.attr('colspan');
	if (!isNullOrUndefined(colspan)) {
		colspan = Number(colspan);
		for (var j=0; j<colspan; j++) {
			$(tdIdHeaderSubJq(col_header_no, j)).css('display', 'none');
		}
	}
}

function tdIdHeaderJq(header_no) {
	return "#_" + header_no;
}

function tdIdHeaderSubJq(header_no, sub_no) {
	return "#_" + header_no + "_sec_" + sub_no;
}

function tdIdContentJq(colNo, rowNo) {
	return "#_" + colNo + "_" + rowNo;
}

