package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component	//일반적인 스프링 빈
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 실제 이미지가 저장될 경로(서버)
	public static final String FILE_UPLOAD_PATH="D:\\java\\6_spring_project\\memo\\workspace\\images/";
	
	// input: MultipartFile, userLoginId
	// output: imgPath
	public String saveFile(String userLoginId, MultipartFile file) {

		// 파일 디렉토리 ex) aaaa_23011718035458/sun.png
		String directoryName = userLoginId+"_"+System.currentTimeMillis()+"/";	//aaaa_235151/;
		String filePath = FILE_UPLOAD_PATH + directoryName;	//D:\\java\\6_spring_project\\memo\\workspace\\imagesaaaa_23011718035458/
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			// 폴더 만드는데 실패시 imgPath는 null
			return null;
		}
		
		//파일 업로드 : byte 단위로 된다
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath+file.getOriginalFilename());	//파일디렉토리에 파일명붙이기(꼭영문!)
			//originalFileNaame은 사용자가 올린 파일명
			Files.write(path, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴한다.
		// http://localhost/images/aaaa_23011718035458/sun.png
		return "/images/"+directoryName+file.getOriginalFilename();
	}
	
	///이미지 제거 메소드
	public void deleteFile(String imgPath) {	//images/aaaa_23011718035458/sun.png
		//						\\images/				/images/ 중복구문제거
		Path path = Paths.get(FILE_UPLOAD_PATH+imgPath.replace("/images/", ""));
		if(Files.exists(path)) {
			//이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				logger.error("[이미지 삭제]이미지 삭제 실패. imgPath:{}",imgPath);
			}
			//디렉토리(폴더) 삭제
			path = path.getParent();
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.error("[이미지 삭제]디렉토리 삭제 실패. path:{}",path);
				}
			}
		}
	}
	
}
