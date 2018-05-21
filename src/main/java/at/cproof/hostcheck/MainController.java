package at.cproof.hostcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private HostAnalyzer hostAnalyzer;

    @RequestMapping(value = "/lookup/{domain}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Set<String>> lookupGet(@PathVariable("domain") String domain) {
        //try parsing the domain
       return lookupUrl(domain);
    }

    @RequestMapping(value = "/lookup", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Set<String>> lookupPost(@RequestParam("domain") String domain) {
        //try parsing the domain
        return lookupUrl(domain);
    }

    private Map<String,Set<String>> lookupUrl(String url) {
        //try parsing the domain
        try {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }

            Logger.getGlobal().info("Begin lookup for: " + url);
            String performanceStringForDomain = hostAnalyzer.getPerformanceStringForUrl(url);

            Map<String, Set<String>> ret = new HashMap<>();

            //parse Strings
            Set<String> hostsFromPerformance = hostAnalyzer.getHostsFromPerformance(performanceStringForDomain);
            ret.put("hosts",hostsFromPerformance);
            Logger.getGlobal().info("Lookup complete for: " + url + " -- found " + hostsFromPerformance.size());

            return ret;

        } catch (URISyntaxException | IOException e) {
            throw new IllegalDomainException();
        }
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")  // 400
    private class IllegalDomainException extends RuntimeException {

    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(MainController.class, args);
    }


}
