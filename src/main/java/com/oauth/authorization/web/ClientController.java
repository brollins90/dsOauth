package com.oauth.authorization.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oauth.authorization.domain.Client;
import com.oauth.authorization.service.ClientService;

@Controller
@RequestMapping("/client")
public class ClientController {
	
    @Autowired
    private ClientService clientService;
    
    
    @RequestMapping("/profile")
    public String client(String clientId, Model model) {
        Client client = clientService.findClient(clientId);
        model.addAttribute("client", client);
        return "client";
    }
    
    @RequestMapping("/edit")
    public String editClient(
            Client client,
            Model model) {
    	clientService.addClient(client);
        return "redirect:client?clientId=" + client.getClientId();
    }
    
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String newClient(Model model) {
    	model.addAttribute("client", new Client());
        return "newClient";
    }
    
    @RequestMapping(value="/new", method=RequestMethod.POST)
    public String newClient(Model model, Client client) {
    	clientService.addClient(client);
    	return "redirect:profile?clientId=" + client.getClientId();
    }

}
