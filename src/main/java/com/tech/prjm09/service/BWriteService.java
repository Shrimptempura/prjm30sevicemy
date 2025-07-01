package com.tech.prjm09.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.prjm09.dao.IDao;

public class BWriteService implements BServiceInter {

	private final IDao iDao;

	public BWriteService(IDao iDao) {
		this.iDao = iDao;
	}

	@Override
	public void execute(Model model) {
		System.out.println(">>>>>>>>BWriteService");
		Map<String, Object> map = model.asMap();
		
		MultipartHttpServletRequest mtfRequest = (MultipartHttpServletRequest) map.get("mtfRequest");
		
		String bname = mtfRequest.getParameter("bname");
		String btitle = mtfRequest.getParameter("btitle");
		String bcontent = mtfRequest.getParameter("bcontent");
		
		System.out.println("title: " + btitle);
		iDao.write(bname, btitle, bcontent);
		
		String workPath = System.getProperty("user.dir");
//		String root = "C:\\hsts2025\\sts25_work\\prjm29replyboard_mpsupdown_multi\\"
//				+ "src\\main\\resources\\static\\files";
		
		String root = workPath + "\\src\\main\\resources\\static\\files";
		
		List<MultipartFile> fileList = mtfRequest.getFiles("file");
		
		int bid = iDao.selBid();
		System.out.println("bid>>>" + bid);
		
		for (MultipartFile mf : fileList) {
			String originalFile = mf.getOriginalFilename();
			System.out.println("files: " + originalFile);
			long longtime = System.currentTimeMillis();
			
			String changeFile = longtime + "_" + originalFile;
			System.out.println("change files: " + changeFile);
			
			String pathfile = root + "\\" + changeFile;
			try {
				if (!originalFile.equals("")) {
					mf.transferTo(new File(pathfile));
					System.out.println("upload success");
					
					// db에 기록
					iDao.imgwrite(bid, originalFile, changeFile);
					System.out.println("rebrdimgtv write sucess");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
