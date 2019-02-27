package com.hua.committee;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hua.committee.dao.CommitteDAO;
import com.hua.committee.dao.UserDAO;
import com.hua.committee.model.Committe;
import com.hua.committee.model.User;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/users")
public class UsersController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int pageSize = 5;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@Autowired
	@Qualifier("committeDAO")
	private CommitteDAO committeDAO;
	
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public String users(Model model,Authentication authentication) {
		List<User> usersList= userDAO.getAll(1,0);
		
		model.addAttribute("usersList", usersList);
		int total = userDAO.getCount();
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
		
		
		for(int i=0; i<usersList.size(); i++)
		{
			List<Committe> comms = committeDAO.getAllByUserId(usersList.get(i).getId(), 1, 0);
			if(comms.size() > 0)
				usersList.get(i).setInCommitte(true);
			else
				usersList.get(i).setInCommitte(false);
		}
		
		return "users";
		
	}
	
	@RequestMapping(value="/load", method = RequestMethod.GET)
    public @ResponseBody List<User> loadUsers(HttpServletRequest request,HttpServletResponse response)
    {
		int page = Integer.parseInt(request.getParameter("page"));
		int offset = (page - 1) * pageSize;
		List<User> usersList= userDAO.getAll(page,offset);
		
		for(int i=0; i<usersList.size(); i++)
		{
			List<Committe> comms = committeDAO.getAllByUserId(usersList.get(i).getId(), 1, 0);
			if(comms.size() > 0)
				usersList.get(i).setInCommitte(true);
			else
				usersList.get(i).setInCommitte(false);
		}
		
		return usersList;
    }
	
	@RequestMapping(value = "user/{userId:\\d+}", method = RequestMethod.GET)
	public String getUser(Model model, @PathVariable("userId") int userId) {
		User user = userDAO.getById(userId);
		model.addAttribute("user", user);
		return "user";
	}
	
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String getProfile(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDAO.getByUsername(auth.getName());
		
		model.addAttribute("user", user);
		return "profile";
	}
	
	@RequestMapping(value = "profile", method = RequestMethod.POST)
	public String UserProfile(Model model, @ModelAttribute("user") @Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			return "user";
		}
		userDAO.update(user);

		return "redirect:/index";

	}
	
	@RequestMapping(value = "delete/{userId:\\d+}", method = RequestMethod.GET)
	public String deleteUser(Model model, @PathVariable("userId") int userId) {

		if (userDAO.deleteById(userId)) {
			return "redirect:/users/all";
		} else {
			model.addAttribute("message", "Error when deleting user");
			return "test";
		}

	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String createUser(Model model, @ModelAttribute("user") @Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			return "user";
		}

		if (user.getId() == 0) {
			userDAO.save(user);
			Resource r=new ClassPathResource("mail-context.xml");  
			BeanFactory b=new XmlBeanFactory(r);  
			
			CustomMail m=(CustomMail)b.getBean("mailMail");  
			
			String sender="commission.test2018@gmail.com";//write here sender gmail id  
			String receiver=user.getEmail();//write here receiver id  
			
			String subject="Έχετε προστεθεί ως χρήστης στην ηλεκτρονική πλατφόρμα Διαχείρισης Επιτροπών. " + System.lineSeparator() + "Ο κωδικός χρήστη είναι: " + user.getUsername() + System.lineSeparator() + "O κωδικός πρόσβασης είναι: " + user.getPassword() + System.lineSeparator() + "Θα ενημερωθείτε μέσω email για την προσθήκη σας σε κάποια επιτροπή. Αφού λάβετε αυτό το email, συνδεθείτε στην εφαρμογή, τροποποιήστε τα στοιχεία σύνδεσης του λογαριασμού σας και δείτε πληροφορίες για την επιτροπή." ;
			m.sendMail(sender,receiver,"Εισαγωγή χρήστη στο σύστημα Διαχείρισης Επιτροπών",subject);
		} else {
			userDAO.update(user);
		}

		return "redirect:/users/all";

	}
 	
 	@RequestMapping(value="register", method = RequestMethod.GET)
 	public String register(Model model) {
 		
 		model.addAttribute("title", "Register");
 		User user = new User();
 		model.addAttribute("user", user);
 		return "user";
 		
 	}
 	
 	@RequestMapping(value="/searchusers", method = RequestMethod.GET)
    public @ResponseBody List<User> searchUsers(HttpServletRequest request,HttpServletResponse response)
    {
		
		String username=request.getParameter("username");
		
		List<User> usersList= userDAO.getAllSearch(username);

		
		//refactor of pagination
		int total = usersList.size();
		@SuppressWarnings("unused")
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
		
		if(usersList.size() >0)
			usersList.get(0).setTotal(total);
		
		for(int i=0; i<usersList.size(); i++)
		{
			List<Committe> comms = committeDAO.getAllByUserId(usersList.get(i).getId(), 1, 0);
			if(comms.size() > 0)
				usersList.get(i).setInCommitte(true);
			else
				usersList.get(i).setInCommitte(false);
		}
		
		return usersList;
    }
 	
	@RequestMapping(value="/searchldap", method = RequestMethod.GET)
    public @ResponseBody User ldapUser(HttpServletRequest request,HttpServletResponse response)
    {
		User user= new User();
		String email=request.getParameter("email");
		
		LdapContext ctx = null;
        //NamingEnumeration<?> results = null;
        try {
        	Hashtable<String, String> env = new Hashtable<String, String>();

            env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://hercules.hua.gr:3268");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "it20915@hua.gr");
            env.put(Context.SECURITY_CREDENTIALS, "5&7zd3y5e");
            
    		ctx = new InitialLdapContext(env, null);
    		System.out.println("Connection Successful.");
    		////////////////////////////////////////////////////////////
    		try {

                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String[] attrIDs = { "distinguishedName",
                        "sn",
                        "givenname",
                        "mail",
                        "telephonenumber",
                        "department",
                        "title"};
                constraints.setReturningAttributes(attrIDs);
                //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
                //Second Attribute can be uid=username
                @SuppressWarnings("rawtypes")
				NamingEnumeration answer = ctx.search("DC=hua,DC=gr", "sAMAccountName="
                        + email, constraints);
                
                if (answer.hasMore()) {
                    Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                    
                    user.setFirstName((attrs.get("givenname")).toString().substring(11));
                    user.setLastName((attrs.get("sn")).toString().substring(4));
                    user.setEmail((attrs.get("mail")).toString().substring(6));
                    user.setUsername((attrs.get("mail")).toString().substring(6));
                    user.setUsername(user.getUsername().substring(0, user.getUsername().length() - 7));
                    user.setPassword((attrs.get("mail")).toString().substring(6));
                    
                    User user_exists = new User();
                    user_exists = userDAO.getByEmail(user.getEmail());
                    if(user_exists.getId() > 0 )
                    	user.setExists(true);
                    else
                    	user.setExists(false);

                }else{
                    throw new Exception("Invalid User");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
    		
    		
        }catch(NamingException nex){
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        
		///////////////////////////////

		
		return user;
    }

}