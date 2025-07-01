package com.tech.prjm09.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.dto.BDto;
import com.tech.prjm09.dto.ReBrdimgDto;
import com.tech.prjm09.service.BContentViewService;
import com.tech.prjm09.service.BDownloadService;
import com.tech.prjm09.service.BListService;
import com.tech.prjm09.service.BServiceInter;
import com.tech.prjm09.service.BWriteService;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BController {
	
	private BServiceInter bServiceInter;
	
	@Autowired
	private IDao iDao;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	
	@RequestMapping("list")
	public String list(HttpServletRequest request, SearchVO searchVO, Model model) {
		System.out.println("list() ctr");
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		
		bServiceInter = new BListService(iDao);
		bServiceInter.execute(model);
		
		return "list";
	}
	
	@RequestMapping("write_view")
	public String write_view(Model model) {
		System.out.println("wirte_view() ctr");
		return "write_view";
	}
		
	@RequestMapping("write")
	public String write(MultipartHttpServletRequest mtfRequest, Model model) {
		System.out.println("write() ctr");
		
		model.addAttribute("mtfRequest", mtfRequest);
		
		bServiceInter = new BWriteService(iDao);
		bServiceInter.execute(model);
		
		return "redirect:list";
	}
	
	@GetMapping("download")
	public String download(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		System.out.println("download() ctr");
		
		model.addAttribute("request", request);
		model.addAttribute("response", response);
		
		bServiceInter = new BDownloadService(iDao);
		bServiceInter.execute(model);
		
		String bid = request.getParameter("bid");
		
		return "content_view?bid = " + bid;
	}
	
	@GetMapping("content_view")
	public String content_view(HttpServletRequest request, Model model) {
		System.out.println("content_view() ctr");
		
		model.addAttribute("request", request);
		bServiceInter = new BContentViewService(iDao);
		bServiceInter.execute(model);

		return "content_view";
	}
	
	@GetMapping("modify_view")
	public String modify_view(HttpServletRequest request, Model model) {
		System.out.println("modify_view() ctr");
//		model.addAttribute("request", request);
//		command = new BModifyViewCommand();
//		command.execute(model);
		
		String bid = request.getParameter("bid");
		BDto dto = iDao.modifyView(bid);
		
		model.addAttribute("content_view", dto);
		
		return "modify_view";
	}
	
	@PostMapping("modify")
	public String modify(HttpServletRequest request, Model model) {
		System.out.println("modify() ctr");		
//		model.addAttribute("request", request);
//		command = new BModifyCommand();
//		command.execute(model);
		
		String bid = request.getParameter("bid");
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		
		iDao.modify(bid, bname, btitle, bcontent);
		
		return "redirect:list";
	}
	
	@GetMapping("reply_view")
	public String reply_view(HttpServletRequest request, Model model) {
		System.out.println("reply_view() ctr");		
//		model.addAttribute("request", request);
//		command = new BReplyViewCommand();
//		command.execute(model);
		
		String bid = request.getParameter("bid");
		
		BDto dto = iDao.reply_view(bid);
		model.addAttribute("reply_view", dto);
		
		return "reply_view";
	}
	
	@PostMapping("reply")
	public String reply(HttpServletRequest request, Model model) {
		System.out.println("reply() ctr");
//		model.addAttribute("request", request);
//		command = new BReplyCommand();
//		command.execute(model);
		String bid = request.getParameter("bid");
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		
		String bgroup = request.getParameter("bgroup");
		String bstep = request.getParameter("bstep");
		String bindent = request.getParameter("bindent");
		
		iDao.replyShape(bgroup, bstep);
		iDao.reply(bid, bname, btitle, bcontent, bgroup, bstep, bindent);
		
		return "redirect:list";
	}
	
	@RequestMapping("delete")
	public String delete(HttpServletRequest request, Model model) {
		System.out.println("delete ctr");
//		model.addAttribute("request", request);
//		command = new BDeleteCommand();
//		command.execute(model);
		
		String bid = request.getParameter("bid");
		iDao.delete(bid);
		
		return "redirect:list";
	}
	
	
	
}
