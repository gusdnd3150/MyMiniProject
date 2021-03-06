package com.example.demo.service;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.dao.MyPageDao;
import com.example.demo.file.FileService;
import com.example.demo.hireVo.HireApplyVo;
import com.example.demo.hireVo.HireInfoVo;
import com.example.demo.hireVo.HireMultipleVo;
import com.example.demo.hireVo.HireVo;
import com.example.demo.vo.ApplyVo;
import com.example.demo.vo.MediaVo;
import com.example.demo.vo.MessageVo;
import com.example.demo.vo.PagingVo;
import com.example.demo.vo.PortfolioFileVo;
import com.example.demo.vo.ResumeVo;
import com.example.demo.vo.UserVo;

@Service
public class MyPageService {

	@Autowired
	private MyPageDao dao;
	@Autowired
	private FileService fileService;
	
	//이력서 select
	public List<ResumeVo> selectResume(PagingVo paging){
		return dao.selectResume(paging);
	}
	
	//토탈개수
	public int totalUserResume(int id) {
		return dao.totalUserResume(id);
	}
	
	// 채용 토탈개수
	public int totalHireCount(int id) {
		return dao.totalHireCount(id);
	}
	
	// 채용 리스트 페이징
	public List<HireVo> selectHireListByPaging(PagingVo paging){
		return dao.selectHireListByPaging(paging);
	}
	
	// 1분영상 list
	public List<MediaVo> selectMediaList(int id){
		List<MediaVo> list=null;
		try {
			list= dao.selectMediaList(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int applyTotal(int id) {
		int total =0;
		try {
			
			total=dao.applyTotal( id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	//이력서 상태전환
	public String modResumeState(ResumeVo resume) {
		String result =null;
		int value=0;
		try {
			value=dao.modResumeState(resume);
			if(value > 0) {
				result="success";
			}else {
				result="fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		return result;
	}
	
	//이력서 삭제
	public String deleteResume(int resume_id) {
		String result =null;
		try {
			dao.deleteResume(resume_id);
			//dao.deleteApply(resume_id);
		    result="success";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		return result;
	}
	
	// 파일리스트
	public List<PortfolioFileVo> selectFileList(int id){
		 List<PortfolioFileVo> list =dao.selectFileList(id);
				 
		return list;
		
	}
	
	//구직자 파일 insert
	public String insertFileUserFile(MultipartHttpServletRequest upfile,HttpServletRequest request) {
		String result="";
		PortfolioFileVo fileVo= new PortfolioFileVo();
		
		try {
			fileVo = fileService.uploadUserFile(upfile,request);
			
		    System.out.println("업로드 후 : "+fileVo.toString());
		    
		    dao.insertFileUserFile(fileVo);
		    result="success";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		return result;
	}
	
	//파일삭제
	public String deleteFileUserFile(PortfolioFileVo fileVo,HttpServletRequest request) {
		String result ="";
		try {
			
			fileService.deleteFile(fileVo,request); //파일삭제
			dao.deleteFileUserFile(fileVo);
			
			result="success";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		
		return result;
	}
	
	// 다운로드
	public void downLoadFile(PortfolioFileVo fileVo,HttpServletRequest request
			,HttpServletResponse response) {
		try {
			fileService.downloadFile(fileVo,request,response); //파일삭제
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String insertMediaFileIntro(MultipartHttpServletRequest file,HttpServletRequest request) {
		String result =null;
		MediaVo media =null; 
		UserVo user= (UserVo) request.getSession().getAttribute("USER");
		
		try {
			media =fileService.insertMediaFileIntro(file, request);
			media.setId(user.getId());
			
			dao.insertMedia(media);
			result="success";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		
		return result;
	}
	
	// 채용공고 등록
	public String insertHire(HireMultipleVo hireMultipleVo,HttpServletRequest request) {
		String result =null;
		UserVo user = (UserVo) request.getSession().getAttribute("USER");
		int hire_id=0;
		HireVo hire =hireMultipleVo.getHireVo();
		HireApplyVo hireApply =hireMultipleVo.getHireApplyVo();
		List<HireInfoVo> hireInfo =hireMultipleVo.getHireInfoVoList();
		
		try {
			
			// 채용공고
			hire.setId(user.getId());
			dao.insertHire(hire);
			hire_id = hire.getHire_id();
			
			// 접수방법
			hireApply.setHire_id(hire_id);
			dao.insertHireApply(hireApply);
			
			// 모집상세
			for(int i=0;i<hireInfo.size();i++) {
				hireInfo.get(i).setHire_id(hire_id);
			}
			dao.insertHireInfo(hireInfo);
			
			result ="success";
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		return result;
		
	}
	
	public List<HireVo> selectHireList(){
		return dao.selectHireList();
	}
	
	public String modHireState(HireVo vo) {
		String result=null;
	    
		try {
			
			int test =dao.modHireState(vo);
			if(test > 0) {
				result="success";
			}else {
				result="fail";			
				}
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		
		
		return result;
	}
	
	public MediaVo selectMedia(int media_id) {
		MediaVo media =null;
		
		try {
			media =dao.selectMedia(media_id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return media;
	}
	
	// 메시지 리스트
	public List<MessageVo> selectMessageList(PagingVo paging){
		return dao.selectMessageList(paging);
	}
	
	public int totalUserMessage(int id) {
		return dao.totalUserMessage(id);
	}
	
	public void updateMessageCheck(int id) {
		dao.updateMessageCheck(id);
	}
	
	public String insertMessage(MessageVo message) {
		String result="";
		try {
			int after = dao.insertMessage(message);
			
			if(after >0) {
				result="success";
			}else {
				result="fail";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result="fail";
		}
		
		return result;
	}
	
	public List<ApplyVo> selectApplyListByPaging(PagingVo paging){
		List<ApplyVo> list =null;
		try {
			
			list =dao.selectApplyListByPaging(paging);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	// 지원자 목록 select
	public List<ApplyVo> selectApplyListById(int hire_id){
		return dao.selectApplyListById(hire_id);
	}
	
}
