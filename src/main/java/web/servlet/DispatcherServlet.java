package web.servlet;

import web.annotation.RequestMapping;
import web.context.WebApplicationContext;
import web.context.support.AnnotationConfigWebApplicationContext;
import web.helper.XmlScanComponentHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String, Method> urlMappingMethod = new HashMap<>();
    private Map<String, Object> urlMappingObjs = new HashMap<>();
    private List<String> packageNames = new ArrayList<>();
    private List<String> controllerNames = new ArrayList<>();
    private Map<String, Object> controllerObjs = new HashMap<>();
    private Map<String, Class<?>> controllerClass = new HashMap<>();

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;


    private String configLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        configLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = super.getServletContext().getResource(configLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.packageNames = XmlScanComponentHelper.getNodeValue(new ClassPathXmlResource(xmlPath));
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(configLocation);
        refresh();
    }

    private void refresh() {
        this.initController();
        this.initHandlerMapping(this.webApplicationContext);
        this.initHandlerAdapter(this.webApplicationContext);
    }

    private void initController() {
        System.out.println("packageNames: " + packageNames);
        this.controllerNames = this.scanPackages(this.packageNames);
        for (String controllerName : this.controllerNames) {
            Object object = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName);
                this.controllerClass.put(controllerName, clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                object = clz.newInstance();
                this.controllerObjs.put(controllerName, object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> mapControllerNames = new ArrayList<>();
        for (String packName : packages) {
            mapControllerNames.addAll(scanPackage(packName));
        }
        return mapControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempNames = new ArrayList<>();
        URI uri = null;
        try {
            uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
            if (uri == null) return tempNames;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File dir = new File(uri);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                tempNames.addAll(scanPackage(packageName + "." + file.getName()));
            } else {
                String controllerName = packageName
                        + "."
                        + file.getName().replace(".class", "");
                tempNames.add(controllerName);
            }
        }
        return tempNames;
    }

    private void initMapping() {

        for (String controllerName : this.controllerNames) {
            Object obj = this.controllerObjs.get(controllerName);
            Class<?> clz = this.controllerClass.get(controllerName);
            Method[] methods = clz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping) {
                        String uri = method.getAnnotation(RequestMapping.class).value();
                        this.urlMappingNames.add(uri);
                        this.urlMappingObjs.put(uri, obj);
                        this.urlMappingMethod.put(uri, method);
                    }
                }
            }
        }
    }

    private void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new RequestMappingHandlerMapping(webApplicationContext);
    }

    private void initHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(webApplicationContext);
    }

    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response){

    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String sPath = request.getServletPath(); //获取请求的path
//        System.out.println("+++++++++++++++++++++++++++++++doGet: " + sPath);
//        if (!this.urlMappingNames.contains(sPath)) {
//            return;
//        }
//
//        Method method = this.urlMappingMethod.get(sPath); //获取bean类定义
//        Object obj = this.urlMappingObjs.get(sPath);  //获取bean实例
//        Object objResult = null;
//        try {
//            objResult = method.invoke(obj); //方法调用
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //将方法返回值写入response
//        response.getWriter().append(objResult.toString());
//    }
}
