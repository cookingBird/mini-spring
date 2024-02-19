package web.servlet;

import web.annotation.RequestMapping;
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


    private String configLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        configLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = super.getServletContext().getResource(configLocation);
            System.out.println("________________________xmlPath: " + xmlPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ClassPathXmlResource res = new ClassPathXmlResource(xmlPath);
        XmlScanComponentHelper helper = new XmlScanComponentHelper();
        packageNames = helper.getNodeValue(res);
        refresh();
    }

    private void refresh() {
        this.initController();
        this.initMapping();
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath(); //获取请求的path
        System.out.println("+++++++++++++++++++++++++++++++doGet: " + sPath);
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }

        Method method = this.urlMappingMethod.get(sPath); //获取bean类定义
        Object obj = this.urlMappingObjs.get(sPath);  //获取bean实例
        Object objResult = null;
        try {
            objResult = method.invoke(obj); //方法调用
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将方法返回值写入response
        response.getWriter().append(objResult.toString());
    }
}
