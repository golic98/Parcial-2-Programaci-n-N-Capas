package com.example.P2PNC.config;


import com.example.P2PNC.model.Book;
import com.example.P2PNC.service.BookService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuth extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BookService bookService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);

        if (jwtToken.split("\\.").length != 3) {
            filterChain.doFilter(request, response);
            return;
        }

        String isbn;
        try {
            isbn = jwtUtils.extractUsername(jwtToken);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isbn != null && request.getAttribute("authenticatedUser") == null) {
            Book user = bookService.findByIsbn(isbn);

            if (jwtUtils.isTokenValid(jwtToken, isbn)) {
                request.setAttribute("authenticatedUser", user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
