package com.example.fitness.security;

import com.example.fitness.security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("auth token filter called");
        try{
            String jwt=parseJwt(request);
            if(jwt!=null && jwtUtils.validateJwtToken(jwt)) {
                String userId=jwtUtils.getUserIdFromToken(jwt);
                //UserDetails userDetails= userDetailsService.loadUserByUsername(username);
                Claims claims= jwtUtils.getAllClaims(jwt);
                List<String> roles= (List<String>) claims.get("roles", List.class);
                List<GrantedAuthority> authorities= List.of();
                if(roles!=null) {
                    authorities = (List<GrantedAuthority>) roles.stream().map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role)).toList();
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(

                        userId, null, authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }
        catch (Exception e){
            e.printStackTrace();

        }
        filterChain.doFilter(request, response);

    }
    private String parseJwt(HttpServletRequest request){
        String jwt=jwtUtils.getJwtFromHeader(request);
        return jwt;


    }
}
