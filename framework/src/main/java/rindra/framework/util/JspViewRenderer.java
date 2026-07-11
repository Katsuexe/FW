package rindra.framework.util;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rindra.framework.helper.ViewRenderer;

public class JspViewRenderer implements ViewRenderer {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
