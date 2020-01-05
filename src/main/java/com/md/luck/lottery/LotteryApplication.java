package com.md.luck.lottery;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author madong
 */
@SpringBootApplication
@EnableTransactionManagement
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }
    //配置http某个端口自动跳转https
    @Bean
    public TomcatServletWebServerFactory servletContainer() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //监听的http端口
        connector.setPort(8082);
        connector.setSecure(false);
        //跳转的https端口
        connector.setRedirectPort(443);
        return connector;
    }

//    public static void main(String[] args) {
//        String str = "&#123456;&#128525;";
//        Pattern pattern = Pattern.compile("\\&\\#\\d+;");
//        Matcher matcher = pattern.matcher(str);
////        if (matcher.start() > 0) {
////            System.out.println("-------");
////        }
//        System.out.println(matcher.groupCount());
//        while (matcher.find()) {
//            String s = matcher.group(0);
////            String s1 = matcher.group(1);
//            System.out.println(s);
//            System.out.println("--------------");
////            System.out.println(s1);
//        }
//    }
}
