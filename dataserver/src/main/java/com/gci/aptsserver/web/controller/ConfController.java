package com.gci.aptsserver.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/conf")
public class ConfController {

	

	@RequestMapping(value = "/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("/conf/index");

		return mv;
	}

	

}
