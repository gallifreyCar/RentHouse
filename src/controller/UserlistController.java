package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Pojo.User;
import Pojo.Userlist;
import service.UserlistService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class UserlistController {

    @Autowired
    private UserlistService userlistService;


    @RequestMapping("/findhasuserlist")
    public String findhasuserlist(HttpSession httpSession, Model model) throws Exception {
        User user1 = (User) httpSession.getAttribute("user");
        Integer user_id = user1.getId();
        Userlist userlist = userlistService.findhasuserlist(user_id);
        model.addAttribute("userlist", userlist);
        model.addAttribute("mainPage", "updateuserlist.jsp");
        return "zuke/main";
    }

    @RequestMapping("/afindhasuserlist")
    public String afindhasuserlist(HttpSession httpSession, Model model) throws Exception {
        User user1 = (User) httpSession.getAttribute("user");
        Integer user_id = user1.getId();
        Userlist userlist = userlistService.findhasuserlist(user_id);
        model.addAttribute("userlist", userlist);
        model.addAttribute("mainPage", "updateuserlist.jsp");
        return "admin/main1";
    }


    @RequestMapping("/cfindhasuserlist")
    public String cfindhasuserlist(HttpSession httpSession, Model model) throws Exception {
        User user1 = (User) httpSession.getAttribute("user");
        Integer user_id = user1.getId();
        Userlist userlist = userlistService.findhasuserlist(user_id);
        model.addAttribute("userlist", userlist);
        model.addAttribute("mainPage", "cupdateuserlist.jsp");
        return "superadmin/main";
    }


    @RequestMapping("/checkuserlists")
    @ResponseBody
    public String checkuserlists(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        boolean flag = userlistService.checkuserlists(user.getId());

        if (!flag) {
            model.addAttribute("info", "???????????????????????????????????????");
            return "no";
        }
        model.addAttribute("info", "");
        return "yes";
    }

    //???????????????????????????
    @RequestMapping("/checkuserlist")
    public String checkuserlist(Model model, Userlist userlist, HttpSession httpSession) throws Exception {
        if (userlist.getId() == null) {
            model.addAttribute("info", "???????????????????????????????????????");
            Userlist list = userlistService.checkuserlist(userlist.getIdcard());

            if (list != null) {
                model.addAttribute("error", "????????????????????????,???????????????????????????????????????????????????");
                model.addAttribute("mainPage", "updateuserlist.jsp");
                model.addAttribute("userlist", userlist);
            } else {
                User user1 = (User) httpSession.getAttribute("user");
                Integer user_id = user1.getId();
                userlist.setUser_id(user_id);
                userlistService.insertuserlist(userlist);
                Userlist list1 = userlistService.checkuserlist(userlist.getIdcard());
                model.addAttribute("error", "??????????????????");
                model.addAttribute("mainPage", "updateuserlist.jsp");
                model.addAttribute("userlist", list1);
            }
        } else {

            Userlist list = userlistService.finduserlistupdate(userlist);
            if (list != null) {
                model.addAttribute("error", "??????????????????????????????");
                model.addAttribute("mainPage", "updateuserlist.jsp");
                model.addAttribute("userlist", userlist);
            } else {
                userlistService.updateuserlist(userlist);
                model.addAttribute("error", "????????????");
                model.addAttribute("mainPage", "updateuserlist.jsp");
                model.addAttribute("userlist", userlist);
            }
        }
        return "zuke/main";
    }

    @RequestMapping("/findalluserlist")
    public String findalluserlist(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Userlist> userlist = userlistService.findalluserlist();
        PageInfo<Userlist> p = new PageInfo<Userlist>(userlist);
        model.addAttribute("userlist", userlist);
        model.addAttribute("p", p);
        model.addAttribute("mainPage", "userlist.jsp");
        return "admin/main1";

    }

    @RequestMapping("/bfindalluserlist")
    public String bfindalluserlist(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Userlist> userlist = userlistService.findalluserlist();
        PageInfo<Userlist> p = new PageInfo<Userlist>(userlist);
        model.addAttribute("userlist", userlist);
        model.addAttribute("p", p);
        model.addAttribute("mainPage", "buserlist.jsp");
        return "superadmin/main";

    }


    //??????????????????
    @RequestMapping("/deleteuserlist")
    public String deleteuserlist(Model model, Integer id) {
        userlistService.deleteuserlist(id);
        model.addAttribute("error", "deletesuccess");
        return "redirect:findalluserlist.action";
    }
}
