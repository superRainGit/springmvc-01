package cn.zhang.springmvc.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorld {

	/**
	 * 1. 使用@RequestMapping注解来映射请求的URL
	 * 2. 返回值会通过视图解析器解析为实际的物理视图  对于InternalResourceViewResolver 视图解析器 会做如下的解析
	 * prefix + returnVal + suffix
	 * 即 /WEB-INF/views/success.jsp
	 * @return
	 */
	@RequestMapping("/helloWorld")
	public String hello() {
		System.out.println("HelloWorld.hello()");
		return "success";
	}
}
