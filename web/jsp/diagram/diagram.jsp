<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->

<%@ include file="/jsp/styleTCG.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<meta charset="utf-8" />
	<title>Mobius | TCG</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/highstock/highstock.js"></script>
	<script src="<%=request.getContextPath() %>/jsp/assets/plugins/highstock/modules/exporting.js"></script>
	
	<style type="text/css">
		.operator {vertical-align: top; margin-bottom: 0px; }
		.watched-item {vertical-align: top; margin-bottom: 0px; }
		.ex-symbol {vertical-align: top; margin-bottom: 0px; }
	</style>
	
	<script type="text/javascript">
	
	var operators, wathcedSymbols, exSymbols, listedSymbolMap, listedExSymbolMap;
	jQuery(document).ready(function() {
		
		var url = '<%=request.getContextPath() %>/jsp/diagram/getAllInfos.do';
		$.getJSON(url, function(data) {
			window.operators = data.operators;
			window.wathcedSymbols = data.wathcedSymbols;
			window.exSymbols = data.exSymbols;
			window.listedSymbolMap = data.listedSymbolMap;
			window.listedExSymbolMap = data.listedExSymbolMap;
			
			var operatorElems = $( "button[class~='operator']" )[0];
			$(operatorElems).click();
		});
		
		
	});
	
	function drawByItemId(itemId, operator, watchedSymbol, exchangeSymbol, divId) {
		var url = '<%=request.getContextPath() %>/jsp/diagram/getStatisticsByItemId.do?itemId='+itemId;
		$.getJSON(url, function(data) {
			drawWithOperator(operator, watchedSymbol, exchangeSymbol, divId, data);
		});
	}
	
	function drawWithOperator(operator, watchedSymbol, exchangeSymbol, divId, data) {
		var watchedSymbolUp = watchedSymbol.toUpperCase();
		var exchangeSymbolUp = exchangeSymbol.toUpperCase();
		// split the data set into ohlc and volume
		var ohlc = [], volume = [], count = [], dataLength = data.length;
			
		for (var i = 0; i < dataLength; i++) {
			ohlc.push([ data[i][0], // the date
						data[i][1], // open
						data[i][2], // high
						data[i][3], // low
						data[i][4] // close
			]);
			volume.push([ data[i][0], // the date
						  data[i][5] // the volume
			]);
			count.push([ data[i][0], // the date
						 data[i][7] // the count
			]);
		}

		// set the allowed units for data grouping
		var groupingUnits = [[ 'millisecond', // unit name
		                	[1, 2, 5, 10, 20, 25, 50, 100, 200, 500] // allowed multiples
		                ], [ 'second', [1, 2, 5, 10, 15, 30]
		                ], [ 'minute', [1, 2, 5, 10, 15, 30]
		                ], [ 'hour', [1, 2, 3, 4, 6, 8, 12]
		                ], [ 'day', [1]
		                ], [ 'week', [1]
		                ], [ 'month', [1, 3, 6]
		                ], [ 'year', null
		                ]];

		// create the chart
		var divJqId = '#' + divId;
		$(divJqId).highcharts('StockChart', {
		    rangeSelector: {
				inputEnabled: $(divJqId).width() > 480,
		        selected: 1
		    },
		    title: { text: watchedSymbolUp + '_' +exchangeSymbolUp + ' at ' + operator  },
		    yAxis: [{
		        labels: { align: 'right', x: -3 },
		        title: { text: watchedSymbolUp + '_' +exchangeSymbolUp  },
		        height: '50%',
		        lineWidth: 2
		    }, {
		    	labels: { align: 'right', x: -3 },
		        title: { text: watchedSymbolUp + ' Vol' },
		        top: '65%',
		        height: '25%',
		        offset: 0,
		        lineWidth: 2
		    }, {
		    	labels: { align: 'right', x: -3 },
		        title: { text: 'Count' },
		        top: '90%',
		        height: '25%',
		        offset: 0,
		        lineWidth: 2
		    }],
		    series: [{
		        type: 'candlestick',
		        name: exchangeSymbol,
		        data: ohlc,
		        dataGrouping: { units: groupingUnits  }
		    }, {
		        type: 'column',
		        name: watchedSymbolUp + ' Vol',
		        data: volume,
		        yAxis: 1,
		        dataGrouping: { units: groupingUnits }
		    }, {
		        type: 'column',
		        name: 'Count',
		        data: count,
		        yAxis: 2,
		        dataGrouping: { units: groupingUnits }
		    }]
		});
	}
	
	function contains(arr, obj) {
		for (idx in arr) {
			if (arr[idx] == obj) {
				return true;
			}
		}
		return false;
	}
	
	var selectedOperator = null, selectedSymbol = null, selectedExSymbol = 'BTC';
	function clickOperator(op, divId) {
		selectedOperator = op;
		
		var allWatchedSymbols = $( "button[class~='watched-item']" );// 
		var watchedSymbols = eval('listedSymbolMap.'+op);// var watchedSymbols = 
		$.each(allWatchedSymbols, function( index, elem ) {
			elem = $(elem);
			var resu = contains(watchedSymbols, elem.text());
			enableOrDisableButton(resu, elem, 'btn-success');
		});
		
		// if (selectedSymbol == null || !contains(watchedSymbols, selectedSymbol)) {
			selectedSymbol = watchedSymbols[0];
		// }
		
		// 
		checkExSymbolButtons();
		
		drawBySymbol(divId);
	}
	
	function checkExSymbolButtons() {
		var allExSymbols = $( "button[class~='ex-symbol']" );// 
		var exSymbols = eval('listedExSymbolMap.' + selectedOperator + '_' + selectedSymbol);
		$.each(allExSymbols, function( index, elem ) {
			elem = $(elem);
			var resu = contains(exSymbols, elem.text());
			enableOrDisableButton(resu, elem, 'btn-inverse');
		});
		
		if (!contains(exSymbols, selectedExSymbol)) {
			selectedExSymbol = exSymbols[0];
		}
	}
	
	function enableOrDisableButton(resu, elem, cssClz) {
		if (resu == true) {
			if (!elem.hasClass(cssClz)) elem.addClass(cssClz);
			elem.removeClass("disabled");
			elem.addClass("active");
			elem.prop("disabled", false );
		} else {
			elem.removeClass(cssClz);
			elem.removeClass("active");
			elem.addClass("disabled");
			elem.prop("disabled", true );
		}
	}
	
	function clickSymbol(symbol, divId) {
		selectedSymbol = symbol;
		checkExSymbolButtons();
		if (selectedOperator != null) {
			drawBySymbol(divId);
		}
	}
	
	function clickExSymbol(exSymbol, divId) {
		selectedExSymbol = exSymbol;
		if (selectedOperator != null) {
			drawBySymbol(divId);
		}
	}
	
	function drawBySymbol(divId) {
		var url = '<%=request.getContextPath() %>/jsp/diagram/getStatisticsBySymbol.do?';
		url = url + 'operator='+ selectedOperator + '&symbol=' + selectedSymbol + '&exchange=' + selectedExSymbol; 
		$.getJSON(url, function(data) {
			if (data.length == 0) {
				alert("The Item[" + selectedSymbol + "_" + selectedExSymbol + "@" + selectedOperator + "] do not exsit!");
			} else {
				drawWithOperator(selectedOperator, selectedSymbol, selectedExSymbol, divId, data);
			}
		});
		
		url = "<%=request.getContextPath() %>/jsp/diagram/getOneDayStatistics.do?";
		url = url + 'operator='+ selectedOperator + '&symbol=' + selectedSymbol + '&exchange=' + selectedExSymbol; 
		$.getJSON(url, function(data) {
			
			var low24h = new Number(data[2]).toFixed(8);
			var high24h = new Number(data[1]).toFixed(8);
			var exSymbolVol = new Number(data[4]).toFixed(8);
			$('#high24h').html(high24h);
			$('#low24h').html(low24h);
			$('#ex_symbol_title').html(selectedExSymbol+' ' + ' Vol: ');
			$('#ex_symbol_vol').html(exSymbolVol);
		});
	}
	
	
	</script>
