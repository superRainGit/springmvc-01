package cn.zhang.springmvc.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.zhang.springmvc.beans.User;

//@SessionAttributes(value={"user"})
@Controller
@RequestMapping("/springmvc")
public class SpringMvcTest {
	
	private static final String SUCCESS = "success";
	
	/**
	 * 通过在return语句中 加上 redirect:  或者  forward: 语句进行转发和重定向
	 * 原理是这样的:
	 * 	先调用handler的方法  然后获取对应的视图
	 * 	调用视图解析器将逻辑视图解析为物理视图
	 * 	在视图解析器解析逻辑视图的时候  会判断视图的名称是不是以 redirect: 开头  如果是  就创建一个
	 * 	重定向的视图  ； 如果是以 forward开头  那么创建一个  InternalView 的视图 用来转发 
	 * @return
	 */
	@RequestMapping("/testRedirect")
	public String testRedirect() {
		return "redirect:/index.jsp";
	}
	
	/**
	 * 可以通过自己实现View接口来自定义自己的视图   当需要使用到自己定义的视图的时候
	 * 据需要配置一个BeanNameViewResolver视图解析器  此时方法的返回值就需要是IOC容器中
	 * View视图的名字
	 * @return
	 */
	@RequestMapping("/testView")
	public String testView() {
		return "helloView";
	}
	
	/**
	 * 流程:
	 * 执行目标方法之后  无论返回的是一个String View 还是一个ModeAndView对象   springMVC都会将其转换成一个ModelAndView对象
	 * 然后调用render方法  之后调用视图解析器将视图解析成View对象  然后调用View对象的render方法将视图进行渲染
	 * 最后会获取一个RequestDispatcher对象将视图进行转发  获取到最终的视图结果
	 * 此时需要两个对象  View  接口表示最抽象的视图   最常用的用于使用的视图是 InternalView
	 * ViewResolver接口表示的是视图解析器对象   最常用的对象是 InternaleViewResolver
	 * @return
	 */
	@RequestMapping("/testViewAndViewResolver")
	public String testViewAndViewResolver() {
		return SUCCESS;
	}
	
	/**
	 * 有 @ModelAttribute 修饰的方法  会在每个目标方法执行前被 SpringMVC 调用
	 * @param id
	 * @param map
	 */
	@ModelAttribute
	public void getUser(@RequestParam(value="id", required=false) Integer id, 
			Map<String, Object> map) {
		System.out.println("SpringMvcTest.getUser()");
		if(id != null) {
			User user = new User(1, "Tom", "123456", 12, "tom@sina.com");
			System.out.println("从数据库获取的一个User对象:" + user);
			map.put("user", user);
		}
	}
	
	/**
	 * 运行流程:
	 * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象  把对象放到了 Map 中  键为  user
	 * 2. SpringMVC 中 Map 中 取出  user 对象   并把表单的请求参数赋值给该  User对象
	 * 3. SpringMVC 将上述对象 传入到 目标方法的参数
	 * 
	 * 注意: 在 @ModelAttribute 修饰的方法中  放入到  Map 时的键需要和目标方法入参类型的
	 * 第一个字母小写  的字符串一致!
	 * 
	 * 源代码分析的流程:
	 * 1. 调用 @ModelAttribute 注解 修饰的方法  实际上吧 @ModelAttribute Map 中的数据  放在了implicitModel 中
	 * 2. 解析请求处理器的目标参数 实际上该目标参数来自于 WebDataBinder 的 target 属性
	 *   1) 创建 WebDataBinder 对象:
	 *   	确定objectName属性:若传入的attrName属性值为 "" 则 objectName 为类名的第一个字母小写
	 *   	*注意: attrName 若目标方法的POJO 属性使用了 @ModelAttribute 来修饰 则 attrName 值即为 @ModelAttribute 
	 *   		的value属性值
	 *   	确定target属性:
	 *   		>在implicitModel 中查找 attrName 对应的属性值  若存在  则直接返回其中的数据
	 *   		>如果不存在,则验证当前 Handler 是否使用了 @SessionAttributes 进行修饰  若使用了则尝试从 Session 中获取 attrName
	 *          所对应的属性值  **若 session中没有对应的属性值   则抛出了异常
	 *          >若 Handler 没有使用 @SessionAttribute 进行修饰 或 @SessionAttribtes 中没有使用 value 值指定key和attrName
	 *          相匹配  则通过反射创建了 POJO
	 *    2) SpringMVC 把表单的请求参数  赋给了 WebDataBinder 的 target 对应的属性
	 *    3) *SpringMVC 会把 WebDataBinder 的 attrName 和 target 给到 implicitModel
	 *    4) 把  WebDataBinder 的 target 作为参数传递给目标方法的入参
	 * @param user
	 * @return
	 */
	@RequestMapping("/testModelAttribute")
	public String testModelAttribute(User user) {
		System.out.println("修改的对象是:" + user);
		return SUCCESS;
	}
	
