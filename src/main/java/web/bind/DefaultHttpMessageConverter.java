package web.bind;

import util.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DefaultHttpMessageConverter implements HttpMessageConverter {
    String defaultContentType = "text/json;charset=UTF-8";
    String defaultCharacterEncoding = "UTF-8";

    ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object object, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(object, response);
        response.flushBuffer();
    }

    private void writeInternal(Object obj, HttpServletResponse response) throws IOException {
        String json = null;
        if (obj.getClass() == String.class) {
            json = (String) obj;
        }
        else if (obj.getClass() == Number.class) {
            json = obj.toString();
        }
        else {
            json = this.objectMapper.writeValuesAsString(obj);
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.write(json);
    }
}
