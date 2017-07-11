package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.PatternLayout;

public class TasfePatternLayout extends PatternLayout {
	static {
		defaultConverterMap.put("ip", TasfeConverter.class.getName());
		defaultConverterMap.put("id", PrimarykeyConverter.class.getName());
		defaultConverterMap.put("msg", TasfeMsgConverter.class.getName());
		defaultConverterMap.put("timeStamp", TimeConverter.class.getName());
	}
}
