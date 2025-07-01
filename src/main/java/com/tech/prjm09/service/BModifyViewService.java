package com.tech.prjm09.service;

import java.util.Map;

import org.springframework.ui.Model;

import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.dto.BDto;

import jakarta.servlet.http.HttpServletRequest;

public class BModifyViewService implements BServiceInter {

	private final IDao iDao;

	public BModifyViewService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		System.out.println(">>>>>>>>BModifyViewService");
		Map<String, Object> map = model.asMap();
		
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String bid = request.getParameter("bid");
		BDto dto = iDao.modifyView(bid);
		
		model.addAttribute("content_view", dto);
	}

}
