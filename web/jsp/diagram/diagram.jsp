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
	
	
	<script type="text/javascript">
	
	jQuery(document).ready(function() {
		var operator = 'mintpal', watchedSymbol = 'uro', exchangeSymbol = 'btc';
		<%-- var url = '<%=request.getContextPath() %>/jsp/diagram/fetchTradeData.do?operator='+operator+
				'&watchedSymbol=' + watchedSymbol + '&exchangeSymbol=' + exchangeSymbol; --%>
		
		/* $.getJSON(url, function(data) {
			drawWithOperator(operator, watchedSymbol, exchangeSymbol, 'container', data);
		}); */
		
		<%-- var url2 = '<%=request.getContextPath() %>/jsp/diagram/getStatisticsByItemId.do?itemId=20'; --%>
		/* $.getJSON(url2, function(data) {
			drawWithOperator(operator, watchedSymbol, exchangeSymbol, 'container2', data);
		}); */
		
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
		$.each(arr, function(n, value) {
	           if (value == obj) {
	        	   return true;
	           }
        });
		return false;
	}
	
	var selectedOperator = null, selectedSymbol = null, selectedExSymbol = 'BTC';
	function clickOperator(op, divId) {
		selectedOperator = op;
		
		// 
		var url = '<%=request.getContextPath() %>/jsp/diagram/getActiveWatchedAndExSymbols.do?operator='+ selectedOperator;
		
		$.getJSON(url, function(data) {
			
			$('#watchedSymbol').each(function(index, elem){
				elem = $(elem);
				if (contains(data, elem.text())) {
					elem.removeClass("disabled");
					elem.addClass("active");
					elem.removeAttr('disabled');
				} else {
					elem.removeClass("active");
					elem.addClass("disabled");
					//  disabled = 'true' 'disabled'
					
					
					try{
						alert(elem.attr('disabled'));
						elem.attr('disabled', 'disabled');
					}catch(error){
						alert(error);
						throw error;
					}finally{
					} 
					
				}
			});
			
			if (selectedSymbol != null) {
				if (contains(data, selectedSymbol) && contains(data, selectedExSymbol) ) {
					drawBySymbol(divId);
				} else {
					$('#message01').html('At Operator[' + selectedOperator + '], we do not find Symbol[' + selectedSymbol + '].');
				}
			} else {
				$('#message01').html('At Operator[' + selectedOperator + '], Please select existed Symbol.');
			}
			
		});
		
		
	}
	
	function clickSymbol(symbol, divId) {
		selectedSymbol = symbol;
		if (selectedOperator != null) {
			drawBySymbol(divId);
		}
	}
	
	function drawBySymbol(divId) {
		var url = '<%=request.getContextPath() %>/jsp/diagram/getStatisticsBySymbol.do?operator='+ selectedOperator;
		url = url + '&symbol=' + selectedSymbol + '&exchange=' + selectedExSymbol; 
		$.getJSON(url, function(data) {
			if (data.length == 0) {
				alert("This Item do not exsit!");
			} else {
				drawWithOperator(selectedOperator, selectedSymbol, selectedExSymbol, divId, data);
			}
			
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
							<button class="btn dropdown-toggle" data-toggle="dropdown">Watched Items<i class="icon-angle-down"></i></button>
							<ul class="dropdown-menu">
							  <c:forEach var="wat" items="${its }">
								  <li>
								  <!-- drawByItemId(itemId, operator, watchedSymbol, exchangeSymbol, divId) -->
									<a href="javascript:drawByItemId(${wat.id }, '${wat.operator }', '${wat.watchedSymbol }', '${wat.exchangeSymbol }', 'container');">
										<span class="label label-info" ><i ></i> ${wat.operator }_${wat.watchedSymbol }_${wat.exchangeSymbol } </span>
									</a>
								  </li>
							  </c:forEach>
							</ul>
						</div>
						<br>
						
						<div class="btn-group caption ">
							<span class="label label-success">Operators: </span>
							<c:forEach var="op" items="${operators }">
								 <span class="badge badge-success"> 
								 	<a id='operator' href="javascript:clickOperator('${op }', 'container');">${op }</a>
								 </span> &nbsp;&nbsp;
							</c:forEach>
							<br>
							<span class="label label-success">&nbsp;&nbsp;Symbols: </span>
							<c:forEach var="sy" items="${wathcedSymbols }"  varStatus="vs">
								clickSymbol
								 <span class="badge badge-warning">
								 <!-- href="javascript:clickSymbol('${sy }', 'container');"  -->
									<button type="button" class="btn btn-large btn-primary" id='watchedSymbol' onclick="clickSymbol('${sy }', 'container');">${sy }</button>
								 </span> &nbsp;&nbsp;
								 <c:if test="${vs.count % 10 == 0 }"> <br> <span class="label" style="background-color: white;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span> </c:if>
							</c:forEach>
							<br>
							<span class="label label-success">Exchange: </span>
							<c:forEach var="ex" items="${exchangeSymbols }" >
								<span class="badge badge-info">
									<a id='exSymbol' href="javascript:clickExSymbol('${ex }', 'container');">${ex }</a> 
								</span>&nbsp;&nbsp;
							</c:forEach>
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