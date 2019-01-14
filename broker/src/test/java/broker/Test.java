
package broker;

import cn.dbw.config.FuncodeEnum;

public class Test {
	
	public static void main(String[] args) {
		String a="a";
		System.out.println(a.getBytes().length);
		System.out.println("{asd".startsWith("{"));
		
		System.out.println(FuncodeEnum.MESSAGE_BROAD.name());
	}

}
