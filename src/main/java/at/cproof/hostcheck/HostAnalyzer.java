package at.cproof.hostcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class HostAnalyzer  implements InitializingBean {

    @Value("${webdriver.gecko.driver}")
    private String geckoDriverPath;

    @Value("${firefox.path}")
    private String firefoxPath;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        Logger.getGlobal().info("set driver path to: " + geckoDriverPath);
    }

    public String getPerformanceStringForUrl(String domain) throws URISyntaxException {
        URI u = new URI(domain); //check validity
        if (!domain.startsWith("http") && !domain.startsWith("https")) {
            throw new URISyntaxException(domain,"domain has to start with http(s)");
        }

        //firefox is maybe not located in PATH
        FirefoxBinary firefoxBinary;
        if (firefoxPath != null && !firefoxPath.isEmpty()) {
            firefoxBinary = new FirefoxBinary(new File(firefoxPath));
        }
        else {
            firefoxBinary = new FirefoxBinary();
        }

        firefoxBinary.addCommandLineOptions("--headless");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);

        try {
            driver.get(domain);
            driver.manage().timeouts().implicitlyWait(10,
                    TimeUnit.SECONDS);
            String netData = driver.executeScript("return JSON.stringify(window.performance.getEntries());").toString();
            return netData;
        } finally {
            driver.quit();
        }
    }


    public Set<String> getHostsFromPerformance(String performance) throws IOException {
        ObjectMapper om = new ObjectMapper();
        List<PerformanceEntry> entries = Arrays.asList(om.readValue(performance, PerformanceEntry[].class));

        //filter for resource events
        entries = entries.stream().filter((e) -> e.getEntryType().equals("resource")).collect(Collectors.toList());

        //domain name sorter
        Comparator<String> byDomain = (o1, o2) -> {
            String[] c1 = o1.split("\\.");
            String[] c2 = o2.split("\\.");
            ArrayUtils.reverse(c1);
            ArrayUtils.reverse(c2);
            String c1r = StringUtils.join(c1, ".");
            String c2r = StringUtils.join(c2, ".");
            return c1r.compareTo(c2r);
        };

        Supplier<TreeSet<String>> supplier =
                () -> new TreeSet<String>(byDomain);

        //get hostname strings
        Set<String> hostnames = entries.stream().map((e) -> {
            URI uri = null;
            try {
                uri = new URI(e.getName().replace("|","%7C")); //Google Fonts fix
                String domain = uri.getHost();
                if (domain == null) {
                    System.out.println(e.getName());
                    domain = uri.getAuthority();
                }
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            return null;
        }).filter((s) -> s != null)
                .collect(Collectors.toCollection(supplier));

        //done :)
        System.out.println("done");
        return hostnames;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PerformanceEntry {
        private String name;
        private String entryType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEntryType() {
            return entryType;
        }

        public void setEntryType(String entryType) {
            this.entryType = entryType;
        }
    }
}
