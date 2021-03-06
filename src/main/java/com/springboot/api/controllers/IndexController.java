package com.springboot.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * IndexController
 */
@Controller
public class IndexController implements ErrorController  {
 
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private static String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @RequestMapping(value = "/")
    public String index(Model model, Authentication auth) {

        
        if ( auth != null && auth instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) auth;

            OAuth2AuthorizedClient client = authorizedClientService
                    .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
            
            String userInfoEndpointUri = new String("");

            if(client != null) {
                userInfoEndpointUri = client.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUri();
            }
            
            if (!StringUtils.isEmpty(userInfoEndpointUri)) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
    
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
    
                ParameterizedTypeReference<HashMap<String, String>> responseType = 
                    new ParameterizedTypeReference<HashMap<String, String>>() {};

                HttpEntity<?> entity = new HttpEntity<>("", headers);
                ResponseEntity<HashMap<String, String>> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, responseType);
    
                Map<String, String> userAttributes = response.getBody();
                model.addAttribute("name", userAttributes.get("name"));
            }
        }

        return "index";
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/login")
    public String login(Model model) {

        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        
        if (type != ResolvableType.NONE &&  ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
    
        if ( clientRegistrations != null) {
            clientRegistrations.forEach(registration -> 
                oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
            
            model.addAttribute("urls", oauth2AuthenticationUrls);
        }

        return "login";
    }
    
    @RequestMapping(value = "/source")
    @ResponseBody
    public String error() {
        return "https://github.com/juliosilvacwb/cadastro-pessoa";
    }
    
    @RequestMapping(value = "/login/oauth2/code/google")
    @ResponseBody
    public String alth2() {
        return "home";
    }
    
    @Override
    @RequestMapping("/error")
    public String getErrorPath() {
        return "index";
    }
    
}