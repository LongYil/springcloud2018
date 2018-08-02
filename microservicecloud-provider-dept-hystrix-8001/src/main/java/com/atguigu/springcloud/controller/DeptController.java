package com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.enties.Dept;
import com.atguigu.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
public class DeptController
{
	@Autowired
	private DeptService service = null;

	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
	//一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
	@HystrixCommand(fallbackMethod = "processHystrix_Get")
	public Dept get(@PathVariable("id") Long id)
	{

		Dept dept = this.service.get(id);
		
		if (null == dept) {
			throw new RuntimeException("该ID：" + id + "没有没有对应的信息");
		}
		
		return dept;
	}

	public Dept processHystrix_Get(@PathVariable("id") Long id)
	{	
		Dept dept = new Dept();
		dept.setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand");
		dept.setDeptno(id);
		dept.setDb_source("no this database in MySQL");
		return dept;
//		return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand").setDb_source("no this database in MySQL");
	}
	
}

//@RestController
//public class DeptController
//{
//	@Autowired
//	private DeptService service;
//	@Autowired
//	private DiscoveryClient client;
//
//	@RequestMapping(value = "/dept/add", method = RequestMethod.POST)
//	public boolean add(@RequestBody Dept dept)
//	{
//		return service.add(dept);
//	}
//
//	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
//	public Dept get(@PathVariable("id") Long id)
//	{
//		return service.get(id);
//	}
//
//	@RequestMapping(value = "/dept/list", method = RequestMethod.GET)
//	public List<Dept> list()
//	{
//		return service.list();
//	}
//
//	
////	@Autowired
////	private DiscoveryClient client;
//	@RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
//	public Object discovery()
//	{
//		List<String> list = client.getServices();
//		System.out.println("**********" + list);
//
//		List<ServiceInstance> srvList = client.getInstances("MICROSERVICECLOUD-DEPT");
//		for (ServiceInstance element : srvList) {
//			System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
//					+ element.getUri());
//		}
//		return this.client;
//	}
//	
//	@ResponseBody
//	@GetMapping("/sayhello")
//	public String sayHello() {
//		return "hello";
//	}
//	
//	
//}
