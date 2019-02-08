package com.hua.committee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

//import javax.naming.Context;
//import javax.naming.NamingEnumeration;
//import javax.naming.NamingException;
//import javax.naming.directory.Attribute;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;
//import javax.naming.directory.SearchControls;
//import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hua.committee.dao.CommitteDAO;
import com.hua.committee.dao.MemberDAO;
import com.hua.committee.model.Committe;
import com.hua.committee.model.Member;


@SuppressWarnings("deprecation")
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	@Qualifier("committeDAO")
	private CommitteDAO committeDAO;
	
	@Autowired
	@Qualifier("memberDAO")
	private MemberDAO memberDAO;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,Authentication authentication,SecurityContextHolderAwareRequestWrapper request) {
		logger.info("Welcome home! The client locale is {}.", locale);
		logger.info("SPRING VERSION " + org.springframework.core.SpringVersion.getVersion());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("pageTitle", "Log In");
		model.addAttribute("serverTime", formattedDate );
		         
		
		// SENT REMINDER IF NEEDED
		SimpleDateFormat formattedDate_ = new SimpleDateFormat("dd/MM/yyyy");            
		Calendar c = Calendar.getInstance();        
		c.add(Calendar.DATE, 2);  // number of days to add      
		String tomorrow = (String)(formattedDate_.format(c.getTime()));
		System.out.println("next date is " + tomorrow);
		
		List<Committe> committesList= committeDAO.getAllMeetingDate(tomorrow);
		
		for(int i=0;i<committesList.size(); i++)
		{
			List<Member> members = memberDAO.getByCommitteId(committesList.get(i).getId());
			for(int j=0;j<members.size();j++)
			{
				//send email to member
				Resource r=new ClassPathResource("mail-context.xml");  
				//@SuppressWarnings("deprecation")
				BeanFactory b=new XmlBeanFactory(r);  
				
				CustomMail m=(CustomMail)b.getBean("mailMail");  
				
				String sender="commission.test2018@gmail.com";//write here sender gmail id  
				String receiver=members.get(j).getEmail();//write here receiver id  
				
				String subject="Σας υπενθυμίζουμε ότι η επιτροπή: " + committesList.get(i).getTitle() + " θα συνεδριάσει σε 2 μέρες.";
				m.sendMail(sender,receiver,"Συνάντηση - Reminder!",subject);
			}
			committeDAO.updateReminder(committesList.get(i));
		}
		//END OF REMINDER
		
		if(request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_COMMITTE_ADMIN") || request.isUserInRole("ROLE_COMMITTE_MEMBER"))
			return "index";
		else
			return "login";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Locale locale, Model model) {
		
		model.addAttribute("pageTitle", "Αρχική");
		
		return "index";
	}
	
	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Login Form - Database Authentication");
		model.addObject("message", "This page is for ROLE_ADMIN only!");
		model.setViewName("admin");

		return model;

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "authfailed", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		logger.info("Welcome.");
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Λάθος συνθηματικό ή κωδικός!");
		}

		if (logout != null) {
			logger.info("Logout");
			model.addObject("msg", "Έχετε αποσυνδεθεί.");
		}
		
		model.setViewName("login");

		return model;

	}
	
	/*@RequestMapping(value = "index", method = RequestMethod.GET)
	public String users(Model model,Authentication authentication) {
		
		if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]"))
		{
			logger.info("Welcome {} - .", authentication.getName());
			logger.info("Welcome {}.", authentication.getAuthorities().toString());
			return "home";	
		}
		else
		{
			logger.info("Welcome {} - .", authentication.getName());
			logger.info("Welcome {}.", authentication.getAuthorities().toString());
			return "users";	
		}
		
	}*/
	
}
