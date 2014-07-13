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
		var url = '<%=request.getContextPath() %>/jsp/diagram/fetchTradeData.do?operator='+operator+
				'&watchedSymbol=' + watchedSymbol + '&exchangeSymbol=' + exchangeSymbol;
		var watchedSymbolUp = watchedSymbol.toUpperCase();
		var exchangeSymbolUp = exchangeSymbol.toUpperCase();
		$.getJSON(url, function(data) {
			// Create the chart
			// alert(data);
			// split the data set into ohlc and volume
			var ohlc = [],
				volume = [],
				count = [],
				dataLength = data.length;
				
			for (var i = 0; i < dataLength; i++) {
				ohlc.push([
					data[i][0], // the date
					data[i][1], // open
					data[i][2], // high
					data[i][3], // low
					data[i][4] // close
				]);
				
				volume.push([
					data[i][0], // the date
					data[i][5] // the volume
				]);
				count.push([
					data[i][0], // the date
					data[i][7] // the count
				]);
			}

			// set the allowed units for data grouping
			var groupingUnits = [[
			                  	'millisecond', // unit name
			                	[1, 2, 5, 10, 20, 25, 50, 100, 200, 500] // allowed multiples
			                ], [
			                	'second',
			                	[1, 2, 5, 10, 15, 30]
			                ], [
			                	'minute',
			                	[1, 2, 5, 10, 15, 30]
			                ], [
			                	'hour',
			                	[1, 2, 3, 4, 6, 8, 12]
			                ], [
			                	'day',
			                	[1]
			                ], [
			                	'week',
			                	[1]
			                ], [
			                	'month',
			                	[1, 3, 6]
			                ], [
			                	'year',
			                	null
			                ]];

			// create the chart
			$('#container').highcharts('StockChart', {
			    
			    rangeSelector: {
					inputEnabled: $('#container').width() > 480,
			        selected: 1
			    },

			    title: {
			        text: watchedSymbolUp + '_' +exchangeSymbolUp + ' at ' + operator
			    },

			    yAxis: [{
			        labels: {
			    		align: 'right',
			    		x: -3
			    	},
			        title: {
			            text: watchedSymbolUp + '_' +exchangeSymbolUp
			        },
			        height: '50%',
			        lineWidth: 2
			    }, {
			    	labels: {
			    		align: 'right',
			    		x: -3
			    	},
			        title: {
			            text: watchedSymbolUp + ' Vol'
			        },
			        top: '65%',
			        height: '25%',
			        offset: 0,
			        lineWidth: 2
			    }, {
			    	labels: {
			    		align: 'right',
			    		x: -3
			    	},
			        title: {
			            text: 'Count'
			        },
			        top: '90%',
			        height: '25%',
			        offset: 0,
			        lineWidth: 2
			    }],
			    
			    series: [{
			        type: 'candlestick',
			        name: exchangeSymbol,
			        data: ohlc,
			        dataGrouping: {
						units: groupingUnits
			        }
			    }, {
			        type: 'column',
			        name: watchedSymbolUp + ' Vol',
			        data: volume,
			        yAxis: 1,
			        dataGrouping: {
						units: groupingUnits
			        }
			    }, {
			        type: 'column',
			        name: 'Count',
			        data: count,
			        yAxis: 2,
			        dataGrouping: {
						units: groupingUnits
			        }
			    }]
			});
		});
	});
	
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
						
					</div>
					
					<div id="container" style="height: 400px; min-width: 310px">
						
					</div>
					
				</div>
				<div class="span6">
				
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