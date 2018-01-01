package com.skpm.jee7demo.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import com.skpm.jee7demo.dao.CredentialsDao;

import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(value="loginController")
public class LoginController {

	@Inject
	private CredentialsDao credentialDao;
	private String userName;
	private String password;


	public String login() {
	if("suresh".equals(this.userName) && "suresh".equals(this.password))
	{
		System.out.println("user valid");
	}
	else
	{
		System.out.println("Invalid user");
	}
	return "/faces/login.xhtml?faces-redirect=true";
//		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserName()
	{
		return this.userName;
	}
	public void setPassword(String pass)
	{
		this.password = pass;
	}
	public String getPassword()
	{
		return this.password;
	}

	@PostConstruct
	public void init()
	{
		System.out.println("System started...");
	}

}
