package com.tcs.utx.digiframe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tcs.utx.digiframe.model.KeyValuePair;
import com.tcs.utx.digiframe.service.BrandingDetailsService;

public class TSSVStringUtil {

	public static final String PATTERN_ALPHANUMERICSPACE = "[a-zA-Z0-9 ]+";
	public static final String PATTERN_ALPHANUMERIC = "[a-zA-Z0-9]+";
	public static final String PATTERN_MARKDOWN = "^[a-zA-Z0-9 #\\-_*=.+:!\\[\\]()\\n,&\\'\\\"\\|\\/?\\\\%^`~$@]+$";
	public static final String PATTERN_ALPHANUMERICSPACE_WITH_UNDERSCORE = "[a-zA-Z0-9_ ]+";
	public static final String PATTERN_ALPHABETS = "[a-zA-Z]+";
	public static final String PATTERN_NUMERIC = "[0-9]+";
	public static final String PATTERN_FOR_NAMES = "[.a-zA-Z0-9 _()&,-/\n]+";
	public static final String PATTERN_FOR_NAMES_WITHOUT_COMMA = "[.a-zA-Z0-9 _()&-\\/\n]+";
	public static final String PATTERN_FOR_NAMES_1 = "[.a-zA-Z0-9 _()&,-\\/\n]+";
	public static final String PATTERN_FOR_FILENAMES = "[.a-zA-Z0-9 _-]+";
	public static final String PATTERN_CVSS3 = "^CVSS:3\\.1\\/((AV:[NALP]|AC:[LH]|PR:[UNLH]|UI:[NR]|S:[UC]|[CIA]:[NLH]|E:[XUPFH]|RL:[XOTWU]|RC:[XURC]|[CIA]R:[XLMH]|MAV:[XNALP]|MAC:[XLH]|MPR:[XUNLH]|MUI:[XNR]|MS:[XUC]|M[CIA]:[XNLH])\\/)*(AV:[NALP]|AC:[LH]|PR:[UNLH]|UI:[NR]|S:[UC]|[CIA]:[NLH]|E:[XUPFH]|RL:[XOTWU]|RC:[XURC]|[CIA]R:[XLMH]|MAV:[XNALP]|MAC:[XLH]|MPR:[XUNLH]|MUI:[XNR]|MS:[XUC]|M[CIA]:[XNLH])$";
	public static final String PATTERN_KEYVALUE = "^((\\\"([^\\n\\r\"]+)\\\")\\:\\:(\\\"([^\\n\\r\"]+)\\\"[\\r\\n]))*(\\\"([^\\n\\r\"]+)\\\")\\:\\:(\\\"([^\\n\\r\"]+)\\\")$";
	public static final String PATTERN_IP_CIDR_1 = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$";
	public static final String PATTERN_IP_CIDR_2 = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\-\\b([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\b)?$";
	public static final String PATTERN_IP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	public static final String PATTERN_FOR_ASSET = "[.a-zA-Z0-9 _()'\\()* &-/\n]+";
	public static final String PATTERN_FOR_DESCRIPTION = "[.a-zA-Z0-9 _\\()&,-:/\n]+";
	public static final String COMMA_SEPARATED_IPS = "^(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(,\\s*(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)))*$";
	public static final String PATTERN_FOR_URL = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
	public static final String PATTERN_FOR_CONTACT = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*(,\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*)*$";
	public static final String PATTERN_FOR_COMMA_SEPARATED_EMPID = "^(\\d{4}|\\d{5}|\\d{6}|\\d{7}|\\d{8}|\\d{9})(,\\s*(\\d{4}|\\d{5}|\\d{6}|\\d{7}|\\d{8}|\\d{9}))*$";
	public static final String PATTERN_FOR_DOMAIN = "^((?!-)[A-Za-z0-9-]" + "{1,63}(?<!-)\\.)" + "+[A-Za-z]{2,6}";
	public static final String PATTERN_FOR_IPCIDR = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))?$";
	public static final String IP_RANGE = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
			+ "($|\\-)([01]?\\d\\d?|2[0-4]\\d|25[0-5])?$";
	public static final String PATTERN_FOR_COMMA_SEPARATED_URLS = "^(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)+)(,\\s*https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)+)*$";
	public static final String PATTERN_ASVSQUESTION = "[.a-zA-Z0-9 _\\()'&-:=,\"?/\n]+";
	public static final String PATTERN_QUESTIONDESCRITION = "[.a-zA-Z0-9 _\\()'{}&*-:;<>=,\"?/\n]+";
    public static final String PATTERN_ASVSVERSION = "[a-zA-Z0-9., ]+";
	public static final String PATTERN_SECTIONNAME = "[a-zA-Z ]+";
	public static final String PATTERN_DECIMAL = "[0-9]+(\\.[0-9][0-9]?)?";
	public static final String PATTERN_COMMA_URL = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)+)(:==:\\s*https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)+)*$";
	public static final String PATTERN_FOR_DATE_AND_TIME_24HR_FORMAT = "^[0-3]?[0-9]\\/[0-3]?[0-9]\\/(?:[0-9]{2})?[0-9]{2}$";
	public static final String PATTERN_QUESTION = "[.a-zA-Z0-9 _\\()&,-:/\n]+";
	public static final String PATTERN_NEWQUESTION = "[.a-zA-Z0-9 _()&-?\n]+";

	
	public static String generateStringFromParameters(String msg, Object... values) {
		StringBuilder sb = new StringBuilder(msg);
		for (int i = 0; i < values.length; i++) {
			int sPos = sb.indexOf("{" + i + "}");
			sb.replace(sPos, ("{" + i + "}").length() + sPos, values[i].toString());
		}
		return sb.toString();
	}

	public static boolean matchPattern(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}



	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static Date parseTimeStamp(String strDate) throws ParseException {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date result = sdfDate.parse(strDate);
		return result;
	}

	public static boolean checkValidityForDynamicDropdowns(List<Map<String, Object>> List, String value) {

		for (int i=0; i<List.size(); i++) {
			if(List.get(i).containsValue(value)) {
				return true;
			}
		}
		return false;
		
	}
	
	public static int checkValidityForDynamicDropdownsIndex(List<Map<String, Object>> List, String value) {
		
		for (int i=0; i<List.size(); i++) {
			if(List.get(i).containsValue(value)) {
				return i;
			}
		}
		return -1;
		
	}
	
	public static int checkStringInList(List<String> List, String value) {
		
		for (int i=0; i<List.size(); i++) {
			if(List.get(i).equals(value)) {
				return i;
			}
		}
		return -1;
		
	}

	 public static boolean isKeyInKeyValuePair(String key, ArrayList<KeyValuePair> lstDto) {

			Iterator<KeyValuePair> itr = lstDto.iterator();
			while (itr.hasNext()) {
				KeyValuePair obj = itr.next();
				if (key.equals(obj.getValue())) {
					return true;
				}

				if (key.equals(obj.getKey())) {
					return true;
				}
				
			}

			return false;
		}
	 
	 


}