</head>

<body class="page-header-fixed">
	<input type="hidden" id="editable" name="editable" value=${editable }>
	<input type="hidden" id="_required_params_" value="searchField,searchValue,interest,editable">

	<div class="page-content">
		<div class="container-fluid">

			<div class="row-fluid">
				<div class="span6">
					<div class="portlet-title">
						<div class="caption">Trade Number Diagram</div>
						<div class="tools"></div>
					</div>
					<div class="clearfix">
						<div class="btn-group caption ">
							<span class="label label-success">Operators: </span>
							<c:forEach var="op" items="${operators }">
								 <span class=""> 
								 	<button type="button" class="operator btn-small btn-primary" onclick="clickOperator('${op }', 'container');">${op }</button>
								 </span> &nbsp;&nbsp;
							</c:forEach>
							<br>
							<span class="label label-success">&nbsp;&nbsp;Symbols: </span>
							<span >
							<c:forEach var="sy" items="${wathcedSymbols }"  varStatus="vs">
								 <button type="button" class="watched-item btn-mini btn-success" onclick="clickSymbol('${sy }', 'container');">${sy }</button>
								 <c:if test="${vs.count % 10 == 0 }"> 
								 <br> 
								 <span class="label label-success" style="background-color: white;">
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								 </span>
								 </c:if>
							</c:forEach>
							</span> &nbsp;&nbsp;
							<br>
							<span class="label label-success">Exchange: </span>
							<span class="">
							<c:forEach var="ex" items="${exchangeSymbols }" >
								<button type="button" class="ex-symbol btn-mini btn-inverse" onclick="clickExSymbol('${ex }', 'container');">${ex }</button>
								&nbsp;&nbsp;
							</c:forEach>
							</span>
						</div>
						<br>
						
						<div class="">
						     <div class="row-fluid show-grid">
				              <div class="span4"> <span class="label label-success">24H High: </span> <span class="label " id="high24h"></span> </div>
				              <div class="span4"> <span class="label label-success">24H Low: </span> <span class="label " id="low24h"></span> </div>
				              <div class="span4"> <span class="label label-success" id="ex_symbol_title"></span> <span class="label " id="ex_symbol_vol"></span> </div>
				             
				             <!-- 
				             // last price, max(high), min(low), sum(watched_vol), sum(exchange_vol), sum(count)
		var sb = new StringBuilder();
		sb.append("<tr> <td>").append(selectedSymbol).append(" Trade Count</td> <td>").append(arr[5]).append("</td> <td>Last</td> <td>").append(arr[0]).append("</td> </tr> <br>");
		sb.append("<tr> <td>24H High</td> <td>").append(arr[1]).append("</td> <td>24H Low</td> <td>").append(arr[2]).append("</td> </tr> <br>");
		sb.append("<tr> <td>BTC Vol</td> <td>").append(arr[4]).append("</td> <td>").append(selectedSymbol).append(" Vol</td> <td>").append(arr[3]).append("</td> </tr> <br>");
		// sb.append("<tr> <td>BTC Vol</td> <td>").append(arr[3]).append("</td> <td>").append(selectedSymbol).append(" Trade Count</td> <td>").append(arr[5]).append("</td> </tr> <br>");
		
				              -->
				            </div>
				            
				             <div class="row-fluid show-grid">
				              <div class="span4"> <span class="label label-success">24H High: </span> <span class="label ">high</span> </div>
				              <div class="span4"> <span class="label label-success">24H Low: </span> <span class="label ">low</span> </div>
				              <div class="span4"> <span class="label label-success">BTC Vol: </span> <span class="label ">btc_vol</span> </div>
				            </div>
				            
						</div>
						<br>
						
						<div class="btn-group caption ">
							 <span id="message01" style="line-height: 20px;vertical-align: top; font-size: 14px; padding: 7px 14px; "></span>
						</div>
						
					</div>
					
					<div id="container" style="height: 400px; min-width: 310px">
						
					</div>
					
				</div>
				<div class="span6">
					<div class="portlet-title">
						<div class="caption">Trade Number Diagram 2</div>
						<div class="tools"></div>
					</div>
					<div class="clearfix">
						
					</div>
					
					<div id="container2" style="height: 400px; min-width: 310px">
						
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6"></div>
				<div class="span6"></div>
			</div>
			
		</div>
	</div>

	<div class="footer">
		<div class="footer-inner">
			2013 &copy; COOK|Crawler.
		</div>
		<div class="footer-tools">
			<span class="go-top">
			<i class="icon-angle-up"></i>
			</span>
		</div>
	</div>

	
</html>