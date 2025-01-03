 package controller;

import model.User;
import view.SceneManager;
import view.generalPages.RegisterPage;

public class UserController {

	private SceneManager sceneManager;
	private ValidationErrorCallback errorCallback;
    public UserController(SceneManager sceneManager, ValidationErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
        this.sceneManager = sceneManager;
    }
    public interface ValidationErrorCallback {
        void displayError(String errorMessage);
    }
    public String checkRegisterInput(String email, String name, String password) {
        if (email.isEmpty()) {
            return "Email must not be empty.";
        }else if(!email.endsWith("@gmail.com")) {
        	return "email must end with @gmail.com";
        }
        if (name.isEmpty()) {
            return "Name must not be empty.";
        }
        if (password.isEmpty()) {
            return "Password must not be empty.";
        }
        if (password.length() < 5) {
            return "Password must be at least 5 characters long.";
        }
        if (getUserByEmail(email) != null) {
            return "Email is already in use.";
        }
        if (getUserByUsername(name) != null) {
            return "Name is already taken.";
        }
        return null;
    }
    public String getUserByUsername(String name) {
        User temp = new User(); 
        User result = temp.getUserByUserName(name);
        return (result != null) ? result.getUser_name() : null;
    }
    public User getUserById(String id) {
        User temp = new User();
        User result = temp.getUserById(id);
        return (result != null) ? result : null;
    }
    public String getUserByEmail(String email) {
        User temp = new User();
        User result = temp.getUserByEmail(email);
        return (result != null) ? result.getUser_email() : null;
    }

    public void Register(String name, String email, String role, String password) {
    	String validationError = checkRegisterInput(email, name, password);
    	 if (validationError != null) {
	           if (errorCallback != null) {
	               errorCallback.displayError(validationError);
	           }
	           return; 
    	 }
    	 User user = new User();
    	 if(user.Register(name, email, role, password)) {
    		 errorCallback.displayError("Register Success");
        	 if (errorCallback instanceof RegisterPage) {
               ((RegisterPage) errorCallback).navigateToLandingPage();
        	 } 
    	 }else {
    		 errorCallback.displayError("register failed");
    	 }
    	
    }
    
    public void Login(String email,String password) {
    	User temp = new User();
    	User result = temp.login(email, password);
    	if(result!=null) {
    		 System.out.println("Login successful for user: " + result.getUser_name());
    		 String role = result.getUser_role();
    		 errorCallback.displayError("Login Success");
    		 switch (role) {
    		 	case"Guest":
    		 		sceneManager.showGuestLandingPage(result);
    		 		break;
    		 	case "Vendor":
    		 		sceneManager.showVendorLandingPage(result);
    		 		break;
    		 	case "Organizer":
    		 		sceneManager.showOrganizerLandingPage(result);
    		 		break;
    		 	case"admin":
    		 		sceneManager.showAdminLandingPage(result);
    		 		break;
			}
    	}else {
    		if(errorCallback != null) {
                errorCallback.displayError("Invalid email or password.");
            }
    	}
    }
    
  
    public String checkUserNameInput(String name, String userId) {
    	User user = new User();
   
    	if(name.isEmpty()) {
    		return "username cannot be empty";
    	}
    	if(user.getUserById(userId).getUser_name().equals(name)) {
    		return "username must not be the same with current name";
    	}
    	if(user.getUserByUserName(name)!=null) {
    		return "username must be unique";
    	}

    	return null;
    }
    public void changeUserName(String name, String userId) {
        String validationError = checkUserNameInput(name, userId);
        if (validationError != null) {
            if (errorCallback != null) {
                errorCallback.displayError(validationError); 
            }
            return; 
        }
        User user = new User();
        if(user.changeUserName(name, userId)) {
        	errorCallback.displayError("Success");
        }else {
        	errorCallback.displayError("Failed to change username");
        }
        
    }
    public String checkUserEmailInput(String email, String userId) {
    	User user = new User();
   
    	if(email.isEmpty()) {
    		return "email cannot be empty";
    	}
    	if(user.getUserById(userId).getUser_email().equals(email)) {
    		return "email must not be the same with current name";
    	}
    	if(user.getUserByEmail(email)!=null) {
    		return "email must be unique";
    	}

    	return null;
    }
    public void changeUserEmail(String email, String userId) {
        String validationError = checkUserNameInput(email, userId);
        if (validationError != null) {
            if (errorCallback != null) {
                errorCallback.displayError(validationError); 
            }
            return; 
        }
        User user = new User();
        if(user.changeUserEmail(email, userId)) {
        	errorCallback.displayError("Success");
        }else {
        	errorCallback.displayError("Failed to change username");
        }
        
    }
    public String checkUserPasswordInput(String newPassword,String oldPassword, String userId) {
    	User user = new User();
   
    	if(newPassword.isEmpty()) {
    		return "password cannot be empty";
    	}
    	if(!user.getUserById(userId).getUser_password().equals(oldPassword)) {
    		return "Old password must same as the password from database";
    	}
    	if(newPassword.length()<5) {
    		return "password must be at lest 5 characters";
    	}

    	return null;
    }
    public void changeUserPassword(String newPassword,String oldPassword, String userId) {
        String validationError = checkUserPasswordInput(newPassword,oldPassword, userId);
        if (validationError != null) {
            if (errorCallback != null) {
                errorCallback.displayError(validationError); 
            }
            return; 
        }
        User user = new User();
        if(user.changeUserPassword(newPassword, userId)) {
        	errorCallback.displayError("Success");
        }else {
        	errorCallback.displayError("Failed to change username");
        }
        
    }
}

