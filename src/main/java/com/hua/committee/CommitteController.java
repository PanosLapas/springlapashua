package com.hua.committee;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hua.committee.dao.CommitteDAO;
import com.hua.committee.dao.CommitteFollowUpDAO;
import com.hua.committee.dao.DocumentDAO;
import com.hua.committee.dao.UserDAO;
import com.hua.committee.dao.UserRoleDAO;
import com.hua.committee.dao.MemberDAO;
import com.hua.committee.model.Committe;
import com.hua.committee.model.CommitteFollowUp;
import com.hua.committee.model.CommitteUI;
import com.hua.committee.model.Document;
import com.hua.committee.model.User;
import com.hua.committee.model.Member;
import com.hua.committee.model.UserRole;
import com.hua.committee.sc.RolesEnum;

@SuppressWarnings("deprecation")
@Controller
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)
@RequestMapping(value = "/committe")
public class CommitteController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int pageSize = 5;
	
	@Autowired
	@Qualifier("committeDAO")
	private CommitteDAO committeDAO;
	
	@Autowired
	@Qualifier("committeFollowUpDAO")
	private CommitteFollowUpDAO committeFollowUpDAO;
	
	@Autowired
	@Qualifier("documentDAO")
	private DocumentDAO documentDAO;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@Autowired
	@Qualifier("memberDAO")
	private MemberDAO memberDAO;
	
	@Autowired
	@Qualifier("userRoleDAO")
	private UserRoleDAO userRoleDAO;
	
	@RequestMapping(value="create", method = RequestMethod.GET)
 	public String create(Model model) {
 		model.addAttribute("title", "Create Committe");
 		Committe committe = new Committe();
 		model.addAttribute("committe", committe);
 		return "committe";
 		
 	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createCommitte(Model model, @ModelAttribute("committe") @Valid Committe committe,
			@RequestParam("files") MultipartFile[] files, BindingResult result) 
	{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String uname = auth.getName();
		User user = new User();
		
		user = userDAO.getByUsername(uname);
		
		committe.setUserId(user.getId());
		
		if (result.hasErrors()) {
			logger.info("error on result");
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
		        logger.info(error.getObjectName() + " - " + error.getDefaultMessage());
		    }
			return "committe";
		}

		if (committe.getId() == 0) {
			committe.setStatus((byte) 1);
			committe = committeDAO.save(committe);
			
			
			if(files != null && files.length > 0)
			{
				for (MultipartFile file : files)
				{
					if(!file.isEmpty())
					{
						Document doc = new Document();
						doc.setCommitteId(committe.getId());
						doc.setTitle(file.getOriginalFilename());
						doc.setType(file.getContentType());
						try {
							doc.setContent(file.getBytes());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						documentDAO.save(doc);	
					}
				}
			}
			//save follow up
			CommitteFollowUp history = new CommitteFollowUp();
			history.setCommitteId(committe.getId());
			history.setUserId(user.getId());
			history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " πρόσθεσε νέα επιτροπή με τίτλο : " + committe.getTitle());
			committeFollowUpDAO.save(history);
		} 
		else 
		{
			committeDAO.update(committe);
			
			if(files != null && files.length > 0)
			{
				for (MultipartFile file : files)
				{
					if(!file.isEmpty())
					{
						Document doc = new Document();
						doc.setCommitteId(committe.getId());
						doc.setTitle(file.getOriginalFilename());
						doc.setType(file.getContentType());
						try {
							doc.setContent(file.getBytes());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						documentDAO.save(doc);	
					}
				}
			}
			//save follow up
			CommitteFollowUp history = new CommitteFollowUp();
			history.setCommitteId(committe.getId());
			history.setUserId(user.getId());
			history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " τροποποίησε τα στοιχεία της επιτροπής : " + committe.getTitle());
			committeFollowUpDAO.save(history);
		}

		return "redirect:/committe/all";

	}
	
	@RequestMapping(value = "update_bymember", method = RequestMethod.POST)
	public String updateCommitte(Model model, @ModelAttribute("committe") @Valid Committe committe,
			@RequestParam("files") MultipartFile[] files, BindingResult result) 
	{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String uname = auth.getName();
		User user = new User();
		
		user = userDAO.getByUsername(uname);
		
		//committe.setUserId(user.getId());
		
		if (result.hasErrors()) {
			logger.info("error on result");
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
		        logger.info(error.getObjectName() + " - " + error.getDefaultMessage());
		    }
			return "committe_bymember";
		}
		
		committeDAO.update(committe);
		
		if(files != null && files.length > 0)
		{
			for (MultipartFile file : files)
			{
				if(!file.isEmpty())
				{
					Document doc = new Document();
					doc.setCommitteId(committe.getId());
					doc.setTitle(file.getOriginalFilename());
					doc.setType(file.getContentType());
					try {
						doc.setContent(file.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					documentDAO.save(doc);	
				}
			}
		}
		
		//save follow up
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committe.getId());
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " τροποποίησε τα στοιχεία της επιτροπής : " + committe.getTitle());
		committeFollowUpDAO.save(history);

		return "redirect:/committe/all_bymember";

	}
	
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public String committes(Model model,Authentication authentication) {
		
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		List<Committe> committesList= committeDAO.getAll(1,0);
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		model.addAttribute("committesList", committes);
		
		int total = committeDAO.getCount();
		int total_pages = 0;
		int temp =0;
		int mod = total % pageSize;
		int div = total/pageSize;
		
		if(mod == 0)
			total_pages = div;
		else
		{
			temp = total - mod;
			total_pages = temp/pageSize;
			total_pages += 1;
		}
			
		model.addAttribute("total",total);
		model.addAttribute("total_pages",total_pages);
		
		return "committes";
	}
	
	@RequestMapping(value = "all_bymember", method = RequestMethod.GET)
	public String committesMembers(Model model,Authentication authentication) {
		
		User user = userDAO.getByUsername(authentication.getName());
		
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		List<Committe> committesList= committeDAO.getAllByUserId(user.getId(),1,0);
		List<Document> docs = new ArrayList<Document>();
		
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		model.addAttribute("committesList", committes);
		
		int total = committeDAO.getCountByMember(user.getId());
		int total_pages = 0;
		int temp =0;
		int mod = total % pageSize;
		int div = total/pageSize;
		
		if(mod == 0)
			total_pages = div;
		else
		{
			temp = total - mod;
			total_pages = temp/pageSize;
			total_pages += 1;
		}
			
		model.addAttribute("total",total);
		model.addAttribute("total_pages",total_pages);
		
		return "committes_bymember";
	}
	
	@RequestMapping(value="/loadcom", method = RequestMethod.GET)
    public @ResponseBody List<CommitteUI> loadCommittees(HttpServletRequest request,HttpServletResponse response)
    {
		logger.info("load comms");
		int page = Integer.parseInt(request.getParameter("page"));
		int offset = (page - 1) * pageSize;
		
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		List<Committe> committesList= committeDAO.getAll(page,offset);
		
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		return committes;
    }
	
	@RequestMapping(value="/loadcom_bymember", method = RequestMethod.GET)
    public @ResponseBody List<CommitteUI> loadCommitteesByMember(HttpServletRequest request,HttpServletResponse response)
    {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		int page = Integer.parseInt(request.getParameter("page"));
		int offset = (page - 1) * pageSize;
		List<Committe> committesList= committeDAO.getAllByUserId(user.getId(), page, offset);
		
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		return committes;
    }
	
	@RequestMapping(value="/searchcom", method = RequestMethod.GET)
    public @ResponseBody List<CommitteUI> searchCommittes(HttpServletRequest request,HttpServletResponse response)
    {
		
		String date_from=request.getParameter("date_from");
		String date_to=request.getParameter("date_to");
		String title=request.getParameter("title");
		logger.info("from: " + date_from + " - to: " + date_to + " - title: " + title);

		List<Committe> committesList= committeDAO.getAllSearch(1,0,date_from,date_to,title);
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		//refactor of pagination
		int total = committes.size();
		int total_pages = 0;
		int temp =0;
		int mod = total % pageSize;
		int div = total/pageSize;
		
		if(mod == 0)
			total_pages = div;
		else
		{
			temp = total - mod;
			total_pages = temp/pageSize;
			total_pages += 1;
		}
		
		if(committes.size() > 0)
		{
			committes.get(0).getCommitte().setTotal(total);
			committes.get(0).getCommitte().setTotal_pages(total_pages);
		}
		
		return committes;
    }
	
	@RequestMapping(value="/searchcom_bymember", method = RequestMethod.GET)
    public @ResponseBody List<CommitteUI> searchCommittesByMember(HttpServletRequest request,HttpServletResponse response)
    {
		
		String date_from=request.getParameter("date_from");
		String date_to=request.getParameter("date_to");
		String title=request.getParameter("title");
		logger.info("from: " + date_from + " - to: " + date_to + " - title: " + title);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());

		List<Committe> committesList= committeDAO.getAllSearchByMember(user.getId(),1,0,date_from,date_to,title);
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		//refactor of pagination
		int total = committes.size();
		int total_pages = 0;
		int temp =0;
		int mod = total % pageSize;
		int div = total/pageSize;
		
		if(mod == 0)
			total_pages = div;
		else
		{
			temp = total - mod;
			total_pages = temp/pageSize;
			total_pages += 1;
		}
		
		if(committes.size() > 0)
		{
			committes.get(0).getCommitte().setTotal(total);
			committes.get(0).getCommitte().setTotal_pages(total_pages);
		}
		
		return committes;
    }
	
	@RequestMapping(value="/download/{id}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable("id") int id) throws IOException {
		
		Document doc = new Document();
		doc = documentDAO.getById(id);
		
        if(doc == null){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
        response.setContentType(doc.getType());
        response.setContentLength(doc.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + doc.getTitle() +"\"");
  
        FileCopyUtils.copy(doc.getContent(), response.getOutputStream());
    }
	
	@RequestMapping(value="/downloadAll/{id}", method = RequestMethod.GET)
    public void zipFiles(HttpServletResponse response, @PathVariable("id") int id) throws IOException{
		
		List<Document> docs;
		docs = documentDAO.getByCommitteIdWithContent(id);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
        for (Document doc : docs) 
        {
    	    ZipEntry entry = new ZipEntry(doc.getTitle());
    	    entry.setSize(doc.getContent().length);
    	    zos.putNextEntry(entry);
    	    zos.write(doc.getContent());
    	    zos.closeEntry();
        }
        zos.close();
        String filename = "files-" + id + ".zip";
        // the response variable is just a standard HttpServletResponse
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
        response.setContentType("application/zip");

        try{            
            response.getOutputStream().write(baos.toByteArray());
            response.flushBuffer();
        }
        catch (IOException e){
            e.printStackTrace();        
        }
        finally{
            baos.close();
        }
        
    }
	
	@RequestMapping(value = "modify/{committeId:\\d+}", method = RequestMethod.GET)
	public String getCommitte(Model model, @PathVariable("committeId") int committeId) {
		
		logger.info("modify committe " + committeId);
		Committe committe = committeDAO.getById(committeId);
 		
 		logger.info("modify committe " + committeId + " get docs");
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		logger.info("modify committe " + committeId + " get members");
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		model.addAttribute("committe", committe);
		
		logger.info("has president : " + committe.isHasPresident());
		
		logger.info("modify committe " + committeId + " get users");
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		logger.info("modify committe " + committeId + " add users_emails");
		model.addAttribute("emails", emails);
		
		return "committe";
	}
	
	@RequestMapping(value = "modify_bymember/{committeId:\\d+}&{isCommitteAdmin}", method = RequestMethod.GET)
	public String getCommitteByMember(Model model, @PathVariable("committeId") int committeId,@PathVariable("isCommitteAdmin") boolean isCommitteAdmin) {
		
		logger.info("modify_bymember committe " + committeId);
		Committe committe = committeDAO.getById(committeId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		if(userRoleDAO.getUserRole(user.getId(),committeId))
		{
			isCommitteAdmin = true;
		}
		else
			isCommitteAdmin = false;
		
		committe.setIsCommitteAdmin(isCommitteAdmin);
 		
 		logger.info("modify_bymember committe " + committeId + " get docs");
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		logger.info("modify_bymember committe " + committeId + " get members");
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		
		logger.info("has president : " + committe.isHasPresident());
		model.addAttribute("committe", committe);
		model.addAttribute("members", members);
		
		logger.info("modify_bymember committe " + committeId + " get users");
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		logger.info("modify_bymember committe " + committeId + " add users_emails");
		model.addAttribute("emails", emails);
 		
		return "committe_bymember";
	}
	
	//@SuppressWarnings("null")
	@RequestMapping(value = "delete/{committeId:\\d+}", method = RequestMethod.GET)
	public String deleteById(Model model, @PathVariable("committeId") int committeId) {
		
		logger.info("delete committe " + committeId);
		Committe committe = committeDAO.getById(committeId);
		List<Committe> committesList = committeDAO.deleteById(committeId);
		
		List<CommitteUI> committes = new ArrayList<CommitteUI>();
		List<Document> docs = new ArrayList<Document>();
		
		for(int i=0; i < committesList.size(); i++)
		{
			docs = documentDAO.getByCommitteId(committesList.get(i).getId());
			
			CommitteUI com = new CommitteUI();
			com.setCommitte(committesList.get(i));
			com.setDocuments(docs);
			committes.add(com);
			docs = new ArrayList<Document>();
		}
		
		model.addAttribute("committesList", committes);
		
		int total = committeDAO.getCount();
		int total_pages = 0;
		int temp =0;
		int mod = total % pageSize;
		int div = total/pageSize;
		
		if(mod == 0)
			total_pages = div;
		else
		{
			temp = total - mod;
			total_pages = temp/pageSize;
			total_pages += 1;
		}
			
		model.addAttribute("total",total);
		model.addAttribute("total_pages",total_pages);
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(0);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " διέγραψε την επιτροπή :" + committe.getTitle());
		committeFollowUpDAO.save(history);
		
		//return "committes";
		return "redirect:/committe/all";
	}
	
	@RequestMapping(value = "deleteDoc/{docId:\\d+}&{committeId:\\d+}", method = RequestMethod.GET)
	public String deleteDoc(Model model, @PathVariable("docId") int docId, @PathVariable("committeId") int committeId) {
		
		logger.info("delete docId " + docId);
		Document doc = documentDAO.getById(docId);
		
		Committe committe = committeDAO.getById(committeId);
 		model.addAttribute("committe", committe);
 		
 		documentDAO.deleteById(docId);
 		
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " διέγραψε από την επιτροπή " + committe.getTitle() + " το αρχείο με όνομα : " + doc.getTitle());
		committeFollowUpDAO.save(history);
 		
		return "committe";
	}
	
	@RequestMapping(value = "deleteDoc_bymember/{docId:\\d+}&{committeId:\\d+}", method = RequestMethod.GET)
	public String deleteDocByMember(Model model, @PathVariable("docId") int docId, @PathVariable("committeId") int committeId) {
		
		logger.info("delete docId " + docId);
		Document doc = documentDAO.getById(docId);
		
		Committe committe = committeDAO.getById(committeId);
		committe.setIsCommitteAdmin(true);
 		model.addAttribute("committe", committe);
 		
 		documentDAO.deleteById(docId);
 		
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " διέγραψε από την επιτροπή " + committe.getTitle() + " το αρχείο με όνομα : " + doc.getTitle());
		committeFollowUpDAO.save(history);
 		
		return "committe_bymember";
	}
	
	@RequestMapping(value = "deleteMember/{memberId:\\d+}&{committeId:\\d+}", method = RequestMethod.GET)
	public String deleteMember(Model model, @PathVariable("memberId") int memberId, @PathVariable("committeId") int committeId) {
		
		logger.info("delete memberId " + memberId);
		
		Member member = memberDAO.getById(memberId);
		
		Committe committe = committeDAO.getById(committeId);
 		model.addAttribute("committe", committe);
 		
 		memberDAO.deleteById(memberId);
 		userRoleDAO.deleteByCommitteId(committeId,member.getUserId());
 		
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//send email to member
		Resource r=new ClassPathResource("mail-context.xml");  
		//@SuppressWarnings("deprecation")
		BeanFactory b=new XmlBeanFactory(r);  
		
		CustomMail m=(CustomMail)b.getBean("mailMail");  
		
		String sender="commission.test2018@gmail.com";//write here sender gmail id  
		String receiver=member.getEmail();//write here receiver id  
		
		String subject="Έχετε διαγραφεί από την επιτροπή: " + committe.getTitle();
		m.sendMail(sender,receiver,"Διαγραφή από επιτροπή",subject);
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " διέγραψε από την επιτροπή " + committe.getTitle() + " τον χρήστη με όνομα : " + member.getLastName() + " " + member.getFirstName() );
		committeFollowUpDAO.save(history);
 		
		return "committe";
	}
	
	@RequestMapping(value = "deleteMember_bymember/{memberId:\\d+}&{committeId:\\d+}", method = RequestMethod.GET)
	public String deleteMemberByMember(Model model, @PathVariable("memberId") int memberId, @PathVariable("committeId") int committeId) {
		
		logger.info("delete memberId " + memberId);
		
		Member member = memberDAO.getById(memberId);
		
		Committe committe = committeDAO.getById(committeId);
		committe.setIsCommitteAdmin(true);
 		model.addAttribute("committe", committe);
 		
 		memberDAO.deleteById(memberId);
 		userRoleDAO.deleteByCommitteId(committeId,member.getUserId());
 		
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//send email to member
		Resource r=new ClassPathResource("mail-context.xml");  
		//@SuppressWarnings("deprecation")
		BeanFactory b=new XmlBeanFactory(r);  
		
		CustomMail m=(CustomMail)b.getBean("mailMail");  
		
		String sender="commission.test2018@gmail.com";//write here sender gmail id  
		String receiver=member.getEmail();//write here receiver id  
		
		String subject="Έχετε διαγραφεί από την επιτροπή: " + committe.getTitle();
		m.sendMail(sender,receiver,"Διαγραφή από επιτροπή",subject);
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " διέγραψε από την επιτροπή " + committe.getTitle() + " τον χρήστη με όνομα : " + member.getLastName() + " " + member.getFirstName() );
		committeFollowUpDAO.save(history);
 		
		return "committe_bymember";
	}
	
	@RequestMapping(value = "addMember/{committeId:\\d+}&{member}&{isAdmin}", method = RequestMethod.GET) 
	public String addMember(Model model, @PathVariable("committeId") int committeId,@PathVariable("member") String memberEmail,@PathVariable("isAdmin") boolean isAdmin) { 
		
		logger.info("add member " + committeId + " - " + memberEmail + "-" + isAdmin); 
		
		//GET Role
		String role ="";
		String roleName="";
		int role_id=0;
		if(isAdmin)
		{
			role = RolesEnum.ROLE_COMMITTE_ADMIN.rolename();
			roleName="ROLE_COMMITTE_ADMIN";
			role_id=2;
		}
		else
		{
			role = RolesEnum.ROLE_COMMITTE_MEMBER.rolename();
			roleName="ROLE_COMMITTE_MEMBER";
			role_id=3;
		}
		
		//Get User
		User user = userDAO.getByEmail(memberEmail);
		
		//Save Member
		Member member = new Member();
		member.setCommitteId(committeId);
		member.setLastName(user.getLastName());
		member.setFirstName(user.getFirstName());
		member.setEmail(memberEmail);
		member.setRole(role);
		member.setUserId(user.getId());
 		
		memberDAO.save(member);
		
		//Save User_Role
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(role_id);
		userRole.setUserName(user.getUsername());
		userRole.setRoleName(roleName);
		userRole.setCommitteId(committeId);

		userRoleDAO.save(userRole);
		
		//Get updated committe
		Committe committe = committeDAO.getById(committeId);
 		model.addAttribute("committe", committe);
 		
 		logger.info("add memmber to committe " + committeId + " get docs");
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		logger.info("add memmber to committe " + committeId + " get members");
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		logger.info("add memmber to committe " + committeId + " get user_emails");
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//send email to new member
		Resource r=new ClassPathResource("mail-context.xml");  
		//@SuppressWarnings("deprecation")
		BeanFactory b=new XmlBeanFactory(r);  
		
		CustomMail m=(CustomMail)b.getBean("mailMail");  
		
		String sender="commission.test2018@gmail.com";//write here sender gmail id  
		String receiver=member.getEmail();//write here receiver id  
		
		//@SuppressWarnings("deprecation")
		String subject="Έχετε προστεθεί στην επιτροπή: " + committe.getTitle() + " με ρόλο: " + role + ", η οποία θα συνεδριάσει στις " + committe.getMeetingDate().toLocaleString();
		m.sendMail(sender,receiver,"Πρόσκληση συμμετοχής σε επιτροπή",subject);  
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " πρόσθεσε στην επιτροπή " + committe.getTitle() + " τον χρήστη με όνομα : " 
				+ member.getLastName() + " " + member.getFirstName() + " και με ρόλο : " + role);
		committeFollowUpDAO.save(history);
		
		return "committe";
	}
	
	@RequestMapping(value = "addMember_bymember/{committeId:\\d+}&{member}&{isAdmin}", method = RequestMethod.GET) 
	public String addMemberByMember(Model model, @PathVariable("committeId") int committeId,@PathVariable("member") String memberEmail,@PathVariable("isAdmin") boolean isAdmin) { 
		
		logger.info("add member " + committeId + " - " + memberEmail + "-" + isAdmin); 
		
		//GET Role
		String role ="";
		String roleName="";
		int role_id=0;
		if(isAdmin)
		{
			role = RolesEnum.ROLE_COMMITTE_ADMIN.rolename();
			roleName="ROLE_COMMITTE_ADMIN";
			role_id=2;
		}
		else
		{
			role = RolesEnum.ROLE_COMMITTE_MEMBER.rolename();
			roleName="ROLE_COMMITTE_MEMBER";
			role_id=3;
		}
		
		//Get User
		User user = userDAO.getByEmail(memberEmail);
		
		//Save Member
		Member member = new Member();
		member.setCommitteId(committeId);
		member.setLastName(user.getLastName());
		member.setFirstName(user.getFirstName());
		member.setEmail(memberEmail);
		member.setRole(role);
		member.setUserId(user.getId());
 		
		memberDAO.save(member);
		
		//Save User_Role
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(role_id);
		userRole.setUserName(user.getUsername());
		userRole.setRoleName(roleName);
		userRole.setCommitteId(committeId);

		userRoleDAO.save(userRole);
		
		//Get updated committe
		Committe committe = committeDAO.getById(committeId);
		committe.setIsCommitteAdmin(true);
 		model.addAttribute("committe", committe);
 		
 		logger.info("add memmber to committe " + committeId + " get docs");
 		List<Document> docs = new ArrayList<Document>();
 		docs = documentDAO.getByCommitteId(committeId);
		model.addAttribute("docs", docs);
		
		logger.info("add memmber to committe " + committeId + " get members");
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(committeId);
		for(int i=0; i < members.size(); i++)
		{
			if(members.get(i).getRole().equals(RolesEnum.ROLE_COMMITTE_ADMIN.rolename())){
				committe.setHasPresident(true);
				members.get(i).setIsCommittePresident(true);
			}
			else
			{
				members.get(i).setIsCommittePresident(false);
			}
			logger.info("is president : " + members.get(i).getIsCommittePresident());
		}
		model.addAttribute("members", members);
		
		logger.info("add memmber to committe " + committeId + " get user_emails");
		List<User> users = new ArrayList<User>();
		users=userDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(int i=0;i<users.size();i++)
		{
			emails.add(users.get(i).getEmail());
		}
		
		model.addAttribute("emails", emails);
		
		//send email to new member
		Resource r=new ClassPathResource("mail-context.xml");  
		//@SuppressWarnings("deprecation")
		BeanFactory b=new XmlBeanFactory(r);  
		
		CustomMail m=(CustomMail)b.getBean("mailMail");  
		
		String sender="commission.test2018@gmail.com";//write here sender gmail id  
		String receiver=member.getEmail();//write here receiver id  
		
		//@SuppressWarnings("deprecation")
		String subject="Έχετε προστεθεί στην επιτροπή: " + committe.getTitle() + " με ρόλο: " + role + ", η οποία θα συνεδριάσει στις " + committe.getMeetingDate().toLocaleString();
		m.sendMail(sender,receiver,"Πρόσκληση συμμετοχής σε επιτροπή",subject);  
		
		//save follow up
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		user = userDAO.getByUsername(auth.getName());
		
		CommitteFollowUp history = new CommitteFollowUp();
		history.setCommitteId(committeId);
		history.setUserId(user.getId());
		history.setAction("Ο χρήστης " + user.getLastName() + " " + user.getFirstName() + " πρόσθεσε στην επιτροπή " + committe.getTitle() + " τον χρήστη με όνομα : " 
				+ member.getLastName() + " " + member.getFirstName() + " και με ρόλο : " + role);
		committeFollowUpDAO.save(history);
		
		return "committe_bymember";
	}
	
	@RequestMapping(value = "history", method = RequestMethod.GET)
	public String history(Model model,Authentication authentication) {
		
		return "committe_history";
	}
	
	@RequestMapping(value="/search_history", method = RequestMethod.GET)
    public @ResponseBody List<CommitteFollowUp> searchHistory(HttpServletRequest request,HttpServletResponse response)
    {
		
		String date_=request.getParameter("date_");
		String title=request.getParameter("title");
		String username=request.getParameter("username");
		
		logger.info("date: " + date_ + " - username: " + username + " - title: " + title);
		
		List<User> usersList= new ArrayList<User>();
		if(username != "")
		{
			usersList= userDAO.getAllSearch(username);
		}
		
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		
		for(int i=0;i < usersList.size(); i++)
		{
			userIds.add(usersList.get(i).getId());
		}
		
		
		List<Committe> committesList = new ArrayList<Committe>();
		if(title != "")
		{
			committesList = committeDAO.getByTitle(title);
		}
		
		ArrayList<Integer> committeIds = new ArrayList<Integer>();
		
		for(int i=0;i < committesList.size(); i++)
		{
			committeIds.add(committesList.get(i).getId());
		}
		
		List<CommitteFollowUp> historyList = committeFollowUpDAO.getByFilter(committeIds, userIds, date_);

		return historyList;
    }
	
}
