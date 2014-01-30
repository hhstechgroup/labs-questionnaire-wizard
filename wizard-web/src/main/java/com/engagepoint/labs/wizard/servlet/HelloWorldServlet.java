package com.engagepoint.labs.wizard.servlet;

import com.engagepoint.labs.wizard.bean.HelloWorldBean;

import javax.ejb.EJB;
import java.io.IOException;

@javax.servlet.annotation.WebServlet(name = "HelloWorldServlet", urlPatterns = "/helloworld")
public class HelloWorldServlet extends javax.servlet.http.HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8786994986490780818L;
	@EJB
	private HelloWorldBean helloWorldBean;

	protected void doPost(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {

	}

	@Override
	protected void doGet(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		String hello = helloWorldBean.sayHello();
		request.setAttribute("hello", hello);
		request.getRequestDispatcher("/pages/hello.jsp").forward(request, response);
	}
}
