package jef.tools.management;

import jef.tools.Exceptions;
import jef.tools.reflect.ClassUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 信号量处理工具
 * @author Administrator
 *
 */
@Slf4j
public class SignalUtils {
	private static TermHandler TERM=null;

	/**
	 * 获得终止信号量处理器
	 * @return
	 */
	public static synchronized TermHandler getTermHandler() {
		if(TERM==null){
			String clzName;
			if(ClassUtils.isPresent("sun.misc.Signal.Signal", null)){
				clzName="jef.tools.management.SunJdkTERMHandler";
			}else{
				log.error("Can not operate signals: Unknown JDK"+System.getProperty("java.vm.vendor"));
				return null;
			}
			try {
				Class<?> clz=Class.forName(clzName);
				TERM=(TermHandler)clz.newInstance();
				TERM.activate();
			} catch (Exception e) {
				Exceptions.log(e);
			}
		}
		return TERM;
	}
	
	
	
}
