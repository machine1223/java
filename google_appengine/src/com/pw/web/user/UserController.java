package com.pw.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 1/20/14
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        model.asMap().clear();
        List<HashMap<String,Object>> users = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String,Object> user = new HashMap<String, Object>();
            user.put("id",i+1);
            user.put("name","name"+(i+1));
            user.put("age",10*(i+1));
            users.add(user);
        }
        model.addAttribute("users",users);
        return "user/list";
    }

    @RequestMapping(value="/getUser/:id", method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object searchTwitterAlerts(@RequestParam(value="id",required=true) long id, Model model) {

        return model;
    }
}
