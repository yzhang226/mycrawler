package org.omega.trade.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.trade.entity.CandleStick;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsp/diagram")
public class DiagramController {
	
	private static final Log log = LogFactory.getLog(DiagramController.class);
	
	
	@RequestMapping("/toDiagramPage.do")
	public String toDiagramPage(Model model, HttpServletRequest request ) {
		String jsp = "diagram/diagram";
		return jsp;
	}
	
	@RequestMapping("/fetchTradeData.do")
	@ResponseBody
	public List<CandleStick> fetchTradeData(@RequestParam String operator, 
											@RequestParam String watchedSymbol, 
											@RequestParam String exchangeSymbol) {
		List<CandleStick> sticks = new ArrayList<>();
		
		
		return sticks;
	}
	
}
