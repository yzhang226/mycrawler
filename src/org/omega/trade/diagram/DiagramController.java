package org.omega.trade.diagram;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.common.Utils;
import org.omega.trade.entity.CandleStick;
import org.omega.trade.entity.WatchListItem;
import org.omega.trade.service.MarketTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsp/diagram")
public class DiagramController {
	
	private static final Log log = LogFactory.getLog(DiagramController.class);
	
	@Autowired
	private MarketTradeService marketTradeService;
	
	@RequestMapping("/toDiagramPage.do")
	public String toDiagramPage(Model model, HttpServletRequest request ) {
		String jsp = "diagram/diagram";
		return jsp;
	}
	
	@RequestMapping("/fetchTradeData.do")
	@ResponseBody
	public Object[][] fetchTradeData(@RequestParam String operator, 
											@RequestParam String watchedSymbol, 
											@RequestParam String exchangeSymbol) {
		List<CandleStick> sticks = new ArrayList<>();
		
		WatchListItem item = new WatchListItem(operator, watchedSymbol, exchangeSymbol);
		String table = Utils.getMarketTradeTable(item);
		
		StringBuilder highLowSql = new StringBuilder("select max(price) as high, min(price) as low  from ");
		highLowSql.append(table).append(" where trade_time > ? and trade_time < ?");
		StringBuilder openSql = new StringBuilder("select price from ");
		openSql.append(table).append(" where trade_time > ? and trade_time < ? order by trade_time asc limit 1");
		StringBuilder closeSql = new StringBuilder("select price from ");
		closeSql.append(table).append(" where trade_time > ? and trade_time < ? order by trade_time desc limit 1");
		StringBuilder watchedVolSql = new StringBuilder("select sum(total_units) as watchedVolume, max(total_cost) as exchangeVolume from ");
		watchedVolSql.append(table).append(" where trade_time > ? and trade_time < ?");
		StringBuilder countTradeSql = new StringBuilder("select count(total_units) countTrade from ");
		countTradeSql.append(table).append(" where trade_time > ? and trade_time < ?");
		
		
		
		StringBuilder maxMTimeSql = new StringBuilder("select max(trade_time), min(trade_time) from ");
		maxMTimeSql.append(table);
		Object[] maxMin = (Object[]) findUnique(maxMTimeSql.toString());
		long maxMTimeSec = ((BigInteger) maxMin[0]).longValue();
		long minMTimeSec = ((BigInteger) maxMin[1]).longValue();
		
		int maxDiffMilliSec = (int) (maxMTimeSec - minMTimeSec);
		int maxHours = maxDiffMilliSec / 1000 / 60 / 60;
//		int num = maxHours > 6 ?  6 * 60 / 5 : maxHours * 60 / 5;
		int num = maxHours * 60 / 5;
		int increment = 5 * 60 * 1000;// five minutes
		
		long first = Utils.getLastFiveMinuteTime(maxMTimeSec).getMillis();
		long second = Utils.getSecondFiveMinuteTime(maxMTimeSec).getMillis();
		
		Object[][] arr = new Object[num][8];
		Double open = null;
		Object[] highLow = null;
		Double close = null;
		Object[] vols = null;
		BigInteger countTrade = null;
		long nfirst, nsecond;
		int arrIdx = 0;
		for (int i=num-1; i>=0; i--) {
			nfirst = first - increment * i;
			nsecond = second - increment * i;
			
			open = (Double) findUnique(openSql.toString(), nsecond, nfirst);
			if (open == null) {
				if (arrIdx == 0) {
					open = 0.0;
				} else {
					open = (Double) arr[arrIdx-1][1];
				}
				
				highLow = new Double[]{open, open};
				close = open;
				vols = new Double[]{0.0, 0.0};
				countTrade = new BigInteger("0");
			} else {
				highLow = (Object[]) findUnique(highLowSql.toString(), nsecond, nfirst);
				close = (Double) findUnique(closeSql.toString(), nsecond, nfirst);
				vols = (Object[]) findUnique(watchedVolSql.toString(), nsecond, nfirst);
				countTrade = (BigInteger) findUnique(countTradeSql.toString(), nsecond, nfirst);
			}
			
			arr[arrIdx][0] = nsecond;
			arr[arrIdx][1] = open;
			arr[arrIdx][2] = (Double) highLow[0];
			arr[arrIdx][3] = (Double) highLow[1];
			arr[arrIdx][4] = close;
			arr[arrIdx][5] = (Double) vols[0];
			arr[arrIdx][6] = (Double) vols[1];
			arr[arrIdx][7] = countTrade.intValue();
			
			arrIdx++;
		}
		
		return arr;
	}
	
	private Object findUnique(String sql, Object... params) {
		return marketTradeService.createSQLQuery(sql, params).uniqueResult();
	}
	
}
