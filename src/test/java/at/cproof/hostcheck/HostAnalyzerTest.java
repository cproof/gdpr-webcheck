package at.cproof.hostcheck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
public class HostAnalyzerTest {

    @Value("${webdriver.gecko.driver}")
    private String firefoxPath;

    @Autowired
    private HostAnalyzer hostAnalyzer;


    @BeforeEach
    private void setPath() {
        System.out.println("Gecko Driver path: " + firefoxPath);
        System.setProperty("webdriver.gecko.driver", firefoxPath);
    }


    @Test
    void main() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);

        try {
            driver.get("http://www.google.com");
            driver.manage().timeouts().implicitlyWait(4,
                    TimeUnit.SECONDS);
            WebElement queryBox = driver.findElement(By.name("q"));
            queryBox.sendKeys("headless firefox");
            WebElement searchBtn = driver.findElement(By.name("btnK"));
            searchBtn.click();
            WebElement iresDiv = driver.findElement(By.id("ires"));
            iresDiv.findElements(By.tagName("a")).get(0).click();
            System.out.println(driver.getPageSource());
            String netData = driver.executeScript("return JSON.stringify(window.performance.getEntries());").toString();
            System.out.println(netData);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testGetHostsFromPerformanceEntries() throws IOException {
        String entries = "[{\"name\":\"https://cdn.mdn.mozilla.net/static/build/styles/mdn.aba181e2e425.css\",\"entryType\":\"resource\",\"startTime\":1436.8009709907478,\"duration\":237.17954895913635,\"initiatorType\":\"link\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1436.8009709907478,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1673.9805199498842,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/styles/wiki.733560e8e110.css\",\"entryType\":\"resource\",\"startTime\":1436.937403623747,\"duration\":203.0990988820356,\"initiatorType\":\"link\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1436.937403623747,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1640.0365025057827,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/styles/zone-firefox.d69a0e7a4cf6.css\",\"entryType\":\"resource\",\"startTime\":1437.022636372398,\"duration\":193.07235447185803,\"initiatorType\":\"link\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.022636372398,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1630.094990844256,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/styles/locale-de.a0d0521e251c.css\",\"entryType\":\"resource\",\"startTime\":1437.099737374711,\"duration\":202.85635119506537,\"initiatorType\":\"link\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.099737374711,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1639.9560885697763,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/js/mozilla-dnthelper.3936bd979b1d.js\",\"entryType\":\"resource\",\"startTime\":1437.1687066306863,\"duration\":192.99224171164224,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.1687066306863,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1630.1609483423285,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/jsi18n/de/javascript.628a2f3cb935.js\",\"entryType\":\"resource\",\"startTime\":1437.284659309946,\"duration\":202.56722243639183,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.284659309946,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1639.8518817463378,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/js/main.7d60b1e16ebc.js\",\"entryType\":\"resource\",\"startTime\":1437.3707955859677,\"duration\":191.71585871241314,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.3707955859677,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1629.0866542983808,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/js/wiki.c60705233ffd.js\",\"entryType\":\"resource\",\"startTime\":1437.441873072475,\"duration\":192.11822956823448,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.441873072475,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1629.5601026407094,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/js/newsletter.7cd3274169f2.js\",\"entryType\":\"resource\",\"startTime\":1437.509336449499,\"duration\":192.5097580956051,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1437.509336449499,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1630.019094545104,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://www.google-analytics.com/analytics.js\",\"entryType\":\"resource\",\"startTime\":1681.4665453932153,\"duration\":418.89064909406306,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1681.4665453932153,\"domainLookupStart\":2048.5076739591364,\"domainLookupEnd\":2048.5456221087125,\"connectStart\":2048.6245301657673,\"connectEnd\":2048.6245301657673,\"secureConnectionStart\":0,\"requestStart\":2049.3377144371625,\"responseStart\":2098.5155045296838,\"responseEnd\":2100.3571944872783,\"transferSize\":15184,\"encodedBodySize\":14597,\"decodedBodySize\":35943},{\"name\":\"https://cdn.mdn.mozilla.net/static/fonts/OpenSans-Regular-webfont.3f642fa3ea74.woff2\",\"entryType\":\"resource\",\"startTime\":1749.0973761565149,\"duration\":164.6344328257519,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1749.0973761565149,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1913.7318089822668,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/styles/libs/font-awesome/fonts/fontawesome-webfont.fdf491ce5ff5.woff?v=4.1.0\",\"entryType\":\"resource\",\"startTime\":1749.3955401888975,\"duration\":204.45408876252895,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1749.3955401888975,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1953.8496289514264,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/fonts/OpenSans-Semibold-webfont.b25e8a5a61a4.woff2\",\"entryType\":\"resource\",\"startTime\":1749.857242675405,\"duration\":246.02658779876606,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1749.857242675405,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1995.883830474171,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/fonts/locales/ZillaSlab-Regular.f9de6143fdfa.woff2\",\"entryType\":\"resource\",\"startTime\":1750.7848641094834,\"duration\":128.36142299537414,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1750.7848641094834,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1879.1462871048575,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/fonts/locales/ZillaSlab-Bold.8d7f01331d2b.woff2\",\"entryType\":\"resource\",\"startTime\":1751.6709232845026,\"duration\":294.89928681572883,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1751.6709232845026,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":2046.5702101002314,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/fonts/OpenSans-Italic-webfont.47c24d65c5a6.woff2\",\"entryType\":\"resource\",\"startTime\":1767.5468027178104,\"duration\":248.69380059753303,\"initiatorType\":\"other\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1767.5468027178104,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":2016.2406033153434,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://cdn.mdn.mozilla.net/static/build/js/syntax-prism.05dc5663d445.js\",\"entryType\":\"resource\",\"startTime\":1800.0578257517348,\"duration\":33.23715786430239,\"initiatorType\":\"script\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":1800.0578257517348,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":1833.2949836160371,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0},{\"name\":\"https://www.google-analytics.com/r/collect?v=1&_v=j66&aip=1&a=33200704&t=pageview&_s=1&dl=https%3A%2F%2Fdeveloper.mozilla.org%2Fde%2FFirefox%2FHeadless-Mode&dr=https%3A%2F%2Fwww.google.at%2F&ul=de&de=UTF-8&dt=Headless%20mode%20-%20Mozilla%20%7C%20MDN&sd=24-bit&sr=1366x768&vp=1349x685&je=0&_u=YEBAAAAB~&jid=720251497&gjid=135927242&cid=917979648.1523556291&tid=UA-36116321-5&_gid=1682204221.1523556291&_r=1&cd12=1347302&cd17=Mozilla%2FFirefox%2FHeadless_mode&z=384614410\",\"entryType\":\"resource\",\"startTime\":2111.984989398612,\"duration\":50.48459184656895,\"initiatorType\":\"img\",\"nextHopProtocol\":\"h2\",\"redirectStart\":0,\"redirectEnd\":0,\"fetchStart\":2111.984989398612,\"domainLookupStart\":0,\"domainLookupEnd\":0,\"connectStart\":0,\"connectEnd\":0,\"secureConnectionStart\":0,\"requestStart\":0,\"responseStart\":0,\"responseEnd\":2162.469581245181,\"transferSize\":0,\"encodedBodySize\":0,\"decodedBodySize\":0}]";
        HostAnalyzer gdpr = new HostAnalyzer();
        gdpr.getHostsFromPerformance(entries);
    }

    @Test
    public void testGetPerformanceStringForDomain() throws URISyntaxException {
        String perf = hostAnalyzer.getPerformanceStringForUrl("https://developer.mozilla.org/de/Firefox/Headless-Mode");
    }

    @Test
    public void testFull() throws URISyntaxException, IOException {
        //String entries = HostAnalyzer.getPerformanceStringForUrl("https://portal.etsi.org/tbsitemap/stq/htlmreferencewebpage.aspx");
        String entries = hostAnalyzer.getPerformanceStringForUrl("http://www.netztest.at");
        Set<String> hostsFromPerformance = hostAnalyzer.getHostsFromPerformance(entries);
        assertEquals(hostsFromPerformance.size(),4);
    }
}
