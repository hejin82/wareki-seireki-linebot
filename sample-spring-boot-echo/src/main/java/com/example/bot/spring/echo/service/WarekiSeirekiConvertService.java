package com.example.bot.spring.echo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WarekiSeirekiConvertService {
	private static final Map<String, String> WAREKI_TO_SEIREKI_MAP = new HashMap<>();
	private static final Map<Integer, String> SEIREKI_TO_WAREKI_MAP = new HashMap<>();
	
	static {
		WAREKI_TO_SEIREKI_MAP.put("平成29", "2017");
		
		SEIREKI_TO_WAREKI_MAP.put(2017, "平成29");
	}
	
	public String execute(String inputText) {
		String outText = "";
		
		if (isWarekiText(inputText)) { // 和暦入力か？
			String wareki = parseWarekiText(inputText);
			
			outText = WAREKI_TO_SEIREKI_MAP.getOrDefault(wareki, "Not Found");
		}
		else if (isSeirekiText(inputText)) { // 西暦入力か？
        	int seireki = parseSeirekiText(inputText);
        	
        	outText = SEIREKI_TO_WAREKI_MAP.getOrDefault(seireki, "Not Found");
        }
		
		return outText;
	}
	
	private boolean isWarekiText(String inputText) {
		return parseWarekiText(inputText) != null;
	}
	
	private boolean isSeirekiText(String inputText) {
		if (StringUtils.isEmpty(inputText)) {
			return false;
		}
		
    	try {
    		int seireki = parseSeirekiText(inputText);
    		if (1900 <= seireki && seireki <= 2099) {
    			return true;
    		}
    	} catch(NumberFormatException e) {
    		// Nothing to do...
    	}
    	return false;
    }
    
	// 平成29年
	// 昭和31
	// 大正12年ごろ
	// 明治42年
	
	private String parseWarekiText(String message) {
		String result = null;
		
		if (!StringUtils.isEmpty(message)) {
			String regex = "(¥¥d+)";
			Pattern p = Pattern.compile(regex);
			
			Matcher m = p.matcher(message);
			if (m.find()){
			  String matchstr = m.group();
			  System.out.println(matchstr + "の部分にマッチしました");
	
			  System.out.println("group1:" + m.group(1));
			  System.out.println("group2:" + m.group(2));
			  
			  result = m.group(1) + m.group(2);
			}
		}
		
		return result;
    }
	
    private int parseSeirekiText(String message) {
    	return Integer.parseInt(message.trim().replaceAll("[^0-9]", ""));
    }
}
