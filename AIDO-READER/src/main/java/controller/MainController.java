package controller;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import exception.*;
import logic.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MainController {
	Integer [] UNIC = {33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,58,59,60,61,62,63,64,123,124,125,126};
	String osname = System.getProperty("os.name").split(" ")[0];
	String opath = (osname.equals("Windows"))?"C:":"/home/ec2-user";
	@Autowired
	private AIService service;

	@GetMapping("LOGIN")
	public ModelAndView login(HttpSession session,String a) {
		ModelAndView mav = new ModelAndView();
		session.invalidate();
		return mav;
	}
	@PostMapping("MAIN2")
	public ModelAndView main(HttpSession session,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		System.out.println("a");
		String ID = request.getParameter("ID");
		String PWD = request.getParameter("PWD");
		System.out.println("id : " +ID+" pass :" + PWD);
		MBR mbr = service.selectOne(ID,PWD);
		String name =mbr.getEMAIL_ADR().substring(0, mbr.getEMAIL_ADR().indexOf('@'));
		System.out.println(mbr.getBSTOR_CD()+","+mbr.getCMPN_CD());
		mbr.setNAME(name);
		session.setAttribute("MBR", mbr);
		mav.addObject("id",ID);
		mav.addObject("pwd",PWD);
		return mav;
	}
//	@GetMapping("FIND_IDPWD")
//	public ModelAndView findIdPw(String a) {
//		return mav;
//	}
	@PostMapping("FIND_IDPWD")
	public ModelAndView findIdPw2(String customeremail) throws Exception {
		ModelAndView mav = new ModelAndView("alert");
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/dooraymail.properties");
			props.load(fis);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MBR mbr = new MBR();
		try {
			mbr = service.selectemail(customeremail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int temppwd;
		if(mbr == null) {
			throw new LoginException("해당 이메일을 가진 회원이 존재하지 않습니다.", "FIND_IDPWD.ai");
		}else {
			temppwd = (int)(Math.random()*10000)+1000;
			try {
				service.chgpass(mbr.getMBR_NO(), temppwd+"");	
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new LoginException("비밀번호 재발급중 오류발생 02-0000-0000으로 문의 해주세요", "FIND_IDPWD");
			}
		}
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.userpw")); 
				} 
			}); 
		try { 
			MimeMessage message = new MimeMessage(session); 
			message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user"))); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(customeremail)); 	
			// 메일 제목 
			message.setSubject(MimeUtility.encodeText("AIDO-READER 아이디 비밀번호 안내", "UTF-8", "B")); 
			// 메일 내용 
			message.setText(MimeUtility.encodeText("회원님의 아이디는 : " + mbr.getEMAIL_ADR().substring(0,mbr.getEMAIL_ADR().indexOf("@")) +"\n임시비밀번호는 : "+temppwd),"UTF-8", "B");
			// send the message 
			Transport.send(message); 
//			//System.out.println("Success Message Send"); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		}
		mav.addObject("msg","메일 전송이 완료 되었습니다.");
		mav.addObject("url","LOGIN.ai");
		return mav;
	}
	
	@PostMapping("MAIN-INQR")
	public ModelAndView inquire(MultipartHttpServletRequest request,HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("alert");
		Properties props = new Properties();
		String memail = request.getParameter("memail");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/dooraymail.properties");
			props.load(fis);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		javax.mail.Session Msession = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.userpw")); 
				} 
			}); 
		try { 
			MimeMessage message = new MimeMessage(Msession); 
			message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user"))); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(props.getProperty("mail.smtp.user")));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(memail));
			// 메일 제목 
			message.setSubject(MimeUtility.encodeText(title, "UTF-8", "B")); 
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message2= new MimeBodyPart();
			
			//내용
			message2.setContent(content, "text/plain");
			multipart.addBodyPart(message2);
			//내용
			
			//첨부파일
			MultipartFile mf = request.getFile("file1");
			if((mf != null) && (!mf.isEmpty())) {
				multipart.addBodyPart(bodyPart(mf));
			}
			message.setContent(multipart);
			Transport.send(message);	//메일 전송.
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		}
		mav.addObject("msg","메일 전송이 완료 되었습니다.");
		mav.addObject("url","MAIN.ai");
		return mav;
	}
	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		//업로드 파일의 이름
		String orgFile = mf.getOriginalFilename();
		//업로드 되는 위치
		String path = opath+"/mailupload/";
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}else {
			File[] deletefile = f.listFiles();
			if(deletefile != null) {
    			for(File a1 : deletefile) {
    				a1.delete();
    			}
    		}
		}
		File f1 = new File(path + orgFile);	//업로드된 내용을 저장하는 파일
		try {
			mf.transferTo(f1);	//업로드 완성
			body.attachFile(f1);	//메일 첨부
			//첨부파일이름 설정
			body.setFileName(new String(orgFile.getBytes("UTF-8"), "8859_1"));
//			body.setFileName(new String(orgFile.getBytes("UTF-8"), "8859_1"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	
/*	@PostMapping("FIND_IDPWD")
	public ModelAndView findIdPw2(String customeremail) {
		ModelAndView mav = new ModelAndView("alert");
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/Users/uddon/Desktop/mail.properties");
			props.load(fis);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MBR mbr = new MBR();
		try {
			mbr = service.selectemail(customeremail);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(mbr +"null");
		}
		int temppwd;
		if(mbr == null) {
			throw new LoginException("해당 이메일을 가진 회원이 존재하지 않습니다.", "FIND_IDPWD.ai");
		}else {
			temppwd = (int)(Math.random()*10000)+1000;
			try {
				service.chgpass(mbr.getMBR_NO(), temppwd+"");	
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new LoginException("비밀번호 재발급중 오류발생 02-0000-0000으로 문의 해주세요", "FIND_IDPWD");
			}
			//System.out.println(temppwd);
			}
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.userpw")); 
				} 
			}); 
		try { 
			MimeMessage message = new MimeMessage(session); 
			message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user"))); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(customeremail)); 	
			// 메일 제목 
			message.setSubject("AIDO-READER 아이디 비밀번호 안내"); 
			// 메일 내용 
			message.setText("회원님의 아이디는 : " + mbr.getEMAIL_ADR().substring(0,mbr.getEMAIL_ADR().indexOf("@")) +"\n임시비밀번호는 : "+temppwd); 
			// send the message 
			Transport.send(message); 
			//System.out.println("Success Message Send"); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		}
		mav.addObject("msg","메일 전송이 완료 되었습니다.");
		mav.addObject("url","LOGIN.ai");
		return mav;
	}
*/
		
//		------------------------------------
		
//		---------------------------------
		/*//System.out.println(customeremail);
		ModelAndView mav = new ModelAndView("alert");
		Mail mail = new Mail(); 
		Properties prop = new Properties();
		MBR mbr = new MBR();
		try {
			mbr = service.selectemail(customeremail);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(mbr +"null");
		}
		
		if(mbr == null) {
			throw new LoginException("해당 이메일을 가진 회원이 존재하지 않습니다.", "FIND_IDPWD.ai");
		}else {
			int temppwd = (int)(Math.random()*10000);
			try {
				service.chgpass(mbr.getMBR_NO(), temppwd+"");	
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new LoginException("비밀번호 재발급중 오류발생 02-0000-0000으로 문의 해주세요", "FIND_IDPWD");
			}
			
			try {
				FileInputStream fis = new FileInputStream(opath+"/Users/uddon/Desktop/mail.properties");
				prop.load(fis);	//mail.properties의 내용을 Properties(Map 객체)객체로 로드.
				mail.setNaverid(prop.getProperty("mail.smtp.user"));
				mail.setNaverpw(prop.getProperty("mail.smtp.userpw"));
				mail.setContents("회원님의 아이디는 : " + mbr.getEMAIL_ADR().substring(0,mbr.getEMAIL_ADR().indexOf("@")) +"\n임시비밀번호는 : "+temppwd);
				mail.setTitle("AIDO-READER 아이디 비밀번호 안내");
				mail.setRecipient(mbr.getEMAIL_ADR());
				mail.setMtype("text/plain");
			} catch(IOException e) {
				e.printStackTrace();
				//System.out.println( "email null");
			}
		}
		mailSend1(mail);
		mav.addObject("msg","메일 전송이 완료 되었습니다.");
		mav.addObject("url","LOGIN.ai");
		return mav;
	}*/
	/*
	private final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}
	}
	private void mailSend1(Mail mail) {
		//네이버 메일 전송을 위한 인증 객체
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(), mail.getNaverpw());
		//메일 전송을 위한 환경 변수 설정
		Properties prop = new Properties();
		//session : 메일 전송을 위한 객체
		javax.mail.Session msession = javax.mail.Session.getInstance(prop, auth);
		//mimeMsg : 메일 내용을 저장하기 위한 객체
		MimeMessage mimeMsg = new MimeMessage(msession);
		try {
			//보내는이 설정.
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid()+"@naver.com"));
			String email = mail.getRecipient().substring(0,mail.getRecipient().indexOf("@"))+"<"+mail.getRecipient()+">";
			InternetAddress addrs = new InternetAddress();	
			try {
					addrs = new InternetAddress(new String(email.getBytes("utf-8"), "8859_1"));
				} catch (UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}			
			//보낸일자
			mimeMsg.setSentDate(new Date());
			//받는사람들
			mimeMsg.setRecipient(Message.RecipientType.TO,addrs);
			//제목
			mimeMsg.setSubject(mail.getTitle());
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			//내용
			message.setContent(mail.getContents(), mail.getMtype());
			multipart.addBodyPart(message);
			//첨부파일
			for(MultipartFile mf : mail.getFile1()) {
				if((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			mimeMsg.setContent(multipart);
			Transport.send(mimeMsg);	//메일 전송.
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		//업로드 파일의 이름
		String orgFile = mf.getOriginalFilename();
		//업로드 되는 위치
		String path = "d:/20200224/spring/mailupload/";
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		File f1 = new File(path + orgFile);	//업로드된 내용을 저장하는 파일
		try {
			mf.transferTo(f1);	//업로드 완성
			body.attachFile(f1);	//메일 첨부
			//첨부파일이름 설정
			body.setFileName(new String(orgFile.getBytes("UTF-8"), "8859_1"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return body;
	}*/
	/*
	private void mailSend(Mail mail) {
		//네이버 메일 전송을 위한 인증 객체
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(), mail.getNaverpw());
		//메일 전송을 위한 환경 변수 설정
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/Users/uddon/Desktop/mail.propertiesmail.properties");
			prop.load(fis);	//mail.properties의 내용을 Properties(Map 객체)객체로 로드.
			prop.put("mail.smtp.user", mail.getNaverid());
		} catch(IOException e) {
			e.printStackTrace();
		}
		//session : 메일 전송을 위한 객체
		Session session = Session.getInstance(prop, auth);
		//mimeMsg : 메일 내용을 저장하기 위한 객체
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			//보내는이 설정.
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid()+"@naver.com"));
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			//홍길동 <hong@aaa.bbb>, 김삿갓 <kim@bbb.ccc> 형태의 수신자를 ,를 기준으로 분리
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {*/
					/*
					 * new String(email.getBytes("utf-8"), "8859_1")
					 * - email.getBytes("utf-8") : email 문자열을 byte[] 형태로 변경. utf-8 문자로 인식
					 * - 8859_1 : byte[]배열을 8859_1로 변경하여 문자열로 생성
					 * - 웹 환경(수신된 메일에서 한글이름 유지되도록)의 타입은 8859_1 타입형태
					 */
					/*addrs.add(new InternetAddress(new String(email.getBytes("utf-8"), "8859_1")));
				} catch (UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0; i<addrs.size(); i++) {
				arr[i] = addrs.get(i);
			}
			//보낸일자
			mimeMsg.setSentDate(new Date());
			//받는사람들
			mimeMsg.setRecipients(Message.RecipientType.TO,arr);
			//제목
			mimeMsg.setSubject(mail.getTitle());
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			//내용
			message.setContent(mail.getContents(), mail.getMtype());
			multipart.addBodyPart(message);
			//첨부파일
			for(MultipartFile mf : mail.getFile1()) {
				if((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			mimeMsg.setContent(multipart);
			Transport.send(mimeMsg);	//메일 전송.
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}*/
	
	@GetMapping("*")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		return mav;
		}
	//0405_수정사항
	@PostMapping("LOGIN")
	public ModelAndView Login(HttpSession session,String ID, String PWD) {
		ModelAndView mav = new ModelAndView();
		if(ID == null || ID.trim().equals("")) {
			throw new LoginException("아이디를 입력하세요", "LOGIN.ai");
		}else if(PWD == null || PWD.trim().equals("")) {
			throw new LoginException("비밀번호를 입력하세요", "LOGIN.ai");
		}
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/URL.properties");
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String SERVER1 = prop.getProperty("SERVER1");
		String SERVER2 = prop.getProperty("SERVER2");
		String SERVER3 = prop.getProperty("SERVER3");
		String SERVER4 = prop.getProperty("SERVER4");
		String SERVER5 = prop.getProperty("SERVER5");

		MBR mbr = service.selectOne(ID,PWD);
		if(mbr == null) {
			throw new LoginException("아이디를 확인하세요", "LOGIN.ai");
		}else if(mbr.getDEL_YN().equals("Y")) {
			throw new LoginException("이용할 수 없는 아이디 입니다.", "LOGIN.ai");
		}else {
			if(mbr.getPWDCHK().equals("1")) {
				mbr.setNAME(mbr.getEMAIL_ADR().substring(0, mbr.getEMAIL_ADR().indexOf('@')));
				session.setAttribute("MBR", mbr);
				String name = mbr.getNAME();
				Integer mbrno = mbr.getMBR_NO();
//				System.out.println("s12");
				mav.addObject("ID", ID);
				mav.addObject("PWD",PWD);
				if(mbr.getBSTOR_CD() == 5){
					Integer ecitest = Integer.parseInt(name.substring(name.length()-1));
					System.out.println("adfs : " + ecitest);
					if(ecitest == 1 || ecitest == 6 ){
						if(httptest(SERVER1)) {
							mav.setViewName("SERVER1");
						}else{
							mav.setViewName("redirect:MAIN.ai");
						}
					}else if(ecitest == 2 || ecitest == 7){
						if(httptest(SERVER2)) {
							mav.setViewName("SERVER2");
						}else{
							mav.setViewName("redirect:MAIN.ai");
						}
					}else if(ecitest == 3 ){
						if(httptest(SERVER3)) {
							mav.setViewName("SERVER3");
						}else{
							mav.setViewName("redirect:MAIN.ai");
						}
					}else if(ecitest == 4 ){
						if(httptest(SERVER4)) {
							mav.setViewName("SERVER4");
						}else{
							mav.setViewName("redirect:MAIN.ai");
						}
					}else{
						if(httptest(SERVER5)) {
							mav.setViewName("SERVER5");
						}else{
							mav.setViewName("redirect:MAIN.ai");
						}
					}
				}else{
					mav.clear();
					mav.setViewName("redirect:MAIN.ai");
				}
			}else {
				throw new LoginException("비밀번호를 확인하세요", "LOGIN.ai"); 
			}	
		}
		return mav;
	}

	public boolean  httptest(String ip){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","text/html; charset=utf-8");
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet("http://"+ip);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(2*1000)
				.setConnectTimeout(2*1000)
				.setConnectionRequestTimeout(2*1000)
				.build();
		httpget.setConfig(requestConfig);

		HttpResponse response = null;
		try {
			response = client.execute(httpget);
			if (response.getStatusLine().getStatusCode() >=200 && response.getStatusLine().getStatusCode() <=300 ) {
				System.out.println("[RESPONSE] " + response.getStatusLine().getStatusCode());
				return true;
			} else {
				return false;
			}
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@RequestMapping("serverno")
	public ResponseEntity<JSONArray> serverno(HttpServletRequest request ,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		MBR mbr = (MBR)session.getAttribute("MBR");

		String serverno = request.getParameter("serverno");

		Properties servernoprop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/URL.properties");
			servernoprop.load(fis);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		////System.out.println(mbr);
		String serverip = servernoprop.getProperty("SERVER"+serverno);

		responseHeaders.add("Content-Type","application/json; charset=utf-8");


		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("serverip" ,serverip);
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}

	@PostMapping("MAIN_INI_ADM")
	public ModelAndView passchg(String currentpass,String newpass,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		MBR s_mbr = (MBR) session.getAttribute("MBR");
		MBR mbr = service.selectOne(s_mbr.getEMAIL_ADR().substring(0, s_mbr.getEMAIL_ADR().indexOf("@")),currentpass);
		if(mbr.getPWDCHK().equals("1")) {
			try {
				service.chgpass(mbr.getMBR_NO(),newpass);
				MBR mbr2 = service.selectOne(s_mbr.getEMAIL_ADR().substring(0, s_mbr.getEMAIL_ADR().indexOf("@")),newpass);
				mbr2.setNAME(mbr2.getEMAIL_ADR().substring(0, mbr2.getEMAIL_ADR().indexOf('@')));
				session.setAttribute("MBR", mbr2);
				mav.addObject("msg","비밀번호 변경 완료"); 
				mav.addObject("url","MAIN.ai");
			} catch (Exception e) {
				e.printStackTrace();
				throw new LoginException("비밀번호 변경중 오류 발생", "MAIN-INI_ADM.ai");
			} 
		}
		mav.setViewName("alert");
		return mav;
	}

	@RequestMapping({"OCR_EXAM_ADM","OCR_TYPE_ADM"})
	public ModelAndView examklist(Integer pageNum, String searchcontent, Integer limit, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		//한페이지당 보여질 게시물의 건수
		int listcount = service.OCR_EXAM_Count(searchcontent);
		//System.out.println(pageNum+","+limit+","+searchcontent);
		List<OCR_EXAM> OCR_EXAM_List = service.getOcrExamList(pageNum, limit, searchcontent);
		////System.out.println(OCR_EXAM_List.toString());
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("EXAMList", OCR_EXAM_List);
		return mav;
	}
	
//	delocrex
	
	@RequestMapping("copyexocrrow")
	public ResponseEntity<JSONArray> copyexocrrow(HttpServletRequest request ,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
//		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		String schyr = request.getParameter("schyr");
		Integer exam_kind = Integer.parseInt(request.getParameter("examkind"));
		String exam_nm = request.getParameter("exam_nm");
		String examcd = request.getParameter("examcd");
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		try {
			service.insertcexrow(schyr,exam_kind,exam_nm,mbrno);
			String excd =service.getnewocrexamcd(schyr,exam_kind,exam_nm);
			List<Integer> copyblist = service.getocrexBlist(examcd);
			List<Integer> blist = service.getBlist();
			for(Integer bstor : blist) {
				service.insert_bstor_exam(excd,bstor,exam_kind,cmpncd);	
			}
			for(Integer cp : copyblist) {
				service.update_bstor_exam(excd,cp,cmpncd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		rec.setEXAM_DT(rec.getEXAM_DT().substring(0,4)+"-"+rec.getEXAM_DT().substring(4,6)+"-"+rec.getEXAM_DT().substring(6));
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result" ,"시험추가 완료");
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	
	@RequestMapping("createexocrrow")
	public ResponseEntity<JSONArray> createexocrrow(HttpServletRequest request ,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
//		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		String schyr = request.getParameter("schyr");
		Integer exam_kind = Integer.parseInt(request.getParameter("examkind"));
		String exam_nm = request.getParameter("exam_nm");
//		String examcd = request.getParameter("examcd");
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		try {
			service.insertcexrow(schyr,exam_kind,exam_nm,mbrno);
			String excd =service.getnewocrexamcd(schyr,exam_kind,exam_nm);
			List<Integer> blist = service.getBlist();
			for(Integer bstor : blist) {
				service.insert_bstor_exam(excd,bstor,exam_kind,cmpncd);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		rec.setEXAM_DT(rec.getEXAM_DT().substring(0,4)+"-"+rec.getEXAM_DT().substring(4,6)+"-"+rec.getEXAM_DT().substring(6));
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result" ,"시험추가 완료");
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	@RequestMapping("BSTOR_RC_REG_SN")
	public ModelAndView SNlist(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		int listcount = service.OCR_EXAM_Count(searchcontent,1);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList(pageNum, limit, searchcontent,1,bstor_cd);
		////System.out.println(OCR_EXAM_List);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("EXAMList", OCR_EXAM_List);
		//System.out.println(OCR_EXAM_List);
		return mav;
	}
	
	@RequestMapping("BSTOR_RC_REG_MOCK")
	public ModelAndView MOCKlist(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}	
		}
		if(limit == null) {
			limit=5;
		}
		//한페이지당 보여질 게시물의 건수
		int listcount = service.OCR_EXAM_Count(searchcontent,2);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList(pageNum, limit, searchcontent,2,bstor_cd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("EXAMList", OCR_EXAM_List);
		return mav;
	}
	@RequestMapping("BSTOR_RC_REG_IR")
	public ModelAndView IRlist(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}

		int listcount = service.OCR_EXAM_Count(searchcontent,3);
		System.out.println("A");
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList(pageNum, limit, searchcontent,3,bstor_cd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("EXAMList", OCR_EXAM_List);
		return mav;
	}
	
	@RequestMapping("BSTOR_RC_ADM_SN")
	public ModelAndView SN_ADM_list(Integer pageNum, String searchcontent,Integer limit,String searchtype, HttpSession session) {
		ModelAndView mav = new ModelAndView();
//		//System.out.println("searchtype :" +searchtype);
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		Integer exam_cd = service.minocrcd(1);
		if(searchtype == null || searchtype.trim().equals("")) {
			searchtype=exam_cd.toString();
		}else{
			exam_cd = Integer.parseInt(searchtype);
		}
		System.out.println("limit : " + limit);
		System.out.println("search : " + searchcontent);
		System.out.println("page : " + pageNum);
		System.out.println("ex : " +searchtype);
//		int exam_cd = Integer.parseInt(searchtype);
		int OCR_Cd = service.ocr_bstor_ocrcd(exam_cd+"");
		int listcount = service.SNCount(searchcontent,OCR_Cd,exam_cd);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList2(pageNum, limit,OCR_Cd,bstor_cd);
		//List<BSTOR> BSTOR_LIST = service.getBSTORList(pageNum, limit, searchcontent,1);
		List<BSTOR> BSTOR_WARNING = service.getBSTORWANING(pageNum, limit, searchcontent,OCR_Cd,exam_cd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("searchtype", searchtype);
		mav.addObject("EXAMList", OCR_EXAM_List);
		mav.addObject("BSTORList",BSTOR_WARNING);
		mav.addObject("examcd2",exam_cd);
//		////System.out.println(BSTOR_WARNING);
		//////System.out.println(OCR_EXAM_List);
		return mav;
	}
	
	@RequestMapping("BSTOR_RC_ADM_MOCK")
	public ModelAndView MOCK_ADM_list(Integer pageNum, String searchcontent,Integer limit,String searchtype, HttpSession session) {
		ModelAndView mav = new ModelAndView();
//		//System.out.println("searchtype :" +searchtype);
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		Integer exam_cd = service.minocrcd(2);
		if(searchtype == null || searchtype.trim().equals("")) {
			searchtype=exam_cd.toString();
		}else{
			exam_cd = Integer.parseInt(searchtype);
		}
//		//System.out.println("limit : " + limit);
//		//System.out.println("search : " + searchcontent);
//		//System.out.println("page : " + pageNum);
//		//System.out.println("ex : " +searchtype);
		int OCR_Cd = service.ocr_bstor_ocrcd(exam_cd+"");
		int listcount = service.SNCount(searchcontent,OCR_Cd,exam_cd);
//		//System.out.println(listcount);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList2(pageNum, limit,OCR_Cd,bstor_cd);
		//List<BSTOR> BSTOR_LIST = service.getBSTORList(pageNum, limit, searchcontent,1);
		List<BSTOR> BSTOR_WARNING = service.getBSTORWANING(pageNum, limit, searchcontent,OCR_Cd,exam_cd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("searchtype", searchtype);
		mav.addObject("EXAMList", OCR_EXAM_List);
		mav.addObject("BSTORList",BSTOR_WARNING);
		mav.addObject("exam_cd",exam_cd);
//		//System.out.println(BSTOR_WARNING);
//		//System.out.println(OCR_EXAM_List);
		return mav;
	}
	
	@RequestMapping("BSTOR_RC_ADM_IR")
	public ModelAndView IR_ADM_list(Integer pageNum, String searchcontent,Integer limit,String searchtype, HttpSession session) {
		ModelAndView mav = new ModelAndView();
//		//System.out.println("searchtype :" +searchtype);
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		Integer exam_cd = service.minocrcd(3);
		if(searchtype == null || searchtype.trim().equals("")) {
			searchtype=exam_cd.toString();
		}else{
			exam_cd = Integer.parseInt(searchtype);
		}
//		//System.out.println("limit : " + limit);
//		//System.out.println("search : " + searchcontent);
//		//System.out.println("page : " + pageNum);
//		//System.out.println("ex : " +searchtype);
		int OCR_Cd = service.ocr_bstor_ocrcd(exam_cd+"");
		int listcount = service.SNCount(searchcontent,OCR_Cd,exam_cd);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OCR_EXAM> OCR_EXAM_List = service.getSNExamList2(pageNum, limit,OCR_Cd,bstor_cd);
		//List<BSTOR> BSTOR_LIST = service.getBSTORList(pageNum, limit, searchcontent,1);
		List<BSTOR> BSTOR_WARNING = service.getBSTORWANING(pageNum, limit, searchcontent,OCR_Cd,exam_cd);
		//System.out.println(BSTOR_WARNING);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("searchtype", searchtype);
		mav.addObject("EXAMList", OCR_EXAM_List);
		mav.addObject("BSTORList",BSTOR_WARNING);
		mav.addObject("exam_cd",exam_cd);
//		//System.out.println(BSTOR_WARNING);
//		//System.out.println(OCR_EXAM_List);
		return mav;
	}
	
	@RequestMapping(value="examrec")
	public ResponseEntity<JSONArray> mtest(HttpServletRequest request,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examCd = request.getParameter("examCd");
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent").trim();
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
		System.out.println("rsearchcontent:"+rsearchcontent+"\n examCd :"+ examCd +"\n rlimit :"+ rlimit+"\n rpageNum :"+ rpageNum);
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		int recodecnt = 0;
		List<OCR_EXAM_REC> exam_rec = new ArrayList<>();
		try {
			exam_rec= service.OcrExamRec(examCd,rlimit,rpageNum,rsearchcontent);
			recodecnt = service.OCR_EXAMRec_Count(examCd,rsearchcontent);
			System.out.println("cnt : " + recodecnt);
			System.out.println("rec : " + exam_rec.toString());
		}catch (Exception e5){
			e5.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		
		if(exam_rec.size()>0 ) {
			for(OCR_EXAM_REC rec : exam_rec) {
//				//System.out.println(rec);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("bstor_cd" , rec.getBSTOR_CD());
				jsonObject.put("bstor_nm" , rec.getBSTOR_NM());
				jsonObject.put("sttus" , rec.getCD_NM());
				jsonObject.put("sttus_cd" , rec.getSTTUS_CD());
				jsonObject.put("useyn" , rec.getUSE_YN());
				//System.out.print(rec.getUSE_YN()+"   ");
				jsonArray.add(jsonObject);
				System.out.println(jsonObject);
				/*String branch=null;
				try {
					branch = URLDecoder.decode(rec.getBSTOR_NM(), "UTF-8");
					//System.out.println(branch);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				html.append("<tr><td><input type=\"checkbox\"></td><td>"+branch+"</td><td>"+rec.getCD_NM()+"</td></tr>");*/
			}
			int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
			int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
			int rendpage = rstartpage + 9;
			if (rendpage > rmaxpage) rendpage = rmaxpage;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rlimit", rlimit);
			jsonObject.put("rpageNum", rpageNum);
			jsonObject.put("rmaxpage",rmaxpage);
			jsonObject.put("rstartpage",rstartpage);
			jsonObject.put("rendpage",rendpage);
			jsonObject.put("recodecnt",recodecnt);
			jsonObject.put("r_cnt" , recodecnt);
			jsonArray.add(jsonObject);
		}
//		html.append("</tbody></table>");
//		//System.out.println(html.toString());
		////System.out.println(exam_rec);
//		response.setCharacterEncoding("UTF-8");
//		return html.toString();
//		return exam_rec;
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="exampic")
	public ResponseEntity<JSONArray> ocrpic(HttpServletRequest request ,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examCd = request.getParameter("examCd");
		int OCR_Cd = service.ocr_bstor_ocrcd(examCd);
		File download = new File(request.getServletContext().getRealPath("/")+"images/ocr");
		String jsppath ="images/ocr/"+OCR_Cd+".jpg";
		if(!download.exists()) download.mkdirs();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject(); 
		jsonObject.put("imgsrc" ,jsppath);
		jsonArray.add(jsonObject);	
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	 @RequestMapping("chkocrex")
	  public ResponseEntity<JSONArray> chkocrex(HttpServletRequest request, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    String examcd = request.getParameter("examcd");
	    String bstorcd = request.getParameter("bstorcd");

	    String useyn = request.getParameter("useyn");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    Integer cmpncd = Integer.valueOf(mbr.getCMPN_CD());
	    Integer mbrno = mbr.getMBR_NO();
	    JSONArray jsonArray = new JSONArray();
	    JSONObject jsonObject = new JSONObject();
	    try {
	      service.examocruseyn(examcd,bstorcd,useyn, cmpncd,mbrno);
	    } catch (Exception e) {
	      jsonObject.put("e", e.toString());
	    } 
	    jsonArray.add(jsonObject);
	    return new ResponseEntity<>(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	
	/*@GetMapping("insert_bstor_exam")
	@ResponseBody
	public ResponseEntity<JSONArray> reg_bstor_exam(String examCd,String bstorCd,HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		int OCR_Cd = service.ocr_bstor_ocrcd(examCd);
		MBR mbr = (MBR) session.getAttribute("MBR");
        int  CMPN_CD = mbr.getCMPN_CD();
        JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject(); 
		try {
			//service.insert_bstor_exam(examCd,bstorCd,OCR_Cd,CMPN_CD);
			String bs = service.getbstor(bstorCd);
			jsonObject.put("STTUS" ,bs + "점 응시 대상 추가 완료");
			jsonArray.add(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("STTUS" ,"추가 실패");
			jsonArray.add(jsonObject);
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}*/
	
	@RequestMapping("cexamcopy")
	public ResponseEntity<JSONArray> cexamcopy(HttpServletRequest request ,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examCd = request.getParameter("examCd");
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		OCR_EXAM rec = service.selectcex(examCd);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exam_cd" , rec.getEXAM_CD());
		jsonObject.put("exam_nm" , rec.getEXAM_NM());
		jsonObject.put("schyr" , rec.getSCHYR());
		jsonObject.put("examkind" , rec.getEXAM_KIND());
		////System.out.println(jsonObject);
		jsonArray.add(jsonObject);

		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("newocrmexam")
	public ResponseEntity<JSONArray> newocrmexam(HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exam_cd" , 0);
		jsonObject.put("exam_nm" , "");
		jsonObject.put("schyr" , "");
		jsonObject.put("examkind" , "");
		////System.out.println(jsonObject);
		jsonArray.add(jsonObject);

		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	@RequestMapping("delocrex")
	public ResponseEntity<JSONArray> delocrex(HttpServletRequest request , HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examcd = request.getParameter("examcd");
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer mbrno = mbr.getMBR_NO();
		try {
			service.delocrex(examcd,mbrno);	
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("al", e.toString());
			jsonArray.add(jsonObject);
		}
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	@GetMapping("update_ocr_exam")
	@ResponseBody
	public String update_ocr_exam(Integer exam_kind,String exam_nm,String schyr,Integer exam_cd,HttpSession session) {
		//System.out.println(exam_kind+","+exam_nm+","+schyr+","+exam_cd);
		try {
			service.update_ocr_exam(exam_cd,exam_nm,schyr,exam_kind);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
//	--------------------------------------------------------------------------------------------------------
	
	@RequestMapping("SN_OCR_LIST")
	public ResponseEntity<JSONArray> sn_list(HttpServletRequest request,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_NM = request.getParameter("BSTOR_NM");
		String BSTOR_CD; 
		if(BSTOR_NM == null || BSTOR_NM.trim().equals("")) {
			BSTOR_CD = request.getParameter("BSTOR_CD");
		}else {
			BSTOR_CD = service.getbstorcd(BSTOR_NM);
		}
		String examcd = request.getParameter("examCd");
//		//System.out.println(examcd);
//		//System.out.println(request.getParameter("rpageNum"));
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent");
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
		if(rpageNum == 0) {
			rpageNum = 1;
		}
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			rsearchcontent = rsearchcontent.trim();
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
		if(rlimit == 0) {
			rlimit=5;
		}
		int eCd=0;
		if(examcd == null || examcd.trim().equals("")) {
			eCd=3;
		}else{
			eCd = Integer.parseInt(examcd);
		}
		int recodecnt = service.OCR_SN_STU_Count(eCd,BSTOR_CD,rsearchcontent);
		List<OCR_EXAM_RC> exam_rec = service.getcStuList(eCd,BSTOR_CD,rsearchcontent,rpageNum,rlimit);

		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : exam_rec) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("name" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			jsonObject.put("BSTOR_CD" , BSTOR_CD);
			jsonArray.add(jsonObject);
//			//System.out.println("json : " + jsonObject);
		}
		int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
		int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int rendpage = rstartpage + 9;
		if (rendpage > rmaxpage) rendpage = rmaxpage;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rpageNum", rpageNum);
		jsonObject.put("rmaxpage",rmaxpage);
		jsonObject.put("rstartpage",rstartpage);
		jsonObject.put("rendpage",rendpage);
		
		////System.out.println("rmaxpage"+rmaxpage+"\nrstartpage"+rstartpage+"\nrpageNum"+rpageNum+"\nrendpage"+rendpage+"\nrecodecnt"+recodecnt+"\nr_cnt"+recodecnt);
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
}
	@RequestMapping("IR_OCR_LIST")
	public ResponseEntity<JSONArray> ir_list(HttpServletRequest request,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_NM = request.getParameter("BSTOR_NM");
		String BSTOR_CD; 
		if(BSTOR_NM == null || BSTOR_NM.trim().equals("")) {
			BSTOR_CD = request.getParameter("BSTOR_CD");
		}else {
			BSTOR_CD = service.getbstorcd(BSTOR_NM);
		}
		String examcd = request.getParameter("examCd");
//		//System.out.println(examcd);
//		//System.out.println(request.getParameter("rpageNum"));
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent");
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
		if(rpageNum == 0) {
			rpageNum = 1;
		}
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			rsearchcontent = rsearchcontent.trim();
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
		if(rlimit == 0) {
			rlimit=5;
		}
		int examCd  = 0;
		if(examcd == null || examcd.trim().equals("")) {
			examCd=5;
		}else{
			examCd = Integer.parseInt(examcd);
		}
		int recodecnt = service.OCR_SN_STU_Count(examCd,BSTOR_CD,rsearchcontent);
		List<OCR_EXAM_RC> exam_rec = service.getcStuList(examCd,BSTOR_CD,rsearchcontent,rpageNum,rlimit);
		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : exam_rec) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("name" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			jsonObject.put("BSTOR_CD" , BSTOR_CD);
			jsonArray.add(jsonObject);
//			//System.out.println("json : " + jsonObject);
		}
		int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
		int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int rendpage = rstartpage + 9;
		if (rendpage > rmaxpage) rendpage = rmaxpage;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rpageNum", rpageNum);
		jsonObject.put("rmaxpage",rmaxpage);
		jsonObject.put("rstartpage",rstartpage);
		jsonObject.put("rendpage",rendpage);
		
		////System.out.println("rmaxpage"+rmaxpage+"\nrstartpage"+rstartpage+"\nrpageNum"+rpageNum+"\nrendpage"+rendpage+"\nrecodecnt"+recodecnt+"\nr_cnt"+recodecnt);
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
}
	
	@RequestMapping("MOCK_OCR_LIST")
	public ResponseEntity<JSONArray> mock_list(HttpServletRequest request,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_NM = request.getParameter("BSTOR_NM");
		String BSTOR_CD; 
		if(BSTOR_NM == null || BSTOR_NM.trim().equals("")) {
			BSTOR_CD = request.getParameter("BSTOR_CD");
		}else {
			BSTOR_CD = service.getbstorcd(BSTOR_NM);
		}
		String examcd = request.getParameter("examCd");
//		//System.out.println(examcd);
//		//System.out.println(request.getParameter("rpageNum"));
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent");
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
		if(rpageNum == 0) {
			rpageNum = 1;
		}
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			rsearchcontent = rsearchcontent.trim();
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
		if(rlimit == 0) {
			rlimit=5;
		}
		int examCd  = 0;
		if(examcd == null || examcd.trim().equals("")) {
			examCd=1;
		}else{
			examCd = Integer.parseInt(examcd);
		}
		int recodecnt = service.OCR_SN_STU_Count(examCd,BSTOR_CD,rsearchcontent);
		List<OCR_EXAM_RC> exam_rec = service.getcStuList(examCd,BSTOR_CD,rsearchcontent,rpageNum,rlimit);
		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : exam_rec) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("name" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			jsonObject.put("BSTOR_CD" , BSTOR_CD);
			jsonArray.add(jsonObject);
//			//System.out.println("json : " + jsonObject);
		}
		//System.out.println(recodecnt+","+rlimit);
		int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
		int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int rendpage = rstartpage + 9;
		if (rendpage > rmaxpage) rendpage = rmaxpage;
		int boardno = recodecnt - (rlimit * (rpageNum - 1));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rpageNum", rpageNum);
		jsonObject.put("rmaxpage",rmaxpage);
		jsonObject.put("rstartpage",rstartpage);
		jsonObject.put("rendpage",rendpage);
		
		////System.out.println("rmaxpage"+rmaxpage+"\nrstartpage"+rstartpage+"\nrpageNum"+rpageNum+"\nrendpage"+rendpage+"\nrecodecnt"+recodecnt+"\nr_cnt"+recodecnt);
		jsonArray.add(jsonObject);
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
}
	@RequestMapping("SN_OCR_EXAM_LIST")
	public ResponseEntity<JSONArray> sn_exam_list(HttpServletRequest request,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_CD = request.getParameter("BSTOR_CD");
		String examcd = request.getParameter("examCd");
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent").trim();
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
		if(rpageNum == 0) {
			rpageNum = 1;
		}
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
		if(rlimit == 0) {
			rlimit=5;
		}
		int examCd  = 0;
		if(examcd == null || examcd.trim().equals("")) {
			examCd=3;
		}else{
			examCd = Integer.parseInt(examcd);
		}
		int recodecnt = service.OCR_SN_STU_Count(examCd,BSTOR_CD,rsearchcontent);
		////System.out.println("bstorcd" + BSTOR_CD +"\nexamCd" + examCd +"\nrpageNum" + rpageNum 
				//+"\nrsearchcontent" + rsearchcontent +"\nrlimit" + rlimit +"\nrecodecnt" + recodecnt);
		List<String> exam_no = service.ocr_rc_exam_no(BSTOR_CD,examCd,rlimit,rsearchcontent,rpageNum);
		////System.out.println("ex :" + exam_no);
		List<OCR_EXAM_RC> exam_rec = new ArrayList<OCR_EXAM_RC>();
		for (int index = 0;index<exam_no.size();index++) {
			String stdn_no = exam_no.get(index);
			List<String> sttus = service.ocr_rc_sn_sttus(stdn_no,BSTOR_CD,examCd);
			int a = 0;
			for(String rec : sttus) if(rec.equals("WARNING")) a++;
			String status = (a>0)?"WARNING":"SUCCESS";
			OCR_EXAM_RC ocr_rc = new OCR_EXAM_RC();
			ocr_rc.setEXMN_NO(exam_no.get(index)+"");
			ocr_rc.setSTTUS(status);
			ocr_rc.setSTDN_NM(service.stdn_nm(exam_no.get(index)));
			exam_rec.add(ocr_rc);
			////System.out.println("ocr : " + ocr_rc);
		}
		////System.out.println();
		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : exam_rec) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("name" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			
			jsonArray.add(jsonObject);
//			//System.out.println("json : " + jsonObject);
		}
		int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
		int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int rendpage = rstartpage + 9;
		if (rendpage > rmaxpage) rendpage = rmaxpage;
		int boardno = recodecnt - (rlimit * (rpageNum - 1));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rpageNum", rpageNum);
		jsonObject.put("rmaxpage",rmaxpage);
		jsonObject.put("rstartpage",rstartpage);
		jsonObject.put("rendpage",rendpage);
		
		////System.out.println("rmaxpage"+rmaxpage+"\nrstartpage"+rstartpage+"\nrpageNum"+rpageNum+"\nrendpage"+rendpage+"\nrecodecnt"+recodecnt+"\nr_cnt"+recodecnt);
		jsonArray.add(jsonObject);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
}
	
	
		/*StringBuilder html = new StringBuilder();
		html.append("<table class=\"table table-bordered table-responsive-md text-center\" style=\"width: 100%\"> <thead> <tr><th><input type=\"checkbox\"></th><th>std_no</th><th>name</th><th>status</th></tr></thead><tbody>");
		if(exam_rec.size()>0 ) {
			for(OCR_EXAM_RC rec : exam_rec) {
				html.append("<tr class=\"ex\" id=\""+rec.getEXMN_NO()+"\" onclick=\"selex('"+rec.getEXMN_NO()+"')\"><td><input type=\"checkbox\"></td><td>"+rec.getEXMN_NO()+"</td><td>"+rec.getSTDN_NM()+"</td>");
				if(rec.getSTTUS().equals("WARNING")) {
					html.append("<td><button class=\"btn btn-danger rounded-pill mb-3\">WARNING</button></td></tr>");			
				}else {
					html.append("<td><button class=\"btn btn-success rounded-pill mb-3\">SUCCESS</button></td></tr>");
				}
			}
				
		}
		html.append("</tbody></table>");
		////System.out.println(html.toString());
		
		////System.out.println(exam_rec);
		 
		
		return html.toString();
		//return html.toString();*/
			
	/*@RequestMapping("MOCK_OCR_LIST")
	public ResponseEntity<JSONArray> mock_list(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_CD = request.getParameter("BSTOR_CD");
		String exam_cd = request.getParameter("exam_cd");
		//System.out.println("examcd :" + exam_cd);
		
		//System.out.println(BSTOR_CD);
		List<Integer> exam_no = service.ocr_rc_exam_no(BSTOR_CD,exam_cd);
		//System.out.println("ex :" + exam_no);
		List<OCR_EXAM_RC> exam_rec = new ArrayList<OCR_EXAM_RC>();
		for (int index = 0;index<exam_no.size();index++) {
			int stdn_no = exam_no.get(index);
			List<String> sttus = service.ocr_rc_sn_sttus(stdn_no);
			int a = 0;
			for(String rec : sttus) if(rec.equals("WARRING")) a++;
			String status = (a>0)?"WARNING":"SUCCESS";
			OCR_EXAM_RC ocr_rc = new OCR_EXAM_RC();
			ocr_rc.setEXMN_NO(exam_no.get(index)+"");
			ocr_rc.setSTTUS(status);
			ocr_rc.setSTDN_NM(service.stdn_nm(exam_no.get(index)));
			exam_rec.add(ocr_rc);
			//System.out.println("ocr : " + ocr_rc);
		}
		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : exam_rec) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("name" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			jsonArray.add(jsonObject);
			//System.out.println("json : " + jsonObject);
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
}*/
	@RequestMapping("filerecogTF")
	public ResponseEntity<JSONArray> filerecogTF(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");

		//파라미터 가져오기
		String exam_cd = request.getParameter("examcd");
		String sep = request.getParameter("sep");

		//세션정보 가져오기
		MBR mbr = (MBR) session.getAttribute("MBR");
        int  CMPN_CD = mbr.getCMPN_CD();
		int  MBR_NO = mbr.getMBR_NO();		   
		int  BSTOR_CD = mbr.getBSTOR_CD();

		int ocr_cd = service.ocrcd(exam_cd);

		JSONArray jsonArray = new JSONArray();
		List<FILELOG> list = new ArrayList<FILELOG>();
		Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}

		list = service.floglist(exam_cd,CMPN_CD,BSTOR_CD,ocr_cd+"",sep,"U");
		String arg_val ="EXAM_CD : "+exam_cd+", CMPN_CD: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+",%";
		String job_nm = "TOS_CALL";
		String step_nm = "01. JAVA";
		String pid1 = service.pid(exam_cd,CMPN_CD,BSTOR_CD,ocr_cd+"",sep);

		Integer pidsys = service.pidsys(arg_val,job_nm,step_nm,pid1);

		System.out.println("P " + pidsys+","+arg_val+","+job_nm+","+step_nm+","+pid1);
		String s = service.ivalue(exam_cd,CMPN_CD,BSTOR_CD,sep)+"";
		Integer mno_bst = service.getmno_bst(BSTOR_CD);

		List<String> imglist = new ArrayList<String>();

		for(FILELOG fl : list) {
			if(fl.getPID().equals(pid1)) {
				imglist.add(fl.getCHG_FILE_NM()+".jpg");
//				System.out.println(fl.getCHG_FILE_NM()+".jpg");
			}
		}

		try {
			if(list.size() == 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("view" , 1);
					jsonArray.add(jsonObject);
			} else {
				if(pidsys == null) {
					pidsys = 0;
				}
				int timediff =pidsys;

				int temptime = imglist.size() * 20 / 60;
				int tempdiff = temptime + 1;

				System.out.println("td"+ timediff +"\n te : " +temptime + "\n ted :" + tempdiff );
				if (timediff < tempdiff) {
					String imgnm = s3dlpath+"/ocr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + imglist.get(0);
					OCR_EXAM_RC recog = service.getrcobjt(imgnm);
//			        //System.out.println("AS:" + recog + "\n qw");
					if (recog == null) {
						int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, ocr_cd+"", sep, pid1);
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("view", 2);
						jsonObject.put("filecnt", filecnt);

						int a = Math.round(((imglist.size() * 20 - timediff * 60) / 60));
						a++;

						if (imglist.size() * 20 % 60 == 0) {
							a--;
						}
						jsonObject.put("minu", a);
						jsonArray.add(jsonObject);
					} else {
						int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, ocr_cd+"", sep, pid1);
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("view", Integer.valueOf(4));
						jsonObject.put("al", "인식완료");
						jsonArray.add(jsonObject);
					}
				} else {
					int reuploadcnt = 0;
					List<String> ilist = new ArrayList<>();
					for (FILELOG fl : list) {
						int tdiff = service.gettdiff(fl);
						if (tdiff > temptime && fl.getPID().equals(pid1)) {
							String imgname = s3dlpath+"/ocr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + fl.getCHG_FILE_NM()+".jpg";
							int recog = service.reuploadocr(imgname);
							System.out.println("RE : " + recog);
							if (recog == 0) {
								ilist.add(fl.getORIGIN_FILE_NM());
							}
						}
					}
					if (ilist.size() == 0) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("view", Integer.valueOf(3));
						jsonObject.put("filecnt", "0개");
						jsonArray.add(jsonObject);
					} else {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("view", Integer.valueOf(3));
						jsonObject.put("filecnt", ilist.size() + "개");
						String il = "";
						for (String li : ilist) {
							il = String.valueOf(il) + li + ",";
						}
						jsonObject.put("filename", il);
						System.out.println("reupd : "+ il);
						jsonArray.add(jsonObject);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping("s3upload.ai")
	public ResponseEntity<JSONArray> s3upload(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray(); 
		Properties prop = new Properties();
		String notupload = "";
		int fcount = 0;

		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		String path = opath+"/properties/temp/OCR/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		List<MultipartFile> mpf = request.getFiles("files");
            
		String exam_cd = request.getParameter("examcd");
//            //System.out.println(exam_cd);
        MBR mbr = (MBR) session.getAttribute("MBR");
        int  CMPN_CD = mbr.getCMPN_CD();
		int  MBR_NO = mbr.getMBR_NO();
		int  BSTOR_CD = mbr.getBSTOR_CD();
		int ocrcd = service.ocrcd(exam_cd);
        Map<String, String> map = new HashedMap();
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        map.clear();
        map.put("PID", pid.substring(0,pid.indexOf("@")));
        map.put("STEP_NM", "01. JAVA");
        map.put("JOB_NM", "S3_UPLOAD");
        map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", MBR_NO : "+MBR_NO);
        map.put("ST_END_DIV", "begin");
        // 임시 파일을 복사한다.
        String sep = "OCR";
        int s = service.ivalue(exam_cd,CMPN_CD,BSTOR_CD,sep);
		int allfile = mpf.size();

		for(int i = 0; i < mpf.size(); i++) {
			String orgfile = mpf.get(i).getOriginalFilename();
			String filename;
			//System.out.println(filename);

			File filepath = new File(path + exam_cd + "/" + mbr.getCMPN_CD() + "/" + mbr.getBSTOR_CD());
			if (!filepath.exists()) filepath.mkdirs();
//			System.out.println("A : " + mpf.get(i).getOriginalFilename());
			File file = new File(filepath + "/" + mpf.get(i).getOriginalFilename());
			String exname = FilenameUtils.getExtension(mpf.get(i).getOriginalFilename());

			pid = ManagementFactory.getRuntimeMXBean().getName();
			String pids = pid.substring(0, pid.indexOf("@"))+s;
			map.clear();
			map.put("PID", pids);
			map.put("STEP_NM", "01. JAVA");
			map.put("JOB_NM", "S3_UPLOAD");
			map.put("ARG_VAL", "EXAM_CD : " + exam_cd + ", CMPN_CD: " + mbr.getCMPN_CD() + ", BSTOR_CD : " + mbr.getBSTOR_CD() + ", MBR_NO : " + mbr.getMBR_NO());
			service.javalog(map);

			try {
				mpf.get(i).transferTo(file);
				Properties s3pathprop = new Properties();
				try {
					FileInputStream fis = new FileInputStream(opath+"/properties/OCRUpload.properties");
					s3pathprop.load(fis);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				////System.out.println(mbr);
				String dl = s3pathprop.getProperty("DEVLIVE");
				String s3dlpath = "aido";

				if (dl.equals("DEV")) {
					s3dlpath += "_dev";
				} else if (dl.equals("TS")) {
					s3dlpath += "_ts";
				}

				String s3path = s3dlpath + "/ocr/" + exam_cd + "/" + mbr.getCMPN_CD() + "/" + mbr.getBSTOR_CD() + "/";
				/*if (exname.equals("pdf")) {
					List<File> pfile = service.conv(file, i, exam_cd, CMPN_CD, BSTOR_CD);
					for (int a = 0; a < pfile.size(); a++) {
						service.insert_filename(exam_cd, CMPN_CD, BSTOR_CD, orgfile, pid.substring(0, pid.indexOf("@"))+s, sep, ocrcd + "", "U");
						filename = service.getchgfilename(exam_cd, CMPN_CD, BSTOR_CD, ocrcd + "", pid.substring(0, pid.indexOf("@")) + s, "OCR") + ".jpg";
						map.put("LOG_MSG", "Upload Start "+filename);
						service.javalog(map);
						map.remove("LOG_MSG");
						try {
							System.out.println("s3f path : "+s3path+filename+" / "+pfile.get(a).getName());
							s3Client.putObject("edu-ai", s3path+filename, pfile.get(a));
							map.put("LOG_MSG", "Upload success 경로 : "+s3path +"파일명 : "+ filename);
							map.put("CMPLT_MSG", "success");
							fcount++;
							System.out.println("FCNTP : " + fcount);
						} catch (Exception e) {
							map.put("LOG_MSG", "Upload fail 경로 : "+s3path +"파일명 : "+ filename);
			            	map.put("CMPLT_MSG", "fail");
			            }
						service.javalog(map);
						map.remove("LOG_MSG");
						map.remove("CMPLT_MSG");
					}
				}else {*/
				if( exname.equals("jpg") || exname.equals("jpeg")) {
					service.insert_filename(exam_cd, CMPN_CD, BSTOR_CD, orgfile, pids, sep, ocrcd + "", "U");
					filename = service.getchgfilename(exam_cd, CMPN_CD, BSTOR_CD, ocrcd + "", pids, "OCR") + ".jpg";
					map.put("LOG_MSG", "Upload Start " + filename);
					service.javalog(map);
					map.remove("LOG_MSG");
					try {
						s3Client.putObject("edu-ai", s3path + filename, file);
						map.put("LOG_MSG", "Upload success 경로 : " + s3path + "파일명 : " + filename);
						map.put("CMPLT_MSG", "success");
//						service.insert_filename(exam_cd,CMPN_CD,BSTOR_CD,orgfile,pid.substring(0,pid.indexOf("@"))+s,sep,ocrcd+"");
						fcount = fcount + 1;
					} catch (AmazonS3Exception e) {
						e.printStackTrace();
						map.put("LOG_MSG", "Upload fail 경로 : " + s3path + "파일명 :" + filename);
						map.put("CMPLT_MSG", "fail");
					}
					service.javalog(map);
				}else {
						//확장자명이 틀려서 오류난 파일명 쌓기
						notupload += orgfile + ",";
				}
			} catch (Exception e1) {
				map.put("LOG_MSG", "file transfer error");
				map.put("CMPLT_MSG", "fail");
				service.javalog(map);
			}
			File[] deletefile = filepath.listFiles();
            if(deletefile != null) {
        		for(File a1 : deletefile) {
        			a1.delete();
        		}
        	}
		}

        map.put("LOG_MSG", fcount +"개 업로드 완료");
		map.put("CMPLT_MSG", "success");
		service.javalog(map);
			
//-----------------------------------
//         원격제어호출   
//-----------------------------------            

    	Properties ubuntuprop = new Properties();
    	try {
    		FileInputStream fis = new FileInputStream(opath+"/properties/OCRUpload.properties");
    		ubuntuprop.load(fis);
    	} catch (Exception e1) {
    		e1.printStackTrace();
    	}
    		
    		////System.out.println(mbr);
    	String keyname = ubuntuprop.getProperty("privateKey");
    	String publicDNS = ubuntuprop.getProperty("host");
    	File authfile = new File(keyname);
    	String user = ubuntuprop.getProperty("user");
    	String host = publicDNS;
    	int port = Integer.parseInt(ubuntuprop.getProperty("port"));
    	String OCR_CD = null;
    	try {
    		JSch jsch=new JSch();
    	    String privateKey = keyname;
    	    jsch.addIdentity(privateKey);

    		    ////System.out.println("identity added ");

    	    Session jschsession = jsch.getSession(user, host, port);
    		   // //System.out.println("session created.");

    	    jschsession.setConfig("StrictHostKeyChecking","no");
    	    jschsession.setConfig("GSSAPIAuthentication","no");
    	    jschsession.setServerAliveInterval(120 * 1000);
    	    jschsession.setServerAliveCountMax(1000);
    	    jschsession.setConfig("TCPKeepAlive","yes");
    		    
    	    jschsession.connect();
    		    
    	    Channel channel = jschsession.openChannel("exec");

    	    ChannelExec channelExec = (ChannelExec) channel;
    		    
    		  //  //System.out.println("==> Connected to" + host);
    		   
    	    String command1 =ubuntuprop.getProperty("command1");
    	    String command2 =ubuntuprop.getProperty("command2");
    	    String command3 =ubuntuprop.getProperty("command3");
    	    String command4 =ubuntuprop.getProperty("command4");
    		    
    	    String DEVLIVE = ubuntuprop.getProperty("DEVLIVE");
    	    String dirt = null;
    	    String dpath = null;
    	    if(DEVLIVE.equals("DEV")) {
    	    	dirt = "/"+DEVLIVE;
    	    	dpath ="_"+DEVLIVE;
    	    }else if(DEVLIVE.equals("TS")) {
    	    	dirt = "/"+DEVLIVE;
    	    	dpath ="_"+DEVLIVE;
    	    }else {
    	    	dirt = "";
       	    	dpath ="";
    	    }
    	    int ocr_cd = service.ocrcd(exam_cd);
    		  //  //System.out.println(ocr_cd);
    	    if(ocr_cd < 10) {
    	    	OCR_CD = "0"+ ocr_cd;
    	    }
    		    
    	    String command = "sh /home/ubuntu/talend"+dirt+"/OCR"+OCR_CD+"_00_MAIN"+dpath+"/OCR"+OCR_CD+"_00_MAIN"+dpath+"/OCR"+OCR_CD+"_00_MAIN"+dpath+"_run.sh --context_param CMPN_CD="+CMPN_CD+" --context_param BSTOR_CD="+BSTOR_CD+" --context_param EXAM_CD="+exam_cd+" --context_param MBR_NO="+MBR_NO+" --context_param OCR_MST_CD="+ocr_cd; ;
    		    
    	    channelExec.setCommand(command);
    	    channelExec.connect();

    		 //   //System.out.println("==> Connected to" + host +"\n"+command );
    	    pid = ManagementFactory.getRuntimeMXBean().getName();
    	    String pids = pid.substring(0,pid.indexOf("@"))+s;
    	    map.put("PID", pids);
           	map.put("STEP_NM", "01. JAVA");
			map.put("JOB_NM", "TOS_CALL");
           	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", MBR_NO : "+MBR_NO);
           	map.put("LOG_MSG", "Tos 호출 " + command);
           	map.put("ST_END_DIV","begin");
           	map.put("CMPLT_MSG", "success");
           	service.javalog(map);
    	}catch(Exception e){
    	   	e.printStackTrace();
    	   	map.put("LOG_MSG", "서버 접속 실패");
    	   	map.put("ST_END_DIV","end");
    	   	map.put("CMPLT_MSG", "fail");
    	   	service.javalog(map);
    	}
//        -----------------------------*/
		int filecnt  = 0;
		String pid1 = service.pid(exam_cd,CMPN_CD,BSTOR_CD,ocrcd+"",sep);
		if(pid1 != null) {
			////System.out.println(pid1);
			filecnt = service.getflist(exam_cd,CMPN_CD,BSTOR_CD,ocrcd+"",sep,pid1).size();
//			System.out.println(filecnt);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("allfile", Integer.valueOf(allfile));
		jsonObject.put("filecnt", Integer.valueOf(filecnt));
		int temptime = ((filecnt * 9)+110) / 60;
		int tempdiff = temptime + 1;
		if (temptime >= 1) {
			String time = tempdiff + "";
//	          System.out.println("TIME : " + time);
			jsonObject.put("time", time);
		} else {
			jsonObject.put("time", "1");
		}
		jsonObject.put("notupd", notupload);
		jsonArray.add(jsonObject);

    	return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	
		
	
	@RequestMapping("s3download")
	public ResponseEntity<JSONArray> s3download(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String EXAMNO = request.getParameter("EXAMNO");
		String examCd = request.getParameter("examCd");
		String BSTOR_CD = request.getParameter("BSTOR_CD");
	//	//System.out.println("BSTOR_CD : "+BSTOR_CD);
		String img = service.ocr_rc_snimg(EXAMNO,examCd,BSTOR_CD);
	//	//System.out.println("i : " +img);
		List<OCR_EXAM_RC> result = service.ocr_rc_sn(EXAMNO,examCd,BSTOR_CD,img);
//		//System.out.println("RES : " + result.toString());
//		int stdn_no = Integer.parseInt(result.get(0).getEXMN_NO());
		////System.out.println(result.get(0).getEXMN_NO());
		int a = 0 ;
		
		
//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		String exam_no = request.getParameter("EXAMNO");
		////System.out.println(exam_no);
		
			
		
		String imgpath = img;
		String key = imgpath.substring(0,imgpath.lastIndexOf("/"))+"/crop/crop_"+imgpath.substring(imgpath.lastIndexOf("/")+1);
		S3Object s3o = s3Client.getObject("edu-ai", key);
	    
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/"+imgpath.substring(imgpath.indexOf("/")+1,imgpath.lastIndexOf("/")));
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		for(File a1 : deletefile) {
			a1.delete();
		}
		}
		String jsppath ="images/"+imgpath.substring(imgpath.indexOf("/")+1);
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgpath.substring(imgpath.lastIndexOf("/")));
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    
//		----------
		
		JSONArray jsonArray = new JSONArray();
		if(result.size() != 0) {
		////System.out.println(result.get(0).getEXTRA_DATA());
			for(OCR_EXAM_RC rec : result) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("std_no" , rec.getEXMN_NO());
				jsonObject.put("lsn_cd" , rec.getLSN_CD());
				jsonObject.put("cmpn_cd" , rec.getCMPN_CD());
				jsonObject.put("bstor_cd" , rec.getBSTOR_CD());
				jsonObject.put("exam_cd" , rec.getEXAM_CD());
				jsonObject.put("ocr_mst_cd" , rec.getOCR_MST_CD());
				jsonObject.put("ocr_img" , rec.getOCR_IMG());
				jsonObject.put("std_nm" , rec.getSTDN_NM());
				jsonObject.put("sttus" , rec.getSTTUS());
				jsonObject.put("extra" , rec.getEXTRA_DATA());
				jsonObject.put("std_sc" , rec.getSTD_SC());
				jsonObject.put("prcn_rank" , rec.getPRCN_RANK());
				jsonObject.put("grade" , rec.getGRAD());
				jsonObject.put("schol" , rec.getSCHOL());
			//jsonObject.put("schosl" , rec.getEXTRA_DATA().get("));
				jsonObject.put("jspimg", jsppath);
				jsonObject.put("lsn_nm", rec.getLSN_NM());
			////System.out.println(jsppath);
				jsonArray.add(jsonObject);
			}
		}else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("jspimg", jsppath);
			jsonArray.add(jsonObject);
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}

	@RequestMapping("s3download2")
	public ResponseEntity<JSONArray> s3download2(HttpServletRequest request,HttpServletResponse response,HttpSession session)throws Exception{ 
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String BSTOR_CD = request.getParameter("BSTOR_CD");
		String EXAMNO = request.getParameter("EXAMNO");
		String examCd = request.getParameter("examCd");
		MBR mbr = (MBR) session.getAttribute("MBR");
		////System.out.println("a");
		String img = service.ocr_rc_snimg(EXAMNO,examCd,BSTOR_CD);
		List<OCR_EXAM_RC> dbresult = service.ocr_rc_sn(EXAMNO,examCd,BSTOR_CD,img);
		String std_nm =request.getParameter("std_nm");
		String schol =request.getParameter("schol");
		//System.out.println(std_nm+","+schol);
		if(!dbresult.get(0).getSTDN_NM().equals(std_nm) || !dbresult.get(0).getSCHOL().equals(request.getParameter("schol"))) {
			service.updateocrinfo(EXAMNO,examCd,BSTOR_CD,std_nm,schol,img);
		}
		List<String[]> dblist = new ArrayList<String[]>();
		int dbcnt = 0;
		for(OCR_EXAM_RC dbrc : dbresult) {
			if(dbrc.getLSN_CD() == 1) {
				String[] dbkor = {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(dbkor);
 			}else if((dbrc.getLSN_CD()+"").equals(request.getParameter("mathlsn"))) {
				String[] dbmath = {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(dbmath);
			}else if(dbrc.getLSN_CD() == 4) {
				String[] dbeng = {dbrc.getLSN_CD()+"","","",dbrc.getGRAD()+""};
				dblist.add(dbeng);
			}else if(dbrc.getLSN_CD() == 34) {
				String[] dbhist = {dbrc.getLSN_CD()+"","","",dbrc.getGRAD()+""};
				dblist.add(dbhist);
			}else if((dbrc.getLSN_CD()+"").equals(request.getParameter("tam1lsn"))) {
				String[] dbtam1 = {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(dbtam1);
			}else if((dbrc.getLSN_CD()+"").equals(request.getParameter("tam2lsn"))) {
				String[] dbtam2 = {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(dbtam2);
			}else if((dbrc.getLSN_CD()+"").equals(request.getParameter("fllsn"))) {
				String[] dbfl = {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(dbfl);
			}else {
				String[] extra= {dbrc.getLSN_CD()+"",dbrc.getSTD_SC()+"",dbrc.getPRCN_RANK()+"",dbrc.getGRAD()+""};
				dblist.add(extra);
			}
		//	//System.out.println(dblist.get(dbcnt)[0]);
			dbcnt ++;
		}
		
		int  MBR_NO = mbr.getMBR_NO();	
		//String[] stu = {request.getParameter("stdno"),request.getParameter("name"),request.getParameter("schol")};
		List<String[]> list = new ArrayList<String[]>();
		String[] kor = {"1",request.getParameter("korstd"),request.getParameter("korrank"),request.getParameter("korgrade")};
		String[] math = {request.getParameter("mathlsn"),request.getParameter("mathstd"),request.getParameter("mathrank"),request.getParameter("mathgrade")};
		String[] eng = {"4","","",request.getParameter("enggrade")};
		String[] tam1 = {request.getParameter("tam1lsn"),request.getParameter("tam1std"),request.getParameter("tam1rank"),request.getParameter("tam1grade")};
		String[] tam2 = {request.getParameter("tam2lsn"),request.getParameter("tam2std"),request.getParameter("tam2rank"),request.getParameter("tam2grade")};
		String[] fl = {request.getParameter("fllsn"),request.getParameter("flstd"),request.getParameter("flrank"),request.getParameter("flgrade")};
		String[] hist = {"34","","",request.getParameter("histgrade")};
		
		list.add(kor);list.add(math);list.add(eng);list.add(tam1);list.add(tam2);list.add(fl);list.add(hist);
		try {
			for(int k=0;k<list.size();k++) {
				for(int l=0;l<dblist.size();l++) {	
					if(dblist.get(l)[0].equals(list.get(k)[0])) {
						int eqcnt = 0;
						for(int j = 1; j<list.get(k).length;j++) {
							if(dblist.get(l)[j].equals(list.get(k)[j])) {
								eqcnt ++;
							}	
						}
						if(eqcnt != 3) {
							service.updateocr(list.get(k),EXAMNO,examCd,BSTOR_CD,MBR_NO);
							//System.out.println("lsn : " + list.get(k)[0]+"\nstd_sc : " + list.get(k)[1]+"\nprcn : " + list.get(k)[2]+"\ngrd : " + list.get(k)[3]);
						}
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
//		int stdn_no = Integer.parseInt(result.get(0).getEXMN_NO());
		////System.out.println(result.get(0).getEXMN_NO());
		int a = 0 ;
		
		List<OCR_EXAM_RC> result = service.ocr_rc_sn(EXAMNO,examCd,BSTOR_CD,img);
//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		String exam_no = request.getParameter("EXAMNO");
		////System.out.println(exam_no);
		String imgpath = result.get(0).getOCR_IMG();
		String key = imgpath.substring(0,imgpath.lastIndexOf("/"))+"/crop/crop_"+imgpath.substring(imgpath.lastIndexOf("/")+1);
		S3Object s3o = s3Client.getObject("edu-ai", key);
	    
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/"+imgpath.substring(imgpath.indexOf("/")+1,imgpath.lastIndexOf("/")));
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		for(File a1 : deletefile) {
			a1.delete();
		}
		}
		String jsppath ="images/"+imgpath.substring(imgpath.indexOf("/")+1);
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgpath.substring(imgpath.lastIndexOf("/")));
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    
//		----------
		
		JSONArray jsonArray = new JSONArray();		
		for(OCR_EXAM_RC rec : result) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("std_no" , rec.getEXMN_NO());
			jsonObject.put("lsn_cd" , rec.getLSN_CD());
			jsonObject.put("cmpn_cd" , rec.getCMPN_CD());
			jsonObject.put("bstor_cd" , rec.getBSTOR_CD());
			jsonObject.put("exam_cd" , rec.getEXAM_CD());
			jsonObject.put("ocr_mst_cd" , rec.getOCR_MST_CD());
			jsonObject.put("ocr_img" , rec.getOCR_IMG());
			jsonObject.put("std_nm" , rec.getSTDN_NM());
			jsonObject.put("sttus" , rec.getSTTUS());
			jsonObject.put("extra" , rec.getEXTRA_DATA());
			jsonObject.put("std_sc" , rec.getSTD_SC());
			jsonObject.put("prcn_rank" , rec.getPRCN_RANK());
			jsonObject.put("grade" , rec.getGRAD());
			jsonObject.put("schol" , rec.getSCHOL());
			jsonObject.put("jspimg", jsppath);
			jsonObject.put("lsn_nm", rec.getLSN_NM());
			////System.out.println(jsppath);
			jsonArray.add(jsonObject);
		}
		////System.out.println(jsonArray);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
		
		 }
	
	@RequestMapping({"alarm"})
	  public ResponseEntity<JSONArray> alarmlist(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    JSONArray jsonArray = new JSONArray();
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int CMPN_CD = mbr.getCMPN_CD();
	    int BSTOR_CD = mbr.getBSTOR_CD();
	    List<ALRAM> list = service.omralarmlist(CMPN_CD, BSTOR_CD);
	    if(list.size() > 5) {
	    	for (int a = 0 ; a < 5 ; a++) {
	    		ALRAM rc = list.get(a);
	    		JSONObject jsonObject = new JSONObject();
	    		jsonObject.put("exam_nm", rc.getEXAM_NM()  +" "+ rc.getBSTOR_NM() +"점 "+ rc.getSUB_NM());
	    		if (rc.getDATEDIF() < 60) {
	    			jsonObject.put("al_date", String.valueOf(rc.getDATEDIF()) + "분 전" );
	    		} else if (rc.getDATEDIF() < 1440) {
	    			jsonObject.put("al_date", String.valueOf(rc.getDATEDIF() / 60) + "시간 전");
	    		} else {
	    			jsonObject.put("al_date", String.valueOf(rc.getDATEDIF() / 1440) + "일 전");
	    		}
	    		jsonArray.add(jsonObject);
	    	}
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	
	@RequestMapping({"OMR_REG"})
	  public ModelAndView OMR_REG(Integer limit, Integer pageNum, String searchcontent, HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    if (pageNum == null || pageNum.toString().equals(""))
	      pageNum = Integer.valueOf(1); 
	    if (searchcontent == null || searchcontent.trim().equals("")) {
	    	searchcontent = null;
	    }else {
	    	for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
	    }
	       
	    if (limit == null)
	      limit = Integer.valueOf(5); 
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int bstor_cd = mbr.getBSTOR_CD();
	    int cmpncd = mbr.getCMPN_CD();
	    int listcount = service.OMR_Count(searchcontent);
	    List<OMR_MST> OMR_List = service.OMRList(pageNum, limit, searchcontent);
	    int maxpage = (int)(listcount / limit.intValue() + 0.95D);
	    int startpage = ((int)(pageNum.intValue() / 10.0D + 0.9D) - 1) * 10 + 1;
	    int endpage = startpage + 9;
	    if (endpage > maxpage)
	      endpage = maxpage; 
	    int boardno = listcount - limit.intValue() * (pageNum.intValue() - 1);
	    mav.addObject("pageNum", pageNum);
	    mav.addObject("maxpage", Integer.valueOf(maxpage));
	    mav.addObject("startpage", Integer.valueOf(startpage));
	    mav.addObject("endpage", Integer.valueOf(endpage));
	    mav.addObject("listcount", Integer.valueOf(listcount));
	    mav.addObject("boardno", Integer.valueOf(boardno));
	    mav.addObject("OMRLIST", OMR_List);
	    return mav;
	  }
	
	@RequestMapping("OMR_RECOG")
	public ModelAndView omrRecoglist(Integer  limit,Integer pageNum,HttpSession session, String searchcontent) {
		ModelAndView mav = new ModelAndView();
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
	    	for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}

		}
		if(limit == null) {
			limit=10;
		}
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		String bstor_nm = service.getbstor(bstor_cd+"");
		int cmpncd = mbr.getCMPN_CD();
		int listcount = service.OMR_Examlsn_Count(searchcontent,bstor_cd,cmpncd);
		List<OMR_EXAM> OMR_EXAM_sub_List = service.OMRExamsubList(pageNum, limit, searchcontent,bstor_cd,cmpncd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("bstor_cd", bstor_cd);
	    mav.addObject("bstor_nm", bstor_nm);
		mav.addObject("osname", System.getProperty("os.name")+" , "+osname);
		mav.addObject("EXAMList", OMR_EXAM_sub_List);
//		//System.out.println(OMR_EXAM_sub_List);
		return mav;
	}
	@RequestMapping("MOCK_ADM")
	public ModelAndView mock_adm(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
	    	for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}

		}
		if(limit == null) {
			limit=5;
		}
		int listcount = service.OMR_EXAM_Count(searchcontent);
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		List<OMR_EXAM> OMR_EXAM_List = service.OMRExamList(pageNum, limit, searchcontent);
		for(OMR_EXAM dt : OMR_EXAM_List) {
			dt.setEXAM_DT(dt.getEXAM_DT().substring(0,4)+"-"+dt.getEXAM_DT().substring(4,6)+"-"+dt.getEXAM_DT().substring(6));
		}
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("EXAMList", OMR_EXAM_List);
		return mav;
	}
	
	 @RequestMapping("chkex")
	  public ResponseEntity<JSONArray> chkex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    String examcd = request.getParameter("examcd");
	    String useyn = request.getParameter("useyn");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    Integer cmpncd = Integer.valueOf(mbr.getCMPN_CD());
	    Integer mbrno = mbr.getMBR_NO();
	    JSONArray jsonArray = new JSONArray();
	    JSONObject jsonObject = new JSONObject();
	    try {
	      service.examuseyn(examcd, useyn, cmpncd,mbrno);
	    } catch (Exception e) {
	      jsonObject.put("e", e.toString());
	    } 
	    jsonArray.add(jsonObject);
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	
	@RequestMapping("newmexam")
	public ResponseEntity<JSONArray> newmexam(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exam_cd" , 0);
		jsonObject.put("examdt" , 2021-01-01 );
		jsonObject.put("year" , "");
		jsonObject.put("kind" , "");
		jsonObject.put("eci_exam_cd" , "");	
		jsonObject.put("exname" , "");
		////System.out.println(jsonObject);
		jsonArray.add(jsonObject);

		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}

	@RequestMapping("mexamcopy")
	public ResponseEntity<JSONArray> mexamcopy(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examCd = request.getParameter("examCd");
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		OMR_EXAM rec = service.selectmex(examCd);
		rec.setEXAM_DT(rec.getEXAM_DT().substring(0,4)+"-"+rec.getEXAM_DT().substring(4,6)+"-"+rec.getEXAM_DT().substring(6));
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exam_cd" , rec.getEXAM_CD());
		jsonObject.put("examdt" , rec.getEXAM_DT());
		jsonObject.put("year" , rec.getSCHYR());
		jsonObject.put("eci_exam_cd" , rec.getECI_EXAM_CD());
		jsonObject.put("kind" , rec.getEXAM_KIND());
		jsonObject.put("exname" , rec.getEXAM_NM());
		////System.out.println(jsonObject);
		jsonArray.add(jsonObject);

		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	@RequestMapping("copyexrow")
	public ResponseEntity<JSONArray> copyexrow(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examdate = request.getParameter("examdt");
		String arr [] = examdate.split("-");
		String examdt = "";
		for(String a : arr) {
			examdt += a;
		}
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		String schyr = request.getParameter("schyr");
		String exam_kind = request.getParameter("exam_kind");
		String exam_nm = request.getParameter("exam_nm");
		String examcd = request.getParameter("examcd");
		int eci_exam_cd = Integer.parseInt(request.getParameter("eci_exam_cd"));
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		List<Integer> blist = service.getBlist();
		//System.out.println(blist.toString());
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			service.insertmexrow(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno,eci_exam_cd);
			OMR_EXAM exam = service.getomrexam(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno);
			
			List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(examcd,cmpncd);

			for(OMR_EXAM_LSN lscdd : lsnlist) {
				System.out.print(lscdd.getLSN_CD()+",");
			}
			
			
			if(lsnlist.size() > 1) {
				for(OMR_EXAM_LSN lsn : lsnlist) {
					for(Integer bst : blist) {
						service.insertmlsn(lsn,cmpncd,bst,mbrno,exam.getEXAM_CD());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("result" ,e.toString());
			jsonArray.add(jsonObject);
		}
//		rec.setEXAM_DT(rec.getEXAM_DT().substring(0,4)+"-"+rec.getEXAM_DT().substring(4,6)+"-"+rec.getEXAM_DT().substring(6));
		
		jsonObject.put("result" ,"시험추가 완료");
		jsonArray.add(jsonObject);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	@RequestMapping("createexrow")
	public ResponseEntity<JSONArray> createexrow(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examdate = request.getParameter("examdt");
		String arr [] = examdate.split("-");
		String examdt = "";
		for(String a : arr) {
			examdt += a;
		}
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		String schyr = request.getParameter("schyr");
		String exam_kind = request.getParameter("exam_kind");
		String exam_nm = request.getParameter("exam_nm");
		int eci_exam_cd = Integer.parseInt(request.getParameter("eci_exam_cd"));
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		try {
			service.insertmexrow(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno,eci_exam_cd);
			OMR_EXAM exam = service.getomrexam(examdt,schyr,exam_kind,exam_nm,cmpncd,bstorcd,mbrno);
			Integer examcd = exam.getEXAM_CD();
			List<Integer> lsncdlist =service.getlsncdlist();
			for(int lsn : lsncdlist) {
				int omrcd = 0;
				int quei = 0; 
				if(lsn== 1){
					omrcd = 1;
					quei = 45; 
				}else if(lsn== 2 || lsn == 3){
					omrcd = 2;
					quei = 30; 
				}else if(lsn== 4){
					omrcd = 3;
					quei = 45; 
				}else if(lsn== 34 || (lsn>= 5 && lsn<= 27 && lsn!= 6 && lsn!= 7 && lsn!= 8 && lsn!= 9 )){
					omrcd = 4;
					quei = 20; 
		    	}else {
		    		omrcd = 5;
		    		quei = 30; 
				}
				//service.insertmlsn2(lsn, cmpncd, bstorcd, mbrno, examcd,omrcd,quei);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		rec.setEXAM_DT(rec.getEXAM_DT().substring(0,4)+"-"+rec.getEXAM_DT().substring(4,6)+"-"+rec.getEXAM_DT().substring(6));
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result" ,"시험추가 완료");
		jsonArray.add(jsonObject);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	@RequestMapping("mlsnlist")
	public ResponseEntity<JSONArray> mlsnlist(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examcd = request.getParameter("examCd");
		MBR mbr = (MBR)session.getAttribute("MBR");
		int exam = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		int cmpncd = mbr.getCMPN_CD();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		try {
			List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(examcd,cmpncd);
			for(OMR_EXAM_LSN lsn : lsnlist) {
				JSONObject jsonObject = new JSONObject();
				if(lsn.getLSN_CD()== 1 || lsn.getLSN_CD()== 39 || lsn.getLSN_CD()== 40 ){
					jsonObject.put("period" ,"1");
				}else if(lsn.getLSN_CD()== 36 || lsn.getLSN_CD()== 37 || lsn.getLSN_CD()== 38 || lsn.getLSN_CD()== 41 ){
					jsonObject.put("period" ,"2"); 
				}else if(lsn.getLSN_CD()== 4){
					jsonObject.put("period" ,"3");
				}else if(lsn.getLSN_CD()== 34 || lsn.getLSN_CD()== 5  || (lsn.getLSN_CD()>= 10 && lsn.getLSN_CD()<= 33)){
					jsonObject.put("period" ,"4");
		    	}else {
		    		jsonObject.put("period" ,"5");
				}
				jsonObject.put("omrnm" ,lsn.getOMR_NM());
				jsonObject.put("subject" ,lsn.getLSN_NM());
				jsonObject.put("quei" ,lsn.getQUEI_NUM());
				jsonObject.put("tot" ,lsn.getTOT_DISMK());
				jsonObject.put("examcd" ,lsn.getEXAM_CD());
				jsonObject.put("lsncd" ,lsn.getLSN_CD());
//				//System.out.println(jsonObject);
				jsonArray.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping({"mlsncopy"})
	  public ResponseEntity<JSONArray> mlsncopy(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String examcd = request.getParameter("examcd");
	    String lsncd = request.getParameter("lsncd");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int bstorcd = mbr.getBSTOR_CD();
	    int mbrno = mbr.getMBR_NO();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    JSONArray jsonArray = new JSONArray();
	    try {
	      OMR_EXAM_LSN lsnobj = service.getlsnobj(examcd, lsncd, cmpncd);
	      JSONObject jsonObject = new JSONObject();
	      if(lsnobj.getLSN_CD()== 1 || lsnobj.getLSN_CD()== 39 || lsnobj.getLSN_CD()== 40 ){
				jsonObject.put("period" ,"1");
			}else if(lsnobj.getLSN_CD()== 36 || lsnobj.getLSN_CD()== 37 || lsnobj.getLSN_CD()== 38 || lsnobj.getLSN_CD()== 41 ){
				jsonObject.put("period" ,"2"); 
			}else if(lsnobj.getLSN_CD()== 4){
				jsonObject.put("period" ,"3");
			}else if(lsnobj.getLSN_CD()== 34 || lsnobj.getLSN_CD()== 5  || (lsnobj.getLSN_CD()>= 10 && lsnobj.getLSN_CD()<= 33)){
				jsonObject.put("period" ,"4");
	    	}else {
	    		jsonObject.put("period" ,"5");
			}
	      
	      
	      jsonObject.put("omrnm", lsnobj.getOMR_NM());
	      jsonObject.put("quei", Integer.valueOf(lsnobj.getQUEI_NUM()));
	      jsonObject.put("tot", Integer.valueOf(lsnobj.getTOT_DISMK()));
	      jsonObject.put("examcd", Integer.valueOf(lsnobj.getEXAM_CD()));
	      jsonObject.put("lsncd", Integer.valueOf(lsnobj.getLSN_CD()));
	      jsonArray.add(jsonObject);
	      List<OMR_EXAM_LSN> lsnlist = service.getnewlsnlist(examcd, cmpncd);
	      jsonObject = new JSONObject();
	      if (lsnlist.size() >= 1)
	        for (OMR_EXAM_LSN lsn : lsnlist) {
	          jsonObject = new JSONObject();
	          jsonObject.put("lsncd", Integer.valueOf(lsn.getLSN_CD()));
	          jsonObject.put("lsnnm", lsn.getLSN_NM());
	          jsonArray.add(jsonObject);
	        }  
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	@RequestMapping("OMR_SEL")
	public ModelAndView omrsel(String examcd,String lsncd,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<OMR_MST> mlist = new ArrayList<OMR_MST>();
		try {
			 mlist = service.mmstlist();
			 //System.out.println("tr"+mlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(mlist);
		mav.addObject("examcd" , examcd);
		mav.addObject("lsncd" , lsncd);
		mav.addObject("mlist" , mlist);
		return mav;
	}
	
	@RequestMapping("newlsnrow")
	public ResponseEntity<JSONArray> newlsnrow(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examcd = request.getParameter("examcd");
		MBR mbr = (MBR)session.getAttribute("MBR");
		int exam = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		int cmpncd = mbr.getCMPN_CD();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		List<OMR_EXAM_LSN> lsnlist = service.getnewlsnlist(examcd,cmpncd);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("examcd", examcd);
		jsonArray.add(jsonObject);
		if(lsnlist.size() >= 1) {
			for(OMR_EXAM_LSN lsn : lsnlist) {
				jsonObject = new JSONObject();
				jsonObject.put("lsncd", lsn.getLSN_CD());
				jsonObject.put("lsnnm", lsn.getLSN_NM());
				jsonArray.add(jsonObject);
			}
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	@RequestMapping("createlsnrow")
	public ResponseEntity<JSONArray> createlsnrow(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String examcd = request.getParameter("examcd");
		String lsn_cd = request.getParameter("lsn_cd");
		String quei = request.getParameter("quei");
		String omr_kind = request.getParameter("omr_kind");
		Integer omrcd = service.getomrcd(omr_kind);
		String tot = request.getParameter("tot");
		
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		List<Integer> bstrlist = service.getBlist();
//		//System.out.println(bstrlist.toString());
		
		try {
			List<OMR_EXAM_LSN> OEL_OBJ = service.getOELM_OBJ(examcd, lsn_cd, cmpncd);
			//System.out.println("oel : "+OEL_OBJ);
			if(OEL_OBJ.size() != 0) {
				//System.out.println("A");
				for(Integer bstr : bstrlist) {
					try {
						service.updatemlsnrow(examcd,lsn_cd,quei,omr_kind,tot,cmpncd,bstr,mbrno,omrcd);	
					} catch (NullPointerException e3) {
						e3.printStackTrace();	
					}
				}
			}else {
				for(Integer bstr : bstrlist) {
					service.insertmlsnrow(examcd,lsn_cd,quei,omr_kind,tot,cmpncd,bstr,mbrno,omrcd);	
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		try {
			List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(examcd,cmpncd);
			for(OMR_EXAM_LSN lsn : lsnlist) {
				JSONObject jsonObject = new JSONObject();
				if(lsn.getLSN_CD()== 1 || lsn.getLSN_CD()== 39 || lsn.getLSN_CD()== 40 ){
					jsonObject.put("period" ,"1");
				}else if(lsn.getLSN_CD()== 36 || lsn.getLSN_CD()== 37 || lsn.getLSN_CD()== 38 || lsn.getLSN_CD()== 41 ){
					jsonObject.put("period" ,"2"); 
				}else if(lsn.getLSN_CD()== 4){
					jsonObject.put("period" ,"3");
				}else if(lsn.getLSN_CD()== 34 || lsn.getLSN_CD()== 5  || (lsn.getLSN_CD()>= 10 && lsn.getLSN_CD()<= 33)){
					jsonObject.put("period" ,"4");
		    	}else {
		    		jsonObject.put("period" ,"5");
				}
				
				
				jsonObject.put("omrnm" ,lsn.getOMR_NM());
				jsonObject.put("subject" ,lsn.getLSN_NM());
				jsonObject.put("quei" ,lsn.getQUEI_NUM());
				jsonObject.put("tot" ,lsn.getTOT_DISMK());
				jsonObject.put("examcd" ,lsn.getEXAM_CD());
				jsonObject.put("lsncd" ,lsn.getLSN_CD());
				jsonArray.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	 @RequestMapping({"dellsn"})
	  public ResponseEntity<JSONArray> dellsn(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String examcd = request.getParameter("examcd");
	    String lsncd = request.getParameter("lsncd");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int bstorcd = mbr.getBSTOR_CD();
	    int mbrno = mbr.getMBR_NO();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    JSONArray jsonArray = new JSONArray();
	    try {
	      service.dellsn(examcd, lsncd, cmpncd,mbrno);
	      List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(examcd, cmpncd);
	      for (OMR_EXAM_LSN lsn : lsnlist) {
	        JSONObject jsonObject = new JSONObject();
	        if(lsn.getLSN_CD()== 1 || lsn.getLSN_CD()== 39 || lsn.getLSN_CD()== 40 ){
				jsonObject.put("period" ,"1");
			}else if(lsn.getLSN_CD()== 36 || lsn.getLSN_CD()== 37 || lsn.getLSN_CD()== 38 || lsn.getLSN_CD()== 41 ){
				jsonObject.put("period" ,"2"); 
			}else if(lsn.getLSN_CD()== 4){
				jsonObject.put("period" ,"3");
			}else if(lsn.getLSN_CD()== 34 || lsn.getLSN_CD()== 5  || (lsn.getLSN_CD()>= 10 && lsn.getLSN_CD()<= 33)){
				jsonObject.put("period" ,"4");
	    	}else {
	    		jsonObject.put("period" ,"5");
			}
	        jsonObject.put("omrnm", lsn.getOMR_NM());
	        jsonObject.put("subject", lsn.getLSN_NM());
	        jsonObject.put("quei", Integer.valueOf(lsn.getQUEI_NUM()));
	        jsonObject.put("tot", Integer.valueOf(lsn.getTOT_DISMK()));
	        jsonObject.put("examcd", Integer.valueOf(lsn.getEXAM_CD()));
	        jsonObject.put("lsncd", Integer.valueOf(lsn.getLSN_CD()));
	        jsonArray.add(jsonObject);
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	  @RequestMapping({"delex"})
	  public ResponseEntity<JSONArray> delex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String examcd = request.getParameter("examcd");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int bstorcd = mbr.getBSTOR_CD();
	    int mbrno = mbr.getMBR_NO();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    JSONArray jsonArray = new JSONArray();
	    try {
	      service.delex(examcd, cmpncd,mbrno);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	  @RequestMapping({"delquei"})
	  public ResponseEntity<JSONArray> delquei(MultipartHttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String exam_cd = request.getParameter("examcd");
	    String lsn_cd = request.getParameter("lsncd");
	    String queino = request.getParameter("queino");
	    Integer omr_mst_cd = service.getomrcdbylsn(lsn_cd, exam_cd);
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int bstorcd = mbr.getBSTOR_CD();
	    int mbrno = mbr.getMBR_NO();
	    try {
	      service.delquei(exam_cd, lsn_cd, cmpncd, queino);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    List<OMR_EXAM_LSN_QUEI> qlist = service.getqlist(Integer.valueOf(cmpncd), exam_cd, lsn_cd);
	    JSONArray jsonArray = new JSONArray();
	    if (qlist.size() > 0) {
	      JSONObject jsonObject1 = new JSONObject();
	      jsonObject1.put("examcd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getEXAM_CD()));
	      jsonObject1.put("omr_mst_cd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getOMR_MST_CD()));
	      jsonObject1.put("lsncd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getLSN_CD()));
	      jsonArray.add(jsonObject1);
	      for (OMR_EXAM_LSN_QUEI qu : qlist) {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("queino", Integer.valueOf(qu.getQUEI_NO()));
	        jsonObject.put("quearea", qu.getQUE_AREA());
	        jsonObject.put("quedtlarea", qu.getQUE_DTL_AREA());
	        jsonObject.put("quetype", qu.getQUE_TYPE());
	        jsonObject.put("crano", Integer.valueOf(qu.getCRA_NO()));
	        jsonObject.put("dismk", Integer.valueOf(qu.getDISMK()));
	        jsonArray.add(jsonObject);
	      } 
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
		
	@RequestMapping("qexcelupload")
	public ResponseEntity<JSONArray> qexcelupload(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String exam_cd = request.getParameter("examcd");
		String lsn_cd = request.getParameter("lsncd");
		//System.out.println("lsncd : "+lsn_cd);
		Integer omr_mst_cd = service.getomrcdbylsn(lsn_cd,exam_cd);
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		List<MultipartFile> mpfS = request.getFiles("files");
		MultipartFile mpf = mpfS.get(0);
		String path = opath+"/properties/temp/OMR/QUEI/";
		String orgfile =mpf.getOriginalFilename();				
		File filePath = new File(path +exam_cd+"/"+cmpncd+"/"+omr_mst_cd+"/"+lsn_cd);
		String fp = path +exam_cd+"/"+cmpncd+"/"+omr_mst_cd+"/"+lsn_cd+"/"+orgfile;
		if(!filePath.exists()) filePath.mkdirs();
		String exname = FilenameUtils.getExtension(mpf.getOriginalFilename());
		    File file = new File(filePath+"/"+ mpf.getOriginalFilename());
			
		    
		    try {
				mpf.transferTo(file);
			} catch (IllegalStateException | IOException e1) {
				e1.printStackTrace();
			}			
						
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fp);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}    
			
			Workbook wb = null;    
		        /*
		         * 파일의 확장자를 체크해서 .XLS 라면 HSSFWorkbook에
		         * .XLSX라면 XSSFWorkbook에 각각 초기화 한다.
		         */
			if(fp.toUpperCase().endsWith(".XLS")) {
				try {
					wb = new HSSFWorkbook(fis);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}else if(fp.toUpperCase().endsWith(".XLSX")) {
				try {
					wb = new XSSFWorkbook(fis);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			Sheet sheet = wb.getSheetAt(0);
	        
	        /*
	         * 시트에서 유효한(데이터가 있는) 행의 갯수를 가져온다.
	         */
	        int numOfRows = sheet.getPhysicalNumberOfRows();
	        //System.out.println("numOfRows :"+numOfRows); 
	        
	        int numOfCells = 0;
	        
	        Row row = null;
	        Cell cell = null;
	        
	        String cellName = "";
	        
	        // key : 컬럼 / value: 데이터
	        /*
	         * 각 Row마다의 값을 저장할 맴 객체
	         * 저장되는 형식은 다음과 같다
	         * put("A","이름");
	         * put("B","게임명");
	         */
	        Map<String, String> map = null;
	        
	        /*
	         * 각 Row를 리스트에 담는다
	         * 하나의 Row는 하나의 Map으로 표현되며
	         * List에는 모든 Row가 포함될 것이다.
	         */
	        List<Map<String, String>> result = new ArrayList<Map<String, String>>(); 
	        
	        /*
	         * Row만큼 반복을 한다.
	         */
	        ReadOption readOption = new ReadOption(); 
	        readOption.setStartRow(2);
	        for(int rowIndex = readOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
	            
	            /*
	             * 워크북에서 가져온 시트에서 rowIndex에 해당하는 Row를 가져온다.
	             * 하나의 Row는 여러개의 Cell을 가진다.
	             */
	            row = sheet.getRow(rowIndex);
	            
	            if(row != null) {
	                
	                // 유효한 셀의 갯수
	                /*
	                 * 가져온 Row의 Cell의 갯수를 구한다.
	                 */
	                numOfCells = row.getPhysicalNumberOfCells();
	                
	                
	                /*
	                 * 데이터를 담을 맵 객체 초기화
	                 */
	                map = new HashMap<String, String>();
	                
	                /*
	                 * Cell의 수 만큼 반복
	                 */
	                for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
	                    
	                    /*
	                     * Row에서 CellIndex에 해당하는 Cell을 가져온다.
	                     */
	                    cell = row.getCell(cellIndex);
	                    ////System.out.println(cell);
	                    /*
	                     * 현재 Cell의 이름을 가져온다.
	                     * 이름의 예 : A,B,C,D,E..... 
	                     */
	                    cellName = CellRef.getName(cell, cellIndex);
	                    ////System.out.println(cellName);
	                    /*
	                     * 추출 대상 컬럼인지 확인한다.
	                     * 추출 대생 컬럼이 아니라면
	                     * for로 다시 올라간다. 
	                     */
	                    
	                   // if(!readOption.getOutputColumns().contains(cellName) ) {
	                    //    continue;
	                    //}
	                    
	                    /*
	                     * 맵 객체의 Cell의 이름을 키(Key)로 데이터를 담는다.
	                     */
	                    map.put(cellName, CellRef.getValue(cell));
	                }
	                
	                /*
	                 * 만들어진 Map 객체를 List에 넣는다.
	                 */
	                result.add(map);
	                }
	            }
	       try {
	    	   service.deletelsnquei(cmpncd,exam_cd,lsn_cd,omr_mst_cd);
	       } catch (Exception e3) {
	    	  e3.printStackTrace();
	       }
	        for(Map<String, String> maps : result) {
	        	Float qno = Float.parseFloat(maps.get("A"));
	        	Integer QUEI_NO = Math.round(qno);
	        	Float crano = Float.parseFloat(maps.get("B"));
	        	Integer CRA_NO = Math.round(crano);
	        	Float dismk = Float.parseFloat(maps.get("C"));
	        	Integer DISMK = Math.round(dismk);
	        	String QUE_AREA = maps.get("D");
	        	String QUE_DTL_AREA = maps.get("E");
	        	String QUE_TYPE = maps.get("F");
	        	Float l_cd = Float.parseFloat(maps.get("G"));
	        	Integer ecicd = Math.round(l_cd);
	        	System.out.print(ecicd + " , ");
	        	Integer lcd = service.getlsncd(ecicd); 
	        	System.out.println(lcd);
	        	Integer ocd = service.getomrcdbylsnonly(lcd.toString());
//	        	//System.out.println(QUEI_NO+","+cmpncd+","+exam_cd+","+lsn_cd+","+omr_mst_cd+","+CRA_NO+","+DISMK+","+QUE_AREA+","+QUE_DTL_AREA+","+QUE_TYPE+","+mbrno+","+lcd);
	        	try {
	        		//OMR_EXAM_LSN_QUEI queiobjt = service.selectquei(cmpncd,exam_cd,lsn_cd,QUEI_NO);
	        		//if(queiobjt == null) {
	        		service.insertqueimst(QUEI_NO,cmpncd,exam_cd,lcd.toString(),ocd,CRA_NO,DISMK,QUE_AREA,QUE_DTL_AREA,QUE_TYPE,mbrno);
	        		//}
				} catch (Exception exc) {
					//exc.printStackTrace();
				}
	        }
	        List<OMR_EXAM_LSN_QUEI> qlist = service.getqlist(cmpncd,exam_cd,lsn_cd);
	        JSONArray jsonArray = new JSONArray();
	        if(qlist.size() > 0) {
	        	JSONObject jsonObject1 = new JSONObject();
		        jsonObject1.put("examcd", qlist.get(0).getEXAM_CD());
		        jsonObject1.put("omr_mst_cd", qlist.get(0).getOMR_MST_CD());
		        jsonObject1.put("lsncd", qlist.get(0).getLSN_CD());
		        jsonArray.add(jsonObject1);
		        for(OMR_EXAM_LSN_QUEI qu : qlist) {
		        	JSONObject jsonObject = new JSONObject();
		        	jsonObject.put("queino", qu.getQUEI_NO());
		        	jsonObject.put("quearea", qu.getQUE_AREA());
		        	jsonObject.put("quedtlarea", qu.getQUE_DTL_AREA());
		        	jsonObject.put("quetype", qu.getQUE_TYPE());
		        	jsonObject.put("crano", qu.getCRA_NO());
		        	jsonObject.put("dismk", qu.getDISMK());
		        	jsonArray.add(jsonObject);
		        }	
	        }
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}	
	
	
	@RequestMapping("qlist")
	public ResponseEntity<JSONArray> qlist(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		String exam_cd = request.getParameter("examcd");
		String lsn_cd = request.getParameter("lsncd");
		System.out.println(lsn_cd +","+exam_cd);
		Integer omr_mst_cd = service.getomrcdbylsn(lsn_cd,exam_cd);
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int bstorcd = mbr.getBSTOR_CD();
		int mbrno = mbr.getMBR_NO();
		
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		 List<OMR_EXAM_LSN_QUEI> qlist = service.getqlist(cmpncd,exam_cd,lsn_cd);
	        JSONArray jsonArray = new JSONArray();
	        if(qlist.size() > 0) {
	        	JSONObject jsonObject1 = new JSONObject();
		        jsonObject1.put("examcd", qlist.get(0).getEXAM_CD());
		        jsonObject1.put("omr_mst_cd", qlist.get(0).getOMR_MST_CD());
		        jsonObject1.put("lsncd", qlist.get(0).getLSN_CD());
		        jsonArray.add(jsonObject1);
		        for(OMR_EXAM_LSN_QUEI qu : qlist) {
		        	JSONObject jsonObject = new JSONObject();
		        	jsonObject.put("queino", qu.getQUEI_NO());
		        	jsonObject.put("quearea", qu.getQUE_AREA());
		        	jsonObject.put("quedtlarea", qu.getQUE_DTL_AREA());
		        	jsonObject.put("quetype", qu.getQUE_TYPE());
		        	jsonObject.put("crano", qu.getCRA_NO());
		        	jsonObject.put("dismk", qu.getDISMK());
		        	jsonArray.add(jsonObject);
//		        	//System.out.println(jsonObject);
		        }	
	        }
	return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
}	
//
	  @RequestMapping({"update_omr_exam"})
	  public ResponseEntity<JSONArray> update_omr_exam(MultipartHttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String exam_cd = request.getParameter("examcd");
	    String exam_dt = request.getParameter("exam_dt");
	    String[] examdt = exam_dt.split("-");
	    String edt = "";
	    byte b;
	    int i;
	    String[] arrayOfString1;
	    for (i = (arrayOfString1 = examdt).length, b = 0; b < i; ) {
	      String dt = arrayOfString1[b];
	      edt = String.valueOf(edt) + dt;
	      b++;
	    } 
	    String schyr = request.getParameter("schyr");
	    String exam_kind = request.getParameter("exam_kind");
	    String exam_nm = request.getParameter("exam_nm");
	    int eci_exam_cd = Integer.parseInt(request.getParameter("eci_exam_cd"));
	    
//	    System.out.println(exam_cd+","+ exam_nm+","+ schyr+","+ exam_kind+","+ edt+","+eci_exam_cd);
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int mbrno = mbr.getMBR_NO();
	    int bstorcd = mbr.getBSTOR_CD();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    try {
	      service.update_omr_exam(exam_cd, exam_nm, schyr, exam_kind, edt, cmpncd, bstorcd, mbrno,eci_exam_cd);
//	      OMR_EXAM omrexamobj =service.OMRExamobj(exam_cd);
//	      System.out.println("asd : "+ omrexamobj.toString());
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    JSONArray jsonArray = new JSONArray();
	    List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(exam_cd, cmpncd);
	    for (OMR_EXAM_LSN lsn : lsnlist) {
	      JSONObject jsonObject = new JSONObject();
	      if(lsn.getLSN_CD()== 1 || lsn.getLSN_CD()== 39 || lsn.getLSN_CD()== 40 ){
				jsonObject.put("period" ,"1");
			}else if(lsn.getLSN_CD()== 36 || lsn.getLSN_CD()== 37 || lsn.getLSN_CD()== 38 || lsn.getLSN_CD()== 41 ){
				jsonObject.put("period" ,"2"); 
			}else if(lsn.getLSN_CD()== 4){
				jsonObject.put("period" ,"3");
			}else if(lsn.getLSN_CD()== 34 || lsn.getLSN_CD()== 5  || (lsn.getLSN_CD()>= 10 && lsn.getLSN_CD()<= 33)){
				jsonObject.put("period" ,"4");
	    	}else {
	    		jsonObject.put("period" ,"5");
			}
	      jsonObject.put("omrnm", lsn.getOMR_NM());
	      jsonObject.put("subject", lsn.getLSN_NM());
	      jsonObject.put("quei", Integer.valueOf(lsn.getQUEI_NUM()));
	      jsonObject.put("tot", Integer.valueOf(lsn.getTOT_DISMK()));
	      jsonObject.put("examcd", Integer.valueOf(lsn.getEXAM_CD()));
	      jsonObject.put("lsncd", Integer.valueOf(lsn.getLSN_CD()));
	      jsonArray.add(jsonObject);
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	  @RequestMapping({"update_omr_lsn"})
	  public ResponseEntity<JSONArray> update_omr_lsn(MultipartHttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String exam_cd = request.getParameter("examcd");
	    String lsncd = request.getParameter("lsncd");
	    String queino = request.getParameter("queino");
	    String omr_kind = request.getParameter("omr_kind");
	    String dismk = request.getParameter("dismk");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int mbrno = mbr.getMBR_NO();
	    int bstorcd = mbr.getBSTOR_CD();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    try {
	      Integer omrcd = service.getomrcd(omr_kind);
	      service.update_omr_lsn(exam_cd, lsncd, queino, omrcd+"", dismk, cmpncd, mbrno);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    JSONArray jsonArray = new JSONArray();
	    List<OMR_EXAM_LSN> lsnlist = service.getlsnlist(exam_cd, cmpncd);
	    for (OMR_EXAM_LSN lsn : lsnlist) {
	      JSONObject jsonObject = new JSONObject();
	      if(lsn.getLSN_CD()== 1 || lsn.getLSN_CD()== 39 || lsn.getLSN_CD()== 40 ){
				jsonObject.put("period" ,"1");
			}else if(lsn.getLSN_CD()== 36 || lsn.getLSN_CD()== 37 || lsn.getLSN_CD()== 38 || lsn.getLSN_CD()== 41 ){
				jsonObject.put("period" ,"2"); 
			}else if(lsn.getLSN_CD()== 4){
				jsonObject.put("period" ,"3");
			}else if(lsn.getLSN_CD()== 34 || lsn.getLSN_CD()== 5  || (lsn.getLSN_CD()>= 10 && lsn.getLSN_CD()<= 33)){
				jsonObject.put("period" ,"4");
	    	}else {
	    		jsonObject.put("period" ,"5");
			}
	      jsonObject.put("omrnm", lsn.getOMR_NM());
	      jsonObject.put("subject", lsn.getLSN_NM());
	      jsonObject.put("quei", Integer.valueOf(lsn.getQUEI_NUM()));
	      jsonObject.put("tot", Integer.valueOf(lsn.getTOT_DISMK()));
	      jsonObject.put("examcd", Integer.valueOf(lsn.getEXAM_CD()));
	      jsonObject.put("lsncd", Integer.valueOf(lsn.getLSN_CD()));
	      jsonArray.add(jsonObject);
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	  @RequestMapping({"update_omr_quei"})
	  public ResponseEntity<JSONArray> update_omr_quei(MultipartHttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    String exam_cd = request.getParameter("examcd");
	    String lsncd = request.getParameter("lsncd");
	    String queino = request.getParameter("queino");
	    String cra = request.getParameter("cra");
	    String dismk = request.getParameter("dismk");
	    String qarea = request.getParameter("qarea");
	    String qdtlarea = request.getParameter("qdtlarea");
	    String qtype = request.getParameter("qtype");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    int cmpncd = mbr.getCMPN_CD();
	    int mbrno = mbr.getMBR_NO();
	    int bstorcd = mbr.getBSTOR_CD();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    try {
	      service.update_omr_quei(exam_cd, lsncd, queino, cra, dismk, qarea, qdtlarea, qtype, cmpncd, mbrno);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    List<OMR_EXAM_LSN_QUEI> qlist = service.getqlist(Integer.valueOf(cmpncd), exam_cd, lsncd);
	    JSONArray jsonArray = new JSONArray();
	    if (qlist.size() > 0) {
	      JSONObject jsonObject1 = new JSONObject();
	      jsonObject1.put("examcd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getEXAM_CD()));
	      jsonObject1.put("omr_mst_cd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getOMR_MST_CD()));
	      jsonObject1.put("lsncd", Integer.valueOf(((OMR_EXAM_LSN_QUEI)qlist.get(0)).getLSN_CD()));
	      jsonArray.add(jsonObject1);
	      for (OMR_EXAM_LSN_QUEI qu : qlist) {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("queino", Integer.valueOf(qu.getQUEI_NO()));
	        jsonObject.put("quearea", qu.getQUE_AREA());
	        jsonObject.put("quedtlarea", qu.getQUE_DTL_AREA());
	        jsonObject.put("quetype", qu.getQUE_TYPE());
	        jsonObject.put("crano", Integer.valueOf(qu.getCRA_NO()));
	        jsonObject.put("dismk", Integer.valueOf(qu.getDISMK()));
	        jsonArray.add(jsonObject);
	      } 
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	  
	
//
//-------------------------------------
	
	@RequestMapping("OMR_RECOG2")
	public ModelAndView omrRecoglist2(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
	    	for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}

		}
		if(limit == null) {
			limit=5;
		}
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		MBR mbr = (MBR) session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int listcount = service.getbstcnt(searchcontent,cmpncd);
		int listcount1000 = service.getbst1000cnt(searchcontent,cmpncd);

		List<BSTOR> blist = service.getbstlist(pageNum, limit, searchcontent,cmpncd);
		List<BSTOR> blist1000 = service.getbst1000list(pageNum, limit, searchcontent,cmpncd);
		
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		
		if(mbr.getBSTOR_CD() != 3){
			mav.addObject("listcount",listcount1000);
			maxpage = (int) ((double) listcount / limit + 0.95);
			startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
			endpage = startpage + 9;
			if (endpage > maxpage) endpage = maxpage;
			boardno = listcount - (limit * (pageNum - 1));
			
		}else {
			mav.addObject("listcount",listcount);	
		}
		String bstor_nm = service.getbstor(mbr.getBSTOR_CD()+"");
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("boardno", boardno);
		mav.addObject("bstor_cd", mbr.getBSTOR_CD());
		mav.addObject("bstor_nm", bstor_nm );
		
		mav.addObject("mbr", mbr);
		
		if(mbr.getBSTOR_CD() != 3) {
			mav.addObject("bstorlist", blist1000);
		}else {
			mav.addObject("bstorlist", blist);
		}
		////System.out.println(blist.toString());


/*
		List<FILELOG> FLOG = service.RECALL_LIST();
		System.out.println("flog 크기 : " + FLOG.size());
		if(FLOG.size() !=0){
			Properties ubuntuprop = new Properties();
			try {
				FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
				ubuntuprop.load(fis);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			////System.out.println(mbr);
			String keyname = ubuntuprop.getProperty("privateKey");
			File authfile = new File(keyname);
			String publicDNS = ubuntuprop.getProperty("NLB");
			String user = ubuntuprop.getProperty("user");
			String host = publicDNS;
			int port = Integer.parseInt(ubuntuprop.getProperty("port"));
			System.out.println("privateKey : " + keyname + "\n user :" + user);
			Map<String, String> map = new HashedMap();
			for(FILELOG flog : FLOG){
				Integer e_cd = flog.getEXAM_CD();
				Integer b_cd = flog.getBSTOR_CD();
				Integer c_cd = flog.getCMPN_CD();
				Integer m_cd = flog.getMST_CD();
				try {
					//원격제어 객체 생성
					JSch jsch=new JSch();
					String privateKey = keyname;
					jsch.addIdentity(privateKey);

					////System.out.println("identity added ");
					System.out.println("host : "+ host);
					Session jschsession = jsch.getSession(user, host, port);
					System.out.println("session created.");

					//원격제어 조건 설정
					jschsession.setConfig("StrictHostKeyChecking","no");
					jschsession.setConfig("GSSAPIAuthentication","no");
					jschsession.setServerAliveInterval(120 * 1000);
					jschsession.setServerAliveCountMax(1000);
					jschsession.setConfig("TCPKeepAlive","yes");

					//연결
					jschsession.connect();


					//명령 객체 생성
					Channel channel = jschsession.openChannel("exec");

					ChannelExec channelExec = (ChannelExec) channel;

					System.out.println("==> Connected to" + host);

					String command1 =ubuntuprop.getProperty("command");
//    		    String command2 =ubuntuprop.getProperty("command2");
//    		    String command3 =ubuntuprop.getProperty("command3");
//    		    String command4 =ubuntuprop.getProperty("command4");
					String OMR_CD = null;
					if(m_cd < 10) {
						OMR_CD = "0"+ m_cd;
					}
					String command = command1+ " --context_param EXAM_CD="+e_cd+" --context_param CMPN_CD="+c_cd+" --context_param BSTOR_CD="+b_cd+" --context_param OMR_MST_CD="+m_cd+" --context_param MBR_NO=4";
					System.out.println("COM : " + command);
					channelExec.setCommand(command);
					channelExec.connect();

					System.out.println("==> Connected to" + host +"\n"+command );

					map.put("PID", "000001");
					map.put("STEP_NM", "01. JAVA");
					map.put("JOB_NM", "TOS_CALL");
					map.put("ARG_VAL", "EXAM_CD : "+e_cd+", CMPN_CD: "+c_cd+", BSTOR_CD : "+b_cd+", MBR_NO : 4, OMR_MST_CD : "+m_cd+" , ML_SERVER : NLB");
					map.put("LOG_MSG", "Tos 호출 " + command);
					map.put("ST_END_DIV","begin");
					map.put("CMPLT_MSG", "success");
					service.javalog(map);
					service.sttuschg(c_cd,b_cd,e_cd+"",OMR_CD,4,2);
					service.UPDATE_REUPLD_YN(e_cd+"", c_cd, b_cd, "0", "\'Z\'");
				} catch(JSchException e){
					e.printStackTrace();
					map.put("LOG_MSG", "서버 접속 실패");
					map.put("ST_END_DIV","end");
					map.put("CMPLT_MSG", "fail");
					service.javalog(map);
				}
			}
		}
*/
		return mav;
	}
	@RequestMapping("getuploadexam")
	public ResponseEntity<JSONArray> getuploadexam(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray(); 
		Integer epageNum = Integer.parseInt(request.getParameter("epageNum"));
		Integer elimit = Integer.parseInt(request.getParameter("elimit"));
		Integer bstorcd = Integer.parseInt(request.getParameter("bstorcd"));
		String esearchcontent = request.getParameter("esearchcontent");
    	if(esearchcontent == null || esearchcontent.trim().equals("")) {
    		esearchcontent = null;
    	}else {
    		for(int idx : UNIC) {
    			char a = (char)idx;
    			esearchcontent = esearchcontent.replace(a+"", "");	
    		}
    	}
		MBR mbr = (MBR) session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		int listcount = service.OMR_Examlsn_Count(esearchcontent,bstorcd,cmpncd);
		List<OMR_EXAM> OMR_EXAM_sub_List = service.OMRExamsubList(epageNum, elimit, esearchcontent,bstorcd,cmpncd);
		int maxpage = (int) ((double) listcount / elimit + 0.95);
		int startpage = ((int) (epageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (elimit * (epageNum - 1));
		
		for(OMR_EXAM exam :OMR_EXAM_sub_List) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("exam_cd", exam.getEXAM_CD());
			jsonObject.put("omr_cd", exam.getOMR_MST_CD());
			jsonObject.put("exam_nm", exam.getEXAM_NM());
			jsonObject.put("schyr", exam.getSCHYR());
			jsonObject.put("omr_nm", exam.getOMR_NM());
			jsonObject.put("cd_nm", exam.getCD_NM());
			jsonArray.add(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("epageNum", epageNum);
		jsonObject.put("emaxpage",maxpage);
		jsonObject.put("estartpage",startpage);
		jsonObject.put("eendpage",endpage);
		jsonObject.put("listcount",listcount);
		jsonObject.put("boardno", boardno);
		jsonObject.put("bstorcd", bstorcd);
		jsonArray.add(jsonObject);
		
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
//	getuploadexam
	/*
	 * if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}
		if(limit == null) {
			limit=10;
		}
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		String bstor_nm = service.getbstor(bstor_cd+"");
		int cmpncd = mbr.getCMPN_CD();
		int listcount = service.OMR_Examlsn_Count(searchcontent,bstor_cd,cmpncd);
		List<OMR_EXAM> OMR_EXAM_sub_List = service.OMRExamsubList(pageNum, limit, searchcontent,bstor_cd,cmpncd);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("bstor_cd", bstor_cd);
	    mav.addObject("bstor_nm", bstor_nm);
		mav.addObject("EXAMList", OMR_EXAM_sub_List);
		//System.out.println(OMR_EXAM_sub_List);
	 * 
	 * */
	
	
	/*
	@RequestMapping("tests3upload")
	public ResponseEntity<JSONArray> tests3upload(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray(); 
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/tests3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		String path = opath+"/properties/temp/OMR/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
            List<MultipartFile> mpf = request.getFiles("files");
            
            String exam_cd = request.getParameter("examcd");
            String omr_mst_cd = request.getParameter("omrcd");
           
            MBR mbr = (MBR) session.getAttribute("MBR");
            int  CMPN_CD = mbr.getCMPN_CD();
		    int  MBR_NO = mbr.getMBR_NO();		   
		    int  BSTOR_CD = mbr.getBSTOR_CD();
            Map<String, String> map = new HashedMap();
            String pid = ManagementFactory.getRuntimeMXBean().getName();
            map.clear();
        	map.put("PID", pid.substring(0,pid.indexOf("@")));
        	map.put("STEP_NM", "01. JAVA");
        	map.put("JOB_NM", "S3_UPLOAD");
        	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", MBR_NO : "+MBR_NO+", omr_mst_cd: "+omr_mst_cd);
        	map.put("ST_END_DIV", "begin");
            // 임시 파일을 복사한다.
        	String sep = "OMR";
        	int s = service.ivalue(exam_cd,CMPN_CD,BSTOR_CD,sep);
        	int filecount = 0;
        	int allfile = mpf.size();
            for(int i = s; i < mpf.size()+s; i++) {
            	String orgfile =mpf.get(i).getOriginalFilename();
            	String filename = null;
            	char cha =(char) (i/1000+97);
            	filename = cha+"";
            	filename +=exam_cd+""+CMPN_CD+""+BSTOR_CD+""+omr_mst_cd+"_";
//            	//System.out.println(filename);
            	if(i/10 == 0) {
            		filename += "000"+i;
            	}else if(i/10 < 10) {
            		filename += "00"+i;
            	}else if(i/100 < 10) {
            		filename += "0"+i;
            	}else {
            		filename += i;
            	}
            	File filepath = new File(path +exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd);
            	if(!filepath.exists()) filepath.mkdirs();
            	File file = new File(filepath+"/"+ mpf.get(i).getOriginalFilename());
            	String exname = FilenameUtils.getExtension(mpf.get(i).getOriginalFilename());
            	
            	pid = ManagementFactory.getRuntimeMXBean().getName();
            	map.clear();
            	map.put("PID", pid.substring(0,pid.indexOf("@")));
            	map.put("STEP_NM", "01. JAVA");
            	map.put("JOB_NM", "S3_UPLOAD");
            	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", MBR_NO : "+MBR_NO+", OMR_MST_CD : "+omr_mst_cd);
            	service.javalog(map);
            	
            	try {
					mpf.get(i).transferTo(file);
					String s3path = "EC";
					map.put("LOG_MSG", "Upload Start "+filename+"."+exname);
					service.javalog(map);
					map.remove("LOG_MSG");
					try {
						s3Client.putObject("edu-data-load", s3path+"/"+filename+"."+exname, file);
						map.put("LOG_MSG", "Upload success 경로 : "+s3path +"파일명 : "+filename+"."+exname);
						map.put("CMPLT_MSG", "success");
						service.insert_filename(exam_cd,CMPN_CD,BSTOR_CD,orgfile,filename+"."+exname,pid.substring(0,pid.indexOf("@"))+s,sep,omr_mst_cd);
						filecount++;
					} catch (Exception e) {
						e.printStackTrace();
						map.put("LOG_MSG", "Upload fail 경로 : "+s3path +"파일명 :"+filename+"."+exname);
	            		map.put("CMPLT_MSG", "fail");
					} 
					service.javalog(map);
				} catch (Exception e) {
					map.put("LOG_MSG", "file transfer error");
					map.put("CMPLT_MSG", "fail");
					service.javalog(map);
				}  	
          }
            JSONObject jsonObject = new JSONObject();
			jsonObject.put("result" ,"업로드 완료");
			jsonArray.add(jsonObject);
			return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}*/
//-------------------------------------
	
	
	
	
	
	@RequestMapping("omrs3upload")
	public ResponseEntity<JSONArray> omrs3upload(MultipartHttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray(); 
		//프로퍼티 객체생성
		Properties prop = new Properties();
		try {
			//프로퍼티 읽기
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String notupload = "";
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		String SNO = prop.getProperty("SNO");
		System.out.print("서버위치 : " + SNO);

		//AWS 인증객체 생성
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		String path = opath+"/properties/temp/OMR/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();

		//파일 리스트 객체에 넣기
            List<MultipartFile> mpf = request.getFiles("files");
            
            String exam_cd = request.getParameter("examcd");
            String omr_mst_cd = request.getParameter("omrcd");
            Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
            ////System.out.println("exam_cd : " + exam_cd + ", omr_mst_cd  : " + omr_mst_cd);
            MBR mbr = (MBR) session.getAttribute("MBR");
            int  CMPN_CD = mbr.getCMPN_CD();
		    int  MBR_NO = mbr.getMBR_NO();		   
		    String sep = "OMR";
		    

			//파일로그에서 조건에 맞는 갯수 구하기
        	Integer s = service.ivalue(exam_cd,CMPN_CD,BSTOR_CD,sep);
            Map<String, String> map = new HashedMap();
            
    		//프로세스 아이디 구하기
            String pid = ManagementFactory.getRuntimeMXBean().getName();
            map.clear();
//            System.out.println("pid : "+pid.substring(0,pid.indexOf("@"))+s);

    		//프로세스 아이디에 파일갯수를 붙여 같은 프로세스 아이디 내에서 구별한다.
        	map.put("PID", pid.substring(0,pid.indexOf("@"))+s);
        	map.put("STEP_NM", "01. JAVA");
        	map.put("JOB_NM", "S3_UPLOAD");
        	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", omr_mst_cd: "+omr_mst_cd+", MBR_NO : "+MBR_NO);
        	map.put("ST_END_DIV", "begin");
           
        	String ip = request.getRemoteAddr();
        	
        	int filecount = 0;
        	int allfile = mpf.size();
            for(int i = 0; i < mpf.size(); i++) {

        		//파일명 가져오기
            	String orgfile =mpf.get(i).getOriginalFilename();

        		//S3에 업로드할 파일 경로 가져오기
            	File filepath = new File(path +exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd);

        		//경로가 존재하지 않으면 디렉토리 만들기
            	if(!filepath.exists()) filepath.mkdirs();

        		//비어있는 파일객체로 지정된 경로에 생성하기
            	File file = new File(filepath+"/"+ mpf.get(i).getOriginalFilename());

        		//파일 확장자명 가져오기
            	String exname = FilenameUtils.getExtension(mpf.get(i).getOriginalFilename());
            	////System.out.println(exname);
            	pid = ManagementFactory.getRuntimeMXBean().getName();
            	map.clear();
            	map.put("PID", pid.substring(0,pid.indexOf("@"))+s);
            	map.put("STEP_NM", "01. JAVA");
            	map.put("JOB_NM", "S3_UPLOAD");
            	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD : "+omr_mst_cd+", MBR_NO : "+MBR_NO);
				map.put("ST_END_DIV", "begin");
            	//service.javalog(map);
            	Properties s3pathprop = new Properties();
	    		try {

	    			//S3 업로드에 필요한 프로퍼티 가져오기
	    			FileInputStream fis = new FileInputStream(opath+"/properties/OCRUpload.properties");
	    			s3pathprop.load(fis);
	    		} catch (Exception e1) {
	    			e1.printStackTrace();
	    		}
	    		// 라이브 개발 테스트 서버 구분
	    		String dl = s3pathprop.getProperty("DEVLIVE");
				String s3dlpath = "aido";
				if(dl.equals("DEV")) {
					s3dlpath+="_dev";
				}else if(dl.equals("TS")) {
					s3dlpath+="_ts";
				}
				////System.out.println("DL : " + s3dlpath);
				String filename = "";
            	try {

            		//비어있는 객체에 멀티파트 파일로 채워넣기
					mpf.get(i).transferTo(file);
					String s3path = s3dlpath+"/omr/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd+"/";
//					System.out.println("fliename : " +  filename);
//					System.out.println("exname : " +exname);
					

					//확장자명 구분 하기
					if( exname.equals("jpg") || exname.equals("jpeg")) {
						try {

							//파일로그에 쌓기
							service.insert_filename(exam_cd,CMPN_CD,BSTOR_CD,orgfile,pid.substring(0,pid.indexOf("@"))+s,sep,omr_mst_cd,"U");
							
							//시퀀스로 되어있는 파일이름에 확장자명을 넣음
							filename = service.getchgfilename(exam_cd,CMPN_CD,BSTOR_CD,omr_mst_cd,pid.substring(0,pid.indexOf("@"))+s,"OMR")+".jpg";
							map.put("LOG_MSG", "Upload Start "+filename);
							service.javalog(map);
							map.remove("LOG_MSG");

							//s3에 경로 및 파일 설정하고 파일 업로드
							s3Client.putObject("edu-ai", s3dlpath+"/omr/"+exam_cd+"/"+CMPN_CD+"/"+BSTOR_CD+"/"+omr_mst_cd+"/"+filename, file);
							map.put("LOG_MSG", "Upload success 경로 : "+s3path +"파일명 : "+filename);
							map.put("CMPLT_MSG", "success");
							
							filecount++;
							////System.out.println("filecount : "+filecount);
						} catch (Exception e) {
							e.printStackTrace();

							//업로드 중 오류시 시스템로그에 실패 로그 쌓기 
							map.put("LOG_MSG", "Upload fail 경로 : "+s3path +"파일명 :"+filename);
							map.put("CMPLT_MSG", "fail");
						}	 
						service.javalog(map);
					}else {

						//확장자명이 틀려서 오류난 파일명 쌓기
						notupload += orgfile + ",";
					}
				} catch (Exception e) {
					map.put("LOG_MSG", "file transfer error");
					map.put("CMPLT_MSG", "fail");
					service.javalog(map);
				}
            	File[] deletefile = filepath.listFiles();
            	if(deletefile != null) {
        			for(File a1 : deletefile) {
        				a1.delete();
        			}
        		}
          }
           if(filecount != 0) {
           	System.out.println("fcnt : " + filecount);
        	   service.sttuschg(CMPN_CD,BSTOR_CD,exam_cd,omr_mst_cd,MBR_NO,4);
           }
            map.remove("ARG_VAL");
            map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD : "+omr_mst_cd +", MBR_NO : "+MBR_NO+ ",WEB_SERVER : "+SNO);
            map.put("LOG_MSG", filecount +"개 업로드 완료");
			map.put("CMPLT_MSG", "success");
			service.javalog(map);
			
			
			
			
		
			
//-----------------------------------
//         원격제어호출   
//-----------------------------------            
/*	@RequestMapping("recogstart")
	public ResponseEntity<JSONArray> recogstart(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		Map<String, String> map = new HashedMap();
		String omr_mst_cd = request.getParameter("omrcd");
		Integer bstorcd = Integer.parseInt(request.getParameter("bstorcd"));
		String omrcd = request.getParameter("omrcd");
		String examcd = request.getParameter("examcd");
		 MBR mbr = (MBR) session.getAttribute("MBR");
        Integer CMPN_CD = mbr.getCMPN_CD();
        Integer MBR_NO = mbr.getMBR_NO();
        JSONArray jsonArray = new JSONArray();
        String sep = "OMR";*/


		//조건에 맞는 가장 최근 프로세스 아이디 값 가져오기
        String pid1 = service.pid(exam_cd,CMPN_CD,BSTOR_CD,omr_mst_cd,sep);

        int one = 0; int two = 0 ;int three = 0;int four = 0;int five = 0;
        int serverno = 0;
      //프로퍼티 객체 생성
        Properties ubuntuprop = new Properties();
    		try {
    			FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
    			ubuntuprop.load(fis);
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
    		////System.out.println(mbr);
    		String keyname = ubuntuprop.getProperty("privateKey");
    		File authfile = new File(keyname);
    		String publicDNS = "";
    		String user = ubuntuprop.getProperty("user");
    		String host = publicDNS;
    		int port = Integer.parseInt(ubuntuprop.getProperty("port"));
    		System.out.println("privateKey : " + keyname + "\n user :" + user);

//        --- 프로세스수가 적은 서버 찾기
        for(int Sev_no = 1;Sev_no<6;Sev_no++) {
        	publicDNS = ubuntuprop.getProperty("host"+Sev_no);
        	host = publicDNS;
    	    
    		try {

    			//원격제어 객체 생성
    			JSch jsch=new JSch();
    		    String privateKey = keyname;
    		    jsch.addIdentity(privateKey);
    		    
    		    ////System.out.println("identity added ");

    		    Session jschsession = jsch.getSession(user, host, port);
    		    System.out.println("session created.");


    			//원격제어 조건 설정
    		    jschsession.setConfig("StrictHostKeyChecking","no");
    		    jschsession.setConfig("GSSAPIAuthentication","no");
    		    jschsession.setServerAliveInterval(120 * 1000);
    		    jschsession.setServerAliveCountMax(1000);
    		    jschsession.setConfig("TCPKeepAlive","yes");		            
    		    

    			//연결
    		    jschsession.connect();
    		    

    			//명령 객체 생성
    		    Channel channel = jschsession.openChannel("exec");

    		    ChannelExec channelExec = (ChannelExec) channel;
				String DL = ubuntuprop.getProperty("DEVLIVE");
				String command = "";
				if(DL.equals("LIVE")){
					command = "ps -ef | grep OMR01_00_MAIN_run.sh | wc -l";
				}else if (DL.equals("DEV")){
					command = "ps -ef | grep OMR01_00_MAIN_DEV_run.sh | wc -l";
				}

//    		    String command = "pwd";
    		    System.out.println("COM : " + command);
    		    channelExec.setCommand(command);
    		    channelExec.connect();

    		    StringBuilder outputBuffer = new StringBuilder();
    	        InputStream in = channel.getInputStream();
    	        ((ChannelExec) channel).setErrStream(System.err);        
    	        
    	        channel.connect();  //실행
    	        
    	  
    	        byte[] tmp = new byte[1024];
    	        int h = 0;
    	        while (true) {
    	        	while (in.available() > 0) {
    	        		int i = in.read(tmp, 0, 1024);
    	                outputBuffer.append(new String(tmp, 0, i));
    	                if (i < 0) break;
    	            }
    	            if (channel.isClosed()) {
    	                if(Sev_no == 1) {
//    	                	String str1 = outputBuffer.toString().trim();
//    	                	System.out.println("str1 : " + str1);
//    	                	int inte1 = Integer.parseInt(str1);
//    	                	System.out.println("inte1 :" + inte1);
    	                	one=Integer.parseInt(outputBuffer.toString().trim())-2;
    	                }else if(Sev_no == 2) {
    	                	two=Integer.parseInt(outputBuffer.toString().trim())-2;
    	                }else if(Sev_no == 3) {
							three=Integer.parseInt(outputBuffer.toString().trim())-2;
						}else if(Sev_no == 4) {
							four=Integer.parseInt(outputBuffer.toString().trim())-2;
						}else {
    	                	five=Integer.parseInt(outputBuffer.toString().trim())-2;
    	                }
    	                channel.disconnect();
    	                break;
    	            }
    	        }
    		}catch(Exception e){
    		    e.printStackTrace();
    		}
    	}


        if(one<=two && one<=three && one<=four && one<=five) {
        	publicDNS = ubuntuprop.getProperty("host1");
			serverno = 1;
        }else if(two<=three && two<=one && two<=four && two<=five) {
        	publicDNS = ubuntuprop.getProperty("host2");
			serverno = 2;
        }else if(three<=one && three<=two && three<=four && three<=five) {
			publicDNS = ubuntuprop.getProperty("host3");
			serverno = 3;
		}else if(four<=three && four<=one && four<=two && four<=five) {
			publicDNS = ubuntuprop.getProperty("host4");
			serverno = 4;
		}else {
        	publicDNS = ubuntuprop.getProperty("host5");
			serverno = 5;
        }
        host = publicDNS;
		System.out.println("serverno : " + serverno);
		System.out.println("1 :" + one+"\n 2 :" +two +"\n 3 :" +three +"\n 4 :" +four +"\n 5 :" +five);

//	----------------------------------------------------------

		/*
    		if((mbr.getBSTOR_CD() == 5 || mbr.getBSTOR_CD() == 3) && mbr.getMBR_NO() != 3) {
    			String name = mbr.getNAME();
    			Integer ecitest = Integer.parseInt(name.substring(name.length()-1));
    			
    			//계정번호확인 
    			switch (ecitest%3) {
					case 1: {
						publicDNS = ubuntuprop.getProperty("host1");
						host = publicDNS;
						serverno = 1;
						break;
	    			}
					case 2: {
						publicDNS = ubuntuprop.getProperty("host2");
						host = publicDNS;
						serverno = 2;
						break;
	    			}
					case 0: {
						publicDNS = ubuntuprop.getProperty("host3");
						host = publicDNS;
						serverno = 3;
						break;
	    			}
				}
    		}else {
    			publicDNS = ubuntuprop.getProperty("host");
        		host = publicDNS;
    		}
    		*/
    		
    		try {

    			//원격제어 객체 생성
    			JSch jsch=new JSch();
    		    String privateKey = keyname;
    		    jsch.addIdentity(privateKey);
    		    
    		    ////System.out.println("identity added ");
				System.out.println("host : "+ host);
    		    Session jschsession = jsch.getSession(user, host, port);
    		    System.out.println("session created.");


    			//원격제어 조건 설정
    		    jschsession.setConfig("StrictHostKeyChecking","no");
    		    jschsession.setConfig("GSSAPIAuthentication","no");
    		    jschsession.setServerAliveInterval(120 * 1000);
    		    jschsession.setServerAliveCountMax(1000);
    		    jschsession.setConfig("TCPKeepAlive","yes");		            
    		    

    			//연결
    		    jschsession.connect();
    		    

    			//명령 객체 생성
    		    Channel channel = jschsession.openChannel("exec");

    		    ChannelExec channelExec = (ChannelExec) channel;
    		    
    		    System.out.println("==> Connected to" + host);
    		   
    		    String command1 =ubuntuprop.getProperty("command");
//    		    String command2 =ubuntuprop.getProperty("command2");
//    		    String command3 =ubuntuprop.getProperty("command3");
//    		    String command4 =ubuntuprop.getProperty("command4");
    		    String OMR_CD = null;
    		    if(Integer.parseInt(omr_mst_cd) < 10) {
    		    	OMR_CD = "0"+ omr_mst_cd;
    		    }
    		    String command = command1+ " --context_param EXAM_CD="+exam_cd+" --context_param CMPN_CD="+CMPN_CD+" --context_param BSTOR_CD="+BSTOR_CD+" --context_param OMR_MST_CD="+omr_mst_cd+" --context_param MBR_NO="+MBR_NO;
    		    System.out.println("COM : " + command);
    		    channelExec.setCommand(command);
    		    channelExec.connect();

    		    System.out.println("==> Connected to" + host +"\n"+command );
    		    map.put("PID", pid1);
            	map.put("STEP_NM", "01. JAVA");
            	map.put("JOB_NM", "TOS_CALL");
            	map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD : "+omr_mst_cd+", MBR_NO : "+MBR_NO+" , ML_SERVER : "+serverno);
            	map.put("LOG_MSG", "Tos 호출 " + command);
            	map.put("ST_END_DIV","begin");
            	map.put("CMPLT_MSG", "success");
            	service.javalog(map);
            	System.out.println(CMPN_CD+","+BSTOR_CD+","+exam_cd+","+omr_mst_cd+","+MBR_NO);

            	service.sttuschg(CMPN_CD,BSTOR_CD,exam_cd,omr_mst_cd,MBR_NO,2);
            	service.UPDATE_REUPLD_YN(exam_cd, CMPN_CD, BSTOR_CD, pid1, "\'N\'");
          } catch(JSchException e){
    		    map.put("LOG_MSG", "서버 접속 실패 : "+e.toString());
    		    map.put("ST_END_DIV","end");
    		    map.put("CMPLT_MSG", "fail");
    		    service.javalog(map);
    		    service.UPDATE_REUPLD_YN(exam_cd, CMPN_CD, BSTOR_CD, pid1, "\'Y\'");
    	 }
    		int filecnt  = 0;
    		if(pid1 != null) {
	        ////System.out.println(pid1);
	       	 filecnt = service.getflist(exam_cd,CMPN_CD,BSTOR_CD,omr_mst_cd,sep,pid1).size();
//	         System.out.println("FCNTA : "+filecnt);
	     }
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("allfile", Integer.valueOf(allfile));
	        jsonObject.put("filecnt", Integer.valueOf(filecnt));
	        int file7 = (filecnt * 9);
//	        System.out.println("file7 : " + file7);
	        int temptime = (file7+110) / 60;
	        int tempdiff = temptime + 1;
	        if (temptime > 1) {
	          String time = tempdiff + "";
//	          System.out.println("TIME : " + time);
	          jsonObject.put("time", time);
	        } else {
	          jsonObject.put("time", "1");
	        }
	        
	        jsonObject.put("notupd", notupload);
	        jsonArray.add(jsonObject);
    		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
//        -----------------------------
    			
	//------
		@RequestMapping("albtest")
		public ModelAndView albtest(HttpSession session) {
			ModelAndView mav = new ModelAndView();
			Map<String, String> map = new HashedMap();
			MBR mbr = (MBR)session.getAttribute("MBR");
			String name = mbr.getNAME();
			Integer ecitest = Integer.parseInt(name.substring(name.length()-1));
			System.out.println(ecitest);
			
			/*
			//프로퍼티 객체 생성
	        Properties ubuntuprop = new Properties();
//	        String a = ""; String  b = "" ;String c = "";
	        int a = 0; int b = 0 ;int c = 0;
	        
	        for(int Sev_no = 1;Sev_no<4;Sev_no++) {
	        	try {
	    			FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
	    			ubuntuprop.load(fis);
	    		} catch (Exception e1) {
	    			e1.printStackTrace();
	    		}
	    		////System.out.println(mbr);
	    		String keyname = ubuntuprop.getProperty("privateKey");
	    		String publicDNS = ubuntuprop.getProperty("host"+Sev_no);
	    		File authfile = new File(keyname);
	    		String user = ubuntuprop.getProperty("user");
	    	    String host = publicDNS;
	    	    int port = Integer.parseInt(ubuntuprop.getProperty("port"));
	    	    
	    		try {

	    			//원격제어 객체 생성
	    			JSch jsch=new JSch();
	    		    String privateKey = keyname;
	    		    jsch.addIdentity(privateKey);
	    		    
	    		    ////System.out.println("identity added ");

	    		    Session jschsession = jsch.getSession(user, host, port);
	    		    System.out.println("session created.");


	    			//원격제어 조건 설정
	    		    jschsession.setConfig("StrictHostKeyChecking","no");
	    		    jschsession.setConfig("GSSAPIAuthentication","no");
	    		    jschsession.setServerAliveInterval(120 * 1000);
	    		    jschsession.setServerAliveCountMax(1000);
	    		    jschsession.setConfig("TCPKeepAlive","yes");		            
	    		    

	    			//연결
	    		    jschsession.connect();
	    		    

	    			//명령 객체 생성
	    		    Channel channel = jschsession.openChannel("exec");

	    		    ChannelExec channelExec = (ChannelExec) channel;
	    		    
	    		    System.out.println("==> Connected to" + host);
	    		   
	    		    String command1 =ubuntuprop.getProperty("command");
//	    		    String command2 =ubuntuprop.getProperty("command2");
//	    		    String command3 =ubuntuprop.getProperty("command3");
//	    		    String command4 =ubuntuprop.getProperty("command4");
	    		    
	    		    String command = "ps -ef | grep OMR01_00_MAIN_run.sh  | wc -l";
//	    		    String command = "pwd";
	    		    System.out.println("COM : " + command);
	    		    channelExec.setCommand(command);
	    		    channelExec.connect();

	    		    StringBuilder outputBuffer = new StringBuilder();
	    	        InputStream in = channel.getInputStream();
	    	        ((ChannelExec) channel).setErrStream(System.err);        
	    	        
	    	        channel.connect();  //실행
	    	        
	    	  
	    	        byte[] tmp = new byte[1024];
	    	        int h = 0;
	    	        while (true) {
	    	        	while (in.available() > 0) {
	    	        		int i = in.read(tmp, 0, 1024);
	    	                outputBuffer.append(new String(tmp, 0, i));
	    	                if (i < 0) break;
	    	            }
	    	            if (channel.isClosed()) {
	    	                System.out.print("결과 : ");
	    	                System.out.println(outputBuffer.toString());
	    	                if(Sev_no == 1) {
//	    	                	String str1 = outputBuffer.toString().trim();
//	    	                	System.out.println("str1 : " + str1);
//	    	                	int inte1 = Integer.parseInt(str1);
//	    	                	System.out.println("inte1 :" + inte1);
	    	                	a=Integer.parseInt(outputBuffer.toString().trim())-2;
	    	                }else if(Sev_no == 2) {
	    	                	b=Integer.parseInt(outputBuffer.toString().trim())-2;
	    	                }else {
	    	                	c=Integer.parseInt(outputBuffer.toString().trim())-2;
	    	                }
	    	                channel.disconnect();
	    	                break;
	    	            }
	    	        }
	    		}catch(Exception e){
	    		    e.printStackTrace();
	    		}
	    	}
	        if(a<=b && a<=c) {
	        	
	        }*/
	        return mav;
		}
	//-------	

	

	@RequestMapping("OMRfilerecogTF")
	public ResponseEntity<JSONArray> OMRfilerecogTF(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String exam_cd = request.getParameter("examcd");
		String omr_cd = request.getParameter("omrcd");
		String sep = request.getParameter("sep");
        MBR mbr = (MBR) session.getAttribute("MBR");
        int  CMPN_CD = mbr.getCMPN_CD();
		int  MBR_NO = mbr.getMBR_NO();
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
		
		Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
		
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		List<FILELOG> list = new ArrayList<FILELOG>();
		String pid1 = service.pid(exam_cd,CMPN_CD,BSTOR_CD,omr_cd,sep);
		Integer pid1time = service.pidtime(pid1);
		if(pid1time == null){
			pid1time = 0;
		}
		if(pid1 != null){
//			System.out.println("pid1 : " + pid1 );
		}else{
//			System.out.println("pid1 : " + null );
		}
		list = service.floglist(pid1,exam_cd,CMPN_CD,BSTOR_CD,omr_cd,sep);
		if(list.size() !=0){
//			System.out.println("list : " + list.size());
		}else{
//			System.out.println("listsize : "+ 0 );
		}
		String arg_val ="%EXAM_CD : "+exam_cd+", CMPN_CD%: "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD%"+omr_cd+"%";
		String job_nm = "TOS_CALL";
		String step_nm = "01. JAVA";

		
		////System.out.println("PUD1 : " + pid1 + ", arg : " + arg_val);
//		System.out.println("C ");
		Integer pidsys = service.pidsys(arg_val,job_nm,step_nm,pid1);
		if(pidsys == null){
			pidsys = 0;
		}
		//-------------------------------------------------------------
/*
		if(pidsys == null && pid1 != null && pid1time > 3) {
//			-----------------------------------------------------------------
			int a = 0; int b = 0 ;int c = 0;
			int serverno = 0;
			//프로퍼티 객체 생성
			Properties ubuntuprop = new Properties();
			try {
				FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
				ubuntuprop.load(fis);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			////System.out.println(mbr);
			String keyname = ubuntuprop.getProperty("privateKey");
			File authfile = new File(keyname);
			String publicDNS = "";
			String user = ubuntuprop.getProperty("user");
			String host = publicDNS;
			int port = Integer.parseInt(ubuntuprop.getProperty("port"));
			System.out.println("privateKey : " + keyname + "\n user :" + user);

//        --- 프로세스수가 적은 서버 찾기
			for(int Sev_no = 1;Sev_no<4;Sev_no++) {
				publicDNS = ubuntuprop.getProperty("host"+Sev_no);
				host = publicDNS;

				try {

					//원격제어 객체 생성
					JSch jsch=new JSch();
					String privateKey = keyname;
					jsch.addIdentity(privateKey);

					////System.out.println("identity added ");

					Session jschsession = jsch.getSession(user, host, port);
					System.out.println("session created.");


					//원격제어 조건 설정
					jschsession.setConfig("StrictHostKeyChecking","no");
					jschsession.setConfig("GSSAPIAuthentication","no");
					jschsession.setServerAliveInterval(120 * 1000);
					jschsession.setServerAliveCountMax(1000);
					jschsession.setConfig("TCPKeepAlive","yes");


					//연결
					jschsession.connect();


					//명령 객체 생성
					Channel channel = jschsession.openChannel("exec");

					ChannelExec channelExec = (ChannelExec) channel;
					String DL = ubuntuprop.getProperty("DEVLIVE");
					String command = "";
					if(DL.equals("LIVE")){
						command = "ps -ef | grep OMR01_00_MAIN_run.sh | wc -l";
					}else if (DL.equals("DEV")){
						command = "ps -ef | grep OMR01_00_MAIN_DEV_run.sh | wc -l";
					}

//    		    String command = "pwd";
					System.out.println("COM : " + command);
					channelExec.setCommand(command);
					channelExec.connect();

					StringBuilder outputBuffer = new StringBuilder();
					InputStream in = channel.getInputStream();
					((ChannelExec) channel).setErrStream(System.err);

					channel.connect();  //실행


					byte[] tmp = new byte[1024];
					int h = 0;
					while (true) {
						while (in.available() > 0) {
							int i = in.read(tmp, 0, 1024);
							outputBuffer.append(new String(tmp, 0, i));
							if (i < 0) break;
						}
						if (channel.isClosed()) {
							if(Sev_no == 1) {
//    	                	String str1 = outputBuffer.toString().trim();
//    	                	System.out.println("str1 : " + str1);
//    	                	int inte1 = Integer.parseInt(str1);
//    	                	System.out.println("inte1 :" + inte1);
								a=Integer.parseInt(outputBuffer.toString().trim())-2;
							}else if(Sev_no == 2) {
								b=Integer.parseInt(outputBuffer.toString().trim())-2;
							}else {
								c=Integer.parseInt(outputBuffer.toString().trim())-2;
							}
							channel.disconnect();
							break;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			System.out.println("a : "+a+" b : "+b+" c : "+c);
			if(a<=b && a<=c) {
				publicDNS = ubuntuprop.getProperty("host1");
				serverno = 1;
			}else if(b<a && b<=c) {
				publicDNS = ubuntuprop.getProperty("host2");
				serverno = 2;
			}else {
				publicDNS = ubuntuprop.getProperty("host3");
				serverno = 3;
			}
			host = publicDNS;
			Map<String, String> map = new HashedMap();
			try {

				//원격제어 객체 생성
				JSch jsch=new JSch();
				String privateKey = keyname;
				jsch.addIdentity(privateKey);

				////System.out.println("identity added ");
				System.out.println("host : "+ host);
				Session jschsession = jsch.getSession(user, host, port);
				System.out.println("session created.");


				//원격제어 조건 설정
				jschsession.setConfig("StrictHostKeyChecking","no");
				jschsession.setConfig("GSSAPIAuthentication","no");
				jschsession.setServerAliveInterval(120 * 1000);
				jschsession.setServerAliveCountMax(1000);
				jschsession.setConfig("TCPKeepAlive","yes");


				//연결
				jschsession.connect();


				//명령 객체 생성
				Channel channel = jschsession.openChannel("exec");

				ChannelExec channelExec = (ChannelExec) channel;

				System.out.println("==> Connected to" + host);

				String command1 =ubuntuprop.getProperty("command");
//    		    String command2 =ubuntuprop.getProperty("command2");
//    		    String command3 =ubuntuprop.getProperty("command3");
//    		    String command4 =ubuntuprop.getProperty("command4");




				String command = command1+ " --context_param EXAM_CD="+exam_cd+" --context_param CMPN_CD="+CMPN_CD+" --context_param BSTOR_CD="+BSTOR_CD+" --context_param OMR_MST_CD="+omr_cd+" --context_param MBR_NO="+MBR_NO;
				System.out.println("COM : " + command);
				channelExec.setCommand(command);
				channelExec.connect();

				System.out.println("==> Connected to" + host +"\n"+command );
				map.put("PID", pid1);
				map.put("STEP_NM", "01. JAVA");
				map.put("JOB_NM", "TOS_CALL");
				map.put("ARG_VAL", "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD : "+omr_cd+", MBR_NO : "+MBR_NO+" , ML_SERVER : "+serverno);
				map.put("LOG_MSG", "Tos 호출 " + command);
				map.put("ST_END_DIV","begin");
				map.put("CMPLT_MSG", "success");
				service.javalog(map);
				System.out.println(CMPN_CD+","+BSTOR_CD+","+exam_cd+","+omr_cd+","+MBR_NO);
				service.sttuschg(CMPN_CD,BSTOR_CD,exam_cd,omr_cd+"",MBR_NO,2);
				service.UPDATE_REUPLD_YN(exam_cd, CMPN_CD, BSTOR_CD, pid1, "\'N\'");
			} catch(JSchException e){
				map.put("LOG_MSG", "서버 접속 실패 : "+e.toString());
				map.put("ST_END_DIV","end");
				map.put("CMPLT_MSG", "fail");
				service.javalog(map);
				service.UPDATE_REUPLD_YN(exam_cd, CMPN_CD, BSTOR_CD, pid1, "\'Y\'");
			}
//			-----------------------------------------------------------------
			arg_val = "EXAM_CD : "+exam_cd+", CMPN_CD : "+CMPN_CD+", BSTOR_CD : "+BSTOR_CD+", OMR_MST_CD : "+omr_cd+", MBR_NO : "+MBR_NO+" , ML_SERVER : "+serverno ;
			job_nm ="TOS_CALL";
			step_nm ="01. JAVA";
		 	pidsys =service.pidsys(arg_val,job_nm,step_nm,pid1);
		}*/
		//----------------------------------------------------------
//		System.out.println("PIDSYS :" + pidsys);
		String s = service.ivalue(exam_cd,CMPN_CD,BSTOR_CD,sep)+"";
//		System.out.println("S : "+ s);
		Integer mno_bst = service.getmno_bst(BSTOR_CD);
//		System.out.println("D : " +mno_bst);
//		System.out.println("pidsys : "+pidsys);

		List<String> imglist = new ArrayList<String>();
		
		for(FILELOG fl : list) {
			if(fl.getPID().equals(pid1)) {
				imglist.add(fl.getCHG_FILE_NM()+".jpg");
//				System.out.println(fl.getCHG_FILE_NM()+".jpg");
			}
		}

		
		JSONArray jsonArray = new JSONArray();
		
		try {
			if(list.size() == 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("view" , 1);
					jsonArray.add(jsonObject);
			} else  {
				int timediff =pidsys;
				
				//int temptime = realimglist.size() * 20 / 60;
				int temptime = ((imglist.size() * 9)+110) / 60;
			    int tempdiff = temptime + 1;
			    
			    System.out.println("td"+ timediff +"\n te : " +temptime + "\n ted :" + tempdiff );
			    if (timediff < tempdiff) {
			    	String imgnm = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + imglist.get(0);
//			        String imgnm = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + realimglist.get(0);
//			        System.out.println("imgnm : " +imgnm);
			        OMR_RECOG recog = service.getrecogobjt(imgnm);
//			        //System.out.println("AS:" + recog + "\n qw");
					if (recog == null) {
			           	int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, omr_cd, sep, pid1);
			           	JSONObject jsonObject = new JSONObject();
			           	jsonObject.put("view", 2);
			           	jsonObject.put("filecnt", filecnt);
			           	//int a = Math.round(((realimglist.size() * 20 - timediff * 60) / 60));
			           	int a = Math.round(((((imglist.size() * 9)+110) - timediff * 60) / 60));
			           	a++;
//			           	//System.out.println("tot-timediff" + a);
//			           	if (realimglist.size() * 20 % 60 == 0) {a--;}
			           	if (((imglist.size() * 9)+110) % 60 == 0) {
			           		a--;
			           	}
			           	jsonObject.put("minu", a);
			           	jsonArray.add(jsonObject);
					} else {
			            int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, omr_cd, sep, pid1);
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("view", Integer.valueOf(4));
			            jsonObject.put("al", "인식완료");
			            jsonArray.add(jsonObject);
			        }
			    } else {
			    	int reuploadcnt = 0;
			       /* List<String> ilist = new ArrayList<>();
			        for (FILELOG fl : list) {
			        	int tdiff = service.gettdiff(fl);
			            if (tdiff > temptime && fl.getPID().equals(pid1)) {
			            	String imgname = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + fl.getCHG_FILE_NM()+".jpg";
		              		int recog = service.reuploadomr(imgname);
		              		if (recog == 0) {
		              			ilist.add(fl.getORIGIN_FILE_NM());
		              		}
			            }
			        }
			        if (ilist.size() == 0) {
			        	JSONObject jsonObject = new JSONObject();
			        	jsonObject.put("view", Integer.valueOf(3));
			        	jsonObject.put("filecnt", "0개");
			        	jsonArray.add(jsonObject);
			        } else {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("view", Integer.valueOf(3));
							jsonObject.put("filecnt", ilist.size() + "개");
							String il = "";
							for (String li : ilist) {
								il = String.valueOf(il) + li + ",";
							}
			            	jsonObject.put("filename", il);
			            	System.out.println("reupd : "+ il);
			            	jsonArray.add(jsonObject);
			        }*/
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("view", Integer.valueOf(3));
					jsonArray.add(jsonObject);

				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
//		System.out.println("a : " +jsonArray.toString());
		return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	}
			    
			    
			    
			    
			    
			    
			    /*
			    List<String> ilist = new ArrayList<>();
		          for (FILELOG fl : list) {
		            if (fl.getPID().equals(pid1)) {
		              String imgname = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + fl.getCHG_FILE_NM();
		              int recog = service.reuploadomr(imgname);
		              if (recog == 0)
		                ilist.add(fl.getCHG_FILE_NM()); 
		            } 
		          } 
		          String il = "";
		          for (String li : ilist) {
			         il += li + ","; 
		          }
		          if(il.length() != 0) {
		        	  il=il.substring(0, il.length()-1);
		          }
			    if (timediff < tempdiff) {
			          String imgnm = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + realimglist.get(0);
			          OMR_RECOG recog = 
			        		  service.getrecogobjt(imgnm);
//			          //System.out.println("AS:" + recog + "qw");
			          if (recog == null) {
			            int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, omr_cd, sep, pid1);
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("view", 2);
			            jsonObject.put("filecnt", filecnt);
			            jsonObject.put("filename", il);
			            int a = Math.round(((realimglist.size() * 20 - timediff * 60) / 60));
			            a++;
			            //System.out.println("tot-timediff" + a);
			            if (realimglist.size() * 20 % 60 == 0)
			              a--; 
			            jsonObject.put("minu", a);
			            jsonArray.add(jsonObject);
			          } else {
			            int filecnt = service.filecnt(exam_cd, CMPN_CD, BSTOR_CD, omr_cd, sep, pid1);
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("view", 4);
			            jsonObject.put("al", "인식완료");
			            jsonObject.put("filename", il);
			            jsonArray.add(jsonObject);
			          } 
			        } else {
			          int reuploadcnt = 0;
			          List<String> iilist = new ArrayList<>();
			          for (FILELOG fl : list) {
			            int tdiff = service.gettdiff(fl);
			            if (tdiff > temptime && 
			              fl.getPID().equals(pid1)) {
			              String imgname = s3dlpath+"/omr/" + exam_cd + "/" + CMPN_CD + "/" + BSTOR_CD + "/" + omr_cd + "/" + fl.getCHG_FILE_NM();
			              int recog = service.reuploadomr(imgname);
			              if (recog == 0)
			                iilist.add(fl.getCHG_FILE_NM()); 
			            } 
			          } 
			          if (iilist.size() == 0) {
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("view", Integer.valueOf(3));
			            jsonObject.put("filecnt", "0");
			            jsonObject.put("filename", "없습니다");
			            jsonArray.add(jsonObject);
			          } else {
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("view", Integer.valueOf(3));
			            jsonObject.put("filecnt", ilist.size() + "개");
			            
//			            //System.out.println(il);
			            String iil = "";
				          for (String li : ilist) {
					         iil += li + ","; 
				          } 
				          il=il.substring(0, il.length()-1);
//			            //System.out.println("sdf : " + il);
			            jsonObject.put("filename", iil);
			            jsonArray.add(jsonObject);
			          } 
			        } 
			      }
			 //System.out.println(jsonArray.get(0).toString());
			    } catch (Exception e1) {
			    	jsonArray.add(e1);
			      e1.printStackTrace();
			    } 
			    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
			   }*/
	
	@RequestMapping("REUPLOAD_OCR")
	public ModelAndView REUPLOAD_OCR(String examcd,String ocrcd,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer  bstorcd = mbr.getBSTOR_CD();
		Integer  cmpncd = mbr.getCMPN_CD();
		String sep = "OCR";
		String pid1 = service.pid(examcd, cmpncd, bstorcd, ocrcd, sep);
		JSONArray jsonArray = new JSONArray();
		List<FILELOG> flist = service.getflist(examcd,cmpncd,bstorcd,ocrcd,sep,pid1);
		mav.addObject("flist",flist);
		mav.addObject("examcd",examcd);
		mav.addObject("ocrcd",ocrcd);
		return mav;
	}

	@RequestMapping("REUPLOAD_OMR")
	public ModelAndView REUPLOAD_OMR(String CHG_FILE_NM,String omrcd,String examcd,Integer bstorcd,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer  cmpncd = mbr.getCMPN_CD();
		String sep = "OMR";
		
		String pid1 = service.pid(examcd, cmpncd, bstorcd, omrcd, sep);
		JSONArray jsonArray = new JSONArray();
		List<FILELOG> flist = service.getflist(examcd, cmpncd, bstorcd, omrcd, sep,pid1);
		List<FILELOG> realflist = new ArrayList<FILELOG>();
		
		Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
		
		
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		////System.out.println(exam_no);
		List<String> imglist = new ArrayList<String>();
		
		List<FILELOG> reallist = new ArrayList<FILELOG>();
		
		boolean upload = true;
		for(FILELOG imgname : flist) {
			////System.out.println("imgname : "+imgname);
			String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/job_fail/"+imgname.getCHG_FILE_NM()+".jpg";
			S3Object s3o = new S3Object();
			try {
				 s3o = s3Client.getObject("edu-ai", key);
				 ////System.out.println("1");
				 reallist.add(imgname);
			} catch (AmazonS3Exception aws) {
				key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/check_fail/"+imgname.getCHG_FILE_NM()+".jpg";
				try {
					 s3o = s3Client.getObject("edu-ai", key);
					 reallist.add(imgname);
				}catch (AmazonS3Exception aws3) {
					continue;
				}
			}
		}
		
		mav.addObject("flist",reallist);
		mav.addObject("examcd",examcd);
		mav.addObject("omrcd",omrcd);
		mav.addObject("bstorcd",bstorcd);
		return mav;
	}
	@RequestMapping("updcheck")
	public ModelAndView updcheck(String omrcd,String examcd,Integer bstorcd,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer  cmpncd = mbr.getCMPN_CD();
		String sep = "OMR";
		String pid1 = service.pid(examcd, cmpncd, bstorcd, omrcd, sep);
		JSONArray jsonArray = new JSONArray();
		List<FILELOG> flist = service.getflist(examcd, cmpncd, bstorcd, omrcd, sep,pid1);
		Properties prop = new Properties();
		 try {
		      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
		      prop.load(fis);
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
			String dl = prop.getProperty("DEVLIVE");
			String s3dlpath = "aido";
			if(dl.equals("DEV")) {
				s3dlpath+="_dev";
			}else if(dl.equals("TS")) {
				s3dlpath+="_ts";
			}
			
			
			String AccessKey = prop.getProperty("Access");
			String SecretKey = prop.getProperty("Secret");
			AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

			//String path = opath+"/Users/uddon/Desktop/temp/";
			
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
		            .withRegion(Regions.AP_NORTHEAST_2)
		            .withCredentials(new AWSStaticCredentialsProvider(credentials))
		            .build();
			////System.out.println(exam_no);
			
			List<FILELOG> reuld = new ArrayList<FILELOG>();
			boolean upload = true;
			for(FILELOG fl : flist) {			
				String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+fl.getCHG_FILE_NM()+".jpg";
				S3Object s3o = new S3Object();
				try {
					 s3o = s3Client.getObject("edu-ai", key);
					 reuld.add(fl);
				} catch (AmazonS3Exception aws) {
					continue;
				}
			}
			////System.out.println(reuld.toString());
		
		mav.addObject("flist",reuld);
		mav.addObject("examcd",examcd);
		mav.addObject("omrcd",omrcd);
		mav.addObject("bstorcd",bstorcd);
		return mav;
	}
	@RequestMapping("s3delete")
	public ResponseEntity<JSONArray> s3delete(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String fileString = request.getParameter("filelist");
		String examcd = request.getParameter("examcd");
		String bstorcd = request.getParameter("bstorcd");
		String omrcd = request.getParameter("omrcd");
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer  cmpncd = mbr.getCMPN_CD();
		
		List<String> filelist = Arrays.asList(fileString.split("/"));
		Properties prop = new Properties();
		 try {
		      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
		      prop.load(fis);
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
			String dl = prop.getProperty("DEVLIVE");
			String s3dlpath = "aido";
			if(dl.equals("DEV")) {
				s3dlpath+="_dev";
			}else if(dl.equals("TS")) {
				s3dlpath+="_ts";
			}
			
			
			String AccessKey = prop.getProperty("Access");
			String SecretKey = prop.getProperty("Secret");
			AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

			//String path = opath+"/Users/uddon/Desktop/temp/";
			
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
		            .withRegion(Regions.AP_NORTHEAST_2)
		            .withCredentials(new AWSStaticCredentialsProvider(credentials))
		            .build();
			
			for (String fl : filelist) {
				String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+fl;
				s3Client.deleteObject("edu-ai", key);
			}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "삭제되었습니다.");
		jsonArray.add(jsonObject);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	   }
	@RequestMapping("reuploadcimg")
	public ResponseEntity<JSONArray> reuploadcimg(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
//		ModelAndView mav = new ModelAndView();
//		//System.out.println(CHG_FILE_NM);
		String imgname = null ;
		MBR mbr = (MBR)session.getAttribute("MBR");
		int bstorcd = mbr.getBSTOR_CD();
		int cmpncd = mbr.getCMPN_CD();
		String filename = request.getParameter("filename");
		String examcd = request.getParameter("examcd");
		String ocrcd = request.getParameter("ocrcd");
//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
//			imgname = service.getomrfilename(examcd,omrcd,CHG_FILE_NM,bstorcd,cmpncd);
			imgname = filename;
//			//System.out.println("in :"+imgname);
		}catch (IOException io) {
			io.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		////System.out.println(exam_no);
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
		
		String key = s3dlpath+"/ocr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/crop/"+imgname;
		S3Object s3o = new S3Object();
		try {
			 s3o = s3Client.getObject("edu-ai", key);	
		} catch (AmazonS3Exception aws) {
			key = s3dlpath+"/ocr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/job_fail/"+imgname;
			try {
				s3o = s3Client.getObject("edu-ai", key);	
			} catch (AmazonS3Exception aws2) {
				key = s3dlpath+"/ocr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/job_done/"+imgname;
				try {
					s3o = s3Client.getObject("edu-ai", key);	
				} catch (Exception aws3) {
						aws3.printStackTrace();
				}
			}
		} 
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/ocr/"+examcd+"/"+cmpncd+"/"+bstorcd);
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}
		String jsppath ="images/ocr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+imgname;
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgname);
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    JSONArray jsonArray = new JSONArray();
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("jspimg", jsppath);
	    ////System.out.println(jsppath);
	    jsonArray.add(jsonObject);
	    return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	   }
	@RequestMapping("reuploadmimg")
	public ResponseEntity<JSONArray> reuploadmimg(HttpServletRequest request ,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String filename = request.getParameter("filename");
		String examcd = request.getParameter("examcd");
		String omrcd = request.getParameter("omrcd");
		Integer bstorcd = Integer.parseInt(request.getParameter("bstorcd"));
		String imgname = request.getParameter("filename")+".jpg";
		System.out.println("img : " + imgname);
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		
//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
//			imgname = service.getomrfilename(examcd,omrcd,CHG_FILE_NM,bstorcd,cmpncd);
			
//			//System.out.println("in :"+imgname);
		}catch (IOException io) {
			io.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		////System.out.println(exam_no);
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
		String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/check_fail/"+imgname;
		////System.out.println(key);
		S3Object s3o = new S3Object();
		try {
			 s3o = s3Client.getObject("edu-ai", key);
		} catch (AmazonS3Exception aws) {
			key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/job_fail/"+imgname;
			////System.out.println(key);
			try {
				s3o = s3Client.getObject("edu-ai", key);	
			} catch (AmazonS3Exception aws2) {
				key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+imgname;
				////System.out.println(key);
				try {
					s3o = s3Client.getObject("edu-ai", key);	
				} catch (Exception aws3) {
					key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/rot_images/rot_"+imgname;
					////System.out.println(key);
					try {
						s3o = s3Client.getObject("edu-ai", key);	
					} catch (Exception aws4) {
						aws4.printStackTrace();
					}
				}
			}
		} 
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd);
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}
		String jsppath ="images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+imgname;
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgname);
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    
	    JSONArray jsonArray = new JSONArray();
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("jspimg", jsppath);
	    jsonArray.add(jsonObject);
//	    //System.out.println(jsppath);
	    return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	@RequestMapping("reuploadimg")
	public ModelAndView reuploadimg(String CHG_FILE_NM, String omrcd,String examcd, HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ModelAndView mav = new ModelAndView();
//		//System.out.println(CHG_FILE_NM);
		String imgname = null ;
		MBR mbr = (MBR)session.getAttribute("MBR");
		int bstorcd = mbr.getBSTOR_CD();
		int cmpncd = mbr.getCMPN_CD();
//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
//			imgname = service.getomrfilename(examcd,omrcd,CHG_FILE_NM,bstorcd,cmpncd);
			imgname = CHG_FILE_NM;
//			//System.out.println("in :"+imgname);
		}catch (IOException io) {
			io.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		////System.out.println(exam_no);
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
		String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/chk_fail/"+imgname;		
		S3Object s3o = new S3Object();
		try {
			 s3o = s3Client.getObject("edu-ai", key);	
		} catch (AmazonS3Exception aws) {
			key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/job_fail/"+imgname;
			try {
				s3o = s3Client.getObject("edu-ai", key);	
			} catch (AmazonS3Exception aws2) {
				key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/rot_images/rot_"+imgname;
				try {
					s3o = s3Client.getObject("edu-ai", key);	
				} catch (Exception aws3) {
					aws3.printStackTrace();
				}
			}
		} 
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd);
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}
		String jsppath ="images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+imgname;
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgname);
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    mav.addObject("jspimg", jsppath);
//	    //System.out.println(jsppath);
	    mav.setViewName("OMR_CARD");
	    return mav;
	}
	
	@RequestMapping("OMRrecoglist")
	public ResponseEntity<JSONArray> OMRrecoglist(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String exam_cd = request.getParameter("examcd");
		String omr_cd = request.getParameter("omrcd");
        MBR mbr = (MBR) session.getAttribute("MBR");
//        int  CMPN_CD = mbr.getCMPN_CD();
//		int  MBR_NO = mbr.getMBR_NO();		   
		Integer  bstorcd = mbr.getBSTOR_CD();
		Integer  cmpncd = mbr.getCMPN_CD();
		int rpageNum = Integer.parseInt(request.getParameter("rpageNum"));
		String rsearchcontent = request.getParameter("rsearchcontent");
		int rlimit = Integer.parseInt(request.getParameter("rlimit"));
//		//System.out.println("Rl : " + rlimit);
		
		if(rpageNum == 0) {
			rpageNum = 1;
		}
		if(rsearchcontent == null || rsearchcontent.trim().equals("")) {
			rsearchcontent=null;
		}else {
			rsearchcontent = rsearchcontent.trim();
	    	for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}

		}
		if(rlimit == 0) {
			rlimit=5;
		}
		////System.out.println(rpageNum);	
//		//System.out.println(exam_cd+","+omr_cd+","+bstorcd+","+rsearchcontent+","+cmpncd);
		
		int recodecnt = service.omr_recog_cnt(exam_cd,omr_cd,bstorcd,rsearchcontent,cmpncd);
//		//System.out.println("recode : "+recodecnt);
		int rmaxpage = (int) ((double) recodecnt / rlimit + 0.95);
		int rstartpage = ((int) (rpageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int rendpage = rstartpage + 9;
		if (rendpage > rmaxpage) rendpage = rmaxpage;
		int boardno = recodecnt - (rlimit * (rpageNum - 1));
		JSONArray jsonArray = new JSONArray();
		List<OMR_RECOG> list;
		try {
//			//System.out.println(exam_cd+","+omr_cd+","+BSTOR_CD+","+rpageNum+","+rlimit+","+rsearchcontent);
			 list =service.getomrrecog(exam_cd,omr_cd,bstorcd,rpageNum,rlimit,rsearchcontent,cmpncd);
			 if(list.size() != 0) {
				 for(OMR_RECOG reg : list) {
					 
					 JSONObject jsonObject = new JSONObject();
					 	jsonObject.put("OMR_KEY" , reg.getOMR_KEY());
						jsonObject.put("EXAM_CD" , reg.getEXAM_CD());
						jsonObject.put("LSN_CD" ,  reg.getLSN_CD());
						jsonObject.put("LSN_NM" ,  reg.getLSN_NM());
						jsonObject.put("OMR_MST_CD" ,reg.getOMR_MST_CD());
						String a =  reg.getEXMN_NO();
						a=a.replace("-1", "□");
						jsonObject.put("EXMN_NO" ,a);
						jsonObject.put("STDN_NM" ,reg.getSTDN_NM());
						if(reg.getERR_CNT() == 0) {
							jsonObject.put("STTUS" ,"SUCCESS");	
						}else{
							jsonObject.put("STTUS" ,"WARNING");
						}
						////System.out.println(jsonObject);
						jsonArray.add(jsonObject); 
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rpageNum", rpageNum);
		jsonObject.put("rmaxpage",rmaxpage);
		jsonObject.put("rstartpage",rstartpage);
		jsonObject.put("rendpage",rendpage);
		jsonObject.put("cnt",recodecnt);
		////System.out.println(jsonObject);
		jsonArray.add(jsonObject);
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	
	@RequestMapping("OMR_GRDG")
	public ModelAndView OMR_GRDG(Integer  limit,Integer pageNum, String searchcontent,String searchbstor,String searchmock,String searchgrade,String searchsub,String searchscore,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		System.out.println("sub : " + searchscore);
//		System.out.println("limit : "+limit+", pageNum : "+pageNum+" scontent : "+searchcontent+"\n");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
//			System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
			
//			System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
			
		}
		List<Integer> sbstr = service.getbstorlist();
		if(searchbstor == null || searchbstor.trim().equals("")) {
			searchbstor = "(";
			for(Integer bst : sbstr) {
				searchbstor+=bst+",";
			}
			searchbstor=searchbstor.substring(0,searchbstor.length()-1);
			searchbstor += ")";
			if(searchbstor.length()==1) {
				searchbstor=null;
			};
		}
		List<Integer> smock = service.getsmocklist();
		if(searchmock == null || searchmock.trim().equals("")) {
			searchmock = "(";
			for(Integer bst : smock) {
				searchmock+=bst+",";
			}
			searchmock=searchmock.substring(0,searchmock.length()-1);
			searchmock += ")";
			
			if(searchmock.length()==1) {
				searchmock=null;
			};
			
		}
		List<String> sgrade = service.getsgragelist();
		if(searchgrade == null || searchgrade.trim().equals("")) {
			searchgrade = "(";
			for(String bst : sgrade) {
				searchgrade+="\'"+bst+"\',";
			}
			searchgrade=searchgrade.substring(0,searchgrade.length()-1);
			searchgrade += ")";
			
			if(searchgrade.length()==1) {
				searchgrade=null;
			};
			
		}
		List<String> ssub = service.getssublist();
		if(searchsub == null || searchsub.trim().equals("")) {
			searchsub = "(";
			for(String bst : ssub) {
				searchsub+="\'"+bst+"\',";
			}
			searchsub+="\'선택과목없음\'";
			searchsub += ")";
			
			if(searchgrade.length()==1) {
				searchgrade=null;
			};
			
		}
		if(searchscore == null || searchscore.trim().equals("")) {
			searchscore="NOTEQ";
		}else if(searchscore.equals("NOT")) {
			searchscore = "NOT";
		}else if(searchscore.equals("EQ")) {
			searchscore = "EQ";
		}else{
			searchscore = "NOTEQ";
		}

		if(limit == null) {
			limit=20;
		}

//		System.out.println("bst : " + searchbstor);
//		System.out.println("mock : " + searchmock);
//		System.out.println("grd : " + searchgrade);
//		System.out.println("sub : " + searchsub);
//		System.out.println("score : " + searchscore);
//		System.out.println("li : " + limit);
//		System.out.println("pageNum : " + pageNum);


		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer bstor_cd = mbr.getBSTOR_CD();
		Integer cmpncd = mbr.getCMPN_CD();
//		System.out.println(searchcontent+" , "+bstor_cd+" , "+cmpncd+" , "+searchbstor+" , "+searchmock+" , "+searchgrade+" , "+searchsub+" , "+searchscore);
		int listcount = 0;
		List<OMR_RECOG> OMR_EXAM_sub_List = null;
		if(mbr.getBSTOR_CD() != 3) {
			listcount  = service.OMR_Exam_GRAD_Count1000(searchcontent, bstor_cd, cmpncd, searchbstor, searchmock, searchgrade, searchsub, searchscore);
			OMR_EXAM_sub_List = service.OMRExamgradList1000(pageNum, limit, searchcontent, bstor_cd, cmpncd, searchbstor, searchmock, searchgrade, searchsub, searchscore);
		}else{
			listcount  = service.OMR_Exam_GRAD_Count(searchcontent, bstor_cd, cmpncd, searchbstor, searchmock, searchgrade, searchsub, searchscore);
			OMR_EXAM_sub_List = service.OMRExamgradList(pageNum, limit, searchcontent, bstor_cd, cmpncd, searchbstor, searchmock, searchgrade, searchsub, searchscore);
		}
//		for (OMR_RECOG omlist : OMR_EXAM_sub_List ) {
//			System.out.println(omlist.toString());
//		}
//		System.out.println("LC : " + OMR_EXAM_sub_List.toString());
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("listcount",listcount);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);

		mav.addObject("boardno", boardno);
		mav.addObject("searchbstor", searchbstor);
		mav.addObject("searchgrade", searchgrade);
		mav.addObject("searchsub", searchsub);
		mav.addObject("searchmock", searchmock);
		mav.addObject("searchscore", searchscore);
		mav.addObject("EXAMList", OMR_EXAM_sub_List);
		mav.addObject("pageNum", pageNum);
		return mav;
	}
//	재업로드 필요 파일 목록 페이지 추가  --- 21.06.28 추가
	@RequestMapping("REUPLD_OMR")
	public ModelAndView REUPLD_OMR(Integer  limit,Integer pageNum, String searchcontent,String searchbstor,String searchmock,String searchgrade,String searchsub,String searchscore,HttpSession session) {
		ModelAndView mav = new ModelAndView();

		//페이지 번호 처리
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		//검색어 처리
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
//			System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		//지점 번호 리스트
		List<Integer> sbstr = service.getBlist();

		//지점 번호 검색 처리
		if(searchbstor == null || searchbstor.trim().equals("")) {
			searchbstor = "(";
			for(Integer bst : sbstr) {
				searchbstor += bst+",";
			}
			searchbstor=searchbstor.substring(0,searchbstor.length()-1);
			searchbstor += ")";
			if(searchbstor.length()==1) {
				searchbstor=null;
			};
		}

		//EXAM_CD 리스트
		List<Integer> smock = service.getsmocklist();

		//EXAM_CD 검색처리
		if(searchmock == null || searchmock.trim().equals("")) {
			searchmock = "(";
			for(Integer bst : smock) {
				searchmock+=bst+",";
			}
			searchmock=searchmock.substring(0,searchmock.length()-1);
			searchmock += ")";
			
			if(searchmock.length()==1) {
				searchmock=null;
			};
		}

		//학년리스트
		List<String> sgrade = service.getsgragelist();

		//학년 검색처리
		if(searchgrade == null || searchgrade.trim().equals("")) {
			searchgrade = "(";
			for(String bst : sgrade) {
				searchgrade+="\'"+bst+"\',";
			}
			searchgrade=searchgrade.substring(0,searchgrade.length()-1);
			searchgrade += ")";
			
			if(searchgrade.length()==1) {
				searchgrade=null;
			};
			
		}

		//교과리스트
		List<String> ssub = service.getssublist();

		//교과 검색처리
		if(searchsub == null || searchsub.trim().equals("")) {
			searchsub = "(";
			for(String bst : ssub) {
				searchsub+="\'"+bst+"\',";
			}
			searchsub+="\'선택과목없음\'";
			searchsub += ")";
			
			if(searchgrade.length()==1) {
				searchgrade=null;
			};
		}

		//페이지 갯수 처리
		if(limit == null) {
			limit=20;
		}

		//파라미터 출력
//		System.out.println("bst : " + searchbstor);
//		System.out.println("mock : " + searchmock);
//		System.out.println("grd : " + searchgrade);
//		System.out.println("sub : " + searchsub);
//		System.out.println("score : " + searchscore);
//		System.out.println("li : " + limit);
//		System.out.println("pageNum : " + pageNum);

		//세션정보 가져오기
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer bstor_cd = mbr.getBSTOR_CD();
		Integer cmpncd = mbr.getCMPN_CD();

//		System.out.println(searchcontent+" , "+bstor_cd+" , "+cmpncd+" , "+searchbstor+" , "+searchmock+" , "+searchgrade+" , "+searchsub+" , "+searchscore);

		//재업로드 갯수 (관리자용 / 개발자용)
		int REUPLD_CNT = service.REUPLD_CNT(searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub);
		int REUPLD_CNT1000 = service.REUPLD_CNT1000(searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub);

		//재업로드 리스트 (관리자용 / 개발자용)
		List<FILELOG> RELPLD_LIST = service.RELPLD_LIST(pageNum, limit, searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
		List<FILELOG> RELPLD_LIST1000 = service.RELPLD_LIST1000(pageNum, limit, searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);

		//리스트 출력
//		for (FILELOG RELPLD_OBJ : RELPLD_LIST ) {
//			System.out.println(RELPLD_OBJ.toString());
//		}

		//페이징 처리
		int maxpage = (int) ((double) REUPLD_CNT / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;

		//개발용 부분
		if(mbr.getMBR_NO() != 3) {
			mav.addObject("listcount",REUPLD_CNT1000);
			maxpage = (int) ((double) REUPLD_CNT1000 / limit + 0.95);
			startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		    endpage = startpage + 9;
			if (endpage > maxpage) endpage = maxpage;
			mav.addObject("maxpage",maxpage);
			mav.addObject("startpage",startpage);
			mav.addObject("endpage",endpage);
			mav.addObject("EXAMList", RELPLD_LIST1000);
		}else {
			mav.addObject("listcount",REUPLD_CNT);
			mav.addObject("maxpage",maxpage);
			mav.addObject("startpage",startpage);
			mav.addObject("endpage",endpage);
			mav.addObject("EXAMList", RELPLD_LIST);
		}

		//받은 파라미터 다시 넘기기
		mav.addObject("searchbstor", searchbstor);
		mav.addObject("searchgrade", searchgrade);
		mav.addObject("searchsub", searchsub);
		mav.addObject("searchmock", searchmock);
		mav.addObject("searchscore", searchscore);
		mav.addObject("pageNum", pageNum);
		return mav;
	}
	/*
	@RequestMapping("OMR_GRDG_ADM")
	public ModelAndView OMR_GRDG_ADM(Integer  limit,Integer pageNum, String searchcontent,String searchbstor,String searchmock,String searchgrade,String searchsub,String searchscore,HttpSession session) {
		ModelAndView mav = new ModelAndView();
//		//System.out.println("sub : " + searchsub);
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		List<Integer> sbstr = service.getbstorlist();
		if(searchbstor == null || searchbstor.trim().equals("")) {
			searchbstor = "(";
			for(Integer bst : sbstr) {
				searchbstor+=bst+",";
			}
			searchbstor=searchbstor.substring(0,searchbstor.length()-1);
			searchbstor += ")";
		}
		List<Integer> smock = service.getsmocklist();
		if(searchmock == null || searchmock.trim().equals("")) {
			searchmock = "(";
			for(Integer bst : smock) {
				searchmock+=bst+",";
			}
			searchmock=searchmock.substring(0,searchmock.length()-1);
			searchmock += ")";
			
		}
		List<String> sgrade = service.getsgragelist();
		if(searchgrade == null || searchgrade.trim().equals("")) {
			searchgrade = "(";
			for(String bst : sgrade) {
				searchgrade+="\'"+bst+"\',";
			}
			searchgrade=searchgrade.substring(0,searchgrade.length()-1);
			searchgrade += ")";
			
		}
		List<String> ssub = service.getssublist();
		if(searchsub == null || searchsub.trim().equals("")) {
			searchsub = "(";
			for(String bst : ssub) {
				searchsub+="\'"+bst+"\',";
			}
			searchsub+="\'선택과목없음\'";
			searchsub += ")";
			
		}
		if(searchscore == null || searchscore.trim().equals("")) {
			searchscore=3+"";
		}else if(searchscore.equals("NOT")) {
			searchscore = 1 +"";
		}else if(searchscore.equals("EQ")) {
			searchscore = 2 +"";
		}else{
			searchscore = 3 +"";
		}
//		//System.out.println("bst : " + searchbstor);
//		//System.out.println("mock : " + searchmock);
//		//System.out.println("grd : " + searchgrade);
//		//System.out.println("sub : " + searchsub);
//		//System.out.println("score : " + searchscore);
//		//System.out.println("li" + limit);
		if(limit == null) {
			limit=20;
		}
		
		MBR mbr = (MBR) session.getAttribute("MBR");
		Integer bstor_cd = mbr.getBSTOR_CD();
		Integer cmpncd = mbr.getCMPN_CD();
		int listcount = service.OMR_Exam_GRAD_Count(searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
		List<OMR_RECOG> OMR_EXAM_sub_List = service.OMRExamgradList(pageNum, limit, searchcontent,bstor_cd,cmpncd,searchbstor,searchmock,searchgrade,searchsub,searchscore);
		for (OMR_RECOG omlist : OMR_EXAM_sub_List ) {
//			//System.out.println(omlist);
		}
		////System.out.println(OMR_EXAM_sub_List);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		
		mav.addObject("searchbstor", searchbstor);
		mav.addObject("searchgrade", searchgrade);
		mav.addObject("searchsub", searchsub);
		mav.addObject("searchmock", searchmock);
		mav.addObject("searchscore", searchscore);
		
		mav.addObject("EXAMList", OMR_EXAM_sub_List);
		return mav;
	  }*/

	//0405_수정사항	
	@RequestMapping("recoglist")
	public ResponseEntity<JSONArray> grdgrecoglist(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String exam_cd = request.getParameter("examcd");
		String lsn_cd = request.getParameter("lsncd");
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
		String rsearchcontent = request.getParameter("rsearchcontent").trim();

		if(rsearchcontent.trim().equals("") || rsearchcontent == null) {
			rsearchcontent  = null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				rsearchcontent = rsearchcontent.replace(a+"", "");	
			}
		}
        MBR mbr = (MBR) session.getAttribute("MBR");
//        int  CMPN_CD = mbr.getCMPN_CD();
//		int  MBR_NO = mbr.getMBR_NO();		   
//		int  BSTOR_CD = mbr.getBSTOR_CD();
		
		JSONArray jsontot = new JSONArray();
		
		List<OMR_GRDG> list;
		try {
//			System.out.println(exam_cd+","+lsn_cd+","+BSTOR_CD+","+ rsearchcontent);
			List<OMR_GRDG> stdnolist = service.stdnolist(exam_cd,lsn_cd,BSTOR_CD);
//			System.out.println("stdno : " +stdnolist.size());
			String freq = service.freq(exam_cd,BSTOR_CD,stdnolist.get(0).getOMR_MST_CD());
			list = service.getfullgradlist(exam_cd,lsn_cd,BSTOR_CD,rsearchcontent);
			Integer stdsize = stdnolist.size();
			Integer gradsize = list.size();
			Integer pre =  0;
			int[] grplsn = new int[stdsize];
			for(int quei=0;quei<stdsize;quei++){
				if(stdnolist.get(quei).getOMR_MST_CD() == 6 || stdnolist.get(quei).getOMR_MST_CD() == 16 || stdnolist.get(quei).getOMR_MST_CD() == 3){
					grplsn[quei]=45;
				}else if(stdnolist.get(quei).getOMR_MST_CD() == 7){
					grplsn[quei]=30;
				}else{
					grplsn[quei]=20;
				}
			}
			for(int ss=0;ss<stdsize;ss++) {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();

				int nameerr = 0;
				int noerr = 0;
				int sexerr = 0;
				int birtherr = 0;

				if (list.size() != 0) {
					if (list.get(pre).getEXMN_NO() == null || list.get(pre).getEXMN_NO().equals("-1")) {
						jsonObject.put("EXMN_NO", "□□□□□□");
						noerr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getEXMN_NO();
						a = a.replace("-1", "□");
						jsonObject.put("EXMN_NO", a);
						if (!a.equals(list.get(pre).getEXMN_NO())) {
							noerr++;
						}
					}
					if (list.get(pre).getSTDN_NM() == null || list.get(pre).getSTDN_NM().equals("-1") || list.get(pre).getSTDN_NM().trim().equals("")) {
						jsonObject.put("STDN_NM", "□");
						nameerr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getSTDN_NM();
						a = a.replace("-1", "□");
						jsonObject.put("STDN_NM", a);
						if (!a.equals(list.get(pre).getSTDN_NM())) {
							nameerr++;
							//System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
					jsonObject.put("LSN_CD", list.get(0).getLSN_CD());
					if (list.get(pre).getSEX() == null || list.get(pre).getSEX().equals("-1")) {
						jsonObject.put("SEX", "□");
						sexerr++;

					} else {
						jsonObject.put("SEX", list.get(pre).getSEX());
					}
					if (list.get(pre).getBTHDAY() == null || list.get(pre).getBTHDAY().equals("-1")) {
						jsonObject.put("BIRTH", "□");
						birtherr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getBTHDAY();
						a = a.replace("-1", "□");
						jsonObject.put("BIRTH", a);
						if (!a.equals(list.get(pre).getBTHDAY())) {
							birtherr++;
//							//System.out.println("a : " + glist.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
//					System.out.print(list.get(0).getLSN_SEQ());
					jsonObject.put("OMR_MST_CD", list.get(pre).getOMR_MST_CD());
					jsonObject.put("LSN_SEQ", list.get(pre).getLSN_SEQ());
					jsonObject.put("TOT_SC", list.get(pre).getTOT_SC());
					jsonObject.put("OMR_IMG", list.get(pre).getOMR_IMG());
					jsonObject.put("OMR_KEY", list.get(pre).getOMR_KEY());
					////System.out.println(list.get(0).getOMR_IMG());
//					//System.out.println("obj : "+jsonObject);
					int err = 0;
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						if (list.get(gr).getERR_YN().equals("Y")) {
							err++;
						}
					}
//					for (OMR_GRDG grad : list) {
//						if (grad.getERR_YN().equals("Y")) {
//							err++;
//						}
//					}
					String sch_no = list.get(pre).getEXMN_NO().substring(0, 5);
					if (err != 0) {
						System.out.println("ere: "+ err);
						jsonObject.put("ERR_YN", "Y");
					} else if (!sch_no.equals(freq)) {
						jsonObject.put("ERR_YN", "Z");
					} else {
						jsonObject.put("ERR_YN", "N");
					}
					if (nameerr != 0) {
						System.out.println("nameerr : "+ nameerr );
						jsonObject.put("NAME_ERR_YN", "Y");
					} else {
//						System.out.println("noerr : "+ nameerr );
						jsonObject.put("NAME_ERR_YN", "N");
					}

					if (sexerr != 0) {
						jsonObject.put("SEX_ERR_YN", "Y");
					} else {
						jsonObject.put("SEX_ERR_YN", "N");
					}

					if (noerr != 0) {
						jsonObject.put("NO_ERR_YN", "Y");
					} else {
						jsonObject.put("NO_ERR_YN", "N");
					}

					if (birtherr != 0) {
						jsonObject.put("BIRTH_ERR_YN", "Y");
					} else {
						jsonObject.put("BIRTH_ERR_YN", "N");
					}
					jsonArray.add(jsonObject);
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						jsonObject = new JSONObject();
						jsonObject.put("QUEI_NO", list.get(gr).getQUEI_NO());
						if (list.get(gr).getMARK_NO() == null || list.get(gr).getMARK_NO().equals("-1")) {
							jsonObject.put("MARK_NO", ' ');
						} else {
							int minuscnt = 0;
							String a = list.get(gr).getMARK_NO();
							a = a.replace("-1", "□");
							jsonObject.put("MARK_NO", a);
						}
						jsonObject.put("ERR_YN", list.get(gr).getERR_YN());
						jsonObject.put("CRA_YN", list.get(gr).getCRA_YN());
						jsonArray.add(jsonObject);
					}
					jsontot.add(jsonArray);
				}
				pre += grplsn[ss];
			}
//			System.out.println(jsontot.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("OMR_DATA")
	  public ModelAndView OMR_DATA(String data, HttpServletRequest request, HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    ////System.out.println("D" + data);
	    String[] rlist = data.split(",");
	    String exam_cd = rlist[0];
	    String lsn_cd = rlist[1];
	    Integer BSTOR_CD = Integer.valueOf(Integer.parseInt(rlist[2]));
	    String exmnno = rlist[3];
	    String omrimg = rlist[4];
	    String rsearchcontent = null;
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    JSONArray jsonArray = new JSONArray();
	    try {
	      OMR_GRDG stdno = service.getstdno(omrimg, lsn_cd);
	      ////System.out.println(stdno);
	      List<OMR_GRDG> list = service.getgradlist(exam_cd, lsn_cd, BSTOR_CD.intValue(), stdno.getEXMN_NO(), Integer.valueOf(stdno.getOMR_KEY()),rsearchcontent);
	      JSONObject jsonObject = new JSONObject();
	      
	      
	     
	      int nameerr = 0;
			int noerr = 0;
			int sexerr = 0;
			int birtherr = 0;
			if(list.size() !=0){
				if(list.get(0).getEXMN_NO() == null || list.get(0).getEXMN_NO().equals("-1") ){
					jsonObject.put("EXMN_NO" , "□□□□□□");
					noerr++;
				}else {
					int minuscnt = 0;
					String a = list.get(0).getEXMN_NO();
					a=a.replace("-1", "□");
					jsonObject.put("EXMN_NO" , a);
					if(! a.equals(list.get(0).getEXMN_NO())) {
						noerr++;
						////System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
						
					}
				}
				
				if(list.get(0).getSTDN_NM() == null || list.get(0).getSTDN_NM().equals("-1") || list.get(0).getSTDN_NM().trim().equals("")){
					jsonObject.put("STDN_NM" , "□");	
					nameerr++;
				}else {
					int minuscnt = 0;
					String a = list.get(0).getSTDN_NM();
					a=a.replace("-1", "□");
					jsonObject.put("STDN_NM" , a);
					if(! a.equals(list.get(0).getSTDN_NM())) {
						nameerr++;
						//System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
					}
				}
				jsonObject.put("OMR_MST_CD" , list.get(0).getOMR_MST_CD());
				
			jsonObject.put("LSN_SEQ" , list.get(0).getLSN_SEQ());
			jsonObject.put("TOT_SC", Integer.valueOf(((OMR_GRDG)list.get(0)).getTOT_SC()));
	        
	        jsonObject.put("OMR_IMG", ((OMR_GRDG)list.get(0)).getOMR_IMG());
	        
	        if(list.get(0).getSEX().equals("-1")) {
	        	jsonObject.put("SEX" , "□");
				sexerr++;
			}else {
				jsonObject.put("SEX" , list.get(0).getSEX());
			}
	        
	        if(list.get(0).getBTHDAY() == null || list.get(0).getBTHDAY().equals("-1")){
				jsonObject.put("BIRTH" , "□");
				birtherr++;
			}else {
				int minuscnt = 0;
				String a = list.get(0).getBTHDAY();
				a=a.replace("-1", "□");
				jsonObject.put("BIRTH" , a);
				if(! a.equals(list.get(0).getBTHDAY())) {
					birtherr++;
//					////System.out.println("a : " + glist.get(0).getEXMN_NO() + "/n b : " + a);		
				}
			}
	        jsonObject.put("OMR_KEY", Integer.valueOf(((OMR_GRDG)list.get(0)).getOMR_KEY()));
	        ////System.out.println(jsonObject);
	        int err = 0;
	        for (OMR_GRDG grad : list) {
	          if (grad.getERR_YN().equals("Y"))
	            err++; 
	        } 
	        String sch_no = list.get(0).getEXMN_NO().substring(0,5);
			String freq = service.freq(exam_cd,BSTOR_CD,(Integer)list.get(0).getOMR_MST_CD());
			if(err != 0 ) {

				jsonObject.put("ERR_YN" , "Y");
			}else if( ! sch_no.equals(freq)) {
				jsonObject.put("ERR_YN" , "Z");
			}else {
				jsonObject.put("ERR_YN" , "N");
			}
			
			if(nameerr != 0) {
				jsonObject.put("NAME_ERR_YN" , "Y");
			}else {
				jsonObject.put("NAME_ERR_YN" , "N");
			}
			
			if(sexerr != 0) {
				jsonObject.put("SEX_ERR_YN" , "Y");
			}else {
				jsonObject.put("SEX_ERR_YN" , "N");
			}

			if(noerr != 0) {
				jsonObject.put("NO_ERR_YN" , "Y");
			}else {
				jsonObject.put("NO_ERR_YN" , "N");
			}
			
			if(birtherr != 0) {
				jsonObject.put("BIRTH_ERR_YN" , "Y");
			}else {
				jsonObject.put("BIRTH_ERR_YN" , "N");
			}

	        jsonArray.add(jsonObject);
	        for (OMR_GRDG grad : list) {
	          jsonObject = new JSONObject();
	          jsonObject.put("QUEI_NO", grad.getQUEI_NO());
	          if(grad.getMARK_NO()==null || grad.getMARK_NO().equals("-1")) {
					jsonObject.put("MARK_NO", ' ');	
				}else {
					int minuscnt = 0;
					String a = grad.getMARK_NO();
					a=a.replace("-1", "□");	
					jsonObject.put("MARK_NO", a );	
				}
	          jsonObject.put("ERR_YN", grad.getERR_YN());
	          jsonObject.put("CRA_YN", grad.getCRA_YN());
	          jsonArray.add(jsonObject);
	          ////System.out.println(jsonObject);
	        } 
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    mav.addObject("omrlist", jsonArray);
	    return mav;
	  }
	  
	  @RequestMapping({"recoglistnm"})
	  public ResponseEntity<JSONArray> recoglistnm(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    String exam_cd = request.getParameter("examcd");
	    String lsn_cd = request.getParameter("lsncd");
	    Integer BSTOR_CD = Integer.valueOf(Integer.parseInt(request.getParameter("bstorcd")));
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    String rsearchcontent = null;
	    JSONArray jsonArray = new JSONArray();
	    try {
	      List<OMR_GRDG> stdnolist = service.stdnolist(exam_cd, lsn_cd, BSTOR_CD.intValue());
	      for (OMR_GRDG stdno : stdnolist) {
	        ////System.out.println(stdno);
	        List<OMR_GRDG> list = service.getgradlist(exam_cd, lsn_cd, BSTOR_CD.intValue(), stdno.getEXMN_NO(), Integer.valueOf(stdno.getOMR_KEY()),rsearchcontent);
	        JSONObject jsonObject = new JSONObject();
	       
	        int nameerr = 0;
			int noerr = 0;
			int sexerr = 0;
			int birtherr = 0;
			if(list.size() !=0){
				if(list.get(0).getEXMN_NO() == null || list.get(0).getEXMN_NO().equals("-1")){
					jsonObject.put("EXMN_NO" , "□□□□□□");
					noerr++;
				}else {
					int minuscnt = 0;
					String a = list.get(0).getEXMN_NO();
					a=a.replace("-1", "□");
					jsonObject.put("EXMN_NO" , a);
					if(! a.equals(list.get(0).getEXMN_NO())) {
						noerr++;
						////System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
						
					}
				}
				
				if(list.get(0).getSTDN_NM() == null || list.get(0).getSTDN_NM().equals("-1") || list.get(0).getSTDN_NM().trim().equals("")){
					jsonObject.put("STDN_NM" , "□");	
					nameerr++;
				}else {
					int minuscnt = 0;
					String a = list.get(0).getSTDN_NM();
					a=a.replace("-1", "□");
					jsonObject.put("STDN_NM" , a);
					if(! a.equals(list.get(0).getSTDN_NM())) {
						nameerr++;
						//System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
					}
				}
			
	          jsonObject.put("OMR_IMG", ((OMR_GRDG)list.get(0)).getOMR_IMG());
	          ////System.out.println(jsonObject);
	          int err = 0;
	          for (OMR_GRDG grad : list) {
	            if (grad.getERR_YN().equals("Y"))
	              err++; 
	          }
				String sch_no = list.get(0).getEXMN_NO().substring(0,5);
				String freq = service.freq(exam_cd,BSTOR_CD,(Integer)list.get(0).getOMR_MST_CD());
				if(err != 0 ) {
					jsonObject.put("ERR_YN" , "Y");
				}else if( ! sch_no.equals(freq)) {
					jsonObject.put("ERR_YN" , "Z");
				}else {
					jsonObject.put("ERR_YN" , "N");
				}
				if(nameerr != 0) {
					jsonObject.put("NAME_ERR_YN" , "Y");
				}else {
					jsonObject.put("NAME_ERR_YN" , "N");
				}
				
				if(sexerr != 0) {
					jsonObject.put("SEX_ERR_YN" , "Y");
				}else {
					jsonObject.put("SEX_ERR_YN" , "N");
				}

				if(noerr != 0) {
					jsonObject.put("NO_ERR_YN" , "Y");
				}else {
					jsonObject.put("NO_ERR_YN" , "N");
				}
				
				if(birtherr != 0) {
					jsonObject.put("BIRTH_ERR_YN" , "Y");
				}else {
					jsonObject.put("BIRTH_ERR_YN" , "N");
				}


	          jsonArray.add(jsonObject);
	        } 
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	 @RequestMapping("OMR_CARD")
	  public ModelAndView OMR_CARD(String data, HttpServletRequest request, HttpSession session) {
	    ModelAndView mav = new ModelAndView();
	    ////System.out.println(data);
	    String[] rlist = data.split(",");
	    String examcd = rlist[0];
	    String lsncd = rlist[1];
	    Integer BSTOR_CD = Integer.valueOf(Integer.parseInt(rlist[2]));
	    String exmnno = rlist[3];
	    String omrimg = rlist[4];
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    ////System.out.println(omrimg);
	    int a = 0;
	    Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
	    String imgpath = omrimg;
	    String key = String.valueOf(imgpath.substring(0, imgpath.lastIndexOf("/"))) + "/rot_images/rot_" + imgpath.substring(imgpath.lastIndexOf("/") + 1);
	    S3Object s3o = s3Client.getObject("edu-ai", key);
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes = null;
	    try {
	      bytes = IOUtils.toByteArray(objectInputStream);
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    } 
	    File download = new File(String.valueOf(request.getServletContext().getRealPath("/")) + "images/" + imgpath.substring(imgpath.indexOf("/") + 1, imgpath.lastIndexOf("/")));
	    File[] deletefile = download.listFiles();
	    if (deletefile != null) {
	      byte b;
	      int i;
	      File[] arrayOfFile;
	      for (i = (arrayOfFile = deletefile).length, b = 0; b < i; ) {
	        File a1 = arrayOfFile[b];
	        a1.delete();
	        b++;
	      } 
	    } 
	    String jsppath = "images/" + imgpath.substring(imgpath.indexOf("/") + 1);
	    if (!download.exists())
	      download.mkdirs(); 
	    try {
	      FileOutputStream lFileOutputStream = new FileOutputStream(download + "/" + imgpath.substring(imgpath.lastIndexOf("/")));
	      lFileOutputStream.write(bytes);
	      lFileOutputStream.close();
	    } catch (Throwable e) {
	      e.printStackTrace();
	    } 
	    mav.addObject("jspimg", jsppath);
	    return mav;
	  }
	
	@RequestMapping("omrs3download")
	public ResponseEntity<JSONArray> omrs3download(HttpServletRequest request,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String omrimg = request.getParameter("omrimg");

//		System.out.println(omrimg);

//		----------
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		String exam_no = request.getParameter("EXAMNO");

		String imgpath = omrimg;
		String key = imgpath.substring(0,imgpath.lastIndexOf("/"))+"/rot_images/rot_"+imgpath.substring(imgpath.lastIndexOf("/")+1);
		S3Object s3o = null;
		try {
			s3o = s3Client.getObject("edu-ai", key);
		}catch (Exception e1){
			System.out.println("rot 없음");
			key = imgpath.substring(0,imgpath.lastIndexOf("/"))+"/rot_images/"+imgpath.substring(imgpath.lastIndexOf("/")+1);
			try {
				s3o = s3Client.getObject("edu-ai", key);
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
	    
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/"+imgpath.substring(imgpath.indexOf("/")+1,imgpath.lastIndexOf("/")));
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}
		String jsppath ="images/"+imgpath.substring(imgpath.indexOf("/")+1);
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgpath.substring(imgpath.lastIndexOf("/")));
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
	    jsonObject.put("jspimg", jsppath);
	    jsonArray.add(jsonObject);
	    return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("scoring")
	public ResponseEntity<JSONArray> scoring(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		Integer exam_cd = Integer.parseInt(request.getParameter("examcd"));
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd")); 
		Integer lsn_cd = Integer.parseInt(request.getParameter("lsncd"));
		String rsearchcontent = request.getParameter("rsearchcontent").trim();
		
		for(int idx : UNIC) {
			char a = (char)idx;
			rsearchcontent = rsearchcontent.replace(a+"", "");	
		}
        
		MBR mbr = (MBR) session.getAttribute("MBR");
        Integer  CMPN_CD = mbr.getCMPN_CD();
        Integer  MBR_NO = mbr.getMBR_NO();		   
        
		
		JSONArray jsontot = new JSONArray();
		
		List<OMR_GRDG> list;
		


		try {
			service.scoring(CMPN_CD,BSTOR_CD,exam_cd,lsn_cd,MBR_NO);
			Thread.sleep(3000);
//			System.out.println(exam_cd+","+lsn_cd+","+BSTOR_CD+","+ rsearchcontent);
			List<OMR_GRDG> stdnolist = service.stdnolist(exam_cd.toString(),lsn_cd.toString(),BSTOR_CD);
//			System.out.println("stdno : " +stdnolist.size());
			String freq = service.freq(exam_cd.toString(),BSTOR_CD,stdnolist.get(0).getOMR_MST_CD());
			list = service.getfullgradlist(exam_cd.toString(),lsn_cd.toString(),BSTOR_CD,rsearchcontent);
			Integer stdsize = stdnolist.size();
			Integer gradsize = list.size();
			Integer pre =  0;
			int[] grplsn = new int[stdsize];
			for(int quei=0;quei<stdsize;quei++){
				if(stdnolist.get(quei).getOMR_MST_CD() == 6 || stdnolist.get(quei).getOMR_MST_CD() == 16 || stdnolist.get(quei).getOMR_MST_CD() == 3){
					grplsn[quei]=45;
				}else if(stdnolist.get(quei).getOMR_MST_CD() == 7){
					grplsn[quei]=30;
				}else{
					grplsn[quei]=20;
				}
			}
			for(int ss=0;ss<stdsize;ss++) {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();

				int nameerr = 0;
				int noerr = 0;
				int sexerr = 0;
				int birtherr = 0;

				if (list.size() != 0) {
					if (list.get(pre).getEXMN_NO() == null || list.get(pre).getEXMN_NO().equals("-1")) {
						jsonObject.put("EXMN_NO", "□□□□□□");
						noerr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getEXMN_NO();
						a = a.replace("-1", "□");
						jsonObject.put("EXMN_NO", a);
						if (!a.equals(list.get(pre).getEXMN_NO())) {
							noerr++;
						}
					}
					if (list.get(pre).getSTDN_NM() == null || list.get(pre).getSTDN_NM().equals("-1") || list.get(pre).getSTDN_NM().trim().equals("")) {
						jsonObject.put("STDN_NM", "□");
						nameerr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getSTDN_NM();
						a = a.replace("-1", "□");
						jsonObject.put("STDN_NM", a);
						if (!a.equals(list.get(pre).getSTDN_NM())) {
							nameerr++;
							//System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
					jsonObject.put("LSN_CD", list.get(0).getLSN_CD());
					if (list.get(pre).getSEX() == null || list.get(pre).getSEX().equals("-1")) {
						jsonObject.put("SEX", "□");
						sexerr++;

					} else {
						jsonObject.put("SEX", list.get(pre).getSEX());
					}
					if (list.get(pre).getBTHDAY() == null || list.get(pre).getBTHDAY().equals("-1")) {
						jsonObject.put("BIRTH", "□");
						birtherr++;
					} else {
						int minuscnt = 0;
						String a = list.get(pre).getBTHDAY();
						a = a.replace("-1", "□");
						jsonObject.put("BIRTH", a);
						if (!a.equals(list.get(pre).getBTHDAY())) {
							birtherr++;
//							//System.out.println("a : " + glist.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
//					System.out.print(list.get(0).getLSN_SEQ());
					jsonObject.put("OMR_MST_CD", list.get(pre).getOMR_MST_CD());
					jsonObject.put("LSN_SEQ", list.get(pre).getLSN_SEQ());
					jsonObject.put("TOT_SC", list.get(pre).getTOT_SC());
					jsonObject.put("OMR_IMG", list.get(pre).getOMR_IMG());
					jsonObject.put("OMR_KEY", list.get(pre).getOMR_KEY());
					////System.out.println(list.get(0).getOMR_IMG());
//					//System.out.println("obj : "+jsonObject);
					int err = 0;
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						if (list.get(gr).getERR_YN().equals("Y")) {
							err++;
						}
					}
//					for (OMR_GRDG grad : list) {
//						if (grad.getERR_YN().equals("Y")) {
//							err++;
//						}
//					}
					String sch_no = list.get(pre).getEXMN_NO().substring(0, 5);
					if (err != 0) {
						jsonObject.put("ERR_YN", "Y");
					} else if (!sch_no.equals(freq)) {
						jsonObject.put("ERR_YN", "Z");
					} else {
						jsonObject.put("ERR_YN", "N");
					}
					if (nameerr != 0) {
						jsonObject.put("NAME_ERR_YN", "Y");
					} else {
						jsonObject.put("NAME_ERR_YN", "N");
					}

					if (sexerr != 0) {
						jsonObject.put("SEX_ERR_YN", "Y");
					} else {
						jsonObject.put("SEX_ERR_YN", "N");
					}

					if (noerr != 0) {
						jsonObject.put("NO_ERR_YN", "Y");
					} else {
						jsonObject.put("NO_ERR_YN", "N");
					}

					if (birtherr != 0) {
						jsonObject.put("BIRTH_ERR_YN", "Y");
					} else {
						jsonObject.put("BIRTH_ERR_YN", "N");
					}
					jsonArray.add(jsonObject);
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						jsonObject = new JSONObject();
						jsonObject.put("QUEI_NO", list.get(gr).getQUEI_NO());
						if (list.get(gr).getMARK_NO() == null || list.get(gr).getMARK_NO().equals("-1")) {
							jsonObject.put("MARK_NO", ' ');
						} else {
							int minuscnt = 0;
							String a = list.get(gr).getMARK_NO();
							a = a.replace("-1", "□");
							jsonObject.put("MARK_NO", a);
						}
						jsonObject.put("ERR_YN", list.get(gr).getERR_YN());
						jsonObject.put("CRA_YN", list.get(gr).getCRA_YN());
						jsonArray.add(jsonObject);
					}
					jsontot.add(jsonArray);
				}
				pre += grplsn[ss];
			}
//			System.out.println(jsontot.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("gradsave")
	public ResponseEntity<JSONArray> gradsave(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String examcd = request.getParameter("examcd");
		String lsncd = request.getParameter("lsncd");
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
		MBR mbr =(MBR)session.getAttribute("MBR");
		String rsearchcontent = request.getParameter("rsearchcontent").trim();
		for(int idx : UNIC) {
			char a = (char)idx;
			rsearchcontent = rsearchcontent.replace(a+"", "");	
		}
		Integer mbrno = mbr.getMBR_NO();
		int idex = Integer.parseInt(request.getParameter("index"));
		List<List<String>> list = new ArrayList<List<String>>();
		for(int i = 0 ;i <idex; i++) {
			String key = "tdArr" + i;
			////System.out.println(key);
			String str =request.getParameter(key);
			////System.out.println(str);
			List<String> tdlist = Arrays.asList(str.split(","));
//			System.out.println(tdlist.toString());
			list.add(tdlist);
		}
		System.out.println(list.toString());


		List<OMR_GRDG> grdg = new ArrayList<OMR_GRDG>();
		List<List<String>> dblist = new ArrayList<List<String>>();
		try {

			List<OMR_GRDG> stdnolist = service.stdnolist(examcd,lsncd,BSTOR_CD);
			for(OMR_GRDG stdno : stdnolist) {
				grdg = service.getgradlist(examcd,lsncd,BSTOR_CD,stdno.getEXMN_NO(),stdno.getOMR_KEY(),rsearchcontent);
				List<String> dbrec = new ArrayList<String>();
				////System.out.println("Sdfsdf");
				dbrec.add(grdg.get(0).getOMR_KEY()+"");

				if(grdg.get(0).getEXMN_NO() == null || grdg.get(0).getEXMN_NO().equals("-1") ){
					dbrec.add("□□□□□□");
				}else {
					dbrec.add(grdg.get(0).getEXMN_NO());
				}
				if(grdg.get(0).getSEX() == null || grdg.get(0).getSEX().equals("-1")){
					dbrec.add("□");
				}else {
					dbrec.add(grdg.get(0).getSEX());
				}
				if(grdg.get(0).getBTHDAY() == null ||grdg.get(0).getBTHDAY().equals("-1")){
					dbrec.add("□");
				}else {
					dbrec.add(grdg.get(0).getBTHDAY());
				}
				if(grdg.get(0).getSTDN_NM() == null ||grdg.get(0).getSTDN_NM().equals("-1")){
					dbrec.add("□");
				}else {
					dbrec.add(grdg.get(0).getSTDN_NM());
				}
				dbrec.add(grdg.get(0).getTOT_SC()+"");
				for(OMR_GRDG grad : grdg) {
					if(grad.getMARK_NO() == null || grad.getMARK_NO().equals("-1")) {
						dbrec.add("");
					}else {
						dbrec.add(grad.getMARK_NO()+"");
					}

				}
				dblist.add(dbrec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 try {
			for(int k=0;k<list.size();k++) {
				for(int l=0;l<dblist.size();l++) {
					if(dblist.get(l).get(0).equals(list.get(k).get(0))) {
						for(int j = 6; j<list.get(k).size();j++) {
							if(! dblist.get(l).get(j).equals(list.get(k).get(j))) {
								String blankstr = list.get(k).get(j).replace("-1","");
////								String blankstr = list.get(k).get(j);
//								/*if(list.get(k).get(j).trim().equals("-1-1")) {
//									blankstr  = "-1";
//								}*/
								if(! blankstr.equals(" ") && ! blankstr.trim().equals("") && ! blankstr.equals("-1")) {
////									//System.out.println(dblist.get(l).get(0)+","+BSTOR_CD+","+examcd+","+lsncd+","+blankstr+","+mbrno+","+(j-5));
									service.updategrdgrec(dblist.get(l).get(0),BSTOR_CD,examcd,lsncd,blankstr,mbrno,j-5+"");
									service.updategrdgerr(dblist.get(l).get(0),BSTOR_CD,examcd,lsncd,"N",mbrno,j-5+"");
									////System.out.println("chg : " + dblist.get(l).get(0));
								}else if((blankstr.equals(" ") || blankstr.trim().equals("") || blankstr.equals("-1")) && ! dblist.get(l).get(j).equals(" ")) {
									service.updategrdgrec(dblist.get(l).get(0),BSTOR_CD,examcd,lsncd,"-1",mbrno,j-5+"");
									service.updategrdgerr(dblist.get(l).get(0),BSTOR_CD,examcd,lsncd,"Y",mbrno,j-5+"");
									////System.out.println("chg1 :" + dblist.get(l).get(0));
								}
							}
						}
						////System.out.println("js : "+list.get(k).get(1));

////						String temp1 = list.get(k).get(1).replace("-1","");
////						String temp2 = list.get(k).get(2).replace("-1","");
////						String temp3 = list.get(k).get(3).replace("-1","");
////						String temp4 = list.get(k).get(4).replace("-1","");
////
						String temp1 = list.get(k).get(1);
						String temp2 = list.get(k).get(2);
						String temp3 = list.get(k).get(3);
						String temp4 = list.get(k).get(4);
						System.out.println("list.get(k).get(1) : " +list.get(k).get(1));
						if(list.get(k).get(1).equals("□□")) {
							temp1 = "-1-1-1-1-1-1";
						}
						if(list.get(k).get(2).equals("□□")) {
							temp2 = "-1";
						}
						if(list.get(k).get(3).equals("□□")) {
							temp3 = "-1";
						}
						if(list.get(k).get(4).equals("□□")) {
							temp4 = "-1";
						}

						if(list.get(k).get(1).equals("-1-1")) {
							temp1 = "-1-1-1-1-1-1";
						}
						if(list.get(k).get(2).equals("-1-1")) {
							temp2 = "-1";
						}
						if(list.get(k).get(3).equals("-1-1")) {
							temp3 = "-1";
						}
						if(list.get(k).get(4).equals("-1-1")) {
							temp4 = "-1";
						}


						if(list.get(k).get(1).trim().equals("") || list.get(k).get(1).trim().equals("-1")) {
							temp1 = "□□□□□□";
						}
						if(list.get(k).get(2).trim().equals("") || list.get(k).get(2).trim().equals("-1")) {
							temp2 = "□";
						}
						if(list.get(k).get(3).trim().equals("") || list.get(k).get(3).trim().equals("-1")) {
							temp3 = "□";
						}
						if(list.get(k).get(4).trim().equals("") || list.get(k).get(4).trim().equals("-1")) {
							temp4 = "□";
						}
////						System.out.println("dblist : "+dblist.get(l).toString());
////						System.out.println("t1 : "+ temp1 +" db1 : "+dblist.get(l).get(1));
////						System.out.println("t2 : "+ temp2 +" db1 : "+dblist.get(l).get(2));
////						System.out.println("t3 : "+ temp3 +" db1 : "+dblist.get(l).get(3));
////						System.out.println("t4 : "+ temp4 +" db1 : "+dblist.get(l).get(4));

						if (!(dblist.get(l).get(1).equals(temp1)) || !(dblist.get(l).get(2).equals(temp2)) || !(dblist.get(l).get(3).equals(temp3)) || !(dblist.get(l).get(4).equals(temp4)) ) {
						    service.updategrdgstdinfo(dblist.get(l).get(0), BSTOR_CD, examcd, lsncd,temp1,temp2,mbrno,temp3,temp4);
						}

////						if (! dblist.get(l).get(1).equals("X") && (list.get(k).get(1).trim().equals("") || list.get(k).get(1).equals("-1") || list.get(k).get(1).trim().equals("X"))) {
////							service.updategrdgstdinfo(dblist.get(l).get(0), BSTOR_CD, examcd, lsncd, "-1", dblist.get(l).get(2), mbrno);
////						}else if (dblist.get(l).get(1).equals("X") && ! (list.get(k).get(1).trim().equals("") || list.get(k).get(1).equals("-1") || list.get(k).get(1).trim().equals("X"))) {
////							service.updategrdgstdinfo(dblist.get(l).get(0), BSTOR_CD, examcd, lsncd, list.get(k).get(1), dblist.get(l).get(2), mbrno);
////						}
////						if (! dblist.get(l).get(2).equals("X") && (list.get(k).get(2).trim().equals("") || list.get(k).get(2).equals("-1") || list.get(k).get(2).trim().equals("X"))) {
////							service.updategrdgstdinfo(dblist.get(l).get(0), BSTOR_CD, examcd, lsncd, "-1", , mbrno);
////						}else if (dblist.get(l).get(2).equals("X") && ! (list.get(k).get(2).trim().equals("") || list.get(k).get(2).equals("-1") || list.get(k).get(2).trim().equals("X"))) {
////							service.updategrdgstdinfo(dblist.get(l).get(0), BSTOR_CD, examcd, lsncd, list.get(k).get(1), dblist.get(l).get(2), mbrno);
////						}

					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}


		 JSONArray jsontot = new JSONArray();
			
			List<OMR_GRDG> glist;
		try {
//			System.out.println(exam_cd+","+lsn_cd+","+BSTOR_CD+","+ rsearchcontent);
			List<OMR_GRDG> stdnolist = service.stdnolist(examcd,lsncd,BSTOR_CD);
//			System.out.println("stdno : " +stdnolist.size());
			String freq = service.freq(examcd,BSTOR_CD,stdnolist.get(0).getOMR_MST_CD());
			glist = service.getfullgradlist(examcd,lsncd,BSTOR_CD,rsearchcontent);
			Integer stdsize = stdnolist.size();
			Integer gradsize = glist.size();
			Integer pre =  0;
			int[] grplsn = new int[stdsize];
			for(int quei=0;quei<stdsize;quei++){
				if(stdnolist.get(quei).getOMR_MST_CD() == 6 || stdnolist.get(quei).getOMR_MST_CD() == 16 || stdnolist.get(quei).getOMR_MST_CD() == 3){
					grplsn[quei]=45;
				}else if(stdnolist.get(quei).getOMR_MST_CD() == 7){
					grplsn[quei]=30;
				}else{
					grplsn[quei]=20;
				}
			}
			for(int ss=0;ss<stdsize;ss++) {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();

				int nameerr = 0;
				int noerr = 0;
				int sexerr = 0;
				int birtherr = 0;

				if (glist.size() != 0) {
					if (glist.get(pre).getEXMN_NO() == null || glist.get(pre).getEXMN_NO().equals("-1")) {
						jsonObject.put("EXMN_NO", "□□□□□□");
						noerr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getEXMN_NO();
						a = a.replace("-1", "□");
						jsonObject.put("EXMN_NO", a);
						if (!a.equals(glist.get(pre).getEXMN_NO())) {
							noerr++;
						}
					}
					if (glist.get(pre).getSTDN_NM() == null || glist.get(pre).getSTDN_NM().equals("-1") || glist.get(pre).getSTDN_NM().trim().equals("")) {
						jsonObject.put("STDN_NM", "□");
						nameerr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getSTDN_NM();
						a = a.replace("-1", "□");
						jsonObject.put("STDN_NM", a);
						if (!a.equals(glist.get(pre).getSTDN_NM())) {
							nameerr++;
							//System.out.println("a : " + glist.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
					jsonObject.put("LSN_CD", glist.get(0).getLSN_CD());
					if (glist.get(pre).getSEX() == null || glist.get(pre).getSEX().equals("-1")) {
						jsonObject.put("SEX", "□");
						sexerr++;

					} else {
						jsonObject.put("SEX", glist.get(pre).getSEX());
					}
					if (glist.get(pre).getBTHDAY() == null || glist.get(pre).getBTHDAY().equals("-1")) {
						jsonObject.put("BIRTH", "□");
						birtherr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getBTHDAY();
						a = a.replace("-1", "□");
						jsonObject.put("BIRTH", a);
						if (!a.equals(glist.get(pre).getBTHDAY())) {
							birtherr++;
//							//System.out.println("a : " + gglist.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
//					System.out.print(glist.get(0).getLSN_SEQ());
					jsonObject.put("OMR_MST_CD", glist.get(pre).getOMR_MST_CD());
					jsonObject.put("LSN_SEQ", glist.get(pre).getLSN_SEQ());
					jsonObject.put("TOT_SC", glist.get(pre).getTOT_SC());
					jsonObject.put("OMR_IMG", glist.get(pre).getOMR_IMG());
					jsonObject.put("OMR_KEY", glist.get(pre).getOMR_KEY());
					////System.out.println(glist.get(0).getOMR_IMG());
//					//System.out.println("obj : "+jsonObject);
					int err = 0;
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						if (glist.get(gr).getERR_YN().equals("Y")) {
							err++;
						}
					}
//					for (OMR_GRDG grad : glist) {
//						if (grad.getERR_YN().equals("Y")) {
//							err++;
//						}
//					}
					String sch_no = glist.get(pre).getEXMN_NO().substring(0, 5);
					if (err != 0) {
						jsonObject.put("ERR_YN", "Y");
					} else if (!sch_no.equals(freq)) {
						jsonObject.put("ERR_YN", "Z");
					} else {
						jsonObject.put("ERR_YN", "N");
					}
					if (nameerr != 0) {
						System.out.println("nameerr : "+ nameerr );
						jsonObject.put("NAME_ERR_YN", "Y");
					} else {
						jsonObject.put("NAME_ERR_YN", "N");
					}

					if (sexerr != 0) {
						jsonObject.put("SEX_ERR_YN", "Y");
					} else {
						jsonObject.put("SEX_ERR_YN", "N");
					}

					if (noerr != 0) {
						jsonObject.put("NO_ERR_YN", "Y");
					} else {
						jsonObject.put("NO_ERR_YN", "N");
					}

					if (birtherr != 0) {
						jsonObject.put("BIRTH_ERR_YN", "Y");
					} else {
						jsonObject.put("BIRTH_ERR_YN", "N");
					}
					jsonArray.add(jsonObject);
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						jsonObject = new JSONObject();
						jsonObject.put("QUEI_NO", glist.get(gr).getQUEI_NO());
						if (glist.get(gr).getMARK_NO() == null || glist.get(gr).getMARK_NO().equals("-1")) {
							jsonObject.put("MARK_NO", ' ');
						} else {
							int minuscnt = 0;
							String a = glist.get(gr).getMARK_NO();
							a = a.replace("-1", "□");
							jsonObject.put("MARK_NO", a);
						}
						jsonObject.put("ERR_YN", glist.get(gr).getERR_YN());
						jsonObject.put("CRA_YN", glist.get(gr).getCRA_YN());
						jsonArray.add(jsonObject);
					}
					jsontot.add(jsonArray);
				}
				pre += grplsn[ss];
			}
//			System.out.println(jsontot.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
			return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}
	
	
	@RequestMapping("SEL_LSN")
	public ModelAndView SEL_LSN(String bstorcd,String examcd,String lsncd,String keylist,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println(bstorcd+","+examcd+","+lsncd);
		String omrkey = "("+keylist+")";
		List<OMR_GRDG> glist = service.sellsnlist(bstorcd,examcd,lsncd,omrkey);
		mav.addObject("lsnlist",glist);
		return mav;
	}
	//updatenolsn
	@RequestMapping("updatenolsn")
	public ResponseEntity<JSONArray> updatenolsn(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String keylsnstr = request.getParameter("keylsnlist");
		MBR mbr =(MBR)session.getAttribute("MBR");
		Integer mbrno = mbr.getMBR_NO();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		
		List<String> keylsnlist = Arrays.asList(keylsnstr.split(","));
		for(String keylsn : keylsnlist) {
			List<String> keysplitlsn = Arrays.asList(keylsn.split("a"));
			////System.out.println("key : " +keysplitlsn.get(0)+"\n lsn : " + keysplitlsn.get(1));
//			//System.out.println(keysplitlsn.toString());
			String omrkey = keysplitlsn.get(0).toString();
			String lsncd = keysplitlsn.get(1).toString();
			try {
				OMR_RECOG beforeobj = service.getregobjt(omrkey);
				String blcd =beforeobj.getLSN_CD()+"";
				System.out.print(lsncd+" , "+blcd);
				
				if(lsncd.equals(blcd)) {
					System.out.print(lsncd+" , "+blcd);
					continue;
				}
				service.updatenolsn(omrkey,lsncd,mbrno);
				OMR_RECOG regobj = service.getregobjt(omrkey);
				service.updateavg(regobj,mbrno,blcd);
				jsonObject.put("msg", "변경완료");
				jsonArray.add(jsonObject);
			} catch (Exception e) {
				jsonObject.put("msg", omrkey+"에러");
				jsonArray.add(jsonObject);
			}
		}
		return new ResponseEntity<JSONArray>(jsonArray,responseHeaders,HttpStatus.CREATED); 
	}
	@RequestMapping("transdata")
	public ResponseEntity<JSONObject> transdata(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String examcd = request.getParameter("examcd");
		String lsncd = request.getParameter("lsncd");
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
		MBR mbr =(MBR)session.getAttribute("MBR");
	
		Integer mbrno = mbr.getMBR_NO();
		int idex = Integer.parseInt(request.getParameter("index"));
		List<List<String>> list = new ArrayList<List<String>>();
		////System.out.println(idex);
		for(int i = 0 ;i <idex; i++) {
			List<String> tdlist = new ArrayList<String>();
			String key = "tdArr" + i;
			////System.out.println(key);
			String str =request.getParameter(key);
			////System.out.println(str);
			List<String> checklist = Arrays.asList(str.split(","));
			
			list.add(checklist);
		}
		String json = "{\"EXAM_CD\":"+examcd + ",\"LSN_CD\":" + lsncd + ",\"DATA\":[";
		for(List<String> td : list) {
			json += "{\"EXMN_NO\" : "+td.get(1)+",\"STDN_NM\":"+td.get(2)+",\"TOT_SC\":"+td.get(3)+",";
			for(int a = 4; a<td.size(); a++) {
				json+="\""+(a-3)+"번\":"+td.get(a)+",";
			}
			json += "}";
		}
		json+="]}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("senddata", json);
		return new ResponseEntity<JSONObject>(jsonObject,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("deletedata")
	public ResponseEntity<JSONArray> deletedata(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		String examcd = request.getParameter("examcd");
		String lsncd = request.getParameter("lsncd");
		Integer BSTOR_CD = Integer.parseInt(request.getParameter("bstorcd"));
		MBR mbr =(MBR)session.getAttribute("MBR");
		String rsearchcontent = request.getParameter("rsearchcontent").trim();
		for(int idx : UNIC) {
			char a = (char)idx;
			rsearchcontent = rsearchcontent.replace(a+"", "");	
		}
		Integer mbrno = mbr.getMBR_NO();
		int idex = Integer.parseInt(request.getParameter("index"));
		String str =request.getParameter("stdArr");
		List<String> stdlist = Arrays.asList(str.split(","));
		////System.out.println(stdlist);
		for(String OMR_KEY : stdlist) {
			service.deletegrdg(OMR_KEY,BSTOR_CD,examcd,lsncd,mbrno);	
		}
		if(lsncd.equals("-1") || lsncd.equals("-2")) {
			List<Integer> omrlist = service.getomrcdnolsn(BSTOR_CD.toString(), examcd);
			for(Integer omrcd : omrlist) {
				Integer delnocnt = service.reupdsttus(BSTOR_CD.toString(),examcd,omrcd);
				if(delnocnt == 0) {
					service.update_updsttus(BSTOR_CD.toString(),examcd,omrcd);
				}
			}
		}else {
			Integer omrmstcd = service.getomrcdbylsn(lsncd, examcd);
			Integer delnocnt = service.reupdsttus(BSTOR_CD.toString(),examcd,omrmstcd);
			if(delnocnt == 0) {
				service.update_updsttus(BSTOR_CD.toString(),examcd,omrmstcd);
			}
		}
		
		 JSONArray jsontot = new JSONArray();
			
			List<OMR_GRDG> glist;
		try {
//			System.out.println(exam_cd+","+lsn_cd+","+BSTOR_CD+","+ rsearchcontent);
			List<OMR_GRDG> stdnolist = service.stdnolist(examcd,lsncd,BSTOR_CD);
//			System.out.println("stdno : " +stdnolist.size());
			String freq = service.freq(examcd,BSTOR_CD,stdnolist.get(0).getOMR_MST_CD());
			glist = service.getfullgradlist(examcd,lsncd,BSTOR_CD,rsearchcontent);
			Integer stdsize = stdnolist.size();
			Integer gradsize = glist.size();
			Integer pre =  0;
			int[] grplsn = new int[stdsize];
			for(int quei=0;quei<stdsize;quei++){
				if(stdnolist.get(quei).getOMR_MST_CD() == 6 || stdnolist.get(quei).getOMR_MST_CD() == 16 || stdnolist.get(quei).getOMR_MST_CD() == 3){
					grplsn[quei]=45;
				}else if(stdnolist.get(quei).getOMR_MST_CD() == 7){
					grplsn[quei]=30;
				}else{
					grplsn[quei]=20;
				}
			}
			for(int ss=0;ss<stdsize;ss++) {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();

				int nameerr = 0;
				int noerr = 0;
				int sexerr = 0;
				int birtherr = 0;

				if (glist.size() != 0) {
					if (glist.get(pre).getEXMN_NO() == null || glist.get(pre).getEXMN_NO().equals("-1")) {
						jsonObject.put("EXMN_NO", "□□□□□□");
						noerr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getEXMN_NO();
						a = a.replace("-1", "□");
						jsonObject.put("EXMN_NO", a);
						if (!a.equals(glist.get(pre).getEXMN_NO())) {
							noerr++;
						}
					}
					if (glist.get(pre).getSTDN_NM() == null || glist.get(pre).getSTDN_NM().equals("-1") || glist.get(pre).getSTDN_NM().trim().equals("")) {
						jsonObject.put("STDN_NM", "□");
						nameerr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getSTDN_NM();
						a = a.replace("-1", "□");
						jsonObject.put("STDN_NM", a);
						if (!a.equals(glist.get(pre).getSTDN_NM())) {
							nameerr++;
							//System.out.println("a : " + list.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
					jsonObject.put("LSN_CD", glist.get(0).getLSN_CD());
					if (glist.get(pre).getSEX() == null || glist.get(pre).getSEX().equals("-1")) {
						jsonObject.put("SEX", "□");
						sexerr++;

					} else {
						jsonObject.put("SEX", glist.get(pre).getSEX());
					}
					if (glist.get(pre).getBTHDAY() == null || glist.get(pre).getBTHDAY().equals("-1")) {
						jsonObject.put("BIRTH", "□");
						birtherr++;
					} else {
						int minuscnt = 0;
						String a = glist.get(pre).getBTHDAY();
						a = a.replace("-1", "□");
						jsonObject.put("BIRTH", a);
						if (!a.equals(glist.get(pre).getBTHDAY())) {
							birtherr++;
//							//System.out.println("a : " + glist.get(0).getEXMN_NO() + "/n b : " + a);
						}
					}
//					System.out.print(list.get(0).getLSN_SEQ());
					jsonObject.put("OMR_MST_CD", glist.get(pre).getOMR_MST_CD());
					jsonObject.put("LSN_SEQ", glist.get(pre).getLSN_SEQ());
					jsonObject.put("TOT_SC", glist.get(pre).getTOT_SC());
					jsonObject.put("OMR_IMG", glist.get(pre).getOMR_IMG());
					jsonObject.put("OMR_KEY", glist.get(pre).getOMR_KEY());
					////System.out.println(list.get(0).getOMR_IMG());
//					//System.out.println("obj : "+jsonObject);
					int err = 0;
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						if (glist.get(gr).getERR_YN().equals("Y")) {
							err++;
						}
					}
//					for (OMR_GRDG grad : glist) {
//						if (grad.getERR_YN().equals("Y")) {
//							err++;
//						}
//					}
					String sch_no = glist.get(pre).getEXMN_NO().substring(0, 5);
					if (err != 0) {
						jsonObject.put("ERR_YN", "Y");
					} else if (!sch_no.equals(freq)) {
						jsonObject.put("ERR_YN", "Z");
					} else {
						jsonObject.put("ERR_YN", "N");
					}
					if (nameerr != 0) {
						jsonObject.put("NAME_ERR_YN", "Y");
					} else {
						jsonObject.put("NAME_ERR_YN", "N");
					}

					if (sexerr != 0) {
						jsonObject.put("SEX_ERR_YN", "Y");
					} else {
						jsonObject.put("SEX_ERR_YN", "N");
					}

					if (noerr != 0) {
						jsonObject.put("NO_ERR_YN", "Y");
					} else {
						jsonObject.put("NO_ERR_YN", "N");
					}

					if (birtherr != 0) {
						jsonObject.put("BIRTH_ERR_YN", "Y");
					} else {
						jsonObject.put("BIRTH_ERR_YN", "N");
					}
					jsonArray.add(jsonObject);
					for (int gr = pre; gr < pre + grplsn[ss]; gr++) {
						jsonObject = new JSONObject();
						jsonObject.put("QUEI_NO", glist.get(gr).getQUEI_NO());
						if (glist.get(gr).getMARK_NO() == null || glist.get(gr).getMARK_NO().equals("-1")) {
							jsonObject.put("MARK_NO", ' ');
						} else {
							int minuscnt = 0;
							String a = glist.get(gr).getMARK_NO();
							a = a.replace("-1", "□");
							jsonObject.put("MARK_NO", a);
						}
						jsonObject.put("ERR_YN", glist.get(gr).getERR_YN());
						jsonObject.put("CRA_YN", glist.get(gr).getCRA_YN());
						jsonArray.add(jsonObject);
					}
					jsontot.add(jsonArray);
				}
				pre += grplsn[ss];
			}
//			System.out.println(jsontot.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
			return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}
	@RequestMapping("deletegrdgrec")
	public ResponseEntity<JSONArray> deletegrdgrec(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		MBR mbr =(MBR)session.getAttribute("MBR");
		Integer mbrno = mbr.getMBR_NO();
		int idex = Integer.parseInt(request.getParameter("index"));
		String str =request.getParameter("stdArr");
		List<String> stdlist = Arrays.asList(str.split(","));
//		//System.out.println(stdlist.toString());
		
		for(String stdno : stdlist) {
//			//System.out.println("std"+stdno.toString());
			List<String> objkey = Arrays.asList(stdno.split("/"));
			String examcd = objkey.get(0);
			String BSTOR_CD = objkey.get(1);
			String lsncd = objkey.get(2);
//			//System.out.println("EXAM " +examcd+"BSTOR_CD " +BSTOR_CD+"lsncd " +lsncd );
			service.deletegrdgexam(BSTOR_CD,examcd,lsncd,mbrno);
			if(lsncd.equals("-1")) {
				System.out.println("-1");
				List<Integer> nolsnomrcd = service.getomrcdnolsn(BSTOR_CD,examcd);
				for(Integer omrcd:nolsnomrcd) {
					Integer delnocnt = service.reupdsttus(BSTOR_CD,examcd,omrcd);
					if(delnocnt == 0) {
						System.out.println("0");
						service.update_updsttus(BSTOR_CD,examcd,omrcd);
					}
				}
			}else {
				System.out.println(lsncd+","+examcd+","+BSTOR_CD);
				Integer omrmstcd = service.getomrcdbylsn(lsncd, examcd);
				Integer delnocnt = service.reupdsttus(BSTOR_CD,examcd,omrmstcd);
				if(delnocnt == 0) {
					System.out.println("a");
					service.update_updsttus(BSTOR_CD,examcd,omrmstcd);
				}
			}
		}
		
		JSONArray jsontot = new JSONArray();
		return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}

	@RequestMapping("scoregrdgrec")
	public ResponseEntity<JSONArray> scoregrdgrec(HttpServletRequest request ,HttpServletResponse response,HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		MBR mbr =(MBR)session.getAttribute("MBR");
		Integer  CMPN_CD = mbr.getCMPN_CD();
		Integer  MBR_NO = mbr.getMBR_NO();
		int idex = Integer.parseInt(request.getParameter("index"));
		String str =request.getParameter("stdArr");
		List<String> stdlist = Arrays.asList(str.split(","));
		////System.out.println(stdlist.toString());

		for(String stdno : stdlist) {
			////System.out.println("std"+stdno.toString());
			List<String> objkey = Arrays.asList(stdno.split("/"));
			Integer examcd = Integer.parseInt(objkey.get(0));
			Integer BSTOR_CD = Integer.parseInt(objkey.get(1));
			Integer lsncd = Integer.parseInt(objkey.get(2));
			service.scoring(CMPN_CD,BSTOR_CD,examcd,lsncd,MBR_NO);
		}
		JSONArray jsontot = new JSONArray();
		return new ResponseEntity<JSONArray>(jsontot,responseHeaders,HttpStatus.CREATED);
	}

	@RequestMapping("SEND_DATA")
	public ModelAndView senddata(String data) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("DATA",data);
		return mav;
	}
	@RequestMapping("IMG_WIN")
	public ModelAndView imgwin(String data) {
		ModelAndView mav = new ModelAndView();
//	    System.out.println("ne : " + data);
	    mav.setViewName("OMR_CARD");
	    mav.addObject("jspimg", data);
	    return mav;
	}
	@RequestMapping({"report"})
	  public ResponseEntity<JSONArray> report(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InterruptedException {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    String repor = request.getParameter("repor");
	    String REPORT = request.getParameter("REPORT");
	    int idex = Integer.parseInt(request.getParameter("index"));
		String str =request.getParameter("stdArr");
//		String examlist = "("+str+")";
//		List<String> stdlist = Arrays.asList(str.split(","));
//		
//		for(String strd : stdlist) {
//			examlist += strd+",";
//		}
//		examlist=examlist.substring(0, examlist.length()-1);
//		//System.out.println("examlist" + examlist);
		Integer mbrno = Integer.valueOf(mbr.getMBR_NO());
	    JSONArray jsonArray = new JSONArray();
	    Properties ubuntuprop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
	      ubuntuprop.load(fis);
	    } catch (Exception e1) {
	      e1.printStackTrace();
	    }
	    String keyname = ubuntuprop.getProperty("privateKey");
	    String publicDNS = ubuntuprop.getProperty("host");
	    File authfile = new File(keyname);
	    String user = ubuntuprop.getProperty("user");
	    String host = publicDNS;
	    int port = Integer.parseInt(ubuntuprop.getProperty("port"));
	    String dl = ubuntuprop.getProperty("DEVLIVE");
	    Map<String, String> hashedMap = new HashedMap();
	    try {
	      JSch jsch = new JSch();
	      String privateKey = keyname;
	      jsch.addIdentity(privateKey);
	      Session jschsession = jsch.getSession(user, host, port);
	      jschsession.setConfig("StrictHostKeyChecking", "no");
	      jschsession.setConfig("GSSAPIAuthentication", "no");
	      jschsession.setServerAliveInterval(120000);
	      jschsession.setServerAliveCountMax(1000);
	      jschsession.setConfig("TCPKeepAlive", "yes");
	      jschsession.connect();
	      Channel channel = jschsession.openChannel("exec");
	      ChannelExec channelExec = (ChannelExec)channel;
	      ////System.out.println("==> Connected to" + host);
//	      String command1 = ubuntuprop.getProperty("command1");
//	      String command2 = ubuntuprop.getProperty("command2");X
//	      String command3 = ubuntuprop.getProperty("command3");
//	      String command4 = ubuntuprop.getProperty("command4");
	      String dlpath = REPORT;
	      if(dl.equals("DEV")) {
	    	  dlpath +="_DEV";
	      }else if(dl.equals("TS")) {
	    	  dlpath+="_TS";
	      }
	      String command = "sh /home/ubuntu/talend/";
	      if(dl.equals("DEV")) {
	    	command +="DEV/";
	      }else if (dl.equals("TS")) {
			command +="TS/";
		  }
	      command  += "OMR/OMR_" + dlpath + "/OMR_" + dlpath + "/OMR_" + dlpath + "_run.sh --context_param EXAM_CD="+str;
	      ////System.out.println("COMMAND");
	      channelExec.setCommand(command);
	      channelExec.connect();
	      ////System.out.println("==> Connected to" + host + "\n" + command);
	      String pid = ManagementFactory.getRuntimeMXBean().getName();
	      hashedMap.put("PID", pid.substring(0, pid.indexOf("@")));
	      hashedMap.put("STEP_NM", "01. JAVA");
	      hashedMap.put("JOB_NM", REPORT);
	      hashedMap.put("ARG_VAL", "");
	      hashedMap.put("LOG_MSG", "Tos " + command);
	      hashedMap.put("ST_END_DIV", "begin");
	      hashedMap.put("CMPLT_MSG", "success");
	      service.javalog((Map)hashedMap);
	    } catch (Exception e) {
	      e.printStackTrace();
	      hashedMap.put("LOG_MSG", "리포트 출력간 오류 발생");
	      hashedMap.put("ST_END_DIV", "end");
	      hashedMap.put("CMPLT_MSG", "fail");
	      service.javalog((Map)hashedMap);
	    } 
	    Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		String path = opath+"/properties/temp/OCR/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
	    String seq = REPORT.substring(REPORT.length() - 1);
	    ////System.out.println(seq);
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
	    String key = s3dlpath+"/omr/report/report" + seq + ".xlsx";
	    ////System.out.println(key);
	   
	    Thread.sleep(45000);
	    
	    S3Object s3o = s3Client.getObject("edu-ai", key);
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes = null;
	    try {
	      bytes = IOUtils.toByteArray(objectInputStream);
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    } 
	    File download = new File(String.valueOf(request.getServletContext().getRealPath("/")) + "images/report/" + repor);
	    File[] deletefile = download.listFiles();
	    if (deletefile != null) {
	      byte b;
	      int i;
	      File[] arrayOfFile;
	      for (i = (arrayOfFile = deletefile).length, b = 0; b < i; ) {
	        File a1 = arrayOfFile[b];
	        a1.delete();
	        b++;
	      } 
	    } 
	    String reportpath = "images/report/" + repor + "/" + repor + ".xlsx";
	    if (!download.exists())
	      download.mkdirs(); 
	    try {
	      FileOutputStream lFileOutputStream = new FileOutputStream(download + "/" + repor + ".xlsx");
	      lFileOutputStream.write(bytes);
	      lFileOutputStream.close();
	    } catch (Throwable e) {
	      e.printStackTrace();
	    } 
	    
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("RPATH", reportpath);
	    jsonObject.put("report", repor);
	    jsonArray.add(jsonObject);
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	}
	  
	@RequestMapping("report4")
	public ResponseEntity<JSONArray> report4(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InterruptedException {
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json; charset=utf-8");
	    MBR mbr = (MBR)session.getAttribute("MBR");
	    String REPORT = request.getParameter("REPORT");
	    String repor = request.getParameter("repor");
	    Integer mbrno = Integer.valueOf(mbr.getMBR_NO());
	    Integer bstorcd = Integer.valueOf(mbr.getBSTOR_CD());
	    String str =request.getParameter("stdArr");
		//String examlist = "("+str+")";

		System.out.println("exam : " + str + " \nbstor :"+bstorcd);

	    JSONArray jsonArray = new JSONArray();
	    Properties ubuntuprop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/OMRUpload.properties");
	      ubuntuprop.load(fis);
	    } catch (Exception e1) {
	      e1.printStackTrace();
	    } 
	    String keyname = ubuntuprop.getProperty("privateKey");
	    String publicDNS = ubuntuprop.getProperty("host");
	    File authfile = new File(keyname);
	    String user = ubuntuprop.getProperty("user");
	    String host = publicDNS;
	    System.out.println("h : "+host);
	    String dl =  ubuntuprop.getProperty("DEVLIVE");
	    int port = Integer.parseInt(ubuntuprop.getProperty("port"));
	    Map<String, String> hashedMap = new HashedMap();
	    try {
	      JSch jsch = new JSch();
	      String privateKey = keyname;
	      jsch.addIdentity(privateKey);
	      Session jschsession = jsch.getSession(user, host, port);
	      jschsession.setConfig("StrictHostKeyChecking", "no");
	      jschsession.setConfig("GSSAPIAuthentication", "no");
	      jschsession.setServerAliveInterval(120000);
	      jschsession.setServerAliveCountMax(1000);
	      jschsession.setConfig("TCPKeepAlive", "yes");
	      jschsession.connect();
	      Channel channel = jschsession.openChannel("exec");
	      ChannelExec channelExec = (ChannelExec)channel;
	      System.out.println("==> Connected to" + host);
//	      String command1 = ubuntuprop.getProperty("command1");
//	      String command2 = ubuntuprop.getProperty("command2");
//	      String command3 = ubuntuprop.getProperty("command3");
//	      String command4 = ubuntuprop.getProperty("command4");
	      String dlpath = REPORT;
	      if(dl.equals("DEV")) {
	    	  dlpath +="_DEV";
	      }else if(dl.equals("TS")) {
	    	  dlpath +="_TS";
	      }
	      
	      
	      String command = "sh /home/ubuntu/talend/";
	      if(dl.equals("DEV")) {
	    	command +="DEV/";
	      }else if(dl.equals("TS")) {
	    	  command +="TS/";  
	      }
	      
	      command  += "OMR/OMR_" + dlpath + "/OMR_" + dlpath + "/OMR_" + dlpath + "_run.sh --context_param BSTOR_CD=" + bstorcd +" --context_param EXAM_CD="+str;

	      System.out.println("COMMAND : " + command);
	      channelExec.setCommand(command);
	      channelExec.connect();
	      ////System.out.println("==> Connected to" + host + "\n" + command);
	      String pid = ManagementFactory.getRuntimeMXBean().getName();
	      hashedMap.put("PID", pid.substring(0, pid.indexOf("@")));
	      hashedMap.put("STEP_NM", "01. JAVA");
	      hashedMap.put("JOB_NM", REPORT);
	      hashedMap.put("ARG_VAL", "BSTOR_CD :" + bstorcd);
	      hashedMap.put("LOG_MSG", "Tos " + command);
	      hashedMap.put("ST_END_DIV", "begin");
	      hashedMap.put("CMPLT_MSG", "success");
	      service.javalog((Map)hashedMap);
	    } catch (Exception e) {
	      e.printStackTrace();
	      hashedMap.put("LOG_MSG", "tos 접속 실패");
	      hashedMap.put("ST_END_DIV", "end");
	      hashedMap.put("CMPLT_MSG", "fail");
	      service.javalog((Map)hashedMap);
	    } 
	    Properties prop = new Properties();
	    try {
	      FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
	      prop.load(fis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    String AccessKey = prop.getProperty("Access");
	    String SecretKey = prop.getProperty("Secret");
	    AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();

	    String seq = REPORT.substring(REPORT.length() - 1);
	    ////System.out.println(seq);
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}
	    String key = s3dlpath+"/omr/report/" + bstorcd + "_" + repor + ".xlsx";
	    
	    Thread.sleep(50000);
	    
	    S3Object s3o = s3Client.getObject("edu-ai", key);
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes = null;
	    try {
	      bytes = IOUtils.toByteArray(objectInputStream);
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    } 
	    File download = new File(String.valueOf(request.getServletContext().getRealPath("/")) + "images/report/" + repor);
	    File[] deletefile = download.listFiles();
	    if (deletefile != null) {
	      byte b;
	      int i;
	      File[] arrayOfFile;
	      for (i = (arrayOfFile = deletefile).length, b = 0; b < i; ) {
	        File a1 = arrayOfFile[b];
	        a1.delete();
	        b++;
	      } 
	    } 
	    String reportpath = "images/report/" + repor + "/" + repor + ".xlsx";
	    if (!download.exists())
	      download.mkdirs(); 
	    try {
	      FileOutputStream lFileOutputStream = new FileOutputStream(download + "/" + repor + ".xlsx");
	      lFileOutputStream.write(bytes);
	      lFileOutputStream.close();
	    } catch (Throwable e) {
	      e.printStackTrace();
	    } 
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("RPATH", reportpath);
	    jsonObject.put("report", repor);
	    jsonArray.add(jsonObject);
	    return new ResponseEntity(jsonArray, responseHeaders, HttpStatus.CREATED);
	  }
	@RequestMapping("FILT_BSTOR")
	public ModelAndView FILT_BSTOR(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		if(limit == null || limit == 5) {
			limit = 40;
		}
		if(pageNum == null) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent= null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		
		List<BSTOR> blist = new ArrayList<BSTOR>();
		try {
			 blist = service.filtblist(limit,pageNum,searchcontent);
			 System.out.println(blist.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		//System.out.println(blist);
		mav.addObject("blist" , blist);
		int listcount = service.filtblistcnt(searchcontent);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		return mav;
	}
	@RequestMapping("FILT_MOCK")
	public ModelAndView FILT_MOCK(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		if(limit == null) {
			limit = 5;
		}
		if(pageNum == null) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent= null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		
		List<OMR_EXAM> examlist = new ArrayList<OMR_EXAM>();
		try {
			 examlist = service.filtEXAMlist(limit,pageNum,searchcontent);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		//System.out.println(examlist);
		mav.addObject("examlist" , examlist);
		int listcount = service.filtEXAMcnt(searchcontent);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		return mav;
	}
	@RequestMapping("FILT_GRDG")
	public ModelAndView FILT_GRDG(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		if(limit == null) {
			limit = 5;
		}
		if(pageNum == null) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent= null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		
		List<OMR_EXAM> glist = new ArrayList<OMR_EXAM>();
		try {
			glist = service.filtglist(limit,pageNum,searchcontent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("blist" , glist);
		int listcount = service.filtgcnt(searchcontent);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		return mav;
	}
	/*@RequestMapping("FILT_LSN")
	public ModelAndView FILT_LSN(String examcd,String lsncd,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<BSTOR> blist = new ArrayList<BSTOR>();
		try {
			 blist = service.filtblist();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(blist);
		mav.addObject("blist" , blist);
		return mav;
	}*/
	@RequestMapping("FILT_SUB")
	public ModelAndView FILT_SUB(Integer  limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		if(limit == null) {
			limit = 5;
		}
		if(pageNum == null) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent= null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		////System.out.println("l : "+ limit+" : p : "+ pageNum+" : s : "+searchcontent);
		
		List<OMR_RECOG> blist = new ArrayList<OMR_RECOG>();
		try {
			 blist = service.filtsubjlist(limit,pageNum,searchcontent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("blist" , blist);
		int listcount = service.filtsubjcnt(searchcontent);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		return mav;
	}
	
	
	@RequestMapping("imgdwld")
	public ModelAndView imgdwld(String omrkey,String reg,HttpServletRequest request,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		////System.out.println(omrkey+","+reg);
		String omrimg = service.getrecogobjtkey(omrkey);
		//---------------------------------------------------
		String imgpath = omrimg;
		
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//String path = opath+"/Users/uddon/Desktop/temp/";
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();
		
		String key = imgpath.substring(0,imgpath.lastIndexOf("/"))+"/rot_images/rot_"+imgpath.substring(imgpath.lastIndexOf("/")+1);
		S3Object s3o = s3Client.getObject("edu-ai", key);
	    
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File download = new File(request.getServletContext().getRealPath("/")+"images/"+imgpath.substring(imgpath.indexOf("/")+1,imgpath.lastIndexOf("/")));
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}
		String jsppath ="images/"+imgpath.substring(imgpath.indexOf("/")+1);
		if(!download.exists()) download.mkdirs();
	    try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgpath.substring(imgpath.lastIndexOf("/")));
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
		//---------------------------------------------------
		
		mav.setViewName("OMR_CARD");
	    mav.addObject("jspimg", jsppath);
		return mav;
	}
	//chkfailimg
	@RequestMapping("OMR_REPORT")
	public ModelAndView OMR_REPORT(Integer limit,Integer pageNum, String searchcontent,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {
			searchcontent=null;
		}else {
			for(int idx : UNIC) {
				char a = (char)idx;
				searchcontent = searchcontent.replace(a+"", "");	
			}
		}
		if(limit == null) {
			limit=5;
		}
		////System.out.println(limit+"\n"+pageNum+"\n"+searchcontent+"\n");
		MBR mbr = (MBR) session.getAttribute("MBR");
		int bstor_cd = mbr.getBSTOR_CD();
		String bstor_nm = service.getbstor(bstor_cd+"");
		int cmpncd = mbr.getCMPN_CD();
		int listcount = service.OMR_EXAM_Count(searchcontent);
		List<OMR_EXAM> OMR_EXAM_sub_List = service.OMRExamList(pageNum, limit, searchcontent);
		int maxpage = (int) ((double) listcount / limit + 0.95);
		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		int endpage = startpage + 9;
		if (endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (limit * (pageNum - 1));
		////System.out.println("tel:"+mbr.getTEL_NO());
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardno", boardno);
		mav.addObject("bstor_cd", bstor_cd);
	    mav.addObject("bstor_nm", bstor_nm);
		mav.addObject("EXAMList", OMR_EXAM_sub_List);
		return mav;
	}
//	
//	@RequestMapping("UPDATE_TOS_CALL")
//	public ModelAndView utc () {
//		ModelAndView mav = new ModelAndView();
//		Integer cmpncd = 1;
//		try {
//			List<Integer> blist = service.getbstorlist();
//			for(Integer excd= 1;excd<15;excd++) {
//				for(Integer omrcd = 1 ;omrcd<=9;omrcd ++) {
//					for(Integer bstorcd : blist) {
//						List<String> pidlist = service.pid3(excd, cmpncd, bstorcd, omrcd, "OMR");
//						
//						if(pidlist.size() != 0) {
//							String arg_val ="EXAM_CD : "+excd+",%, BSTOR_CD : "+bstorcd+",% OMR_MST_CD%"+omrcd;
//							for(String pid : pidlist) {
//								service.utc(excd,cmpncd,bstorcd,omrcd,pid,arg_val);
//							}
//						}
//					}
//				}
//			}
//			
//		} catch (Exception e) {
//			System.out.print("err , ");
//		}
//		return mav;
//	}
//	
//	@RequestMapping("ipprint")
//	public ModelAndView ipprint(HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView();
//		String ip = request.getRemoteAddr();
//		System.out.println("IP : " + ip);
//		return mav;
//	}

	//이미지 명 클릭시 --- 21.06.28 수정사항
	@RequestMapping("SEQ_IMG")
	public ResponseEntity<JSONObject> SEQ_IMG(HttpServletRequest request ,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");

		//세션정보 가져오기
		MBR mbr =(MBR)session.getAttribute("MBR");
        Integer  CMPN_CD = mbr.getCMPN_CD();
        Integer  MBR_NO = mbr.getMBR_NO();

        //ORGIN_FILE_NM 클릭시 CHG_FILE_NM(PK)을 받게됨 CHG_FILE_NM 가져오기
        Integer seq = Integer.parseInt(request.getParameter("SEQ"));

        //CHG_FILE_NM 해당 LOG_FILE_CHG_TXN 레코드 가져오기
		FILELOG flog = service.SeqToImg(seq);

		//필요한 레코드 값 가져오기
		int examcd = flog.getEXAM_CD();
		int cmpncd = flog.getCMPN_CD();
		int bstorcd = flog.getBSTOR_CD();
		int omrmstcd = flog.getMST_CD();

		//CHG_FILE_NM은 정수값이므로 JPG 확장자를 붙임 (업로드시 JPG형태만 업로드 가능하게 해놓음)
		String imgname = seq+".jpg";

		//S3객체 생성 준비
	    Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (Exception e) {
			e.printStackTrace();
		}

		//키 가져오기
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		String path = opath+"/properties/temp/OCR/";

		//S3 객체생성
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();

		//DEV LIVE 구분
		String DEVLIVE = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(DEVLIVE.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(DEVLIVE.equals("TS")) {
			s3dlpath+="_ts";
		}

		//이미지 다운로드를 위한 S3경로(check_fail -> JOB_FAIL -> 기본경로 -> *업로드 완료 된 경우<개발용>)
		String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrmstcd+"/check_fail/"+imgname;
		S3Object s3o = new S3Object();
		try {
			 s3o = s3Client.getObject("edu-ai", key);
		} catch (AmazonS3Exception aws) {
			key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrmstcd+"/job_fail/"+imgname;
			System.out.println(key);
			try {
				s3o = s3Client.getObject("edu-ai", key);	
			} catch (AmazonS3Exception aws2) {
				key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrmstcd+"/"+imgname;
				System.out.println(key);
				try {
					s3o = s3Client.getObject("edu-ai", key);	
				} catch (Exception aws3) {
					key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrmstcd+"/rot_images/rot_"+imgname;
					System.out.println(key);
					try {
						s3o = s3Client.getObject("edu-ai", key);	
					} catch (Exception aws4) {
						aws4.printStackTrace();
					}
				}
			}
		} 
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;

	    //이미지를 바이트 형태로 가져오기
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//로컬 절대 경로 설정하여 폴더 및 비어있는 파일객체 생성
		File download = new File(request.getServletContext().getRealPath("/")+"images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd);
		if(!download.exists()) download.mkdirs();

		//로컬경로에 파일 존재하면 삭제
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}

		//상대 경로 생성
		String jsppath ="images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+imgname;

		//바이트 형태의 이미지를 절대 경로에 작성하기
		try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgname);
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }

		//JSP로 상대경로 전달
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("jspimg", jsppath);
				
		return new ResponseEntity<>(jsonObject,responseHeaders,HttpStatus.CREATED);
	}

	//재업로드 파일 목록 띄우기 --2021.06.28 코드리뷰
	@RequestMapping("REUPLD_VIEW")
	public ModelAndView REUPLD_VIEW(String seqArr,HttpSession session){
		ModelAndView mav =  new ModelAndView();
		List<FILELOG> flog = new ArrayList<FILELOG>();

		//CHG_FILE_NM 리스트 처리
		if(! seqArr.trim().equals("")) {
			String seqlist = "("+seqArr+")";

			// IN 조건절을 통해 해당되는 LOG_FILE_CHG_TXN 리스트 가져오기
			flog = service.SeqlistToFlist(seqlist);			
		}

		//JSP로 리스트 전달
		mav.addObject("FLOGLIST",flog);
		return mav;
	}

	//화면에서 이미지 명 클릭시 이미지 보여주기 --2021.06.28 코드리뷰
	@RequestMapping("REUPLD_IMG")
	public ResponseEntity<JSONObject> REUPLD_IMG(HttpServletRequest request ,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");

		//파라미터 가져오기
		String filename = request.getParameter("filename");
		String examcd = request.getParameter("examcd");
		String omrcd = request.getParameter("omrcd");
		Integer bstorcd = Integer.parseInt(request.getParameter("bstorcd"));
		String imgname = filename+".jpg";

		//세션정보 가져오기
		MBR mbr = (MBR)session.getAttribute("MBR");
		int cmpncd = mbr.getCMPN_CD();
		
		//S3객체 준비
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(opath+"/properties/AIDOS3.properties");
			prop.load(fis);
		}catch (IOException io) {
			io.printStackTrace();
		}

		//S3 접속키 가져오기
		String AccessKey = prop.getProperty("Access");
		String SecretKey = prop.getProperty("Secret");
		AWSCredentials credentials = new BasicAWSCredentials(AccessKey, SecretKey);

		//S3객체 생성
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	            .withRegion(Regions.AP_NORTHEAST_2)
	            .withCredentials(new AWSStaticCredentialsProvider(credentials))
	            .build();

		//DEV LIVE 구분
		String dl = prop.getProperty("DEVLIVE");
		String s3dlpath = "aido";
		if(dl.equals("DEV")) {
			s3dlpath+="_dev";
		}else if(dl.equals("TS")) {
			s3dlpath+="_ts";
		}

		//이미지 다운로드를 위한 S3경로(check_fail -> JOB_FAIL -> 기본경로 -> *회전처리 완료 경로<개발용>)
		String key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/check_fail/"+imgname;
		S3Object s3o = new S3Object();
		try {
			 s3o = s3Client.getObject("edu-ai", key);
		} catch (AmazonS3Exception aws) {
			key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/job_fail/"+imgname;
			try {
				s3o = s3Client.getObject("edu-ai", key);	
			} catch (AmazonS3Exception aws2) {
				key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+imgname;
				try {
					s3o = s3Client.getObject("edu-ai", key);	
				} catch (Exception aws3) {
					key = s3dlpath+"/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/rot_images/rot_"+imgname;
					try {
						s3o = s3Client.getObject("edu-ai", key);	
					} catch (Exception aws4) {
						aws4.printStackTrace();
					}
				}
			}
		}

		//이미지를 BYTE 형태로 가져오기
	    S3ObjectInputStream objectInputStream = s3o.getObjectContent();
	    byte[] bytes=null;
		try {
			bytes = IOUtils.toByteArray(objectInputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//로컬 절대 경로 설정하여 폴더 및 비어있는 파일객체 생성
		File download = new File(request.getServletContext().getRealPath("/")+"images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd);
		if(!download.exists()) download.mkdirs();

		//로컬경로에 파일 존재하면 삭제
		File[] deletefile = download.listFiles();
		if(deletefile != null) {
		   for(File a1 : deletefile) {
			  a1.delete();
		   }
		}

		//상대 경로 생성
		String jsppath ="images/omr/"+examcd+"/"+cmpncd+"/"+bstorcd+"/"+omrcd+"/"+imgname;

		//바이트 형태의 이미지를 절대 경로에 작성하기
		try{
	        FileOutputStream lFileOutputStream = new FileOutputStream(download+"/"+imgname);
	        lFileOutputStream.write(bytes);
	        lFileOutputStream.close();
	    }catch(Throwable e){
	        e.printStackTrace();
	    }

		//JSP로 상대경로 전달
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("jspimg", jsppath);
	    return new ResponseEntity<>(jsonObject,responseHeaders,HttpStatus.CREATED);
	}

	//최종 재업로드 처리 --2021.06.28 코드리뷰
	@RequestMapping("REUPLD_STTUS")
	public ResponseEntity<JSONObject> REUPLD_STTUS(HttpServletRequest request ,HttpServletResponse response,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONObject jsonObject = new JSONObject();

		//재업로드 대상 CHG_FILE_NM 리스트 가져오기
		String seqArr = request.getParameter("seqArr");
		if(! seqArr.trim().equals("")) {
			String REUPLD_LIST = "("+seqArr+")";
			try {
				//IN 조건문을 통해 해당되는 REUPLD_YN 컬럼 상태값 변경
				service.REUPLD_STTUS(REUPLD_LIST);
				jsonObject.put("msg", "재업로드 처리 되었습니다");
			} catch (Exception e) {
				jsonObject.put("msg", "오류발생");
			}
		}
		return new ResponseEntity<>(jsonObject,responseHeaders,HttpStatus.CREATED);
	}

	@RequestMapping("REUPLD_MLIST")
	public ResponseEntity<JSONArray> REUPLD_MLIST(HttpServletRequest request,HttpSession session){
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		String examcd = request.getParameter("examcd");
		Integer bstorcd = Integer.parseInt(request.getParameter("bstorcd"));
		String mstcd = request.getParameter("mstcd");
		String searchcontent = request.getParameter("rsearchcontent");

		MBR mbr = (MBR)session.getAttribute("MBR");
		Integer CMPN_CD = mbr.getCMPN_CD();
		try {
			List<FILELOG> flist = service.floglist(examcd,CMPN_CD,bstorcd,mstcd,"OMR","Y",searchcontent);
			System.out.println("FL : "+flist.toString());
			for(FILELOG flog : flist){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("CHG_FILE_NM",flog.getCHG_FILE_NM());
				jsonObject.put("ORIGIN_FILE_NM",flog.getORIGIN_FILE_NM());
				jsonObject.put("EXAM_CD",flog.getEXAM_CD());
				jsonObject.put("MST_CD",flog.getMST_CD());
				jsonObject.put("BSTOR_CD",flog.getBSTOR_CD());
				jsonArray.add(jsonObject);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(jsonArray,responseHeaders,HttpStatus.CREATED);
	}
}