	/**
	 * 如果只是使用 简单的为 目标方法传入一个 简单的Map类型的参数
	 * 此时的数据只会封装到 request 域对象中
	 * 如果想要封装到  session 对象中
	 * 就需要使用 @SessionAttributes 注解
	 * - 使用 value 属性指定  哪些 属性会被存放到 session 的域对象中
	 * - 使用 types 属性指定  哪些  类型的属性将被存放到  session  域对象中
	 * 注:  SessionAttributes 注解 只能 用于修饰类  而不能修饰  方法
	 * @param map
	 * @return
	 */
	@RequestMapping("/testSessionAttributes")
	public String testSessionAttributes(Map<String, Object> map) {
		User user = new User("Tom", "987654321", 22, "123@sina.com");
		map.put("user", user);
		return SUCCESS;
	}
	
	/**
	 * 实际上  可以为目标方法传入一个 Map 类型
	 * (也可以传入一个Model或者ModelMap类型的数据)的参数
	 * 当返回成为 ModelAndView对象时  会将目标方法的返回值封转成为视图的名字
	 * 然后将map中的数据封转成为mode模型
	 * @param map
	 * @return
	 */
	@RequestMapping("/testMap")
	public String testMap(Map<String, Object> map) {
		map.put("users", Arrays.asList("AA", "BB", "CC"));
		return SUCCESS;
	}
	
	/**
	 * 可以通过返回 ModelAndView 对象
	 * 其中可以包含视图和模型信息
	 * SpringMVC会将 ModelAndView的模型数据放入到 request 域对象中
	 * @return
	 */
	@RequestMapping("/testModelAndView")
	public ModelAndView testModeAndView() {
		ModelAndView modelAndView = new ModelAndView(SUCCESS);
		modelAndView.addObject("time", new Date());
		return modelAndView;
	}
	
	/**
	 * SpringMVC支持使用 Servlet 原生的 API
	 * 支持的类型包括
	 * HttpServletRequest
	 * HttpServletResponse
	 * HttpSession
	 * Principal
	 * Locale
	 * InputStream
	 * OutputStream
	 * Writer
	 * Reader
	 * 因此可以直接通过这些 Serlvet 原生的API 进行代码的编写
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/testServletApi")
	public String testServletApi(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("SpringMvcTest.testServletApi() " + request + ", " + response);
		return SUCCESS;
	}
	
	/**
	 * 使用简单的POJO对象映射
	 * SpringMVC 会按请求的参数名和POJO属性进行自动的装配
	 * 自动的为该对象填充属性值   同时支持级联属性
	 * 普通属性即是传入的参数的名字与POJO的属性名一致就可以
	 * 如果是级联属性  就需要使用类似于[address.city]..这样的属性进行级联
	 * @param user
	 * @return
	 */
	@RequestMapping("/testPojo")
	public String testPojo(User user) {
		System.out.println("SpringMvcTest.testPojo():" + user);
		return SUCCESS;
	}
	
	/**
	 * 使用  @CookieValue 注解来映射请求的Cookie中的信息
	 * 一般使用的较少   了解即可   使用方式同   @RequestParam
	 * @param sessionId
	 * @return
	 */
	@RequestMapping("/testCookieValue")
	public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {
		System.out.println("SpringMvcTest.testCookieValue() sessionId : " + sessionId);
		return SUCCESS;
	}
	
	/**
	 * 使用  @RequestHeader 注解来映射请求头信息
	 * 一般使用的较少  使用的方式同 @RequestParam
	 * @param al
	 * @return
	 */
	@RequestMapping("/testRequestHeader")
	public String testRequestHeader(@RequestHeader(value="Accept-Language") String al) {
		System.out.println("SpringMvcTest.testRequestHeader() Accept-Language : " + al);
		return SUCCESS;
	}
	
	/**
	 * 使用 @RequestParam 注解映射  请求的请求参数  
	 * - value  指定该注解映射哪个请求参数的参数名
	 * - required  指定该请求参数是不是必须的  默认是 true  即必须
	 * - defaultValue  指定该请求参数的默认值   
	 * 		如果不指定此参数    则当没有传入指定的参数   且required 属性为false
	 * 		那么就会给  指定的参数赋值为  null 
	 * 		如果此时想要映射到一个基本属性上  那么此时就会抛一个异常:null值不能被赋值到一个基本变量上
	 * @param username
	 * @param age
	 * @return
	 */
	@RequestMapping(value="/testRequestParam")
	public String testRequestParam(@RequestParam("username") String username,
			@RequestParam(value="age", required = false ,defaultValue="0") int age) {
		System.out.println("SpringMvcTest.testRequestParam(), username = " + username + ", age = " + age);
		return SUCCESS;
	}
	
