package com.gcu.topic71c.controller;

import com.gcu.topic71c.model.OrderModel;
import com.gcu.topic71c.model.UserModel;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/app")
public class TestController {

    @Autowired
    EurekaClient client;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Demo Microservices Application");
        return "home";
    }

    @GetMapping("/getUsers")
    public String getUsers(Model model){
        Application application = client.getApplication("user-service");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();

        String url = "http://" + hostname + ":" + port + "/service/users";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<UserModel>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserModel>>() {});
        List<UserModel> users = response.getBody();

        model.addAttribute("title", "List of Users");
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/getOrders")
    public String getOrders(Model model){
        Application application = client.getApplication("order-service");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();

        String url = "http://" + hostname + ":" + port + "/service/orders";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<OrderModel>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderModel>>() {});
        List<OrderModel> orders = response.getBody();

        model.addAttribute("title", "List of Orders");
        model.addAttribute("orders", orders);
        return "orders";
    }
}
