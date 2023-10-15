package com.pjonas.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.pjonas.todolist.user.IUserRepository;
import com.pjonas.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    IUserRepository repository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (!request.getServletPath().equals("/users/create-user") &&
                !request.getServletPath().equals("/h2-console")
        ) {
            String[] credentials = parseUsernameAndPassword(request, "Authorization");
            String userName = credentials[0];
            String password = credentials[1];

            if (isCrendentialsValid(userName, password)) {
                var user = repository.findByUsername(userName);
                request.setAttribute("idUser", user.getId());
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Credenciais inválidas");
            }
            return;
        }

        filterChain.doFilter(request, response);


    }

    private String[] parseUsernameAndPassword(HttpServletRequest request, String authKey) {
        String authEncoded = request
                .getHeader(authKey)
                .substring("Basic".length()).trim();
        String authDecodedString = new String(Base64.getDecoder().decode(authEncoded));

        return authDecodedString.split(":");
    }

    private boolean isCrendentialsValid(String userName, String password)
            throws IOException {
        // Validacao de userName
        UserModel user = repository.findByUsername(userName);
        if (user == null) return false;
        //Validacao de senha
        BCrypt.Result passwordVerify = BCrypt.verifyer().verify(
                password.toCharArray(), user.getPassword().toCharArray()
        );
        if (!passwordVerify.verified) return false;


        return true;
    }
}