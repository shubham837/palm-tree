package crossover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    protected static final String HOME_VIEW = "index";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHomePage() {
        log.debug("Rendering home page.");
        return HOME_VIEW;
    }
}
