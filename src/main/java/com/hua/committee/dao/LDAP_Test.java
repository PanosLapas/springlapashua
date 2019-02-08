package com.hua.committee.dao;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
//import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
//import javax.naming.directory.Attribute;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;
//import javax.naming.directory.SearchControls;
//import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.hua.committee.model.User;


public class LDAP_Test {
	public static void main(String[] args) {
		//--------------------------------------------------------
		
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
    		@SuppressWarnings("unused")
			User in = getUserBasicAttributes("it20915",ctx);
        }catch(NamingException nex){
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
      
	}
	
    @SuppressWarnings("null")
	public static User getUserBasicAttributes(String username, LdapContext ctx) {
        User user=null;
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
                    + username, constraints);
            
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println("distinguishedName "+ attrs.get("distinguishedName"));
                System.out.println("givenname "+ attrs.get("givenname"));
                System.out.println("sn "+ attrs.get("sn"));
                System.out.println("mail "+ attrs.get("mail"));
                System.out.println("department "+ attrs.get("department"));
                System.out.println("title "+ attrs.get("title"));
                
                user.setFirstName((attrs.get("givenname")).toString());
                user.setLastName((attrs.get("sn")).toString());
                user.setEmail((attrs.get("mail")).toString());
                user.setUsername((attrs.get("mail")).toString());
                user.setPassword((attrs.get("mail")).toString());

            }else{
                throw new Exception("Invalid User");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
}