	/**
	 * Rest 风格的 URL
	 * 以CRUD为例:
	 * 	- 新增    /order    POST
	 *  - 修改   /order/1  PUT
	 *  - 获取   /order/1  GET
	 *  - 删除   /order/1  DELETE
	 * 如何 发送  PUT 请求 和 DELETE请求呢?
	 * 	1. 需要配置  HiddenHttpMethodFilter
	 *  2. 需要发送 POST 类型的请求
	 *  3. 需要再发送一个POST请求时  需要  携带一个 name="_method" 的隐藏域   值为 DELETE 或者 PUT
	 *  
	 * 有一个小BUG:  在 Tomcat8(JSP2.3)以后的版本   JSP的访问只支持 GET POST 和   HEAD 请求  
	 * 对  PUT 和    DELETE  请求  不再支持
	 * 如果想要进行SpringMVC的REST风格的URL的映射  那么解决办法就是以下的两种
	 * 	- 就需要使用  Tomcat7及以下版本   
	 *  - 将请求重定向到 另一个  URL 上  进行JSP页面的访问
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/testRest/{id}", method=RequestMethod.DELETE)
	public String testRestDelete(@PathVariable("id") String id) {
		System.out.println("SpringMvcTest.testRestDelete() " + id);
		return SUCCESS;
	}
	
	@RequestMapping(value="/testRest", method=RequestMethod.POST)
	public String testRestPost() {
		System.out.println("SpringMvcTest.testRestPost()");
		return SUCCESS;
	}
	
	@RequestMapping(value="/testRest/{id}", method=RequestMethod.PUT)
	public String testRestPut(@PathVariable("id") int i) {
		System.out.println("SpringMvcTest.testRestPut() " + i);
		return SUCCESS;
	}
	
	@RequestMapping(value="/testRest/{id}", method=RequestMethod.GET)
	public String testRestGet(@PathVariable("id") Integer i) {
		System.out.println("SpringMvcTest.testRestGet() " + i);
		return SUCCESS;
	}
	
	
	/**
	 * 使用 @PathVariable 注解可以映射  @RequestMapping 中的URL中的占位符
	 * @param id
	 * @return
	 */
	@RequestMapping("/testPathVariable/{id}")
	public String testPathVariable(@PathVariable("id") Integer id) {
		System.out.println("SpringMvcTest.testPathVariable() " + id);
		return SUCCESS;
	}
	
	/**
	 * 使用@RequestMapping注解的 params 和 headers 属性可以指定更加详细的匹配规则
	 * 不过一般不常用  了解即可
	 * params 和   headers属性可以还可以进行一些简单的表达式
	 * 一般的简单的表达式有:
	 * 	param1:必须有某一个参数名
	 *  !param1:必须不能有这个参数名
	 *  param1=value1:必须有这个参数名  而且这个值必须是value1
	 *  param1!=value1:必须有这个参数名  而且这个值必须不能是value1
	 * 而且可以使用',' 进行表达式之间的分割   并且它们之间的关系是与(&)的关系
	 * @return
	 */
	@RequestMapping(value="/testParamAndHeader", params={"username", "age=10", "!gender"})
	public String testParamAndHeader() {
		System.out.println("SpringMvcTest.testParamAndHeader()");
		return SUCCESS;
	}
	
	/**
	 * 常用: 使用@RequestMapping注解的method属性指定请求的方式[get/post..]
	 * 可以更加细粒度的映射请求
	 * @return
	 */
	@RequestMapping(value="/testMethod", method=RequestMethod.POST)
	public String testMethod() {
		System.out.println("SpringMvcTest.testMethod()");
		return SUCCESS;
	}

	/**
	 * 1. @RequestMapping 除了修饰方法  还可以修饰类
	 * 2. 
	 * 	- 在类定义处:提供初步的请求信息   相对于WEB应用的根目录
	 *  - 在方法定义处:提供进一步的细分策略
	 *  	相对于类定义处的URL  如果类定义处没有使用@RequestMapping注解修饰 
	 *  	那么方法上标注的URL  相对于WEB应用的根路径
	 * @return
	 */
	@RequestMapping("/testRequestMapping")
	public String testRequestMapping() {
		System.out.println("SpringMvcTest.te stRequestMapping()");
		return SUCCESS;
	}
}
