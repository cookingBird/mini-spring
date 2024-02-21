package web.bind;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpMessageConverter {
    void write(Object object, HttpServletResponse response) throws IOException;
}
